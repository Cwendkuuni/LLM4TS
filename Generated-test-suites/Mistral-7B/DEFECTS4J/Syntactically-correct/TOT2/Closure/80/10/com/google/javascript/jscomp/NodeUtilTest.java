package com.google.javascript.jscomp.jsonml;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import com.google.javascript.rhino.Node;
import org.junit.Test;

public class NodeUtilTest {

 @Test
 public void testIsForIn() {
 NodeUtil.isForIn(new Node("for in statement"));
 // Test for different structures
 testIsForInWithDifferentStructure();

 // Test for FOR and other statements
 testIsForInWithForLoopsAndOtherStatements();
 }

 @Test
 public void testIsStatement() {
 NodeUtil.isStatement(new Node("statement"));
 // Test for different contexts
 testIsStatementWithDifferentContexts();
 }

 @Test
 public void testIsFunctionDeclaration() {
 NodeUtil.isFunctionDeclaration(new Node("function declaration"));
 // Test for different structures
 testIsFunctionDeclarationWithDifferentStructures();

 // Test for function expressions
 testIsFunctionDeclarationWithFunctionExpressions();
 }

 @Test
 public void testIsHoistedFunctionDeclaration() {
 NodeUtil.isHoistedFunctionDeclaration(new Node("hoisted function declaration"));
 // Test for hoisted function declarations
 testIsHoistedFunctionDeclarationWithHoistedFunctionDeclarations();

 // Test for non-hoisted function declarations
 testIsHoistedFunctionDeclarationWithNonHoistedFunctionDeclarations();
 }

 // Test cases for edge cases and exceptions
 @Test
 public void testIsForInWithDifferentStructure() {
 Node forInNodeWithInit = new Node("for (var i = 0; i < 10; i++) {}", Token.FOR);
 assertTrue(NodeUtil.isForIn(forInNodeWithInit));

 Node forInNodeWithInitAndStep = new Node("for (var i = 0; i < 10; i += 2) {}", Token.FOR);
 assertTrue(NodeUtil.isForIn(forInNodeWithInitAndStep));

 Node forNode = new Node("for (; ;) {}", Token.FOR);
 assertFalse(NodeUtil.isForIn(forNode));

 Node ifStatement = new Node("if (condition) {}", Token.IF);
 assertFalse(NodeUtil.isForIn(ifStatement));
 }

 @Test
 public void testIsForInWithForLoopsAndOtherStatements() {
 Node forLoop = new Node("for (var i = 0; i < 10; i++) {}", Token.FOR);
 assertFalse(NodeUtil.isForIn(forLoop));

 Node ifStatement = new Node("if (condition) {}", Token.IF);
 assertFalse(NodeUtil.isForIn(ifStatement));
 }

 @Test
 public void testIsStatementWithDifferentContexts() {
 Node statement1 = new Node("return 5;", Token.RETURN);
 assertTrue(NodeUtil.isStatement(statement1));

 Node expression1 = new Node("5 + 10", Token.ADD);
 assertFalse(NodeUtil.isStatement(expression1));

 Node expression2 = new Node("function foo() {}", Token.FUNCTION);
 assertFalse(NodeUtil.isStatement(expression2));
 }

 @Test
 public void testIsFunctionDeclarationWithDifferentStructures() {
 Node functionDeclarationWithFormals = new Node("function foo(x, y) {}", Token.FUNCTION);
 assertTrue(NodeUtil.isFunctionDeclaration(functionDeclarationWithFormals));

 Node functionDeclarationWithoutFormals = new Node("function foo() {}", Token.FUNCTION);
 assertTrue(NodeUtil.isFunctionDeclaration(functionDeclarationWithoutFormals));
 }

 @Test
 public void testIsFunctionDeclarationWithFunctionExpressions() {
 Node functionExpression1 = new Node("function foo() {}", Token.FUNCTION);
 assertFalse(NodeUtil.isFunctionDeclaration(functionExpression1));

 Node functionExpression2 = new Node("(function foo() {})()", Token.FUNCTION);
 assertFalse(NodeUtil.isFunctionDeclaration(functionExpression2));
 }

 @Test
 public void testIsHoistedFunctionDeclarationWithHoistedFunctionDeclarations() {
 Node hoistedFunctionDeclaration1 = new Node("function foo() {}", Token.FUNCTION);
 assertTrue(NodeUtil.isHoistedFunctionDeclaration(hoistedFunctionDeclaration1));

 Node hoistedFunctionDeclaration2 = new Node("function foo() {}", Token.FUNCTION);
 hoistedFunctionDeclaration2.setParent(new Node("script", Token.SCRIPT));
 assertTrue(NodeUtil.isHoistedFunctionDeclaration(hoistedFunctionDeclaration2));
 }

 @Test
 public void testIsHoistedFunctionDeclarationWithNonHoistedFunctionDeclarations() {
 Node nonHoistedFunctionDeclaration1 = new Node("function foo() {}", Token.FUNCTION);
 assertFalse(NodeUtil.isHoistedFunctionDeclaration(nonHoistedFunctionDeclaration1));

 Node nonHoistedFunctionDeclaration2 = new Node("function foo() {}", Token.FUNCTION);
 nonHoistedFunctionDeclaration2.setParent(new Node("function bar() {}", Token.FUNCTION));
 assertFalse(NodeUtil.isHoistedFunctionDeclaration(nonHoistedFunctionDeclaration2));
 }
}