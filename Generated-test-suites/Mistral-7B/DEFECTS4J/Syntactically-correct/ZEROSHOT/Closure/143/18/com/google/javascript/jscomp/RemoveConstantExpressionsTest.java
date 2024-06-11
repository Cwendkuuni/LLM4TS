import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.ParallelCompilerPass;
import com.google.javascript.jscomp.Result;
import com.google.javascript.jscomp.test.TestNodeFactory;
import com.google.javascript.rhino.Token;

import java.util.List;

public class RemoveConstantExpressionsTest {

  private AbstractCompiler compiler;
  private RemoveConstantExpressions removeConstantExpressions;
  private TestNodeFactory nodeFactory;

  @org.junit.Before
  public void setUp() {
    compiler = new AbstractCompiler();
    removeConstantExpressions = new RemoveConstantExpressions(compiler);
    nodeFactory = new TestNodeFactory(compiler);
  }

  @org.junit.Test
  public void testRemoveConstantExpressionWithNoSideEffects() {
    Node root = nodeFactory.createExpressionStatement(1);
    Node constantExpression = nodeFactory.createAddition(
        nodeFactory.createNumberLiteral(1),
        nodeFactory.createIdentifier("foo"));
    root.replaceChildren(Lists.newArrayList(constantExpression));

    ParallelCompilerPass.runPasses(Lists.newArrayList(removeConstantExpressions), root);

    Node result = root;
    Node foo = result.getFirstChild();
    assertFalse("Expected foo to be present", result.hasNoChildren());
    assertFalse("Expected foo to have children", foo.hasNoChildren());
  }

  @org.junit.Test
  public void testRemoveConstantExpressionWithSideEffects() {
    Node root = nodeFactory.createExpressionStatement(1);
    Node sideEffectExpression = nodeFactory.createAssignment(
        nodeFactory.createIdentifier("x"),
        nodeFactory.createNumberLiteral(1));
    Node constantExpression = nodeFactory.createAddition(
        nodeFactory.createNumberLiteral(1),
        sideEffectExpression);
    root.replaceChildren(Lists.newArrayList(constantExpression));

    ParallelCompilerPass.runPasses(Lists.newArrayList(removeConstantExpressions), root);

    Node result = root;
    Node x = result.getFirstChild();
    assertTrue("Expected x to have children", x.hasChildren());
    Node assignment = x.getFirstChild();
    assertEquals("Assignment node type", Token.ASSIGN, assignment.getType());
    Node numberLiteral = assignment.getFirstChild();
    assertEquals("Expected number literal to be 1", 1, numberLiteral.getIntValue());
  }

  @org.junit.Test
  public void testRemoveConstantExpressionWithSideEffectsAndNoSideEffects() {
    Node root = nodeFactory.createExpressionStatement(1);
    Node sideEffectExpression = nodeFactory.createAssignment(
        nodeFactory.createIdentifier("x"),
        nodeFactory.createNumberLiteral(1));
    Node noSideEffectExpression = nodeFactory.createIdentifier("foo");
    Node constantExpression = nodeFactory.createAddition(
        nodeFactory.createNumberLiteral(1),
        sideEffectExpression, noSideEffectExpression);
    root.replaceChildren(Lists.newArrayList(constantExpression));

    ParallelCompilerPass.runPasses(Lists.newArrayList(removeConstantExpressions), root);

    Node result = root;
    Node x = result.getFirstChild();
    assertTrue("Expected x to have children", x.hasChildren());
    Node assignment = x.getFirstChild();
    assertEquals("Assignment node type", Token.ASSIGN, assignment.getType());
    Node numberLiteral = assignment.getFirstChild();
    assertEquals("Expected number literal to be 1", 1, numberLiteral.getIntValue());
    Node foo = result.getLastChild();
    assertFalse("Expected foo to have no children", foo.hasChildren());
  }

  @org.junit.Test
  public void testRemoveConstantExpressionWithComplexSideEffect() {
    Node root = nodeFactory.createExpressionStatement(1);
    Node sideEffectExpression = nodeFactory.createExpressionStatement(
        nodeFactory.createAssignment(
            nodeFactory.createIdentifier("x"),
            nodeFactory.createAddition(
                nodeFactory.createIdentifier("y"),
                nodeFactory.createNumberLiteral(1))));
    Node constantExpression = nodeFactory.createAddition(
        nodeFactory.createNumberLiteral(1),
        sideEffectExpression);
    root.replaceChildren(Lists.newArrayList(constantExpression));

    ParallelCompilerPass.runPasses(Lists.newArrayList(removeConstantExpressions), root);

    Node result = root;
    Node x = result.getFirstChild();
    assertTrue("Expected x to have children", x.hasChildren());
    Node assignment = x.getFirstChild();
    assertEquals("Assignment node type", Token.ASSIGN, assignment.getType());
    Node y = assignment.getFirstChild();
    assertEquals("Expected y to be present", assignment.getChildCount(), 1);
    Node addition = y.getFirstChild();
    assertEquals("Expected y to be an identifier", Token.IDENT, y.getType());
    assertEquals("Expected addition node type", Token.ADD, addition.getType());
    Node numberLiteral = addition.getLastChild();
    assertEquals("Expected number literal to be 1", 1, numberLiteral.getIntValue());
  }

  private void assertEquals(int expectedType, int actualType) {
    assertEquals("Expected " + expectedType + " but got " + actualType,
        expectedType, actualType);
  }
}