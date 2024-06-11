package com.google.common.base.test;

import static org.junit.Assert.*;

import com.google.common.base.*;
import org.junit.*;

public class SuppliersTest {

 private static final long NANOS_PER_MILLIS = 1000000000L;

 @Test
 public void testCompose() {
 Supplier<String> supplier1 = () -> "supplier1";
 Supplier<String> supplier2 = s -> s + "_supplier2";

 Supplier<String> supplier3 = Suppliers.compose(supplier2, supplier1);
 assertEquals("supplier1_supplier2", supplier3.get());
 }

 @Test(expected = NullPointerException.class)
 public void testComposeNullFunction() {
 Function<Supplier<Object>, Object> function = null;
 Supplier<Object> supplier = () -> {};

 try {
 Suppliers.compose(function, supplier);
 fail("Expected NullPointerException");
 } catch (NullPointerException e) {
 // Expected
 }
 }

 @Test(expected = NullPointerException.class)
 public void testComposeNullSupplier() {
 Supplier<Object> supplier = null;
 Function<Supplier<Object>, Object> function = () -> {};

 try {
 Suppliers.compose(function, supplier);
 fail("Expected NullPointerException");
 } catch (NullPointerException e) {
 // Expected
 }
 }

 @Test
 public void testMemoize() {
 Supplier<Integer> supplier1 = () -> 1;
 Supplier<Integer> memoizedSupplier = Suppliers.memoize(supplier1);

 assertEquals(1, memoizedSupplier.get());
 assertEquals(1, memoizedSupplier.get());
 }

 @Test
 public void testMemoizeWithExpiration() {
 Supplier<Long> supplier = () -> System.nanoTime();
 Supplier<Long> memoizedSupplier = Suppliers.memoizeWithExpiration(supplier, 1, TimeUnit.MILLISECONDS);

 // Get first value
 long value1 = memoizedSupplier.get();
 long expiration1 = Platform.systemNanoTime() - value1;
 assertTrue(expiration1 < NANOS_PER_MILLISECONDS);

 // Get second value before expiration
 long value2 = memoizedSupplier.get();
 long expiration2 = Platform.systemNanoTime() - value2;
 assertTrue(expiration2 > value1);
 assertTrue(expiration2 < NANOS_PER_MILLISECS + 1);

 // Get second value after expiration
 long value3 = memoizedSupplier.get();
 long expiration3 = Platform.systemNanoTime() - value3;
 assertTrue(expiration3 > NANOS_PER_MILLISECS + 1);
 }

 @Test(expected = NullPointerException.class)
 public void testMemoizeNullSupplier() {
 Supplier<Object> supplier = null;
 Suppliers.memoize(supplier);
 fail("Expected NullPointerException");
 }

 @Test
 public void testOfInstance() {
 Supplier<String> supplier = Suppliers.ofInstance("Instance");
 assertEquals("Instance", supplier.get());
 }

 @Test(expected = NullPointerException.class)
 public void testOfInstanceNullInstance() {
 Supplier<Object> supplier = Suppliers.ofInstance(null);
 fail("Expected NullPointerException");
 }

 @Test
 public void testSynchronizedSupplier() {
 Supplier<Long> supplier = () -> System.nanoTime();
 Supplier<Long> synchronizedSupplier = Suppliers.synchronizedSupplier(supplier);

 // Get first value
 long value1 = synchronizedSupplier.get();

 // Get second value concurrently
 long value2 = 0L;
 long startTime = System.nanoTime();
 new Thread(() -> value2 = synchronizedSupplier.get()).start();
 long endTime = System.nanoTime();

 // Ensure thread finished before checking result
 long waitTime = endTime - startTime;
 assertTrue("Thread did not finish in a reasonable time", waitTime < 1000);

 assertEquals(value1, value2);
 }
}