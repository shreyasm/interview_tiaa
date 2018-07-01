package com.interview.vo;

import org.junit.Test;

import static com.interview.vo.RawMaterialAcquireStatus.NOT_ENOUGH_ITEMS;
import static com.interview.vo.RawMaterialAcquireStatus.SUCCESS;
import static com.interview.vo.RawMaterialType.BOLT;
import static com.interview.vo.RawMaterialType.MACHINE;
import static com.interview.vo.RawMaterialsRepository.rawMaterialsRepository;
import static java.util.Map.of;
import static org.junit.Assert.assertEquals;

public class RawMaterialsRepositoryTest {

  @Test
  public void shouldReturnSuccessWhenRepositoryHasItems() {
    var repository = rawMaterialsRepository(2, 4);
    assertEquals(SUCCESS, repository.acquire(of(MACHINE, 1, BOLT, 2)));
  }

  @Test
  public void shouldReturnFailureWhenRepositoryDoesNotHaveEnoughBolts() {
    var repository = rawMaterialsRepository(2, 1);
    assertEquals(NOT_ENOUGH_ITEMS, repository.acquire(of(MACHINE, 1, BOLT, 2)));
  }

  @Test
  public void shouldReturnFailureWhenRepositoryDoesNotHaveEnoughMachines() {
    var repository = rawMaterialsRepository(1, 5);
    assertEquals(NOT_ENOUGH_ITEMS, repository.acquire(of(MACHINE, 2, BOLT, 2)));
  }

  @Test
  public void shouldAcquireItemCorrectly() {
    var repository = rawMaterialsRepository(2, 4);
    assertEquals(SUCCESS, repository.acquire(of(MACHINE, 1, BOLT, 2)));
    assertEquals(SUCCESS, repository.acquire(of(MACHINE, 1, BOLT, 2)));
    assertEquals(NOT_ENOUGH_ITEMS, repository.acquire(of(MACHINE, 1, BOLT, 2)));
  }

}