import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.common.collect.ImmutableList;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal.Callback;
import com.google.javascript.rhino.Token;

@RunWith(JUnit4.class)
public class FunctionRewriterTest {

  private static class TestFunctionRewriter implements CompilerPass {
    @Override
    public void process(Node externs, Node root) {}
  }

  @Test
  public void testEmptyFunction() {
    AbstractCompiler compiler = new Compiler();
    FunctionRewriter rewriter = new FunctionRewriter(compiler);

    Node emptyFunction = Node.newFunction(
        Node.newName("EmptyFunction"),
        Node.newList(Node.newList(), Node.newList()),
        Node.newList(Node.newList()),
        Node.newList(Node.newList()),
        Node.newList(Token.EMPTY, Token.EOF),
        Token.EOF,
        1,
        1,
        null);

    Node root = Node.newBlock(Token.EOF, Token.EOF);
    root.addChildToBack(emptyFunction);

    rewriter.process(null, root);

    Node result = compiler.getRootNode();
    Node emptyFnHelper = Node.newName("JSCompiler_emptyFn");
    assertEquals(emptyFunction, result.getChild(0));
    assertEquals(emptyFnHelper, result.getChild(1));
    assertEquals(Node.NEW_EXPR, result.getChild(1).getType());
    Node emptyFnConstructor = result.getChild(1).getFirstChild();
    assertEquals(Token.CALL, emptyFnConstructor.getType());
    assertEquals(emptyFnHelper, emptyFnConstructor.getFirstChild());
    assertNull(emptyFnConstructor.getLastChild());
  }

  @Test
  public void testIdentityFunction() {
    AbstractCompiler compiler = new Compiler();
    FunctionRewriter rewriter = new FunctionRewriter(compiler);

    Node identityFunction = Node.newFunction(
        Node.newName("IdentityFunction"),
        Node.newList(Node.newList(), Node.newList()),
        Node.newList(Node.newList()),
        Node.newList(Node.newList()),
        Node.newList(Token.RETURN, Node.newName("arg")),
        Token.EOF,
        1,
        1,
        null);

    Node root = Node.newBlock(Token.EOF, Token.EOF);
    root.addChildToBack(identityFunction);

    rewriter.process(null, root);

    Node result = compiler.getRootNode();
    Node identityFnHelper = Node.newName("JSCompiler_identityFn");
    assertEquals(identityFunction, result.getChild(0));
    assertEquals(identityFnHelper, result.getChild(1));
    assertEquals(Node.NEW_EXPR, result.getChild(1).getType());
    Node identityFnConstructor = result.getChild(1).getFirstChild();
    assertEquals(Token.CALL, identityFnConstructor.getType());
    assertEquals(identityFnHelper, identityFnConstructor.getFirstChild());
    assertEquals(Node.newName("arg"), identityFnConstructor.getLastChild());
  }

  @Test
  public void testSetterFunction() {
    AbstractCompiler compiler = new Compiler();
    FunctionRewriter rewriter = new FunctionRewriter(compiler);

    Node setterFunction = Node.newFunction(
        Node.newName("SetterFunction"),
        Node.newList(Node.newList(), Node.newList()),
        Node.newList(Node.newList(), Node.newList()),
        Node.newList(Node.newList(), Node.newList()),
        Node.newList(
            Token.VAR,
            Node.newName("value"),
            Token.ASSIGN,
            Node.newGetProp(
                Node.newThis(),
                Node.newName("property"))),
        Token.EOF,
        1,
        1,
        null);

    Node root = Node.newBlock(Token.EOF, Token.EOF);
    root.addChildToBack(setterFunction);

    rewriter.process(null, root);

    Node result = compiler.getRootNode();
    Node setterFnHelper = Node.newName("JSCompiler_set");
    assertEquals(setterFunction, result.getChild(0));
    assertEquals(setterFnHelper, result.getChild(1));
    assertEquals(Node.NEW_EXPR, result.getChild(1).getType());
    Node setterFnConstructor = result.getChild(1).getFirstChild();
    assertEquals(Token.CALL, setterFnConstructor.getType());
    assertEquals(setterFnHelper, setterFnConstructor.getFirstChild());
    assertEquals(Node.newName("property"), setterFnConstructor.getLastChild(0));
    assertEquals(Node.NEW_EXPR, setterFnConstructor.getLastChild(1).getType());
    Node setterFnBody = setterFnConstructor.getLastChild(1);
    assertEquals(Node.VAR, setterFnBody.getType());
    assertEquals(Node.NEW_EXPR, setterFnBody.getFirstChild().getType());
    Node setterFnHelper2 = setterFnBody.getFirstChild().getFirstChild();
    assertEquals(Token.CALL, setterFnHelper2.getType());
    assertEquals(setterFnHelper, setterFnHelper2.getFirstChild());
    assertEquals(Node.newName("value"), setterFnHelper2.getLastChild());
  }

