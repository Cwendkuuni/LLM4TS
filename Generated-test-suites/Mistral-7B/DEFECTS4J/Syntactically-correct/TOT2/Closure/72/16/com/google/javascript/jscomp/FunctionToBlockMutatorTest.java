import static org.junit.Assert.*;
import static com.google.javascript.jscomp.FunctionToBlockMutator.*;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Supplier;
import com.google.common.base.Preconditions;
import com.google.common.collect.LinkedHashMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.javascript.rhino.Node;

import org.junit.Before;
import org.junit.Test;

public class FunctionToBlockMutatorTest {
  private AbstractCompiler compiler;
  private Supplier<String> safeNameIdSupplier;

  @Before
  public void setUp() {
    compiler = new NoOpCompiler();
    safeNameIdSupplier = () -> "test_id";
  }

  @Test
  public void testMutateFunctionWithNoArguments() {
    String fnName = "testFunction";
    Node fnNode = NodeUtil.newFunctionNode(fnName, null, null, null);
    Node callNode = NodeUtil.newCallNode(fnName, null);
    String resultName = "result";
    boolean needsDefaultResult = false;
    boolean isCallInLoop = false;
    Node mutatedFunction = mutate(fnName, fnNode, callNode, resultName,
        needsDefaultResult, isCallInLoop);

    assertEquals(NodeUtil.getType(mutatedFunction), Token.BLOCK);
    // TODO: Add assertions for the transformed function.
  }

  @Test
  public void testMutateFunctionWithOneArgument() {
    String fnName = "testFunction";
    Node fnNode = NodeUtil.newFunctionNode(fnName, null, null, null);
    Node callNode = NodeUtil.newCallNode(fnName, NodeUtil.newNumberNode(3));
    String resultName = "result";
    boolean needsDefaultResult = false;
    boolean isCallInLoop = false;
    Node mutatedFunction = mutate(fnName, fnNode, callNode, resultName,
        needsDefaultResult, isCallInLoop);

    assertEquals(NodeUtil.getType(mutatedFunction), Token.BLOCK);
    // TODO: Add assertions for the transformed function.
  }

  @Test
  public void testMutateFunctionWithMultipleArguments() {
    String fnName = "testFunction";
    Node fnNode = NodeUtil.newFunctionNode(fnName, null, null, null);
    Node callNode = NodeUtil.newCallNode(fnName,
        NodeUtil.newNumberNode(3), NodeUtil.newNumberNode(5));
    String resultName = "result";
    boolean needsDefaultResult = false;
    boolean isCallInLoop = false;
    Node mutatedFunction = mutate(fnName, fnNode, callNode, resultName,
        needsDefaultResult, isCallInLoop);

    assertEquals(NodeUtil.getType(mutatedFunction), Token.BLOCK);
    // TODO: Add assertions for the transformed function.
  }

  @Test
  public void testMutateFunctionWithThrowingException() {
    String fnName = "testFunction";
    Node fnNode = NodeUtil.newThrowingFunctionNode(fnName, null, null);
    Node callNode = NodeUtil.newCallNode(fnName, null);
    String resultName = "result";
    boolean needsDefaultResult = false;
    boolean isCallInLoop = false;
    try {
      mutate(fnName, fnNode, callNode, resultName, needsDefaultResult,
          isCallInLoop);
      fail("Expected an exception.");
    } catch (RuntimeException e) {
      // Expected.
    }
  }

  @Test
  public void testMutateFunctionWithLoop() {
    String fnName = "testFunction";
    Node fnNode = NodeUtil.newFunctionNode(fnName, null, null, null);
    Node callNode = NodeUtil.newCallNode(fnName, null);
    String resultName = "result";
    boolean needsDefaultResult = false;
    boolean isCallInLoop = true;
    Node mutatedFunction = mutate(fnName, fnNode, callNode, resultName,
        needsDefaultResult, isCallInLoop);

    assertEquals(NodeUtil.getType(mutatedFunction), Token.BLOCK);
    // TODO: Add assertions for the transformed function.
  }

  @Test
  public void testAliasAndInlineArguments() {
    String fnName = "testFunction";
    Node fnTemplateRoot = NodeUtil.newFunctionNode(fnName, null, null, null);
    LinkedHashMap<String, Node> argMap =
        Maps.newLinkedHashMap(Maps.of("arg1", NodeUtil.newNumberNode(3)));
    Set<String> namesToAlias = Sets.of("arg1");
    Node result = aliasAndInlineArguments(fnTemplateRoot, argMap, namesToAlias);

    assertEquals(NodeUtil.getType(result), Token.BLOCK);
    // TODO: Add assertions for the transformed function.
  }
}

class NoOpCompiler implements AbstractCompiler {
  @Override
  public Node compile(Node n, Node parent) {
    return n;
  }

  @Override
  public void setCodingConvention(CodingConvention convention) {
    // No-op.
  }
}