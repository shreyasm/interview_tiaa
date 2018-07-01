package com.interview;

import com.interview.vo.ProductJobSummary;
import com.interview.vo.RawMaterialsRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static com.interview.vo.RawMaterialAcquireStatus.NOT_ENOUGH_ITEMS;
import static com.interview.vo.RawMaterialAcquireStatus.SUCCESS;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ProductCreatorTest {

  RawMaterialsRepository rawMaterialsRepository;

  @Before
  public void setUp() {
    rawMaterialsRepository = mock(RawMaterialsRepository.class);
  }

  @Test
  public void shouldCreateProductsTillRepositoryExhausted() {
    ProductCreator creator = new ProductCreator(rawMaterialsRepository, 60);
    when(rawMaterialsRepository.acquire(any())).thenReturn(SUCCESS).thenReturn(SUCCESS).thenReturn(NOT_ENOUGH_ITEMS);

    ProductJobSummary productsCreated = creator.createProductsTillTheRepositoryIsExhausted();

    assertEquals(2, productsCreated.getNumberOfProducts());
    assertEquals(120, productsCreated.getTotalTimeTaken());
  }

  @Test
  public void shouldTakeTotalTimeInAccordanceToNeededForOneProduct() {
    ProductCreator creator = new ProductCreator(rawMaterialsRepository, 50);
    when(rawMaterialsRepository.acquire(any())).thenReturn(SUCCESS).thenReturn(SUCCESS).thenReturn(NOT_ENOUGH_ITEMS);

    ProductJobSummary productsCreated = creator.createProductsTillTheRepositoryIsExhausted();

    assertEquals(2, productsCreated.getNumberOfProducts());
    assertEquals(100, productsCreated.getTotalTimeTaken());
  }
}