package com.example.junit.test;

import static org.junit.Assert.*;

import java.util.concurrent.Supplier;
import java.util.concurrent.TimeUnit;

import org.junit.Test;

import com.google.common.base.Suppliers;

public class SuppliersTest {

    // Test for compose method
    
    @Test
    public void testCompose() {
        Function<Integer, Integer> function = new Function<Integer, Integer>() {
            @Override
            public Integer apply(Integer input) {
                return input * 2;
            }
        };

        Supplier<Integer> supplier = Suppliers.ofInstance(4);
        Supplier<Integer> expectedResult = Suppliers.compose(function, supplier);

        assertEquals(expectedResult.get(), 8);
    }

    // Test for memoize method

    @Test
    public void testMemoize() {
        Supplier<String> supplier = () -> "Hello";
        Supplier<String> memoizedSupplier = Suppliers.memoize(supplier);

        assertEquals(memoizedSupplier.get(), "Hello");
        assertEquals(memoizedSupplier.get(), "Hello");
    }

    // Test for memoizeWithExpiration method

    @Test
    public void testMemoizeWithExpiration() {
        Supplier<String> supplier = () -> "Hello";
        Supplier<String> memoizedSupplier = Suppliers.memoizeWithExpiration(supplier, 100, TimeUnit.MILLISECONDS);

        assertEquals(memoizedSupplier.get(), "Hello");
        try {
            Thread.sleep(150);
            assertEquals(memoizedSupplier.get(), "Hello");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        assertEquals(memoizedSupplier.get(), "Hello");
        try {
            Thread.sleep(200);
            assertEquals(memoizedSupplier.get(), "Hello"); // This should fail
        } catch (Exception e) {
            assertTrue(e instanceof IllegalStateException);
        }
    }

    // Test for ofInstance method

    @Test
    public void testOfInstance() {
        Supplier<String> supplier = Suppliers.ofInstance("Hello");

        assertEquals(supplier.get(), "Hello");
        assertEquals(supplier.get(), "Hello");
    }

    // Test for synchronizedSupplier method

    @Test
    public void testSynchronizedSupplier() {
        Supplier<String> supplier = () -> "Hello";
        Supplier<String> synchronizedSupplier = Suppliers.synchronizedSupplier(supplier);

        Thread thread1 = new Thread(() -> assertEquals(synchronizedSupplier.get(), "Hello"));
        Thread thread2 = new Thread(() -> assertEquals(synchronizedSupplier.get(), "Hello"));

        thread1.start();
        thread2.start();

        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // Test for supplierFunction method (using a mock function)

    @Test
    public void testSupplierFunction() {
        Function<Supplier<Object>, Object> mockFunction = new Function<Supplier<Object>, Object>() {
            @Override
            public Object apply(Supplier<Object> input) {
                return input.get();
            }
        };

        Supplier<Object> supplier = Suppliers.ofInstance(new Object());
        Object expectedResult = Suppliers.supplierFunction().apply(supplier);

        assertEquals(expectedResult, supplier.get());
    }

    // Test for compose with null function and supplier

    @Test(expected = NullPointerException.class)
    public void testComposeWithNullFunction() {
        Function<Integer, Integer> function = null;
        Supplier<Integer> supplier = Suppliers.ofInstance(4);

        Suppliers.compose(function, supplier); // This should throw a NullPointerException
    }

    // Test for memoize with null supplier

    @Test(expected = NullPointerException.class)
    public void testMemoizeWithNullSupplier() {
        Supplier<String> supplier = null;

        Suppliers.memoize(supplier); // This should throw a NullPointerException
    }

    // Test for memoizeWithExpiration with negative duration

    @Test(expected = IllegalArgumentException.class)
    public void testMemoizeWithNegativeDuration() {
        Supplier<String> supplier = () -> "Hello";

        Suppliers.memoizeWithExpiration(supplier, -100, TimeUnit.MILLISECONDS); // This should throw an IllegalArgumentException
    }

    // Test for ofInstance with null instance

    @Test(expected = NullPointerException.class)
    public void testOfInstanceWithNullInstance() {
        Suppliers.ofInstance(null); // This should throw a NullPointerException
    }

    // Test for synchronizedSupplier with null supplier

    @Test(expected = NullPointerException.class)
    public void testSynchronizedSupplierWithNullSupplier() {
        Supplier<String> supplier = null;

        Suppliers.synchronizedSupplier(supplier); // This should throw a NullPointerException
    }
}