import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.Token;
import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node forNode = createNode("for (var i in array) { }");
    assertTrue("Invalid FOR-IN node", NodeUtil.isForIn(forNode));

    Node invalidForNode = createNode("for (var i = 0; i < 10; i++) { }");
    assertFalse("Invalid node is FOR-IN", NodeUtil.isForIn(invalidForNode));
  }

  @Test
  public void testIsStatement() {
    Node statementNode = createNode("if (true) { }");
    assertTrue("Invalid statement node", NodeUtil.isStatement(statementNode));

    Node expressionNode = createNode("2 + 3");
    assertFalse("Invalid expression node", NodeUtil.isStatement(expressionNode));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node functionDeclaration = createNode("function myFunction() { }");
    assertTrue("Invalid function declaration node", NodeUtil.isFunctionDeclaration(functionDeclaration));

    Node functionExpression = createNode("var myFunction = function() { }");
    assertFalse(
        "Invalid function expression node", NodeUtil.isFunctionDeclaration(functionExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node hoistedFunctionDeclaration = createNode("function myFunction() { }", null, null);
    assertTrue("Invalid hoisted function declaration node", NodeUtil.isHoistedFunctionDeclaration(hoistedFunctionDeclaration));

    Node nonHoistedFunctionDeclaration = createNode("function myFunction() { }", null, createNode("func() { }", null, null));
    assertFalse(
        "Invalid non-hoisted function declaration node", NodeUtil.isHoistedFunctionDeclaration(nonHoistedFunctionDeclaration));
  }

  private Node createNode(String code, Node... children) {
    Node root = new Node();
    root.setCode(code.toCharArray(), 0, code.length());
    if (children != null) {
      for (Node child : children) {
        root.appendChild(child);
      }
    }
    return root;
  }

  private Node createNode(String code, Node parent, Node... siblings) {
    Node node = createNode(code);
    node.setParent(parent);
    if (siblings != null) {
      for (Node sibling : siblings) {
        sibling.setNextSibling(node);
        node.setPreviousSibling(sibling);
      }
    }
    return node;
  }
}