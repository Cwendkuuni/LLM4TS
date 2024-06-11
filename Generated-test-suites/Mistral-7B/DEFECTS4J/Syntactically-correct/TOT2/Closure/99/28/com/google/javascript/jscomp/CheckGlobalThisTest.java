import static org.junit.Assert.*;
import com.google.javascript.jscomp.CheckGlobalThis;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.NodeTraversal;

public class CheckGlobalThisTest {

  @Test
  public void testConstructor() {
    CheckLevel level = CheckLevel.WARNING;
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
    assertNotNull(checkGlobalThis);
  }

  
  // Add more test cases here
  
}