import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static com.google.javascript.rhino.NodeFactory.createAssign;
import static com.google.javascript.rhino.NodeFactory.createIdentifier;
import static com.google.javascript.rhino.NodeFactory.createThis;
import static com.google.javascript.rhino.NodeFactory.createFunction;
import static com.google.javascript.rhino.NodeFactory.createPropertyAccess;
import static com.google.javascript.rhino.NodeFactory.createString;
import static com.google.javascript.jscomp.AbstractCompiler.NullCompiler;

import org.junit.Test;

import com.google.common.collect.ImmutableList;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

public class CheckGlobalThisTest {

  private CheckGlobalThis checker;

  @Test
  public void testNoGlobalThisWarning() throws Exception {
    Compiler compiler = new Compiler();
    checker = new CheckGlobalThis(compiler);
    NodeTraversal traversal = new NodeTraversal(compiler.getFactory(), checker);

    Node node = createFunction(Token.FUNCTION, null, ImmutableList.of(), null, createBlock(null, null));
    traversal.traverse(NodeUtil.rootNode(node));

    assertTrue(checker.compiler.getDiagnostics().isEmpty());
  }

  @Test
  public void testGlobalThisWarning() throws Exception {
    Compiler compiler = new Compiler();
    checker = new CheckGlobalThis(compiler);
    NodeTraversal traversal = new NodeTraversal(compiler.getFactory(), checker);

    Node node = createAssign(Token.ASSIGN, null, createIdentifier("x"), createThis());
    traversal.traverse(NodeUtil.rootNode(node));

    ImmutableList<DiagnosticPosition> diagnostics = ImmutableList.of(new DiagnosticPosition(node.getLineNumber(), node.getLineOffset(), GLOBAL_THIS));
    assertTrue(checker.compiler.getDiagnostics().equals(compiler.getDiagnostics()));
    assertTrue(checker.compiler.getDiagnostics().size() == 1);
    assertTrue(checker.compiler.getDiagnostics().get(0).getStart().getParent().equals(node));
  }

  @Test
  public void testGlobalThisInFunction() throws Exception {
    Compiler compiler = new Compiler();
    checker = new CheckGlobalThis(compiler);
    NodeTraversal traversal = new NodeTraversal(compiler.getFactory(), checker);

    Node functionNode = createFunction(Token.FUNCTION, null, ImmutableList.of(), null, createBlock(null, createAssign(Token.ASSIGN, null, createIdentifier("x"), createThis())));
    traversal.traverse(NodeUtil.rootNode(functionNode));

    ImmutableList<DiagnosticPosition> diagnostics = ImmutableList.of(new DiagnosticPosition(functionNode.getLineNumber(), functionNode.getLineOffset(), GLOBAL_THIS));
    assertTrue(checker.compiler.getDiagnostics().equals(compiler.getDiagnostics()));
    assertTrue(checker.compiler.getDiagnostics().size() == 1);
    assertTrue(checker.compiler.getDiagnostics().get(0).getStart().getParent().equals(functionNode));
  }

  @Test
  public void testThisInConstructor() throws Exception {
    Compiler compiler = new Compiler();
    checker = new CheckGlobalThis(compiler);
    NodeTraversal traversal = new NodeTraversal(compiler.getFactory(), checker);

    Node constructorNode = createFunction(Token.FUNCTION, Token.FUNCTION_DECL, ImmutableList.of("x"), null, createBlock(null, createAssign(Token.ASSIGN, null, createIdentifier("this"), createThis())), null);
    Node prototypeNode = createObjectLiteral(createProperty("prototype", createFunction(Token.FUNCTION, null, ImmutableList.of(), null, createBlock(null, null))));
    Node rootNode = createFunction(Token.FUNCTION, Token.SCRIPT, null, null,
        createBlock(null, createAssign(Token.ASSIGN, null, createIdentifier("a"), constructorNode), createAssign(Token.ASSIGN, null, createIdentifier("b"), prototypeNode)));

    traversal.traverse(NodeUtil.rootNode(rootNode));

    assertTrue(checker.compiler.getDiagnostics().isEmpty());
  }

