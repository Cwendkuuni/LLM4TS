import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.Node;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node forNode = Node.newForStatement(1, null, null, null, null, null);
    Node normalNode = Node.newExpressionStatement(Node.newName("x"));

    assertTrue(NodeUtil.isForIn(forNode));
    assertFalse(NodeUtil.isForIn(normalNode));
  }

  @Test
  public void testIsStatement() {
    Node scriptNode = Node.newScript(null);
    Node statementNode = Node.newExpressionStatement(Node.newName("x"));
    Node expressionNode = Node.newExpression(Node.newName("x")).setType(Token.CALL);

    assertTrue(NodeUtil.isStatement(statementNode));
    assertTrue(NodeUtil.isStatement(scriptNode));
    assertFalse(NodeUtil.isStatement(expressionNode));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node functionDeclaration = Node.newFunction(Node.newName("func"), null, null, null);
    Node functionExpression = Node.newExpressionStatement(Node.newFunction(Node.newName("func"), null, null, null));

    assertTrue(NodeUtil.isFunctionDeclaration(functionDeclaration));
    assertFalse(NodeUtil.isFunctionDeclaration(functionExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node scriptNode = Node.newScript(Node.newFunction(Node.newName("func"), null, null, null));
    Node functionDeclarationInFunction = Node.newFunction(Node.newName("func"), null, null, null).setParent(Node.newFunction(Node.newName("func2"), null, null, null));

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(scriptNode.getFirstChild()));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(functionDeclarationInFunction));
  }

  @Test(expected = AssertionError.class)
  public void testPreconditionsCheckStateFail() {
    Node node = Node.newExpressionStatement(Node.newName("x"));
    Node parentlessNode = Node.newExpressionStatement(Node.newName("x"));
    parentlessNode.setParent(null);

    NodeUtil.isStatement(node);
    NodeUtil.isStatement(parentlessNode);
  }
}