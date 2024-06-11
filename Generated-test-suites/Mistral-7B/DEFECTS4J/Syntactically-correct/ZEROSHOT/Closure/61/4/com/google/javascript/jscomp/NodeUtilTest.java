import static org.junit.Assert.*;
import static com.google.javascript.rhino.NodeFactory.createNode;
import static com.google.javascript.rhino.Token.*;

import com.google.common.base.Preconditions;

import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node forNode = createNode(FOR, null, createNode(IDENTIFIER, "i"), createNode(IDENTIFIER, "array"), createNode(SEMICOLON), createNode(BLOCK, null, null, null), createNode(SEMICOLON), createNode(SEMICOLON));
    assertTrue(NodeUtil.isForIn(forNode));

    Node notForInNode = createNode(FOR, null, createNode(IDENTIFIER, "i"), createNode(IDENTIFIER, "array"), createNode(KEYWORD, "in"), createNode(BLOCK, null, null, null), createNode(SEMICOLON), createNode(SEMICOLON));
    assertFalse(NodeUtil.isForIn(notForInNode));
  }

  @Test
  public void testIsStatement() {
    Node statementNode = createNode(IDENTIFIER, "foo");
    assertTrue(NodeUtil.isStatement(statementNode));

    Node expressionNode = createNode(IDENTIFIER, "foo");
    Node parentNode = createNode(ASSIGN, null, expressionNode, createNode(IDENTIFIER, "bar"));
    assertFalse(NodeUtil.isStatement(expressionNode));
    assertFalse(NodeUtil.isStatement(parentNode));

    Node functionNode = createNode(FUNCTION, null, null, createNode(BLOCK, null, null, null), null, null);
    assertTrue(NodeUtil.isStatement(functionNode));

    Node functionExpressionNode = createNode(FUNCTION, null, null, createNode(BLOCK, null, null, null), null, createNode(IDENTIFIER, "func"));
    assertFalse(NodeUtil.isStatement(functionExpressionNode));

    Node scriptNode = createNode(SCRIPT, null, null, null);
    assertTrue(NodeUtil.isStatement(scriptNode));

    Node labelNode = createNode(LABEL, null, null, null, createNode(IDENTIFIER, "foo"));
    assertTrue(NodeUtil.isStatement(labelNode));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node functionDeclarationNode = createNode(FUNCTION, null, createNode(IDENTIFIER, "func"), null, null, createNode(BLOCK, null, null, null));
    assertTrue(NodeUtil.isFunctionDeclaration(functionDeclarationNode));

    Node functionExpressionNode = createNode(FUNCTION, null, null, createNode(BLOCK, null, null, null), null, createNode(IDENTIFIER, "func"));
    assertFalse(NodeUtil.isFunctionDeclaration(functionExpressionNode));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node hoistedFunctionDeclarationNode = createNode(FUNCTION, null, createNode(IDENTIFIER, "func"), null, null, createNode(BLOCK, null, null, null));
    Node scriptNode = createNode(SCRIPT, null, null, null);
    scriptNode.addChild(hoistedFunctionDeclarationNode);
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(hoistedFunctionDeclarationNode));

    Node functionDeclarationNodeInFunctionNode = createNode(FUNCTION, null, createNode(IDENTIFIER, "func"), null, null, createNode(BLOCK, null, null, null));
    Node functionNode = createNode(FUNCTION, null, null, null, null, null);
    functionNode.addChild(functionDeclarationNodeInFunctionNode);
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(functionDeclarationNodeInFunctionNode));
  }

  @Test(expected = IllegalStateException.class)
  public void testCheckState() {
    Node node = createNode(IDENTIFIER, "foo");
    NodeUtil.isStatement(null);
  }
}