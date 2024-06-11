import static org.junit.Assert.*;
import static com.google.javascript.jscomp.jsonml.NodeUtil.*;
import com.google.javascript.rhino.*;

public class NodeUtilTest {

  @org.junit.Test
  public void testIsForIn() {
    Node node = Context.getCurrentContext().newNode("for (var i in [1, 2, 3]) {}");
    assertTrue(isForIn(node));

    Node invalidNode = Context.getCurrentContext().newNode("if (true) {}");
    assertFalse(isForIn(invalidNode));
  }

  @org.junit.Test
  public void testIsStatement() {
    Node statementNode = Context.getCurrentContext().newNode("{}");
    assertTrue(isStatement(statementNode));

    Node expressionNode = Context.getCurrentContext().newNode("1 + 1");
    assertFalse(isStatement(expressionNode));
  }

  @org.junit.Test
  public void testIsFunctionDeclaration() {
    Node functionDeclarationNode = Context.getCurrentContext().newNode(
        "function myFunction() {}", Context.TOP_LEVEL);
    assertTrue(isFunctionDeclaration(functionDeclarationNode));

    Node functionExpressionNode = Context.getCurrentContext().newNode(
        "var myFunction = function() {}", Context.TOP_LEVEL);
    assertFalse(isFunctionDeclaration(functionExpressionNode));
  }

  @org.junit.Test
  public void testIsHoistedFunctionDeclaration() {
    Node hoistedFunctionDeclarationNode = Context.getCurrentContext().newNode(
        "function myFunction() {}", Context.TOP_LEVEL);
    assertTrue(isHoistedFunctionDeclaration(hoistedFunctionDeclarationNode));

    Node notHoistedFunctionDeclarationNode = Context.getCurrentContext().newNode(
        "function myFunction() {}",
        Context.newNode("function outerFunction() {function myFunction() {}", Context.TOP_LEVEL).getFirstChild());
    assertFalse(isHoistedFunctionDeclaration(notHoistedFunctionDeclarationNode));
  }
}