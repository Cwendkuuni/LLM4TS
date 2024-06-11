package com.google.common.base;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.*;
import java.util.concurrent.Callable;
import java.util.concurrent.Supplier;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.MockitoAnnotations;

public class SuppliersTest {

    @SuppressWarnings("unchecked")
    private static <T> Supplier<T> createMockSupplier() {
        return mock(Supplier.class);
    }

    @SuppressWarnings("unchecked")
    private static <T> Function<Supplier<T>, T> supplierFunction() {
        return (Function<Supplier<T>, T>) SupplierFunctionImpl.INSTANCE;
    }

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @After
    public void cleanUp() {
        verifyNoMoreInteractions(any());
    }

    @Test
    public void testCompose() {
        final int value = 1;
        final Function<Integer, Integer> function = (Function<Integer, Integer>) (input) -> input * 2;
        Supplier<Integer> supplier = createMockSupplier();

        Supplier<Integer> composed = Suppliers.compose(function, supplier);

        ArgumentCaptor<Integer> argCaptor = ArgumentCaptor.forClass(Integer.class);
        verify(supplier).get();
        assertEquals(function.apply(value), composed.get());
        verify(supplier).get();
    }

    @Test
    public void testMemoize() {
        final int value = 1;
        final Supplier<Integer> supplier = new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                return value;
            }
        }.asSupplier();

        Supplier<Integer> memoized = Suppliers.memoize(supplier);

        assertEquals(value, memoized.get());
        assertEquals(value, memoized.get());
    }

    @Test
    public void testMemoizeWithExpiration() {
        final int value = 1;
        final long duration = 5;
        final TimeUnit unit = TimeUnit.MILLISECONDS;
        final Supplier<Integer> supplier = new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                return value;
            }
        }.asSupplier();

        Supplier<Integer> memoizedWithExpiration = Suppliers.memoizeWithExpiration(supplier, duration, unit);

        assertEquals(value, memoizedWithExpiration.get());
        assertEquals(value, memoizedWithExpiration.get());

        try {
            Thread.sleep(duration + 1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        assertEquals(value, memoizedWithExpiration.get());
        assertNull(memoizedWithExpiration.get());
    }

    @Test
    public void testOfInstance() {
        final String value = "value";
        Supplier<String> supplierOfInstance = Suppliers.ofInstance(value);

        assertEquals(value, supplierOfInstance.get());
        assertEquals(value, supplierOfInstance.get());
    }

    @Test
    public void testSynchronizedSupplier() {
        final int value = 1;
        final Supplier<Integer> supplier = new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                return value;
            }
        }.asSupplier();

        Supplier<Integer> synchronizedSupplier = Suppliers.synchronizedSupplier(supplier);

        assertEquals(value, synchronizedSupplier.get());
        assertEquals(value, synchronizedSupplier.get());

        final int thread1Value = 2;
        final Thread thread1 = new Thread(() -> assertEquals(value, synchronizedSupplier.get()));
        thread1.start();

        final int thread2Value = 3;
        final Thread thread2 = new Thread(() -> assertEquals(value, synchronizedSupplier.get()));
        thread2.start();

        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}