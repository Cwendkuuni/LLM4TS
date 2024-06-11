package com.google.common.base;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.fail;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.Supplier;
import java.util.concurrent.TimeUnit;

import org.junit.Test;

public class SuppliersTest {

    @Test
    public void testCompose() {
        Function<Integer, String> func = new Function<Integer, String>() {
            @Override
            public String apply(Integer input) {
                return input.toString();
            }
        };
        Supplier<Integer> supplier = Suppliers.ofInstance(42);

        Supplier<String> composed = Suppliers.compose(func, supplier);
        assertEquals("42", composed.get());
    }

    @Test
    public void testCompose_NullFunction() {
        Supplier<Integer> supplier = Suppliers.ofInstance(42);

        try {
            Suppliers.compose(null, supplier);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test
    public void testCompose_NullSupplier() {
        Function<Integer, String> func = new Function<Integer, String>() {
            @Override
            public String apply(Integer input) {
                return input.toString();
            }
        };

        try {
            Suppliers.compose(func, null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test
    public void testMemoize() {
        Supplier<String> supplier = () -> "Hello";
        Supplier<String> memoized = Suppliers.memoize(supplier);

        assertEquals("Hello", memoized.get());
        assertEquals("Hello", memoized.get());
    }

    @Test
    public void testMemoize_NullSupplier() {
        try {
            Suppliers.memoize(null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test
    public void testMemoizeWithExpiration() {
        Supplier<Long> supplier = () -> System.nanoTime();
        Supplier<Long> memoized = Suppliers.memoizeWithExpiration(supplier, 1, TimeUnit.SECONDS);

        long currentTime = System.nanoTime();
        long previousTime = currentTime;

        for (int i = 0; i < 10; i++) {
            long result = memoized.get();
            assertEquals("Previous result should be cached", previousTime, result);
            currentTime = System.nanoTime();
            previousTime = result;
        }

        // Verify that the supplier is called again after the expiration time
        for (int i = 0; i < 10; i++) {
            long result = memoized.get();
            long newTime = System.nanoTime();
            checkArgument(newTime > previousTime, "Supplier should have been called again");
            previousTime = newTime;
        }
    }

    @Test
    public void testMemoizeWithExpiration_NullSupplier() {
        try {
            Suppliers.memoizeWithExpiration(null, 1, TimeUnit.SECONDS);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test
    public void testOfInstance() {
        Supplier<String> supplier = Suppliers.ofInstance("Hello");
        assertEquals("Hello", supplier.get());
    }

    @Test
    public void testOfInstance_NullInstance() {
        try {
            Suppliers.ofInstance(null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test
    public void testSynchronizedSupplier() {
        Supplier<String> supplier = () -> "Hello";
        Supplier<String> synchronizedSupplier = Suppliers.synchronizedSupplier(supplier);

        Thread thread1 = new Thread(() -> {
            for (int i = 0; i < 10000; i++) {
                synchronized (synchronizedSupplier) {
                    assertEquals("Hello", synchronizedSupplier.get());
                }
            }
        });

        Thread thread2 = new Thread(() -> {
            for (int i = 0; i < 10000; i++) {
                synchronized (synchronizedSupplier) {
                    assertEquals("Hello", synchronizedSupplier.get());
                }
            }
        });

        thread1.start();
        thread2.start();

        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testSupplierFunction() {
        Supplier<String> supplier = Suppliers.ofInstance("Hello");
        Function<Supplier<String>, String> supplierFunction = Suppliers.supplierFunction();
        String result = supplierFunction.apply(supplier);
        assertEquals("Hello", result);
    }
}