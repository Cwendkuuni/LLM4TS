import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeUtil.*;
import static com.google.javascript.rhino.Token.*;

import com.google.javascript.jscomp.MinimizeExitPoints;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.AbstractPostOrderCallback;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.NodeUtil;

import org.junit.Test;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class MinimizeExitPointsTest {

  private Compiler compiler;
  private MinimizeExitPoints minimizer;

  @Before
  public void setUp() {
    compiler = new Compiler();
    minimizer = new MinimizeExitPoints(compiler);
  }

  // Basic test cases for each method

  @Test
  public void testLabelExit() {
    Node root = getTestNode("LabelExit.js");
    NodeTraversal.traverse(compiler, root, minimizer);
    assertFalse("Expected no explicit exit from label block",
        hasExplicitExitPoint(root));
  }

  @Test
  public void testBreakInLoop() {
    Node root = getTestNode("BreakInLoop.js");
    NodeTraversal.traverse(compiler, root, minimizer);
    assertEquals("Expected one explicit exit from loop", 1,
        countExplicitExits(root));
  }

  @Test
  public void testContinueInLoop() {
    Node root = getTestNode("ContinueInLoop.js");
    NodeTraversal.traverse(compiler, root, minimizer);
    assertEquals("Expected no explicit exit from loop", 0,
        countExplicitExits(root));
  }

  @Test
  public void testFunctionExit() {
    Node root = getTestNode("FunctionExit.js");
    NodeTraversal.traverse(compiler, root, minimizer);
    assertEquals("Expected one explicit exit from function", 1,
        countExplicitExits(root));
  }

  // Edge cases and exception handling scenarios

  @Test
  public void testLabelWithBreak() {
    Node root = getTestNode("LabelWithBreak.js");
    NodeTraversal.traverse(compiler, root, minimizer);
    assertFalse("Expected no explicit exit from label block",
        hasExplicitExitPoint(root));
  }

  @Test(expected = RuntimeException.class)
  public void testLabelWithMismatchedBreak() {
    Node root = getTestNode("LabelWithMismatchedBreak.js");
    NodeTraversal.traverse(compiler, root, minimizer);
  }

  @Test
  public void testNestedLoopsWithBreak() {
    Node root = getTestNode("NestedLoopsWithBreak.js");
    NodeTraversal.traverse(compiler, root, minimizer);
    assertEquals("Expected one explicit exit from the outer loop", 1,
        countExplicitExits(root));
  }

  @Test
  public void testNestedLoopsWithContinue() {
    Node root = getTestNode("NestedLoopsWithContinue.js");
    NodeTraversal.traverse(compiler, root, minimizer);
    assertEquals("Expected no explicit exit from the outer loop", 0,
        countExplicitExits(root));
  }

  @Test
  public void testLabelWithContinue() {
    Node root = getTestNode("LabelWithContinue.js");
    NodeTraversal.traverse(compiler, root, minimizer);
    assertFalse("Expected no explicit exit from label block",
        hasExplicitExitPoint(root));
  }

  @Test
  public void testIfElseWithReturn() {
    Node root = getTestNode("IfElseWithReturn.js");
    NodeTraversal.traverse(compiler, root, minimizer);
    assertEquals("Expected one explicit exit from if block", 1,
        countExplicitExits(root));
  }

  @Test
  public void testIfElseWithContinue() {
    Node root = getTestNode("IfElseWithContinue.js");
    NodeTraversal.traverse(compiler, root, minimizer);
    assertEquals("Expected no explicit exit from if block", 0,
        countExplicitExits(root));
  }

  // Helper methods

  private Node getTestNode(String fileName) {
    return getNodeFromClasspath(compiler, "TestCases." + fileName);
  }

  private boolean hasExplicitExitPoint(Node node) {
    return NodeUtil.hasExitPoint(node, Token.BREAK, Token.RETURN);
  }

  private int countExplicitExits(Node node) {
    int count = 0;
    for (Node n = node; n != null; n = n.getParent()) {
      if (NodeUtil.hasExitPoint(n, Token.BREAK, Token.RETURN)) {
        count++;
      }
    }
    return count;
  }
}