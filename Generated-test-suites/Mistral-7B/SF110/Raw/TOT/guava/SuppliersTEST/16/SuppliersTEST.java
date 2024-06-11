package com.google.common.base;

import static org.junit.Assert.*;

import java.util.concurrent.*;
import javax.annotation.*;
import java.io.*;
import org.junit.*;

public class SuppliersTest {

 @BeforeClass
 public static void setup() {
 // Set up any static variables or initialization here
 }

 @AfterClass
 public static void teardown() {
 // Clean up any static resources or finalization here
 }

 @Test
 public void testComposeFunctionAndSupplier() {
 Function<Integer, Integer> function = new Function<Integer, Integer>() {
 @Override
 public Integer apply(Integer input) {
 return input * 2;
 }
 };
 Supplier<Integer> supplier = Suppliers.ofInstance(5);
 Supplier<Integer> composedSupplier = Suppliers.compose(function, supplier);
 assertEquals(10, composedSupplier.get());
 }

 @Test
 public void testComposeNullFunctionAndSupplier() {
 try {
 Suppliers.compose(null, Suppliers.ofInstance(5));
 fail("Expected NullPointerException");
 } catch (NullPointerException e) {
 // Expected
 }
 }

 @Test
 public void testComposeNullSupplier() {
 try {
 Suppliers.compose(new Function<Integer, Integer>() {}, Suppliers.ofInstance(null));
 fail("Expected NullPointerException");
 } catch (NullPointerException e) {
 // Expected
 }
 }

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

 @Test
 public void testMemoizeNullSupplier() {
 try {
 Suppliers.memoize(null);
 fail("Expected NullPointerException");
 } catch (NullPointerException e) {
 // Expected
 }
 }

 @Test
 public void testMemoizeWithExpirationShortDuration() {
 Supplier<Long> supplier = new MemoizingSupplier<Long>(new Supplier<Long>() {
 @Override
 public Long get() {
 return System.nanoTime();
 }
 });
 Supplier<Long> expiringMemoizingSupplier = Suppliers.memoizeWithExpiration(supplier, 1, TimeUnit.NANOSECONDS);
 long value1 = expiringMemoizingSupplier.get();
 long value2 = expiringMemoizingSupplier.get();
 assertEquals(value1, value2);
 // Sleep for a short time to allow the expiration to occur
 try {
 Thread.sleep(1);
 } catch (InterruptedException e) {
 throw new RuntimeException(e);
 }
 long value3 = expiringMemoizingSupplier.get();
 assertNotEquals(value1, value3);
 }

 @Test
 public void testMemoizeWithExpirationLongDuration() {
 Supplier<Long> supplier = new MemoizingSupplier<Long>(new Supplier<Long>() {
 @Override
 public Long get() {
 return System.nanoTime();
 }
 });
 Supplier<Long> expiringMemoizingSupplier = Suppliers.memoizeWithExpiration(supplier, 1000, TimeUnit.MILLISECONDS);
 long value1 = expiringMemoizingSupplier.get();
 long value2 = expiringMemoizingSupplier.get();
 assertEquals(value1, value2);
 // Sleep for a long time to allow the expiration to occur
 try {
 Thread.sleep(1001);
 } catch (InterruptedException e) {
 throw new RuntimeException(e);
 }
 long value3 = expiringMemoizingSupplier.get();
 assertNotEquals(value1, value3);
 }

 @Test
 public void testMemoizeWithExpirationZeroDuration() {
 Supplier<Long> supplier = new MemoizingSupplier<Long>(new Supplier<Long>() {
 @Override
 public Long get() {
 return System.nanoTime();
 }
 });
 Supplier<Long> expiringMemoizingSupplier = Suppliers.memoizeWithExpiration(supplier, 0, TimeUnit.MILLISECONDS);
 long value1 = expiringMemoizingSupplier.get();
 long value2 = expiringMemoizingSupplier.get();
 assertEquals(value1, value2);
 }

 @Test
 public void testMemoizeWithExpirationNullSupplier() {
 try {
 Suppliers.memoizeWithExpiration(null, 0, TimeUnit.NANOSECONDS);
 fail("Expected NullPointerException");
 } catch (NullPointerException e) {
 // Expected
 }
 }

 @Test
 public void testOfInstanceNullInstance() {
 try {
 Suppliers.ofInstance(null);
 fail("Expected NullPointerException");
 } catch (NullPointerException e) {
 // Expected
 }
 }

 @Test
 public void testSynchronizedSupplier() {
 final int expectedValue = 42;
 Supplier<Integer> delegate = new Supplier<Integer>() {
 @Override
 public Integer get() {
 return expectedValue;
 }
 };
 Supplier<Integer> synchronizedSupplier = Suppliers.synchronizedSupplier(delegate);
 ExecutorService executor = Executors.newFixedThreadPool(10);
 for (int i = 0; i < 10; i++) {
 executor.submit(new Runnable() {
 @Override
 public void run() {
 int value = synchronizedSupplier.get();
 assertEquals(expectedValue, value);
 }
 });
 }
 executor.shutdown();
 }

 @After
 public void afterTest() {
 // Clean up any test resources here
 }
}