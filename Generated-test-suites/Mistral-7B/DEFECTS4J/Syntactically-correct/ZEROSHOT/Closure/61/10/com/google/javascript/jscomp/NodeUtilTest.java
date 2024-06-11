import static org.junit.Assert.*;
import static com.google.javascript.jscomp.jsonml.NodeUtil.*;

import com.google.common.base.Preconditions;
import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node forNode = new Node(Token.FOR, new int[]{}, new int[]{}, new int[0]);
    Node ifNode = new Node(Token.IF, new int[]{}, new int[]{}, new int[0]);

    assertTrue(isForIn(forNode));
    assertFalse(isForIn(ifNode));
  }

  @Test
  public void testIsStatement() {
    Node scriptNode = new Node(Token.SCRIPT, new int[]{}, new int[]{}, new int[0]);
    Node statementNode = new Node(Token.ASSIGN, new int[]{}, new int[]{}, new int[0]);
    Node expressionNode = new Node(Token.IDENT, new int[]{}, new int[]{}, new int[0]);

    assertTrue(isStatement(scriptNode));
    assertTrue(isStatement(statementNode));
    assertFalse(isStatement(expressionNode));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node functionDeclaration = new Node(Token.FUNCTION, new int[]{}, new int[]{}, new int[0]);
    Node functionExpression = new Node(Token.FUNCTION, new int[]{}, new int[]{}, new int[1]);
    functionExpression.setChild(0, new Node(Token.IDENT, new int[]{}, new int[]{}, new int[0]));

    assertTrue(isFunctionDeclaration(functionDeclaration));
    assertFalse(isFunctionDeclaration(functionExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node scriptNode = new Node(Token.SCRIPT, new int[]{}, new int[]{}, new int[0]);
    Node functionDeclaration = new Node(Token.FUNCTION, new int[]{}, new int[]{}, new int[0]);
    Node hoistedFunctionDeclaration = new Node(Token.FUNCTION, new int[]{}, new int[]{}, new int[0]);
    hoistedFunctionDeclaration.setChild(0, new Node(Token.IDENT, new int[]{}, new int[]{}, new int[0]));
    scriptNode.setChild(0, hoistedFunctionDeclaration);

    functionDeclaration.setParent(scriptNode);
    assertTrue(isHoistedFunctionDeclaration(hoistedFunctionDeclaration));
    assertFalse(isHoistedFunctionDeclaration(functionDeclaration));
  }

  @Test(expected = IllegalStateException.class)
  public void testCheckState() {
    Node node = new Node(Token.IDENT, new int[]{}, new int[]{}, new int[0]);
    Node parent = new Node(Token.BLOCK, new int[]{}, new int[]{}, new int[0]);
    node.setParent(parent);
    Preconditions.checkState(parent == null);
  }
}