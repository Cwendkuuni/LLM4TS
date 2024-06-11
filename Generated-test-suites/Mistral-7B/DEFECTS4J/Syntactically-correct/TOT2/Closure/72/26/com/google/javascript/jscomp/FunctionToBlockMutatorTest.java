import com.google.javascript.jscomp.FunctionToBlockMutator;
import com.google.javascript.rhino.Node;
import org.junit.Test;
import static org.junit.Assert.assertNotNull;

public class FunctionToBlockMutatorTest {

  @Test
  public void testMutateFunctionWithNoArguments() {
    AbstractCompiler compiler = new NoOpCompiler();
    Supplier<String> safeNameIdSupplier = () -> "test";
    FunctionToBlockMutator functionToBlockMutator = new FunctionToBlockMutator(compiler, safeNameIdSupplier);

    String fnName = "testFunction";
    Node fnNode = Node.newFunction(Token.FUNCTION, null, null, null, null, null, null);
    Node callNode = new Node(Token.CALL, fnNode, null);
    String resultName = "result";

    Node mutatedBlock = functionToBlockMutator.mutate(fnName, fnNode, callNode, resultName, false, false);

    assertNotNull(mutatedBlock);
  }

  @Test
  public void testMutateFunctionWithSingleArgument() {
    AbstractCompiler compiler = new NoOpCompiler();
    Supplier<String> safeNameIdSupplier = () -> "test";
    FunctionToBlockMutator functionToBlockMutator = new FunctionToBlockMutator(compiler, safeNameIdSupplier);

    String fnName = "testFunction";
    Node fnNode = Node.newFunction(Token.FUNCTION, null, null, null, null, null, null);
    Node argNode = Node.newInt(1);
    Node callNode = Node.newCall(fnNode, argNode, null);
    String resultName = "result";

    Node mutatedBlock = functionToBlockMutator.mutate(fnName, fnNode, callNode, resultName, false, false);

    assertNotNull(mutatedBlock);
  }

  @Test
  public void testMutateFunctionWithMultipleArguments() {
    AbstractCompiler compiler = new NoOpCompiler();
    Supplier<String> safeNameIdSupplier = () -> "test";
    FunctionToBlockMutator functionToBlockMutator = new FunctionToBlockMutator(compiler, safeNameIdSupplier);

    String fnName = "testFunction";
    Node fnNode = Node.newFunction(Token.FUNCTION, null, null, null, null, null, null);
    Node arg1Node = Node.newInt(1);
    Node arg2Node = Node.newInt(2);
    Node callNode = Node.newCall(fnNode, arg1Node, arg2Node, null);
    String resultName = "result";

    Node mutatedBlock = functionToBlockMutator.mutate(fnName, fnNode, callNode, resultName, false, false);

    assertNotNull(mutatedBlock);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testMutateFunctionWithNullFnNode() {
    AbstractCompiler compiler = new NoOpCompiler();
    Supplier<String> safeNameIdSupplier = () -> "test";
    FunctionToBlockMutator functionToBlockMutator = new FunctionToBlockMutator(compiler, safeNameIdSupplier);

    String fnName = "testFunction";
    Node callNode = new Node(Token.CALL, null, null);
    String resultName = "result";

    functionToBlockMutator.mutate(fnName, null, callNode, resultName, false, false);
  }

  @Test
  public void testMutateFunctionWithEmptyFnNode() {
    AbstractCompiler compiler = new NoOpCompiler();
    Supplier<String> safeNameIdSupplier = () -> "test";
    FunctionToBlockMutator functionToBlockMutator = new FunctionToBlockMutator(compiler, safeNameIdSupplier);

    String fnName = "testFunction";
    Node fnNode = Node.newBlock(null);
    Node callNode = new Node(Token.CALL, fnNode, null);
    String resultName = "result";

    Node mutatedBlock = functionToBlockMutator.mutate(fnName, fnNode, callNode, resultName, false, false);

    assertNotNull(mutatedBlock);
  }

  @Test
  public void testMutateFunctionWithCallInLoop() {
    AbstractCompiler compiler = new NoOpCompiler();
    Supplier<String> safeNameIdSupplier = () -> "test";
    FunctionToBlockMutator functionToBlockMutator = new FunctionToBlockMutator(compiler, safeNameIdSupplier);

    String fnName = "testFunction";
    Node fnNode = Node.newFunction(Token.FUNCTION, null, null, null, null, null, null);
    Node argNode = Node.newInt(1);
    Node callNode = Node.newCall(fnNode, argNode, null);
    Node loopNode = Node.newFor(Token.FOR, null, argNode, Node.newInt(0), Node.newInt(1), null, null, null);
    Node resultName = "result";

    Node mutatedBlock = functionToBlockMutator.mutate(fnName, fnNode, callNode, resultName, true, false);

    assertNotNull(mutatedBlock);
  }
}

class NoOpCompiler implements AbstractCompiler {
  @Override
  public void reportCode(Node node, String message, int lineNumber, int column) {
  }

  @Override
  public void reportError(Node node, String message, int lineNumber, int column) {
  }

  @Override
  public void reportWarning(Node node, String message, int lineNumber, int column) {
  }

  @Override
  public void reportFatalError(String message) {
  }

  @Override
  public void reportFatalError(Node node, String message, int lineNumber, int column) {
  }

  @Override
  public Node apply(Node transformedNode, Node code) {
    return transformedNode;
  }

  @Override
  public Node apply(Node code) {
    return Node.newBlock(null);
  }
}