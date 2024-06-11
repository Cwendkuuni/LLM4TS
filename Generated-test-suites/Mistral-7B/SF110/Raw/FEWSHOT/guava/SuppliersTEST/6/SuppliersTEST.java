package com.google.common.base;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.concurrent.Supplier;
import org.junit.Test;

public class SuppliersTest {

 @Test
 public void testCompose() {
 Supplier<String> supplier1 = () -> "supplier1";
 Supplier<Integer> supplier2 = () -> 1;
 Supplier<String> expected = () -> "supplier1-1";
 Supplier<String> result = Suppliers.compose(s -> s + s.getValue(), supplier1);
 Supplier<Integer> functionSupplier = Suppliers.supplierFunction();
 Supplier<String> functionResult = Suppliers.compose(functionSupplier, supplier2);

 assertEquals(expected.get(), result.get());
 assertEquals(expected.get(), functionResult.get());
 }

 @Test
 public void testMemoize() {
 Supplier<String> supplier = () -> "supplier";
 Supplier<String> memoizedSupplier = Suppliers.memoize(supplier);

 assertEquals(supplier.get(), memoizedSupplier.get());
 assertEquals(supplier.get(), memoizedSupplier.get());
 }

 @Test
 public void testMemoizeWithExpiration() {
 long duration = 1;
 TimeUnit unit = TimeUnit.MILLISECONDS;
 Supplier<String> supplier = () -> "supplier";
 Supplier<String> memoizedSupplier = Suppliers.memoizeWithExpiration(supplier, duration, unit);

 assertEquals(supplier.get(), memoizedSupplier.get());
 assertEquals(supplier.get(), memoizedSupplier.get());

 try {
 Thread.sleep(duration * 2);
 } catch (InterruptedException e) {
 e.printStackTrace();
 }

 assertEquals(supplier.get(), memoizedSupplier.get());
 assertNull(memoizedSupplier.get());
 }

 @Test
 public void testOfInstance() {
 String instance = "instance";
 Supplier<String> supplier = Suppliers.ofInstance(instance);

 assertEquals(instance, supplier.get());
 }

 @Test(expected = NullPointerException.class)
 public void testSynchronizedSupplierNullDelegate() {
 Supplier<String> supplier = null;
 Suppliers.synchronizedSupplier(supplier);
 }

 @Test
 public void testSynchronizedSupplier() {
 Supplier<String> supplier = () -> "supplier";
 Supplier<String> synchronizedSupplier = Suppliers.synchronizedSupplier(supplier);

 Supplier<String> supplier1 = () -> "supplier1";
 Supplier<String> supplier2 = () -> "supplier2";
 Thread thread1 = new Thread(() -> assertEquals(supplier.get(), synchronizedSupplier.get()));
 Thread thread2 = new Thread(() -> assertEquals(supplier.get(), synchronizedSupplier.get()));

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