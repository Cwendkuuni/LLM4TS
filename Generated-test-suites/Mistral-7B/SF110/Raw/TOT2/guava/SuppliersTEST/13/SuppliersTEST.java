import static org.junit.Assert.*;
import static com.google.common.base.Suppliers.*;

public class SuppliersTest {

    @Test
    public void testCompose() {
        Function<Integer, Integer> addOne = (num) -> num + 1;
        Supplier<Integer> supplierOfOne = ofInstance(1);
        Supplier<Integer> composedSupplier = compose(addOne, supplierOfOne);
        assertEquals(2, composedSupplier.get());
    }

    @Test(expected = NullPointerException.class)
    public void testComposeNullFunction() {
        Function<Integer, Integer> nullFunction = null;
        Supplier<Integer> supplierOfOne = ofInstance(1);
        Suppliers.compose(nullFunction, supplierOfOne);
    }

    @Test(expected = NullPointerException.class)
    public void testComposeNullSupplier() {
        Function<Integer, Integer> addOne = (num) -> num + 1;
        Supplier<Integer> nullSupplier = null;
        Suppliers.compose(addOne, nullSupplier);
    }

    @Test(expected = ClassCastException.class)
    public void testComposeMismatchedTypes() {
        Function<String, Integer> addOneString = (str) -> str.length();
        Supplier<Double> supplierOfOne = ofInstance(1.0);
        Suppliers.compose(addOneString, supplierOfOne);
    }

    @Test
    public void testMemoize() {
        Supplier<Integer> supplier = () -> 42;
        Supplier<Integer> memoizedSupplier = memoize(supplier);
        assertEquals(42, memoizedSupplier.get());
        assertEquals(42, memoizedSupplier.get());
    }

    // Add test cases for remaining methods

    @Test
    public void testThreadSafety() {
        Supplier<Integer> supplier = () -> new Random().nextInt();
        Supplier<Integer> threadSafeSupplier = synchronizedSupplier(supplier);
        Thread thread1 = new Thread(() -> assertEquals(threadSafeSupplier.get(), threadSafeSupplier.get()));
        Thread thread2 = new Thread(() -> assertEquals(threadSafeSupplier.get(), threadSafeSupplier.get()));
        thread1.start();
        thread2.start();
        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}