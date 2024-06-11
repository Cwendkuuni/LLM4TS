import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.Node;

import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = Node.newForInStatement();
    assertTrue(NodeUtil.isForIn(nodeForIn));

    Node nodeIfStatement = Node.newIfStatement();
    assertFalse(NodeUtil.isForIn(nodeIfStatement));
  }

  @Test
  public void testIsStatement() {
    Node nodeStatement = Node.newStatement(Node.newExpressionStatement(Node.newName("x")));
    assertTrue(NodeUtil.isStatement(nodeStatement));

    Node nodeExpression = Node.newExpression(Node.newName("x"));
    assertFalse(NodeUtil.isStatement(nodeExpression));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = Node.newFunctionDeclaration("myFunction", new Node[] {Node.newParameter("p", null, null)}, Node.newBlock(new Node[] {}));
    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));

    Node nodeFunctionExpression = Node.newExpressionStatement(Node.newFunctionExpression(Node.newName("myFunction"), new Node[] {Node.newParameter("p", null, null)}, Node.newBlock(new Node[] {})));
    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeScript = Node.newScript(new Node[] {Node.newFunctionDeclaration("myFunction", new Node[] {Node.newParameter("p", null, null)}, Node.newBlock(new Node[] {}))});
    Node nodeFunction = nodeScript.getChild(0);
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunction));

    Node nodeFunctionInsideFunction = Node.newFunctionDeclaration("myFunction", new Node[] {Node.newParameter("p", null, null)}, Node.newBlock(new Node[] {Node.newFunctionDeclaration("innerFunction", new Node[] {Node.newParameter("p", null, null)}, Node.newBlock(new Node[] {}))}));
    Node nodeInnerFunction = nodeFunctionInsideFunction.getChild(0).getChild(0);
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeInnerFunction));
  }

  @Test
  public void testNullInput() {
    assertNull(NodeUtil.isForIn(null));
    assertNull(NodeUtil.isStatement(null));
    assertNull(NodeUtil.isFunctionDeclaration(null));
    assertNull(NodeUtil.isHoistedFunctionDeclaration(null));
  }

  @Test
  public void testEmptyNode() {
    Node emptyNode = Node.newExpression(Node.SENTINEL);
    assertFalse(NodeUtil.isForIn(emptyNode));
    assertFalse(NodeUtil.isStatement(emptyNode));
    assertFalse(NodeUtil.isFunctionDeclaration(emptyNode));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(emptyNode));
  }
}