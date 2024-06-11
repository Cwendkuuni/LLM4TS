import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

import com.google.javascript.jscomp.jsonml.Node;
import com.google.javascript.jscomp.jsonml.NodeUtil;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = Node.newForIn("for in");
    assertTrue(NodeUtil.isForIn(nodeForIn));

    Node nodeNotForIn = Node.newExpression(1);
    assertFalse(NodeUtil.isForIn(nodeNotForIn));
  }

  @Test
  public void testIsStatement() {
    Node nodeStatement = Node.newExpression(1);
    Node statementInScript = Node.newExpression(1, Node.SCRIPT);
    Node statementInBlock = Node.newBlock(Node.newExpression(1));
    Node statementInLabel = Node.newLabel("label", Node.newExpression(1));

    assertTrue(NodeUtil.isStatement(statementInScript));
    assertTrue(NodeUtil.isStatement(statementInBlock));
    assertTrue(NodeUtil.isStatement(statementInLabel));
    assertTrue(NodeUtil.isStatement(nodeStatement));

    Node nodeNotStatement = Node.newIdentifier("identifier");
    assertFalse(NodeUtil.isStatement(nodeNotStatement));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = Node.newFunction("functionName", null, Node.newBlock(Node.newExpression(1)));
    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));

    Node nodeFunctionExpression = Node.newExpression(Node.newIdentifier("functionName"), null, Node.newBlock(Node.newExpression(1)));
    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));

    Node nodeNotFunction = Node.newExpression(1);
    assertFalse(NodeUtil.isFunctionDeclaration(nodeNotFunction));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeHoistedFunctionDeclaration = Node.newFunction("hoistedFunctionName", null, Node.newBlock(Node.newExpression(1)), Node.newScript(Node.newIdentifier("scriptName"), null));
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeHoistedFunctionDeclaration));

    Node nodeNotHoistedFunctionDeclaration = Node.newFunction("functionName", null, Node.newBlock(Node.newExpression(1)), Node.newBlock(Node.newExpression(1)));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeNotHoistedFunctionDeclaration));

    Node nodeNotFunction = Node.newExpression(1);
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeNotFunction));
  }
}