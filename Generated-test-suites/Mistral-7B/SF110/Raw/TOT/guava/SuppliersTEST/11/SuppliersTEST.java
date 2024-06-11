package com.google.common.base;

import static org.junit.Assert.*;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.junit.Test;

import com.google.common.base.Function;
import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;

public class SuppliersTest {

    @Test
    public void testCompose() throws Exception {
        Function<String, Integer> addOne = new Function<String, Integer>() {
            @Override
            public Integer apply(String input) {
                return Integer.valueOf(Integer.parseInt(input) + 1);
            }
        };
        Supplier<String> supplier = Suppliers.ofInstance("5");
        Supplier<Integer> composedSupplier = Suppliers.compose(addOne, supplier);

        Integer result = composedSupplier.get();
        assertEquals(Integer.valueOf(6), result);
    }

    @Test
    public void testMemoize() throws Exception {
        Supplier<Long> supplier = new Supplier<Long>() {
            long counter = 0;
            @Override
            public Long get() {
                return Long.valueOf(++counter);
            }
        };
        Supplier<Long> memoizedSupplier = Suppliers.memoize(supplier);

        Long result1 = memoizedSupplier.get();
        Long result2 = memoizedSupplier.get();
        Long result3 = memoizedSupplier.get();

        assertEquals(result1, result2);
        assertEquals(result1, result3);
    }

    @Test
    public void testMemoizeWithExpiration() throws InterruptedException, ExecutionException {
        Callable<String> expensiveCallable = new Callable<String>() {
            @Override
            public String call() throws Exception {
                Thread.sleep(100);
                return "Expensive Result";
            }
        };
        Supplier<String> supplier = new Supplier<String>() {
            @Override
            public String get() throws Exception {
                return expensiveCallable.call();
            }
        };
        Supplier<String> memoizedSupplierWithExpiration = Suppliers.memoizeWithExpiration(supplier, 500, TimeUnit.MILLISECONDS);

        String result1 = memoizedSupplierWithExpiration.get();
        String result2 = memoizedSupplierWithExpiration.get();
        String result3 = memoizedSupplierWithExpiration.get();

        assertEquals(result1, result2);
        assertEquals(result1, result3);

        Thread.sleep(1050);
        String result4 = memoizedSupplierWithExpiration.get();

        assertNotEquals(result1, result4);
    }

    @Test(expected = NullPointerException.class)
    public void testMemoizeNullDelegate() {
        Suppliers.memoize(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMemoizeWithNegativeDuration() {
        Supplier<Long> supplier = new Supplier<Long>() {
            @Override
            public Long get() {
                return Long.valueOf(1L);
            }
        };
        Suppliers.memoizeWithExpiration(supplier, -1000, TimeUnit.MILLISECONDS);
    }

    @Test
    public void testOfInstance() {
        Supplier<String> supplier = Suppliers.ofInstance(null);
        assertNull(supplier.get());
        supplier = Suppliers.ofInstance("Hello");
        assertEquals("Hello", supplier.get());
    }

    @Test
    public void testSynchronizedSupplier() {
        Supplier<Long> supplier = new Supplier<Long>() {
            long counter = 0;
            @Override
            public Long get() {
                return Long.valueOf(++counter);
            }
        };
        Supplier<Long> synchronizedSupplier = Suppliers.synchronizedSupplier(supplier);

        Future<Long> future1 = new Callable<Long>() {
            @Override
            public Long call() {
                return synchronizedSupplier.get();
            }
        }.call();
        Future<Long> future2 = new Callable<Long>() {
            @Override
            public Long call() {
                return synchronizedSupplier.get();
            }
        }.call();

        Long result1 = future1.get();
        Long result2 = future2.get();

        assertEquals(result1, result2);
    }
}