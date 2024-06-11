import static org.junit.Assert.*;
import static com.google.javascript.jscomp.jsonml.NodeUtil.*;
import com.google.common.base.Preconditions;
import com.google.javascript.rhino.Node;

public class NodeUtilTest {

  @org.junit.Test
  public void testIsForIn() {
    Node forInNode = Node.newForIn("for (var i in array) { ... }");
    assertTrue(isForIn(forInNode));

    Node notForInNode = Node.newExpressionStatement(Node.newExpression("foo()"));
    assertFalse(isForIn(notForInNode));

    Node invalidForInNode = Node.newExpressionStatement(Node.newExpression("for { i = 0; }"));
    assertFalse(isForIn(invalidForInNode));
  }

  @org.junit.Test
  public void testIsStatement() {
    Node statementNode = Node.newExpressionStatement(Node.newExpression("foo()"));
    assertTrue(isStatement(statementNode));

    Node notStatementNode = Node.newTypeDeclaration("function foo() { }");
    assertFalse(isStatement(notStatementNode));

    Node invalidNode = Node.newExpression("foo()");
    assertFalse(isStatement(invalidNode));
  }

  @org.junit.Test
  public void testIsFunctionDeclaration() {
    Node functionDeclarationNode = Node.newFunctionDeclaration("foo", new Node[0], Node.newBlock(new Node[0]));
    assertTrue(isFunctionDeclaration(functionDeclarationNode));

    Node functionExpressionNode = Node.newExpressionStatement(Node.newExpression("function foo() { }"));
    assertFalse(isFunctionDeclaration(functionExpressionNode));

    Node notFunctionNode = Node.newExpressionStatement(Node.newExpression("1 + 1"));
    assertFalse(isFunctionDeclaration(notFunctionNode));
  }

  @org.junit.Test
  public void testIsHoistedFunctionDeclaration() {
    Node hoistedFunctionDeclarationNode = Node.newFunctionDeclaration("foo", new Node[0], Node.newBlock(new Node[0]));
    Node functionDeclarationWithinFunctionNode = Node.newFunctionDeclaration("bar", new Node[0], Node.newBlock(new Node[0]));
    Node hoistedFunctionDeclarationWithinFunctionNode = Node.newFunctionDeclaration("baz", new Node[0], Node.newBlock(new Node[0]));

    Node functionNodeInScript = Node.newFunctionDeclaration("qux", new Node[0], Node.newBlock(new Node[0]));
    Node scriptNode = Node.newBlock(new Node[] { functionNodeInScript });

    // hoistedFunctionDeclaration
    assertTrue(isHoistedFunctionDeclaration(hoistedFunctionDeclarationNode));

    // functionDeclarationWithinFunction
    assertFalse(isHoistedFunctionDeclaration(functionDeclarationWithinFunctionNode));

    // hoistedFunctionDeclarationWithinFunction
    assertTrue(isHoistedFunctionDeclaration(hoistedFunctionDeclarationWithinFunctionNode));

    // functionNodeInScript
    assertFalse(isHoistedFunctionDeclaration(functionNodeInScript));
  }
}