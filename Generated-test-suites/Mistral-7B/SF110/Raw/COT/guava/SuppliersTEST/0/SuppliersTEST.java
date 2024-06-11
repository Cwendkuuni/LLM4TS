package com.google.common.base.test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.concurrent.*;
import org.junit.*;
import org.mockito.*;

import com.google.common.base.*;

@GwtTestingMode(Mode.MANUAL)
public class SuppliersTest {

  @Test
  public void testCompose() {
    Function<Integer, Integer> func = arg -> arg.intValue() * 2;
    Supplier<Integer> supplier = Suppliers.ofInstance(10);
    Supplier<Integer> composedSupplier = Suppliers.compose(func, supplier);

    assertEquals(20, composedSupplier.get().intValue());
  }

  @Test
  public void testMemoize() {
    Supplier<Integer> supplier = mock(Supplier.class);
    when(supplier.get()).thenReturn(1);
    Supplier<Integer> memoizedSupplier = Suppliers.memoize(supplier);

    assertEquals(1, memoizedSupplier.get());
    assertEquals(1, memoizedSupplier.get());
  }

  @Test
  public void testMemoizeWithExpiration() {
    Supplier<Integer> supplier = mock(Supplier.class);
    when(supplier.get()).thenReturn(1);
    Supplier<Integer> memoizedSupplier = Suppliers.memoizeWithExpiration(supplier, 10, TimeUnit.MILLISECONDS);

    assertEquals(1, memoizedSupplier.get());
    assertEquals(1, memoizedSupplier.get());

    try {
      Thread.sleep(15);
    } catch (InterruptedException e) {
      fail("Thread was interrupted");
    }

    assertEquals(1, memoizedSupplier.get());
    assertNotEquals(1, memoizedSupplier.get()); // should return different value since expired
  }

  @Test
  public void testOfInstance() {
    Supplier<String> supplier = Suppliers.ofInstance("Hello, World!");

    assertEquals("Hello, World!", supplier.get());
  }

  @Test
  public void testSynchronizedSupplier() {
    Supplier<Integer> supplier = mock(Supplier.class);
    when(supplier.get()).thenReturn(1);
    Supplier<Integer> synchronizedSupplier = Suppliers.synchronizedSupplier(supplier);

    Thread thread1 = new Thread(() -> assertEquals(1, synchronizedSupplier.get()));
    Thread thread2 = new Thread(() -> assertEquals(1, synchronizedSupplier.get()));

    thread1.start();
    thread2.start();

    try {
      thread1.join();
      thread2.join();
    } catch (InterruptedException e) {
      fail("Thread was interrupted");
    }
  }

  @Test(expected = NullPointerException.class)
  public void testComposeNullFunction() {
    Function<Integer, Integer> function = null;
    Supplier<Integer> supplier = Suppliers.ofInstance(10);
    Suppliers.compose(function, supplier);
  }

  @Test(expected = NullPointerException.class)
  public void testComposeNullSupplier() {
    Function<Integer, Integer> function = arg -> arg.intValue() * 2;
    Supplier<Integer> supplier = null;
    Suppliers.compose(function, supplier);
  }

  @Test(expected = NullPointerException.class)
  public void testMemoizeNullDelegate() {
    Supplier<Integer> delegate = null;
    Suppliers.memoize(delegate);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testMemoizeWithExpirationNegativeDuration() {
    Supplier<Integer> supplier = mock(Supplier.class);
    Suppliers.memoizeWithExpiration(supplier, -1, TimeUnit.MILLISECONDS);
  }
}