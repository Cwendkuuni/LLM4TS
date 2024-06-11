import static org.junit.Assert.*;
import static com.google.javascript.jscomp.MustBeReachingVariableDef.*;

import com.google.common.collect.ImmutableSet;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.Node;

import org.junit.Test;

public class MustBeReachingVariableDefTest {

  @Test
  public void testSimpleVariable() {
    Scope scope = new Scope();
    AbstractCompiler compiler = new AbstractCompiler();
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();

    MustBeReachingVariableDef analyzer = new MustBeReachingVariableDef(cfg, scope, compiler);
    Node root = new Node(Token.BLOCK, ImmutableSet.of());
    Node aAssignment = new Node(Token.ASSIGN, ImmutableSet.of(new Node(Token.NAME, "a")));
    Node bAssignment = new Node(Token.ASSIGN, ImmutableSet.of(new Node(Token.NAME, "b")));
    Node useA = new Node(Token.NAME, "a");
    Node useB = new Node(Token.NAME, "b");

    root.appendChild(aAssignment);
    root.appendChild(bAssignment);
    root.appendChild(useA);
    root.appendChild(useB);

    cfg.setRootNode(root);
    analyzer.flowThrough(root, analyzer.createInitialEstimateLattice());

    MustDef output = analyzer.getAnalysis(root);

    Definition aDef = output.reachingDef.get(scope.getVar("a"));
    assertNotNull(aDef);
    assertEquals(aAssignment, aDef.node);

    Definition bDef = output.reachingDef.get(scope.getVar("b"));
    assertNotNull(bDef);
    assertEquals(bAssignment, bDef.node);
  }

  @Test
  public void testComplexVariable() {
    Scope scope = new Scope();
    AbstractCompiler compiler = new AbstractCompiler();
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();

    MustBeReachingVariableDef analyzer = new MustBeReachingVariableDef(cfg, scope, compiler);
    Node root = new Node(Token.BLOCK, ImmutableSet.of());
    Node aAssignment = new Node(Token.ASSIGN, ImmutableSet.of(new Node(Token.NAME, "a")));
    Node bAssignment = new Node(Token.ASSIGN, ImmutableSet.of(new Node(Token.NAME, "b")));
    Node cAssignment = new Node(Token.ASSIGN, ImmutableSet.of(new Node(Token.NAME, "c")));
    Node dAssignment = new Node(Token.ASSIGN, ImmutableSet.of(new Node(Token.NAME, "d")));
    Node useA = new Node(Token.NAME, "a");
    Node useB = new Node(Token.NAME, "b");
    Node useC = new Node(Token.NAME, "c");
    Node useD = new Node(Token.NAME, "d");

    root.appendChild(aAssignment);
    root.appendChild(bAssignment);
    root.appendChild(cAssignment);
    root.appendChild(dAssignment);
    root.appendChild(useA);
    root.appendChild(useB);
    root.appendChild(useC);
    root.appendChild(useD);

    cfg.setRootNode(root);
    analyzer.flowThrough(root, analyzer.createInitialEstimateLattice());

    MustDef output = analyzer.getAnalysis(root);

    Definition aDef = output.reachingDef.get(scope.getVar("a"));
    assertNotNull(aDef);
    assertEquals(aAssignment, aDef.node);

    Definition bDef = output.reachingDef.get(scope.getVar("b"));
    assertNotNull(bDef);
    assertEquals(bAssignment, bDef.node);

    Definition cDef = output.reachingDef.get(scope.getVar("c"));
    assertNotNull(cDef);
    assertEquals(cAssignment, cDef.node);

    Definition dDef = output.reachingDef.get(scope.getVar("d"));
    assertNotNull(dDef);
    assertEquals(dAssignment, dDef.node);
  }