  @Test
  public void testGetterFunction() {
    AbstractCompiler compiler = new Compiler();
    FunctionRewriter rewriter = new FunctionRewriter(compiler);

    Node getterFunction = Node.newFunction(
        Node.newName("GetterFunction"),
        Node.newList(Node.newList(), Node.newList()),
        Node.newList(Node.newList(), Node.newList()),
        Node.newList(Node.newList(), Node.newList()),
        Node.newList(
            Node.newGetProp(
                Node.newThis(),
                Node.newName("property"))),
        Token.EOF,
        1,
        1,
        null);

    Node root = Node.newBlock(Token.EOF, Token.EOF);
    root.addChildToBack(getterFunction);

    rewriter.process(null, root);

    Node result = compiler.getRootNode();
    Node getterFnHelper = Node.newName("JSCompiler_get");
    assertEquals(getterFunction, result.getChild(0));
    assertEquals(getterFnHelper, result.getChild(1));
    assertEquals(Node.NEW_EXPR, result.getChild(1).getType());
    Node getterFnConstructor = result.getChild(1).getFirstChild();
    assertEquals(Token.CALL, getterFnConstructor.getType());
    assertEquals(getterFnHelper, getterFnConstructor.getFirstChild());
    assertEquals(Node.newName("property"), getterFnConstructor.getLastChild());
  }

  @Test
  public void testNoReduceFunction() {
    AbstractCompiler compiler = new Compiler();
    FunctionRewriter rewriter = new FunctionRewriter(compiler);

    Node noReduceFunction = Node.newFunction(
        Node.newName("NoReduceFunction"),
        Node.newList(Node.newList(), Node.newList()),
        Node.newList(Node.newList(), Node.newList()),
        Node.newList(Node.newList(), Node.newList()),
        Node.newList(Token.RETURN, Node.newName("a")),
        Token.EOF,
        1,
        1,
        null);

    Node root = Node.newBlock(Token.EOF, Token.EOF);
    root.addChildToBack(noReduceFunction);

    rewriter.process(null, root);

    Node result = compiler.getRootNode();
    assertEquals(noReduceFunction, result.getChild(0));
  }

  @Test
  public void testReduceFunctionWithConstantReturn() {
    AbstractCompiler compiler = new Compiler();
    FunctionRewriter rewriter = new FunctionRewriter(compiler);

    Node reduceFunctionWithConstantReturn = Node.newFunction(
        Node.newName("ReduceFunctionWithConstantReturn"),
        Node.newList(Node.newList(), Node.newList()),
        Node.newList(Node.newList(), Node.newList()),
        Node.newList(Node.newList(), Node.newList()),
        Node.newList(Token.RETURN, Node.newNumber(42)),
        Token.EOF,
        1,
        1,
        null);

    Node root = Node.newBlock(Token.EOF, Token.EOF);
    root.addChildToBack(reduceFunctionWithConstantReturn);

    rewriter.process(null, root);

    Node result = compiler.getRootNode();
    Node reductionHelper = Node.newName("JSCompiler_returnArg");
    assertEquals(reduceFunctionWithConstantReturn, result.getChild(0));
    assertEquals(reductionHelper, result.getChild(1));
    assertEquals(Node.NEW_EXPR, result.getChild(1).getType());
    Node reductionHelperConstructor = result.getChild(1).getFirstChild();
    assertEquals(Token.CALL, reductionHelperConstructor.getType());
    assertEquals(reductionHelper, reductionHelperConstructor.getFirstChild());
    assertEquals(Node.newNumber(42), reductionHelperConstructor.getLastChild());
  }
}