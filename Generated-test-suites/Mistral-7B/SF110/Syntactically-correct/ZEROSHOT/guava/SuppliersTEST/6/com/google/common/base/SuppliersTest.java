package com.google.common.base.test;

import com.google.common.base.Function;
import com.google.common.base.Supplier;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import static org.junit.Assert.*;

@RunWith(JUnit4.class)
public class SuppliersTest {

 @Test
 public void testCompose() {
 Supplier<Integer> supplier1 = () -> 1;
 Function<Integer, Integer> function = i -> i * i;
 Supplier<Integer> expectedSupplier = () -> function.apply(1);
 Supplier<Integer> actualSupplier = Suppliers.compose(function, supplier1);

 assertEquals(expectedSupplier, actualSupplier);
 assertSame(supplier1, actualSupplier.getDelegate());
 assertEquals(1, actualSupplier.get().intValue());
 }

 @Test
 public void testCompose_NullFunctionOrSupplier() {
 assertNull(Suppliers.compose(null, () -> {}));
 assertNull(Suppliers.compose(() -> {}, null));
 }

 @Test
 public void testMemoize() {
 Supplier<String> supplier = () -> "hello";
 Supplier<String> memoizedSupplier = Suppliers.memoize(supplier);

 assertEquals(supplier, memoizedSupplier.getDelegate());
 assertEquals("hello", memoizedSupplier.get());
 assertEquals("hello", memoizedSupplier.get());
 }

 @Test
 public void testMemoizeWithExpiration() {
 Supplier<Long> supplier = () -> System.nanoTime();
 Supplier<Long> memoizedSupplier = Suppliers.memoizeWithExpiration(supplier, 10, TimeUnit.MILLISECONDS);

 long firstResult = memoizedSupplier.get();
 long secondResult = memoizedSupplier.get();
 long thirdResult = memoizedSupplier.get();

 assertEquals(firstResult, secondResult);
 assertEquals(firstResult, thirdResult);

 Thread.yield();
 Thread.yield();

 assertNotSame(firstResult, thirdResult);
 }

 @Test
 public void testMemoizeWithExpiration_NegativeDuration() {
 Supplier<Long> supplier = () -> System.nanoTime();
 assertThrows(IllegalArgumentException.class, () -> Suppliers.memoizeWithExpiration(supplier, -10, TimeUnit.MILLISECONDS));
 }

 @Test
 public void testOfInstance() {
 Supplier<String> supplier = Suppliers.ofInstance("test");
 assertEquals("test", supplier.get());
 }

 @Test
 public void testSynchronizedSupplier() {
 Supplier<Long> supplier = () -> System.nanoTime();
 Supplier<Long> synchronizedSupplier = Suppliers.synchronizedSupplier(supplier);

 Long firstResult = synchronizedSupplier.get();
 Long secondResult = synchronizedSupplier.get();

 assertEquals(firstResult, secondResult);
 }

 @Test
 public void testSupplierFunction() {
 Supplier<String> supplier = Suppliers.ofInstance("test");
 Function<Supplier<String>, String> supplierFunction = Suppliers.supplierFunction();

 assertEquals("test", supplierFunction.apply(supplier).get());
 }

 @Test
 public void testThreadSafety() {
 final AtomicInteger counter = new AtomicInteger(0);

 Supplier<Long> supplier = () -> {
 counter.incrementAndGet();
 return System.nanoTime();
 };

 Supplier<Long> synchronizedSupplier = Suppliers.synchronizedSupplier(supplier);

 Runnable task = () -> {
 for (int i = 0; i < 10_000; i++) {
 synchronizedSupplier.get();
 }
 };

 Thread thread1 = new Thread(task);
 Thread thread2 = new Thread(task);

 thread1.start();
 thread2.start();

 try {
 thread1.join();
 thread2.join();
 } catch (InterruptedException e) {
 // ignore
 }

 assertEquals(2 * 10_000, counter.get());
 }
}