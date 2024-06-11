import static org.junit.Assert.*;
import static com.google.javascript.rhino.NodeType.*;

import org.junit.Test;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.jsonml.NodeUtil;
import com.google.javascript.rhino.Node;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = Node.newForIn(1, 1, 1, "label", null, null, null, null, null, null);
    assertTrue(NodeUtil.isForIn(nodeForIn));

    Node nodeNotForIn = Node.newCall(1, 1, "someFunction", null, null, null);
    assertFalse(NodeUtil.isForIn(nodeNotForIn));
  }

  @Test
  public void testIsStatement() {
    Node nodeScript = Node.newText(1, 1, "var x = 1;");
    assertTrue(NodeUtil.isStatement(nodeScript));

    Node nodeFunction = Node.newFunction(1, 1, 0, null, null, null, null, null, null, null);
    assertTrue(NodeUtil.isStatement(nodeFunction));

    Node nodeCall = Node.newCall(1, 1, "someFunction", null, null, null);
    assertFalse(NodeUtil.isStatement(nodeCall));

    Node nodeIf = Node.newIf(1, 1, 1, null, null, null, null);
    assertTrue(NodeUtil.isStatement(nodeIf));

    Node nodeLabel = Node.newLabel(1, 1, "label");
    assertTrue(NodeUtil.isStatement(nodeLabel));

    Node nodeComment = Node.newComment(1, 1, "// This is a comment.");
    assertFalse(NodeUtil.isStatement(nodeComment));
  }

  @Test(expected = IllegalStateException.class)
  public void testCheckStateParentIsNull() {
    Node node = Node.newText(1, 1, "var x = 1;");
    NodeUtil.isStatement(node);
    Preconditions.checkState(node.getParent() != null);
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = Node.newFunction(1, 1, 0, null, null, null, null, null, null, null);
    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));

    Node nodeFunctionExpression = Node.newCall(1, 1, "function(){}", null, null, null);
    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeFunctionDeclaration = Node.newFunction(1, 1, 0, null, null, null, null, null, null, null);
    Node parentScript = Node.newText(1, 1, "function main(){\n  " + nodeFunctionDeclaration.toString() + "\n}");
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclaration));

    Node nodeFunctionDeclarationInFunction = Node.newFunction(1, 1, 0, null, null, null, null, null, null, null);
    Node parentFunction = Node.newFunction(1, 1, 0, null, null, null, null, null, null, null);
    parentFunction.setChild(0, nodeFunctionDeclarationInFunction);
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclarationInFunction));
  }
}