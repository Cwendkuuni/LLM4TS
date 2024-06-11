package com.google.common.base.test;

import static org.junit.Assert.*;

import java.util.concurrent.Supplier;

import org.junit.Test;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.base.Suppliers;

public class SuppliersTest {

    @SuppressWarnings("unchecked")
    @Test
    public void testCompose() {
        Function<Integer, String> function = new Function<Integer, String>() {
            @Override
            public String apply(Integer integer) {
                return integer.toString();
            }
        };
        Supplier<Integer> supplier = Suppliers.ofInstance(5);
        Supplier<String> supplierComposition = Suppliers.compose(function, supplier);
        assertEquals("5", supplierComposition.get());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testMemoize() {
        Supplier<Integer> supplier = new Supplier<Integer>() {
            private int counter = 0;

            @Override
            public Integer get() {
                return ++counter;
            }
        };
        Supplier<Integer> memoizedSupplier = Suppliers.memoize(supplier);
        assertEquals(1, memoizedSupplier.get());
        assertEquals(1, memoizedSupplier.get());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testMemoizeWithExpiration() {
        Supplier<Long> supplier = new Supplier<Long>() {
            private long lastResult = 0;

            @Override
            public Long get() {
                return lastResult;
            }
        };
        Supplier<Long> memoizedSupplierWithExpiration = Suppliers.memoizeWithExpiration(supplier, 100, TimeUnit.MILLISECONDS);
        long result1 = memoizedSupplierWithExpiration.get();
        long result2 = memoizedSupplierWithExpiration.get();
        long result3 = memoizedSupplierWithExpiration.get();
        assertEquals(result1, result2);
        assertEquals(result1, result3);
        Preconditions.checkState(System.currentTimeMillis() - result1 > 100);
        assertEquals(result3, memoizedSupplierWithExpiration.get());
    }

    @Test
    public void testOfInstance() {
        Supplier<String> supplier = Suppliers.ofInstance("Hello");
        assertEquals("Hello", supplier.get());
    }

    @Test
    public void testSynchronizedSupplier() {
        Supplier<Integer> supplier = new Supplier<Integer>() {
            private int counter = 0;

            @Override
            public Integer get() {
                return ++counter;
            }
        };
        Supplier<Integer> synchronizedSupplier = Suppliers.synchronizedSupplier(supplier);
        Thread thread1 = new Thread(() -> assertEquals(0, synchronizedSupplier.get()));
        Thread thread2 = new Thread(() -> assertEquals(0, synchronizedSupplier.get()));
        thread1.start();
        thread2.start();
        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            fail();
        }
        assertEquals(1, synchronizedSupplier.get());
    }

    @Test(expected = NullPointerException.class)
    public void testPreconditionsChecks() {
        Suppliers.compose(null, Suppliers.ofInstance(5));
    }

    @Test(expected = NullPointerException.class)
    public void testMemoizingNullSupplier() {
        Suppliers.memoize(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMemoizeWithNegativeExpirationDuration() {
        Suppliers.memoizeWithExpiration(Suppliers.ofInstance(5), -100, TimeUnit.MILLISECONDS);
    }
}