  @Test
  public void testDefinitionsFromDifferentPaths() {
    Scope scope = new Scope();
    AbstractCompiler compiler = new AbstractCompiler();
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();

    MustBeReachingVariableDef analyzer = new MustBeReachingVariableDef(cfg, scope, compiler);
    Node root = new Node(Token.BLOCK, ImmutableSet.of());
    Node aAssignment1 = new Node(Token.ASSIGN, ImmutableSet.of(new Node(Token.NAME, "a")));
    Node aAssignment2 = new Node(Token.ASSIGN, ImmutableSet.of(new Node(Token.NAME, "a")));
    Node useA1 = new Node(Token.NAME, "a");
    Node useA2 = new Node(Token.NAME, "a");

    root.appendChild(new Node(Token.IF, ImmutableSet.of(new Node(Token.NAME, "x"))));
    root.appendChild(new Node(Token.BLOCK, ImmutableSet.of()));
    root.getLastChild().appendChild(aAssignment1);
    root.getLastChild().appendChild(new Node(Token.GOTO, ImmutableSet.of(new Node(Token.NAME, "exit"))));

    root.appendChild(new Node(Token.ELSE));
    root.getLastChild().appendChild(new Node(Token.BLOCK, ImmutableSet.of()));
    root.getLastChild().appendChild(new Node(Token.BLOCK, ImmutableSet.of()));
    root.getLastChild().getLastChild().appendChild(aAssignment2);
    root.getLastChild().getLastChild().appendChild(new Node(Token.NAME, "exit"));

    root.appendChild(new Node(Token.NAME, "exit"));

    root.appendChild(useA1);
    root.appendChild(useA2);

    cfg.setRootNode(root);
    analyzer.flowThrough(root, analyzer.createInitialEstimateLattice());

    MustDef output = analyzer.getAnalysis(root);

    Definition aDef1 = output.reachingDef.get(scope.getVar("a"));
    assertNotNull(aDef1);
    assertEquals(aAssignment1, aDef1.node);

    Definition aDef2 = output.reachingDef.get(scope.getVar("a"));
    assertNotNull(aDef2);
    assertEquals(aAssignment2, aDef2.node);
  }

  @Test
  public void testEscapedVariables() {
    Scope scope = new Scope();
    AbstractCompiler compiler = new AbstractCompiler();
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();

    MustBeReachingVariableDef analyzer = new MustBeReachingVariableDef(cfg, scope, compiler);
    Node root = new Node(Token.BLOCK, ImmutableSet.of());
    Node aAssignment = new Node(Token.ASSIGN, ImmutableSet.of(new Node(Token.NAME, "a")));
    Node escapeVariable = new Node(Token.ASSIGN, ImmutableSet.of(new Node(Token.NAME, "a")));
    Node useA = new Node(Token.NAME, "a");

    root.appendChild(aAssignment);
    root.appendChild(escapeVariable);
    root.appendChild(useA);

    cfg.setRootNode(root);
    analyzer.flowThrough(root, analyzer.createInitialEstimateLattice());

    MustDef output = analyzer.getAnalysis(root);

    Definition aDef = output.reachingDef.get(scope.getVar("a"));
    assertNull(aDef);
  }

