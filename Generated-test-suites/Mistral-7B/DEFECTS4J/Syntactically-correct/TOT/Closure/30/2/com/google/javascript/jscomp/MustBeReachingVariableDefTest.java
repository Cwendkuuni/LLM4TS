import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.Node;

import java.util.Iterator;

import org.junit.Before;
import org.junit.Test;

public class MustBeReachingVariableDefTest {

  private Scope jsScope;
  private AbstractCompiler compiler;
  private ControlFlowGraph<Node> cfg;

  @Before
  public void setUp() {
    jsScope = new Scope();
    jsScope.putVar("a", new Var(jsScope, "a", Node.UNDEFINED));
    jsScope.putVar("b", new Var(jsScope, "b", Node.UNDEFINED));
    jsScope.putVar("c", new Var(jsScope, "c", Node.UNDEFINED));

    compiler = new AbstractCompiler(jsScope);
    cfg = new ControlFlowGraph<Node>(jsScope.getRootNode(), compiler);
  }

  @Test
  public void testSimpleAssignment() {
    Node assign = Node.newAssign(Token.ASSIGN, "a", Node.NAME, "b");
    MustBeReachingVariableDef mustBeReachingVariableDef =
        new MustBeReachingVariableDef(cfg, jsScope, compiler);
    Node definition = Node.NAME("b");

    MustDef initial = mustBeReachingVariableDef.computeInitialEstimateLattice();
    MustDef result = mustBeReachingVariableDef.flowThrough(assign, initial);

    Node use = Node.NAME("a");
    assertSame(definition, mustBeReachingVariableDef.getDef("a", use));
  }

  @Test
  public void testMultipleAssignments() {
    Node assign1 = Node.newAssign(Token.ASSIGN, "a", Node.NAME, "b");
    Node assign2 = Node.newAssign(Token.ASSIGN, "a", Node.NAME, Node.INT_LITERAL(1));

    MustBeReachingVariableDef mustBeReachingVariableDef =
        new MustBeReachingVariableDef(cfg, jsScope, compiler);
    MustDef initial = mustBeReachingVariableDef.computeInitialEstimateLattice();

    MustDef result = mustBeReachingVariableDef.flowThrough(assign1, initial);
    Node definition1 = Node.NAME("b");
    assertSame(definition1, mustBeReachingVariableDef.getDef("a", assign1));

    result = mustBeReachingVariableDef.flowThrough(assign2, result);
    Node definition2 = Node.INT_LITERAL(1);
    assertSame(definition2, mustBeReachingVariableDef.getDef("a", assign2));
  }

  @Test
  public void testMultipleUses() {
    Node assign = Node.newAssign(Token.ASSIGN, "a", Node.NAME, "b");
    Node use1 = Node.NAME("a");
    Node use2 = Node.NAME("a");

    MustBeReachingVariableDef mustBeReachingVariableDef =
        new MustBeReachingVariableDef(cfg, jsScope, compiler);
    MustDef initial = mustBeReachingVariableDef.computeInitialEstimateLattice();

    MustDef result = mustBeReachingVariableDef.flowThrough(assign, initial);
    Node definition = Node.NAME("b");

    assertSame(definition, mustBeReachingVariableDef.getDef("a", use1));
    assertSame(definition, mustBeReachingVariableDef.getDef("a", use2));
  }

  @Test
  public void testMultipleDefinitions() {
    Node assign1 = Node.newAssign(Token.ASSIGN, "a", Node.NAME, "b");
    Node assign2 = Node.newAssign(Token.ASSIGN, "a", Node.NAME, Node.INT_LITERAL(1));

    MustBeReachingVariableDef mustBeReachingVariableDef =
        new MustBeReachingVariableDef(cfg, jsScope, compiler);
    MustDef initial = mustBeReachingVariableDef.computeInitialEstimateLattice();

    // First definition
    MustDef result1 = mustBeReachingVariableDef.flowThrough(assign1, initial);
    Node definition1 = Node.NAME("b");
    assertSame(definition1, mustBeReachingVariableDef.getDef("a", assign1));

    // Second definition
    MustDef result2 = mustBeReachingVariableDef.flowThrough(assign2, result1);
    Node definition2 = Node.INT_LITERAL(1);
    assertSame(definition2, mustBeReachingVariableDef.getDef("a", assign2));

    // Use
    Node use = Node.NAME("a");
    assertSame(definition2, mustBeReachingVariableDef.getDef("a", use));
  }

  @Test
  public void testUndefined() {
    MustBeReachingVariableDef mustBeReachingVariableDef =
        new MustBeReachingVariableDef(cfg, jsScope, compiler);
    MustDef initial = mustBeReachingVariableDef.computeInitialEstimateLattice();

    Node use = Node.NAME("undefinedVar");
    assertNull(mustBeReachingVariableDef.getDef("undefinedVar", use));
  }

