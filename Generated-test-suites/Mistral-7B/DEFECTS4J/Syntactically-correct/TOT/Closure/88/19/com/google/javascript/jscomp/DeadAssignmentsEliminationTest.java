import static com.google.javascript.jscomp.NodeUtil.*;
import static org.junit.Assert.*;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeTraversal.NodeTraversalType;
import com.google.javascript.rhino.Node;

import org.junit.Before;
import org.junit.Test;

public class DeadAssignmentsEliminationTest {

  private AbstractCompiler compiler;

  @Before
  public void setUp() {
    compiler = new AbstractCompiler();
  }

  @Test
  public void testSimpleAssignment() {
    String sourceCode = "function foo() {" +
        "  var x = 1;" +
        "  x = 2;" +
        "  return x;" +
        "}";
    Node root = compiler.parseAndCompile(sourceCode, null, null).getRootNode();

    DeadAssignmentsElimination deadAssignmentsElimination =
        new DeadAssignmentsElimination(compiler);
    NodeTraversal nodeTraversal = new NodeTraversal(root, NodeTraversalType.POST_ORDER);

    deadAssignmentsElimination.process(null, root);
    nodeTraversal.traverse();

    Node x = findNode(root, "x");
    assertNotNull(x);
    assertEquals(new Node(Token.NUMBER, 2), x);
  }

  @Test
  public void testAssignmentWithCondition() {
    String sourceCode = "function foo() {" +
        "  var x = 1;" +
        "  if (x < 2) {" +
        "    x = 2;" +
        "  }" +
        "  return x;" +
        "}";
    Node root = compiler.parseAndCompile(sourceCode, null, null).getRootNode();

    DeadAssignmentsElimination deadAssignmentsElimination =
        new DeadAssignmentsElimination(compiler);
    NodeTraversal nodeTraversal = new NodeTraversal(root, NodeTraversalType.POST_ORDER);

    deadAssignmentsElimination.process(null, root);
    nodeTraversal.traverse();

    Node x = findNode(root, "x");
    assertNotNull(x);
    assertEquals(new Node(Token.NUMBER, 1), x);
  }

  @Test
  public void testAssignmentWithReturnStatement() {
    String sourceCode = "function foo() {" +
        "  var x = 1;" +
        "  return x = 2;" +
        "}";
    Node root = compiler.parseAndCompile(sourceCode, null, null).getRootNode();

    DeadAssignmentsElimination deadAssignmentsElimination =
        new DeadAssignmentsElimination(compiler);
    NodeTraversal nodeTraversal = new NodeTraversal(root, NodeTraversalType.POST_ORDER);

    deadAssignmentsElimination.process(null, root);
    nodeTraversal.traverse();

    Node x = findNode(root, "x");
    assertNotNull(x);
    assertEquals(new Node(Token.NUMBER, 1), x);
  }

  @Test
  public void testAssignmentInsideLoop() {
    String sourceCode = "function foo() {" +
        "  for (var i = 0; i < 10; i++) {" +
        "    i = i + 1;" +
        "  }" +
        "  return i;" +
        "}";
    Node root = compiler.parseAndCompile(sourceCode, null, null).getRootNode();

    DeadAssignmentsElimination deadAssignmentsElimination =
        new DeadAssignmentsElimination(compiler);
    NodeTraversal nodeTraversal = new NodeTraversal(root, NodeTraversalType.POST_ORDER);

    deadAssignmentsElimination.process(null, root);
    nodeTraversal.traverse();

    Node i = findNode(root, "i");
    assertNotNull(i);
    assertEquals(new Node(Token.NUMBER, 10), i);
  }

  @Test
  public void testAssignmentToDeadVariable() {
    String sourceCode = "function foo() {" +
        "  var x = 1;" +
        "  var deadX = x;" +
        "  x = 2;" +
        "  return x;" +
        "}";
    Node root = compiler.parseAndCompile(sourceCode, null, null).getRootNode();

    DeadAssignmentsElimination deadAssignmentsElimination =
        new DeadAssignmentsElimination(compiler);
    NodeTraversal nodeTraversal = new NodeTraversal(root, NodeTraversalType.POST_ORDER);

    deadAssignmentsElimination.process(null, root);
    nodeTraversal.traverse();

    Node x = findNode(root, "x");
    assertNotNull(x);
    assertEquals(new Node(Token.NUMBER, 2), x);

    Node deadX = findNode(root, "deadX");
    assertNotNull(deadX);
    assertEquals(new Node(Token.NUMBER, 1), deadX);
  }

