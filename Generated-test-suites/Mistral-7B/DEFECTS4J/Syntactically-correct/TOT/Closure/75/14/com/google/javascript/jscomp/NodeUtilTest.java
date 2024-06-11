import static org.junit.Assert.*;
import static com.google.javascript.jscomp.jsonml.NodeUtil.*;
import com.google.javascript.rhino.*;

public class NodeUtilTest {

  @ org.junit.Test
  public void testIsForIn() {
    Node nodeForIn = Context.getCurrentContext().newNode("for (var i in array) { }");
    assertTrue(isForIn(nodeForIn));

    Node nodeFor = Context.getCurrentContext().newNode("for (var i = 0; i < 10; i++) { }");
    assertFalse(isForIn(nodeFor));

    Node nodeIf = Context.getCurrentContext().newNode("if (condition) { }");
    assertFalse(isForIn(nodeIf));
  }

  @ org.junit.Test
  public void testIsStatement() {
    Node nodeStatement = Context.getCurrentContext().newNode("statement;");
    assertTrue(isStatement(nodeStatement));

    Node nodeExpression = Context.getCurrentContext().newNode("expression;");
    assertFalse(isStatement(nodeExpression));

    Node nodeFunction = Context.getCurrentContext().newNode("function functionName() { }");
    assertTrue(isStatement(nodeFunction));
  }

  @ org.junit.Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = Context.getCurrentContext().newNode("function functionName() { }");
    assertTrue(isFunctionDeclaration(nodeFunctionDeclaration));

    Node nodeFunctionExpression = Context.getCurrentContext().newNode("function functionName() { }"); // function expression
    assertFalse(isFunctionDeclaration(nodeFunctionExpression));

    Node nodeVariableFunction = Context.getCurrentContext().newNode("var functionName = function() { }"); // variable function
    assertFalse(isFunctionDeclaration(nodeVariableFunction));
  }

  @ org.junit.Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeScript = Context.getCurrentContext().newNode("function functionName() { }");
    assertTrue(isHoistedFunctionDeclaration(nodeScript));

    Node nodeFunctionInFunction = Context.getCurrentContext().newNode("function outerFunction() { function functionName() { } }");
    assertTrue(isHoistedFunctionDeclaration(nodeFunctionInFunction));

    Node nodeFunctionInBlock = Context.getCurrentContext().newNode("function outerFunction() { { function functionName() { } } }");
    assertFalse(isHoistedFunctionDeclaration(nodeFunctionInBlock));
  }
}