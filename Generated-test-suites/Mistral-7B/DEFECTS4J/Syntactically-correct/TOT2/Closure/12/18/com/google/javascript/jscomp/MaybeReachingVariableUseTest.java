import com.google.javascript.jscomp.MaybeReachingVariableUse;
import com.google.javascript.jscomp.Scope;
import org.junit.Test;
import static org.junit.Assert.*;

public class MaybeReachingVariableUseTest {

 private Scope createScope() {
 // Create a sample scope for testing
 Scope scope = new Scope(null, null, null);
 // Add some local variables
 scope.putVar("a", new Var(1, 0, null, null));
 scope.putVar("b", new Var(2, 0, null, null));
 return scope;
 }

 @Test
 public void testBasicUsage() {
 MaybeReachingVariableUse maybeReachingVariableUse = new MaybeReachingVariableUse(
 new ControlFlowGraph<>(null), createScope(null), null);
 // Test basic usage of a local variable "a"
 Node aDefinitionNode = Node.newNode(Token.NAME, "a", null, null);
 Node aUseNode1 = Node.newNode(Token.NAME, "a", null, null);
 Node aUseNode2 = Node.newNode(Token.NAME, "a", null, null);
 Node aAssignmentNode = Node.newNode(Token.ASSIGN, "=", null, null, aDefinitionNode, null);
 maybeReachingVariableUse.compute(aAssignmentNode, null, null);
 List<Node> usesOfA = maybeReachingVariableUse.getUses("a", aDefinitionNode);
 assertTrue(usesOfA.contains(aUseNode1));
 assertTrue(usesOfA.contains(aUseNode2));
 }

 @Test
 public void testNoUse() {
 MaybeReachingVariableUse maybeReachingVariableUse = new MaybeReachingVariableUse(
 new ControlFlowGraph<>(null), createScope(null), null);
 // Test no use case of a local variable "b"
 Node bDefinitionNode = Node.newNode(Token.NAME, "b", null, null);
 Node bAssignmentNode = Node.newNode(Token.ASSIGN, "=", null, null, bDefinitionNode, null);
 maybeReachingVariableUse.compute(bAssignmentNode, null, null);
 List<Node> usesOfB = maybeReachingVariableUse.getUses("b", bDefinitionNode);
 assertEmpty(usesOfB);
 }

