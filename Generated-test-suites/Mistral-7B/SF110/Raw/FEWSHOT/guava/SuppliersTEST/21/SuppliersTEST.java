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
        Function<String, Integer> function = new Function<String, Integer>() {
            @Override
            public Integer apply(String input) {
                return input.length();
            }
        };

        Supplier<String> supplier = Suppliers.ofInstance("test");

        Supplier<Integer> expectedSupplier = new Supplier<Integer>() {
            @Override
            public Integer get() {
                return 5;
            }
        };

        Supplier<Integer> actualSupplier = Suppliers.compose(function, supplier);

        assertEquals(expectedSupplier, actualSupplier);
    }

    @Test
    public void testMemoize() {
        Supplier<Long> delegate = Suppliers.ofInstance(1L);
        Supplier<Long> memoizedSupplier = Suppliers.memoize(delegate);

        Long result1 = memoizedSupplier.get();
        Long result2 = memoizedSupplier.get();

        assertEquals(result1, result2);
    }

    @Test
    public void testMemoizeWithExpiration() {
        Supplier<Long> delegate = Suppliers.ofInstance(1L);
        Supplier<Long> memoizedSupplier = Suppliers.memoizeWithExpiration(delegate, 100, TimeUnit.MILLISECONDS);

        Long result1 = memoizedSupplier.get();
        Long result2 = memoizedSupplier.get();

        assertEquals(result1, result2);

        try {
            Thread.sleep(150);
            Long result3 = memoizedSupplier.get();
            fail("Expected exception: UnsupportedOperationException");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (UnsupportedOperationException e) {
            // Expected
        }
    }

    @Test
    public void testOfInstance() {
        Supplier<String> delegate = Suppliers.ofInstance("test");

        String result = delegate.get();
        assertEquals("test", result);
    }

    @Test
    public void testSynchronizedSupplier() {
        Supplier<Integer> delegate = Suppliers.ofInstance(1);
        Supplier<Integer> synchronizedSupplier = Suppliers.synchronizedSupplier(delegate);

        Thread thread1 = new Thread(() -> synchronizedSupplier.get());
        Thread thread2 = new Thread(() -> synchronizedSupplier.get());

        thread1.start();
        thread2.start();

        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        Integer result1 = delegate.get();
        Integer result2 = delegate.get();

        assertEquals(result1, result2);
    }

    @Test(expected = NullPointerException.class)
    public void testComposeNullFunction() {
        Supplier<String> supplier = Suppliers.ofInstance("test");
        Function<String, Integer> function = null;

        Supplier<Integer> result = Suppliers.compose(function, supplier);
    }

    @Test(expected = NullPointerException.class)
    public void testComposeNullSupplier() {
        Function<String, Integer> function = new Function<String, Integer>() {
            @Override
            public Integer apply(String input) {
                return input.length();
            }
        };
        Supplier<String> supplier = null;

        Supplier<Integer> result = Suppliers.compose(function, supplier);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMemoizeWithNegativeDuration() {
        Supplier<Long> delegate = Suppliers.ofInstance(1L);

        try {
            Suppliers.memoizeWithExpiration(delegate, -100, TimeUnit.MILLISECONDS);
            fail("Expected exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }
}