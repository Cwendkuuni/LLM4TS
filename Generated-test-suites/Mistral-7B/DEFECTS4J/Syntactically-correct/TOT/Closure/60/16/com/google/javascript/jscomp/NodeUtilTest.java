import static org.junit.Assert.*;
import static com.google.javascript.jscomp.jsonml.NodeUtil.*;
import com.google.common.base.Preconditions;
import com.google.javascript.rhino.Node;

public class NodeUtilTest {

  Node validForInNode;
  Node invalidForInNode;
  Node validStatementNode;
  Node invalidStatementNode;
  Node validFunctionDeclarationNode;
  Node invalidFunctionDeclarationNode;
  Node validHoistedFunctionDeclarationNode;
  Node invalidHoistedFunctionDeclarationNode;

  @org.junit.Before
  public void setUp() {
    validForInNode = new Node(Token.FOR, null, 0, 3);
    validStatementNode = new Node(Token.IF, null, 0, 2);
    validFunctionDeclarationNode = new Node(Token.FUNCTION, null, 0, 2);
    validFunctionDeclarationNode.setIdentifier("testFunction");
    validHoistedFunctionDeclarationNode = new Node(Token.FUNCTION, null, 0, 2);
    validHoistedFunctionDeclarationNode.setIdentifier("testFunction");
    validHoistedFunctionDeclarationNode.setParent(new Node(Token.SCRIPT, null, 0, 3));

    invalidForInNode = new Node(Token.FOR, null, 0, 2);
    invalidStatementNode = new Node(Token.IDENT, "invalid", 0, 5);
    invalidFunctionDeclarationNode = new Node(Token.FUNCTION, null, 0, 1);
    invalidHoistedFunctionDeclarationNode = new Node(Token.FUNCTION, null, 0, 1);
    invalidHoistedFunctionDeclarationNode.setParent(new Node(Token.IDENT, "invalid", 0, 5));
  }

  @org.junit.Test
  public void testIsForIn() {
    assertTrue(isForIn(validForInNode));
    assertFalse(isForIn(validStatementNode));
    assertFalse(isForIn(validFunctionDeclarationNode));
    assertFalse(isForIn(validHoistedFunctionDeclarationNode));
    assertFalse(isForIn(invalidForInNode));
    assertFalse(isForIn(invalidStatementNode));
    assertFalse(isForIn(invalidFunctionDeclarationNode));
    assertFalse(isForIn(invalidHoistedFunctionDeclarationNode));
  }

  @org.junit.Test
  public void testIsStatement() {
    assertTrue(isStatement(validStatementNode));
    assertFalse(isStatement(validForInNode));
    assertFalse(isStatement(validFunctionDeclarationNode));
    assertFalse(isStatement(validHoistedFunctionDeclarationNode));
    assertFalse(isStatement(invalidStatementNode));
    assertFalse(isStatement(invalidForInNode));
    assertFalse(isStatement(invalidFunctionDeclarationNode));
    assertFalse(isStatement(invalidHoistedFunctionDeclarationNode));
  }

  @org.junit.Test
  public void testIsFunctionDeclaration() {
    assertFalse(isFunctionDeclaration(validForInNode));
    assertFalse(isFunctionDeclaration(validStatementNode));
    assertTrue(isFunctionDeclaration(validFunctionDeclarationNode));
    assertFalse(isFunctionDeclaration(validHoistedFunctionDeclarationNode));
    assertFalse(isFunctionDeclaration(invalidFunctionDeclarationNode));
    assertFalse(isFunctionDeclaration(invalidHoistedFunctionDeclarationNode));
    assertFalse(isFunctionDeclaration(invalidStatementNode));
    assertFalse(isFunctionDeclaration(invalidForInNode));
  }

  @org.junit.Test
  public void testIsHoistedFunctionDeclaration() {
    assertFalse(isHoistedFunctionDeclaration(validForInNode));
    assertFalse(isHoistedFunctionDeclaration(validStatementNode));
    assertFalse(isHoistedFunctionDeclaration(validFunctionDeclarationNode));
    assertTrue(isHoistedFunctionDeclaration(validHoistedFunctionDeclarationNode));
    assertFalse(isHoistedFunctionDeclaration(invalidFunctionDeclarationNode));
    assertFalse(isHoistedFunctionDeclaration(invalidHoistedFunctionDeclarationNode));
    assertFalse(isHoistedFunctionDeclaration(invalidStatementNode));
    assertFalse(isHoistedFunctionDeclaration(invalidForInNode));
  }
}