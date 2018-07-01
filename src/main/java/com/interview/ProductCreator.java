package com.interview;

import com.interview.vo.ProductJobSummary;
import com.interview.vo.RawMaterialAcquireStatus;
import com.interview.vo.RawMaterialType;
import com.interview.vo.RawMaterialsRepository;

import java.util.Map;

import static com.interview.vo.ProductJobSummary.zeroProducts;
import static com.interview.vo.RawMaterialAcquireStatus.SUCCESS;
import static com.interview.vo.RawMaterialType.BOLT;
import static com.interview.vo.RawMaterialType.MACHINE;
import static java.lang.Thread.sleep;
import static java.util.Map.of;

public class ProductCreator implements Runnable {
  private static final Map<RawMaterialType, Integer> RAW_MATERIAL_NEEDED_FOR_A_PRODUCT = of(MACHINE, 1, BOLT, 2);
  private final RawMaterialsRepository repository;
  private final int timeToAssemble;
  private ProductJobSummary productJobSummary;

  public ProductCreator(RawMaterialsRepository repository, int timeToAssemble) {
    this.repository = repository;
    this.timeToAssemble = timeToAssemble;
    productJobSummary = zeroProducts();
  }

  public void run() {
    productJobSummary = createProductsTillTheRepositoryIsExhausted();
  }

  ProductJobSummary createProductsTillTheRepositoryIsExhausted() {
    ProductJobSummary jobSummary = zeroProducts();

    while (tryToAcquireRawMaterial() == SUCCESS) {
      jobSummary = jobSummary.addAProductToSummary(createProduct(timeToAssemble));
    }

    return jobSummary;
  }

  private RawMaterialAcquireStatus tryToAcquireRawMaterial() {
    return repository.acquire(RAW_MATERIAL_NEEDED_FOR_A_PRODUCT);
  }

  private int createProduct(int timeToAssemble) {
    try {
      sleep(timeToAssemble * (long) 10);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    }
    return timeToAssemble;
  }

  public ProductJobSummary getProductJobSummary() {
    return productJobSummary;
  }
}