  @Test
  public void testThisInPrototypeMethod() throws Exception {
    Compiler compiler = new Compiler();
    checker = new CheckGlobalThis(compiler);
    NodeTraversal traversal = new NodeTraversal(compiler.getFactory(), checker);

    Node constructorNode = createFunction(Token.FUNCTION, Token.FUNCTION_DECL, ImmutableList.of("x"), null, createBlock(null, null), null);
    Node prototypeNode = createObjectLiteral(createProperty("prototype", createProperty("method", createFunction(Token.FUNCTION, null, ImmutableList.of(), null, createBlock(null, createAssign(Token.ASSIGN, null, createIdentifier("this"), createThis()))))));
    Node rootNode = createFunction(Token.FUNCTION, Token.SCRIPT, null, null,
        createBlock(null, createAssign(Token.ASSIGN, null, createIdentifier("a"), constructorNode), createAssign(Token.ASSIGN, null, createIdentifier("b"), prototypeNode)));

    traversal.traverse(NodeUtil.rootNode(rootNode));

    assertTrue(checker.compiler.getDiagnostics().isEmpty());
  }

  @Test
  public void testThisInFunctionWithNoParent() throws Exception {
    Compiler compiler = new Compiler();
    checker = new CheckGlobalThis(compiler);
    NodeTraversal traversal = new NodeTraversal(compiler.getFactory(), checker);

    Node node = createThis();
    traversal.traverse(NodeUtil.rootNode(node));

    assertTrue(checker.compiler.getDiagnostics().isEmpty());
  }

  @Test
  public void testThisInFunctionWithParent() throws Exception {
    Compiler compiler = new Compiler();
    checker = new CheckGlobalThis(compiler);
    NodeTraversal traversal = new NodeTraversal(compiler.getFactory(), checker);

    Node parentNode = createIdentifier("parent");
    Node node = createThis();
    Node parent = createAssign(Token.ASSIGN, null, parentNode, node);

    traversal.traverse(NodeUtil.rootNode(parent));

    assertTrue(checker.compiler.getDiagnostics().isEmpty());
  }

  @Test
  public void testThisInFunctionWithPrototypeProperty() throws Exception {
    Compiler compiler = new Compiler();
    checker = new CheckGlobalThis(compiler);
    NodeTraversal traversal = new NodeTraversal(compiler.getFactory(), checker);

    Node parentNode = createIdentifier("parent");
    Node prototypeNode = createObjectLiteral(createProperty("prototype", createFunction(Token.FUNCTION, null, ImmutableList.of(), null, createBlock(null, null))));
    Node functionNode = createFunction(Token.FUNCTION, null, ImmutableList.of(), null, createBlock(null, createAssign(Token.ASSIGN, null, createIdentifier("this"), createPropertyAccess(parentNode, "prototype"))));
    Node parent = createAssign(Token.ASSIGN, null, parentNode, prototypeNode);
    Node node = createAssign(Token.ASSIGN, null, createIdentifier("x"), functionNode);

    traversal.traverse(NodeUtil.rootNode(node));

    assertTrue(checker.compiler.getDiagnostics().isEmpty());
  }

  @Test
  public void testThisInFunctionWithPrototypeSubProperty() throws Exception {
    Compiler compiler = new Compiler();
    checker = new CheckGlobalThis(compiler);
    NodeTraversal traversal = new NodeTraversal(compiler.getFactory(), checker);

    Node parentNode = createIdentifier("parent");
    Node prototypeNode = createObjectLiteral(createProperty("prototype", createObjectLiteral(createProperty("subPrototype", null))));
    Node functionNode = createFunction(Token.FUNCTION, null, ImmutableList.of(), null, createBlock(null, createAssign(Token.ASSIGN, null, createIdentifier("this"), createPropertyAccess(parentNode, "prototype", "subPrototype"))));
    Node parent = createAssign(Token.ASSIGN, null, parentNode, prototypeNode);
    Node node = createAssign(Token.ASSIGN, null, createIdentifier("x"), functionNode);

    traversal.traverse(NodeUtil.rootNode(node));

    assertTrue(checker.compiler.getDiagnostics().isEmpty());
  }

  @Test
  public void testThisInFunctionWithLentPrototype() throws Exception {
    Compiler compiler = new Compiler();
    checker = new CheckGlobalThis(compiler);
    NodeTraversal traversal = new NodeTraversal(compiler.getFactory(), checker);

    Node lendsNode = createFunction(Token.FUNCTION, null, ImmutableList.of(), null, createBlock(null, null), createName("lends", "Object.prototype"));
    Node prototypeNode = createObjectLiteral(createProperty("prototype", createFunction(Token.FUNCTION, null, ImmutableList.of(), null, createBlock(null, null))));
    Node functionNode = createFunction(Token.FUNCTION, null, ImmutableList.of(), null, createBlock(null, createAssign(Token.ASSIGN, null, createIdentifier("this"), createPropertyAccess(lendsNode, "prototype"))));
    Node parent = createAssign(Token.ASSIGN, null, createIdentifier("parent"), prototypeNode);
    Node node = createAssign(Token.ASSIGN, null, createIdentifier("x"), functionNode);

    traversal.traverse(NodeUtil.rootNode(node));

    assertTrue(checker.compiler.getDiagnostics().isEmpty());
  }

