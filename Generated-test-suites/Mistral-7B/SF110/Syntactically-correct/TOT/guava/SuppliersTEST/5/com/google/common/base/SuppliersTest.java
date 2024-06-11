package com.google.common.base;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.concurrent.TimeUnit;

import org.junit.Test;

import com.google.common.base.Function;
import com.google.common.base.Supplier;
import com.google.common.base.ThreadSafeSupplier;
import com.google.common.base.Suppliers;

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
 Supplier<Integer> supplier = () -> 1;
 Supplier<Integer> memoizedSupplier = Suppliers.memoize(supplier);
 assertEquals(1, memoizedSupplier.get());
 assertEquals(1, memoizedSupplier.get());
 }

 // Test for memoizeWithExpiration method
 @Test
 public void testMemoizeWithExpiration() {
 Supplier<Long> supplier = () -> System.nanoTime();
 Supplier<Long> expiringMemoizingSupplier = Suppliers.memoizeWithExpiration(supplier, 10, TimeUnit.MILLISECONDS);
 long initialValue = expiringMemoizingSupplier.get();
 long newValue = expiringMemoizingSupplier.get();
 checkArgument(initialValue != newValue, "Initial and new values should not be equal");
 }

 // Test for ofInstance method
 @Test
 public void testOfInstance() {
 Supplier<String> supplierOfHello = Suppliers.ofInstance("Hello");
 assertNotNull(supplierOfHello.get());
 assertEquals("Hello", supplierOfHello.get());
 }

 // Test for synchronizedSupplier method
 @Test
 public void testSynchronizedSupplier() {
 Supplier<Integer> supplier = () -> 1;
 Supplier<Integer> synchronizedSupplier = Suppliers.synchronizedSupplier(supplier);
 Thread thread1 = new Thread(() -> {
 for (int i = 0; i < 1000; i++) {
 synchronizedSupplier.get();
 }
 });
 Thread thread2 = new Thread(() -> {
 for (int i = 0; i < 1000; i++) {
 synchronizedSupplier.get();
 }
 });
 thread1.start();
 thread2.start();
 try {
 thread1.join();
 thread2.join();
 } catch (InterruptedException e) {
 // Handle exception
 }
 }

}