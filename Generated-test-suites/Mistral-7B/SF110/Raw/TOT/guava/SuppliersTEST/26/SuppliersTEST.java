package com.google.common.base;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.concurrent.*;

import org.junit.*;
import org.mockito.*;

@GwtIncompatible // Since we're using Mockito
public class SuppliersTest {

    @Test
    public void testCompose() {
        Function<Integer, Integer> addOne = input -> input + 1;
        Supplier<Integer> supplierOfSeven = Suppliers.ofInstance(7);

        Supplier<Integer> supplierOfEight = Suppliers.compose(addOne, supplierOfSeven);

        assertEquals(8, supplierOfEight.get());
    }

    @Test
    public void testMemoize() {
        Supplier<String> supplierOfHello = Suppliers.ofInstance("Hello");
        MemoizingSupplier<String> memoizedSupplier = Suppliers.memoize(supplierOfHello);

        assertEquals("Hello", memoizedSupplier.get());
        assertEquals("Hello", memoizedSupplier.get());
    }

    @Test
    public void testMemoizeWithExpiration() {
        Supplier<String> supplierOfHello = Suppliers.ofInstance("Hello");
        Supplier<String> supplierWithOneSecondExpiration = Suppliers.memoizeWithExpiration(supplierOfHello, 1, TimeUnit.SECONDS);

        assertEquals("Hello", supplierWithOneSecondExpiration.get());
        assertEquals("Hello", supplierWithOneSecondExpiration.get());
        assertEquals("Hello", supplierWithOneSecondExpiration.get());
        assertEquals("Hello", supplierWithOneSecondExpiration.get());

        sleep(2000L); // Let expiration time pass

        assertEquals("Hello", supplierWithOneSecondExpiration.get()); // Should call original supplier again
    }

    @Test(expected = NullPointerException.class)
    public void testComposeWithNullFunction() {
        Supplier<Integer> supplierOfSeven = Suppliers.ofInstance(7);
        Function<? super Integer, Integer> nullFunction = null;

        try {
            Suppliers.compose(nullFunction, supplierOfSeven);
        } catch (NullPointerException e) {
            throw e; // Propagate exception
        }
    }

    @Test(expected = NullPointerException.class)
    public void testComposeWithNullSupplier() {
        Function<Integer, Integer> addOne = input -> input + 1;
        Supplier<Integer> nullSupplier = null;

        try {
            Suppliers.compose(addOne, nullSupplier);
        } catch (NullPointerException e) {
            throw e; // Propagate exception
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMemoizeWithNegativeDuration() {
        Supplier<String> supplierOfHello = Suppliers.ofInstance("Hello");

        try {
            Suppliers.memoizeWithExpiration(supplierOfHello, -1, TimeUnit.MILLISECONDS);
        } catch (IllegalArgumentException e) {
            throw e; // Propagate exception
        }
    }

    @Test
    public void testThreadSafeSupplier() {
        final AtomicInteger counter = new AtomicInteger(0);
        Supplier<String> originalSupplier = () -> "Hello";
        Supplier<String> threadSafeSupplier = Suppliers.synchronizedSupplier(originalSupplier);

        // Multithreaded usage
        ExecutorService executor = Executors.newFixedThreadPool(10);
        for (int i = 0; i < 10000; i++) {
            executor.submit(() -> {
                threadSafeSupplier.get();
                counter.incrementAndGet();
            });
        }
        executor.shutdown();

        assertEquals(10000, counter.get()); // Should not exceed this number
    }

    private void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}