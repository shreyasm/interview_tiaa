package com.interview.vo;

import java.util.EnumMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;

import static com.interview.vo.RawMaterialAcquireStatus.NOT_ENOUGH_ITEMS;
import static com.interview.vo.RawMaterialAcquireStatus.SUCCESS;
import static com.interview.vo.RawMaterialType.BOLT;
import static com.interview.vo.RawMaterialType.MACHINE;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;

public class RawMaterialsRepository {

  private Map<RawMaterialType, Integer> repository = new EnumMap<>(RawMaterialType.class);

  private RawMaterialsRepository(int numberOfMachines, int numberOfBolts) {
    repository.put(MACHINE, numberOfMachines);
    repository.put(BOLT, numberOfBolts);
  }

  public static RawMaterialsRepository rawMaterialsRepository(int numberOfMachines, int numberOfBolts) {
    return new RawMaterialsRepository(numberOfMachines, numberOfBolts);
  }

  public synchronized RawMaterialAcquireStatus acquire(Map<RawMaterialType, Integer> itemsToAcquire) {
    if (isAnyItemNotAvailable(itemsToAcquire)) return NOT_ENOUGH_ITEMS;

    Map<RawMaterialType, Integer> newRepositoryStatus =
      itemsToAcquire
        .keySet()
        .stream()
        .collect(toMap(identity(), itemsLeftAfterRemoving(itemsToAcquire)));

    repository.putAll(newRepositoryStatus);

    return SUCCESS;
  }

  private Function<RawMaterialType, Integer> itemsLeftAfterRemoving(Map<RawMaterialType, Integer> itemsToAcquire) {
    return item -> getCount(repository, item) - getCount(itemsToAcquire, item);
  }

  private Integer getCount(Map<RawMaterialType, Integer> itemsToAcquire, RawMaterialType item) {
    return itemsToAcquire.get(item);
  }

  private boolean isAnyItemNotAvailable(Map<RawMaterialType, Integer> itemsToAcquire) {
    return itemsToAcquire.keySet().stream().anyMatch(isItemNotAdequatelyAvailable(itemsToAcquire));
  }

  private Predicate<RawMaterialType> isItemNotAdequatelyAvailable(Map<RawMaterialType, Integer> itemsToAcquire) {
    return item -> !doesRepositoryContainEnoughItem(item, getCount(itemsToAcquire, item));
  }

  private boolean doesRepositoryContainEnoughItem(RawMaterialType item, Integer numberToAcquire) {
    return getCount(repository, item) >= numberToAcquire;
  }
}
