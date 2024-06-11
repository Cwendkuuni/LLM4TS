import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.google.common.base.Preconditions;
import org.junit.Test;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.Node;
import static com.google.javascript.rhino.Token.*;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node forNode = new Node(FOR);
    forNode.setChild(0, new Node(TokenType.IDENTIFIER, "for"));
    forNode.setChild(1, new Node(IDENTIFIER, "i"));
    forNode.setChild(2, new Node(TokenType.ASSIGN, ASSIGN));
    forNode.setChild(3, new Node(TokenType.KEYWORD, "in"));
    forNode.setChild(4, new Node(TokenType.IDENTIFIER, "arr"));

    assertTrue(NodeUtil.isForIn(forNode));

    Node invalidForNode = new Node(FOR);
    invalidForNode.setChild(0, new Node(TokenType.IDENTIFIER, "for"));
    invalidForNode.setChild(1, new Node(TokenType.IDENTIFIER, "i"));
    invalidForNode.setChild(2, new Node(TokenType.ASSIGN, ASSIGN));

    assertFalse(NodeUtil.isForIn(invalidForNode));
  }

  @Test
  public void testIsStatement() {
    Node scriptNode = new Node(SCRIPT);
    Node statementNode = new Node(IDENTIFIER, "statement");
    scriptNode.setChild(0, statementNode);

    assertTrue(NodeUtil.isStatement(statementNode));

    Node expressionNode = new Node(IDENTIFIER, "expression");

    assertFalse(NodeUtil.isStatement(expressionNode));

    Node functionNode = new Node(FUNCTION);
    functionNode.setChild(0, new Node(TOKEN, FUNCTION));
    functionNode.setChild(1, new Node(IDENTIFIER, "func"));
    functionNode.setChild(2, new Node(BLOCK, 0));

    assertTrue(NodeUtil.isStatement(functionNode));

    Node ifNode = new Node(IF);
    ifNode.setChild(0, new Node(TokenType.KEYWORD, "if"));
    ifNode.setChild(1, new Node(BLOCK, 0));

    assertTrue(NodeUtil.isStatement(ifNode));

    Node forNode = new Node(FOR);
    forNode.setChild(0, new Node(TokenType.IDENTIFIER, "for"));
    forNode.setChild(1, new Node(IDENTIFIER, "i"));
    forNode.setChild(2, new Node(TokenType.ASSIGN, ASSIGN));
    forNode.setChild(3, new Node(TokenType.KEYWORD, "in"));
    forNode.setChild(4, new Node(TokenType.IDENTIFIER, "arr"));

    assertTrue(NodeUtil.isStatement(forNode));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node functionDeclarationNode = new Node(FUNCTION);
    functionDeclarationNode.setChild(0, new Node(TOKEN, FUNCTION));
    functionDeclarationNode.setChild(1, new Node(IDENTIFIER, "func"));
    functionDeclarationNode.setChild(2, new Node(BLOCK, 0));

    assertTrue(NodeUtil.isFunctionDeclaration(functionDeclarationNode));

    Node functionExpressionNode = new Node(IDENTIFIER, "func");
    functionExpressionNode.setChild(0, new Node(ASSIGN, ASSIGN));
    functionExpressionNode.setChild(1, new Node(BLOCK, 0));

    assertFalse(NodeUtil.isFunctionDeclaration(functionExpressionNode));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node hoistedFunctionDeclaration = new Node(FUNCTION);
    hoistedFunctionDeclaration.setChild(0, new Node(TOKEN, FUNCTION));
    hoistedFunctionDeclaration.setChild(1, new Node(IDENTIFIER, "func"));
    hoistedFunctionDeclaration.setChild(2, new Node(BLOCK, 0));

    Node scriptNode = new Node(SCRIPT);
    scriptNode.setChild(0, hoistedFunctionDeclaration);

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(hoistedFunctionDeclaration));

    Node functionNodeInsideFunction = new Node(FUNCTION);
    functionNodeInsideFunction.setChild(0, new Node(TOKEN, FUNCTION));
    functionNodeInsideFunction.setChild(1, new Node(IDENTIFIER, "func"));
    functionNodeInsideFunction.setChild(2, new Node(BLOCK, 0));

    Node functionNodeInsideScript = new Node(FUNCTION);
    functionNodeInsideScript.setChild(0, new Node(TOKEN, FUNCTION));
    functionNodeInsideScript.setChild(1, new Node(IDENTIFIER, "func"));
    functionNodeInsideScript.setChild(2, new Node(BLOCK, 0));

    Node scriptNodeWithFunction = new Node(SCRIPT);
    scriptNodeWithFunction.setChild(0, functionNodeInsideScript);

    assertFalse(NodeUtil.isHoistedFunctionDeclaration(functionNodeInsideFunction));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(functionNodeInsideScript));

    Node scriptNodeWithFunctionAtTop = new Node(FUNCTION);
    scriptNodeWithFunctionAtTop.setChild(0, hoistedFunctionDeclaration);
    scriptNodeWithFunctionAtTop.setChild(1, functionNodeInsideScript);

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(hoistedFunctionDeclaration));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(functionNodeInsideScript));
  }
}