package com.example.suppliers;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.google.common.base.Suppliers;
import org.junit.Test;

public class SuppliersTest {

    @Test
    public void testCompose() {
        Function<Integer, String> func = i -> i.toString();
        Supplier<Integer> supplier = mock(Supplier.class);
        when(supplier.get()).thenReturn(42);

        Supplier<String> expectedSupplier = Suppliers.ofInstance("42");
        Supplier<String> supplierComposition = Suppliers.compose(func, supplier);

        String result = supplierComposition.get();
        verify(supplier).get();
        assertEquals(expectedSupplier.get(), result);
    }

    @Test
    public void testMemoize() {
        Supplier<String> supplier1 = Suppliers.ofInstance("Hello");
        Supplier<String> supplier2 = Suppliers.memoize(supplier1);

        String result1 = supplier1.get();
        String result2 = supplier2.get();

        assertEquals(result1, result2);
    }

    @Test
    public void testMemoizeWithExpiration() {
        Supplier<String> supplier = Suppliers.ofInstance("Hello");
        Supplier<String> expiringMemoizingSupplier = Suppliers.memoizeWithExpiration(supplier, 100, TimeUnit.MILLISECONDS);

        String result1 = expiringMemoizingSupplier.get();
        String result2 = expiringMemoizingSupplier.get();

        assertEquals(result1, result2);

        try {
            Thread.sleep(150);
            String result3 = expiringMemoizingSupplier.get();
            fail("Expected an exception but got: " + result3);
        } catch (InterruptedException e) {
            // Expected
        }
    }
}