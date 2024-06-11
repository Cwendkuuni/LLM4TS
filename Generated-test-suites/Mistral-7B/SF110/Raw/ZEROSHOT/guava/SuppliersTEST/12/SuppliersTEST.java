package com.google.common.base.test;

import static org.junit.Assert.*;

import java.util.concurrent.Supplier;

import org.junit.Test;

import com.google.common.base.*;

public class SuppliersTest {

 @Test
 public void testCompose() {
 Supplier<String> supplier1 = () -> "Supplier 1";
 Supplier<String> supplier2 = () -> "Supplier 2";
 Supplier<String> expected = () -> "Supplier 1: Supplier 2";

 Supplier<String> actual = Suppliers.compose(supplier2, supplier1);

 assertEquals(expected, actual);
 }

 @Test
 public void testMemoize() {
 Supplier<String> supplier = () -> "Supplier";
 Supplier<String> memoizedSupplier = Suppliers.memoize(supplier);

 assertEquals(supplier, memoizedSupplier);

 String result1 = memoizedSupplier.get();
 String result2 = memoizedSupplier.get();

 assertEquals(result1, result2);
 }

 @Test
 public void testMemoizeWithExpiration() {
 Supplier<String> supplier = () -> "Supplier";

 long duration = 1;
 TimeUnit timeUnit = TimeUnit.MILLISECONDS;

 Supplier<String> expiringMemoizedSupplier = Suppliers.memoizeWithExpiration(supplier, duration, timeUnit);

 assertEquals(supplier, expiringMemoizedSupplier);

 String result1 = expiringMemoizedSupplier.get();
 String result2 = expiringMemoizedSupplier.get();

 assertEquals(result1, result2);

 try {
 Thread.sleep(duration + 1);
 } catch (InterruptedException e) {
 e.printStackTrace();
 }

 String result3 = expiringMemoizedSupplier.get();

 assertNotSame(result1, result3);
 }

 @Test
 public void testOfInstance() {
 Supplier<String> supplier = Suppliers.ofInstance("Instance");

 assertEquals("Instance", supplier.get());
 }

 @Test
 public void testSynchronizedSupplier() {
 Supplier<String> supplier = () -> "Supplier";
 Supplier<String> synchronizedSupplier = Suppliers.synchronizedSupplier(supplier);

 Supplier<String> supplier1Thread1 = () -> new Supplier<String>() {
 @Override
 public String get() {
 return synchronizedSupplier.get();
 }
 };

 Supplier<String> supplier2Thread2 = () -> new Supplier<String>() {
 @Override
 public String get() {
 return synchronizedSupplier.get();
 }
 };

 Thread thread1 = new Thread(supplier1Thread1);
 Thread thread2 = new Thread(supplier2Thread2);

 thread1.start();
 thread2.start();

 try {
 thread1.join();
 thread2.join();
 } catch (InterruptedException e) {
 e.printStackTrace();
 }

 String result1 = supplier1Thread1.get();
 String result2 = supplier2Thread2.get();

 assertEquals(result1, result2);
 }
}