package com.google.common.base;

import static org.junit.Assert.*;

import java.util.concurrent.Supplier;

import org.junit.Test;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
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
 Supplier<Integer> supplierOfFive = Suppliers.ofInstance(5);
 Supplier<Integer> composedSupplier = Suppliers.compose(addOne, supplierOfFive);
 assertEquals(6, composedSupplier.get());
 }

 // Test for memoize method with simple Supplier
 @Test
 public void testMemoizeSimpleSupplier() {
 Supplier<String> supplier = () -> "Hello";
 Supplier<String> memoizedSupplier = Suppliers.memoize(supplier);
 assertEquals("Hello", memoizedSupplier.get());
 assertEquals("Hello", memoizedSupplier.get());
 }

 // Test for memoize method with expensive Supplier
 @Test
 public void testMemoizeExpensiveSupplier() {
 Supplier<String> expensiveSupplier = new Supplier<String>() {
 @Override
 public String get() {
 Thread.sleep(1000);
 return "Expensive";
 }
 };
 Supplier<String> memoizedSupplier = Suppliers.memoize(expensiveSupplier);
 assertEquals("Expensive", memoizedSupplier.get());
 assertEquals("Expensive", memoizedSupplier.get());
 }

 // Test for memoizeWithExpiration method
 @Test
 public void testMemoizeWithExpiration() {
 Supplier<String> supplier = () -> "Hello";
 Supplier<String> memoizedSupplier = Suppliers.memoizeWithExpiration(supplier, 100, TimeUnit.MILLISECONDS);
 assertEquals("Hello", memoizedSupplier.get());
 assertEquals("Hello", memoizedSupplier.get());
 try {
 Thread.sleep(200);
 memoizedSupplier.get();
 fail("Expected IllegalStateException");
 } catch (IllegalStateException e) {
 // Expected
 }
 Thread.sleep(300);
 assertEquals("Hello", memoizedSupplier.get());
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
 Supplier<String> supplier = () -> "Hello";
 Supplier<String> synchronizedSupplier = Suppliers.synchronizedSupplier(supplier);
 Thread thread1 = new Thread(() -> assertEquals("Hello", synchronizedSupplier.get()));
 Thread thread2 = new Thread(() -> assertEquals("Hello", synchronizedSupplier.get()));
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