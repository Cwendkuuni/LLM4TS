package com.google.common.base.test;

import com.google.common.base.Suppliers;
import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.Supplier;

public class SuppliersTest {

    // Test for compose method
    @Test
    public void testCompose() {
        Function<Integer, Integer> addOne = x -> x + 1;
        Supplier<Integer> supplier = Suppliers.ofInstance(5);
        Supplier<Integer> composedSupplier = Suppliers.compose(addOne, supplier);
        Assert.assertEquals(6, composedSupplier.get());
    }

    // Test for memoize method
    @Test
    public void testMemoize() {
        Function<String, Integer> lengthFunction = String::length;
        Supplier<String> stringSupplier = Suppliers.ofInstance("Google");
        Supplier<Integer> memoizedSupplier = Suppliers.memoize(Suppliers.supplierFunction().andThen(stringSupplier));
        Assert.assertEquals(6, memoizedSupplier.get()); // First call
        Assert.assertEquals(6, memoizedSupplier.get()); // Second call
    }

    // Test for memoizeWithExpiration method
    @Test
    public void testMemoizeWithExpiration() {
        Function<String, Integer> lengthFunction = String::length;
        Supplier<String> stringSupplier = Suppliers.ofInstance("Google");
        Supplier<Integer> memoizedWithExpirationSupplier = Suppliers.memoizeWithExpiration(Suppliers.supplierFunction().andThen(stringSupplier), 100, TimeUnit.MILLISECONDS);
        Assert.assertEquals(6, memoizedWithExpirationSupplier.get()); // First call
        try {
            Thread.sleep(150);
            Assert.assertEquals(6, memoizedWithExpirationSupplier.get()); // Second call within expiration time
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            Thread.sleep(300);
            Assert.assertEquals(6, memoizedWithExpirationSupplier.get()); // Third call after expiration time
            Assert.assertEquals(7, memoizedWithExpirationSupplier.get()); // Fourth call after expiration time
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // Test for ofInstance method
    @Test
    public void testOfInstance() {
        Supplier<String> stringSupplier = Suppliers.ofInstance("Google");
        Assert.assertEquals("Google", stringSupplier.get());
    }

    // Test for synchronizedSupplier method
    @Test
    public void testSynchronizedSupplier() {
        Function<Integer, Integer> addOne = x -> x + 1;
        Supplier<Integer> supplier = Suppliers.ofInstance(5);
        Supplier<Integer> synchronizedSupplier = Suppliers.synchronizedSupplier(Suppliers.compose(addOne, supplier));
        Assert.assertEquals(6, synchronizedSupplier.get()); // First thread
        Assert.assertEquals(6, synchronizedSupplier.get()); // Second thread
    }
}