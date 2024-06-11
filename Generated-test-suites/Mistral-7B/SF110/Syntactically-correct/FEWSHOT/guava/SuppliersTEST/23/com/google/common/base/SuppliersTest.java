package com.google.common.base;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import com.google.common.base.Function;
import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import org.junit.Test;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class SuppliersTest {

    @Test
    public void testCompose() {
        Function<Integer, Integer> addOne = i -> i + 1;
        Supplier<Integer> supplierOfFive = Suppliers.ofInstance(5);

        Supplier<Integer> composedSupplier = Suppliers.compose(addOne, supplierOfFive);

        assertEquals(6, composedSupplier.get());
    }

    @Test
    public void testComposeWithNullFunction() {
        assertThrows(NullPointerException.class, () -> Suppliers.compose(null, Suppliers.ofInstance(1)));
    }

    @Test
    public void testComposeWithNullSupplier() {
        Function<Integer, Integer> addOne = i -> i + 1;

        assertThrows(NullPointerException.class, () -> Suppliers.compose(addOne, null));
    }

    @Test
    public void testMemoize() {
        Supplier<Integer> supplier = () -> 1;
        Supplier<Integer> memoizedSupplier = Suppliers.memoize(supplier);

        assertEquals(1, memoizedSupplier.get());
        assertEquals(1, memoizedSupplier.get());
    }

    @Test
    public void testMemoizeWithNullSupplier() {
        assertThrows(NullPointerException.class, () -> Suppliers.memoize(null));
    }

    @Test
    public void testMemoizeWithExpiration() {
        Supplier<Long> supplier = () -> System.currentTimeMillis();
        Supplier<Long> expiringMemoizingSupplier = Suppliers.memoizeWithExpiration(supplier, 1, TimeUnit.SECONDS);

        long currentTimeMillis = System.currentTimeMillis();
        long firstValue = expiringMemoizingSupplier.get();

        assertTrue(currentTimeMillis > firstValue);

        long secondValue = expiringMemoizingSupplier.get();

        assertEquals(currentTimeMillis, secondValue);
    }

    @Test
    public void testMemoizeWithNegativeExpiration() {
        assertThrows(IllegalArgumentException.class, () -> Suppliers.memoizeWithExpiration(Suppliers.ofInstance(1), -1, TimeUnit.MILLISECONDS));
    }

    @Test
    public void testMemoizeWithZeroExpiration() {
        Supplier<String> supplier = () -> "hello";

        Supplier<String> memoizedSupplier = Suppliers.memoize(supplier);

        assertEquals("hello", memoizedSupplier.get());
        assertEquals("hello", memoizedSupplier.get());
    }

    @Test
    public void testOfInstance() {
        Supplier<String> supplier = Suppliers.ofInstance("hello");

        assertEquals("hello", supplier.get());
        assertEquals("hello", supplier.get());
    }

    @Test
    public void testOfInstanceWithNull() {
        assertThrows(NullPointerException.class, () -> Suppliers.ofInstance(null));
    }

    @Test
    public void testSynchronizedSupplier() {
        Supplier<String> supplier = () -> "hello";
        Supplier<String> synchronizedSupplier = Suppliers.synchronizedSupplier(supplier);

        String thread1 = synchronizedSupplier.get();
        String thread2 = synchronizedSupplier.get();

        assertSame(thread1, thread2);
    }

    @Test
    public void testSupplierFunction() {
        Function<Supplier<Integer>, Integer> supplierFunction = Suppliers.supplierFunction();

        Supplier<Integer> supplierOfFive = Suppliers.ofInstance(5);

        assertEquals(5, supplierFunction.apply(supplierOfFive).intValue());
    }

    @Test
    public void testSupplierFunctionWithNullSupplier() {
        Function<Supplier<Integer>, Integer> supplierFunction = Suppliers.supplierFunction();

        assertThrows(NullPointerException.class, () -> supplierFunction.apply(null));
    }
}