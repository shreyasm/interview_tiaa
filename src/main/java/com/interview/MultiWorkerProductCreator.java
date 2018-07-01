package com.interview;

import com.interview.vo.ProductJobSummary;
import com.interview.vo.RawMaterialsRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.IntConsumer;

import static com.interview.vo.ProductJobSummary.productJobSummary;
import static com.interview.vo.ProductJobSummary.zeroProducts;
import static java.util.Comparator.comparingInt;
import static java.util.stream.Collectors.toList;
import static java.util.stream.IntStream.range;

public class MultiWorkerProductCreator {

  private final int numberOfWorkers;
  private final RawMaterialsRepository rawMaterialsRepository;
  private final int timeToAssemble;

  public MultiWorkerProductCreator(int numberOfWorkers, RawMaterialsRepository rawMaterialsRepository, int timeToAssemble) {
    this.numberOfWorkers = numberOfWorkers;
    this.rawMaterialsRepository = rawMaterialsRepository;
    this.timeToAssemble = timeToAssemble;
  }

  public ProductJobSummary createProductsUsingMultipleWorkers() throws InterruptedException {

    Map<Thread, ProductCreator> workers = new HashMap<>();

    range(0, numberOfWorkers).forEach(startCreatingProduct(workers));

    waitForCompletion(workers);

    return deriveTotalProductJobSummary(jobSummaryForEach(workers));
  }

  private IntConsumer startCreatingProduct(Map<Thread, ProductCreator> workerThreads) {
    return i -> {
      ProductCreator task = new ProductCreator(rawMaterialsRepository, timeToAssemble);
      Thread workerThread = new Thread(task);
      workerThreads.put(workerThread, task);

      workerThread.start();
    };
  }

  private ProductJobSummary deriveTotalProductJobSummary(List<ProductJobSummary> productJobSummaries) {
    Optional<ProductJobSummary> jobTakingMaximumTime = productJobSummaries.stream().max(comparingInt(ProductJobSummary::getTotalTimeTaken));

    int maximumTimeTakenForAnyWorker = jobTakingMaximumTime.orElse(zeroProducts()).getTotalTimeTaken();
    int totalNumberOfProductsCreated = productJobSummaries.stream().mapToInt(ProductJobSummary::getNumberOfProducts).sum();

    return productJobSummary(totalNumberOfProductsCreated, maximumTimeTakenForAnyWorker);
  }


  private List<ProductJobSummary> jobSummaryForEach(Map<Thread, ProductCreator> workers) {
    return workers.values().stream().map(ProductCreator::getProductJobSummary).collect(toList());
  }

  private void waitForCompletion(Map<Thread, ProductCreator> workers) throws InterruptedException {
    for (Thread workerThread : workers.keySet()) {
      workerThread.join();
    }
  }
}
