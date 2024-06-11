package com.google.common.base.test;

import com.google.common.base.Suppliers;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.concurrent.TimeUnit;

public class SuppliersTest {

    private Function<Supplier<String>, String> function;
    private Supplier<String> supplier;

    @Before
    public void setup() {
        function = (Supplier<String> s) -> "Function Result: " + s.get();
        supplier = Suppliers.ofInstance("Supplier Instance");
    }

    @Test
    public void testCompose() {
        Supplier<String> composedSupplier = Suppliers.compose(function, supplier);
        assertEquals("Supplier Instance", composedSupplier.get());
        assertEquals("Function Result: Supplier Instance", function.apply(composedSupplier).toString());
    }

    @Test
    public void testMemoize() {
        Supplier<String> memoizedSupplier = Suppliers.memoize(supplier);
        assertEquals("Supplier Instance", memoizedSupplier.get());
        assertEquals("Supplier Instance", memoizedSupplier.get());
    }

    @Test
    public void testMemoizeWithExpiration() {
        long duration = 10;
        TimeUnit timeUnit = TimeUnit.MILLISECONDS;
        Supplier<String> expiringMemoizedSupplier = Suppliers.memoizeWithExpiration(supplier, duration, timeUnit);
        assertEquals("Supplier Instance", expiringMemoizedSupplier.get());
        try {
            Thread.sleep(duration + 10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertEquals("Supplier Instance", expiringMemoizedSupplier.get()); // should still work
        try {
            Thread.sleep(2 * (duration + 10));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertThrows(RuntimeException.class, () -> expiringMemoizedSupplier.get()); // should throw an exception
    }

    @Test
    public void testSynchronizedSupplier() {
        Supplier<String> synchronizedSupplier = Suppliers.synchronizedSupplier(supplier);
        Thread thread1 = new Thread(() -> assertEquals("Supplier Instance", synchronizedSupplier.get()));
        Thread thread2 = new Thread(() -> assertEquals("Supplier Instance", synchronizedSupplier.get()));
        thread1.start();
        thread2.start();
        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}