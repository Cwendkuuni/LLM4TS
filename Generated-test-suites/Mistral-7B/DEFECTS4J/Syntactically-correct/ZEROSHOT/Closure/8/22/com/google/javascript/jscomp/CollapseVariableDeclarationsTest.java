import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeTraversal.AbstractPostOrderCallback.AbstractPostOrderCallbackFlag.*;
import static com.google.javascript.rhino.NodeType.*;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.Token;

import java.util.List;
import java.util.Set;

/**
 * Test class for CollapseVariableDeclarations.java.
 */
public class CollapseVariableDeclarationsTest {
  private AbstractCompiler compiler;
  private CollapseVariableDeclarations collapseVariableDeclarations;

  @Before
  public void setUp() {
    compiler = new CompilerOptions()
        .addPasses(new CompilerPass[]{new CollapseVariableDeclarations(compiler)})
        .compile("function foo() {}");
    collapseVariableDeclarations = (CollapseVariableDeclarations) compiler.getPassManager()
        .getPass(CollapseVariableDeclarations.class);
  }

  @Test
  public void testSimpleCollapse() {
    Node root = compiler.getRootNode();
    Node functionNode = root.getFirstChild();
    List<Node> variableDeclarations = Lists.newArrayList();

    NodeTraversal.traverse(compiler, functionNode, new GatherVariablesForTest());

    for (Node node : variableDeclarations) {
      assertEquals("var a, b = 1, c = 2;", getCode(node));
    }
  }

  @Test
  public void testSimpleCollapseWithComments() {
    String sourceCode =
        "function foo() {\n"
        + "  // Collapse these\n"
        + "  var a; \n"
        + "  var b = 1; \n"
        + "  var c = 2;\n"
        + "  // Collapse these\n"
        + "  var d = 3; \n"
        + "  var e = 4;\n"
        + "}";

    compiler = new CompilerOptions()
        .addInput("foo.js", sourceCode)
        .addPasses(new CompilerPass[]{new CollapseVariableDeclarations(compiler)})
        .compile();

    Node root = compiler.getRootNode();
    Node functionNode = root.getFirstChild();
    List<Node> variableDeclarations = Lists.newArrayList();

    NodeTraversal.traverse(compiler, functionNode, new GatherVariablesForTest());

    for (Node node : variableDeclarations) {
      assertEquals("var a, b = 1, c = 2, d = 3, e = 4;", getCode(node));
    }
  }

  @Test
  public void testSimpleCollapseWithAssignments() {
    String sourceCode =
        "function foo() {\n"
        + "  var a = 1;\n"
        + "  var b = a;\n"
        + "  var c = 2;\n"
        + "}";

    compiler = new CompilerOptions()
        .addInput("foo.js", sourceCode)
        .addPasses(new CompilerPass[]{new CollapseVariableDeclarations(compiler)})
        .compile();

    Node root = compiler.getRootNode();
    Node functionNode = root.getFirstChild();
    List<Node> variableDeclarations = Lists.newArrayList();

    NodeTraversal.traverse(compiler, functionNode, new GatherVariablesForTest());

    for (Node node : variableDeclarations) {
      Node varNode = node.getFirstChild();
      assertEquals("var a = 1, b = a, c = 2;", getCode(node));
      assertEquals("var a = 1, b = a;", getCode(varNode));
    }
  }

  @Test
  public void testComplexCollapse() {
    String sourceCode =
        "function foo() {\n"
        + "  var a;\n"
        + "  var b = 1;\n"
        + "  var c = 2;\n"
        + "  var d = 3;\n"
        + "  var e = 4;\n"
        + "  var e = 5;\n"
        + "  var f = g = 6;\n"
        + "  var g = 7;\n"
        + "}";

    compiler = new CompilerOptions()
        .addInput("foo.js", sourceCode)
        .addPasses(new CompilerPass[]{new CollapseVariableDeclarations(compiler)})
        .compile();

    Node root = compiler.getRootNode();
    Node functionNode = root.getFirstChild();
    List<Node> variableDeclarations = Lists.newArrayList();

    NodeTraversal.traverse(compiler, functionNode, new GatherVariablesForTest());

    for (Node node : variableDeclarations) {
      assertEquals("var a, b = 1, c = 2, d = 3, e = 5, f = g = 6, g;", getCode(node));
    }
  }

