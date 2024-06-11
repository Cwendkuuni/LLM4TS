import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = Node.newNode(Token.FOR, new int[] {1, 2, 3});
    assertTrue(NodeUtil.isForIn(nodeForIn));

    Node nodeNotForIn = Node.newNode(Token.FUNCTION, new int[] {0});
    assertFalse(NodeUtil.isForIn(nodeNotForIn));
  }

  @Test
  public void testIsStatement() {
    Node nodeScript = Node.newNode(Token.SCRIPT, new int[] {});
    assertTrue(NodeUtil.isStatement(nodeScript));

    Node nodeExpression = Node.newNode(Token.ADD, new int[] {1, 2});
    assertFalse(NodeUtil.isStatement(nodeExpression));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunction = Node.newNode(
        Token.FUNCTION, new int[] {1, Token.IDENT, "myFunction", Token.LBRACE, Token.RBRACE});
    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunction));

    Node nodeFunctionExpression = Node.newNode(
        Token.FUNCTION, new int[] {1, Token.IDENT, "myFunction", Token.LPAREN, Token.RPAREN, Token.LBRACE, Token.RBRACE});
    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeScriptWithFunction = Node.newNode(
        Token.SCRIPT, new int[] {
            1, Token.FUNCTION, new int[] {1, Token.IDENT, "myFunction", Token.LBRACE, Token.RBRACE},
            null,
            1, Token.LPAREN, null, Token.RPAREN
        });
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeScriptWithFunction.getFirstChild()));

    Node nodeFunctionInFunction = Node.newNode(
        Token.FUNCTION, new int[] {1, Token.IDENT, "outerFunction", Token.LBRACE,
            Node.newNode(
                Token.FUNCTION, new int[] {1, Token.IDENT, "myFunction", Token.LBRACE, Token.RBRACE}),
            null,
            1, Token.RPAREN
        });
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionInFunction.getFirstChild(1)));

    Node nodeFunctionNotInScript = Node.newNode(
        Token.FUNCTION, new int[] {1, Token.IDENT, "myFunction", Token.LBRACE, Token.RBRACE},
        Node.newNode(Token.SCRIPT, new int[] {}));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionNotInScript));
  }
}