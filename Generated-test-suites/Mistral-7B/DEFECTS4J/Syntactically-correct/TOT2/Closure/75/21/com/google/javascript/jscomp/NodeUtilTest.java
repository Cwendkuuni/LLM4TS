import static org.junit.Assert.*;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = new Node(Token.FOR, new int[]{1, 2, 3});
    assertTrue(NodeUtil.isForIn(nodeForIn));

    Node nodeNotForIn = new Node(Token.IF, new int[]{1, 2, 3});
    assertFalse(NodeUtil.isForIn(nodeNotForIn));

    Node nodeNull = null;
    assertFalse(NodeUtil.isForIn(nodeNull));

    Node nodeNotNode = new Node(Token.NUMBER, new int[]{1, 2, 3});
    assertFalse(NodeUtil.isForIn(nodeNotNode));
  }

  // ... (Add the remaining test methods here)

  @Test
  public void testIsStatement() {
    Node nodeStatement = new Node(Token.PRINT, new int[]{1, 2, 3});
    assertTrue(NodeUtil.isStatement(nodeStatement));

    Node nodeExpression = new Node(Token.ADD, new int[]{1, 2, 3});
    assertFalse(NodeUtil.isStatement(nodeExpression));

    Node nodeNull = null;
    assertFalse(NodeUtil.isStatement(nodeNull));

    Node nodeNotNode = new Node(Token.NUMBER, new int[]{1, 2, 3});
    assertFalse(NodeUtil.isStatement(nodeNotNode));
  }

  // ... (Add the remaining test methods here)

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = new Node(Token.FUNCTION, new int[]{1, 2, 3}, null);
    nodeFunctionDeclaration.setProperty("name", "myFunction");
    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));

    Node nodeFunctionExpression = new Node(Token.FUNCTION, new int[]{1, 2, 3}, new Node[0]);
    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));

    Node nodeNull = null;
    assertFalse(NodeUtil.isFunctionDeclaration(nodeNull));

    Node nodeNotNode = new Node(Token.NUMBER, new int[]{1, 2, 3});
    assertFalse(NodeUtil.isFunctionDeclaration(nodeNotNode));
  }

  // ... (Add the remaining test methods here)

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeFunctionDeclarationInScript = new Node(Token.FUNCTION, new int[]{1, 2, 3}, null);
    nodeFunctionDeclarationInScript.setProperty("name", "myFunction");
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclarationInScript));

    Node nodeFunctionDeclarationInFunction = new Node(Token.FUNCTION, new int[]{1, 2, 3}, new Node[0]);
    nodeFunctionDeclarationInFunction.setProperty("name", "myFunction");
    Node parentFunction = new Node(Token.FUNCTION, new int[]{1, 2, 3}, new Node[0]);
    parentFunction.setProperty("name", "outerFunction");
    nodeFunctionDeclarationInFunction.setParent(parentFunction);
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclarationInFunction));

    Node nodeNull = null;
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeNull));

    Node nodeNotNode = new Node(Token.NUMBER, new int[]{1, 2, 3});
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeNotNode));
  }

  // ... (Add the remaining test methods here)
}