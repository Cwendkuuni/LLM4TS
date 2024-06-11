import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.google.common.base.Preconditions;
import org.junit.Test;
import com.google.javascript.jscomp.jsonml.Node;
import static com.google.javascript.rhino.Token.*;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeFor = new Node(FOR, null, 0, 0);
    Node nodeForWithThreeChildren = new Node(FOR, null, 0, 3);

    assertTrue(NodeUtil.isForIn(nodeForWithThreeChildren));
    assertFalse(NodeUtil.isForIn(nodeFor));
  }

  @Test
  public void testIsStatement() {
    Node nodeScript = new Node(SCRIPT, null, 0, 0);
    Node nodeBlock = new Node(BLOCK, null, 0, 0);
    Node nodeLabel = new Node(LABEL, null, 0, 0);
    Node nodeFunction = new Node(FUNCTION, null, 0, 0);
    Node nodeFunctionWithStatementParent = new Node(BLOCK, null, 0, 1);
    Node nodeFunctionWithStatementParentFunction = new Node(FUNCTION, null, 0, 0);
    nodeFunctionWithStatementParentFunction.addChild(nodeFunctionWithStatementParent);

    Preconditions.checkState(nodeScript != null);
    Preconditions.checkState(nodeBlock != null);
    Preconditions.checkState(nodeLabel != null);
    Preconditions.checkState(nodeFunction != null);
    Preconditions.checkState(nodeFunctionWithStatementParent != null);
    Preconditions.checkState(nodeFunctionWithStatementParentFunction != null);

    assertTrue(NodeUtil.isStatement(nodeScript));
    assertTrue(NodeUtil.isStatement(nodeBlock));
    assertTrue(NodeUtil.isStatement(nodeLabel));
    assertFalse(NodeUtil.isStatement(nodeFunction));
    assertTrue(NodeUtil.isStatement(nodeFunctionWithStatementParent));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = new Node(FUNCTION, "funcName", 0, 0);
    Node nodeFunctionExpression = new Node(FUNCTION, null, 0, 0);
    Node nodeFunctionExpressionWithStatementParent = new Node(BLOCK, null, 0, 1);
    Node nodeFunctionExpressionWithStatementParentFunction = new Node(FUNCTION, null, 0, 0);
    nodeFunctionExpressionWithStatementParentFunction.addChild(nodeFunctionExpressionWithStatementParent);

    Preconditions.checkState(nodeFunctionDeclaration != null);
    Preconditions.checkState(nodeFunctionExpression != null);
    Preconditions.checkState(nodeFunctionExpressionWithStatementParent != null);
    Preconditions.checkState(nodeFunctionExpressionWithStatementParentFunction != null);

    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));
    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));
    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpressionWithStatementParent));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeScript = new Node(SCRIPT, null, 0, 0);
    Node nodeFunctionDeclaration = new Node(FUNCTION, "funcName", 0, 0);
    Node nodeFunctionDeclarationInScript = new Node(SCRIPT, null, 0, 1);
    Node nodeFunctionDeclarationInFunction = new Node(FUNCTION, "funcNameParent", 0, 0);
    nodeFunctionDeclarationInFunction.addChild(nodeFunctionDeclaration);

    Preconditions.checkState(nodeScript != null);
    Preconditions.checkState(nodeFunctionDeclaration != null);
    Preconditions.checkState(nodeFunctionDeclarationInScript != null);
    Preconditions.checkState(nodeFunctionDeclarationInFunction != null);

    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclaration));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclarationInScript));
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclarationInFunction));
  }
}