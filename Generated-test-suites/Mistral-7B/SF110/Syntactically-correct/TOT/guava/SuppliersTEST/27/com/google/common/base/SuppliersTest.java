package com.google.common.base.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.concurrent.TimeUnit;

import org.junit.Before;
import org.junit.Test;

import com.google.common.base.Function;
import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;

public class SuppliersTest {

 private Supplier<String> supplier1;
 private Supplier<String> supplier2;
 private Supplier<String> supplier3;
 private Supplier<String> supplier4;

 @Before
 public void setup() {
 supplier1 = Suppliers.ofInstance("Hello World");
 supplier2 = Suppliers.memoize(supplier1);
 supplier3 = Suppliers.memoizeWithExpiration(supplier1, 10, TimeUnit.MILLISECONDS);
 supplier4 = Suppliers.synchronizedSupplier(supplier1);
 }

 @Test
 public void testCompose() {
 Function<String, Integer> lengthFunction = new Function<String, Integer>() {
 @Override
 public Integer apply(String input) {
 return input.length();
 }
 };
 Supplier<Integer> supplier5 = Suppliers.compose(lengthFunction, supplier1);
 assertEquals(13, supplier5.get());
 }

 @Test
 public void testMemoize() {
 assertEquals("Hello World", supplier2.get());
 assertEquals("Hello World", supplier2.get());
 }

 @Test
 public void testMemoizeWithExpiration() {
 assertEquals("Hello World", supplier3.get());
 try {
 Thread.sleep(15);
 assertEquals("Hello World", supplier3.get());
 } catch (InterruptedException e) {
 // Do nothing
 }
 assertNull(supplier3.get());
 }

 @Test(expected = NullPointerException.class)
 public void testMemoizeWithNullDelegate() {
 Supplier<String> nullSupplier = null;
 Suppliers.memoize(nullSupplier);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testMemoizeWithNegativeExpiration() {
 Supplier<String> supplier6 = Suppliers.memoizeWithExpiration(supplier1, -1, TimeUnit.MILLISECONDS);
 }

 @Test
 public void testOfInstance() {
 assertEquals("Hello World", supplier1.get());
 assertEquals("Hello World", supplier1.get());
 }

 @Test
 public void testSynchronizedSupplier() throws InterruptedException {
 Thread thread1 = new Thread(() -> {
 try {
 Supplier<String> supplier7 = supplier4;
 assertEquals("Hello World", supplier7.get());
 supplier7 = null;
 } finally {
 supplier1 = null;
 }
 });
 Thread thread2 = new Thread(() -> {
 try {
 Supplier<String> supplier8 = supplier4;
 assertEquals("Hello World", supplier8.get());
 supplier8 = null;
 } finally {
 supplier1 = null;
 }
 });
 thread1.start();
 thread2.start();
 thread1.join();
 thread2.join();
 assertNotNull(supplier1);
 }

 @Test
 public void testSupplierFunction() {
 Supplier<Object> supplier9 = Suppliers.supplierFunction();
 assertNotNull(supplier9);
 assertEquals(supplier9.get(), supplier9.get());
 }
}