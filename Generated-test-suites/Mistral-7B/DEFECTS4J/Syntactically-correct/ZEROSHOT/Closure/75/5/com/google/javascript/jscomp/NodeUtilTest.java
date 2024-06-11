import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.jsonml.Node;
import com.google.javascript.rhino.Token;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeFor = new Node(Token.FOR, null, null, null);
    Node nodeIf = new Node(Token.IF, null, null, null);

    assertTrue(NodeUtil.isForIn(nodeFor));
    assertFalse(NodeUtil.isForIn(nodeIf));
  }

  @Test
  public void testIsStatement() {
    Node nodeScript = new Node(Token.SCRIPT, null, null, null);
    Node nodeFunction = new Node(Token.FUNCTION, null, null, null);
    Node nodeIf = new Node(Token.IF, null, null, null);

    assertTrue(NodeUtil.isStatement(nodeScript));
    assertTrue(NodeUtil.isStatement(nodeFunction));
    assertFalse(NodeUtil.isStatement(nodeIf));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunction = new Node(Token.FUNCTION, "testFunction", null, null);
    Node nodeIf = new Node(Token.IF, null, null, null);
    Node nodeExpression = new Node(Token.FUNCTION, null, null, null);
    nodeExpression.setProperty("name", "testFunction");

    Preconditions.checkState(nodeFunction.getParent() != null);
    Preconditions.checkState(nodeIf.getParent() != null);

    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunction));
    assertFalse(NodeUtil.isFunctionDeclaration(nodeIf));
    assertFalse(NodeUtil.isFunctionDeclaration(nodeExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeScript = new Node(Token.SCRIPT, null, null, null);
    Node nodeFunctionDeclaration = new Node(Token.FUNCTION, "testFunction", null, null);
    Node nodeFunctionExpression = new Node(Token.FUNCTION, null, null, null);
    nodeFunctionExpression.setProperty("name", "testFunction");

    Preconditions.checkState(nodeFunctionDeclaration.getParent() != null);
    Preconditions.checkState(nodeFunctionExpression.getParent() == null);

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclaration));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionExpression));

    // Test with a function in a function
    Node nodeFunctionInFunction = new Node(Token.FUNCTION, null, null, null);
    Node nodeFunctionInFunctionDeclaration = new Node(Token.FUNCTION, "testFunction", null, null);
    nodeFunctionInFunction.setParent(nodeFunctionInFunctionDeclaration);

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionInFunctionDeclaration));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionInFunction));
  }
}