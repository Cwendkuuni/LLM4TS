import static org.junit.Assert.*;
import static com.google.javascript.jscomp.jsonml.NodeUtil.*;

import com.google.common.base.Preconditions;
import org.junit.Test;

/**
 * Tests for NodeUtil class.
 */
public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = new Node(Token.FOR, null, 0, 0);
    nodeForIn.setChildCount(3);
    assertTrue(isForIn(nodeForIn));

    Node nodeNotForIn = new Node(Token.IF, null, 0, 0);
    assertFalse(isForIn(nodeNotForIn));
  }

  @Test
  public void testIsStatement() {
    Node nodeStatement = new Node(Token.IF, null, 0, 0);
    Node parentScript = new Node(Token.SCRIPT, null, 0, 0);
    parentScript.setChild(0, nodeStatement);
    assertTrue(isStatement(nodeStatement));

    Node nodeNotStatement = new Node(Token.IF, null, 0, 0);
    Node parentExpression = new Node(Token.ADD, null, 0, 0);
    parentExpression.setChild(0, nodeNotStatement);
    assertFalse(isStatement(nodeNotStatement));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = new Node(Token.FUNCTION, "name", 0, 0);
    nodeFunctionDeclaration.setChildCount(1);
    Node body = new Node(Token.BLOCK, null, 0, 0);
    nodeFunctionDeclaration.setChild(0, body);
    Node parentScript = new Node(Token.SCRIPT, null, 0, 0);
    parentScript.setChild(0, nodeFunctionDeclaration);
    assertTrue(isFunctionDeclaration(nodeFunctionDeclaration));

    Node nodeFunctionExpression = new Node(Token.FUNCTION, null, 0, 0);
    nodeFunctionExpression.setChildCount(1);
    body = new Node(Token.BLOCK, null, 0, 0);
    nodeFunctionExpression.setChild(0, body);
    Node parentExpression = new Node(Token.ADD, null, 0, 0);
    parentExpression.setChild(0, nodeFunctionExpression);
    assertFalse(isFunctionDeclaration(nodeFunctionExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeHoistedFunctionDeclaration = new Node(Token.FUNCTION, "name", 0, 0);
    nodeHoistedFunctionDeclaration.setChildCount(1);
    Node body = new Node(Token.BLOCK, null, 0, 0);
    nodeHoistedFunctionDeclaration.setChild(0, body);
    Node parentScript = new Node(Token.SCRIPT, null, 0, 0);
    parentScript.setChild(0, nodeHoistedFunctionDeclaration);
    assertTrue(isHoistedFunctionDeclaration(nodeHoistedFunctionDeclaration));

    Node nodeFunctionDeclaration = new Node(Token.FUNCTION, "name", 0, 0);
    nodeFunctionDeclaration.setChildCount(1);
    body = new Node(Token.BLOCK, null, 0, 0);
    nodeFunctionDeclaration.setChild(0, body);
    Node parentFunction = new Node(Token.FUNCTION, null, 0, 0);
    parentFunction.setChild(0, nodeFunctionDeclaration);
    Node parentScript2 = new Node(Token.SCRIPT, null, 0, 0);
    parentScript2.setChild(0, parentFunction);
    assertFalse(isHoistedFunctionDeclaration(nodeFunctionDeclaration));
  }
}