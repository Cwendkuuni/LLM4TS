import static org.junit.Assert.*;
import static com.google.javascript.jscomp.jsonml.NodeUtil.*;
import com.google.common.base.Preconditions;
import com.google.javascript.rhino.Node;

import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = Node.newNode(Token.FOR, new int[] { Token.FOR, Token.IDENT, Token.KEYWORD, Token.SEMICOLON, Token.Ident("in"), Token.SEMICOLON, Token.Ident("item"), Token.SEMICOLON, Token.LEFT_BRACE, new int[]{}, Token.RIGHT_BRACE });
    assertTrue(isForIn(nodeForIn));

    Node nodeNotForIn = Node.newNode(Token.FUNCTION, new int[] { Token.FUNCTION, Token.IDENT, Token.LEFT_PAREN, Token.RIGHT_PAREN, Token.LEFT_BRACE, new int[]{}, Token.RIGHT_BRACE });
    assertFalse(isForIn(nodeNotForIn));
  }

  @Test
  public void testIsStatement() {
    Node nodeStatement = Node.newNode(Token.IF, new int[] { Token.IF, Token.LPAREN, Token.IDENT, Token.RPAREN, Token.LEFT_BRACE, new int[]{}, Token.RIGHT_BRACE });
    assertTrue(isStatement(nodeStatement));

    Node nodeExpression = Node.newNode(Token.IDENT, new int[] { Token.IDENT, Token.LPAREN, Token.RPAREN });
    assertFalse(isStatement(nodeExpression));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = Node.newNode(Token.FUNCTION, new int[] { Token.FUNCTION, Token.IDENT, Token.LPAREN, new int[]{}, Token.RIGHT_PAREN, Token.LEFT_BRACE, new int[]{}, Token.RIGHT_BRACE });
    assertTrue(isFunctionDeclaration(nodeFunctionDeclaration));

    Node nodeFunctionExpression = Node.newNode(Token.IDENT, new int[] { Token.IDENT, Token.LPAREN, new int[]{}, Token.RPAREN });
    assertFalse(isFunctionDeclaration(nodeFunctionExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeHoistedFunctionDeclaration = Node.newNode(Token.FUNCTION, new int[] { Token.FUNCTION, Token.IDENT, Token.LPAREN, new int[]{}, Token.RIGHT_PAREN, Token.SEMICOLON, Token.LEFT_BRACE, new int[]{}, Token.RIGHT_BRACE });
    Node parentNode = Node.newNode(Token.SCRIPT, new int[] { Token.SCRIPT, new int[]{} });
    nodeHoistedFunctionDeclaration.setParent(parentNode);
    assertTrue(isHoistedFunctionDeclaration(nodeHoistedFunctionDeclaration));

    Node nodeNotHoistedFunctionDeclaration = Node.newNode(Token.FUNCTION, new int[] { Token.FUNCTION, Token.IDENT, Token.LPAREN, new int[]{}, Token.RIGHT_PAREN, Token.SEMICOLON, Token.LEFT_BRACE, new int[]{}, Token.RIGHT_BRACE });
    Node parentNodeNotHoistedFunctionDeclaration = Node.newNode(Token.BLOCK, new int[] { Token.BLOCK, new int[]{} });
    nodeNotHoistedFunctionDeclaration.setParent(parentNodeNotHoistedFunctionDeclaration);
    assertFalse(isHoistedFunctionDeclaration(nodeNotHoistedFunctionDeclaration));
  }

  @Test
  public void testPreconditionsFail() {
    Node nodeParentIsNull = Node.newNode(Token.IDENT, new int[] { Token.IDENT, Token.LPAREN, Token.RPAREN });
    assertFalse(isStatement(nodeParentIsNull));
    Preconditions.checkState(nodeParentIsNull.getParent() != null);
  }
}