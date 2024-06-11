import static org.junit.Assert.*;
import com.google.common.base.Suppliers;

public class SuppliersTest {

    @Test
    public void testCompose() {
        Function<Integer, Integer> function = i -> i * 2;
        Supplier<Integer> supplier = Suppliers.ofInstance(1);
        Supplier<Integer> expectedSupplier = Suppliers.compose(function, supplier);
        Supplier<Integer> actualSupplier = Suppliers.compose(function, supplier);
        assertEquals(expectedSupplier, actualSupplier);
        assertEquals(2, expectedSupplier.get());
        assertEquals(2, actualSupplier.get());
    }
}