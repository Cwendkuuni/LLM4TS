import static org.junit.Assert.*;
import static com.google.common.base.Suppliers.*;

public class SuppliersTest {

    // Test cases for compose method here

    // Test cases for memoize method here

    // Test cases for memoizeWithExpiration method here

    // Test cases for ofInstance method here

    // Test cases for synchronizedSupplier method here

    // Test cases for supplierFunction method here

    @Test
    public void testComposeFunctionAndSupplier() {
        Function<Integer, Integer> addOne = x -> x + 1;
        Supplier<Integer> supplier = of(1);
        Supplier<Integer> composedSupplier = compose(addOne, supplier);
        assertEquals(2, composedSupplier.get());
    }

    // Test cases for edge cases and exception handling scenarios for compose method here

    @Test
    public void testComposeNullFunctionOrNullSupplier() {
        Function<Integer, Integer> nullFunction = null;
        Supplier<Integer> nullSupplier = null;
        assertNull(compose(nullFunction, nullSupplier));
    }

    @Test
    public void testComposeFunctionWithNullArgument() {
        Function<Integer, Integer> addOne = x -> x + 1;
        Supplier<Integer> supplier = of(null);
        assertNull(compose(addOne, supplier).get());
    }

    @Test
    public void testComposeSupplierWithNullResult() {
        Function<Integer, Integer> addOne = x -> null;
        Supplier<Integer> supplier = of(1);
        assertNull(compose(addOne, supplier).get());
    }

    // Test other edge cases and exception handling scenarios for other methods here

}