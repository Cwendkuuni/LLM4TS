package com.google.common.base;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.concurrent.*;
import javax.annotation.Nullable;
import org.junit.Test;

public class SuppliersTest {

 @Test
 public void testCompose() {
 Supplier<Integer> supplier1 = () -> 1;
 Function<Integer, Integer> function1 = i -> i * i;
 Supplier<Integer> supplier2 = Suppliers.compose(function1, supplier1);

 assertEquals(1, supplier1.get().intValue());
 assertEquals(1, function1.apply(1).intValue());
 assertEquals(1, supplier2.get().intValue());
 assertEquals(1, function1.apply(supplier1.get()).intValue());

 supplier1 = () -> 2;
 assertEquals(4, supplier2.get().intValue());
 }

 @Test
 public void testMemoize() {
 Supplier<String> supplier = () -> "hello";
 Supplier<String> memoizedSupplier = Suppliers.memoize(supplier);

 assertEquals("hello", supplier.get());
 assertEquals("hello", memoizedSupplier.get());
 assertEquals("hello", memoizedSupplier.get());
 }

 @Test
 public void testMemoizeWithExpiration() {
 Supplier<String> supplier = () -> "hello";
 Supplier<String> memoizedSupplier = Suppliers.memoizeWithExpiration(supplier, 1, TimeUnit.SECONDS);

 assertEquals("hello", supplier.get());
 assertEquals("hello", memoizedSupplier.get());
 assertEquals("hello", memoizedSupplier.get());

 try {
 Thread.sleep(1500);
 assertEquals("hello", memoizedSupplier.get());
 } catch (InterruptedException e) {
 // InterruptedException is expected
 }

 assertEquals("hello", supplier.get());
 assertEquals("hello", memoizedSupplier.get());
 }

 @Test
 public void testOfInstance() {
 Supplier<String> supplier = Suppliers.ofInstance("hello");
 assertEquals("hello", supplier.get());
 }

 @Test
 public void testSynchronizedSupplier() {
 Supplier<Integer> supplier = () -> 1;
 Supplier<Integer> synchronizedSupplier = Suppliers.synchronizedSupplier(supplier);

 Thread thread1 = new Thread(() -> {
 try {
 for (int i = 0; i < 1000; i++) {
 synchronizedSupplier.get();
 }
 } catch (Exception e) {
 e.printStackTrace();
 }
 });

 Thread thread2 = new Thread(() -> {
 try {
 for (int i = 0; i < 1000; i++) {
 synchronizedSupplier.get();
 }
 } catch (Exception e) {
 e.printStackTrace();
 }
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

 @Test
 public void testSupplierFunction() {
 Function<Supplier<Object>, Object> supplierFunction = Suppliers.supplierFunction();
 Supplier<Object> supplier = () -> new Object();

 assertEquals(supplier.get(), supplierFunction.apply(supplier).getClass().getSuperclass().getSimpleName().equals("Object") ? supplierFunction.apply(supplier) : supplierFunction.apply(supplier).toString());
 }
}