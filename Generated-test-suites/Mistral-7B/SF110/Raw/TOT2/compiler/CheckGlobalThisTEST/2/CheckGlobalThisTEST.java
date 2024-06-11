import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.Test;

public class CheckGlobalThisTest {

  @Test
  public void testCheckGlobalThisConstructor() {
    AbstractCompiler compilerMock = mock(AbstractCompiler.class);
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compilerMock);
    assertNotNull(checkGlobalThis);
  }
}