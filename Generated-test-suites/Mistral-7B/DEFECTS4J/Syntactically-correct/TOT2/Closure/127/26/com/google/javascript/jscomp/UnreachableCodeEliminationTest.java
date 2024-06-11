import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeTraversal.AbstractShallowCallback.visit;
import static com.google.javascript.jscomp.NodeTraversal.AbstractShallowCallback.visitRoot;
import static com.google.javascript.rhino.Token.*;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.GraphReachability;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.ControlFlowAnalysis;
import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.GraphReachability.Branch;
import com.google.javascript.rhino.NodeUtil;

public class UnreachableCodeEliminationTest {
  private AbstractCompiler compiler;
  private CompilerOptions options;

  @Rule
  public ExpectedException expectedException = ExpectedException.none();

  @Before
  public void setUp() {
    options = new CompilerOptions(null);
    compiler = new AbstractCompiler(null, options);
  }

  /**
   * Test method for {@link UnreachableCodeElimination#UnreachableCodeElimination(AbstractCompiler, boolean)}.
   */
  @Test
  public void testConstructor() {
    UnreachableCodeElimination pass = new UnreachableCodeElimination(compiler, true);
    assertNotNull(pass);
  }

  /**
   * Test method for {@link UnreachableCodeElimination#process(Node, Node)}.
   */
  @Test
  public void testProcess() {
    String testCode = "function test() { return; }";
    Node externs = Node.parse(testCode);
    Node toplevel = Node.parse("function main() { test(); }");

    UnreachableCodeElimination pass = new UnreachableCodeElimination(compiler, true);
    pass.process(externs, toplevel);

    Node expectedCode = Node.parse("function main() {}");
    // This assertion checks if the actual code after passing through the
    // UnreachableCodeElimination pass matches the expected code.
    assertEquals(expectedCode.toString(), toplevel.toString());
  }

  /**
   * Test method for {@link UnreachableCodeElimination#process(Node, Node)} with a
   * complex case that includes nested functions and control structures.
   */
  @Test
  public void testProcessComplex() {
    String testCode =
        "function test() {"
        + "  function nestedFunction() {"
        + "    var x = 1;"
        + "    if (false) {"
        + "      return;"
        + "    }"
        + "    var y = 2;"
        + "  }"
        + "  var z = 3;"
        + "  if (true) {"
        + "    nestedFunction();"
        + "  } else {"
        + "    var w = 4;"
        + "  }"
        + "}"
        + "function main() {"
        + "  test();"
        + "}"
        + "";

    Node externs = Node.parse(testCode);
    Node toplevel = Node.parse("function main() {}");

    UnreachableCodeElimination pass = new UnreachableCodeElimination(compiler, true);
    pass.process(externs, toplevel);

    Node expectedCode = Node.parse("function test() {}"
        + "function main() {}");

    // This assertion checks if the actual code after passing through the
    // UnreachableCodeElimination pass matches the expected code.
    assertEquals(expectedCode.toString(), toplevel.toString());
  }

  /**
   * Test method for {@link UnreachableCodeElimination#process(Node, Node)} with a
   * case that includes conditional branches and a loop.
   */
  @Test
  public void testProcessWithConditionalAndLoop() {
    String testCode =
        "function test() {"
        + "  var x = 1;"
        + "  for (var i = 0; i < 10; i++) {"
        + "    if (i === 5) {"
        + "      return;"
        + "    }"
        + "    x++;"
        + "  }"
        + "}"
        + "function main() {"
        + "  test();"
        + "}"
        + "";

    Node externs = Node.parse(testCode);
    Node toplevel = Node.parse("function main() {}");

    UnreachableCodeElimination pass = new UnreachableCodeElimination(compiler, true);
    pass.process(externs, toplevel);

    Node expectedCode = Node.parse("function test() {"
        + "  var x = 1;"
        + "  for (var i = 0; i < 10; i++) {"
        + "    x++;"
        + "  }"
        + "}"
        + "function main() {}");

    // This assertion checks if the actual code after passing through the
    // UnreachableCodeElimination pass matches the expected code.
    assertEquals(expectedCode.toString(), toplevel.toString());
  }

  /**
   * Test method for {@link UnreachableCodeElimination#process(Node, Node)} with a
   * case that includes a switch statement.
   */
  @Test
  public void testProcessWithSwitch() {
    String testCode =
        "function test(a) {"
        + "  switch (a) {"
        + "    case 1:"
        + "      return;"
        + "  }"
        + "}"
        + "function main() {"
        + "  test(1);"
        + "}"
        + "";

    Node externs = Node.parse(testCode);
    Node toplevel = Node.parse("function main() {}");

    UnreachableCodeElimination pass = new UnreachableCodeElimination(compiler, true);
    pass.process(externs, toplevel);

    Node expectedCode = Node.parse("function test(a) {}"
        + "function main() {}");

    // This assertion checks if the actual code after passing through the
    // UnreachableCodeElimination pass matches the expected code.
    assertEquals(expectedCode.toString(), toplevel.toString());
  }

  /**
   * Test method for {@link UnreachableCodeElimination#process(Node, Node)} with a
   * case that includes error handling.
   */
  @Test
  public void testProcessWithErrorHandling() {
    String testCode =
        "function test() {"
        + "  try {"
        + "    throw new Error('Test error');"
        + "  } catch (e) {"
        + "    return;"
        + "  }"
        + "}"
        + "function main() {"
        + "  test();"
        + "}"
        + "";

    Node externs = Node.parse(testCode);
    Node toplevel = Node.parse("function main() {}");

    UnreachableCodeElimination pass = new UnreachableCodeElimination(compiler, true);
    pass.process(externs, toplevel);

    Node expectedCode = Node.parse("function test() {}"
        + "function main() {}");

    // This assertion checks if the actual code after passing through the
    // UnreachableCodeElimination pass matches the expected code.
    assertEquals(expectedCode.toString(), toplevel.toString());
  }

