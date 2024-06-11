import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeTraversal.AbstractPostOrderCallback.Scope.Var.*;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;

import java.util.List;
import java.util.Set;

public class CollapseVariableDeclarationsTest {

  @org.junit.Test
  public void testCollapseSingleVariableDeclaration() {
    String input = "function f() {\n" +
        "  var a;\n" +
        "  var b = 1;\n" +
        "  var c = 2;\n" +
        "}";

    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions(null);

    Node root = compiler.parseTree(input, null, null);
    CollapseVariableDeclarations collapseVariableDeclarations =
        new CollapseVariableDeclarations(compiler);
    collapseVariableDeclarations.process(null, root);

    Node expected = new Node("function f() {\n" +
        "  var a, b = 1, c = 2;\n" +
        "}");

    NodeTraversal traversal = new NodeTraversal(compiler, root, null, null);
    List<Node> nodes = Lists.newArrayList();
    traversal.inOrder(new NodeTraversal.AbstractPostOrderCallback() {
      @Override
      public void visit(NodeTraversal t, Node n) {
        nodes.add(n);
      }
    });

    String actual = compiler.getSourceCode(root);
    assertEquals(expected.toString(), actual);
    assertEquals(3, nodes.size());
    assertEquals("function f() {\n" +
        "  var a, b = 1, c = 2;\n" +
        "}", nodes.get(0).toString());
  }

  @org.junit.Test
  public void testCollapseMultipleVariablesInSameLine() {
    String input = "function f() {\n" +
        "  var a, b = 1, c = 2, d = 3;\n" +
        "}";

    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions(null);

    Node root = compiler.parseTree(input, null, null);
    CollapseVariableDeclarations collapseVariableDeclarations =
        new CollapseVariableDeclarations(compiler);
    collapseVariableDeclarations.process(null, root);

    Node expected = new Node("function f() {\n" +
        "  var a, b = 1, c = 2, d;\n" +
        "}");

    NodeTraversal traversal = new NodeTraversal(compiler, root, null, null);
    List<Node> nodes = Lists.newArrayList();
    traversal.inOrder(new NodeTraversal.AbstractPostOrderCallback() {
      @Override
      public void visit(NodeTraversal t, Node n) {
        nodes.add(n);
      }
    });

    String actual = compiler.getSourceCode(root);
    assertEquals(expected.toString(), actual);
    assertEquals(4, nodes.size());
    assertEquals("function f() {\n" +
        "  var a, b = 1, c = 2, d;\n" +
        "}", nodes.get(0).toString());
  }

  @org.junit.Test
  public void testCollapseMultipleVariablesOnMultipleLines() {
    String input = "function f() {\n" +
        "  var a;\n" +
        "  var b = 1;\n" +
        "  var c = 2;\n" +
        "}";

    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions(null);

    Node root = compiler.parseTree(input, null, null);
    CollapseVariableDeclarations collapseVariableDeclarations =
        new CollapseVariableDeclarations(compiler);
    collapseVariableDeclarations.process(null, root);

    Node expected = new Node("function f() {\n" +
        "  var a, b = 1, c = 2;\n" +
        "}");

    NodeTraversal traversal = new NodeTraversal(compiler, root, null, null);
    List<Node> nodes = Lists.newArrayList();
    traversal.inOrder(new NodeTraversal.AbstractPostOrderCallback() {
      @Override
      public void visit(NodeTraversal t, Node n) {
        nodes.add(n);
      }
    });

    String actual = compiler.getSourceCode(root);
    assertEquals(expected.toString(), actual);
    assertEquals(3, nodes.size());
    assertEquals("function f() {\n" +
        "  var a, b = 1, c = 2;\n" +
        "}", nodes.get(0).toString());
  }

  @org.junit.Test
  public void testCollapseSingleVariableAssignment() {
    String input = "function f() {\n" +
        "  var a = 1;\n" +
        "  var b = 1;\n" +
        "  var c = 2;\n" +
        "}";

    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions(null);

    Node root = compiler.parseTree(input, null, null);
    CollapseVariableDeclarations collapseVariableDeclarations =
        new CollapseVariableDeclarations(compiler);
    collapseVariableDeclarations.process(null, root);

    Node expected = new Node("function f() {\n" +
        "  var a = b = 1, c = 2;\n" +
        "}");

    NodeTraversal traversal = new NodeTraversal(compiler, root, null, null);
    List<Node> nodes = Lists.newArrayList();
    traversal.inOrder(new NodeTraversal.AbstractPostOrderCallback() {
      @Override
      public void visit(NodeTraversal t, Node n) {
        nodes.add(n);
      }
    });

    String actual = compiler.getSourceCode(root);
    assertEquals(expected.toString(), actual);
    assertEquals(3, nodes.size());
    assertEquals("function f() {\n" +
        "  var a = b = 1, c = 2;\n" +
        "}", nodes.get(0).toString());
  }

