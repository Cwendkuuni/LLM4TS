package com.google.common.base.test;

import static org.junit.Assert.*;

import java.util.concurrent.*;
import javax.annotation.*;
import org.junit.*;

import com.google.common.base.*;
import com.google.common.annotations.*;

@GwtIncompatible // JUnit tests are not compatible with GWT
public class SuppliersTest {

    @SuppressWarnings("unchecked")
    @Test
    public void testCompose() {
        Function<Integer, Integer> addOne = num -> num + 1;
        Supplier<Integer> supplier = Suppliers.ofInstance(42);
        Supplier<Integer> composedSupplier = Suppliers.compose(addOne, supplier);
        assertEquals(43, composedSupplier.get());
    }

    @Test(expected = NullPointerException.class)
    public void testComposeWithNullFunction() {
        Supplier<Integer> supplier = Suppliers.ofInstance(42);
        Function<? super Integer, Integer> nullFunction = null;
        Supplier<Integer> composedSupplier = Suppliers.compose(nullFunction, supplier);
    }

    @Test(expected = NullPointerException.class)
    public void testComposeWithNullSupplier() {
        Function<Integer, Integer> addOne = num -> num + 1;
        Supplier<Integer> nullSupplier = null;
        Supplier<Integer> composedSupplier = Suppliers.compose(addOne, nullSupplier);
    }

    @Test
    public void testMemoize() {
        Supplier<String> originalSupplier = Suppliers.ofInstance("Hello World");
        Supplier<String> memoizedSupplier = Suppliers.memoize(originalSupplier);
        assertEquals("Hello World", memoizedSupplier.get());
        assertEquals("Hello World", memoizedSupplier.get());
    }

    @Test(expected = NullPointerException.class)
    public void testMemoizeWithNullSupplier() {
        Supplier<String> nullSupplier = null;
        Supplier<String> memoizedSupplier = Suppliers.memoize(nullSupplier);
    }

    @Test
    public void testMemoizeWithExpiration() {
        Supplier<String> originalSupplier = Suppliers.ofInstance("Hello World");
        Supplier<String> memoizedSupplier = Suppliers.memoizeWithExpiration(originalSupplier, 10, TimeUnit.MILLISECONDS);
        assertEquals("Hello World", memoizedSupplier.get());
        assertEquals("Hello World", memoizedSupplier.get());
        Thread.sleep(20); // wait for expiration
        assertEquals("Hello World", memoizedSupplier.get()); // should call originalSupplier again
    }

    @Test(expected = NullPointerException.class)
    public void testMemoizeWithExpirationWithNullSupplier() {
        Supplier<String> nullSupplier = null;
        Suppliers.memoizeWithExpiration(nullSupplier, 10, TimeUnit.MILLISECONDS);
    }

    @Test
    public void testOfInstance() {
        Supplier<String> supplier = Suppliers.ofInstance("Hello World");
        assertEquals("Hello World", supplier.get());
        assertEquals("Hello World", supplier.get());
    }

    @Test(expected = NullPointerException.class)
    public void testOfInstanceWithNullInstance() {
        Suppliers.ofInstance(null);
    }

    @Test
    public void testSynchronizedSupplier() {
        AtomicInteger counter = new AtomicInteger(0);
        Supplier<Integer> supplier = () -> counter.incrementAndGet();
        Supplier<Integer> synchronizedSupplier = Suppliers.synchronizedSupplier(supplier);
        assertEquals(1, synchronizedSupplier.get());
        assertEquals(1, synchronizedSupplier.get());
        assertEquals(2, synchronizedSupplier.get());
    }

    @Test
    public void testSupplierFunction() {
        Supplier<String> supplier = Suppliers.ofInstance("Hello World");
        Function<Supplier<String>, String> supplierFunction = Suppliers.supplierFunction();
        assertEquals("Hello World", supplierFunction.apply(supplier));
    }
}