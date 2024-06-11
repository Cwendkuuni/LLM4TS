import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeType;
import com.google.javascript.jscomp.Token;

import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node forNode = new Node(Token.FOR);
    forNode.setChild(0, new Node(Token.KEYWORD, "for"));
    forNode.setChild(1, new Node(Token.LEFT_PAREN));
    forNode.setChild(2, new Node(Token.IDENT, "array"));
    forNode.setChild(3, new Node(Token.COLON));
    forNode.setChild(4, new Node(Token.IDENT, "element"));
    forNode.setChild(5, new Node(Token.SEMICOLON));
    forNode.setChild(6, new Node(Token.LEFT_BRACE));
    forNode.setChild(7, new Node(Token.STATEMENT));
    forNode.setChild(8, new Node(Token.STATEMENT));
    forNode.setChild(9, new Node(Token.RIGHT_BRACE));

    assertTrue(NodeUtil.isForIn(forNode));

    Node ifNode = new Node(Token.IF);
    ifNode.setChild(0, new Node(Token.KEYWORD, "if"));
    ifNode.setChild(1, new Node(Token.LPAREN));
    ifNode.setChild(2, new Node(Token.RPAREN));
    ifNode.setChild(3, new Node(Token.STATEMENT));

    assertFalse(NodeUtil.isForIn(ifNode));
  }

  @Test
  public void testIsStatement() {
    Node statementNode = new Node(Token.IDENT, "statement");

    Node functionNode = new Node(Token.FUNCTION);
    functionNode.setChild(0, new Node(Token.KEYWORD, "function"));
    functionNode.setChild(1, new Node(Token.IDENT, "functionName"));
    functionNode.setChild(2, new Node(Token.LEFT_PAREN));
    functionNode.setChild(3, new Node(Token.RIGHT_PAREN));
    functionNode.setChild(4, new Node(Token.BLOCK));
    functionNode.setChild(5, statementNode);

    assertTrue(NodeUtil.isStatement(statementNode));
    assertTrue(NodeUtil.isStatement(functionNode));

    assertFalse(NodeUtil.isStatement(new Node(Token.IDENT, "identifier")));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node functionDeclaration = new Node(Token.FUNCTION);
    functionDeclaration.setChild(0, new Node(Token.KEYWORD, "function"));
    functionDeclaration.setChild(1, new Node(Token.IDENT, "functionName"));
    functionDeclaration.setChild(2, new Node(Token.LEFT_PAREN));
    functionDeclaration.setChild(3, new Node(Token.RIGHT_PAREN));
    functionDeclaration.setChild(4, new Node(Token.BLOCK));

    assertTrue(NodeUtil.isFunctionDeclaration(functionDeclaration));

    Node functionExpression = new Node(Token.FUNCTION);
    functionExpression.setChild(0, new Node(Token.IDENT, "functionName"));
    functionExpression.setChild(1, new Node(Token.LEFT_PAREN));
    functionExpression.setChild(2, new Node(Token.RIGHT_PAREN));
    functionExpression.setChild(3, new Node(Token.BLOCK));

    assertFalse(NodeUtil.isFunctionDeclaration(functionExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node scriptNode = new Node(Token.SCRIPT);

    Node functionDeclaration = new Node(Token.FUNCTION);
    functionDeclaration.setChild(0, new Node(Token.KEYWORD, "function"));
    functionDeclaration.setChild(1, new Node(Token.IDENT, "functionName"));
    functionDeclaration.setChild(2, new Node(Token.LEFT_PAREN));
    functionDeclaration.setChild(3, new Node(Token.RIGHT_PAREN));
    functionDeclaration.setChild(4, new Node(Token.BLOCK));

    scriptNode.setChild(0, functionDeclaration);

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(functionDeclaration));

    Node hoistedFunctionDeclaration = new Node(Token.FUNCTION);
    hoistedFunctionDeclaration.setChild(0, new Node(Token.KEYWORD, "function"));
    hoistedFunctionDeclaration.setChild(1, new Node(Token.IDENT, "functionName"));
    hoistedFunctionDeclaration.setChild(2, new Node(Token.LEFT_PAREN));
    hoistedFunctionDeclaration.setChild(3, new Node(Token.RIGHT_PAREN));
    hoistedFunctionDeclaration.setChild(4, new Node(Token.BLOCK));

    Node functionNode = new Node(Token.FUNCTION);
    functionNode.setChild(0, new Node(Token.IDENT, "functionName"));
    functionNode.setChild(1, new Node(Token.LEFT_PAREN));
    functionNode.setChild(2, new Node(Token.RIGHT_PAREN));
    functionNode.setChild(3, new Node(Token.BLOCK));

    scriptNode.setChild(1, functionNode);

    assertFalse(NodeUtil.isHoistedFunctionDeclaration(functionNode));
  }
}