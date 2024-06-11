import static org.junit.Assert.*;
import static com.google.javascript.jscomp.jsonml.NodeUtil.*;
import com.google.javascript.rhino.Node;

public class NodeUtilTest {

  @org.junit.Test
  public void testIsForIn() {
    Node forNode = new Node(Token.FOR, null);
    forNode.setChild(0, new Node(Token.SEMICOLON, null));
    forNode.setChild(1, new Node(Token.IDENT, "var"));
    forNode.setChild(2, new Node(Token.IDENT, "i"));
    forNode.setChild(3, new Node(Token.KEYWORD, "in"));
    Node array = new Node(Token.ARRAY_LITERAL, null);
    Node element = new Node(Token.OBJECT_LITERAL, null);
    array.setChild(0, element);

    assertTrue(isForIn(forNode));
    assertFalse(isForIn(new Node(Token.IF, null)));
    assertFalse(isForIn(new Node(Token.RETURN, null)));
  }

  @org.junit.Test
  public void testIsStatement() {
    Node statement = new Node(Token.ASSIGN, null);
    Node parentScript = new Node(Token.SCRIPT, null);
    parentScript.setChild(0, statement);

    assertTrue(isStatement(statement));
    assertFalse(isStatement(new Node(Token.IDENT, "var")));
  }

  @org.junit.Test
  public void testIsFunctionDeclaration() {
    Node functionDeclaration = new Node(Token.FUNCTION, null);
    Node name = new Node(Token.IDENT, "myFunction");
    functionDeclaration.setChild(0, name);
    Node body = new Node(Token.BLOCK, null);
    functionDeclaration.setChild(1, body);

    Node parentScript = new Node(Token.SCRIPT, null);
    parentScript.setChild(0, functionDeclaration);

    assertTrue(isFunctionDeclaration(functionDeclaration));
    assertFalse(isFunctionDeclaration(new Node(Token.FUNCTION, null)));
  }

  @org.junit.Test
  public void testIsHoistedFunctionDeclaration() {
    Node hoistedFunctionDeclaration = new Node(Token.FUNCTION, null);
    Node name = new Node(Token.IDENT, "myFunction");
    hoistedFunctionDeclaration.setChild(0, name);
    Node body = new Node(Token.BLOCK, null);
    hoistedFunctionDeclaration.setChild(1, body);

    Node parentFunction = new Node(Token.FUNCTION, null);
    parentFunction.setChild(0, hoistedFunctionDeclaration);

    Node parentScript = new Node(Token.SCRIPT, null);
    parentScript.setChild(0, parentFunction);

    assertTrue(isHoistedFunctionDeclaration(hoistedFunctionDeclaration));
    assertFalse(isHoistedFunctionDeclaration(new Node(Token.FUNCTION, null)));
  }
}