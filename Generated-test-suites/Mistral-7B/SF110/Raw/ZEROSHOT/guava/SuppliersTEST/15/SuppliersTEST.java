package com.google.common.base;

import static org.junit.Assert.*;

import java.util.concurrent.Supplier;

import org.junit.Before;
import org.junit.Test;

public class SuppliersTest {

 @SuppressWarnings("unchecked")
 private <T> Supplier<T> createSupplier() {
 return (Supplier<T>)new SupplierOfInstance<T>(new Object());
 }

 @Before
 public void setUp() {
 // Set up any required state here
 }

 @Test
 public void testCompose() {
 Function<Integer, String> function = new Function<Integer, String>() {
 @Override
 public String apply(Integer input) {
 return input.toString();
 }
 };
 Supplier<Integer> supplier = createSupplier();
 Supplier<String> composedSupplier = Suppliers.compose(function, supplier);
 assertEquals("0", composedSupplier.get().toString());
 }

 @Test
 public void testMemoize() {
 Supplier<String> supplier = createSupplier();
 Supplier<String> memoizedSupplier = Suppliers.memoize(supplier);
 Supplier<String> memoizedSupplier2 = Suppliers.memoize(supplier);
 assertEquals(supplier, memoizedSupplier.getDelegate());
 assertEquals(supplier, memoizedSupplier2.getDelegate());
 assertEquals(supplier.get().toString(), memoizedSupplier.get().toString());
 assertEquals(supplier.get().toString(), memoizedSupplier2.get().toString());
 }

 @Test
 public void testMemoizeWithExpiration() {
 Supplier<String> supplier = createSupplier();
 Supplier<String> memoizedSupplier = Suppliers.memoizeWithExpiration(supplier, 1, TimeUnit.SECONDS);
 Supplier<String> memoizedSupplier2 = Suppliers.memoizeWithExpiration(supplier, 1, TimeUnit.SECONDS);
 assertEquals(supplier, memoizedSupplier.getDelegate());
 assertEquals(supplier, memoizedSupplier2.getDelegate());
 assertEquals(supplier.get().toString(), memoizedSupplier.get().toString());
 assertEquals(supplier.get().toString(), memoizedSupplier2.get().toString());
 try {
 Thread.sleep(1000);
 } catch (InterruptedException e) {
 // Handle exception
 }
 assertEquals(supplier.get().toString(), memoizedSupplier.get().toString());
 assertEquals(supplier.get().toString(), memoizedSupplier2.get().toString());
 try {
 Thread.sleep(2000);
 } catch (InterruptedException e) {
 // Handle exception
 }
 assertNotEquals(supplier.get().toString(), memoizedSupplier.get().toString());
 assertNotEquals(supplier.get().toString(), memoizedSupplier2.get().toString());
 }

 @Test
 public void testSynchronizedSupplier() {
 Supplier<String> supplier = createSupplier();
 Supplier<String> synchronizedSupplier = Suppliers.synchronizedSupplier(supplier);
 Thread thread1 = new Thread(() -> {
 for (int i = 0; i < 10000; i++) {
 synchronizedSupplier.get();
 }
 });
 Thread thread2 = new Thread(() -> {
 for (int i = 0; i < 10000; i++) {
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

 @Test
 public void testSupplierFunction() {
 Function<Supplier<Object>, Object> supplierFunction = Suppliers.supplierFunction();
 Supplier<Object> supplier = createSupplier();
 assertEquals(supplier.get(), supplierFunction.apply(supplier));
 }
}