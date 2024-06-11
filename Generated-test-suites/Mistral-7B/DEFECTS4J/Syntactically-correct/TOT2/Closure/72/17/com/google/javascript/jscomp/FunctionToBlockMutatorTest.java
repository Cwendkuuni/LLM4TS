import static org.junit.Assert.*;
import static com.google.javascript.jscomp.FunctionToBlockMutator.*;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Supplier;
import com.google.javascript.rhino.Node;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class FunctionToBlockMutatorTest {

  @Test
  public void testMutateEmptyFunction() {
    AbstractCompiler compiler = new NoOpCompiler();
    Supplier<String> safeNameIdSupplier = () -> "test";
    FunctionToBlockMutator functionToBlockMutator = new FunctionToBlockMutator(
        compiler, safeNameIdSupplier);

    Node fnNode = NodeUtil.newFunctionNode();
    Node callNode = Node.newIdentifier("testFn");
    String resultName = "result";
    boolean needsDefaultResult = false;
    boolean isCallInLoop = false;

    Node mutatedNode = functionToBlockMutator.mutate(
        null, fnNode, callNode, resultName, needsDefaultResult, isCallInLoop);

    assertEquals(fnNode.getType(), mutatedNode.getType());
    assertEquals(fnNode.getNumChildren(), mutatedNode.getNumChildren());
  }

  @Test
  public void testMutateFunctionWithReturnStatement() {
    AbstractCompiler compiler = new NoOpCompiler();
    Supplier<String> safeNameIdSupplier = () -> "test";
    FunctionToBlockMutator functionToBlockMutator = new FunctionToBlockMutator(
        compiler, safeNameIdSupplier);

    Node fnNode = NodeUtil.newFunctionNodeWithReturnStatement();
    Node callNode = Node.newIdentifier("testFn");
    String resultName = "result";
    boolean needsDefaultResult = false;
    boolean isCallInLoop = false;

    Node mutatedNode = functionToBlockMutator.mutate(
        null, fnNode, callNode, resultName, needsDefaultResult, isCallInLoop);

    // The return statement should be replaced with break statement.
    Node expectedBreak = Node.newBreak();
    assertEquals(expectedBreak.getType(), mutatedNode.getLastChild().getType());
  }

  @Test
  public void testMutateFunctionWithArguments() {
    AbstractCompiler compiler = new NoOpCompiler();
    Supplier<String> safeNameIdSupplier = () -> "test";
    FunctionToBlockMutator functionToBlockMutator = new FunctionToBlockMutator(
        compiler, safeNameIdSupplier);

    Node fnNode = NodeUtil.newFunctionNodeWithArguments();
    Node callNode = NodeUtil.newCallNode("testFn", nodeList(
        NodeUtil.newNumberLiteral(1), NodeUtil.newNumberLiteral(2)));
    String resultName = "result";
    boolean needsDefaultResult = false;
    boolean isCallInLoop = false;

    Node mutatedNode = functionToBlockMutator.mutate(
        null, fnNode, callNode, resultName, needsDefaultResult, isCallInLoop);

    // The arguments should be inlined and aliased.
    Map<String, Node> expectedArgMap = Maps.newHashMap();
    expectedArgMap.put("arg1", NodeUtil.newNumberLiteral(1));
    expectedArgMap.put("arg2", NodeUtil.newNumberLiteral(2));
    assertEquals(expectedArgMap, FunctionArgumentInjector.getFunctionCallParameterMap(
        mutatedNode, callNode, safeNameIdSupplier));
  }

  @Test
  public void testMutateFunctionWithLoop() {
    AbstractCompiler compiler = new NoOpCompiler();
    Supplier<String> safeNameIdSupplier = () -> "test";
    FunctionToBlockMutator functionToBlockMutator = new FunctionToBlockMutator(
        compiler, safeNameIdSupplier);

    Node fnNode = NodeUtil.newFunctionNodeWithArgumentsAndLoop();
    Node callNode = NodeUtil.newCallNode("testFn");
    String resultName = "result";
    boolean needsDefaultResult = false;
    boolean isCallInLoop = true;

    Node mutatedNode = functionToBlockMutator.mutate(
        null, fnNode, callNode, resultName, needsDefaultResult, isCallInLoop);

    // The loop should be initialized to undefined.
    Node expectedInit = NodeUtil.newUndefinedNode(fnNode);
    assertEquals(expectedInit, mutatedNode.getFirstChild());
  }

  @Test(expected = AssertionError.class)
  public void testMutateFunctionWithDuplicateLocalName() {
    AbstractCompiler compiler = new NoOpCompiler();
    Supplier<String> safeNameIdSupplier = () -> "test";
    FunctionToBlockMutator functionToBlockMutator = new FunctionToBlockMutator(
        compiler, safeNameIdSupplier);

    Node fnNode = NodeUtil.newFunctionNodeWithDuplicateLocalName();
    Node callNode = Node.newIdentifier("testFn");
    String resultName = "result";
    boolean needsDefaultResult = false;
    boolean isCallInLoop = false;

    Node mutatedNode = functionToBlockMutator.mutate(
        null, fnNode, callNode, resultName, needsDefaultResult, isCallInLoop);

    fail("Expected an AssertionError due to duplicate local name.");
  }

  @Test
  public void testMutateFunctionWithUnreachableCode() {
    AbstractCompiler compiler = new NoOpCompiler();
    Supplier<String> safeNameIdSupplier = () -> "test";
    FunctionToBlockMutator functionToBlockMutator = new FunctionToBlockMutator(
        compiler, safeNameIdSupplier);

    Node fnNode = NodeUtil.newFunctionNodeWithUnreachableCode();
    Node callNode = Node.newIdentifier("testFn");
    String resultName = "result";
    boolean needsDefaultResult = false;
    boolean isCallInLoop = false;

    Node mutatedNode = functionToBlockMutator.mutate(
        null, fnNode, callNode, resultName, needsDefaultResult, isCallInLoop);

    // Unreachable code should be removed.
    assertTrue(NodeUtil.isNullOrEmpty(fnNode.getFirstChildList()));
  }

  @Test
  public void testMutateFunctionWithEmptyBlock() {
    AbstractCompiler compiler = new NoOpCompiler();
    Supplier<String> safeNameIdSupplier = () -> "test";
    FunctionToBlockMutator functionToBlockMutator = new FunctionToBlockMutator(
        compiler, safeNameIdSupplier);

    Node fnNode = NodeUtil.newFunctionNodeWithEmptyBlock();
    Node callNode = Node.newIdentifier("testFn");
    String resultName = "result";
    boolean needsDefaultResult = false;
    boolean isCallInLoop = false;

    Node mutatedNode = functionToBlockMutator.mutate(
        null, fnNode, callNode, resultName, needsDefaultResult, isCallInLoop);

    // Empty block should not be modified.
    assertEquals(fnNode.getType(), mutatedNode.getType());
    assertEquals(fnNode.getNumChildren(), mutatedNode.getNumChildren());
  }

  private static NodeUtil nodeList(Node... nodes) {
    return NodeUtil.newList(Arrays.asList(nodes));
  }

  private static class NoOpCompiler implements AbstractCompiler {
    @Override
    public Node compile(Node tree, CompilerOptions options) {
      return tree;
    }

    @Override
    public Node makeUnreachable(Node tree, SourceFile sourceFile) {
      return tree;
    }

    @Override
    public Node makeConstant(Node tree, SourceFile sourceFile) {
      return tree;
    }

    @Override
    public Node makeDeclaration(Node tree, SourceFile sourceFile) {
      return tree;
    }

    @Override
    public Node makeAssignment(Node tree, Node right, SourceFile sourceFile) {
      return tree;
    }

    @Override
    public Node makeBinaryOperator(
        Node left, BinaryOperator op, Node right, SourceFile sourceFile) {
      return new Node(op, left, right);
    }

    @Override
    public Node makeCall(Node node, List<Node> arguments, SourceFile sourceFile) {
      return new Node(Token.CALL, node, arguments);
    }

    @Override
    public Node makeUnaryOperator(Node node, UnaryOperator op, SourceFile sourceFile) {
      return new Node(op, node);
    }

    @Override
    public Node makeConditional(Node condition, Node ifTrue, Node ifFalse, SourceFile sourceFile) {
      return new Node(Token.IF, condition, ifTrue, ifFalse);
    }

    @Override
    public Node makeLoop(Node loopInit, Node loopCondition, Node loopBody, SourceFile sourceFile) {
      return new Node(Token.WHILE, loopInit, loopCondition, loopBody);
    }

    @Override
    public Node makeStatementBlock(List<Node> statements, SourceFile sourceFile) {
      return new Node(Token.BLOCK, statements);
    }

    @Override
    public Node makeExpressionStatement(Node expression, SourceFile sourceFile) {
      return new Node(Token.EXPR_STATEMENT, expression);
    }

    @Override
    public Node makeDeclareVar(Node[] ids, Node initializer, SourceFile sourceFile) {
      return new Node(Token.VAR, ids, initializer);
    }

    @Override
    public Node makeIdentifier(String name, SourceFile sourceFile) {
      return new Node(Token.NAME, name);
    }

    @Override
    public Node makeStringLiteral(String text, SourceFile sourceFile) {
      return new Node(Token.STRING_LITERAL, text);
    }

    @Override
    public Node makeNumberLiteral(double number, SourceFile sourceFile) {
      return new Node(Token.NUMBER_LITERAL, number);
    }

    @Override
    public Node makeNullLiteral(SourceFile sourceFile) {
      return NodeUtil.newNullNode();
    }

    @Override
    public Node makeBooleanLiteral(boolean bool, SourceFile sourceFile) {
      return new Node(bool ? Token.TRUE : Token.FALSE);
    }

    @Override
    public Node makeThis(SourceFile sourceFile) {
      return NodeUtil.newThisNode();
    }

    @Override
    public Node makeArrayLiteral(List<Node> elements, SourceFile sourceFile) {
      return new Node(Token.ARRAY_LITERAL, elements);
    }

    @Override
    public Node makeFunction(Node body, List<Node> params, SourceFile sourceFile) {
      return new Node(Token.FUNCTION, params, body);
    }

    @Override
    public Node makeFunctionDeclaration(String name, List<Node> params, Node body, SourceFile sourceFile) {
      return new Node(Token.FUNCTION_DECLARATION, name, params, body);
    }

    @Override
    public Node makeStatement(Node statement, SourceFile sourceFile) {
      return statement;
    }

    @Override
    public Node makeType(int type, SourceFile sourceFile) {
      return new Node(type);
    }

    @Override
    public Node makeReference(Node target, SourceFile sourceFile) {
      return Node.newReference(target);
    }

    @Override
    public Node makeQualifiedName(Node name, Node qualifiedName, SourceFile sourceFile) {
      return new Node(Token.QUALIFIED_NAME, name, qualifiedName);
    }

    @Override
    public Node makeNew(Node constructor, List<Node> arguments, SourceFile sourceFile) {
      return new Node(Token.NEW, constructor, arguments);
    }

    @Override
    public Node makeMember(Node object, String name, SourceFile sourceFile) {
      return new Node(Token.MEMBER, object, Node.newString(Token.NAME, name));
    }

    @Override
    public Node makeCallMember(Node object, String name, List<Node> arguments, SourceFile sourceFile) {
      return new Node(Token.CALL_MEMBER, object, Node.newString(Token.NAME, name), arguments);
    }

    @Override
    public Node makeIndexedGet(Node index, Node target, SourceFile sourceFile) {
      return new Node(Token.INDEXED_GET, index, target);
    }

    @Override
    public Node makeIndexedSet(Node index, Node value, Node target, SourceFile sourceFile) {
      return new Node(Token.INDEXED_SET, index, value, target);
    }

    @Override
    public Node makeSuper(int kind, SourceFile sourceFile) {
      return NodeUtil.newSuperNode(kind);
    }

    @Override
    public Node makeWith(Node target, Node body, SourceFile sourceFile) {
      return new Node(Token.WITH, target, body);
    }

    @Override
    public Node makeTryCatch(Node tryBlock, List<Node> catches, Node finallyBlock, SourceFile sourceFile) {
      return new Node(Token.TRY_CATCH, tryBlock, catches, finallyBlock);
    }

    @Override
    public Node makeTryFinally(Node tryBlock, Node finallyBlock, SourceFile sourceFile) {
      return new Node(Token.TRY_FINALLY, tryBlock, finallyBlock);
    }

    @Override
    public Node makeCatchClause(Node exceptionType, Node body, SourceFile sourceFile) {
      return new Node(Token.CATCH_CLAUSE, exceptionType, body);
    }

    @Override
    public Node makeIfStatement(Node condition, Node thenPart, Node elsePart, SourceFile sourceFile) {
      return new Node(Token.IF, condition, thenPart, elsePart);
    }

    @Override
    public Node makeCase(Node value, Node statement, SourceFile sourceFile) {
      return new Node(Token.CASE, value, statement);
    }

    @Override
    public Node makeDefaultCase(Node statement, SourceFile sourceFile) {
      return new Node(Token.DEFAULT_CASE, statement);
    }

    @Override
    public Node makeSwitchStatement(Node expression, List<Node> cases, Node defaultCase, SourceFile sourceFile) {
      return new Node(Token.SWITCH_STATEMENT, expression, cases, defaultCase);
    }

    @Override
    public Node makeLabel(String labelName, SourceFile sourceFile) {
      return Node.newLabel(labelName);
    }

    @Override
    public Node makeBreakStatement(String labelName, SourceFile sourceFile) {
      return new Node(Token.BREAK, Node.newLabel(labelName));
    }

    @Override
    public Node makeContinueStatement(String labelName, SourceFile sourceFile) {
      return new Node(Token.CONTINUE, Node.newLabel(labelName));
    }

    @Override
    public Node makeReturnStatement(Node value, SourceFile sourceFile) {
      return new Node(Token.RETURN, value);
    }

    @Override
    public Node makeThrowStatement(Node value, SourceFile sourceFile) {
      return new Node(Token.THROW, value);
    }
  }
}