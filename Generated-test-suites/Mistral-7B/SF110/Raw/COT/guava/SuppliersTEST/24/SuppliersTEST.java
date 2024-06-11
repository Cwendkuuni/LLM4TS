package com.google.common.base.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.util.concurrent.TimeUnit;

import org.junit.Test;

import com.google.common.base.Suppliers;
import com.google.common.base.Function;
import com.google.common.base.Supplier;
import com.google.common.base.Preconditions;
import com.google.common.base.Objects;

public class SuppliersTest {

    @SuppressWarnings("unchecked")
    @Test
    public void testCompose() {
        Function<String, Integer> addOne = (s) -> Integer.valueOf(Integer.valueOf(s.length()) + 1);
        Supplier<String> stringSupplier = Suppliers.ofInstance("Test");
        Supplier<Integer> integerSupplier = Suppliers.compose(addOne, stringSupplier);

        assertEquals(Integer.valueOf(5), integerSupplier.get());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testMemoize() {
        Supplier<Integer> supplier = () -> 42;
        Supplier<Integer> memoizedSupplier = Suppliers.memoize(supplier);

        assertEquals(supplier.get(), memoizedSupplier.get());
        assertEquals(supplier.get(), memoizedSupplier.get());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testMemoizeWithExpiration() {
        Supplier<String> supplier = () -> "Hello";
        Supplier<String> expiringMemoizingSupplier = Suppliers.memoizeWithExpiration(supplier, 100, TimeUnit.MILLISECONDS);

        String initialValue = expiringMemoizingSupplier.get();
        assertEquals("Hello", initialValue);

        try {
            Thread.sleep(150);
            String value = expiringMemoizingSupplier.get();
            assertNull(value);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testOfInstanceNull() {
        assertThrows(NullPointerException.class, () -> Suppliers.ofInstance(null));
    }

    @Test
    public void testSynchronizedSupplier() {
        Supplier<Integer> supplier = () -> 42;
        Supplier<Integer> synchronizedSupplier = Suppliers.synchronizedSupplier(supplier);

        Supplier<Integer> supplier1 = () -> 43;
        Supplier<Integer> synchronizedSupplier1 = Suppliers.synchronizedSupplier(supplier1);

        Thread thread1 = new Thread(() -> assertEquals(supplier.get(), synchronizedSupplier.get()));
        Thread thread2 = new Thread(() -> assertEquals(supplier1.get(), synchronizedSupplier1.get()));

        thread1.start();
        thread2.start();

        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Test(expected = NullPointerException.class)
    public void testFunctionNull() {
        Supplier<String> supplier = Suppliers.ofInstance("Test");
        Function<Supplier<String>, String> function = null;
        Suppliers.compose(function, supplier);
    }

    @Test(expected = NullPointerException.class)
    public void testSupplierNull() {
        Function<String, Integer> function = (s) -> Integer.valueOf(Integer.valueOf(s.length()) + 1);
        Suppliers.compose(function, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMemoizeNegativeDuration() {
        Supplier<Integer> supplier = () -> 42;
        assertThrows(IllegalArgumentException.class, () -> Suppliers.memoizeWithExpiration(supplier, -1, TimeUnit.MILLISECONDS));
    }
}