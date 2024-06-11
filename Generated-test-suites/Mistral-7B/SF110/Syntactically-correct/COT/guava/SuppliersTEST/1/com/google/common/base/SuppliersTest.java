package com.google.common.base.test;

import static org.junit.Assert.*;

import java.util.concurrent.*;

import org.junit.*;

import com.google.common.base.*;
import com.google.common.base. Suppliers;
import com.google.common.base.Function;
import com.google.common.base.Supplier;
import com.google.common.base.SupplierFunction;

public class SuppliersTest {

    @SuppressWarnings("unchecked")
    @Test
    public void testCompose() {
        Function<Integer, Integer> addOne = new Function<Integer, Integer>() {
            @Override
            public Integer apply(Integer input) {
                return input + 1;
            }
        };

        Supplier<Integer> supplier = Suppliers.ofInstance(42);

        Supplier<Integer> composedSupplier = Suppliers.compose(addOne, supplier);

        assertEquals(43, composedSupplier.get());
    }

    @Test(expected = NullPointerException.class)
    public void testComposeWithNullFunction() {
        Supplier<Integer> supplier = Suppliers.ofInstance(42);
        Function<? super Integer, Integer> nullFunction = null;

        try {
            Suppliers.compose(nullFunction, supplier);
        } catch (NullPointerException e) {
            // Expected exception
        }
    }

    @Test(expected = NullPointerException.class)
    public void testComposeWithNullSupplier() {
        Function<Integer, Integer> addOne = new Function<Integer, Integer>() {
            @Override
            public Integer apply(Integer input) {
                return input + 1;
            }
        };

        try {
            Suppliers.compose(addOne, null);
        } catch (NullPointerException e) {
            // Expected exception
        }
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
        long duration = 10;
        TimeUnit timeUnit = TimeUnit.MILLISECONDS;
        Supplier<Integer> supplier = () -> 42;
        Supplier<Integer> expiringMemoizingSupplier = Suppliers.memoizeWithExpiration(supplier, duration, timeUnit);

        assertEquals(42, expiringMemoizingSupplier.get());
        assertEquals(42, expiringMemoizingSupplier.get());

        try {
            Thread.sleep(duration + 1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        assertNotEquals(42, expiringMemoizingSupplier.get());
    }

    @Test(expected = NullPointerException.class)
    public void testMemoizeWithNullSupplier() {
        try {
            Suppliers.memoize(null);
        } catch (NullPointerException e) {
            // Expected exception
        }
    }

    @Test
    public void testOfInstance() {
        Supplier<String> supplier = Suppliers.ofInstance("Hello World");

        assertEquals("Hello World", supplier.get());
    }

    @Test(expected = NullPointerException.class)
    public void testOfInstanceWithNullInstance() {
        try {
            Suppliers.ofInstance(null);
        } catch (NullPointerException e) {
            // Expected exception
        }
    }

    @Test
    public void testSynchronizedSupplier() {
        AtomicInteger atomicInteger = new AtomicInteger(0);
        Supplier<Integer> supplier = () -> atomicInteger.incrementAndGet();
        Supplier<Integer> synchronizedSupplier = Suppliers.synchronizedSupplier(supplier);

        Runnable task = () -> {
            for (int i = 0; i < 100; i++) {
                synchronizedSupplier.get();
            }
        };

        ExecutorService executor = Executors.newFixedThreadPool(10);
        executor.submit(task);
        executor.shutdown();

        assertEquals(101, atomicInteger.get());
    }

    @Test
    public void testSupplierFunction() {
        Supplier<Integer> supplier = () -> 42;
        SupplierFunction<Integer> supplierFunction = Suppliers.supplierFunction();

        assertEquals(42, supplierFunction.apply(supplier).get());
    }

}