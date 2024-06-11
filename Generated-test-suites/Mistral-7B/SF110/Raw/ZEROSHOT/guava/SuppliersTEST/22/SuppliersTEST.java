package com.google.common.base;

import static org.junit.Assert.*;

import java.util.concurrent.*;
import org.junit.*;

public class SuppliersTest {

 @SuppressWarnings("unchecked")
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

 @Test(expected = NullPointerException.class)
 public void testComposeNullFunction() {
 Function<Integer, Integer> function = null;
 Supplier<Integer> supplier = Suppliers.ofInstance(5);
 try {
 Suppliers.compose(function, supplier);
 } catch (NullPointerException e) {
 fail("Did not throw NullPointerException: function is marked null.");
 }
 }

 @Test(expected = NullPointerException.class)
 public void testComposeNullSupplier() {
 Supplier<Integer> supplier = null;
 Function<Integer, Integer> function = (x) -> x * 2;
 try {
 Suppliers.compose(function, supplier);
 } catch (NullPointerException e) {
 fail("Did not throw NullPointerException: supplier is marked null.");
 }
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
 Supplier<Long> supplier = () -> System.nanoTime();
 Supplier<Long> memoizedSupplier = Suppliers.memoizeWithExpiration(supplier, 1, TimeUnit.SECONDS);
 long initial = memoizedSupplier.get();
 long current = System.nanoTime();
 long delay = 2 * 1000 * 1000 * 1000L / 2; // 1 second / 2
 try {
 Thread.sleep(delay);
 } catch (InterruptedException e) {
 e.printStackTrace();
 }
 assertEquals(initial, memoizedSupplier.get());
 }

 @Test(expected = NullPointerException.class)
 public void testMemoizeNullSupplier() {
 Supplier<Integer> supplier = null;
 try {
 Suppliers.memoize(supplier);
 } catch (NullPointerException e) {
 // Expected
 }
 }

 @Test
 public void testOfInstance() {
 Supplier<String> supplier = Suppliers.ofInstance("hello");
 assertEquals("hello", supplier.get());
 }

 @Test(expected = NullPointerException.class)
 public void testOfInstanceNullInstance() {
 Supplier<String> supplier = Suppliers.ofInstance(null);
 }

 @Test
 public void testSynchronizedSupplier() {
 final AtomicInteger count = new AtomicInteger(0);
 Supplier<Integer> supplier = new Supplier<Integer>() {
 @Override
 public Integer get() {
 count.incrementAndGet();
 return 42;
 }
 };
 Supplier<Integer> synchronizedSupplier = Suppliers.synchronizedSupplier(supplier);
 for (int i = 0; i < 1000; i++) {
 synchronizedSupplier.get();
 }
 assertEquals(1000, count.get());
 }
}