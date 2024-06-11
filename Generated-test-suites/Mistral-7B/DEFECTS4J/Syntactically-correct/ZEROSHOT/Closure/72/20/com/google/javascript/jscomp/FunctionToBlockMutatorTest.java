import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import com.google.javascript.jscomp.FunctionToBlockMutator;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.Node;

import org.junit.Test;

public class FunctionToBlockMutatorTest {

  @Test
  public void testMutateFunctionWithArguments() {
    AbstractCompiler compiler = new AbstractCompiler();
    Supplier<String> safeNameIdSupplier = () -> "test_id";
    FunctionToBlockMutator functionToBlockMutator =
        new FunctionToBlockMutator(compiler, safeNameIdSupplier);

    String fnName = "testFunction";
    Node fnNode = NodeUtil.newFunctionNode(Token.FUNCTION, null, null, "testFunction", Lists.newArrayList());
    Node callNode = NodeUtil.newCallNode(fnName, Lists.newArrayList(
        NodeUtil.newNumberLiteral(1), NodeUtil.newNumberLiteral(2)));
    String resultName = "result";
    boolean needsDefaultResult = false;
    boolean isCallInLoop = false;

    Node newFnNode = functionToBlockMutator.mutate(fnName, fnNode, callNode, resultName, needsDefaultResult, isCallInLoop);

    assertNotNull(newFnNode);

    Node newBlock = NodeUtil.getFunctionBody(newFnNode);
    List<Node> args = FunctionArgumentInjector.getFunctionCallParameterMap(
        newFnNode, callNode, safeNameIdSupplier).values();

    for (Node arg : args) {
      assertEquals(arg.getType(), Token.VAR);
      assertEquals(arg.getFirstChild().getStringValue(), resultName);
    }

    Node argument = args.get(0);
    Node argumentAssign = NodeUtil.getFirstChildOfType(newBlock, Token.ASSIGN);
    assertEquals(argumentAssign.getFirstChild(), argument);

    assertEquals(newBlock.getLastChild().getType(), Token.RETURN);
  }

  @Test
  public void testMutateFunctionWithoutArguments() {
    AbstractCompiler compiler = new AbstractCompiler();
    Supplier<String> safeNameIdSupplier = () -> "test_id";
    FunctionToBlockMutator functionToBlockMutator =
        new FunctionToBlockMutator(compiler, safeNameIdSupplier);

    String fnName = "testFunction";
    Node fnNode = NodeUtil.newFunctionNode(Token.FUNCTION, null, null, "testFunction", Lists.newArrayList());
    Node callNode = NodeUtil.newCallNode(fnName, Lists.newArrayList());
    String resultName = "result";
    boolean needsDefaultResult = false;
    boolean isCallInLoop = false;

    Node newFnNode = functionToBlockMutator.mutate(fnName, fnNode, callNode, resultName, needsDefaultResult, isCallInLoop);

    assertNotNull(newFnNode);

    Node newBlock = NodeUtil.getFunctionBody(newFnNode);
    assertNull(newBlock.getFirstChild());
    assertEquals(newBlock.getLastChild().getType(), Token.RETURN);
  }

  @Test
  public void testMutateFunctionWithArgumentsInLoop() {
    AbstractCompiler compiler = new AbstractCompiler();
    Supplier<String> safeNameIdSupplier = () -> "test_id";
    FunctionToBlockMutator functionToBlockMutator =
        new FunctionToBlockMutator(compiler, safeNameIdSupplier);

    String fnName = "testFunction";
    Node fnNode = NodeUtil.newFunctionNode(Token.FUNCTION, null, null, "testFunction", Lists.newArrayList());
    Node callNode = NodeUtil.newCallNode(fnName, Lists.newArrayList(
        NodeUtil.newNumberLiteral(1), NodeUtil.newNumberLiteral(2)));
    String resultName = "result";
    boolean needsDefaultResult = false;
    boolean isCallInLoop = true;

    Node newFnNode = functionToBlockMutator.mutate(fnName, fnNode, callNode, resultName, needsDefaultResult, isCallInLoop);

    assertNotNull(newFnNode);

    Node newBlock = NodeUtil.getFunctionBody(newFnNode);
    List<Node> args = FunctionArgumentInjector.getFunctionCallParameterMap(
        newFnNode, callNode, safeNameIdSupplier).values();

    for (Node arg : args) {
      assertEquals(arg.getType(), Token.VAR);
      assertEquals(arg.getFirstChild().getStringValue(), resultName);
    }

    Node argument = args.get(0);
    Node argumentAssign = NodeUtil.getFirstChildOfType(newBlock, Token.ASSIGN);
    assertEquals(argumentAssign.getFirstChild(), argument);

    // Make sure that the variables are initialized
    Node firstChild = newBlock.getFirstChild();
    assertEquals(firstChild.getType(), Token.VAR);
    assertEquals(firstChild.getFirstChild().getStringValue(), resultName);
    assertEquals(firstChild.getLastChild().getType(), Token.ASSIGN);
    assertEquals(firstChild.getLastChild().getFirstChild().getType(), Token.UNDEFINED);

    assertEquals(newBlock.getLastChild().getType(), Token.BREAK);
  }
}