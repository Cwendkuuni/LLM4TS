package com.google.common.base.test;

import com.google.common.base.*;
import org.junit.*;
import static org.junit.Assert.*;;

public class SuppliersTest {

 @BeforeClass
 public static void setup() {
 // Set up any static state or shared resources here, if necessary
 }

 @AfterClass
 public static void teardown() {
 // Clean up any static state or shared resources here, if necessary
 }

 @Test
 public void testCompose() {
 Function<Integer, Integer> addOne = new Function<Integer, Integer>() {
 @Override
 public Integer apply(Integer input) {
 return input + 1;
 }
 };
 Supplier<Integer> supplier = Suppliers.ofInstance(42);
 Supplier<Integer> composedSupplier = Suppliers.compose(addOne, supplier);
 assertEquals(43, composedSupplier.get());
 }

 @Test(expected = NullPointerException.class)
 public void testComposeNullFunction() {
 Function<Integer, Integer> nullFunction = null;
 Supplier<Integer> supplier = Suppliers.ofInstance(42);
 try {
 Suppliers.compose(nullFunction, supplier);
 } catch (NullPointerException e) {
 // Expected exception
 }
 }

 @Test(expected = NullPointerException.class)
 public void testComposeNullSupplier() {
 Function<Integer, Integer> addOne = new Function<Integer, Integer>() {
 @Override
 public Integer apply(Integer input) {
 return input + 1;
 }
 };
 Supplier<Integer> nullSupplier = null;
 try {
 Suppliers.compose(addOne, nullSupplier);
 } catch (NullPointerException e) {
 // Expected exception
 }
 }

 @Test
 public void testMemoize() {
 Supplier<String> delegate = new Supplier<String>() {
 @Override
 public String get() {
 return "Hello";
 }
 };
 Supplier<String> memoizedSupplier = Suppliers.memoize(delegate);
 assertEquals("Hello", memoizedSupplier.get());
 assertEquals("Hello", memoizedSupplier.get());
 }

 @Test
 public void testMemoizeWithExpirationShortDuration() {
 Supplier<String> delegate = new Supplier<String>() {
 @Override
 public String get() {
 return "Hello";
 }
 };
 Supplier<String> memoizedSupplier = Suppliers.memoizeWithExpiration(delegate, 1, TimeUnit.MILLISECONDS);
 assertEquals("Hello", memoizedSupplier.get());
 assertEquals("Hello", memoizedSupplier.get());
 // Sleep for 2ms to allow expiration
 try {
 Thread.sleep(2);
 } catch (InterruptedException e) {
 // Handle interruption
 }
 assertEquals("Hello", memoizedSupplier.get());
 }

 @Test
 public void testMemoizeWithExpirationLongDuration() {
 Supplier<String> delegate = new Supplier<String>() {
 @Override
 public String get() {
 return "Hello";
 }
 };
 Supplier<String> memoizedSupplier = Suppliers.memoizeWithExpiration(delegate, 100, TimeUnit.MILLISECONDS);
 assertEquals("Hello", memoizedSupplier.get());
 assertEquals("Hello", memoizedSupplier.get());
 // Sleep for 150ms to allow expiration
 try {
 Thread.sleep(150);
 } catch (InterruptedException e) {
 // Handle interruption
 }
 assertEquals("Hello", memoizedSupplier.get());
 // Sleep for another 100ms to allow new value to be computed
 try {
 Thread.sleep(100);
 } catch (InterruptedException e) {
 // Handle interruption
 }
 assertEquals("Hello", memoizedSupplier.get());
 // New value should now be computed and cached
 assertEquals("New Hello", memoizedSupplier.get());
 }

 @Test(expected = NullPointerException.class)
 public void testMemoizeNullDelegate() {
 Supplier<String> nullDelegate = null;
 try {
 Suppliers.memoize(nullDelegate);
 } catch (NullPointerException e) {
 // Expected exception
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testMemoizeNegativeDuration() {
 Supplier<String> delegate = new Supplier<String>() {
 @Override
 public String get() {
 return "Hello";
 }
 };
 try {
 Suppliers.memoizeWithExpiration(delegate, -1, TimeUnit.MILLISECONDS);
 } catch (IllegalArgumentException e) {
 // Expected exception
 }
 }
}