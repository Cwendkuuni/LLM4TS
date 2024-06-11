import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import java.util.concurrent.Supplier;
import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.base.Suppliers;

@RunWith(JUnit4.class)
public class SuppliersTest {

    // Testing compose method
    @Test
    public void testCompose() {
        Function<Integer, Integer> function = new Function<Integer, Integer>() {
            @Override
            public Integer apply(Integer input) {
                return input + 1;
            }
        };
        Supplier<Integer> supplier = Suppliers.ofInstance(10);
        Supplier<Integer> expectedSupplier = new Supplier<Integer>() {
            @Override
            public Integer get() {
                return 11;
            }
        };
        Supplier<Integer> actualSupplier = Suppliers.compose(function, supplier);
        Assert.assertEquals(expectedSupplier, actualSupplier);
    }

    // Testing memoize method
    @Test
    public void testMemoize() {
        Supplier<Integer> supplier = new Supplier<Integer>() {
            int counter = 0;
            @Override
            public Integer get() {
                return counter++;
            }
        };
        Supplier<Integer> memoizedSupplier = Suppliers.memoize(supplier);
        Assert.assertEquals(0, memoizedSupplier.get());
        Assert.assertEquals(0, memoizedSupplier.get());
        Assert.assertEquals(1, memoizedSupplier.get());
        Assert.assertEquals(1, memoizedSupplier.get());
    }

    // Testing memoizeWithExpiration method
    @Test
    public void testMemoizeWithExpiration() throws InterruptedException {
        Supplier<Long> supplier = new Supplier<Long>() {
            @Override
            public Long get() {
                return System.nanoTime();
            }
        };
        Supplier<Long> expiringMemoizingSupplier = Suppliers.memoizeWithExpiration(supplier, 10, TimeUnit.MILLISECONDS);
        long initialValue = expiringMemoizingSupplier.get();
        Thread.sleep(20); // wait for expiration
        long newValue = expiringMemoizingSupplier.get();
        Assert.assertNotSame(initialValue, newValue);
    }

    // Testing ofInstance method
    @Test
    public void testOfInstance() {
        Supplier<String> supplier = Suppliers.ofInstance("Hello");
        Assert.assertEquals("Hello", supplier.get());
    }

    // Testing synchronizedSupplier method
    @Test
    public void testSynchronizedSupplier() {
        Supplier<Integer> supplier = new Supplier<Integer>() {
            private int i = 0;
            @Override
            public Integer get() {
                return i++;
            }
        };
        Supplier<Integer> synchronizedSupplier = Suppliers.synchronizedSupplier(supplier);
        Thread thread1 = new Thread(() -> Assert.assertEquals(0, synchronizedSupplier.get()));
        Thread thread2 = new Thread(() -> Assert.assertEquals(0, synchronizedSupplier.get()));
        thread1.start();
        thread2.start();
        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // Testing supplierFunction method
    @Test
    public void testSupplierFunction() {
        Supplier<String> supplier = new Supplier<String>() {
            @Override
            public String get() {
                return "Hello";
            }
        };
        Function<Supplier<String>, String> supplierFunction = Suppliers.supplierFunction();
        String expectedResult = "Hello";
        String result = supplierFunction.apply(supplier);
        Assert.assertEquals(expectedResult, result);
    }

    // Testing null arguments
    @Test(expected = NullPointerException.class)
    public void testNullArguments() {
        Function<Object, Object> function = null;
        Supplier<Object> supplier = new Supplier<Object>() {
            @Override
            public Object get() {
                return null;
            }
        };
        Suppliers.compose(function, supplier);
    }

    // Testing null delegate in memoizeWithExpiration
    @Test(expected = NullPointerException.class)
    public void testNullDelegateMemoizeWithExpiration() {
        Suppliers.memoizeWithExpiration(null, 10, TimeUnit.MILLISECONDS);
    }

    // Testing negative duration in memoizeWithExpiration
    @Test(expected = IllegalArgumentException.class)
    public void testNegativeDurationMemoizeWithExpiration() {
        Suppliers.memoizeWithExpiration(new Supplier<Integer>() {
            @Override
            public Integer get() {
                return 1;
            }
        }, -10, TimeUnit.MILLISECONDS);
    }
}