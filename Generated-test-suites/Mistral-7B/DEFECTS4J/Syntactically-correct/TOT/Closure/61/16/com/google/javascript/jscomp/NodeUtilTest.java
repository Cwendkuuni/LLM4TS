import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeType;
import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = new Node(Token.FOR, new int[]{}, new int[]{0, 0, 0});
    assertTrue(NodeUtil.isForIn(nodeForIn));

    Node nodeNotForIn = new Node(Token.IF, new int[]{}, new int[]{0, 0, 0});
    assertFalse(NodeUtil.isForIn(nodeNotForIn));
  }

  @Test
  public void testIsStatement() {
    Node nodeStatement = new Node(Token.IF, new int[]{}, new int[]{0, 0, 0});
    Node parentBlock = new Node(Token.BLOCK, new int[]{}, new int[]{0, 0, 0});
    nodeStatement.setParent(parentBlock);
    assertTrue(NodeUtil.isStatement(nodeStatement));

    Node nodeFunction = new Node(Token.FUNCTION, new int[]{}, new int[]{0, 0, 0});
    assertFalse(NodeUtil.isStatement(nodeFunction));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = new Node(Token.FUNCTION, new int[]{"id"}, new int[]{0, 0, 0, 1});
    nodeFunctionDeclaration.setProperty("name", "testFunction");
    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));

    Node nodeFunctionExpression = new Node(Token.FUNCTION, new int[]{}, new int[]{0, 0, 0});
    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node scriptNode = new Node(Token.SCRIPT, new int[]{}, new int[]{0, 0, 0});
    Node functionDeclaration = new Node(Token.FUNCTION, new int[]{"id"}, new int[]{0, 0, 0, 1});
    functionDeclaration.setProperty("name", "testFunction");
    functionDeclaration.setParent(scriptNode);
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(functionDeclaration));

    Node blockNode = new Node(Token.BLOCK, new int[]{}, new int[]{0, 0, 0});
    Node functionDeclarationInsideBlock = new Node(Token.FUNCTION, new int[]{"id"}, new int[]{0, 0, 0, 1});
    functionDeclarationInsideBlock.setProperty("name", "testFunction");
    functionDeclarationInsideBlock.setParent(blockNode);
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(functionDeclarationInsideBlock));

    Node functionNode = new Node(Token.FUNCTION, new int[]{"id"}, new int[]{0, 0, 0, 1});
    functionNode.setProperty("name", "testFunction");
    Node functionCallNode = new Node(Token.CALL, new int[]{"id"}, new int[]{0, 0, 0, 1, 1});
    functionCallNode.setChild(0, functionNode);
    Node functionDeclarationAsParameter = new Node(Token.FUNCTION, new int[]{"id"}, new int[]{0, 0, 0, 1});
    functionDeclarationAsParameter.setProperty("name", "testFunction");
    functionDeclarationAsParameter.setParent(nodeFunctionCallNode);
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(functionDeclarationAsParameter));
  }

  @Test
  public void testIsStatement_NodeWithNullParent() {
    Node nodeStatement = new Node(Token.IF, new int[]{}, new int[]{0, 0, 0});
    assertNull(nodeStatement.getParent());
    assertFalse(NodeUtil.isStatement(nodeStatement));
  }

  @Test
  public void testIsFunctionDeclaration_NullNode() {
    assertNull(NodeUtil.isFunctionDeclaration(null));
  }

  @Test
  public void testIsHoistedFunctionDeclaration_NullNode() {
    assertNull(NodeUtil.isHoistedFunctionDeclaration(null));
  }

  @Test
  public void testIsFunctionDeclaration_FunctionNodeWithoutName() {
    Node nodeFunctionDeclaration = new Node(Token.FUNCTION, new int[]{}, new int[]{0, 0, 0, 0});
    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));
  }
}