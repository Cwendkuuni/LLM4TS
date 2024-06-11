import org.junit.Test;
import static org.mockito.Mockito.*;

public class CheckGlobalThisTest {
  @Test
  public void testCheckGlobalThisConstructor() {
    AbstractCompiler compilerMock = mock(AbstractCompiler.class);
    CheckLevel levelMock = mock(CheckLevel.class);

    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compilerMock, levelMock);

    verify(compilerMock).setSourceFile("sourceFile");
    verify(compilerMock).addErrorListener(checkGlobalThis);
  }
}