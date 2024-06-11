import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeUtil.*;
import static com.google.javascript.jscomp.NodeTraversal.traverse;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.AbstractNode;
import com.google.javascript.jscomp.AbstractPostOrderCallback;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.DataFlowAnalysis;
import com.google.javascript.jscomp.DeadAssignmentsElimination;
import com.google.javascript.jscomp.FlowState;
import com.google.javascript.jscomp.Graph;
import com.google.javascript.jscomp.LiveVariablesAnalysis;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.ScopedCallback;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.JUnit4;

import java.util.Arrays;
import java.util.List;

@RunWith(JUnit4.class)
public class DeadAssignmentsEliminationTest {

  @Mock private AbstractCompiler compiler;
  private DeadAssignmentsElimination deadAssignmentsElimination;
  private ControlFlowGraph<Node> controlFlowGraph;
  private LiveVariablesAnalysis liveVariablesAnalysis;
  private Scope scope;

  @Before
  public void setUp() {
    controlFlowGraph = new ControlFlowGraph<Node>(new Graph<Node>());
    liveVariablesAnalysis = new LiveVariablesAnalysis(controlFlowGraph, scope, compiler);
    deadAssignmentsElimination = new DeadAssignmentsElimination(compiler);
  }

  @Test
  public void testEmpty() {
    Node root = new Node(Token.PROGRAM, null);
    traverse(compiler, root, deadAssignmentsElimination);
    assertEquals(0, controlFlowGraph.size());
  }

  @Test
  public void testSimpleAssignments() {
    String code = "var x = 1, y = 1;";
    Node root = parseAndTraverseToRoot(code);
    traverse(compiler, root, deadAssignmentsElimination);
    assertEquals(0, controlFlowGraph.size());
  }

  @Test
  public void testDeadAssignments() {
    String code = "var x = 1, y = 1; x = y;";
    Node root = parseAndTraverseToRoot(code);
    traverse(compiler, root, deadAssignmentsElimination);
    assertEquals(1, controlFlowGraph.size());

    List<Node> nodes = Iterables.asList(controlFlowGraph.getDirectedGraphNodes().iterator().next().getValue());
    Node node = nodes.get(1);
    assertNull(node.getFirstChild());
    assertNull(node.getNext());
  }

  @Test
  public void testIfStatement() {
    String code = "var x = 1; if (x) { x = 2; }";
    Node root = parseAndTraverseToRoot(code);
    traverse(compiler, root, deadAssignmentsElimination);
    assertEquals(3, controlFlowGraph.size());

    List<Node> nodes = Iterables.asList(controlFlowGraph.getDirectedGraphNodes().iterator().next().getValue());
    Node cond = nodes.get(1);
    assertEquals(1, cond.getChildCount());
    assertEquals(Token.IF, cond.getType());

    nodes = Iterables.asList(controlFlowGraph.getDirectedGraphNodes().iterator().next().getValue().getChild(1).getValue().getChildren());
    Node assign = nodes.get(0);
    assertEquals(1, assign.getChildCount());
    assertEquals(Token.ASSIGN, assign.getType());
    assertEquals("x", assign.getFirstChild().getString());
    assertEquals(2, assign.getLastChild().getInt());
  }

  @Test
  public void testWhileStatement() {
    String code = "var x = 1; while (x) { x = 2; }";
    Node root = parseAndTraverseToRoot(code);
    traverse(compiler, root, deadAssignmentsElimination);
    assertEquals(4, controlFlowGraph.size());

    List<Node> nodes = Iterables.asList(controlFlowGraph.getDirectedGraphNodes().iterator().next().getValue());
    Node whileStmt = nodes.get(1);
    assertEquals(2, whileStmt.getChildCount());
    assertEquals(Token.WHILE, whileStmt.getType());

    nodes = Iterables.asList(whileStmt.getChild(1).getValue().getChildren());
    Node assign = nodes.get(0);
    assertEquals(1, assign.getChildCount());
    assertEquals(Token.ASSIGN, assign.getType());
    assertEquals("x", assign.getFirstChild().getString());
    assertEquals(2, assign.getLastChild().getInt());
  }

