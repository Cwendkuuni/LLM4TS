import static org.junit.Assert.*;
import com.google.javascript.jscomp.CrossModuleMethodMotion;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.Parser;

public class CrossModuleMethodMotionTest {

  @org.junit.Before
  public void setUp() throws Exception {
    Parser parser = new Parser();
    Compiler compiler = new Compiler();
    compiler.addPass(new CompilerPass() {
      @Override
      public void process(Node externRoot, Node root) {
        new CrossModuleMethodMotion(compiler, new CrossModuleMethodMotion.IdGenerator(), true).process(externRoot, root);
      }
    });
    compiler.setProgramLoader(new TestLoader());
    compiler.compile(parser.parse("test.js", null), null);
    root = compiler.getRootNode();
  }

  @org.junit.Test
  public void testMoveMethod() {
    // Basic functionality test
    Node method = root.getFirstChild();
    assertNotNull(method);
    assertEquals("A.prototype.b", method.getFirstChild().getName());
    assertEquals("function(){}", method.getChild(1).toString());

    // Test moving method to deeper module
    Node deepestCommonModule = root.getLastChild();
    method = (Node) deepestCommonModule.getFirstChild();
    assertEquals("B.prototype.b", method.getFirstChild().getName());
    Node newMethod = method.getLastChild();
    assertEquals("function(){}", newMethod.toString());

    // Test method call
    Node call = root.getLastChild().getLastChild();
    assertEquals("A.prototype.b()", call.toString());

    // Test method call after moving
    Node newCall = deepestCommonModule.getLastChild();
    assertEquals("B.prototype.b()", newCall.toString());
  }

  @org.junit.Test(expected = AssertionError.class)
  public void testNullDeepestCommonModuleError() {
    // Test null deepest common module error
    Node nullDeepestCommonModule = new Node(Node.OBJECT_PROPERTY, null,
        new Node(Node.IDENTIFIER, "null"));
    new CrossModuleMethodMotion(null, new CrossModuleMethodMotion.IdGenerator(), true).process(null, null);
  }
}