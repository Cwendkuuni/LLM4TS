import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.javascript.jscomp.ControlFlowGraph.Branch;
import com.google.javascript.jscomp.DataFlowAnalysis;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.MaybeReachingVariableUse;
import com.google.javascript.rhino.Node;

import java.util.Collection;
import java.util.List;

public class MaybeReachingVariableUseTest {

  @org.junit.Test
  public void testEmpty() {
    Scope scope = new Scope();
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
    MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(cfg, scope, null);

    Node entry = Node.newBlock(Token.BLOCK);
    Node exit = Node.newBlock(Token.BLOCK);

    Branch branch = new Branch(entry, exit);
    cfg.addEdge(entry, exit, branch);

    ReachingUses result = analysis.computeFixedPoint();
    assertNotNull(result);
    assertNull(result.mayUseMap.keys().iterator().next());
  }

  @org.junit.Test
  public void testVariableAssignment() {
    Scope scope = new Scope();
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();

    Node entry = Node.newBlock(Token.BLOCK);
    Node assign = Node.newName("a").set("=");
    Node value = Node.newNumber(1.0);
    Node use = Node.newName("a");

    Branch branch1 = new Branch(entry, assign);
    Branch branch2 = new Branch(assign, use);
    Branch branch3 = new Branch(use, Node.newBlock(Token.BLOCK));

    cfg.addEdge(entry, assign, branch1);
    cfg.addEdge(assign, use, branch2);
    cfg.addEdge(use, Node.newBlock(Token.BLOCK), branch3);

    MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(cfg, scope, null);

    ReachingUses result = analysis.computeFixedPoint();
    assertNotNull(result);
    assertFalse(result.mayUseMap.isEmpty());
    assertTrue(result.mayUseMap.containsKey(scope.getVar("a")));
    assertEquals(List.of(assign, use), result.mayUseMap.get(scope.getVar("a")));
  }

  @org.junit.Test
  public void testVariableAssignmentInConditional() {
    Scope scope = new Scope();
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();

    Node entry = Node.newBlock(Token.BLOCK);
    Node assign = Node.newName("a").set("=");
    Node value = Node.newNumber(1.0);
    Node condition = Node.newName("x").set("=").set(Node.newBoolean(true));

    Branch branch1 = new Branch(entry, condition);
    Branch branch2 = new Branch(condition, assign);
    Branch branch3 = new Branch(assign, Node.newBlock(Token.BLOCK));
    Branch branch4 = new Branch(Node.newBlock(Token.BLOCK), Node.newBlock(Token.BLOCK));

    cfg.addEdge(entry, condition, branch1);
    cfg.addEdge(condition, assign, branch2);
    cfg.addEdge(assign, Node.newBlock(Token.BLOCK), branch3);
    cfg.addEdge(Node.newBlock(Token.BLOCK), Node.newBlock(Token.BLOCK), branch4);

    MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(cfg, scope, null);

    ReachingUses result = analysis.computeFixedPoint();
    assertNotNull(result);
    assertFalse(result.mayUseMap.isEmpty());
    assertTrue(result.mayUseMap.containsKey(scope.getVar("a")));
    assertEquals(List.of(assign), result.mayUseMap.get(scope.getVar("a")));
  }

  @org.junit.Test
  public void testVariableAssignmentInLoop() {
    Scope scope = new Scope();
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();

    Node entry = Node.newBlock(Token.BLOCK);
    Node assign = Node.newName("a").set("=");
    Node value = Node.newNumber(1.0);
    Node condition = Node.newName("i").set("<").set(Node.newNumber(10));
    Node increment = Node.newName("i").set("+=").set(Node.newNumber(1));

    Branch branch1 = new Branch(entry, condition);
    Branch branch2 = new Branch(condition, assign);
    Branch branch3 = new Branch(assign, increment);
    Branch branch4 = new Branch(increment, Node.newName("i"));
    Branch branch5 = new Branch(Node.newName("i"), Node.newBlock(Token.BLOCK));

    cfg.addEdge(entry, condition, branch1);
    cfg.addEdge(condition, assign, branch2);
    cfg.addEdge(assign, increment, branch3);
    cfg.addEdge(increment, Node.newName("i"), branch4);
    cfg.addEdge(Node.newName("i"), Node.newBlock(Token.BLOCK), branch5);

    MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(cfg, scope, null);

    ReachingUses result = analysis.computeFixedPoint();
    assertNotNull(result);
    assertFalse(result.mayUseMap.isEmpty());
    assertTrue(result.mayUseMap.containsKey(scope.getVar("a")));
    assertEquals(List.of(assign), result.mayUseMap.get(scope.getVar("a")));
  }

  @org.junit.Test
  public void testVariableAssignmentInFunction() {
    Scope scope = new Scope();
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();

    Node entry = Node.newBlock(Token.BLOCK);
    Node function = new Node(Token.NAME, "foo");
    Node assign = Node.newName("a").set("=");
    Node value = Node.newNumber(1.0);

    Branch branch1 = new Branch(entry, function);
    Branch branch2 = new Branch(function.set("return"), assign);
    Branch branch3 = new Branch(assign, Node.newBlock(Token.BLOCK));

    cfg.addEdge(entry, function, branch1);
    cfg.addEdge(function, assign, branch2);
    cfg.addEdge(assign, Node.newBlock(Token.BLOCK), branch3);

    MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(cfg, scope, null);

    ReachingUses result = analysis.computeFixedPoint();
    assertNotNull(result);
    assertFalse(result.mayUseMap.isEmpty());
    assertTrue(result.mayUseMap.containsKey(scope.getVar("a")));
    assertEquals(List.of(assign), result.mayUseMap.get(scope.getVar("a")));
  }

  @org.junit.Test
  public void testEscapedVariable() {
    Scope scope = new Scope();
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();

    Node entry = Node.newBlock(Token.BLOCK);
    Node assign = Node.newName("a").set("=");
    Node value = Node.newName("global");
    Node use = Node.newName("a");

    Branch branch1 = new Branch(entry, assign);
    Branch branch2 = new Branch(assign, use);

    cfg.addEdge(entry, assign, branch1);
    cfg.addEdge(assign, use, branch2);

    MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(cfg, scope, null);

    ReachingUses result = analysis.computeFixedPoint();
    assertNotNull(result);
    assertFalse(result.mayUseMap.isEmpty());
    assertFalse(result.mayUseMap.containsKey(scope.getVar("a")));
  }
}