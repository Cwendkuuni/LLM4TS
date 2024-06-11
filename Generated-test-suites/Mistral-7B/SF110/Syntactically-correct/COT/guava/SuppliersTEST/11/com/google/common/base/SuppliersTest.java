import com.google.common.base.Suppliers;
import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.Supplier;
import java.util.function.Function;

public class SuppliersTest {

    @SuppressWarnings("unchecked")
    @Test
    public void testCompose() {
        Function<Integer, Integer> addOne = x -> x + 1;
        Supplier<Integer> supplierOfFive = Suppliers.ofInstance(5);

        Supplier<Integer> supplier = Suppliers.compose(addOne, supplierOfFive);

        Assert.assertEquals(6, supplier.get());
    }

    @Test
    public void testComposeNullFunction() {
        Supplier<String> supplierOfString = Suppliers.ofInstance("Hello");
        Function<Supplier<String>, String> function = null;

        Assert.assertThrows(NullPointerException.class, () -> Suppliers.compose(function, supplierOfString));
    }

    @Test
    public void testComposeNullSupplier() {
        Function<Supplier<String>, String> function = x -> x.get().length() > 5 ? x.get().get() : "Short";
        Supplier<Supplier<String>> supplierOfSupplier = Suppliers.ofInstance(null);

        Assert.assertThrows(NullPointerException.class, () -> Suppliers.compose(function, supplierOfSupplier));
    }

    @Test
    public void testMemoize() {
        Supplier<String> supplier1 = () -> "One";
        Supplier<String> supplier2 = Suppliers.memoize(supplier1);

        Assert.assertEquals("One", supplier1.get());
        Assert.assertEquals("One", supplier2.get());
        Assert.assertEquals("One", supplier2.get());
    }

    @Test
    public void testMemoizeNullSupplier() {
        Assert.assertThrows(NullPointerException.class, () -> Suppliers.memoize(null));
    }

    @Test
    public void testMemoizeWithExpiration() {
        long duration = 10;
        TimeUnit unit = TimeUnit.MILLISECONDS;

        Supplier<String> supplier = () -> "Expensive Value";
        Supplier<String> expiringMemoizingSupplier = Suppliers.memoizeWithExpiration(supplier, duration, unit);

        String value1 = expiringMemoizingSupplier.get();
        String value2 = expiringMemoizingSupplier.get();

        long start = System.currentTimeMillis();

        try {
            Thread.sleep(duration + 100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        String value3 = expiringMemoizingSupplier.get();

        long end = System.currentTimeMillis();

        Assert.assertEquals("Expensive Value", value1);
        Assert.assertEquals("Expensive Value", value2);
        Assert.assertNotEquals("Expensive Value", value3);
    }

    @Test
    public void testMemoizeWithExpirationNullSupplier() {
        Assert.assertThrows(NullPointerException.class, () -> Suppliers.memoizeWithExpiration(null, 0, TimeUnit.MILLISECONDS));
    }

    @Test
    public void testOfInstance() {
        Supplier<String> supplier = Suppliers.ofInstance("Instance Value");
        Assert.assertEquals("Instance Value", supplier.get());
    }

    @Test
    public void testOfInstanceNull() {
        Assert.assertThrows(NullPointerException.class, () -> Suppliers.ofInstance(null));
    }

    @Test
    public void testSynchronizedSupplier() {
        Supplier<String> supplier = () -> "Synchronized Value";
        Supplier<String> synchronizedSupplier = Suppliers.synchronizedSupplier(supplier);

        Thread thread1 = new Thread(() -> {
            for (int i = 0; i < 100; i++) {
                Assert.assertEquals("Synchronized Value", synchronizedSupplier.get());
            }
        });

        Thread thread2 = new Thread(() -> {
            for (int i = 0; i < 100; i++) {
                Assert.assertEquals("Synchronized Value", synchronizedSupplier.get());
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

    @Test
    public void testSynchronizedSupplierNull() {
        Assert.assertThrows(NullPointerException.class, () -> Suppliers.synchronizedSupplier(null));
    }

}