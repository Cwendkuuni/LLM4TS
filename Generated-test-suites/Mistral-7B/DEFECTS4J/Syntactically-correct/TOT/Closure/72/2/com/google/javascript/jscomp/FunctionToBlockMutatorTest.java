import com.google.javascript.jscomp.FunctionToBlockMutator;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.Supplier;

import org.junit.Test;
import static org.junit.Assert.*;

public class FunctionToBlockMutatorTest {

  @Test
  public void testMutateFunctionWithNoArguments() {
    AbstractCompiler compiler = new AbstractCompiler();
    Supplier<String> safeNameIdSupplier = () -> "test";
    FunctionToBlockMutator mutator = new FunctionToBlockMutator(compiler, safeNameIdSupplier);

    String fnName = "testFunction";
    Node fnNode = NodeUtil.newFunctionNode(Token.FUNCTION, Token.VOID,
        NodeUtil.newList(Token.VAR, Token.IDENT, "a"),
        NodeUtil.newFunctionBody(Token.BLOCK,
            NodeUtil.newStatement(Token.ASSIGN, Token.IDENT, "a",
                NodeUtil.newNumberLiteral(1))));
    Node callNode = NodeUtil.newCallNode(Token.CALL, fnNode, Lists.newArrayList());

    Node result = mutator.mutate(fnName, fnNode, callNode, null, false, false);

    // The mutated block should have no children.
    assertNull(result.getFirstChild());
    // Check that local variable 'a' was renamed.
    assertTrue(NodeUtil.getLocalVariableName(result, 0).startsWith("test_"));
  }

  @Test
  public void testMutateFunctionWithArguments() {
    AbstractCompiler compiler = new AbstractCompiler();
    Supplier<String> safeNameIdSupplier = () -> "test";
    FunctionToBlockMutator mutator = new FunctionToBlockMutator(compiler, safeNameIdSupplier);

    String fnName = "testFunction";
    Node fnNode = NodeUtil.newFunctionNode(Token.FUNCTION, Token.VOID,
        NodeUtil.newList(Token.VAR, Token.IDENT, "a"),
        NodeUtil.newFunctionBody(Token.BLOCK,
            NodeUtil.newStatement(Token.ASSIGN, Token.IDENT, "a",
                NodeUtil.newNumberLiteral(1)),
            NodeUtil.newStatement(Token.ASSIGN, Token.IDENT, "b",
                NodeUtil.newNumberLiteral(2))));
    Node callNode = NodeUtil.newCallNode(Token.CALL, fnNode,
        Lists.newArrayList(NodeUtil.newNumberLiteral(3), NodeUtil.newNumberLiteral(4)));

    Node result = mutator.mutate(fnName, fnNode, callNode, null, false, false);

    Node argMap = NodeUtil.getFunctionCallParameterMap(fnNode, callNode, safeNameIdSupplier);
    Map<String, Node> newArgMap = Maps.newLinkedHashMap(argMap);
    newArgMap.put("a", NodeUtil.newNumberLiteral(3));
    newArgMap.put("b", NodeUtil.newNumberLiteral(4));

    Node expectedResult = FunctionArgumentInjector.inject(
        NodeUtil.newFunctionBody(Token.BLOCK,
            NodeUtil.newStatement(Token.ASSIGN, Token.IDENT, "a", NodeUtil.newNumberLiteral(3)),
            NodeUtil.newStatement(Token.ASSIGN, Token.IDENT, "b", NodeUtil.newNumberLiteral(4))),
        null, newArgMap);

    Node expectedLabel = new Node(Token.LABEL);
    expectedLabel.addChildToFront(
        new Node(Token.LABEL_NAME, "test_label_testFunction_test_"));
    expectedLabel.addChildToBack(expectedResult);

    Node mutatedBlock = NodeUtil.getFunctionBody(result);

    Node mutatedLabel = mutatedBlock.getFirstChild();
    assertEquals(expectedLabel.toString(), mutatedLabel.toString());
    Node mutatedBody = mutatedBlock.getLastChild();
    assertEquals(expectedResult.toString(), mutatedBody.toString());
  }

  @Test
  public void testMutateFunctionWithLabel() {
    AbstractCompiler compiler = new AbstractCompiler();
    Supplier<String> safeNameIdSupplier = () -> "test";
    FunctionToBlockMutator mutator = new FunctionToBlockMutator(compiler, safeNameIdSupplier);

    String fnName = "testFunction";
    Node fnNode = NodeUtil.newFunctionNode(Token.FUNCTION, Token.VOID,
        NodeUtil.newList(Token.VAR, Token.IDENT, "a"),
        NodeUtil.newFunctionBody(Token.BLOCK,
            new Node(Token.LABEL, "test_label"),
            NodeUtil.newStatement(Token.ASSIGN, Token.IDENT, "a",
                NodeUtil.newNumberLiteral(1))));
    Node callNode = NodeUtil.newCallNode(Token.CALL, fnNode, Lists.newArrayList());

    Node result = mutator.mutate(fnName, fnNode, callNode, null, false, false);

    Node expectedResult = NodeUtil.newLabel(
        "test_label_testFunction_test_"+safeNameIdSupplier.get());
    Node mutatedLabel = result.getFirstChild();
    assertEquals(expectedResult.toString(), mutatedLabel.toString());
    Node mutatedBody = result.getLastChild();
    // The mutated body should have no children.
    assertNull(mutatedBody.getFirstChild());
  }
}