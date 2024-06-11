package com.google.javascript.jscomp.DeadAssignmentsElimination;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.AbstractPostOrderCallback;
import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.ControlFlowGraph.Branch;
import com.google.javascript.jscomp.DataFlowAnalysis.FlowState;
import com.google.javascript.jscomp.DeadAssignmentsElimination;
import com.google.javascript.jscomp.LiveVariablesAnalysis.LiveVariableLattice;
import com.google.javascript.rhino.Node;
import org.junit.Test;
import static com.google.common.truth.Truth.assertThat;

public class DeadAssignmentsEliminationTest {

 private AbstractCompiler compiler;
 DeadAssignmentsElimination deadAssignmentsElimination;

 @Before
 public void setUp() {
 compiler = new AbstractCompiler();
 deadAssignmentsElimination = new DeadAssignmentsElimination(compiler);
 }

 @Test
 public void testSimpleDeadAssignment() {
 Node root = Node.newBlock("root");
 Node x = Node.newName("x");
 Node assignX = Node.newAssignment(x, Node.newNumber(1));
 Node assignDeadX = Node.newAssignment(x, Node.newNumber(2));
 Node printX = Node.newCall("print", Node.newName("x"));
 root.addChild(assignX);
 root.addChild(assignDeadX);
 root.addChild(printX);

 deadAssignmentsElimination.process(null, root);

 assertThat(root.getChild(2).toString()).isEqualTo("print(1)"); // print statement should not be changed
 }

 @Test
 public void testDeadAssignmentInCondition() {
 Node root = Node.newBlock("root");
 Node x = Node.newName("x");
 Node assignX = Node.newAssignment(x, Node.newNumber(1));
 Node condition = Node.newConditional(
 Node.newName("y"), // condition
 Node.newName("y"), // false value
 Node.newName("x"), // true value
 Node.newName("z") // consequence
 );
 root.addChild(assignX);
 root.addChild(condition);

 deadAssignmentsElimination.process(null, root);

 assertThat(root.toString()).isEqualTo("root(assign x=1, y, z)"); // condition should not be changed
 }

 @Test
 public void testDeadAssignmentInLoop() {
 Node root = Node.newBlock("root");
 Node x = Node.newName("x");
 Node assignX = Node.newAssignment(x, Node.newNumber(1));
 Node forNode = Node.newForInit(Node.newName("i"), Node.newNumber(1), Node.newNumber(3), null);
 Node loopBody = Node.newBlock("body");
 Node assignDeadX = Node.newAssignment(x, Node.newNumber(2));
 loopBody.addChild(assignDeadX);
 Node incrementI = Node.newIncrement(Node.newName("i"));
 loopBody.addChild(incrementI);
 forNode.setBody(loopBody);
 root.addChild(forNode);

 deadAssignmentsElimination.process(null, root);

 // Assignment to x should be removed since it is dead
 assertThat(root.toString()).isEqualTo("root(for i=1 to 3, body(increment i))");
 }

