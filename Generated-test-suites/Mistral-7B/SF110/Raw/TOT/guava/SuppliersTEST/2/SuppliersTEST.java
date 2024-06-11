package com.google.common.base;

import com.google.common.base.Suppliers;
import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.*;
import java.util.function.Function;
import java.util.function.Supplier;

public class SuppliersTest {

    @Test
    public void testCompose() {
        Function<Integer, String> function = i -> Integer.toString(i);
        Supplier<Integer> supplier = Suppliers.ofInstance(42);
        Supplier<String> composed = Suppliers.compose(function, supplier);

        Assert.assertEquals("42", composed.get());
    }

    @Test
    public void testCompose_NullFunction() {
        Supplier<Integer> supplier = Suppliers.ofInstance(42);
        Function<? super Integer, String> function = null;

        try {
            Suppliers.compose(function, supplier);
            Assert.fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // expected
        }
    }

    @Test
    public void testCompose_NullSupplier() {
        Function<Integer, String> function = i -> Integer.toString(i);
        Supplier<Integer> supplier = null;

        try {
            Suppliers.compose(function, supplier);
            Assert.fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // expected
        }
    }

    @Test
    public void testMemoize() {
        Supplier<Integer> supplier = () -> 42;
        Supplier<Integer> memoized = Suppliers.memoize(supplier);

        Assert.assertEquals(42, memoized.get());
        Assert.assertEquals(42, memoized.get());
    }

    @Test
    public void testMemoize_MemoizingSupplierInstance() {
        Supplier<Integer> supplier = () -> 42;
        Supplier<Integer> memoized = Suppliers.memoize(new MemoizingSupplier<>(supplier));

        Assert.assertEquals(42, memoized.get());
        Assert.assertEquals(42, memoized.get());
    }

    @Test
    public void testMemoizeWithExpiration() {
        Supplier<Integer> supplier = () -> {
            Thread.sleep(100);
            return 42;
        };
        Supplier<Integer> memoizedWithExpiration = Suppliers.memoizeWithExpiration(supplier, 50, TimeUnit.MILLISECONDS);

        Assert.assertEquals(42, memoizedWithExpiration.get());
        Assert.assertEquals(42, memoizedWithExpiration.get());

        try {
            Thread.sleep(200);
            memoizedWithExpiration.get();
            Assert.fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            // expected
        }
    }

    @Test
    public void testOfInstance() {
        Supplier<String> supplier = Suppliers.ofInstance("Hello, World!");
        Assert.assertEquals("Hello, World!", supplier.get());
    }

    @Test
    public void testOfInstance_Null() {
        try {
            Suppliers.ofInstance(null);
            Assert.fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // expected
        }
    }

    @Test
    public void testSynchronizedSupplier() {
        Supplier<Integer> supplier = () -> 42;
        Supplier<Integer> synchronizedSupplier = Suppliers.synchronizedSupplier(supplier);

        new Thread(() -> {
            try {
                Assert.assertEquals(42, synchronizedSupplier.get());
            } catch (Exception e) {
                Assert.fail("Unexpected exception: " + e.getMessage());
            }
        }).start();

        try {
            Assert.assertEquals(42, synchronizedSupplier.get());
        } catch (Exception e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testSupplierFunction() {
        Function<Supplier<String>, String> function = Suppliers.supplierFunction();
        Supplier<String> supplier = Suppliers.ofInstance("Hello, World!");

        Assert.assertEquals("Hello, World!", function.apply(supplier));
    }
}