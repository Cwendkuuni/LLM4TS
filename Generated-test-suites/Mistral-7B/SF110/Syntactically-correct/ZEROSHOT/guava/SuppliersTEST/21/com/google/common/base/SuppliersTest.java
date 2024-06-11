package com.google.common.base;

import static org.junit.Assert.*;

import java.util.concurrent.*;

import org.junit.*;

@SuppressWarnings("unchecked")
public class SuppliersTest {

    @Test
    public void testCompose() {
        Function<Integer, Integer> addOne = new Function<Integer, Integer>() {
            @Override
            public Integer apply(Integer input) {
                return input + 1;
            }
        };

        Supplier<Integer> numberSupplier = Suppliers.ofInstance(1);
        Supplier<Integer> composedSupplier = Suppliers.compose(addOne, numberSupplier);

        assertEquals(2, composedSupplier.get());
    }

    @Test
    public void testMemoize() {
        Supplier<Integer> supplier = () -> 1;
        Supplier<Integer> memoizedSupplier = Suppliers.memoize(supplier);

        assertEquals(1, memoizedSupplier.get());
        assertEquals(1, memoizedSupplier.get());
    }

    @Test
    public void testMemoizeWithExpiration() {
        Supplier<Long> supplier = () -> System.nanoTime();
        Supplier<Long> memoizedSupplierWithExpiration = Suppliers.memoizeWithExpiration(supplier, 100, TimeUnit.MILLISECONDS);

        long initialValue = memoizedSupplierWithExpiration.get();
        long currentTime = System.nanoTime();

        assertEquals(initialValue, memoizedSupplierWithExpiration.get());
        assertEquals(initialValue, memoizedSupplierWithExpiration.get());

        try {
            Thread.sleep(110);
        } catch (InterruptedException e) {
            fail();
        }

        assertNotEquals(initialValue, memoizedSupplierWithExpiration.get());
        assertEquals(currentTime, memoizedSupplierWithExpiration.get());
    }

    @Test
    public void testOfInstance() {
        String testString = "Test String";
        Supplier<String> stringSupplier = Suppliers.ofInstance(testString);

        assertEquals(testString, stringSupplier.get());
    }

    @Test
    public void testSynchronizedSupplier() {
        AtomicInteger counter = new AtomicInteger(0);
        Supplier<Integer> supplier = new Supplier<Integer>() {
            @Override
            public Integer get() {
                return counter.incrementAndGet();
            }
        };

        Supplier<Integer> synchronizedSupplier = Suppliers.synchronizedSupplier(supplier);

        ExecutorService executor = Executors.newFixedThreadPool(10);

        for (int i = 0; i < 10; i++) {
            executor.submit(() -> synchronizedSupplier.get());
        }

        executor.shutdown();

        while (!executor.isTerminated()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                fail();
            }
        }

        assertEquals(10, counter.get());
    }
}