import static org.junit.Assert.*;
import com.google.javascript.jscomp.FunctionToBlockMutator;
import com.google.javascript.jscomp.Node;
import com.google.javascript.rhino.Token;

public class FunctionToBlockMutatorTest {

  private static Node createTestFunctionNode(String name) {
    Node functionNode = new Node(Token.FUNCTION, null, null, null);
    functionNode.addChildToBack(new Node(Token.PARAM, null, name));
    functionNode.addChildToBack(new Node(Token.BLOCK, null));
    return functionNode;
  }

  private static Node createTestCallNode(String functionName, Node arg) {
    Node callNode = new Node(Token.CALL, null, createTestFunctionNode(functionName), arg);
    return callNode;
  }

  @org.junit.Test
  public void testMutateFunction() {
    AbstractCompiler compiler = new AbstractCompiler();
    Supplier<String> safeNameIdSupplier = () -> "test";
    FunctionToBlockMutator mutator = new FunctionToBlockMutator(compiler, safeNameIdSupplier);

    String functionName = "testFunction";
    String resultName = "result";
    Node functionNode = createTestFunctionNode(functionName);
    Node callNode = createTestCallNode(functionName, new Node(Token.INT_LITERAL, null, 5));

    Node mutatedNode = mutator.mutate(functionName, functionNode, callNode, resultName, false, false);

    // TODO: Add assertions to check the mutatedNode

    assertEquals(functionNode.getLastChild().getType(), Token.RETURN);
    assertEquals(mutatedNode.getLastChild().getType(), Token.BREAK);
  }

  @org.junit.Test
  public void testMutateFunctionWithResult() {
    AbstractCompiler compiler = new AbstractCompiler();
    Supplier<String> safeNameIdSupplier = () -> "test";
    FunctionToBlockMutator mutator = new FunctionToBlockMutator(compiler, safeNameIdSupplier);

    String functionName = "testFunction";
    String resultName = "result";
    Node functionNode = createTestFunctionNode(functionName);
    Node callNode = createTestCallNode(functionName, new Node(Token.INT_LITERAL, null, 5));

    Node mutatedNode = mutator.mutate(functionName, functionNode, callNode, resultName, true, false);

    // TODO: Add assertions to check the mutatedNode

    assertEquals(functionNode.getLastChild().getType(), Token.RETURN);
    assertEquals(mutatedNode.getLastChild().getType(), Token.ASSIGN);
    assertEquals(mutatedNode.getChild(1).getType(), Token.VOID);
    assertEquals(mutatedNode.getChild(1).getNext().getType(), Token.BREAK);
  }

  @org.junit.Test
  public void testMutateFunctionWithLoop() {
    AbstractCompiler compiler = new AbstractCompiler();
    Supplier<String> safeNameIdSupplier = () -> "test";
    FunctionToBlockMutator mutator = new FunctionToBlockMutator(compiler, safeNameIdSupplier);

    String functionName = "testFunction";
    String resultName = "result";
    Node functionNode = createTestFunctionNode(functionName);
    Node callNode = createTestCallNode(functionName, new Node(Token.INT_LITERAL, null, 5));

    Node mutatedNode = mutator.mutate(functionName, functionNode, callNode, resultName, false, true);

    // TODO: Add assertions to check the mutatedNode

    // The VAR declarations should be initialized.
    for (Node c = mutatedNode; c != null; c = c.getNext()) {
      if (NodeUtil.isVar(c)) {
        Node name = c.getFirstChild();
        Node value = c.getLastChild();
        assertEquals(Token.ASSIGN, value.getType());
        assertEquals(Token.UNDEFINED, value.getFirstChild().getType());
      }
    }
  }

  @org.junit.Test
  public void testMutateFunctionWithArgs() {
    AbstractCompiler compiler = new AbstractCompiler();
    Supplier<String> safeNameIdSupplier = () -> "test";
    FunctionToBlockMutator mutator = new FunctionToBlockMutator(compiler, safeNameIdSupplier);

    String functionName = "testFunction";
    String argName = "arg";
    String resultName = "result";
    Node functionNode = createTestFunctionNode(functionName);
    Node argNode = new Node(Token.VAR, null, new Node(Token.NAME, null, argName));
    Node callNode = createTestCallNode(functionName, argNode);

    Node mutatedNode = mutator.mutate(functionName, functionNode, callNode, resultName, false, false);

    // TODO: Add assertions to check the mutatedNode

    // The arguments should be aliased and inlined.
    for (Node c = mutatedNode; c != null; c = c.getNext()) {
      if (NodeUtil.isVar(c)) {
        Node name = c.getFirstChild();
        if (name.getStringValue().equals(argName)) {
          Node value = c.getLastChild();
          assertNotNull(value);
          assertNotNull(value.getFirstChild());
          assertNotNull(value.getFirstChild().getFirstChild());
          assertEquals(Token.IDENT, value.getFirstChild().getType());
          assertEquals(argName, value.getFirstChild().getStringValue());
          break;
        }
      }
    }
  }

  @org.junit.Test
  public void testReplaceReturns() {
    AbstractCompiler compiler = new AbstractCompiler();
    Supplier<String> safeNameIdSupplier = () -> "test";
    FunctionToBlockMutator mutator = new FunctionToBlockMutator(compiler, safeNameIdSupplier);

    String functionName = "testFunction";
    String resultName = "result";
    Node functionNode = createTestFunctionNode(functionName);
    Node callNode = createTestCallNode(functionName, new Node(Token.INT_LITERAL, null, 5));

    Node mutatedNode = mutator.mutate(functionName, functionNode, callNode, resultName, false, false).getLastChild();
    Node replacedNode = replaceReturns(mutatedNode, resultName, null, false);

    // TODO: Add assertions to check the replacedNode

    assertEquals(mutatedNode.getType(), Token.BLOCK);
    assertEquals(replacedNode.getType(), Token.ASSIGN);
    assertEquals(replacedNode.getFirstChild().getType(), Token.IDENT);
    assertEquals(replacedNode.getFirstChild().getStringValue(), resultName);
    assertEquals(replacedNode.getLastChild().getType(), Token.BREAK);
  }

  @org.junit.Test
  public void testReplaceReturnsWithResult() {
    AbstractCompiler compiler = new AbstractCompiler();
    Supplier<String> safeNameIdSupplier = () -> "test";
    FunctionToBlockMutator mutator = new FunctionToBlockMutator(compiler, safeNameIdSupplier);

    String functionName = "testFunction";
    String resultName = "result";
    Node functionNode = createTestFunctionNode(functionName);
    Node callNode = createTestCallNode(functionName, new Node(Token.INT_LITERAL, null, 5));

    Node mutatedNode = mutator.mutate(functionName, functionNode, callNode, resultName, false, false).getLastChild();
    Node replacedNode = replaceReturns(mutatedNode, resultName, resultName, true);

    // TODO: Add assertions to check the replacedNode

    assertEquals(mutatedNode.getType(), Token.BLOCK);
    assertEquals(replacedNode.getType(), Token.ASSIGN);
    assertEquals(replacedNode.getFirstChild().getType(), Token.IDENT);
    assertEquals(replacedNode.getFirstChild().getStringValue(), resultName);
    assertEquals(replacedNode.getLastChild().getType(), Token.VOID);
  }

  private static Node replaceReturns(Node node, String resultName, String labelName, boolean resultMustBeSet) {
    return FunctionToBlockMutator.replaceReturns(node, resultName, labelName, resultMustBeSet);
  }
}