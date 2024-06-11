import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeType;
import com.google.javascript.jscomp.token.Token;

import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node node = Node.objectLiteral();
    assertFalse(NodeUtil.isForIn(node));

    node = Node.newForInStatement();
    assertTrue(NodeUtil.isForIn(node));
  }

  @Test
  public void testIsStatement() {
    Node node = Node.objectLiteral();
    assertFalse(NodeUtil.isStatement(node));

    node = Node.newExpressionStatement(Node.newIdentifier("x"));
    assertTrue(NodeUtil.isStatement(node));

    node = Node.newVariableStatement(Node.newVariable("x", null));
    assertTrue(NodeUtil.isStatement(node));

    node = Node.newBlockStatement(Node.newStatementList());
    assertTrue(NodeUtil.isStatement(node));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node node = Node.newFunctionDeclaration("func", Node.newFunctionParameters(),
        Node.newStatementList(), Node.newEmptyStatement());
    assertTrue(NodeUtil.isFunctionDeclaration(node));

    node = Node.newExpressionStatement(Node.newFunctionExpression(Node.newIdentifier("func"), null,
        Node.newStatementList(), Node.newEmptyStatement()));
    assertFalse(NodeUtil.isFunctionDeclaration(node));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node node = Node.newFunctionDeclaration("func", Node.newFunctionParameters(),
        Node.newStatementList(), Node.newEmptyStatement());

    Node scriptNode = Node.newScript(Node.newStatementList());
    scriptNode.addChild(node);
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(node));

    node = Node.newVariableStatement(Node.newVariable("x", null));
    scriptNode.addChild(node);
    scriptNode.addChild(node); // duplicate declaration
    Node functionNode = Node.newFunctionDeclaration("func2", Node.newFunctionParameters(),
        Node.newStatementList(), Node.newEmptyStatement());
    scriptNode.addChild(functionNode);
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(functionNode));
  }

  @Test
  public void testIsFunctionExpression() {
    // Not implemented in NodeUtil
  }
}