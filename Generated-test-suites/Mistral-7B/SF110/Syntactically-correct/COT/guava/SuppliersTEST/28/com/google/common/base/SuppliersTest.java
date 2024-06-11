import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import static org.junit.Assert.*;
import com.google.common.base.*;

@RunWith(JUnit4.class)
public class SuppliersTest {

    @Test
    public void testCompose() {
        Function<Integer, Integer> addOne = x -> x + 1;
        Supplier<Integer> supplierOfFive = Suppliers.ofInstance(5);
        Supplier<Integer> composedSupplier = Suppliers.compose(addOne, supplierOfFive);
        assertEquals(6, composedSupplier.get());
    }

    @Test(expected = NullPointerException.class)
    public void testComposeNullFunction() {
        Function<? super Integer, Integer> nullFunction = null;
        Supplier<Integer> supplierOfFive = Suppliers.ofInstance(5);
        Suppliers.compose(nullFunction, supplierOfFive);
    }

    @Test(expected = NullPointerException.class)
    public void testComposeNullSupplier() {
        Function<Integer, Integer> addOne = x -> x + 1;
        Supplier<Integer> nullSupplier = null;
        Suppliers.compose(addOne, nullSupplier);
    }

    @Test
    public void testMemoize() {
        Supplier<Integer> supplierOfSquare = new Supplier<Integer>() {
            int cachedValue;

            @Override
            public Integer get() {
                if (cachedValue == 0) {
                    cachedValue = 1 * 1;
                }
                return cachedValue;
            }
        };
        Supplier<Integer> memoizedSupplier = Suppliers.memoize(supplierOfSquare);
        assertEquals(1, memoizedSupplier.get());
        assertEquals(1, memoizedSupplier.get());
    }

    @Test
    public void testMemoizeWithExpiration() {
        long duration = 10;
        TimeUnit timeUnit = TimeUnit.MILLISECONDS;
        Function<Long, Long> function = x -> x * 2;
        Supplier<Long> supplierOfFiveHundered = Suppliers.ofInstance(500L);
        Supplier<Long> memoizedSupplierWithExpiration = Suppliers.memoizeWithExpiration(supplierOfFiveHundered, duration, timeUnit);
        long firstResult = memoizedSupplierWithExpiration.get();
        long secondResult = memoizedSupplierWithExpiration.get();
        long thirdResult = memoizedSupplierWithExpiration.get();
        assertEquals(500L, firstResult);
        assertEquals(500L, secondResult);
        assertEquals(500L, thirdResult);
        try {
            Thread.sleep(duration * 2);
            long fourthResult = memoizedSupplierWithExpiration.get();
            fail("Expected exception");
        } catch (InterruptedException e) {
            // Ignore
        }
        long fifthResult = memoizedSupplierWithExpiration.get();
        assertNotEquals(firstResult, fifthResult);
    }

    @Test(expected = NullPointerException.class)
    public void testMemoizeNullSupplier() {
        Supplier<Integer> nullSupplier = null;
        Suppliers.memoize(nullSupplier);
    }

    @Test
    public void testOfInstance() {
        String text = "Hello, World!";
        Supplier<String> supplier = Suppliers.ofInstance(text);
        assertEquals(text, supplier.get());
    }

    @Test
    public void testSynchronizedSupplier() {
        int count = 0;
        Supplier<Integer> supplier = new Supplier<Integer>() {
            @Override
            public Integer get() {
                count++;
                return count;
            }
        };
        Supplier<Integer> synchronizedSupplier = Suppliers.synchronizedSupplier(supplier);
        assertEquals(1, synchronizedSupplier.get());
        assertEquals(1, synchronizedSupplier.get());
        assertEquals(2, synchronizedSupplier.get());
    }

}