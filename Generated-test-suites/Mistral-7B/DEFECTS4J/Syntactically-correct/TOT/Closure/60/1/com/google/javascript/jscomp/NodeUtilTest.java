import static com.google.javascript.jscomp.jsonml.NodeUtil.isForIn;
import static com.google.javascript.jscomp.jsonml.NodeUtil.isFunctionDeclaration;
import static com.google.javascript.jscomp.jsonml.NodeUtil.isHoistedFunctionDeclaration;
import static com.google.javascript.jscomp.jsonml.NodeUtil.isStatement;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.google.javascript.jscomp.Node;
import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node forNode = Node.newNode(Token.FOR);
    forNode.setChild(0, Node.newNode(Token.IDENT, "var"));
    forNode.setChild(1, Node.newNode(Token.IDENT, "i"));
    forNode.setChild(2, Node.newNode(Token.LEFT_PAREN, null));
    forNode.setChild(3, Node.newNode(Token.IDENT, "array"));
    forNode.setChild(4, Node.newNode(Token.RIGHT_PAREN, null));
    forNode.setChild(5, Node.newNode(Token.BLOCK, null));

    assertTrue(isForIn(forNode));

    Node invalidForNode = Node.newNode(Token.FOR);
    assertFalse(isForIn(invalidForNode));
  }

  @Test
  public void testIsStatement() {
    Node scriptNode = Node.newNode(Token.SCRIPT, null);

    Node statementNode1 = Node.newNode(Token.PRINT, null);
    statementNode1.setChild(0, Node.newNode(Token.STRING_LITERAL, "Hello World!"));
    scriptNode.addChild(statementNode1);

    Node statementNode2 = Node.newNode(Token.VAR, null);
    statementNode2.setChild(0, Node.newNode(Token.IDENT, "x"));
    statementNode2.setChild(1, Node.newNode(Token.ASSIGN, null));
    statementNode2.setChild(2, Node.newNode(Token.NUMBER_LITERAL, "5"));
    scriptNode.addChild(statementNode2);

    Node statementNode3 = Node.newNode(Token.IF, null);
    statementNode3.setChild(0, Node.newNode(Token.COMPARE, Token.LT, null));
    statementNode3.setChild(1, Node.newNode(Token.IDENT, "x"));
    statementNode3.setChild(2, Node.newNode(Token.NUMBER_LITERAL, "5"));
    statementNode3.setChild(3, Node.newNode(Token.BLOCK, null));
    statementNode3.addChild(0, Node.newNode(Token.PRINT, null));
    statementNode3.addChild(0, Node.newNode(Token.STRING_LITERAL, "x is less than 5"));
    scriptNode.addChild(statementNode3);

    Node expressionNode = Node.newNode(Token.FUNCTION, null);
    expressionNode.setChild(0, Node.newNode(Token.IDENT, "myFunction"));
    expressionNode.setChild(1, Node.newNode(Token.LEFT_PAREN, null));
    expressionNode.setChild(2, Node.newChild(Token.RIGHT_PAREN, null));
    expressionNode.setChild(3, Node.newNode(Token.BLOCK, null));

    Node functionNode = Node.newNode(Token.FUNCTION, null);
    functionNode.setChild(0, Node.newNode(Token.IDENT, "myFunction"));
    functionNode.setChild(1, Node.newNode(Token.LEFT_PAREN, null));
    functionNode.setChild(2, Node.newNode(Token.RIGHT_PAREN, null));
    functionNode.setChild(3, Node.newNode(Token.BLOCK, null));

    assertTrue(isStatement(statementNode1));
    assertTrue(isStatement(statementNode2));
    assertTrue(isStatement(statementNode3));
    assertFalse(isStatement(expressionNode));
    assertFalse(isStatement(functionNode));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node functionDeclarationNode1 = Node.newNode(Token.FUNCTION, null);
    functionDeclarationNode1.setChild(0, Node.newNode(Token.IDENT, "myFunction"));
    functionDeclarationNode1.setChild(1, Node.newNode(Token.LEFT_PAREN, null));
    functionDeclarationNode1.setChild(2, Node.newNode(Token.RIGHT_PAREN, null));
    functionDeclarationNode1.setChild(3, Node.newNode(Token.BLOCK, null));

    Node functionDeclarationNode2 = Node.newNode(Token.FUNCTION, null);
    functionDeclarationNode2.setChild(0, Node.newNode(Token.IDENT, "anonymousFunction"));
    functionDeclarationNode2.setChild(1, Node.newNode(Token.LEFT_PAREN, null));
    functionDeclarationNode2.setChild(2, Node.newNode(Token.RIGHT_PAREN, null));
    functionDeclarationNode2.setChild(3, Node.newNode(Token.BLOCK, null));
    functionDeclarationNode2.setParent(Node.getCurrentScope());

    assertTrue(isFunctionDeclaration(functionDeclarationNode1));
    assertTrue(isFunctionDeclaration(functionDeclarationNode2));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node scriptNode = Node.newNode(Token.SCRIPT, null);

    Node functionDeclarationNode1 = Node.newNode(Token.FUNCTION, null);
    functionDeclarationNode1.setChild(0, Node.newNode(Token.IDENT, "myFunction"));
    functionDeclarationNode1.setChild(1, Node.newNode(Token.LEFT_PAREN, null));
    functionDeclarationNode1.setChild(2, Node.newNode(Token.RIGHT_PAREN, null));
    functionDeclarationNode1.setChild(3, Node.newNode(Token.BLOCK, null));
    scriptNode.addChild(functionDeclarationNode1);

    Node functionCallNode = Node.newNode(Token.CALL, null);
    functionCallNode.setChild(0, Node.newNode(Token.IDENT, "myFunction"));
    scriptNode.addChild(functionCallNode);

    Node functionDeclarationNode2 = Node.newNode(Token.FUNCTION, null);
    functionDeclarationNode2.setChild(0, Node.newNode(Token.IDENT, "anonymousFunction"));
    functionDeclarationNode2.setChild(1, Node.newNode(Token.LEFT_PAREN, null));
    functionDeclarationNode2.setChild(2, Node.newNode(Token.RIGHT_PAREN, null));
    functionDeclarationNode2.setChild(3, Node.newNode(Token.BLOCK, null));
    functionDeclarationNode2.setParent(scriptNode);

    Node functionCallNode2 = Node.newNode(Token.CALL, null);
    functionCallNode2.setChild(0, Node.newNode(Token.IDENT, "anonymousFunction"));
    scriptNode.addChild(functionCallNode2);

    assertTrue(isHoistedFunctionDeclaration(functionDeclarationNode1));
    assertFalse(isHoistedFunctionDeclaration(functionDeclarationNode2));
  }

  @Test
  public void testNegativeCases() {
    Node nonStatementNode = Node.newNode(Token.NUMBER_LITERAL, "5");
    assertFalse(isStatement(nonStatementNode));

    Node nonFunctionDeclarationNode = Node.newNode(Token.IF, null);
    assertFalse(isFunctionDeclaration(nonFunctionDeclarationNode));

    Node nonHoistedFunctionDeclarationNode = Node.newNode(Token.FUNCTION, null);
    nonHoistedFunctionDeclarationNode.setChild(0, Node.newNode(Token.IDENT, "myFunction"));
    nonHoistedFunctionDeclarationNode.setChild(1, Node.newNode(Token.LEFT_PAREN, null));
    nonHoistedFunctionDeclarationNode.setChild(2, Node.newNode(Token.RIGHT_PAREN, null));
    nonHoistedFunctionDeclarationNode.setChild(3, Node.newNode(Token.BLOCK, null));
    nonHoistedFunctionDeclarationNode.setParent(Node.newNode(Token.BLOCK, null));
    assertFalse(isHoistedFunctionDeclaration(nonHoistedFunctionDeclarationNode));
  }
}