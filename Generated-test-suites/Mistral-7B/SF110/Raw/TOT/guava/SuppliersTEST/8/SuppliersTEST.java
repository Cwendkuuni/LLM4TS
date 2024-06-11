package com.google.common.base.test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.concurrent.TimeUnit;

import org.junit.Before;
import org.junit.Test;

import com.google.common.base.Function;
import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;

public class SuppliersTest {

    private Supplier<Integer> supplier1;
    private Supplier<Integer> supplier2;
    private Function<Supplier<Integer>, Integer> function;

    @Before
    public void setUp() {
        supplier1 = mock(Supplier.class);
        supplier2 = mock(Supplier.class);
        function = (Supplier<Integer> s) -> s.get() + 1;
    }

    @Test
    public void testCompose() {
        Supplier<Integer> composedSupplier = Suppliers.compose(function, supplier1);
        when(supplier1.get()).thenReturn(1);

        int result = composedSupplier.get();
        assertEquals(2, result);
        verify(supplier1).get();
    }

    @Test
    public void testCompose_NullFunction() {
        try {
            Suppliers.compose(null, supplier1);
            fail();
        } catch (NullPointerException e) {
            // expected
        }
    }

    @Test
    public void testCompose_NullSupplier() {
        try {
            Suppliers.compose(function, null);
            fail();
        } catch (NullPointerException e) {
            // expected
        }
    }

    @Test
    public void testMemoize() {
        Supplier<Integer> delegate = () -> 42;
        Supplier<Integer> memoizedSupplier = Suppliers.memoize(delegate);

        int firstResult = memoizedSupplier.get();
        int secondResult = memoizedSupplier.get();

        assertEquals(firstResult, secondResult);
    }

    @Test
    public void testMemoize_MemoizingSupplier() {
        Supplier<Integer> delegate = mock(Supplier.class);
        Supplier<Integer> memoizedSupplier = Suppliers.memoize(delegate);

        when(delegate.get()).thenReturn(1);
        when(delegate.get()).thenReturn(2);

        int firstResult = memoizedSupplier.get();
        int secondResult = memoizedSupplier.get();

        assertEquals(1, firstResult);
        assertEquals(1, secondResult);
    }

    @Test
    public void testMemoizeWithExpiration() {
        Supplier<Long> delegate = () -> System.nanoTime();
        Supplier<Long> memoizedSupplier = Suppliers.memoizeWithExpiration(delegate, 100, TimeUnit.MILLISECONDS);

        long firstResult = memoizedSupplier.get();
        long secondResult = memoizedSupplier.get();

        assertTrue(firstResult < secondResult);
    }

    @Test
    public void testOfInstance() {
        Supplier<Integer> supplierOfInstance = Suppliers.ofInstance(42);
        assertEquals(42, supplierOfInstance.get());
    }

    @Test
    public void testSynchronizedSupplier() {
        Supplier<Integer> delegate = () -> 42;
        Supplier<Integer> synchronizedSupplier = Suppliers.synchronizedSupplier(delegate);

        Thread thread1 = new Thread(() -> {
            synchronized (synchronizedSupplier) {
                int result1 = synchronizedSupplier.get();
                assertEquals(42, result1);
            }
        });

        Thread thread2 = new Thread(() -> {
            synchronized (synchronizedSupplier) {
                int result2 = synchronizedSupplier.get();
                assertEquals(42, result2);
            }
        });

        thread1.start();
        thread2.start();

        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}