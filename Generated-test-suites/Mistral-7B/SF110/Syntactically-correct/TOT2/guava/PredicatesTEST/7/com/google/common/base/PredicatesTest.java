import static org.junit.Assert.*;
import static com.google.common.base.Predicates.*;

public class PredicatesTest {

  @Test
  public void testAlwaysTrue() {
    Predicate<Object> predicate = alwaysTrue();
    assertTrue(predicate.apply(null));
    assertTrue(predicate.apply(new Object()));
  }
}