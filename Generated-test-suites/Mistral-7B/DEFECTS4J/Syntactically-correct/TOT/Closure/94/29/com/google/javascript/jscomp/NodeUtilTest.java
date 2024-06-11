import static org.junit.Assert.*;
import static com.google.javascript.jscomp.jsonml.NodeUtil.*;
import com.google.javascript.rhino.Node;

public class NodeUtilTest {

  @org.junit.Test
  public void testIsForIn() {
    // Test case for a valid FOR-IN loop
    Node forNode = new Node(Token.FOR);
    forNode.addChild(new Node(Token.IDENT, "i"));
    forNode.addChild(new Node(Token.KEYWORD, "in"));
    forNode.addChild(new Node(Token.IDENT, "array"));

    assertTrue(isForIn(forNode));

    // Test case for a non-FOR-IN loop
    Node notForNode = new Node(Token.IF);
    assertTrue(!isForIn(notForNode));
  }

  @org.junit.Test
  public void testIsStatement() {
    // Test case for a valid statement
    Node statementNode = new Node(Token.IF, new Node(Token.IDENT, "x"), new Node(Token.PRINT, new Node(Token.STRING, "Hello World!")));

    assertTrue(isStatement(statementNode));

    // Test case for a non-statement node
    Node nonStatementNode = new Node(Token.ADD, new Node(Token.NUMBER, 1), new Node(Token.NUMBER, 2));

    assertFalse(isStatement(nonStatementNode));
  }

  @org.junit.Test
  public void testIsFunctionDeclaration() {
    // Test case for a valid function declaration
    Node functionNode = new Node(Token.FUNCTION, new Node(Token.IDENT, "myFunction"), new Node(Token.BLOCK, new Node(Token.RETURN, new Node(Token.NUMBER, 42))));

    assertTrue(isFunctionDeclaration(functionNode));

    // Test case for a non-function declaration node
    Node notFunctionNode = new Node(Token.IF, new Node(Token.IDENT, "x"), new Node(Token.PRINT, new Node(Token.STRING, "Hello World!")));

    assertFalse(isFunctionDeclaration(notFunctionNode));
  }

  @org.junit.Test
  public void testIsHoistedFunctionDeclaration() {
    // Test case for a hoisted function declaration
    Node hoistedFunctionNode = new Node(Token.SCRIPT, null, new Node(Token.FUNCTION, new Node(Token.IDENT, "myFunction"), new Node(Token.BLOCK, new Node(Token.RETURN, new Node(Token.NUMBER, 42)))));

    assertTrue(isHoistedFunctionDeclaration(hoistedFunctionNode));

    // Test case for a non-hoisted function declaration
    Node notHoistedFunctionNode = new Node(Token.BLOCK, new Node(Token.FUNCTION, new Node(Token.IDENT, "myFunction"), new Node(Token.BLOCK, new Node(Token.RETURN, new Node(Token.NUMBER, 42)))));

    assertFalse(isHoistedFunctionDeclaration(notHoistedFunctionNode));
  }
}