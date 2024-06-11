import org.junit.Test;
import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeUtil.*;
import static com.google.javascript.jscomp.FunctionToBlockMutator.*;

public class FunctionToBlockMutatorTest {

  @Test
  public void testMutateFunction() {
    AbstractCompiler compiler = new AbstractCompiler() {};
    Supplier<String> safeNameIdSupplier = () -> "test";

    FunctionToBlockMutator mutator = new FunctionToBlockMutator(compiler, safeNameIdSupplier);

    Node givenFunctionNode = createFunctionNode(
        "testFunction",
        List.of(createParameterNode("arg1", Type.INT), createParameterNode("arg2", Type.STRING)),
        createBlockNode(
            List.of(
                createAssignStatementNode("result", createAddExpr(
                    createNumberLiteralNode(1), createNumberLiteralNode(2))),
                createReturnStatement(createNumberLiteralNode(3))
            )
        )
    );

    String resultName = "result";
    Node expectedResultNode = createNumberLiteralNode(4);
    Node callNode = createCallExprNode(givenFunctionNode, List.of(
        createNumberLiteralNode(5), createStringLiteralNode("test")
    ));

    Node mutatedNode = mutator.mutate(
        "testFunction", givenFunctionNode, callNode, resultName, false, false);

    Node actualResultNode = NodeUtil.getFunctionBody(mutatedNode);

    assertEquals(actualResultNode, expectedResultResultNode);
  }

  private Node createFunctionNode(
      String name, List<Node> parameters, Node body) {
    return new Node(Token.FUNCTION,
        createIdentifierNode(name),
        createFunctionDeclarationNode(parameters, body),
        Type.VOID,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null
    );
  }

  private Node createParameterNode(String name, Type type) {
    return new Node(Token.VAR, createIdentifierNode(name), null, null, null, null, null, null, type, null, null);
  }

  private Node createBlockNode(List<Node> statements) {
    return new Node(Token.BLOCK, null, statements.toArray(new Node[0]));
  }

  private Node createAssignStatementNode(String name, Node value) {
    return createStatementNode(new Node(Token.ASSIGN, createIdentifierNode(name), value));
  }

  private Node createAddExpr(Node left, Node right) {
    return new Node(Token.ADD, left, right);
  }

  private Node createNumberLiteralNode(int value) {
    return new Node(Token.NUMBER_LITERAL, createNumber(value));
  }

  private Node createStringLiteralNode(String value) {
    return new Node(Token.STRING_LITERAL, createString(value));
  }

  private Node createCallExprNode(Node callee, List<Node> arguments) {
    return new Node(Token.CALL, callee, arguments.toArray(new Node[0]));
  }

  private Node createReturnStatement(Node value) {
    return new Node(Token.RETURN, value);
  }

  private Node createStatementNode(Node statement) {
    return new Node(Token.BLOCK, null, statement);
  }

  private Node expectedResultResultNode = createNumberLiteralNode(4);
}