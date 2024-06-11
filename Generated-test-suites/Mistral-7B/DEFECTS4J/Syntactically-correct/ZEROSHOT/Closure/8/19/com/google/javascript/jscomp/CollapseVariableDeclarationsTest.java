import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeTraversal.AbstractPostOrderCallback.Scope.Var.*;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.Scope;

import java.util.List;
import java.util.Set;

/**
 * Tests for the CollapseVariableDeclarations class.
 */
public class CollapseVariableDeclarationsTest {

  private AbstractCompiler compiler;

  @Before
  public void setUp() {
    CompilerOptions options = new CompilerOptions(null);
    compiler = new AbstractCompiler(options);
  }

  /**
   * Test collapsing multiple simple variable declarations.
   */
  @Test
  public void testCollapsingSimpleVariables() {
    String jsCode = "function foo() {\n" +
        "  var a;\n" +
        "  var b = 1;\n" +
        "  var c = 2;\n" +
        "  var d;\n" +
        "}";

    Node root = parser(jsCode);
    CompilerPass pass = new CollapseVariableDeclarations(compiler);
    pass.process(null, root);
    assertEquals("var a, b = 1, c = 2, d;", root.toSource(Printer.plainJs).replaceAll("\\s+", " "));
  }

  /**
   * Test collapsing multiple variable declarations with initial values.
   */
  @Test
  public void testCollapsingVariablesWithInitialValues() {
    String jsCode = "function foo() {\n" +
        "  var a = 1;\n" +
        "  var b = 2;\n" +
        "  var c = 3;\n" +
        "}";

    Node root = parser(jsCode);
    CompilerPass pass = new CollapseVariableDeclarations(compiler);
    pass.process(null, root);
    assertEquals("var a = 1, b = 2, c = 3;", root.toSource(Printer.plainJs).replaceAll("\\s+", " "));
  }

  /**
   * Test collapsing multiple assignments to the same variable.
   */
  @Test
  public void testCollapsingMultipleAssignmentsToSameVariable() {
    String jsCode = "function foo() {\n" +
        "  var a;\n" +
        "  a = true;\n" +
        "  a = false;\n" +
        "}";

    Node root = parser(jsCode);
    CompilerPass pass = new CollapseVariableDeclarations(compiler);
    pass.process(null, root);
    assertEquals("var a;", root.toSource(Printer.plainJs).replaceAll("\\s+", " "));
    assertEquals("a = false;", root.getChild(0).toSource(Printer.plainJs).replaceAll("\\s+", " "));
  }

  /**
   * Test collapsing multiple assignments to different variables.
   */
  @Test
  public void testCollapsingMultipleAssignmentsToDifferentVariables() {
    String jsCode = "function foo() {\n" +
        "  var a = 1;\n" +
        "  var b = 2;\n" +
        "  a = true;\n" +
        "  b = false;\n" +
        "}";

    Node root = parser(jsCode);
    CompilerPass pass = new CollapseVariableDeclarations(compiler);
    pass.process(null, root);
    assertEquals("var a = 1, b = 2;", root.toSource(Printer.plainJs).replaceAll("\\s+", " "));
    assertEquals("a = true;", root.getChild(0).getChild(0).toSource(Printer.plainJs).replaceAll("\\s+", " "));
    assertEquals("b = false;", root.getChild(1).toSource(Printer.plainJs).replaceAll("\\s+", " "));
  }

  /**
   * Test collapsing multiple variable declarations inside a function.
   */
  @Test
  public void testCollapsingVariablesInsideFunction() {
    String jsCode = "function foo() {\n" +
        "  function bar() {\n" +
        "    var a;\n" +
        "    var b = 1;\n" +
        "    var c = 2;\n" +
        "  }\n" +
        "}";

    Node root = parser(jsCode);
    CompilerPass pass = new CollapseVariableDeclarations(compiler);
    pass.process(null, root);
    // The function definition is not collapsed since it can't be grouped with other variables.
    assertEquals("function bar() {\n" +
        "  var a, b = 1, c = 2;\n" +
        "}", root.getChild(0).toSource(Printer.plainJs).replaceAll("\\s+", " "));
    // The variables inside the function are grouped together.
    assertEquals("var a, b = 1, c = 2;", root.getChild(0).getChild(0).toSource(Printer.plainJs).replaceAll("\\s+", " "));
  }

  /**
   * Test collapsing multiple variable declarations in a for loop.
   */
  @Test
  public void testCollapsingVariablesInsideForLoop() {
    String jsCode = "function foo() {\n" +
        "  for (var i = 0, j = 1, k = 2; i < 5; i++) {\n" +
        "    // Some code here.\n" +
        "  }\n" +
        "}";

    Node root = parser(jsCode);
    CompilerPass pass = new CollapseVariableDeclarations(compiler);
    pass.process(null, root);
    // The variables inside the for loop are grouped together.
    assertEquals("var i = 0, j = 1, k = 2;", root.getChild(0).toSource(Printer.plainJs).replaceAll("\\s+", " "));
  }

