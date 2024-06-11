import org.junit.Assert;
import org.junit.Test;

public class SuppliersTest {

    @Test
    public void testCompose() {
        Function<Integer, Integer> addOne = input -> input + 1;
        Supplier<Integer> supplierOfFive = Suppliers.ofInstance(5);
        Supplier<Integer> composedSupplier = Suppliers.compose(addOne, supplierOfFive);
        Assert.assertEquals(6, composedSupplier.get());
    }

    // Edge case: Check if compose works with null function or supplier
    @Test(expected = NullPointerException.class)
    public void testComposeWithNullFunction() {
        Function<Integer, Integer> nullFunction = null;
        Supplier<Integer> supplierOfFive = Suppliers.ofInstance(5);
        Suppliers.compose(nullFunction, supplierOfFive);
    }

    @Test(expected = NullPointerException.class)
    public void testComposeWithNullSupplier() {
        Function<Integer, Integer> addOne = input -> input + 1;
        Supplier<Integer> nullSupplier = null;
        Suppliers.compose(addOne, nullSupplier);
    }
}