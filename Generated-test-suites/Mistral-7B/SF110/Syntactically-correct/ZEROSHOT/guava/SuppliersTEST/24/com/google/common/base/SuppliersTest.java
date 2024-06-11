package com.google.common.base;

import static org.junit.Assert.*;

import java.util.concurrent.*;

import org.junit.Test;

public class SuppliersTest {

 @Test
 public void testCompose() {
 Supplier<Integer> supplier1 = Suppliers.ofInstance(1);
 Function<Integer, Integer> function = new Function<Integer, Integer>() {
 @Override
 public Integer apply(Integer input) {
 return input * 2;
 }
 };
 Supplier<Integer> supplier2 = Suppliers.compose(function, supplier1);

 assertEquals(2, supplier2.get());
 assertEquals(2, supplier2.get());
 }

 @Test
 public void testMemoize() {
 Supplier<Integer> supplier1 = Suppliers.memoize(new Supplier<Integer>() {
 @Override
 public Integer get() {
 return 2;
 }
 });
 Supplier<Integer> supplier2 = Suppliers.memoize(supplier1);

 assertEquals(2, supplier1.get());
 assertEquals(2, supplier2.get());
 }

 @Test
 public void testMemoizeWithExpiration() {
 Supplier<Long> supplier = Suppliers.memoizeWithExpiration(Suppliers.ofInstance(System.currentTimeMillis()), 100, TimeUnit.MILLISECONDS);

 long time1 = supplier.get();
 long time2 = supplier.get();
 long time3 = supplier.get();

 assertEquals(time1, time2);
 assertEquals(time1, time3);

 long delay = 150;
 try {
 Thread.sleep(delay);
 } catch (InterruptedException e) {
 e.printStackTrace();
 }

 time2 = supplier.get();
 time3 = supplier.get();

 assertNotEquals(time1, time2);
 assertNotEquals(time1, time3);
 }

 @Test
 public void testSynchronizedSupplier() {
 final int counter = 0;
 Supplier<Integer> supplier = Suppliers.synchronizedSupplier(new Supplier<Integer>() {
 @Override
 public Integer get() {
 counter++;
 return counter;
 }
 });

 for (int i = 0; i < 100; i++) {
 assertEquals(i + 1, supplier.get());
 }
 }

 @Test(expected = NullPointerException.class)
 public void testNullFunction() {
 Function<Integer, Integer> function = null;
 Supplier<Integer> supplier = Suppliers.compose(function, Suppliers.ofInstance(1));
 }

 @Test(expected = NullPointerException.class)
 public void testNullSupplier() {
 Supplier<Integer> supplier = null;
 Function<Integer, Integer> function = new Function<Integer, Integer>() {
 @Override
 public Integer apply(Integer input) {
 return input * 2;
 }
 };
 Supplier<Integer> supplier2 = Suppliers.compose(function, supplier);
 }
}