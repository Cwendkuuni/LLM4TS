import static org.junit.Assert.*;
import static com.google.javascript.rhino.NodeFactory.createNode;
import static com.google.javascript.rhino.NodeFactory.createScope;
import static com.google.javascript.rhino.NodeFactory.createToken;
import static com.google.javascript.jscomp.NodeUtil.isFunction;
import static com.google.javascript.jscomp.NodeUtil.isName;
import static com.google.javascript.jscomp.NodeUtil.isAssign;
import static com.google.javascript.jscomp.NodeUtil.isExpressionNode;
import static com.google.javascript.jscomp.NodeUtil.getConditionExpression;
import static com.google.javascript.jscomp.NodeUtil.getOpFromAssignmentOp;
import static com.google.javascript.jscomp.NodeUtil.containsFunction;
import static com.google.javascript.jscomp.NodeUtil.isForIn;
import static com.google.javascript.jscomp.DeadAssignmentsElimination.matchRemovableAssigns;

import org.junit.Test;
import org.junit.Before;
import org.junit.After;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.Token;

public class DeadAssignmentsEliminationTest {

  private Compiler compiler;
  private Node rootNode;
  private NodeTraversal nodeTraversal;
  private Scope scope;

  @Before
  public void setUp() throws Exception {
    CompilerOptions options = new CompilerOptions();
    compiler = new Compiler(options);
    scope = createScope();
    rootNode = createNode("function foo() {}");
    nodeTraversal = new NodeTraversal(compiler, rootNode, null, null, null, null,
        null, null, new DeadAssignmentsElimination(compiler));
  }

  @After
  public void tearDown() throws Exception {
    compiler = null;
    rootNode = null;
    nodeTraversal = null;
    scope = null;
  }

  @Test
  public void testDeadAssignmentsElimination_NormalCase() {
    String input = "function foo() {"
        + "  var x = 1;"
        + "  x = 2;"
        + "  x = 3;"
        + "  return x;"
        + "}";

    nodeTraversal.process(null, NodeFactory.createFromSource(input));
    assertNull(nodeTraversal.getErrorMessages());
    assertNotNull(nodeTraversal.getOutput());
    Node output = nodeTraversal.getOutput();
    assertEquals(createToken(Token.FUNCTION, "foo", null), output.getFirstChild());
    Node funcBody = output.getLastChild();
    assertEquals(createToken(Token.RETURN, null, null), funcBody.getLastChild());
    Node xAssign1 = funcBody.getFirstChild().getNext();
    assertEquals(createToken(Token.NAME, "x", null), xAssign1.getFirstChild());
    assertEquals(createNode(Token.ASSIGN, null, null, xAssign1.getFirstChild(),
        NodeFactory.newInt(1)), xAssign1.getChildAfter(0));
    Node xAssign2 = xAssign1.getNext();
    assertEquals(createToken(Token.NAME, "x", null), xAssign2.getFirstChild());
    assertEquals(createNode(Token.ASSIGN, null, null, xAssign2.getFirstChild(),
        NodeFactory.newInt(2)), xAssign2.getChildAfter(0));
    Node xAssign3 = xAssign2.getNext();
    assertEquals(createToken(Token.NAME, "x", null), xAssign3.getFirstChild());
    assertEquals(createNode(Token.ASSIGN, null, null, xAssign3.getFirstChild(),
        NodeFactory.newInt(3)), xAssign3.getChildAfter(0));
    Node xReturn = xAssign3.getNext();
    assertEquals(createToken(Token.RETURN, null, null), xReturn);

    // Dead assignment elimination should have removed x = 2 and x = 3
    Node expectedOutput = createNode("function foo() { return 1; }");
    assertNodeEquals(expectedOutput, output);
  }

  @Test
  public void testDeadAssignmentsElimination_NoAssignments() {
    String input = "function foo() {}";

    nodeTraversal.process(null, NodeFactory.createFromSource(input));
    assertNull(nodeTraversal.getErrorMessages());
    assertNotNull(nodeTraversal.getOutput());

    // No assignments to eliminate
    Node output = nodeTraversal.getOutput();
    assertEquals(createToken(Token.FUNCTION, "foo", null), output.getFirstChild());
  }

