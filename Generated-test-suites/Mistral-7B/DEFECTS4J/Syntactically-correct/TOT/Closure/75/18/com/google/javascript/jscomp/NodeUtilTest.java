import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static com.google.common.truth.Truth.assertThat;

import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node forNode = Node.newNode(Token.FOR, new int[] {0, 1, 2});
    assertTrue(NodeUtil.isForIn(forNode));

    Node notForNode = Node.newNode(Token.IF, new int[] {0, 1, 2});
    assertFalse(NodeUtil.isForIn(notForNode));
  }

  @Test
  public void testIsStatement() {
    Node statementNode = Node.newNode(Token.IF, new int[] {0, 1, 2});
    assertTrue(NodeUtil.isStatement(statementNode));

    Node expressionNode = Node.newNode(Token.FUNCTION, new int[] {0, 1});
    assertFalse(NodeUtil.isStatement(expressionNode));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node functionDeclaration = Node.newNode(Token.FUNCTION, new int[] {0, 1, 2, 3});
    functionDeclaration.setProperty("id", "testFunction");

    Node functionExpression = Node.newNode(Token.FUNCTION, new int[] {0, 1, 2, 3});
    Node parent = Node.newNode(Token.ASSIGN, new int[] {0, 1, 2});
    functionExpression.setParent(parent);

    assertTrue(NodeUtil.isFunctionDeclaration(functionDeclaration));
    assertFalse(NodeUtil.isFunctionDeclaration(functionExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node scriptNode = Node.newNode(Token.SCRIPT, new int[] {0, 1, 2});
    Node functionDeclaration = Node.newNode(Token.FUNCTION, new int[] {0, 1, 2, 3});
    functionDeclaration.setProperty("id", "testFunction");
    Node functionCallNode = Node.newNode(Token.CALL, new int[] {0, 1, 2});
    functionCallNode.setProperty("callee", Node.newText("testFunction"));

    Node functionDeclarationInBlock = Node.newNode(Token.FUNCTION, new int[] {0, 1, 2, 3});
    Node blockNode = Node.newNode(Token.BLOCK, new int[] {0, 1, 2});
    functionDeclarationInBlock.setParent(blockNode);

    Node functionDeclarationInFunction = Node.newNode(Token.FUNCTION, new int[] {0, 1, 2, 3});
    Node functionNode = Node.newNode(Token.FUNCTION, new int[] {0, 1, 2, 3, 4, 5, 6});
    functionDeclarationInFunction.setParent(functionNode);

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(functionDeclaration));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(functionDeclarationInBlock));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(functionDeclarationInFunction));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(functionCallNode));
  }
}