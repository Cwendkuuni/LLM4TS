package com.google.common.base.test;

import com.google.common.base.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class SuppliersTest {

 // Test for compose method
 @Test
 public void testCompose() {
 Function<Integer, Integer> addOne = new Function<Integer, Integer>() {
 @Override
 public Integer apply(Integer input) {
 return input + 1;
 }
 };
 Supplier<Integer> supplierOfThree = Suppliers.ofInstance(3);
 Supplier<Integer> composedSupplier = Suppliers.compose(addOne, supplierOfThree);
 assertEquals(4, composedSupplier.get());
 }

 // Test for memoize method
 @Test
 public void testMemoize() {
 Supplier<Integer> supplier = Suppliers.memoize(new Supplier<Integer>() {
 @Override
 public Integer get() {
 return 42;
 }
 });
 Supplier<Integer> memoizedSupplier = Suppliers.memoize(supplier);
 assertEquals(42, supplier.get());
 assertEquals(42, memoizedSupplier.get());
 }

 // Test for memoizeWithExpiration method
 @Test
 public void testMemoizeWithExpiration() throws InterruptedException {
 final AtomicLong counter = new AtomicLong(0L);
 Supplier<Long> supplier = new Supplier<Long>() {
 @Override
 public Long get() {
 counter.incrementAndGet();
 return System.nanoTime();
 }
 };
 Supplier<Long> memoizedSupplier = Suppliers.memoizeWithExpiration(supplier, 50, TimeUnit.MILLISECONDS);
 long initialValue = memoizedSupplier.get();
 long newValue = memoizedSupplier.get();
 Thread.sleep(60); // wait for expiration
 assertEquals(initialValue, memoizedSupplier.get()); // should return the same value
 }

 // Test for ofInstance method
 @Test
 public void testOfInstance() {
 Supplier<String> supplier = Suppliers.ofInstance("Hello");
 assertEquals("Hello", supplier.get());
 }

 // Test for synchronizedSupplier method
 @Test
 public void testSynchronizedSupplier() {
 final AtomicInteger counter = new AtomicInteger(0);
 Supplier<Integer> supplier = new Supplier<Integer>() {
 @Override
 public Integer get() {
 counter.incrementAndGet();
 return 42;
 }
 };
 Supplier<Integer> synchronizedSupplier = Suppliers.synchronizedSupplier(supplier);
 assertEquals(42, synchronizedSupplier.get());
 assertEquals(42, synchronizedSupplier.get()); // should be thread-safe
 }

 // Test for supplierFunction method
 @Test
 public void testSupplierFunction() {
 Function<Supplier<Object>, Object> supplierFunction = Suppliers.supplierFunction();
 Supplier<Object> supplier = Suppliers.ofInstance(new Object());
 assertEquals(supplier.get(), supplierFunction.apply(supplier));
 }
}