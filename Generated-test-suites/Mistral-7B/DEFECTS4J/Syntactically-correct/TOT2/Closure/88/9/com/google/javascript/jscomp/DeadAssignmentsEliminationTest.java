import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.rhino.Node;

public class DeadAssignmentsEliminationTest {

  @Test
  public void testDeadAssignmentElimination() {
    AbstractCompiler compiler = new AbstractCompiler();
    DeadAssignmentsElimination deadAssignmentsElimination =
        new DeadAssignmentsElimination(compiler);

    String sourceCode = "function main() {" +
        "  var x = 1;" +
        "  x = 2;" +
        "  var y = 1;" +
        "  x = y;" +
        "  y = 3;" +
        "  print(x);" +
        "}" ;

    Node root = AbstractNodeTraversal.parse(sourceCode, null);
    deadAssignmentsElimination.process(null, root);

    String expectedOutput = "function main() {" +
        "  var x = 1;" +
        "  var y = 1;" +
        "  x = y;" +
        "  y = 3;" +
        "  print(1);" +
        "}" ;

    String actualOutput = AbstractNodeTraversal.print(root);
    assertEquals(expectedOutput, actualOutput);
  }

  @Test
  public void testDeadAssignmentEliminationWithConditionals() {
    AbstractCompiler compiler = new AbstractCompiler();
    DeadAssignmentsElimination deadAssignmentsElimination =
        new DeadAssignmentsElimination(compiler);

    String sourceCode = "function main() {" +
        "  var x = 1;" +
        "  if (true) {" +
        "    x = 2;" +
        "  } else {" +
        "    x = 3;" +
        "  }" +
        "  print(x);" +
        "}" ;

    Node root = AbstractNodeTraversal.parse(sourceCode, null);
    deadAssignmentsElimination.process(null, root);

    String expectedOutput = "function main() {" +
        "  var x = 1;" +
        "  if (true) {" +
        "  } else {" +
        "    x = 3;" +
        "  }" +
        "  print(1);" +
        "}" ;

    String actualOutput = AbstractNodeTraversal.print(root);
    assertEquals(expectedOutput, actualOutput);
  }

  @Test
  public void testDeadAssignmentEliminationWithForLoop() {
    AbstractCompiler compiler = new AbstractCompiler();
    DeadAssignmentsElimination deadAssignmentsElimination =
        new DeadAssignmentsElimination(compiler);

    String sourceCode = "function main() {" +
        "  var sum = 0;" +
        "  for (var i = 0; i < 10; i++) {" +
        "    sum += i;" +
        "    i = i + 2;" +
        "  }" +
        "  print(sum);" +
        "}" ;

    Node root = AbstractNodeTraversal.parse(sourceCode, null);
    deadAssignmentsElimination.process(null, root);

    String expectedOutput = "function main() {" +
        "  var sum = 0;" +
        "  for (var i = 0; i < 10; i += 2) {" +
        "    sum += i;" +
        "  }" +
        "  print(sum);" +
        "}" ;

    String actualOutput = AbstractNodeTraversal.print(root);
    assertEquals(expectedOutput, actualOutput);
  }

  @Test
  public void testDeadAssignmentEliminationWithFunctionCall() {
    AbstractCompiler compiler = new AbstractCompiler();
    DeadAssignmentsElimination deadAssignmentsElimination =
        new DeadAssignmentsElimination(compiler);

    String sourceCode = "function main() {" +
        "  function square(x) {" +
        "    return x * x;" +
        "  }" +
        "  var x = 1;" +
        "  x = square(x);" +
        "  print(x);" +
        "}" ;

    Node root = AbstractNodeTraversal.parse(sourceCode, null);
    deadAssignmentsElimination.process(null, root);

    String expectedOutput = "function main() {" +
        "  function square(x) {" +
        "    return x * x;" +
        "  }" +
        "  var x = 1;" +
        "  x = 1;" +
        "  print(1);" +
        "}" ;

    String actualOutput = AbstractNodeTraversal.print(root);
    assertEquals(expectedOutput, actualOutput);
  }

  @Test(expected = AssertionError.class)
  public void testDeadAssignmentEliminationWithException() {
    AbstractCompiler compiler = new AbstractCompiler();
    DeadAssignmentsElimination deadAssignmentsElimination =
        new DeadAssignmentsElimination(compiler);

    String sourceCode = "function main() {" +
        "  try {" +
        "    var x = 1;" +
        "    x = 2;" +
        "    throw new Error(\"Something went wrong!\");" +
        "  } catch (e) {" +
        "    x = 3;" +
        "  }" +
        "  print(x);" +
        "}" ;

    Node root = AbstractNodeTraversal.parse(sourceCode, null);
    deadAssignmentsElimination.process(null, root);

    String actualOutput = AbstractNodeTraversal.print(root);
    // The test should fail with an AssertionError because the DeadAssignmentsElimination
    // cannot eliminate dead assignments that are part of exception handling.
  }

