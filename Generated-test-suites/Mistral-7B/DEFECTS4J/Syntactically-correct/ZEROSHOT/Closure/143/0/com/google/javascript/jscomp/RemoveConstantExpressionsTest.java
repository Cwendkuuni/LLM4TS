import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.ParallelCompilerPass.Result;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.TestCompiler;
import com.google.javascript.rhino.NodeFactory;

import java.util.List;

public class RemoveConstantExpressionsTest {

  private static final AbstractCompiler COMPILER = new TestCompiler();
  private static final CompilerPass REMOVE_CONSTANT_EXPRESSIONS =
      new RemoveConstantExpressions(COMPILER);

  @org.junit.BeforeClass
  public static void setUpBeforeClass() throws Exception {
    COMPILER.init();
  }

  @org.junit.AfterClass
  public static void tearDownAfterClass() throws Exception {
    COMPILER.cleanup();
  }

  @org.junit.Test
  public void testRemoveConstantExpressions() {
    Node input = NodeFactory.createText("1 + foo() + bar()");
    Result result = new Result();
    NodeTraversal.traverse(null, input, REMOVE_CONSTANT_EXPRESSIONS);
    Node output = result.getChangedNode();

    Node expected = NodeFactory.createSequence(
        NodeFactory.createName("foo"),
        NodeFactory.createName("bar")
    );

    assertTrue(output.equals(expected));
    assertFalse(result.changed);
  }

  @org.junit.Test
  public void testNoSideEffectExpressionIsRemoved() {
    Node input = NodeFactory.createText("1 + 2");
    Result result = new Result();
    NodeTraversal.traverse(null, input, REMOVE_CONSTANT_EXPRESSIONS);
    Node output = result.getChangedNode();

    assertTrue(output.equals(input));
    assertFalse(result.changed);
  }

  @org.junit.Test
  public void testSideEffectExpressionIsNotRemoved() {
    Node input = NodeFactory.createText("console.log(1 + 2)");
    Result result = new Result();
    NodeTraversal.traverse(null, input, REMOVE_CONSTANT_EXPRESSIONS);
    Node output = result.getChangedNode();

    assertTrue(output.equals(input));
    assertFalse(result.changed);
  }

  @org.junit.Test
  public void testSideEffectSubexpressionIsNotRemoved() {
    Node input = NodeFactory.createText("console.log(1 + foo())");
    Result result = new Result();
    NodeTraversal.traverse(null, input, REMOVE_CONSTANT_EXPRESSIONS);
    Node output = result.getChangedNode();

    Node expected = NodeFactory.createSequence(
        NodeFactory.createName("console.log"),
        NodeFactory.createText("1"),
        NodeFactory.createName("foo")
    );

    assertTrue(output.equals(expected));
    assertFalse(result.changed);
  }

  @org.junit.Test
  public void testSideEffectSubexpressionIsReplacedWithSideEffectExpression() {
    Node input = NodeFactory.createText("console.log(1 + foo())");
    Result result = new Result();
    NodeTraversal.traverse(null, input, REMOVE_CONSTANT_EXPRESSIONS);
    Node output = result.getChangedNode();

    Node expected = NodeFactory.createSequence(
        NodeFactory.createName("console.log"),
        NodeFactory.createName("foo")
    );

    assertTrue(output.equals(expected));
    assertFalse(result.changed);
  }

  @org.junit.Test
  public void testMultipleSideEffectSubexpressionsAreReplacedWithSideEffectExpressions() {
    Node input = NodeFactory.createText("console.log(1 + foo()) + console.log(2 + bar())");
    Result result = new Result();
    NodeTraversal.traverse(null, input, REMOVE_CONSTANT_EXPRESSIONS);
    Node output = result.getChangedNode();

    Node expected = NodeFactory.createSequence(
        NodeFactory.createName("console.log"),
        NodeFactory.createName("foo"),
        NodeFactory.createName("console.log"),
        NodeFactory.createName("bar")
    );

    assertTrue(output.equals(expected));
    assertFalse(result.changed);
  }

  @org.junit.Test
  public void testInputIsSideEffectExpression() {
    Node input = NodeFactory.createText("console.log(1 + 2)");
    Result result = new Result();
    NodeTraversal.traverse(null, input, REMOVE_CONSTANT_EXPRESSIONS);
    Node output = result.getChangedNode();

    assertTrue(output.equals(input));
    assertFalse(result.changed);
  }

  @org.junit.Test
  public void testInputIsSideEffectStatement() {
    Node input = NodeFactory.createText("console.log(1 + 2);");
    Result result = new Result();
    NodeTraversal.traverse(null, input, REMOVE_CONSTANT_EXPRESSIONS);
    Node output = result.getChangedNode();

    assertTrue(output.equals(input));
    assertFalse(result.changed);
  }

  @org.junit.Test
  public void testInputIsEmptyStatement() {
    Node input = NodeFactory.createText(";");
    Result result = new Result();
    NodeTraversal.traverse(null, input, REMOVE_CONSTANT_EXPRESSIONS);
    Node output = result.getChangedNode();

    assertTrue(output.equals(input));
    assertFalse(result.changed);
  }

  @org.junit.Test
  public void testInputIsComment() {
    Node input = NodeFactory.createComment("// This is a comment");
    Result result = new Result();
    NodeTraversal.traverse(null, input, REMOVE_CONSTANT_EXPRESSIONS);
    Node output = result.getChangedNode();

    assertTrue(output.equals(input));
    assertFalse(result.changed);
  }

  @org.junit.Test
  public void testInputIsVariableDeclaration() {
    Node input = NodeFactory.createVar("x", null, null, NodeFactory.createInt(1));
    Result result = new Result();
    NodeTraversal.traverse(null, input, REMOVE_CONSTANT_EXPRESSIONS);
    Node output = result.getChangedNode();

    assertTrue(output.equals(input));
    assertFalse(result.changed);
  }

  @org.junit.Test
  public void testInputIsVariableAssignment() {
    Node input = NodeFactory.createAssignment(
        NodeFactory.createName("x"), NodeFactory.createInt(1)
    );
    Result result = new Result();
    NodeTraversal.traverse(null, input, REMOVE_CONSTANT_EXPRESSIONS);
    Node output = result.getChangedNode();

    assertTrue(output.equals(input));
    assertFalse(result.changed);
  }

  @org.junit.Test
  public void testInputIsExpressionStatement() {
    Node input = NodeFactory.createExpressionStatement(
        NodeFactory.createText("1 + 2")
    );
    Result result = new Result();
    NodeTraversal.traverse(null, input, REMOVE_CONSTANT_EXPRESSIONS);
    Node output = result.getChangedNode();

    assertTrue(output.equals(input));
    assertFalse(result.changed);
  }

  @org.junit.Test
  public void testInputIsStatement() {
    Node input = NodeFactory.createStatement(
        NodeFactory.createExpressionStatement(
            NodeFactory.createText("1 + 2")
        )
    );
    Result result = new Result();
    NodeTraversal.traverse(null, input, REMOVE_CONSTANT_EXPRESSIONS);
    Node output = result.getChangedNode();

    assertTrue(output.equals(input));
    assertFalse(result.changed);
  }
}

class TestCompiler extends AbstractCompiler {
  @Override
  public void process(Node externs, Node root) {
    super.process(externs, root);
  }
}