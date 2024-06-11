import static org.junit.Assert.*;
import static com.google.javascript.jscomp.jsonml.NodeUtil.*;
import com.google.javascript.rhino.Node;

public class NodeUtilTest {

  @org.junit.Test
  public void testIsForIn() {
    Node node = Node.newForInStatement();
    assertTrue(isForIn(node));

    Node invalidNode = Node.newCallStatement();
    assertFalse(isForIn(invalidNode));
  }

  @org.junit.Test
  public void testIsStatement() {
    Node statementNode = Node.newExpressionStatement(Node.newIdentifier("x"));
    assertTrue(isStatement(statementNode));

    Node nonStatementNode = Node.newCallExpression(Node.newIdentifier("print"), Node.newString("Hello World"));
    assertFalse(isStatement(nonStatementNode));
  }

  @org.junit.Test
  public void testIsFunctionDeclaration() {
    Node functionNode = Node.newFunctionDeclaration("foo", Node.newArray(), Node.newBlock(Node.newExpressionStatement(Node.newIdentifier("x").setValue(Node.newNumber(5)))));
    assertTrue(isFunctionDeclaration(functionNode));

    Node functionExpressionNode = Node.newExpressionStatement(Node.newFunctionExpression(Node.newIdentifier("bar"), Node.newArray(), Node.newBlock(Node.newExpressionStatement(Node.newIdentifier("y").setValue(Node.newNumber(10))))));
    assertFalse(isFunctionDeclaration(functionExpressionNode));
  }

  @org.junit.Test
  public void testIsHoistedFunctionDeclaration() {
    Node hoistedFunctionNode = Node.newStatement(Node.newFunctionDeclaration("foo", Node.newArray(), Node.newBlock(Node.newExpressionStatement(Node.newIdentifier("x").setValue(Node.newNumber(5))), Node.newReturnStatement(Node.newIdentifier("x")))));
    assertTrue(isHoistedFunctionDeclaration(hoistedFunctionNode));

    Node nonHoistedFunctionNode = Node.newStatement(Node.newFunctionExpression(Node.newIdentifier("bar"), Node.newArray(), Node.newBlock(Node.newReturnStatement(Node.newIdentifier("y")))));
    assertFalse(isHoistedFunctionDeclaration(nonHoistedFunctionNode));
  }
}