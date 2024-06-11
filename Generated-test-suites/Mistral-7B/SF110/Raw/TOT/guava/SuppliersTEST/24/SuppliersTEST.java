package com.google.common.base.test;

import com.google.common.base.Function;
import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import org.junit.Test;
import static org.junit.Assert.*;

public class SuppliersTest {

 // Test for compose method
 @Test
 public void testCompose() {
 Function<Integer, String> function = new Function<Integer, String>() {
 @Override
 public String apply(Integer input) {
 return input.toString() + " World";
 }
 };
 Supplier<Integer> supplier = Suppliers.ofInstance(42);
 Supplier<String> composedSupplier = Suppliers.compose(function, supplier);
 assertEquals("42 World", composedSupplier.get());
 }

 // Test for memoize method
 @Test
 public void testMemoize() {
 Supplier<Integer> supplier = new Supplier<Integer>() {
 @Override
 public Integer get() {
 return 42;
 }
 };
 Supplier<Integer> memoizedSupplier = Suppliers.memoize(supplier);
 assertEquals(42, memoizedSupplier.get());
 assertEquals(42, memoizedSupplier.get());
 }

 // Test for memoizeWithExpiration method
 @Test
 public void testMemoizeWithExpiration() throws InterruptedException {
 Supplier<Long> supplier = new Supplier<Long>() {
 @Override
 public Long get() {
 return System.nanoTime();
 }
 };
 Supplier<Long> memoizedSupplierWithExpiration = Suppliers.memoizeWithExpiration(supplier, 100, TimeUnit.MILLISECONDS);
 long initialValue = memoizedSupplierWithExpiration.get();
 Thread.sleep(150);
 long newValue = memoizedSupplierWithExpiration.get();
 assertNotEquals(initialValue, newValue);
 }

 // Test for ofInstance method
 @Test
 public void testOfInstance() {
 Supplier<String> supplier = Suppliers.ofInstance("Hello");
 assertEquals("Hello", supplier.get());
 }

 // Test for synchronizedSupplier method
 @Test
 public void testSynchronizedSupplier() throws InterruptedException {
 final AtomicInteger counter = new AtomicInteger(0);
 Supplier<Integer> supplier = new Supplier<Integer>() {
 @Override
 public Integer get() {
 counter.incrementAndGet();
 return 1;
 }
 };
 Supplier<Integer> synchronizedSupplier = Suppliers.synchronizedSupplier(supplier);
 for (int i = 0; i < 1000; i++) {
 supplier.get();
 }
 assertEquals(1000, counter.get());
 for (int i = 0; i < 1000; i++) {
 synchronizedSupplier.get();
 }
 assertEquals(1000, counter.get());
 }

 @Test
 public void testSupplierFunction() {
 Supplier<Object> supplier = () -> null;
 Function<Supplier<Object>, Object> function = Suppliers.supplierFunction();
 Object result = function.apply(supplier);
 assertNull(result);
 }
}