 @Test
 public void testDeadAssignmentInFunction() {
 Node root = Node.newBlock("root");
 Node funcName = Node.newName("func");
 Node func = Node.newFunction("func", new Node[0], Node.newBlock("funcBody"));
 root.addChild(func);
 Node x = Node.newName("x");
 Node assignX = Node.newAssignment(x, Node.newNumber(1));
 Node funcBody = Node.newBlock("funcBody");
 funcBody.addChild(assignX);
 Node funcReturn = Node.newReturn(Node.newName("x"));
 funcBody.addChild(funcReturn);
 func.setBody(funcBody);

 deadAssignmentsElimination.process(null, root);

 // Assignment to x should not be removed since it is assigned to return value
 assertThat(func.getBody().toString()).isEqualTo("funcBody(assign x=1, return x)");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullRoot() {
 deadAssignmentsElimination.process(null, null);
 }

 @Test(expected = NullPointerException.class)
 public void testNullNode() {
 Node root = Node.newBlock("root");
 deadAssignmentsElimination.process(null, root);
 root.setChild(0, null);
 }

 @Test
 public void testDeadAssignmentInIf() {
 Node root = Node.newBlock("root");
 Node x = Node.newName("x");
 Node assignX = Node.newAssignment(x, Node.newNumber(1));
 Node ifNode = Node.newIf(
 Node.newName("y"), // condition
 Node.newBlock("ifBody"), // consequence
 Node.newBlock("elseBody") // alternative
 );
 root.addChild(assignX);
 root.addChild(ifNode);
 Node assignDeadX = Node.newAssignment(x, Node.newNumber(2));
 ifNode.setConsequence(assignDeadX);

 deadAssignmentsElimination.process(null, root);

 // Assignment to x should be removed since it is dead
 assertThat(ifNode.getConsequence().toString()).isEqualTo("ifBody()");
 }

 @Test
 public void testDeadAssignmentInSwitch() {
 Node root = Node.newBlock("root");
 Node x = Node.newName("x");
 Node assignX = Node.newAssignment(x, Node.newNumber(1));
 Node switchNode = Node.newSwitchExpression(x);
 Node caseNode = Node.newCase(Node.newNumber(1), Node.newBlock("caseBody"));
 switchNode.addCase(caseNode);
 root.addChild(assignX);
 root.addChild(switchNode);
 Node assignDeadX = Node.newAssignment(x, Node.newNumber(2));
 caseNode.setBody(assignDeadX);

 deadAssignmentsElimination.process(null, root);

 // Assignment to x should be removed since it is dead
 assertThat(caseNode.getBody().toString()).isEqualTo("caseBody()");
 }

 @Test
 public void testDeadAssignmentInReturn() {
 Node root = Node.newBlock("root");
 Node x = Node.newName("x");
 Node assignX = Node.newAssignment(x, Node.newNumber(1));
 Node returnStatement = Node.newReturn(Node.newName("x"));
 root.addChild(assignX);
 root.addChild(returnStatement);

 deadAssignmentsElimination.process(null, root);

 // Assignment to x should not be removed since it is return statement
 assertThat(returnStatement.getChild(0).toString()).isEqualTo("x");
 }

 @Test
 public void testDeadAssignmentInVariableDeclaration() {
 Node root = Node.newBlock("root");
 Node x = Node.newName("x");
 Node assignX = Node.newAssignment(x, Node.newNumber(1));
 Node variableDeclaration = Node.newVar("x", Node.newName("x"), Node.newNumber(1));
 root.addChild(assignX);
 root.addChild(variableDeclaration);

 deadAssignmentsElimination.process(null, root);

 // Assignment to x should not be removed since it is part of variable declaration
 assertThat(root.toString()).isEqualTo("root(assign x=1, var x=1)");
 }

 @Test
 public void testDeadAssignmentInExpression() {
 Node root = Node.newBlock("root");
 Node x = Node.newName("x");
 Node assignX = Node.newAssignment(x, Node.newNumber(1));
 Node expression = Node.newCall("Math.sqrt", Node.newName("x"));
 root.addChild(assignX);
 root.addChild(expression);

 deadAssignmentsElimination.process(null, root);

 // Assignment to x should not be removed since it is part of the expression
 assertThat(expression.getChild(0).toString()).isEqualTo("x");
 }

 @Test
 public void testDeadAssignmentInList() {
 Node root = Node.newBlock("root");
 Node x = Node.newName("x");
 Node assignX = Node.newAssignment(x, Node.newNumber(1));
 Node list = Node.newList(new Node[] { Node.newNumber(2), Node.newName("x"), Node.newNumber(3) });
 root.addChild(assignX);
 root.addChild(list);

 deadAssignmentsElimination.process(null, root);

 // Assignment to x should not be removed since it is part of the list
 assertThat(list.getChild(1).toString()).isEqualTo("x");
 }

 @Test
 public void testDeadAssignmentInMap() {
 Node root = Node.newBlock("root");
 Node x = Node.newName("x");
 Node assignX = Node.newAssignment(x, Node.newNumber(1));
 Node map = Node.newObjectLiteral();
 Node key = Node.newString("key");
 Node value = Node.newName("x");
 map.addProperty(key, value);
 root.addChild(assignX);
 root.addChild(map);

 deadAssignmentsElimination.process(null, root);

 // Assignment to x should not be removed since it is part of the map
 assertThat(map.getProperty(key).getChild(0).toString()).isEqualTo("x");
 }

 @Test
 public void testDeadAssignmentInObjectInitializer() {
 Node root = Node.newBlock("root");
 Node x = Node.newName("x");
 Node assignX = Node.newAssignment(x, Node.newNumber(1));
 Node objectInitializer = Node.newObjectInitializer("Object", new Node[] {
 Node.newProperty("prop1", Node.newName("x")),
 Node.newProperty("prop2", Node.newNumber(2))
 });
 root.addChild(assignX);
 root.addChild(objectInitializer);

 deadAssignmentsElimination.process(null, root);

 // Assignment to x should not be removed since it is part of the object initializer
 assertThat(objectInitializer.getProperty("prop1").getChild(0).toString()).isEqualTo("x");
 }

 @Test
 public void testDeadAssignmentInArrayLiteral() {
 Node root = Node.newBlock("root");
 Node x = Node.newName("x");
 Node assignX = Node.newAssignment(x, Node.newNumber(1));
 Node arrayLiteral = Node.newArrayLiteral(new Node[] { Node.newNumber(2), Node.newName("x"), Node.newNumber(3) });
 root.addChild(assignX);
 root.addChild(arrayLiteral);

 deadAssignmentsElimination.process(null, root);

 // Assignment to x should not be removed since it is part of the array literal
 assertThat(arrayLiteral.getChild(1).toString()).isEqualTo("x");
 }

 @Test
 public void testDeadAssignmentInFunctionCall() {
 Node root = Node.newBlock("root");
 Node x = Node.newName("x");
 Node assignX = Node.newAssignment(x, Node.newNumber(1));
 Node functionCall = Node.newCall("function", Node.newArray(new Node[] { Node.newName("x") }));
 root.addChild(assignX);
 root.addChild(functionCall);

 deadAssignmentsElimination.process(null, root);

 // Assignment to x should not be removed since it is argument in function call
 assertThat(functionCall.getArguments().get(0).toString()).isEqualTo("x");
 }
}