package com.google.javascript.jscomp.test;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.AbstractCompiler.CompilerPass;
import com.google.javascript.jscomp.AbstractNode;
import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.DataFlowAnalysis;
import com.google.javascript.jscomp.GraphUtil;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.graph.DiGraph;
import com.google.javascript.rhino.Token;

import org.junit.Test;
import org.mockito.ArgumentCaptor;

public class DeadAssignmentsEliminationTest {

  @Test
  public void testDeadAssignmentsElimination() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    ControlFlowGraph cfg = mock(ControlFlowGraph.class);
    DataFlowAnalysis liveness = mock(DataFlowAnalysis.class);

    DeadAssignmentsElimination deadAssignmentsElimination =
        new DeadAssignmentsElimination(compiler);

    when(liveness.analyze()).thenReturn(true);

    Node root = new Node(Token.PROGRAM, null);
    Node a = Node.newName("a");
    Node b = Node.newName("b");
    Node c = Node.newName("c");
    Node assign1 = new Node(Token.ASSIGN, a, new Node(Token.NUMBER, 1));
    Node assign2 = new Node(Token.ASSIGN, a, new Node(Token.NUMBER, 2));
    Node assign3 = new Node(Token.ASSIGN, b, new Node(Token.NUMBER, 1));
    Node ifStatement = new Node(Token.IF, assign1, new Node(Token.VOID, null));
    Node returnStatement = new Node(Token.RETURN, null);
    Node block = new Node(Token.BLOCK, null);

    block.addChild(ifStatement);
    block.addChild(assign2);
    block.addChild(returnStatement);
    root.addChild(block);
    root.addChild(assign3);

    Scope scope = mock(Scope.class);
    when(scope.isDeclared(a.getString(), false)).thenReturn(true);
    when(scope.getVar(a.getString())).thenReturn(new Var("a", 0));

    NodeTraversal traversal = new NodeTraversal(
        null, null, root, null, null, null, null);
    when(compiler.getTraversal()).thenReturn(traversal);

    ArgumentCaptor<CompilerPass> compilerPassCaptor =
        ArgumentCaptor.forClass(CompilerPass.class);
    when(compiler.applyPass(compilerPassCaptor.capture()))
        .thenReturn(root);

    deadAssignmentsElimination.process(null, root);

    AbstractNode expectedRoot = new Node(Token.PROGRAM, null);
    AbstractNode expectedBlock = new Node(Token.BLOCK, null);
    AbstractNode expectedIfStatement = new Node(Token.IF, assign3, null);
    AbstractNode expectedReturnStatement = new Node(Token.RETURN, null);

    Node actualRoot = compilerPassCaptor.getValue().getChild(0);
    assertThat(actualRoot).isSameInstanceAs(expectedRoot);

    AbstractNode actualBlock =
        actualRoot.getChild(0).getChild(0).getChild(0);
    assertThat(actualBlock).isSameInstanceAs(expectedBlock);

    AbstractNode actualIfStatement =
        actualBlock.getChild(0).getChild(0);
    AbstractNode actualAssign3 =
        actualBlock.getChild(0).getChild(1);
    assertThat(actualIfStatement).isSameInstanceAs(expectedIfStatement);
    assertThat(actualAssign3).isSameInstanceAs(assign3);

    assertThat(actualBlock.getChildCount()).isEqualTo(2);
    assertThat(actualReturnStatement).isSameInstanceAs(returnStatement);
  }
}