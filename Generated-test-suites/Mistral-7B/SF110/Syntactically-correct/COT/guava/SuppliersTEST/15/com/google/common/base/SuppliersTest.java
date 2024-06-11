package com.google.common.base.test;

import static org.junit.Assert.*;

import java.util.concurrent.Supplier;

import org.junit.Test;

import com.google.common.base.Suppliers;
import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.base.SupplierFunction;

public class SuppliersTest {

    // Test for compose method

    @Test
    public void testCompose() {
        Function<Integer, String> func = i -> String.valueOf(i * 2);
        Supplier<Integer> supplier = Suppliers.ofInstance(5);
        Supplier<String> composedSupplier = Suppliers.compose(func, supplier);
        assertEquals("10", composedSupplier.get().toString());
    }

    // Test for memoize method

    @Test
    public void testMemoize() {
        Supplier<Integer> delegate = () -> 42;
        Supplier<Integer> memosupplier = Suppliers.memoize(delegate);
        assertEquals(42, memosupplier.get());
        assertEquals(42, memosupplier.get());
    }

    // Test for memoizeWithExpiration method

    @Test
    public void testMemoizeWithExpiration() {
        Supplier<Long> delegate = () -> System.currentTimeMillis();
        Supplier<Long> expiringMemosupplier = Suppliers.memoizeWithExpiration(delegate, 50, TimeUnit.MILLISECONDS);
        long currentTime = System.currentTimeMillis();
        long firstValue = expiringMemosupplier.get();
        long secondValue = expiringMemosupplier.get();
        long thirdValue = expiringMemosupplier.get();
        long fourthValue = expiringMemosupplier.get();
        assertEquals(currentTime, firstValue);
        assertEquals(firstValue, secondValue);
        assertEquals(firstValue, thirdValue);
        assertEquals(firstValue, fourthValue);
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertNotEquals(firstValue, expiringMemosupplier.get());
    }

    // Test for ofInstance method

    @Test
    public void testOfInstance() {
        Supplier<String> supplier = Suppliers.ofInstance("Hello World!");
        assertEquals("Hello World!", supplier.get());
    }

    // Test for synchronizedSupplier method

    @Test
    public void testSynchronizedSupplier() {
        Supplier<String> supplier = () -> "Thread Safe";
        Supplier<String> synchronizedSupplier = Suppliers.synchronizedSupplier(supplier);
        Thread thread1 = new Thread(() -> System.out.println(synchronizedSupplier.get()));
        Thread thread2 = new Thread(() -> System.out.println(synchronizedSupplier.get()));
        thread1.start();
        thread2.start();
        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertEquals("Thread Safe", synchronizedSupplier.get());
        assertEquals("Thread Safe", synchronizedSupplier.get());
    }

    // Test for supplierFunction method

    @Test
    public void testSupplierFunction() {
        Supplier<Integer> supplier = Suppliers.ofInstance(5);
        SupplierFunction<Supplier<Integer>> supplierFunction = Suppliers.supplierFunction();
        Integer functionReturnValue = supplierFunction.apply(supplier);
        assertEquals(5, functionReturnValue.intValue());
    }

    // Test for compose method with null arguments

    @Test(expected = NullPointerException.class)
    public void testComposeNullFunction() {
        Function<Integer, Integer> func = null;
        Supplier<Integer> supplier = Suppliers.ofInstance(5);
        Suppliers.compose(func, supplier);
    }

    @Test(expected = NullPointerException.class)
    public void testComposeNullSupplier() {
        Function<Integer, Integer> func = i -> i * 2;
        Supplier<Integer> supplier = null;
        Suppliers.compose(func, supplier);
    }

    // Test for memoize method with null delegate

    @Test(expected = NullPointerException.class)
    public void testMemoizeNullDelegate() {
        Supplier<Integer> delegate = null;
        Suppliers.memoize(delegate);
    }

    // Test for memoizeWithExpiration method with negative duration

    @Test(expected = IllegalArgumentException.class)
    public void testMemoizeWithExpirationNegativeDuration() {
        Supplier<Long> delegate = () -> System.currentTimeMillis();
        Suppliers.memoizeWithExpiration(delegate, -1, TimeUnit.MILLISECONDS);
    }

    // Test for ofInstance method with null instance

    @Test(expected = NullPointerException.class)
    public void testOfInstanceNullInstance() {
        Suppliers.ofInstance(null);
    }

    // Test for synchronizedSupplier method with null delegate
    @Test(expected = NullPointerException.class)
    public void testSynchronizedSupplierNullDelegate() {
        Supplier<String> delegate = null;
        Suppliers.synchronizedSupplier(delegate);
    }
}