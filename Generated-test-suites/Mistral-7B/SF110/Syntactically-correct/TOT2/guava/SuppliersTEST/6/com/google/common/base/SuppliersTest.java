package com.example.junit4;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import com.google.common.base.*;

@RunWith(JUnit4.class)
public class SuppliersTest {

    @Test
    public void testCompose() {
        Function<Integer, Integer> addOne = x -> x + 1;
        Supplier<Integer> supplier = Suppliers.ofInstance(1);
        Supplier<Integer> composedSupplier = Suppliers.compose(addOne, supplier);

        Assert.assertEquals(2, composedSupplier.get());
    }

    @Test(expected = NullPointerException.class)
    public void testComposeNullFunction() {
        Function<Integer, Integer> nullFunction = null;
        Supplier<Integer> supplier = Suppliers.ofInstance(1);
        Suppliers.compose(nullFunction, supplier);
    }

    @Test(expected = NullPointerException.class)
    public void testComposeNullSupplier() {
        Function<Integer, Integer> function = x -> x + 1;
        Supplier<Integer> nullSupplier = null;
        Suppliers.compose(function, nullSupplier);
    }

    @Test
    public void testComposeSameInstance() {
        Function<Integer, Integer> addOne = x -> x + 1;
        Supplier<Integer> supplier = Suppliers.ofInstance(1);
        Supplier<Integer> composedSupplier = Suppliers.compose(addOne, supplier);

        Assert.assertSame(supplier, composedSupplier);
    }

    @Test(expected = RuntimeException.class)
    public void testComposeFunctionException() {
        Function<Integer, Integer> functionThrowingException = x -> { throw new RuntimeException("Function exception"); };
        Supplier<Integer> supplier = Suppliers.ofInstance(1);
        Suppliers.compose(functionThrowingException, supplier);
    }

    @Test(expected = RuntimeException.class)
    public void testComposeSupplierException() {
        Function<Integer, Integer> function = x -> x + 1;
        Supplier<Integer> supplierThrowingException = () -> { throw new RuntimeException("Supplier exception"); };
        Suppliers.compose(function, supplierThrowingException);
    }

    @Test
    public void testMemoize() {
        Function<Integer, Integer> addOne = x -> x + 1;
        Supplier<Integer> supplier = Suppliers.ofInstance(1);
        Supplier<Integer> memoizedSupplier = Suppliers.memoize(supplier);

        int result1 = memoizedSupplier.get();
        int result2 = memoizedSupplier.get();

        Assert.assertEquals(result1, result2);
    }

    @Test(expected = NullPointerException.class)
    public void testMemoizeNullSupplier() {
        Supplier<Integer> nullSupplier = null;
        Suppliers.memoize(nullSupplier);
    }

    @Test
    public void testMemoizeWithExpiration() {
        Function<Integer, Integer> addOne = x -> x + 1;
        Supplier<Integer> supplier = Suppliers.ofInstance(1);
        Supplier<Integer> memoizedSupplierWithExpiration = Suppliers.memoizeWithExpiration(supplier, 10, TimeUnit.MILLISECONDS);

        int result1 = memoizedSupplierWithExpiration.get();
        int result2 = memoizedSupplierWithExpiration.get();

        Assert.assertEquals(result1, result2);

        try {
            Thread.sleep(11);
            int result3 = memoizedSupplierWithExpiration.get();
            Assert.assertNotEquals(result1, result3);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMemoizeWithExpirationNegativeDuration() {
        Supplier<Integer> supplier = Suppliers.ofInstance(1);
        Suppliers.memoizeWithExpiration(supplier, -10, TimeUnit.MILLISECONDS);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMemoizeWithExpirationInvalidTimeUnit() {
        Supplier<Integer> supplier = Suppliers.ofInstance(1);
        Suppliers.memoizeWithExpiration(supplier, 10, new InvalidTimeUnit("INVALID_UNIT"));
    }

}