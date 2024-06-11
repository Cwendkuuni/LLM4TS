import static org.junit.Assert.*;
import static com.google.javascript.rhino.NodeType.*;

import com.google.common.base.Preconditions;
import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = Node.newNode(Token.FOR, new Object[]{"id", "id", "id"});
    assertTrue(NodeUtil.isForIn(nodeForIn));

    Node nodeNotForIn = Node.newNode(Token.IF, new Object[]{"id", "id"});
    assertFalse(NodeUtil.isForIn(nodeNotForIn));
  }

  @Test
  public void testIsStatement() {
    Node nodeStatement = Node.newNode(Token.IF, new Object[]{"id", "id"});
    Node parentScript = Node.newNode(Token.SCRIPT, new Object[0]);
    parentScript.addChildToChildren(nodeStatement);

    assertTrue(NodeUtil.isStatement(nodeStatement));

    Node nodeNotStatement = Node.newNode(Token.IF, new Object[]{"id", "id"});
    assertFalse(NodeUtil.isStatement(nodeNotStatement));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = Node.newNode(
        Token.FUNCTION, new Object[]{"id", Token.ID, "id"}, null);
    Node parentBlock = Node.newNode(Token.BLOCK, new Object[0]);
    parentBlock.addChildToChildren(nodeFunctionDeclaration);

    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));

    Node nodeFunctionExpression = Node.newNode(
        Token.FUNCTION, new Object[]{"id", Token.ID, "id"}, null);
    Node parentExpression = Node.newNode(Token.ASSIGN, new Object[]{"id", "id"});
    parentExpression.addChildToChildren(nodeFunctionExpression);

    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeHoistedFunctionDeclaration = Node.newNode(
        Token.FUNCTION, new Object[]{"id", Token.ID, "id"}, null);
    Node parentScript = Node.newNode(Token.SCRIPT, new Object[0]);
    parentScript.addChildToChildren(nodeHoistedFunctionDeclaration);

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeHoistedFunctionDeclaration));

    Node nodeFunctionDeclarationInFunction = Node.newNode(
        Token.FUNCTION, new Object[]{"id", Token.ID, "id"}, null);
    Node parentFunction = Node.newNode(Token.FUNCTION, new Object[]{"id", "id", "id"}, null);
    parentFunction.addChildToChildren(nodeFunctionDeclarationInFunction);

    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclarationInFunction));
  }
}