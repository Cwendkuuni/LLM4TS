import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CollapseVariableDeclarations.*;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.rhino.NodeFactory;
import org.junit.Test;

public class CollapseVariableDeclarationsTest {

  @Test
  public void testSimpleCollapse() {
    String sourceCode = "function foo() {"
        + "  var a;"
        + "  var b = 1;"
        + "  var c = 2;"
        + "  var d = 3;"
        + "  return a, b + c * d;"
        + "}";

    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions(null);
    Node root = NodeFactory.createProgram(NodeFactory.createSourceFile("test.js", sourceCode));

    CollapseVariableDeclarations pass = new CollapseVariableDeclarations(compiler);
    pass.process(null, root);

    Node expectedResult = NodeFactory.createProgram(
        NodeFactory.createSourceFile("test.js",
            "function foo() {"
                + "  var a, b = 1, c = 2, d = 3;"
                + "  return a, b + c * d;"
                + "}"));

    assertEquals(expectedResult.toString(), root.toString());
  }

  @Test
  public void testCollapseAssigments() {
    String sourceCode = "function foo() {"
        + "  var a = true;"
        + "  var b = true;"
        + "  var c = true;"
        + "  return a, b, c;"
        + "}";

    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions(null);
    Node root = NodeFactory.createProgram(NodeFactory.createSourceFile("test.js", sourceCode));

    CollapseVariableDeclarations pass = new CollapseVariableDeclarations(compiler);
    pass.process(null, root);

    Node expectedResult = NodeFactory.createProgram(
        NodeFactory.createSourceFile("test.js",
            "function foo() {"
                + "  var c = b = a = true;"
                + "  return c, b, a;"
                + "}"));

    assertEquals(expectedResult.toString(), root.toString());
  }

  @Test(expected = AssertionError.class)
  public void testCollapseVariablesInsideFunction() {
    String sourceCode = "function foo() {"
        + "  function bar() {"
        + "    var a;"
        + "    var b = 1;"
        + "    var c = 2;"
        + "  }"
        + "  return bar();"
        + "}"
        + "foo();";

    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions(null);
    Node root = NodeFactory.createProgram(NodeFactory.createSourceFile("test.js", sourceCode));

    CollapseVariableDeclarations pass = new CollapseVariableDeclarations(compiler);
    pass.process(null, root);

    fail("Expected an AssertionError, but the test passed.");
  }

  @Test(expected = AssertionError.class)
  public void testCollapseVariablesInsideIfStatement() {
    String sourceCode = "function foo() {"
        + "  if (true) {"
        + "    var a;"
        + "    var b = 1;"
        + "  }"
        + "  return a, b;"
        + "}"
        + "foo();";

    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions(null);
    Node root = NodeFactory.createProgram(NodeFactory.createSourceFile("test.js", sourceCode));

    CollapseVariableDeclarations pass = new CollapseVariableDeclarations(compiler);
    pass.process(null, root);

    fail("Expected an AssertionError, but the test passed.");
  }

  @Test
  public void testEdgeCaseWithSingleDeclaration() {
    String sourceCode = "function foo() {"
        + "  var a;"
        + "  return a;"
        + "}"
        + "foo();";

    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions(null);
    Node root = NodeFactory.createProgram(NodeFactory.createSourceFile("test.js", sourceCode));

    CollapseVariableDeclarations pass = new CollapseVariableDeclarations(compiler);
    pass.process(null, root);

    Node expectedResult = NodeFactory.createProgram(
        NodeFactory.createSourceFile("test.js",
            "function foo() {"
                + "  var a;"
                + "  return a;"
                + "}"));

    assertEquals(expectedResult.toString(), root.toString());
  }

  @Test
  public void testEdgeCaseWithSingleAssignment() {
    String sourceCode = "function foo() {"
        + "  var a = 1;"
        + "  return a;"
        + "}"
        + "foo();";

    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions(null);
    Node root = NodeFactory.createProgram(NodeFactory.createSourceFile("test.js", sourceCode));

    CollapseVariableDeclarations pass = new CollapseVariableDeclarations(compiler);
    pass.process(null, root);

    Node expectedResult = NodeFactory.createProgram(
        NodeFactory.createSourceFile("test.js",
            "function foo() {"
                + "  var a = 1;"
                + "  return a;"
                + "}"));

    assertEquals(expectedResult.toString(), root.toString());
  }

