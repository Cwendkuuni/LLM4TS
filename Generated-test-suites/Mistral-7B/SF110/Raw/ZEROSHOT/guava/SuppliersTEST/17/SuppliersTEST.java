package com.google.common.base.test;

import static org.junit.Assert.*;
import static com.google.common.base.Preconditions.*;

import java.util.concurrent.*;
import org.junit.*;

import com.google.common.base.*;
import com.google.common.base.test.util.*;
import com.google.common.collect.*;

@SuppressWarnings("unchecked")
public class SuppliersTest {

    @SuppressWarnings("rawtypes")
    private static <T> Supplier<T> createSupplier() {
        return new Supplier<T>() {
            @Override
            public T get() {
                throw new UnsupportedOperationException();
            }
        };
    }

    @Test
    public void testCompose() throws Exception {
        Function<Integer, String> function = new Function<Integer, String>() {
            @Override
            public String apply(Integer input) {
                return input.toString();
            }
        };
        Supplier<Integer> supplier = Suppliers.ofInstance(42);
        Supplier<String> expected = Suppliers.compose(function, supplier);
        Supplier<String> actual = Suppliers.compose(function, supplier);
        assertEquals(expected, actual);
        assertEquals("42", actual.get());
    }

    @Test(expected = NullPointerException.class)
    public void testComposeNullFunction() {
        Supplier<Integer> supplier = Suppliers.ofInstance(42);
        Function<Integer, String> function = null;
        Suppliers.compose(function, supplier);
    }

    @Test(expected = NullPointerException.class)
    public void testComposeNullSupplier() {
        Function<Integer, String> function = new Function<Integer, String>() {
            @Override
            public String apply(Integer input) {
                return input.toString();
            }
        };
        Suppliers.compose(function, null);
    }

    @Test
    public void testMemoize() throws Exception {
        Supplier<String> delegate = new Supplier<String>() {
            private String result;

            @Override
            public String get() {
                if (result == null) {
                    result = "result";
                }
                return result;
            }
        };
        Supplier<String> expected = Suppliers.memoize(delegate);
        Supplier<String> actual = Suppliers.memoize(delegate);
        assertEquals(expected, actual);
        assertEquals("result", expected.get());
        assertEquals("result", actual.get());
    }

    @Test
    public void testMemoizeDifferentInstances() throws Exception {
        Supplier<String> delegate1 = new Supplier<String>() {
            private String result;

            @Override
            public String get() {
                if (result == null) {
                    result = "result1";
                }
                return result;
            }
        };
        Supplier<String> delegate2 = new Supplier<String>() {
            private String result;

            @Override
            public String get() {
                if (result == null) {
                    result = "result2";
                }
                return result;
            }
        };
        Supplier<String> expected1 = Suppliers.memoize(delegate1);
        Supplier<String> actual1 = Suppliers.memoize(delegate1);
        assertEquals(expected1, actual1);
        Supplier<String> expected2 = Suppliers.memoize(delegate2);
        Supplier<String> actual2 = Suppliers.memoize(delegate2);
        assertEquals(expected2, actual2);
        assertEquals("result1", expected1.get());
        assertEquals("result2", expected2.get());
    }

    @Test
    public void testMemoizeWithExpiration() throws Exception {
        Supplier<String> delegate = new Supplier<String>() {
            private String result;

            @Override
            public String get() {
                if (result == null) {
                    result = "result";
                }
                return result;
            }
        };
        Supplier<String> expected = Suppliers.memoizeWithExpiration(delegate, 100, TimeUnit.MILLISECONDS);
        Supplier<String> actual = Suppliers.memoizeWithExpiration(delegate, 100, TimeUnit.MILLISECONDS);
        assertEquals(expected, actual);
        assertEquals("result", expected.get());
        Thread.sleep(200);
        assertEquals("result", expected.get());
        Thread.sleep(500);
        assertEquals("result", actual.get());
    }

    @Test
    public void testOfInstance() throws Exception {
        Tester<String> tester = new Tester<String>() {
            @Override
            public void test(
                    @Nullable String input,
                    @Nullable String expectedOutput,
                    @Nullable String actualOutput) {
                assertEquals(expectedOutput, actualOutput);
            }
        };
        Supplier<String> expected = Suppliers.ofInstance("test");
        Supplier<String> actual = Suppliers.ofInstance("test");
        assertEquals(expected, actual);
        assertEquals("test", expected.get());
        assertEquals("test", actual.get());
        tester.test(null, null, null);
        tester.test("test", "test", "test");
    }

    @Test
    public void testSynchronizedSupplier() throws Exception {
        AtomicInteger counter = new AtomicInteger(0);
        Supplier<Integer> delegate = new Supplier<Integer>() {
            private int result;

            @Override
            public Integer get() {
                if (result == 0) {
                    result = counter.incrementAndGet();
                }
                return result;
            }
        };
        Supplier<Integer> expected = Suppliers.synchronizedSupplier(delegate);
        Supplier<Integer> actual = Suppliers.synchronizedSupplier(delegate);
        assertEquals(expected, actual);
        List<Integer> results = Lists.newArrayList();
        for (int i = 0; i < 10; i++) {
            results.add(expected.get());
            results.add(actual.get());
        }
        for (int i = 0; i < results.size(); i++) {
            assertEquals(i + 1, results.get(i));
            assertEquals(i + 1, results.get(i + 1));
        }
    }

    @Test
    public void testSupplierFunction() throws Exception {
        Function<Supplier<Object>, Object> function = Suppliers.supplierFunction();
        Supplier<Object> supplier = Suppliers.ofInstance(new Object());
        Object expected = function.apply(supplier);
        Object actual = function.apply(supplier);
        assertEquals(expected, actual);
        assertEquals(supplier.get(), actual);
    }
}