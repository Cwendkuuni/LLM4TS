import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.Node;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = Node.newForIn(1, 2, 3);
    assertTrue(NodeUtil.isForIn(nodeForIn));

    Node nodeNotForIn = Node.newExpression(1);
    assertFalse(NodeUtil.isForIn(nodeNotForIn));
  }

  @Test
  public void testIsStatement() {
    Node nodeStatement = Node.newExpressionStatement(Node.newIdentifier("x"));
    assertTrue(NodeUtil.isStatement(nodeStatement));

    Node nodeNotStatement = Node.newIdentifier("x");
    assertFalse(NodeUtil.isStatement(nodeNotStatement));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = Node.newFunction(
        1, "myFunction", new Node[0], Node.newBlock(new Node[0]));
    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));

    Node nodeFunctionExpression = Node.newExpressionFunction(
        1, "myFunction", new Node[0], Node.newBlock(new Node[0]));
    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeHoistedFunctionDeclaration = Node.newFunction(
        1, "myFunction", new Node[0], Node.newBlock(new Node[0]));
    Node parentScript = Node.newScript(new Node[0]);
    parentScript.addChild(nodeHoistedFunctionDeclaration);
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeHoistedFunctionDeclaration));

    Node nodeFunctionDeclarationInFunction = Node.newFunction(
        1, "myFunction", new Node[0], Node.newBlock(new Node[0]));
    Node parentFunction = Node.newExpressionFunction(
        1, "myParentFunction", new Node[0], Node.newBlock(new Node[0]));
    parentFunction.addChild(nodeFunctionDeclarationInFunction);
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclarationInFunction));
  }

  @Test
  public void testIsForInWithEmptyStatements() {
    Node nodeForIn = Node.newForIn(
        1, Node.newIdentifier("i"), Node.newName("myArray"), Node.newBlock(
            Node.newExpressionStatement(Node.newIdentifier("x")),
            Node.newExpressionStatement(Node.newIdentifier("y"))));
    assertTrue(NodeUtil.isForIn(nodeForIn));
  }

  @Test
  public void testIsStatementWithEmptyStatement() {
    Node nodeStatement = Node.newExpressionStatement(Node.newIdentifier("x"));
    Node emptyStatement = Node.newExpressionStatement(Node.newIdentifier(""));
    Node parent = Node.newBlock(new Node[] {nodeStatement, emptyStatement});
    assertTrue(NodeUtil.isStatement(nodeStatement));
    assertTrue(NodeUtil.isStatement(parent));
  }

  @Test(expected = AssertionError.class)
  public void testThrowAssertionError() {
    Node node = Node.newExpression(1);
    NodeUtil.isFunctionDeclaration(node);
  }

  @Test(expected = AssertionError.class)
  public void testThrowAssertionErrorParentNull() {
    Node node = Node.newExpression(1);
    NodeUtil.isStatement(node);
    Node parent = null;
    NodeUtil.isStatement(node, parent);
  }
}