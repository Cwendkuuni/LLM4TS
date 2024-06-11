import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.AbstractShallowCallback;
import com.google.javascript.jscomp.ControlFlowAnalysis;
import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.GraphReachability;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.graph.DiGraph;
import com.google.javascript.rhino.NodeUtil;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UnreachableCodeEliminationTest {

  private AbstractCompiler compiler;
  private ControlFlowGraph<Node> cfg;
  private Logger logger;

  @org.junit.Before
  public void setUp() {
    compiler = mock(AbstractCompiler.class);
    logger = Logger.getLogger(UnreachableCodeElimination.class.getName());
    logger.setLevel(Level.FINE);

    ControlFlowAnalysis cfa = new ControlFlowAnalysis(compiler, false, false);
    cfg = cfa.getCfg();
  }

  @org.junit.Test
  public void testUnreachableCodeElimination() {
    Node root = Node.newBlock("function foo() { if (true) { return; alert('unreachable'); } }");
    new GraphReachability<Node, ControlFlowGraph.Branch>(cfg).compute(cfg.getEntry().getValue());

    UnreachableCodeElimination elimination = new UnreachableCodeElimination(compiler, true);
    elimination.process(Node.newBlock("function foo() {}"), root);

    Node expectedRoot = Node.newBlock("function foo() {}");
    Node actualRoot = root;

    while (actualRoot != null && expectedRoot != actualRoot) {
      actualRoot = actualRoot.getParent();
    }

    assertTrue("Expected root to be updated, but it was: " + expectedRoot, actualRoot == expectedRoot);
  }

  @org.junit.Test
  public void testUnconditionalBranching() {
    Node root = Node.newBlock("function foo() {"
        + "for (var i = 0; i < 10; i++) {"
        + "  if (i == 5) { break; }"
        + "}"
        + "return; "
        + "}");

    new GraphReachability<Node, ControlFlowGraph.Branch>(cfg).compute(cfg.getEntry().getValue());

    UnreachableCodeElimination elimination = new UnreachableCodeElimination(compiler, true);
    elimination.process(Node.newBlock("function foo() {}"), root);

    Node expectedRoot = Node.newBlock("function foo() {"
        + "for (var i = 0; i < 10; i++) {"
        + "  if (i == 5) { continue; }"
        + "}"
        + "}");

    Node actualRoot = root;

    while (actualRoot != null && expectedRoot != actualRoot) {
      actualRoot = actualRoot.getParent();
    }

    assertTrue("Expected root to be updated, but it was: " + expectedRoot, actualRoot == expectedRoot);
  }

  @org.junit.Test
  public void testRemoveNoOpStatements() {
    Node root = Node.newBlock("function foo() {"
        + "var x = 1 + 1;"
        + "return;"
        + "}");

    new GraphReachability<Node, ControlFlowGraph.Branch>(cfg).compute(cfg.getEntry().getValue());

    UnreachableCodeElimination elimination = new UnreachableCodeElimination(compiler, true);
    elimination.process(Node.newBlock("function foo() {}"), root);

    Node expectedRoot = Node.newBlock("function foo() {}");

    Node actualRoot = root;

    while (actualRoot != null && expectedRoot != actualRoot) {
      actualRoot = actualRoot.getParent();
    }

    assertTrue("Expected root to be updated, but it was: " + expectedRoot, actualRoot == expectedRoot);

    NodeUtil nodeUtil = NodeUtil.get(compiler);
    Node expectedNoOpStatement = Node.newExpressionStatement(Node.newNumberLiteral(1));
    Node actualNoOpStatement = nodeUtil.getChild(root, 0);

    assertTrue("Expected no-op statement to be removed, but it was: " + actualNoOpStatement, actualNoOpStatement == null);
    assertTrue("Expected no-op statement to be removed, but it was not: " + expectedNoOpStatement, expectedNoOpStatement == null);
  }

  @org.junit.Test
  public void testKeepPrototypeProperties() {
    Node root = Node.newBlock("function foo() {"
        + "function Bar() {}"
        + "Bar.prototype.baz = function() {}"
        + "}");

    new GraphReachability<Node, ControlFlowGraph.Branch>(cfg).compute(cfg.getEntry().getValue());

    UnreachableCodeElimination elimination = new UnreachableCodeElimination(compiler, true);
    elimination.process(Node.newBlock("function foo() {}"), root);

    Node expectedRoot = Node.newBlock("function foo() {"
        + "function Bar() {}"
        + "}");

    Node actualRoot = root;

    while (actualRoot != null && expectedRoot != actualRoot) {
      actualRoot = actualRoot.getParent();
    }

    assertTrue("Expected root to be updated, but it was: " + expectedRoot, actualRoot == expectedRoot);

    Node prototypeProperty = NodeUtil.getChild(root, 1);
    assertTrue("Expected prototype property to remain: " + prototypeProperty, prototypeProperty != null);
  }

  @org.junit.Test
  public void testKeepBreaksAndContinues() {
    Node root = Node.newBlock("function foo() {"
        + "for (var i = 0; i < 10; i++) {"
        + "  if (i == 5) { break; }"
        + "}"
        + "for (var i = 0; i < 10; i++) {"
        + "  if (i == 5) { continue; }"
        + "}"
        + "}");

    new GraphReachability<Node, ControlFlowGraph.Branch>(cfg).compute(cfg.getEntry().getValue());

    UnreachableCodeElimination elimination = new UnreachableCodeElimination(compiler, false);
    elimination.process(Node.newBlock("function foo() {}"), root);

    Node expectedRoot = Node.newBlock("function foo() {"
        + "for (var i = 0; i < 10; i++) {"
        + "  if (i == 5) {"
        + "    continue;"
        + "  }"
        + "}"
        + "for (var i = 0; i < 10; i++) {"
        + "  if (i == 5) {"
        + "    break;"
        + "  }"
        + "}"
        + "}");

    Node actualRoot = root;

    while (actualRoot != null && expectedRoot != actualRoot) {
      actualRoot = actualRoot.getParent();
    }

    assertTrue("Expected root to be updated, but it was: " + expectedRoot, actualRoot == expectedRoot);
  }

  @org.junit.Test
  public void testRemoveEmptyBlock() {
    Node root = Node.newBlock("function foo() {"
        + "{"
        + "}"
        + "}");

    new GraphReachability<Node, ControlFlowGraph.Branch>(cfg).compute(cfg.getEntry().getValue());

    UnreachableCodeElimination elimination = new UnreachableCodeElimination(compiler, true);
    elimination.process(Node.newBlock("function foo() {}"), root);

    Node expectedRoot = Node.newBlock("function foo() {}");

    Node actualRoot = root;

    while (actualRoot != null && expectedRoot != actualRoot) {
      actualRoot = actualRoot.getParent();
    }

    assertTrue("Expected root to be updated, but it was: " + expectedRoot, actualRoot == expectedRoot);
  }

  @org.junit.Test
  public void testRemoveDeadFunction() {
    Node root = Node.newBlock("function foo() {"
        + "function bar() {}"
        + "}");

    new GraphReachability<Node, ControlFlowGraph.Branch>(cfg).compute(cfg.getEntry().getValue());

    UnreachableCodeElimination elimination = new UnreachableCodeElimination(compiler, true);
    elimination.process(Node.newBlock("function foo() {}"), root);

    Node expectedRoot = Node.newBlock("function foo() {}");

    Node actualRoot = root;

    while (actualRoot != null && expectedRoot != actualRoot) {
      actualRoot = actualRoot.getParent();
    }

    assertTrue("Expected root to be updated, but it was: " + expectedRoot, actualRoot == expectedRoot);

    Node deadFunction = NodeUtil.getChild(root, 0);
    assertTrue("Expected dead function to be removed, but it was: " + deadFunction, deadFunction == null);
  }
}