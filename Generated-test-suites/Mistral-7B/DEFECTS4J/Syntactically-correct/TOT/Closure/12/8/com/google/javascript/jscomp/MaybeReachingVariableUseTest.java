import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.testtype.CompilationTest;

import java.util.Collection;
import java.util.List;

public class MaybeReachingVariableUseTest extends CompilationTest {

  public void testSimpleAssignment() {
    String jsCode = "function foo() { var x = 1; x = 2; }";
    Compile(jsCode);

    MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(
        getControlFlowGraph(), getScope(), getCompiler());
    Collection<Node> uses = analysis.getUses("x", getEntryNode());
    assertEquals(1, uses.size());
    assertTrue(uses.iterator().next().isName());
    assertTrue(uses.iterator().next().getString().equals("x"));
  }

  public void testAssignmentInIfStatement() {
    String jsCode = "function foo() { if (1) { var x = 1; x = 2; } }";
    Compile(jsCode);

    MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(
        getControlFlowGraph(), getScope(), getCompiler());
    Collection<Node> uses = analysis.getUses("x", getEntryNode());
    assertEquals(0, uses.size());
  }

  public void testAssignmentInLoop() {
    String jsCode = "function foo() { for (var i = 0; i < 1; i++) { i = i + 1; } }";
    Compile(jsCode);

    MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(
        getControlFlowGraph(), getScope(), getCompiler());
    Collection<Node> uses = analysis.getUses("i", getEntryNode());
    assertNull(uses);
  }

  public void testAssignmentInFunctionCall() {
    String jsCode = "function foo() { alert(bar(1)); var x = 2; } function bar(x) { return x + 1; }";
    Compile(jsCode);

    MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(
        getControlFlowGraph(), getScope(), getCompiler());
    Collection<Node> uses = analysis.getUses("x", getEntryNode());
    assertEquals(0, uses.size());
  }

  public void testAssignmentInCatchBlock() {
    String jsCode = "try { throw new Error(); } catch (e) { var x = 1; }";
    Compile(jsCode);

    MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(
        getControlFlowGraph(), getScope(), getCompiler());
    Collection<Node> uses = analysis.getUses("x", getEntryNode());
    assertNull(uses);
  }

  public void testAssignmentInFunctionParameter() {
    String jsCode = "function foo(x) { x = 2; }";
    Compile(jsCode);

    MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(
        getControlFlowGraph(), getScope(), getCompiler());
    Collection<Node> uses = analysis.getUses("x", getEntryNode());
    assertNull(uses);
  }

  public void testEscapedVariable() {
    String jsCode = "function foo(x) { this.x = x; }";
    Compile(jsCode);

    MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(
        getControlFlowGraph(), getScope(), getCompiler());
    Collection<Node> uses = analysis.getUses("x", getEntryNode());
    assertNull(uses);
  }

  public void testAssignmentInConditionalExpression() {
    String jsCode = "function foo() { var x = 1; var y = 2; alert(x > y ? x : y); x = 3; }";
    Compile(jsCode);

    MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(
        getControlFlowGraph(), getScope(), getCompiler());
    Collection<Node> uses = analysis.getUses("x", getEntryNode());
    assertEquals(1, uses.size());
    assertTrue(uses.iterator().next().isName());
    assertTrue(uses.iterator().next().getString().equals("x"));
  }

  public void testAssignmentWithSideEffect() {
    String jsCode = "function foo() { var x = 1; for (var i = 0; i < 10; i++) { x = x + i; } }";
    Compile(jsCode);

    MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(
        getControlFlowGraph(), getScope(), getCompiler());
    Collection<Node> uses = analysis.getUses("x", getEntryNode());
    assertEquals(11, uses.size());
  }

  public void testAssignmentWithReturnStatement() {
    String jsCode = "function foo() { var x = 1; return x; x = 2; }";
    Compile(jsCode);

    MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(
        getControlFlowGraph(), getScope(), getCompiler());
    Collection<Node> uses = analysis.getUses("x", getEntryNode());
    assertEquals(1, uses.size());
    assertTrue(uses.iterator().next().isName());
    assertTrue(uses.iterator().next().getString().equals("x"));
  }

  public void testAssignmentWithMultipleDefinitions() {
    String jsCode = "function foo() { var x = 1; x = 2; x = 3; }";
    Compile(jsCode);

    MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(
        getControlFlowGraph(), getScope(), getCompiler());
    Collection<Node> uses = analysis.getUses("x", getEntryNode());
    assertEquals(3, uses.size());
    assertTrue(uses.iterator().next().isName());
    assertTrue(uses.iterator().next().getString().equals("x"));
    assertTrue(uses.iterator().next().isName());
    assertTrue(uses.iterator().next().getString().equals("x"));
  }

  public void testAssignmentWithMultipleUses() {
    String jsCode = "function foo() { var x = 1; alert(x); x = 2; alert(x); }";
    Compile(jsCode);

    MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(
        getControlFlowGraph(), getScope(), getCompiler());
    Collection<Node> uses = analysis.getUses("x", getEntryNode());
    assertEquals(2, uses.size());
    assertTrue(uses.iterator().next().isName());
    assertTrue(uses.iterator().next().getString().equals("x"));
  }

  private ControlFlowGraph<Node> getControlFlowGraph() {
    return (ControlFlowGraph<Node>) getGraph();
  }

  private Scope getScope() {
    return getCompiler().getCurrentScope();
  }
}