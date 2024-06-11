package com.google.common.base;

import static org.junit.Assert.*;

import java.util.concurrent.*;

import org.junit.*;

@SuppressWarnings("unchecked")
public class SuppliersTest {

 @Test
 public void testCompose() {
 Supplier<String> supplier1 = () -> "hello";
 Supplier<String> supplier2 = s -> s.toUpperCase();
 Supplier<String> composedSupplier = Suppliers.compose(supplier2, supplier1);
 assertEquals("HELLO", composedSupplier.get());
 }

 @Test
 public void testMemoize() {
 Supplier<Integer> supplier = () -> 42;
 Supplier<Integer> memoizedSupplier = Suppliers.memoize(supplier);
 assertEquals(42, memoizedSupplier.get());
 assertEquals(42, memoizedSupplier.get());
 }

 @Test
 public void testMemoizeWithExpiration() {
 Supplier<Long> supplier = () -> System.nanoTime();
 Supplier<Long> memoizedSupplier = Suppliers.memoizeWithExpiration(supplier, 1, TimeUnit.SECONDS);
 long initialValue = memoizedSupplier.get();
 long newValue = System.nanoTime();
 assertTrue(initialValue < newValue);
 assertEquals(initialValue, memoizedSupplier.get()); // should return cached value
 }

 @Test(expected = NullPointerException.class)
 public void testComposeWithNullFunction() {
 Supplier<String> supplier = () -> "world";
 Function<Supplier<String>, String> function = null;
 try {
 Suppliers.compose(function, supplier);
 fail();
 } catch (NullPointerException e) {
 // expected
 }
 }

 @Test(expected = NullPointerException.class)
 public void testComposeWithNullSupplier() {
 Function<Supplier<String>, String> function = s -> s.get().toUpperCase();
 Supplier<String> supplier = null;
 try {
 Suppliers.compose(function, supplier);
 fail();
 } catch (NullPointerException e) {
 // expected
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testMemoizeWithNegativeDuration() {
 Supplier<Integer> supplier = () -> 42;
 try {
 Suppliers.memoizeWithExpiration(supplier, -1, TimeUnit.MILLISECONDS);
 fail();
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testSynchronizedSupplier() {
 final IntSupplier originalSupplier = () -> 42;
 Supplier<Integer> synchronizedSupplier = Suppliers.synchronizedSupplier(originalSupplier);
 AtomicInteger counter = new AtomicInteger();
 for (int i = 0; i < 1000; i++) {
 IntSupplier currentSupplier = () -> synchronizedSupplier.get();
 int result = currentSupplier.get();
 counter.getAndIncrement();
 assertEquals(42, result);
 }
 assertEquals(1000, counter.get());
 }

 @Test
 public void testOfInstance() {
 Supplier<String> supplier = Suppliers.ofInstance("test");
 assertEquals("test", supplier.get());
 }

 @Test
 public void testOfInstanceWithNullArgument() {
 Supplier<String> supplier = Suppliers.ofInstance(null);
 assertNull(supplier.get());
 }
}