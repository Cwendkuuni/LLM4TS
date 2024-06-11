import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeUtil.*;
import static com.google.javascript.jscomp.NodeTraversal.*;

import org.junit.Before;
import org.junit.Test;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.PrepareAst;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.Token;

public class PrepareAstTest {

  private AbstractCompiler compiler;
  private CompilerOptions options;

  @Before
  public void setUp() {
    options = new CompilerOptions();
    compiler = new AbstractCompiler(options);
  }

  @Test
  public void testPrepareAst() {
    String source = "function foo() {}";
    Node root = AbstractCompiler.parse(source, null, null);

    PrepareAst pass = new PrepareAst(compiler, false);
    pass.process(null, root);

    // Check that no changes were reported
    assertTrue(pass.checkOnly);

    // Check that no annotations were added
    NodeTraversal.traverse(compiler, root, (NodeTraversal.Callback) node -> {
      assertFalse(node.hasProp(Node.IS_DISPATCHER));
      assertFalse(node.hasProp(Node.FREE_CALL));
      assertFalse(node.hasProp(Node.DIRECT_EVAL));
      return true;
    });

    // Check that no blocks were added to control structures
    NodeTraversal.traverse(compiler, root, (NodeTraversal.Callback) node -> {
      if (NodeUtil.isControlStructure(node) && !node.isLabel()) {
        Node firstChild = node.getFirstChild();
        assertNull(firstChild);
      }
      return true;
    });

    // Test with a simple object literal
    source = "{\n" +
        "  x: 1,\n" +
        "  y: function() {}\n" +
        "}";
    root = AbstractCompiler.parse(source, null, null);
    pass = new PrepareAst(compiler, false);
    pass.process(null, root);

    // Check that annotations were copied from keys to values
    Node objLit = root;
    Node x = objLit.getFirstChild();
    Node y = x.getNext();
    Node value = y.getFirstChild();
    assertEquals("function() {}\n  /* JSDoc */", value.getSource());

    // Test with an object literal with a getter method
    source = "{\n" +
        "  get x() {\n" +
        "    return 1;\n" +
        "  }\n" +
        "}";
    root = AbstractCompiler.parse(source, null, null);
    pass = new PrepareAst(compiler, false);
    pass.process(null, root);

    // Check that the getter method was annotated
    Node getter = objLit.getFirstChild().getLastChild();
    assertTrue(getter.hasProp(Node.IS_GETTER));
  }

  @Test(expected = AssertionError.class)
  public void testCheckOnlyNormalizesNodeTypes() {
    String source = "var x = 1;\n" +
        "if (true) {}";
    Node root = AbstractCompiler.parse(source, null, null);

    PrepareAst pass = new PrepareAst(compiler, true);
    pass.process(null, root);

    // Check that an error was thrown
  }
}