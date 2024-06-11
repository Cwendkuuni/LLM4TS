import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static com.google.javascript.rhino.NodeType.BLOCK;
import static com.google.javascript.rhino.NodeType.FUNCTION;
import static com.google.javascript.rhino.NodeType.LABEL;
import static com.google.javascript.rhino.NodeType.SCRIPT;
import static com.google.javascript.rhino.TokenType.FOR;

import org.junit.Test;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.jsonml.NodeUtil;
import com.google.javascript.rhino.Node;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = Node.newForInStatement(1, null, null, null);
    assertTrue(NodeUtil.isForIn(nodeForIn));

    Node nodeNotForIn = Node.newExpressionStatement(Null.instance());
    assertFalse(NodeUtil.isForIn(nodeNotForIn));
  }

  @Test
  public void testIsStatement() {
    Node nodeStatement = Node.newExpressionStatement(Node.newName("x"));
    assertTrue(NodeUtil.isStatement(nodeStatement));

    Node nodeExpression = Node.newExpression(Node.newName("x"));
    assertFalse(NodeUtil.isStatement(nodeExpression));

    Node nodeBlock = Node.newBlock(null);
    assertTrue(NodeUtil.isStatement(nodeBlock));

    Node nodeLabel = Node.newLabel("label");
    assertTrue(NodeUtil.isStatement(nodeLabel));

    Node nodeScript = Node.newScript(null);
    assertTrue(NodeUtil.isStatement(nodeScript));

    Node nodeFunction = Node.newFunction(null, null, null, null, null, 0);
    assertFalse(NodeUtil.isStatement(nodeFunction));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = Node.newFunction(Node.newName("foo"), null, null, null, null, 0);
    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));

    Node nodeFunctionExpression = Node.newExpressionStatement(Node.newFunction(Node.newName("foo"), null, null, null, null, 0));
    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeRootFunction = Node.newFunction(Node.newName("foo"), null, null, null, null, 0);
    Node nodeRootBlock = Node.newBlock(null);
    nodeRootBlock.addChildToStatementList(nodeRootFunction);

    Node nodeFunction = Node.newFunction(Node.newName("foo"), null, null, null, null, 0);
    Node nodeBlock = Node.newBlock(null);
    nodeBlock.addChildToStatementList(nodeFunction);

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeRootFunction));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunction));

    Node nodeScript = Node.newScript(null);
    nodeScript.addChildToStatementList(nodeFunctionDeclaration);

    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclaration));

    Node nodeFunctionInScript = Node.newFunction(Node.newName("foo"), null, null, null, null, 0);
    nodeScript.addChildToStatementList(nodeFunctionInScript);

    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionInScript));
  }
}