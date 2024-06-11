import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.ScopeCreator;
import com.google.javascript.jscomp.Token;
import com.google.javascript.jscomp.jstype.JSType;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.NodeUtil;

import org.junit.Before;
import org.junit.Test;

public class SyntacticScopeCreatorTest {

  private AbstractCompiler compiler;
  private SyntacticScopeCreator scopeCreator;
  private Scope scope;
  private Node rootNode;

  @Before
  public void setUp() {
    compiler = mock(AbstractCompiler.class);
    scopeCreator = new SyntacticScopeCreator(compiler);
  }

  @Test
  public void shouldCreateGlobalScope() {
    scope = scopeCreator.createScope(createRootNode("test.js"), null);
    assertNotNull(scope);
    assertNotNull(scope.getVars());
  }

  @Test
  public void shouldCreateFunctionScope() {
    rootNode = createRootNode("test.js", createFunctionNode("testFunction"));
    scope = scopeCreator.createScope(rootNode, null);
    assertNotNull(scope);
    assertNotNull(scope.getVars());
  }

  @Test(expected = IllegalArgumentException.class)
  public void shouldThrowIllegalArgumentExceptionWhenParentIsNull() {
    scopeCreator.createScope(null, null);
  }

  @Test
  public void shouldNotDeclareVariableTwiceInGlobalScope() {
    rootNode = createRootNode("test.js", createVariableNode("x"));
    scope = scopeCreator.createScope(rootNode, null);
    scopeCreator.createScope(createVariableNode("x"), rootNode.getFirstChild());

    assertNotNull(scope.getVar("x"));

    ScopeCreator.DefaultRedeclarationHandler redeclarationHandler =
        scopeCreator.new DefaultRedeclarationHandler();
    scope = scopeCreator.createScope(createVariableNode("x"), rootNode.getFirstChild(), redeclarationHandler);

    assertNotNull(scope.getVar("x"));
  }

  @Test
  public void shouldNotDeclareVariableTwiceInFunctionScope() {
    rootNode = createRootNode("test.js", createFunctionNode("testFunction"));
    scope = scopeCreator.createScope(rootNode, null);

    Node variableNode1 = createVariableNode("x");
    Node variableNode2 = createVariableNode("x");
    scope.createScope(variableNode1, rootNode.getFirstChild());
    scope.createScope(variableNode2, variableNode1);

    assertNotNull(scope.getVar("x"));

    ScopeCreator.DefaultRedeclarationHandler redeclarationHandler =
        scopeCreator.new DefaultRedeclarationHandler();
    scope = scopeCreator.createScope(variableNode2, variableNode1, redeclarationHandler);

    assertNotNull(scope.getVar("x"));
  }

  @Test
  public void shouldNotDeclareArgumentsVariableTwice() {
    rootNode = createRootNode("test.js", createFunctionNode("testFunction"));
    scope = scopeCreator.createScope(rootNode, null);

    Node argumentsNode = createVariableNode("arguments");
    scope.createScope(argumentsNode, rootNode.getFirstChild());
    scope.createScope(argumentsNode, rootNode.getFirstChild().getNext());

    assertNotNull(scope.getVar("arguments"));
  }

  @Test(expected = IllegalArgumentException.class)
  public void shouldThrowIllegalArgumentExceptionWhenVariableNameIsEmpty() {
    rootNode = createNode("test.js", Token.VAR);
    scopeCreator.createScope(rootNode, null);
  }

  @Test
  public void shouldDeclareFunctionNameAsVariable() {
    rootNode = createRootNode("test.js", createFunctionNode("testFunction"));
    scope = scopeCreator.createScope(rootNode, null);

    Node functionNameNode = rootNode.getFirstChild();
    assertNotNull(scope.getVar(functionNameNode.getString()));
  }

  @Test
  public void shouldDeclareArgumentsAsVariable() {
    rootNode = createRootNode("test.js", createFunctionNode("testFunction"));
    scope = scopeCreator.createScope(rootNode, null);

    Node argumentsNode = rootNode.getFirstChild().getNext();
    assertNotNull(scope.getVar(argumentsNode.getString()));
  }

