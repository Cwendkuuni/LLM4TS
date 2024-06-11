package com.google.common.base.test;

import com.google.common.base.Suppliers;
import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

public class SuppliersTest {

    // Test for compose method
    @Test
    public void testCompose() {
        Function<Integer, Integer> addOne = new Function<Integer, Integer>() {
            @Override
            public Integer apply(Integer input) {
                return input + 1;
            }
        };

        Supplier<Integer> supplierOfTwo = Suppliers.ofInstance(2);
        Supplier<Integer> composedSupplier = Suppliers.compose(addOne, supplierOfTwo);

        Assert.assertEquals(4, composedSupplier.get());
    }

    // Test for memoize method with a simple supplier
    @Test
    public void testMemoizeSimpleSupplier() {
        Supplier<Integer> supplier = () -> 1;
        Supplier<Integer> memoizedSupplier = Suppliers.memoize(supplier);

        Assert.assertEquals(1, memoizedSupplier.get());
        Assert.assertEquals(1, memoizedSupplier.get());
    }

    // Test for memoize method with an expensive supplier
    @Test
    public void testMemoizeExpensiveSupplier() throws Exception {
        final Callable<Integer> expensiveFunction = () -> {
            Thread.sleep(1000);
            return 42;
        };

        Supplier<Integer> supplier = Suppliers.memoize(Suppliers.ofCallable(expensiveFunction));

        Assert.assertEquals(42, supplier.get());
        Assert.assertEquals(42, supplier.get());
    }

    // Test for memoizeWithExpiration method with a simple supplier
    @Test
    public void testMemoizeWithExpirationSimpleSupplier() {
        Supplier<Integer> supplier = () -> 1;
        Supplier<Integer> memoizedSupplierWithExpiration = Suppliers.memoizeWithExpiration(supplier, 1, TimeUnit.SECONDS);

        Assert.assertEquals(1, memoizedSupplierWithExpiration.get());
        Assert.assertEquals(1, memoizedSupplierWithExpiration.get());

        Thread.sleep(2000);

        Assert.assertEquals(1, memoizedSupplierWithExpiration.get());
        Assert.assertEquals(1, memoizedSupplierWithExpiration.get());

        Thread.sleep(3000);

        Assert.assertNotEquals(1, memoizedSupplierWithExpiration.get());
    }

    // Test for memoizeWithExpiration method with an expensive supplier
    @Test
    public void testMemoizeWithExpirationExpensiveSupplier() throws Exception {
        final Callable<Integer> expensiveFunction = () -> {
            Thread.sleep(1000);
            return 42;
        };

        Supplier<Integer> supplier = Suppliers.memoizeWithExpiration(Suppliers.ofCallable(expensiveFunction), 1, TimeUnit.SECONDS);

        Assert.assertEquals(42, supplier.get());
        Assert.assertEquals(42, supplier.get());

        Thread.sleep(3000);

        Assert.assertNotEquals(42, supplier.get());

        Thread.sleep(5000);

        Assert.assertEquals(42, supplier.get());
    }

    // Test for ofInstance method
    @Test
    public void testOfInstance() {
        Supplier<String> supplierOfNull = Suppliers.ofInstance(null);
        Assert.assertNull(supplierOfNull.get());

        Supplier<String> supplierOfEmptyString = Suppliers.ofInstance("");
        Assert.assertEquals("", supplierOfEmptyString.get());

        Supplier<String> supplierOfHelloWorld = Suppliers.ofInstance("Hello World");
        Assert.assertEquals("Hello World", supplierOfHelloWorld.get());
    }

    // Test for synchronizedSupplier method
    @Test
    public void testSynchronizedSupplier() {
        final int counter = 0;
        Supplier<Integer> supplier = new Supplier<Integer>() {
            @Override
            public Integer get() {
                counter++;
                return counter;
            }
        };

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

        Assert.assertEquals(2000, counter);
    }

    // Test for supplierFunction method
    @Test
    public void testSupplierFunction() {
        Function<Supplier<Integer>, Integer> supplierFunction = Suppliers.supplierFunction();
        Supplier<Integer> supplierOfFive = Suppliers.ofInstance(5);

        Assert.assertEquals(5, supplierFunction.apply(supplierOfFive).intValue());
    }
}