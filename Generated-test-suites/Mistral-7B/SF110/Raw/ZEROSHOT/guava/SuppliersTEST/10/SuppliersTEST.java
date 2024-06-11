package com.google.common.base;

import static org.junit.Assert.*;

import java.util.concurrent.*;

import org.junit.*;

@SuppressWarnings("unchecked")
public class SuppliersTest {

 @BeforeClass
 public static void setup() {
 // Add any setup code here, if necessary
 }

 @AfterClass
 public static void teardown() {
 // Add any teardown code here, if necessary
 }

 @Test
 public void testCompose() {
 Function<Integer, Integer> function = new Function<Integer, Integer>() {
 @Override
 public Integer apply(Integer input) {
 return input * 2;
 }
 };
 Supplier<Integer> supplier = Suppliers.ofInstance(5);
 Supplier<Integer> composedSupplier = Suppliers.compose(function, supplier);
 assertEquals(10, composedSupplier.get());
 }

 @Test
 public void testMemoize() {
 Supplier<Integer> delegateSupplier = new Supplier<Integer>() {
 @Override
 public Integer get() {
 return 42;
 }
 };
 Supplier<Integer> memoizedSupplier = Suppliers.memoize(delegateSupplier);
 assertEquals(42, memoizedSupplier.get());
 assertEquals(42, memoizedSupplier.get());
 }

 @Test
 public void testMemoizeWithExpiration() {
 Supplier<Integer> delegateSupplier = new Supplier<Integer>() {
 @Override
 public Integer get() {
 return 42;
 }
 };
 Supplier<Integer> memoizedSupplierWithExpiration = Suppliers.memoizeWithExpiration(delegateSupplier, 100, TimeUnit.MILLISECONDS);
 assertEquals(42, memoizedSupplierWithExpiration.get());
 assertEquals(42, memoizedSupplierWithExpiration.get());
 try {
 Thread.sleep(150);
 } catch (InterruptedException e) {
 // Handle exception, if necessary
 }
 assertEquals(42, memoizedSupplierWithExpiration.get());
 try {
 Thread.sleep(200);
 } catch (InterruptedException e) {
 // Handle exception, if necessary
 }
 assertNotEquals(42, memoizedSupplierWithExpiration.get());
 }

 @Test
 public void testSynchronizedSupplier() {
 Supplier<Integer> delegateSupplier = new Supplier<Integer>() {
 @Override
 public Integer get() {
 return 42;
 }
 };
 Supplier<Integer> synchronizedSupplier = Suppliers.synchronizedSupplier(delegateSupplier);
 assertEquals(42, synchronizedSupplier.get());
 assertEquals(42, synchronizedSupplier.get());
 }

 @After
 public void teardownTest() {
 // Add any teardown code here, if necessary
 }
}