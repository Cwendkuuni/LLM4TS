import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeTraversal.AbstractPostOrderCallback.GatherCollapses.canBeRedeclared;
import static com.google.javascript.jscomp.NodeTraversal.AbstractPostOrderCallback.GatherCollapses.blacklistStubVars;
import static com.google.javascript.rhino.Node.VAR;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Token;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Set;

public class CollapseVariableDeclarationsTest {

  private AbstractCompiler compiler;
  private CollapseVariableDeclarations collapseVariableDeclarations;

  @Before
  public void setUp() {
    compiler = new AbstractCompiler();
    compiler.addPasses(new CompilerPass[]{new CollapseVariableDeclarations(compiler)});
  }

  @Test
  public void testCollapsingVariableDeclarations() {
    // Test a simple case of collapsing multiple variable declarations into one
    Node root = createNode(
        "function test() {" +
            "var a;" +
            "var b = 1;" +
            "var c = 2;" +
            "}",
        null);

    compiler.process(null, root);

    Node expected = createNode(
        "function test() {" +
            "var a, b = 1, c = 2;" +
            "}",
        null);

    assertCodeEquals(expected, root);
  }

  @Test
  public void testCollapsingAssignments() {
    // Test a simple case of collapsing multiple assignments into one
    Node root = createNode(
        "function test() {" +
            "a = true;" +
            "b = true;" +
            "var c = true;" +
            "}",
        null);

    compiler.process(null, root);

    Node expected = createNode(
        "function test() {" +
            "var c = b = a = true;" +
            "}",
        null);

    assertCodeEquals(expected, root);
  }

  @Test
  public void testCollapsingVariableDeclarationsWithinFunction() {
    // Test collapsing variable declarations within a function
    Node root = createNode(
        "function test() {" +
        "  var a;" +
        "  var b = 1;" +
        "  var c = 2;" +
        "  var d = 3;" +
        "  var e = 4;" +
        "}",
        null);

    compiler.process(null, root);

    Node expected = createNode(
        "function test() {" +
            "var a, b = 1, c = 2, d = 3, e = 4;" +
            "}",
        null);

    assertCodeEquals(expected, root);
  }

  @Test
  public void testCollapsingVariableDeclarationsWithinIfStatements() {
    // Test collapsing variable declarations within if statements
    Node root = createNode(
        "function test() {" +
            "if (true) {" +
            "  var a;" +
            "  var b = 1;" +
            "  var c = 2;" +
            "}" +
            "else {" +
            "  var d = 1;" +
            "  var e = 2;" +
            "}",
        null);

    compiler.process(null, root);

    Node expected = createNode(
        "function test() {" +
            "if (true) {" +
            "  var a, b = 1, c = 2;" +
            "}" +
            "else {" +
            "  var d, e = 1;" +
            "}",
        null);

    assertCodeEquals(expected, root);
  }

  @Test
  public void testCollapsingVariableDeclarationsWithinWhileStatements() {
    // Test collapsing variable declarations within while statements
    Node root = createNode(
        "function test() {" +
            "while (true) {" +
            "  var a;" +
            "  var b = 1;" +
            "  var c = 2;" +
            "}" +
            "}",
        null);

    compiler.process(null, root);

    Node expected = createNode(
        "function test() {" +
            "while (true) {" +
            "  var a, b = 1, c = 2;" +
            "}" +
            "}",
        null);

    assertCodeEquals(expected, root);
  }

  @Test
  public void testBlacklistedVariables() {
    // Test blacklisting variables that should not be collapsed
    Node root = createNode(
        "function test() {" +
        "  var a;" +
        "  function foo() {" +
        "    var a;" +
        "  }" +
        "}",
        null);

    compiler.process(null, root);

    Node expected = createNode(
        "function test() {" +
            "var a;" +
            "function foo() {" +
            "  var a;" +
            "}" +
            "}",
        null);

    assertCodeEquals(expected, root);
  }

