import static org.junit.Assert.*;
import static com.google.javascript.jscomp.jsonml.NodeUtil.*;

import org.junit.Test;

import com.google.common.base.Preconditions;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = Node.newNode(Token.FOR, new int[] { 1, 2, 3 });
    assertTrue(isForIn(nodeForIn));

    Node nodeNotForIn = Node.newNode(Token.IF, new int[] { 1 });
    assertFalse(isForIn(nodeNotForIn));
  }

  @Test
  public void testIsStatement() {
    Node nodeStatement = Node.newNode(Token.IF, new int[] { 1 });
    assertTrue(isStatement(nodeStatement));

    Node nodeExpression = Node.newExpression(Node.newIntLiteral(1));
    assertFalse(isStatement(nodeExpression));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = Node.newNode(
        Token.FUNCTION,
        new int[] { 1, 1, 1 },
        new String[] { "myFunction" },
        Node.newBlock(Node.newStatement(Node.newExpression(Node.newIntLiteral(1)))));

    assertTrue(isFunctionDeclaration(nodeFunctionDeclaration));

    Node nodeFunctionExpression = Node.newNode(
        Token.FUNCTION,
        new int[] { 1, 1 },
        null,
        Node.newBlock(Node.newStatement(Node.newExpression(Node.newIntLiteral(1)))));

    assertFalse(isFunctionDeclaration(nodeFunctionExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeScript = Node.newNode(Token.SCRIPT, new int[] {});
    Node nodeFunctionDeclaration = Node.newNode(
        Token.FUNCTION,
        new int[] { 1, 1, 1 },
        new String[] { "myFunction" },
        Node.newBlock(Node.newStatement(Node.newExpression(Node.newIntLiteral(1)))));
    nodeScript.addChild(nodeFunctionDeclaration);

    assertTrue(isHoistedFunctionDeclaration(nodeFunctionDeclaration));

    Node nodeFunctionInBlock = Node.newNode(
        Token.FUNCTION,
        new int[] { 1, 1, 1 },
        new String[] { "myFunction" },
        Node.newBlock(Node.newStatement(Node.newExpression(Node.newIntLiteral(1)))));
    Node nodeBlock = Node.newNode(Token.BLOCK, new int[] {});
    nodeBlock.addChild(nodeFunctionInBlock);
    Node nodeFunctionInScript = Node.newNode(Token.FUNCTION, new int[] { 1 }, null, nodeBlock);
    Node nodeScriptWithFunctionInBlock = Node.newNode(Token.SCRIPT, new int[] {});
    nodeScriptWithFunctionInBlock.addChild(nodeFunctionInScript);

    assertFalse(isHoistedFunctionDeclaration(nodeFunctionInBlock));
  }

  @Test(expected = RuntimeException.class)
  public void testCheckState() {
    Node nodeNull = Node.NULL;
    Preconditions.checkState(nodeNull != null);
  }
}