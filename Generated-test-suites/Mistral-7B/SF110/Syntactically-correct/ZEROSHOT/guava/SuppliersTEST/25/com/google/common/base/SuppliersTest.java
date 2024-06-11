package com.google.common.base.test;

import com.google.common.base.Suppliers;
import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.Supplier;

public class SuppliersTest {

    @Test
    public void testCompose() {
        Function<Integer, String> function = new Function<Integer, String>() {
            @Override
            public String apply(Integer input) {
                return "Function: " + input;
            }
        };

        Supplier<Integer> supplier = Suppliers.ofInstance(42);
        Supplier<String> composedSupplier = Suppliers.compose(function, supplier);

        String result = composedSupplier.get();
        Assert.assertEquals("Function: 42", result);
    }

    @Test
    public void testMemoize() {
        Supplier<Integer> supplier = () -> 42;
        Supplier<Integer> memoizedSupplier = Suppliers.memoize(supplier);

        Integer result1 = memoizedSupplier.get();
        Integer result2 = memoizedSupplier.get();

        Assert.assertEquals(result1, result2);
    }

    @Test
    public void testMemoizeWithExpiration() {
        Supplier<Long> supplier = () -> System.nanoTime();
        Supplier<Long> memoizedSupplierWithExpiration = Suppliers.memoizeWithExpiration(supplier, 100, TimeUnit.MILLISECONDS);

        Long result1 = memoizedSupplierWithExpiration.get();
        Long result2 = memoizedSupplierWithExpiration.get();

        Assert.assertNotSame(result1, result2);
    }

    @Test
    public void testOfInstance() {
        String instance = "Hello World";
        Supplier<String> supplier = Suppliers.ofInstance(instance);

        String result = supplier.get();
        Assert.assertEquals(instance, result);
    }

    @Test
    public void testSynchronizedSupplier() {
        Supplier<Integer> supplier = () -> 42;
        Supplier<Integer> synchronizedSupplier = Suppliers.synchronizedSupplier(supplier);

        Thread thread1 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                synchronizedSupplier.get();
            }
        });

        Thread thread2 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                synchronizedSupplier.get();
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
        Function<Supplier<String>, String> function = Suppliers.supplierFunction();
        Supplier<String> supplier = Suppliers.ofInstance("Hello World");
        String result = function.apply(supplier);

        Assert.assertEquals("Hello World", result);
    }
}