  @Test
  public void testForLoop() {
    String code = "var x = 1; for (var i = 0; i < 1; i++) { x = i; }";
    Node root = parseAndTraverseToRoot(code);
    traverse(compiler, root, deadAssignmentsElimination);
    assertEquals(5, controlFlowGraph.size());

    List<Node> nodes = Iterables.asList(controlFlowGraph.getDirectedGraphNodes().iterator().next().getValue());
    Node forStmt = nodes.get(1);
    assertEquals(3, forStmt.getChildCount());
    assertEquals(Token.FOR, forStmt.getType());

    nodes = Iterables.asList(forStmt.getChild(1).getValue().getChildren());
    Node assign = nodes.get(0);
    assertEquals(1, assign.getChildCount());
    assertEquals(Token.ASSIGN, assign.getType());
    assertEquals("i", assign.getFirstChild().getString());
    assertEquals(0, assign.getLastChild().getInt());
  }

  @Test
  public void testSwitchStatement() {
    String code = "var x = 1; switch (x) { case 1: x = 2; }";
    Node root = parseAndTraverseToRoot(code);
    traverse(compiler, root, deadAssignmentsElimination);
    assertEquals(5, controlFlowGraph.size());

    List<Node> nodes = Iterables.asList(controlFlowGraph.getDirectedGraphNodes().iterator().next().getValue());
    Node switchStmt = nodes.get(1);
    assertEquals(2, switchStmt.getChildCount());
    assertEquals(Token.SWITCH, switchStmt.getType());

    nodes = Iterables.asList(switchStmt.getChild(1).getValue().getChildren());
    Node caseStmt = nodes.get(0);
    assertEquals(1, caseStmt.getChildCount());
    assertEquals(Token.CASE, caseStmt.getType());
    assertEquals(1, caseStmt.getLastChild().getInt());

    nodes = Iterables.asList(caseStmt.getChild(1).getValue().getChildren());
    Node assign = nodes.get(0);
    assertEquals(1, assign.getChildCount());
    assertEquals(Token.ASSIGN, assign.getType());
    assertEquals("x", assign.getFirstChild().getString());
    assertEquals(2, assign.getLastChild().getInt());
  }

  @Test
  public void testReturnStatement() {
    String code = "var x = 1; return x = 2;";
    Node root = parseAndTraverseToRoot(code);
    traverse(compiler, root, deadAssignmentsElimination);
    assertEquals(1, controlFlowGraph.size());

    List<Node> nodes = Iterables.asList(controlFlowGraph.getDirectedGraphNodes().iterator().next().getValue());
    Node returnStmt = nodes.get(1);
    assertEquals(1, returnStmt.getChildCount());
    assertEquals(Token.RETURN, returnStmt.getType());

    nodes = Iterables.asList(returnStmt.getChild(1).getValue().getChildren());
    Node assign = nodes.get(0);
    assertEquals(1, assign.getChildCount());
    assertEquals(Token.ASSIGN, assign.getType());
    assertEquals("x", assign.getFirstChild().getString());
    assertEquals(2, assign.getLastChild().getInt());
  }

  @Test
  public void testFunctionDeclaration() {
    String code = "function foo() { var x = 1; x = 2; }";
    Node root = parseAndTraverseToRoot(code);
    traverse(compiler, root, deadAssignmentsElimination);
    assertEquals(4, controlFlowGraph.size());

    List<Node> nodes = Iterables.asList(controlFlowGraph.getDirectedGraphNodes().iterator().next().getValue());
    Node functionDecl = nodes.get(1);
    assertEquals(1, functionDecl.getChildCount());
    assertEquals(Token.FUNCTION, functionDecl.getType());

    nodes = Iterables.asList(functionDecl.getChild(1).getValue().getChildren());
    Node body = nodes.get(0);
    assertEquals(1, body.getChildCount());
    assertEquals(Token.BLOCK, body.getType());

    nodes = Iterables.asList(body.getChild(1).getValue().getChildren());
    Node assign = nodes.get(0);
    assertEquals(1, assign.getChildCount());
    assertEquals(Token.ASSIGN, assign.getType());
    assertEquals("x", assign.getFirstChild().getString());
    assertEquals(2, assign.getLastChild().getInt());
  }

