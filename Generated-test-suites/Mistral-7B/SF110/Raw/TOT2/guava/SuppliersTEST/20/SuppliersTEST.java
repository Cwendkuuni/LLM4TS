import org.junit.Test;
import static org.junit.Assert.*;
import com.google.common.base.Function;
import com.google.common.base.Supplier;

public class SuppliersTest {

  @Test
  public void testCompose() {
    Function<Integer, String> function = new Function<Integer, String>() {
      @Override
      public String apply(Integer input) {
        return input.toString();
      }
    };

    Supplier<Integer> supplier = new SupplierOfInstance<>(1);
    Supplier<String> composedSupplier = Suppliers.compose(function, supplier);

    String expectedValue = "1";
    String actualValue = composedSupplier.get();

    assertEquals(expectedValue, actualValue);
  }
}