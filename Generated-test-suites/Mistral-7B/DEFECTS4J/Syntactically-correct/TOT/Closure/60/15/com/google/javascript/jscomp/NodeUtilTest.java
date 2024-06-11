import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeType;
import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node forNode = Node.newForStatement("for (var i = 0; i < 10; i++) {}", Token.KEYWORD_FOR, 0);
    Node notForNode = Node.newExpressionStatement("console.log('Hello, World!')", Token.PRINT, 0);

    assertTrue(NodeUtil.isForIn(forNode));
    assertFalse(NodeUtil.isForIn(notForNode));
  }

  @Test
  public void testIsStatement() {
    Node statementNode = Node.newExpressionStatement("var x = 5;", Token.VAR, 0);
    Node expressionNode = Node.newExpression("5 + 3", Token.PLUS, 0);

    assertTrue(NodeUtil.isStatement(statementNode));
    assertFalse(NodeUtil.isStatement(expressionNode));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node functionDeclaration = Node.newFunction(
        "function foo() { return 42; }", Token.FUNCTION, 0, null, null, null, null, null);
    Node functionExpression = Node.newExpressionStatement("function bar() { return 42; }", Token.FUNCTION, 0);

    assertTrue(NodeUtil.isFunctionDeclaration(functionDeclaration));
    assertFalse(NodeUtil.isFunctionDeclaration(functionExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node scriptNode = Node.newBlock("function foo() { return 42; }", Token.BLOCK, 0);
    Node functionDeclaration = Node.newFunction(
        "function bar() { return 42; }", Token.FUNCTION, 0, null, null, null, null, null);
    Node functionExpression = Node.newExpressionStatement("function baz() { return 42; }", Token.FUNCTION, 0);

    Node hoistedFunctionDeclaration = Node.newFunction(
        "function qux() { return 42; }", Token.FUNCTION, 0, null, null, null, null, null);
    Node hoistedScriptNode = Node.newBlock("function qux() { return 42; }", Token.BLOCK, 0);

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(hoistedFunctionDeclaration));
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(hoistedScriptNode));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(functionDeclaration));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(functionExpression));
  }

  @Test
  public void testNullNode() {
    assertNull(NodeUtil.isForIn(null));
    assertNull(NodeUtil.isStatement(null));
    assertNull(NodeUtil.isFunctionDeclaration(null));
    assertNull(NodeUtil.isHoistedFunctionDeclaration(null));
  }

  @Test
  public void testEmptyNode() {
    Node emptyNode = Node.newBlock("", Token.BLOCK, 0);

    assertFalse(NodeUtil.isForIn(emptyNode));
    assertFalse(NodeUtil.isStatement(emptyNode));
    assertNull(NodeUtil.isFunctionDeclaration(emptyNode));
    assertNull(NodeUtil.isHoistedFunctionDeclaration(emptyNode));
  }

  @Test
  public void testFunctionTypeNode() {
    Node functionTypeNode = Node.newExpression(
        "function", Token.FUNCTION, NodeType.FUNCTION_DECLARATION);

    assertFalse(NodeUtil.isForIn(functionTypeNode));
    assertFalse(NodeUtil.isStatement(functionTypeNode));
    assertTrue(NodeUtil.isFunctionDeclaration(functionTypeNode));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(functionTypeNode));
  }

  @Test
  public void testUndefinedNode() {
    Node undefinedNode = Node.newIdentifier("undefined", Token.IDENTIFIER);

    assertFalse(NodeUtil.isForIn(undefinedNode));
    assertFalse(NodeUtil.isStatement(undefinedNode));
    assertNull(NodeUtil.isFunctionDeclaration(undefinedNode));
    assertNull(NodeUtil.isHoistedFunctionDeclaration(undefinedNode));
  }

  @Test
  public void testNonNodeObject() {
    String nonNodeObject = "Not a Node";

    assertNull(NodeUtil.isForIn(nonNodeObject));
    assertNull(NodeUtil.isStatement(nonNodeObject));
    assertNull(NodeUtil.isFunctionDeclaration(nonNodeObject));
    assertNull(NodeUtil.isHoistedFunctionDeclaration(nonNodeObject));
  }

  @Test
  public void testInvalidNodeType() {
    Node invalidNode = Node.newExpression("1 + 1", Token.PLUS, NodeType.ASSIGNMENT);

    assertFalse(NodeUtil.isForIn(invalidNode));
    assertFalse(NodeUtil.isStatement(invalidNode));
    assertNull(NodeUtil.isFunctionDeclaration(invalidNode));
    assertNull(NodeUtil.isHoistedFunctionDeclaration(invalidNode));
  }
}