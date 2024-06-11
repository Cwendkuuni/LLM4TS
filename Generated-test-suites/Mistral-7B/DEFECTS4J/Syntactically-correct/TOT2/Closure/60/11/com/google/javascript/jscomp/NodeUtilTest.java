import static org.junit.Assert.*;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node node = new Node("for in statement");
    assertTrue(NodeUtil.isForIn(node));
  }

  @Test
  public void testIsForInFalse() {
    Node node = new Node("if statement");
    assertFalse(NodeUtil.isForIn(node));
  }

  @Test
  public void testIsForInNullNode() {
    assertFalse(NodeUtil.isForIn(null));
  }

  @Test
  public void testIsForInNodeWithOtherTypeThanFor() {
    Node node = new Node("if statement");
    assertFalse(NodeUtil.isForIn(node));
  }

  @Test
  public void testIsForInNodeWithThreeChildrenAndNotFor() {
    Node node = new Node("if statement with three children");
    assertFalse(NodeUtil.isForIn(node));
  }

  @Test
  public void testIsForInNodeWithFourChildrenAndFor() {
    Node node = new Node("for statement with four children");
    assertFalse(NodeUtil.isForIn(node));
  }

  @Test
  public void testIsStatement() {
    Node node = new Node("statement");
    assertTrue(NodeUtil.isStatement(node));
  }

  @Test
  public void testIsStatementFalse() {
    Node node = new Node("expression");
    assertFalse(NodeUtil.isStatement(node));
  }

  @Test
  public void testIsStatementNullParentNode() {
    Node node = new Node("statement");
    assertFalse(NodeUtil.isStatement(node));
  }

  @Test
  public void testIsStatementNodeWithFunctionTypeButNotStatement() {
    Node node = new Node("function expression");
    assertFalse(NodeUtil.isStatement(node));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node node = new Node("function declaration");
    assertTrue(NodeUtil.isFunctionDeclaration(node));
  }

  @Test
  public void testIsFunctionDeclarationFalse() {
    Node node = new Node("expression");
    assertFalse(NodeUtil.isFunctionDeclaration(node));
  }

  @Test
  public void testIsFunctionDeclarationNullParentNode() {
    Node node = new Node("function declaration");
    assertFalse(NodeUtil.isFunctionDeclaration(node));
  }

  @Test
  public void testIsFunctionDeclarationNodeWithOtherTypeThanFunction() {
    Node node = new Node("expression");
    assertFalse(NodeUtil.isFunctionDeclaration(node));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node node = new Node("function declaration in the scope root");
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(node));
  }

  @Test
  public void testIsHoistedFunctionDeclarationFalse() {
    Node node = new Node("function declaration not in the scope root");
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(node));
  }

  @Test
  public void testIsHoistedFunctionDeclarationNodeWithoutAParentNode() {
    Node node = new Node("function declaration without a parent node");
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(node));
  }

  @Test
  public void testIsHoistedFunctionDeclarationNodeParentIsScript() {
    Node node = new Node("function declaration with parent script");
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(node));
  }

  @Test
  public void testIsHoistedFunctionDeclarationNodeParentIsFunction() {
    Node parentNode = new Node("function");
    Node node = new Node("function declaration with parent function");
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(node));
  }
}