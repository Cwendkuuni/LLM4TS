import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static com.google.javascript.jscomp.jsonml.NodeUtil.isForIn;
import static com.google.javascript.jscomp.jsonml.NodeUtil.isStatement;
import static com.google.javascript.jscomp.jsonml.NodeUtil.isFunctionDeclaration;
import static com.google.javascript.jscomp.jsonml.NodeUtil.isHoistedFunctionDeclaration;

import com.google.common.base.Preconditions;
import com.google.javascript.rhino.Node;

public class NodeUtilTest {

  @org.junit.Test
  public void testIsForIn() {
    Node forNode = new Node(Token.FOR);
    forNode.setChildCount(3);
    Node forIn = new Node(forNode);

    assertTrue(isForIn(forIn));

    Node notForIn = new Node(Token.FOR);
    notForIn.setChildCount(2);
    Node notForInNode = new Node(notForIn);

    assertFalse(isForIn(notForInNode));
  }

  @org.junit.Test
  public void testIsStatement() {
    Node scriptNode = new Node(Token.SCRIPT);
    Node statementNode = new Node(Token.PRINT, new Node("output"));
    Node statementInsideScript = new Node(Token.ASSIGN, new Node("x"), new Node(1));

    Node blockNode = new Node(Token.BLOCK);
    Node labelNode = new Node(Token.LABEL, "label");
    Node statementInsideBlock = new Node(Token.ASSIGN, new Node("y"), new Node(2));
    Node statementInsideFunction = new Node(Token.ASSIGN, new Node("z"), new Node(3));

    Node functionNode = new Node(Token.FUNCTION, new Node("funcName"), new Node(Token.BLOCK));
    Node functionExpressionNode = new Node(Token.FUNCTION, new Node("funcName"), new Node(Token.ASSIGN, new Node("x"), new Node(1)));

    assertTrue(isStatement(statementNode));
    assertTrue(isStatement(statementInsideScript));
    assertTrue(isStatement(statementInsideBlock));
    assertTrue(isStatement(statementInsideFunction));

    assertTrue(isStatement(scriptNode));
    assertFalse(isStatement(blockNode));
    assertFalse(isStatement(labelNode));
    assertFalse(isStatement(functionNode));
    assertFalse(isStatement(functionExpressionNode));
  }

  @org.junit.Test
  public void testIsFunctionDeclaration() {
    Node functionDeclaration = new Node(Token.FUNCTION, new Node("funcName"), new Node(Token.BLOCK));
    Node functionExpression = new Node(Token.FUNCTION, new Node("funcName"), new Node(Token.ASSIGN, new Node("x"), new Node(1)));

    assertTrue(isFunctionDeclaration(functionDeclaration));
    assertFalse(isFunctionDeclaration(functionExpression));
  }

  @org.junit.Test
  public void testIsHoistedFunctionDeclaration() {
    Node scriptNode = new Node(Token.SCRIPT);
    Node functionDeclaration = new Node(Token.FUNCTION, new Node("funcName"), new Node(Token.BLOCK));
    Node functionDeclarationInsideScript = new Node(Token.FUNCTION, new Node("funcName"), new Node(Token.BLOCK));

    Node functionNode = new Node(Token.FUNCTION, new Node("funcName"), new Node(Token.BLOCK));
    Node functionDeclarationInsideFunction = new Node(Token.FUNCTION, new Node("funcName"), new Node(Token.BLOCK));

    assertTrue(isHoistedFunctionDeclaration(functionDeclaration));
    assertFalse(isHoistedFunctionDeclaration(functionNode));
    assertFalse(isHoistedFunctionDeclaration(functionDeclarationInsideFunction));
    assertFalse(isHoistedFunctionDeclaration(functionDeclarationInsideScript));
  }
}