  @Test
  public void testStubVariables() {
    // Test blacklisting stub variables
    Node root = createNode(
        "function test() {" +
        "  var a;" +
        "  if (true) {" +
        "    var a;" +
        "  }" +
        "}",
        null);

    compiler.process(null, root);

    Node expected = createNode(
        "function test() {" +
            "var a;" +
            "if (true) {" +
            "  var a;" +
            "}" +
            "}",
        null);

    assertCodeEquals(expected, root);
  }

  @Test(expected = AssertionError.class)
  public void testCollapsingVariablesInDifferentScopes() {
    // Test collapsing variables in different scopes should cause an error
    Node root = createNode(
        "function test() {" +
            "var a;" +
            "function foo() {" +
            "  var a;" +
            "}",
        null);

    compiler.process(null, root);
  }

  @Test(expected = AssertionError.class)
  public void testCollapsingVariablesWithinFunctionCalls() {
    // Test collapsing variables within function calls should cause an error
    Node root = createNode(
        "function test() {" +
          "var a;" +
          "foo(a);" +
          "}",
        null);

    compiler.process(null, root);
  }

  @Test(expected = AssertionError.class)
  public void testCollapsingVariablesWithinTemplateLiterals() {
    // Test collapsing variables within template literals should cause an error
    Node root = createNode(
        "function test() {" +
          "var a = \"${a}\";" +
          "}",
        null);

    compiler.process(null, root);
  }

  @Test(expected = AssertionError.class)
  public void testCollapsingVariablesWithinClassDeclarations() {
    // Test collapsing variables within class declarations should cause an error
    Node root = createNode(
        "class Test {" +
          "  var a;" +
          "}",
        null);

    compiler.process(null, root);
  }

  @Test(expected = AssertionError.class)
  public void testCollapsingVariablesInLoopInitializers() {
    // Test collapsing variables in loop initializers should cause an error
    Node root = createNode(
        "function test() {" +
          "for (var a, b = 0; b < 10; b++) {" +
          "}" +
          "}",
        null);

    compiler.process(null, root);
  }

  @Test(expected = AssertionError.class)
  public void testCollapsingVariablesInLoopUpdaters() {
    // Test collapsing variables in loop updaters should cause an error
    Node root = createNode(
        "function test() {" +
          "for (var a; a < 10; a++) {" +
          "}" +
          "}",
        null);

    compiler.process(null, root);
  }

  @Test(expected = AssertionError.class)
  public void testCollapsingVariablesInConditionalExpressions() {
    // Test collapsing variables in conditional expressions should cause an error
    Node root = createNode(
        "function test() {" +
          "var a = true;" +
          "if (a && b) {" +
          "}" +
          "}",
        null);

    compiler.process(null, root);
  }

  @Test(expected = AssertionError.class)
  public void testCollapsingVariablesInExpressions() {
    // Test collapsing variables in expressions should cause an error
    Node root = createNode(
        "function test() {" +
          "var a = 1;" +
          "var b = 2;" +
          "var c = a + b;" +
          "}",
        null);

    compiler.process(null, root);
  }

  @Test(expected = AssertionError.class)
  public void testCollapsingVariablesInSwitchStatements() {
    // Test collapsing variables in switch statements should cause an error
    Node root = createNode(
        "function test() {" +
          "switch (true) {" +
          "  case 1:" +
          "    var a = 1;" +
          "    break;" +
          "  case 2:" +
          "    var b = 2;" +
          "    break;" +
          "}" +
          "}",
        null);

    compiler.process(null, root);
  }

  @Test(expected = AssertionError.class)
  public void testCollapsingVariablesInCatchStatements() {
    // Test collapsing variables in catch statements should cause an error
    Node root = createNode(
        "function test() {" +
          "try {" +
          "  throw new Error('test');" +
          "}" +
          "catch (a) {" +
          "}" +
          "}",
        null);

    compiler.process(null, root);
  }

