import static org.junit.Assert.*;
import org.junit.Test;

public class SuppliersTest {
    @Test
    public void testCompose() {
        Function<Integer, Integer> addOne = i -> i + 1;
        Supplier<Integer> supplierOfFive = Suppliers.ofInstance(5);

        Supplier<Integer> composedSupplier = Suppliers.compose(addOne, supplierOfFive);

        assertEquals(6, composedSupplier.get());
    }
}