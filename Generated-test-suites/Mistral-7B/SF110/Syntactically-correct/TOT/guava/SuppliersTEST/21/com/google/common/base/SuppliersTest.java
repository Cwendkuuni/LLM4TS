package com.google.common.base.test;

import com.google.common.base.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class SuppliersTest {

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

    @Test
    public void testComposeWithNullFunction() {
        Supplier<String> supplier = Suppliers.ofInstance("Hello");
        Function<String, String> nullFunction = null;

        try {
            Suppliers.compose(nullFunction, supplier);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test
    public void testComposeWithNullSupplier() {
        Function<String, String> function = new Function<String, String>() {
            @Override
            public String apply(String input) {
                return input.toUpperCase();
            }
        };

        try {
            Suppliers.compose(function, null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test
    public void testMemoize() {
        Supplier<Integer> supplier = new Supplier<Integer>() {
            int value = 0;
            @Override
            public Integer get() {
                return ++value;
            }
        };

        Supplier<Integer> memoizedSupplier = Suppliers.memoize(supplier);

        assertEquals(1, memoizedSupplier.get());
        assertEquals(1, memoizedSupplier.get());
        assertEquals(2, memoizedSupplier.get());
    }

    @Test
    public void testMemoizeWithMemoizingSupplier() {
        Supplier<Integer> supplier = new MemoizingSupplier<Integer>(new Supplier<Integer>() {
            int value = 0;
            @Override
            public Integer get() {
                return ++value;
            }
        });

        Supplier<Integer> memoizedSupplier = Suppliers.memoize(supplier);

        assertEquals(1, memoizedSupplier.get());
        assertEquals(1, memoizedSupplier.get());
        assertEquals(2, memoizedSupplier.get());
    }

    @Test
    public void testMemoizeWithExpiration() {
        Supplier<Long> supplier = () -> System.nanoTime();

        Supplier<Long> memoizedSupplier = Suppliers.memoizeWithExpiration(supplier, 100, TimeUnit.MILLISECONDS);

        long currentTime = System.currentTimeMillis();
        long time1 = memoizedSupplier.get();
        long time2 = memoizedSupplier.get();

        long expirationTime = currentTime + 100;

        assertTrue(time1 < expirationTime);
        assertTrue(time2 >= expirationTime);
    }

    @Test
    public void testMemoizeWithExpirationWithZeroDuration() {
        Supplier<Long> supplier = () -> System.nanoTime();

        try {
            Suppliers.memoizeWithExpiration(supplier, 0, TimeUnit.MILLISECONDS);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

}