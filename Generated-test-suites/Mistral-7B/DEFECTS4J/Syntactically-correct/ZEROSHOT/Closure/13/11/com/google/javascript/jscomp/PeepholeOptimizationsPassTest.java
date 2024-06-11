import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CompilerPassTestUtil.*;

import com.google.common.collect.Lists;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.AbstractPeepholeOptimization;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerPassTestCase;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.PeepholeOptimizationsPass;
import com.google.javascript.rhino.NodeFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Tests for {@link PeepholeOptimizationsPass}.
 */
public class PeepholeOptimizationsPassTest extends CompilerPassTestCase {

  private static class TestPeepholeOptimization1
      implements AbstractPeepholeOptimization {

    @Override
    public String getName() {
      return "TestPeepholeOptimization1";
    }

    @Override
    public void optimizeSubtree(Node root) {
      Node n = root.getFirstChild();
      while (n != null) {
        if (n.isAdd()) {
          Node left = n.getFirstChild();
          Node right = n.getNext();
          if (left.isNumber() && right.isNumber()) {
            int leftValue = ((Number) left.getValue()).intValueExact();
            int rightValue = ((Number) right.getValue()).intValueExact();
            Node result = NodeFactory.createNumber(NodeFactory.createInt(leftValue + rightValue));
            n.replaceWith(result);
          }
        }
        Node next = n.getNext();
        n = next;
      }
    }

    @Override
    public void beginTraversal(AbstractCompiler compiler) {}

    @Override
    public void endTraversal(AbstractCompiler compiler) {}
  }

  private static class TestPeepholeOptimization2
      implements AbstractPeepholeOptimization {

    @Override
    public String getName() {
      return "TestPeepholeOptimization2";
    }

    @Override
    public void optimizeSubtree(Node root) {
      Node n = root.getFirstChild();
      while (n != null) {
        if (n.isCall()) {
          Node function = n.getFirstChild();
          Node args = n.getLastChild();
          if (function.isIdentifier() && function.getValue().equals("sum")) {
            Node newFunction = NodeFactory.createCall(
                NodeFactory.createName("Array"), "sum", Lists.newArrayList(args));
            n.replaceWith(newFunction);
          }
        }
        Node next = n.getNext();
        n = next;
      }
    }

    @Override
    public void beginTraversal(AbstractCompiler compiler) {}

    @Override
    public void endTraversal(AbstractCompiler compiler) {}
  }

  @Override
  protected List<CompilerPass> getCompilerPasses() {
    return Arrays.asList(
        new CompilerPassWrapper(new CompilerOptions(), new CompilerPass() {
          @Override
          public void process(Node externs, Node root) {
            PeepholeOptimizationsPass pass =
                new PeepholeOptimizationsPass(getCompiler(),
                    new TestPeepholeOptimization1(),
                    new TestPeepholeOptimization2());
            pass.process(externs, root);
          }
        }));
  }

  public void testSimpleAddition() throws Exception {
    String input =
        "function sum(a, b) {" +
        "  return a + b;" +
        "}";

    Node root = parse(input);

    Node expected = NodeFactory.createCall(
        NodeFactory.createName("sum"), NodeFactory.createNumber(1), NodeFactory.createNumber(2));

    assertEquals(expected, getCompiler().getRootNode());
  }

  public void testFunctionCallWithArrayArgument() throws Exception {
    String input =
        "function sum(numbers) {" +
        "  var result = 0;" +
        "  for (var i = 0; i < numbers.length; i++) {" +
        "    result += numbers[i];" +
        "  }" +
        "  return result;" +
        "}";

    Node root = parse(input);

    Node expected = NodeFactory.createCall(
        NodeFactory.createName("sum"), NodeFactory.createArray(
            NodeFactory.createNumber(1),
            NodeFactory.createNumber(2),
            NodeFactory.createNumber(3)));

    assertEquals(expected, getCompiler().getRootNode());
  }
}