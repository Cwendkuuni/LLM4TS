package com.google.common.base;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.concurrent.*;
import javax.annotation.*;
import java.io.*;
import com.google.common.annotations.*;
import org.junit.*;
import org.mockito.MockitoAnnotations;

@GwtCompatible
public class SuppliersTest {

 @Before
 public void setupMocks() {
 MockitoAnnotations.initMocks(this);
 }

 @Test
 public void testCompose() {
 Function<Integer, Integer> function = arg -> arg * 2;
 Supplier<Integer> supplier = mock(Supplier.class);
 when(supplier.get()).thenReturn(1);

 Supplier<Integer> composedSupplier = Suppliers.compose(function, supplier);
 assertEquals(2, composedSupplier.get());

 verify(supplier).get();
 }

 @Test
 public void testMemoize() {
 Supplier<Integer> delegate = mock(Supplier.class);
 when(delegate.get()).thenReturn(1);

 Supplier<Integer> memoizedSupplier = Suppliers.memoize(delegate);
 assertEquals(1, memoizedSupplier.get());
 assertEquals(1, memoizedSupplier.get());
 }

 @Test
 public void testMemoizeWithExpiration() {
 Supplier<Integer> delegate = mock(Supplier.class);
 when(delegate.get()).thenReturn(1);

 Supplier<Integer> expiringMemoizedSupplier = Suppliers.memoizeWithExpiration(delegate, 1, TimeUnit.SECONDS);
 assertEquals(1, expiringMemoizedSupplier.get());
 assertEquals(1, expiringMemoizedSupplier.get());

 Thread.currentThread().sleep(2000); // Wait for 2 seconds
 assertEquals(1, expiringMemoizedSupplier.get());
 }

 @Test
 public void testOfInstance() {
 Supplier<String> supplierOfInstance = Suppliers.ofInstance("test");
 assertEquals("test", supplierOfInstance.get());
 }

 @Test
 public void testSynchronizedSupplier() {
 Supplier<Integer> delegate = mock(Supplier.class);
 when(delegate.get()).thenReturn(1);

 Supplier<Integer> synchronizedSupplier = Suppliers.synchronizedSupplier(delegate);
 assertEquals(1, synchronizedSupplier.get());
 assertEquals(1, synchronizedSupplier.get());
 }
}