package com.google.common.base;

import static org.junit.Assert.*;

import java.util.concurrent.Supplier;
import java.util.concurrent.TimeUnit;

import org.junit.Test;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.base.Suppliers;

public class SuppliersTest {

 // Test for compose method
 @Test
 public void testCompose() {
 Function<Integer, String> function = new Function<Integer, String>() {
 @Override
 public String apply(Integer input) {
 return input.toString() + "_function";
 }
 };
 Supplier<Integer> supplier = Suppliers.ofInstance(1);
 Supplier<String> expected = Suppliers.compose(function, supplier);
 Supplier<String> actual = expected;
 String result = actual.get();
 String expectedResult = "1_function";
 assertEquals(expectedResult, result);
 }

 // Test for memoize method
 @Test
 public void testMemoize() {
 Supplier<Integer> supplier = new Supplier<Integer>() {
 @Override
 public Integer get() {
 return 1;
 }
 };
 Supplier<Integer> memoizedSupplier = Suppliers.memoize(supplier);
 Supplier<Integer> memoizedSupplier2 = Suppliers.memoize(memoizedSupplier);
 assertEquals(memoizedSupplier, memoizedSupplier2);
 }

 // Test for memoizeWithExpiration method
 @Test
 public void testMemoizeWithExpiration() {
 Supplier<Long> supplier = new Supplier<Long>() {
 @Override
 public Long get() {
 return System.currentTimeMillis();
 }
 };
 Supplier<Long> memoizedSupplierWithExpiration = Suppliers.memoizeWithExpiration(supplier, 100, TimeUnit.MILLISECONDS);
 long result1 = memoizedSupplierWithExpiration.get();
 long result2 = memoizedSupplierWithExpiration.get();
 long result3 = memoizedSupplierWithExpiration.get();
 assertEquals(result1, result2);
 assertEquals(result1, result3);
 try {
 Thread.sleep(150);
 } catch (InterruptedException e) {
 e.printStackTrace();
 }
 long result4 = memoizedSupplierWithExpiration.get();
 assertNotEquals(result1, result4);
 }

 // Test for ofInstance method
 @Test
 public void testOfInstance() {
 Supplier<String> supplier = Suppliers.ofInstance("Hello");
 String result = supplier.get();
 assertEquals("Hello", result);
 }

 // Test for synchronizedSupplier method
 @Test
 public void testSynchronizedSupplier() {
 Supplier<Integer> supplier = new Supplier<Integer>() {
 @Override
 public Integer get() {
 return 1;
 }
 };
 Supplier<Integer> synchronizedSupplier = Suppliers.synchronizedSupplier(supplier);
 Supplier<Integer> supplier1 = new Supplier<Integer>() {
 @Override
 public Integer get() {
 return 2;
 }
 };
 Supplier<Integer> synchronizedSupplier2 = Suppliers.synchronizedSupplier(supplier1);
 Supplier<Integer> supplier3 = Suppliers.synchronizedSupplier(supplier);
 Supplier<Integer> supplier4 = Suppliers.synchronizedSupplier(supplier);
 assertEquals(synchronizedSupplier.get(), supplier3.get());
 assertEquals(synchronizedSupplier2.get(), supplier4.get());
 }
}