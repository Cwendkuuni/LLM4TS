import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.ParallelCompilerPass;
import com.google.javascript.rhino.Token;

import java.util.List;

public class RemoveConstantExpressionsTest {

  abstract static class TestCompiler extends AbstractCompiler {
    public TestCompiler() {
      super(null, null, null);
    }

    public ParallelCompilerPass getPass() {
      return (ParallelCompilerPass) getPasses().get(0);
    }

    public void assertNodeEquals(Node expected, Node actual) {
      StringBuilder sb = new StringBuilder();
      printNode(sb, expected, 0);
      String expectedString = sb.toString();
      sb.setLength(0);
      printNode(sb, actual, 0);
      String actualString = sb.toString();

      assertTrue("Expected: " + expectedString, expectedString.equals(actualString));
    }
  }

  public static class RemoveConstantExpressionsTestWithNodeInput extends TestCompiler {

    @Override
    protected List<CompilerPass> getPasses() {
      List<CompilerPass> passes = super.getPasses();
      passes.add(new RemoveConstantExpressions(this));
      return passes;
    }

    @ org.junit.Test
    public void testRemoveConstantExpression() {
      String input = "1 + foo() + bar()";

      Node root = parseTree(input);
      process(root);

      Node expected = new Node(Token.EXPR_LIST, Lists.newArrayList(
          new Node(Token.NAME, "foo"),
          new Node(Token.NAME, "bar")
      ));

      assertNodeEquals(expected, root.getFirstChild());
    }

    @ org.junit.Test
    public void testRemoveConstantExpressionWithParenthesis() {
      String input = "(1 + foo() + bar())";

      Node root = parseTree(input);
      process(root);

      Node expected = new Node(Token.NAME, "foo");
      expected.addChildToEnd(new Node(Token.NAME, "bar"));

      assertNodeEquals(expected, root.getFirstChild());
    }

    @ org.junit.Test
    public void testRemoveConstantExpressionWithSideEffect() {
      String input = "console.log(1 + foo() + bar())";

      Node root = parseTree(input);
      process(root);

      Node expected = new Node(Token.CALL, new Node(Token.NAME, "console.log"),
          Lists.newArrayList(
              new Node(Token.NAME, "foo"),
              new Node(Token.NAME, "bar")
          )
      );

      assertNodeEquals(expected, root);
    }

    @ org.junit.Test
    public void testRemoveConstantExpressionWithSideEffectInsideParenthesis() {
      String input = "(console.log(1 + foo() + bar()))";

      Node root = parseTree(input);
      process(root);

      Node expected = new Node(Token.CALL, new Node(Token.NAME, "console.log"),
          Lists.newArrayList(
              new Node(Token.NAME, "foo"),
              new Node(Token.NAME, "bar")
          )
      );

      assertNodeEquals(expected, root);
    }

    @ org.junit.Test
    public void testRemoveConstantExpressionWithSideEffectInsideAnotherSideEffect() {
      String input = "console.log(1 + foo()) + bar()";

      Node root = parseTree(input);
      process(root);

      Node expected = new Node(Token.EXPR_LIST, Lists.newArrayList(
          new Node(Token.CALL, new Node(Token.NAME, "console.log"),
              Lists.newArrayList(new Node(Token.NAME, "foo"))),
          new Node(Token.NAME, "bar")
      ));

      assertNodeEquals(expected, root);
    }

    @ org.junit.Test
    public void testRemoveConstantExpressionWithNoSideEffect() {
      String input = "1 + foo() + bar()";
      Node root = parseTree(input);

      assertFalse("Should not change the node if there are no side effects",
          process(root) != root);

      assertNodeEquals(new Node(Token.EXPR_LIST, Lists.newArrayList(
          new Node(Token.NAME, "foo"),
          new Node(Token.NAME, "bar")
      )), root.getFirstChild());
    }
  }

  public static class RemoveConstantExpressionsTestWithTreeInput extends TestCompiler {

    @Override
    protected List<CompilerPass> getPasses() {
      List<CompilerPass> passes = super.getPasses();
      passes.add(new RemoveConstantExpressions(this));
      return passes;
    }

    @ org.junit.Test
    public void testRemoveConstantExpressionWithTreeInput() {
      String input = "1 + foo() + bar()";
      Node root = parseTree(input);

      process(root);

      Node expected = new Node(Token.EXPR_LIST, Lists.newArrayList(
          new Node(Token.NAME, "foo"),
          new Node(Token.NAME, "bar")
      ));

      assertNodeEquals(expected, root.getFirstChild());
    }
  }

  private Node parseTree(String input) {
    return getSourceFactory().makeSingleStatement(input);
  }

  private boolean process(Node node) {
    NodeTraversal traversal = new NodeTraversal(getSourceFactory(), null, null, null);
    getPass().process(null, node);
    return !traversal.getCallback().getResult().changed;
  }
}