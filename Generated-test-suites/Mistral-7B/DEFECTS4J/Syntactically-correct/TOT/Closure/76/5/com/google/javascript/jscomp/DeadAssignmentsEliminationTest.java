package com.google.javascript.jscomp.DeadAssignmentsEliminationTest;

import static com.google.javascript.jscomp.NodeTraversal.traverse;
import static com.google.javascript.rhino.NodeOperations.newAssignment;
import static com.google.javascript.rhino.NodeOperations.newBinaryOp;
import static com.google.javascript.rhino.NodeOperations.newExpression;
import static com.google.javascript.rhino.NodeOperations.newIdentifier;
import static com.google.javascript.rhino.NodeOperations.newIfStatement;
import static com.google.javascript.rhino.NodeOperations.newLiteral;
import static com.google.javascript.rhino.NodeOperations.newReturnStatement;
import static com.google.javascript.rhino.NodeOperations.newStatement;
import static com.google.javascript.rhino.NodeOperations.newVariable;
import static com.google.javascript.rhino.NodeOperations.newWhileStatement;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerPassCallback;
import com.google.javascript.jscomp.DataFlowAnalysis.FlowState;
import com.google.javascript.jscomp.DeadAssignmentsElimination;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.NodeOperations;

public class DeadAssignmentsEliminationTest {
  private AbstractCompiler compiler;
  private CompilerOptions options;
  private Scope scope;
  private Node root;
  private DeadAssignmentsElimination eliminator;

  @Before
  public void setUp() throws Exception {
    options = new CompilerOptions();
    compiler = new AbstractCompiler(options);
    scope = new Scope(null, null);
    root = newExpression(NodeOperations.newBlock(newStatementList()));
    eliminator = new DeadAssignmentsElimination(compiler);
  }

  @After
  public void tearDown() throws Exception {
    if (eliminator != null) {
      eliminator.process(null, root);
      eliminator = null;
    }
    compiler = null;
    scope = null;
    root = null;
  }

  @Test
  public void testIfDeadAssignmentsEliminated() {
    Node x = newVariable("x");
    Node y = newVariable("y");
    Node z = newVariable("z");

    Node assignX = newAssignment(x, newLiteral(1));
    Node assignY = newAssignment(y, newLiteral(2));
    Node assignZ = newAssignment(z, x);

    Node condition = newLiteral(false);
    Node ifStmt = newIfStatement(assignX, newBlock(newStatementList()),
        newBlock(newStatementList()), condition);

    root.addChild(ifStmt);
    root.addChild(assignY);
    root.addChild(newStatement(newReturnStatement(newIdentifier("z"))));

    eliminator.process(null, root);

    Node actual = root;
    Node expected = newBlock(newStatementList(), assignX, assignY, assignZ,
        newReturnStatement(newIdentifier("z")));

    assertEquals(expected, actual);
  }

  @Test
  public void testIfDeadAssignmentIsNotRemovedWhenItCanBeReused() {
    Node x = newVariable("x");
    Node y = newVariable("y");
    Node z = newVariable("z");

    Node assignX = newAssignment(x, newLiteral(1));
    Node assignY = newAssignment(y, x);
    Node assignZ = newAssignment(z, x);

    Node condition = newLiteral(false);
    Node ifStmt = newIfStatement(assignX, newBlock(newStatementList()),
        newBlock(newStatementList()), condition);

    root.addChild(ifStmt);
    root.addChild(assignY);
    root.addChild(assignZ);
    root.addChild(newStatement(newReturnStatement(newIdentifier("z"))));

    eliminator.process(null, root);

    Node actual = root;
    Node expected = newBlock(newStatementList(), assignX, assignY, assignZ,
        newReturnStatement(newIdentifier("z")));

    assertEquals(expected, actual);
  }

  @Test
  public void testDeadAssignmentIsRemovedWhenItDoesNotAffectControlFlow() {
    Node x = newVariable("x");
    Node y = newVariable("y");
    Node z = newVariable("z");

    Node assignX = newAssignment(x, newLiteral(1));
    Node assignY = newAssignment(y, x);
    Node assignZ = newAssignment(z, x);

    Node ifStmt = newIfStatement(newLiteral(true),
        newBlock(newStatementList()), newBlock(newStatementList()), null);

    root.addChild(ifStmt);
    root.addChild(assignX);
    root.addChild(assignY);
    root.addChild(assignZ);
    root.addChild(newStatement(newReturnStatement(newIdentifier("z"))));

    eliminator.process(null, root);

    Node actual = root;
    Node expected = newBlock(newStatementList(), assignX, assignZ,
        newReturnStatement(newIdentifier("z")));

    assertEquals(expected, actual);
  }

