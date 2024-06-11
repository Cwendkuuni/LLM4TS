import static org.junit.Assert.*;
import com.google.javascript.rhino.Node;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node forNode = new Node(Token.FOR);
    Node forNodeWithThreeChildren = new Node(Token.FOR, new Node[]{new Node(Token.IDENTIFIER), new Node(Token.IDENTIFIER), new Node(Token.SEMICOLON)});
    assertTrue(NodeUtil.isForIn(forNodeWithThreeChildren));
    assertFalse(NodeUtil.isForIn(forNode));
  }

  @Test
  public void testIsStatement() {
    Node statementNode = new Node(Token.PRINT, new Node[]{new Node(Token.STRING_LITERAL, "Hello World")});
    Node expressionNode = new Node(Token.ADD, new Node[]{new Node(Token.NUMBER_LITERAL, 5), new Node(Token.NUMBER_LITERAL, 3)});
    assertTrue(NodeUtil.isStatement(statementNode));
    assertFalse(NodeUtil.isStatement(expressionNode));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node functionDeclaration = new Node(Token.FUNCTION, new Node[]{new Node(Token.IDENTIFIER, "myFunction")}, new Node[]{new Node(Token.BLOCK, new Node[]{})});
    assertTrue(NodeUtil.isFunctionDeclaration(functionDeclaration));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node scriptNode = new Node(Token.SCRIPT, new Node[]{new Node(Token.FUNCTION, new Node[]{new Node(Token.IDENTIFIER, "myFunction")}, new Node[]{new Node(Token.BLOCK, new Node[]{new Node(Token.RETURN, new Node[]{new Node(Token.NUMBER_LITERAL, 42)})})})});
    Node functionDeclaration = new Node(Token.FUNCTION, new Node[]{new Node(Token.IDENTIFIER, "myFunction")}, new Node[]{new Node(Token.BLOCK, new Node[]{})});
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(scriptNode.getFirstChild()));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(functionDeclaration));
  }
}