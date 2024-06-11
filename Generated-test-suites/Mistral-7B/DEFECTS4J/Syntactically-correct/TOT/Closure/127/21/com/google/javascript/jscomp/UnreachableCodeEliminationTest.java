import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeTraversal.traverse;
import static com.google.javascript.rhino.NodeFactory.createBlock;
import static com.google.javascript.rhino.NodeFactory.createIdentifier;
import static com.google.javascript.rhino.NodeFactory.createIntLiteral;
import static com.google.javascript.rhino.NodeFactory.createReturnStatement;
import static com.google.javascript.rhino.NodeFactory.createStatement;
import static com.google.javascript.rhino.NodeFactory.createVoidFunction;

import org.junit.Before;
import org.junit.Test;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerPassManager;
import com.google.javascript.jscomp.CompilationTestBase;
import com.google.javascript.jscomp.ControlFlowAnalysis;
import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.GraphReachability;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal.AbstractShallowCallback;
import com.google.javascript.jscomp.UnreachableCodeElimination;
import com.google.javascript.rhino.NodeUtil;

public class UnreachableCodeEliminationTest extends CompilationTestBase {
  private static final String SOURCE =
      "function foo() {" +
      "  var x = 42;" +
      "  return;" +
      "  alert('unreachable');" +
      "}";

  private UnreachableCodeElimination elimination;
  private AbstractCompiler compiler;

  @Before
  public void setUp() {
    compiler = getNewCompiler();
    elimination = new UnreachableCodeElimination(compiler, true);
  }

  @Test
  public void testSimpleReturn() {
    Node root = parseAndCompile(SOURCE);
    CompilerPassManager manager = new CompilerPassManager(compiler, false, null);
    manager.addPass(elimination);
    manager.runPasses(root);

    ControlFlowAnalysis cfa = new ControlFlowAnalysis(compiler, false, false);
    ControlFlowGraph<Node> cfg = cfa.getCfg();
    assertFalse(verifyGraphReachability(cfg));
  }

  @Test
  public void testNoOpStatement() {
    // This test case checks that statements such as a.b.MyClass.prototype.propertyName
    // are not removed as dead code.
    String source =
        "var a = {" +
        "  b: {" +
        "    MyClass: {" +
        "      prototype: {" +
        "        propertyName: 42" +
        "      }" +
        "    }" +
        "  }" +
        "};";

    Node root = parseAndCompile(source);
    CompilerPassManager manager = new CompilerPassManager(compiler, false, null);
    manager.addPass(elimination);
    manager.runPasses(root);

    Node propertyName = findNode(root, "propertyName");
    assertNotNull(propertyName);
  }

  @Test
  public void testBreak() {
    String source =
        "function foo() {" +
        "  for (var i = 0; i < 10; i++) {" +
        "    if (i === 3) {" +
        "      break;" +
        "    }" +
        "    x = i;" +
        "  }" +
        "  return;" +
        "}" +
        "function bar() {" +
        "  foo();" +
        "}" +
        "function baz() {" +
        "  bar();" +
        "}" +
        "baz();";

    Node root = parseAndCompile(source);
    CompilerPassManager manager = new CompilerPassManager(compiler, false, null);
    manager.addPass(elimination);
    manager.runPasses(root);

    Node x = findNode(root, "x");
    assertNotNull(x);
  }

  @Test
  public void testContinue() {
    String source =
        "function foo() {" +
        "  for (var i = 0; i < 10; i++) {" +
        "    if (i === 3) {" +
        "      continue;" +
        "    }" +
        "    x = i;" +
        "  }" +
        "  return;" +
        "}" +
        "function bar() {" +
        "  foo();" +
        "}" +
        "function baz() {" +
        "  bar();" +
        "}" +
        "baz();";

    Node root = parseAndCompile(source);
    CompilerPassManager manager = new CompilerPassManager(compiler, false, null);
    manager.addPass(elimination);
    manager.runPasses(root);

    Node x = findNode(root, "x");
    assertNotNull(x);
  }

  @Test
  public void testReturn() {
    String source =
        "function foo() {" +
        "  for (var i = 0; i < 10; i++) {" +
        "    if (i === 3) {" +
        "      return;" +
        "    }" +
        "    x = i;" +
        "  }" +
        "}" +
        "function bar() {" +
        "  foo();" +
        "}" +
        "function baz() {" +
        "  bar();" +
        "}" +
        "baz();";

    Node root = parseAndCompile(source);
    CompilerPassManager manager = new CompilerPassManager(compiler, false, null);
    manager.addPass(elimination);
    manager.runPasses(root);

    Node x = findNode(root, "x");
    assertNull(x);
  }

  @Test
  public void testReturnWithoutParent() {
    Node root = createVoidFunction("foo");
    traverse(compiler, root, new AbstractShallowCallback() {
      @Override
      public void visit(NodeTraversal t, Node n, Node parent) {
        if (n.getType() == Token.RETURN) {
          if (parent == null) {
            throw new AssertionError("Return statement should have a parent.");
          }
        }
      }
    });

    elimination = new UnreachableCodeElimination(compiler, true);
    CompilerPassManager manager = new CompilerPassManager(compiler, false, null);
    manager.addPass(elimination);
    manager.runPasses(root);

    logger.info("Removed " + root.toString());
    logger.info("New root: " + root.toString());
  }

  @Test
  public void testEmptyFunction() {
    Node root = createVoidFunction("foo");
    elimination = new UnreachableCodeElimination(compiler, true);
   CompilerPassManager manager = new CompilerPassManager(compiler, false, null);
    manager.addPass(elimination);
    manager.runPasses(root);

    logger.info("Removed " + root.toString());
    logger.info("New root: " + root.toString());
  }

  private boolean verifyGraphReachability(ControlFlowGraph<Node> cfg) {
    GraphReachability<Node, ControlFlowGraph.Branch> reachability =
        new GraphReachability<Node, ControlFlowGraph.Branch>(cfg);
    return reachability.compute(cfg.getEntry().getValue())
        .contains(cfg.getExit().getValue());
  }

  private Node findNode(Node root, String name) {
    NodeUtil.traverse(root, new NodeUtil.FindingByNameVisitor(name));
    return null;
  }
}