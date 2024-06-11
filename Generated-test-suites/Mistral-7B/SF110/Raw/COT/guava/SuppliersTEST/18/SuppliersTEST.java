import static org.junit.Assert.*;
import static com.google.common.base.Preconditions.*;

import org.junit.Test;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.Supplier;
import java.util.concurrent.TimeUnit;

public class SuppliersTest {

    @SuppressWarnings("unchecked")
    private static <T> Supplier<T> createSupplier(Class<T> clazz) {
        try {
            return (Supplier<T>) Suppliers.class.getDeclaredMethod("ofInstance", clazz).invoke(null);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testCompose() {
        Function<Integer, Integer> addOne = (number) -> number + 1;
        Supplier<Integer> supplier = createSupplier(Integer.class);

        Supplier<Integer> composedSupplier = Suppliers.compose(addOne, supplier);

        assertEquals(1, composedSupplier.get());
    }

    @Test(expected = NullPointerException.class)
    public void testComposeNullFunction() {
        Supplier<Integer> supplier = createSupplier(Integer.class);

        try {
            Suppliers.compose(null, supplier);
        } catch (NullPointerException e) {
            throw new AssertionError("Unexpected exception", e);
        }
    }

    @Test(expected = NullPointerException.class)
    public void testComposeNullSupplier() {
        Function<Integer, Integer> addOne = (number) -> number + 1;

        try {
            Suppliers.compose(addOne, null);
        } catch (NullPointerException e) {
            throw new AssertionError("Unexpected exception", e);
        }
    }

    @Test
    public void testMemoize() {
        Supplier<Integer> supplier = () -> 42;
        Supplier<Integer> memoizedSupplier = Suppliers.memoize(supplier);

        assertEquals(42, memoizedSupplier.get());
        assertEquals(42, memoizedSupplier.get());
    }

    @Test
    public void testMemoizeWithExpiration() {
        Supplier<Long> supplier = () -> System.nanoTime();
        Supplier<Long> expiringSupplier = Suppliers.memoizeWithExpiration(supplier, 100, TimeUnit.MILLISECONDS);

        long previousValue = 0;
        long currentValue = 0;

        for (int i = 0; i < 10; i++) {
            currentValue = expiringSupplier.get();
            assertTrue("Value should be different for each call", currentValue > previousValue);
            previousValue = currentValue;
        }

        // Check that the value is the same for the next 10 calls
        for (int i = 0; i < 10; i++) {
            currentValue = expiringSupplier.get();
            assertEquals("Value should be the same for each call", previousValue, currentValue);
        }

        // Check that the value is updated after 100ms
        try {
            Thread.sleep(110);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        currentValue = expiringSupplier.get();
        assertNotEquals("Value should be different after 100ms", previousValue, currentValue);
    }

    @Test(expected = NullPointerException.class)
    public void testMemoizeNullSupplier() {
        try {
            Suppliers.memoize(null);
        } catch (NullPointerException e) {
            throw new AssertionError("Unexpected exception", e);
        }
    }

    @Test
    public void testOfInstance() {
        String instance = "Suppliers test";
        Supplier<String> supplier = Suppliers.ofInstance(instance);

        assertEquals("Suppliers test", supplier.get());
    }

    @Test(expected = NullPointerException.class)
    public void testOfInstanceNullInstance() {
        try {
            Suppliers.ofInstance(null);
        } catch (NullPointerException e) {
            throw new AssertionError("Unexpected exception", e);
        }
    }

    @Test
    public void testSynchronizedSupplier() {
        Supplier<String> supplier = () -> "Suppliers test";
        Supplier<String> synchronizedSupplier = Suppliers.synchronizedSupplier(supplier);

        assertNotSame("Suppliers should not be the same instance", supplier, synchronizedSupplier);

        String thread1Value = synchronizedSupplier.get();
        String thread2Value = synchronizedSupplier.get();

        assertEquals("Values should be the same for different threads", thread1Value, thread2Value);
    }

}