import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import com.google.javascript.rhino.Node;

public class NodeUtilTest {

  @org.junit.Test
  public void testIsForIn() {
    Node node = new Node("for in");
    assertTrue(NodeUtil.isForIn(node));
  }

  @org.junit.Test
  public void testIsForInFalse() {
    Node node = new Node("if");
    assertFalse(NodeUtil.isForIn(node));
  }

  @org.junit.Test
  public void testIsStatement() {
    Node node = new Node("return 5;");
    assertTrue(NodeUtil.isStatement(node));
  }

  @org.junit.Test
  public void testIsStatementFalse() {
    Node node = new Node("5");
    assertFalse(NodeUtil.isStatement(node));
  }

  @org.junit.Test
  public void testIsFunctionDeclaration() {
    Node node = new Node("function foo() {}", 0);
    assertTrue(NodeUtil.isFunctionDeclaration(node));
  }

  @org.junit.Test
  public void testIsFunctionDeclarationFalse() {
    Node node = new Node("return function foo() {}", 0);
    assertFalse(NodeUtil.isFunctionDeclaration(node));
  }

  @org.junit.Test
  public void testIsHoistedFunctionDeclaration() {
    Node node = new Node("function foo() {}", 0);
    Node parent = new Node("function bar() {foo();}", 0);
    node.setParent(parent);
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(node));
  }

  @org.junit.Test
  public void testIsHoistedFunctionDeclarationFalse() {
    Node node = new Node("function foo() {}", 0);
    Node parent = new Node("var foo = function() {}", 0);
    node.setParent(parent);
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(node));
  }

  @org.junit.Test
  public void testIsForInWithNullNode() {
    Node node = null;
    assertFalse(NodeUtil.isForIn(node));
  }

  @org.junit.Test
  public void testIsForInWithEmptyNode() {
    Node node = new Node("");
    assertFalse(NodeUtil.isForIn(node));
  }

  @org.junit.Test
  public void testIsStatementWithNullNode() {
    Node node = null;
    assertFalse(NodeUtil.isStatement(node));
  }

  @org.junit.Test
  public void testIsStatementWithEmptyNode() {
    Node node = new Node("");
    assertFalse(NodeUtil.isStatement(node));
  }

  @org.junit.Test
  public void testIsFunctionDeclarationWithNullNode() {
    Node node = null;
    assertFalse(NodeUtil.isFunctionDeclaration(node));
  }

  @org.junit.Test
  public void testIsFunctionDeclarationWithEmptyNode() {
    Node node = new Node("");
    assertFalse(NodeUtil.isFunctionDeclaration(node));
  }

  @org.junit.Test
  public void testIsFunctionDeclarationWithNonFunctionNode() {
    Node node = new Node("5");
    assertFalse(NodeUtil.isFunctionDeclaration(node));
  }

  @org.junit.Test
  public void testIsHoistedFunctionDeclarationWithNonFunctionNode() {
    Node node = new Node("5");
    Node parent = new Node("function foo() {}", 0);
    node.setParent(parent);
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(node));
  }
}