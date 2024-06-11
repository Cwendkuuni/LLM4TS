import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

import org.junit.Test;

/**
 * Tests for {@link NodeUtil} class.
 */
public class NodeUtilTest {

  /**
   * Test for {@link NodeUtil#isForIn(Node)}.
   */
  @Test
  public void testIsForIn() {
    Node nodeForIn = Node.newForIn();
    assertTrue(NodeUtil.isForIn(nodeForIn));

    Node nodeNotForIn = Node.newExpressionStatement(Node.newName("x"));
    assertFalse(NodeUtil.isForIn(nodeNotForIn));
  }

  /**
   * Test for {@link NodeUtil#isStatement(Node)}.
   */
  @Test
  public void testIsStatement() {
    Node nodeStatement = Node.newExpressionStatement(Node.newName("x"));
    assertTrue(NodeUtil.isStatement(nodeStatement));

    Node nodeNotStatement = Node.newName("x");
    assertFalse(NodeUtil.isStatement(nodeNotStatement));

    Node nodeFunction = Node.newFunction(null, Node.newName("x"), null, Node.newBlock(null));
    assertTrue(NodeUtil.isStatement(nodeFunction));
  }

  /**
   * Test for {@link NodeUtil#isFunctionDeclaration(Node)}.
   */
  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = Node.newFunction(
        Node.newName("myFunction"), null, null, Node.newBlock(null));
    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));

    Node nodeFunctionExpression = Node.newExpressionStatement(
        Node.newFunction(null, Node.newName("myFunction"), null, Node.newBlock(null)));
    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));
  }

  /**
   * Test for {@link NodeUtil#isHoistedFunctionDeclaration(Node)}.
   */
  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeHoistedFunctionDeclaration = Node.newFunction(
        Node.newName("myFunction"), null, null, Node.newBlock(null));
    Node functionNode = Node.newFunction( Node.newName("myFunction"), null, null, Node.newBlock(null) );
    Node scriptNode = Node.newScript(null);
    Node parentNode = Node.newBlock(null);
    parentNode.addChild(functionNode);
    scriptNode.addChild(parentNode);

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeHoistedFunctionDeclaration));
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(functionNode));
  }
}