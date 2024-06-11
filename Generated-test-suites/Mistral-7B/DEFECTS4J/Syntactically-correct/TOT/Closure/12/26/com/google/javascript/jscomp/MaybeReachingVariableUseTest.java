import static org.junit.Assert.*;
import static com.google.javascript.jscomp.TestUtils.*;
import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.graph.GraphNode;
import com.google.javascript.rhino.Node;

import org.junit.Before;
import org.junit.Test;

public class MaybeReachingVariableUseTest {

  private MaybeReachingVariableUse analysis;
  private ControlFlowGraph<Node> cfg;
  private Scope scope;

  @Before
  public void setUp() {
    cfg = new ControlFlowGraph<Node>(node1, node2, node3, node4, node5, node6);
    scope = new Scope();
    analysis = new MaybeReachingVariableUse(cfg, scope, null);
  }

  @Test
  public void testSimplePath() {
    Node defNode = node3;
    Node useNode = node6;
    Var variable = scope.getVar("x");
    analysis.compute();
    assertEquals(Sets.newHashSet(useNode), analysis.getUses(variable.getName(), defNode));
  }

  @Test
  public void testNoPath() {
    Node defNode = node1;
    Node useNode = node6;
    Var variable = scope.getVar("x");
    analysis.compute();
    assertEmpty(analysis.getUses(variable.getName(), defNode));
  }

  @Test
  public void testMultipleDefinitions() {
    Node defNode1 = node2;
    Node defNode2 = node4;
    Node useNode = node6;
    Var variable = scope.getVar("x");
    analysis.compute();
    Set<Node> uses = analysis.getUses(variable.getName(), defNode1);
    assertFalse(uses.contains(defNode2));
    assertEquals(Sets.newHashSet(useNode), uses);

    uses = analysis.getUses(variable.getName(), defNode2);
    assertFalse(uses.contains(defNode1));
    assertFalse(uses.contains(useNode));
    assertEmpty(uses);
  }

  @Test
  public void testEscapedVariable() {
    Node defNode = node2;
    Node useNode = node6;
    Var variable = scope.getVar("global");
    analysis.compute();
    assertEmpty(analysis.getUses(variable.getName(), defNode));
  }

  @Test
  public void testIfStatement() {
    Node ifNode = node5;
    Node defNode = node4;
    Node useNode1 = node6;
    Node useNode2 = node7;
    Var variable = scope.getVar("x");
    analysis.compute();
    Set<Node> uses = analysis.getUses(variable.getName(), defNode);
    assertFalse(uses.contains(ifNode));
    assertEquals(Sets.newHashSet(useNode1, useNode2), uses);
  }

  @Test
  public void testWhileStatement() {
    Node whileNode = node3;
    Node defNode = node2;
    Node useNode = node6;
    Var variable = scope.getVar("x");
    analysis.compute();
    Set<Node> uses = analysis.getUses(variable.getName(), defNode);
    assertFalse(uses.contains(whileNode));
    assertEquals(Sets.newHashSet(useNode), uses);
  }

  @Test
  public void testForLoop() {
    Node forNode = node1;
    Node defNode = node2;
    Node useNode = node6;
    Var variable = scope.getVar("x");
    analysis.compute();
    Set<Node> uses = analysis.getUses(variable.getName(), defNode);
    assertFalse(uses.contains(forNode));
    assertEquals(Sets.newHashSet(useNode), uses);
  }

  @Test
  public void testFunctionCall() {
    Node callNode = node4;
    Node defNode = node2;
    Node useNode = node6;
    Var variable = scope.getVar("x");
    analysis.compute();
    Set<Node> uses = analysis.getUses(variable.getName(), defNode);
    assertFalse(uses.contains(callNode));
    assertEquals(Sets.newHashSet(useNode), uses);
  }

  @Test
  public void testFunctionDefinition() {
    Node functionNode = node1;
    Node defNode = node2;
    Node useNode = node6;
    Var variable = scope.getVar("x");
    analysis.compute();
    Set<Node> uses = analysis.getUses(variable.getName(), defNode);
    assertFalse(uses.contains(functionNode));
    assertEquals(Sets.newHashSet(useNode), uses);
  }

  @Test
  public void testVariableRenaming() {
    Node defNode = node2;
    Node useNode = node6;
    Var variable = scope.getVar("x");
    analysis.compute();
    Set<Node> uses = analysis.getUses(variable.getName(), defNode);
    assertFalse(uses.contains(useNode));

    variable = scope.getVar("y");
    uses = analysis.getUses(variable.getName(), defNode);
    assertEquals(Sets.newHashSet(useNode), uses);
  }
}