package com.google.common.base;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.junit.Assert.*;

import com.google.common.base.*;
import org.junit.Test;

public class SuppliersTest {

    @Test
    public void testCompose() {
        Function<Integer, Integer> addOne = (i) -> i + 1;
        Supplier<Integer> supplierOfFive = Suppliers.ofInstance(5);

        Supplier<Integer> composedSupplier = Suppliers.compose(addOne, supplierOfFive);

        assertEquals(6, composedSupplier.get());
    }

    @Test
    public void testMemoize() {
        Function<Integer, Integer> function = (i) -> i * i;
        Supplier<Integer> supplier = Suppliers.memoize(Suppliers.ofInstance(function));

        assertEquals(0, supplier.get()); // First call
        assertEquals(0, supplier.get()); // Second call
        assertEquals(1, supplier.get()); // Third call
        assertEquals(1, supplier.get()); // Fourth call
    }

    @Test
    public void testMemoizeWithExpiration() {
        Function<Integer, Integer> function = (i) -> i * i;
        Supplier<Integer> supplier = Suppliers.memoizeWithExpiration(Suppliers.ofInstance(function), 500, TimeUnit.MILLISECONDS);

        assertEquals(0, supplier.get()); // First call
        assertEquals(0, supplier.get()); // Second call (before expiration)
        assertEquals(1, supplier.get()); // Third call
        assertEquals(1, supplier.get()); // Fourth call (before expiration)
        assertEquals(4, supplier.get()); // Fifth call (after expiration)
        assertEquals(4, supplier.get()); // Sixth call (after expiration)
    }

    @Test
    public void testOfInstance() {
        Supplier<String> supplier = Suppliers.ofInstance("Hello");

        assertEquals("Hello", supplier.get());
    }

    @Test
    public void testSynchronizedSupplier() {
        AtomicInteger counter = new AtomicInteger(0);
        Supplier<Integer> supplier = () -> counter.incrementAndGet();

        Supplier<Integer> synchronizedSupplier = Suppliers.synchronizedSupplier(supplier);

        Runnable task = () -> {
            for (int i = 0; i < 100; i++) {
                assertEquals(i + 1, synchronizedSupplier.get());
            }
        };

        Executors.newFixedThreadPool(10).submit(task);
        Executors.newFixedThreadPool(10).submit(task);
        Executors.newFixedThreadPool(10).shutdown();
    }
}