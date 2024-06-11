package com.google.common.base;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.google.common.base.Supplier;
import org.junit.Test;

public class SuppliersTest {

    @Test
    public void testCompose() {
        Function<Integer, String> function = argument -> argument.toString();
        Supplier<Integer> supplier = mock(Supplier.class);
        when(supplier.get()).thenReturn(42);

        Supplier<String> composedSupplier = Suppliers.compose(function, supplier);

        assertEquals("42", composedSupplier.get());
        verify(supplier).get();
    }

    @Test
    public void testMemoize() {
        Supplier<Integer> supplier = () -> 42;
        Supplier<Integer> memoizedSupplier = Suppliers.memoize(supplier);

        assertEquals(42, memoizedSupplier.get());
        assertEquals(42, memoizedSupplier.get());
    }

    @Test
    public void testMemoizeWithExpiration() {
        Supplier<Long> supplier = () -> System.currentTimeMillis();
        Supplier<Long> memoizedSupplier = Suppliers.memoizeWithExpiration(supplier, 100, TimeUnit.MILLISECONDS);

        long initialTime = memoizedSupplier.get();
        long newTime = System.currentTimeMillis();

        assertEquals(initialTime, memoizedSupplier.get());
        assertEquals(initialTime, memoizedSupplier.get());

        Thread.sleep(150);

        assertEquals(newTime, memoizedSupplier.get());
    }

    @Test
    public void testOfInstance() {
        String instance = "instance";
        Supplier<String> supplier = Suppliers.ofInstance(instance);

        assertEquals(instance, supplier.get());
        assertEquals(instance, supplier.get());
    }

    @Test
    public void testSynchronizedSupplier() {
        Supplier<Long> supplier = () -> System.currentTimeMillis();
        Supplier<Long> synchronizedSupplier = Suppliers.synchronizedSupplier(supplier);

        long firstCall = synchronizedSupplier.get();
        long secondCall = synchronizedSupplier.get();

        assertEquals(firstCall, secondCall);
    }
}