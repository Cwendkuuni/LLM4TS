import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.jsonml.Node;
import com.google.javascript.rhino.Token;

import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = Node.newNode(Token.FOR, new int[]{}, new int[]{}, null);
    Node nodeFor = Node.newNode(Token.FOR, new int[]{}, new int[]{}, null);

    assertTrue(NodeUtil.isForIn(nodeForIn));
    assertFalse(NodeUtil.isForIn(nodeFor));
  }

  @Test
  public void testIsStatement() {
    Node nodeScript = Node.newNode(Token.SCRIPT, new int[]{}, new int[]{}, null);
    Node nodeStatement = Node.newNode(Token.ASSIGN, new int[]{}, new int[]{}, null);

    assertTrue(NodeUtil.isStatement(nodeScript));
    assertTrue(NodeUtil.isStatement(nodeStatement));

    Node nodeExpression = Node.newNode(Token.IDENT, new int[]{}, new int[]{}, "test");

    assertFalse(NodeUtil.isStatement(nodeExpression));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = Node.newNode(
        Token.FUNCTION, new int[]{-1, -1}, new int[]{Token.IDENT, Token.RIGHT_BRACE},
        "testFunction");

    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));

    Node nodeFunctionExpression = Node.newNode(
        Token.FUNCTION, new int[]{-1, -1, -1}, new int[]{Token.IDENT, Token.LEFT_PAREN, Token.RIGHT_PAREN,
            Token.RIGHT_BRACE}, "testFunction");

    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeScript = Node.newNode(Token.SCRIPT, new int[]{}, new int[]{}, null);
    Node nodeFunctionDeclaration = Node.newNode(
        Token.FUNCTION, new int[]{-1, -1}, new int[]{Token.IDENT, Token.RIGHT_BRACE}, "testFunction");

    Node nodeFunction = Node.newNode(Token.FUNCTION, new int[]{-1, -1}, new int[]{Token.IDENT, Token.LEFT_PAREN,
        Token.RIGHT_PAREN, Token.RIGHT_BRACE}, "testFunction");
    Node nodeFunctionInScript = Node.newNode(nodeFunction, new int[]{}, new int[]{}, null);

    Node nodeFunctionInBlock = Node.newNode(Token.BLOCK, new int[]{Token.FUNCTION}, new int[]{}, null);
    Node nodeFunctionDeclarationInBlock = Node.newNode(nodeFunctionDeclaration, new int[]{}, new int[]{}, null);

    Node nodeFunctionInFunction = Node.newNode(Token.FUNCTION, new int[]{}, new int[]{Token.IDENT, Token.LEFT_PAREN,
        Token.RIGHT_PAREN, Token.RIGHT_BRACE}, "testFunction");
    Node nodeFunctionDeclarationInFunction = Node.newNode(nodeFunctionDeclaration, new int[]{}, new int[]{}, null);

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclaration));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionExpression));

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclarationInScript));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionInScript));

    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclarationInBlock));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionInBlock));

    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclarationInFunction));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionInFunction));
  }
}