  @Test
  public void testAssignmentToGlobalVariable() {
    String sourceCode = "function foo() {" +
        "  var x = 1;" +
        "  globalX = x;" +
        "  x = 2;" +
        "  return x;" +
        "}";
    Node root = compiler.parseAndCompile(sourceCode, null, null).getRootNode();

    DeadAssignmentsElimination deadAssignmentsElimination =
        new DeadAssignmentsElimination(compiler);
    NodeTraversal nodeTraversal = new NodeTraversal(root, NodeTraversalType.POST_ORDER);

    // Global variables are not handled in DeadAssignmentElimination
    deadAssignmentsElimination.process(null, root);
    nodeTraversal.traverse();

    Node x = findNode(root, "x");
    assertNotNull(x);
    assertEquals(new Node(Token.NUMBER, 2), x);

    Node globalX = findNode(root, "globalX");
    assertNotNull(globalX);
    assertEquals(new Node(Token.NAME, "x"), globalX);
  }

  @Test
  public void testEmptyFunction() {
    String sourceCode = "function foo() {}";
    Node root = compiler.parseAndCompile(sourceCode, null, null).getRootNode();

    DeadAssignmentsElimination deadAssignmentsElimination =
        new DeadAssignmentsElimination(compiler);
    NodeTraversal nodeTraversal = new NodeTraversal(root, NodeTraversalType.POST_ORDER);

    deadAssignmentsElimination.process(null, root);
    nodeTraversal.traverse();

    assertTrue(root.getChildCount() == 0);
  }

  @Test
  public void testAssignmentWithFunctionExpression() {
    String sourceCode = "function foo() {" +
        "  var x = 1;" +
        "  return (function(x) {" +
        "    x = 2;" +
        "  })(x)" +
        "}";
    Node root = compiler.parseAndCompile(sourceCode, null, null).getRootNode();

    DeadAssignmentsElimination deadAssignmentsElimination =
        new DeadAssignmentsElimination(compiler);
    NodeTraversal nodeTraversal = new NodeTraversal(root, NodeTraversalType.POST_ORDER);

    deadAssignmentsElimination.process(null, root);
    nodeTraversal.traverse();

    Node x = findNode(root, "x");
    assertNotNull(x);
    assertEquals(new Node(Token.NUMBER, 1), x);

    Node functionExpression = findNode(root, "(function(x) {x = 2;})");
    assertNotNull(functionExpression);

    // Function expressions should not be removed because they have their own
    // scopes
    Node functionBody = functionExpression.getFirstChild();
    assertEquals(new Node(Token.VAR, null, null, null, null, null, null), functionBody);
    assertNotNull(functionBody.getFirstChild());
  }

  @Test
  public void testAssignmentWithComplexCondition() {
    String sourceCode = "function foo() {" +
        "  var x = 1;" +
        "  if (x % 2 == 0 && x % 3 == 0) {" +
        "    x = 2;" +
        "  }" +
        "  return x;" +
        "}";
    Node root = compiler.parseAndCompile(sourceCode, null, null).getRootNode();

    DeadAssignmentsElimination deadAssignmentsElimination =
        new DeadAssignmentsElimination(compiler);
    NodeTraversal nodeTraversal = new NodeTraversal(root, NodeTraversalType.POST_ORDER);

    deadAssignmentsElimination.process(null, root);
    nodeTraversal.traverse();

    Node x = findNode(root, "x");
    assertNotNull(x);
    assertEquals(new Node(Token.NUMBER, 1), x);
  }

  @Test
  public void testAssignmentWithComplexConditionWithAssignment() {
    String sourceCode = "function foo() {" +
        "  var x = 1;" +
        "  if (x % 2 == 0 && (x = x + 1) % 3 == 0) {" +
        "    x = 2;" +
        "  }" +
        "  return x;" +
        "}";
    Node root = compiler.parseAndCompile(sourceCode, null, null).getRootNode();

    DeadAssignmentsElimination deadAssignmentsElimination =
        new DeadAssignmentsElimination(compiler);
    NodeTraversal nodeTraversal = new NodeTraversal(root, NodeTraversalType.POST_ORDER);

    deadAssignmentsElimination.process(null, root);
    nodeTraversal.traverse();

    Node x = findNode(root, "x");
    assertNotNull(x);
    assertEquals(new Node(Token.NUMBER, 2), x);
  }

  @Test
  public void testAssignmentWithBlock() {
    String sourceCode = "function foo() {" +
        "  var x = 1;" +
        "  { x = 2; }" +
        "  return x;" +
        "}";
    Node root = compiler.parseAndCompile(sourceCode, null, null).getRootNode();

    DeadAssignmentsElimination deadAssignmentsElimination =
        new DeadAssignmentsElimination(compiler);
    NodeTraversal nodeTraversal = new NodeTraversal(root, NodeTraversalType.POST_ORDER);

    deadAssignmentsElimination.process(null, root);
    nodeTraversal.traverse();

    Node x = findNode(root, "x");
    assertNotNull(x);
    assertEquals(new Node(Token.NUMBER, 2), x);
  }

