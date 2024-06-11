package com.google.common.base;

import static org.junit.Assert.*;

import java.util.concurrent.*;
import org.junit.*;

@SuppressWarnings("unchecked")
public class SuppliersTest {

 @Test
 public void testCompose() {
 Supplier<String> supplier1 = () -> "Supplier 1";
 Supplier<String> supplier2 = (Supplier<String>) () -> "Supplier 2";
 Supplier<String> expected = () -> "Supplier 1" + " Supplier 2";
 Supplier<String> actual = Suppliers.compose(supplier1, supplier2);
 assertEquals(expected, actual);
 }

 @Test
 public void testMemoize() {
 Supplier<Integer> supplier = () -> 1;
 Supplier<Integer> memosupplier = Suppliers.memoize(supplier);
 assertEquals(1, memosupplier.get());
 assertEquals(1, memosupplier.get());
 }

 @Test
 public void testMemoizeWithExpiration() {
 Supplier<Long> supplier = () -> System.nanoTime();
 Supplier<Long> expiringSupplier = Suppliers.memoizeWithExpiration(supplier, 10, TimeUnit.MILLISECONDS);
 long firstResult = expiringSupplier.get();
 long secondResult = expiringSupplier.get();
 long thirdResult = expiringSupplier.get();
 long fourthResult = expiringSupplier.get();

 assertEquals(firstResult, secondResult);
 assertEquals(firstResult, thirdResult);
 assertEquals(firstResult, fourthResult);

 long delay = TimeUnit.MILLISECONDS.toNanos(11);
 try {
 Thread.sleep(delay);
 } catch (InterruptedException e) {
 e.printStackTrace();
 }

 assertEquals(firstResult, expiringSupplier.get());
 assertEquals(firstResult, expiringSupplier.get());

 long fifthResult = expiringSupplier.get();
 assertEquals(fifthResult, fifthResult + 1); // expired and a new result was generated
 }

 @Test
 public void testOfInstance() {
 Supplier<String> supplier = Suppliers.ofInstance("Instance");
 assertEquals("Instance", supplier.get());
 }

 @Test
 public void testSynchronizedSupplier() {
 Supplier<Integer> supplier = () -> new Random().nextInt();
 Supplier<Integer> synchronizedSupplier = Suppliers.synchronizedSupplier(supplier);

 Thread thread1 = new Thread(() -> assertEquals(synchronizedSupplier.get(), synchronizedSupplier.get()));
 Thread thread2 = new Thread(() -> assertEquals(synchronizedSupplier.get(), synchronizedSupplier.get()));

 thread1.start();
 thread2.start();

 try {
 thread1.join();
 thread2.join();
 } catch (InterruptedException e) {
 e.printStackTrace();
 }
 }

}