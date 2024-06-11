package com.google.common.base.test;

import com.google.common.base.Function;
import com.google.common.base.Supplier;
import com.google.common.base.Preconditions;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

public class SuppliersTest {

 private Function<Integer, Integer> addOne;
 private Supplier<Integer> constantSupplier;
 private Supplier<Integer> memoizedSupplier;
 private Supplier<Integer> memoizedWithExpirationSupplier;
 private Supplier<Integer> supplierOfInstance;
 private Supplier<Integer> synchronizedSupplier;
 private Supplier<Integer> supplierFunction;

 @Before
 public void setup() {
 addOne = (x) -> x + 1;
 constantSupplier = Suppliers.ofInstance(1);
 memoizedSupplier = Suppliers.memoize(constantSupplier);
 memoizedWithExpirationSupplier = Suppliers.memoizeWithExpiration(memoizedSupplier, 10, TimeUnit.MILLISECONDS);
 supplierOfInstance = Suppliers.ofInstance(2);
 synchronizedSupplier = Suppliers.synchronizedSupplier(supplierOfInstance);
 supplierFunction = Suppliers.supplierFunction();
 }

 @Test
 public void testCompose() {
 final Supplier<Integer> supplier = Suppliers.ofInstance(1);
 final Function<Integer, Integer> function = (x) -> x * x;

 Supplier<Integer> composedSupplier = Suppliers.compose(function, supplier);

 assertEquals(1, composedSupplier.get().intValue());
 assertEquals(1, supplier.get().intValue());

 composedSupplier = Suppliers.compose(function, composedSupplier);

 assertEquals(4, composedSupplier.get().intValue());
 assertEquals(1, supplier.get().intValue());
 }

 @Test
 public void testMemoize() {
 memoizedSupplier.get();
 memoizedSupplier.get();

 assertEquals(1, memoizedSupplier.get().intValue());
 assertEquals(1, constantSupplier.get().intValue());
 }

 @Test
 public void testMemoizeWithExpiration() {
 memoizedWithExpirationSupplier.get();
 memoizedWithExpirationSupplier.get();

 assertEquals(1, memoizedWithExpirationSupplier.get().intValue());
 assertEquals(1, memoizedSupplier.get().intValue());

 try {
 memoizedWithExpirationSupplier.get(); // should throw an Exception
 } catch (Exception e) {
 assertEquals("Expired", e.getMessage());
 }
 }

 @Test
 public void testOfInstance() {
 assertNotNull(supplierOfInstance.get());
 assertEquals(2, supplierOfInstance.get().intValue());
 }

 @Test
 public void testSynchronizedSupplier() {
 final Supplier<Integer> supplier = Suppliers.ofInstance(1);
 final Thread thread = new Thread(() -> synchronizedSupplier.get());
 thread.start();

 try {
 thread.join();
 } catch (InterruptedException e) {
 // ignore
 }

 assertEquals(2, synchronizedSupplier.get().intValue());
 assertEquals(1, supplier.get().intValue());
 }

 @Test
 public void testSupplierFunction() {
 assertNotNull(supplierFunction.apply(supplierOfInstance));
 assertEquals(2, supplierFunction.apply(supplierOfInstance).intValue());
 }

 @Test(expected = NullPointerException.class)
 public void testNullFunction() {
 assertThrows(NullPointerException.class, () -> {
 Suppliers.compose(null, constantSupplier);
 });
 }

 @Test(expected = NullPointerException.class)
 public void testNullSupplier() {
 assertThrows(NullPointerException.class, () -> {
 Suppliers.compose(addOne, null);
 });
 }
}