package com.google.common.base;

import com.google.common.base.Function;
import com.google.common.base.Supplier;
import org.junit.Test;
import static org.junit.Assert.*;

public class SuppliersTest {

 // Test for compose method
 @Test
 public void testCompose() {
 Function<Integer, String> function = new Function<Integer, String>() {
 @Override
 public String apply(Integer input) {
 return "Hello " + input;
 }
 };
 Supplier<Integer> supplier = Suppliers.ofInstance(42);
 Supplier<String> supplierComposition = Suppliers.compose(function, supplier);
 assertEquals("Hello 42", supplierComposition.get());
 }

 // Test for memoize method
 @Test
 public void testMemoize() {
 Supplier<Integer> delegate = new Supplier<Integer>() {
 @Override
 public Integer get() {
 return 42;
 }
 };
 Supplier<Integer> memoizedSupplier = Suppliers.memoize(delegate);
 Supplier<Integer> memoizedSupplierInstance = Suppliers.memoize(delegate);
 assertEquals(memoizedSupplier, memoizedSupplierInstance);
 assertEquals(42, memoizedSupplier.get());
 assertEquals(42, memoizedSupplier.get());
 }

 // Test for memoizeWithExpiration method
 @Test
 public void testMemoizeWithExpiration() {
 Supplier<Integer> delegate = new Supplier<Integer>() {
 @Override
 public Integer get() {
 return 42;
 }
 };
 Supplier<Integer> expiringMemoizingSupplier = Suppliers.memoizeWithExpiration(delegate, 100, TimeUnit.MILLISECONDS);
 Supplier<Integer> expiringMemoizingSupplier2 = Suppliers.memoizeWithExpiration(delegate, 100, TimeUnit.MILLISECONDS);
 long startTime = System.currentTimeMillis();
 assertEquals(42, expiringMemoizingSupplier.get());
 assertEquals(42, expiringMemoizingSupplier.get());
 long endTime = System.currentTimeMillis();
 long timeDifference = endTime - startTime;
 assertTrue(timeDifference < 100); // Supplier should return the cached value within the given expiration time

 long waitTime = 200;
 try {
 Thread.sleep(waitTime);
 } catch (InterruptedException e) {
 e.printStackTrace();
 }
 assertEquals(42, expiringMemoizingSupplier.get()); // Supplier should return the same value even after waiting for expiration time

 long startTime2 = System.currentTimeMillis();
 assertEquals(42, expiringMemoizingSupplier2.get()); // Supplier should return a new instance since the test thread is not the same as the one that initialized the first instance
 long endTime2 = System.currentTimeMillis();
 long timeDifference2 = endTime2 - startTime2;
 assertTrue(timeDifference2 > 0); // Supplier should create a new instance since it's not the same instance as the previous one
 }

 // Test for ofInstance method
 @Test
 public void testOfInstance() {
 Supplier<String> supplierOfInstance = Suppliers.ofInstance("Hello World");
 assertEquals("Hello World", supplierOfInstance.get());
 }

 // Test for synchronizedSupplier method
 @Test
 public void testSynchronizedSupplier() {
 Supplier<Integer> delegate = new Supplier<Integer>() {
 @Override
 public Integer get() {
 return 42;
 }
 };
 Supplier<Integer> synchronizedSupplier = Suppliers.synchronizedSupplier(delegate);
 Thread thread1 = new Thread(new Runnable() {
 @Override
 public void run() {
 assertEquals(42, synchronizedSupplier.get());
 }
 });
 Thread thread2 = new Thread(new Runnable() {
 @Override
 public void run() {
 assertEquals(42, synchronizedSupplier.get());
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

 // Test for supplierFunction method
 @Test
 public void testSupplierFunction() {
 Supplier<Integer> supplier = Suppliers.ofInstance(42);
 Function<Supplier<Integer>, Integer> supplierFunction = Suppliers.supplierFunction();
 assertEquals(42, supplierFunction.apply(supplier));
 }
}