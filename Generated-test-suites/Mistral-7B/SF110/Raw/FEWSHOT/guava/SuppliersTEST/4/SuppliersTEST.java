package com.google.common.base;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.concurrent.*;
import javax.annotation.*;
import java.io.*;
import com.google.common.annotations.*;
import org.junit.Test;
import org.mockito.Mockito;

@GwtCompatible
public final class SuppliersTest {

 @Test
 public void testCompose() {
 Supplier<String> supplier = mock(Supplier.class);
 Function<String, Integer> function = arg -> arg.length();
 Supplier<String> composedSupplier = Suppliers.compose(function, supplier);

 verify(supplier, times(1)).get();
 String result = composedSupplier.get();
 assertEquals(5, result.length());
 }

 @Test
 public void testMemoize() {
 Supplier<String> supplier = () -> "Hello";
 Supplier<String> memoizedSupplier = Suppliers.memoize(supplier);

 assertEquals("Hello", memoizedSupplier.get());
 assertEquals("Hello", memoizedSupplier.get());
 }

 @Test
 public void testMemoizeWithExpiration() {
 Supplier<String> supplier = () -> "Hello";
 Supplier<String> memSupplier = Suppliers.memoizeWithExpiration(supplier, 100, TimeUnit.MILLISECONDS);

 assertEquals("Hello", memSupplier.get());
 assertEquals("Hello", memSupplier.get());
 try {
 Thread.sleep(200);
 assertEquals("Hello", memSupplier.get());
 } catch (InterruptedException e) {
 throw new RuntimeException(e);
 }

 assertEquals("Hello", memSupplier.get()); // Should be a new instance
 }

 @Test
 public void testOfInstance() {
 Supplier<String> supplier = Suppliers.ofInstance("World");
 assertEquals("World", supplier.get());
 }

 @Test
 public void testSynchronizedSupplier() {
 final AtomicInteger counter = new AtomicInteger(0);
 Supplier<String> supplier = () -> Integer.toString(counter.incrementAndGet());
 Supplier<String> synchronizedSupplier = Suppliers.synchronizedSupplier(supplier);

 for (int i = 0; i < 10; i++) {
 Thread thread = new Thread(() -> assertEquals(String.valueOf(i + 1), synchronizedSupplier.get()));
 thread.start();
 }

 for (int i = 0; i < 10; i++) {
 try {
 thread.join();
 } catch (InterruptedException e) {
 throw new RuntimeException(e);
 }
 }
 }

 @Test
 public void testSupplierFunction() {
 Supplier<String> supplier = Suppliers.ofInstance("Test");
 Function<Supplier<String>, String> function = Suppliers.supplierFunction();

 assertEquals("Test", function.apply(supplier));
 }
}