import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeUtil.*;

import com.google.common.base.Preconditions;
import com.google.javascript.rhino.Node;

public class NodeUtilTest {

  @ org.junit.Test
  public void testIsForIn() {
    Node nodeForIn = Node.newForIn();
    Node nodeNotForIn = Node.newCall(Token.IDENTIFIER, "console.log");

    assertTrue(isForIn(nodeForIn));
    assertFalse(isForIn(nodeNotForIn));
  }

  @ org.junit.Test
  public void testIsStatement() {
    Node nodeStatement = Node.newExpressionStatement(Node.newCall(Token.IDENTIFIER, "console.log"));
    Node nodeNotStatement = Node.newCall(Token.IDENTIFIER, "console.log");

    assertTrue(isStatement(nodeStatement));
    assertFalse(isStatement(nodeNotStatement));
  }

  @ org.junit.Test
  public void testIsFunctionDeclaration() {
    Node nodeFunction = Node.newFunction(null, null, Node.newBlock(null));
    Node nodeExpression = Node.newCall(Token.IDENTIFIER, "console.log");

    assertTrue(isFunctionDeclaration(nodeFunction));
    assertFalse(isFunctionDeclaration(nodeExpression));
  }

  @ org.junit.Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeFunction = Node.newFunction(null, null, Node.newBlock(null));
    Node nodeFunctionInFunction = new Node(Token.FUNCTION, Node.newIdentifier("outerFunction"), null, Node.newBlock(Node.newFunction(null, null, Node.newBlock(Node.newIdentifier("innerFunction")))));
    Node nodeFunctionInScript = Node.newFunction(null, null, Node.newBlock(Node.newIdentifier("functionInScript")));

    assertTrue(isHoistedFunctionDeclaration(nodeFunction));
    assertFalse(isHoistedFunctionDeclaration(nodeFunctionInFunction));
    assertFalse(isHoistedFunctionDeclaration(nodeFunctionInScript));
  }

  @ org.junit.Test(expected = AssertionError.class)
  public void testIsForInWithNullNode() {
    Node node = null;
    isForIn(node);
  }

  @ org.junit.Test(expected = AssertionError.class)
  public void testIsStatementWithNullNode() {
    Node node = null;
    isStatement(node);
  }

  @ org.junit.Test(expected = AssertionError.class)
  public void testIsFunctionDeclarationWithNullNode() {
    Node node = null;
    isFunctionDeclaration(node);
  }

  @ org.junit.Test(expected = AssertionError.class)
  public void testIsHoistedFunctionDeclarationWithNullNode() {
    Node node = null;
    isHoistedFunctionDeclaration(node);
  }
}