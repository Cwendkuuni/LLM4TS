package com.google.common.base;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.util.concurrent.Supplier;

import org.junit.Test;

public class SuppliersTest {

    @Test
    public void testCompose() {
        Function<Integer, String> function = new Function<Integer, String>() {
            @Override
            public String apply(Integer input) {
                return "Hello " + input;
            }
        };

        Supplier<Integer> supplier = Suppliers.ofInstance(42);
        Supplier<String> composedSupplier = Suppliers.compose(function, supplier);

        assertEquals("Hello 42", composedSupplier.get());
    }

    @Test
    public void testMemoize() {
        Supplier<Integer> supplier = () -> 42;
        Supplier<Integer> memoizedSupplier = Suppliers.memoize(supplier);

        assertEquals(42, memoizedSupplier.get());
        assertEquals(42, memoizedSupplier.get());
    }

    @Test
    public void testMemoizeWithExpiration() {
        Function<Long, String> function = new Function<Long, String>() {
            @Override
            public String apply(Long input) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return "Result for " + input;
            }
        };

        Supplier<Long> supplier = Suppliers.ofInstance(1L);
        long duration = 1000L;
        TimeUnit timeUnit = TimeUnit.MILLISECONDS;
        Supplier<String> memoizedWithExpirationSupplier = Suppliers.memoizeWithExpiration(supplier, duration, timeUnit);

        assertEquals("Result for 1", memoizedWithExpirationSupplier.get());
        assertEquals("Result for 1", memoizedWithExpirationSupplier.get());

        try {
            Thread.sleep(duration + 100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        assertEquals("Result for 1", memoizedWithExpirationSupplier.get());
        assertEquals("Result for 2", memoizedWithExpirationSupplier.get());
    }

    @Test
    public void testOfInstance() {
        Supplier<String> supplier = Suppliers.ofInstance("Hello");
        assertEquals("Hello", supplier.get());
    }

    @Test
    public void testSynchronizedSupplier() {
        Supplier<Integer> supplier = () -> 42;
        Supplier<Integer> synchronizedSupplier = Suppliers.synchronizedSupplier(supplier);

        Thread thread1 = new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                assertEquals(42, synchronizedSupplier.get());
            }
        });

        Thread thread2 = new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                assertEquals(42, synchronizedSupplier.get());
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

    @Test
    public void testSupplierFunction() {
        Function<Supplier<Object>, Object> supplierFunction = Suppliers.supplierFunction();
        Supplier<Object> supplier = Suppliers.ofInstance(new Object());

        assertEquals(supplier.get(), supplierFunction.apply(supplier));
    }

    @Test(expected = NullPointerException.class)
    public void testNullFunction() {
        Supplier<Integer> supplier = Suppliers.ofInstance(42);
        Function<Supplier<Integer>, Integer> function = null;
        Supplier<Integer> composedSupplier = Suppliers.compose(function, supplier);
    }

    @Test(expected = NullPointerException.class)
    public void testNullDelegate() {
        Function<Supplier<Object>, Object> function = new Function<Supplier<Object>, Object>() {
            @Override
            public Object apply(Supplier<Object> input) {
                return input.get();
            }
        };
        Supplier<Object> supplier = null;
        Supplier<Object> composedSupplier = Suppliers.compose(function, supplier);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeExpirationDuration() {
        Supplier<String> supplier = () -> "Result";
        long duration = -10L;
        TimeUnit timeUnit = TimeUnit.MILLISECONDS;
        Supplier<String> memoizedWithExpirationSupplier = Suppliers.memoizeWithExpiration(supplier, duration, timeUnit);
    }
}