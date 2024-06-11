import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import com.google.common.base.Suppliers;
import java.util.concurrent.Supplier;
import java.util.concurrent.TimeUnit;

@RunWith(JUnit4.class)
public class SuppliersTest {

    @Test
    public void testCompose() {
        Function<Integer, Integer> function = i -> i * 2;
        Supplier<Integer> supplier = Suppliers.ofInstance(4);
        Supplier<Integer> expectedSupplier = Suppliers.compose(function, supplier);

        Assert.assertEquals(expectedSupplier.get(), 8);
    }

    @Test
    public void testMemoize() {
        Supplier<Integer> supplier = () -> 1;
        Supplier<Integer> memoizedSupplier = Suppliers.memoize(supplier);

        Assert.assertEquals(memoizedSupplier.get(), 1);
        Assert.assertEquals(memoizedSupplier.get(), 1);
    }

    @Test
    public void testMemoizeWithExpiration() {
        long duration = 100;
        TimeUnit timeUnit = TimeUnit.MILLISECONDS;
        Supplier<Integer> supplier = () -> 1;
        Supplier<Integer> memoizedSupplierWithExpiration = Suppliers.memoizeWithExpiration(supplier, duration, timeUnit);

        Assert.assertEquals(memoizedSupplierWithExpiration.get(), 1);
        try {
            Thread.sleep(duration + 50);
            Assert.assertEquals(memoizedSupplierWithExpiration.get(), 1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Assert.assertNotEquals(memoizedSupplierWithExpiration.get(), 1);
    }

    @Test(expected = NullPointerException.class)
    public void testComposeWithNullFunction() {
        Function<Integer, Integer> function = null;
        Supplier<Integer> supplier = Suppliers.ofInstance(4);
        Suppliers.compose(function, supplier);
    }

    @Test(expected = NullPointerException.class)
    public void testComposeWithNullSupplier() {
        Function<Integer, Integer> function = i -> i * 2;
        Supplier<Integer> supplier = null;
        Suppliers.compose(function, supplier);
    }

    @Test(expected = NullPointerException.class)
    public void testMemoizeWithNullDelegate() {
        Supplier<Integer> delegate = null;
        Suppliers.memoize(delegate);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMemoizeWithNegativeExpiration() {
        Supplier<Integer> supplier = () -> 1;
        Suppliers.memoizeWithExpiration(supplier, -1, TimeUnit.MILLISECONDS);
    }
}