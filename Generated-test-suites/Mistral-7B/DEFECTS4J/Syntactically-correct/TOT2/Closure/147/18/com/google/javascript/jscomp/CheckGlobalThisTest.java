import static org.junit.Assert.assertNotNull;
import static com.google.javascript.jscomp.DiagnosticType.warning;
import com.google.javascript.jscomp.CheckGlobalThis;
import com.google.javascript.jscomp.CompilationTestBase;

public class CheckGlobalThisTest extends CompilationTestBase {

  public void testCheckGlobalThisConstructor() {
    CheckGlobalThis checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    assertNotNull(checker);
  }
}