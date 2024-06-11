package com.google.common.base.test;

import static org.junit.Assert.*;
import static com.google.common.base.Preconditions.*;

import java.util.concurrent.*;
import javax.annotation.*;
import java.io.*;
import org.junit.*;
import org.junit.annotations.*;
import com.google.common.base.*;
import com.google.common.annotations.*;

@GwtIncompatible
@RunWith(Parameterized.class)
public class SuppliersTest {

    @Parameterized.Parameters(name = "{index}: testMethodName({0})")
    public static Object[] testData() {
        return new Object[][]{
            {Suppliers::compose, (Function<? super Integer, Integer>) (i -> i + 1), Suppliers::ofInstance, (Supplier<Integer>) () -> 1},
            {Suppliers::memoize, Suppliers::memoizeWithExpiration, Suppliers::memoize, Suppliers::ofInstance},
            {Suppliers::memoizeWithExpiration, Suppliers::memoizeWithExpiration, Suppliers::memoize, Suppliers::ofInstance},
            {Suppliers::ofInstance, null, Suppliers::memoize, Suppliers::ofInstance},
            {Suppliers::synchronizedSupplier, Suppliers::memoize, Suppliers::memoize, Suppliers::ofInstance},
            {Suppliers::supplierFunction, null, Suppliers::memoize, Suppliers::ofInstance},
        };
    }

    private final Function<? super Integer, Integer> function;
    private final Supplier<Supplier<Integer>> supplierSupplier;
    private final Supplier<Integer> supplier;

    public SuppliersTest(Function<? super Integer, Integer> function, Supplier<Supplier<Integer>> supplierSupplier, Supplier<Integer> supplier) {
        this.function = function;
        this.supplierSupplier = supplierSupplier;
        this.supplier = supplier;
    }

    @Test
    public void testCompose() {
        Supplier<Integer> composedSupplier = Suppliers.compose(this.function, this.supplierSupplier.get());
        Integer result = composedSupplier.get();
        Integer expected = this.function.apply(this.supplier.get());
        assertEquals(expected, result);
    }

    @Test(expected = NullPointerException.class)
    public void testComposeWithNullFunction() {
        Function<? super Integer, Integer> nullFunction = null;
        Supplier<Supplier<Integer>> supplier = Suppliers.ofInstance(new SupplierOfInstance<>(new SupplierOfInstance<>(new Integer(1))));
        Supplier<Integer> composedSupplier = Suppliers.compose(nullFunction, supplier);
    }

    @Test(expected = NullPointerException.class)
    public void testComposeWithNullSupplier() {
        Function<? super Integer, Integer> function = (i -> i + 1);
        Supplier<Integer> nullSupplier = null;
        Supplier<Supplier<Integer>> supplier = Suppliers.ofInstance(new SupplierOfInstance<>(new SupplierOfInstance<>(new Integer(1))));
        Supplier<Integer> composedSupplier = Suppliers.compose(function, nullSupplier);
    }

    @Test
    public void testMemoize() {
        Supplier<Integer> supplier = Suppliers.ofInstance(new SupplierOfInstance<>(new Integer(1)));
        Supplier<Integer> memoizedSupplier = Suppliers.memoize(supplier);
        Integer result1 = memoizedSupplier.get();
        Integer result2 = memoizedSupplier.get();
        assertEquals(result1, result2);
    }

    @Test
    public void testMemoizeWithExpiration() {
        Supplier<Integer> supplier = Suppliers.ofInstance(new SupplierOfInstance<>(new Integer(1)));
        Supplier<Integer> memoizedSupplierWithExpiration = Suppliers.memoizeWithExpiration(supplier, 1, TimeUnit.SECONDS);
        Integer result1 = memoizedSupplierWithExpiration.get();
        Integer result2 = memoizedSupplierWithExpiration.get();
        Integer result3 = memoizedSupplierWithExpiration.get();
        assertEquals(result1, result2);
        assertEquals(result1, result3);

        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            fail("InterruptedException should not be thrown");
        }

        Integer result4 = memoizedSupplierWithExpiration.get();
        assertNotEquals(result1, result4);
    }

    @Test(expected = NullPointerException.class)
    public void testMemoizeWithNullSupplier() {
        Supplier<Integer> nullSupplier = null;
        Suppliers.memoize(nullSupplier);
    }

    @Test
    public void testOfInstance() {
        Supplier<Integer> supplier = Suppliers.ofInstance(new Integer(1));
        Integer result = supplier.get();
        Integer expected = 1;
        assertEquals(expected, result);
    }

    @Test(expected = NullPointerException.class)
    public void testOfInstanceWithNullInstance() {
        Suppliers.ofInstance(null);
    }

    @Test
    public void testSynchronizedSupplier() {
        Supplier<Integer> supplier = Suppliers.ofInstance(new SupplierOfInstance<>(new Integer(1)));
        Supplier<Integer> synchronizedSupplier = Suppliers.synchronizedSupplier(supplier);
        Integer result1 = synchronizedSupplier.get();
        Integer result2 = synchronizedSupplier.get();
        assertEquals(result1, result2);
    }

    @Test
    public void testSupplierFunction() {
        Supplier<Integer> supplier = Suppliers.ofInstance(new SupplierOfInstance<>(new Integer(1)));
        Function<Supplier<Integer>, Integer> supplierFunction = Suppliers.supplierFunction();
        Integer result = supplierFunction.apply(supplier);
        Integer expected = supplier.get();
        assertEquals(expected, result);
    }

    @Test(expected = NullPointerException.class)
    public void testSupplierFunctionWithNullSupplier() {
        Function<Supplier<Integer>, Integer> supplierFunction = Suppliers.supplierFunction();
        Supplier<Integer> nullSupplier = null;
        supplierFunction.apply(nullSupplier);
    }
}