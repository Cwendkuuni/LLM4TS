import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeTraversal.AbstractPostOrderCallback.CallbackFlags.Visit;
import static com.google.javascript.rhino.NodeType.*;

import org.junit.Before;
import org.junit.Test;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.GraphReachability;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.ParserUtils;
import com.google.javascript.rhino.NodeFactory;

public class UnreachableCodeEliminationTest {
  private AbstractCompiler compiler;
  private Node root;

  @Before
  public void setUp() {
    CompilerOptions options = new CompilerOptions();
    compiler = new AbstractCompiler(options);
    NodeFactory factory = new NodeFactory();
    root = ParserUtils.parseJavaScriptSource("function test() { if (false) { return; } alert('unreachable'); }", factory);
  }

  @Test
  public void testUnreachableCodeElimination() {
    NodeTraversal traversal = new NodeTraversal(compiler);
    UnreachableCodeElimination elimination = new UnreachableCodeElimination(compiler, true);

    elimination.process(null, root);

    NodeTraversal.traverse(compiler, root, elimination, Visit.LIVE);

    Node test = root.getChild(0);
    assertNull(test.getChild(1));
  }

  @Test
  public void testNoOpStatements() {
    Node root = ParserUtils.parseJavaScriptSource("function test() { a.b.MyClass.prototype.propertyName; }", new NodeFactory());

    AbstractCompiler compiler = new AbstractCompiler(new CompilerOptions());
    UnreachableCodeElimination elimination = new UnreachableCodeElimination(compiler, true);

    elimination.process(null, root);

    NodeTraversal traversal = new NodeTraversal(compiler);
    traversal.traverse(root, elimination);

    assertTrue(NodeUtil.mayHaveSideEffects(root.getChild(0)));
    assertFalse(NodeUtil.mayHaveSideEffects(root.getChild(0).getFirstChild()));
  }

  @Test
  public void testUnconditionalBranching() {
    Node root = ParserUtils.parseJavaScriptSource("function test() { while (true) { break; } break; }", new NodeFactory());

    AbstractCompiler compiler = new AbstractCompiler(new CompilerOptions());
    UnreachableCodeElimination elimination = new UnreachableCodeElimination(compiler, false);

    elimination.process(null, root);

    NodeTraversal traversal = new NodeTraversal(compiler);
    traversal.traverse(root, elimination);

    assertEquals(TOKEN_END, root.getType());
  }

  @Test
  public void testReturnStatements() {
    Node root = ParserUtils.parseJavaScriptSource("function test() { if (true) { return; } return; }", new NodeFactory());

    AbstractCompiler compiler = new AbstractCompiler(new CompilerOptions());
    UnreachableCodeElimination elimination = new UnreachableCodeElimination(compiler, false);

    elimination.process(null, root);

    NodeTraversal traversal = new NodeTraversal(compiler);
    traversal.traverse(root, elimination);

    assertEquals(TOKEN_RETURN, root.getType());
    assertNull(root.getNext());
  }

  @Test
  public void testRemoveDeadCodeInsideLoop() {
    Node root = ParserUtils.parseJavaScriptSource("function test() { while (true) { return; } alert('unreachable'); }", new NodeFactory());

    AbstractCompiler compiler = new AbstractCompiler(new CompilerOptions());
    UnreachableCodeElimination elimination = new UnreachableCodeElimination(compiler, false);

    elimination.process(null, root);

    NodeTraversal traversal = new NodeTraversal(compiler);
    traversal.traverse(root, elimination);

    assertEquals(TOKEN_END, root.getType());
  }

  @Test
  public void testRemoveDeadCodeInsideIfStatement() {
    Node root = ParserUtils.parseJavaScriptSource("function test() { if (true) { return; } alert('unreachable'); }", new NodeFactory());

    AbstractCompiler compiler = new AbstractCompiler(new CompilerOptions());
    UnreachableCodeElimination elimination = new UnreachableCodeElimination(compiler, false);

    elimination.process(null, root);

    NodeTraversal traversal = new NodeTraversal(compiler);
    traversal.traverse(root, elimination);

    assertEquals(TOKEN_RETURN, root.getType());
    assertNull(root.getNext());
  }

  @Test
  public void testRemoveDeadCodeInsideSwitchStatement() {
    Node root = ParserUtils.parseJavaScriptSource("function test() {" +
        "switch (1) {" +
        "  case 1:" +
        "    return;" +
        "  case 2:" +
        "  default:" +
        "    alert('unreachable');" +
        "}", new NodeFactory());

    AbstractCompiler compiler = new AbstractCompiler(new CompilerOptions());
    UnreachableCodeElimination elimination = new UnreachableCodeElimination(compiler, false);

    elimination.process(null, root);

    NodeTraversal traversal = new NodeTraversal(compiler);
    traversal.traverse(root, elimination);

    assertEquals(TOKEN_RETURN, root.getType());
    assertNull(root.getNext());
  }
}