package com.google.common.base.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.concurrent.TimeUnit;

import org.junit.Test;

import com.google.common.base.Function;
import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;

public class SuppliersTest {

    // Test compose method
    @Test
    public void testCompose() {
        Function<Integer, String> function = new Function<Integer, String>() {
            @Override
            public String apply(Integer input) {
                return "Result: " + input;
            }
        };
        Supplier<Integer> supplier = Suppliers.ofInstance(1);
        Supplier<String> composedSupplier = Suppliers.compose(function, supplier);
        String result = composedSupplier.get();
        assertEquals("Result: 1", result);
    }

    // Test memoize method
    @Test
    public void testMemoize() {
        Supplier<Integer> supplier = () -> 1;
        Supplier<Integer> memoizedSupplier = Suppliers.memoize(supplier);
        Integer firstResult = memoizedSupplier.get();
        Integer secondResult = memoizedSupplier.get();
        assertEquals(firstResult, secondResult);
    }

    // Test memoizeWithExpiration method
    @Test
    public void testMemoizeWithExpiration() throws InterruptedException {
        Supplier<Long> supplier = () -> System.nanoTime();
        Supplier<Long> memoizedSupplierWithExpiration = Suppliers.memoizeWithExpiration(supplier, 1, TimeUnit.SECONDS);
        Long firstResult = memoizedSupplierWithExpiration.get();
        Long secondResult = memoizedSupplierWithExpiration.get();
        assertEquals(firstResult, secondResult);
        Thread.sleep(1500L);
        Long thirdResult = memoizedSupplierWithExpiration.get();
        assertNotSame(firstResult, thirdResult);
    }

    // Test ofInstance method
    @Test
    public void testOfInstance() {
        String instance = "Test Instance";
        Supplier<String> supplier = Suppliers.ofInstance(instance);
        String result = supplier.get();
        assertEquals(instance, result);
    }

    // Test synchronizedSupplier method
    @Test
    public void testSynchronizedSupplier() throws InterruptedException {
        Supplier<Integer> supplier = () -> 1;
        Supplier<Integer> synchronizedSupplier = Suppliers.synchronizedSupplier(supplier);
        Thread thread1 = new Thread(() -> synchronizedSupplier.get());
        Thread thread2 = new Thread(() -> synchronizedSupplier.get());
        thread1.start();
        thread2.start();
        thread1.join();
        thread2.join();
        Integer firstResult = thread1.getResult();
        Integer secondResult = thread2.getResult();
        assertEquals(firstResult, secondResult);
    }
}