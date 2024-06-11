import static org.junit.Assert.*;
import static com.google.javascript.jscomp.MaybeReachingVariableUse.*;

import com.google.common.collect.ImmutableSet;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilationTestBase;
import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.GraphNode;
import com.google.javascript.jscomp.Scope;

import java.util.Collection;
import java.util.List;

public class MaybeReachingVariableUseTest extends CompilationTestBase {

  public void testSimpleLocalVariable() {
    String jsCode = "function foo() { var x = 1; x = 2; }";
    Compile(jsCode);

    Scope jsScope = compilation.getFunctions()[0].getBody().getScope();
    MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(
        getControlFlowGraph(compilation.getFunctions()[0]), jsScope, compiler);

    ReachingUses result = analysis.run();
    Collection<Node> uses = analysis.getUses("x", getFunctions()[0].getBody().get(0));

    assertEquals(1, uses.size());
    assertEquals(getFunctions()[0].getBody().get(1), uses.iterator().next());
  }

  public void testConditionalLocalVariable() {
    String jsCode = "function foo() { var x = 1; if (true) { x = 2; } }";
    Compile(jsCode);

    Scope jsScope = compilation.getFunctions()[0].getBody().getScope();
    MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(
        getControlFlowGraph(compilation.getFunctions()[0]), jsScope, compiler);

    ReachingUses result = analysis.run();
    Collection<Node> uses = analysis.getUses("x", getFunctions()[0].getBody().get(0));

    assertEquals(1, uses.size());
    assertEquals(getFunctions()[0].getBody().get(1), uses.iterator().next());
  }

  public void testLocalVariableInsideLoop() {
    String jsCode = "function foo() { for (var i = 0; i < 10; i++) { i = i + 1; } }";
    Compile(jsCode);

    Scope jsScope = compilation.getFunctions()[0].getBody().getScope();
    MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(
        getControlFlowGraph(compilation.getFunctions()[0]), jsScope, compiler);

    ReachingUses result = analysis.run();
    Collection<Node> uses = analysis.getUses("i", getFunctions()[0].getBody().get(0));

    assertEquals(11, uses.size()); // 10 iterations and once in the initialization
    for (int i = 0; i < 10; i++) {
      assertEquals(getFunctions()[0].getBody().get(i + 2), uses.iterator().next());
    }
    assertEquals(getFunctions()[0].getBody().get(11), uses.iterator().next());
  }

  public void testEscapedVariable() {
    String jsCode = "function foo(x) { return x; }";
    Compile(jsCode);

    Scope jsScope = compilation.getFunctions()[0].getParametersScope();
    MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(
        getControlFlowGraph(compilation.getFunctions()[0]), jsScope, compiler);

    ReachingUses result = analysis.run();
    Collection<Node> uses = analysis.getUses("x", getFunctions()[0].getBody().get(0));

    assertEquals(0, uses.size());
  }

  public void testFunctionParameter() {
    String jsCode = "function foo(x) { return x; }";
    Compile(jsCode);

    Scope jsScope = compilation.getFunctions()[0].getParametersScope();
    MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(
        getControlFlowGraph(compilation.getFunctions()[0]), jsScope, compiler);

    ReachingUses result = analysis.run();
    Collection<Node> uses = analysis.getUses("x", getFunctions()[0]);

    assertEquals(1, uses.size());
    assertEquals(getFunctions()[0].getParameters()[0], uses.iterator().next());
  }

  public void testGlobalVariable() {
    String jsCode = "var x = 1; function foo() { x = 2; }";
    Compile(jsCode);

    Scope jsScope = compilation.getTopLevelScope();
    MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(
        getControlFlowGraph(getTopFunction()), jsScope, compiler);

    ReachingUsages result = analysis.run();
    Collection<Node> uses = analysis.getUses("x", getTopFunction().getBody().get(0));

    assertEquals(2, uses.size());
    assertEquals(getTopFunction().getBody().get(0), uses.iterator().next());
    assertEquals(getTopFunction().getBody().get(1), uses.iterator().next());
  }

  public void testFunctionCall() {
    String jsCode = "function foo() { var x = 1; bar(); } function bar() { x = 2; }";
    Compile(jsCode);

    Scope jsScope = compilation.getFunctions()[0].getBody().getScope();
    MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(
        getControlFlowGraph(compilation.getFunctions()[0]), jsScope, compiler);

    ReachingUsages result = analysis.run();
    Collection<Node> uses = analysis.getUses("x", getFunctions()[0].getBody().get(0));

    assertEquals(1, uses.size());
    assertEquals(getFunctions()[0].getBody().get(0), uses.iterator().next());
  }

  public void testVariableShadowing() {
    String jsCode = "function foo() { var x = 1; var x = 2; }";
    Compile(jsCode);

    Scope jsScope = compilation.getFunctions()[0].getBody().getScope();
    MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(
        getControlFlowGraph(compilation.getFunctions()[0]), jsScope, compiler);

    ReachingUsages result = analysis.run();
    Collection<Node> uses = analysis.getUses("x", getFunctions()[0].getBody().get(0));

    assertEquals(0, uses.size());
  }

  public void testVariableRenaming() {
    String jsCode = "function foo() { var x = 1; var y = x; }";
    Compile(jsCode);

    Scope jsScope = compilation.getFunctions()[0].getBody().getScope();
    MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(
        getControlFlowGraph(compilation.getFunctions()[0]), jsScope, compiler);

    ReachingUsages result = analysis.run();
    Collection<Node> uses = analysis.getUses("x", getFunctions()[0].getBody().get(0));

    assertEquals(1, uses.size());
    assertEquals(getFunctions()[0].getBody().get(0), uses.iterator().next());
  }

  private AbstractCompiler compiler = new SimpleCompiler();

  private ControlFlowGraph<Node> getControlFlowGraph(Function func) {
    return func.getControlFlowGraph();
  }

  private Function getTopFunction() {
    return compilation.getFunctions()[0];
  }

  @Override
  protected ImmutableList<String> getExtraJavaClasses() {
    return ImmutableList.<String>of("com.google.javascript.jscomp.Scope",
        "com.google.javascript.jscomp.ControlFlowGraph",
        "com.google.javascript.jscomp.GraphNode",
        "com.google.javascript.jscomp.LatticeElement",
        "com.google.common.base.Preconditions",
        "com.google.common.collect.HashMultimap",
        "com.google.common.collect.Multimap",
        "com.google.common.collect.Sets");
  }
}