package com.google.common.base;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.concurrent.*;
import javax.annotation.*;
import java.io.*;
import com.google.common.annotations.*;
import org.junit.*;
import org.mockito.*;

@GwtCompatible
public class SuppliersTest {

 @Before
 public void setUp() {
 final Supplier<String> stringSupplier = mock(Supplier.class);
 final Function<Supplier<String>, String> function = mock(Function.class);
 }

 @Test
 public void testCompose() {
 final String result = "expectedResult";
 final Function<Supplier<String>, String> function = mock(Function.class);
 when(function.apply(any(Supplier.class))).thenReturn(result);
 final Supplier<String> supplier = Suppliers.compose(function, Suppliers.ofInstance("input"));
 assertEquals(result, supplier.get());
 verify(function).apply(eq(Suppliers.ofInstance("input")));
 }

 @Test
 public void testMemoize() {
 final Supplier<String> delegate = mock(Supplier.class);
 final Supplier<String> memoized = Suppliers.memoize(delegate);
 assertSame(delegate, memoized);
 }

 @Test
 public void testMemoizeWithExpiration() {
 final Supplier<String> delegate = mock(Supplier.class);
 final long duration = 10;
 final TimeUnit unit = TimeUnit.MILLISECONDS;
 final Supplier<String> memoizedWithExpiration = Suppliers.memoizeWithExpiration(delegate, duration, unit);
 verify(delegate, never()).get();
 }

 @Test
 public void testMemoizeWithExpiration_get() {
 final Supplier<String> delegate = mock(Supplier.class);
 final long duration = 10;
 final TimeUnit unit = TimeUnit.MILLISECONDS;
 final Supplier<String> memoizedWithExpiration = Suppliers.memoizeWithExpiration(delegate, duration, unit);
 final String result = "expectedResult";
 when(delegate.get()).thenReturn(result);
 final String firstResult = memoizedWithExpiration.get();
 assertEquals(result, firstResult);
 final long expirationNanos = unit.toNanos(duration);
 final long now = System.nanoTime();
 final long expirationTime = now + expirationNanos;
 final String secondResult = memoizedWithExpiration.get();
 assertEquals(result, secondResult);
 final long currentTime = System.nanoTime();
 assertTrue(currentTime > expirationTime);
 final String thirdResult = memoizedWithExpiration.get();
 assertNotEquals(result, thirdResult);
 }

 @Test
 public void testOfInstance() {
 final String expected = "expected";
 final Supplier<String> supplier = Suppliers.ofInstance(expected);
 assertEquals(expected, supplier.get());
 }

 @Test
 public void testSynchronizedSupplier() {
 final Supplier<String> delegate = mock(Supplier.class);
 final Supplier<String> synchronizedSupplier = Suppliers.synchronizedSupplier(delegate);
 verify(delegate, times(1)).get();
 }

 @Test
 public void testSupplierFunction() {
 final Function<Supplier<Object>, Object> supplierFunction = Suppliers.supplierFunction();
 final Supplier<Object> supplier = mock(Supplier.class);
 final Object result = "expectedResult";
 when(supplier.get()).thenReturn(result);
 final Object actualResult = supplierFunction.apply(supplier);
 assertEquals(result, actualResult);
 }
}