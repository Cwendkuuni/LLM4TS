import static org.junit.Assert.*;
import static com.google.javascript.rhino.NodeType.*;

import com.google.common.base.Preconditions;
import org.junit.Test;

import com.google.javascript.jscomp.jsonml.NodeUtil;
import com.google.javascript.rhino.Node;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = new Node(FOR, null, 0, 0);
    Node nodeWithThreeChildren = new Node(IF, null, 0, 0);
    nodeWithThreeChildren.setChild(0, new Node(TOKEN, "for", 0, 0));
    nodeWithThreeChildren.setChild(1, new Node(TOKEN, "(", 0, 0));
    nodeWithThreeChildren.setChild(2, new Node(TOKEN, ")", 0, 0));

    assertTrue(NodeUtil.isForIn(nodeForIn));
    assertFalse(NodeUtil.isForIn(nodeWithThreeChildren));
  }

  @Test
  public void testIsStatement() {
    Node nodeStatement = new Node(IF, null, 0, 0);
    Node nodeExpression = new Node(BINOP, null, 0, 0);
    Node nodeScript = new Node(SCRIPT, null, 0, 0);
    Node nodeBlock = new Node(BLOCK, null, 0, 0);
    Node nodeLabel = new Node(LABEL, null, 0, 0);

    assertTrue(NodeUtil.isStatement(nodeStatement));
    assertTrue(NodeUtil.isStatement(nodeScript));
    assertTrue(NodeUtil.isStatement(nodeBlock));
    assertTrue(NodeUtil.isStatement(nodeLabel));

    Node nodeFunction = new Node(FUNCTION, null, 0, 0);
    Node nodeFunctionExpression = new Node(FUNCTION, null, 0, 0);
    nodeFunctionExpression.setChild(0, new Node(IDENT, "foo", 0, 0));

    assertFalse(NodeUtil.isStatement(nodeFunction));
    assertTrue(NodeUtil.isStatement(nodeFunctionExpression));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = new Node(FUNCTION, null, 0, 0);
    nodeFunctionDeclaration.setChild(0, new Node(IDENT, "foo", 0, 0));
    Node nodeStatementFunction = new Node(FUNCTION, null, 0, 0);
    nodeStatementFunction.setChild(0, new Node(IDENT, "foo", 0, 0));
    Node nodeExpressionFunction = new Node(FUNCTION, null, 0, 0);
    nodeExpressionFunction.setChild(0, new Node(IDENT, "foo", 0, 0));

    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));
    assertFalse(NodeUtil.isFunctionDeclaration(nodeStatementFunction));
    assertFalse(NodeUtil.isFunctionDeclaration(nodeExpressionFunction));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeFunctionDeclarationInScript = new Node(FUNCTION, null, 0, 0);
    nodeFunctionDeclarationInScript.setChild(0, new Node(IDENT, "foo", 0, 0));
    Node nodeFunctionDeclarationInFunction = new Node(FUNCTION, null, 0, 0);
    nodeFunctionDeclarationInFunction.setChild(0, new Node(IDENT, "bar", 0, 0));
    nodeFunctionDeclarationInFunction.setParent(new Node(FUNCTION, null, 0, 0));

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclarationInScript));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclarationInFunction));
  }
}