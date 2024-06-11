package com.example.junit;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.concurrent.Supplier;

import org.junit.Test;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.base.Suppliers;

public class SuppliersTest {

    // Test for compose method
    @Test
    public void testCompose() {
        Function<Integer, Integer> addOne = new Function<Integer, Integer>() {
            @Override
            public Integer apply(Integer input) {
                return input + 1;
            }
        };

        Supplier<Integer> supplierOfOne = Suppliers.ofInstance(1);
        Supplier<Integer> composedSupplier = Suppliers.compose(addOne, supplierOfOne);

        assertEquals(2, composedSupplier.get());
    }

    // Test for memoize method with normal functionality
    @Test
    public void testMemoizeNormal() {
        Supplier<String> supplier = () -> "hello";
        Supplier<String> memoizedSupplier = Suppliers.memoize(supplier);

        assertEquals("hello", memoizedSupplier.get());
        assertEquals("hello", memoizedSupplier.get());
    }

    // Test for memoize method with different results for same input
    @Test
    public void testMemoizeDifferentResults() {
        Supplier<String> supplier = () -> "hello";
        Supplier<String> memoizedSupplier = Suppliers.memoize(supplier);

        when(supplier.get()).thenReturn("world");

        assertEquals("hello", memoizedSupplier.get());
        assertEquals("hello", memoizedSupplier.get());
        assertEquals("world", memoizedSupplier.get());
    }

    // Test for memoizeWithExpiration method with normal functionality
    @Test
    public void testMemoizeWithExpirationNormal() {
        Supplier<String> supplier = () -> "hello";
        Supplier<String> memoizedSupplier = Suppliers.memoizeWithExpiration(supplier, 1000, TimeUnit.MILLISECONDS);

        assertEquals("hello", memoizedSupplier.get());
        assertEquals("hello", memoizedSupplier.get());
        assertEquals("hello", memoizedSupplier.get());

        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        assertEquals("hello", memoizedSupplier.get());
    }

    // Test for memoizeWithExpiration method with expired result
    @Test(expected = IllegalArgumentException.class)
    public void testMemoizeWithExpirationExpired() {
        Supplier<String> supplier = () -> "hello";
        Supplier<String> memoizedSupplier = Suppliers.memoizeWithExpiration(supplier, 1, TimeUnit.MILLISECONDS);

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        memoizedSupplier.get();
    }

    // Test for ofInstance method with null instance
    @Test(expected = NullPointerException.class)
    public void testOfInstanceNullInstance() {
        Suppliers.ofInstance(null);
    }

    // Test for ofInstance method with valid instance
    @Test
    public void testOfInstanceValidInstance() {
        String instance = "hello";
        Supplier<String> supplier = Suppliers.ofInstance(instance);

        assertEquals(instance, supplier.get());
        assertEquals(instance, supplier.get());
    }

    // Test for synchronizedSupplier method with normal functionality
    @Test
    public void testSynchronizedSupplierNormal() {
        Supplier<String> supplier = () -> "hello";
        Supplier<String> synchronizedSupplier = Suppliers.synchronizedSupplier(supplier);

        new Thread(() -> synchronizedSupplier.get()).start();
        new Thread(() -> synchronizedSupplier.get()).start();

        assertEquals("hello", synchronizedSupplier.get());
        assertEquals("hello", synchronizedSupplier.get());
    }

}