import static org.junit.Assert.*;
import static com.google.javascript.jscomp.jsonml.NodeUtil.*;
import com.google.common.base.Preconditions;
import com.google.javascript.rhino.*;

public class NodeUtilTest {

  @org.junit.Test
  public void testIsForIn() {
    Node nodeForIn = NodeOperations.createForInStatement("for (var i in arr) { }");
    assertTrue(isForIn(nodeForIn));

    Node nodeFor = NodeOperations.createForStatement("for (var i = 0; i < 10; i++) { }");
    assertFalse(isForIn(nodeFor));

    Node nodeIf = NodeOperations.createIfStatement("if (condition) { }");
    assertFalse(isForIn(nodeIf));

    Node nodeFunction = NodeOperations.createFunctionDeclaration("function foo() { }");
    assertFalse(isForIn(nodeFunction));
  }

  @org.junit.Test
  public void testIsStatement() {
    Node nodeStatement = NodeOperations.createExpressionStatement(
        NodeOperations.createNumberLiteral(10));
    assertTrue(isStatement(nodeStatement));

    Node nodeFunction = NodeOperations.createFunctionDeclaration("function foo() { }");
    assertTrue(isStatement(nodeFunction));

    Node nodeIf = NodeOperations.createIfStatement("if (condition) { }");
    assertTrue(isStatement(nodeIf));

    Node nodeFor = NodeOperations.createForStatement("for (var i = 0; i < 10; i++) { }");
    assertTrue(isStatement(nodeFor));

    Node nodeScript = NodeOperations.createScript(NodeOperations.createStringLiteral("console.log('Hello World')"));
    assertFalse(isStatement(nodeScript));
  }

  @org.junit.Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = NodeOperations.createFunctionDeclaration("function foo() { }");
    assertTrue(isFunctionDeclaration(nodeFunctionDeclaration));

    Node nodeFunctionExpression = NodeOperations.createExpressionStatement(
        NodeOperations.createFunctionExpression("function bar() { }"));
    assertFalse(isFunctionDeclaration(nodeFunctionExpression));
  }

  @org.junit.Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeScript = NodeOperations.createScript(
        "function foo() { console.log('foo') }",
        "function bar() { console.log('bar') }");

    Node nodeFoo = NodeOperations.getFirstChild(NodeOperations.getChild(nodeScript, 0));
    assertTrue(isHoistedFunctionDeclaration(nodeFoo));

    Node nodeBar = NodeOperations.getFirstChild(NodeOperations.getChild(nodeScript, 1));
    assertFalse(isHoistedFunctionDeclaration(nodeBar));
  }
}