import static org.junit.Assert.*;
import static com.google.javascript.jscomp.jsonml.NodeUtil.*;
import com.google.common.base.Preconditions;
import com.google.javascript.rhino.Node;

public class NodeUtilTest {

  @org.junit.Test
  public void testIsForIn() {
    Node nodeForIn = Node.newForIn("for (var i in arr) { }");
    assertTrue(isForIn(nodeForIn));

    Node nodeFor = Node.newFor("for (var i = 0; i < arr.length; i++) { }");
    assertFalse(isForIn(nodeFor));

    Node nodeIf = Node.newIf("if (condition) { }");
    assertFalse(isForIn(nodeIf));

    Node nodeFunction = Node.newFunction("function foo() { }");
    assertFalse(isForIn(nodeFunction));
  }

  @org.junit.Test
  public void testIsStatement() {
    Node nodeStatement = Node.newPrint("print('Hello World');");
    assertTrue(isStatement(nodeStatement));

    Node nodeExpression = Node.newIdent("x");
    assertFalse(isStatement(nodeExpression));

    Node nodeFor = Node.newFor("for (var i = 0; i < arr.length; i++) { }");
    assertTrue(isStatement(nodeFor.getStatement()));

    Node nodeFunction = Node.newFunction("function foo() { }");
    assertFalse(isStatement(nodeFunction));
  }

  @org.junit.Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = Node.newFunctionDeclaration("function foo() { }");
    assertTrue(isFunctionDeclaration(nodeFunctionDeclaration));

    Node nodeFunctionExpression = Node.newFunctionExpression("function foo() { }");
    assertFalse(isFunctionDeclaration(nodeFunctionExpression));

    Node nodeIf = Node.newIf("if (condition) { function foo() { } }");
    assertFalse(isFunctionDeclaration(nodeIf));
  }

  @org.junit.Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeScript = Node.newScript("function foo() { }");
    assertTrue(isHoistedFunctionDeclaration(nodeScript));

    Node nodeFunction = Node.newFunctionDeclaration("function foo() { }");
    Node nodeFunctionCall = Node.newCall(Node.newIdent("foo"), null);
    Node nodeBlock = Node.newBlock(nodeFunctionCall);
    Node nodeScript2 = Node.newScript(nodeBlock);
    assertFalse(isHoistedFunctionDeclaration(nodeFunction));
    assertFalse(isHoistedFunctionDeclaration(nodeFunctionCall));
    assertTrue(isHoistedFunctionDeclaration(nodeScript2));
  }
}