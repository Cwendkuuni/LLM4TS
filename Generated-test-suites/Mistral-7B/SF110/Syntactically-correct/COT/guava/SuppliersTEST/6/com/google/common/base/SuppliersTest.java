package com.google.common.base.test;

import com.google.common.base.Supplier;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import static org.junit.Assert.*;
import com.google.common.base.Preconditions;
import java.util.concurrent.*;

@RunWith(JUnit4.class)
public class SuppliersTest {

    // Test for compose method
    @Test
    public void testCompose() {
        Function<Integer, Integer> function = new Function<Integer, Integer>() {
            @Override
            public Integer apply(Integer input) {
                return input + 1;
            }
        };
        Supplier<Integer> supplier = Suppliers.ofInstance(1);
        Supplier<Integer> expectedResult = Suppliers.compose(function, supplier);

        assertEquals(2, expectedResult.get());
    }

    // Test for memoize method
    @Test
    public void testMemoize() {
        Supplier<String> delegate = new Supplier<String>() {
            private String value = "Hello";
            
            @Override
            public String get() {
                return value;
            }
        };
        Supplier<String> memoizedSupplier = Suppliers.memoize(delegate);

        assertEquals("Hello", memoizedSupplier.get());
        assertEquals("Hello", memoizedSupplier.get());
    }

    // Test for memoizeWithExpiration method
    @Test
    public void testMemoizeWithExpiration() throws InterruptedException {
        Supplier<Long> delegate = new Supplier<Long>() {
            private volatile long value = 0L;
            
            @Override
            public Long get() {
                if (value == 0L) {
                    value = 1L;
                    Thread.sleep(100);
                }
                return value;
            }
        };
        Supplier<Long> memoizedSupplier = Suppliers.memoizeWithExpiration(delegate, 100, TimeUnit.MILLISECONDS);

        long result1 = memoizedSupplier.get();
        long result2 = memoizedSupplier.get();

        assertEquals(result1, result2);

        Thread.sleep(150);
        long result3 = memoizedSupplier.get();

        assertNotEquals(result1, result3);
    }

    // Test for ofInstance method
    @Test
    public void testOfInstance() {
        Supplier<String> supplier = Suppliers.ofInstance("Test");

        assertEquals("Test", supplier.get());
    }

    // Test for synchronizedSupplier method
    @Test
    public void testSynchronizedSupplier() {
        Supplier<String> delegate = Suppliers.ofInstance("Test");
        Supplier<String> synchronizedSupplier = Suppliers.synchronizedSupplier(delegate);

        Thread thread1 = new Thread(() -> assertEquals("Test", synchronizedSupplier.get()));
        Thread thread2 = new Thread(() -> assertEquals("Test", synchronizedSupplier.get()));

        thread1.start();
        thread2.start();

        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}