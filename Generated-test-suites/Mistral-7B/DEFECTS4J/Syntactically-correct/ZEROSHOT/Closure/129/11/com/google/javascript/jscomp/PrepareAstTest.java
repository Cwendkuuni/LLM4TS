import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.Token;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class PrepareAstTest {

  @Test
  public void testNormalizeNodeTypes() {
    AbstractCompiler compiler = new AbstractCompiler();
    PrepareAst prepareAst = new PrepareAst(compiler);

    Node root = createNode("if (true) {}", Token.IF);

    prepareAst.process(null, root);

    Node ifNode = root;
    assertTrue(ifNode.getFirstChild().isBlock());

    Node block = ifNode.getFirstChild();
    assertFalse(block.isBlock());
    assertNull(block.getParent());
  }

  @Test
  public void testNormalizeBlocks() {
    AbstractCompiler compiler = new AbstractCompiler();
    PrepareAst prepareAst = new PrepareAst(compiler);

    Node root = createNode("if (true) {}", Token.IF);

    prepareAst.process(null, root);

    Node ifNode = root;
    assertTrue(ifNode.getFirstChild().isBlock());

    Node block = ifNode.getFirstChild();
    assertTrue(block.isBlock());
    assertSame(ifNode, block.getParent());
  }

  @Test
  public void testPrepareAnnotations() {
    AbstractCompiler compiler = new AbstractCompiler();
    PrepareAst prepareAst = new PrepareAst(compiler);

    Node root = createNode("(function() {\n" +
        "  // JSDOC\n" +
        "  function Bar() {\n" +
        "    this.b = 1;\n" +
        "  }\n" +
        "})()", Token.FUNCTION);

    prepareAst.process(null, root);

    Node functionNode = root;
    Node annotationsNode = functionNode.getFirstChild();
    assertTrue(annotationsNode.isObjectLit());

    Node jsdocInfoNode = annotationsNode.getFirstChild();
    assertTrue(jsdocInfoNode.isName());
    assertSame("Bar", jsdocInfoNode.getString());
  }

  @Test
  public void testAnnotationCalls() {
    AbstractCompiler compiler = new AbstractCompiler();
    PrepareAst prepareAst = new PrepareAst(compiler);

    Node root = createNode("(function() {\n" +
        "  function Foo() {\n" +
        "    function Bar() {\n" +
        "      // JSDOC\n" +
        "      eval();\n" +
        "    }\n" +
        "    Bar();\n" +
        "  }\n" +
        "})()", Token.FUNCTION);

    prepareAst.process(null, root);

    Node functionNode = root;
    Node barNode = functionNode.getLastChild();
    assertTrue(barNode.isFunction());
    assertTrue(barNode.getBooleanProp(Node.DIRECT_EVAL));
  }

  @Test
  public void testAnnotationDispatchers() {
    AbstractCompiler compiler = new AbstractCompiler();
    PrepareAst prepareAst = new PrepareAst(compiler);

    Node root = createNode("var x = {\n" +
        "  get foo() {\n" +
        "    // JSDOC\n" +
        "    return eval('1');\n" +
        "  }\n" +
        "}", Token.OBJECT_LITERAL);

    prepareAst.process(null, root);

    Node objlitNode = root;
    Node getNode = objlitNode.getFirstChild();
    Node nameNode = getNode.getFirstChild();
    assertTrue(nameNode.isName());
    assertSame("foo", nameNode.getString());
    assertTrue(getNode.getBooleanProp(Node.IS_GETTER));
    assertTrue(getNode.getBooleanProp(Node.IS_DISPATCHER));
  }

  @Test
  public void testNormalizeObjectLiteralKeyAnnotations() {
    AbstractCompiler compiler = new AbstractCompiler();
    PrepareAst prepareAst = new PrepareAst(compiler);

    Node root = createNode("var x = {\n" +
        "  a: {\n" +
        "    // JSDOC\n" +
        "    get b() {\n" +
        "      return eval('1');\n" +
        "    }\n" +
        "  },\n" +
        "  c: 1\n" +
        "}", Token.OBJECT_LITERAL);

    prepareAst.process(null, root);

    Node objlitNode = root;
    Node aNode = objlitNode.getFirstChild();
    assertTrue(aNode.isObjectLit());

    Node bNode = aNode.getFirstChild();
    Node getNode = bNode.getFirstChild();
    assertTrue(getNode.isGet());

    Node jsdocInfoNode = bNode.getLastChild();
    assertTrue(jsdocInfoNode.isObjectLit());
    Node valueNode = jsdocInfoNode.getFirstChild();
    assertTrue(valueNode.isFunction());
  }

  private Node createNode(String code, int type) {
    return IR.parseTree(code, null, null).getRootNode().getFirstChild();
  }

  private static boolean isControlStructure(Node node) {
    return NodeUtil.isControlStructure(node)
        && !node.isLabel()
        && !node.isSwitch();
  }
}