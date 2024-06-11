import com.google.common.base.Supplier;
import com.google.common.base.Function;
import org.junit.Assert;
import org.junit.Test;
import java.util.concurrent.TimeUnit;

public class SuppliersTest {

    @SuppressWarnings("unchecked")
    @Test
    public void testCompose() {
        Function<Integer, String> function = new Function<Integer, String>() {
            @Override
            public String apply(Integer input) {
                return String.valueOf(input.doubleValue());
            }
        };
        Supplier<Integer> supplier = Suppliers.ofInstance(1);
        Supplier<String> composedSupplier = Suppliers.compose(function, supplier);
        String result = composedSupplier.get();
        Assert.assertEquals("1.0", result);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testComposeNullFunction() {
        Supplier<Integer> supplier = Suppliers.ofInstance(1);
        try {
            Suppliers.compose(null, supplier);
            Assert.fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testComposeNullSupplier() {
        Function<Integer, String> function = new Function<Integer, String>() {
            @Override
            public String apply(Integer input) {
                return String.valueOf(input.doubleValue());
            }
        };
        try {
            Suppliers.compose(function, null);
            Assert.fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test
    public void testMemoize() {
        Supplier<Integer> supplier = () -> 1;
        Supplier<Integer> memoizedSupplier = Suppliers.memoize(supplier);
        Assert.assertEquals(1, memoizedSupplier.get());
        Assert.assertEquals(1, memoizedSupplier.get());
    }

    @Test
    public void testMemoizeNullSupplier() {
        try {
            Suppliers.memoize(null);
            Assert.fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test
    public void testMemoizeWithExpirationPositiveDuration() {
        Supplier<Long> delegate = () -> 1L;
        Supplier<Long> expiringMemoizingSupplier = Suppliers.memoizeWithExpiration(delegate, 1, TimeUnit.SECONDS);
        Long result1 = expiringMemoizingSupplier.get();
        Assert.assertEquals(1L, result1);
        try {
            Thread.sleep(1000L + 100L);
            Long result2 = expiringMemoizingSupplier.get();
            Assert.assertEquals(1L, result2);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testMemoizeWithExpirationZeroDuration() {
        Supplier<Long> delegate = () -> 1L;
        Supplier<Long> expiringMemoizingSupplier = Suppliers.memoizeWithExpiration(delegate, 0, TimeUnit.SECONDS);
        Long result1 = expiringMemoizingSupplier.get();
        Assert.assertEquals(1L, result1);
        Long result2 = expiringMemoizingSupplier.get();
        Assert.assertEquals(1L, result2);
    }

    @Test
    public void testMemoizeWithExpirationNegativeDuration() {
        Supplier<Long> delegate = () -> 1L;
        try {
            Suppliers.memoizeWithExpiration(delegate, -1, TimeUnit.SECONDS);
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testOfInstanceNullInstance() {
        try {
            Suppliers.ofInstance(null);
            Assert.fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test
    public void testSynchronizedSupplier() {
        Supplier<Integer> supplier = () -> 1;
        Supplier<Integer> synchronizedSupplier = Suppliers.synchronizedSupplier(supplier);
        Thread thread1 = new Thread(() -> synchronizedSupplier.get());
        Thread thread2 = new Thread(() -> synchronizedSupplier.get());
        thread1.start();
        thread2.start();
        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        Assert.assertEquals(1, synchronizedSupplier.get());
        Assert.assertEquals(1, synchronizedSupplier.get());
    }
}