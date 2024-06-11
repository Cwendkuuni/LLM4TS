import static org.junit.Assert.*;
import static com.google.javascript.rhino.NodeType.*;

import org.junit.Test;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.jsonml.NodeUtil;
import com.google.javascript.rhino.Node;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = new Node(FOR, null, null, new int[0]);
    Node nodeNotForIn = new Node(SCRIPT, null, null, new int[0]);

    assertTrue(NodeUtil.isForIn(nodeForIn));
    assertFalse(NodeUtil.isForIn(nodeNotForIn));
  }

  @Test
  public void testIsStatement() {
    Node nodeStatement = new Node(SCRIPT, null, null, new int[0]);
    Node nodeExpression = new Node(ADD, null, null, new int[0]);

    assertTrue(NodeUtil.isStatement(nodeStatement));
    assertFalse(NodeUtil.isStatement(nodeExpression));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = new Node(FUNCTION, "funcName", null, new int[0]);
    Node nodeFunctionExpression = new Node(FUNCTION, null, null, new int[0]);
    Node nodeLabel = new Node(LABEL, "labelName", null, new int[0]);

    Node parentScript = new Node(SCRIPT, null, null, new int[0]);
    parentScript.addChild(nodeFunctionDeclaration);

    Node parentBlock = new Node(BLOCK, null, null, new int[0]);
    parentBlock.addChild(nodeFunctionDeclaration);

    Node parentFunction = new Node(FUNCTION, null, null, new int[0]);
    parentFunction.addChild(nodeFunctionDeclaration);
    parentFunction.addChild(nodeLabel);

    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));
    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));
    assertFalse(NodeUtil.isFunctionDeclaration(nodeLabel));

    // Test with parents
    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration, parentScript));
    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration, parentBlock));
    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration, parentFunction));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeFunctionDeclaration = new Node(FUNCTION, "funcName", null, new int[0]);
    Node nodeFunctionDeclarationInScript = new Node(FUNCTION, "funcName", null, new int[0]);
    Node nodeFunctionDeclarationInFunction = new Node(FUNCTION, null, null, new int[0]);
    Node nodeFunctionDeclarationInFunctionWithChild = new Node(FUNCTION, null, null, new int[0]);
    Node nodeFunctionDeclarationInFunctionWithChildAndStatement = new Node(FUNCTION, null, null, new int[0]);
    Node nodeFunctionExpression = new Node(FUNCTION, null, null, new int[0]);
    Node nodeLabel = new Node(LABEL, "labelName", null, new int[0]);

    Node parentScript = new Node(SCRIPT, null, null, new int[0]);
    parentScript.addChild(nodeFunctionDeclarationInScript);

    Node parentFunction = new Node(FUNCTION, null, null, new int[0]);
    parentFunction.addChild(nodeFunctionDeclarationInFunction);
    parentFunction.addChild(nodeFunctionDeclarationInFunctionWithChild);
    parentFunction.addChild(nodeFunctionDeclarationInFunctionWithChildAndStatement);
    parentFunction.addChild(nodeFunctionExpression);
    parentFunction.addChild(nodeLabel);

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclarationInScript));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclarationInFunction));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclarationInFunctionWithChild));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclarationInFunctionWithChildAndStatement));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionExpression));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeLabel));

    // Test with children
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclaration, null));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclaration, parentScript));
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclaration, parentFunction));
  }

  /**
   * Helper method for testing {@code NodeUtil.isFunctionDeclaration} and
   * {@code NodeUtil.isHoistedFunctionDeclaration} with parents.
   */
  private boolean isFunctionDeclarationOrHoisted(Node node, Node parent) {
    return NodeUtil.isFunctionDeclaration(node, parent)
        || NodeUtil.isHoistedFunctionDeclaration(node, parent);
  }

  @Test
  public void testIsFunctionDeclarationOrHoistedWithParents() {
    Node nodeFunctionDeclarationInScript = new Node(FUNCTION, "funcName", null, new int[0]);
    Node nodeFunctionDeclarationInBlock = new Node(FUNCTION, "funcName", null, new int[0]);
    Node nodeFunctionDeclarationInFunction = new Node(FUNCTION, null, null, new int[0]);

    Node parentScript = new Node(SCRIPT, null, null, new int[0]);
    parentScript.addChild(nodeFunctionDeclarationInScript);

    Node parentBlock = new Node(BLOCK, null, null, new int[0]);
    parentBlock.addChild(nodeFunctionDeclarationInBlock);

    Node parentFunction = new Node(FUNCTION, null, null, new int[0]);
    parentFunction.addChild(nodeFunctionDeclarationInFunction);

    assertTrue(isFunctionDeclarationOrHoisted(nodeFunctionDeclarationInScript, parentScript));
    assertTrue(isFunctionDeclarationOrHoisted(nodeFunctionDeclarationInBlock, parentBlock));
    assertFalse(isFunctionDeclarationOrHoisted(nodeFunctionDeclarationInFunction, parentFunction));
  }
}