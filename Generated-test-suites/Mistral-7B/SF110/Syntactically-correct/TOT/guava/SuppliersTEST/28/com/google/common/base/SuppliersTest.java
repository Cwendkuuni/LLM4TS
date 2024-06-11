package com.google.common.base;

import static org.junit.Assert.*;

import java.util.concurrent.TimeUnit;

import org.junit.Test;

import com.google.common.base.Function;
import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;

public class SuppliersTest {

    // Test for compose method
    @Test
    public void testCompose() {
        Function<Integer, String> func1 = new Function<Integer, String>() {
            @Override
            public String apply(Integer input) {
                return "Function 1: " + input;
            }
        };

        Supplier<Integer> supplier1 = Suppliers.ofInstance(10);

        Supplier<String> expectedSupplier = new Supplier<String>() {
            @Override
            public String get() {
                return "Function 1: 10";
            }
        };

        Supplier<String> supplierComposition = Suppliers.compose(func1, supplier1);

        assertEquals(expectedSupplier, supplierComposition);
        assertEquals("Function 1: 10", supplierComposition.get());
    }

    // Test for memoize method
    @Test
    public void testMemoize() {
        Supplier<Integer> supplier1 = new Supplier<Integer>() {
            private int counter = 0;

            @Override
            public Integer get() {
                return ++counter;
            }
        };

        Supplier<Integer> memoizedSupplier = Suppliers.memoize(supplier1);

        assertEquals(supplier1, memoizedSupplier);
        assertEquals(1, memoizedSupplier.get());
        assertEquals(1, memoizedSupplier.get());
    }

    // Test for memoizeWithExpiration method
    @Test
    public void testMemoizeWithExpiration() throws Exception {
        Supplier<Long> supplier1 = new Supplier<Long>() {
            @Override
            public Long get() {
                return System.nanoTime();
            }
        };

        Supplier<Long> memoizedSupplierWithExpiration = Suppliers.memoizeWithExpiration(supplier1, 1, TimeUnit.SECONDS);

        long currentTime = System.nanoTime();

        Long result1 = memoizedSupplierWithExpiration.get();
        Long result2 = memoizedSupplierWithExpiration.get();

        // First call should be instant
        long timeDifference1 = currentTime - result1.longValue();
        assertTrue(timeDifference1 <= 0);

        // Second call should return cached value for one second
        Thread.sleep(1000);

        Long result3 = memoizedSupplierWithExpiration.get();
        Long result4 = memoizedSupplierWithExpiration.get();

        // Third and fourth call should be instant as cache is still valid
        long timeDifference2 = currentTime - result3.longValue();
        assertTrue(timeDifference2 <= 0);
        long timeDifference3 = currentTime - result4.longValue();
        assertTrue(timeDifference3 <= 0);

        // Fifth call should get a new value as cache has expired
        Thread.sleep(2000);

        Long result5 = memoizedSupplierWithExpiration.get();
        Long result6 = memoizedSupplierWithExpiration.get();

        // Fifth and sixth call should be instant as new value is cached
        long timeDifference4 = currentTime - result5.longValue();
        assertTrue(timeDifference4 <= 0);
        long timeDifference5 = currentTime - result6.longValue();
        assertTrue(timeDifference5 <= 0);
    }

    // Test for ofInstance method
    @Test
    public void testOfInstance() {
        Supplier<String> supplier1 = Suppliers.ofInstance("Test String");

        assertEquals("Test String", supplier1.get());
    }

    // Test for synchronizedSupplier method
    @Test
    public void testSynchronizedSupplier() {
        Supplier<String> supplier1 = new Supplier<String>() {
            private volatile String value = null;

            @Override
            public String get() {
                if (value == null) {
                    value = "Synchronized Supplier";
                }
                return value;
            }
        };

        Supplier<String> synchronizedSupplier = Suppliers.synchronizedSupplier(supplier1);

        Thread thread1 = new Thread(() -> {
            for (int i = 0; i < 100; i++) {
                assertEquals("Synchronized Supplier", synchronizedSupplier.get());
            }
        });

        Thread thread2 = new Thread(() -> {
            for (int i = 0; i < 100; i++) {
                assertEquals("Synchronized Supplier", synchronizedSupplier.get());
            }
        });

        thread1.start();
        thread2.start();

        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // Test for supplierFunction method
    @Test
    public void testSupplierFunction() {
        Supplier<String> supplier1 = Suppliers.ofInstance("Test String");
        Function<Supplier<String>, String> supplierFunction = Suppliers.supplierFunction();

        String result = supplierFunction.apply(supplier1);

        assertEquals("Test String", result);
    }
}