import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.Node;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.BitSet;
import java.util.Set;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class LiveVariablesAnalysisTest {

 private ControlFlowGraph<Node> controlFlowGraph;
 private Scope jsScope;

 @Before
 public void setup() {
 controlFlowGraph = mock(ControlFlowGraph.class);
 jsScope = mock(Scope.class);
 }

 @Test
 public void testLiveVariablesAnalysis_SimpleVariable() {
 LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(controlFlowGraph, jsScope, null);

 // Test for a simple variable that is live throughout the method
 Var simpleVariable = new Var("simpleVariable", 0);
 when(jsScope.getVar(simpleVariable.getName())).thenReturn(simpleVariable);
 when(controlFlowGraph.getOutEdges(Mockito.any())).thenReturn(null);

 analysis.computeLattice(NodeUtil.createRootNode(), analysis.createEntryLattice());

 // Check if the variable is live at the end of the method
 assertTrue(analysis.isLive(simpleVariable));
 }

 @Test
 public void testLiveVariablesAnalysis_DeadVariable() {
 LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(controlFlowGraph, jsScope, null);

 // Test for a variable that is dead at the end of the method
 Var deadVariable = new Var("deadVariable", 1);
 when(jsScope.getVar(deadVariable.getName())).thenReturn(deadVariable);
 when(controlFlowGraph.getOutEdges(Mockito.any()))
 .thenReturn(List.of(new DiGraphEdge<>(Mockito.any(), Mockito.argThat(e -> Branch.ON_EX.equals(e.getValue())))));

 analysis.computeLattice(NodeUtil.createRootNode(), analysis.createEntryLattice());

 // Check if the variable is not live at the end of the method
 assertFalse(analysis.isLive(deadVariable));
 }

 @Test
 public void testLiveVariablesAnalysis_EscapedVariable() {
 LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(controlFlowGraph, jsScope, null);

 // Test for a variable that escapes the function and is in the escaped locals set
 Var escapedVariable = new Var("escapedVariable", 2);
 when(jsScope.getVar(escapedVariable.getName())).thenReturn(escapedVariable);
 when(controlFlowGraph.getOutEdges(Mockito.any())).thenReturn(null);
 analysis.getEscapedLocals().add(escapedVariable);

 analysis.computeLattice(NodeUtil.createRootNode(), analysis.createEntryLattice());

 // Check if the variable is live even though it escapes the function
 assertTrue(analysis.isLive(escapedVariable));
 }
}