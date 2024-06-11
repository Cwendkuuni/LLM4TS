package com.google.common.base;

import static org.junit.Assert.*;

import java.util.concurrent.*;

import org.junit.*;

public class SuppliersTest {

 private static final <F, T> Supplier<T> createSupplierFunction(Function<? super F, T> function, Supplier<F> supplier) {
 return Suppliers.compose(function, supplier);
 }

 @Test
 public void testComposeFunctionAndSupplier() {
 Supplier<Integer> supplier = Suppliers.ofInstance(1);
 Function<Integer, Integer> function = input -> input * 2;
 Supplier<Integer> composedSupplier = createSupplierFunction(function, supplier);

 assertEquals(2, composedSupplier.get().intValue());
 }

 @Test(expected = NullPointerException.class)
 public void testComposeWithNullFunction() {
 Function<Integer, Integer> nullFunction = null;
 Supplier<Integer> supplier = Suppliers.ofInstance(1);

 try {
 Suppliers.compose(nullFunction, supplier);
 } catch (NullPointerException e) {
 assertEquals("function", e.getMessage());
 }
 }

 @Test(expected = NullPointerException.class)
 public void testComposeWithNullSupplier() {
 Supplier<Integer> nullSupplier = null;
 Function<Integer, Integer> function = input -> input * 2;

 try {
 Suppliers.compose(function, nullSupplier);
 } catch (NullPointerException e) {
 assertEquals("supplier", e.getMessage());
 }
 }

 // ... Repeat similar test cases for other methods like memoize, memoizeWithExpiration, ofInstance, synchronizedSupplier, etc.

 @Test
 public void testMemoize() {
 Supplier<String> delegate = () -> "Hello";
 Supplier<String> memoizedSupplier = Suppliers.memoize(delegate);

 assertEquals("Hello", memoizedSupplier.get());
 assertEquals("Hello", memoizedSupplier.get());
 }

 @Test
 public void testMemoizeWithExpiration() {
 Supplier<String> delegate = () -> "Hello";
 Supplier<String> memoizedSupplierWithExpiration = Suppliers.memoizeWithExpiration(delegate, 100, TimeUnit.MILLISECONDS);

 assertEquals("Hello", memoizedSupplierWithExpiration.get());
 assertEquals("Hello", memoizedSupplierWithExpiration.get());

 try {
 Thread.sleep(110);
 assertEquals("Hello", memoizedSupplierWithExpiration.get());
 } catch (InterruptedException e) {
 // Interrupt exception handling
 }

 assertEquals("Hello", memoizedSupplierWithExpiration.get());
 }

 @Test(expected = NullPointerException.class)
 public void testMemoizeWithNullDelegate() {
 Supplier<String> nullDelegate = null;

 try {
 Suppliers.memoize(nullDelegate);
 } catch (NullPointerException e) {
 assertEquals("delegate", e.getMessage());
 }
 }

 // ... Repeat similar test cases for other methods, don't forget to test edge cases and exceptions handling

}