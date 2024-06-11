import static com.google.javascript.jscomp.jsonml.NodeUtil.isForIn;
import static com.google.javascript.jscomp.jsonml.NodeUtil.isHoistedFunctionDeclaration;
import static com.google.javascript.jscomp.jsonml.NodeUtil.isStatement;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.Node;
import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node forNode = new Node(Token.FOR, new int[]{}, new int[]{0, 0, 0});
    Node notForNode = new Node(Token.IF, new int[]{}, new int[]{0, 0, 0});

    assertTrue("Expected FOR node to be FOR-IN loop", isForIn(forNode));
    assertFalse("Expected IF node to not be FOR-IN loop", isForIn(notForNode));
  }

  @Test
  public void testIsStatement() {
    Node scriptNode = new Node(Token.SCRIPT, new int[]{}, new int[]{0, 0, 0});
    Node functionNode = new Node(Token.FUNCTION, new int[]{}, new int[]{0, 0, 0});
    Node expressionNode = new Node(Token.IF, new int[]{}, new int[]{0, 0, 0});

    assertTrue("Expected script node to be statement", isStatement(scriptNode));
    assertTrue("Expected function node to be statement if it is a declaration", isStatement(functionNode));
    assertFalse("Expected expression node not to be statement", isStatement(expressionNode));

    // Check function node is statement when it is an expression
    Node functionExpressionNode = new Node(Token.FUNCTION, new int[]{}, new int[]{0, 0, 0}, null);
    assertTrue("Expected function expression node to be statement when it is an expression", isStatement(functionExpressionNode));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node functionDeclaration = new Node(Token.FUNCTION, new int[]{"id"}, new int[]{0, 0, 0}, null);
    Node functionExpression = new Node(Token.FUNCTION, new int[]{}, new int[]{0, 0, 0}, null);
    Node ifStatement = new Node(Token.IF, new int[]{}, new int[]{0, 0, 0});

    assertTrue("Expected function declaration node to be function declaration", isFunctionDeclaration(functionDeclaration));
    assertFalse("Expected function expression node not to be function declaration", isFunctionDeclaration(functionExpression));
    assertFalse("Expected if statement node not to be function declaration", isFunctionDeclaration(ifStatement));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node scriptNode = new Node(Token.SCRIPT, new int[]{}, new int[]{0, 0, 0});
    Node functionDeclaration = new Node(Token.FUNCTION, new int[]{"id"}, new int[]{0, 0, 0}, null);
    Node functionDeclarationInFunction = new Node(Token.FUNCTION, new int[]{}, new int[]{0, 0, 0}, null, functionDeclaration);

    assertFalse("Expected non-hoisted function declaration not to be hoisted", isHoistedFunctionDeclaration(functionDeclaration));
    assertTrue("Expected hoisted function declaration to be hoisted", isHoistedFunctionDeclaration(functionDeclarationInFunction));
  }

  @Test(expected = IllegalStateException.class)
  public void testCheckParentNode() {
    Node node = new Node(Token.FUNCTION, new int[]{}, new int[]{0, 0, 0});
    Preconditions.checkState(node.getParent() != null);
  }
}