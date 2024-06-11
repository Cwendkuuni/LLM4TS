package com.example.test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.concurrent.Supplier;

import org.junit.Before;
import org.junit.Test;

import com.google.common.base.Suppliers;

public class SuppliersTest {

 private Function<Object, Integer> function;
 private Supplier<Object> supplier;
 private Supplier<Integer> composedSupplier;
 private Supplier<Integer> memoizedSupplier;
 private Supplier<Integer> memoizedWithExpirationSupplier;
 private Supplier<Integer> supplierOfInstance;

 @Before
 public void setup() {
 function = mock(Function.class);
 supplier = mock(Supplier.class);
 when(supplier.get()).thenReturn(123);
 composedSupplier = Suppliers.compose(function, supplier);
 memoizedSupplier = Suppliers.memoize(Suppliers.memoize(Suppliers.ofInstance(456)));
 memoizedWithExpirationSupplier = Suppliers.memoizeWithExpiration(memoizedSupplier, 100, TimeUnit.MILLISECONDS);
 supplierOfInstance = Suppliers.ofInstance(789);
 }

 @Test
 public void testCompose() {
 Supplier<Integer> result = composedSupplier.get();
 assertEquals(123, result.get());
 verify(function).apply(supplier);
 }

 @Test(expected = NullPointerException.class)
 public void testComposeNullFunction() {
 Supplier<Integer> result = Suppliers.compose(null, supplier).get();
 fail("Expected NullPointerException");
 }

 @Test(expected = NullPointerException.class)
 public void testComposeNullSupplier() {
 Supplier<Integer> result = Suppliers.compose(function, null).get();
 fail("Expected NullPointerException");
 }

 @Test(expected = NullPointerException.class)
 public void testMemoizeNullSupplier() {
 Supplier<Integer> result = Suppliers.memoize(null).get();
 fail("Expected NullPointerException");
 }

 @Test
 public void testMemoize() {
 Supplier<Integer> result = memoizedSupplier.get();
 assertEquals(456, result.get());
 result = memoizedSupplier.get();
 assertEquals(456, result.get());
 }

 @Test
 public void testMemoizeWithExpiration() {
 Supplier<Integer> result = memoizedWithExpirationSupplier.get();
 assertEquals(456, result.get());
 result = memoizedWithExpirationSupplier.get();
 assertEquals(456, result.get());
 try {
 Thread.sleep(110);
 } catch (InterruptedException e) {
 e.printStackTrace();
 }
 result = memoizedWithExpirationSupplier.get();
 assertNotEquals(456, result.get());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testMemoizeWithNegativeExpiration() {
 Supplier<Integer> result = Suppliers.memoizeWithExpiration(memoizedSupplier, -100, TimeUnit.MILLISECONDS).get();
 fail("Expected IllegalArgumentException");
 }

 @Test
 public void testOfInstance() {
 Supplier<Integer> result = supplierOfInstance.get();
 assertEquals(789, result.get());
 }

 @Test(expected = NullPointerException.class)
 public void testOfInstanceNullInstance() {
 Supplier<Integer> result = Suppliers.ofInstance(null).get();
 fail("Expected NullPointerException");
 }

 @Test
 public void testSynchronizedSupplier() {
 Supplier<Integer> result = Suppliers.synchronizedSupplier(supplier).get();
 assertEquals(123, result.get());
 }

 @Test
 public void testSupplierFunction() {
 Supplier<Object> supplier = mock(Supplier.class);
 when(supplier.get()).thenReturn("Hello World");
 Supplier<String> result = Suppliers.supplierFunction().apply(supplier);
 assertEquals("Hello World", result.get());
 }
}