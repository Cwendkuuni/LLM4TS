import static org.junit.Assert.*;
import static com.google.javascript.jscomp.MaybeReachingVariableUse.*;

import com.google.common.collect.Multimap;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.Node;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public class MaybeReachingVariableUseTest {

  @org.junit.Test
  public void testBasicFunctionality() {
    // Given a simple function with one local variable
    Scope jsScope = new Scope();
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<Node>(new Node("main"));
    MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(cfg, jsScope, null);

    // When we analyze the function
    analysis.analyze();

    // Then we should find that the local variable is only used at the end of the function
    Collection<Node> uses = analysis.getUses("x", analysis.getCfg().getNameEntryNode());
    assertEquals(1, uses.size());
    Node lastUse = uses.iterator().next();

    // When we check that the last use is indeed the last definition of the variable
    GraphNode<Node, Branch> lastDefinition = analysis.getCfg().getNode(lastUse);
    FlowState<ReachingUses> lastDefinitionState = lastDefinition.getAnnotation();
    ReachingUses lastDefinitionReachingUses = lastDefinitionState.getOut();
    assertEquals(1, lastDefinitionReachingUses.mayUseMap.size());
    assertTrue(lastDefinitionReachingUses.mayUseMap.containsKey(jsScope.getVar("x")));
  }

  @org.junit.Test
  public void testMultipleDefinitions() {
    // Given a function with multiple definitions of the same local variable
    Scope jsScope = new Scope();
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<Node>(new Node("main"));
    MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(cfg, jsScope, null);

    // When we analyze the function
    analysis.analyze();

    // Then we should find that the last definition of the variable is the only use
    Collection<Node> uses = analysis.getUses("x", analysis.getCfg().getNameEntryNode());
    assertEquals(1, uses.size());
    Node lastUse = uses.iterator().next();

    // When we check that the last use is indeed the last definition of the variable
    GraphNode<Node, Branch> lastDefinition = analysis.getCfg().getNode(lastUse);
    FlowState<ReachingUses> lastDefinitionState = lastDefinition.getAnnotation();
    ReachingUses lastDefinitionReachingUses = lastDefinitionState.getOut();
    assertEquals(1, lastDefinitionReachingUses.mayUseMap.size());
    assertTrue(lastDefinitionReachingUses.mayUseMap.containsKey(jsScope.getVar("x")));

    // Given another definition of the variable
    Node definition2 = new Node(Token.ASSIGN, null, new Node("x"), null);
    GraphNode<Node, Branch> definition2Node = new GraphNode<Node, Branch>(definition2);
    cfg.addEdge(new DiGraphEdge<Node>(analysis.getCfg().getNode(lastUse), definition2Node));
    analysis.reanalyze();

    // Then the first definition is no longer a use of the variable
    Collection<Node> usesAfterDefinition2 = analysis.getUses("x", analysis.getCfg().getNameEntryNode());
    assertEquals(0, usesAfterDefinition2.size());

    // Given the previous use of the variable
    Node previousUse = analysis.getCfg().getNameEntryNode().getFirstChild();
    Collection<Node> usesBeforeDefinition2 = analysis.getUses("x", previousUse);
    assertEquals(1, usesBeforeDefinition2.size());

    // Then the previous use is still considered a use of the variable
    Node previousDefinition = usesBeforeDefinition2.iterator().next();
    GraphNode<Node, Branch> previousDefinitionNode = analysis.getCfg().getNode(previousDefinition);
    FlowState<ReachingUses> previousDefinitionState = previousDefinitionNode.getAnnotation();
    ReachingUses previousDefinitionReachingUses = previousDefinitionState.getOut();
    assertEquals(1, previousDefinitionReachingUses.mayUseMap.size());
    assertTrue(previousDefinitionReachingUses.mayUseMap.containsKey(jsScope.getVar("x")));
  }

  @org.junit.Test
  public void testEscapedVariables() {
    // Given a function with an escaped variable
    Scope jsScope = new Scope();
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<Node>(new Node("main"));
    MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(cfg, jsScope, null);

    // When we add an escaped variable to the scope
    Var escapedVar = new Var("escape", jsScope, null);
    jsScope.addVar(escapedVar);

  // Then the escaped variable should not be considered a use of any definition
    Collection<Node> uses = analysis.getUses("escape", analysis.getCfg().getNameEntryNode());
    assertEquals(0, uses.size());

    // Given a definition of the escaped variable
    Node definition = new Node(Token.ASSIGN, null, new Node("escape"), null);
    GraphNode<Node, Branch> definitionNode = new GraphNode<Node, Branch>(definition);
    cfg.addEdge(new DiGraphEdge<Node>(analysis.getCfg().getNode(analysis.getCfg().getNameEntryNode()), definitionNode));
    analysis.reanalyze();

    // Then the definition of the escaped variable is still not considered a use
    Collection<Node> usesAfterDefinition = analysis.getUses("escape", analysis.getCfg().getNameEntryNode());
    assertEquals(0, usesAfterDefinition.size());
  }

  @org.junit.Test
  public void testNullInput() {
    // Given a null input
    MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(null, null, null);

    // Then an exception should be thrown
    try {
      analysis.analyze();
      fail("Expected AnalysisException to be thrown");
    } catch (AnalysisException e) {
      // Expected
    }
  }
}