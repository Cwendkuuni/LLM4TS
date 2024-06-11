import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.UnreachableCodeElimination;
import com.google.javascript.jscomp.CompilerPass;
import org.junit.Before;
import org.junit.Test;

public class UnreachableCodeEliminationTest {

  private AbstractCompiler compiler;
  private UnreachableCodeElimination eliminationPass;
  private Node rootNode;

  @Before
  public void setup() {
    compiler = mock(AbstractCompiler.class);
    eliminationPass = new UnreachableCodeElimination(compiler, false);
    rootNode = new Node(Token.PROGRAM, null);
  }

  @Test
  public void testSimpleReturnStatement() {
    Node returnStatement = new Node(Token.RETURN, null);
    Node body = new Node(Token.BLOCK, null);
    Node ifStatement = new Node(Token.IF, null);
    ifStatement.addChild(new Node(Token.LITERAL, "condition"));
    ifStatement.addChild(body);
    body.addChild(returnStatement);
    rootNode.addChild(ifStatement);

    eliminationPass.process(null, rootNode);

    assertTrue(returnStatement.isEmpty());
    assertFalse(returnStatement.getParent() instanceof Node);
  }

  @Test
  public void testSimpleReturnStatementWithSideEffects() {
    Node returnStatement = new Node(Token.ASSIGN, null);
    returnStatement.addChild(new Node(Token.IDENT, "x"));
    returnStatement.addChild(new Node(Token.LITERAL, "5"));
    Node body = new Node(Token.BLOCK, null);
    Node ifStatement = new Node(Token.IF, null);
    ifStatement.addChild(new Node(Token.LITERAL, "condition"));
    ifStatement.addChild(body);
    body.addChild(returnStatement);
    rootNode.addChild(ifStatement);

    eliminationPass.process(null, rootNode);

    assertFalse(returnStatement.isEmpty());
    assertTrue(returnStatement.getParent() instanceof Node);
  }

  @Test
  public void testComplexReturnStatement() {
    Node returnStatement = new Node(Token.ASSIGN, null);
    Node assign = returnStatement.addChild(new Node(Token.IDENT, "x"));
    assign.addChild(new Node(Token.ASSIGN, null));
    assign.getLastChild().addChild(new Node(Token.IDENT, "5"));
    Node ifStatement = new Node(Token.IF, null);
    Node condition = new Node(Token.LITERAL, "condition");
    ifStatement.addChild(condition);
    Node body = new Node(Token.BLOCK, null);
    body.addChild(returnStatement);
    rootNode.addChild(ifStatement);
    rootNode.addChild(new Node(Token.RETURN, null));

    eliminationPass.process(null, rootNode);

    assertTrue(returnStatement.isEmpty());
    assertTrue(returnStatement.getParent() instanceof Node);
  }

  @Test
  public void testSimpleBreakStatement() {
    Node breakStatement = new Node(Token.BREAK, null);
    Node switchStatement = new Node(Token.SWITCH, null);
    switchStatement.addChild(new Node(Token.IDENT, "expression"));
    switchStatement.addChild(new Node(Token.BLOCK, null));
    Node block = switchStatement.getLastChild();
    block.addChild(breakStatement);
    rootNode.addChild(switchStatement);

    eliminationPass.process(null, rootNode);

    assertTrue(breakStatement.isEmpty());
    assertFalse(breakStatement.getParent() instanceof Node);
  }

  @Test
  public void testSimpleContinueStatement() {
    Node continueStatement = new Node(Token.CONTINUE, null);
    Node forStatement = new Node(Token.FOR, null);
    forStatement.addChild(new Node(Token.IDENT, "expression"));
    forStatement.addChild(new Node(Token.BLOCK, null));
    Node block = forStatement.getLastChild();
    block.addChild(continueStatement);
    rootNode.addChild(forStatement);

    eliminationPass.process(null, rootNode);

    assertTrue(continueStatement.isEmpty());
    assertFalse(continueStatement.getParent() instanceof Node);
  }

  @Test
  public void testComplexCode() {
    Node expression = new Node(Token.IDENT, "x");
    Node ifStatement = new Node(Token.IF, null);
    ifStatement.addChild(new Node(Token.LITERAL, "condition"));
    ifStatement.addChild(new Node(Token.BLOCK, null));
    Node block = ifStatement.getLastChild();
    Node returnStatement = new Node(Token.RETURN, null);
    block.addChild(returnStatement);
    Node function = new Node(Token.FUNCTION, null);
    Node body = function.addChild(new Node(Token.BLOCK, null));
    body.addChild(ifStatement);
    rootNode.addChild(function);

    eliminationPass.process(null, rootNode);

    assertTrue(returnStatement.isEmpty());
    assertTrue(returnStatement.getParent() instanceof Node);
  }

  @Test(expected = AssertionError.class)
  public void testDeadCodeAfterReturnsWithNoOpStatements() {
    Node returnStatement = new Node(Token.RETURN, null);
    Node noOpStatement = new Node(Token.ASSIGN, null);
    rootNode.addChild(returnStatement);
    rootNode.addChild(noOpStatement);

    eliminationPass.process(null, rootNode);
  }

  @Test
  public void testDeadCodeAfterReturnsWithSideEffects() {
    Node returnStatement = new Node(Token.RETURN, null);
    Node assign = new Node(Token.ASSIGN, null);
    assign.addChild(new Node(Token.IDENT, "x"));
    assign.addChild(new Node(Token.LITERAL, "5"));
    rootNode.addChild(returnStatement);
    rootNode.addChild(assign);

    eliminationPass.process(null, rootNode);

    assertFalse(returnStatement.isEmpty());
    assertTrue(returnStatement.getParent() instanceof Node);
  }

  @Test
  public void testDeadCodeBeforeReturn() {
    Node assign = new Node(Token.ASSIGN, null);
    assign.addChild(new Node(Token.IDENT, "x"));
    assign.addChild(new Node(Token.LITERAL, "5"));
    Node returnStatement = new Node(Token.RETURN, null);
    rootNode.addChild(assign);
    rootNode.addChild(returnStatement);

    eliminationPass.process(null, rootNode);

    assertTrue(assign.isEmpty());
    assertFalse(assign.getParent() instanceof Node);
  }

  @Test(expected = AssertionError.class)
  public void testDeadCodeBeforeReturnWithSideEffects() {
    Node assign = new Node(Token.ASSIGN, null);
    assign.addChild(new Node(Token.IDENT, "x"));
    assign.addChild(new Node(Token.LITERAL, "5"));
    Node returnStatement = new Node(Token.RETURN, null);
    rootNode.addChild(assign);
    rootNode.addChild(returnStatement);
    rootNode.addChild(new Node(Token.ASSIGN, null));

    eliminationPass.process(null, rootNode);
  }
}