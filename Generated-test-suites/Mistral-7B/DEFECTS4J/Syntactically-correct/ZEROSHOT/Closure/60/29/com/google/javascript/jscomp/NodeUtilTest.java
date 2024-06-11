import static org.junit.Assert.*;
import static com.google.javascript.rhino.NodeType.*;

import org.junit.Test;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.jsonml.NodeUtil;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = new Node(FOR, 1, new int[] { 1, 2, 3 });
    assertTrue(NodeUtil.isForIn(nodeForIn));

    Node nodeNotForIn = new Node(ASSIGN, 1, new int[] { 1, 2 });
    assertFalse(NodeUtil.isForIn(nodeNotForIn));
  }

  @Test
  public void testIsStatement() {
    Node nodeScript = new Node(SCRIPT, 1, new int[0]);
    assertTrue(NodeUtil.isStatement(nodeScript));

    Node nodeExpression = new Node(ASSIGN, 1, new int[] { 1, 2 });
    assertFalse(NodeUtil.isStatement(nodeExpression));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = new Node(FUNCTION, 1, new int[] { 1, 2, 3 });
    nodeFunctionDeclaration.setProperty("id", "functionName");

    Node nodeFunctionExpression = new Node(FUNCTION, 1, new int[] { 1, 2, 3 });
    nodeFunctionExpression.setProperty("id", null);

    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));
    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeFunctionDeclarationInScript = new Node(FUNCTION, 1, new int[] { 1, 2, 3 });
    nodeFunctionDeclarationInScript.setProperty("id", "functionName");

    Node nodeFunctionDeclarationInFunction = new Node(FUNCTION, 1, new int[] { 1, 2, 3 });
    nodeFunctionDeclarationInFunction.setProperty("id", "functionName");
    Node parentFunction = new Node(FUNCTION, 1, new int[0]);
    parentFunction.setProperty("id", "outerFunctionName");
    nodeFunctionDeclarationInFunction.setParent(parentFunction);

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclarationInScript));
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclarationInFunction));

    Node nodeFunctionDeclarationNotInFunction = new Node(FUNCTION, 1, new int[] { 1, 2, 3 });
    nodeFunctionDeclarationNotInFunction.setProperty("id", "functionName");
    Node parentScript = new Node(SCRIPT, 1, new int[0]);
    nodeFunctionDeclarationNotInFunction.setParent(parentScript);

    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclarationNotInFunction));
  }
}