import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

import com.google.javascript.jscomp.jsonml.NodeUtil;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = new Node(Token.FOR, null, null, null, 0, null, null, 0, 0);
    Node nodeNotForIn = new Node(Token.IF, null, null, null, 0, null, null, 0, 0);

    assertTrue(NodeUtil.isForIn(nodeForIn));
    assertFalse(NodeUtil.isForIn(nodeNotForIn));
  }

  @Test
  public void testIsStatement() {
    Node nodeScript = new Node(Token.SCRIPT, null, null, null, 0, null, null, 0, 0);
    Node nodeBlock = new Node(Token.BLOCK, null, null, null, 0, null, null, 0, 0);
    Node nodeLabel = new Node(Token.LABEL, null, null, null, 0, null, null, 0, 0);
    Node nodeFunction = new Node(Token.FUNCTION, null, null, null, 0, null, null, 0, 0);
    Node nodeExpression = new Node(Token.IDENT, null, null, null, 0, null, null, 0, 0);

    assertTrue(NodeUtil.isStatement(nodeScript));
    assertTrue(NodeUtil.isStatement(nodeBlock));
    assertTrue(NodeUtil.isStatement(nodeLabel));
    assertFalse(NodeUtil.isStatement(nodeFunction));
    assertFalse(NodeUtil.isStatement(nodeExpression));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = new Node(Token.FUNCTION, null, "functionName", null, 0, null, null, 0, 0);
    Node nodeFunctionExpression = new Node(Token.FUNCTION, null, null, null, 0, null, null, 0, 0);

    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));
    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeFunctionDeclarationAtRoot = new Node(Token.FUNCTION, null, "functionName", null, 0, null, null, 0, 0);
    Node nodeFunctionDeclarationWithinFunction = new Node(Token.FUNCTION, null, "functionName", null, 0, null, null, 0, 0);

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclarationAtRoot));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclarationWithinFunction));
  }

}