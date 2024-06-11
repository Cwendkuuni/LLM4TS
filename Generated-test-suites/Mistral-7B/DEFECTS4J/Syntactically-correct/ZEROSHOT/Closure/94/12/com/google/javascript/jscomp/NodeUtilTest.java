import static org.junit.Assert.*;
import static com.google.javascript.jscomp.jsonml.NodeUtil.*;

import com.google.common.base.Preconditions;
import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeWithThreeChildren = new Node(Token.FOR, new int[]{-1, -1, -1});
    Node nodeWithFourChildren = new Node(Token.FOR, new int[]{-1, -1, -1, -1});

    assertTrue(isForIn(nodeWithThreeChildren));
    assertFalse(isForIn(nodeWithFourChildren));
  }

  @Test
  public void testIsStatement() {
    Node nodeWithParentScript = new Node(Token.SCRIPT, new int[]{-1, -1});
    Node nodeWithParentBlock = new Node(Token.BLOCK, new int[]{-1, -1});
    Node nodeWithParentLabel = new Node(Token.LABEL, new int[]{-1, -1});
    Node nodeWithParentFunction = new Node(Token.FUNCTION, new int[]{-1, -1});
    Node nodeWithParentFunctionExpression = new Node(Token.FUNCTION, new int[]{-1, 1});
    Node nodeWithoutParent = new Node(Token.SCRIPT, new int[]{-1, -1});

    assertTrue(isStatement(nodeWithParentScript.getFirstChild()));
    assertTrue(isStatement(nodeWithParentBlock));
    assertTrue(isStatement(nodeWithParentLabel));
    assertFalse(isStatement(nodeWithParentFunction));
    assertFalse(isStatement(nodeWithParentFunctionExpression));
    assertFalse(isStatement(nodeWithoutParent));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node functionDeclaration = new Node(Token.FUNCTION, new int[]{-1, -1});
    Node functionExpression = new Node(Token.FUNCTION, new int[]{-1, 1});

    assertTrue(isFunctionDeclaration(functionDeclaration));
    assertFalse(isFunctionDeclaration(functionExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node scriptNode = new Node(Token.SCRIPT, new int[]{-1, -1});
    Node functionDeclaration = new Node(Token.FUNCTION, new int[]{-1, -1});
    Node functionDeclarationInFunction = new Node(Token.FUNCTION, new int[]{-1, -1});
    Node functionDeclarationInScript = new Node(Token.FUNCTION, new int[]{-1, -1});
    Node functionExpression = new Node(Token.FUNCTION, new int[]{-1, 1});
    Node functionExpressionInFunction = new Node(Token.FUNCTION, new int[]{-1, 1});
    Node functionExpressionInScript = new Node(Token.FUNCTION, new int[]{-1, 1});

    assertFalse(isHoistedFunctionDeclaration(functionDeclaration));
    assertFalse(isHoistedFunctionDeclaration(functionExpression));
    assertTrue(isHoistedFunctionDeclaration(functionDeclarationInFunction));
    assertFalse(isHoistedFunctionDeclaration(functionDeclarationInScript));
    assertFalse(isHoistedFunctionDeclaration(functionExpressionInFunction));
    assertFalse(isHoistedFunctionDeclaration(functionExpressionInScript));
  }

  @Test(expected = AssertionError.class)
  public void testCheckStateParentIsNull() {
    Node node = new Node(Token.SCRIPT, new int[]{-1, -1});
    isStatement(node);
  }
}