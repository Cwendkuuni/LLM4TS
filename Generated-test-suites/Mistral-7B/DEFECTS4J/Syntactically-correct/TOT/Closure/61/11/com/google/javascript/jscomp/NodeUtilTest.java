import static org.junit.Assert.*;
import static com.google.javascript.jscomp.jsonml.NodeUtil.*;
import com.google.javascript.jscomp.testing.TopLevelTestBase;

public class NodeUtilTest extends TopLevelTestBase {

  @org.junit.Test
  public void testIsForIn() {
    Node node = createNode("for (var i in arr) {}");
    assertTrue(isForIn(node));

    node = createNode("for (var i = 0; i < arr.length; i++) {}");
    assertFalse(isForIn(node));

    node = createNode("for (var i = 0; arr.length; i++) {}"); // trailing semicolon
    assertFalse(isForIn(node));

    node = createNode("for (var i = 0; arr.length; ) {}"); // missing initialization
    assertFalse(isForIn(node));

    node = createNode("for (var i in arr) { if (condition) break; }");
    assertTrue(isForIn(node));
  }

  @org.junit.Test
  public void testIsStatement() {
    Node statement = createNode("{}");
    assertTrue(isStatement(statement));

    Node expression = createNode("1 + 1");
    assertFalse(isStatement(expression));

    Node functionExpression = createNode("function foo() {}");
    assertFalse(isStatement(functionExpression));

    Node hoistedFunctionDeclaration = createNode(
        "function foo() {}",
        createNode("{}") // empty script
    );
    assertTrue(isStatement(hoistedFunctionDeclaration));
  }

  @org.junit.Test
  public void testIsFunctionDeclaration() {
    Node functionDeclaration = createNode(
        "function foo() {}",
        createNode("{}") // empty script
    );
    assertTrue(isFunctionDeclaration(functionDeclaration));

    Node functionExpression = createNode("function foo() {}");
    assertFalse(isFunctionDeclaration(functionExpression));

    Node variableDeclaration = createNode("function foo() {} var bar = 1");
    assertFalse(isFunctionDeclaration(variableDeclaration));
  }

  @org.junit.Test
  public void testIsHoistedFunctionDeclaration() {
    Node hoistedFunctionDeclaration = createNode(
        "function foo() {}",
        createNode("{}") // empty script
    );
    assertTrue(isHoistedFunctionDeclaration(hoistedFunctionDeclaration));

    Node nonHoistedFunctionDeclaration = createNode(
        "function foo() {}",
        createNode(
            "function bar() {}",
            hoistedFunctionDeclaration
        )
    );
    assertFalse(isHoistedFunctionDeclaration(nonHoistedFunctionDeclaration));
  }

  private Node createNode(String nodeStr) {
    return createNode(nodeStr, null);
  }

  private Node createNode(String nodeStr, Node parent) {
    return parser().parseProgram(nodeStr).getBody().getStatements()[0];
  }
}