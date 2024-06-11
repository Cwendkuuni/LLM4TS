import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.jscomp.CheckGlobalThis;
import com.google.javascript.jscomp.Checker;
import com.google.javascript.rhino.Node;

public class CheckGlobalThisTest {
  @Test
  public void testSafeUsesOfThis() {
    Checker checker = new Checker();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(checker, CheckLevel.WARNING);
    Node code = parseJavaScript("function safeFunction() {}"); // Function annotation not necessary for safe use of this
    checker.process(code, null, checkGlobalThis);
    assertTrue(checker.getDiagnostics().isEmpty());
  }

  // Add test cases for other safe use cases of this keyword here...
}