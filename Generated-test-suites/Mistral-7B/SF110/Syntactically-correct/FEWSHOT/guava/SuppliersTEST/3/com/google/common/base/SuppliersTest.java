package com.google.common.base;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.junit.Assert.*;

import java.util.concurrent.*;
import org.junit.Test;

public class SuppliersTest {

 @Test
 public void testCompose() {
 Supplier<String> stringSupplier = () -> "hello";
 Function<String, Integer> lengthFunction = String::length;
 Supplier<Integer> composedSupplier = Suppliers.compose(lengthFunction, stringSupplier);
 assertEquals(5, composedSupplier.get());
 }

 @Test
 public void testMemoize() {
 Supplier<Integer> delegate = () -> 42;
 Supplier<Integer> memoizedSupplier = Suppliers.memoize(delegate);
 assertEquals(42, memoizedSupplier.get());
 assertEquals(42, memoizedSupplier.get());
 }

 @Test
 public void testMemoizeWithExpiration() {
 long duration = 100;
 TimeUnit timeUnit = TimeUnit.MILLISECONDS;
 Supplier<Long> delegate = () -> System.nanoTime();
 Supplier<Long> memoizedWithExpirationSupplier = Suppliers.memoizeWithExpiration(delegate, duration, timeUnit);

 long previousTime = 0L;
 for (int i = 0; i < 10; i++) {
 previousTime = memoizedWithExpirationSupplier.get();
 try {
 Thread.sleep(timeUnit.toMillis(duration) + 10);
 } catch (InterruptedException e) {
 Thread.currentThread().interrupt();
 }
 assertEquals(previousTime, memoizedWithExpirationSupplier.get());
 }
 }

 @Test
 public void testOfInstance() {
 Supplier<Integer> instanceSupplier = Suppliers.ofInstance(42);
 assertEquals(42, instanceSupplier.get());
 }

 @Test
 public void testSynchronizedSupplier() {
 Supplier<Integer> delegate = () -> 42;
 Supplier<Integer> synchronizedSupplier = Suppliers.synchronizedSupplier(delegate);
 assertSame(delegate, synchronizedSupplier);

 Supplier<Integer> thread1 = () -> synchronizedSupplier.get();
 Supplier<Integer> thread2 = () -> synchronizedSupplier.get();

 new Thread(() -> assertEquals(42, thread1.get())).start();
 new Thread(() -> assertEquals(42, thread2.get())).start();

 try {
 Thread.sleep(100);
 } catch (InterruptedException e) {
 Thread.currentThread().interrupt();
 }
 }

 @Test
 public void testSupplierFunction() {
 Function<Supplier<Object>, Object> supplierFunction = Suppliers.supplierFunction();
 assertEquals(null, supplierFunction.apply(null));
 Supplier<Object> supplier = () -> new Object();
 assertEquals(supplier.get(), supplierFunction.apply(supplier));
 }
}