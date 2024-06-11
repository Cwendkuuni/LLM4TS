import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeUtil.isControlStructure;
import static com.google.javascript.jscomp.NodeUtil.isControlStructureCodeBlock;
import static com.google.javascript.jscomp.NodeUtil.isGet;
import static com.google.javascript.jscomp.NodeUtil.isLabel;
import static com.google.javascript.jscomp.NodeUtil.isObjectLit;
import static com.google.javascript.jscomp.NodeUtil.isSwitch;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeTraversal.Callback;
import com.google.javascript.jscomp.PreconditionsUtil;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.Token;

/**
 * Test class for PrepareAst.
 */
public class PrepareAstTest {

  private static class TestCompiler extends CompilerPass {
    @Override
    public void process(Node externs, Node root) {
      new PrepareAst(null, true).process(externs, root);
    }
  }

  @org.junit.Test
  public void testPrepareAst() {
    String source =
        "function foo(x) {\n"
        + "  if (x > 10) {\n"
        + "    return 1;\n"
        + "  } else {\n"
        + "    return 0;\n"
        + "  }\n"
        + "}\n";

    AbstractCompiler compiler = newAbstractCompilerFromString(source);
    Node root = compiler.parseTree(source);

    new TestCompiler().process(null, root);

    // Check that the AST is in a consistent state.
    assertEquals(0, root.getChildCount());
    Node ifNode = root.getFirstChild();
    assertEquals(1, ifNode.getChildCount());
    Node ifCondition = ifNode.getFirstChild();
    assertEquals(1, ifCondition.getChildCount());
    Node ifStatement = ifCondition.getFirstChild();
    assertEquals(3, ifStatement.getChildCount());
    Node ifBody = ifStatement.getLastChild();
    assertTrue(isBlock(ifBody));

    // Check that annotations are correctly placed.
    assertEquals(0, ifNode.getBooleanProp(Node.FREE_CALL));
    assertTrue(ifNode.getBooleanProp(Node.DIRECT_EVAL));
    assertFalse(ifCondition.getBooleanProp(Node.IS_DISPATCHER));
    assertFalse(ifCondition.getFirstChild().getBooleanProp(Node.IS_DISPATCHER));
    assertTrue(ifStatement.getBooleanProp(Node.IS_DISPATCHER));

    // Check that blocks are added to control structures.
    Node whileNode = root.getFirstChild().getNext();
    Node whileCondition = whileNode.getFirstChild();
    Node whileBody = whileNode.getLastChild();
    assertTrue(isBlock(whileBody));

    // Test object literal annotations.
    String objectSource =
        "var x = {\n"
        + "  a: 'b',\n"
        + "  c: function() {\n"
        + "    return 1;\n"
        + "  },\n"
        + "  d: 'e'\n"
        + "}";
    AbstractCompiler objectCompiler = newAbstractCompilerFromString(objectSource);
    Node objectRoot = objectCompiler.parseTree(objectSource);

    new TestCompiler().process(null, objectRoot);
    Node objlit = objectRoot;
    assertNull(objlit.getJSDocInfo());
    for (Node key = objlit.getFirstChild(); key != null; key = key.getNext()) {
      Node value = key.getFirstChild();
      if (value.isFunction()) {
        assertEquals("c", key.getString());
        assertEquals("function c() {\n"
            + "  return 1;\n"
            + "}", value.toString());
      } else {
        assertEquals("a", key.getString());
        assertEquals("b", value.getString());
        assertEquals("b", value.getStringProp(Node.VALUE));
      }
      Node valueJsDoc = value.getJSDocInfo();
      if (valueJsDoc != null) {
        assertEquals("c", valueJsDoc.getName());
        assertEquals("Function", valueJsDoc.getTag());
      }
    }
  }

  private static AbstractCompiler newAbstractCompilerFromString(String source) {
    PreconditionsUtil.checkArgument(source != null, "source is null");
    return new AbstractCompiler() {};
  }
}