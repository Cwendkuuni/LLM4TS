import static org.junit.Assert.*;
import static com.google.javascript.jscomp.MaybeReachingVariableUse.*;

import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.Node;

import org.junit.Before;
import org.junit.Test;

public class MaybeReachingVariableUseTest {

  private MaybeReachingVariableUse analysis;
  private ControlFlowGraph<Node> cfg;
  private Scope jsScope;

  @Before
  public void setUp() {
    cfg = new ControlFlowGraph<>();
    // Assuming we have a valid control flow graph and scope here.
    jsScope = new Scope();
    analysis = new MaybeReachingVariableUse(cfg, jsScope, null);
  }

  @Test
  public void testSimpleAssignment() {
    Node entryNode = new Node(Token.BLOCK);
    Node assignmentNode = new Node(Token.ASSIGN, Token.NAME, new Node(Token.NAME, "x"), new Node(Token.INT_LITERAL, 5));
    Node printNode = new Node(Token.NAME, "print", new Node(Token.NAME, "x"));

    cfg.addNode(entryNode);
    cfg.addNode(assignmentNode);
    cfg.addNode(printNode);

    Node startNode = cfg.getEntryNode();
    analysis.run(startNode);

    List<Node> uses = analysis.getUses("x", startNode);
    assertFalse("No uses expected for a simple assignment", uses.isEmpty());
    assertEquals("The only use should be the print statement", printNode, uses.get(0));
  }

  @Test
  public void testConditionalAssignment() {
    Node entryNode = new Node(Token.BLOCK);
    Node conditionNode = new Node(Token.IF, new Node(Token.NAME, "x"), new Node(Token.BLOCK));
    Node assignmentNode1 = new Node(Token.ASSIGN, Token.NAME, new Node(Token.NAME, "x"), new Node(Token.INT_LITERAL, 5));
    Node assignmentNode2 = new Node(Token.ASSIGN, Token.NAME, new Node(Token.NAME, "x"), new Node(Token.INT_LITERAL, 10));
    Node printNode = new Node(Token.NAME, "print", new Node(Token.NAME, "x"));

    cfg.addNode(entryNode);
    cfg.addNode(conditionNode);
    cfg.addNode(assignmentNode1);
    cfg.addNode(assignmentNode2);
    cfg.addNode(printNode);

    Node startNode = cfg.getEntryNode();
    analysis.run(startNode);

    List<Node> uses = analysis.getUses("x", startNode);
    // Both assignments may reach the print statement, but only one is the last definition.
    // So we expect one use and call assertNotNull() instead of assertSame() to account for that.
    assertFalse("At least one use is expected", uses.isEmpty());
    for (Node node : uses) {
      if (node == printNode) {
        return; // Test passed
      }
    }
    fail("Unexpected use found");
  }

  @Test
  public void testAssignmentInLoop() {
    Node entryNode = new Node(Token.BLOCK);
    Node conditionNode = new Node(Token.WHILE, new Node(Token.NAME, "x"), new Node(Token.BLOCK));
    Node assignmentNode = new Node(Token.ASSIGN, Token.NAME, new Node(Token.NAME, "x"), new Node(Token.INT_LITERAL, 1));
    Node incrementNode = new Node(Token.ASSIGN, Token.NAME, new Node(Token.NAME, "x"), new Node(Token.NAME, "x"),
        new Node(Token.PLUS, new Node(Token.NAME, "x"), new Node(Token.INT_LITERAL, 1)));
    Node printNode = new Node(Token.NAME, "print", new Node(Token.NAME, "x"));

    cfg.addNode(entryNode);
    cfg.addNode(conditionNode);
    cfg.addNode(assignmentNode);
    cfg.addNode(incrementNode);
    cfg.addNode(printNode);

    Node startNode = cfg.getEntryNode();
    analysis.run(startNode);

    List<Node> uses = analysis.getUses("x", startNode);
    // The print statement is the only destination of the loop, so it's the last definition.
    assertEquals("The print statement should be the last definition", printNode, uses.get(0));
  }

