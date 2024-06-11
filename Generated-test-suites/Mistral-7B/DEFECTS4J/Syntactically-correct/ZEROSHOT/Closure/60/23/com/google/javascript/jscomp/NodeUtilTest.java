import static org.junit.Assert.*;
import static com.google.javascript.rhino.NodeType.*;

import com.google.common.base.Preconditions;
import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeWithForIn = createNode("for (var x in y) {}", FOR, 3);
    assertTrue(NodeUtil.isForIn(nodeWithForIn));

    Node nodeWithoutForIn = createNode("for (var x = 0; x < 5; x++) {}", FOR, 3, 2, 1);
    assertFalse(NodeUtil.isForIn(nodeWithoutForIn));
  }

  @Test
  public void testIsStatement() {
    Node statementNode = createNode("if (x > 0) {}", IF, 1);
    assertTrue(NodeUtil.isStatement(statementNode));

    Node expressionNode = createNode("x + y", ADD, 2);
    assertFalse(NodeUtil.isStatement(expressionNode));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node functionDeclaration = createNode(
        "function foo() {}", FUNCTION, 1, "foo", 1, "foo", false);
    assertTrue(NodeUtil.isFunctionDeclaration(functionDeclaration));

    Node functionExpression = createNode(
        "var foo = function() {}", FUNCTION, 1, null, 1, "foo", true);
    assertFalse(NodeUtil.isFunctionDeclaration(functionExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node hoistedFunctionDeclaration = createNode(
        "function foo() {}", FUNCTION, 1, "foo", 1, "foo", false, null, SCRIPT);
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(hoistedFunctionDeclaration));

    Node nonHoistedFunctionDeclaration = createNode(
        "function foo() {}", FUNCTION, 1, "foo", 1, "foo", false, null, BLOCK);
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nonHoistedFunctionDeclaration));
  }

  private Node createNode(String nodeType, int... childrenCounts) {
    Node node = new Node();
    node.setType(Token.INT_LITERAL); // Default type
    node.setLine(1);
    node.setColumn(1);
    if (childrenCounts != null) {
      node.setChildCount(childrenCounts.length);
      for (int i = 0; i < childrenCounts.length; i++) {
        node.setChild(i, createNode( nodeType, childrenCounts[i]));
      }
    }

    if (nodeType != null) {
      node.setType(NodeType.fromToken(nodeType.charAt(0)));
    }

    return node;
  }

  private Node createNode(String nodeType, int type, int... childrenCounts) {
    return createNode(nodeType, new int[] {type, childrenCounts.length});
  }

  private Node createNode(String nodeType, int type, String name, int... childrenCounts) {
    return createNode(nodeType, name, type, null, childrenCounts.length, null, false);
  }

  private Node createNode(
      String nodeType, int type, String name, String fnName, int argCount, String sourceName, boolean isExpression) {
    Node node = new Node();
    node.setType(NodeType.fromToken(nodeType.charAt(0)));
    node.setLine(1);
    node.setColumn(1);
    if (type >= 0) {
      node.setType(type);
    }
    if (name != null) {
      node.setName(name);
    }
    if (fnName != null) {
      node.setFunctionName(fnName);
    }
    if (argCount > 0) {
      node.setArgumentCount(argCount);
    }
    if (sourceName != null) {
      node.setSourceName(sourceName);
    }
    node.setIsExpression(isExpression);
    if (childrenCounts.length > 0) {
      node.setChildCount(childrenCounts.length);
      for (int i = 0; i < childrenCounts.length; i++) {
        node.setChild(i, createNode("", 0));
      }
    }
    return node;
  }
}