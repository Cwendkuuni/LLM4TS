import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.Node;
import org.junit.Assert;
import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node forNode = Node.newForStatement();
    Assert.assertTrue(NodeUtil.isForIn(forNode));

    Node notForNode = Node.newExpressionStatement(Node.newName("x"));
    Assert.assertFalse(NodeUtil.isForIn(notForNode));
  }

  @Test
  public void testIsStatement() {
    Node statementNode = Node.newExpressionStatement(Node.newName("x"));
    Node notStatementNode = Node.newIdentifier("x");
    Assert.assertTrue(NodeUtil.isStatement(statementNode.getParent()));
    Assert.assertFalse(NodeUtil.isStatement(notStatementNode));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node functionNode = Node.newFunction(Node.newName("foo"), new Node[0], Node.newBlock(new Node[0]));
    Assert.assertTrue(NodeUtil.isFunctionDeclaration(functionNode));

    Node functionExpressionNode = Node.newExpressionStatement(Node.newFunction(Node.newName("foo"), new Node[0], Node.newBlock(new Node[0])));
    Assert.assertFalse(NodeUtil.isFunctionDeclaration(functionExpressionNode));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node scriptNode = Node.newScript(new Node[0]);
    Node functionDeclarationNode = Node.newFunction(Node.newName("foo"), new Node[0], Node.newBlock(new Node[0]));
    Node hoistedFunctionDeclarationNode = Node.newFunction(Node.newName("foo"), new Node[0], Node.newBlock(new Node[0]));
    scriptNode.addChildToEnd(functionDeclarationNode);
    scriptNode.addChildToEnd(hoistedFunctionDeclarationNode);

    Assert.assertFalse(NodeUtil.isHoistedFunctionDeclaration(functionDeclarationNode));
    Assert.assertTrue(NodeUtil.isHoistedFunctionDeclaration(hoistedFunctionDeclarationNode));
  }
}