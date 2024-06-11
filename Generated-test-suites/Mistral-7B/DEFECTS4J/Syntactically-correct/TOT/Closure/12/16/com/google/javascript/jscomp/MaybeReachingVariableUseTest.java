import static org.junit.Assert.*;
import static com.google.javascript.jscomp.MaybeReachingVariableUse.*;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.Node;

import org.junit.Test;

public class MaybeReachingVariableUseTest {

  @Test
  public void testSimpleCase() {
    String jsCode = "function foo() {" +
        "var x = 1;" +
        "var y = x;" +
        "return y;" +
        "}";

    AbstractCompiler compiler = new AbstractCompiler();
    Scope jsScope = compiler.parseAndScope(jsCode);

    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>(jsCode, jsScope);
    MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(cfg, jsScope, compiler);

    Node defNode = analysis.getGraph().getEntryNode();
    Collection<Node> uses = analysis.getUses("x", defNode);
    assertEquals(1, uses.size());
    assertTrue(uses.contains(analysis.getGraph().getNodeForSourcePosition(1, 6)));
  }

  @Test
  public void testNestedFunction() {
    String jsCode = "function foo() {" +
        "function bar() {" +
        "var x = 1;" +
        "return x;" +
        "}" +
        "return bar();" +
        "}";

    AbstractCompiler compiler = new AbstractCompiler();
    Scope jsScope = compiler.parseAndScope(jsCode);

    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>(jsCode, jsScope);
    MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(cfg, jsScope, compiler);

    Node defNode = analysis.getGraph().getEntryNode();
    Collection<Node> uses = analysis.getUses("x", defNode);
    assertEquals(1, uses.size());
    assertTrue(uses.contains(analysis.getGraph().getNodeForSourcePosition(3, 8)));
  }

  @Test
  public void testAssignment() {
    String jsCode = "function foo() {" +
        "var x = 1;" +
        "x += 1;" +
        "return x;" +
        "}";

    AbstractCompiler compiler = new AbstractCompiler();
    Scope jsScope = compiler.parseAndScope(jsCode);

    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>(jsCode, jsScope);
    MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(cfg, jsScope, compiler);

    Node defNode = analysis.getGraph().getEntryNode();
    Collection<Node> uses = analysis.getUses("x", defNode);
    assertEquals(2, uses.size());
    assertTrue(uses.contains(analysis.getGraph().getNodeForSourcePosition(1, 6)));
    assertTrue(uses.contains(analysis.getGraph().getNodeForSourcePosition(2, 6)));
  }

  @Test
  public void testIfStatement() {
    String jsCode = "function foo() {" +
        "var x = 1;" +
        "if (true) {" +
        "  var y = x;" +
        "}" +
        "return x;" +
        "}";

    AbstractCompiler compiler = new AbstractCompiler();
    Scope jsScope = compiler.parseAndScope(jsCode);

    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>(jsCode, jsScope);
    MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(cfg, jsScope, compiler);

    Node defNode = analysis.getGraph().getEntryNode();
    Collection<Node> uses = analysis.getUses("x", defNode);
    assertEquals(2, uses.size());
    assertTrue(uses.contains(analysis.getGraph().getNodeForSourcePosition(1, 6)));
    assertTrue(uses.contains(analysis.getGraph().getNodeForSourcePosition(3, 6)));
  }

  @Test
  public void testWhileStatement() {
    String jsCode = "function foo() {" +
        "var x = 1;" +
        "while (true) {" +
        "  var y = x;" +
        "}" +
        "return x;" +
        "}";

    AbstractCompiler compiler = new AbstractCompiler();
    Scope jsScope = compiler.parseAndScope(jsCode);

    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>(jsCode, jsScope);
    MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(cfg, jsScope, compiler);

    Node defNode = analysis.getGraph().getEntryNode();
    Collection<Node> uses = analysis.getUses("x", defNode);
    assertEquals(1, uses.size());
    assertTrue(uses.contains(analysis.getGraph().getNodeForSourcePosition(1, 6)));
  }

  @Test
  public void testForStatement() {
    String jsCode = "function foo() {" +
        "for (var i = 0; i < 10; i++) {" +
        "  var x = i;" +
        "}" +
        "return 0;" +
        "}";

    AbstractCompiler compiler = new AbstractCompiler();
    Scope jsScope = compiler.parseAndScope(jsCode);

    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>(jsCode, jsScope);
    MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(cfg, jsScope, compiler);

    Node defNode = analysis.getGraph().getEntryNode();
    Collection<Node> uses = analysis.getUses("i", defNode);
    assertEquals(1, uses.size());
    assertTrue(uses.contains(analysis.getGraph().getNodeForSourcePosition(2, 8)));
  }

  @Test
  public void testForInStatement() {
    String jsCode = "function foo() {" +
        "for (var i in ['a', 'b']) {" +
        "  var x = i;" +
        "}" +
        "return 0;" +
        "}";

    AbstractCompiler compiler = new AbstractCompiler();
    Scope jsScope = compiler.parseAndScope(jsCode);

    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>(jsCode, jsScope);
    MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(cfg, jsScope, compiler);

    Node defNode = analysis.getGraph().getEntryNode();
    Collection<Node> uses = analysis.getUses("i", defNode);
    assertEquals(0, uses.size());
  }

  @Test(expected = RuntimeException.class)
  public void testInvalidVariable() {
    String jsCode = "function foo() {" +
        "var x = 1;" +
        "y = x;" +
        "return x;" +
        "}";

    AbstractCompiler compiler = new AbstractCompiler();
    Scope jsScope = compiler.parseAndScope(jsCode);

    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>(jsCode, jsScope);
    MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(cfg, jsScope, compiler);

    Node defNode = analysis.getGraph().getEntryNode();
    Collection<Node> uses = analysis.getUses("y", defNode);
    fail("Expected exception, but got: " + uses);
  }

  @Test
  public void testEscapedVariable() {
    String jsCode = "function foo() {" +
        "function bar() {" +
        "}" +
        "var x = this;" +
        "return x;" +
        "}";

    AbstractCompiler compiler = new AbstractCompiler();
    Scope jsScope = compiler.parseAndScope(jsCode);

    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>(jsCode, jsScope);
    MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(cfg, jsScope, compiler);

    Node defNode = analysis.getGraph().getEntryNode();
    Collection<Node> uses = analysis.getUses("x", defNode);
    assertEquals(0, uses.size());
  }
}