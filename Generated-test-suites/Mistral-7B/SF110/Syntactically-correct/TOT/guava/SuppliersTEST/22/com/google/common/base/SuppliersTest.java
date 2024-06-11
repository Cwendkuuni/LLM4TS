import com.google.common.base.Function;
import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class SuppliersTest {

    private Supplier<String> stringSupplier;
    private Function<Supplier<String>, String> stringFunction;

    @Before
    public void setUp() {
        stringSupplier = Suppliers.ofInstance("Hello World");
        stringFunction = Suppliers.supplierFunction();
    }

    @Test
    public void testCompose() {
        Function<Supplier<String>, String> function = (Supplier<String> s) -> s.get().toUpperCase();
        Supplier<String> composed = Suppliers.compose(function, stringSupplier);
        assertEquals("HELLO WORLD", composed.get());
    }

    @Test
    public void testMemoize() {
        Supplier<Integer> supplier = () -> 42;
        Supplier<Integer> memoized = Suppliers.memoize(supplier);
        assertEquals(42, memoized.get());
        assertEquals(42, memoized.get());
    }

    @Test
    public void testMemoizeWithExpiration() {
        Supplier<Long> supplier = () -> System.currentTimeMillis();
        Supplier<Long> memoizedWithExpiration = Suppliers.memoizeWithExpiration(supplier, 100, TimeUnit.MILLISECONDS);
        long initial = memoizedWithExpiration.get();
        assertEquals(initial, memoizedWithExpiration.get());
        try {
            Thread.sleep(150);
        } catch (InterruptedException e) {
            fail("Interrupted");
        }
        assertNotEquals(initial, memoizedWithExpiration.get());
    }

    @Test
    public void testOfInstance() {
        Supplier<String> supplier = Suppliers.ofInstance("Test");
        assertEquals("Test", supplier.get());
    }

    @Test(expected = NullPointerException.class)
    public void testOfInstanceNull() {
        Suppliers.ofInstance(null);
    }

    @Test
    public void testSynchronizedSupplier() {
        Supplier<String> supplier = () -> "Synchronized";
        Supplier<String> synchronizedSupplier = Suppliers.synchronizedSupplier(supplier);
        assertSame(supplier, synchronizedSupplier.getDelegate());
        Thread thread1 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                assertEquals("Synchronized", synchronizedSupplier.get());
            }
        });
        Thread thread2 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                assertEquals("Synchronized", synchronizedSupplier.get());
            }
        });
        thread1.start();
        thread2.start();
        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            fail("Interrupted");
        }
    }
}