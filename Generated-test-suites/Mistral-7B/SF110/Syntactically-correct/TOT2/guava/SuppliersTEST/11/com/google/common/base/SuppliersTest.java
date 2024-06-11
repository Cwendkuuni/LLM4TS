
package com.google.common.base;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class SuppliersTest {

    @SuppressWarnings("unchecked")
    @Test
    public void testCompose() {
        Function<Integer, String> function = mock(Function.class);
        Supplier<Integer> supplier = mock(Supplier.class);
        when(supplier.get()).thenReturn(1);

        Supplier<String> result = Suppliers.compose(function, supplier);

        verify(function).apply(1);
        assertEquals("1", result.get());
    }

    @Test
    public void testMemoize() {
        Supplier<Integer> supplier = () -> 42;
        Supplier<Integer> memoizedSupplier = Suppliers.memoize(supplier);

        assertEquals(42, memoizedSupplier.get());
        assertEquals(42, memoizedSupplier.get());
    }

    @Test
    public void testMemoizeWithExpiration() {
        Supplier<String> supplier = () -> "initialValue";
        Supplier<String> memoizedSupplier = Suppliers.memoizeWithExpiration(supplier, 1, TimeUnit.SECONDS);

        String initialValue = memoizedSupplier.get();
        String laterValue = memoizedSupplier.get();

        assertEquals("initialValue", initialValue);
        assertEquals("initialValue", laterValue);

        Thread.sleep(2_000);

        String newerValue = memoizedSupplier.get();
        assertNotEquals("initialValue", newerValue);
    }

    @Test(expected = NullPointerException.class)
    public void testComposeWithNullFunction() {
        Supplier<Integer> supplier = mock(Supplier.class);
        assertThrows(NullPointerException.class, () -> Suppliers.compose(null, supplier));
    }

    @Test(expected = NullPointerException.class)
    public void testComposeWithNullSupplier() {
        Function<Integer, String> function = mock(Function.class);
        assertThrows(NullPointerException.class, () -> Suppliers.compose(function, null));
    }

    @Test(expected = RuntimeException.class)
    public void testComposeWithExceptionalSupplier() {
        Function<Integer, String> function = mock(Function.class);
        Supplier<Integer> supplier = mock(Supplier.class);
        when(supplier.get()).thenThrow(new RuntimeException());

        assertThrows(RuntimeException.class, () -> Suppliers.compose(function, supplier));
    }

    @Test(expected = NullPointerException.class)
    public void testMemoizeWithNullDelegate() {
        assertThrows(NullPointerException.class, () -> Suppliers.memoize(null));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMemoizeWithNegativeDuration() {
        Supplier<String> supplier = mock(Supplier.class);
        assertThrows(IllegalArgumentException.class, () -> Suppliers.memoizeWithExpiration(supplier, -1, TimeUnit.SECONDS));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMemoizeWithZeroDuration() {
        Supplier<String> supplier = mock(Supplier.class);
        assertThrows(IllegalArgumentException.class, () -> Suppliers.memoizeWithExpiration(supplier, 0, TimeUnit.SECONDS));
    }
}
