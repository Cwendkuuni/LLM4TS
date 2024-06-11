import static org.junit.Assert.*;
import static com.google.javascript.jscomp.jsonml.NodeUtil.*;
import com.google.common.base.Preconditions;
import com.google.javascript.rhino.Node;

public class NodeUtilTest {

  Node validForInStatement;
  Node invalidForInStatement;
  Node validStatement;
  Node invalidStatement;
  Node validFunctionDeclaration;
  Node validHoistedFunctionDeclaration;
  Node invalidFunctionDeclaration;

  @org.junit.Before
  public void setUp() {
    // Initialize test nodes here
    validForInStatement = Node.newNode(Token.FOR, new Node[] {
        Node.newNode(Token.IDENT, "var"),
        Node.newNode(Token.IDENT, "i"),
        Node.newNode(Token.ASSIGN, null, new Node[] {
            Node.newNode(Token.IDENT, "array"),
            Node.newNode(Token.LBRACKET, null),
            Node.newNode(Token.NUMBER, 0),
            Node.newNode(Token.RBRACKET, null),
            Node.newNode(Token.SEMICOLON, null),
            Node.newNode(Token.KEYWORD, "in"),
            Node.newNode(Token.IDENT, "array"),
            Node.newNode(Token.LBRACE, null),
            Node.newNode(Token.RBRACE, null)
        }),
        Node.newNode(Token.SEMICOLON, null),
        Node.newNode(Token.IDENT, "item")
    });

    invalidForInStatement = Node.newNode(Token.FOR, new Node[] {
        Node.newNode(Token.IDENT, "var"),
        Node.newNode(Token.IDENT, "i"),
        Node.newNode(Token.ASSIGN, null, new Node[] {
            Node.newNode(Token.IDENT, "array"),
            Node.newNode(Token.LBRACKET, null),
            Node.newNode(Token.NUMBER, 0),
            Node.newNode(Token.RBRACKET, null),
            Node.newNode(Token.SEMICOLON, null),
            Node.newNode(Token.KEYWORD, "is"), // wrong keyword
            Node.newNode(Token.IDENT, "array"),
            Node.newNode(Token.LBRACE, null),
            Node.newNode(Token.RBRACE, null)
        }),
        Node.newNode(Token.SEMICOLON, null),
        Node.newNode(Token.IDENT, "item")
    });

    validStatement = Node.newNode(Token.ASSIGN, null, new Node[] {
        Node.newNode(Token.IDENT, "x"),
        Node.newNode(Token.NUMBER, 5)
    });

    invalidStatement = Node.newNode(Token.ASSIGN, null, new Node[] {
        Node.newNode(Token.IDENT, "x"),
        Node.newNode(Token.STRING, "Hello") // invalid type
    });

    validFunctionDeclaration = Node.newNode(Token.FUNCTION, new Node[] {
        Node.newNode(Token.IDENT, "myFunction"),
        Node.newNode(Token.LBRACE, null),
        Node.newNode(Token.RETURN, null, new Node[] {
            Node.newNode(Token.NUMBER, 10)
        }),
        Node.newNode(Token.RBRACE, null)
    });

    validHoistedFunctionDeclaration = Node.newNode(Token.FUNCTION, new Node[] {
        Node.newNode(Token.IDENT, "myFunction"),
        Node.newNode(Token.LBRACE, null),
        Node.newNode(Token.RETURN, null, new Node[] {
            Node.newNode(Token.NUMBER, 10)
        }),
        Node.newNode(Token.SEMICOLON, null), // hoist semicolon
        Node.newNode(Token.SCRIPT, null, new Node[] { validStatement })
    });

    invalidFunctionDeclaration = Node.newNode(Token.FUNCTION, new Node[] {
        Node.newNode(Token.IDENT, "myFunction"), // missing semicolon
        Node.newNode(Token.LBRACE, null),
        Node.newNode(Token.RETURN, null, new Node[] {
            Node.newNode(Token.NUMBER, 10)
        }),
        Node.newNode(Token.RBRACE, null)
    });
  }

  @org.junit.Test
  public void testIsForIn() {
    assertTrue(isForIn(validForInStatement));
    assertFalse(isForIn(validStatement));
    assertFalse(isForIn(invalidForInStatement));
  }

  @org.junit.Test
  public void testIsStatement() {
    assertTrue(isStatement(validStatement));
    assertTrue(isStatement(validForInStatement));
    assertFalse(isStatement(invalidStatement));
    assertFalse(isStatement(validFunctionDeclaration));
  }

  @org.junit.Test
  public void testIsFunctionDeclaration() {
    assertFalse(isFunctionDeclaration(validStatement));
    assertFalse(isFunctionDeclaration(invalidStatement));
    assertTrue(isFunctionDeclaration(validFunctionDeclaration));
    assertFalse(isFunctionDeclaration(invalidFunctionDeclaration));
  }

  @org.junit.Test
  public void testIsHoistedFunctionDeclaration() {
    assertFalse(isHoistedFunctionDeclaration(validFunctionDeclaration));
    assertTrue(isHoistedFunctionDeclaration(validHoistedFunctionDeclaration));
    assertFalse(isHoistedFunctionDeclaration(invalidFunctionDeclaration));
  }
}