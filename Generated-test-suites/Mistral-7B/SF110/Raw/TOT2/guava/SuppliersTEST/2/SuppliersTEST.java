import static org.junit.Assert.*;
import static com.google.common.base.Suppliers.*;

public class SuppliersTest {

    @Test
    public void testCompose() {
        Function<Integer, Integer> addOne = n -> n + 1;
        Supplier<Integer> supplierOfFive = ofInstance(5);
        Supplier<Integer> composedSupplier = compose(addOne, supplierOfFive);

        assertEquals(6, composedSupplier.get());
    }
}