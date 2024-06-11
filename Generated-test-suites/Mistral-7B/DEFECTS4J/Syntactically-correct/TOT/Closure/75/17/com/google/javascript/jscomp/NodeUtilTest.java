import static org.junit.Assert.*;
import static com.google.javascript.jscomp.jsonml.NodeUtil.*;
import com.google.javascript.rhino.Node;

public class NodeUtilTest {

  @org.junit.Test
  public void testIsForIn() {
    Node nodeForIn = Node.newNode(Token.FOR, new int[]{}, new Node[]{
      Node.newNode(Token.IDENT, "var"),
      Node.newNode(Token.IDENT, "i"),
      Node.newNode(Token.LEFT_PAREN, new int[]{Token.LPAREN}, null),
      Node.newNode(Token.IDENT, "array"),
      Node.newNode(Token.RIGHT_PAREN, new int[]{Token.RPAREN}, null),
      Node.newNode(Token.COLON),
      Node.newNode(Token.ASSIGN, new int[]{Token.ASSIGN}, new Node[]{
        Node.newNode(Token.IDENT, "i"),
        Node.newNode(Token.GET_PROPERTY, new int[]{Token.GETPROP}, new Node[]{
          Node.newNode(Token.IDENT, "length"),
          Node.newNode(Token.IDENT, "array")
        })
      }),
      Node.newNode(Token.COLON),
      Node.newNode(Token.BLOCK, new int[]{Token.BLOCK}, null)
    });
    assertTrue(isForIn(nodeForIn));

    Node nodeNotForIn = Node.newNode(Token.IF, new int[]{Token.IF}, null);
    assertFalse(isForIn(nodeNotForIn));
  }

  @org.junit.Test
  public void testIsStatement() {
    Node nodeStatement = Node.newNode(Token.ASSIGN, new int[]{Token.ASSIGN}, new Node[]{
      Node.newNode(Token.IDENT, "x"),
      Node.newNode(Token.NUM, 42)
    });
    assertTrue(isStatement(nodeStatement));

    Node nodeNotStatement = Node.newNode(Token.ADD, new int[]{Token.PLUS}, new Node[]{
      Node.newNode(Token.IDENT, "x"),
      Node.newNode(Token.NUM, 42)
    });
    assertFalse(isStatement(nodeNotStatement));
  }

  @org.junit.Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = Node.newNode(Token.FUNCTION, new int[]{Token.FUNCTION}, new Node[]{
      Node.newNode(Token.IDENT, "myFunction"),
      Node.newNode(Token.BLOCK, new int[]{Token.BLOCK}, null),
      Node.newNode(Token.RIGHT_BRACE, new int[]{Token.RBRAKE}, null)
    });
    assertTrue(isFunctionDeclaration(nodeFunctionDeclaration));

    Node nodeFunctionExpression = Node.newNode(Token.FUNCTION, new int[]{Token.FUNCTION}, new Node[]{
      Node.newNode(Token.IDENT, "myFunction"),
      Node.newNode(Token.LEFT_PAREN, new int[]{Token.LPAREN}, null),
      Node.newNode(Token.RIGHT_PAREN, new int[]{Token.RPAREN}, null),
      Node.newNode(Token.BLOCK, new int[]{Token.BLOCK}, null),
      Node.newNode(Token.RIGHT_BRACE, new int[]{Token.RBRAKE}, null)
    });
    assertFalse(isFunctionDeclaration(nodeFunctionExpression));
  }

  @org.junit.Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeHoistedFunctionDeclaration = Node.newNode(Token.FUNCTION, new int[]{Token.FUNCTION}, null);
    nodeHoistedFunctionDeclaration.setParent(Node.newNode(Token.SCRIPT, new int[]{Token.SCRIPT}, null));
    nodeHoistedFunctionDeclaration.setLine(1);
    Node body = Node.newNode(Token.BLOCK, new int[]{Token.BLOCK}, null);
    body.setLine(2);
    nodeHoistedFunctionDeclaration.setFirstChild(Node.newNode(Token.IDENT, "myFunction"), 0);
    nodeHoistedFunctionDeclaration.setLastChild(body);

    assertTrue(isHoistedFunctionDeclaration(nodeHoistedFunctionDeclaration));

    Node nodeNotHoistedFunctionDeclaration = Node.newNode(Token.FUNCTION, new int[]{Token.FUNCTION}, null);
    nodeNotHoistedFunctionDeclaration.setParent(Node.newNode(Node.newNode(Token.FUNCTION, new int[]{Token.FUNCTION}, null), 0));
    Node body2 = Node.newNode(Token.BLOCK, new int[]{Token.BLOCK}, null);
    body2.setLine(2);
    nodeNotHoistedFunctionDeclaration.setFirstChild(Node.newNode(Token.IDENT, "myFunction"), 0);
    nodeNotHoistedFunctionDeclaration.setLastChild(body2);

    assertFalse(isHoistedFunctionDeclaration(nodeNotHoistedFunctionDeclaration));
  }
}