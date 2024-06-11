import static org.junit.Assert.*;
import static com.google.javascript.jscomp.jsonml.NodeUtil.*;

import com.google.javascript.jscomp.testing.TopLevelTestBase;
import com.google.javascript.rhino.Node;

public class NodeUtilTest extends TopLevelTestBase {

  @org.junit.Test
  public void testIsForIn() {
    Node n = createNode("for (var i in array) {}");
    assertTrue(isForIn(n));

    n = createNode("for (var i = 0; i < 10; i++) {}");
    assertFalse(isForIn(n));

    n = createNode("for (var i in obj) { if (i === 'key') { break; } }");
    assertTrue(isForIn(n));
  }

  @org.junit.Test
  public void testIsStatement() {
    Node statement = createNode("if (true) {}");
    assertTrue(isStatement(statement));

    Node expression = createNode("2 + 2");
    assertFalse(isStatement(expression));

    Node functionExpression = createNode("function add(x, y) { return x + y; }");
    assertFalse(isStatement(functionExpression));

    Node functionDeclaration = createNode(
        "function add(x, y) {\n" +
        "  var result = x + y;\n" +
        "  return result;\n" +
        "}",
        null,
        null,
        Token.FUNCTION_DECLARATION,
        new String[]{"add"},
        null,
        new Node[0]);
    assertTrue(isStatement(functionDeclaration));
  }

  @org.junit.Test
  public void testIsFunctionDeclaration() {
    Node functionExpression = createNode(
        "function add(x, y) { return x + y; }",
        null,
        null,
        Token.FUNCTION,
        new String[0],
        null,
        new Node[0]);
    assertFalse(isFunctionDeclaration(functionExpression));

    Node functionDeclaration = createNode(
        "function add(x, y) {\n" +
        "  var result = x + y;\n" +
        "  return result;\n" +
        "}",
        null,
        null,
        Token.FUNCTION_DECLARATION,
        new String[]{"add"},
        null,
        new Node[0]);
    assertTrue(isFunctionDeclaration(functionDeclaration));
  }

  @org.junit.Test
  public void testIsHoistedFunctionDeclaration() {
    Node functionDeclarationInFunction = createNode(
        "function f() {\n" +
        "  function g() {}\n" +
        "}",
        null,
        null,
        Token.FUNCTION,
        null,
        null,
        new Node[0]);
    assertTrue(isHoistedFunctionDeclaration(functionDeclarationInFunction));

    Node functionDeclarationInScript = createNode(
        "function g() {}\n" +
        "function f() {}\n",
        null,
        null,
        Token.SCRIPT,
        null,
        null,
        new Node[0]);
    assertFalse(isHoistedFunctionDeclaration(functionDeclarationInScript));

    Node functionDeclarationInBlock = createNode(
        "function f() {\n" +
        "  function g() {}\n" +
        "}",
        null,
        null,
        Token.BLOCK,
        null,
        null,
        new Node[0]);
    assertFalse(isHoistedFunctionDeclaration(functionDeclarationInBlock));

    Node functionDeclarationInFunctionScope = createNode(
        "function f() {\n" +
        "  function g() {}\n" +
        "}",
        null,
        null,
        Token.FUNCTION,
        null,
        null,
        new Node[0]);
    assertFalse(isHoistedFunctionDeclaration(functionDeclarationInFunctionScope));
  }

  private Node createNode(String code, Node parent, Node previousSibling, int type, String[] identifiers, Node[] children, Node expression) {
    // Implementation of Node creation is omitted for brevity
  }
}