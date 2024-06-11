package com.google.common.base.test;

import static org.junit.Assert.*;

import java.util.concurrent.Supplier;

import org.junit.Test;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.base.Suppliers;

public class SuppliersTest {

    @SuppressWarnings("unchecked")
    private static <T> Supplier<T> createSupplier() {
        return new Supplier<T>() {
            T value;

            @Override
            public T get() {
                if (value == null) {
                    value = new Object() {}.toString().getClass().newInstance();
                }
                return value;
            }
        };
    }

    @Test
    public void testCompose() {
        Function<String, Integer> addOne = new Function<String, Integer>() {
            @Override
            public Integer apply(String input) {
                return Integer.valueOf(Integer.parseInt(input) + 1);
            }
        };

        Supplier<String> supplier = Suppliers.ofInstance("1");
        Supplier<Integer> composedSupplier = Suppliers.compose(addOne, supplier);

        assertEquals(Integer.valueOf(2), composedSupplier.get());
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
        Supplier<Long> supplier = () -> System.nanoTime();
        Supplier<Long> memoizedSupplierWithExpiration = Suppliers.memoizeWithExpiration(supplier, 10, TimeUnit.MILLISECONDS);

        long previous = memoizedSupplierWithExpiration.get();
        try {
            Thread.sleep(20);
            long current = memoizedSupplierWithExpiration.get();
            assertEquals(previous, current);
        } catch (InterruptedException e) {
            fail("Interrupted while testing memoized supplier with expiration");
        }
    }

    @Test
    public void testOfInstance() {
        Supplier<String> supplier = Suppliers.ofInstance("Hello");
        assertEquals("Hello", supplier.get());
    }

    @Test
    public void testSynchronizedSupplier() {
        Supplier<String> supplier = () -> "Synchronized";
        Supplier<String> synchronizedSupplier = Suppliers.synchronizedSupplier(supplier);

        Thread thread1 = new Thread(() -> assertEquals("Synchronized", synchronizedSupplier.get()));
        Thread thread2 = new Thread(() -> assertEquals("Synchronized", synchronizedSupplier.get()));

        thread1.start();
        thread2.start();

        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            fail("Interrupted while testing synchronized supplier");
        }
    }

    @Test
    public void testMemoizingSupplierWithNullDelegate() {
        try {
            Suppliers.memoize(null);
            fail("Expected NullPointerException");
        } catch (NullPointerException expected) {
            // Expected
        }
    }

    @Test
    public void testExpiringMemoizingSupplierWithZeroExpirationTime() {
        try {
            Suppliers.memoizeWithExpiration(Suppliers.ofInstance(new Object()), 0, TimeUnit.MILLISECONDS);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
            // Expected
        }
    }

    @Test
    public void testExpiringMemoizingSupplierWithNegativeExpirationTime() {
        try {
            Suppliers.memoizeWithExpiration(Suppliers.ofInstance(new Object()), -1, TimeUnit.MILLISECONDS);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
            // Expected
        }
    }

    @Test
    public void testSupplierCompositionWithNullFunctionOrSupplier() {
        try {
            Suppliers.compose(null, Suppliers.ofInstance(new Object()));
            fail("Expected NullPointerException");
        } catch (NullPointerException expected) {
            // Expected
        }

        try {
            Suppliers.compose(Suppliers.ofInstance(new Function<Object, Object>() {}), null);
            fail("Expected NullPointerException");
        } catch (NullPointerException expected) {
            // Expected
        }
    }
}