import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import com.google.common.collect.Sets;
import com.google.javascript.jscomp.ControlFlowGraph.AbstractCfgNodeTraversalCallback;
import com.google.javascript.jscomp.ControlFlowGraph.Branch;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.Node;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

public class MustBeReachingVariableDefTest {

  private ControlFlowGraph<Node> cfg;
  private Scope jsScope;
  private AbstractCompiler compiler;

  @Before
  public void setUp() {
    // Create a simple control flow graph for testing purpose.
    Node root = Node.newBlock("root");
    Node entry = Node.newFunction("entry", root);
    Node assignment = Node.newAssignment(Node.newName("x"), Node.newNumber(1));
    Node ifStatement = Node.newIf(Node.newBooleanLiteral(true));
    Node elseStatement = Node.newIf(Node.newBooleanLiteral(false));
    Node function = Node.newFunction("test", entry);

    Node xDef = Node.newVar("x", entry);
    Node xUse = Node.newGet(Node.newName("x"), Node.newName("x"), null);

    entry.setLastChild(assignment);
    ifStatement.setLastChild(xUse);
    elseStatement.setLastChild(Node.newPrint(Node.newString("else")));
    function.setLastChild(ifStatement);
    function.setFirstChild(elseStatement);

    Node call = Node.newCall(function, Node.newArray(), null);
    root.setLastChild(call);

    this.cfg = new ControlFlowGraph<Node>(root, null);

    // Create a simple scope for testing purpose.
    this.jsScope = new Scope();
    this.jsScope.declareVar("x", Node.newRootNode());

    this.compiler = new AbstractCompiler() {
      @Override
      public ControlFlowGraph<Node> buildControlFlowGraph(Node n, Scope scope) {
        return cfg;
      }
    };
  }

  @Test
  public void testComputeMustDef_Assignments() {
    MustBeReachingVariableDef analyzer = new MustBeReachingVariableDef(
        cfg, jsScope, compiler);
    Node useNode = cfg.getNode(Node.newName("x")).getFirstChild();
    Node defNode = analyzer.getDef("x", useNode);
    assertSame(assignment, defNode);
  }

  @Test
  public void testComputeMustDef_Conditional() {
    MustBeReachingVariableDef analyzer = new MustBeReachingVariableDef(
        cfg, jsScope, compiler);
    Node useNode = cfg.getNode(ifStatement).getLastChild();
    assertNull(analyzer.getDef("x", useNode));
  }

  @Test
  public void testComputeMustDef_FunctionCall() {
    MustBeReachingVariableDef analyzer = new MustBeReachingVariableDef(
        cfg, jsScope, compiler);
    Node useNode = cfg.getNode(call).getLastChild();
    assertNull(analyzer.getDef("x", useNode));
  }

  @Test
  public void testComputeMustDef_EscapedVariable() {
    MustBeReachingVariableDef analyzer = new MustBeReachingVariableDef(
        cfg, jsScope, compiler);
    this.jsScope.declareVar("y", Node.newName("y"));
    this.escaped.add(this.jsScope.getVar("y"));
    analyzer = new MustBeReachingVariableDef(
        cfg, jsScope, compiler);
    Node useNode = cfg.getNode(Node.newName("x")).getFirstChild();
    assertNull(analyzer.getDef("y", useNode));
  }

  @Test
  public void testComputeMustDef_Dependency() {
    MustBeReachingVariableDef analyzer = new MustBeReachingVariableDef(
        cfg, jsScope, compiler);
    Node useNode = cfg.getNode(assignment).getLastChild();
    assertSame(xDef, analyzer.getDef("x", useNode));

    Set<Var> deps = Sets.newHashSet();
    analyzer.computeDependence(new Definition(xDef), xUse);
    for (Var dep : xDef.depends) {
      deps.add(dep);
    }
    assertSame(jsScope.getVar("x"), analyzer.jsScope.getVar("x"));
    assertSame(jsScope.getVar("x"), deps.iterator().next());
  }

  @Test
  public void testDependsonOuterScopeVariables_False() {
    MustBeReachingVariableDef analyzer = new MustBeReachingVariableDef(
        cfg, jsScope, compiler);
    Node useNode = cfg.getNode(assignment).getLastChild();
    assertFalse(analyzer.dependsOnOuterScopeVars("x", useNode));
  }

  @Test
  public void testDependsonOuterScopeVariables_True() {
    Scope outerScope = new Scope();
    outerScope.declareVar("w", Node.newRootNode());
    this.jsScope = outerScope;
    this.jsScope.declareVar("x", Node.newRootNode());
    MustBeReachingVariableDef analyzer = new MustBeReachingVariableDef(
        cfg, jsScope, compiler);
    Node useNode = cfg.getNode(assignment).getLastChild();
    assertTrue(analyzer.dependsOnOuterScopeVars("w", useNode));
  }
}