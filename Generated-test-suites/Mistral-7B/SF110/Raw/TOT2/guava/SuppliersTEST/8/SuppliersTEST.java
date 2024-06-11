import static org.junit.Assert.*;
import static com.google.common.base.FunctionalArgument.*;
import static com.google.common.base.Preconditions.*;
import com.google.common.base.*;

public class SuppliersTest {

  @Test
  public void testCompose() {
    Function<String, Integer> addOne = input -> input.length() + 1;
    Supplier<String> stringSupplier = Suppliers.ofInstance("Hello");
    Supplier<Integer> intSupplier = Suppliers.compose(addOne, stringSupplier);

    assertEquals(6, intSupplier.get()); // expected length of "Hello" + 1
  }

  @Test(expected = NullPointerException.class)
  public void testComposeNullFunction() {
    Function<String, Integer> addOne = null;
    Supplier<String> stringSupplier = Suppliers.ofInstance("Hello");
    Suppliers.compose(addOne, stringSupplier);
  }

  @Test(expected = NullPointerException.class)
  public void testComposeNullSupplier() {
    Function<String, Integer> addOne = input -> input.length() + 1;
    Supplier<String> stringSupplier = null;
    Suppliers.compose(addOne, stringSupplier);
  }
}