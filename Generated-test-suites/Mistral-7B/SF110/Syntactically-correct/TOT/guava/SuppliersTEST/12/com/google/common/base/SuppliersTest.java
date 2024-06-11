package com.google.common.base.test;

import static org.junit.Assert.*;

import java.util.concurrent.*;

import org.junit.*;

import com.google.common.base.*;

public class SuppliersTest {

 @Test
 public void testCompose() {
 Function<Integer, Integer> addOne = new Function<Integer, Integer>() {
 @Override
 public Integer apply(Integer input) {
 return input + 1;
 }
 };

 Supplier<Integer> supplierOfFive = Suppliers.ofInstance(5);
 Supplier<Integer> supplierOfSix = Suppliers.compose(addOne, supplierOfFive);

 assertEquals(6, supplierOfSix.get());
 }

 @Test
 public void testComposeWithNullFunctionOrSupplier() {
 Function<Integer, Integer> function = null;
 Supplier<Integer> supplier = Suppliers.ofInstance(5);

 try {
 Suppliers.compose(function, supplier);
 fail("Expected NullPointerException");
 } catch (NullPointerException e) {
 // expected
 }

 function = new Function<Integer, Integer>() {};
 supplier = null;

 try {
 Suppliers.compose(function, supplier);
 fail("Expected NullPointerException");
 } catch (NullPointerException e) {
 // expected
 }
 }

 @Test
 public void testMemoize() {
 Supplier<Integer> supplierOfSquare = new Supplier<Integer>() {
 @Override
 public Integer get() {
 return new Integer(Math.pow(2, (int)(Math.random() * 10)));
 }
 };

 Supplier<Integer> memoizedSupplier = Suppliers.memoize(supplierOfSquare);

 assertEquals(Math.pow(2, 0), memoizedSupplier.get()); // first call
 assertEquals(Math.pow(2, 0), memoizedSupplier.get()); // second call
 assertEquals(Math.pow(2, 5), memoizedSupplier.get()); // third call
 }

 @Test
 public void testMemoizeWithExpiration() {
 Supplier<Long> supplierOfCurrentTime = new Supplier<Long>() {
 @Override
 public Long get() {
 return System.nanoTime();
 }
 };

 Supplier<Long> memoizedSupplierWithExpiration = Suppliers.memoizeWithExpiration(supplierOfCurrentTime, 100, TimeUnit.MILLISECONDS);

 long initialTime = memoizedSupplierWithExpiration.get();
 long newTime = System.nanoTime();

 assertEquals(initialTime, memoizedSupplierWithExpiration.get()); // first call
 assertEquals(newTime, memoizedSupplierWithExpiration.get()); // second call (should be new)
 }
}