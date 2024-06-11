import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.Token;

import java.util.List;
import java.util.Set;

public class CollapseVariableDeclarationsTest {

  private AbstractCompiler compiler;

  @org.junit.Before
  public void setUp() {
    compiler = mock(AbstractCompiler.class);
  }

  @org.junit.Test
  public void testNoCollapses() {
    // Given
    Node root = new Node(Token.PROGRAM);

    // When
    new CollapseVariableDeclarations(compiler).process(null, root);

    // Then
    assertEquals(0, ((CollapseVariableDeclarations) compiler.getPassManager()
        .getPass(CompilerPass.CollapseVariableDeclarations.class)
        .getCollapses()).size());
  }

  @org.junit.Test
  public void testSingleVariableDeclaration() {
    // Given
    Node root = new Node(Token.PROGRAM);
    Node varNode = new Node(Token.VAR, "a");
    root.addChildToBack(varNode);

    // When
    new CollapseVariableDeclarations(compiler).process(null, root);

    // Then
    assertEquals(0, ((CollapseVariableDeclarations) compiler.getPassManager()
        .getPass(CompilerPass.CollapseVariableDeclarations.class)
        .getCollapses()).size());
  }

  @org.junit.Test
  public void testMultipleVariableDeclarations() {
    // Given
    Node root = new Node(Token.PROGRAM);
    Node varNode1 = new Node(Token.VAR, "a");
    Node varNode2 = new Node(Token.VAR, "b");
    Node varNode3 = new Node(Token.VAR, "c");
    root.addChildToBack(varNode1);
    root.addChildToBack(varNode2);
    root.addChildToBack(varNode3);

    when(compiler.getLifeCycleStage().isNormalized()).thenReturn(false);

    // When
    new CollapseVariableDeclarations(compiler).process(null, root);

    // Then
    List<CollapseVariableDeclarations.Collapse> collapses = ((CollapseVariableDeclarations) compiler
        .getPassManager()
        .getPass(CompilerPass.CollapseVariableDeclarations.class)
        .getCollapses());
    assertEquals(1, collapses.size());
    assertEquals(varNode1, collapses.get(0).getStartNode());
    assertEquals(varNode3, collapses.get(0).getEndNode());
  }

  @org.junit.Test
  public void testMultipleVariableDeclarationsWithAssignments() {
    // Given
    Node root = new Node(Token.PROGRAM);
    Node varNode1 = new Node(Token.VAR, "a");
    Node assignNode1 = new Node(Token.ASSIGN, Node.OP_ASSIGN);
    Node valueNode1 = new Node(Token.NUMBER, 1.0);
    assignNode1.addChildToBack(valueNode1);
    varNode1.addChildToBack(assignNode1);
    Node varNode2 = new Node(Token.VAR, "b");
    Node assignNode2 = new Node(Token.ASSIGN, Node.OP_ASSIGN);
    Node valueNode2 = new Node(Token.NUMBER, 1.0);
    assignNode2.addChildToBack(valueNode2);
    varNode2.addChildToBack(assignNode2);
    Node varNode3 = new Node(Token.VAR, "c");
    Node assignNode3 = new Node(Token.ASSIGN, Node.OP_ASSIGN);
    Node valueNode3 = new Node(Token.NUMBER, 1.0);
    assignNode3.addChildToBack(valueNode3);
    varNode3.addChildToBack(assignNode3);
    root.addChildToBack(varNode1);
    root.addChildToBack(varNode2);
    root.addChildToBack(varNode3);

    when(compiler.getLifeCycleStage().isNormalized()).thenReturn(false);

    // When
    new CollapseVariableDeclarations(compiler).process(null, root);

    // Then
    List<CollapseVariableDeclarations.Collapse> collapses = ((CollapseVariableDeclarations) compiler
        .getPassManager()
        .getPass(CompilerPass.CollapseVariableDeclarations.class)
        .getCollapses());
    assertEquals(1, collapses.size());
    assertEquals(varNode1, collapses.get(0).getStartNode());
    assertEquals(varNode3, collapses.get(0).getEndNode());
  }

