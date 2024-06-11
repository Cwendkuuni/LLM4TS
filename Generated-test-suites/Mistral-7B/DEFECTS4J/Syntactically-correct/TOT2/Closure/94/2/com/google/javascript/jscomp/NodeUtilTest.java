import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.google.javascript.rhino.Node;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node node = new Node("for in");
    assertTrue(NodeUtil.isForIn(node));
  }

  @Test
  public void testIsForInWithInvalidNodeType() {
    Node node = new Node("invalidType");
    assertFalse(NodeUtil.isForIn(node));
  }

  @Test
  public void testIsStatement() {
    Node node = new Node("statement");
    Node parentNode = new Node("script");
    node.setParent(parentNode);
    assertTrue(NodeUtil.isStatement(node));
  }

  @Test
  public void testIsStatementWithInvalidParentNodeType() {
    Node node = new Node("statement");
    Node parentNode = new Node("expression");
    node.setParent(parentNode);
    assertFalse(NodeUtil.isStatement(node));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node functionNode = new Node("function foo(){}");
    assertTrue(NodeUtil.isFunctionDeclaration(functionNode));
  }

  @Test
  public void testIsFunctionDeclarationWithInvalidNodeType() {
    Node functionNode = new Node("invalidType");
    assertFalse(NodeUtil.isFunctionDeclaration(functionNode));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node functionNode = new Node("function foo(){}");
    Node scriptNode = new Node("script");
    functionNode.setParent(scriptNode);
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(functionNode));
  }

  @Test
  public void testIsHoistedFunctionDeclarationWithFunctionNodeWithoutParent() {
    Node functionNode = new Node("function foo(){}");
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(functionNode));
  }

  @Test
  public void testIsHoistedFunctionDeclarationWithFunctionNodeWithoutParentAndScriptNode() {
    Node functionNode = new Node("function foo(){}");
    Node scriptNode = null;
    functionNode.setParent(scriptNode);
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(functionNode));
  }

  @Test
  public void testIsFunctionDeclarationWithLessThanThreeChildren() {
    Node functionNode = new Node("function foo(){}");
    functionNode.setChildCount(2);
    assertFalse(NodeUtil.isFunctionDeclaration(functionNode));
  }

  @Test
  public void testIsStatementWithLessThanTwoParentNodes() {
    Node node = new Node("statement");
    Node parentNode = new Node("script");
    Node grandParentNode = new Node("script");
    node.setParent(parentNode);
    parentNode.setParent(grandParentNode);
    assertFalse(NodeUtil.isStatement(node));
  }
}