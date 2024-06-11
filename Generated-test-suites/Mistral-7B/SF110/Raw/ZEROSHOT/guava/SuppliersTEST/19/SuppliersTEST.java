package com.google.common.base;

import static org.junit.Assert.*;

import java.util.concurrent.*;

import org.junit.*;

public class SuppliersTest {

 @Test
 public void testCompose() {
 Supplier<Integer> supplier1 = () -> 1;
 Function<Integer, Integer> function = i -> i * 2;
 Supplier<Integer> composedSupplier = Suppliers.compose(function, supplier1);

 assertEquals(1, supplier1.get().intValue());
 assertEquals(2, composedSupplier.get().intValue());
 }

 @Test
 public void testMemoize() {
 Supplier<Integer> supplier = () -> 1 + 2;
 Supplier<Integer> memoizedSupplier = Suppliers.memoize(supplier);

 assertEquals(3, supplier.get().intValue());
 assertEquals(3, memoizedSupplier.get().intValue());
 assertEquals(3, memoizedSupplier.get().intValue());
 }

 @Test
 public void testMemoizeWithExpiration() {
 Supplier<Long> supplier = () -> System.nanoTime();
 Supplier<Long> memoizedSupplierWithExpiration = Suppliers.memoizeWithExpiration(supplier, 1, TimeUnit.SECONDS);

 long initialTime = System.nanoTime();
 long firstResult = memoizedSupplierWithExpiration.get();
 long secondResult = memoizedSupplierWithExpiration.get();
 long thirdResult = memoizedSupplierWithExpiration.get();

 assertEquals(initialTime, firstResult);
 assertEquals(initialTime, secondResult);
 assertEquals(initialTime + 1000000000, thirdResult); // one second has passed
 }

 @Test
 public void testOfInstance() {
 Object instance = new Object();
 Supplier<Object> supplierOfInstance = Suppliers.ofInstance(instance);

 assertEquals(instance, supplierOfInstance.get());
 }

 @Test
 public void testSynchronizedSupplier() {
 Supplier<Integer> supplier = () -> 1;
 Supplier<Integer> synchronizedSupplier = Suppliers.synchronizedSupplier(supplier);

 ExecutorService executor = Executors.newFixedThreadPool(2);
 Future<Integer> future1 = executor.submit(() -> synchronizedSupplier.get());
 Future<Integer> future2 = executor.submit(() -> synchronizedSupplier.get());

 try {
 future1.get();
 future2.get();
 } catch (InterruptedException | ExecutionException e) {
 throw new RuntimeException(e);
 }

 assertEquals(1, future1.get().intValue());
 assertEquals(1, future2.get().intValue());
 }

 @After
 public void tearDown() {
 Executors.shutdownNow(executor);
 }
}