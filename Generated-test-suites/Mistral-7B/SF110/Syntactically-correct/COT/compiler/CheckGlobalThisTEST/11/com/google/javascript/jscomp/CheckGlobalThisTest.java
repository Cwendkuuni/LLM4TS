import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Callback;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.JSDocInfo;

public class CheckGlobalThisTest {

  @org.junit.Test
  public void testShouldTraverseFunctionAnnotatedWithConstructor() {
    NodeTraversal traversal = createTestTraversal();
    CheckGlobalThis checker = new CheckGlobalThis(createTestCompiler());
    checker.visit(traversal, createTestFunctionNodeAnnotatedWithConstructor(), null);
    assertFalse(checker.shouldTraverse(traversal, null, null));
  }

  @org.junit.Test
  public void testShouldTraverseFunctionAnnotatedWithThis() {
    NodeTraversal traversal = createTestTraversal();
    CheckGlobalThis checker = new CheckGlobalThis(createTestCompiler());
    checker.visit(traversal, createTestFunctionNodeAnnotatedWithThis(), null);
    assertFalse(checker.shouldTraverse(traversal, null, null));
  }

  @org.junit.Test
  public void testShouldTraverseFunctionWithNoLogicalPlaceToPutThisAnnotation() {
    NodeTraversal traversal = createTestTraversal();
    CheckGlobalThis checker = new CheckGlobalThis(createTestCompiler());
    checker.visit(traversal, createTestFunctionWithNoLogicalPlaceToPutThisAnnotation(), null);
    assertTrue(checker.shouldTraverse(traversal, null, null));
  }

  @org.junit.Test
  public void testShouldReportGlobalThis() {
    NodeTraversal traversal = createTestTraversal();
    CheckGlobalThis checker = new CheckGlobalThis(createTestCompiler());
    Node node = createTestNodeWithGlobalThis();
    checker.visit(traversal, node, null);
    assertTrue(checker.assignLhsChild == null);
    checker.visit(traversal, node, node.getParent());
    assertNotNull(checker.compiler.getLatestError());
  }

  @org.junit.Test
  public void testShouldNotReportGlobalThisInsideAssignLhs() {
    NodeTraversal traversal = createTestTraversal();
    CheckGlobalThis checker = new CheckGlobalThis(createTestCompiler());
    Node node = createTestNodeWithGlobalThisInsideAssignLhs();
    checker.visit(traversal, node, null);
    checker.visit(traversal, node, node.getParent());
    assertNotNull(checker.compiler.getLatestError());
    assertTrue(checker.assignLhsChild != null);
    assertFalse(checker.shouldReportThis(node));
  }

  @org.junit.Test
  public void testShouldNotReportGlobalThisInsideGetProperty() {
    NodeTraversal traversal = createTestTraversal();
    CheckGlobalThis checker = new CheckGlobalThis(createTestCompiler());
    Node node = createTestNodeWithGlobalThisInsideGetProperty();
    checker.visit(traversal, node, null);
    checker.visit(traversal, node, node.getParent());
    assertNotNull(checker.compiler.getLatestError());
    assertFalse(checker.shouldReportThis(node));
  }

  @org.junit.Test
  public void testShouldNotReportGlobalThisInsideGetPropertyWithPrototype() {
    NodeTraversal traversal = createTestTraversal();
    CheckGlobalThis checker = new CheckGlobalThis(createTestCompiler());
    Node node = createTestNodeWithGlobalThisInsideGetPropertyWithPrototype();
    checker.visit(traversal, node, null);
    checker.visit(traversal, node, node.getParent());
    assertNotNull(checker.compiler.getLatestError());
    assertFalse(checker.shouldReportThis(node));
  }

  private Node createTestFunctionNodeAnnotatedWithConstructor() {
    return NodeFactory.createFunctionDeclaration("constructorFunction", new Node[0], null, null, null);
  }

  private Node createTestFunctionNodeAnnotatedWithThis() {
    return NodeFactory.createFunctionDeclaration("thisFunction", new Node[0], null, null, "@this");
  }

  private Node createTestFunctionWithNoLogicalPlaceToPutThisAnnotation() {
    Node body = NodeFactory.createExpressionStatement(NodeFactory.createAssign(NodeFactory.createIdentifier("x"), NodeFactory.createNumberLiteral(42)));
    return NodeFactory.createFunctionDeclaration("noThisFunction", new Node[0], null, body, null);
  }

  private Node createTestNodeWithGlobalThis() {
    return NodeFactory.createThis();
  }

  private Node createTestNodeWithGlobalThisInsideAssignLhs() {
    Node leftSide = NodeFactory.createIdentifier("a");
    Node rightSide = NodeFactory.createThis();
    return NodeFactory.createAssign(leftSide, rightSide);
  }

