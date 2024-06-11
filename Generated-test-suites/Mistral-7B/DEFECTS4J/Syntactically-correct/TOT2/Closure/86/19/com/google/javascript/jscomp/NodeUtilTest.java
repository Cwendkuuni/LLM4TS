import org.junit.Assert;
import org.junit.Test;
import com.google.javascript.rhino.Node;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node forNode = new Node(Token.FOR);
    forNode.setChild(0, new Node(Token.IDENTIFIER, "a"));
    forNode.setChild(1, new Node(Token.KEYWORD, "in"));
    forNode.setChild(2, new Node(Token.IDENTIFIER, "arr"));

    Assert.assertTrue(NodeUtil.isForIn(forNode));

    Node invalidForNode = new Node(Token.FOR);
    Assert.assertFalse(NodeUtil.isForIn(invalidForNode));
  }

  @Test
  public void testIsStatement() {
    Node statementNode = new Node(Token.IF);
    statementNode.setParent(new Node(Token.BLOCK));

    Assert.assertTrue(NodeUtil.isStatement(statementNode));

    Node expressionNode = new Node(Token.IF);
    Assert.assertFalse(NodeUtil.isStatement(expressionNode));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node functionDeclarationNode = new Node(Token.FUNCTION, "myFunction");
    functionDeclarationNode.setParent(new Node(Token.BLOCK));

    Assert.assertTrue(NodeUtil.isFunctionDeclaration(functionDeclarationNode));

    Node functionExpressionNode = new Node(Token.FUNCTION, "myFunction");
    Assert.assertFalse(NodeUtil.isFunctionDeclaration(functionExpressionNode));
  }

  @Test(expected = AssertionError.class)
  public void testNullParentNodeInIsFunctionDeclaration() {
    Node functionDeclarationNode = new Node(Token.FUNCTION, "myFunction");

    functionDeclarationNode.setParent(null);

    NodeUtil.isFunctionDeclaration(functionDeclarationNode);
  }
}