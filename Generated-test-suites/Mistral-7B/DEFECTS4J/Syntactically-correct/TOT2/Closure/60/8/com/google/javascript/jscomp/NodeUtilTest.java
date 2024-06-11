import static org.junit.Assert.assertEquals;
import static com.google.javascript.rhino.NodeFactory.createNode;

public class NodeUtilTest {
  @Test
  public void testIsForIn() {
    Node forNode = createNode(Token.FOR, null, null, null, null);
    Node notForNode = createNode(Token.IF, null, null, null, null);
    assertEquals(true, NodeUtil.isForIn(forNode));
    assertEquals(false, NodeUtil.isForIn(notForNode));
  }

  @Test
  public void testIsStatement() {
    Node statementNode = createNode(Token.IF, null, null, null, null);
    Node expressionNode = createNode(Token.ASSIGN, null, null, null, null);
    assertEquals(true, NodeUtil.isStatement(statementNode));
    assertEquals(false, NodeUtil.isStatement(expressionNode));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node functionNode = createNode(Token.FUNCTION, "myFunction", null, null, null);
    Node notFunctionNode = createNode(Token.IF, null, null, null, null);
    assertEquals(true, NodeUtil.isFunctionDeclaration(functionNode));
    assertEquals(false, NodeUtil.isFunctionDeclaration(notFunctionNode));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node hoistedFunctionNode = createNode(Token.FUNCTION, "myFunction", null, null, null);
    Node notHoistedFunctionNode = createNode(Token.FUNCTION, "myFunction", null, null, null);
    Node scriptNode = createNode(Token.SCRIPT, null, null, null, null);
    Node functionInsideFunctionNode = createNode(Token.FUNCTION, "myFunction", null, null, hoistedFunctionNode);
    assertEquals(false, NodeUtil.isHoistedFunctionDeclaration(hoistedFunctionNode));
    assertEquals(false, NodeUtil.isHoistedFunctionDeclaration(notHoistedFunctionNode));
    assertEquals(true, NodeUtil.isHoistedFunctionDeclaration(functionInsideFunctionNode));
  }

  @Test(expected = AssertionError.class)
  public void testIsForInWithNullNode() {
    Node nullNode = null;
    NodeUtil.isForIn(nullNode);
  }

  @Test
  public void testIsForInWithEmptyNode() {
    Node emptyNode = createNode(Token.EMPTY, null, null, null, null);
    assertFalse(NodeUtil.isForIn(emptyNode));
  }

  @Test
  public void testIsStatementWithInvalidNodeType() {
    Node variableNode = createNode(Token.VAR, "myVariable", null, null, null);
    assertFalse(NodeUtil.isStatement(variableNode));
  }

  @Test
  public void testIsFunctionDeclarationWithInvalidNodeType() {
    Node assignmentNode = createNode(Token.ASSIGN, null, null, null, null);
    assertFalse(NodeUtil.isFunctionDeclaration(assignmentNode));
  }

  @Test
  public void testIsHoistedFunctionDeclarationWithInvalidNodeType() {
    Node variableNode = createNode(Token.VAR, "myVariable", null, null, null);
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(variableNode));
  }
}