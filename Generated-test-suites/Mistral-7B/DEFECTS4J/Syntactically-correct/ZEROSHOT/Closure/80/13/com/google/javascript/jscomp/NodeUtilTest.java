import static org.junit.Assert.*;
import static com.google.javascript.rhino.NodeType.*;

import org.junit.Test;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.jsonml.NodeUtil;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = new Node(FOR, 0);
    nodeForIn.setChildCount(3);
    assertTrue(NodeUtil.isForIn(nodeForIn));

    Node nodeNotForIn = new Node(IF, 0);
    assertFalse(NodeUtil.isForIn(nodeNotForIn));
  }

  @Test
  public void testIsStatement() {
    Node nodeStatement = new Node(SCRIPT, 0);
    assertTrue(NodeUtil.isStatement(nodeStatement));

    Node nodeExpression = new Node(ADD, 0);
    assertFalse(NodeUtil.isStatement(nodeExpression));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = new Node(FUNCTION, 0);
    nodeFunctionDeclaration.setProperty("id", "testFunction");
    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));

    Node nodeFunctionExpression = new Node(FUNCTION, 0);
    nodeFunctionExpression.setProperty("id", "testFunction");
    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));

    Node nodeNotFunction = new Node(IF, 0);
    assertFalse(NodeUtil.isFunctionDeclaration(nodeNotFunction));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeHoistedFunctionDeclaration = new Node(FUNCTION, 0);
    nodeHoistedFunctionDeclaration.setProperty("id", "testFunction");
    Node parentScript = new Node(SCRIPT, 0);
    parentScript.setChild(nodeHoistedFunctionDeclaration);
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeHoistedFunctionDeclaration));

    Node nodeFunctionDeclarationInFunction = new Node(FUNCTION, 0);
    nodeFunctionDeclarationInFunction.setProperty("id", "testFunction");
    Node parentFunction = new Node(FUNCTION, 0);
    parentFunction.setChild(nodeFunctionDeclarationInFunction);
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclarationInFunction));

    Node nodeFunctionDeclarationInBlock = new Node(BLOCK, 0);
    nodeFunctionDeclarationInBlock.setChild(nodeFunctionDeclarationInFunction);
    Node parentBlock = new Node(SCRIPT, 0);
    parentBlock.setChild(nodeFunctionDeclarationInBlock);
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclarationInFunction));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testIsHoistedFunctionDeclarationNullParent() {
    Node nodeFunctionDeclaration = new Node(FUNCTION, 0);
    nodeFunctionDeclaration.setProperty("id", "testFunction");
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclaration));
  }
}