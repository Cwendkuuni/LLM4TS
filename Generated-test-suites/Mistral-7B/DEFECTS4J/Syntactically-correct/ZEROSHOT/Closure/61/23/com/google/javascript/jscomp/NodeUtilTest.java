import static org.junit.Assert.*;
import static com.google.javascript.jscomp.jsonml.NodeUtil.*;

import com.google.common.base.Preconditions;
import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node n = Node.newNode(Token.FOR, new int[]{}, new int[3]);
    assertTrue(isForIn(n));

    n = Node.newNode(Token.IF, new int[]{}, new int[0]);
    assertFalse(isForIn(n));

    n = Node.newNode(Token.VAR, new int[]{}, new int[1]);
    assertFalse(isForIn(n));
  }

  @Test
  public void testIsStatement() {
    Node n = Node.newNode(Token.SCRIPT, new int[]{Token.SCRIPT, Token.BLOCK}, new int[0]);
    Node script = n.getChild(0);
    Node block = script.getChild(0);

    for (int i = 0; i < block.getChildCount(); i++) {
      Node child = block.getChild(i);
      assertTrue(isStatement(child));
    }

    n = Node.newNode(Token.FUNCTION, new int[]{}, new int[1]);
    Node func = n.getChild(0);
    assertFalse(isStatement(func));
  }

  @Test(expected = AssertionError.class)
  public void testIsStatementNullParent() {
    Node n = Node.newNode(Token.SCRIPT, new int[]{Token.SCRIPT}, new int[0]);
    Node parent = n.getParent();
    Preconditions.checkState(parent != null);
    assertFalse(isStatement(n));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node n = Node.newNode(Token.FUNCTION, new int[]{Token.IDENT, Token.COLON, Token.BLOCK}, new int[3]);
    Node name = n.getChild(0);
    Node body = n.getLastChild();

    Node funcDeclaration = Node.newNode(Token.FUNCTION, new int[]{Token.IDENT, Token.COLON, Token.BLOCK}, new int[3]);
    Node funcName = funcDeclaration.getChild(0);
    Node funcBody = funcDeclaration.getLastChild();

    assertEquals(name.getString(), funcName.getString());
    assertTrue(isFunctionDeclaration(n));
    assertTrue(isFunctionDeclaration(funcDeclaration));

    Node funcExpression = Node.newNode(Token.FUNCTION, new int[]{}, new int[1]);
    Node funcNameExpression = funcExpression.getChild(0);
    Node funcBodyExpression = funcExpression.getLastChild();

    assertFalse(isFunctionDeclaration(funcExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node n = Node.newNode(Token.SCRIPT, new int[]{Token.SCRIPT, Token.FUNCTION, Token.BLOCK}, new int[3]);
    Node functionNode = n.getChild(1);

    assertTrue(isFunctionDeclaration(functionNode));
    assertTrue(isHoistedFunctionDeclaration(functionNode));

    n = Node.newNode(Token.FUNCTION, new int[]{Token.FUNCTION, Token.BLOCK}, new int[2]);
    Node functionInFunctionNode = n.getChild(0);

    assertTrue(isFunctionDeclaration(functionInFunctionNode));
    assertFalse(isHoistedFunctionDeclaration(functionInFunctionNode));
  }
}