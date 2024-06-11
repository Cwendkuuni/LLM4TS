import static org.junit.Assert.*;
import com.google.javascript.rhino.Node;

public class NodeUtilTest {
  @Test
  public void testIsForIn() {
    Node node = new Node(); // Assuming Node has a default constructor
    assertFalse(NodeUtil.isForIn(node));

    node.setType(Token.FOR);
    node.setChildCount(3);
    assertTrue(NodeUtil.isForIn(node));

    Node forNode = new Node();
    forNode.setType(Token.FOR_IN);
    assertFalse(NodeUtil.isForIn(forNode));

    Node forInNode = new Node();
    forInNode.setType(Token.FOR);
    forInNode.setChildCount(2);
    assertFalse(NodeUtil.isForIn(forInNode));
  }

  @Test
  public void testIsStatement() {
    Node node = new Node();
    assertFalse(NodeUtil.isStatement(node));

    Node scriptNode = new Node();
    scriptNode.setType(Token.SCRIPT);
    node.setParent(scriptNode);
    assertTrue(NodeUtil.isStatement(node));

    Node blockNode = new Node();
    blockNode.setType(Token.BLOCK);
    node.setParent(blockNode);
    assertTrue(NodeUtil.isStatement(node));

    Node labelNode = new Node();
    labelNode.setType(Token.LABEL);
    node.setParent(labelNode);
    assertTrue(NodeUtil.isStatement(node));

    Node functionNode = new Node();
    functionNode.setType(Token.FUNCTION_EXPRESSION);
    assertFalse(NodeUtil.isStatement(functionNode));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node node = new Node();
    assertFalse(NodeUtil.isFunctionDeclaration(node));

    Node functionDeclarationNode = new Node();
    functionDeclarationNode.setType(Token.FUNCTION);
    assertTrue(NodeUtil.isFunctionDeclaration(functionDeclarationNode));

    Node functionExpressionNode = new Node();
    functionExpressionNode.setType(Token.FUNCTION_EXPRESSION);
    assertFalse(NodeUtil.isFunctionDeclaration(functionExpressionNode));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node node = new Node();
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(node));

    Node scriptNode = new Node();
    scriptNode.setType(Token.SCRIPT);
    Node functionDeclarationNode = new Node();
    functionDeclarationNode.setType(Token.FUNCTION);
    node.setParent(functionDeclarationNode);
    scriptNode.setParent(node);
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(node));

    Node functionNode = new Node();
    functionNode.setType(Token.FUNCTION);
    functionNode.setParent(new Node().setType(Token.SCRIPT));
    Node hoistedFunctionDeclarationNode = new Node().setType(Token.FUNCTION);
    hoistedFunctionDeclarationNode.setParent(functionNode);
    node.setParent(hoistedFunctionDeclarationNode);
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(node));
  }
}