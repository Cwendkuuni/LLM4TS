package com.google.common.base.test;

import com.google.common.base.Suppliers;
import org.junit.Test;
import static org.junit.Assert.*;

public class SuppliersTest {

    // Test for compose method
    @Test
    public void testCompose() {
        Function<Integer, Integer> addOne = (i) -> i + 1;
        Supplier<Integer> supplierOfFive = Suppliers.ofInstance(5);
        Supplier<Integer> composedSupplier = Suppliers.compose(addOne, supplierOfFive);

        assertEquals(6, composedSupplier.get());
    }

    @Test(expected = NullPointerException.class)
    public void testComposeWithNullFunction() {
        Function<Integer, Integer> nullFunction = null;
        Supplier<Integer> supplierOfFive = Suppliers.ofInstance(5);
        Suppliers.compose(nullFunction, supplierOfFive);
    }

    @Test(expected = NullPointerException.class)
    public void testComposeWithNullSupplier() {
        Function<Integer, Integer> addOne = (i) -> i + 1;
        Supplier<Integer> nullSupplier = null;
        Suppliers.compose(addOne, nullSupplier);
    }

    // Test for memoize method
    @Test
    public void testMemoize() {
        Supplier<Integer> supplier = () -> 42;
        Supplier<Integer> memoizedSupplier = Suppliers.memoize(supplier);
        assertEquals(42, memoizedSupplier.get());
        assertEquals(42, memoizedSupplier.get());
    }

    // Test for memoizeWithExpiration method
    @Test
    public void testMemoizeWithExpiration() {
        Supplier<Long> supplier = () -> System.nanoTime();
        Supplier<Long> memoizedSupplier = Suppliers.memoizeWithExpiration(supplier, 100, TimeUnit.MILLISECONDS);
        long initialValue = memoizedSupplier.get();
        long newValue = System.nanoTime();
        long expirationTime = initialValue + 100;

        assertEquals(initialValue, memoizedSupplier.get());
        assertEquals(initialValue, memoizedSupplier.get());
        Thread.sleep(150); // Wait for expiration
        assertEquals(newValue, memoizedSupplier.get());
    }

    @Test(expected = NullPointerException.class)
    public void testMemoizeWithNullSupplier() {
        Supplier<Long> nullSupplier = null;
        Suppliers.memoizeWithExpiration(nullSupplier, 100, TimeUnit.MILLISECONDS);
    }

    // Test for ofInstance method
    @Test
    public void testOfInstance() {
        Supplier<String> supplierOfStringInstance = Suppliers.ofInstance("Hello, World!");
        assertEquals("Hello, World!", supplierOfStringInstance.get());
    }

    // Test for synchronizedSupplier method
    @Test
    public void testSynchronizedSupplier() {
        Supplier<Integer> supplier = () -> 42;
        Supplier<Integer> synchronizedSupplier = Suppliers.synchronizedSupplier(supplier);

        Thread thread1 = new Thread(() -> assertEquals(42, synchronizedSupplier.get()));
        Thread thread2 = new Thread(() -> assertEquals(42, synchronizedSupplier.get()));

        thread1.start();
        thread2.start();

        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test(expected = NullPointerException.class)
    public void testSynchronizedSupplierWithNullSupplier() {
        Supplier<Integer> nullSupplier = null;
        Suppliers.synchronizedSupplier(nullSupplier);
    }
}