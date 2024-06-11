import static org.junit.Assert.*;
import static com.google.javascript.jscomp.jsonml.NodeUtil.*;

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
    Node nodeStatement = new Node(Token.IF, new int[]{}, new int[1]);
    assertTrue(isStatement(nodeStatement));

    Node nodeNotStatement = new Node(Token.IDENT, new int[]{}, new int[0]);
    assertFalse(isStatement(nodeNotStatement));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = new Node(Token.FUNCTION, new int[]{"name"}, new int[1]);
    nodeFunctionDeclaration.setChild(0, new Node(Token.IDENT, new int[]{"name"}, new int[0]));
    assertTrue(isFunctionDeclaration(nodeFunctionDeclaration));

    Node nodeFunctionExpression = new Node(Token.FUNCTION, new int[0], new int[1]);
    nodeFunctionExpression.setChild(0, new Node(Token.IDENT, new int[]{}, new int[0]));
    assertFalse(isFunctionDeclaration(nodeFunctionExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeScript = new Node(Token.SCRIPT, new int[]{}, new int[0]);
    Node nodeFunctionDeclaration = new Node(Token.FUNCTION, new int[]{"name"}, new int[1]);
    nodeFunctionDeclaration.setChild(0, new Node(Token.IDENT, new int[]{"name"}, new int[0]));
    nodeScript.setChild(0, nodeFunctionDeclaration);
    assertTrue(isHoistedFunctionDeclaration(nodeFunctionDeclaration));

    Node nodeFunctionDeclarationInFunction = new Node(Token.FUNCTION, new int[]{"foo"}, new int[1]);
    Node nodeScriptInFunction = new Node(Token.SCRIPT, new int[]{}, new int[0]);
    nodeFunctionDeclarationInFunction.setChild(0, new Node(Token.IDENT, new int[]{"name"}, new int[0]));
    nodeScriptInFunction.setChild(0, nodeFunctionDeclarationInFunction);
    Node nodeFunction = new Node(Token.FUNCTION, new int[]{}, new int[1]);
    nodeFunction.setChild(0, nodeScriptInFunction);
    assertFalse(isHoistedFunctionDeclaration(nodeFunctionDeclarationInFunction));
  }
}