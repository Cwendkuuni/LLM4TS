package org.apache.commons.cli.test; // Change the package name if necessary

import static org.junit.Assert.*;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import com.google.common.base.*;
import com.google.common.collect.ImmutableList;
import org.junit.Test;

import java.util.*;
import java.util.concurrent.*;

public class SuppliersTest {

    @Test
    public void testCompose() {
        Function<String, Integer> function = new Function<String, Integer>() {
            @Override
            public Integer apply(String input) {
                return input.length();
            }
        };

        Supplier<String> supplier = Suppliers.ofInstance("Test");

        Supplier<Integer> composedSupplier = Suppliers.compose(function, supplier);

        assertEquals(4, composedSupplier.get());
    }

    @Test
    public void testMemoize() {
        Supplier<Integer> delegate = new Supplier<Integer>() {
            private int count = 0;

            @Override
            public Integer get() {
                return ++count;
            }
        };

        Supplier<Integer> memoizedSupplier = Suppliers.memoize(delegate);

        assertEquals(1, memoizedSupplier.get());
        assertEquals(1, memoizedSupplier.get());
        assertEquals(2, memoizedSupplier.get());
    }

    @Test
    public void testMemoizeWithExpiration() {
        Supplier<Long> delegate = new Supplier<Long>() {
            @Override
            public Long get() {
                return System.nanoTime();
            }
        };

        Supplier<Long> memoizedSupplierWithExpiration = Suppliers.memoizeWithExpiration(delegate, 100, TimeUnit.MILLISECONDS);

        long initialTime = memoizedSupplierWithExpiration.get();
        Thread.sleep(150);

        long currentTime = memoizedSupplierWithExpiration.get();
        assertEquals(initialTime, currentTime);
    }

    @Test
    public void testOfInstance() {
        Supplier<String> supplier = Suppliers.ofInstance("Test");

        assertEquals("Test", supplier.get());
    }

    @Test
    public void testSynchronizedSupplier() {
        AtomicInteger atomicInteger = new AtomicInteger(0);
        Supplier<Integer> supplier = () -> atomicInteger.getAndIncrement();

        Supplier<Integer> synchronizedSupplier = Suppliers.synchronizedSupplier(supplier);

        List<Thread> threads = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Thread thread = new Thread(() -> {
                for (int j = 0; j < 1000; j++) {
                    synchronizedSupplier.get();
                }
            });
            threads.add(thread);
            thread.start();
        }

        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        assertEquals(10000, atomicInteger.get());
    }

    @Test
    public void testSupplierFunction() {
        Function<Supplier<Integer>, Integer> supplierFunction = Suppliers.supplierFunction();

        Supplier<Integer> supplier = Suppliers.ofInstance(123);

        int result = supplierFunction.apply(supplier);

        assertEquals(123, result);
    }
}