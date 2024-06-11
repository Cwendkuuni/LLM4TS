import static org.junit.Assert.*;
import static com.google.javascript.jscomp.jsonml.NodeUtil.*;
import com.google.javascript.jscomp.testing.TopLevelTestBase;

public class NodeUtilTest extends TopLevelTestBase {

  public void testIsForIn() {
    Node node = createNode("for (var i in arr) {}", Token.FOR);
    assertTrue(isForIn(node));

    node = createNode("for (var i = 0; i < arr.length; i++) {}", Token.FOR);
    assertFalse(isForIn(node));

    node = createNode("function forInFunc() {}", Token.FUNCTION);
    assertFalse(isForIn(node));
  }

  public void testIsStatement() {
    Node node = createNode("{}");
    assertTrue(isStatement(node));

    node = createNode("1 + 1");
    assertFalse(isStatement(node));
  }

  public void testIsFunctionDeclaration() {
    Node node = createNode("function func() {}", Token.FUNCTION);
    assertTrue(isFunctionDeclaration(node));

    node = createNode("1 + (function() {})()");
    assertFalse(isFunctionDeclaration(node));
  }

  public void testIsHoistedFunctionDeclaration() {
    Node scriptNode = createNode("function foo() {}", Token.SCRIPT);
    Node functionNode = scriptNode.getFirstChild();
    assertTrue(isHoistedFunctionDeclaration(functionNode));

    Node functionExpressionNode = createNode("function foo() {}", Token.FUNCTION_EXPRESSION);
    assertFalse(isHoistedFunctionDeclaration(functionExpressionNode));
  }

  private Node createNode(String code, int type) {
    return parser().parse(code, "test", null).getFirstChild();
  }
}