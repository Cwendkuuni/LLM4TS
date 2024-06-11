package com.google.common.base;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.*;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

public class SuppliersTest {

    private static final long DURATION_NANOS = 100;

    private Function<String, String> function;
    private Supplier<String> supplier;
    private Supplier<String> supplierFunction;
    private MemoizingSupplier<String> memoizingSupplier;
    private ExpiringMemoizingSupplier<String> expiringMemoizingSupplier;
    private SupplierOfInstance<String> supplierOfInstance;
    private ThreadSafeSupplier<String> threadSafeSupplier;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        this.function = mock(Function.class);
        this.supplier = mock(Supplier.class);
        this.supplierFunction = Suppliers.supplierFunction();
        this.memoizingSupplier = new MemoizingSupplier<String>(this.supplier);
        this.expiringMemoizingSupplier = Suppliers.memoizeWithExpiration(this.supplier, DURATION_NANOS, TimeUnit.NANOS);
        this.supplierOfInstance = Suppliers.ofInstance("foo");
        this.threadSafeSupplier = Suppliers.synchronizedSupplier(this.supplier);
    }

    @Test
    public void testCompose() {
        when(this.supplier.get()).thenReturn("bar");

        Supplier<String> composedSupplier = Suppliers.compose(this.function, this.supplier);

        assertEquals("bar", composedSupplier.get());
        verify(this.function).apply(this.supplier);
    }

    @Test
    public void testMemoize() {
        when(this.supplier.get()).thenReturn("bar");

        Supplier<String> memoizedSupplier = Suppliers.memoize(this.supplier);

        assertEquals("bar", memoizedSupplier.get());
        mockStatic(Suppliers.class);
        Suppliers.memoizingSupplier = new MemoizingSupplier<String>(mock(Supplier.class));
        assertEquals(memoizedSupplier, Suppliers.memoize(this.supplier));
        verify(memoizingSupplier, never()).get();
    }

    @Test
    public void testMemoizeWithExpiration() {
        when(this.supplier.get()).thenReturn("bar");

        Supplier<String> expiringMemoizingSupplier = Suppliers.memoizeWithExpiration(this.supplier, DURATION_NANOS, TimeUnit.NANOS);

        assertEquals("bar", expiringMemoizingSupplier.get());
        verify(this.supplier, times(2)).get();
    }

    @Test
    public void testOfInstance() {
        assertEquals("foo", Suppliers.ofInstance("foo").get());
        assertEquals("foo", Suppliers.ofInstance((String)null).get());
    }

    @Test
    public void testSynchronizedSupplier() {
        when(this.supplier.get()).thenReturn("bar");

        Supplier<String> synchronizedSupplier = Suppliers.synchronizedSupplier(this.supplier);

        assertEquals("bar", synchronizedSupplier.get());
        verify(this.supplier, times(1)).get();
    }

    @Test(expected = NullPointerException.class)
    public void testComposeNullFunction() {
        Suppliers.compose(null, this.supplier);
    }

    @Test(expected = NullPointerException.class)
    public void testComposeNullSupplier() {
        Suppliers.compose(this.function, null);
    }

    @Test(expected = NullPointerException.class)
    public void testMemoizeNullSupplier() {
        Suppliers.memoize(null);
    }

    @Test
    public void testMemoizeWithExpirationZeroDuration() {
        checkArgument(DURATION_NANOS > 0, "Duration should be greater than zero");
        when(this.supplier.get()).thenReturn("bar");

        Supplier<String> expiringMemoizingSupplier = Suppliers.memoizeWithExpiration(this.supplier, 0, TimeUnit.NANOS);

        assertEquals("bar", expiringMemoizingSupplier.get());
        verify(this.supplier, times(1)).get();
    }

    @Test
    public void testExpiringMemoizingSupplierExpired() {
        when(this.supplier.get()).thenReturn("bar");

        Supplier<String> expiringMemoizingSupplier = Suppliers.memoizeWithExpiration(this.supplier, DURATION_NANOS, TimeUnit.NANOS);

        Thread thread = new Thread(() -> {
            try {
                Thread.sleep(DURATION_NANOS + 1);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            assertEquals("bar", expiringMemoizingSupplier.get());
        });
        thread.start();

        try {
            thread.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testThreadSafeSupplierGet() {
        when(this.supplier.get()).thenReturn("bar");

        this.threadSafeSupplier.get();
        verify(this.supplier, times(1)).get();
    }
}