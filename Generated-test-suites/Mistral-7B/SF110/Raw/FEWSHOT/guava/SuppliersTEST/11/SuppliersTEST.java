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
 final Supplier<String> supplier = mock(Supplier.class);
 final Function<Supplier<String>, String> function = mock(Function.class);
 }

 @Test
 public void testCompose() {
 final Function<Supplier<String>, String> function = args -> args.get().toString();
 final Supplier<String> supplier1 = new SupplierOfInstance<>("Hello");
 final Supplier<String> supplier2 = Suppliers.compose(function, supplier1);
 assertEquals("Hello", supplier2.get());
 }

 @Test
 public void testMemoize() {
 final Supplier<String> supplier = new Suppliers.SupplierOfInstance<>("Hello");
 final Supplier<String> memoizedSupplier = Suppliers.memoize(supplier);
 assertEquals("Hello", memoizedSupplier.get());
 assertEquals("Hello", memoizedSupplier.get());
 }

 @Test
 public void testMemoizeWithExpiration() {
 final Supplier<String> supplier = new Suppliers.SupplierOfInstance<>("Hello");
 final Supplier<String> memoizedSupplier = Suppliers.memoizeWithExpiration(supplier, 100, TimeUnit.MILLISECONDS);
 assertEquals("Hello", memoizedSupplier.get());
 assertEquals("Hello", memoizedSupplier.get());
 try {
 Thread.sleep(150);
 } catch (InterruptedException e) {
 e.printStackTrace();
 }
 assertNotEquals("Hello", memoizedSupplier.get());
 }

 @Test
 public void testOfInstance() {
 final String instance = "Hello";
 final Supplier<String> supplier = Suppliers.ofInstance(instance);
 assertEquals(instance, supplier.get());
 }

 @Test
 public void testSynchronizedSupplier() {
 final Supplier<String> supplier = new Suppliers.SupplierOfInstance<>("Hello");
 final Supplier<String> synchronizedSupplier = Suppliers.synchronizedSupplier(supplier);
 assertEquals("Hello", synchronizedSupplier.get());
 assertEquals("Hello", synchronizedSupplier.get());
 }

 @Test
 public void testCompositionOfFunctionsAndSuppliers() {
 final String result = "Hello World";
 final Function<Supplier<String>, Supplier<String>> composeFunction = args -> new Suppliers.SupplierComposition<>(this::func1, args);
 final Function<Supplier<String>, String> func1 = args -> args.get().toString();
 final Supplier<String> supplier = new Suppliers.SupplierOfInstance<>("Hello");
 final Supplier<String> composedSupplier = composeFunction.apply(supplier);
 assertEquals(result, composedSupplier.get());
 }

 @Test
 public void testMemoizingComposedSupplier() {
 final Function<Supplier<String>, String> func1 = args -> args.get().toString();
 final Function<Supplier<String>, Supplier<String>> composeFunction = args -> new Suppliers.SupplierComposition<>(func1, args);
 final Supplier<String> supplier = new Suppliers.SupplierOfInstance<>("Hello");
 final Supplier<String> memoizedComposedSupplier = Suppliers.memoize(composeFunction.apply(supplier));
 assertEquals("Hello", memoizedComposedSupplier.get());
 assertEquals("Hello", memoizedComposedSupplier.get());
 }

 @After
 public void tearDown() {
 verifyNoMoreInteractions(supplier);
 verifyNoMoreInteractions(function);
 }
}