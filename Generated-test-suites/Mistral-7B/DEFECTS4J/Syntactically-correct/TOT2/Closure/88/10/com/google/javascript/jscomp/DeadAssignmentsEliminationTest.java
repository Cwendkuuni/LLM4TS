import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeTraversal.traverse;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.DeadAssignmentsElimination;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;

import org.junit.Test;

public class DeadAssignmentsEliminationTest {

  @Test
  public void testDeadAssignmentsElimination() {
    String jsCode = "function main() {\n" +
            "  var x = 1;\n" +
            "  if (true) {\n" +
            "    var y = 1;\n" +
            "    x = y;\n" +
            "    y = 2;\n" +
            "  } else {\n" +
            "    x = 2;\n" +
            "  }\n" +
            "  return x;\n" +
            "}";

    AbstractCompiler compiler = new AbstractCompiler();
    CompilerPass deadAssignmentsElimination = new DeadAssignmentsElimination(compiler);

    Node root = AbstractNode.createTreeFromCode(jsCode);
    Scope externs = new Scope();
    deadAssignmentsElimination.process(externs, root);

    NodeTraversal nodeTraversal = new NodeTraversal(compiler, root, externs);
    traverse(deadAssignmentsElimination, nodeTraversal);

    StringBuilder actual = new StringBuilder();
    Node outputNode = root;
    while (outputNode != null && outputNode.getType() != Token.PROGRAM) {
      actual.append(outputNode.toSource(compiler.getOptions()));
      outputNode = outputNode.getParent();
    }

    String expected = "function main() {\n" +
            "  var x = 1;\n" +
            "  if (true) {\n" +
            "  } else {\n" +
            "    var x = 2;\n" +
            "  }\n" +
            "  return x;\n" +
            "}";

    assertEquals(expected, actual.toString());
  }

  @Test
  public void testDeadAssignmentsInFunctionCall() {
    String jsCode = "function main() {\n" +
            "  function foo(x) {\n" +
            "    x = 1;\n" +
            "  }\n" +
            "  foo(1);\n" +
            "  return 1;\n" +
            "}";

    AbstractCompiler compiler = new AbstractCompiler();
    CompilerPass deadAssignmentsElimination = new DeadAssignmentsElimination(compiler);

    Node root = AbstractNode.createTreeFromCode(jsCode);
    Scope externs = new Scope();
    deadAssignmentsElimination.process(externs, root);

    NodeTraversal nodeTraversal = new NodeTraversal(compiler, root, externs);
    traverse(deadAssignmentsElimination, nodeTraversal);

    StringBuilder actual = new StringBuilder();
    Node outputNode = root;
    while (outputNode != null && outputNode.getType() != Token.PROGRAM) {
      actual.append(outputNode.toSource(compiler.getOptions()));
      outputNode = outputNode.getParent();
    }

    String expected = "function main() {\n" +
            "  function foo(x) {} // Dead assignment x = 1 is removed\n" +
            "  foo(1);\n" +
            "  return 1;\n" +
            "}";

    assertEquals(expected, actual.toString());
  }

  @Test
  public void testDeadAssignmentsInLoop() {
    String jsCode = "function main() {\n" +
            "  for (var i = 0; i < 10; i++) {\n" +
            "    var j = i;\n" +
            "    j = j + 1;\n" +
            "  }\n" +
            "  return i;\n" +
            "}";

    AbstractCompiler compiler = new AbstractCompiler();
    CompilerPass deadAssignmentsElimination = new DeadAssignmentsElimination(compiler);

    Node root = AbstractNode.createTreeFromCode(jsCode);
    Scope externs = new Scope();
    deadAssignmentsElimination.process(externs, root);

    NodeTraversal nodeTraversal = new NodeTraversal(compiler, root, externs);
    traverse(deadAssignmentsElimination, nodeTraversal);

    StringBuilder actual = new StringBuilder();
    Node outputNode = root;
    while (outputNode != null && outputNode.getType() != Token.PROGRAM) {
      actual.append(outputNode.toSource(compiler.getOptions()));
      outputNode = outputNode.getParent();
    }

    String expected = "function main() {\n" +
            "  for (var i = 0; i < 10; i++) {\n" +
            "  }\n" +
            "  return i;\n" +
            "}";

    assertEquals(expected, actual.toString());
  }

  @Test
  public void testDeadAssignmentsInConditional() {
    String jsCode = "function main() {\n" +
            "  var x = 1;\n" +
            "  if (true) {\n" +
            "    x = 1;\n" +
            "  } else {\n" +
            "    x = 2;\n" +
            "  }\n" +
            "  return x;\n" +
            "}";

    AbstractCompiler compiler = new AbstractCompiler();
    CompilerPass deadAssignmentsElimination = new DeadAssignmentsElimination(compiler);

    Node root = AbstractNode.createTreeFromCode(jsCode);
    Scope externs = new Scope();
    deadAssignmentsElimination.process(externs, root);

    NodeTraversal nodeTraversal = new NodeTraversal(compiler, root, externs);
    traverse(deadAssignmentsElimination, nodeTraversal);

    StringBuilder actual = new StringBuilder();
    Node outputNode = root;
    while (outputNode != null && outputNode.getType() != Token.PROGRAM) {
      actual.append(outputNode.toSource(compiler.getOptions()));
      outputNode = outputNode.getParent();
    }

    String expected = "function main() {\n" +
            "  var x = 1;\n" +
            "  if (true) { // Dead assignment x = 1 is removed}\n" +
            "  }\n" +
            "  return x;\n" +
            "}";

    assertEquals(expected, actual.toString());
  }

  @Test(expected = java.lang.RuntimeException.class)
  public void testFunctionWithClosure() {
    String jsCode = "function main() {\n" +
            "  function foo() {\n" +
            "    var x = 1;\n" +
            "  }\n" +
            "  return foo;\n" +
            "}";

    AbstractCompiler compiler = new AbstractCompiler();
    CompilerPass deadAssignmentsElimination = new DeadAssignmentsElimination(compiler);

    Node root = AbstractNode.createTreeFromCode(jsCode);
    Scope externs = new Scope();
    try {
      deadAssignmentsElimination.process(externs, root);
      fail("Should have thrown a runtime exception");
    } catch (RuntimeException e) {
      // Expected
    }
  }

  @Test(expected = java.lang.IllegalArgumentException.class)
  public void testNullExterns() {
    AbstractCompiler compiler = new AbstractCompiler();
    CompilerPass deadAssignmentsElimination = new DeadAssignmentsElimination(compiler);

    try {
      deadAssignmentsElimination.process(null, null);
      fail("Should have thrown an IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      // Expected
    }
  }

  @Test(expected = java.lang.NullPointerException.class)
  public void testNullRoot() {
    AbstractCompiler compiler = new AbstractCompiler();
    CompilerPass deadAssignmentsElimination = new DeadAssignmentsElimination(compiler);

    try {
      deadAssignmentsElimination.process(null, null);
      fail("Should have thrown a NullPointerException");
    } catch (NullPointerException e) {
      // Expected
    }
  }

  @Test(expected = java.lang.NullPointerException.class)
  public void testNullCompiler() {
    AbstractCompiler compiler = null;
    CompilerPass deadAssignmentsElimination = new DeadAssignmentsElimination(compiler);

    try {
      deadAssignmentsElimination.process(null, null);
      fail("Should have thrown a NullPointerException");
    } catch (NullPointerException e) {
      // Expected
    }
  }
}