  /**
   * Test method for {@link UnreachableCodeElimination#process(Node, Node)} with a
   * case that includes a return inside a loop.
   */
  @Test
  public void testProcessWithReturnInLoop() {
    String testCode =
        "function test() {"
        + "  for (var i = 0; i < 10; i++) {"
        + "    if (i === 5) {"
        + "      return;"
        + "    }"
        + "  }"
        + "}"
        + "function main() {"
        + "  test();"
        + "}"
        + "";

    Node externs = Node.parse(testCode);
    Node toplevel = Node.parse("function main() {}");

    UnreachableCodeElimination pass = new UnreachableCodeElimination(compiler, true);
    expectedException.expect(AssertionError.class);
    pass.process(externs, toplevel);

    // The test should fail with an AssertionError because the code is not
    // reachable after the return inside the loop.
  }

  /**
   * Test method for {@link UnreachableCodeElimination#process(Node, Node)} with a
   * case that includes a try-catch block with no finally.
   */
  @Test
  public void testProcessWithTryCatchNoFinally() {
    String testCode =
        "function test() {"
        + "  try {"
        + "    throw new Error('Test error');"
        + "  } catch (e) {"
        + "    return;"
        + "  }"
        + "}"
        + "function main() {"
        + "  test();"
        + "}"
        + "";

    Node externs = Node.parse(testCode);
    Node toplevel = Node.parse("function main() {}");

    UnreachableCodeElimination pass = new UnreachableCodeElimination(compiler, true);
    expectedException.expect(AssertionError.class);
    pass.process(externs, toplevel);

    // The test should fail with an AssertionError because the code in the
    // catch block is not reachable.
  }

  /**
   * Test method for {@link UnreachableCodeElimination#process(Node, Node)} with a
   * case that includes a try-finally block.
   */
  @Test
  public void testProcessWithTryFinally() {
    String testCode =
        "function test() {"
        + "  try {"
        + "    throw new Error('Test error');"
        + "  } finally {"
        + "    return;"
        + "  }"
        + "}"
        + "function main() {"
        + "  test();"
        + "}"
        + "";

    Node externs = Node.parse(testCode);
    Node toplevel = Node.parse("function main() {}");

    UnreachableCodeElimination pass = new UnreachableCodeElimination(compiler, true);
    pass.process(externs, toplevel);

    Node expectedCode = Node.parse("function test() {"
        + "  try {"
        + "    throw new Error('Test error');"
        + "  }"
        + "}"
        + "function main() {}");

    // This assertion checks if the actual code after passing through the
    // UnreachableCodeElimination pass matches the expected code.
    assertEquals(expectedCode.toString(), toplevel.toString());

    // Verifying that the finally block was not removed since it is reachable.
    Node finallyBlock = findNodeWithType(externs, FINALLY);
    assertNotNull(finallyBlock);
  }

  /**
   * Test method for {@link UnreachableCodeElimination#process(Node, Node)} with a
   * case that includes a try-catch-finally block.
   */
  @Test
  public void testProcessWithTryCatchFinally() {
    String testCode =
        "function test() {"
        + "  try {"
        + "    throw new Error('Test error');"
        + "  } catch (e) {"
        + "    return;"
        + "  } finally {"
        + "    return;"
        + "  }"
        + "}"
        + "function main() {"
        + "  test();"
        + "}"
        + "";

    Node externs = Node.parse(testCode);
    Node toplevel = Node.parse("function main() {}");

    UnreachableCodeElimination pass = new UnreachableCodeElimination(compiler, true);
    pass.process(externs, toplevel);

    Node expectedCode = Node.parse("function test() {"
        + "  try {"
        + "    throw new Error('Test error');"
        + "  } catch (e) {"
        + "  }"
        + "}"
        + "function main() {}");

    // This assertion checks if the actual code after passing through the
    // UnreachableCodeElimination pass matches the expected code.
    assertEquals(expectedCode.toString(), toplevel.toString());

    // Verifying that all blocks were removed.
    Node tryBlock = findNodeWithType(externs, TRY);
    Node catchBlock = findNodeWithType(externs, CATCH);
    Node finallyBlock = findNodeWithType(externs, FINALLY);
    assertNull(tryBlock);
    assertNull(catchBlock);
    assertNull(finallyBlock);
  }

  /**
   * Test method for {@link UnreachableCodeElimination#process(Node, Node)} with a
   * case that includes a for-in loop.
   */
  @Test
  public void testProcessWithForIn() {
    String testCode =
        "function test() {"
        + "  for (var i in [1,2,3]) {"
        + "    if (i === 2) {"
        + "      return;"
        + "    }"
        + "  }"
        + "}"
        + "function main() {"
        + "  test();"
        + "}"
        + "";

    Node externs = Node.parse(testCode);
    Node toplevel = Node.parse("function main() {}");

    UnreachableCodeElimination pass = new UnreachableCodeElimination(compiler, true);
    expectedException.expect(AssertionError.class);
    pass.process(externs, toplevel);

    // The test should fail with an AssertionError because the code inside
    // the for-in loop is not reachable.
  }

  /**
   * Helper method to find a node with the given node type in the given node.
   * @param node The root node to search.
   * @param nodeType The node type to search for.
   * @return The found node or null if it does not exist.
   */
  private Node findNodeWithType(Node node, int nodeType) {
    visitRoot(node, new visit() {
      Node foundNode = null;
      @Override
      public void visit(Node n) {
        if (n.getType() == nodeType) {
          foundNode = n;
          return;
        }
        visitChildren(n);
      }
    });
    return foundNode;
  }
}