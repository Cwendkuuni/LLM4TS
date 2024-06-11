package com.google.common.base;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.junit.Assert.*;

import java.util.concurrent.*;
import javax.annotation.*;
import java.io.*;
import com.google.common.annotations.*;

@GwtCompatible
public final class SuppliersTest {

    @Test
    public void testCompose() {
        Function<Integer, Integer> function = new Function<Integer, Integer>() {
            @Override
            public Integer apply(Integer input) {
                return input + 1;
            }
        };
        Supplier<Integer> supplier = Suppliers.ofInstance(5);
        Supplier<Integer> composedSupplier = Suppliers.compose(function, supplier);

        assertEquals(6, composedSupplier.get());
        assertEquals(function, composedSupplier.getDelegateFunction());
        assertEquals(supplier, composedSupplier.getDelegateSupplier());
    }

    @Test
    public void testMemoize() {
        Supplier<Integer> delegate = new Supplier<Integer>() {
            private volatile int value;

            @Override
            public Integer get() {
                if (value == 0) {
                    value = 1;
                }
                return value;
            }
        };
        Supplier<Integer> memoizedSupplier = Suppliers.memoize(delegate);

        assertEquals(1, memoizedSupplier.get());
        assertEquals(1, memoizedSupplier.get());
        assertEquals(delegate, memoizedSupplier.getDelegateSupplier());
    }

    @Test
    public void testMemoizeWithExpiration() {
        Supplier<Long> delegate = new Supplier<Long>() {
            @Override
            public Long get() {
                return System.nanoTime();
            }
        };
        Supplier<Long> expiringMemoizedSupplier = Suppliers.memoizeWithExpiration(delegate, 10, TimeUnit.MILLISECONDS);

        long initialTime = expiringMemoizedSupplier.get();
        assertTrue(initialTime > 0);

        long currentTime;
        for (int i = 0; i < 10; i++) {
            currentTime = expiringMemoizedSupplier.get();
            assertTrue(currentTime > initialTime);
        }
    }

    @Test
    public void testOfInstance() {
        Supplier<String> instanceSupplier = Suppliers.ofInstance("hello");
        assertEquals("hello", instanceSupplier.get());
    }

    @Test
    public void testSynchronizedSupplier() {
        Supplier<Integer> delegate = new Supplier<Integer>() {
            private volatile int value;

            @Override
            public Integer get() {
                if (value == 0) {
                    value = 1;
                }
                return value;
            }
        };
        Supplier<Integer> synchronizedSupplier = Suppliers.synchronizedSupplier(delegate);

        Thread thread1 = new Thread(() -> assertEquals(1, synchronizedSupplier.get()));
        Thread thread2 = new Thread(() -> assertEquals(1, synchronizedSupplier.get()));
        thread1.start();
        thread2.start();

        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testSupplierFunction() {
        Supplier<String> supplier = Suppliers.ofInstance("supplierFunction");
        Function<Supplier<String>, String> function = Suppliers.supplierFunction();

        assertEquals("supplierFunction", function.apply(supplier).get());
    }
}