  @Test
  public void testDeadAssignmentIsNotRemovedWhenItAffectsControlFlow() {
    Node x = newVariable("x");
    Node y = newVariable("y");
    Node z = newVariable("z");

    Node assignX = newAssignment(x, newLiteral(1));
    Node assignY = newAssignment(y, x);
    Node assignZ = newAssignment(z, x);

    Node assignX2 = newAssignment(x, newLiteral(2));

    Node ifStmt = newIfStatement(newLiteral(true), newBlock(newStatementList()),
        newBlock(newStatementList()), null);

    root.addChild(ifStmt);
    root.addChild(assignX);
    root.addChild(assignY);
    root.addChild(assignZ);
    root.addChild(assignX2);
    root.addChild(newStatement(newReturnStatement(newIdentifier("z"))));

    eliminator.process(null, root);

    Node actual = root;
    Node expected = newBlock(newStatementList(), assignX, assignY, assignZ,
        newAssignment(x, newLiteral(2)), newReturnStatement(newIdentifier("z")));

    assertEquals(expected, actual);
  }

  @Test
  public void testDeadAssignmentIsNotRemovedWhenItIsUsedInsideAFunctionExpression() {
    Node x = newVariable("x");
    Node y = newVariable("y");

    Node function = newExpression(
        newAssignment(newIdentifier("f"), newFunctionExpression(null,
            newList(newIdentifier("x")), newBlock(newStatementList(),
                newAssignment(x, newLiteral(1)), newReturnStatement(x)))));

    Node assignY = newAssignment(y, x);

    root.addChild(function);
    root.addChild(assignY);
    root.addChild(newStatement(newReturnStatement(newIdentifier("y"))));

    eliminator.process(null, root);

    Node actual = root;
    Node expected = newBlock(newStatementList(), function, assignY,
        newReturnStatement(newIdentifier("y")));

    assertEquals(expected, actual);
  }

  @Test
  public void testDeadAssignmentIsNotRemovedWhenItIsUsedOutsideAFunctionExpression() {
    Node x = newVariable("x");
    Node y = newVariable("y");

    Node function = newExpression(newAssignment(newIdentifier("f"),
        newFunctionExpression(null, newList(newIdentifier("x")),
            newBlock(newStatementList(), newReturnStatement(x)))));

    Node assignY = newAssignment(y, x);

    root.addChild(newExpression(function));
    root.addChild(assignY);
    root.addChild(newStatement(newReturnStatement(newIdentifier("y"))));

    eliminator.process(null, root);

    Node actual = root;
    Node expected = newBlock(newStatementList(), newExpression(function),
        assignY, newReturnStatement(newIdentifier("y")));

    assertEquals(expected, actual);
  }

  @Test
  public void testDeadAssignmentIsNotRemovedWhenItIsUsedAsCondition() {
    Node x = newVariable("x");
    Node y = newVariable("y");

    Node assignX = newAssignment(x, newLiteral(1));
    Node assignY = newAssignment(y, x);

    Node ifStmt = newIfStatement(x, newBlock(newStatementList()),
        newBlock(newStatementList()), null);
    root.addChild(ifStmt);
    root.addChild(assignX);
    root.addChild(assignY);
    root.addChild(newStatement(newReturnStatement(newIdentifier("y"))));

    eliminator.process(null, root);

    Node actual = root;
    Node expected = newBlock(newStatementList(), ifStmt, assignX,
        assignY, newReturnStatement(newIdentifier("y")));

    assertEquals(expected, actual);
  }

  @Test
  public void testDeadAssignmentIsNotRemovedWhenItIsUsedAsComparisonOperator() {
    Node x = newVariable("x");
    Node y = newVariable("y");

    Node assignX = newAssignment(x, newLiteral(1));
    Node assignY = newAssignment(y, x);

    Node condition = newBinaryOp(Token.LT, x, y);
    Node ifStmt = newIfStatement(condition, newBlock(newStatementList()),
        newBlock(newStatementList()), null);

    root.addChild(ifStmt);
    root.addChild(assignX);
    root.addChild(assignY);
    root.addChild(newStatement(newReturnStatement(newIdentifier("x"))));

    eliminator.process(null, root);

    Node actual = root;
    Node expected = newBlock(newStatementList(), ifStmt, assignX, assignY,
        newReturnStatement(newIdentifier("x")));

    assertEquals(expected, actual);
  }

  @Test
  public void testDeadAssignmentIsNotRemovedWhenItIsUsedAsIncrementOrDecrementOperator() {
    Node x = newVariable("x");
    Node y = newVariable("y");

    Node assignX = newAssignment(x, newLiteral(1));
    Node assignY =
        newAssignment(y, newBinaryOp(Token.INC, newIdentifier(x.getName())));

    Node condition = newLiteral(false);
    Node whileStmt = newWhileStatement(condition,
        newBlock(newStatementList()), null);

    root.addChild(whileStmt);
    root.addChild(assignX);
    root.addChild(assignY);
    root.addChild(newStatement(newReturnStatement(newIdentifier("x"))));

    eliminator.process(null, root);

    Node actual = root;
    Node expected = newBlock(newStatementList(), whileStmt, assignX, assignY,
        newReturnStatement(newIdentifier("x")));

    assertEquals(expected, actual);
  }
}