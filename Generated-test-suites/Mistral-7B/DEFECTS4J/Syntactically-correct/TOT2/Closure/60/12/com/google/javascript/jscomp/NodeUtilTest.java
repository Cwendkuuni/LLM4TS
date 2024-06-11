import static org.junit.Assert.*;
import com.google.javascript.rhino.Node;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node forNode = new Node(Token.FOR);
    Node forNodeWithThreeChildren = new Node(Token.FOR, new Node[] { new Node(Token.IDENT, "i"), new Node(Token.EQUALS), new Node(Token.IDENT, "myArray") });
    assertTrue(NodeUtil.isForIn(forNodeWithThreeChildren));
    assertFalse(NodeUtil.isForIn(forNode));
  }

  @Test
  public void testIsStatement() {
    Node statementNode = new Node(Token.PRINT, new Node(Token.STRING_LITERAL, "Hello World"));
    assertTrue(NodeUtil.isStatement(statementNode));

    Node expressionNode = new Node(Token.IDENT, "x");
    assertFalse(NodeUtil.isStatement(expressionNode));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node functionNode = new Node(Token.FUNCTION, new Node[] { new Node(Token.IDENT, "myFunction"), new Node(Token.BLOCK) });
    assertTrue(NodeUtil.isFunctionDeclaration(functionNode));

    Node functionExpressionNode = new Node(Token.FUNCTION, new Node[] { new Node(Token.IDENT, "myFunction"), new Node(Token.BLOCK), new Node(Token.IDENT, "x") });
    assertFalse(NodeUtil.isFunctionDeclaration(functionExpressionNode));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node hoistedFunctionNode = new Node(Token.FUNCTION, new Node[] { new Node(Token.IDENT, "myFunction") }, new Node(Token.BLOCK));
    Node functionNodeWithinFunction = new Node(Token.FUNCTION, new Node[] { new Node(Token.IDENT, "myFunction"), new Node(Token.BLOCK) }, new Node(Token.FUNCTION, new Node[] { new Node(Token.IDENT, "outerFunction") }, new Node(Token.BLOCK)));

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(hoistedFunctionNode));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(functionNodeWithinFunction));
  }
}