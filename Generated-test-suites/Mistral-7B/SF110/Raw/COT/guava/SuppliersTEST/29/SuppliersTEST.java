package com.google.common.base.test;

import com.google.common.base.Suppliers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import static org.junit.Assert.*;
import java.util.concurrent.TimeUnit;

@RunWith(JUnit4.class)
public class SuppliersTest {

 // Test for compose method
 @Test
 public void testCompose() {
 Supplier<String> supplier1 = () -> "supplier1";
 Supplier<String> supplier2 = (Supplier<String> input) -> input.get() + " supplier2";
 Supplier<String> expected = () -> "supplier1 supplier2";

 Supplier<String> actual = Suppliers.compose(supplier2, supplier1);
 assertEquals(expected, actual);
 }

 // Test for memoize method with default implementation
 @Test
 public void testMemoize() {
 Supplier<Integer> supplier = () -> 42;
 Supplier<Integer> memoizedSupplier = Suppliers.memoize(supplier);

 assertEquals(42, memoizedSupplier.get());
 assertEquals(42, memoizedSupplier.get());
 }

 // Test for memoizeWithExpiration method
 @Test
 public void testMemoizeWithExpiration() {
 Supplier<Long> supplier = () -> System.nanoTime();
 Supplier<Long> expiringSupplier = Suppliers.memoizeWithExpiration(supplier, 10, TimeUnit.MILLISECONDS);

 long initialValue = expiringSupplier.get();
 try {
 Thread.sleep(15);
 long valueAfterExpiration = expiringSupplier.get();
 fail("Expected IllegalStateException but got " + valueAfterExpiration);
 } catch (IllegalStateException e) {
 // Expected
 }

 long newValue = expiringSupplier.get();
 assertNotSame(initialValue, newValue);
 }

 // Test for ofInstance method
 @Test
 public void testOfInstance() {
 Supplier<String> supplier = Suppliers.ofInstance("Hello");
 String value = supplier.get();
 assertEquals("Hello", value);
 }

 // Test for synchronizedSupplier method
 @Test
 public void testSynchronizedSupplier() {
 Supplier<Integer> supplier = () -> new Random().nextInt();
 Supplier<Integer> synchronizedSupplier = Suppliers.synchronizedSupplier(supplier);

 int firstValue = synchronizedSupplier.get();
 int secondValue = synchronizedSupplier.get();

 assertEquals(firstValue, secondValue);
 }

 // Test for supplierFunction method
 @Test
 public void testSupplierFunction() {
 Supplier<String> supplier = () -> "Hello";
 Function<Supplier<String>, String> function = Suppliers.supplierFunction();

 assertEquals(supplier.get(), function.apply(supplier));
 }

 // Test for compose method with null arguments
 @Test(expected = NullPointerException.class)
 public void testComposeWithNullFunction() {
 Function<Object, String> nullFunction = null;
 Supplier<Object> supplier = () -> new Object();
 Suppliers.compose(nullFunction, supplier);
 }

 @Test(expected = NullPointerException.class)
 public void testComposeWithNullSupplier() {
 Function<Object, String> function = () -> "Hello";
 Supplier<Object> nullSupplier = null;
 Suppliers.compose(function, nullSupplier);
 }

 // Test for memoize method with null delegate
 @Test(expected = NullPointerException.class)
 public void testMemoizeWithNullDelegate() {
 Supplier<Integer> nullDelegate = null;
 Suppliers.memoize(nullDelegate);
 }

 // Test for memoizeWithExpiration method with negative duration
 @Test(expected = IllegalArgumentException.class)
 public void testMemoizeWithExpirationWithNegativeDuration() {
 Supplier<Long> supplier = () -> System.nanoTime();
 Suppliers.memoizeWithExpiration(supplier, -1, TimeUnit.MILLISECONDS);
 }

 // Test for ofInstance method with null instance
 @Test(expected = NullPointerException.class)
 public void testOfInstanceWithNullInstance() {
 Suppliers.ofInstance(null);
 }

 // Test for synchronizedSupplier method with null delegate
 @Test(expected = NullPointerException.class)
 public void testSynchronizedSupplierWithNullDelegate() {
 Supplier<Object> nullDelegate = null;
 Suppliers.synchronizedSupplier(nullDelegate);
 }
}