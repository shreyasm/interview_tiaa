package com.interview;

import java.security.InvalidParameterException;

import static com.interview.vo.RawMaterialsRepository.rawMaterialsRepository;
import static java.lang.Integer.parseInt;

public class Main {
  public static void main(String[] args) throws InterruptedException {

    if (args.length < 2) {
      throw new InvalidParameterException("Need to pass three parameters - total number of machines, total number of bolts and time taken for each product.");
    }

    var repository = rawMaterialsRepository(parseInt(args[0]), parseInt(args[1]));
    var creator = new MultiWorkerProductCreator(3, repository, parseInt(args[2]));
    var jobSummary = creator.createProductsUsingMultipleWorkers();

    System.out.println("Total products = " + jobSummary.getNumberOfProducts());
    System.out.println("Total time taken = " + jobSummary.getTotalTimeTaken());
  }
}
