import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.collect.Sets;
import org.junit.Test;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.Scope;

public class MustBeReachingVariableDefTest {

  @Test
  public void testNewInstance() {
    Scope jsScope = mock(Scope.class);
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    Set<Var> escaped = Sets.newHashSet();

    MustBeReachingVariableDef analyzer = new MustBeReachingVariableDef(
        mock(ControlFlowGraph.class), jsScope, compiler);

    assertNull(analyzer.getDef("nonExistentVariable", null));
  }

  @Test
  public void testGetDefForLocalVariable() {
    Scope jsScope = mock(Scope.class);
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    Set<Var> escaped = Sets.newHashSet();

    ControlFlowGraph cfg = mock(ControlFlowGraph.class);
    when(cfg.hasNode(mock(Node.class))).thenReturn(true);

    Var varToTest = mock(Var.class);
    when(jsScope.getVar("testVariable")).thenReturn(varToTest);

    MustBeReachingVariableDef analyzer = new MustBeReachingVariableDef(cfg, jsScope, compiler);

    Node definitionNode = mock(Node.class);
    when(definitionNode.getString()).thenReturn("definitionNode");

    when(jsScope.getRootNode()).thenReturn(definitionNode);

    Node useNode = mock(Node.class);

    when(analyzer.getDef("testVariable", useNode)).thenReturn(definitionNode);

    assertSame(definitionNode, analyzer.getDef("testVariable", useNode));
  }

  @Test
  public void testGetDefForUndefinedVariable() {
    Scope jsScope = mock(Scope.class);
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    Set<Var> escaped = Sets.newHashSet();

    ControlFlowGraph cfg = mock(ControlFlowGraph.class);
    when(cfg.hasNode(mock(Node.class))).thenReturn(true);

    Var varToTest = mock(Var.class);
    when(jsScope.getVar("testVariable")).thenReturn(varToTest);

    MustBeReachingVariableDef analyzer = new MustBeReachingVariableDef(cfg, jsScope, compiler);

    assertNull(analyzer.getDef("testVariable", mock(Node.class)));
  }

  @Test
  public void testGetDefForVariableThatIsInExternScope() {
    Scope jsScope = mock(Scope.class);
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    Set<Var> escaped = Sets.newHashSet();

    ControlFlowGraph cfg = mock(ControlFlowGraph.class);
    when(cfg.hasNode(mock(Node.class))).thenReturn(true);

    Var varToTest = mock(Var.class);
    when(jsScope.getVar("testVariable")).thenReturn(varToTest);

    MustBeReachingVariableDef analyzer = new MustBeReachingVariableDef(cfg, jsScope, compiler);

    Node useNode = mock(Node.class);
    when(jsScope.getParentScope()).thenReturn(mock(Scope.class));

    Var parentScopeVar = mock(Var.class);
    when(parentScopeVar.getString()).thenReturn("parentScope");
    when(jsScope.getVar("parentScope")).thenReturn(parentScopeVar);

    when(analyzer.dependsOnOuterScopeVars("testVariable", useNode)).thenReturn(true);

    assertNull(analyzer.getDef("testVariable", useNode));
  }

  @Test
  public void testGetDefForVariableThatIsEscaped() {
    Scope jsScope = mock(Scope.class);
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    Set<Var> escaped = Sets.newHashSet();

    ControlFlowGraph cfg = mock(ControlFlowGraph.class);
    when(cfg.hasNode(mock(Node.class))).thenReturn(true);

    Var varToTest = mock(Var.class);
    when(jsScope.getVar("testVariable")).thenReturn(varToTest);

    MustBeReachingVariableDef analyzer = new MustBeReachingVariableDef(cfg, jsScope, compiler);

    escaped.add(varToTest);

    Node useNode = mock(Node.class);

    assertNull(analyzer.getDef("testVariable", useNode));
  }
}