package com.google.common.base;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.concurrent.Supplier;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class SuppliersTest {

 private Function<Function<Integer, Integer>, Function<Supplier<Integer>, Integer>> function;
 private Supplier<Integer> supplier;
 private Supplier<Integer> composedSupplier;

 @Before
 public void setup() {
 function = f -> i -> i + 2;
 supplier = Mockito.spy(Suppliers.ofInstance(1));
 }

 @Test
 public void testCompose() {
 // typical use case
 composedSupplier = Suppliers.compose(function, supplier);
 assertEquals(4, composedSupplier.get());

 // edge case: null input
 assertNull(Suppliers.compose(null, supplier));

 // error scenario: null supplier
 assertNull(Suppliers.compose(function, null));
 }

 @Test
 public void testMemoize() {
 // typical use case
 Supplier<Integer> delegate = Mockito.spy(Suppliers.ofInstance(1));
 Supplier<Integer> memoizedSupplier = Suppliers.memoize(delegate);
 assertEquals(1, memoizedSupplier.get());
 assertEquals(1, memoizedSupplier.get());

 // edge case: memoized supplier is already memoizing supplier
 Supplier<Integer> memoizingSupplier = new MemoizingSupplier<>(delegate);
 assertEquals(1, memoizedSupplier.get());
 assertEquals(1, memoizedSupplier.get());

 // error scenario: null delegate
 assertNull(Suppliers.memoize(null));
 }

 @Test
 public void testMemoizeWithExpiration() {
 // typical use case
 Supplier<Long> delegate = Suppliers.ofInstance(1L);
 Supplier<Long> expiringMemoizingSupplier = Suppliers.memoizeWithExpiration(delegate, 100, TimeUnit.MILLISECONDS);
 assertEquals(1L, expiringMemoizingSupplier.get());
 assertEquals(1L, expiringMemoizingSupplier.get());

 long startTime = System.currentTimeMillis();
 assertEquals(1L, expiringMemoizingSupplier.get());
 assertEquals(1L, expiringMemoizingSupplier.get());

 long elapsedTime = System.currentTimeMillis() - startTime;
 assertTrue(elapsedTime > 100);

 assertEquals(1L, expiringMemoizingSupplier.get()); // should return the same value
 }

 @Test
 public void testOfInstance() {
 // typical use case
 Supplier<String> supplier = Suppliers.ofInstance("Hello");
 assertEquals("Hello", supplier.get());

 // edge case: null instance
 assertNull(Suppliers.ofInstance(null));
 }

 @Test
 public void testSynchronizedSupplier() {
 // typical use case
 Supplier<String> supplier = Suppliers.ofInstance("Synchronized");
 Supplier<String> synchronizedSupplier = Suppliers.synchronizedSupplier(supplier);

 Thread thread1 = new Thread(() -> assertEquals("Synchronized", synchronizedSupplier.get()));
 Thread thread2 = new Thread(() -> assertEquals("Synchronized", synchronizedSupplier.get()));

 thread1.start();
 thread2.start();

 try {
 thread1.join();
 thread2.join();
 } catch (InterruptedException e) {
 e.printStackTrace();
 }
 }
}