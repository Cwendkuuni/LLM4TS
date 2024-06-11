import static org.junit.Assert.*;
import static com.google.javascript.jscomp.jsonml.NodeUtil.*;
import com.google.javascript.rhino.*;

public class NodeUtilTest {

  Node createNode(String nodeType, int numChildren, Node... children) {
    Node node = new Node(Token.DUMMY);
    node.setType(new Token(nodeType));
    node.setLine(1);
    node.setColumn(1);
    node.setChildCount(numChildren);
    for (int i = 0; i < numChildren; i++) {
      node.addChild(children[i]);
    }
    return node;
  }

  @Test
  public void testIsForIn() {
    Node forNode = createNode(Token.FOR, 3);
    Node init = createNode(Token.VAR, 1, createNode(Token.IDENT, "i"));
    Node condition = createNode(Token.EQUALS, 2, createNode(Token.IDENT, "i"), createNode(Token.INT_LITERAL, 5));
    Node increment = createNode(Token.SEMICOLON, 0);
    forNode.setChild(0, init);
    forNode.setChild(1, condition);
    forNode.setChild(2, increment);

    assertTrue(isForIn(forNode));

    Node invalidForNode = createNode(Token.FOR, 3);
    assertFalse(isForIn(invalidForNode));

    Node ifNode = createNode(Token.IF, 3);
    Node ifCondition = createNode(Token.LOGICAL_OR, 2);
    ifNode.setChild(0, createNode(Token.IDENT, "condition1"));
    ifNode.setChild(1, ifCondition);
    ifCondition.setChild(0, createNode(Token.EQUALS, 2, createNode(Token.IDENT, "a"), createNode(Token.INT_LITERAL, 5)));
    ifCondition.setChild(1, createNode(Token.EQUALS, 2, createNode(Token.IDENT, "b"), createNode(Token.INT_LITERAL, 10)));

    assertFalse(isForIn(ifNode));
  }

  @Test
  public void testIsStatement() {
    Node statementNode = createNode(Token.PRINT, 1, createNode(Token.STRING_LITERAL, "Hello, World!"));

    assertTrue(isStatement(statementNode));

    Node expressionNode = createNode(Token.ASSIGN, 2, createNode(Token.IDENT, "a"), createNode(Token.MINUS, 2, createNode(Token.IDENT, "b"), createNode(Token.INT_LITERAL, 5)));

    assertFalse(isStatement(expressionNode));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node functionDeclaration = createNode(Token.FUNCTION, 2, createNode(Token.IDENT, "add"), createNode(Token.LPAREN, 0), createNode(Token.RPAREN, 0), createNode(Token.BLOCK, 0));

    assertTrue(isFunctionDeclaration(functionDeclaration));

    Node functionExpression = createNode(Token.FUNCTION, 1, createNode(Token.IDENT, "add"), createNode(Token.LPAREN, 0), createNode(Token.RPAREN, 0), createNode(Token.BLOCK, 0));

    assertFalse(isFunctionDeclaration(functionExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node script = createNode(Token.SCRIPT, 0);
    Node functionDeclaration = createNode(Token.FUNCTION, 2, createNode(Token.IDENT, "add"), createNode(Token.LPAREN, 0), createNode(Token.RPAREN, 0), createNode(Token.BLOCK, 0));

    script.addChild(functionDeclaration);

    assertTrue(isHoistedFunctionDeclaration(functionDeclaration));

    Node functionInsideFunction = createNode(Token.FUNCTION, 2, createNode(Token.IDENT, "subtract"), createNode(Token.LPAREN, 0), createNode(Token.RPAREN, 0), createNode(Token.BLOCK, 0));
    Node functionDeclarationInsideFunction = createNode(Token.FUNCTION, 2, createNode(Token.IDENT, "multiply"), createNode(Token.LPAREN, 0), createNode(Token.RPAREN, 0), createNode(Token.BLOCK, 0));
    functionInsideFunction.addChild(functionDeclarationInsideFunction);

    script.addChild(functionInsideFunction);

    assertFalse(isHoistedFunctionDeclaration(functionDeclarationInsideFunction));
  }

}