  @Test
  public void testEdgeCaseWithMultipleDeclarations() {
    String sourceCode = "function foo() {"
        + "  var a;"
        + "  var b = 1;"
        + "  return a, b;"
        + "}"
        + "foo();";

    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions(null);
    Node root = NodeFactory.createProgram(NodeFactory.createSourceFile("test.js", sourceCode));

    CollapseVariableDeclarations pass = new CollapseVariableDeclarations(compiler);
    pass.process(null, root);

    Node expectedResult = NodeFactory.createProgram(
        NodeFactory.createSourceFile("test.js",
            "function foo() {"
                + "  var a, b = 1;"
                + "  return a, b;"
                + "}"));

    assertEquals(expectedResult.toString(), root.toString());
  }

  @Test
  public void testEdgeCaseWithMultipleAssignments() {
    String sourceCode = "function foo() {"
        + "  var a = 1;"
        + "  var b = 2;"
        + "  return a, b;"
        + "}"
        + "foo();";

    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions(null);
    Node root = NodeFactory.createProgram(NodeFactory.createSourceFile("test.js", sourceCode));

    CollapseVariableDeclarations pass = new CollapseVariableDeclarations(compiler);
    pass.process(null, root);

    Node expectedResult = NodeFactory.createProgram(
        NodeFactory.createSourceFile("test.js",
            "function foo() {"
                + "  var a = 1, b = 2;"
                + "  return a, b;"
                + "}"));

    assertEquals(expectedResult.toString(), root.toString());
  }

  @Test
  public void testEdgeCaseWithMultipleDeclarationsAndAssignments() {
    String sourceCode = "function foo() {"
        + "  var a;"
        + "  var b = 1;"
        + "  var c = 2;"
        + "  return a, b = b + 1, c = c + 1;"
        + "}"
        + "foo();";

    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions(null);
    Node root = NodeFactory.createProgram(NodeFactory.createSourceFile("test.js", sourceCode));

    CollapseVariableDeclarations pass = new CollapseVariableDeclarations(compiler);
    pass.process(null, root);

    Node expectedResult = NodeFactory.createProgram(
        NodeFactory.createSourceFile("test.js",
            "function foo() {"
                + "  var a, b = 1, c = 2;"
                + "  return a, b = b + 1, c = c + 1;"
                + "}"));

    assertEquals(expectedResult.toString(), root.toString());
  }

  @Test
  public void testEdgeCaseWithMultipleDeclarationsInsideFunction() {
    String sourceCode = "function foo() {"
        + "  function bar() {"
        + "    var a;"
        + "    var b = 1;"
        + "  }"
        + "  return bar();"
        + "}"
        + "foo();";

    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions(null);
    Node root = NodeFactory.createProgram(NodeFactory.createSourceFile("test.js", sourceCode));

    CollapseVariableDeclarations pass = new CollapseVariableDeclarations(compiler);
    pass.process(null, root);

    fail("Expected an AssertionError, but the test passed.");
  }

  @Test
  public void testEdgeCaseWithVariablesInsideIfStatement() {
    String sourceCode = "function foo() {"
        + "  if (true) {"
        + "    var a;"
        + "    var b = 1;"
        + "  }"
        + "  return a, b;"
        + "}"
        + "foo();";

    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions(null);
    Node root = NodeFactory.createProgram(NodeFactory.createSourceFile("test.js", sourceCode));

    CollapseVariableDeclarations pass = new CollapseVariableDeclarations(compiler);
    pass.process(null, root);

    fail("Expected an AssertionError, but the test passed.");
  }

  @Test
  public void testEdgeCaseWithVariablesInsideIfStatementWithAssignments() {
    String sourceCode = "function foo() {"
        + "  if (true) {"
        + "    var a = 1;"
        + "    var b = 2;"
        + "  }"
        + "  return a, b;"
        + "}"
        + "foo();";

    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions(null);
    Node root = NodeFactory.createProgram(NodeFactory.createSourceFile("test.js", sourceCode));

    CollapseVariableDeclarations pass = new CollapseVariableDeclarations(compiler);
    pass.process(null, root);

    Node expectedResult = NodeFactory.createProgram(
        NodeFactory.createSourceFile("test.js",
            "function foo() {"
                + "  if (true) {"
                + "    var a = 1, b = 2;"
                + "  }"
                + "  return a, b;"
                + "}"));

    assertEquals(expectedResult.toString(), root.toString());
  }
}