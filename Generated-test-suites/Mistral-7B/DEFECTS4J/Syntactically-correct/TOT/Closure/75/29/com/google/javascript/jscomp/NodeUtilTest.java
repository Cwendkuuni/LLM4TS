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

    Node nodeNotForIn = Node.newExpressionStatement(Node.newName("x"));
    assertFalse(NodeUtil.isForIn(nodeNotForIn));
  }

  @Test
  public void testIsStatement() {
    Node nodeStatement = Node.newExpressionStatement(Node.newNumberLiteral(5));
    assertTrue(NodeUtil.isStatement(nodeStatement));

    Node nodeExpression = Node.newExpressionStatement(
        Node.newMemberExpression(Node.newName("x"), "foo"));
    assertFalse(NodeUtil.isStatement(nodeExpression));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = Node.newFunctionDeclaration(
        "myFunction", Node.newArrayLiteral(Node.newName("a")), null,
        Node.newBlock(Node.newExpressionStatement(Node.newReturnStatement(
            Node.newName("x")))));
    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));

    Node nodeFunctionExpression = Node.newExpressionStatement(
        Node.newFunctionExpression(Node.newName("myFunction"),
            Node.newArrayLiteral(Node.newName("a")), null,
            Node.newBlock(Node.newExpressionStatement(
                Node.newReturnStatement(Node.newName("x"))))));
    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeScript = Node.newScript(Node.newReturnStatement(
        Node.newFunctionDeclaration("myFunction", Node.newArrayLiteral(), null,
            Node.newBlock(Node.newExpressionStatement(
                Node.newReturnStatement(Node.newName("x")))))));

    Node nodeFunctionDeclaration = nodeScript.getChild(0);
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclaration));

    Node nodeFunctionDeclarationInFunction = Node.newFunctionDeclaration(
        "myFunction", Node.newArrayLiteral(), null,
        Node.newBlock(Node.newExpressionStatement(
            Node.newReturnStatement(Node.newName("x")))));
    Node nodeFunction = Node.newFunctionExpression(
        "myFunction", Node.newArrayLiteral(), null,
        Node.newBlock(Node.newExpressionStatement(
            Node.newReturnStatement(Node.newName("x")))));
    Node nodeFunctionInScript = Node.newScript(
        Node.newExpressionStatement(nodeFunction));
    Node functionInFunction = nodeFunctionInScript.getChild(0);

    assertFalse(NodeUtil.isHoistedFunctionDeclaration(functionInFunction));
  }
}