  @Test
  public void testThisInFunctionWithPrototypeGet() throws Exception {
    Compiler compiler = new Compiler();
    checker = new CheckGlobalThis(compiler);
    NodeTraversal traversal = new NodeTraversal(compiler.getFactory(), checker);

    Node parentNode = createIdentifier("parent");
    Node prototypeNode = createObjectLiteral(createProperty("prototype", createFunction(Token.FUNCTION, null, ImmutableList.of(), null, createBlock(null, null))));
    Node functionNode = createFunction(Token.FUNCTION, null, ImmutableList.of(), null, createBlock(null, createGet(Token.GET, null, createPropertyAccess(parentNode, "prototype"))));
    Node parent = createAssign(Token.ASSIGN, null, parentNode, prototypeNode);
    Node node = createAssign(Token.ASSIGN, null, createIdentifier("x"), functionNode);

    traversal.traverse(NodeUtil.rootNode(node));

    assertTrue(checker.compiler.getDiagnostics().isEmpty());
  }

  @Test
  public void testThisInFunctionWithPrototypeGetSub() throws Exception {
    Compiler compiler = new Compiler();
    checker = new CheckGlobalThis(compiler);
    NodeTraversal traversal = new NodeTraversal(compiler.getFactory(), checker);

    Node parentNode = createIdentifier("parent");
    Node prototypeNode = createObjectLiteral(createProperty("prototype", createObjectLiteral(createProperty("subPrototype", null))));
    Node functionNode = createFunction(Token.FUNCTION, null, ImmutableList.of(), null, createBlock(null, createGet(Token.GET, null, createPropertyAccess(parentNode, "prototype", "subPrototype"))));
    Node parent = createAssign(Token.ASSIGN, null, parentNode, prototypeNode);
    Node node = createAssign(Token.ASSIGN, null, createIdentifier("x"), functionNode);

    traversal.traverse(NodeUtil.rootNode(node));

    assertTrue(checker.compiler.getDiagnostics().isEmpty());
  }

  private Node createBlock(Node lhs, Node rhs) {
    return createBlock(NullCompiler.getFactory(), null, null, null, lhs, rhs);
  }

  private Node createAssign(int op, Node lhs, Node rhs) {
    return createAssign(NullCompiler.getFactory(), null, null, lhs, rhs);
  }

  private Node createFunction(int type, Node name, List<Node> params, Node body, Node... children) {
    return createFunction(NullCompiler.getFactory(), null, null, name, params, body, children);
  }

  private Node createIdentifier(String name) {
    return createIdentifier(NullCompiler.getFactory(), name);
  }

  private Node createName(String name, Node value) {
    return createName(NullCompiler.getFactory(), name, value);
  }

  private Node createObjectLiteral(Node... properties) {
    return createObjectLiteral(NullCompiler.getFactory(), null, null, properties);
  }

  private Node createProperty(String key, Node value) {
    return createProperty(NullCompiler.getFactory(), key, value);
  }

  private Node createPropertyAccess(Node obj, String name) {
    return createPropertyAccess(NullCompiler.getFactory(), obj, name);
  }

  private Node createPropertyAccess(Node obj, String name, Node index) {
    return createPropertyAccess(NullCompiler.getFactory(), obj, name, index);
  }

  private Node createString(String text) {
    return createString(NullCompiler.getFactory(), text);
  }

  private Node createThis() {
    return createThis(NullCompiler.getFactory());
  }

  private Node createGet(int op, Node obj) {
    return createGet(NullCompiler.getFactory(), null, null, obj);
  }

  private static Node NullCompiler() {
    return (Compiler) NullObject.NULL;
  }
}

class DiagnosticPosition {
  final int line;
  final int offset;
  final DiagnosticType type;

  DiagnosticPosition(int line, int offset, DiagnosticType type) {
    this.line = line;
    this.offset = offset;
    this.type = type;
  }
}