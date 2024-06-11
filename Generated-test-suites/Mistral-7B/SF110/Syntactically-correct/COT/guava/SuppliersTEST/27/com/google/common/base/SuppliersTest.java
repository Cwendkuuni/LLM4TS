package com.google.common.base.test;

import static org.junit.Assert.*;

import java.util.concurrent.*;
import org.junit.*;

import com.google.common.base.*;
import com.google.common.annotations.*;

@GwtIncompatible // since JUnit is not GWT compatible
public class SuppliersTest {

    @SuppressWarnings("unchecked")
    @Test
    public <F, T> void testCompose() {
        Function<F, T> function = arg -> (T)arg;
        Supplier<F> supplier = Suppliers.ofInstance(new Object());
        Supplier<T> composedSupplier = Suppliers.compose(function, supplier);
        assertEquals(supplier.get(), composedSupplier.get());
    }

    @Test
    public <T> void testMemoize() {
        Supplier<String> supplier = () -> "Hello";
        Supplier<String> memoizedSupplier = Suppliers.memoize(supplier);
        assertEquals(memoizedSupplier.get(), memoizedSupplier.get());
    }

    @Test(expected = UnsupportedOperationException.class)
    public <T> void testMemoizeWithExpirationNullDelegate() {
        Suppliers.memoizeWithExpiration(null, 1, TimeUnit.NANOS);
    }

    @Test(expected = IllegalArgumentException.class)
    public <T> void testMemoizeWithExpirationNegativeDuration() {
        Supplier<String> supplier = () -> "Hello";
        Suppliers.memoizeWithExpiration(supplier, -1, TimeUnit.NANOS);
    }

    @Test
    public <T> void testMemoizeWithExpiration() {
        Supplier<String> supplier = () -> "Hello";
        Supplier<String> memoizedSupplier = Suppliers.memoizeWithExpiration(supplier, 1, TimeUnit.NANOS);
        assertEquals(memoizedSupplier.get(), memoizedSupplier.get());

        long start = System.nanoTime();
        memoizedSupplier.get();
        long end = System.nanoTime();
        long elapsed = end - start;
        assertTrue(elapsed < 1);

        start = System.nanoTime();
        memoizedSupplier.get();
        end = System.nanoTime();
        elapsed = end - start;
        assertTrue(elapsed > 0);
    }

    @Test
    public <T> void testOfInstanceNullInstance() {
        assertEquals(Suppliers.ofInstance(null), Suppliers.ofInstance(null));
    }

    @Test
    public <T> void testOfInstance() {
        Object instance = new Object();
        Supplier<T> supplier = Suppliers.ofInstance(instance);
        assertEquals(instance, supplier.get());
    }

    @Test
    public <T> void testSynchronizedSupplierNullDelegate() {
        Supplier<String> supplier = null;
        Supplier<String> synchronizedSupplier = Suppliers.synchronizedSupplier(supplier);
        assertNull(synchronizedSupplier.get());
    }

    @Test
    public void testSupplierFunction() {
        Supplier<String> supplier = () -> "Hello";
        Function<Supplier<String>, String> supplierFunction = Suppliers.supplierFunction();
        assertEquals(supplierFunction.apply(supplier), supplier.get());
    }
}