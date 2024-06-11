package com.google.common.base.test;

import com.google.common.base.Suppliers;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Supplier;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class SuppliersTest {

    private Supplier<Integer> supplier1;
    private Supplier<Integer> supplier2;
    private Supplier<Integer> supplier3;
    private Supplier<Integer> supplier4;

    @Before
    public void setup() {
        supplier1 = Suppliers.ofInstance(1);
        supplier2 = Suppliers.memoize(supplier1);
        supplier3 = Suppliers.memoizeWithExpiration(supplier1, 1, TimeUnit.SECONDS);
        supplier4 = Suppliers.compose(Suppliers.ofInstance(i -> i.intValue() * 2), supplier1);
    }

    @Test
    public void testSupplierOfInstance() {
        assertEquals(1, supplier1.get());
        assertEquals(1, supplier2.get());
        assertNotNull(supplier1);
        assertNotNull(supplier2);
    }

    @Test
    public void testMemoize() {
        assertEquals(1, supplier2.get());
        assertEquals(1, supplier2.get());
        assertEquals(1, supplier3.get());
        assertNull(supplier3.get());
        assertEquals(1, supplier2.get());
    }

    @Test
    public void testMemoizeWithExpiration() {
        assertEquals(1, supplier3.get());
        assertEquals(1, supplier3.get());
        assertEquals(1, supplier3.get());
        assertEquals(1, supplier3.get());
        assertNull(supplier3.get());
    }

    @Test
    public void testCompose() {
        assertEquals(2, supplier4.get());
        assertEquals(2, supplier4.get());
    }

    @Test(expected = NullPointerException.class)
    public void testComposeWithNullFunction() {
        Supplier<Integer> nullFunction = null;
        Supplier<Integer> supplier = Suppliers.ofInstance(1);
        Suppliers.compose(nullFunction, supplier);
    }

    @Test(expected = NullPointerException.class)
    public void testComposeWithNullSupplier() {
        Function<Integer, Integer> function = i -> i * 2;
        Supplier<Integer> nullSupplier = null;
        Suppliers.compose(function, nullSupplier);
    }

    @Test
    public void testSynchronizedSupplier() {
        ConcurrentHashMap<String, Supplier<Integer>> map = new ConcurrentHashMap<>();
        Supplier<Integer> synchronizedSupplier = Suppliers.synchronizedSupplier(() -> Integer.valueOf(42));
        map.put("key1", synchronizedSupplier);
        map.put("key2", Suppliers.ofInstance(43));

        for (int i = 0; i < 10; i++) {
            Supplier<Integer> supplierFromMap = map.get("key1");
            Supplier<Integer> supplierFromMap2 = map.get("key1");
            assertSame(supplierFromMap, supplierFromMap2);
            assertEquals(42, supplierFromMap.get());

            Supplier<Integer> supplierFromMap3 = map.get("key2");
            assertNotSame(supplierFromMap, supplierFromMap3);
            assertEquals(43, supplierFromMap3.get());
        }
    }
}