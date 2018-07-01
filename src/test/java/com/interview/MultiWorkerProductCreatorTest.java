package com.interview;

import com.interview.vo.ProductJobSummary;
import org.junit.Test;

import static com.interview.vo.RawMaterialsRepository.rawMaterialsRepository;
import static org.junit.Assert.assertEquals;

public class MultiWorkerProductCreatorTest {

  @Test
  public void shouldCalculateTotalNumberOfTimeTakenAndTotalNumberOfProducts() throws InterruptedException {
    var rawMaterialsRepository = rawMaterialsRepository(3, 6);
    ProductJobSummary estimate = new MultiWorkerProductCreator(3, rawMaterialsRepository, 60).createProductsUsingMultipleWorkers();

    assertEquals(60, estimate.getTotalTimeTaken());
    assertEquals(3, estimate.getNumberOfProducts());
  }

  @Test
  public void shouldCalculateCorrectlyWhenBoltsAreOddNumbers() throws InterruptedException {
    var rawMaterialsRepository = rawMaterialsRepository(3, 7);
    ProductJobSummary estimate = new MultiWorkerProductCreator(3, rawMaterialsRepository, 60).createProductsUsingMultipleWorkers();

    assertEquals(60, estimate.getTotalTimeTaken());
    assertEquals(3, estimate.getNumberOfProducts());
  }

  @Test
  public void shouldCalculateCorrectlyWhenMachinesAndBoltsNumberDoNotMatch() throws InterruptedException {
    var rawMaterialsRepository = rawMaterialsRepository(10, 7);
    ProductJobSummary estimate = new MultiWorkerProductCreator(2, rawMaterialsRepository, 60).createProductsUsingMultipleWorkers();

    assertEquals(120, estimate.getTotalTimeTaken());
    assertEquals(3, estimate.getNumberOfProducts());
  }

}