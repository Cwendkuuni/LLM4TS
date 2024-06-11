import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

import org.junit.Test;

public class CheckGlobalThisTest {

  @Test
  public void testCheckGlobalThisConstructor() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler);
    assertNull(checkGlobalThis.assignLhsChild);
  }
}