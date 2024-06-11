package com.google.common.base;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.junit.Test;

import java.util.concurrent.Supplier;
import java.util.concurrent.TimeUnit;

public class SuppliersTest {

    @Test
    public void testCompose() {
        Function<String, Integer> addOne = new Function<String, Integer>() {
            @Override
            public Integer apply(String input) {
                return Integer.valueOf(1 + Integer.parseInt(input));
            }
        };

        Supplier<String> stringSupplier = Suppliers.ofInstance("1");
        Supplier<Integer> compositeSupplier = Suppliers.compose(addOne, stringSupplier);

        assertEquals(Integer.valueOf(2), compositeSupplier.get());
    }

    @Test
    public void testMemoize() {
        Function<Integer, Integer> squareFunction = new Function<Integer, Integer>() {
            @Override
            public Integer apply(Integer input) {
                return Integer.valueOf(input * input);
            }
        };

        Supplier<Integer> delegateSupplier = new Supplier<Integer>() {
            private int counter = 0;

            @Override
            public Integer get() {
                return Integer.valueOf(squareFunction.apply(counter++));
            }
        };

        Supplier<Integer> memoizedSupplier = Suppliers.memoize(delegateSupplier);

        assertEquals(Integer.valueOf(0), memoizedSupplier.get());
        assertEquals(Integer.valueOf(1), memoizedSupplier.get());
        assertEquals(Integer.valueOf(4), memoizedSupplier.get());
    }

    @Test
    public void testMemoizeWithExpiration() {
        Function<Integer, Integer> squareFunction = new Function<Integer, Integer>() {
            @Override
            public Integer apply(Integer input) {
                return Integer.valueOf(input * input);
            }
        };

        Supplier<Integer> delegateSupplier = new Supplier<Integer>() {
            private int counter = 0;

            @Override
            public Integer get() {
                return Integer.valueOf(squareFunction.apply(counter++));
            }
        };

        Supplier<Integer> memoizedSupplierWithExpiration = Suppliers.memoizeWithExpiration(delegateSupplier, 1, TimeUnit.SECONDS);

        assertEquals(Integer.valueOf(0), memoizedSupplierWithExpiration.get());
        assertEquals(Integer.valueOf(1), memoizedSupplierWithExpiration.get());
        assertEquals(Integer.valueOf(4), memoizedSupplierWithExpiration.get());

        try {
            Thread.sleep(1500);
            assertEquals(Integer.valueOf(4), memoizedSupplierWithExpiration.get());
        } catch (InterruptedException e) {
            // ignore
        }

        assertEquals(Integer.valueOf(9), memoizedSupplierWithExpiration.get());
    }

    @Test
    public void testOfInstance() {
        Supplier<String> stringSupplier = Suppliers.ofInstance("Hello");
        assertEquals("Hello", stringSupplier.get());
    }

    @Test
    public void testSynchronizedSupplier() {
        AtomicInteger counter = new AtomicInteger(0);

        Supplier<Integer> supplier = new Supplier<Integer>() {
            @Override
            public Integer get() {
                return Integer.valueOf(counter.incrementAndGet());
            }
        };

        Supplier<Integer> synchronizedSupplier = Suppliers.synchronizedSupplier(supplier);

        new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                synchronizedSupplier.get();
            }
        }).start();

        for (int i = 0; i < 1000; i++) {
            synchronizedSupplier.get();
        }

        assertEquals(2000, counter.get());
    }

    @Test
    public void testSupplierFunction() {
        Function<Supplier<Integer>, Integer> supplierFunction = Suppliers.supplierFunction();
        Supplier<Integer> integerSupplier = Suppliers.ofInstance(Integer.valueOf(1));

        assertEquals(Integer.valueOf(1), supplierFunction.apply(integerSupplier));
    }

    @Test(expected = NullPointerException.class)
    public void testNullFunction() {
        Function<? super String, Integer> nullFunction = null;

        Suppliers.compose(nullFunction, Suppliers.ofInstance("1"));
    }

    @Test(expected = NullPointerException.class)
    public void testNullSupplier() {
        Function<String, Integer> function = new Function<String, Integer>() {
            @Override
            public Integer apply(String input) {
                return Integer.valueOf(1);
            }
        };

        Suppliers.compose(function, null);
    }
}