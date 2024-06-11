package com.google.common.base;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.concurrent.Supplier;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class SuppliersTest {

    @Mock
    private Function<Object, Object> functionMock;

    @Mock
    private Supplier<Object> supplierMock;

    @InjectMocks
    private Suppliers suppliers;

    @Test
    public void testCompose() {
        when(supplierMock.get()).thenReturn(new Object());
        when(functionMock.apply(supplierMock)).thenReturn(new Object());

        final Supplier<Object> composedSupplier = suppliers.compose(functionMock, supplierMock);

        assertEquals(functionMock.apply(supplierMock).get(), composedSupplier.get());
    }

    @Test
    public void testMemoize() {
        final Supplier<String> delegate = mock(Supplier.class);
        final Supplier<String> memoizedSupplier = suppliers.memoize(delegate);

        when(delegate.get()).thenReturn("initial");
        assertEquals("initial", memoizedSupplier.get());
        assertEquals("initial", memoizedSupplier.get());
    }

    @Test
    public void testMemoizeWithExpiration() {
        final Supplier<String> delegate = mock(Supplier.class);
        final long duration = 1L;
        final TimeUnit unit = TimeUnit.MILLISECONDS;

        final Supplier<String> memoizedSupplier = suppliers.memoizeWithExpiration(delegate, duration, unit);

        when(delegate.get()).thenReturn("initial");
        assertEquals("initial", memoizedSupplier.get());
        assertEquals("initial", memoizedSupplier.get());

        Thread.sleep(duration + 1);
        assertNotEquals("initial", memoizedSupplier.get());
    }

    @Test
    public void testOfInstance() {
        final Object instance = new Object();
        final Supplier<Object> supplierOfInstance = suppliers.ofInstance(instance);

        assertEquals(instance, supplierOfInstance.get());
    }

    @Test
    public void testSynchronizedSupplier() {
        final Supplier<String> delegate = mock(Supplier.class);
        final Supplier<String> synchronizedSupplier = suppliers.synchronizedSupplier(delegate);

        when(delegate.get()).thenReturn("initial");
        assertEquals("initial", synchronizedSupplier.get());
        assertEquals("initial", synchronizedSupplier.get());
    }
}