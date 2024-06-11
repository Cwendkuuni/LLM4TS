package com.google.common.base;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.concurrent.Supplier;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

public class SuppliersTest {

    private Function<Supplier<Integer>, Integer> function;
    private Supplier<Integer> supplier;
    private Supplier<Integer> composedSupplier;
    private Supplier<Integer> memoizedSupplier;
    private Supplier<Integer> memoizedWithExpirationSupplier;
    private Supplier<Integer> ofInstanceSupplier;
    private Supplier<Integer> synchronizedSupplier;

    @Before
    public void setup() {
        function = mock(Function.class);
        supplier = mock(Supplier.class);
        ofInstanceSupplier = Suppliers.ofInstance(1);
        synchronizedSupplier = Suppliers.synchronizedSupplier(supplier);
    }

    @Test
    public void testCompose() {
        when(function.apply(supplier)).thenReturn(1);

        composedSupplier = Suppliers.compose(function, supplier);

        assertEquals(1, composedSupplier.get());
    }

    @Test
    public void testCompose_supplierFunction() {
        Function<Supplier<Integer>, Integer> function = Suppliers.supplierFunction();
        Supplier<Integer> supplier = Suppliers.ofInstance(1);

        Supplier<Integer> composedSupplier = Suppliers.compose(function, supplier);

        ArgumentCaptor<Supplier<Integer>> supplierArgumentCaptor = ArgumentCaptor.forClass(Supplier.class);
        assertEquals(1, function.apply(supplierArgumentCaptor.capture()).get());
        assertSame(supplier, supplierArgumentCaptor.getValue());
    }

    @Test
    public void testMemoize() {
        Supplier<Integer> delegate = mock(Supplier.class);
        when(delegate.get()).thenReturn(1);

        memoizedSupplier = Suppliers.memoize(delegate);

        assertEquals(1, memoizedSupplier.get());
        assertEquals(memoizedSupplier, memoizedSupplier.get());
    }

    @Test
    public void testMemoizeWithExpiration() {
        Supplier<Integer> delegate = mock(Supplier.class);
        when(delegate.get()).thenReturn(1);

        memoizedWithExpirationSupplier = Suppliers.memoizeWithExpiration(delegate, 100, TimeUnit.MILLISECONDS);

        assertEquals(1, memoizedWithExpirationSupplier.get());
        assertEquals(1, memoizedWithExpirationSupplier.get());

        try {
            Thread.sleep(150L);
        } catch (InterruptedException e) {
            // Do nothing
        }

        assertEquals(1, memoizedWithExpirationSupplier.get());

        try {
            Thread.sleep(1100L);
        } catch (InterruptedException e) {
            // Do nothing
        }

        assertNull(memoizedWithExpirationSupplier.get());
    }

    @Test
    public void testOfInstance() {
        assertEquals(1, ofInstanceSupplier.get());
    }

    @Test
    public void testSynchronizedSupplier() {
        when(supplier.get()).thenReturn(1);

        assertEquals(1, synchronizedSupplier.get());
        assertEquals(1, synchronizedSupplier.get());
    }
}