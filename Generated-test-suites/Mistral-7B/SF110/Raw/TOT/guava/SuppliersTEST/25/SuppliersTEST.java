package com.google.common.base.test;

import com.google.common.base.Function;
import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class SuppliersTest {

    @Test
    public void testCompose() {
        Function<String, Integer> addOne = new Function<String, Integer>() {
            @Override
            public Integer apply(String input) {
                return Integer.valueOf(Integer.parseInt(input) + 1);
            }
        };

        Supplier<String> stringSupplier = Suppliers.ofInstance("10");
        Supplier<Integer> integerSupplier = Suppliers.compose(addOne, stringSupplier);

        Integer result = integerSupplier.get();
        assertEquals(Integer.valueOf(11), result);
    }

    @Test
    public void testMemoize() {
        Supplier<Integer> supplier = () -> 1;
        Supplier<Integer> memoizedSupplier = Suppliers.memoize(supplier);

        assertEquals(1, memoizedSupplier.get());
        assertEquals(1, memoizedSupplier.get());
    }

    @Test
    public void testMemoizeWithExpiration() {
        Supplier<Long> supplier = () -> System.nanoTime();
        Supplier<Long> expiringMemoizingSupplier = Suppliers.memoizeWithExpiration(supplier, 50, TimeUnit.MILLISECONDS);

        long currentTime = System.nanoTime();
        long previousTime = currentTime;

        for (int i = 0; i < 10; i++) {
            long value = expiringMemoizingSupplier.get();
            currentTime = System.nanoTime();
            assertEquals(previousTime, value);
            previousTime = currentTime;
        }

        Thread.yield();

        long value = expiringMemoizingSupplier.get();
        currentTime = System.nanoTime();
        assertNotSame(previousTime, value);
    }

    @Test(expected = NullPointerException.class)
    public void testComposeNullFunction() {
        Supplier<String> stringSupplier = Suppliers.ofInstance("10");
        Function<String, Integer> nullFunction = null;

        Suppliers.compose(nullFunction, stringSupplier);
    }

    @Test(expected = NullPointerException.class)
    public void testComposeNullSupplier() {
        Function<String, Integer> addOne = new Function<String, Integer>() {
            @Override
            public Integer apply(String input) {
                return Integer.valueOf(Integer.parseInt(input) + 1);
            }
        };

        Suppliers.compose(addOne, null);
    }

    @Test(expected = NullPointerException.class)
    public void testMemoizeNullSupplier() {
        Suppliers.memoize(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMemoizeWithNegativeDuration() {
        Supplier<Integer> supplier = () -> 1;
        Suppliers.memoizeWithExpiration(supplier, -1, TimeUnit.MILLISECONDS);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExpiringMemoizingSupplierNegativeDuration() {
        Supplier<Integer> supplier = () -> 1;
        Suppliers.ExpiringMemoizingSupplier<Integer> expiringMemoizingSupplier = new Suppliers.ExpiringMemoizingSupplier<Integer>(supplier, -1, TimeUnit.MILLISECONDS);
    }
}