  @Test
  public void testDeadAssignmentsElimination_NestedIf() {
    String input = "function foo() {"
        + "  var x = 1;"
        + "  if (x > 0) {"
        + "    if (x < 1) {"
        + "      x = 2;"
        + "    } else {"
        + "      x = 3;"
        + "    }"
        + "  }"
        + "  return x;"
        + "}";

    nodeTraversal.process(null, NodeFactory.createFromSource(input));
    assertNull(nodeTraversal.getErrorMessages());
    assertNotNull(nodeTraversal.getOutput());
    Node output = nodeTraversal.getOutput();

    // Dead assignment elimination should have removed x = 2
    Node expectedOutput = createNode("function foo() { return 1; }");
    assertNodeEquals(expectedOutput, output);
  }

  @Test
  public void testDeadAssignmentsElimination_Loop() {
    String input = "function foo() {"
        + "  var x = 1;"
        + "  for (var i = 0; i < 10; i++) {"
        + "    x = i;"
        + "  }"
        + "  return x;"
        + "}";

    nodeTraversal.process(null, NodeFactory.createFromSource(input));
    assertNull(nodeTraversal.getErrorMessages());
    assertNotNull(nodeTraversal.getOutput());
    Node output = nodeTraversal.getOutput();

    // Dead assignment elimination should have removed all x = i assignments
    Node expectedOutput = createNode("function foo() { return 10; }");
    assertNodeEquals(expectedOutput, output);
  }

  @Test
  public void testDeadAssignmentsElimination_FunctionCall() {
    String input = "function foo() {"
        + "  var x = 1;"
        + "  func();"
        + "  x = 2;"
        + "  return x;"
        + "}"
        + "function func() {}";

    nodeTraversal.process(null, NodeFactory.createFromSource(input));
    assertNull(nodeTraversal.getErrorMessages());
    assertNotNull(nodeTraversal.getOutput());
    Node output = nodeTraversal.getOutput();

    // Dead assignment elimination should have removed x = 2
    Node expectedOutput = createNode("function foo() { func(); return 1; }");
    assertNodeEquals(expectedOutput, output);
  }

  @Test
  public void testDeadAssignmentsElimination_GlobalVariable() {
    String input = "function foo() {"
        + "  var x = 1;"
        + "  window.x = 2;"
        + "  x = 3;"
        + "  return x;"
        + "}";

    nodeTraversal.process(null, NodeFactory.createFromSource(input));
    assertNull(nodeTraversal.getErrorMessages());
    assertNotNull(nodeTraversal.getOutput());
    Node output = nodeTraversal.getOutput();

    // Dead assignment elimination should not have removed x = 3
    Node expectedOutput = createNode("function foo() { window.x = 2; return 1; }");
    assertNodeEquals(expectedOutput, output);
  }

  @Test
  public void testDeadAssignmentsElimination_ForIn() {
    String input = "function foo() {"
        + "  var x = 1;"
        + "  for (var k in obj) {"
        + "    x = obj[k];"
        + "  }"
        + "  return x;"
        + "}"
        + "var obj = {};";

    nodeTraversal.process(null, NodeFactory.createFromSource(input));
    assertNull(nodeTraversal.getErrorMessages());
    assertNotNull(nodeTraversal.getOutput());
    Node output = nodeTraversal.getOutput();

    // Dead assignment elimination should not have removed any assignments
    Node expectedOutput = createNode("function foo() { for (var k in obj) {} return 1; }");
    assertNodeEquals(expectedOutput, output);
  }

  @Test(expected = AssertionError.class)
  public void testDeadAssignmentsElimination_Error() {
    String input = "function foo() {"
        + "  var x = 1;"
        + "  x = x;"
        + "  x = 2;"
        + "}";

    nodeTraversal.process(null, NodeFactory.createFromSource(input));
    assertNull(nodeTraversal.getErrorMessages());
    assertNotNull(nodeTraversal.getOutput());

    // Dead assignment elimination should not have removed any assignments
    Node output = nodeTraversal.getOutput();
    assertNotSame(createNode("function foo() { return 1; }"), output);
  }

  private void assertNodeEquals(Node expected, Node actual) {
    assertEquals("Unexpected output", expected, actual);
  }
}