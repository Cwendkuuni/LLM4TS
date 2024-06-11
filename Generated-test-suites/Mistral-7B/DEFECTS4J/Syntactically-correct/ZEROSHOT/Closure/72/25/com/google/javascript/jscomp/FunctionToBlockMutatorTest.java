import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.FunctionToBlockMutator;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.Supplier;

import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.Test;

/**
 * Tests for {@link FunctionToBlockMutator}.
 */
public class FunctionToBlockMutatorTest {

  @Test
  public void testMutateFunction() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    Supplier<String> safeNameIdSupplier = mock(Supplier.class);
    when(safeNameIdSupplier.get()).thenReturn("test");

    FunctionToBlockMutator functionToBlockMutator =
        new FunctionToBlockMutator(compiler, safeNameIdSupplier);

    String fnName = "testFunction";
    Node fnNode = Node.newFunction(Token.FUNCTION, null, null, null);
    Node callNode = Node.newCall(Token.CALL, fnNode, null);
    String resultName = "result";
    boolean needsDefaultResult = true;
    boolean isCallInLoop = false;

    Node mutatedFunction = functionToBlockMutator.mutate(
        fnName, fnNode, callNode, resultName, needsDefaultResult, isCallInLoop);

    assertNotNull(mutatedFunction);
  }

  @Test
  public void testMutateFunctionWithArguments() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    Supplier<String> safeNameIdSupplier = mock(Supplier.class);
    when(safeNameIdSupplier.get()).thenReturn("test");

    FunctionToBlockMutator functionToBlockMutator =
        new FunctionToBlockMutator(compiler, safeNameIdSupplier);

    String fnName = "testFunction";
    Node fnNode = Node.newFunction(Token.FUNCTION, null, null, null);
    Node callNode = Node.newCall(Token.CALL, fnNode, null);
    String resultName = "result";
    boolean needsDefaultResult = true;
    boolean isCallInLoop = false;

    LinkedHashMap<String, Node> argMap = new LinkedHashMap<>();
    argMap.put("arg1", Node.newNumber(Token.NUMBER, 1));
    argMap.put("arg2", Node.newString(Token.STRING, "str"));

    Node mutatedFunction = functionToBlockMutator.mutate(
        fnName, fnNode, callNode, resultName, needsDefaultResult, isCallInLoop);

    assertNotNull(mutatedFunction);

    Node body = NodeUtil.getFunctionBody(mutatedFunction);
    assert(NodeUtil.getChildCount(body) > 0);

    for (Node child : NodeUtil.getChildren(body)) {
      if (child.getType() == Token.VAR && child.getFirstChild().getType()
          == Token.ID) {
        String name = child.getFirstChild().getString();
        Node value = child.getLastChild();

        assert(argMap.containsKey(name));
        assert(value.getType() == Token.GET_PROP);
        assert(value.getFirstChild().getString().equals("arguments"));
        assert(value.getLastChild().getType() == Token.INDEX);
        assert(value.getLastChild().getFirstChild().getIntValue()
            == argMap.get(name).getIndex());
      }
    }
  }

  @Test
  public void testMutateFunctionWithArgumentsAndAliases() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    Supplier<String> safeNameIdSupplier = mock(Supplier.class);
    when(safeNameIdSupplier.get()).thenReturn("test");

    FunctionToBlockMutator functionToBlockMutator =
        new FunctionToBlockMutator(compiler, safeNameIdSupplier);

    String fnName = "testFunction";
    Node fnNode = Node.newFunction(Token.FUNCTION, null, null, null);
    Node callNode = Node.newCall(Token.CALL, fnNode, null);
    String resultName = "result";
    boolean needsDefaultResult = true;
    boolean isCallInLoop = false;

    Set<String> namesToAlias = Set.of("arg1", "arg2");

    LinkedHashMap<String, Node> argMap = new LinkedHashMap<>();
    argMap.put("arg1", Node.newNumber(Token.NUMBER, 1));
    argMap.put("arg2", Node.newString(Token.STRING, "str"));

    Node mutatedFunction = functionToBlockMutator.mutate(
        fnName, fnNode, callNode, resultName, needsDefaultResult, isCallInLoop);

    assertNotNull(mutatedFunction);

    Node body = NodeUtil.getFunctionBody(mutatedFunction);
    assert(NodeUtil.getChildCount(body) > 0);

    for (Node child : NodeUtil.getChildren(body)) {
      if (child.getType() == Token.VAR && child.getFirstChild().getType()
          == Token.ID) {
        String name = child.getFirstChild().getString();
        if (namesToAlias.contains(name)) {
          String aliasName = "alias_" + name;
          Node aliasNode = NodeUtil.newVarNode(aliasName, Node.newUndefinedNode(null));
          Node aliasValue = NodeUtil.newAssign(aliasName, Node.newGetProp(
              Node.newThis(), "arguments"));
          aliasValue.addChildToBack(NodeUtil.newIndex(
              Node.newGetProp(Node.newThis(), "arguments"),
              Node.newInt(argMap.get(name).getIndex())));
          aliasValue.copyInformationFromForTree(child);
          child.replaceChildren(aliasValue, NodeUtil.newVarDecl(name));
        }
      }
    }
  }
}