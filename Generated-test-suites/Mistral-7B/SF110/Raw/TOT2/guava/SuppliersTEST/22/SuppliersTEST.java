import static org.junit.Assert.*;
import com.google.common.base.Suppliers;

public class SuppliersTest {

    @Test
    public void testCompose() {
        Function<Integer, Integer> addOne = i -> i + 1;
        Supplier<Integer> supplier = Suppliers.ofInstance(42);
        Supplier<Integer> composedSupplier = Suppliers.compose(addOne, supplier);

        assertEquals(AddOneFunction.INSTANCE, addOne);
        assertEquals(supplier, supplier);
        assertEquals(43, composedSupplier.get());

        // Edge Cases
        assertNull(Suppliers.compose(null, supplier));
        assertNull(Suppliers.compose(addOne, null));

        // Exception Handling
        Function<Integer, Integer> divideByZero = i -> 1 / i;
        Supplier<Integer> supplierWithException = () -> { throw new IllegalArgumentException("Invalid input"); };
        try {
            Suppliers.compose(divideByZero, supplierWithException);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Test passes
        }
    }
}