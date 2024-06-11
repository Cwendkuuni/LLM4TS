package com.google.common.base.test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.concurrent.*;

import org.junit.*;
import org.mockito.*;

import com.google.common.base.*;

public class SuppliersTest {

    @SuppressWarnings("unchecked")
    private static <F, T> Function<Supplier<F>, T> mockFunction() {
        return mock(Function.class);
    }

    private static <T> Supplier<T> mockSupplier() {
        return mock(Supplier.class);
    }

    @Test
    public void testCompose() throws Exception {
        Function<Supplier<Integer>, Integer> function = mockFunction();
        Supplier<Supplier<Integer>> supplier = mockSupplier();
        Supplier<Integer> expected = mockSupplier();

        when(function.apply(supplier)).thenReturn(expected);

        Function<Supplier<Integer>, Integer> functionComposed = Suppliers.compose(function, supplier);

        Supplier<Integer> result = functionComposed.apply(supplier);

        assertEquals(expected, result);
        verify(function).apply(supplier);
    }

    @Test(expected = NullPointerException.class)
    public void testComposeWithNullFunction() {
        Supplier<Supplier<Integer>> supplier = mockSupplier();
        Function<Supplier<Integer>, Integer> function = null;

        Suppliers.compose(function, supplier);
    }

    @Test(expected = NullPointerException.class)
    public void testComposeWithNullSupplier() {
        Function<Supplier<Integer>, Integer> function = mockFunction();
        Supplier<Supplier<Integer>> supplier = null;

        Suppliers.compose(function, supplier);
    }

    @Test
    public void testMemoize() throws Exception {
        Supplier<String> supplier = mockSupplier();
        String result = "hello";

        when(supplier.get()).thenReturn(result);

        Supplier<String> memoizedSupplier = Suppliers.memoize(supplier);

        assertEquals(result, memoizedSupplier.get());
        assertEquals(result, memoizedSupplier.get());
    }

    @Test
    public void testMemoizeWithExpiration() throws Exception {
        Supplier<String> supplier = mockSupplier();
        String result = "hello";

        when(supplier.get()).thenReturn(result);

        Supplier<String> memoizedSupplier = Suppliers.memoizeWithExpiration(supplier, 10, TimeUnit.MILLISECONDS);

        assertEquals(result, memoizedSupplier.get());
        assertEquals(result, memoizedSupplier.get());

        Thread.sleep(15);

        Supplier<String> memoizedSupplierNewResult = Suppliers.memoizeWithExpiration(supplier, 10, TimeUnit.MILLISECONDS);

        assertEquals(result, memoizedSupplier.get());
        assertEquals(result, memoizedSupplierNewResult.get());

        verify(supplier, times(2)).get();
    }

    @Test(expected = NullPointerException.class)
    public void testMemoizeWithNullSupplier() {
        Suppliers.memoize(null);
    }

    @Test
    public void testOfInstance() {
        String result = "hello";

        Supplier<String> supplier = Suppliers.ofInstance(result);

        assertEquals(result, supplier.get());
    }

    @Test
    public void testSynchronizedSupplier() throws Exception {
        Supplier<Integer> supplier = mockSupplier();
        Integer result = 1;

        when(supplier.get()).thenReturn(result);

        Supplier<Integer> synchronizedSupplier = Suppliers.synchronizedSupplier(supplier);

        assertEquals(result, synchronizedSupplier.get());
        assertEquals(result, synchronizedSupplier.get());

        verify(supplier, atLeastOnce()).get();
    }

    @Test
    public void testSupplierFunction() {
        Supplier<Integer> supplier = mockSupplier();
        Integer result = 1;

        when(supplier.get()).thenReturn(result);

        Function<Supplier<Integer>, Integer> supplierFunction = Suppliers.supplierFunction();

        assertEquals(result, supplierFunction.apply(supplier));
        verify(supplier).get();
    }
}