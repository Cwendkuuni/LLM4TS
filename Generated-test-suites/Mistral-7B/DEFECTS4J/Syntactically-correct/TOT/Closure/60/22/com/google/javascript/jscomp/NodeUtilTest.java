import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.Node;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node forNode = Node.newForIn();
    assertTrue(NodeUtil.isForIn(forNode));

    Node notForNode = Node.newExpressionStatement(Node.newIdentifier("x"));
    assertFalse(NodeUtil.isForIn(notForNode));
  }

  @Test
  public void testIsStatement() {
    Node statementNode = Node.newExpressionStatement(Node.newIdentifier("x"));
    assertTrue(NodeUtil.isStatement(statementNode));

    Node expressionNode = Node.newExpression("x + y");
    assertFalse(NodeUtil.isStatement(expressionNode));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node functionDeclaration = Node.newFunction(
        Node.newIdentifier("func"),
        Node.newArray(),
        Node.newBlock(Node.newExpressionStatement(Node.newReturn(Node.newNumberLiteral(42)))));
    assertTrue(NodeUtil.isFunctionDeclaration(functionDeclaration));

    Node functionExpression = Node.newExpressionFunction(
        Node.newIdentifier("func"),
        Node.newArray(),
        Node.newBlock(Node.newExpressionStatement(Node.newReturn(Node.newNumberLiteral(42)))));
    assertFalse(NodeUtil.isFunctionDeclaration(functionExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node hoistedFunctionDeclaration = Node.newFunction(
        Node.newIdentifier("func"),
        Node.newArray(),
        Node.newBlock(Node.newExpressionStatement(Node.newReturn(Node.newNumberLiteral(42)))));
    Node scriptNode = Node.newScript(Node.newArray(), Node.newBlock(hoistedFunctionDeclaration));
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(hoistedFunctionDeclaration));

    Node notHoistedFunctionDeclaration = Node.newFunction(
        Node.newIdentifier("func"),
        Node.newArray(),
        Node.newBlock(Node.newExpressionStatement(Node.newReturn(Node.newNumberLiteral(42)))));
    Node functionNode = Node.newExpressionStatement(notHoistedFunctionDeclaration);
    Node scriptWithFunctionNode = Node.newScript(Node.newArray(), Node.newBlock(functionNode));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(notHoistedFunctionDeclaration));
  }
}