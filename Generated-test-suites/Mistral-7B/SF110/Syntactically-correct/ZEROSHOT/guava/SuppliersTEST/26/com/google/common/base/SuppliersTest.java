package com.google.common.base.test;

import static org.junit.Assert.*;

import java.util.concurrent.*;

import org.junit.*;

import com.google.common.base.*;
import com.google.common.base.test.util.FunctionTestUtil;
import com.google.common.base.test.util.SupplierTestUtil;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class SuppliersTest {

    @Test
    public void testCompose() {
        Function<Integer, String> function = new Function<Integer, String>() {
            @Override
            public String apply(Integer input) {
                return "Function: " + input;
            }
        };

        Supplier<Integer> supplier = Suppliers.ofInstance(1);
        Supplier<String> expected = Suppliers.compose(function, supplier);

        Supplier<String> actual = Suppliers.compose(FunctionTestUtil.function("FunctionTestUtil.addOne"), SupplierTestUtil.supplierOfInstance(1));

        assertEquals(expected, actual);
    }

    @Test
    public void testMemoize() {
        Supplier<String> supplier = Suppliers.ofInstance("Hello");
        Supplier<String> memoizedSupplier = Suppliers.memoize(supplier);

        String result1 = memoizedSupplier.get();
        String result2 = memoizedSupplier.get();

        assertEquals(result1, result2);
    }

    @Test
    public void testMemoizeWithExpiration() {
        Supplier<String> supplier = Suppliers.ofInstance("Hello");
        Supplier<String> memoizedSupplier = Suppliers.memoizeWithExpiration(supplier, 100, TimeUnit.MILLISECONDS);

        String result1 = memoizedSupplier.get();
        String result2 = memoizedSupplier.get();

        assertEquals(result1, result2);

        try {
            Thread.sleep(110);
            String result3 = memoizedSupplier.get();
            fail("Expected IllegalStateException, but got: " + result3);
        } catch (InterruptedException e) {
            // Ignore
        } catch (IllegalStateException e) {
            // Expected
        }
    }

    @Test
    public void testOfInstance() {
        Supplier<String> supplier = Suppliers.ofInstance("Hello");

        String result = supplier.get();
        assertEquals("Hello", result);
    }

    @Test
    public void testSynchronizedSupplier() {
        Supplier<String> supplier = Suppliers.ofInstance("Hello");
        Supplier<String> synchronizedSupplier = Suppliers.synchronizedSupplier(supplier);

        Thread thread1 = new Thread(() -> {
            String result1 = synchronizedSupplier.get();
            assertEquals("Hello", result1);
        });

        Thread thread2 = new Thread(() -> {
            String result2 = synchronizedSupplier.get();
            assertEquals("Hello", result2);
        });

        thread1.start();
        thread2.start();

        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            // Ignore
        }
    }
}