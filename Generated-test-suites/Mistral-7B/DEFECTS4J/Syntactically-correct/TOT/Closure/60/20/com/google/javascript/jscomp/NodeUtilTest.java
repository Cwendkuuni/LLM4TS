import static org.junit.Assert.*;
import static com.google.javascript.jscomp.jsonml.NodeUtil.*;
import com.google.javascript.jscomp.testing.TopLevelTestRunner;
import com.google.javascript.rhino.Node;

public class NodeUtilTest {

  @org.junit.Test
  public void testIsForIn() {
    Node forNode = new Node(Token.FOR, new Node[]{}, new Node[]{new Node(Token.IDENT, "i"), new Node(Token.IDENT, "array"), new Node(Token.ASSIGN, null, new Node[]{new Node(Token.IDENT, "array"), new Node(Token.NUM, 0)}, null), new Node(Token.KEYWORD, "in"), new Node(Token.IDENT, "array"), new Node(Token.KEYWORD, "Array"), new Node(Token.LBRACE, null, null, null), new Node(Token.RBRACE, null, null, null)});
    assertTrue(isForIn(forNode));

    Node notForNode = new Node(Token.FUNCTION, new Node[]{new Node(Token.IDENT, "myFunction")}, new Node[]{new Node(Token.LBRACE, null, null, null)});
    assertFalse(isForIn(notForNode));
  }

  @org.junit.Test
  public void testIsStatement() {
    Node statementNode = new Node(Token.IF, new Node[]{new Node(Token.LBRACKET, null, null, null), new Node(Token.NUM, 5), new Node(Token.RBRACKET, null, null, null), new Node(Token.KEYWORD, "else"), new Node(Token.LBRACE, null, null, null), new Node(Token.RBRACE, null, null, null)});
    assertTrue(isStatement(statementNode));

    Node expressionNode = new Node(Token.PLUS, new Node[]{new Node(Token.NUM, 5), new Node(Token.NUM, 3)});
    assertFalse(isStatement(expressionNode));
  }

  @org.junit.Test
  public void testIsFunctionDeclaration() {
    Node functionDeclarationNode = new Node(Token.FUNCTION, new Node[]{new Node(Token.IDENT, "myFunction")}, new Node[]{new Node(Token.LBRACE, null, null, null), new Node(Token.RBRACE, null, null, null)});
    assertTrue(isFunctionDeclaration(functionDeclarationNode));

    Node functionExpressionNode = new Node(Token.FUNCTION, new Node[]{new Node(Token.IDENT, "myFunction")}, new Node[]{new Node(Token.LBRACE, null, null, null), new Node(Token.RBRACE, null, null, null)});
    assertFalse(isFunctionDeclaration(functionExpressionNode));
  }

  @org.junit.Test
  public void testIsHoistedFunctionDeclaration() {
    Node hoistedFunctionDeclarationNode = new Node(Token.FUNCTION, new Node[]{new Node(Token.IDENT, "myFunction")}, new Node[]{new Node(Token.LBRACE, null, null, null), new Node(Token.RBRACE, null, null, null)});
    Node scriptNode = new Node(Token.SCRIPT, new Node[]{hoistedFunctionDeclarationNode}, null);
    assertTrue(isHoistedFunctionDeclaration(hoistedFunctionDeclarationNode));

    Node functionNode = new Node(Token.FUNCTION, new Node[]{new Node(Token.IDENT, "myFunction")}, new Node[]{new Node(Token.LBRACE, null, null, null), new Node(Token.RBRACE, null, null, null)});
    Node functionExpressionNode = new Node(Token.ASSIGN, null, new Node[]{new Node(Token.IDENT, "myFunction"), functionNode}, null);
    Node statementNode = new Node(Token.ASSIGN, null, new Node[]{new Node(Token.IDENT, "x"), functionExpressionNode}, null);
    Node scriptWithFunctionExpressionNode = new Node(Token.SCRIPT, new Node[]{statementNode}, null);
    assertFalse(isHoistedFunctionDeclaration(functionNode));
  }

  @org.junit.Test
  public void testPreconditions() {
    Node nullParentNode = new Node(Token.NUM, 5);
    Preconditions.checkState(nullParentNode.getParent() != null, "Parent node should not be null");

    Node arrayNode = new Node(Token.ARRAY, new Node[0], null);
    Preconditions.checkState(arrayNode.getParent() != null, "Parent node should not be null");
  }

  @org.junit.Test
  public void testUnknownTypes() {
    Node unknownTypeNode = new Node(5, null, null, null); // Unknown type
    assertFalse(isForIn(unknownTypeNode));
    assertFalse(isStatement(unknownTypeNode));
    assertFalse(isFunctionDeclaration(unknownTypeNode));
    assertFalse(isHoistedFunctionDeclaration(unknownTypeNode));
  }

  @org.junit.Test
  public void testEmptyNodes() {
    Node emptyStatementNode = new Node(Token.EMPTY, null, null, null);
    assertFalse(isForIn(emptyStatementNode));
    assertFalse(isStatement(emptyStatementNode));
    assertFalse(isFunctionDeclaration(emptyStatementNode));
    assertFalse(isHoistedFunctionDeclaration(emptyStatementNode));

    Node emptyArrayNode = new Node(Token.ARRAY, new Node[0], null);
    assertFalse(isForIn(emptyArrayNode));
    assertFalse(isStatement(emptyArrayNode));
    assertFalse(isFunctionDeclaration(emptyArrayNode));
    assertFalse(isHoistedFunctionDeclaration(emptyArrayNode));
  }
}