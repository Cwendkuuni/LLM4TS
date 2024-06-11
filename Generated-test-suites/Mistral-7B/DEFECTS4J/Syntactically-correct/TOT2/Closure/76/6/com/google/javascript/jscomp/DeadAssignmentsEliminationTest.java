import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeUtil.*;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.AbstractPostOrderCallback;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.DeadAssignmentsElimination;
import com.google.javascript.jscomp.LiveVariablesAnalysis;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.Token;

import org.junit.Test;

/**
 * Tests for the DeadAssignmentsElimination class.
 */
public class DeadAssignmentsEliminationTest {

  @Test
  public void testDeadAssignmentsElimination() {
    String input = "function deadAssignmentsExample() {" +
        "  var x = 1, y = 2;" +
        "  if (x > 1) {" +
        "    x = 3;" +
        "  } else {" +
        "    y = 4;" +
        "  }" +
        "  return x + y;" +
        "}";

    AbstractCompiler compiler = new AbstractCompiler();
    DeadAssignmentsElimination pass = new DeadAssignmentsElimination(compiler);

    Node externs = null;
    Node root = parseAndTraverse(input, externs);

    pass.process(externs, root);

    Node transformed = root;
    Node expected = parseAndTraverse("" +
        "function deadAssignmentsExample() {" +
        "  var x = 1, y = 2;" +
        "  if (x > 1) {" +
        "  } else {" +
        "    y = 4;" +
        "  }" +
        "  return x + y;" +
        "}", null);

    // Perform a shallow comparison to ensure that the structure and order
    // of nodes is the same.
    assertEquals(expected.getStructure(), transformed.getStructure());
  }

  @Test
  public void testDeadAssignmentInLoop() {
    String input = "function deadAssignmentInLoop() {" +
        "  var sum = 0;" +
        "  for (var i = 0; i < 10; i++) {" +
        "    sum += i;" +
        "    sum = sum;" +
        "  }" +
        "  return sum;" +
        "}";

    AbstractCompiler compiler = new AbstractCompiler();
    DeadAssignmentsElimination pass = new DeadAssignmentsElimination(compiler);

    Node externs = null;
    Node root = parseAndTraverse(input, externs);

    pass.process(externs, root);

    Node transformed = root;
    Node expected = parseAndTraverse("function deadAssignmentInLoop() {" +
        "  var sum = 0;" +
        "  for (var i = 0; i < 10; i++) {" +
        "    sum += i;" +
        "  }" +
        "  return sum;" +
        "}", null);

    // Perform a shallow comparison to ensure that the structure and order
    // of nodes is the same.
    assertEquals(expected.getStructure(), transformed.getStructure());
  }

  @Test
  public void testDeadAssignmentInCondition() {
    String input = "function deadAssignmentInCondition() {" +
        "  if (x = 1) {" +
        "    return 1;" +
        "  } else {" +
        "    var y = 2;" +
        "    return y;" +
        "  }" +
        "}";

    AbstractCompiler compiler = new AbstractCompiler();
    DeadAssignmentsElimination pass = new DeadAssignmentsElimination(compiler);

    Node externs = null;
    Node root = parseAndTraverse(input, externs);

    pass.process(externs, root);

    Node transformed = root;
    Node expected = parseAndTraverse("function deadAssignmentInCondition() {" +
        "  if (true) {" +
        "    return 1;" +
        "  } else {" +
        "    var y = 2;" +
        "    return y;" +
        "  }" +
        "}", null);

    // Perform a shallow comparison to ensure that the structure and order
    // of nodes is the same.
    assertEquals(expected.getStructure(), transformed.getStructure());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testEmptyInput() {
    AbstractCompiler compiler = new AbstractCompiler();
    DeadAssignmentsElimination pass = new DeadAssignmentsElimination(compiler);

    Node root = null;
    pass.process(null, root);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNullRoot() {
    AbstractCompiler compiler = new AbstractCompiler();
    DeadAssignmentsElimination pass = new DeadAssignmentsElimination(compiler);

    Node root = null;
    pass.process(null, root);
  }

  @Test
  public void testDeadAssignmentWithReturn() {
    String input = "function deadAssignmentWithReturn() {" +
        "  var x = 1;" +
        "  return x = 2;" +
        "}";

    AbstractCompiler compiler = new AbstractCompiler();
    DeadAssignmentsElimination pass = new DeadAssignmentsElimination(compiler);

    Node externs = null;
    Node root = parseAndTraverse(input, externs);

    try {
      pass.process(externs, root);
      fail("Expected an IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      // Expected
    }
  }

  @Test
  public void testDeadAssignmentWithVar() {
    String input = "function deadAssignmentWithVar() {" +
        "  var x = 1;" +
        "  x = 2;" +
        "}" +
        "function main() {" +
        "  deadAssignmentWithVar();" +
        "}" +
        "main();";

    AbstractCompiler compiler = new AbstractCompiler();
    DeadAssignmentsElimination pass = new DeadAssignmentsElimination(compiler);

    Node externs = parseAndTraverse(input.substring(0, input.indexOf("function")), null);
    Node root = parseAndTraverse(input, externs);

    pass.process(externs, root);

    Node transformed = root;
    Node expected = parseAndTraverse("function deadAssignmentWithVar() {}" +
        "function main() {" +
        "  deadAssignmentWithVar();" +
        "}", null);

    // Perform a shallow comparison to ensure that the structure and order
    // of nodes is the same.
    assertEquals(expected.getStructure(), transformed.getStructure());
  }

  private static Node parseAndTraverse(String code, Node parent) {
    Node externs = null;
    Node root = null;

    AbstractCompiler compiler = new AbstractCompiler();
    root = compiler.parseTree(code, externs, null);

    if (parent != null) {
      Scope scope = compiler.getGlobalScope();
      scope.addChildren(root);
      NodeTraversal.traverse(compiler, root, new AbstractPostOrderCallback() {
        @Override
        public void process(NodeTraversal t, Node n, Node parent) {
          if (n.getType() == Token.BLOCK) {
            n.setParent(parent);
            parent.addChild(n);
          }
        }
      });
    }

    return root;
  }
}