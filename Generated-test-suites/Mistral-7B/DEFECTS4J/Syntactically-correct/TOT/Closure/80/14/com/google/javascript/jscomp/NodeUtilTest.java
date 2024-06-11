import static org.junit.Assert.*;
import static com.google.javascript.jscomp.jsonml.NodeUtil.*;
import com.google.javascript.rhino.*;

public class NodeUtilTest {

  @org.junit.Test
  public void testIsForIn() {
    Node forNode = Context.enterWorld().createNodeFromString("for (var i in array) {}", "test");
    assertTrue("Incorrect identification of FOR-IN loop", isForIn(forNode));

    Node forStatement = Context.enterWorld().createNodeFromString("for (var i = 0; i < 10; i++) {}", "test");
    assertFalse("Identification of non-FOR-IN loop as FOR-IN", isForIn(forStatement));

    Node ifStatement = Context.enterWorld().createNodeFromString("if (true) {}", "test");
    assertFalse("Identification of IF statement as FOR-IN", isForIn(ifStatement));
  }

  @org.junit.Test
  public void testIsStatement() {
    Node scriptNode = Context.enterWorld().createNodeFromString("// Empty script", "test");
    assertTrue("Misidentification of empty script as non-statement", isStatement(scriptNode));

    Node functionExpression = Context.enterWorld().createNodeFromString("function foo() {}", "test");
    assertFalse("Identification of function expression as statement", isStatement(functionExpression));

    Node functionDeclaration = Context.enterWorld().createNodeFromString("function foo() {}", "test");
    assertTrue("Misidentification of function declaration as non-statement", isStatement(functionDeclaration));
  }

  @org.junit.Test
  public void testIsFunctionDeclaration() {
    Node functionExpression = Context.enterWorld().createNodeFromString("function foo() {}", "test");
    assertFalse("Misidentification of function expression as declaration", isFunctionDeclaration(functionExpression));

    Node functionDeclaration = Context.enterWorld().createNodeFromString("function foo() {}", "test");
    assertTrue("Identification of non-declaration as declaration", isFunctionDeclaration(functionDeclaration));
  }

  @org.junit.Test
  public void testIsHoistedFunctionDeclaration() {
    Node globalFunction = Context.enterWorld().createNodeFromString("function foo() {}", "test");
    assertFalse("Identification of non-hoisted function as hoisted", isHoistedFunctionDeclaration(globalFunction));

    Node functionDeclarationWithinFunction = Context.enterWorld().createNodeFromString(
        "function foo() {\n" +
        "  function bar() {\n" +
        "  }\n" +
        "}", "test");
    assertFalse("Identification of inner function as hoisted", isHoistedFunctionDeclaration(functionDeclarationWithinFunction));

    Node hoistedFunctionDeclaration = Context.enterWorld().createNodeFromString(
        "function foo() {\n" +
        "}\n" +
        "function bar() {\n" +
        "}", "test");
    assertTrue("Identification of non-hoisted function as non-hoisted", isHoistedFunctionDeclaration(hoistedFunctionDeclaration));
  }
}