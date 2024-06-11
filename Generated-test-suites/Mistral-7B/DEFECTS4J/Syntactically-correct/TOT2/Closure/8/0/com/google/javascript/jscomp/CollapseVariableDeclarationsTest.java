import static org.junit.Assert.*;
import static com.google.javascript.rhino.NodeTree.*;
import static com.google.javascript.jscomp.CollapseVariableDeclarations.*;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;

import java.util.List;
import java.util.Set;

public class CollapseVariableDeclarationsTest {

  private AbstractCompiler compiler;

  @Before
  public void setUp() {
    CompilerOptions options = CompilerOptions.defaultOptions();
    compiler = new Compiler(options);
  }

  @Test
  public void testCollapseSingleVariableDeclaration() {
    String input =
        "function foo() {"
            + "  var x;"
            + "  var y = 1;"
            + "  var z = 2;"
            + "}";

    Node root = parse(input);
    CollapseVariableDeclarations pass = new CollapseVariableDeclarations(compiler);
    pass.process(null, root);

    Node expected = parse(
        "function foo() {"
            + "  var x, y = 1, z = 2;"
            + "}");

    compareTrees(root, expected);
  }

  @Test
  public void testCollapseMultipleVariableDeclarations() {
    String input =
        "function foo() {"
            + "  var x, y, z;"
            + "  var a, b, c;"
            + "}";

    Node root = parse(input);
    CollapseVariableDeclarations pass = new CollapseVariableDeclarations(compiler);
    pass.process(null, root);

    Node expected = parse(
        "function foo() {"
            + "  var x, y, z, a, b, c;"
            + "}");

    compareTrees(root, expected);
  }

  @Test
  public void testCollapseVariableAssignments() {
    String input =
        "function foo() {"
            + "  var x = 1;"
            + "  var y = 1;"
            + "  var z = 1;"
            + "}";

    Node root = parse(input);
    CollapseVariableDeclarations pass = new CollapseVariableDeclarations(compiler);
    pass.process(null, root);

    Node expected = parse(
        "function foo() {"
            + "  var x = 1, y = 1, z = 1;"
            + "}");

    compareTrees(root, expected);
  }

  @Test
  public void testCollapseMultipleAssignmentsToSameVariable() {
    String input =
        "function foo() {"
            + "  var x = 1;"
            + "  x = 2;"
            + "  x = 3;"
            + "}";

    Node root = parse(input);
    CollapseVariableDeclarations pass = new CollapseVariableDeclarations(compiler);
    pass.process(null, root);

    Node expected = parse(
        "function foo() {"
            + "  var x = 1;"
            + "  x = 2;"
            + "  x = 3;"
            + "}");

    compareTrees(root, expected);
  }

  @Test
  public void testCollapseAssignmentsToDifferentVariables() {
    String input =
        "function foo() {"
            + "  var x = 1;"
            + "  var y = 1;"
            + "  x = 2;"
            + "  y = 3;"
            + "}";

    Node root = parse(input);
    CollapseVariableDeclarations pass = new CollapseVariableDeclarations(compiler);
    pass.process(null, root);

    Node expected = parse(
        "function foo() {"
            + "  var x = 1;"
            + "  var y = 1;"
            + "  x = 2;"
            + "  y = 3;"
            + "}");

    compareTrees(root, expected);
  }

  @Test
  public void testCollapseAssignmentsToSameVariableWithDifferentTypes() {
    String input =
        "function foo() {"
            + "  var x = 1;"
            + "  x = true;"
            + "}";

    Node root = parse(input);
    CollapseVariableDeclarations pass = new CollapseVariableDeclarations(compiler);
    pass.process(null, root);

    Node expected = parse(
        "function foo() {"
            + "  var x = 1;"
            + "  x = true;"
            + "}");

    compareTrees(root, expected);
  }

  @Test
  public void testCollapseAssignmentsToSameVariableWithDifferentTypesInDifferentScopes() {
    String input =
        "function foo() {"
            + "  function bar() {"
            + "    var x = 1;"
            + "    x = true;"
            + "  }"
            + "  var x = 1;"
            + "  x = true;"
            + "}";

    Node root = parse(input);
    CollapseVariableDeclarations pass = new CollapseVariableDeclarations(compiler);
    pass.process(null, root);

    Node expected = parse(
        "function foo() {"
            + "  function bar() {"
            + "    var x = 1;"
            + "    x = true;"
            + "  }"
            + "  var x = 1;"
            + "  x = true;"
            + "}");

    compareTrees(root, expected);
  }

  @Test(expected = AssertionError.class)
  public void testCollapseVariableDeclarationsInsideFunctionCall() {
    String input =
        "function foo() {"
            + "  function bar() {"
            + "    var x = 1;"
            + "  }"
            + "  bar();"
            + "}";

    Node root = parse(input);
    CollapseVariableDeclarations pass = new CollapseVariableDeclarations(compiler);
    pass.process(null, root);
  }

  @Test(expected = AssertionError.class)
  public void testCollapseVariableDeclarationsInsideConditionalExpression() {
    String input =
        "function foo() {"
            + "  var x = 1;"
            + "  var y = 1;"
            + "  var z = 1;"
            + "  x = (y = 2, z = 3)?"
            + "      x = 4"
            + "  : x = 5;"
            + "}";

    Node root = parse(input);
    CollapseVariableDeclarations pass = new CollapseVariableDeclarations(compiler);
    pass.process(null, root);
  }