  private Node createTestNodeWithGlobalThisInsideGetProperty() {
    Node object = NodeFactory.createThis();
    Node property = NodeFactory.createIdentifier("prototype");
    return NodeFactory.createGetProp(object, property);
  }

  private Node createTestNodeWithGlobalThisInsideGetPropertyWithPrototype() {
    Node object = NodeFactory.createThis();
    Node prototype = NodeFactory.createGetProp(NodeFactory.createIdentifier("prototype"), NodeFactory.createIdentifier("prototype"));
    Node property = NodeFactory.createIdentifier("prototype");
    return NodeFactory.createGetProp(object, prototype, property);
  }

  private AbstractCompiler createTestCompiler() {
    return new CompilerOptions();
  }

  private NodeTraversal createTestTraversal() {
    Node root = NodeFactory.createScript("", null, null, null);
    return new NodeTraversal(root, new CheckGlobalThis(createTestCompiler()));
  }

  private static class CompilerOptions extends CompilerOptions.AbstractCompilerOptions {

    @Override
    public boolean shouldCompile(Node node, Node parent) {
      return true;
    }

    @Override
    public DiagnosticType getCompilationErrorType(int errorType) {
      return DiagnosticType.error();
    }
  }

  private static NodeFactory nodeFactory = new NodeFactory();

  private static class NodeFactory {

    public static Node createAssign(Node left, Node right) {
      return NodeFactory.createExpressionStatement(NodeFactory.createAssignop(Token.ASSIGN, left, right));
    }

    public static Node createBlock(Node[] statements) {
      return NodeFactory.createStatementList(statements);
    }

    public static Node createExpressionStatement(Node expression) {
      return NodeFactory.createStatement(NodeFactory.createExpressionStatement(expression));
    }

    public static Node createFunctionDeclaration(String name, Node[] parameters, Node body, Node[] docComments, Node[] jsDocs) {
      return NodeFactory.createFunction(
          Token.FUNCTION,
          NodeFactory.createIdentifier(name),
          parameters,
          body,
          docComments,
          jsDocs);
    }

    public static Node createGetProp(Node object, Node property) {
      return NodeFactory.createGetProp(object, property.getString());
    }

    public static Node createGetProp(Node object, String propertyName) {
      return NodeFactory.createGetProp(object, NodeFactory.createIdentifier(propertyName));
    }

    public static Node createIdentifier(String name) {
      return NodeFactory.createName(name, null);
    }

    public static Node createIfStatement(Node condition, Node thenPart, Node elsePart) {
      Node ifNode = NodeFactory.createIf(condition, thenPart, elsePart);
      return NodeFactory.createStatementList(new Node[] {ifNode});
    }

    public static Node createIsAssignop(int opcode) {
      return NodeFactory.createAssignop(opcode, NodeFactory.createIdentifier("x"), NodeFactory.createIdentifier("y"));
    }

    public static Node createIsGetProp(int opcode, Node object, String propertyName) {
      return NodeFactory.createGetProp(object, NodeFactory.createIdentifier(propertyName));
    }

    public static Node createIsGetProp(int opcode, Node object, Node property) {
      return NodeFactory.createGetProp(object, property);
    }

    public static Node createIsNamedCall(Node callee, Node[] arguments) {
      return NodeFactory.createExpressionStatement(NodeFactory.createNamedCall(callee, arguments));
    }

    public static Node createIsStatement(Node statement) {
      return NodeFactory.createStatement(statement);
    }

    public static Node createIsStatementList(Node[] statements) {
      return NodeFactory.createStatementList(statements);
    }

    public static Node createIsThis() {
      return NodeFactory.createThis();
    }

    public static Node createIsVariableDeclaration(String variable, Node initialValue, Node[] jsDocs) {
      return NodeFactory.createVariableDeclarationList(
          new Node[] {NodeFactory.createVariableDeclaration(variable, initialValue)},
          null,
          null,
          jsDocs);
    }

    public static Node createIsVariableDeclarationList(Node[] declarations, Node[] jsDocs) {
      return NodeFactory.createStatementList(declarations);
    }

    public static Node createIsWhileStatement(Node condition, Node body) {
      Node whileNode = NodeFactory.createWhile(condition, body);
      return NodeFactory.createStatementList(new Node[] {whileNode});
    }

    public static Node createScript(String code, Node[] docComments, Node[] jsDocs) {
      return NodeFactory.createScript(code, docComments, jsDocs);
    }

    public static Node createScriptList(Node[] scripts) {
      return NodeFactory.createStatementList(scripts);
    }

    public static Node createStringLiteral(String value) {
      return NodeFactory.createStringLiteral(value);
    }

    public static Node createStringLiteralFromString(String value) {
      return NodeFactory.createStringLiteral(value);
    }

    public static Node createWhile(Node condition, Node body) {
      return NodeFactory.createWhile(condition, body);
    }
  }
}