package com.google.common.base;

import static org.junit.Assert.*;

import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.Supplier;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class SuppliersTest {

    // Test for compose method
    @Test
    public void testCompose() {
        Function<Integer, Integer> function = new Function<Integer, Integer>() {
            @Override
            public Integer apply(Integer input) {
                return input + 1;
            }
        };

        Supplier<Integer> supplier = Suppliers.ofInstance(1);
        Supplier<Integer> expectedSupplier = new Supplier<Integer>() {
            @Override
            public Integer get() {
                return 2;
            }
        };

        Supplier<Integer> actualSupplier = Suppliers.compose(function, supplier);

        assertEquals(expectedSupplier, actualSupplier);
    }

    @Test(expected = NullPointerException.class)
    public void testComposeWithNullFunction() {
        Supplier<Integer> supplier = Suppliers.ofInstance(1);
        Function<Integer, Integer> function = null;

        try {
            Suppliers.compose(function, supplier);
        } catch (NullPointerException e) {
            throw new AssertionError("Unexpected exception", e);
        }

        fail("Expected NullPointerException");
    }

    @Test(expected = NullPointerException.class)
    public void testComposeWithNullSupplier() {
        Function<Integer, Integer> function = new Function<Integer, Integer>() {
            @Override
            public Integer apply(Integer input) {
                return input + 1;
            }
        };

        Supplier<Integer> supplier = null;

        try {
            Suppliers.compose(function, supplier);
        } catch (NullPointerException e) {
            throw new AssertionError("Unexpected exception", e);
        }

        fail("Expected NullPointerException");
    }

    // Test for memoize method
    @Test
    public void testMemoize() {
        Supplier<String> delegate = new Supplier<String>() {
            private String value;

            @Override
            public String get() {
                if (value == null) {
                    value = "Hello World";
                }
                return value;
            }
        };

        Supplier<String> actualSupplier = Suppliers.memoize(delegate);

        assertEquals("Hello World", actualSupplier.get());
        assertEquals("Hello World", actualSupplier.get());
    }

    // Test for memoizeWithExpiration method
    @Test
    public void testMemoizeWithExpiration() throws InterruptedException {
        Supplier<String> delegate = new Supplier<String>() {
            private String value;

            @Override
            public String get() {
                if (value == null) {
                    sleep(100);
                    value = "Hello World";
                }
                return value;
            }

            private void sleep(long millis) throws InterruptedException {
                Thread.sleep(millis);
            }
        };

        Supplier<String> actualSupplier = Suppliers.memoizeWithExpiration(delegate, 50, TimeUnit.MILLISECONDS);

        assertEquals("Hello World", actualSupplier.get());
        assertEquals("Hello World", actualSupplier.get());

        Thread.sleep(100);

        assertEquals("Hello World", actualSupplier.get()); // Old value should still be returned

        Thread.sleep(150);

        assertEquals("Hello World", actualSupplier.get()); // New value should be returned
    }

    @Test(expected = NullPointerException.class)
    public void testMemoizeWithNullDelegate() {
        Supplier<String> delegate = null;

        try {
            Suppliers.memoize(delegate);
        } catch (NullPointerException e) {
            throw new AssertionError("Unexpected exception", e);
        }

        fail("Expected NullPointerException");
    }

    // Test for ofInstance method
    @Test
    public void testOfInstance() {
        Supplier<String> expectedSupplier = new Supplier<String>() {
            @Override
            public String get() {
                return "Hello World";
            }
        };

        String instance = "Hello World";

        Supplier<String> actualSupplier = Suppliers.ofInstance(instance);

        assertEquals(expectedSupplier, actualSupplier);
    }

    @Test(expected = NullPointerException.class)
    public void testOfInstanceWithNullInstance() {
        Supplier<String> supplier = Suppliers.ofInstance((String)null);

        fail("Expected NullPointerException");
    }

    // Test for synchronizedSupplier method
    @Test
    public void testSynchronizedSupplier() {
        Supplier<String> delegate = new Supplier<String>() {
            private int counter = 0;

            @Override
            public String get() {
                counter++;
                return "Hello World " + counter;
            }
        };

        Supplier<String> actualSupplier = Suppliers.synchronizedSupplier(delegate);

        assertEquals("Hello World 1", actualSupplier.get());
        assertEquals("Hello World 1", actualSupplier.get());
        assertEquals("Hello World 2", actualSupplier.get());
    }

    // Test for supplierFunction method
    @Test
    public void testSupplierFunction() {
        Supplier<String> supplier = new Supplier<String>() {
            @Override
            public String get() {
                return "Hello World";
            }
        };

        Function<Supplier<String>, String> function = Suppliers.supplierFunction();

        assertEquals("Hello World", function.apply(supplier));
    }

    @Test(expected = ClassCastException.class)
    public void testSupplierFunctionWithNonSupplier() {
        Object nonSupplier = new Object();

        try {
            Suppliers.supplierFunction().apply(nonSupplier);
        } catch (ClassCastException e) {
            throw new AssertionError("Unexpected exception", e);
        }

        fail("Expected ClassCastException");
    }
}