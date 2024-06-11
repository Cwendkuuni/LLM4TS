import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;

public class SuppliersTest {

    @Test
    public void testCompose() {
        Function<Integer, Integer> addOne = i -> i + 1;
        Supplier<Integer> supplier = Suppliers.ofInstance(1);

        Supplier<Integer> composedSupplier = Suppliers.compose(addOne, supplier);
        int result = composedSupplier.get();

        assertEquals(2, result);
    }
}