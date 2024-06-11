import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeUtil.*;
import static com.google.javascript.jscomp.LiveVariablesAnalysis.*;

import com.google.common.collect.Sets;
import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.Node;

import java.util.BitSet;
import java.util.List;
import java.util.Set;

public class LiveVariablesAnalysisTest {

  @SuppressWarnings("unchecked")
  private static <T> T newInstance(Class<T> cls, Object... initargs)
      throws InstantiationException, IllegalAccessException {
    return cls.newInstance(initargs);
  }

  @org.junit.Test
  public void testLiveVariablesAnalysis() {
    // Given a simple control flow graph
    Node root = new Node(Token.SCRIPT, 0, 0);
    root.setFirstChild(new Node(Token.BLOCK, 1, 0));
    Node ifNode = new Node(Token.IF, 2, 0);
    ifNode.setFirstChild(new Node(Token.NAME, 3, "x"));
    ifNode.setLastChild(new Node(Token.BLOCK, 4, 0));
    Node elseNode = new Node(Token.ELSE, 5, 0);
    Node elseIfNode = new Node(Token.IF, 6, 0);
    elseIfNode.setFirstChild(new Node(Token.NAME, 7, "x"));
    elseNode.setLastChild(elseIfNode);
    Node elseBlock = new Node(Token.BLOCK, 8, 0);
    elseIfNode.setLastChild(elseBlock);
    root.getLastChild().setNext(ifNode);
    ifNode.getLastChild().setNext(elseNode);

    ControlFlowGraph<Node> cfg = new ControlFlowGraph<Node>(root);

    // Given a simple scope
    Scope jsScope = new Scope();
    jsScope.declareVar("x", null, 0);

    // When LiveVariablesAnalysis is run
    LiveVariablesAnalysis liveAnalysis = (LiveVariablesAnalysis) newInstance(
        LiveVariablesAnalysis.class, cfg, jsScope, null);

    // Then the live variables should be computed correctly
    LiveVariableLattice entryLattice = liveAnalysis.createEntryLattice();
    LiveVariableLattice initialEstimateLattice = liveAnalysis.createInitialEstimateLattice();
    LiveVariableLattice latticeAtRoot = liveAnalysis.flowThrough(root, entryLattice);
    LiveVariableLattice latticeAtIf = liveAnalysis.flowThrough(ifNode, latticeAtRoot);
    LiveVariableLattice latticeAtElse = liveAnalysis.flowThrough(elseNode, latticeAtRoot);

    // Verify the results
    Set<Var> expectedLiveVariables = Sets.newHashSet(jsScope.getVar("x"));
    assertEquals(expectedLiveVariables, liveAnalysis.getEscapedLocals());
    assertTrue(latticeAtRoot.isLive(jsScope.getVar("x").index));
    assertTrue(latticeAtIf.isLive(jsScope.getVar("x").index));
    assertFalse(latticeAtElse.isLive(jsScope.getVar("x").index));

    // Given a simple control flow graph with a for loop
    root = new Node(Token.SCRIPT, 0, 0);
    root.setFirstChild(new Node(Token.BLOCK, 1, 0));
    Node forNode = new Node(Token.FOR, 2, 0);
    Node init = new Node(Token.VAR, 3, "i");
    init.setNext(new Node(Token.NUMBER, 4, "0"));
    forNode.setFirstChild(init);
    Node condition = new Node(Token.NAME, 5, "i");
    condition.setNext(new Node(Token.OPERATOR, 6, Token.LT, new Node(Token.NUMBER, 7, "10")));
    forNode.setLastChild(new Node(Token.BLOCK, 8, 0));
    Node increment = new Node(Token.NAME, 9, "i");
    increment.setNext(new Node(Token.OPERATOR, 10, Token.PLUS, increment, new Node(Token.NUMBER, 11, "1")));
    forNode.getLastChild().setNext(increment);
    root.getLastChild().setNext(forNode);

    cfg = new ControlFlowGraph<Node>(root);
    jsScope = new Scope();
    jsScope.declareVar("i", null, 0);

    liveAnalysis = (LiveVariablesAnalysis) newInstance(
        LiveVariablesAnalysis.class, cfg, jsScope, null);

    // Then the live variables should be computed correctly
    entryLattice = liveAnalysis.createEntryLattice();
    initialEstimateLattice = liveAnalysis.createInitialEstimateLattice();
    latticeAtRoot = liveAnalysis.flowThrough(root, entryLattice);
    LiveVariableLattice latticeAtFor = liveAnalysis.flowThrough(forNode, latticeAtRoot);

    // Verify the results
    expectedLiveVariables = Sets.newHashSet(jsScope.getVar("i"));
    assertEquals(expectedLiveVariables, liveAnalysis.getEscapedLocals());
    assertTrue(latticeAtRoot.isLive(jsScope.getVar("i").index));
    assertTrue(latticeAtFor.isLive(jsScope.getVar("i").index));

  }

}