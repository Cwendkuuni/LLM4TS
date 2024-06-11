package com.google.common.base;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.base.Function;
import com.google.common.base.Supplier;
import com.google.common.base.ThreadSafeSupplier;
import com.google.common.base.supplier.ExpiringMemoizingSupplier;
import com.google.common.base.supplier.MemoizingSupplier;
import com.google.common.base.supplier.SupplierComposition;
import com.google.common.base.supplier.SupplierFunction;
import com.google.common.base.supplier.SupplierOfInstance;

import org.junit.Before;
import org.junit.Test;

public class SuppliersTest {

    private Function<String, String> function;
    private Supplier<String> supplier;
    private Supplier<String> supplierInstance;
    private Supplier<String> synchronizedSupplier;
    private Supplier<String> memoizedSupplier;
    private Supplier<String> memoizedWithExpirationSupplier;

    @Before
    public void setUp() {
        function = mock(Function.class);
        supplier = mock(Supplier.class);
        supplierInstance = new SupplierOfInstance<>("test");
        synchronizedSupplier = new ThreadSafeSupplier<>(supplier);
        memoizedSupplier = new MemoizingSupplier<>(supplier);
        memoizedWithExpirationSupplier = new ExpiringMemoizingSupplier<>(supplier, 10, TimeUnit.MILLISECONDS);
    }

    @Test
    public void testCompose() {
        when(supplier.get()).thenReturn("Test");
        Supplier<String> composedSupplier = Suppliers.compose(function, supplier);
        assertEquals("Test", composedSupplier.get());
        when(function.apply(supplier)).thenReturn("Test2");
        assertEquals("Test2", composedSupplier.get());
    }

    @Test
    public void testMemoize() {
        when(supplier.get()).thenReturn("Test");
        assertEquals("Test", memoizedSupplier.get());
        assertEquals("Test", memoizedSupplier.get());
    }

    @Test
    public void testMemoizeWithExpiration() {
        when(supplier.get()).thenReturn("Test");
        assertEquals("Test", memoizedWithExpirationSupplier.get());
        assertEquals("Test", memoizedWithExpirationSupplier.get());
        try {
            Thread.sleep(15L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertEquals("Test", memoizedWithExpirationSupplier.get());
        assertEquals("Test", memoizedWithExpirationSupplier.get());
        try {
            Thread.sleep(20L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertNull(memoizedWithExpirationSupplier.get());
    }

    @Test
    public void testOfInstance() {
        assertEquals("test", supplierInstance.get());
        assertEquals("test", supplierInstance.get());
    }

    @Test
    public void testSynchronizedSupplier() {
        when(supplier.get()).thenReturn("Test");
        synchronized (synchronizedSupplier) {
            assertEquals("Test", synchronizedSupplier.get());
            assertEquals("Test", synchronizedSupplier.get());
        }
    }

    @Test(expected = NullPointerException.class)
    public void testNullFunction() {
        Suppliers.compose(null, supplier);
    }

    @Test(expected = NullPointerException.class)
    public void testNullSupplier() {
        Suppliers.compose(function, null);
    }
}