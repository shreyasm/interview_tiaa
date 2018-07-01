package com.interview.vo;

public final class ProductJobSummary {

  private final int numberOfProducts;

  private final int totalTimeTaken;

  private ProductJobSummary(int numberOfProducts, int totalTimeTaken) {
    this.numberOfProducts = numberOfProducts;
    this.totalTimeTaken = totalTimeTaken;
  }

  public static ProductJobSummary productJobSummary(int numberOfProducts, int totalTimeTaken) {
    return new ProductJobSummary(numberOfProducts, totalTimeTaken);
  }

  public static ProductJobSummary zeroProducts() {
    return new ProductJobSummary(0 ,0 );
  }

  public ProductJobSummary addAProductToSummary(int timeTakenForNewProduct) {
    return new ProductJobSummary(this.numberOfProducts + 1, this.totalTimeTaken + timeTakenForNewProduct);
  }

  public int getNumberOfProducts() {
    return numberOfProducts;
  }

  public int getTotalTimeTaken() {
    return totalTimeTaken;
  }
}
