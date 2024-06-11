package com.google.common.base.test;

import static org.junit.Assert.*;

import java.util.concurrent.*;

import org.junit.*;

import com.google.common.base.*;
import com.google.common.base.test.util.*;

@GwtTestCase
public class SuppliersTest {

    @SuppressWarnings("unchecked")
    @Test
    public void testCompose() {
        Function<String, Integer> addOne = new Function<String, Integer>() {
            @Override
            public Integer apply(String input) {
                return Integer.valueOf(Integer.parseInt(input) + 1);
            }
        };
        Supplier<String> stringSupplier = Suppliers.ofInstance("10");
        Supplier<Integer> integerSupplier = Suppliers.compose(addOne, stringSupplier);
        assertEquals(11, integerSupplier.get().intValue());
    }

    @Test(expected = NullPointerException.class)
    public void testComposeNullFunction() {
        Supplier<String> stringSupplier = Suppliers.ofInstance("10");
        Function<? super Object, T> nullFunction = null;
        Supplier<Integer> integerSupplier = Suppliers.compose(nullFunction, stringSupplier);
    }

    @Test(expected = NullPointerException.class)
    public void testComposeNullSupplier() {
        Function<String, Integer> addOne = new Function<String, Integer>() {
            @Override
            public Integer apply(String input) {
                return Integer.valueOf(Integer.parseInt(input) + 1);
            }
        };
        Supplier<Integer> integerSupplier = Suppliers.compose(addOne, null);
    }

    @Test
    public void testMemoize() {
        Supplier<Integer> supplier = new Supplier<Integer>() {
            int counter = 0;
            
            @Override
            public Integer get() {
                return Integer.valueOf(++counter);
            }
        };
        Supplier<Integer> memoizedSupplier = Suppliers.memoize(supplier);
        assertEquals(1, memoizedSupplier.get().intValue());
        assertEquals(1, memoizedSupplier.get().intValue());
    }

    @Test
    public void testMemoizeTwice() {
        Supplier<Integer> supplier = new Supplier<Integer>() {
            int counter = 0;
            
            @Override
            public Integer get() {
                return Integer.valueOf(++counter);
            }
        };
        Supplier<Integer> memoizedSupplier = Suppliers.memoize(Suppliers.memoize(supplier));
        assertEquals(1, memoizedSupplier.get().intValue());
        assertEquals(1, memoizedSupplier.get().intValue());
    }

    @Test(expected = NullPointerException.class)
    public void testMemoizeNullSupplier() {
        Suppliers.memoize(null);
    }

    @Test
    public void testMemoizeWithExpiration() {
        Supplier<Long> supplier = new Supplier<Long>() {
            long counter = 0L;

            @Override
            public Long get() {
                return Long.valueOf(++counter);
            }
        };
        Supplier<Long> memoizedSupplier = Suppliers.memoizeWithExpiration(supplier, 100, TimeUnit.MILLISECONDS);
        long initialValue = memoizedSupplier.get().longValue();
        assertEquals(1, memoizedSupplier.get().longValue() - initialValue);
        try {
            Thread.sleep(150);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertEquals(1, memoizedSupplier.get().longValue() - initialValue);
    }

    @Test
    public void testMemoizeWithExpirationTwice() {
        Supplier<Long> supplier = new Supplier<Long>() {
            long counter = 0L;

            @Override
            public Long get() {
                return Long.valueOf(++counter);
            }
        };
        Supplier<Long> memoizedSupplier = Suppliers.memoizeWithExpiration(supplier, 100, TimeUnit.MILLISECONDS);
        long initialValue = memoizedSupplier.get().longValue();
        assertEquals(1, memoizedSupplier.get().longValue() - initialValue);
        try {
            Thread.sleep(150);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertEquals(2, memoizedSupplier.get().longValue() - initialValue);
    }

    @Test(expected = NullPointerException.class)
    public void testMemoizeWithExpirationNullSupplier() {
        Suppliers.memoizeWithExpiration(null, 100, TimeUnit.MILLISECONDS);
    }

    @Test
    public void testOfInstance() {
        String stringValue = "Hello World!";
        Supplier<String> stringSupplier = Suppliers.ofInstance(stringValue);
        assertEquals(stringValue, stringSupplier.get());
    }

    @Test(expected = NullPointerException.class)
    public void testOfInstanceNullInstance() {
        Suppliers.ofInstance(null);
    }

    @Test
    public void testSynchronizedSupplier() {
        final AtomicInteger counter = new AtomicInteger(0);
        Supplier<Integer> supplier = new Supplier<Integer>() {
            @Override
            public Integer get() {
                counter.incrementAndGet();
                return Integer.valueOf(counter.get());
            }
        };
        Supplier<Integer> synchronizedSupplier = Suppliers.synchronizedSupplier(supplier);
        Thread thread1 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                synchronizedSupplier.get();
            }
        });
        Thread thread2 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                synchronizedSupplier.get();
            }
        });
        thread1.start();
        thread2.start();
        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertEquals(2000, counter.get());
    }

    @Test
    public void testSupplierFunction() {
        Supplier<String> stringSupplier = Suppliers.ofInstance("Hello World!");
        Function<Supplier<String>, String> function = Suppliers.supplierFunction();
        assertEquals("Hello World!", function.apply(stringSupplier));
    }
}