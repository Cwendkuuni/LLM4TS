import static org.junit.Assert.*;
import static com.google.javascript.jscomp.jsonml.NodeUtil.*;
import com.google.common.base.Preconditions;
import com.google.javascript.rhino.Node;

import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node forNode = Node.newForInStatement("for (var i in array) {}", new int[0]);
    assertTrue(isForIn(forNode));

    Node notForInNode = Node.newExpressionStatement(Node.newIdentifier("x"));
    assertFalse(isForIn(notForInNode));
  }

  @Test
  public void testIsStatement() {
    Node statementNode = Node.newExpressionStatement(Node.newIdentifier("x"));
    assertTrue(isStatement(statementNode));

    Node notStatementNode = Node.newIdentifier("x");
    assertFalse(isStatement(notStatementNode));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node functionDeclarationNode = Node.newFunction(
        "myFunction", new int[0], Node.newBlock(new int[0]));
    assertTrue(isFunctionDeclaration(functionDeclarationNode));

    Node notFunctionDeclarationNode = Node.newExpressionStatement(
        Node.newCall(Node.newIdentifier("myFunction"), new int[0]));
    assertFalse(isFunctionDeclaration(notFunctionDeclarationNode));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node scriptNode = Node.newScript(
        "function myFunction() {}\n" + // Hoisted function declaration
        "function anotherFunction() {}\n" + // Not hoisted function declaration
        "");
    Node myFunctionNode = scriptNode.getChild(0);
    assertTrue(isHoistedFunctionDeclaration(myFunctionNode));

    Node anotherFunctionNode = scriptNode.getChild(1);
    assertFalse(isHoistedFunctionDeclaration(anotherFunctionNode));
  }

  @Test
  public void testIsFunctionExpression() {
    Node functionExpressionNode = Node.newExpressionStatement(
        Node.newFunction(Node.newIdentifier("x"), new int[0], Node.newBlock(new int[0])));
    assertFalse(isFunctionDeclaration(functionExpressionNode));
    assertTrue(isFunctionExpression(functionExpressionNode));
  }

  @Test(expected = AssertionError.class)
  public void testPreconditionFailure() {
    Node n = Node.newExpressionStatement(Node.newIdentifier("x"));
    Preconditions.checkState(n.getParent() == null);
  }
}