package com.google.common.base;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.concurrent.TimeUnit;

import org.junit.Test;

public class SuppliersTest {

    @Test
    public void testCompose() {
        Function<Integer, String> function = new Function<Integer, String>() {
            @Override
            public String apply(Integer input) {
                return input.toString();
            }
        };

        Supplier<Integer> supplier = Suppliers.ofInstance(1);
        Supplier<String> supplierComposition = Suppliers.compose(function, supplier);

        assertEquals("1", supplierComposition.get());
    }

    @Test
    public void testMemoize() {
        Function<Integer, Integer> function = new Function<Integer, Integer>() {
            private int count = 0;

            @Override
            public Integer apply(Integer input) {
                count++;
                return input;
            }
        };

        Supplier<Integer> supplier = Suppliers.memoize(Suppliers.ofInstance(new Function<Integer, Integer>() {
            @Override
            public Integer apply(Integer input) {
                return input * 2;
            }
        }));

        assertEquals(2, supplier.get());
        assertEquals(2, supplier.get());
        assertEquals(2, supplier.get());

        assertEquals(1, function.getInvocationCount());
    }

    @Test
    public void testMemoizeWithExpiration() {
        Function<Integer, Integer> function = new Function<Integer, Integer>() {
            private int count = 0;

            @Override
            public Integer apply(Integer input) {
                count++;
                return input;
            }
        };

        Supplier<Integer> supplier = Suppliers.memoizeWithExpiration(Suppliers.ofInstance(function), 100, TimeUnit.MILLISECONDS);

        assertEquals(0, function.getInvocationCount());

        supplier.get();
        supplier.get();
        supplier.get();

        assertEquals(1, function.getInvocationCount());

        try {
            Thread.sleep(150);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        supplier.get();
        supplier.get();

        assertEquals(3, function.getInvocationCount());
    }

    @Test
    public void testOfInstance() {
        Supplier<String> supplier = Suppliers.ofInstance("Hello World");

        assertEquals("Hello World", supplier.get());
    }

    @Test
    public void testSynchronizedSupplier() {
        int count = 0;

        Supplier<Integer> supplier = new Supplier<Integer>() {
            @Override
            public Integer get() {
                synchronized (this) {
                    count++;
                    return count;
                }
            }
        };

        Supplier<Integer> synchronizedSupplier = Suppliers.synchronizedSupplier(supplier);

        Thread thread1 = new Thread(() -> System.out.println(synchronizedSupplier.get()));
        Thread thread2 = new Thread(() -> System.out.println(synchronizedSupplier.get()));

        thread1.start();
        thread2.start();

        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        assertEquals(2, count);
    }

    @Test
    public void testSupplierFunction() {
        Function<Supplier<Integer>, Integer> supplierFunction = Suppliers.supplierFunction();
        Supplier<Integer> supplier = Suppliers.ofInstance(1);

        assertEquals(1, supplierFunction.apply(supplier).intValue());
    }
}