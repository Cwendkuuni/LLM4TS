package com.google.common.base.test;

import com.google.common.base.Function;
import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.concurrent.*;

public class SuppliersTest {

 @Test
 public void testCompose() {
 Supplier<String> supplier1 = () -> "Hello";
 Supplier<String> supplier2 = () -> supplier1.get() + " World!";
 Supplier<String> composedSupplier = Suppliers.compose(supplier2, supplier1);
 assertEquals("Hello World!", composedSupplier.get());
 }

 @Test(expected = NullPointerException.class)
 public void testComposeWithNullFunction() {
 Function<Supplier<String>, String> function = null;
 Supplier<String> supplier = () -> "World!";
 try {
 Suppliers.compose(function, supplier);
 } catch (NullPointerException e) {
 // Expected exception
 }
 }

 @Test(expected = NullPointerException.class)
 public void testComposeWithNullSupplier() {
 Supplier<String> supplier = null;
 Function<Supplier<String>, String> function = (Supplier<String> input) -> input.get() + " World!";
 try {
 Suppliers.compose(function, supplier);
 } catch (NullPointerException e) {
 // Expected exception
 }
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
 Supplier<Long> supplier = () -> System.nanoTime();
 Supplier<Long> expiringMemoizingSupplier = Suppliers.memoizeWithExpiration(supplier, 10, TimeUnit.MILLISECONDS);
 long previousTime = 0;
 for (int i = 0; i < 10; i++) {
 long currentTime = expiringMemoizingSupplier.get();
 assertTrue(currentTime > previousTime);
 previousTime = currentTime;
 }
 }

 @Test(expected = NullPointerException.class)
 public void testMemoizeWithNullDelegate() {
 Supplier<Integer> delegate = null;
 try {
 Suppliers.memoize(delegate);
 } catch (NullPointerException e) {
 // Expected exception
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testMemoizeWithNegativeDuration() {
 Supplier<Integer> supplier = () -> 42;
 try {
 Suppliers.memoizeWithExpiration(supplier, -1, TimeUnit.MILLISECONDS);
 } catch (IllegalArgumentException e) {
 // Expected exception
 }
 }
}