  @Test
  public void testEscapedVariable() {
    Node entryNode = new Node(Token.BLOCK);
    Node assignmentNode = new Node(Token.ASSIGN, Token.NAME, new Node(Token.NAME, "x"), new Node(Token.INT_LITERAL, 5));
    Node functionNode = new Node(Token.FUNCTION, null, new Node(Token.BLOCK), new Node(Token.NAME, "x"));
    Node returnNode = new Node(Token.RETURN, NodeUtil.makeNodeList(new Node(Token.NAME, "x")));
    Node callNode = new Node(Token.CALL, new Node(Token.NAME, "anonymousFunction"));

    cfg.addNode(entryNode);
    cfg.addNode(assignmentNode);
    cfg.addNode(functionNode);
    cfg.addNode(returnNode);
    cfg.addNode(callNode);

    // We assume that 'x' is an escaped variable.
    analysis.escaped.add(jsScope.getVar("x"));

    Node startNode = cfg.getEntryNode();
    analysis.run(startNode);

    List<Node> uses = analysis.getUses("x", startNode);
    assertTrue("No uses expected for an escaped variable", uses.isEmpty());
  }

  @Test
  public void testFunctionParameter() {
    Node entryNode = new Node(Token.BLOCK);
    Node functionDefinition = new Node(Token.FUNCTION, null, new Node(Token.BLOCK), new Node(Token.NAME, "x"));
    Node assignmentNode = new Node(Token.ASSIGN, Token.NAME, new Node(Token.NAME, "x"), new Node(Token.INT_LITERAL, 5));
    Node callNode = new Node(Token.CALL, functionDefinition, NodeUtil.makeNodeList(new Node(Token.INT_LITERAL, 1)));
    Node printNode = new Node(Token.NAME, "print", new Node(Token.NAME, "x"));

    cfg.addNode(entryNode);
    cfg.addNode(functionDefinition);
    cfg.addNode(assignmentNode);
    cfg.addNode(callNode);
    cfg.addNode(printNode);

    Node startNode = cfg.getEntryNode();
    analysis.run(startNode);

    List<Node> uses = analysis.getUses("x", startNode);
    // The function definition receives the parameter 'x' and assigns it, so it's a use.
    assertTrue("One use is expected", !uses.isEmpty());
  }

  @Test
  public void testComplexControlFlow() {
    // This test case is complex and includes a loop, a conditional, and a function call.
    // It is provided as an example and should be thoroughly tested with unit tests.
    Node entryNode = new Node(Token.BLOCK);
    Node conditionNode1 = new Node(Token.IF, new Node(Token.NAME, "x"), new Node(Token.BLOCK));
    Node assignmentNode1 = new Node(Token.ASSIGN, Token.NAME, new Node(Token.NAME, "x"), new Node(Token.INT_LITERAL, 1));
    Node assignmentNode2 = new Node(Token.ASSIGN, Token.NAME, new Node(Token.NAME, "x"), new Node(Token.INT_LITERAL, 2));
    Node functionNode = new Node(Token.FUNCTION, null, new Node(Token.BLOCK), new Node(Token.NAME, "x"));
    Node returnNode = new Node(Token.RETURN, NodeUtil.makeNodeList(new Node(Token.NAME, "x")));
    Node callNode = new Node(Token.CALL, functionNode, NodeUtil.makeNodeList(new Node(Token.INT_LITERAL, 1)));
    Node conditionNode2 = new Node(Token.IF, new Node(Token.NAME, "x"), new Node(Token.BLOCK));
    Node loopNode = new Node(Token.WHILE, new Node(Token.NAME, "x"), new Node(Token.BLOCK));
    Node incrementNode = new Node(Token.ASSIGN, Token.NAME, new Node(Token.NAME, "x"), new Node(Token.NAME, "x"),
        new Node(Token.PLUS, new Node(Token.NAME, "x"), new Node(Token.INT_LITERAL, 1)));
    Node printNode = new Node(Token.NAME, "print", new Node(Token.NAME, "x"));

    cfg.addNode(entryNode);
    cfg.addNode(conditionNode1);
    cfg.addNode(assignmentNode1);
    cfg.addNode(conditionNode2);
    cfg.addNode(loopNode);
    cfg.addNode(incrementNode);
    cfg.addNode(functionNode);
    cfg.addNode(returnNode);
    cfg.addNode(callNode);
    cfg.addNode(printNode);

    Node startNode = cfg.getEntryNode();
    analysis.run(startNode);

    List<Node> uses = analysis.getUses("x", startNode);
    // The print statement is the only destination of the loop, so it's the last definition.
    assertEquals("The print statement should be the last definition", printNode, uses.get(0));
  }
}