import static org.junit.Assert.*;
import com.google.javascript.rhino.Node;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node node = new Node("for init; for condition; for increment");
    assertTrue(NodeUtil.isForIn(node));
  }

  @Test
  public void testIsStatement() {
    Node node = new Node("statement1;");
    assertTrue(NodeUtil.isStatement(node));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node node = new Node("function functionName() { /* function body */ }");
    assertTrue(NodeUtil.isFunctionDeclaration(node));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node node = new Node("function functionName() { /* function body */ }");
    Node parentNode = new Node("function outerFunctionName() { functionName(); }");
    node.setParent(parentNode);
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(node));
  }

  @Test
  public void testIsForIn_NotForIn() {
    Node node = new Node("if (condition) { /* code */ }");
    assertFalse(NodeUtil.isForIn(node));
  }

  @Test
  public void testIsStatement_NotStatement() {
    Node node = new Node("/* comment */");
    assertFalse(NodeUtil.isStatement(node));
  }

  @Test
  public void testIsFunctionDeclaration_NotFunctionDeclaration() {
    Node node = new Node("variable = 10;");
    assertFalse(NodeUtil.isFunctionDeclaration(node));
  }

  @Test
  public void testIsFunctionDeclaration_MissingParent() {
    Node node = new Node("function functionName() { /* function body */ }");
    node.setParent(null);
    assertFalse(NodeUtil.isFunctionDeclaration(node));
  }

  @Test
  public void testIsHoistedFunctionDeclaration_NotHoisted() {
    Node node = new Node("function functionName() { /* function body */ }");
    Node parentNode = new Node("statement1; functionName();");
    node.setParent(parentNode);
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(node));
  }

  @Test
  public void testIsHoistedFunctionDeclaration_MissingParent() {
    Node node = new Node("function functionName() { /* function body */ }");
    node.setParent(null);
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(node));
  }
}