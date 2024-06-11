package com.google.common.base;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.concurrent.TimeUnit;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.google.common.base.Function;
import com.google.common.base.Supplier;

public class SuppliersTest {

    private Function<Supplier<String>, String> function;
    private Supplier<String> supplier;
    private Supplier<String> supplierComposition;
    private Supplier<String> memoizingSupplier;
    private Supplier<String> memoizingSupplierWithExpiration;
    private Supplier<String> supplierOfInstance;
    private Supplier<String> synchronizedSupplier;

    @Before
    public void setUp() {
        function = Mockito.mock(Function.class);
        supplier = Mockito.mock(Supplier.class);
        memoizingSupplier = Suppliers.memoize(supplier);
        memoizingSupplierWithExpiration = Suppliers.memoizeWithExpiration(supplier, 1, TimeUnit.SECONDS);
        supplierOfInstance = Suppliers.ofInstance("test");
        synchronizedSupplier = Suppliers.synchronizedSupplier(supplier);
    }

    @Test
    public void testCompose() {
        when(supplier.get()).thenReturn("test");
        supplierComposition = Suppliers.compose(function, supplier);
        assertEquals("test", supplierComposition.get());
        assertEquals(function, supplierComposition.getDelegate());
        assertEquals(supplier, supplierComposition.getSupplier());
    }

    @Test
    public void testMemoize() {
        when(supplier.get()).thenReturn("test");
        assertEquals("test", memoizingSupplier.get());
        assertEquals("test", memoizingSupplier.get());
    }

    @Test
    public void testMemoizeWithExpiration() {
        when(supplier.get()).thenReturn("test");
        assertEquals("test", memoizingSupplierWithExpiration.get());
        assertEquals("test", memoizingSupplierWithExpiration.get());

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        assertNull(memoizingSupplierWithExpiration.get());
    }

    @Test
    public void testOfInstance() {
        assertEquals("test", supplierOfInstance.get());
    }

    @Test
    public void testSynchronizedSupplier() {
        when(supplier.get()).thenReturn("test");
        assertEquals("test", synchronizedSupplier.get());
        assertEquals("test", synchronizedSupplier.get());
    }
}