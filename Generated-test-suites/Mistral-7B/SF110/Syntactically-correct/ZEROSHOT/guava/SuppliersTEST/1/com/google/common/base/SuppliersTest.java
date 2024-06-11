package com.example.test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.concurrent.Supplier;

import org.junit.Before;
import org.junit.Test;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.base.Suppliers;
import com.google.common.collect.Bimaps;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.common.util.concurrent.ThreadFactoryBuilder;

public class SuppliersTest {

 private static final String TEST_FUNCTION_NAME = "testFunction";
 private static final String TEST_INSTANCE = "testInstance";
 private static final long TEST_DURATION = 100;
 private static final TimeUnit TEST_TIME_UNIT = TimeUnit.MILLISECONDS;
 private static final Function<String, Integer> TEST_FUNCTION = new Function<String, Integer>() {
 @Override
 public Integer apply(String input) {
 return input.length();
 }
 };
 private static final Supplier<String> TEST_SUPPLIER = Suppliers.ofInstance(TEST_INSTANCE);
 private Supplier<String> testComposeSupplier;
 private Supplier<String> testMemoizeSupplier;
 private Supplier<String> testMemoizeWithExpirationSupplier;
 private Supplier<String> testSynchronizedSupplier;

 @Before
 public void setUp() {
 testComposeSupplier = Suppliers.compose(TEST_FUNCTION, TEST_SUPPLIER);
 testMemoizeSupplier = Suppliers.memoize(TEST_SUPPLIER);
 testMemoizeWithExpirationSupplier = Suppliers.memoizeWithExpiration(TEST_SUPPLIER, TEST_DURATION, TEST_TIME_UNIT);
 testSynchronizedSupplier = Suppliers.synchronizedSupplier(TEST_MEMOIZE_SUPPLIER);
 }

 @Test
 public void testComposeSupplier() {
 final String expected = TEST_INSTANCE;
 final String result = testComposeSupplier.get();
 assertEquals(expected, result);
 verify(TEST_SUPPLIER, times(1)).get();
 verify(TEST_FUNCTION, times(1)).apply(TEST_INSTANCE);
 }

 @Test
 public void testMemoizeSupplier() {
 final Supplier<String> memoizedSupplier = Suppliers.memoize(testMemoizeSupplier);
 final String firstResult = memoizedSupplier.get();
 final String secondResult = memoizedSupplier.get();
 assertEquals(firstResult, secondResult);
 }

 @Test
 public void testMemoizeWithExpirationSupplier() {
 final long initialNanos = System.nanoTime();
 final long expirationNanos = initialNanos + TEST_DURATION * TEST_TIME_UNIT.toNanos(1);
 final int initialValue = TEST_FUNCTION.apply(TEST_INSTANCE);
 final int firstResult = testMemoizeWithExpirationSupplier.get();
 final int secondResult = testMemoizeWithExpirationSupplier.get();
 assertEquals(initialValue, firstResult);
 assertEquals(initialValue, secondResult);
 await().atMost(TEST_DURATION, TEST_TIME_UNIT.MILLISECONDS).until(() -> testMemoizeWithExpirationSupplier.get() != initialValue);
 final int thirdResult = testMemoizeWithExpirationSupplier.get();
 assertNotEquals(initialValue, thirdResult);
 }

 @Test
 public void testSynchronizedSupplier() {
 final AtomicInteger counter = new AtomicInteger(0);
 final Supplier<String> testSupplier = () -> "{synchronized} " + counter.getAndIncrement();
 final Supplier<String> synchronizedSupplier = Suppliers.synchronizedSupplier(testSupplier);
 final Thread thread1 = new Thread(() -> {
 for (int i = 0; i < 10; i++) {
 synchronizedSupplier.get();
 }
 });
 final Thread thread2 = new Thread(() -> {
 for (int i = 0; i < 10; i++) {
 synchronizedSupplier.get();
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
 final String thread1Result = testSupplier.get();
 final String thread2Result = testSupplier.get();
 assertEquals(thread1Result, thread2Result);
 }

 @Test
 public void testSupplierFunction() {
 final SupplierFunction<String> supplierFunction = Suppliers.supplierFunction();
 final String testInstance = "testInstance";
 final String result = supplierFunction.apply(Suppliers.ofInstance(testInstance));
 assertEquals(testInstance, result);
 }

}