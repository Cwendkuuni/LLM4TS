import static org.junit.Assert.*;
import static com.google.javascript.jscomp.jsonml.NodeUtil.*;

import com.google.common.base.Preconditions;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node forNode = Node.newNode(Token.FOR, new int[] {1, 2, 3}, null);
    assertTrue(isForIn(forNode));

    Node notForNode = Node.newNode(Token.IF, new int[] {1, 2}, null);
    assertFalse(isForIn(notForNode));
  }

  @Test
  public void testIsStatement() {
    Node statementNode = Node.newNode(Token.IF, new int[] {1, 2}, null);
    Node notStatementNode = Node.newNode(Token.IDENT, "foo", new int[] {1});

    Node scriptNode = Node.newNode(Token.SCRIPT, new int[] {1}, null);
    Node blockNode = Node.newNode(Token.BLOCK, new int[] {1}, null);
    Node labelNode = Node.newNode(Token.LABEL, "label", new int[] {1});

    assertTrue(isStatement(statementNode));
    assertTrue(isStatement(scriptNode));
    assertTrue(isStatement(blockNode));
    assertTrue(isStatement(labelNode));

    assertFalse(isStatement(notStatementNode));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node functionNode = Node.newNode(Token.FUNCTION, new int[] {1, 2}, null);
    functionNode.setProperty("id", "functionName");

    Node statementFunctionNode = Node.newNode(Token.FUNCTION, new int[] {1, 2}, null);
    statementFunctionNode.setProperty("id", "functionName");
    Node parentStatement = Node.newNode(Token.IF, new int[] {1, 2}, null);
    parentStatement.addChildToStatementList(statementFunctionNode);

    Node expressionFunctionNode = Node.newNode(Token.FUNCTION, new int[] {1, 2}, null);
    Node parentExpression = Node.newNode(Token.ASSIGN, new int[] {1, 2}, null);
    parentExpression.setRightHandSide(expressionFunctionNode);

    assertTrue(isFunctionDeclaration(functionNode));
    assertFalse(isFunctionDeclaration(statementFunctionNode));
    assertFalse(isFunctionDeclaration(expressionFunctionNode));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node hoistedFunctionDeclaration = Node.newNode(Token.FUNCTION, new int[] {1, 2}, null);
    hoistedFunctionDeclaration.setProperty("id", "functionName");
    Node parentScript = Node.newNode(Token.SCRIPT, new int[] {1}, null);
    parentScript.addChildToStatementList(hoistedFunctionDeclaration);

    Node notHoistedFunctionDeclaration = Node.newNode(Token.FUNCTION, new int[] {1, 2}, null);
    notHoistedFunctionDeclaration.setProperty("id", "functionName");
    Node parentFunction = Node.newNode(Token.FUNCTION, new int[] {1, 2}, null);
    parentFunction.addChildToStatementList(notHoistedFunctionDeclaration);

    assertTrue(isHoistedFunctionDeclaration(hoistedFunctionDeclaration));
    assertFalse(isHoistedFunctionDeclaration(notHoistedFunctionDeclaration));
  }
}