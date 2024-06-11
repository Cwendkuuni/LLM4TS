import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.graph.ControlFlowGraph;
import com.google.javascript.jscomp.graph.DiGraphNode;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

public class UnreachableCodeEliminationTest {

  @SuppressWarnings({"unchecked", "rawtypes"})
  private static void runTest(boolean removeNoOpStatements, Compiler compiler) {
    String input = "function test() {" +
        "  if (true) {" +
        "    return;" +
        "  } else {" +
        "    var x = 1;" +
        "  } " +
        "}";

    Node root = RhinoHelper.parseStringToAbstractSyntaxTree(input);
    CompilerPass pass = new UnreachableCodeElimination(compiler, removeNoOpStatements);
    pass.process(null, root);

    ControlFlowGraph cfg = new ControlFlowAnalysis(compiler, false, false).getCfg();
    DiGraphNode entryNode = cfg.getEntry().getValue();
    DiGraphNode testFnNode = cfg.getDirectedGraphNode(root);

    // Basic functionality test
    Node result = runFunction(compiler, testFnNode);
    assertNull(result);

    // Edge cases and exception handling

    // Test for unreachable code after a return statement with no arguments.
    input = "function test() {" +
        "  if (true) {" +
        "    return;" +
        "  } else {" +
        "    return 1;" +
        "  } " +
        "}";

    root = RhinoHelper.parseStringToAbstractSyntaxTree(input);
    pass.process(null, root);
    result = runFunction(compiler, testFnNode);
    assertEquals(Integer.valueOf(1), result);

    // Test for unreachable code after a return statement with arguments.
    input = "function test() {" +
        "  if (true) {" +
        "    return 1, 2; " +
        "  } else {" +
        "    return 3;" +
        "  } " +
        "}";

    root = RhinoHelper.parseStringToAbstractSyntaxTree(input);
    pass.process(null, root);
    result = runFunction(compiler, testFnNode);
    assertArrayEquals(new Object[]{Integer.valueOf(1), Integer.valueOf(2)}, result.asList());

    // Test for unreachable code with empty block.
    input = "function test() {" +
        "  if (true) {" +
        "  } else {" +
        "    var x = 1;" +
        "  } " +
        "}";

    root = RhinoHelper.parseStringToAbstractSyntaxTree(input);
    pass.process(null, root);
    result = runFunction(compiler, testFnNode);
    assertNull(result);

    // Test for unreachable code in a function that has no return statement.
    input = "function test() {" +
        "  var x = 1;" +
        "}";

    root = RhinoHelper.parseStringToAbstractSyntaxTree(input);
    pass.process(null, root);
    result = runFunction(compiler, testFnNode);
    assertNull(result);

    // Test for unreachable code in a for loop.
    input = "function test() {" +
        "  for (var i = 0; i < 1; i++) {" +
        "    return;" +
        "  } " +
        "  var x = 1;" +
        "}";

    root = RhinoHelper.parseStringToAbstractSyntaxTree(input);
    pass.process(null, root);
    result = runFunction(compiler, testFnNode);
    assertNull(result);

    // Test for unreachable code in a while loop.
    input = "function test() {" +
        "  while (true) {" +
        "    if (false) {" +
        "      return;" +
        "    } " +
        "  } " +
        "  var x = 1;" +
        "}";

    root = RhinoHelper.parseStringToAbstractSyntaxTree(input);
    pass.process(null, root);
    result = runFunction(compiler, testFnNode);
    assertNull(result);

    // Test for unreachable code in a switch statement.
    input = "function test() {" +
        "  switch (1) {" +
        "    case 1:" +
        "      return;" +
        "  } " +
        "  var x = 1;" +
        "}";

    root = RhinoHelper.parseStringToAbstractSyntaxTree(input);
    pass.process(null, root);
    result = runFunction(compiler, testFnNode);
    assertNull(result);

    // Test for unreachable code in a try-catch block.
    input = "function test() {" +
        "  try {" +
        "    throw new Error();" +
        "  } catch (e) {" +
        "    return;" +
        "  } " +
        "  var x = 1;" +
        "}";

    root = RhinoHelper.parseStringToAbstractSyntaxTree(input);
    pass.process(null, root);
    result = runFunction(compiler, testFnNode);
    assertNull(result);

    // Test for unreachable code with unconditional branches.
    input = "function test() {" +
        "  if (true) {" +
        "    break label1;" +
        "  } " +
        "label1: {" +
        "    return;" +
        "  } " +
        "}";

    root = RhinoHelper.parseStringToAbstractSyntaxTree(input);
    pass.process(null, root);
    result = runFunction(compiler, testFnNode);
    assertNull(result);

    // Test for unreachable code with unconditional branches and multiple targets.
    input = "function test() {" +
        "  if (true) {" +
        "    break label1;" +
        "    break label2;" +
        "  } " +
        "label1: {" +
        "    return;" +
        "  } " +
        "label2: {" +
        "    return;" +
        "  } " +
        "}";

    root = RhinoHelper.parseStringToAbstractSyntaxTree(input);
    pass.process(null, root);
    result = runFunction(compiler, testFnNode);
    assertNull(result);

    // Test for unreachable code with conditional branches.
    input = "function test() {" +
        "  if (false) {" +
        "    return;" +
        "  } else {" +
        "    return;" +
        "  } " +
        "  var x = 1;" +
        "}";

    root = RhinoHelper.parseStringToAbstractSyntaxTree(input);
    pass.process(null, root);
    result = runFunction(compiler, testFnNode);
    assertNull(result);

    // Test for unreachable code with no-op statements.
    input = "function test() {" +
        "  if (true) {" +
        "    return;" +
        "  } else {" +
        "    true;" +
        "  } " +
        "}";

    if (removeNoOpStatements) {
      root = RhinoHelper.parseStringToAbstractSyntaxTree(input);
      pass.process(null, root);
      result = runFunction(compiler, testFnNode);
      assertNull(result);
    } else {
      result = runFunction(compiler, testFnNode);
      assertEquals(Token.TRUE, result);
    }

    // Test for unreachable code with empty statement.
    input = "function test() {" +
        "  if (true) {" +
        "    var x = 1;" +
        "  } else {" +
        "  } " +
        "}";

    root = RhinoHelper.parseStringToAbstractSyntaxTree(input);
    pass.process(null, root);
    result = runFunction(compiler, testFnNode);
    assertNull(result);
  }

  @org.junit.Test
  public void testUnreachableCodeElimination() {
    Compiler compiler = new Compiler();
    runTest(false, compiler);
    runTest(true, compiler);
  }

  private static Node runFunction(Compiler compiler, DiGraphNode fnNode) {
    Node root = fnNode.getValue();
    NodeTraversal.traverse(compiler, root, new FunctionCallback() {
      @Override
      public void visit(AbstractCompiler compiler, Node root) {
        Node result = compiler.run(root);
        if (result != null) {
          return;
        }
      }
    });
    return root.getLastChild();
  }

  @SuppressWarnings("ResultOfMethodCallIgnored")
  private static void assertArrayEquals(Object[] expected, Node actual) {
    Object[] result = actual.asList().toArray();
    assertEquals(expected.length, result.length);
    for (int i = 0; i < expected.length; i++) {
      assertEquals(expected[i], result[i]);
    }
  }
}