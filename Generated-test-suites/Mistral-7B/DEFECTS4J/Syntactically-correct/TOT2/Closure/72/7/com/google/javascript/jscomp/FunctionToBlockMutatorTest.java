import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.FunctionToBlockMutator;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.Supplier;

import org.junit.Before;
import org.junit.Test;

public class FunctionToBlockMutatorTest {

  private AbstractCompiler compiler;
  private Supplier<String> safeNameIdSupplier;
  private FunctionToBlockMutator functionToBlockMutator;

  @Before
  public void setUp() {
    compiler = mock(AbstractCompiler.class);
    safeNameIdSupplier = mock(Supplier.class);
    functionToBlockMutator = new FunctionToBlockMutator(compiler, safeNameIdSupplier);
  }

  @Test
  public void testMutateFunctionWithNoArguments() {
    String fnName = "testFunction";
    Node fnNode = Node.newFunctionNode(fnName, null, new Node[0], null, 0, 0, null);
    Node callNode = new Node(Token.CALL, fnNode, null);
    String resultName = "result";
    boolean needsDefaultResult = false;
    boolean isCallInLoop = false;

    Node mutatedFunction = functionToBlockMutator.mutate(fnName, fnNode, callNode,
        resultName, needsDefaultResult, isCallInLoop);

    assertNotNull(mutatedFunction);
  }

  @Test
  public void testMutateFunctionWithArguments() {
    String fnName = "testFunction";
    Node fnNode = Node.newFunctionNode(fnName, null, new Node[0], null, 0, 0, null);
    Node callNode = new Node(Token.CALL, fnNode, new Node[] { NodeUtil.newNumberNode(1), NodeUtil.newNumberNode(2) });
    String resultName = "result";
    boolean needsDefaultResult = false;
    boolean isCallInLoop = false;

    LinkedHashMap<String, Node> argMap = Maps.newLinkedHashMap();
    argMap.put("arg1", NodeUtil.newNumberNode(1));
    argMap.put("arg2", NodeUtil.newNumberNode(2));

    Node mutatedFunction = functionToBlockMutator.mutate(fnName, fnNode, callNode,
        resultName, needsDefaultResult, isCallInLoop);

    assertNotNull(mutatedFunction);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testMutateFunctionWithNullFnName() {
    String fnName = null;
    Node fnNode = Node.newFunctionNode("testFunction", null, new Node[0], null, 0, 0, null);
    Node callNode = new Node(Token.CALL, fnNode, null);
    String resultName = "result";
    boolean needsDefaultResult = false;
    boolean isCallInLoop = false;

    functionToBlockMutator.mutate(fnName, fnNode, callNode, resultName, needsDefaultResult, isCallInLoop);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testMutateFunctionWithNullFnNode() {
    String fnName = "testFunction";
    Node fnNode = null;
    Node callNode = new Node(Token.CALL, fnNode, null);
    String resultName = "result";
    boolean needsDefaultResult = false;
    boolean isCallInLoop = false;

    functionToBlockMutator.mutate(fnName, fnNode, callNode, resultName, needsDefaultResult, isCallInLoop);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testMutateFunctionWithNullCallNode() {
    String fnName = "testFunction";
    Node fnNode = Node.newFunctionNode(fnName, null, new Node[0], null, 0, 0, null);
    Node callNode = null;
    String resultName = "result";
    boolean needsDefaultResult = false;
    boolean isCallInLoop = false;

    functionToBlockMutator.mutate(fnName, fnNode, callNode, resultName, needsDefaultResult, isCallInLoop);
  }

  @Test
  public void testMutateFunctionWithCallInLoop() {
    String fnName = "testFunction";
    Node fnNode = Node.newFunctionNode(fnName, null, new Node[0], null, 0, 0, null);
    Node callNode = new Node(Token.CALL, fnNode, null);
    String resultName = "result";
    boolean needsDefaultResult = false;
    boolean isCallInLoop = true;

    Node mutatedFunction = functionToBlockMutator.mutate(fnName, fnNode, callNode,
        resultName, needsDefaultResult, isCallInLoop);

    assertNotNull(mutatedFunction);
  }

  @Test
  public void testReplaceReturns() {
    String labelName = "testLabel";
    Node block = NodeUtil.newBlockNode(new Node[0]);

    String resultName = "result";
    boolean resultMustBeSet = true;

    Node result = replaceReturns(block, resultName, labelName, resultMustBeSet);

    assertNull(result);
  }

  @Test
  public void testReplaceReturnsWithResult() {
    String labelName = "testLabel";
    Node block = NodeUtil.newBlockNode(new Node[0]);

    String resultName = "result";
    boolean resultMustBeSet = true;

    Node result = replaceReturns(block, resultName, labelName, resultMustBeSet);

    assertNotNull(result);
  }

  @Test
  public void testReplaceReturnsWithBreak() {
    String labelName = "testLabel";
    Node block = NodeUtil.newBlockNode(new Node[0]);

    String resultName = "result";
    boolean resultMustBeSet = false;

    Node result = replaceReturns(block, resultName, labelName, resultMustBeSet);

    assertNotNull(result);
  }

  private Node replaceReturns(Node block, String resultName, String labelName,
      boolean resultMustBeSet) {
    FunctionToBlockMutator functionToBlockMutator = new FunctionToBlockMutator(null, null);
    return functionToBlockMutator.replaceReturns(block, resultName, labelName, resultMustBeSet);
  }
}