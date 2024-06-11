package com.google.common.base;

import static org.junit.Assert.*;

import java.util.concurrent.*;

import org.junit.*;

@SuppressWarnings("unchecked")
public class SuppliersTest {

 @BeforeClass
 public static void setup() {
 // Initialize any static variables or setup any mocks here
 }

 @AfterClass
 public static void teardown() {
 // Teardown any mocks or resources here
 }

 @Test
 public void testCompose() {
 Function<Integer, Integer> function = new Function<Integer, Integer>() {
 @Override
 public Integer apply(Integer input) {
 return input * 2;
 }
 };
 Supplier<Integer> supplier = Suppliers.ofInstance(1);
 Supplier<Integer> composedSupplier = Suppliers.compose(function, supplier);
 assertEquals(2, composedSupplier.get());
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
 public void testMemoizeWithExpiration() {
 Supplier<Long> supplier = new Supplier<Long>() {
 @Override
 public Long get() {
 return System.nanoTime();
 }
 };
 Supplier<Long> memoizedSupplierWithExpiration = Suppliers.memoizeWithExpiration(supplier, 100, TimeUnit.MILLISECONDS);
 long initialValue = memoizedSupplierWithExpiration.get();
 long newValue = memoizedSupplierWithExpiration.get();
 assertTrue(initialValue != newValue);
 }

 @Test(expected = NullPointerException.class)
 public void testComposeWithNullFunction() {
 Function<Integer, Integer> function = null;
 Supplier<Integer> supplier = Suppliers.ofInstance(1);
 Suppliers.compose(function, supplier);
 }

 @Test(expected = NullPointerException.class)
 public void testComposeWithNullSupplier() {
 Function<Integer, Integer> function = new Function<Integer, Integer>() {
 @Override
 public Integer apply(Integer input) {
 return input * 2;
 }
 };
 Supplier<Integer> supplier = null;
 Suppliers.compose(function, supplier);
 }

 @Test(expected = NullPointerException.class)
 public void testMemoizeWithNullDelegate() {
 Supplier<Integer> delegate = null;
 Suppliers.memoizingSupplier(delegate);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testMemoizeWithNegativeExpiration() {
 Supplier<Integer> supplier = new Supplier<Integer>() {
 @Override
 public Integer get() {
 return 42;
 }
 };
 Suppliers.memoizeWithExpiration(supplier, -100, TimeUnit.MILLISECONDS);
 }
}