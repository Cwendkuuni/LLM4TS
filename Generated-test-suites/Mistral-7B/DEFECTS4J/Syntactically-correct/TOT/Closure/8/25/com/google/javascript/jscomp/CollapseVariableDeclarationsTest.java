import static com.google.javascript.jscomp.NodeTraversal.AbstractPostOrderCallback.Scope.Var.VarType.VAR;
import static org.junit.Assert.*;
import static com.google.common.truth.Truth.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.SourceFile;

@RunWith(JUnit4.class)
public class CollapseVariableDeclarationsTest {
  private Compiler compiler;
  private CompilerOptions options;
  private CollapseVariableDeclarations pass;
  private NodeFactory nodeFactory;

  @Before
  public void setUp() throws Exception {
    options = new CompilerOptions();
    compiler = new Compiler();
    compiler.setOptions(options);
    compiler.setPrintStackTraces(true);
    nodeFactory = compiler.getFactory();
  }

  private Node createTestNode(String code) {
    SourceFile source = nodeFactory.createSourceFile("test.js", code);
    Node root = compiler.parse(source, null, null);
    compiler.initPasses();
    return root;
  }

  @Test
  public void testCollapseVariableDeclarations() {
    String input = "function foo() {\n" +
        "  var a;\n" +
        "  var b = 1;\n" +
        "  var c = 2;\n" +
        "  var d;\n" +
        "  var e = 3;\n" +
        "}";

    Node root = createTestNode(input);
    pass = new CollapseVariableDeclarations(compiler);
    pass.process(null, root);

    Node expected = nodeFactory.createBlock("var a, b = 1, c = 2, d, e = 3;");

    Node actual = root;
    NodeUtil.traverse(NodeUtil.typeOf(NodeUtil.rootNode(pass.getCompiledCode())), new TypeChecker())
        .accept(NodeUtil.rootNode(pass.getCompiledCode()));

    assertEquals(expected.toPrint(), actual.toPrint());
  }

  @Test
  public void testCollapseMultipleAssignments() {
    String input = "function foo() {\n" +
        "  a = true;\n" +
        "  b = true;\n" +
        "  var c = true;\n" +
        "}";

    Node root = createTestNode(input);
    pass = new CollapseVariableDeclarations(compiler);
    pass.process(null, root);

    Node expected = nodeFactory.createBlock("var c = b = a = true;");

    Node actual = root;
    NodeUtil.traverse(NodeUtil.typeOf(NodeUtil.rootNode(pass.getCompiledCode())), new TypeChecker())
        .accept(NodeUtil.rootNode(pass.getCompiledCode()));

    assertEquals(expected.toPrint(), actual.toPrint());
  }

  @Test
  public void testCollapseVariablesInsideFunction() {
    String input = "function foo() {\n" +
        "  function bar() {\n" +
        "    var a;\n" +
        "    var b = 1;\n" +
        "    var c = 2;\n" +
        "  }\n" +
        "}";

    Node root = createTestNode(input);
    pass = new CollapseVariableDeclarations(compiler);
    pass.process(null, root);

    Node expected = nodeFactory.createBlock("function bar() {\n" +
        "  var a, b = 1, c = 2;\n" +
        "}");

    Node actual = root;
    NodeUtil.traverse(NodeUtil.typeOf(NodeUtil.rootNode(pass.getCompiledCode())), new TypeChecker())
        .accept(NodeUtil.rootNode(pass.getCompiledCode()));

    assertEquals(expected.toPrint(), actual.toPrint());
  }

  @Test
  public void testDoNotCollapseVariablesInsideConditional() {
    String input = "function foo() {\n" +
        "  if (condition) {\n" +
        "    var a;\n" +
        "    var b = 1;\n" +
        "  }\n" +
        "}";

    Node root = createTestNode(input);
    pass = new CollapseVariableDeclarations(compiler);
    pass.process(null, root);

    Node expected = nodeFactory.createBlock("if (condition) {\n" +
        "  var a;\n" +
        "  var b = 1;\n" +
        "}");

    Node actual = root;
    NodeUtil.traverse(NodeUtil.typeOf(NodeUtil.rootNode(pass.getCompiledCode())), new TypeChecker())
        .accept(NodeUtil.rootNode(pass.getCompiledCode()));

    assertEquals(expected.toPrint(), actual.toPrint());
  }

  @Test
  public void testDoNotCollapseVariablesInsideLoop() {
    String input = "function foo() {\n" +
        "  for (var i = 0; i < 10; i++) {\n" +
        "    var a;\n" +
        "    var b = 1;\n" +
        "  }\n" +
        "}";

    Node root = createTestNode(input);
    pass = new CollapseVariableDeclarations(compiler);
    pass.process(null, root);

    Node expected = nodeFactory.createBlock("for (var i = 0; i < 10; i++) {\n" +
        "  var a;\n" +
        "  var b = 1;\n" +
        "}");

    Node actual = root;
    NodeUtil.traverse(NodeUtil.typeOf(NodeUtil.rootNode(pass.getCompiledCode())), new TypeChecker())
        .accept(NodeUtil.rootNode(pass.getCompiledCode()));

    assertEquals(expected.toPrint(), actual.toPrint());
  }

  private static class TypeChecker extends AbstractPostOrderCallback {
    @Override
    public void visit(NodeTraversal t, Node n, Node parent) {
      if (n.isBlock()) {
        assertNotNull(n.getFirstChild());
      }
    }
  }
}