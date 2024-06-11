package com.google.common.base;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.junit.Assert.*;

import java.util.concurrent.*;
import java.io.Serializable;
import com.google.common.annotations.*;

@GwtCompatible
public final class SuppliersTest {

    @Test
    public void testCompose() {
        Function<Integer, Integer> addOne = new Function<Integer, Integer>() {
            @Override
            public Integer apply(Integer input) {
                return input + 1;
            }
        };
        Supplier<Integer> supplierOfOne = Suppliers.ofInstance(1);
        Supplier<Integer> composedSupplier = Suppliers.compose(addOne, supplierOfOne);
        assertEquals(2, composedSupplier.get());
    }

    @Test
    public void testMemoize() {
        Supplier<Integer> supplier = new Supplier<Integer>() {
            private int counter = 0;
            @Override
            public Integer get() {
                return Integer.valueOf(counter++);
            }
        };
        Supplier<Integer> memoizedSupplier = Suppliers.memoize(supplier);
        assertEquals(Integer.valueOf(0), memoizedSupplier.get());
        assertEquals(Integer.valueOf(0), memoizedSupplier.get());
        assertEquals(Integer.valueOf(1), memoizedSupplier.get());
        assertEquals(Integer.valueOf(1), memoizedSupplier.get());
    }

    @Test
    public void testMemoizeWithExpiration() {
        Supplier<Long> supplier = new Supplier<Long>() {
            @Override
            public Long get() {
                return System.nanoTime();
            }
        };
        Supplier<Long> memoizedSupplierWithExpiration = Suppliers.memoizeWithExpiration(supplier, 10, TimeUnit.MILLISECONDS);
        long previousTime = memoizedSupplierWithExpiration.get();
        long currentTime = memoizedSupplierWithExpiration.get();
        long expirationTime = previousTime + 10;
        assertEquals(previousTime, currentTime);
        assertTrue(previousTime < expirationTime);
        Thread.sleep(15); // Wait for expiration
        assertEquals(currentTime, memoizedSupplierWithExpiration.get());
    }

    @Test
    public void testOfInstance() {
        String instance = "Suppliers Test";
        Supplier<String> supplierOfInstance = Suppliers.ofInstance(instance);
        assertEquals(instance, supplierOfInstance.get());
    }

    @Test
    public void testSynchronizedSupplier() {
        AtomicInteger atomicInteger = new AtomicInteger(0);
        Supplier<Integer> supplier = new Supplier<Integer>() {
            @Override
            public Integer get() {
                atomicInteger.incrementAndGet();
                return Integer.valueOf(atomicInteger.get());
            }
        };
        Supplier<Integer> synchronizedSupplier = Suppliers.synchronizedSupplier(supplier);
        for (int i = 0; i < 100; i++) {
            assertEquals(Integer.valueOf(i + 1), synchronizedSupplier.get());
        }
    }

    @Test
    public void testSupplierFunction() {
        Function<Supplier<Object>, Object> supplierFunction = Suppliers.supplierFunction();
        Supplier<Object> supplierOfInstance = Suppliers.ofInstance(new Object());
        assertEquals(supplierOfInstance.get(), supplierFunction.apply(supplierOfInstance));
    }
}