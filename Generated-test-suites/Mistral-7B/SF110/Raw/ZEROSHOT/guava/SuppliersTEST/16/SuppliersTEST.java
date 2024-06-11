package com.google.common.base.test;

import static org.junit.Assert.*;

import java.util.concurrent.*;

import org.junit.*;

import com.google.common.base.*;
import com.google.common.base.test.util.*;
import com.google.common.collect.*;

@SuppressWarnings("unchecked")
public class SuppliersTest {

 @BeforeClass
 public static void setUp() {
 Suppliers.checkDeprecationWarnings();
 }

 @Test
 public void testCompose() {
 Function<Integer, Integer> addOne = new Function<Integer, Integer>() {
 @Override
 public Integer apply(Integer input) {
 return input + 1;
 }
 };
 Supplier<Integer> supplierOf = Suppliers.ofInstance(42);
 Supplier<Integer> composedSupplier = Suppliers.compose(addOne, supplierOf);
 assertEquals(43, composedSupplier.get());
 }

 @Test
 public void testMemoize() {
 Supplier<String> stringSupplier = new Supplier<String>() {
 @Override
 public String get() {
 return "Hello World";
 }
 };
 Supplier<String> memoizedSupplier = Suppliers.memoize(stringSupplier);
 assertEquals("Hello World", memoizedSupplier.get());
 assertEquals("Hello World", memoizedSupplier.get());
 }

 @Test
 public void testMemoizeWithExpiration() {
 Supplier<String> stringSupplier = new Supplier<String>() {
 @Override
 public String get() {
 return "Hello World";
 }
 };
 Supplier<String> expiringMemoizingSupplier = Suppliers.memoizeWithExpiration(stringSupplier, 1, TimeUnit.MILLISECONDS);
 assertEquals("Hello World", expiringMemoizingSupplier.get());
 assertEquals("Hello World", expiringMemoizingSupplier.get());
 try {
 Thread.sleep(2);
 } catch (InterruptedException e) {
 Thread.currentThread().interrupt();
 }
 assertEquals("Hello World", expiringMemoizingSupplier.get());
 assertNotEquals("Hello World", expiringMemoizingSupplier.get());
 }

 @Test
 public void testOfInstance() {
 Supplier<String> supplierOfInstance = Suppliers.ofInstance("Test");
 assertEquals("Test", supplierOfInstance.get());
 }

 @Test
 public void testSynchronizedSupplier() {
 final Object lock = new Object();
 Supplier<String> supplier = new Supplier<String>() {
 @Override
 public String get() {
 synchronized (lock) {
 return "Thread Safe";
 }
 }
 };
 Supplier<String> synchronizedSupplier = Suppliers.synchronizedSupplier(supplier);
 ExecutorService executor = Executors.newFixedThreadPool(10);
 List<Future<String>> results = Lists.newArrayList();
 for (int i = 0; i < 10; i++) {
 results.add(executor.submit(new Callable<String>() {
 @Override
 public String call() {
 return synchronizedSupplier.get();
 }
 }));
 }
 for (Future<String> future : results) {
 try {
 assertEquals("Thread Safe", future.get());
 } catch (InterruptedException | ExecutionException e) {
 throw new RuntimeException(e);
 }
 }
 }

 @AfterClass
 public static void tearDown() {
 // No tear down necessary
 }
}