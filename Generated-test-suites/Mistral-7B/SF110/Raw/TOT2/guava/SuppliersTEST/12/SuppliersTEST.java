import static org.junit.Assert.*;
import com.google.common.base.Suppliers;

public class SuppliersTest {

    @Test
    public void testCompose() {
        Function<Integer, Integer> addOne = i -> i + 1;
        Supplier<Integer> supplier = Suppliers.ofInstance(42);
        Supplier<Integer> expectedComposed = Suppliers.compose(addOne, supplier);
        Supplier<Integer> actualComposed = Suppliers.compose(addOne, supplier);
        assertEquals(expectedComposed, actualComposed);
        assertEquals(43, actualComposed.get());
    }
}