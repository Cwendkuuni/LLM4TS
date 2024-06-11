import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeTraversal.AbstractShallowCallback.Scope;

import org.junit.Test;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.GraphReachability;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.ControlFlowAnalysis;
import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.DiGraphNode;
import com.google.javascript.rhino.Token;

public class UnreachableCodeEliminationTest {

  @Test
  public void testUnreachableCodeElimination() {
    AbstractCompiler compiler = new AbstractCompiler();
    CompilerOptions options = new CompilerOptions(null);
    UnreachableCodeElimination pass = new UnreachableCodeElimination(compiler, true);

    String input = "function foo() {" +
        "  if (true) {" +
        "    return;" +
        "  }" +
        "  alert('Hello World');" +
        "}";

    Node root = AbstractCompiler.parseTree(input, null, null, options);
    pass.process(null, root);

    // Check that the return statement and the alert statement have been removed.
    assertTrue(root.getLastChild().isReturn());
    assertEquals(0, root.getLastChild().getChildCount());

    String expectedOutput = "function foo() {" +
        "  if (true) {" +
        "  }" +
        "}";

    String output = NodePrinter.printTree(root);
    assertEquals(expectedOutput, output);
  }

  @Test
  public void testNoOpStatements() {
    AbstractCompiler compiler = new AbstractCompiler();
    CompilerOptions options = new CompilerOptions(null);
    UnreachableCodeElimination pass = new UnreachableCodeElimination(compiler, true);

    String input = "function foo() {" +
        "  if (true) {" +
        "    alert('Hello World');" +
        "  }" +
        "  alert('Goodbye World');" +
        "}";

    Node root = AbstractCompiler.parseTree(input, null, null, options);
    pass.process(null, root);

    // Check that the second alert statement has been removed.
    String expectedOutput = "function foo() {" +
        "  if (true) {" +
        "    alert('Hello World');" +
        "  }" +
        "}";

    String output = NodePrinter.printTree(root);
    assertEquals(expectedOutput, output);
  }

  @Test
  public void testNoOpStatementsWithSideEffects() {
    AbstractCompiler compiler = new AbstractCompiler();
    CompilerOptions options = new CompilerOptions(null);
    UnreachableCodeElimination pass = new UnreachableCodeElimination(compiler, false);

    String input = "function foo() {" +
        "  if (true) {" +
        "    alert('Hello World');" +
        "  }" +
        "  alert('Goodbye World');" +
        "}";

    Node root = AbstractCompiler.parseTree(input, null, null, options);
    pass.process(null, root);

    // Check that the second alert statement has NOT been removed.
    String expectedOutput = "function foo() {" +
        "  if (true) {" +
        "    alert('Hello World');" +
        "  }" +
        "  alert('Goodbye World');" +
        "}";

    String output = NodePrinter.printTree(root);
    assertEquals(expectedOutput, output);
  }

  @Test
  public void testControlFlowGraph() {
    AbstractCompiler compiler = new AbstractCompiler();
    CompilerOptions options = new CompilerOptions(null);
    UnreachableCodeElimination pass = new UnreachableCodeElimination(compiler, true);

    String input = "function foo() {" +
        "  if (true) {" +
        "    return;" +
        "  }" +
        "  alert('Hello World');" +
        "}";

    Node root = AbstractCompiler.parseTree(input, null, null, options);
    pass.process(null, root);

    ControlFlowAnalysis cfa = new ControlFlowAnalysis(compiler, false, false);
    cfa.process(null, root);
    ControlFlowGraph<Node> cfg = cfa.getCfg();

    DiGraphNode<Node, Branch> startNode = cfg.getEntry();
    List<DiGraphNode<Node, Branch>> nodes = cfg.getNodes();

    // The start node should have an outgoing edge to the return node.
    assertEquals(1, startNode.getOutEdges().size());
    Branch startToReturn = startNode.getOutEdges().get(0).getValue();
    assertTrue(startToReturn.isUncond());

    // The return node should have no incoming or outgoing edges.
    DiGraphNode<Node, Branch> returnNode = nodes.get(1);
    assertEquals(0, returnNode.getInEdges().size());
    assertEquals(0, returnNode.getOutEdges().size());
  }

  @Test
  public void testUnconditionalBranching() {
    AbstractCompiler compiler = new AbstractCompiler();
    CompilerOptions options = new CompilerOptions(null);
    UnreachableCodeElimination pass = new UnreachableCodeElimination(compiler, true);

    String input = "function foo() {" +
        "  if (true) {" +
        "    break label1;" +
        "  }" +
        "  if (true) {" +
        "    break label1;" +
        "  }" +
        "  label1: {}" +
        "}";

    Node root = AbstractCompiler.parseTree(input, null, null, options);
    pass.process(null, root);

    // Check that both break statements have been removed.
    String expectedOutput = "function foo() {" +
        "  if (true) {" +
        "  }" +
        "  if (true) {" +
        "  }" +
        "  label1: {}" +
        "}";

    String output = NodePrinter.printTree(root);
    assertEquals(expectedOutput, output);
  }

  @Test
  public void testReturnInFunction() {
    AbstractCompiler compiler = new AbstractCompiler();
    CompilerOptions options = new CompilerOptions(null);
    UnreachableCodeElimination pass = new UnreachableCodeElimination(compiler, true);

    String input = "function foo() {" +
        "  function bar() {" +
        "    return;" +
        "  }" +
        "  bar();" +
        "}";

    Node root = AbstractCompiler.parseTree(input, null, null, options);
    pass.process(null, root);

    // Check that the return statement in bar has been removed.
    String expectedOutput = "function foo() {" +
        "  function bar() {" +
        "  }" +
        "  bar();" +
        "}";

    String output = NodePrinter.printTree(root);
    assertEquals(expectedOutput, output);
  }
}