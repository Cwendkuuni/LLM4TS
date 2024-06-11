import org.junit.Test;
import static org.junit.Assert.*;

public class JSPredicateFormTest {
  @Test
  public void testConstructorWithNoArguments() {
    JSPredicateForm pf = new JSPredicateForm();
    assertEquals(0, pf.size());
  }
}