  @org.junit.Test
  public void testCollapseMultipleVariableAssignments() {
    String input = "function f() {\n" +
        "  var a = 1;\n" +
        "  var b = 1;\n" +
        "  var c = 2;\n" +
        "  var d = 3;\n" +
        "}";

    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions(null);

    Node root = compiler.parseTree(input, null, null);
    CollapseVariableDeclarations collapseVariableDeclarations =
        new CollapseVariableDeclarations(compiler);
    collapseVariableDeclarations.process(null, root);

    Node expected = new Node("function f() {\n" +
        "  var a = b = 1, c = d = 3;\n" +
        "}");

    NodeTraversal traversal = new NodeTraversal(compiler, root, null, null);
    List<Node> nodes = Lists.newArrayList();
    traversal.inOrder(new NodeTraversal.AbstractPostOrderCallback() {
      @Override
      public void visit(NodeTraversal t, Node n) {
        nodes.add(n);
      }
    });

    String actual = compiler.getSourceCode(root);
    assertEquals(expected.toString(), actual);
    assertEquals(4, nodes.size());
    assertEquals("function f() {\n" +
        "  var a = b = 1, c = d = 3;\n" +
        "}", nodes.get(0).toString());
  }

  @org.junit.Test
  public void testCollapseVariableDeclarationsWithinFunction() {
    String input = "function f() {\n" +
        "  function g() {\n" +
        "    var a;\n" +
        "    var b = 1;\n" +
        "    var c = 2;\n" +
        "  }\n" +
        "  g();\n" +
        "}";

    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions(null);

    Node root = compiler.parseTree(input, null, null);
    CollapseVariableDeclarations collapseVariableDeclarations =
        new CollapseVariableDeclarations(compiler);
    collapseVariableDeclarations.process(null, root);

    Node expected = new Node("function f() {\n" +
        "  function g() {\n" +
        "    var a, b = 1, c = 2;\n" +
        "  }\n" +
        "  g();\n" +
        "}");

    NodeTraversal traversal = new NodeTraversal(compiler, root, null, null);
    List<Node> nodes = Lists.newArrayList();
    traversal.inOrder(new NodeTraversal.AbstractPostOrderCallback() {
      @Override
      public void visit(NodeTraversal t, Node n) {
        nodes.add(n);
      }
    });

    String actual = compiler.getSourceCode(root);
    assertEquals(expected.toString(), actual);
    assertEquals(3, nodes.size());
    assertEquals("function f() {\n" +
        "  function g() {\n" +
        "    var a, b = 1, c = 2;\n" +
        "  }\n" +
        "  g();\n" +
        "}", nodes.get(0).toString());
  }

  @org.junit.Test
  public void testCollapseVariableDeclarationsWithinVarStatement() {
    String input = "function f() {\n" +
        "  var a;\n" +
        "  var b = 1;\n" +
        "  var c = 2;\n" +
        "  var d;\n" +
        "}";

    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions(null);

    Node root = compiler.parseTree(input, null, null);
    CollapseVariableDeclarations collapseVariableDeclarations =
        new CollapseVariableDeclarations(compiler);
    collapseVariableDeclarations.process(null, root);

    Node expected = new Node("function f() {\n" +
        "  var a, b = 1, c = 2, d;\n" +
        "}");

    NodeTraversal traversal = new NodeTraversal(compiler, root, null, null);
    List<Node> nodes = Lists.newArrayList();
    traversal.inOrder(new NodeTraversal.AbstractPostOrderCallback() {
      @Override
      public void visit(NodeTraversal t, Node n) {
        nodes.add(n);
      }
    });

    String actual = compiler.getSourceCode(root);
    assertEquals(expected.toString(), actual);
    assertEquals(4, nodes.size());
    assertEquals("function f() {\n" +
        "  var a, b = 1, c = 2, d;\n" +
        "}", nodes.get(0).toString());
  }
}