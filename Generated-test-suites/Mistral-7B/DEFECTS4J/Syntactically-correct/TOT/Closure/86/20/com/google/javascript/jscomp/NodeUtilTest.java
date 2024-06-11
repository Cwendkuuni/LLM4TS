import static org.junit.Assert.*;
import static com.google.javascript.jscomp.jsonml.NodeUtil.*;
import com.google.common.base.Preconditions;
import com.google.javascript.rhino.Node;

public class NodeUtilTest {

  @org.junit.Test
  public void testIsForIn() {
    Node forNode = new Node(Token.FOR, new int[]{}, new int[3]);
    Node notForNode = new Node(Token.IF, new int[]{}, new int[0]);

    // Test for a valid FOR-IN loop
    assertTrue(isForIn(forNode));

    // Test for a node that is not a FOR-IN loop
    assertFalse(isForIn(notForNode));
  }

  @org.junit.Test
  public void testIsStatement() {
    Node statementNode = new Node(Token.IF, new int[]{}, new int[0]);
    Node notStatementNode = new Node(Token.IDENT, new int[]{Token.VAR}, new int[0]);

    // Test for a valid statement
    assertTrue(isStatement(statementNode));

    // Test for a node that is not a statement
    assertFalse(isStatement(notStatementNode));
  }

  @org.junit.Test
  public void testIsFunctionDeclaration() {
    Node functionNode = new Node(Token.FUNCTION, new int[]{Token.ID}, new int[1]);
    Node notFunctionNode = new Node(Token.IF, new int[]{}, new int[0]);

    // Test for a valid function declaration
    assertTrue(isFunctionDeclaration(functionNode));

    // Test for a node that is not a function declaration
    assertFalse(isFunctionDeclaration(notFunctionNode));
  }

  @org.junit.Test
  public void testIsHoistedFunctionDeclaration() {
    Node hoistedFunctionNode = new Node(Token.FUNCTION, new int[]{Token.ID}, new int[1]);
    hoistedFunctionNode.setParent(new Node(Token.SCRIPT, new int[]{}, new int[0]));

    Node notHoistedFunctionNode = new Node(Token.FUNCTION, new int[]{Token.ID}, new int[1]);
    Node parentNode = new Node(Token.IF, new int[]{}, new int[0]);
    notHoistedFunctionNode.setParent(parentNode);

    // Test for a hoisted function declaration
    assertTrue(isHoistedFunctionDeclaration(hoistedFunctionNode));

    // Test for a function declaration that is not hoisted
    assertFalse(isHoistedFunctionDeclaration(notHoistedFunctionNode));
  }
}