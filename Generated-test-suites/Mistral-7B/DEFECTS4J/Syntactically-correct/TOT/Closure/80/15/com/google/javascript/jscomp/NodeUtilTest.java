import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node forNode = Node.newForLoop();
    Node normalNode = Node.newExpressionStatement(Node.newExpression(1));

    assertTrue(NodeUtil.isForIn(forNode));
    assertFalse(NodeUtil.isForIn(normalNode));
  }

  @Test
  public void testIsStatement() {
    Node scriptNode = Node.newBlock(Node.newExpression(1));
    Node expressionNode = Node.newExpressionStatement(Node.newExpression(1));

    assertTrue(NodeUtil.isStatement(scriptNode));
    assertTrue(NodeUtil.isStatement(expressionNode));

    Node functionNode = Node.newFunction(null, null, null, null);

    assertTrue(NodeUtil.isStatement(functionNode)); // Function declarations can be statements
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node functionDeclaration = Node.newFunction(
        "myFunction",
        new String[]{"arg1"},
        null,
        null,
        Node.newBlock(Node.newExpressionStatement(Node.newExpression(1))));

    assertTrue(NodeUtil.isFunctionDeclaration(functionDeclaration));

    Node functionExpression = Node.newExpressionStatement(
        Node.newFunction(null, null, null, Node.newBlock(Node.newExpression(1))));

    assertFalse(NodeUtil.isFunctionDeclaration(functionExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node scriptNode = Node.newBlock(
        Node.newFunction(
            "myFunction",
            new String[]{"arg1"},
            null,
            null,
            Node.newBlock(Node.newExpressionStatement(Node.newExpression(1)))));

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(scriptNode.getChild(0)));

    Node functionNodeInFunction = Node.newFunction(
        "myFunction",
        new String[]{"arg1"},
        null,
        null,
        Node.newBlock(Node.newExpressionStatement(Node.newExpression(1))));

    Node functionInFunction = Node.newFunction(
        null,
        null,
        null,
        Node.newBlock(functionNodeInFunction));

    assertFalse(NodeUtil.isHoistedFunctionDeclaration(functionNodeInFunction));
  }
}