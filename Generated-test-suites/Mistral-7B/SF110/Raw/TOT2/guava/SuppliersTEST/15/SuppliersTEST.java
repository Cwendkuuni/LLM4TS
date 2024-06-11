package com.google.common.base;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.google.common.base.Suppliers;
import com.google.common.base.Supplier;
import org.junit.Test;
import java.util.concurrent.TimeUnit;

public class SuppliersTest {

    @Test
    public void testCompose() {
        Function<Integer, String> function = new Function<Integer, String>() {
            @Override
            public String apply(Integer input) {
                return "Result: " + input;
            }
        };
        Supplier<Integer> supplier = Suppliers.ofInstance(1);
        Supplier<String> composedSupplier = Suppliers.compose(function, supplier);

        String result = composedSupplier.get();
        assertEquals("Result: 1", result);

        // Edge cases
        assertThrows(NullPointerException.class, () -> Suppliers.compose(null, supplier));
        assertThrows(NullPointerException.class, () -> Suppliers.compose(function, null));
    }

    @Test
    public void testMemoize() {
        Supplier<Integer> supplier = Suppliers.ofInstance(1);
        Supplier<Integer> memoizedSupplier = Suppliers.memoize(supplier);
        Integer result1 = memoizedSupplier.get();
        Integer result2 = memoizedSupplier.get();

        assertEquals(result1, result2);

        // Edge cases
        assertThrows(NullPointerException.class, () -> Suppliers.memoize(null));
    }

    @Test
    public void testMemoizeWithExpiration() {
        Supplier<String> delegate = mock(Supplier.class);
        when(delegate.get()).thenReturn("Initial Value");

        Supplier<String> memoizedSupplier = Suppliers.memoizeWithExpiration(delegate, 10, TimeUnit.MILLISECONDS);

        String result1 = memoizedSupplier.get();
        String result2 = memoizedSupplier.get();

        assertEquals("Initial Value", result1);
        assertEquals("Initial Value", result2);

        long startTime = System.nanoTime();
        while ((System.nanoTime() - startTime) < 15 * 1000 * 1000) { // Wait for 15ms
            String result3 = memoizedSupplier.get();
            assertEquals("Initial Value", result3);
        }

        String result4 = memoizedSupplier.get();
        assertNotEquals("Initial Value", result4);

        // Edge cases
        assertThrows(NullPointerException.class, () -> Suppliers.memoizeWithExpiration(null, 10, TimeUnit.MILLISECONDS));
        assertThrows(NullPointerException.class, () -> Suppliers.memoizeWithExpiration(delegate, -1, TimeUnit.MILLISECONDS));
    }
}