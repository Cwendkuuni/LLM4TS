import static org.junit.Assert.*;
import static com.google.javascript.jscomp.jsonml.NodeUtil.*;

import com.google.common.base.Preconditions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = new Node(Token.FOR, null, null, 0, 0);
    Node nodeNotForIn = new Node(Token.IF, null, null, 0, 0);

    assertTrue(isForIn(nodeForIn));
    assertFalse(isForIn(nodeNotForIn));
  }

  @Test
  public void testIsStatement() {
    Node parentScript = new Node(Token.SCRIPT, null, null, 0, 0);
    Node nodeStatement = new Node(Token.IF, null, null, 0, 0);
    Node nodeExpression = new Node(Token.ADD, null, null, 0, 0);

    Node nodeStatementAsChildOfScript = new Node(Token.IF, parentScript, null, 0, 0);
    Node nodeExpressionAsChildOfScript = new Node(Token.ADD, parentScript, null, 0, 0);

    assertTrue(isStatement(nodeStatement));
    assertTrue(isStatement(nodeStatementAsChildOfScript));
    assertFalse(isStatement(nodeExpression));
    assertFalse(isStatement(nodeExpressionAsChildOfScript));
  }

  @Test(expected = AssertionError.class)
  public void testCheckStateParentIsNull() {
    Node node = new Node(Token.IF, null, null, 0, 0);
    isStatement(node);
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = new Node(Token.FUNCTION, null, null, 0, 0);
    Node nodeStatement = new Node(Token.IF, null, null, 0, 0);
    Node nodeFunctionExpression = new Node(Token.FUNCTION, null, null, 0, 0);

    assertTrue(isFunctionDeclaration(nodeFunctionDeclaration));
    assertFalse(isFunctionDeclaration(nodeStatement));
    assertFalse(isFunctionDeclaration(nodeFunctionExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeFunctionDeclaration = new Node(Token.FUNCTION, null, null, 0, 0);
    Node nodeFunctionDeclarationAsChildOfScript = new Node(Token.FUNCTION, new Node(Token.SCRIPT, null, null, 0, 0), null, 0, 0);
    Node nodeFunctionDeclarationAsChildOfFunction = new Node(Token.FUNCTION, new Node(Token.FUNCTION, null, null, 0, 0), null, 0, 0);

    assertFalse(isHoistedFunctionDeclaration(nodeFunctionDeclaration));
    assertFalse(isHoistedFunctionDeclaration(nodeFunctionDeclarationAsChildOfFunction));
    assertTrue(isHoistedFunctionDeclaration(nodeFunctionDeclarationAsChildOfScript));
  }
}