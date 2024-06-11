import static com.google.javascript.jscomp.NodeTraversal.AbstractShallowCallback.AbstractShallowCallbackType.ELIMINATION_PASS;
import static org.junit.Assert.assertEquals;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.GraphReachability;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.UnreachableCodeElimination;
import com.google.javascript.rhino.NodeFactory;

import org.junit.Before;
import org.junit.Test;

public class UnreachableCodeEliminationTest {
  private AbstractCompiler compiler;
  private NodeFactory nodeFactory;

  @Before
  public void setUp() {
    compiler = new CompilerOptions().newCompiler();
    nodeFactory = compiler.getCurrentCodingConventions().getNodeFactory();
  }

  @Test
  public void testEmptyFunction() {
    String code = "function() {}";
    Node node = nodeFactory.parseTree(code);
    UnreachableCodeElimination elimination = new UnreachableCodeElimination(compiler, true);
    elimination.process(null, node);
    assertEquals(0, node.getChildCount());
  }

  @Test
  public void testReturnStatement() {
    String code = "function f() { return; }";
    Node node = nodeFactory.parseTree(code);
    UnreachableCodeElimination elimination = new UnreachableCodeElimination(compiler, true);
    elimination.process(null, node);
    assertEquals(0, node.getChildCount());
  }

  @Test
  public void testReturnWithStatement() {
    String code = "function f() { return; x = 1; }";
    Node node = nodeFactory.parseTree(code);
    UnreachableCodeElimination elimination = new UnreachableCodeElimination(compiler, true);
    elimination.process(null, node);
    Node returnStatement = node.getFirstChild();
    assertEquals(1, node.getChildCount());
    assertEquals(returnStatement.getType(), Token.RETURN);
    assertEquals(0, returnStatement.getChildCount());
    Node xAssignment = returnStatement.getNext();
    assertEquals(xAssignment.getType(), Token.ASSIGN);
    assertEquals(1, xAssignment.getChildCount());
    assertEquals(xAssignment.getFirstChild().toString(), "1");
  }

  @Test
  public void testIfStatementWithElse() {
    String code = "function f() { if (true) {} else {} }";
    Node node = nodeFactory.parseTree(code);
    UnreachableCodeElimination elimination = new UnreachableCodeElimination(compiler, true);
    elimination.process(null, node);

    // The ELSE block should still be present since it's reachable.
    Node ifStatement = node.getFirstChild();
    assertEquals(1, ifStatement.getChildCount());
    Node ifCondition = ifStatement.getFirstChild();
    assertEquals(ifCondition.getType(), Token.IF);
    assertEquals(1, ifStatement.getLastChild().getChildCount());
    assertEquals(ifStatement.getLastChild().getChild(0).getType(), Token.ELSE);
  }

  @Test
  public void testIfStatementWithoutElse() {
    String code = "function f() { if (true) {} }";
    Node node = nodeFactory.parseTree(code);
    UnreachableCodeElimination elimination = new UnreachableCodeElimination(compiler, true);
    elimination.process(null, node);

    // The IF block should be removed since there's no ELSE block.
    assertEquals(0, node.getChildCount());
  }

  @Test
  public void testForStatementWithEmptyBody() {
    String code = "function f() { for (;;) {} }";
    Node node = nodeFactory.parseTree(code);
    UnreachableCodeElimination elimination = new UnreachableCodeElimination(compiler, true);
    elimination.process(null, node);

    // The FOR loop should still be present.
    Node forStatement = node.getFirstChild();
    assertEquals(1, forStatement.getChildCount());
    assertEquals(forStatement.getChild(0).getType(), Token.FOR);
    assertEquals(0, forStatement.getLastChild().getChildCount());
  }

  @Test
  public void testForStatementWithEmptyBodyAndContinue() {
    String code = "function f() { for (;;) continue; }";
    Node node = nodeFactory.parseTree(code);
    UnreachableCodeElimination elimination = new UnreachableCodeElimination(compiler, true);
    elimination.process(null, node);

    // The FOR loop should still be present.
    Node forStatement = node.getFirstChild();
    assertEquals(1, forStatement.getChildCount());
    assertEquals(forStatement.getChild(0).getType(), Token.FOR);
    assertEquals(0, forStatement.getLastChild().getChildCount());
  }

