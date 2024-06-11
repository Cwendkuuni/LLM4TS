package com.google.common.base;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.*;

import java.util.concurrent.*;
import javax.annotation.*;
import java.io.*;
import com.google.common.annotations.*;
import org.junit.*;
import org.mockito.*;

@GwtCompatible
public final class SuppliersTest {

 @Before
 public void setup() {
 Suppliers.checkNotNull(any());
 }

 @Test
 public void testCompose() {
 Function<Object, Integer> function = arg -> arg.hashCode();
 Supplier<Object> supplier = mock(Supplier.class);
 when(supplier.get()).thenReturn(new Object());

 Supplier<Integer> composedSupplier = Suppliers.compose(function, supplier);
 assertEquals(new Object().hashCode(), composedSupplier.get());

 verify(supplier).get();
 }

 @Test
 public void testMemoize() {
 Supplier<String> supplier = () -> "hello";
 Supplier<String> memoizedSupplier = Suppliers.memoize(supplier);
 assertEquals("hello", memoizedSupplier.get());
 assertEquals("hello", memoizedSupplier.get());
 }

 @Test
 public void testMemoizeWithExpiration() {
 Supplier<String> supplier = () -> "hello";
 Supplier<String> memoizedSupplier = Suppliers.memoizeWithExpiration(supplier, 1, TimeUnit.MILLISECONDS);
 assertEquals("hello", memoizedSupplier.get());
 assertEquals("hello", memoizedSupplier.get());

 try {
 Thread.sleep(2);
 } catch (InterruptedException e) {
 // InterruptedException handling
 }

 assertEquals("hello", memoizedSupplier.get());
 assertNotSame("hello", memoizedSupplier.get());
 }

 @Test
 public void testOfInstance() {
 Supplier<String> supplier = Suppliers.ofInstance("hello");
 assertEquals("hello", supplier.get());
 }

 @Test
 public void testSynchronizedSupplier() {
 Supplier<String> supplier = () -> "hello";
 Supplier<String> synchronizedSupplier = Suppliers.synchronizedSupplier(supplier);

 Thread thread1 = new Thread(() -> {
 try {
 Thread.sleep(10);
 } catch (InterruptedException e) {
 // InterruptedException handling
 }

 assertEquals("hello", synchronizedSupplier.get());
 });

 Thread thread2 = new Thread(() -> {
 assertEquals("hello", synchronizedSupplier.get());
 });

 thread1.start();
 thread2.start();

 try {
 thread1.join();
 thread2.join();
 } catch (InterruptedException e) {
 // InterruptedException handling
 }
 }

 @Test
 public void testSupplierFunction() {
 Function<Supplier<Object>, Object> supplierFunction = Suppliers.supplierFunction();
 assertEquals(new Object(), supplierFunction.apply(new ObjectSupplier<>()));
 }
}

 class ObjectSupplier<T> implements Supplier<T> {
 Object get() {
 return new Object();
 }
 }