  @Test
  public void testCollapseWithFunctionDeclaration() {
    String sourceCode =
        "function foo() {\n"
        + "  function bar() {\n"
        + "    var a = 1;\n"
        + "    var b = 2;\n"
        + "  }\n"
        + "  var c = 3;\n"
        + "}";

    compiler = new CompilerOptions()
        .addInput("foo.js", sourceCode)
        .addPasses(new CompilerPass[]{new CollapseVariableDeclarations(compiler)})
        .compile();

    Node root = compiler.getRootNode();
    Node functionNode = root.getFirstChild();
    List<Node> variableDeclarations = Lists.newArrayList();

    NodeTraversal.traverse(compiler, functionNode, new GatherVariablesForTest());

    for (Node node : variableDeclarations) {
      Node functionDeclarationNode = null;
      for (Node child = node.getFirstChild(); child != null; child = child.getNext()) {
        if (child.getNodeType() == FUNCTION) {
          functionDeclarationNode = child;
          break;
        }
      }
      assertEquals("function bar() {var a = 1, b = 2;}", getCode(functionDeclarationNode));
      assertEquals("var c = 3;", getCode(node));
    }
  }

  @Test
  public void testCollapseVariableDeclarationsInsideForLoop() {
    String sourceCode =
        "function foo() {\n"
        + "  for (var i = 0; i < 3; i++) {\n"
        + "    var j = i;\n"
        + "    var k = i + 1;\n"
        + "  }\n"
        + "}";

    compiler = new CompilerOptions()
        .addInput("foo.js", sourceCode)
        .addPasses(new CompilerPass[]{new CollapseVariableDeclarations(compiler)})
        .compile();

    Node root = compiler.getRootNode();
    Node functionNode = root.getFirstChild();
    List<Node> variableDeclarations = Lists.newArrayList();

    NodeTraversal.traverse(compiler, functionNode, new GatherVariablesForTest());

    for (Node node : variableDeclarations) {
      assertEquals("var i = 0, j, k;", getCode(node));
    }
  }

  @Test
  public void testCollapseVariableDeclarationsInsideIfStatement() {
    String sourceCode =
        "function foo() {\n"
        + "  if (true) {\n"
        + "    var a = 1;\n"
        + "    var b = 2;\n"
        + "  } else {\n"
        + "    var c = 3;\n"
        + "    var d = 4;\n"
        + "  } \n"
        + "}";

    compiler = new CompilerOptions()
        .addInput("foo.js", sourceCode)
        .addPasses(new CompilerPass[]{new CollapseVariableDeclarations(compiler)})
        .compile();

    Node root = compiler.getRootNode();
    Node functionNode = root.getFirstChild();
    List<Node> variableDeclarations = Lists.newArrayList();

    NodeTraversal.traverse(compiler, functionNode, new GatherVariablesForTest());

    for (Node node : variableDeclarations) {
      assertEquals("var a = 1, b = 2;", getCode(node));
    }
  }

  private String getCode(Node node) {
    StringBuilder stringBuilder = new StringBuilder();
    NodeTraversal.traverse(compiler, node, new RecursiveCodePrinter(stringBuilder));
    return stringBuilder.toString();
  }

  private static class GatherVariablesForTest extends NodeTraversal.AbstractPostOrderCallback {
    @Override
    public void visit(NodeTraversal t, Node n, Node parent) {
      if (n.isVar() || n.isFunction()) {
        if (parent != null && parent.getNodeType() == STATEMENT) {
          List<Node> variables = (List<Node>) testVariables.get();
          variables.add(n);
        }
      }
    }
  }

  private static List<Node> testVariables = Lists.newArrayList();

  private static class RecursiveCodePrinter extends NodePrinter {
    private final StringBuilder stringBuilder;

    RecursiveCodePrinter(StringBuilder stringBuilder) {
      this.stringBuilder = stringBuilder;
    }

    @Override
    public void visit(NodeTraversal t, NodeType nodeType, Node n, Node parent) {
      super.visit(t, nodeType, n, parent);
      if (nodeType == STATEMENT) {
        Node varNode = n.getFirstChild();
        if (varNode != null && varNode.isVar()) {
          stringBuilder.append("var ");
          stringBuilder.append(varNode.getString());
          stringBuilder.append(" = ");
          Node assignNode = varNode.getNext();
          stringBuilder.append(getCode(assignNode));
          stringBuilder.append(";");
          stringBuilder.append(System.lineSeparator());
        }
      }
    }
  }
}