  @Test
  public void testDeadAssignmentWithReturnStatementInsideLoop() {
    String sourceCode = "function foo() {" +
        "  for (var i = 0; i < 10; i++) {" +
        "    if (i % 2 == 0) {" +
        "      return i;" +
        "    }" +
        "    i = i + 1;" +
        "  }" +
        "}";
    Node root = compiler.parseAndCompile(sourceCode, null, null).getRootNode();

    DeadAssignmentsElimination deadAssignmentsElimination =
        new DeadAssignmentsElimination(compiler);
    NodeTraversal nodeTraversal = new NodeTraversal(root, NodeTraversalType.POST_ORDER);

    deadAssignmentsElimination.process(null, root);
    nodeTraversal.traverse();

    // The loop should be terminated before the dead assignment.
    assertTrue(root.getChildCount() == 0);
  }

  @Test
  public void testDeadAssignmentInsideCondition() {
    String sourceCode = "function foo() {" +
        "  var x = 1;" +
        "  if (x = 1) {" +
        "    return true;" +
        "  } else {" +
        "    return false;" +
        "  }" +
        "}";
    Node root = compiler.parseAndCompile(sourceCode, null, null).getRootNode();

    DeadAssignmentsElimination deadAssignmentsElimination =
        new DeadAssignmentsElimination(compiler);
    NodeTraversal nodeTraversal = new NodeTraversal(root, NodeTraversalType.POST_ORDER);

    deadAssignmentsElimination.process(null, root);
    nodeTraversal.traverse();

    // The dead assignment should be removed.
    assertTrue(root.getChildCount() == 1);
    assertEquals(new Node(Token.IF, null, null, null, null, null, null), root);
  }

  @Test
  public void testDeadAssignmentInsideTernaryOperator() {
    String sourceCode = "function foo() {" +
        "  var x = 1;" +
        "  x = x > 0 ? 2 : 3;" +
        "  return x;" +
        "}";
    Node root = compiler.parseAndCompile(sourceCode, null, null).getRootNode();

    DeadAssignmentsElimination deadAssignmentsElimination =
        new DeadAssignmentsElimination(compiler);
    NodeTraversal nodeTraversal = new NodeTraversal(root, NodeTraversalType.POST_ORDER);

    deadAssignmentsElimination.process(null, root);
    nodeTraversal.traverse();

    // The dead assignment should be removed.
    assertTrue(root.getChildCount() == 1);
    assertEquals(new Node(Token.IF, null, null, null, null, null, null), root);
  }

  @Test
  public void testDeadAssignmentInsideWhileLoop() {
    String sourceCode = "function foo() {" +
        "  var x = 1;" +
        "  while (x = 1) {" +
        "    x = x + 1;" +
        "  }" +
        "  return x;" +
        "}";
    Node root = compiler.parseAndCompile(sourceCode, null, null).getRootNode();

    DeadAssignmentsElimination deadAssignmentsElimination =
        new DeadAssignmentsElimination(compiler);
    NodeTraversal nodeTraversal = new NodeTraversal(root, NodeTraversalType.POST_ORDER);

    deadAssignmentsElimination.process(null, root);
    nodeTraversal.traverse();

    // The loop should be terminated before the dead assignment.
    assertTrue(root.getChildCount() == 0);
  }

  @Test
  public void testDeadAssignmentInsideDoWhileLoop() {
    String sourceCode = "function foo() {" +
        "  var x = 1;" +
        "  do {" +
        "    x = 1;" +
        "  } while (false);" +
        "  return x;" +
        "}";
    Node root = compiler.parseAndCompile(sourceCode, null, null).getRootNode();

    DeadAssignmentsElimination deadAssignmentsElimination =
        new DeadAssignmentsElimination(compiler);
    NodeTraversal nodeTraversal = new NodeTraversal(root, NodeTraversalType.POST_ORDER);

    deadAssignmentsElimination.process(null, root);
    nodeTraversal.traverse();

    // The dead assignment should be removed.
    assertTrue(root.getChildCount() == 1);
    assertEquals(new Node(Token.DO, null, null, null, null, null, null), root);
  }

  private Node findNode(Node root, String name) {
    for (Node child = root.getFirstChild(); child != null; child = child.getNext()) {
      if (NodeUtil.isName(child) && child.getString().equals(name)) {
        return child;
      }
      Node foundNode = findNode(child, name);
      if (foundNode != null) {
        return foundNode;
      }
    }
    return null;
  }
}