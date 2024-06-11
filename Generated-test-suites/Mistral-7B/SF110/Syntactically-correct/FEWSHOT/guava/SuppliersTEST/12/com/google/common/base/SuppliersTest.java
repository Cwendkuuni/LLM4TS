package com.google.common.base;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.concurrent.Supplier;
import javax.annotation.Nullable;

import org.junit.Test;

public class SuppliersTest {

    @Test
    public void testCompose() {
        Function<Integer, String> function = new Function<Integer, String>() {
            @Override
            public String apply(Integer input) {
                return "The input is: " + input.toString();
            }
        };

        Supplier<Integer> supplier = Suppliers.ofInstance(1);
        Supplier<String> composedSupplier = Suppliers.compose(function, supplier);

        assertEquals("The input is: 1", composedSupplier.get());
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
    }

    @Test
    public void testMemoizeWithExpiration() {
        Supplier<Long> delegate = () -> System.nanoTime();
        Supplier<Long> expiringMemoizedSupplier = Suppliers.memoizeWithExpiration(delegate, 10, TimeUnit.MILLISECONDS);

        long initialValue = expiringMemoizedSupplier.get();
        long newValue = expiringMemoizedSupplier.get();

        assertEquals(initialValue, newValue);

        try {
            Thread.sleep(15);
            newValue = expiringMemoizedSupplier.get();
            assertEquals(initialValue, newValue);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        newValue = expiringMemoizedSupplier.get();
        assertNotEquals(initialValue, newValue);
    }

    @Test
    public void testOfInstance() {
        Supplier<String> supplier = Suppliers.ofInstance("Hello World");
        assertEquals("Hello World", supplier.get());
    }

    @Test
    public void testSynchronizedSupplier() {
        Supplier<Integer> delegate = () -> new Random().nextInt();
        Supplier<Integer> synchronizedSupplier = Suppliers.synchronizedSupplier(delegate);

        int firstValue = synchronizedSupplier.get();
        int secondValue = synchronizedSupplier.get();

        assertEquals(firstValue, secondValue);
    }

    @Test(expected = NullPointerException.class)
    public void testComposeNullFunction() {
        Supplier<Integer> supplier = Suppliers.ofInstance(1);
        Function<Supplier<Object>, Object> function = null;

        Suppliers.compose(function, supplier);
    }

    @Test(expected = NullPointerException.class)
    public void testComposeNullSupplier() {
        Function<Integer, String> function = i -> "The input is: " + i.toString();
        Supplier<Integer> supplier = null;

        Suppliers.compose(function, supplier);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMemoizeWithNegativeDuration() {
        Supplier<String> delegate = () -> "Hello World";
        long duration = -10;

        Suppliers.memoizeWithExpiration(delegate, duration, TimeUnit.MILLISECONDS);
    }
}