  @Test(expected = AssertionError.class)
  public void testCollapseVariableDeclarationsInsideWhileStatement() {
    String input =
        "function foo() {"
            + "  var i = 0;"
            + "  while (true) {"
            + "    var j = 0;"
            + "  }"
            + "}";

    Node root = parse(input);
    CollapseVariableDeclarations pass = new CollapseVariableDeclarations(compiler);
    pass.process(null, root);
  }

  @Test(expected = AssertionError.class)
  public void testCollapseVariableDeclarationsInsideDoWhileStatement() {
    String input =
        "function foo() {"
            + "  var i = 0;"
            + "  do {"
            + "    var j = 0;"
            + "  } while (true);"
            + "}";

    Node root = parse(input);
    CollapseVariableDeclarations pass = new CollapseVariableDeclarations(compiler);
    pass.process(null, root);
  }

  @Test(expected = AssertionError.class)
  public void testCollapseVariableDeclarationsInsideForStatement() {
    String input =
        "function foo() {"
            + "  for (var i = 0; i < 10; i++) {"
            + "    var j = 0;"
            + "  }"
            + "}";

    Node root = parse(input);
    CollapseVariableDeclarations pass = new CollapseVariableDeclarations(compiler);
    pass.process(null, root);
  }

  @Test(expected = AssertionError.class)
  public void testCollapseVariableDeclarationsInsideSwitchStatement() {
    String input =
        "function foo() {"
            + "  var x = 1;"
            + "  switch (x) {"
            + "    case 1:"
            + "      var y = 1;"
            + "      break;"
            + "    case 2:"
            + "      var z = 1;"
            + "      break;"
            + "  }"
            + "}";

    Node root = parse(input);
    CollapseVariableDeclarations pass = new CollapseVariableDeclarations(compiler);
    pass.process(null, root);
  }

  @Test(expected = AssertionError.class)
  public void testCollapseVariableDeclarationsInsideCatchBlock() {
    String input =
        "function foo() {"
            + "  try {"
            + "    throw 1;"
            + "  } catch (e) {"
            + "    var x = 1;"
            + "  }"
            + "}";

    Node root = parse(input);
    CollapseVariableDeclarations pass = new CollapseVariableDeclarations(compiler);
    pass.process(null, root);
  }

  @Test(expected = AssertionError.class)
  public void testCollapseVariableDeclarationsInsideFinallyBlock() {
    String input =
        "function foo() {"
            + "  try {"
            + "    throw 1;"
            + "  } finally {"
            + "    var x = 1;"
            + "  }"
            + "}";

    Node root = parse(input);
    CollapseVariableDeclarations pass = new CollapseVariableDeclarations(compiler);
    pass.process(null, root);
  }

  @Test
  public void testCollapseVariableDeclarationsInsideFunctionDeclaration() {
    String input =
        "function foo() {"
            + "  function bar() {"
            + "    var x, y, z;"
            + "  }"
            + "}";

    Node root = parse(input);
    CollapseVariableDeclarations pass = new CollapseVariableDeclarations(compiler);
    pass.process(null, root);

    Node expected = parse(
        "function foo() {"
            + "  function bar() {"
            + "    var x, y, z;"
            + "  }"
            + "}");

    compareTrees(root, expected);
  }

  @Test
  public void testCollapseVariableDeclarationsInsideBlock() {
    String input =
        "function foo() {"
            + "  if (true) {"
            + "    var x = 1;"
            + "    var y = 2;"
            + "  }"
            + "  var z = 3;"
            + "}";

    Node root = parse(input);
    CollapseVariableDeclarations pass = new CollapseVariableDeclarations(compiler);
    pass.process(null, root);

    Node expected = parse(
        "function foo() {"
            + "  if (true) {"
            + "    var x = 1, y = 2;"
            + "  }"
            + "  var z = 3;"
            + "}");

    compareTrees(root, expected);
  }

  @Test
  public void testCollapseVariableDeclarationsInsideFunctionCallWithArguments() {
    String input =
        "function foo() {"
            + "  function bar(x, y, z) {"
            + "    var a, b, c;"
            + "  }"
            + "  bar(1, 2, 3);"
            + "}";

    Node root = parse(input);
    CollapseVariableDeclarations pass = new CollapseVariableDeclarations(compiler);
    pass.process(null, root);

    Node expected = parse(
        "function foo() {"
            + "  function bar(x, y, z) {"
            + "    var a, b, c;"
            + "  }"
            + "  bar(1, 2, 3);"
            + "}");

    compareTrees(root, expected);
  }

  private void compareTrees(Node a, Node b) {
    NodeTraversal nt = new NodeTraversal() {
      @Override
      public void visit(Node a, Node t, Node p) {
        Node bNode = p.getFirstChildWithName("children").getFirstChild();
        assertEquals(a.toString(), bNode.toString());
        super.visit(a, t, p);
      }
    };
    nt.traverse(compiler, a, null);
  }

  private Node parse(String input) {
    Node root = null;
    try {
      root = (Node) compiler.parseTreeRecursiveDescent(input);
    } catch (RuntimeException e) {
      fail("Failed to parse input: " + input);
    }
    return root;
  }
}