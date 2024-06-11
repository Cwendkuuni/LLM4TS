import com.google.javascript.jscomp.MaybeReachingVariableUse;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.Node;
import org.junit.Test;
import static org.junit.Assert.*;

public class MaybeReachingVariableUseTest {

 private Scope createMockScope() {
 // Create a mock scope with some variables
 return new Scope();
 }

 @Test
 public void testSimpleVariableUsage() {
 MaybeReachingVariableUse maybeReachingVariableUse = new MaybeReachingVariableUse(
 new ControlFlowGraph<>(null), createMockScope, null);

 Node assignmentNode = Node.newLiteral(Token.ASSIGN, 1);
 Node printNode = Node.newExpression(Token.PRINT, new Node[] {
 Node.newName("A") });
 Node functionNode = Node.newBlock("function() { var A = 1; print(A); }");
 Node functionCallNode = Node.newExpression(Token.CALL, functionNode, null);

 ReachingUses reachingUses = maybeReachingVariableUse.computeMayBeReachingUses(
 assignmentNode, functionCallNode);

 Collection<Node> uses = maybeReachingVariableUse.getUses("A", assignmentNode);
 assertTrue(uses.contains(printNode));
 assertEquals(1, uses.size());
 }

 @Test
 public void testVariableUsageWithCondition() {
 MaybeReachingVariableUse maybeReachingVariableUse = new MaybeReachingVariableUse(
 new ControlFlowGraph<>(null), createMockScope, null);

 Node ifNode = Node.newBlock("if (true) { var A = 1; }", null);
 Node printNode = Node.newExpression(Token.PRINT, new Node[] {
 Node.newName("A") });
 Node functionNode = Node.newBlock("function() { if (true) { var A = 1; } print(A); }", null);
 Node functionCallNode = Node.newExpression(Token.CALL, functionNode, null);

 ReachingUses reachingUses = maybeReachingVariableUse.computeMayBeReachingUses(
 ifNode, functionCallNode);

 Collection<Node> uses = maybeReachingVariableUse.getUses("A", ifNode);
 assertTrue(uses.contains(printNode));
 assertEquals(1, uses.size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullInput() {
 MaybeReachingVariableUse maybeReachingVariableUse = new MaybeReachingVariableUse(
 new ControlFlowGraph<>(null), createMockScope, null);
 maybeReachingVariableUse.computeMayBeReachingUses(null, null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullOutput() {
 MaybeReachingVariableUse maybeReachingVariableUse = new MaybeReachingVariableUse(
 new ControlFlowGraph<>(null), createMockScope, null);
 maybeReachingVariableUse.getUses(null, Node.newNull());
 }

 @Test
 public void testVariableUsageWithMultipleDefinitions() {
 MaybeReachingVariableUse maybeReachingVariableUse = new MaybeReachingVariableUse(
 new ControlFlowGraph<>(null), createMockScope, null);

 Node assignment1Node = Node.newLiteral(Token.ASSIGN, 1);
 Node assignment2Node = Node.newLiteral(Token.ASSIGN, 2);
 Node printNode = Node.newExpression(Token.PRINT, new Node[] {
 Node.newName("A") });
 Node functionNode = Node.newBlock("function() { var A = 1; print(A); var A = 2; }", null);
 Node functionCallNode = Node.newExpression(Token.CALL, functionNode, null);

 ReachingUses reachingUses = maybeReachingVariableUse.computeMayBeReachingUses(
 assignment1Node, functionCallNode);

 Collection<Node> uses = maybeReachingVariableUse.getUses("A", assignment1Node);
 assertTrue(uses.contains(printNode));
 assertEquals(1, uses.size());
 }

 @Test
 public void testVariableUsageWithEscape() {
 MaybeReachingVariableUse maybeReachingVariableUse = new MaybeReachingVariableUse(
 new ControlFlowGraph<>(null), createMockScope, null);

 Node functionNode = Node.newBlock("function f(x) { var esc = x; return function(y) { return esc; } }", null);
 Node functionCallNode = Node.newExpression(Token.CALL, functionNode, new Node[] { Node.newNumber(1) });
 Node useNode = Node.newExpression(Token.CALL, functionCallNode, new Node[] { Node.newNumber(2) });

 ReachingUses reachingUses = maybeReachingVariableUse.computeMayBeReachingUses(
 useNode, Node.newNull());

 assertTrue(reachingUses.mayUseMap.isEmpty());
 }
}