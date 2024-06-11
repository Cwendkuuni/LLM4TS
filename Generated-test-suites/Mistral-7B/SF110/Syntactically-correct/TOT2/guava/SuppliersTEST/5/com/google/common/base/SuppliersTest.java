import static org.junit.Assert.assertEquals;
import com.google.common.base.Suppliers;
import java.util.function.Function;
import java.util.function.Supplier;

public class SuppliersTest {

    @Test
    public void testCompose() {
        Function<String, Integer> addOne = s -> Integer.valueOf(s.length()) + 1;
        Supplier<String> stringSupplier = Suppliers.ofInstance("test");
        Supplier<Integer> composedSupplier = Suppliers.compose(addOne, stringSupplier);

        assertEquals(new Integer(6), composedSupplier.get());
    }
}