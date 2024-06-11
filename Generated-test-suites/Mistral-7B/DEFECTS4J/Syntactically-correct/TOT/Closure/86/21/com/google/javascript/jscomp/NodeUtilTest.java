import static org.junit.Assert.*;
import static com.google.javascript.jscomp.jsonml.NodeUtil.*;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.testing.TopLevelTestBase;
import com.google.javascript.rhino.Node;

public class NodeUtilTest extends TopLevelTestBase {

  /**
   * Test for isForIn method.
   */
  @Test
  public void testIsForIn() {
    Node node = createNode("for (var i in array) {}");
    assertTrue(isForIn(node));

    node = createNode("for (var i = 0; i < array.length; i++) {}");
    assertFalse(isForIn(node));

    node = createNode("for (var i = 0; i < array.length; ) {}"); // incomplete
    assertFalse(isForIn(node));
  }

  /**
   * Test for isStatement method.
   */
  @Test
  public void testIsStatement() {
    Node node = createNode("{}");
    assertTrue(isStatement(node));

    node = createNode("function foo() {}");
    assertTrue(isStatement(node));

    node = createNode("1 + 1");
    assertFalse(isStatement(node));
  }

  /**
   * Test for isFunctionDeclaration method.
   */
  @Test
  public void testIsFunctionDeclaration() {
    Node node = createNode("function foo() {}");
    assertTrue(isFunctionDeclaration(node));

    node = createNode("function foo(bar) {}");
    assertTrue(isFunctionDeclaration(node));

    node = createNode("function foo() { return 42; }");
    assertTrue(isFunctionDeclaration(node));

    node = createNode("var foo = function() {}"); // function expression
    assertFalse(isFunctionDeclaration(node));
  }

  /**
   * Test for isHoistedFunctionDeclaration method.
   */
  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node scriptNode = createNode("function foo() {}");
    Node functionDeclaration = scriptNode.getFirstChild();
    assertTrue(isHoistedFunctionDeclaration(functionDeclaration));

    Node blockNode = createNode("function bar() {}");
    Node functionDeclarationInBlock = blockNode.getFirstChild();
    Node parent = functionDeclarationInBlock.getParent();
    assertFalse(isHoistedFunctionDeclaration(functionDeclarationInBlock));
    assertEquals("function", parent.getTypeName());
  }

  private Node createNode(String code) {
    return parser().parse(code, null).getFirstChild();
  }
}