import static org.junit.Assert.*;
import static com.google.javascript.jscomp.jsonml.NodeUtil.*;

import com.google.common.base.Preconditions;
import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = new Node(Token.FOR, new int[]{}, new int[3]);
    assertTrue(isForIn(nodeForIn));

    Node nodeNotForIn = new Node(Token.IF, new int[]{}, new int[0]);
    assertFalse(isForIn(nodeNotForIn));
  }

  @Test
  public void testIsStatement() {
    Node nodeStatement = new Node(Token.IF, new int[]{}, new int[0]);
    Node parentScript = new Node(Token.SCRIPT, new int[]{}, new int[0]);
    parentScript.setFirstChild(nodeStatement);

    assertTrue(isStatement(nodeStatement));

    Node nodeExpression = new Node(Token.IF, new int[]{}, new int[1]);
    assertFalse(isStatement(nodeExpression));
  }

  @Test(expected = AssertionError.class)
  public void testCheckStateParentIsNull() {
    Node node = new Node(Token.IF, new int[]{}, new int[0]);
    assertFalse(isStatement(node));
    Preconditions.checkState(node.getParent() != null);
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = new Node(Token.FUNCTION, new int[]{Token.ID, Token.LEFT_PAREN, Token.RIGHT_PAREN, Token.BLOCK}, new int[5]);
    Node parentBlock = new Node(Token.BLOCK, new int[]{}, new int[0]);
    parentBlock.setFirstChild(nodeFunctionDeclaration);

    assertTrue(isFunctionDeclaration(nodeFunctionDeclaration));

    Node nodeFunctionExpression = new Node(Token.FUNCTION, new int[]{Token.ID, Token.LEFT_PAREN, Token.RIGHT_PAREN}, new int[3]);
    Node parentExpression = new Node(Token.BINARY_OPERATOR, new int[]{Token.PLUS}, new int[2]);
    parentExpression.setLeftChild(nodeFunctionExpression);

    assertFalse(isFunctionDeclaration(nodeFunctionExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeHoistedFunctionDeclaration = new Node(Token.FUNCTION, new int[]{Token.ID, Token.LEFT_PAREN, Token.RIGHT_PAREN, Token.BLOCK}, new int[5]);
    Node parentScript = new Node(Token.SCRIPT, new int[]{}, new int[0]);
    parentScript.setFirstChild(nodeHoistedFunctionDeclaration);

    assertTrue(isHoistedFunctionDeclaration(nodeHoistedFunctionDeclaration));

    Node nodeFunctionDeclaration = new Node(Token.FUNCTION, new int[]{Token.ID, Token.LEFT_PAREN, Token.RIGHT_PAREN, Token.BLOCK}, new int[5]);
    Node parentFunction = new Node(Token.FUNCTION, new int[]{Token.ID, Token.LEFT_PAREN, Token.RIGHT_PAREN, Token.BLOCK}, new int[4]);
    parentFunction.setFirstChild(nodeFunctionDeclaration);

    assertFalse(isHoistedFunctionDeclaration(nodeFunctionDeclaration));
  }
}