  @Test(expected = AssertionError.class)
  public void testCollapsingVariablesInFinallyStatements() {
    // Test collapsing variables in finally statements should cause an error
    Node root = createNode(
        "function test() {" +
          "try {" +
          "}" +
          "finally {" +
          "  var a;" +
          "}" +
          "}",
        null);

    compiler.process(null, root);
  }

  private Node createNode(String code, Node parent) {
    return compiler.parseTree(code, null, null, parent);
  }

  private void assertCodeEquals(Node expected, Node actual) {
    String expectedCode = NodePrinter.printNode(expected);
    String actualCode = NodePrinter.printNode(actual);
    assertEquals(expectedCode, actualCode);
  }
}

class NodePrinter {
  public static String printNode(Node node) {
    if (node == null) {
      return "null";
    }

    String nodeType = NodeUtil.getClassName(node.getClass());
    String nodeCode = "";

    switch (nodeType) {
      case "ExpressionStatement":
        nodeCode = "(" + printNode(node.getExpression()) + ")";
        break;
      case "StatementList":
        nodeCode = printStatements(node);
        break;
      case "VariableDeclarationList":
        nodeCode = printVariableDeclarations(node);
        break;
      case "VariableDeclaration":
        nodeCode = printVariableDeclaration(node);
        break;
      case "Identifier":
        nodeCode = node.getString();
        break;
      case "NumberLiteral":
        nodeCode = node.getNumber().toString();
        break;
      case "StringLiteral":
        nodeCode = node.getString();
        break;
      case "Boolean":
        nodeCode = node.getBoolean() ? "true" : "false";
        break;
      case "NullLiteral":
        nodeCode = "null";
        break;
      case "ObjectLiteral":
        nodeCode = printObjectLiteral(node);
        break;
      case "ArrayLiteral":
        nodeCode = printArrayLiteral(node);
        break;
      default:
        nodeCode = nodeType;
    }

    JSDocInfo jsDocInfo = node.getJSDocInfo();
    if (jsDocInfo != null) {
      nodeCode += " " + jsDocInfo.getDocumentation();
    }

    return nodeCode;
  }

  private static String printStatements(Node node) {
    StringBuilder sb = new StringBuilder();

    for (Node child = node.getFirstChild(); child != null; child = child.getNext()) {
      if (child.isStatementList()) {
        sb.append("\n").append(printStatements(child));
      } else {
        sb.append(printNode(child)).append(";");
      }
    }

    return sb.toString();
  }

  private static String printVariableDeclarations(Node node) {
    StringBuilder sb = new StringBuilder();

    List<Node> variableDeclarations = node.getChildren();
    for (int i = 0; i < variableDeclarations.size(); i++) {
      Node variableDeclaration = variableDeclarations.get(i);
      if (i > 0) {
        sb.append(", ");
      }
      sb.append(printVariableDeclaration(variableDeclaration));
    }

    return sb.toString();
  }

  private static String printVariableDeclaration(Node node) {
    StringBuilder sb = new StringBuilder();

    Node variable = node.getFirstChild();
    Node initializer = node.getLastChild();

    sb.append("var ");
    sb.append(printNode(variable));

    if (initializer != null) {
      sb.append(" = ");
      sb.append(printNode(initializer));
    }

    return sb.toString();
  }

  private static String printObjectLiteral(Node node) {
    StringBuilder sb = new StringBuilder();

    sb.append("{");
    List<Node> properties = node.getChildren();

    int index = 0;
    for (Node property : properties) {
      if (index > 0) {
        sb.append(", ");
      }

      sb.append(printNode(property.getFirstChild()));
      sb.append(": ");
      sb.append(printNode(property.getLastChild()));

      index++;
    }

    sb.append("}");

    return sb.toString();
  }

  private static String printArrayLiteral(Node node) {
    StringBuilder sb = new StringBuilder();

    List<Node> elements = node.getChildren();

    sb.append("[");

    for (int i = 0; i < elements.size(); i++) {
      if (i > 0) {
        sb.append(", ");
      }

      sb.append(printNode(elements.get(i)));
    }

    sb.append("]");

    return sb.toString();
  }
}