  @Test(expected = AssertionError.class)
  public void testDeadAssignmentEliminationWithVariableDeclaration() {
    AbstractCompiler compiler = new AbstractCompiler();
    DeadAssignmentsElimination deadAssignmentsElimination =
        new DeadAssignmentsElimination(compiler);

    String sourceCode = "function main() {" +
        "  var x = 1;" +
        "  x = 2;" +
        "  var x; // This variable declaration should not affect the elimination." +
        "  print(x);" +
        "}" ;

    Node root = AbstractNodeTraversal.parse(sourceCode, null);
    deadAssignmentsElimination.process(null, root);

    String actualOutput = AbstractNodeTraversal.print(root);
    // The test should fail with an AssertionError because the DeadAssignmentsElimination
    // cannot eliminate dead assignments that are part of variable declarations.
  }

  @Test
  public void testDeadAssignmentEliminationWithMultipleAssignments() {
    AbstractCompiler compiler = new AbstractCompiler();
    DeadAssignmentsElimination deadAssignmentsElimination =
        new DeadAssignmentsElimination(compiler);

    String sourceCode = "function main() {" +
        "  var x = 1;" +
        "  x = 2;" +
        "  x = 3;" +
        "  print(x);" +
        "}" ;

    Node root = AbstractNodeTraversal.parse(sourceCode, null);
    deadAssignmentsElimination.process(null, root);

    String expectedOutput = "function main() {" +
        "  var x = 1;" +
        "  x = 3;" +
        "  print(3);" +
        "}" ;

    String actualOutput = AbstractNodeTraversal.print(root);
    assertEquals(expectedOutput, actualOutput);
  }

  @Test
  public void testDeadAssignmentEliminationWithMultipleAssignmentsAndFunctions() {
    AbstractCompiler compiler = new AbstractCompiler();
    DeadAssignmentsElimination deadAssignmentsElimination =
        new DeadAssignmentsElimination(compiler);

    String sourceCode = "function main() {" +
        "  function add(a, b) {" +
        "    return a + b;" +
        "  }" +
        "  var x = 1;" +
        "  x = 2;" +
        "  x = add(1, 2);" +
        "  print(x);" +
        "}" ;

    Node root = AbstractNodeTraversal.parse(sourceCode, null);
    deadAssignmentsElimination.process(null, root);

    String expectedOutput = "function main() {" +
        "  function add(a, b) {" +
        "    return a + b;" +
        "  }" +
        "  var x = 1;" +
        "  x = add(1, 2);" +
        "  x = 3;" +
        "  print(3);" +
        "}" ;

    String actualOutput = AbstractNodeTraversal.print(root);
    assertEquals(expectedOutput, actualOutput);
  }

  @Test
  public void testDeadAssignmentEliminationWithNestedScopes() {
    AbstractCompiler compiler = new AbstractCompiler();
    DeadAssignmentsElimination deadAssignmentsElimination =
        new DeadAssignmentsElimination(compiler);

    String sourceCode = "function main() {" +
        "  function foo() {" +
        "    var x = 1;" +
        "    x = 2;" +
        "    function bar() {" +
        "      var x = 3;" +
        "      x = 4;" +
        "      print(x);" +
        "    }" +
        "    bar();" +
        "  }" +
        "  foo();" +
        "}" ;

    Node root = AbstractNodeTraversal.parse(sourceCode, null);
    deadAssignmentsElimination.process(null, root);

    String expectedOutput = "function main() {" +
        "  function foo() {" +
        "    var x = 1;" +
        "    function bar() {" +
        "      var x = 3;" +
        "      print(3);" +
        "    }" +
        "    bar();" +
        "    x = 2;" +
        "  }" +
        "  foo();" +
        "}" ;

    String actualOutput = AbstractNodeTraversal.print(root);
    assertEquals(expectedOutput, actualOutput);
  }

  @Test
  public void testDeadAssignmentEliminationWithGlobalScope() {
    AbstractCompiler compiler = new AbstractCompiler();
    DeadAssignmentsElimination deadAssignmentsElimination =
        new DeadAssignmentsElimination(compiler);

    String sourceCode = "var x = 1;" +
        "function main() {" +
        "  x = 2;" +
        "  print(x);" +
        "}" ;

    Node root = AbstractNodeTraversal.parse(sourceCode, null);
    deadAssignmentsElimination.process(null, root);

    String expectedOutput = "var x = 1;" +
        "function main() {" +
        "  print(1);" +
        "}" ;

    String actualOutput = AbstractNodeTraversal.print(root);
    assertEquals(expectedOutput, actualOutput);
  }

  @Test
  public void testDeadAssignmentEliminationWithFunctionReturningAssignment() {
    AbstractCompiler compiler = new AbstractCompiler();
    DeadAssignmentsElimination deadAssignmentsElimination =
        new DeadAssignmentsElimination(compiler);

    String sourceCode = "function main() {" +
        "  function foo() {" +
        "    return x = 1;" +
        "  }" +
        "  var x = foo();" +
        "  print(x);" +
        "}" ;

    Node root = AbstractNodeTraversal.parse(sourceCode, null);
    deadAssignmentsElimination.process(null, root);

    String expectedOutput = "function main() {" +
        "  function foo() {" +
        "    return 1;" +
        "  }" +
        "  var x = foo();" +
        "  print(1);" +
        "}" ;

    String actualOutput = AbstractNodeTraversal.print(root);
    assertEquals(expectedOutput, actualOutput);
  }
}