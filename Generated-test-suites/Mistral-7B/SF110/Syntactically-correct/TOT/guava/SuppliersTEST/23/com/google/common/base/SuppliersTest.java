package com.google.common.base.test;

import com.google.common.base.*;
import org.junit.*;

import java.util.concurrent.*;

import static com.google.common.base.Preconditions.*;
import static org.junit.Assert.*;

public class SuppliersTest {

    @Test
    public void testCompose() {
        Function<Integer, Integer> addOne = new Function<Integer, Integer>() {
            @Override
            public Integer apply(Integer input) {
                return input + 1;
            }
        };

        Supplier<Integer> supplierOfFive = Suppliers.ofInstance(5);
        Supplier<Integer> composedSupplier = Suppliers.compose(addOne, supplierOfFive);

        assertEquals(6, composedSupplier.get());
    }

    @Test
    public void testComposeNullFunction() {
        Supplier<Integer> supplierOfFive = Suppliers.ofInstance(5);

        try {
            Suppliers.compose(null, supplierOfFive);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test
    public void testComposeNullSupplier() {
        Function<Integer, Integer> addOne = new Function<Integer, Integer>() {
            @Override
            public Integer apply(Integer input) {
                return input + 1;
            }
        };

        try {
            Suppliers.compose(addOne, null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test
    public void testMemoize() {
        Supplier<Integer> supplierOfOne = Suppliers.ofInstance(1);
        Supplier<Integer> memoizedSupplier = Suppliers.memoize(supplierOfOne);

        assertEquals(1, memoizedSupplier.get());
        assertEquals(1, memoizedSupplier.get());
    }

    @Test
    public void testMemoizeNullSupplier() {
        try {
            Suppliers.memoize(null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test
    public void testMemoizeWithExpiration() {
        Supplier<Long> supplier = new Supplier<Long>() {
            @Override
            public Long get() {
                return System.nanoTime();
            }
        };

        Supplier<Long> memoizedSupplierWithExpiration = Suppliers.memoizeWithExpiration(supplier, 10, TimeUnit.MILLISECONDS);

        long initialValue = memoizedSupplierWithExpiration.get();

        try {
            Thread.sleep(15);
            memoizedSupplierWithExpiration.get();
            fail("Expected StaleStateException");
        } catch (StaleStateException e) {
            // Expected
        }

        assertEquals(initialValue, memoizedSupplierWithExpiration.get());
    }

    @Test
    public void testMemoizeWithExpirationNullSupplier() {
        try {
            Suppliers.memoizeWithExpiration(null, 10, TimeUnit.MILLISECONDS);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test
    public void testMemoizeWithExpirationZeroDuration() {
        Supplier<Long> supplier = new Supplier<Long>() {
            @Override
            public Long get() {
                return System.nanoTime();
            }
        };

        try {
            Suppliers.memoizeWithExpiration(supplier, 0, TimeUnit.MILLISECONDS);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testOfInstance() {
        Supplier<Integer> supplierOfFive = Suppliers.ofInstance(5);

        assertEquals(5, supplierOfFive.get());
        assertEquals(5, supplierOfFive.get());
    }

    @Test
    public void testOfInstanceNullObject() {
        try {
            Suppliers.ofInstance(null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test
    public void testSynchronizedSupplier() {
        final AtomicInteger counter = new AtomicInteger();
        Supplier<Integer> supplier = new Supplier<Integer>() {
            @Override
            public Integer get() {
                counter.incrementAndGet();
                return 42;
            }
        };

        Supplier<Integer> synchronizedSupplier = Suppliers.synchronizedSupplier(supplier);

        ExecutorService executor = Executors.newFixedThreadPool(10);
        for (int i = 0; i < 100; i++) {
            executor.submit(new Runnable() {
                @Override
                public void run() {
                    synchronizedSupplier.get();
                }
            });
        }

        executor.shutdown();
        try {
            executor.awaitTermination(1, TimeUnit.HOURS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        assertEquals(100, counter.get());
    }

    @Test
    public void testSupplierFunction() {
        Supplier<Integer> supplierOfFourtyTwo = Suppliers.ofInstance(42);
        Function<Supplier<Integer>, Integer> supplierFunction = Suppliers.supplierFunction();

        assertEquals(42, supplierFunction.apply(supplierOfFourtyTwo).intValue());
    }
}