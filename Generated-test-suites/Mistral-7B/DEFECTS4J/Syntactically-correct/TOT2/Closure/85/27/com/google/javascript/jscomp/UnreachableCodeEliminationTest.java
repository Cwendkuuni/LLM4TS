import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeTraversal.AbstractPostOrderCallback.Scope.Root;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.ControlFlowAnalysis;
import com.google.javascript.jscomp.DiGraph;
import com.google.javascript.jscomp.DiGraphEdge;
import com.google.javascript.jscomp.DiGraphNode;
import com.google.javascript.jscomp.GraphReachability;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.Token;

import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UnreachableCodeEliminationTest {

  private static class TestCompiler extends AbstractCompiler {
    @Override
    public NodeUtil nodeUtil() {
      return NodeUtil.getInstance(this);
    }
  }

  private static class TestUnreachableCodeElimination
      extends UnreachableCodeElimination {

    TestUnreachableCodeElimination(TestCompiler compiler,
        boolean removeNoOpStatements) {
      super(compiler, removeNoOpStatements);
    }

    @Override
    public void enterScope(NodeTraversal t) {
      super.enterScope(t);
      if (curCfg != null) {
        System.out.println("CFG for scope: " + curCfg.toString());
      }
    }
  }

  @org.junit.Test
  public void testUnreachableCodeElimination() {
    // Test case 1: Basic functionality
    TestCompiler compiler = new TestCompiler();
    Node rootNode = NodeFactory.createRootNode("function test() {}");

    UnreachableCodeElimination eliminationPass = new TestUnreachableCodeElimination(
        compiler, true);
    eliminationPass.process(null, rootNode);

    assertEquals(0, rootNode.getChildCount());

    // Test case 2: Edge cases - control flow with multiple branches
    String testEdgeCases = "function test() {" +
        "if (true) {" +
        "  return;" +
        "} else {" +
        "  return;" +
        "}";

    Node edgeCasesNode = NodeFactory.createScriptNode(testEdgeCases);
    eliminationPass = new TestUnreachableCodeElimination(compiler, true);
    eliminationPass.process(null, edgeCasesNode);

    assertEquals(1, edgeCasesNode.getChildCount());
    Node remainingStatement = edgeCasesNode.getFirstChild();
    assertNull(remainingStatement.getNext());
    assertEquals(Token.RETURN, remainingStatement.getType());

    // Test case 3: Edge cases - control flow with loops
    String testLoops = "function test() {" +
        "for (var i = 0; i < 10; i++) {" +
        "  if (i > 5) {" +
        "    return;" +
        "  }" +
        "}" +
        "alert('should not be reached');";

    Node loopsNode = NodeFactory.createScriptNode(testLoops);
    eliminationPass = new TestUnreachableCodeElimination(compiler, true);
    eliminationPass.process(null, loopsNode);

    assertEquals(1, loopsNode.getChildCount());
    remainingStatement = loopsNode.getFirstChild();
    assertTrue(remainingStatement.getType() == Token.RETURN);

    // Test case 4: Error scenario - control flow with exceptions
    String testExceptions = "function test() {" +
        "try {" +
        "  throw new Error('an error occurred');" +
        "} catch (e) {" +
        "  return;" +
        "}";

    Node exceptionsNode = NodeFactory.createScriptNode(testExceptions);
    eliminationPass = new TestUnreachableCodeElimination(compiler, true);
    eliminationPass.process(null, exceptionsNode);

    assertEquals(1, exceptionsNode.getChildCount());
    remainingStatement = exceptionsNode.getFirstChild();
    assertNull(remainingStatement.getNext());
    assertFalse(remainingStatement.getType() == Token.RETURN);
  }

  @org.junit.Test
  public void testUnreachableCodeEliminationNoOpStatements() {
    // Test case 1: Basic functionality with no-op statements
    TestCompiler compiler = new TestCompiler();
    Node rootNode = NodeFactory.createRootNode("function test() {" +
        "  return {};" +
        "  alert('unreachable');" +
        "}");

    UnreachableCodeElimination eliminationPass = new TestUnreachableCodeElimination(
        compiler, false);
    eliminationPass.process(null, rootNode);

    assertEquals(1, rootNode.getChildCount());
    assertEquals(Token.RETURN, rootNode.getFirstChild().getType());

    // Test case 2: Edge cases - no-op statements within loops
    String testEdgeCasesNoOp = "function test() {" +
        "for (var i = 0; i < 10; i++) {" +
        "  {}" + // no-op statement
        "}" +
        "alert('should not be reached');";

    Node edgeCasesNoOpNode = NodeFactory.createScriptNode(testEdgeCasesNoOp);
    eliminationPass = new TestUnreachableCodeElimination(compiler, false);
    eliminationPass.process(null, edgeCasesNoOpNode);

    assertEquals(1, edgeCasesNoOpNode.getChildCount());
    assertEquals(Token.RETURN, edgeCasesNoOpNode.getFirstChild().getType());

    // Test case 3: Error scenario - no-op statements within try-catch blocks
    String testExceptionsNoOp = "function test() {" +
        "try {" +
        "  {}" + // no-op statement
        "} catch (e) {" +
        "  alert('should not be reached');" +
        "}";

    Node exceptionsNoOpNode = NodeFactory.createScriptNode(testExceptionsNoOp);
    eliminationPass = new TestUnreachableCodeElimination(compiler, false);
    eliminationPass.process(null, exceptionsNoOpNode);

    assertEquals(1, exceptionsNoOpNode.getChildCount());
    assertEquals(Token.RETURN, exceptionsNoOpNode.getFirstChild().getType());
  }
}