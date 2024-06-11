package com.google.common.base;

import static org.junit.Assert.*;

import java.util.concurrent.*;
import org.junit.*;

@SuppressWarnings("unchecked")
public class SuppliersTest {

 @Test
 public void testCompose() {
 Supplier<Integer> supplierOfTwo = Suppliers.ofInstance(2);
 Function<Integer, Integer> function = new Function<Integer, Integer>() {
 @Override
 public Integer apply(Integer input) {
 return input + 1;
 }
 };
 Supplier<Integer> expectedSupplier = new Supplier<Integer>() {
 @Override
 public Integer get() {
 return 3;
 }
 };
 Supplier<Integer> actualSupplier = Suppliers.compose(function, supplierOfTwo);
 assertEquals(expectedSupplier, actualSupplier);
 }

 @Test
 public void testMemoize() {
 Supplier<Integer> supplier = () -> 1;
 Supplier<Integer> memoizedSupplier = Suppliers.memoize(supplier);
 assertEquals(supplier, memoizedSupplier);
 Integer result1 = memoizedSupplier.get();
 Integer result2 = memoizedSupplier.get();
 assertEquals(result1, result2);
 }

 @Test
 public void testMemoizeWithExpiration() {
 Supplier<Long> supplier = () -> System.nanoTime();
 Supplier<Long> memoizedSupplier = Suppliers.memoizeWithExpiration(supplier, 1, TimeUnit.SECONDS);
 Long result1 = memoizedSupplier.get();
 Long result2 = memoizedSupplier.get();
 Long result3 = memoizedSupplier.get();
 assertEquals(result1, result2);
 assertEquals(result1, result3);
 sleep(1000L); // sleep for 1 second to let the memoized supplier expire
 Long result4 = memoizedSupplier.get();
 assertNotEquals(result1, result4);
 }

 @Test
 public void testOfInstance() {
 Integer instance = 1;
 Supplier<Integer> supplier = Suppliers.ofInstance(instance);
 assertEquals(instance, supplier.get());
 }

 @Test
 public void testSynchronizedSupplier() {
 final AtomicInteger counter = new AtomicInteger(0);
 Supplier<Integer> supplier = () -> {
 counter.incrementAndGet();
 return 1;
 };
 Supplier<Integer> synchronizedSupplier = Suppliers.synchronizedSupplier(supplier);
 ExecutorService executor = Executors.newFixedThreadPool(10);
 for (int i = 0; i < 10; i++) {
 executor.submit(() -> synchronizedSupplier.get());
 }
 executor.shutdown();
 while (!executor.isTerminated()) {
 try {
 Thread.sleep(100);
 } catch (InterruptedException e) {
 // ignore
 }
 }
 assertEquals(10, counter.get());
 }

 @Test
 public void testSupplierFunction() {
 Function<Supplier<Object>, Object> function = Suppliers.supplierFunction();
 Supplier<Object> supplier = () -> "hello";
 Object result = function.apply(supplier);
 assertEquals("hello", result);
 }
}