import static org.junit.Assert.*;
import static com.google.javascript.jscomp.jsonml.NodeUtil.*;
import com.google.common.base.Preconditions;
import com.google.javascript.rhino.Node;

public class NodeUtilTest {

  Node validForIn;
  Node invalidForIn;
  Node validStatement;
  Node invalidStatement;
  Node validFunctionDeclaration;
  Node validHoistedFunctionDeclaration;
  Node invalidFunctionDeclaration;

  @org.junit.Before
  public void setup() {
    // Arrange - create test nodes
    validForIn = new Node(Token.FOR);
    validForIn.setChild(0, new Node(Token.IDENTIFIER, "var"));
    validForIn.setChild(1, new Node(Token.IDENTIFIER, "i"));
    validForIn.setChild(2, new Node(Token.LEFT_PAREN, null));
    validForIn.setChild(3, new Node(Token.IDENTIFIER, "myArray"));
    validForIn.setChild(4, new Node(Token.RIGHT_PAREN, null));
    validForIn.setChild(5, new Node(Token.BLOCK, null));

    invalidForIn = new Node(Token.FOR);
    invalidForIn.setChild(0, new Node(Token.IDENTIFIER, "var"));
    invalidForIn.setChild(1, new Node(Token.IDENTIFIER, "i"));
    invalidForIn.setChild(2, new Node(Token.LEFT_PAREN, null));
    invalidForIn.setChild(3, new Node(Token.IDENTIFIER, "myArray"));
    invalidForIn.setChild(4, new Node(Token.RIGHT_PAREN, null));
    invalidForIn.setChild(5, new Node(Token.ASSIGN, null));

    validStatement = new Node(Token.PRINT, null);
    validStatement.setChild(0, new Node(Token.STRING_LITERAL, "Hello World!"));

    invalidStatement = new Node(Token.PRINT, null);
    invalidStatement.setChild(0, new Node(Token.IDENTIFIER, "invalidFunction"));

    validFunctionDeclaration = new Node(Token.FUNCTION, null);
    validFunctionDeclaration.setChild(0, new Node(Token.IDENTIFIER, "myFunction"));
    validFunctionDeclaration.setChild(1, new Node(Token.BLOCK, null));

    validHoistedFunctionDeclaration = new Node(Token.FUNCTION, null);
    validHoistedFunctionDeclaration.setChild(0, new Node(Token.IDENTIFIER, "myFunctionHoisted"));
    validHoistedFunctionDeclaration.setChild(1, new Node(Token.BLOCK, null));
    validFunctionDeclaration.setParent(new Node(Token.SCRIPT, null));
    validHoistedFunctionDeclaration.setParent(0);

    invalidFunctionDeclaration = new Node(Token.FUNCTION, null);
    invalidFunctionDeclaration.setChild(0, new Node(Token.IDENTIFIER, "invalidFunction"));
    invalidFunctionDeclaration.setChild(1, new Node(Token.BLOCK, null));
    invalidFunctionDeclaration.setParent(new Node(Token.SCRIPT, null));
  }

  @org.junit.Test
  public void testIsForIn() {
    assertTrue(isForIn(validForIn));
    assertFalse(isForIn(validStatement));
    assertFalse(isForIn(validFunctionDeclaration));
  }

  @org.junit.Test
  public void testIsStatement() {
    assertTrue(isStatement(validStatement));
    assertTrue(isStatement(validFunctionDeclaration));
    assertFalse(isStatement(validForIn));
  }

  @org.junit.Test(expected = AssertionError.class)
  public void testIsStatement_NullParent() {
    Node invalidNode = new Node(Token.PRINT, null);
    assertFalse(isStatement(invalidNode));
    invalidNode.setParent(null);
    isStatement(invalidNode);
  }

  @org.junit.Test
  public void testIsFunctionDeclaration() {
    assertTrue(isFunctionDeclaration(validFunctionDeclaration));
    assertFalse(isFunctionDeclaration(validStatement));
    assertFalse(isFunctionDeclaration(validForIn));
  }

  @org.junit.Test
  public void testIsHoistedFunctionDeclaration() {
    assertTrue(isHoistedFunctionDeclaration(validHoistedFunctionDeclaration));
    assertFalse(isHoistedFunctionDeclaration(validFunctionDeclaration));
    assertFalse(isHoistedFunctionDeclaration(validStatement));
  }

  @org.junit.Test
  public void testIsFunctionDeclaration_NullParent() {
    Node invalidNode = new Node(Token.FUNCTION, null);
    assertFalse(isFunctionDeclaration(invalidNode));
    invalidNode.setParent(null);
    isFunctionDeclaration(invalidNode);
  }
}