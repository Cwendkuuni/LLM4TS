import static org.junit.Assert.*;
import static com.google.common.base.Predicates.*;

public class PredicatesTest {

  @Test
  public void testAlwaysTrue() {
    Predicate<Integer> alwaysTrue = alwaysTrue();
    assertTrue(alwaysTrue.apply(null));
    assertTrue(alwaysTrue.apply(0));
    assertTrue(alwaysTrue.apply(1));
    assertTrue(alwaysTrue.apply(new Object()));
  }
}