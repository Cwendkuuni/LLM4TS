import static org.junit.Assert.*;
import static com.google.javascript.jscomp.jsonml.NodeUtil.*;

import com.google.common.base.Preconditions;
import com.google.javascript.rhino.Node;

import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = Node.newForIn(1, 1, 3);
    assertTrue(isForIn(nodeForIn));

    Node nodeNotForIn = Node.newExpression(1, Token.IF, null);
    assertFalse(isForIn(nodeNotForIn));
  }

  @Test
  public void testIsStatement() {
    Node nodeStatement = Node.newExpression(1, Token.IF, null);
    Node parentScript = Node.newScript(1, null);
    parentScript.addChildToEnd(nodeStatement);

    assertTrue(isStatement(nodeStatement));

    Node nodeExpression = Node.newExpression(1, Token.IF, null);
    Node parentBlock = Node.newBlock(1, null);
    parentBlock.addChildToEnd(nodeExpression);

    assertFalse(isStatement(nodeExpression));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = Node.newFunction(1, "func", null, null, null, null);
    Node parentScript = Node.newScript(1, null);
    parentScript.addChildToEnd(nodeFunctionDeclaration);

    assertTrue(isFunctionDeclaration(nodeFunctionDeclaration));

    Node nodeFunctionExpression = Node.newExpression(1, Token.FUNCTION, null);
    Node parentBlock = Node.newBlock(1, null);
    parentBlock.addChildToEnd(nodeFunctionExpression);

    assertFalse(isFunctionDeclaration(nodeFunctionExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeFunctionDeclaration = Node.newFunction(1, "func", null, null, null, null);
    Node scriptNode = Node.newScript(1, null);
    scriptNode.addChildToEnd(nodeFunctionDeclaration);

    assertTrue(isHoistedFunctionDeclaration(nodeFunctionDeclaration));

    Node nodeFunctionDeclaration2 = Node.newFunction(1, "func", null, null, null, null);
    Node functionNode = Node.newFunction(1, "func2", null, null, null, null);
    Node scriptNode2 = Node.newScript(1, null);
    scriptNode2.addChildToEnd(functionNode);
    scriptNode2.addChildToEnd(nodeFunctionDeclaration2);

    assertFalse(isHoistedFunctionDeclaration(nodeFunctionDeclaration2));
  }
}