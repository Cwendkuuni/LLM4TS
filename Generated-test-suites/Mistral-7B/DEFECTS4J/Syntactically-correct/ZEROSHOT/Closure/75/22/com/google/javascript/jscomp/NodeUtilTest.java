import static org.junit.Assert.*;
import static com.google.javascript.rhino.NodeFactory.createToken;
import static com.google.javascript.rhino.NodeFactory.createNode;

import org.junit.Test;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.jsonml.NodeUtil;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeFor = createNode(Token.FOR, null, null, null, null, null, null, null, null, null, null, null);
    Node nodeIf = createNode(Token.IF, null, null, null, null, null, null, null, null, null, null, null);
    Node nodeFunction = createNode(Token.FUNCTION, null, null, null, null, null, null, null, null, null, null, null);

    assertTrue(NodeUtil.isForIn(nodeFor));
    assertFalse(NodeUtil.isForIn(nodeIf));
    assertFalse(NodeUtil.isForIn(nodeFunction));
  }

  @Test
  public void testIsStatement() {
    Node nodeFor = createNode(Token.FOR, null, null, null, null, null, null, null, null, null, null, null);
    Node nodeIf = createNode(Token.IF, null, null, null, null, null, null, null, null, null, null, null);
    Node nodeFunction = createNode(Token.FUNCTION, null, null, null, null, null, null, null, null, null, null, null);
    Node nodeParentScript = createNode(Token.SCRIPT, null, null, null, null, null, null, null, null, null, null, null);
    Node nodeParentBlock = createNode(Token.BLOCK, null, null, null, null, null, null, null, null, null, null, null);
    Node nodeParentLabel = createNode(Token.LABEL, null, null, null, null, null, null, null, null, null, null, null);

    assertTrue(NodeUtil.isStatement(nodeFor));
    assertTrue(NodeUtil.isStatement(nodeIf));
    assertTrue(NodeUtil.isStatement(nodeFunction));
    assertTrue(NodeUtil.isStatement(nodeParentScript));
    assertTrue(NodeUtil.isStatement(nodeParentBlock));
    assertTrue(NodeUtil.isStatement(nodeParentLabel));

    Node nodeToken = createToken(Token.IDENTIFIER, "token", null);
    assertFalse(NodeUtil.isStatement(nodeToken));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFor = createNode(Token.FOR, null, null, null, null, null, null, null, null, null, null, null);
    Node nodeFunction = createNode(Token.FUNCTION, null, null, null, null, null, null, null, null, null, null, null);
    Node nodeFunctionExpression = createNode(Token.FUNCTION, null, null, null, null, null, null, null, null, null, nodeFunction, null);

    assertFalse(NodeUtil.isFunctionDeclaration(nodeFor));
    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunction));
    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeFor = createNode(Token.FOR, null, null, null, null, null, null, null, null, null, null, null);
    Node nodeFunction = createNode(Token.FUNCTION, null, null, null, null, null, null, null, null, null, null, null);
    Node nodeFunctionInScript = createNode(Token.FUNCTION, null, null, null, null, null, null, null, null, null, null, nodeFunction);
    Node nodeFunctionInFunction = createNode(Token.FUNCTION, null, null, null, null, null, null, null, null, null, nodeFunction, null);

    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFor));
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionInScript));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionInFunction));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testPreconditionsCheckStateWithNullParent() {
    Node node = createNode(Token.SCRIPT, null, null, null, null, null, null, null, null, null, null, null);
    NodeUtil.isStatement(node);
  }
}