  @Test
  public void shouldHandleDuplicateVariableDeclaration() {
    rootNode = createRootNode("test.js", createVariableNode("x"));
    scope = scopeCreator.createScope(rootNode, null);

    ScopeCreator.DefaultRedeclarationHandler redeclarationHandler =
        scopeCreator.new DefaultRedeclarationHandler();
    scope = scopeCreator.createScope(createVariableNode("x"), rootNode.getFirstChild(), redeclarationHandler);

    assertNotNull(scope.getVar("x"));
  }

  @Test
  public void shouldReportErrorWhenDeclaringDuplicateVariableInGlobalScope() {
    rootNode = createRootNode("test.js", createVariableNode("x"));
    scope = scopeCreator.createScope(rootNode, null);

    ScopeCreator.DefaultRedeclarationHandler redeclarationHandler =
        scopeCreator.new DefaultRedeclarationHandler();
    scope = scopeCreator.createScope(createVariableNode("x"), rootNode, redeclarationHandler);

    assertNotNull(scope.getVar("x"));

    assert (compiler.getDiagnostics().size() > 0);
  }

  @Test
  public void shouldReportErrorWhenDeclaringDuplicateVariableInFunctionScope() {
    rootNode = createRootNode("test.js", createFunctionNode("testFunction"));
    scope = scopeCreator.createScope(rootNode, null);

    Node variableNode1 = createVariableNode("x");
    Node variableNode2 = createVariableNode("x");
    scope.createScope(variableNode1, rootNode.getFirstChild());
    scope.createScope(variableNode2, variableNode1);

    assertNotNull(scope.getVar("x"));

    assert (compiler.getDiagnostics().size() > 0);
  }

  @Test
  public void shouldReportErrorWhenDeclaringDuplicateArgumentVariable() {
    rootNode = createRootNode("test.js", createFunctionNode("testFunction"));
    scope = scopeCreator.createScope(rootNode, null);

    Node argumentsNode = rootNode.getFirstChild().getNext();
    Node duplicateArgumentsNode = createVariableNode("arguments");
    scope.createScope(duplicateArgumentsNode, argumentsNode, null);

    assertNotNull(scope.getVar("arguments"));

    assert (compiler.getDiagnostics().size() > 0);
  }

  @Test
  public void shouldReportErrorWhenDeclaringDuplicateVariableWithSameNameButDifferentType() {
    rootNode = createRootNode("test.js", createVariableNode("x"));
    scope = scopeCreator.createScope(rootNode, null);

    ScopeCreator.DefaultRedeclarationHandler redeclarationHandler =
        scopeCreator.new DefaultRedeclarationHandler();
    scope = scopeCreator.createScope(createVariableNode("x", JSType.ARRAY), rootNode, redeclarationHandler);

    assertNotNull(scope.getVar("x"));

    assert (compiler.getDiagnostics().size() > 0);
  }

  private Node createRootNode(String sourceName, Node... children) {
    Node node = createNode(sourceName, Token.SCRIPT);
    for (Node child : children) {
      node.addChild(child);
    }
    return node;
  }

  private Node createFunctionNode(String name) {
    Node node = createNode(null, Token.FUNCTION);
    node.addChild(createNode(name, Token.NAME));
    node.addChild(createNode("{}", Token.LPAREN, Token.RPAREN, Token.LBRACE, Token.RBRACE));
    return node;
  }

  private Node createVariableNode(String name, JSType type) {
    Node node = createNode(name, Token.VAR);
    if (type != null) {
      node.setType(type);
    }
    return node;
  }

  private Node createVariableNode(String name) {
    return createVariableNode(name, null);
  }

  private Node createNode(String sourceName, int type) {
    Node node = new Node();
    node.setSourceName(sourceName);
    node.setType(type);
    return node;
  }

  private Node createNode(String sourceName, int... types) {
    Node node = new Node();
    node.setSourceName(sourceName);
    node.setType(types[0]);
    for (int i = 1; i < types.length; i++) {
      node.addType(types[i]);
    }
    return node;
  }
}