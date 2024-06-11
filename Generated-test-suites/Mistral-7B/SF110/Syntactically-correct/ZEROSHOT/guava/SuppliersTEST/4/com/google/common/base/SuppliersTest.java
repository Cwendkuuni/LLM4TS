package com.google.common.base;

import static org.junit.Assert.*;

import java.util.concurrent.*;

import org.junit.Test;

public class SuppliersTest {

 @SuppressWarnings("unchecked")
 private static <T> Supplier<T> supplierOf(T t) {
 return Suppliers.ofInstance(t);
 }

 @Test
 public void testCompose() {
 Function<Integer, Integer> addOne = i -> i + 1;
 Supplier<Integer> supplier = Suppliers.ofInstance(42);
 Supplier<Integer> composedSupplier = Suppliers.compose(addOne, supplier);

 assertEquals(43, composedSupplier.get());
 }

 @Test
 public void testMemoize() {
 Supplier<String> stringSupplier = () -> "hello";
 Supplier<String> memoizedSupplier = Suppliers.memoize(stringSupplier);

 assertEquals("hello", memoizedSupplier.get());
 assertEquals("hello", memoizedSupplier.get());
 }

 @Test
 public void testMemoizeWithExpiration() {
 Supplier<String> stringSupplier = () -> "hello";
 Supplier<String> memoizedSupplier = Suppliers.memoizeWithExpiration(stringSupplier, 100, TimeUnit.MILLISECONDS);

 assertEquals("hello", memoizedSupplier.get());
 assertEquals("hello", memoizedSupplier.get());

 try {
 Thread.sleep(150);
 } catch (InterruptedException e) {
 e.printStackTrace();
 }

 assertEquals("hello", memoizedSupplier.get());
 assertNotEquals("hello", memoizedSupplier.get());
 }

 @Test
 public void testSynchronizedSupplier() {
 Supplier<Integer> supplier = Suppliers.ofInstance(42);
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
 e.printStackTrace();
 }

 assertEquals(42, synchronizedSupplier.get());
 }

 @Test
 public void testSupplierFunction() {
 Supplier<Integer> supplier = Suppliers.ofInstance(42);
 Function<Supplier<Integer>, Integer> supplierFunction = Suppliers.supplierFunction();

 assertEquals(42, supplierFunction.apply(supplier).intValue());
 }
}