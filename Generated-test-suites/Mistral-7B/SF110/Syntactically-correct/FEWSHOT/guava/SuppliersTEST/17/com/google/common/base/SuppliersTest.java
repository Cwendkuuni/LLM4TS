package com.google.common.base;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Function;
import com.google.common.base.Supplier;
import com.google.common.base.ThreadSafeSupplier;
import com.google.common.base.Suppliers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@RunWith(MockitoJUnitRunner.class)
public class SuppliersTest {

    @Mock
    private Supplier<String> mockSupplier;

    private Supplier<String> supplier;

    @Before
    public void setUp() {
        supplier = mockSupplier;
    }

    @Test
    public void testCompose() {
        final Function<String, Integer> addOne = new Function<String, Integer>() {
            @Override
            public Integer apply(String input) {
                return Integer.valueOf(Integer.parseInt(input) + 1);
            }
        };

        final Supplier<Integer> expected = new Supplier<Integer>() {
            @Override
            public Integer get() {
                final String input = supplier.get();
                return addOne.apply(input);
            }
        };

        final Supplier<Integer> actual = Suppliers.compose(addOne, supplier);

        assertEquals(expected, actual);
    }

    @Test
    public void testMemoize() {
        final Supplier<String> delegate = mockSupplier;

        final Supplier<String> memoized = Suppliers.memoize(delegate);

        when(delegate.get()).thenReturn("foo");

        final String result1 = memoized.get();
        final String result2 = memoized.get();

        assertEquals(result1, result2);
    }

    @Test
    @Beta
    public void testMemoizeWithExpiration() {
        final Supplier<String> delegate = mockSupplier;

        final long duration = 10;
        final TimeUnit unit = TimeUnit.MILLISECONDS;

        final Supplier<String> expiringMemoized = Suppliers.memoizeWithExpiration(delegate, duration, unit);

        when(delegate.get()).thenReturn("foo");

        final String result1 = expiringMemoized.get();
        final String result2 = expiringMemoized.get();

        assertEquals(result1, result2);

        final long startTime = System.currentTimeMillis();

        for (int i = 0; i < 100; i++) {
            final String result3 = expiringMemoized.get();
            final long elapsedTime = System.currentTimeMillis() - startTime;
            assertEquals(result1, result3);
            assertEquals(duration, elapsedTime);
        }
    }

    @Test
    public void testOfInstance() {
        final String instance = "foo";

        final Supplier<String> expected = new SupplierOfInstance<String>(instance);

        final Supplier<String> actual = Suppliers.ofInstance(instance);

        assertEquals(expected, actual);
        assertEquals(instance, actual.get());
    }

    @Test
    public void testSynchronizedSupplier() {
        final Supplier<String> delegate = mockSupplier;

        final Supplier<String> synchronizedSupplier =Suppliers.synchronizedSupplier(delegate);

        final String result1 = synchronizedSupplier.get();
        final String result2 = synchronizedSupplier.get();

        assertEquals(result1, result2);
    }

    @Test
    public void testSupplierFunction() {
        final Supplier<String> supplier = mockSupplier;
        final Function<Supplier<String>, String> supplierFunction = Suppliers.supplierFunction();

        when(supplier.get()).thenReturn("foo");

        final String result = supplierFunction.apply(supplier);

        assertEquals("foo", result);
    }
}