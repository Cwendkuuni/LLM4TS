package com.example.test;

import static org.junit.Assert.*;
import com.google.common.base.Suppliers;
import org.junit.Test;

public class SuppliersTest {

 @Test
 public void testCompose() {
 Supplier<Integer>supplier1 = () -> 1;
 Function<Integer, Integer>function = i -> i * 2;
 Supplier<Integer>supplierComposition = Suppliers.compose(function, supplier1);
 assertEquals(2, supplierComposition.get());
 }

 @Test
 public void testMemoize() {
 Supplier<Integer>supplier = () -> 1 + 2;
 Supplier<Integer>memoizedSupplier = Suppliers.memoize(supplier);
 assertEquals(1 + 2, memoizedSupplier.get());
 assertEquals(1 + 2, memoizedSupplier.get());
 }

 @Test
 public void testMemoizeWithExpiration() {
 Supplier<Long>supplier = () -> System.nanoTime();
 Supplier<Long>expiringMemoizingSupplier = Suppliers.memoizeWithExpiration(supplier, 50, TimeUnit.MILLISECONDS);
 long initialValue = expiringMemoizingSupplier.get();
 long currentTime = System.nanoTime();
 long expirationTime = initialValue + 50;
 assertTrue(currentTime < expirationTime);
 assertEquals(initialValue, expiringMemoizingSupplier.get());
 currentTime = System.nanoTime();
 assertTrue(currentTime > expirationTime);
 assertNotSame(initialValue, expiringMemoizingSupplier.get());
 }

 @Test
 public void testOfInstance() {
 Supplier<String>supplierOfInstance = Suppliers.ofInstance("Hello World");
 assertEquals("Hello World", supplierOfInstance.get());
 }

 @Test
 public void testSynchronizedSupplier() {
 Supplier<Integer>supplier1 = () -> 1;
 Supplier<Integer>synchronizedSupplier = Suppliers.synchronizedSupplier(supplier1);
 Thread thread1 = new Thread(() -> {
 for (int i = 0; i < 1000; i++) {
 assertEquals(1, synchronizedSupplier.get());
 }
 });

 Thread thread2 = new Thread(() -> {
 for (int i = 0; i < 1000; i++) {
 assertEquals(1, synchronizedSupplier.get());
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
 }

 @Test
 public void testSupplierFunction() {
 Function<Supplier<Object>, Object>supplierFunction = Suppliers.supplierFunction();
 Supplier<Object>supplier = () -> "Hello World";
 Object result = supplierFunction.apply(supplier);
 assertEquals("Hello World", result);
 }
}