  @Test
  public void testVariablesDefinedInDifferentScopes() {
    Scope outerScope = new Scope();
    AbstractCompiler compiler = new AbstractCompiler();
    ControlFlowGraph<Node> cfgOuter = new ControlFlowGraph<>();

    MustBeReachingVariableDef analyzerOuter = new MustBeReachingVariableDef(cfgOuter, outerScope, compiler);
    Node rootOuter = new Node(Token.BLOCK, ImmutableSet.of());
    Node outerScopeDefinition = new Node(Token.ASSIGN, ImmutableSet.of(new Node(Token.NAME, "outerScopeVariable"))) ;
    Node outerScopeUse = new Node(Token.NAME, "outerScopeVariable");

    rootOuter.appendChild(outerScopeDefinition);
    rootOuter.appendChild(outerScopeUse);

    cfgOuter.setRootNode(rootOuter);
    analyzerOuter.flowThrough(rootOuter, analyzerOuter.createInitialEstimateLattice());

    Scope innerScope = new Scope(outerScope);
    MustBeReachingVariableDef analyzerInner = new MustBeReachingVariableDef(cfgOuter, innerScope, compiler);
    Node rootInner = new Node(Token.BLOCK, ImmutableSet.of());
    Node innerScopeDefinition = new Node(Token.ASSIGN, ImmutableSet.of(new Node(Token.NAME, "innerScopeVariable"))) ;
    Node innerScopeUse = new Node(Token.NAME, "innerScopeVariable");

    rootInner.appendChild(innerScopeDefinition);
    rootInner.appendChild(innerScopeUse);

    Node outerRoot = new Node(Token.BLOCK, ImmutableSet.of(rootOuter, rootInner));

    cfgOuter.setRootNode(outerRoot);
    analyzerInner.flowThrough(outerRoot, analyzerOuter.getAnalysis(rootOuter));

    MustDef output = analyzerInner.getAnalysis(outerRoot);

    Definition outerScopeDefinitionDef = output.reachingDef.get(outerScope.getVar("outerScopeVariable"));
    assertNotNull(outerScopeDefinitionDef);
    assertEquals(outerScopeDefinition, outerScopeDefinitionDef.node);

    Definition innerScopeDefinitionDef = output.reachingDef.get(innerScope.getVar("innerScopeVariable"));
    assertNotNull(innerScopeDefinitionDef);
    assertEquals(innerScopeDefinition, innerScopeDefinitionDef.node);

    Definition outerScopeUseDef = output.reachingDef.get(outerScope.getVar("outerScopeVariable"));
    assertNotNull(outerScopeUseDef);
    assertNull(outerScopeUseDef.node);

    Definition innerScopeUseDef = output.reachingDef.get(innerScope.getVar("innerScopeVariable"));
    assertNotNull(innerScopeUseDef);
    assertNull(innerScopeUseDef.node);
  }

  @Test
  public void testVariablesThatDependOnOuterScopeVariables() {
    Scope outerScope = new Scope();
    AbstractCompiler compiler = new AbstractCompiler();
    ControlFlowGraph<Node> cfgOuter = new ControlFlowGraph<>();

    MustBeReachingVariableDef analyzerOuter = new MustBeReachingVariableDef(cfgOuter, outerScope, compiler);
    Node rootOuter = new Node(Token.BLOCK, ImmutableSet.of());
    Node outerScopeDefinition = new Node(Token.ASSIGN, ImmutableSet.of(new Node(Token.NAME, "outerScopeVariable"))) ;
    Node outerScopeUse = new Node(Token.NAME, "outerScopeVariable");

    rootOuter.appendChild(outerScopeDefinition);
    rootOuter.appendChild(outerScopeUse);

    cfgOuter.setRootNode(rootOuter);
    analyzerOuter.flowThrough(rootOuter, analyzerOuter.createInitialEstimateLattice());

    Scope innerScope = new Scope(outerScope);
    MustBeReachingVariableDef analyzerInner = new MustBeReachingVariableDef(cfgOuter, innerScope, compiler);
    Node rootInner = new Node(Token.BLOCK, ImmutableSet.of());
    Node innerScopeDefinition = new Node(Token.ASSIGN, ImmutableSet.of(new Node(Token.NAME, "innerScopeVariable")));
    Node innerScopeUse = new Node(Token.NAME, "innerScopeVariable");

    rootInner.appendChild(innerScopeDefinition);
    rootInner.appendChild(innerScopeUse);

    Node outerRoot = new Node(Token.BLOCK, ImmutableSet.of(rootOuter, rootInner));

    cfgOuter.setRootNode(outerRoot);
    analyzerInner.flowThrough(outerRoot, analyzerOuter.getAnalysis(rootOuter));

    MustDef output = analyzerInner.getAnalysis(outerRoot);

    Definition outerScopeDefinitionDef = output.reachingDef.get(outerScope.getVar("outerScopeVariable"));
    assertNotNull(outerScopeDefinitionDef);
    assertEquals(outerScopeDefinition, outerScopeDefinitionDef.node);

    Definition innerScopeDefinitionDef = output.reachingDef.get(innerScope.getVar("innerScopeVariable"));
    assertNotNull(innerScopeDefinitionDef);
    assertNull(innerScopeDefinitionDef.node);

    assertTrue(analyzerInner.dependsOnOuterScopeVars("innerScopeVariable", outerScopeUse));
  }
}