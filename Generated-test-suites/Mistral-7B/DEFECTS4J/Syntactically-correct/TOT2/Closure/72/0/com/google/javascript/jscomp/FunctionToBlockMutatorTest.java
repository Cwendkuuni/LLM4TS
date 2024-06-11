import com.google.javascript.jscomp.FunctionToBlockMutator;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Supplier;
import com.google.common.base.Preconditions;
import com.google.common.collect.LinkedHashMap;
import com.google.common.collect.Maps;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class FunctionToBlockMutatorTest {

  private FunctionToBlockMutator functionToBlockMutator;
  private AbstractCompiler compiler;
  private Supplier<String> safeNameIdSupplier;

  @Before
  public void setUp() {
    compiler = new AbstractCompiler();
    safeNameIdSupplier = () -> "test_id";
    functionToBlockMutator = new FunctionToBlockMutator(compiler, safeNameIdSupplier);
  }

  @Test
  public void testMutateFunctionWithNoArguments() {
    String fnName = "testFunction";
    Node fnNode = NodeUtil.newFunctionNode(
        Token.FUNCTION, Token.VAR, null, Token.BLOCK, null, null);
    Node callNode = NodeUtil.newCallNode(null, fnName, null, null);
    Node resultName = Node.newString(Token.NAME, "result");
    Node injectableBlock = functionToBlockMutator.mutate(
        fnName, fnNode, callNode, resultName, false, false);

    assertEquals(injectableBlock.getType(), Token.BLOCK);
    assertEquals(injectableBlock.getFirstChild().getType(), Token.VAR);
    assertEquals(injectableBlock.getLastChild().getType(), Token.RETURN);
  }

  @Test
  public void testMutateFunctionWithOneArgument() {
    String fnName = "testFunction";
    Node fnNode = NodeUtil.newFunctionNode(
        Token.FUNCTION, Token.VAR, null, Token.BLOCK, null, null);
    Node argName = Node.newString(Token.NAME, "arg");
    Node argNode = NodeUtil.newVarNode(argName, NodeUtil.newNullNode(null));
    Node callNode = NodeUtil.newCallNode(null, fnName, argNode, null);
    Node resultName = Node.newString(Token.NAME, "result");
    Node injectableBlock = functionToBlockMutator.mutate(
        fnName, fnNode, callNode, resultName, false, false);

    assertEquals(injectableBlock.getType(), Token.BLOCK);
    assertEquals(injectableBlock.getFirstChild().getType(), Token.VAR);
    assertEquals(injectableBlock.getLastChild().getType(), Token.RETURN);

    Node arg = injectableBlock.getFirstChild().getFirstChild();
    assertEquals(arg.getName(), argName.getName());
    assertEquals(arg.getType(), Token.VAR);
  }

  @Test
  public void testMutateFunctionWithMultipleArguments() {
    String fnName = "testFunction";
    Node fnNode = NodeUtil.newFunctionNode(
        Token.FUNCTION, Token.VAR, null, Token.BLOCK, null, null);
    Node arg1Name = Node.newString(Token.NAME, "arg1");
    Node arg1Node = NodeUtil.newVarNode(arg1Name, NodeUtil.newNullNode(null));
    Node arg2Name = Node.newString(Token.NAME, "arg2");
    Node arg2Node = NodeUtil.newVarNode(arg2Name, NodeUtil.newNullNode(null));
    Node callNode = NodeUtil.newCallNode(null, fnName, arg1Node, arg2Node);
    Node resultName = Node.newString(Token.NAME, "result");
    Node injectableBlock = functionToBlockMutator.mutate(
        fnName, fnNode, callNode, resultName, false, false);

    assertEquals(injectableBlock.getType(), Token.BLOCK);
    assertEquals(injectableBlock.getFirstChild().getType(), Token.VAR);
    assertEquals(injectableBlock.getFirstChild().getFirstChild().getType(), Token.VAR);
    assertEquals(injectableBlock.getLastChild().getType(), Token.RETURN);

    Node arg1 = injectableBlock.getFirstChild().getFirstChild();
    assertEquals(arg1.getName(), arg1Name.getName());
    assertEquals(arg1.getType(), Token.VAR);

    Node arg2 = injectableBlock.getFirstChild().getLastChild();
    assertEquals(arg2.getName(), arg2Name.getName());
    assertEquals(arg2.getType(), Token.VAR);
  }

  @Test
  public void testMutateFunctionWithNoArgumentsInLoop() {
    String fnName = "testFunction";
    Node fnNode = NodeUtil.newFunctionNode(
        Token.FUNCTION, Token.VAR, null, Token.BLOCK, null, null);
    Node callNode = NodeUtil.newCallNode(null, fnName, null, null);
    Node resultName = Node.newString(Token.NAME, "result");
    Node injectableBlock = functionToBlockMutator.mutate(
        fnName, fnNode, callNode, resultName, true, false);

    assertEquals(injectableBlock.getType(), Token.BLOCK);
    assertEquals(injectableBlock.getFirstChild().getType(), Token.BLOCK);
    assertEquals(injectableBlock.getLastChild().getType(), Token.RETURN);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testMutateNullFunction() {
    String fnName = "testFunction";
    Node fnNode = null;
    Node callNode = NodeUtil.newCallNode(null, fnName, null, null);
    Node resultName = Node.newString(Token.NAME, "result");
    functionToBlockMutator.mutate(fnName, fnNode, callNode, resultName, false, false);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testMutateNullCallNode() {
    String fnName = "testFunction";
    Node fnNode = NodeUtil.newFunctionNode(
        Token.FUNCTION, Token.VAR, null, Token.BLOCK, null, null);
    Node callNode = null;
    Node resultName = Node.newString(Token.NAME, "result");
    functionToBlockMutator.mutate(fnName, fnNode, callNode, resultName, false, false);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testMutateNullResultName() {
    String fnName = "testFunction";
    Node fnNode = NodeUtil.newFunctionNode(
        Token.FUNCTION, Token.VAR, null, Token.BLOCK, null, null);
    Node callNode = NodeUtil.newCallNode(null, fnName, null, null);
    Node resultName = null;
    functionToBlockMutator.mutate(fnName, fnNode, callNode, resultName, false, false);
  }

  @Test
  public void testMakeLocalNamesUnique() {
    String fnName = "testFunction";
    Node fnNode = NodeUtil.newFunctionNode(
        Token.FUNCTION, Token.VAR, null, Token.BLOCK, null, null);
    Node block = NodeUtil.getFunctionBody(fnNode);

    Node local1 = NodeUtil.newVarNode(
        Node.newString(Token.NAME, "local1"), NodeUtil.newNullNode(null));
    Node local2 = NodeUtil.newVarNode(
        Node.newString(Token.NAME, "local1"), NodeUtil.newNullNode(null));
    block.addChildrenToBack(local1, local2);

    functionToBlockMutator.makeLocalNamesUnique(block, false);

    Set<String> localNames = Sets.newHashSet(local1.getName(), local2.getName());
    int localNameCount = 0;

    NodeTraversal.traverse(compiler, block, new NodeVisitor() {
      @Override
      public void visitNode(Node node) {
        if (node.getType() == Token.VAR) {
          String name = node.getName().getName();
          localNameCount++;
          assertTrue(localNames.remove(name));
        }
        super.visitNode(node);
      }
    });

    assertEquals(localNameCount, 2);
    assertTrue(localNames.isEmpty());
  }
}