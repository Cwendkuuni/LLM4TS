import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.base.Preconditions;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.DataFlowAnalysis;
import com.google.javascript.jscomp.GraphNode;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.Scope;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public class MaybeReachingVariableUseTest {

  private static final Scope JS_SCOPE = mock(Scope.class);
  private static final ControlFlowGraph<Node> CFG = mock(ControlFlowGraph.class);
  private static final AbstractCompiler COMPILER = mock(AbstractCompiler.class);

  @org.junit.Before
  public void setup() {
    when(JS_SCOPE.getVar("x")).thenReturn(new Var("x", 0, JS_SCOPE));
  }

  @org.junit.Test
  public void testEmptyProgram() {
    MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(CFG, JS_SCOPE, COMPILER);

    ReachingUses entry = analysis.createEntryLattice();
    ReachingUses result = analysis.run(entry);

    assertEquals(entry, result);
  }

  @org.junit.Test
  public void testAssignmentToLocalVariable() {
    Node assignment = Node.newAssignment(Token.KEYWORD_VAR, "x", Node.newNumber(1));
    Node cfgNode = Node.newRoot();
    cfgNode.addChild(assignment);

    MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(CFG, JS_SCOpe, COMPILER);
    ReachingUses input = new ReachingUses();
    ReachingUses output = analysis.flowThrough(cfgNode, input);

    Multimap<Var, Node> mayUseMap = output.mayUseMap;
    Collection<Node> uses = analysis.getUses("x", cfgNode);

    assertEquals(Sets.newHashSet(assignment), uses);
    assertEquals(1, mayUseMap.size());
    assertEquals(Sets.newHashSet(cfgNode), mayUseMap.get("x"));
  }

  @org.junit.Test
  public void testConditionalAssignment() {
    Node conditional = Node.newIf(Token.KEYWORD_IF, Node.newBoolean(true));
    Node assignment1 = Node.newAssignment(Token.KEYWORD_VAR, "x", Node.newNumber(1));
    Node assignment2 = Node.newAssignment(Token.KEYWORD_VAR, "x", Node.newNumber(2));
    Node cfgNode = Node.newRoot();
    cfgNode.addChild(conditional);
    conditional.addChild(assignment1);
    conditional.addChild(Node.newElse());
    conditional.getLastChild().addChild(assignment2);

    MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(CFG, JS_SCOpe, COMPILER);
    ReachingUses input = new ReachingUses();
    ReachingUses output = analysis.flowThrough(cfgNode, input);

    Collection<Node> uses = analysis.getUses("x", cfgNode);
    Set<Node> expectedUses = Sets.newHashSet(assignment1, assignment2);

    assertEquals(expectedUses, uses);
    assertEquals(Sets.newHashSet(assignment1, assignment2), mayUseMap.get("x"));
  }

  @org.junit.Test
  public void testAssignmentToEscapedVariable() {
    Node assignment = Node.newAssignment(Token.KEYWORD_VAR, "x", Node.newNumber(1));
    Node cfgNode = Node.newRoot();
    cfgNode.addChild(assignment);

    when(JS_SCOpe.getVar("x")).thenReturn(new Var("x", 0, JS_SCOpe) {
      @Override
      public boolean isEscaped() {
        return true;
      }
    });

    MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(CFG, JS_SCOpe, COMPILER);
    ReachingUses input = new ReachingUses();
    ReachingUses output = analysis.flowThrough(cfgNode, input);

    assertEquals(input, output);
  }

  @org.junit.Test
  public void testAssignmentToVariableWithSameNameInDifferentScopes() {
    Node assignment1 = Node.newAssignment(Token.KEYWORD_VAR, "x", Node.newNumber(1));
    Node assignment2 = Node.newAssignment(Token.KEYWORD_VAR, "x", Node.newNumber(2));
    Node cfgNode = Node.newRoot();
    cfgNode.addChild(assignment1);
    cfgNode.addChild(Node.newRoot());
    cfgNode.getLastChild().addChild(assignment2);

    MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(CFG, JS_SCOpe, COMPILER);
    ReachingUses input = new ReachingUses();
    ReachingUses output = analysis.flowThrough(cfgNode, input);

    Set<Node> expectedUses = Sets.newHashSet(assignment1, assignment2);
    Collection<Node> uses = analysis.getUses("x", cfgNode);

    assertEquals(expectedUses, uses);
    assertEquals(expectedUses, mayUseMap.get("x"));
  }

  @org.junit.Test
  public void testAssignmentToFunctionParameter() {
    Node assignment = Node.newAssignment(Token.KEYWORD_VAR, "x", Node.newNumber(1));
    Node functionCall = Node.newCall(Token.KEYWORD_FUNCTION, "func", Node.newArray(), Node.newRoot());
    Node cfgNode = Node.newRoot();
    cfgNode.addChild(functionCall);
    functionCall.addChild(Node.newRoot());
    functionCall.getLastChild().addChild(assignment);

    MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(CFG, JS_SCOpe, COMPILER);

    // Function parameters should not be included in the may use map
    ReachingUses input = new ReachingUses();
    ReachingUses output = analysis.flowThrough(cfgNode, input);
    Collection<Node> uses = analysis.getUses("x", cfgNode);

    assertEquals(Sets.emptySet(), uses);
    assertEquals(Sets.emptySet(), mayUseMap.get("x"));
  }
}