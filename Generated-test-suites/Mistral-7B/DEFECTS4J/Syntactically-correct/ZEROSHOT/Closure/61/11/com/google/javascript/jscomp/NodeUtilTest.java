import static org.junit.Assert.*;
import static com.google.javascript.rhino.NodeType.*;

import com.google.common.base.Preconditions;
import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = new Node(FOR, new int[]{}, new Object[]{}, new Object[3]);
    assertTrue(NodeUtil.isForIn(nodeForIn));

    Node nodeNotForIn = new Node(SCRIPT, new int[]{}, new Object[]{}, new Object[0]);
    assertFalse(NodeUtil.isForIn(nodeNotForIn));
  }

  @Test
  public void testIsStatement() {
    Node nodeStatement = new Node(IF, new int[]{}, new Object[]{}, new Object[1]);
    Node parentStatement = new Node(BLOCK, new int[]{}, new Object[]{}, new Object[0]);
    parentStatement.setChild(0, nodeStatement);

    assertTrue(NodeUtil.isStatement(nodeStatement));
    assertTrue(NodeUtil.isStatement(parentStatement));

    Node nodeExpression = new Node(FUNCTION, new int[]{}, new Object[]{}, new Object[0]);
    Node parentExpression = new Node(EXPRESSION_STATEMENT, new int[]{}, new Object[]{}, new Object[]{nodeExpression});

    assertFalse(NodeUtil.isStatement(nodeExpression));
    assertFalse(NodeUtil.isStatement(parentExpression));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = new Node(FUNCTION, new int[]{}, new Object[]{"foo"}, new Object[1]);
    nodeFunctionDeclaration.setChild(0, new Node(IDENTIFIER, new int[]{}, new Object[]{"foo"}, null));

    Node parentScript = new Node(SCRIPT, new int[]{}, new Object[0], new Object[0]);
    parentScript.setChild(0, nodeFunctionDeclaration);

    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));
    assertFalse(NodeUtil.isFunctionDeclaration(parentScript));

    Node nodeFunctionExpression = new Node(FUNCTION, new int[]{}, new Object[0], new Object[1]);
    Node parentBlock = new Node(BLOCK, new int[]{}, new Object[0], new Object[0]);
    parentBlock.setChild(0, nodeFunctionExpression);

    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));
    assertFalse(NodeUtil.isFunctionDeclaration(parentBlock));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeHoistedFunctionDeclaration = new Node(FUNCTION, new int[]{}, new Object[]{"foo"}, new Object[1]);
    nodeHoistedFunctionDeclaration.setChild(0, new Node(IDENTIFIER, new int[]{}, new Object[]{"foo"}, null));

    Node parentScript = new Node(SCRIPT, new int[]{}, new Object[0], new Object[0]);
    parentScript.setChild(0, nodeHoistedFunctionDeclaration);

    Node nodeFunctionInScript = new Node(FUNCTION, new int[]{}, new Object[]{"bar"}, new Object[1]);
    nodeFunctionInScript.setChild(0, new Node(IDENTIFIER, new int[]{}, new Object[]{"bar"}, null));

    Node parentFunction = new Node(FUNCTION, new int[]{}, new Object[0], new Object[1]);
    parentFunction.setChild(0, nodeFunctionInScript);
    parentScript.setChild(1, parentFunction);

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeHoistedFunctionDeclaration));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionInScript));
  }
}