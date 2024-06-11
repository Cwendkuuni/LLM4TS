import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.concurrent.TimeUnit;

import org.junit.*;
import org.junit.Test;

public class SuppliersTest {

    @Test
    public void testCompose() {
        Function<Integer, Integer> addOne = input -> input + 1;
        Supplier<Integer> supplier = Suppliers.ofInstance(1);
        Supplier<Integer> composedSupplier = Suppliers.compose(addOne, supplier);
        assertEquals(2, composedSupplier.get());
    }

    @Test
    public void testMemoize() {
        Supplier<String> supplier = Suppliers.ofInstance("Hello");
        Supplier<String> memoizedSupplier = Suppliers.memoize(supplier);
        assertEquals("Hello", memoizedSupplier.get());
        assertEquals("Hello", memoizedSupplier.get());
    }

    @Test(expected = NullPointerException.class)
    public void testComposeWithNullFunction() {
        Function<Integer, Integer> nullFunction = null;
        Supplier<Integer> supplier = Suppliers.ofInstance(1);
        Suppliers.compose(nullFunction, supplier);
    }

    @Test(expected = NullPointerException.class)
    public void testComposeWithNullSupplier() {
        Function<Integer, Integer> addOne = input -> input + 1;
        Supplier<Integer> nullSupplier = null;
        Suppliers.compose(addOne, nullSupplier);
    }

    @Test
    public void testMemoizeWithDifferentTypes() {
        Supplier<Integer> integerSupplier = Suppliers.ofInstance(1);
        Supplier<String> stringSupplier = Suppliers.memoize(integerSupplier);
        assertEquals(1, stringSupplier.get()); // Should still be an integer, but implicitly convertible to String
    }

    @Test
    public void testMemoizeWithExpiration() {
        final long expirationNanos = 1;
        Supplier<String> supplier = Suppliers.ofInstance("Hello");
        Supplier<String> memoizedSupplier = Suppliers.memoizeWithExpiration(supplier, expirationNanos, TimeUnit.NANOS);
        assertEquals("Hello", memoizedSupplier.get());
        assertEquals("Hello", memoizedSupplier.get());

        Thread.yield();
        Thread.sleep(expirationNanos + 1);

        assertEquals("Hello", memoizedSupplier.get()); // Should get a new instance since the previous one expired
    }

    @Test(expected = NullPointerException.class)
    public void testOfInstanceWithNullInstance() {
        Suppliers.ofInstance(null);
    }

    @Test
    public void testSynchronizedSupplier() {
        Supplier<String> supplier = mock(Supplier.class);
        Supplier<String> synchronizedSupplier = Suppliers.synchronizedSupplier(supplier);

        Thread thread1 = new Thread(() -> {
            synchronized (synchronizedSupplier) {
                assertEquals("Expected value 1", "Value 1", synchronizedSupplier.get());
            }
        });

        Thread thread2 = new Thread(() -> {
            synchronized (synchronizedSupplier) {
                assertEquals("Expected value 2", "Value 2", synchronizedSupplier.get());
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
    }

    @Test(expected = NullPointerException.class)
    public void testSupplierFunctionWithNullInput() {
        SupplierFunction<Object> nullFunction = null;
        nullFunction.apply(null);
    }
}