  @Test
  public void testEscapedVariable() {
    Node assign = Node.newAssign(Token.ASSIGN, "a", Node.NAME, "escapedVar");

    MustBeReachingVariableDef mustBeReachingVariableDef =
        new MustBeReachingVariableDef(cfg, jsScope, compiler);
    Set<Var> escapedSet = Sets.newHashSet();
    mustBeReachingVariableDef.escaped = escapedSet;
    mustBeReachingVariableDef.computeEscaped(jsScope, escapedSet, compiler);

    MustDef initial = mustBeReachingVariableDef.computeInitialEstimateLattice();

    // Assigning to an escaped variable should not have any effect
    MustDef result = mustBeReachingVariableDef.flowThrough(assign, initial);
    Node use = Node.NAME("a");
    assertNull(mustBeReachingVariableDef.getDef("escapedVar", use));
  }

  @Test
  public void testDependsOnOuterScopeVar() {
    Scope outerScope = new Scope();
    outerScope.putVar("outerVar", new Var(outerScope, "outerVar", Node.UNDEFINED));
    jsScope.setParent(outerScope);

    Node assign = Node.newAssign(Token.ASSIGN, "a", Node.NAME, "outerVar");

    MustBeReachingVariableDef mustBeReachingVariableDef =
        new MustBeReachingVariableDef(cfg, jsScope, compiler);

    MustDef initial = mustBeReachingVariableDef.computeInitialEstimateLattice();
    MustDef result = mustBeReachingVariableDef.flowThrough(assign, initial);

    Node use = Node.NAME("a");
    assertNull(mustBeReachingVariableDef.getDef("outerVar", use));
    assertTrue(mustBeReachingVariableDef.dependsOnOuterScopeVars("a", use));

    // Reset the scope
    jsScope = new Scope();
    jsScope.putVar("a", new Var(jsScope, "a", Node.UNDEFINED));
  }

  @Test(expected = AssertionError.class)
  public void testUseAfterDefinition() {
    Node assign = Node.newAssign(Token.ASSIGN, "a", Node.NAME, Node.NAME("a"));

    MustBeReachingVariableDef mustBeReachingVariableDef =
        new MustBeReachingVariableDef(cfg, jsScope, compiler);
    MustDef initial = mustBeReachingVariableDef.computeInitialEstimateLattice();

    MustDef result = mustBeReachingVariableDef.flowThrough(assign, initial);

    Node use = Node.NAME("a");
    mustBeReachingVariableDef.getDef("a", use);
  }

  @Test
  public void testNestedIfStatement() {
    String code =
        "function f() {"
        + "  var a = 1;"
        + "  if (true) {"
        + "    var b = 2;"
        + "    if (false) {"
        + "      var c = 3;"
        + "    } else {"
        + "      var d = 4;"
        + "    }"
        + "  } else {"
        + "    var e = 5;"
        + "  }"
        + "  return a + b + c + d;"
        + "}";

    Node node = RhinoCompiler.compileModule(code);
    cfg = new ControlFlowGraph<Node>(node, compiler);

    MustBeReachingVariableDef mustBeReachingVariableDef =
        new MustBeReachingVariableDef(cfg, jsScope, compiler);

    MustDef initial = mustBeReachingVariableDef.computeInitialEstimateLattice();

    Node useA = Node.NAME("a");
    Node useB = Node.NAME("b");
    Node useC = Node.NAME("c");
    Node useD = Node.NAME("d");
    Node useE = Node.NAME("e");

    MustDef result = mustBeReachingVariableDef.flowThrough(node, initial);

    Node entry = Node.NAME("f");
    Node cond1 = Node.NAME("entry1");
    Node cond2 = Node.NAME("entry2");

    assertSame(entry, mustBeReachingVariableDef.getDef("a", useA));
    assertSame(Node.INT_LITERAL(1), mustBeReachingVariableDef.getDef("a", entry));

    assertSame(cond1, mustBeReachingVariableDef.getDef("b", useB));
    assertSame(Node.INT_LITERAL(2), mustBeReachingVariableDef.getDef("b", cond1));

    assertSame(cond1, mustBeReachingVariableDef.getDef("c", useC));
    assertNull(mustBeReachingVariableDef.getDef("c", useC));

    assertSame(cond2, mustBeReachingVariableDef.getDef("d", useD));
    assertSame(Node.INT_LITERAL(4), mustBeReachingVariableDef.getDef("d", cond2));

    assertNull(mustBeReachingVariableDef.getDef("e", useE));
  }
}