package com.google.common.base;

import static org.junit.Assert.*;

import java.util.concurrent.*;

import org.junit.*;

@SuppressWarnings("unchecked")
public class SuppliersTest {

 @Test
 public void testCompose() {
 Supplier<String> supplier1 = () -> "supplier1";
 Supplier<String> supplier2 = (Supplier<String> s) -> "supplier2_" + s.get();

 Supplier<String> composedSupplier = Suppliers.compose(supplier2, supplier1);

 assertEquals("supplier2_supplier1", composedSupplier.get());
 }

 @Test
 public void testMemoize() {
 Supplier<String> supplier = () -> "supplier";

 Supplier<String> memoizedSupplier = Suppliers.memoize(supplier);

 assertEquals("supplier", memoizedSupplier.get());
 assertEquals("supplier", memoizedSupplier.get());
 }

 @Test
 public void testMemoizeWithExpiration() {
 Supplier<String> supplier = () -> "supplier";

 Supplier<String> memoizedSupplierWithExpiration = Suppliers.memoizeWithExpiration(supplier, 100, TimeUnit.MILLISECONDS);

 assertEquals("supplier", memoizedSupplierWithExpiration.get());
 assertEquals("supplier", memoizedSupplierWithExpiration.get());

 try {
 Thread.sleep(150);
 } catch (InterruptedException e) {
 e.printStackTrace();
 }

 assertEquals("supplier", memoizedSupplierWithExpiration.get());

 try {
 Thread.sleep(200);
 } catch (InterruptedException e) {
 e.printStackTrace();
 }

 assertNotEquals("supplier", memoizedSupplierWithExpiration.get());
 }

 @Test
 public void testOfInstance() {
 String instance = "instance";

 Supplier<String> supplier = Suppliers.ofInstance(instance);

 assertEquals(instance, supplier.get());
 }

 @Test
 public void testSynchronizedSupplier() {
 Supplier<String> supplier = () -> "supplier";

 Supplier<String> synchronizedSupplier = Suppliers.synchronizedSupplier(supplier);

 Thread thread1 = new Thread(() -> {
 try {
 Thread.sleep(10);
 } catch (InterruptedException e) {
 e.printStackTrace();
 }
 assertEquals("supplier", synchronizedSupplier.get());
 });

 Thread thread2 = new Thread(() -> {
 try {
 Thread.sleep(15);
 } catch (InterruptedException e) {
 e.printStackTrace();
 }
 assertEquals("supplier", synchronizedSupplier.get());
 });

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