  @org.junit.Test
  public void testCollapsingAssignments() {
    // Given
    Node root = new Node(Token.PROGRAM);
    Node varNode1 = new Node(Token.VAR, "a");
    Node assignNode1 = new Node(Token.ASSIGN, Node.OP_ASSIGN);
    Node valueNode1 = new Node(Token.NUMBER, 1.0);
    assignNode1.addChildToBack(valueNode1);
    varNode1.addChildToBack(assignNode1);
    Node varNode2 = new Node(Token.VAR, "b");
    Node assignNode2 = new Node(Token.ASSIGN, Node.OP_ASSIGN);
    Node valueNode2 = new Node(Token.NUMBER, 1.0);
    assignNode2.addChildToBack(valueNode2);
    varNode2.addChildToBack(assignNode2);
    Node varNode3 = new Node(Token.VAR, "c");
    Node assignNode3 = new Node(Token.ASSIGN, Node.OP_ASSIGN);
    Node valueNode3 = new Node(Token.NUMBER, 1.0);
    assignNode3.addChildToBack(valueNode3);
    varNode3.addChildToBack(assignNode3);
    Node varNode4 = new Node(Token.VAR, "d");
    Node assignNode4 = new Node(Token.ASSIGN, Node.OP_ASSIGN);
    Node valueNode4 = new Node(Token.NUMBER, 1.0);
    assignNode4.addChildToBack(valueNode4);
    varNode4.addChildToBack(assignNode4);
    Node varNode5 = new Node(Token.VAR, "e");
    Node assignNode5 = new Node(Token.ASSIGN, Node.OP_ASSIGN);
    Node valueNode5 = new Node(Token.NUMBER, 1.0);
    assignNode5.addChildToBack(valueNode5);
    varNode5.addChildToBack(assignNode5);
    root.addChildToBack(varNode1);
    root.addChildToBack(varNode2);
    root.addChildToBack(varNode3);
    root.addChildToBack(varNode4);
    root.addChildToBack(varNode5);

    when(compiler.getLifeCycleStage().isNormalized()).thenReturn(false);

    // When
    new CollapseVariableDeclarations(compiler).process(null, root);

    // Then
    List<CollapseVariableDeclarations.Collapse> collapses = ((CollapseVariableDeclarations) compiler
        .getPassManager()
        .getPass(CompilerPass.CollapseVariableDeclarations.class)
        .getCollapses());
    assertEquals(1, collapses.size());
    assertEquals(varNode1, collapses.get(0).getStartNode());
    assertEquals(varNode5, collapses.get(0).getEndNode());
  }

  @org.junit.Test
  public void testCollapsingVariableDeclarationsWithinFunction() {
    // Given
    Node root = new Node(Token.PROGRAM);
    Node function = new Node(Token.FUNCTION, null, null, null);
    Node body = new Node(Token.BLOCK);
    root.addChildToBack(function);
    function.addChildToBack(body);

    Node varNode1 = new Node(Token.VAR, "a");
    Node assignNode1 = new Node(Token.ASSIGN, Node.OP_ASSIGN);
    Node valueNode1 = new Node(Token.NUMBER, 1.0);
    assignNode1.addChildToBack(valueNode1);
    varNode1.addChildToBack(assignNode1);
    Node varNode2 = new Node(Token.VAR, "b");
    Node assignNode2 = new Node(Token.ASSIGN, Node.OP_ASSIGN);
    Node valueNode2 = new Node(Token.NUMBER, 1.0);
    assignNode2.addChildToBack(valueNode2);
    varNode2.addChildToBack(assignNode2);
    Node varNode3 = new Node(Token.VAR, "c");
    Node assignNode3 = new Node(Token.ASSIGN, Node.OP_ASSIGN);
    Node valueNode3 = new Node(Token.NUMBER, 1.0);
    assignNode3.addChildToBack(valueNode3);
    varNode3.addChildToBack(assignNode3);

    body.addChildToBack(varNode1);
    body.addChildToBack(varNode2);
    body.addChildToBack(varNode3);

    when(compiler.getLifeCycleStage().isNormalized()).thenReturn(false);

    // When
    new CollapseVariableDeclarations(compiler).process(null, root);

    // Then
    List<CollapseVariableDeclarations.Collapse> collapses = ((CollapseVariableDeclarations) compiler
        .getPassManager()
        .getPass(CompilerPass.CollapseVariableDeclarations.class)
        .getCollapses());
    assertEquals(1, collapses.size());
    assertEquals(varNode1, collapses.get(0).getStartNode());
    assertEquals(varNode3, collapses.get(0).getEndNode());
  }
}