import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CheckLevel.*;
import static com.google.javascript.rhino.NodeType.*;
import static com.google.javascript.rhino.JSDocInfo.*;
import com.google.javascript.jscomp.CheckGlobalThis;
import com.google.javascript.jscomp.Diagnostics;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.rhino.*;

public class CheckGlobalThisTest {

  @Test
  public void testCheckGlobalThisConstructor() {
    Node node = new Node();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(new Compiler(), WARNING);
    assertEquals("Unexpected constructor behavior", null, checkGlobalThis);
  }
}