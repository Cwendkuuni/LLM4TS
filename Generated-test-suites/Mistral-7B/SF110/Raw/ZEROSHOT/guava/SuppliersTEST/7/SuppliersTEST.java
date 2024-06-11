package com.google.common.base.test;

import static org.junit.Assert.*;

import java.util.concurrent.Supplier;

import org.junit.Test;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.base.Suppliers;

public class SuppliersTest {

    @SuppressWarnings("unchecked")
    private static <T> Supplier<T> constantSupplier(T value) {
        return Suppliers.ofInstance(value);
    }

    @Test
    public void testCompose() {
        Function<String, Integer> addOne = new Function<String, Integer>() {
            @Override
            public Integer apply(String input) {
                return Integer.valueOf(1 + Integer.parseInt(input));
            }
        };

        Supplier<String> stringSupplier = constantSupplier("5");
        Supplier<Integer> integerSupplier = Suppliers.compose(addOne, stringSupplier);

        assertEquals(Integer.valueOf(6), integerSupplier.get());
    }

    @Test
    public void testMemoize() {
        Function<Integer, Integer> square = new Function<Integer, Integer>() {
            @Override
            public Integer apply(Integer input) {
                return Integer.valueOf(input * input);
            }
        };

        Supplier<Integer> delegate = () -> 5;
        Supplier<Integer> memoized = Suppliers.memoize(delegate);

        assertEquals(5, memoized.get());
        assertEquals(5, memoized.get());
    }

    @Test
    public void testMemoizeWithExpiration() {
        Function<Integer, Integer> square = new Function<Integer, Integer>() {
            @Override
            public Integer apply(Integer input) {
                return Integer.valueOf(input * input);
            }
        };

        Supplier<Integer> delegate = () -> 5;
        Supplier<Integer> memoizedWithExpiration = Suppliers.memoizeWithExpiration(delegate, 100, TimeUnit.MILLISECONDS);

        assertEquals(5, memoizedWithExpiration.get());
        assertEquals(5, memoizedWithExpiration.get());

        try {
            Thread.sleep(150);
            assertEquals(5, memoizedWithExpiration.get());
        } catch (InterruptedException e) {
            fail("Thread was interrupted");
        }

        assertEquals(5, memoizedWithExpiration.get());
        assertNotEquals(5, memoizedWithExpiration.get());
    }

    @Test(expected = NullPointerException.class)
    public void testComposeWithNullFunction() {
        Supplier<String> stringSupplier = constantSupplier("5");
        Function<Supplier<Object>, Supplier<Integer>> nullFunction = null;
        Supplier<Integer> integerSupplier = Suppliers.compose(nullFunction, stringSupplier);
    }

    @Test(expected = NullPointerException.class)
    public void testComposeWithNullDelegate() {
        Function<String, Integer> addOne = new Function<String, Integer>() {
            @Override
            public Integer apply(String input) {
                return Integer.valueOf(1 + Integer.parseInt(input));
            }
        };
        Supplier<String> nullDelegate = null;
        Supplier<Integer> integerSupplier = Suppliers.compose(addOne, nullDelegate);
    }

    @Test(expected = NullPointerException.class)
    public void testMemoizeWithNullDelegate() {
        Supplier<Integer> nullDelegate = null;
        Suppliers.memoize(nullDelegate);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMemoizeWithNegativeExpiration() {
        Function<Integer, Integer> square = new Function<Integer, Integer>() {
            @Override
            public Integer apply(Integer input) {
                return Integer.valueOf(input * input);
            }
        };

        Supplier<Integer> delegate = () -> 5;
        Suppliers.memoizeWithExpiration(delegate, -100, TimeUnit.MILLISECONDS);
    }

    @Test(expected = NullPointerException.class)
    public void testSupplierFunctionWithNullDelegate() {
        Suppliers.supplierFunction().apply(null);
    }
}