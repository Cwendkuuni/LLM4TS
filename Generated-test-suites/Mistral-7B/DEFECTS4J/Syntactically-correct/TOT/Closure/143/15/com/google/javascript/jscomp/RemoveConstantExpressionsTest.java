import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static com.google.javascript.jscomp.NodeTraversal.traverse;

import com.google.common.collect.Lists;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.ParallelCompilerPass;
import com.google.javascript.jscomp.Result;
import com.google.javascript.rhino.Token;

import org.junit.Before;
import org.junit.Test;

public class RemoveConstantExpressionsTest {
  private AbstractCompiler compiler;
  private RemoveConstantExpressions removeConstantExpressions;

  @Before
  public void setup() {
    compiler = new AbstractCompiler();
    removeConstantExpressions = new RemoveConstantExpressions(compiler);
  }

  @Test
  public void testRemoveConstantExpression() {
    String input = "function foo() { return 1 + 2; } function bar() { return 3 + 4; } "
        + "function main() { return foo() + bar(); }";
    Node externs = new Node(Token.PROGRAM, Lists.newArrayList());
    Node root = parseInput(input);

    Result result = new Result();
    removeConstantExpressions.process(externs, root);

    Node simplifiedRoot = result.getTransformedSourceRoot();
    Node mainFunction = getFunctionByName(simplifiedRoot, "main");
    List<Node> mainStatementList = mainFunction.getFirstChild().getChildren();

    Node fooCall = mainStatementList.get(0);
    Node barCall = mainStatementList.get(1);

    assertTrue(fooCall.toString().equals("foo()"));
    assertTrue(barCall.toString().equals("bar()"));
  }

  @Test
  public void testRemoveConstantExpressionWithSideEffects() {
    String input = "function foo() { console.log('foo'); return 1; } function bar() { console.log('bar') "
        + "return 2; } function main() { return foo() + bar(); }";
    Node externs = new Node(Token.PROGRAM, Lists.newArrayList());
    Node root = parseInput(input);

    Result result = new Result();
    removeConstantExpressions.process(externs, root);

    Node simplifiedRoot = result.getTransformedSourceRoot();
    Node mainFunction = getFunctionByName(simplifiedRoot, "main");
    List<Node> mainStatementList = mainFunction.getFirstChild().getChildren();

    Node fooCall = mainStatementList.get(0);
    Node barCall = mainStatementList.get(1);

    List<String> fooLogs = Lists.newArrayList();
    List<String> barLogs = Lists.newArrayList();

    Node foo = getFunctionByName(simplifiedRoot, "foo");
    Node bar = getFunctionByName(simplifiedRoot, "bar");

    NodeTraversal.traverse(
        new LoggingCallback(fooLogs), foo, null);
    NodeTraversal.traverse(
        new LoggingCallback(barLogs), bar, null);

    assertTrue(fooLogs.size() == 1 && fooLogs.get(0).equals("foo"));
    assertTrue(barLogs.size() == 1 && barLogs.get(0).equals("bar"));

    assertTrue(fooCall.toString().equals("foo()"));
    assertTrue(barCall.toString().equals("bar()"));
  }

  @Test
  public void testRemoveConstantExpressionWithNestedSideEffects() {
    String input = "function foo() { console.log('foo'); return 1; } function bar() { console.log('bar') "
        + "return 2; } function baz() { console.log('baz'); return 1 + foo(); } "
        + "function main() { return baz() + bar(); }";
    Node externs = new Node(Token.PROGRAM, Lists.newArrayList());
    Node root = parseInput(input);

    Result result = new Result();
    removeConstantExpressions.process(externs, root);

    Node simplifiedRoot = result.getTransformedSourceRoot();
    Node mainFunction = getFunctionByName(simplifiedRoot, "main");
    List<Node> mainStatementList = mainFunction.getFirstChild().getChildren();

    Node bazFunction = getFunctionByName(simplifiedRoot, "baz");
    Node fooCall = getFirstChildWithType(bazFunction.getFirstChild(), Token.CALL_EXPRESSION);
    Node barCall = mainStatementList.get(1);

    List<String> fooLogs = Lists.newArrayList();
    List<String> barLogs = Lists.newArrayList();

    NodeTraversal.traverse(
        new LoggingCallback(fooLogs), fooCall.getFirstChild(), null);
    NodeTraversal.traverse(
        new LoggingCallback(barLogs), bar, null);

    assertTrue(fooLogs.size() == 1 && fooLogs.get(0).equals("foo"));
    assertTrue(barLogs.size() == 1 && barLogs.get(0).equals("bar"));

    assertTrue(fooCall.toString().equals("foo()"));
    assertTrue(barCall.toString().equals("bar()"));
  }

  private Node parseInput(String input) {
    return new ParallelCompilerPass(new Parser()).process(input, null).getSourceRoot();
  }

  private Node getFunctionByName(Node node, String name) {
    for (Node child = node; child != null; child = child.getNextSibling()) {
      if (child.getType() == Token.FUNCTION_DECLARATION && child.getFirstChild().toString().equals(name)) {
        return child;
      }
    }
    return null;
  }

  private Node getFirstChildWithType(Node node, int type) {
    for (Node child = node.getFirstChild(); child != null; child = child.getNextSibling()) {
      if (child.getType() == type) {
        return child;
      }
    }
    return null;
  }

  private static class LoggingCallback implements NodeTraversal.Callback {
    private List<String> logs;

    LoggingCallback(List<String> logs) {
      this.logs = logs;
    }

    @Override
    public void visit(NodeTraversal traversal, Node node) {
      if (node.getType() == Token.EXPR_CALL && node.getFirstChild().getType() == Token.IDENTIFIER) {
        logs.add(node.getFirstChild().toString());
      }
    }
  }
}