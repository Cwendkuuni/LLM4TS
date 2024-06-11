import static org.junit.Assert.*;
import static com.google.javascript.rhino.NodeFactory.createNode;
import static com.google.javascript.rhino.NodeType.*;

import org.junit.Test;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.jsonml.NodeUtil;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node forNode = createNode(Token.FOR, null);
    Node forNodeWithThreeChildren = createNode(Token.FOR, createNode(Token.IDENT, "label"), createNode(Token.SEMICOLON), createNode(Token.IDENT, "var"));

    assertTrue(NodeUtil.isForIn(forNodeWithThreeChildren));
    assertFalse(NodeUtil.isForIn(forNode));
  }

  @Test
  public void testIsStatement() {
    Node scriptNode = createNode(Token.SCRIPT, null);
    Node blockNode = createNode(BLOCK, null);
    Node labelNode = createNode(Token.LABEL, "label");
    Node statementNode = createNode(Token.IDENT, "identifier");
    Node expressionNode = createNode(Token.IDENT, "identifier");
    Node functionNode = createNode(Token.FUNCTION, createNode(Token.IDENT, "functionName"), createNode(Token.BLOCK, null), null);

    // Script node is a statement
    assertTrue(NodeUtil.isStatement(scriptNode));

    // Block node is a statement
    assertTrue(NodeUtil.isStatement(blockNode));

    // Label node is a statement
    assertTrue(NodeUtil.isStatement(labelNode));

    // Statement node is a statement
    assertTrue(NodeUtil.isStatement(statementNode));

    // Function node can be a statement or an expression
    assertFalse(NodeUtil.isStatement(expressionNode));
    assertTrue(NodeUtil.isStatement(functionNode));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node functionDeclarationNode = createNode(Token.FUNCTION, createNode(Token.IDENT, "functionName"), createNode(Token.BLOCK, null), null);
    Node functionExpressionNode = createNode(Token.IDENT, "functionName");

    assertTrue(NodeUtil.isFunctionDeclaration(functionDeclarationNode));
    assertFalse(NodeUtil.isFunctionDeclaration(functionExpressionNode));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node scriptNode = createNode(Token.SCRIPT, null);
    Node functionDeclarationNode = createNode(Token.FUNCTION, createNode(Token.IDENT, "functionName"), createNode(Token.BLOCK, null), null);
    Node functionNodeInScript = createNode(Token.FUNCTION, createNode(Token.IDENT, "functionName"), createNode(Token.BLOCK, null), null);
    Node functionNodeInFunction = createNode(Token.FUNCTION, null, createNode(Token.BLOCK, null), createNode(Token.FUNCTION, null, null, null));

    // Hoisted function declaration in script
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(functionDeclarationNode));

    // Function declaration not hoisted in function
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(functionNodeInFunction));

    // Function declaration not hoisted in script
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(functionDeclarationNode));

    // Function declaration hoisted in script
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(functionDeclarationNode));

    // Function declaration hoisted in script with child function
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(functionDeclarationNode));

    // Function expression not hoisted
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(functionExpressionNode));
  }
}