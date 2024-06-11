package com.google.javascript.jscomp.test;

import com.google.common.collect.Lists;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.ParallelCompilerPass;
import com.google.javascript.rhino.NodeFactory;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class RemoveConstantExpressionsTest {

 private static Node createNode(int type, int lineNumber) {
 return new Node(type, null, null, null, lineNumber);
 }

 private static Node createBinaryOperator(int opType, Node left, Node right) {
 return new Node(opType, left, right, null, 0);
 }

 private static Node createExpressionStatement(Node expr) {
 return new Node(Token.EXPR_STATEMENT, expr, null, null, 0);
 }

 private static Node createStatementList(Node... statements) {
 return new Node(Token.BLOCK, null, Lists.newArrayList(statements), null, 0);
 }

 private static Compiler createCompiler() {
 return new Compiler();
 }

 @Test
 public void testRemoveConstantExpressions() {
 Compiler compiler = createCompiler();
 CompilerPass pass = new RemoveConstantExpressions(compiler);

 // Test case 1: Simple constant expression
 Node constantExpression = createNode(Token.NUMBER, 1);
 Node constantStatement = createExpressionStatement(constantExpression);
 Node statementList = createStatementList(constantStatement);

 Node root = NodeFactory.createRootNode();
 NodeTraversal.traverse(null, root, statementList);
 ParallelCompilerPass.runPasses(pass, root);
 Node simplifiedRoot = pass.getResult().getOutput();

 // Assert that the constant expression was removed and replaced with an empty statement
 assertEquals(Token.EMPTY, simplifiedRoot.getFirstChild().getType());

 // Test case 2: Constant expression with side effects
 Node sideEffectExpression = createNode(Token.IDENTIFIER, "foo");
 Node sideEffectStatement = createExpressionStatement(sideEffectExpression);
 Node sideEffectStatementList = createStatementList(sideEffectStatement);
 Node sideEffectStatement2 = createExpressionStatement(
 createNumberLiteral(2)); // A constant number

 Node rootWithSideEffects = NodeFactory.createRootNode();
 NodeTraversal.traverse(null, rootWithSideEffects, sideEffectStatementList, sideEffectStatementList);
 ParallelCompilerPass.runPasses(pass, rootWithSideEffects);
 Node simplifiedRootWithSideEffects = pass.getResult().getOutput();

 // Assert that the constant statement was not removed and the side effect statement remains
 assertEquals(Token.EXPR_STATEMENT, simplifiedRootWithSideEffects.getFirstChild().getType());
 assertEquals("foo", simplifiedRootWithSideEffects.getFirstChild().getFirstChild().getFirstChild().getString());

 // Test case 3: Complex constant expression
 Node complexConstantExpression = createBinaryOperator(Token.ADD,
 createBinaryOperator(Token.ADD, constantExpression, sideEffectExpression),
 createNumberLiteral(3));
 Node complexConstantStatement = createExpressionStatement(complexConstantExpression);
 Node complexConstantStatementList = createStatementList(complexConstantStatement);

 Node rootWithComplexConstantExpression = NodeFactory.createRootNode();
 NodeTraversal.traverse(null, rootWithComplexConstantExpression, complexConstantStatementList);
 ParallelCompilerPass.runPasses(pass, rootWithComplexConstantExpression);
 Node simplifiedRootWithComplexConstantExpression = pass.getResult().getOutput();

 // Assert that the complex constant expression was removed and replaced with the side effect statement
 assertEquals(Token.EXPR_STATEMENT, simplifiedRootWithComplexConstantExpression.getFirstChild().getType());
 assertEquals("foo", simplifiedRootWithComplexConstantExpression.getFirstChild().getFirstChild().getFirstChild().getString());
 }

 private Node createNumberLiteral(int value) {
 return new Node(Token.NUMBER, null, null, Integer.toString(value), 0);
 }
}