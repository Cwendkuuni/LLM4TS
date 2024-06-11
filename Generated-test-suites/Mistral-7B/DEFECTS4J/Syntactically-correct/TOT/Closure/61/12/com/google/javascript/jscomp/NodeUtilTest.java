import static org.junit.Assert.*;
import static com.google.javascript.jscomp.jsonml.NodeUtil.*;
import com.google.common.base.Preconditions;
import com.google.javascript.rhino.Node;

public class NodeUtilTest {

  @org.junit.Test
  public void testIsForIn() {
    Node forNode = Node.newForInStatement();
    Node simpleStatementNode = Node.newExpressionStatement(Node.createName("x"));

    assertTrue(isForIn(forNode));
    assertFalse(isForIn(simpleStatementNode));
  }

  @org.junit.Test
  public void testIsStatement() {
    Node scriptNode = Node.newScript(1, null);
    Node blockNode = Node.newBlock(1, null);
    Node labelNode = Node.newLabel();
    Node functionNode = Node.newFunction(1, null, null);
    Node expressionFunctionNode = Node.newExpressionFunction(1, null, null);

    assertTrue(isStatement(scriptNode));
    assertTrue(isStatement(blockNode));
    assertTrue(isStatement(labelNode));
    assertFalse(isStatement(functionNode));
    assertFalse(isStatement(expressionFunctionNode));
  }

  @org.junit.Test
  public void testIsFunctionDeclaration() {
    Node functionDeclaration = Node.newFunction(1, Node.newName("myFunction"), null);
    Node functionExpression = Node.newExpressionFunction(1, Node.newName("myFunction"), null);

    assertTrue(isFunctionDeclaration(functionDeclaration));
    assertFalse(isFunctionDeclaration(functionExpression));
  }

  @org.junit.Test
  public void testIsHoistedFunctionDeclaration() {
    Node scriptNode = Node.newScript(1, null);
    Node functionDeclaration = Node.newFunction(1, Node.newName("myFunction"), null);
    Node functionDeclarationInFunction = Node.newFunction(1, null, Node.newFunction(1, Node.newName("myFunction"), null));

    assertFalse(isHoistedFunctionDeclaration(functionDeclaration));
    assertTrue(isHoistedFunctionDeclaration(functionDeclarationInFunction));
  }

  @org.junit.Test(expected = AssertionError.class)
  public void testCheckStateFailure() {
    Node node = Node.newExpressionStatement(Node.createName("x"));
    Node parentNode = null;
    Preconditions.checkState(parentNode != null);
  }
}