  @Test
  public void testForInStatement() {
    String code = "for (var i in [1]) { i = 2; }";
    Node root = parseAndTraverseToRoot(code);
    traverse(compiler, root, deadAssignmentsElimination);
    assertEquals(4, controlFlowGraph.size());

    List<Node> nodes = Iterables.asList(controlFlowGraph.getDirectedGraphNodes().iterator().next().getValue());
    Node forStmt = nodes.get(1);
    assertEquals(3, forStmt.getChildCount());
    assertEquals(Token.FOR, forStmt.getType());

    nodes = Iterables.asList(forStmt.getChild(1).getValue().getChildren());
    Node assign = nodes.get(0);
    assertEquals(1, assign.getChildCount());
    assertEquals(Token.ASSIGN, assign.getType());
    assertEquals("i", assign.getFirstChild().getString());
    assertEquals(2, assign.getLastChild().getInt());
  }

  @Test
  public void testNestedStatements() {
    String code = "if (true) { var x = 1; if (true) { x = 2; } }";
    Node root = parseAndTraverseToRoot(code);
    traverse(compiler, root, deadAssignmentsElimination);
    assertEquals(7, controlFlowGraph.size());

    List<Node> nodes = Iterables.asList(controlFlowGraph.getDirectedGraphNodes().iterator().next().getValue());
    Node ifStmt = nodes.get(1);
    assertEquals(3, ifStmt.getChildCount());
    assertEquals(Token.IF, ifStmt.getType());

    nodes = Iterables.asList(ifStmt.getChild(1).getValue().getChildren());
    Node ifInnerIfStmt = nodes.get(1);
    assertEquals(3, ifInnerIfStmt.getChildCount());
    assertEquals(Token.IF, ifInnerIfStmt.getType());

    nodes = Iterables.asList(ifInnerIfStmt.getChild(1).getValue().getChildren());
    Node assign = nodes.get(0);
    assertEquals(1, assign.getChildCount());
    assertEquals(Token.ASSIGN, assign.getType());
    assertEquals("x", assign.getFirstChild().getString());
    assertEquals(2, assign.getLastChild().getInt());
  }

  @Test
  public void testSeveralDeadAssignments() {
    String code = "var x = 1, y = 1, z = 1; x = y; y = z; z = 0;";
    Node root = parseAndTraverseToRoot(code);
    traverse(compiler, root, deadAssignmentsElimination);
    assertEquals(3, controlFlowGraph.size());

    List<Node> nodes = Iterables.asList(controlFlowGraph.getDirectedGraphNodes().iterator().next().getValue());
    Node assignX = nodes.get(1);
    assertEquals(1, assignX.getChildCount());
    assertEquals(Token.ASSIGN, assignX.getType());
    assertEquals("y", assignX.getFirstChild().getString());

    nodes = Iterables.asList(assignX.getParent().getValue().getChildren());
    Node assignY = nodes.get(1);
    assertEquals(1, assignY.getChildCount());
    assertEquals(Token.ASSIGN, assignY.getType());
    assertEquals("z", assignY.getFirstChild().getString());

    nodes = Iterables.asList(assignY.getParent().getValue().getChildren());
    Node assignZ = nodes.get(1);
    assertEquals(0, assignZ.getChildCount());
    assertEquals(Token.ASSIGN, assignZ.getType());
    assertEquals(0, assignZ.getInt());
  }

  private Node parseAndTraverseToRoot(String code) {
    AbstractNode ast = Parser.parse(code, compiler);
    assertNotNull(ast);
    traverse(compiler, ast, new NodeTraversal.RootFinder());
    Node root = ((RootFinder) compiler.getPassManager().getRootFinder()).getRootNode();
    return root;
  }
}