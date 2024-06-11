import static org.junit.Assert.*;
import static com.google.javascript.jscomp.jsonml.NodeUtil.*;

import com.google.common.base.Preconditions;
import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = new Node(Token.FOR, new int[]{}, new int[3]);
    assertTrue(isForIn(nodeForIn));

    Node nodeNotForIn = new Node(Token.IF, new int[]{}, new int[1]);
    assertFalse(isForIn(nodeNotForIn));
  }

  @Test
  public void testIsStatement() {
    Node nodeScript = new Node(Token.SCRIPT, new int[]{}, new int[0]);
    assertTrue(isStatement(nodeScript));

    Node nodeExpression = new Node(Token.IDENT, new int[]{}, new int[0]);
    assertFalse(isStatement(nodeExpression));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunction = new Node(Token.FUNCTION, new int[]{"foo"}, new int[1]);
    nodeFunction.setProperty("name", "foo");
    assertTrue(isFunctionDeclaration(nodeFunction));

    Node nodeFunctionExpression = new Node(Token.FUNCTION, new int[]{}, new int[2]);
    assertFalse(isFunctionDeclaration(nodeFunctionExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeScript = new Node(Token.SCRIPT, new int[]{}, new int[0]);
    Node nodeFunctionDeclaration = new Node(Token.FUNCTION, new int[]{"foo"}, new int[1]);
    nodeFunctionDeclaration.setProperty("name", "foo");
    nodeFunctionDeclaration.setParent(nodeScript);
    assertTrue(isHoistedFunctionDeclaration(nodeFunctionDeclaration));

    Node nodeFunction = new Node(Token.FUNCTION, new int[]{"foo"}, new int[1]);
    nodeFunction.setProperty("name", "foo");
    Node nodeBlock = new Node(Token.BLOCK, new int[]{}, new int[1]);
    nodeFunction.setParent(nodeBlock);
    nodeBlock.setParent(nodeScript);
    assertFalse(isHoistedFunctionDeclaration(nodeFunction));
  }
}