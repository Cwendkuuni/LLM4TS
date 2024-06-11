package com.google.common.base;

import org.junit.Test;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

public class SuppliersTest {
    @Test
    public void testComposeFunctionAndSupplier() {
        Function<Integer, Integer> function = mock(Function.class);
        when(function.apply(anyInt())).thenReturn(42);

        Supplier<Integer> supplier = mock(Supplier.class);
        when(supplier.get()).thenReturn(10);

        Supplier<Integer> result = Suppliers.compose(function, supplier);

        assertNotNull(result);
        assertEquals(42, result.get());
    }

    // Add the remaining test methods here...
}