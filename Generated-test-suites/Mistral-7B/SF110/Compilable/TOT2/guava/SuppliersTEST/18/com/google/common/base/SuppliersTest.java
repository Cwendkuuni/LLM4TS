package com.google.common.base;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class SuppliersTest {

    @Test
    public void testCompose() {
        Function<String, Integer> addOne = new Function<String, Integer>() {
            @Override
            public Integer apply(String input) {
                return Integer.valueOf(1 + Integer.parseInt(input));
            }
        };

        Supplier<String> stringSupplier = Suppliers.ofInstance("10");
        Supplier<Integer> integerSupplier = Suppliers.compose(addOne, stringSupplier);

        assertEquals(Integer.valueOf(11), integerSupplier.get());

        // Edge case test for null function
        Function<String, Integer> nullFunction = null;
        assertNull(Suppliers.compose(nullFunction, stringSupplier).get());

        // Edge case test for null supplier
        assertNull(Suppliers.compose(addOne, null).get());
    }
}