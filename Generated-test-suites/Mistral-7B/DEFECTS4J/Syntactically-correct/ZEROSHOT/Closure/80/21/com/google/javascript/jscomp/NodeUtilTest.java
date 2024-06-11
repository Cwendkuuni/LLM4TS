import static org.junit.Assert.*;
import static com.google.javascript.rhino.NodeType.*;

import com.google.common.base.Preconditions;
import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node utilNode = new Node("util");
    utilNode.setType(SCRIPT);

    Node forNode = new Node("for");
    forNode.setType(FOR);
    forNode.setChildCount(3);

    utilNode.addChild(forNode);

    assertTrue(NodeUtil.isForIn(forNode));
    assertFalse(NodeUtil.isForIn(utilNode));
  }

  @Test
  public void testIsStatement() {
    Node scriptNode = new Node("script");
    scriptNode.setType(SCRIPT);

    Node statementNode = new Node("statement");
    statementNode.setType(IF);

    scriptNode.addChild(statementNode);

    assertTrue(NodeUtil.isStatement(statementNode));
    assertTrue(NodeUtil.isStatement(scriptNode));

    Node expressionNode = new Node("expression");
    expressionNode.setType(IF);

    assertFalse(NodeUtil.isStatement(expressionNode));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node functionNode = new Node("function");
    functionNode.setType(FUNCTION);
    functionNode.setChildCount(2);

    Node nameNode = new Node("name");
    nameNode.setType(IDENTIFIER);
    nameNode.setStringValue("myFunction");

    Node bodyNode = new Node("body");
    bodyNode.setType(BLOCK);

    functionNode.setChild(0, nameNode);
    functionNode.setChild(1, bodyNode);

    assertTrue(NodeUtil.isFunctionDeclaration(functionNode));

    Node functionExpressionNode = new Node("function");
    functionExpressionNode.setType(FUNCTION);
    functionExpressionNode.setChildCount(1);

    Node bodyExpressionNode = new Node("body");
    bodyExpressionNode.setType(EXPRESSION);

    functionExpressionNode.setChild(0, bodyExpressionNode);

    assertFalse(NodeUtil.isFunctionDeclaration(functionExpressionNode));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node scriptNode = new Node("script");
    scriptNode.setType(SCRIPT);

    Node functionDeclarationNode = new Node("function");
    functionDeclarationNode.setType(FUNCTION);
    functionDeclarationNode.setChildCount(2);

    Node nameNode = new Node("name");
    nameNode.setType(IDENTIFIER);
    nameNode.setStringValue("myFunction");

    Node bodyNode = new Node("body");
    bodyNode.setType(BLOCK);

    functionDeclarationNode.setChild(0, nameNode);
    functionDeclarationNode.setChild(1, bodyNode);

    scriptNode.addChild(functionDeclarationNode);

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(functionDeclarationNode));

    Node functionDeclarationsNode = new Node("functionDeclarations");
    functionDeclarationsNode.setType(NODE_LIST);
    functionDeclarationsNode.setChildCount(1);
    functionDeclarationsNode.setChild(0, functionDeclarationNode);

    Node functionNode = new Node("function");
    functionNode.setType(FUNCTION);
    functionNode.setChildCount(1);

    Node bodyNode2 = new Node("body");
    bodyNode2.setType(BLOCK);

    functionNode.setChild(0, bodyNode2);

    Node functionExpressionNode = new Node("functionExpression");
    functionExpressionNode.setType(FUNCTION);
    functionExpressionNode.setChildCount(1);

    Node bodyExpressionNode = new Node("bodyExpression");
    bodyExpressionNode.setType(EXPRESSION);

    functionExpressionNode.setChild(0, bodyExpressionNode);

    functionDeclarationsNode.addChild(functionDeclarationNode);
    functionNode.addChild(functionExpressionsNode);

    scriptNode.addChild(functionNode);

    assertFalse(NodeUtil.isHoistedFunctionDeclaration(functionDeclarationNode));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(functionExpressionNode));
  }
}