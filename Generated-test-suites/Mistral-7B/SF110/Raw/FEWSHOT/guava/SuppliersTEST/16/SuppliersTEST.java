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
public final class SuppliersTest {

 @Before
 public void setup() {
 mockStatic(Suppliers.class);
 }

 @After
 public void teardown() {
 reset(Suppliers.class);
 }

 @Test
 public void testCompose() {
     // Given
     Function<Object, Integer> function = arg -> (Integer) (int) arg + 1;
     Supplier<Object> supplier = mock(Supplier.class);
     when(supplier.get()).thenReturn(new Integer(42));

     // When
     Supplier<Integer> composedSupplier = Suppliers.compose(function, supplier);

     // Then
     assertEquals(43, composedSupplier.get());
     verify(supplier).get();
 }

 @Test
 public void testMemoize() {
     // Given
     Supplier<String> delegate = () -> "constant value";

     // When
     Supplier<String> memoizedSupplier = Suppliers.memoize(delegate);

     // Then
     assertEquals("constant value", memoizedSupplier.get());
     assertEquals("constant value", memoizedSupplier.get());
 }

 @Test
 public void testMemoizeWithExpiration() {
     // Given
     Supplier<String> delegate = () -> "expiring value";
     long duration = 10;
     TimeUnit unit = TimeUnit.MILLISECONDS;

     // When
     Supplier<String> expiringMemoizedSupplier = Suppliers.memoizeWithExpiration(delegate, duration, unit);

     // Then
     assertEquals("expiring value", expiringMemoizedSupplier.get());
     assertEquals("expiring value", expiringMemoizedSupplier.get());

     try {
         Thread.sleep(duration + 1);
     } catch (InterruptedException e) {
         e.printStackTrace();
     }

     assertEquals("expiring value", expiringMemoizedSupplier.get());
     assertNotEquals("expiring value", expiringMemoizedSupplier.get());
 }

 @Test
 public void testOfInstance() {
     // Given
     String instance = "instance value";

     // When
     Supplier<String> supplier = Suppliers.ofInstance(instance);

     // Then
     assertEquals(instance, supplier.get());
 }

 @Test
 public void testSynchronizedSupplier() {
     // Given
     Supplier<String> delegate = () -> "synchronized value";

     // When
     Supplier<String> synchronizedSupplier = Suppliers.synchronizedSupplier(delegate);

     // Then
     assertEquals("synchronized value", synchronizedSupplier.get());
     assertEquals("synchronized value", synchronizedSupplier.get());
 }

 @Test
 public void testSupplierFunction() {
     // Given
     Function<Supplier<Object>, Object> expectedFunction = arg -> arg.get();
     Supplier<Object> supplier = mock(Supplier.class);

     // When
     Function<Supplier<Object>, Object> supplierFunction = Suppliers.supplierFunction();

     // Then
     assertEquals(expectedFunction, supplierFunction);
     assertEquals("Suppliers.supplierFunction()", supplierFunction.toString());
 }

 @Test
 public void testSupplierComposition() {
     // Given
     Function<Object, Integer> function = arg -> (Integer) (int) arg + 1;
     Supplier<Object> supplier = mock(Supplier.class);
     when(supplier.get()).thenReturn(new Integer(42));

     // When
     Supplier<Integer> composedSupplier = new Suppliers.SupplierComposition<>(function, supplier);

     // Then
     assertEquals(43, composedSupplier.get());
     verify(supplier).get();
 }

 @Test
 public void testMemoizingSupplier() {
     // Given
     Supplier<String> delegate = () -> "memoized value";

     // When
     Suppliers.MemoizingSupplier<String> memoizingSupplier = new Suppliers.MemoizingSupplier<>(delegate);

     // Then
     assertEquals("memoized value", memoizingSupplier.get());
     assertEquals("memoized value", memoizingSupplier.get());
 }

 @Test
 public void testExpiringMemoizingSupplier() {
     // Given
     Supplier<String> delegate = () -> "expiring memoized value";
     long duration = 10;
     TimeUnit unit = TimeUnit.MILLISECONDS;

     // When
     Suppliers.ExpiringMemoizingSupplier<String> expiringMemoizingSupplier = new Suppliers.ExpiringMemoizingSupplier<>(delegate, duration, unit);

     // Then
     assertEquals("expiring memoized value", expiringMemoizingSupplier.get());
     assertEquals("expiring memoized value", expiringMemoizingSupplier.get());

     try {
         Thread.sleep(duration + 1);
     } catch (InterruptedException e) {
         e.printStackTrace();
     }

     assertEquals("expiring memoized value", expiringMemoizingSupplier.get());
     assertNotEquals("expiring memoized value", expiringMemoizingSupplier.get());
 }
}