package com.google.common.base;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.google.common.base.Suppliers;
import java.util.concurrent.Supplier;
import org.junit.Test;

public class SuppliersTest {

 @Test
 public void testCompose() {
 Supplier<String> supplier1 = mock(Supplier.class);
 Function<String, Integer> function = s -> s.length();
 Supplier<String> supplier2 = Suppliers.compose(function, supplier1);

 when(supplier1.get()).thenReturn("hello");

 assertEquals(5, supplier2.get());

 verify(supplier1).get();
 }

 @Test(expected = NullPointerException.class)
 public void testComposeWithNullFunction() {
 Function<String, Integer> function = null;
 Supplier<String> supplier = mock(Supplier.class);

 Suppliers.compose(function, supplier);
 }

 @Test(expected = NullPointerException.class)
 public void testComposeWithNullSupplier() {
 Supplier<String> supplier = null;
 Function<String, Integer> function = s -> s.length();

 Suppliers.compose(function, supplier);
 }

 @Test
 public void testMemoize() {
 Supplier<Integer> supplier = () -> 42;
 Supplier<Integer> memoizedSupplier = Suppliers.memoize(supplier);

 assertEquals(42, memoizedSupplier.get());
 assertEquals(42, memoizedSupplier.get());
 }

 @Test
 public void testMemoizeWithExpiration() {
 long duration = 100;
 TimeUnit timeUnit = TimeUnit.MILLISECONDS;
 Supplier<Long> supplier = () -> System.currentTimeMillis();
 Supplier<Long> expiringMemoizingSupplier = Suppliers.memoizeWithExpiration(supplier, duration, timeUnit);

 // First call should return the current time
 long currentTime = System.currentTimeMillis();
 long firstResult = expiringMemoizingSupplier.get();

 // Wait for some time, then call again
 try {
 Thread.sleep(duration + 10);
 } catch (InterruptedException e) {
 throw new RuntimeException(e);
 }
 long secondResult = expiringMemoizingSupplier.get();

 assertEquals(currentTime, firstResult); // First result should be cached
 assertNotEquals(currentTime, secondResult); // Second result should be the current time
 }

 @Test
 public void testOfInstance() {
 Supplier<String> supplier = Suppliers.ofInstance("hello");
 assertEquals("hello", supplier.get());
 }

 @Test
 public void testSynchronizedSupplier() {
 Supplier<Integer> supplier = () -> 42;
 Supplier<Integer> synchronizedSupplier = Suppliers.synchronizedSupplier(supplier);

 Thread thread1 = new Thread(() -> {
 for (int i = 0; i < 1000; i++) {
 assertEquals(42, synchronizedSupplier.get());
 }
 });

 Thread thread2 = new Thread(() -> {
 for (int i = 0; i < 1000; i++) {
 assertEquals(42, synchronizedSupplier.get());
 }
 });

 thread1.start();
 thread2.start();

 try {
 thread1.join();
 thread2.join();
 } catch (InterruptedException e) {
 throw new RuntimeException(e);
 }
 }
}