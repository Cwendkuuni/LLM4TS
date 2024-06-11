package com.google.common.base.test;

import com.google.common.base.Function;
import com.google.common.base.Supplier;
import com.google.common.base.Preconditions;
import com.google.common.base.Suppliers;
import org.junit.Test;
import static org.junit.Assert.*;

public class SuppliersTest {

 @Test
 public void testCompose() {
 Supplier<Integer> supplier1 = () -> 1;
 Function<Integer, Integer> function = i -> i * 2;
 Supplier<Integer> supplier2 = Suppliers.compose(function, supplier1);
 assertEquals(2, supplier2.get());
 }

 @Test(expected = NullPointerException.class)
 public void testComposeWithNullFunction() {
 Supplier<String> supplier = Suppliers.ofInstance("Hello");
 Function<? super String, Integer> function = null;
 try {
 Suppliers.compose(function, supplier);
 } catch (NullPointerException e) {
 // expected
 }
 }

 @Test(expected = NullPointerException.class)
 public void testComposeWithNullSupplier() {
 Function<String, Integer> function = s -> s.length();
 Supplier<String> supplier = null;
 try {
 Suppliers.compose(function, supplier);
 } catch (NullPointerException e) {
 // expected
 }
 }

 @Test
 public void testMemoize() {
 Supplier<Integer> supplier = () -> 1;
 Supplier<Integer> memoizedSupplier = Suppliers.memoize(supplier);
 assertEquals(1, memoizedSupplier.get());
 assertEquals(1, memoizedSupplier.get());
 }

 @Test
 public void testMemoizeWithExpiration() {
 Supplier<Long> supplier = () -> System.nanoTime();
 Supplier<Long> memoizedSupplier = Suppliers.memoizeWithExpiration(supplier, 100, TimeUnit.MILLISECONDS);
 long firstResult = memoizedSupplier.get();
 long secondResult = memoizedSupplier.get();
 assertEquals(firstResult, secondResult);
 sleep(150); // wait for expiration
 long thirdResult = memoizedSupplier.get();
 assertNotEquals(firstResult, thirdResult);
 }

 @Test(expected = NullPointerException.class)
 public void testMemoizeWithNullSupplier() {
 Supplier<String> supplier = null;
 try {
 Suppliers.memoize(supplier);
 } catch (NullPointerException e) {
 // expected
 }
 }

 @Test
 public void testOfInstance() {
 Supplier<String> supplier = Suppliers.ofInstance("Hello");
 assertEquals("Hello", supplier.get());
 }

 @Test(expected = NullPointerException.class)
 public void testOfInstanceWithNullInstance() {
 Supplier<String> supplier = Suppliers.ofInstance(null);
 }

 @Test
 public void testSynchronizedSupplier() {
 Supplier<Long> supplier = () -> System.nanoTime();
 Supplier<Long> synchronizedSupplier = Suppliers.synchronizedSupplier(supplier);
 long firstResult = synchronizedSupplier.get();
 long secondResult = synchronizedSupplier.get();
 assertEquals(firstResult, secondResult);
 }

 @Test
 public void testSupplierFunction() {
 Supplier<String> supplier = Suppliers.ofInstance("Hello");
 Function<Supplier<String>, String> function = Suppliers.supplierFunction();
 assertEquals("Hello", function.apply(supplier));
 }
}