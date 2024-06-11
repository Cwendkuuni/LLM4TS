import static org.junit.Assert.*;
import static com.google.javascript.jscomp.jsonml.NodeUtil.*;

import com.google.common.base.Preconditions;
import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = new Node(Token.FOR, 1, new int[]{}, new int[]{3, 3, 1});
    Node nodeNotForIn = new Node(Token.IF, 1, new int[]{}, new int[]{3, 1, 1});

    assertTrue(isForIn(nodeForIn));
    assertFalse(isForIn(nodeNotForIn));
  }

  @Test
  public void testIsStatement() {
    Node nodeScript = new Node(Token.SCRIPT, 1, new int[]{}, new int[]{1, 1, 1});
    Node nodeStatement = new Node(Token.IF, 1, new int[]{}, new int[]{3, 1, 1});
    Node nodeExpression = new Node(Token.FUNCTION, 1, new int[]{}, new int[]{3, 1, 1});
    Node nodeBlock = new Node(Token.BLOCK, 1, new int[]{}, new int[]{1, 1, 1});
    Node nodeLabel = new Node(Token.LABEL, 1, new int[]{}, new int[]{1, 1, 1});
    Node nodeFunction = new Node(Token.FUNCTION, 1, new int[]{}, new int[]{3, 1, 1});

    assertTrue(isStatement(nodeScript));
    assertTrue(isStatement(nodeStatement));
    assertFalse(isStatement(nodeExpression));
    assertTrue(isStatement(nodeBlock));
    assertTrue(isStatement(nodeLabel));
    assertFalse(isStatement(nodeFunction));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = new Node(Token.FUNCTION, 1, new int[]{"id"}, new int[]{3, 3, 1});
    Node nodeFunctionExpression = new Node(Token.FUNCTION, 1, new int[]{}, new int[]{3, 1, 1});

    assertTrue(isFunctionDeclaration(nodeFunctionDeclaration));
    assertFalse(isFunctionDeclaration(nodeFunctionExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeScript = new Node(Token.SCRIPT, 1, new int[]{}, new int[]{1, 1, 1});
    Node nodeFunctionDeclaration = new Node(Token.FUNCTION, 1, new int[]{"id"}, new int[]{3, 3, 1});
    Node nodeFunctionDeclarationInFunction = new Node(
        Token.FUNCTION, 1, new int[]{"id1", "id2"}, new int[]{3, 3, 1, 3, 3, 1});
    Node nodeFunctionExpressionInFunction = new Node(
        Token.FUNCTION, 1, new int[]{}, new int[]{3, 1, 1, 3, 1, 1});

    assertFalse(isHoistedFunctionDeclaration(nodeFunctionDeclaration));
    assertTrue(isHoistedFunctionDeclaration(nodeFunctionDeclarationInFunction));
    assertFalse(isHoistedFunctionDeclaration(nodeFunctionExpression));
    assertFalse(isHoistedFunctionDeclaration(nodeFunctionExpressionInFunction));
    assertFalse(isHoistedFunctionDeclaration(nodeScript));
  }
}