import static org.junit.Assert.*;
import static com.google.javascript.jscomp.jsonml.NodeUtil.*;
import com.google.javascript.rhino.Node;

public class NodeUtilTest {

  @org.junit.Test
  public void testIsForIn() {
    Node forNode = new Node(Token.FOR);
    forNode.setChild(0, new Node(Token.IDENT, "var"));
    forNode.setChild(1, new Node(Token.IDENT, "i"));
    forNode.setChild(2, new Node(Token.ASSIGN, Token.ASSIGN, new Node(Token.IDENT, "array"), new Node(Token.LITERAL, 0)));
    forNode.setChild(3, new Node(Token.KEYWORD, "in"));
    forNode.setChild(4, new Node(Token.IDENT, "array"));
    Node array = new Node(Token.ARRAY, new Node[0]);
    array.setChild(0, new Node(Token.OBJECT, new Node[0]));

    Node falseNode = new Node(Token.FALSE);
    Node trueNode = new Node(Token.TRUE);

    Node nullNode = new Node(Token.NULL);
    Node nonForNode = new Node(Token.IDENT, "foo");

    assertTrue(isForIn(forNode));
    assertFalse(isForIn(falseNode));
    assertFalse(isForIn(trueNode));
    assertFalse(isForIn(nullNode));
    assertFalse(isForIn(nonForNode));
  }

  @org.junit.Test
  public void testIsStatement() {
    Node scriptNode = new Node(Token.SCRIPT);
    scriptNode.setChild(0, new Node(Token.BLOCK, new Node[0]));

    Node statementNode1 = new Node(Token.ASSIGN, Token.ASSIGN, new Node(Token.IDENT, "x"), new Node(Token.LITERAL, 10));
    Node statementNode2 = new Node(Token.IF, Token.KEYWORD, "if", new Node(Token.IDENT, "x"), new Node(Token.BLOCK, new Node[0]));

    Node expressionNode1 = new Node(Token.ASSIGN, Token.ASSIGN, new Node(Token.IDENT, "y"), new Node(Token.IDENT, "x"));
    Node expressionNode2 = new Node(Token.IDENT, "foo");

    assertTrue(isStatement(statementNode1));
    assertTrue(isStatement(statementNode2));
    assertFalse(isStatement(expressionNode1));
    assertFalse(isStatement(expressionNode2));
  }

  @org.junit.Test
  public void testIsFunctionDeclaration() {
    Node functionDeclarationNode = new Node(Token.FUNCTION, new Node[0], new Node[0]);
    functionDeclarationNode.setChild(0, new Node(Token.IDENT, "myFunction"));

    Node functionExpressionNode1 = new Node(Token.FUNCTION, new Node[0], new Node[0]);
    functionExpressionNode1.setChild(0, new Node(Token.IDENT, "myFunction"));
    functionExpressionNode1.setParent(new Node(Token.ASSIGN, Token.ASSIGN, new Node(Token.IDENT, "myVariable"), functionExpressionNode1));

    Node functionExpressionNode2 = new Node(Token.FUNCTION, new Node[0], new Node[0]);
    functionExpressionNode2.setChild(0, new Node(Token.IDENT, "myFunction"));
    functionExpressionNode2.setParent(new Node(Token.CALL, null, functionExpressionNode2, new Node[0]));

    Node ifStatementNode = new Node(Token.IF, Token.KEYWORD, "if", new Node(Token.IDENT, "x"), new Node(Token.BLOCK, new Node[0]));
    Node returnStatementNode = new Node(Token.RETURN, null, new Node(Token.LITERAL, 42));

    assertTrue(isFunctionDeclaration(functionDeclarationNode));
    assertFalse(isFunctionDeclaration(functionExpressionNode1));
    assertFalse(isFunctionDeclaration(functionExpressionNode2));
    assertFalse(isFunctionDeclaration(ifStatementNode));
    assertFalse(isFunctionDeclaration(returnStatementNode));
  }

  @org.junit.Test
  public void testIsHoistedFunctionDeclaration() {
    Node scriptNode = new Node(Token.SCRIPT);

    Node functionDeclarationNode = new Node(Token.FUNCTION, new Node[0], new Node[0]);
    functionDeclarationNode.setChild(0, new Node(Token.IDENT, "myFunction"));
    scriptNode.setChild(0, functionDeclarationNode);

    Node functionDeclarationNodeInFunction = new Node(Token.FUNCTION, new Node[0], new Node[0]);
    functionDeclarationNodeInFunction.setChild(0, new Node(Token.IDENT, "myFunction"));
    Node functionNode = new Node(Token.FUNCTION, new Node[0], new Node[0]);
    functionNode.setChild(0, functionDeclarationNodeInFunction);
    scriptNode.setChild(1, functionNode);

    Node functionExpressionNode = new Node(Token.FUNCTION, new Node[0], new Node[0]);
    functionExpressionNode.setChild(0, new Node(Token.IDENT, "myFunction"));
    Node expressionNode = new Node(Token.ASSIGN, Token.ASSIGN, new Node(Token.IDENT, "myVariable"), functionExpressionNode);
    scriptNode.setChild(2, expressionNode);

    Node ifStatementNode = new Node(Token.IF, Token.KEYWORD, "if", new Node(Token.IDENT, "x"), new Node(Token.BLOCK, new Node[0]));
    Node returnStatementNode = new Node(Token.RETURN, null, new Node(Token.LITERAL, 42));

    assertTrue(isHoistedFunctionDeclaration(functionDeclarationNode));
    assertFalse(isHoistedFunctionDeclaration(functionDeclarationNodeInFunction));
    assertFalse(isHoistedFunctionDeclaration(functionExpressionNode));
    assertFalse(isHoistedFunctionDeclaration(ifStatementNode));
    assertFalse(isHoistedFunctionDeclaration(returnStatementNode));
  }
}