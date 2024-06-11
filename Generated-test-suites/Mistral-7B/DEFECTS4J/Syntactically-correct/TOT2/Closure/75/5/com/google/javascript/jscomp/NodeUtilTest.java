import static org.junit.Assert.*;
import com.google.javascript.rhino.Node;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node node = new Node("for (var i in [1, 2, 3]) { }");
    assertTrue(NodeUtil.isForIn(node));
  }

  @Test
  public void testIsForInFalse() {
    Node node = new Node("if (true) {}");
    assertFalse(NodeUtil.isForIn(node));
  }

  @Test
  public void testIsStatement() {
    Node node = new Node("if (true) {}", new Node("System.out.println('Hello')"));
    assertTrue(NodeUtil.isStatement(node.getChild(0)));
  }

  @Test
  public void testIsStatementFalse() {
    Node node = new Node("function myFunction() {}");
    assertFalse(NodeUtil.isStatement(node));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node node = new Node("function myFunction() {}");
    assertTrue(NodeUtil.isFunctionDeclaration(node));
  }

  @Test
  public void testIsFunctionDeclarationFalse() {
    Node node = new Node("function myFunction() { return 5; }");
    assertFalse(NodeUtil.isFunctionDeclaration(node));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node node = new Node("function myFunction() {}", new Token(Token.FUNCTION, 0, 0));
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(node));
  }

  @Test
  public void testIsHoistedFunctionDeclarationFalse() {
    Node node = new Node("function myFunction() {}", new Token(Token.VAR, 0, 0));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(node));
  }
}