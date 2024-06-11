package com.google.common.base;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;

import java.util.concurrent.TimeUnit;
import javax.annotation.Nullable;
import org.junit.Test;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.base.Supplier;

public class SuppliersTest {
    @Test
    public void testCompose() {
        Function<String, Integer> addOne = new Function<String, Integer>() {
            @Override
            public Integer apply(@Nullable String input) {
                return Integer.valueOf(input.length() + 1);
            }
        };

        Supplier<String> stringSupplier = Suppliers.ofInstance("test");
        Supplier<Integer> result = Suppliers.compose(addOne, stringSupplier);

        assertEquals(Integer.valueOf(6), result.get());
    }

    // Additional test cases for other methods and edge cases
}