  /**
   * Test collapsing multiple variable declarations in a switch statement.
   */
  @Test
  public void testCollapsingVariablesInsideSwitchStatement() {
    String jsCode = "function foo() {\n" +
        "  var a;\n" +
        "  switch (a) {\n" +
        "    case 1:\n" +
        "      var b = 1;\n" +
        "      break;\n" +
        "    case 2:\n" +
        "      var c = 2;\n" +
        "      break;\n" +
        "  }\n" +
        "}";

    Node root = parser(jsCode);
    CompilerPass pass = new CollapseVariableDeclarations(compiler);
    pass.process(null, root);
    // The variables inside the switch statement are grouped together.
    assertEquals("var a;", root.getChild(0).toSource(Printer.plainJs).replaceAll("\\s+", " "));
    assertEquals("var b = 1, c = 2;", root.getChild(3).toSource(Printer.plainJs).replaceAll("\\s+", " "));
  }

  /**
   * Test collapsing multiple variable declarations inside a conditional statement.
   */
  @Test
  public void testCollapsingVariablesInsideConditionalStatement() {
    String jsCode = "function foo() {\n" +
        "  if (true) {\n" +
        "    var a;\n" +
        "    var b = 1;\n" +
        "  } else {\n" +
        "    var c = 2;\n" +
        "  }\n" +
        "}";

    Node root = parser(jsCode);
    CompilerPass pass = new CollapseVariableDeclarations(compiler);
    pass.process(null, root);
    // The variables inside the if statement are grouped together.
    assertEquals("if (true) {\n" +
        "  var a, b = 1;\n" +
        "}", root.getChild(0).toSource(Printer.plainJs).replaceAll("\\s+", " "));
    // The variables inside the else statement are grouped together.
    assertEquals("} else {\n" +
        "  var c = 2;\n" +
        "}", root.getChild(3).toSource(Printer.plainJs).replaceAll("\\s+", " "));
  }

  /**
   * Test that variables inside a for-in loop are not collapsed.
   */
  @Test
  public void testVariablesInsideForInLoopAreNotCollapsed() {
    String jsCode = "function foo(obj) {\n" +
        "  for (var key in obj) {\n" +
        "    var value = obj[key];\n" +
        "  }\n" +
        "}";

    Node root = parser(jsCode);
    CompilerPass pass = new CollapseVariableDeclarations(compiler);
    pass.process(null, root);
    // The variables inside the for-in loop are not grouped together.
    assertEquals("for (var key in obj) {\n" +
        "  var value = obj[key];\n" +
        "}", root.getChild(0).toSource(Printer.plainJs).replaceAll("\\s+", " "));
  }

  /**
   * Test that variable declarations inside a with statement are not collapsed.
   */
  @Test
  public void testVariablesInsideWithStatementAreNotCollapsed() {
    String jsCode = "function foo() {\n" +
        "  with (obj) {\n" +
        "    var a = 1;\n" +
        "    var b = 2;\n" +
        "  }\n" +
        "}";

    Node root = parser(jsCode);
    CompilerPass pass = new CollapseVariableDeclarations(compiler);
    pass.process(null, root);
    // The variables inside the with statement are not grouped together.
    assertEquals("with (obj) {\n" +
        "  var a = 1;\n" +
        "  var b = 2;\n" +
        "}", root.getChild(0).toSource(Printer.plainJs).replaceAll("\\s+", " "));
  }

  /**
   * Test that variable declarations inside a try statement are not collapsed.
   */
  @Test
  public void testVariablesInsideTryStatementAreNotCollapsed() {
    String jsCode = "function foo() {\n" +
        "  try {\n" +
        "    var a = 1;\n" +
        "    throw new Error();\n" +
        "  } catch (e) {\n" +
        "    var b = 1;\n" +
        "  }\n" +
        "}";

    Node root = parser(jsCode);
    CompilerPass pass = new CollapseVariableDeclarations(compiler);
    pass.process(null, root);
    // The variables inside the try statement are not grouped together.
    assertEquals("try {\n" +
        "  var a = 1;\n" +
        "  throw new Error();\n" +
        "} catch (e) {\n" +
        "  var b = 1;\n" +
        "}", root.getChild(0).toSource(Printer.plainJs).replaceAll("\\s+", " "));
  }

  /**
   * Test that variable declarations inside a catch statement are not collapsed.
   */
  @Test
  public void testVariablesInsideCatchStatementAreNotCollapsed() {
    String jsCode = "function foo() {\n" +
        "  try {\n" +
        "    throw new Error();\n" +
        "  } catch (e) {\n" +
        "    var a = 1;\n" +
        "  }\n" +
        "}";

    Node root = parser(jsCode);
    CompilerPass pass = new CollapseVariableDeclarations(compiler);
    pass.process(null, root);
    // The variables inside the catch statement are not grouped together.
    assertEquals("try {\n" +
        "  throw new Error();\n" +
        "} catch (e) {\n" +
        "  var a = 1;\n" +
        "}", root.getChild(1).toSource(Printer.plainJs).replaceAll("\\s+", " "));
  }

  private Node parser(String jsCode) {
    return (Node) parserHelper(null, null, jsCode, null, null);
  }

  private Node parserHelper(Scope scope, Node parent, String jsCode, Node root, int lineNum) {
    AbstractCompiler compiler = new AbstractCompiler(CompilerOptions.getDefaults());
    compiler.parseInit(jsCode, root);
    return root;
  }
}