  @Test
  public void testForStatementWithEmptyBodyAndBreak() {
    String code = "function f() { for (;;) break; }";
    Node node = nodeFactory.parseTree(code);
    UnreachableCodeElimination elimination = new UnreachableCodeElimination(compiler, true);
    elimination.process(null, node);

    // The FOR loop should still be present.
    Node forStatement = node.getFirstChild();
    assertEquals(1, forStatement.getChildCount());
    assertEquals(forStatement.getChild(0).getType(), Token.FOR);
    assertEquals(0, forStatement.getLastChild().getChildCount());
  }

  @Test
  public void testWhileStatementWithEmptyBody() {
    String code = "function f() { while (true) {} }";
    Node node = nodeFactory.parseTree(code);
    UnreachableCodeElimination elimination = new UnreachableCodeElimination(compiler, true);
    elimination.process(null, node);

    // The WHILE loop should still be present.
    Node whileStatement = node.getFirstChild();
    assertEquals(1, whileStatement.getChildCount());
    assertEquals(whileStatement.getChild(0).getType(), Token.WHILE);
    assertEquals(0, whileStatement.getLastChild().getChildCount());
  }

  @Test
  public void testWhileStatementWithEmptyBodyAndBreak() {
    String code = "function f() { while (true) break; }";
    Node node = nodeFactory.parseTree(code);
    UnreachableCodeElimination elimination = new UnreachableCodeElimination(compiler, true);
    elimination.process(null, node);

    // The WHILE loop should still be present.
    Node whileStatement = node.getFirstChild();
    assertEquals(1, whileStatement.getChildCount());
    assertEquals(whileStatement.getChild(0).getType(), Token.WHILE);
    assertEquals(0, whileStatement.getLastChild().getChildCount());
  }

  @Test
  public void testSwitchStatementWithEmptyBody() {
    String code = "function f() { switch (1) {} }";
    Node node = nodeFactory.parseTree(code);
    UnreachableCodeElimination elimination = new UnreachableCodeElimination(compiler, true);
    elimination.process(null, node);

    // The SWITCH statement should still be present.
    Node switchStatement = node.getFirstChild();
    assertEquals(1, switchStatement.getChildCount());
    assertEquals(switchStatement.getChild(0).getType(), Token.SWITCH);
    assertEquals(0, switchStatement.getLastChild().getChildCount());
  }

  @Test
  public void testSwitchStatementWithEmptyBodyAndDefault() {
    String code = "function f() { switch (1) {} }";
    Node node = nodeFactory.parseTree(code);
    UnreachableCodeElimination elimination = new UnreachableCodeElimination(compiler, true);
    elimination.process(null, node);

    // The SWITCH statement should still be present.
    Node switchStatement = node.getFirstChild();
    assertEquals(1, switchStatement.getChildCount());
    assertEquals(switchStatement.getChild(0).getType(), Token.SWITCH);
    assertEquals(1, switchStatement.getLastChild().getChildCount());
    assertEquals(switchStatement.getLastChild().getChild(0).getType(), Token.DEFAULT);
  }

  @Test
  public void testSwitchStatementWithEmptyBodyAndCase() {
    String code = "function f() { switch (1) { case 1: } }";
    Node node = nodeFactory.parseTree(code);
    UnreachableCodeElimination elimination = new UnreachableCodeElimination(compiler, true);
    elimination.process(null, node);

    // The SWITCH statement should still be present.
    Node switchStatement = node.getFirstChild();
    assertEquals(1, switchStatement.getChildCount());
    assertEquals(switchStatement.getChild(0).getType(), Token.SWITCH);
    assertEquals(1, switchStatement.getLastChild().getChildCount());
    assertEquals(switchStatement.getLastChild().getChild(0).getType(), Token.CASE);
  }

  @Test
  public void testFunctionCallWithNoSideEffects() {
    String code = "function f() { return a.b.MyClass.prototype.propertyName; }";
    Node node = nodeFactory.parseTree(code);
    UnreachableCodeElimination elimination = new UnreachableCodeElimination(compiler, true);
    elimination.process(null, node);

    // The function call should still be present.
    Node functionCall = node.getFirstChild();
    assertEquals(1, functionCall.getChildCount());
    assertEquals(functionCall.getChild(0).toString(), "MyClass.prototype.propertyName");
  }

  @Test
  public void testTrueStatement() {
    String code = "function f() { true; }";
    Node node = nodeFactory.parseTree(code);
    UnreachableCodeElimination elimination = new UnreachableCodeElimination(compiler, true);
    elimination.process(null, node);

    // The TRUE statement should be removed since it has no side effects.
    assertEquals(0, node.getChildCount());
  }
}