 @Test
 public void testEscapedVariable() {
 MaybeReachingVariableUse maybeReachingVariableUse = new MaybeReachingVariableUse(
 new ControlFlowGraph<>(null), createScope(null), null);
 // Test escaped variable usage
 Scope scope = createScope();
 scope.putVar("a", new Var(1, 0, null, null));
 scope.putVar("global", new Var(3, 0, null, null));
 maybeReachingVariableUse = new MaybeReachingVariableUse(
 new ControlFlowGraph<>(null), scope, null);
 Node aDefinitionNode = Node.newNode(Token.NAME, "a", null, null);
 Node globalAssignmentNode = Node.newNode(Token.ASSIGN, "=", null, null, Node.newNode(Token.NAME, "global", null, null), null);
 Node aAssignmentNode = Node.newNode(Token.ASSIGN, "=", null, null, aDefinitionNode, null);
 Node aUseNode = Node.newNode(Token.NAME, "a", null, null);
 maybeReachingVariableUse.compute(globalAssignmentNode, null, null);
 maybeReachingVariableUse.compute(aAssignmentNode, null, null);
 List<Node> usesOfA = maybeReachingVariableUse.getUses("a", aDefinitionNode);
 assertFalse(usesOfA.contains(aUseNode));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullScope() {
 MaybeReachingVariableUse maybeReachingVariableUse = new MaybeReachingVariableUse(
 new ControlFlowGraph<>(null), null, null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullControlFlowGraph() {
 MaybeReachingVariableUse maybeReachingVariableUse = new MaybeReachingVariableUse(
 null, createScope(null), null);
 }

 @Test
 public void testConditionalPath() {
 MaybeReachingVariableUse maybeReachingVariableUse = new MaybeReachingVariableUse(
 new ControlFlowGraph<>(null), createScope(null), null);
 // Test conditional path usage of a local variable "a"
 Node aDefinitionNode = Node.newNode(Token.NAME, "a", null, null);
 Node aUseNode1 = Node.newNode(Token.NAME, "a", null, null);
 Node aUseNode2 = Node.newNode(Token.NAME, "a", null, null);
 Node ifNode = Node.newNode(Token.IF, "if", null, null);
 Node aAssignmentNode1 = Node.newNode(Token.ASSIGN, "=", null, null, aDefinitionNode, null);
 Node aAssignmentNode2 = Node.newNode(Token.ASSIGN, "=", null, null, aDefinitionNode, null);
 Node trueBranch = Node.newNode(Token.BLOCK, null, null);
 Node falseBranch = Node.newNode(Token.BLOCK, null, null);
 Node ifCondition = Node.newNode(Token.BINARY, "&&", null, null, Node.newNode(Token.TRUE, null, null), Node.newNode(Token.FALSE, null, null));
 trueBranch.addChild(aAssignmentNode1);
 falseBranch.addChild(aAssignmentNode2);
 ifNode.addChild(ifCondition);
 ifNode.addChild(trueBranch);
 ifNode.addChild(falseBranch);
 maybeReachingVariableUse.compute(ifNode, null, null);
 List<Node> usesOfA = maybeReachingVariableUse.getUses("a", aDefinitionNode);
 assertTrue(usesOfA.contains(aUseNode1));
 assertTrue(usesOfA.contains(aUseNode2));
 }

 @Test
 public void testCycle() {
 MaybeReachingVariableUse maybeReachingVariableUse = new MaybeReachingVariableUse(
 new ControlFlowGraph<>(null), createScope(null), null);
 // Test cycle case of a local variable "a"
 Node aDefinitionNode = Node.newNode(Token.NAME, "a", null, null);
 Node aUseNode1 = Node.newNode(Token.NAME, "a", null, null);
 Node aUseNode2 = Node.newNode(Token.NAME, "a", null, null);
 Node aAssignmentNode1 = Node.newNode(Token.ASSIGN, "=", null, null, aDefinitionNode, null);
 Node aAssignmentNode2 = Node.newNode(Token.ASSIGN, "=", null, null, aDefinitionNode, null);
 Node cycleNode = Node.newNode(Token.NAME, "cycle", null, null);
 Node aUseNodeCycle = Node.newNode(Token.NAME, "a", null, null);
 Node cycleAssignmentNode1 = Node.newNode(Token.ASSIGN, "=", null, null, aDefinitionNode, null);
 Node cycleAssignmentNode2 = Node.newNode(Token.ASSIGN, "=", null, null, aUseNodeCycle, null);
 cycleNode.addChild(cycleAssignmentNode1);
 cycleNode.addChild(cycleAssignmentNode2);
 cycleNode.addChild(aUseNode1);
 cycleNode.addChild(aAssignmentNode1);
 cycleNode.addChild(aUseNode2);
 cycleNode.addChild(aAssignmentNode2);
 maybeReachingVariableUse.compute(cycleNode, null, null);
 List<Node> usesOfA = maybeReachingVariableUse.getUses("a", aDefinitionNode);
 assertTrue(usesOfA.contains(aUseNode1));
 assertTrue(usesOfA.contains(aUseNode2));
 }

 @Test(expected = IllegalStateException.class)
 public void testInvalidGraph() {
 MaybeReachingVariableUse maybeReachingVariableUse = new MaybeReachingVariableUse(
 new ControlFlowGraph<>(null), createScope(null), null);
 // Test invalid control flow graph case
 Node aDefinitionNode = Node.newNode(Token.NAME, "a", null, null);
 Node aUseNode1 = Node.newNode(Token.NAME, "a", null, null);
 Node aUseNode2 = Node.newNode(Token.NAME, "a", null, null);
 Node aAssignmentNode1 = Node.newNode(Token.ASSIGN, "=", null, null, aDefinitionNode, null);
 Node aAssignmentNode2 = Node.newNode(Token.ASSIGN, "=", null, null, aDefinitionNode, null);
 Node aUseNodeInvalid = Node.newNode(Token.NAME, "a", null, null);
 maybeReachingVariableUse.compute(aAssignmentNode1, null, null);
 maybeReachingVariableUse.compute(aAssignmentNode2, null, null);
 maybeReachingVariableUse.compute(aUseNodeInvalid, null, null);
 List<Node> usesOfA = maybeReachingVariableUse.getUses("a", aDefinitionNode);
 assertTrue(usesOfA.contains(aUseNode1));
 assertTrue(usesOfA.contains(aUseNode2));
 }

 @Test(expected = IllegalStateException.class)
 public void testForwardAnalysis() {
 MaybeReachingVariableUse maybeReachingVariableUse = new MaybeReachingVariableUse(
 new ControlFlowGraph<>(null), createScope(null), null);
 // Test forward analysis case
 maybeReachingVariableUse.setForwardAnalysis(true);
 Node aDefinitionNode = Node.newNode(Token.NAME, "a", null, null);
 Node aUseNode1 = Node.newNode(Token.NAME, "a", null, null);
 Node aUseNode2 = Node.newNode(Token.NAME, "a", null, null);
 Node aAssignmentNode1 = Node.newNode(Token.ASSIGN, "=", null, null, aDefinitionNode, null);
 Node aAssignmentNode2 = Node.newNode(Token.ASSIGN, "=", null, null, aDefinitionNode, null);
 Node aUseNodeInvalid = Node.newNode(Token.NAME, "a", null, null);
 maybeReachingVariableUse.compute(aAssignmentNode1, null, null);
 maybeReachingVariableUse.compute(aAssignmentNode2, null, null);
 maybeReachingVariableUse.compute(aUseNodeInvalid, null, null);
 List<Node> usesOfA = maybeReachingVariableUse.getUses("a", aDefinitionNode);
 assertTrue(usesOfA.contains(aUseNode1));
 assertTrue(usesOfA.contains(aUseNode2));
 }
}