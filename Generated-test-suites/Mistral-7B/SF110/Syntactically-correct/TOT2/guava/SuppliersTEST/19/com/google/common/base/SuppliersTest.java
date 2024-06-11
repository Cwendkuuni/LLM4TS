import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class SuppliersTest {
    @Test
    public void testCompose() {
        Function<Integer, Integer> function = i -> i * 2;
        Supplier<Integer> supplier = Suppliers.ofInstance(4);
        Supplier<Integer> composed = Suppliers.compose(function, supplier);
        assertEquals(10, composed.get());
    }

    @Test(expected = NullPointerException.class)
    public void testComposeWithNullFunction() {
        Function<Integer, Integer> function = null;
        Supplier<Integer> supplier = Suppliers.ofInstance(4);
        Suppliers.compose(function, supplier);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testComposeWithFunctionThatDoesntAcceptSupplierType() {
        Function<String, Integer> function = i -> i.length();
        Supplier<Integer> supplier = Suppliers.ofInstance(4);
        Suppliers.compose(function, supplier);
    }

    @Test
    public void testComposeWithNullSupplierOutput() {
        Function<Integer, Integer> function = i -> i * 2;
        Supplier<Integer> supplier = Suppliers.ofInstance(null);
        Suppliers.compose(function, supplier);
    }

    @Test(expected = RuntimeException.class)
    public void testComposeWithFunctionThatThrowsException() {
        Function<Integer, Integer> function = i -> {
            throw new RuntimeException("Function exception");
        };
        Supplier<Integer> supplier = Suppliers.ofInstance(4);
        Suppliers.compose(function, supplier);
    }
}