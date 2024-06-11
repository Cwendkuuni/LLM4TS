import static org.junit.Assert.*;
import static com.google.common.base.Suppliers.*;

public class SuppliersTest {
    @Test
    public void testCompose() {
        Function<Integer, Integer> addOne = i -> i + 1;
        Supplier<Integer> supplier = ofInstance(42);
        Supplier<Integer> composedSupplier = compose(addOne, supplier);
        assertEquals(43, composedSupplier.get());
    }
}