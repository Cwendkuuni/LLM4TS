package com.google.common.base;
import static org.junit.Assert.assertEquals;
import com.google.common.base.Function;
import com.google.common.base.Supplier;
import org.junit.Test;

public class SuppliersTest {

    @Test
    public void testCompose() {
        Function<Integer, String> function = (num) -> num.toString();
        Supplier<Integer> supplier = Suppliers.ofInstance(42);
        Supplier<String> composedSupplier = Suppliers.compose(function, supplier);
        String result = composedSupplier.get();
        assertEquals("42", result);
    }
}