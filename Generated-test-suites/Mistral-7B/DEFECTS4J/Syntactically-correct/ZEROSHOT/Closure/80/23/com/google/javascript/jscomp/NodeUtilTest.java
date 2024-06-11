import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static com.google.javascript.rhino.Token.*;

import com.google.common.base.Preconditions;
import org.junit.Test;
import com.google.javascript.jscomp.jsonml.NodeUtil;
import com.google.javascript.rhino.Node;

/**
 * Test file for NodeUtil class.
 */
public class NodeUtilTest {

  /**
   * Test isForIn method.
   */
  @Test
  public void testIsForIn() {
    Node nodeFor = Node.newForLoop("for (var i in array) {}");
    Node nodeIf = Node.newIfStatement();
    Node nodeFunction = Node.newFunction("function test() {}");

    assertTrue(NodeUtil.isForIn(nodeFor));
    assertFalse(NodeUtil.isForIn(nodeIf));
    assertFalse(NodeUtil.isForIn(nodeFunction));
  }

  /**
   * Test isStatement method.
   */
  @Test
  public void testIsStatement() {
    Node nodeIf = Node.newIfStatement();
    Node nodeFor = Node.newForLoop("for (var i in array) {}");
    Node nodeFunction = Node.newFunction("function test() {}");
    Node nodeScript = Node.newScript("function test() {}");

    Node parentIf = Node.newBlock("if (condition) {}", nodeIf);
    Node parentFunction = Node.newBlock("function test() {}", nodeFunction);
    Node parentScript = Node.newScript("", nodeScript);

    assertTrue(NodeUtil.isStatement(nodeIf));
    assertTrue(NodeUtil.isStatement(nodeFor));
    assertFalse(NodeUtil.isStatement(nodeFunction));
    assertTrue(NodeUtil.isStatement(nodeScript));

    assertTrue(NodeUtil.isStatement(parentIf.getChild(0)));
    assertTrue(NodeUtil.isStatement(parentFunction));
    assertTrue(NodeUtil.isStatement(parentScript));
  }

  /**
   * Test isFunctionDeclaration method.
   */
  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunction = Node.newFunction("function test() {}");
    Node nodeFunctionExpression = Node.newExpressionStatement(Node.newFunctionExpression(null, "test"));

    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunction));
    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));
  }

  /**
   * Test isHoistedFunctionDeclaration method.
   */
  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeFunction = Node.newFunction("function test() {}", Node.newScript(""));
    Node nodeFunctionInFunction = Node.newFunction("function outerFunction() {"
        + "function test() {}", null);

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunction));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionInFunction));
  }
}