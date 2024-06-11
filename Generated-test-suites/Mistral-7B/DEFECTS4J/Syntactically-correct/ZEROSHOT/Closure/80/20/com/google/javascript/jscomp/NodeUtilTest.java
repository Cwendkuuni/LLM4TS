import static org.junit.Assert.*;
import static com.google.javascript.rhino.NodeType.*;

import com.google.common.base.Preconditions;
import org.junit.Test;

import com.google.javascript.jscomp.jsonml.NodeUtil;
import com.google.javascript.rhino.Node;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = new Node(FOR, null, 0, null);
    Node nodeWithThreeChildren = new Node(ASSIGN, null, 0, null);
    nodeWithThreeChildren.setChild(0, new Node(IDENTIFIER, "for", null, null));
    nodeWithThreeChildren.setChild(1, new Node(IDENTIFIER, "in", null, null));
    nodeWithThreeChildren.setChild(2, new Node(IDENTIFIER, "array", null, null));

    assertTrue(NodeUtil.isForIn(nodeForIn));
    assertFalse(NodeUtil.isForIn(nodeWithThreeChildren));
  }

  @Test
  public void testIsStatement() {
    Node nodeScript = new Node(SCRIPT, null, 0, null);
    Node nodeBlock = new Node(BLOCK, null, 0, null);
    Node nodeLabel = new Node(LABEL, "label", null, null);
    Node nodeFunction = new Node(FUNCTION, null, 0, null);
    Node nodeIf = new Node(IF, null, 0, null);
    Node nodeExpression = new Node(IDENTIFIER, "expression", null, null);

    assertTrue(NodeUtil.isStatement(nodeScript));
    assertTrue(NodeUtil.isStatement(nodeBlock));
    assertTrue(NodeUtil.isStatement(nodeLabel));
    assertFalse(NodeUtil.isStatement(nodeFunction));
    assertFalse(NodeUtil.isStatement(nodeIf));
    assertFalse(NodeUtil.isStatement(nodeExpression));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = new Node(FUNCTION, null, 0, null);
    nodeFunctionDeclaration.setChild(0, new Node(IDENTIFIER, "functionName", null, null));
    Node nodeFunctionExpression = new Node(FUNCTION, null, 1, null);
    nodeFunctionExpression.setChild(0, new Node(IDENTIFIER, "anotherName", null, null));
    nodeFunctionExpression.setChild(1, new Node(BLOCK, null, 0, null));

    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));
    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeScript = new Node(SCRIPT, null, 0, null);
    Node nodeFunctionDeclaration = new Node(FUNCTION, null, 0, null);
    nodeFunctionDeclaration.setChild(0, new Node(IDENTIFIER, "functionName", null, null));
    Node nodeFunction = new Node(FUNCTION, null, 1, null);
    nodeFunction.setChild(0, new Node(IDENTIFIER, "anotherFunctionName", null, null));
    nodeFunction.setChild(1, new Node(BLOCK, null, 0, null));

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclaration));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunction));

    Node parentScript = new Node(SCRIPT, null, 0, null);
    parentScript.setChild(0, nodeFunctionDeclaration);
    parentScript.setChild(1, nodeFunction);

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclaration));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunction));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testPreconditionsCheckStateThrowsException() {
    Node node = new Node(SCRIPT, null, 0, null);
    Preconditions.checkState(node.getParent() == null);
  }
}