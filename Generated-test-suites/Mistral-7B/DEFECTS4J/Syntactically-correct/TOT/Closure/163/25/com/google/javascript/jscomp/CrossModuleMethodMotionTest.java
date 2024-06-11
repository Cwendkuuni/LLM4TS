import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.IdGenerator;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.JSModuleGraph;
import com.google.javascript.rhino.Node;

public class CrossModuleMethodMotionTest {

  @Test
  public void testCrossModuleMethodMotion() {
    // Given a simple Java script file with two modules A and B
    String script = "module A {" +
                    "  function foo() {" +
                    "    return 'A';" +
                    "  }" +
                    "}" +
                    "module B {" +
                    "  function foo() {" +
                    "    return 'B';" +
                    "  }" +
                    "}";

    // When compiling the script with CrossModuleMethodMotion pass
    Compiler compiler = new Compiler();
    CompilerPass crossModuleMethodMotion = new CrossModuleMethodMotion(
        compiler, new IdGenerator(), true);
    Node root = compiler.parseTree(script, null);
    crossModuleMethodMotion.process(null, root);

    // Then the method foo from module A should be moved to module B
    Node aFoo = root.lookupNode("module A.foo");
    Node bFoo = root.lookupNode("module B.foo");
    assertNotNull(bFoo);
    assertNull(aFoo);

    // And the behavior of the moved method should be preserved
    assertEquals("B", ((NodeFunction) bFoo.getFirstChild()).getBody().call(null, null).getString());

    // When compiling the script with no common module between A and B
    script = "module A {" +
            "  function foo() {" +
            "    return 'A';" +
            "  }" +
            "}" +
            "module C {" +
            "  function foo() {" +
            "    return 'C';" +
            "  }" +
            "}";

    root = compiler.parseTree(script, null);
    crossModuleMethodMotion.process(null, root);

    // Then the method foo should not be moved
    Node aFooInNewScript = root.lookupNode("module A.foo");
    Node cFoo = root.lookupNode("module C.foo");
    assertNotNull(aFooInNewScript);
    assertNotNull(cFoo);

    // When compiling the script with an unreferenced method
    script = "module A {" +
            "  function foo() {" +
            "    return 'A';" +
            "  }" +
            "}";

    root = compiler.parseTree(script, null);
    crossModuleMethodMotion.process(null, root);

    // Then the method should not be moved
    Node aFooUnreferenced = root.lookupNode("module A.foo");
    assertNotNull(aFooUnreferenced);
  }
}