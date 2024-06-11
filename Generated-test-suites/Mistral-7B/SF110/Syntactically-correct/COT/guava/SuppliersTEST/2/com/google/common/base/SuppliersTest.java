package com.google.common.base;

import static org.junit.Assert.*;

import org.junit.Test;

import java.util.concurrent.*;

public class SuppliersTest {

    @SuppressWarnings("unchecked")
    @Test
    public <F, T> void testCompose() {
        Function<F, T> function = (Function<F, T>) (f -> (T) (Long.valueOf(f.hashCode() * 2)));
        Supplier<F> supplier = Suppliers.ofInstance(new Object());
        Supplier<T> supplierComposition = Suppliers.compose(function, supplier);

        T result = supplierComposition.get();
        Long expected = new Long(supplier.hashCode() * 2);
        assertEquals(expected, result);
    }

    @Test(expected = NullPointerException.class)
    public void testComposeWithNullFunction() {
        Supplier<Object> supplier = Suppliers.ofInstance(new Object());
        Function<? super Object, ?> nullFunction = null;

        try {
            Suppliers.compose(nullFunction, supplier);
        } catch (NullPointerException e) {
            fail("Expected NullPointerException, but got: " + e.getMessage());
        }
    }

    @Test(expected = NullPointerException.class)
    public void testComposeWithNullSupplier() {
        Function<Object, Object> function = (Function<Object, Object>) (o -> o);

        try {
            Suppliers.compose(function, null);
        } catch (NullPointerException e) {
            fail("Expected NullPointerException, but got: " + e.getMessage());
        }
    }

    @Test
    public <T> void testMemoize() {
        Supplier<String> delegate = () -> "Hello";
        Supplier<String> memoizedSupplier = Suppliers.memoize(delegate);

        String result1 = memoizedSupplier.get();
        String result2 = memoizedSupplier.get();

        assertEquals(result1, result2);
    }

    @Test
    public <T> void testMemoizeWithDifferentInputs() {
        Supplier<String> delegate = () -> "Hello";
        Supplier<String> memoizedSupplier = Suppliers.memoize(delegate);

        String result1 = memoizedSupplier.get();
        String result2 = memoizedSupplier.get();
        String result3 = memoizedSupplier.get();

        assertNotSame(result1, result2);
        assertNotSame(result1, result3);
        assertEquals(result1, result3);
    }

    @Test
    public <T> void testMemoizeWithExpirationZero() {
        Supplier<Long> delegate = () -> System.currentTimeMillis();
        Supplier<Long> expiringMemoizingSupplier = Suppliers.memoizeWithExpiration(delegate, 0, TimeUnit.MILLISECONDS);

        long result1 = expiringMemoizingSupplier.get();
        long result2 = expiringMemoizingSupplier.get();

        assertEquals(result1, result2);
    }

    @Test
    public <T> void testMemoizeWithExpirationPositive() {
        Supplier<Long> delegate = () -> System.currentTimeMillis();
        long expirationTime = System.currentTimeMillis() + 100;
        Supplier<Long> expiringMemoizingSupplier = Suppliers.memoizeWithExpiration(delegate, 100, TimeUnit.MILLISECONDS);

        long result1 = expiringMemoizingSupplier.get();
        long result2 = expiringMemoizingSupplier.get();

        assertNotSame(result1, result2);

        long waitTime = 50;
        try {
            Thread.sleep(waitTime);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        long result3 = expiringMemoizingSupplier.get();
        assertEquals(result1, result3);

        try {
            Thread.sleep(waitTime + 50);
            long result4 = expiringMemoizingSupplier.get();
            assertEquals(result3, result4);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @Test(expected = NullPointerException.class)
    public void testMemoizeWithNullDelegate() {
        Suppliers.memoizeWithExpiration(null, 0, TimeUnit.MILLISECONDS);
    }

    @Test
    public <T> void testOfInstanceNullInstance() {
        Supplier<Object> supplierOfInstance = Suppliers.ofInstance(null);

        assertNull(supplierOfInstance.get());
    }

    @Test
    public <T> void testOfInstanceNotNullInstance() {
        Object instance = new Object();
        Supplier<Object> supplierOfInstance = Suppliers.ofInstance(instance);

        assertEquals(instance, supplierOfInstance.get());
    }

    @Test(expected = NullPointerException.class)
    public void testSynchronizedSupplierWithNullDelegate() {
        Suppliers.synchronizedSupplier(null);
    }
}