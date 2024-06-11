import static org.junit.Assert.*;
import static com.google.common.base.Suppliers.*;
import static org.mockito.Mockito.*;

import java.util.concurrent.TimeUnit;

import org.junit.Test;

public class SuppliersTest {

    @Test
    public void testCompose() {
        Function<Integer, Integer> function = i -> i * 2;
        Supplier<Integer> supplier = ofInstance(1);
        Supplier<Integer> composedSupplier = compose(function, supplier);

        // typical use case
        assertEquals(2, composedSupplier.get());

        // edge case: supplier returns null
        supplier = ofInstance(null);
        try {
            composedSupplier.get();
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // expected
        }

        // error scenario: function throws an exception
        function = i -> {
            throw new RuntimeException("Function error");
        };
        try {
            composedSupplier = compose(function, supplier);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testMemoize() {
        Supplier<Integer> supplier = ofInstance(1);
        Supplier<Integer> memoizedSupplier = memoize(supplier);

        // typical use case
        assertEquals(1, memoizedSupplier.get());
        assertEquals(1, memoizedSupplier.get());

        // edge case: supplier returns null
        supplier = ofInstance(null);
        memoizedSupplier = memoize(supplier);
        try {
            memoizedSupplier.get();
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // expected
        }

        // error scenario: supplier throws an exception
        supplier = new ThrowingSupplier<>();
        try {
            memoizedSupplier = memoize(supplier);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testMemoizeWithExpiration() {
        Function<Integer, Integer> function = i -> i;
        Supplier<Integer> supplier = mock(Supplier.class);
        when(supplier.get()).thenReturn(1);
        Supplier<Integer> memoizedSupplier = memoizeWithExpiration(supplier, 100, TimeUnit.MILLISECONDS);

        // typical use case
        assertEquals(1, memoizedSupplier.get());
        assertEquals(1, memoizedSupplier.get());

        // edge case: supplier returns null
        supplier = mock(Supplier.class);
        when(supplier.get()).thenReturn(null);
        try {
            memoizedSupplier.get();
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // expected
        }

        // edge case: expiration time is zero
        memoizedSupplier = memoizeWithExpiration(supplier, 0, TimeUnit.MILLISECONDS);
        assertEquals(1, memoizedSupplier.get());
        assertEquals(1, memoizedSupplier.get());
        try {
            Thread.sleep(1);
            memoizedSupplier.get();
            fail("Expected StaleStateException");
        } catch (StaleStateException e) {
            // expected
        }

        // error scenario: supplier throws an exception
        when(supplier.get()).thenThrow(new RuntimeException("Supplier error"));
        try {
            memoizedSupplier = memoizeWithExpiration(supplier, 100, TimeUnit.MILLISECONDS);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    private static class ThrowingSupplier<T> implements Supplier<T> {
        @Override
        public T get() {
            throw new RuntimeException("Supplier error");
        }
    }
}