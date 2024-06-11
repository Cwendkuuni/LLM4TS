package com.google.common.base;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static org.junit.Assert.*;

import java.util.concurrent.*;
import javax.annotation.*;
import org.junit.Test;

public class SuppliersTest {

 @Test
 public void testCompose() {
 Supplier<Integer> supplier1 = () -> 1;
 Function<Integer, String> function1 = i -> String.valueOf(i + 1);

 Supplier<String> supplierComposition = Suppliers.compose(function1, supplier1);
 assertEquals("2", supplierComposition.get());
 }

 @Test
 public void testMemoize() {
 Supplier<String> supplier1 = () -> "hello";
 Supplier<String> memoizedSupplier = Suppliers.memoize(supplier1);

 assertEquals("hello", memoizedSupplier.get());
 assertEquals("hello", memoizedSupplier.get());
 }

 @Test
 public void testMemoizeWithExpiration() {
 Supplier<Long> supplier1 = () -> 1L;
 Supplier<Long> memoizedSupplier = Suppliers.memoizeWithExpiration(supplier1, 100, TimeUnit.MILLISECONDS);

 assertEquals(1L, memoizedSupplier.get());
 assertEquals(1L, memoizedSupplier.get());
 try {
 Thread.sleep(150);
 } catch (InterruptedException e) {
 e.printStackTrace();
 }
 assertEquals(1L, memoizedSupplier.get());

 Supplier<Long> newSupplier = () -> 2L;
 Supplier<Long> newMemoizedSupplier = Suppliers.memoizeWithExpiration(newSupplier, 100, TimeUnit.MILLISECONDS);
 assertEquals(2L, newMemoizedSupplier.get());
 }

 @Test
 public void testOfInstance() {
 Supplier<String> supplierOfInstance = Suppliers.ofInstance("hello");
 assertEquals("hello", supplierOfInstance.get());
 }

 @Test(expected = NullPointerException.class)
 public void testSynchronizedSupplierNullDelegate() {
 Suppliers.synchronizedSupplier(null);
 }

 @Test
 public void testSynchronizedSupplier() {
 Supplier<Integer> supplier1 = () -> 1;
 Supplier<Integer> synchronizedSupplier = Suppliers.synchronizedSupplier(supplier1);

 Supplier<Integer> supplier2 = () -> 2;
 Supplier<Integer> synchronizedSupplier2 = Suppliers.synchronizedSupplier(supplier2);

 assertEquals(1, synchronizedSupplier.get());
 assertEquals(1, synchronizedSupplier.get());

 assertEquals(2, synchronizedSupplier2.get());
 assertEquals(2, synchronizedSupplier2.get());
 }

 @Test
 public void testSupplierFunction() {
 Function<Supplier<Object>, Object> supplierFunction = Suppliers.supplierFunction();
 assertEquals(null, supplierFunction.apply(null));

 Supplier<Object> supplier = () -> "hello";
 Object result = supplierFunction.apply(supplier);
 assertEquals("hello", result);
 }
}