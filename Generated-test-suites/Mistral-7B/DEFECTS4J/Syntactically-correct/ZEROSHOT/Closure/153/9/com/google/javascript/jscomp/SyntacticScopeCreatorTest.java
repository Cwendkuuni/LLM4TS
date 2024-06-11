import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.JSDocInfo;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.ScopeCreator;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.jscomp.Token;
import com.google.javascript.jscomp.jstype.JSType;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;

public class SyntacticScopeCreatorTest {

  private AbstractCompiler compiler;
  private ScopeCreator scopeCreator;
  private Scope parentScope;
  private Node rootNode;

  @Before
  public void setUp() {
    compiler = mock(AbstractCompiler.class);
    parentScope = mock(Scope.class);
    scopeCreator = new SyntacticScopeCreator(compiler);
    rootNode = Node.newNode(Token.SCRIPT);
  }

  @Test
  public void testCreateScopeForRootNode() {
    Scope scope = scopeCreator.createScope(rootNode, null);
    assertNotNull(scope);
  }

  @Test
  public void testCreateScopeForFunctionNode() {
    Node functionNode = Node.newNode(Token.FUNCTION);
    Node fnNameNode = Node.newNode(Token.IDENTIFIER, "myFunction");
    Node argsNode = Node.newNode(Token.LP);
    Node bodyNode = Node.newNode(Token.BLOCK);

    functionNode.setProp(Node.CHILDREN_PROP,
        Node.newList(fnNameNode, argsNode, bodyNode));

    Scope scope = scopeCreator.createScope(functionNode, parentScope);
    assertNotNull(scope);

    assertScopeContainsVariable(scope, "myFunction");
  }

  @Test
  public void testCreateScopeForFunctionWithArguments() {
    Node functionNode = Node.newNode(Token.FUNCTION);
    Node fnNameNode = Node.newNode(Token.IDENTIFIER, "myFunction");
    Node argsNode = Node.newNode(Token.LP);
    Node valueNode1 = Node.newNode(Token.NAME, "arg1");
    Node valueNode2 = Node.newNode(Token.NAME, "arg2");
    Node bodyNode = Node.newNode(Token.BLOCK);

    argsNode.setProp(Node.CHILDREN_PROP,
        Node.newList(valueNode1, valueNode2));

    functionNode.setProp(Node.CHILDREN_PROP,
        Node.newList(fnNameNode, argsNode, bodyNode));

    Scope scope = scopeCreator.createScope(functionNode, parentScope);
    assertNotNull(scope);

    assertScopeContainsVariable(scope, "myFunction");
    assertScopeContainsVariable(scope, "arg1");
    assertScopeContainsVariable(scope, "arg2");
  }

  @Test
  public void testCreateScopeForGlobalScope() {
    Scope scope = scopeCreator.createScope(rootNode, null);
    assertNotNull(scope);

    assertScopeContainsVariable(scope, "arguments");
  }

  @Test
  public void testCreateScopeForCatchBlock() {
    Node catchNode = Node.newNode(Token.CATCH);
    Node catchVariableNode = Node.newNode(Token.NAME, "catchVar");
    Node catchBlockNode = Node.newNode(Token.BLOCK);

    catchNode.setProp(Node.CHILDREN_PROP,
        Node.newList(catchVariableNode, catchBlockNode));

    Scope scope = scopeCreator.createScope(catchNode, parentScope);
    assertNotNull(scope);

    assertScopeContainsVariable(scope, "catchVar");
  }

  @Test
  public void testCreateScopeForVariableDeclaration() {
    Node variableDeclarationNode = Node.newNode(Token.VAR);
    Node variableNameNode1 = Node.newNode(Token.NAME, "var1");
    Node variableNameNode2 = Node.newNode(Token.NAME, "var2");

    variableDeclarationNode.setProp(Node.CHILDREN_PROP,
        Node.newList(variableNameNode1, variableNameNode2));

    Node parentNode = Node.newNode(Token.BLOCK);

    Scope scope = scopeCreator.createScope(parentNode, parentScope);
    scopeCreator.createScope(variableDeclarationNode, scope);

    assertScopeContainsVariable(scope, "var1");
    assertScopeContainsVariable(scope, "var2");
  }

  @Test
  public void testCreateScopeForVariableDeclarationWithType() {
    Node variableDeclarationNode = Node.newNode(Token.VAR);
    Node variableNameNode = Node.newNode(Token.NAME, "var1");
    Node variableTypeNode = Node.newNode(Token.IDENTIFIER, "String");

    variableDeclarationNode.setProp(Node.CHILDREN_PROP,
        Node.newList(variableNameNode, variableTypeNode));

    Node parentNode = Node.newNode(Token.BLOCK);

    Scope scope = scopeCreator.createScope(parentNode, parentScope);
    scopeCreator.createScope(variableDeclarationNode, scope);

    Var variable = scope.getVar("var1");
    JSType variableType = variable.getType();

    assertNotNull(variable);
    assertNotNull(variableType);
    assertEquals("String", variableType.getName());
  }

  @Test
  public void testCreateScopeForVariableDeclarationWithJSDoc() {
    Node variableDeclarationNode = Node.newNode(Token.VAR);
    Node variableNameNode = Node.newNode(Token.NAME, "var1");
    Node jsDocNode = Node.newNode(Token.COMMENT, "/** @type {String} */");

    variableDeclarationNode.setProp(Node.CHILDREN_PROP,
        Node.newList(variableNameNode, jsDocNode));

    Node parentNode = Node.newNode(Token.BLOCK);

    Scope scope = scopeCreator.createScope(parentNode, parentScope);
    scopeCreator.createScope(variableDeclarationNode, scope);

    Var variable = scope.getVar("var1");
    JSType variableType = variable.getType();

    assertNotNull(variable);
    assertNotNull(variableType);
    assertEquals("String", variableType.getName());
  }

  @Test(expected = AssertionError.class)
  public void testCreateScopeForDuplicateVariableDeclaration() {
    Node variableDeclarationNode1 = Node.newNode(Token.VAR);
    Node variableNameNode1 = Node.newNode(Token.NAME, "duplicateVar");

    Node variableDeclarationNode2 = Node.newNode(Token.VAR);
    Node variableNameNode2 = Node.newNode(Token.NAME, "duplicateVar");

    variableDeclarationNode1.setProp(Node.CHILDREN_PROP,
        Node.newList(variableNameNode1));
    variableDeclarationNode2.setProp(Node.CHILDREN_PROP,
        Node.newList(variableNameNode2));

    Node parentNode = Node.newNode(Token.BLOCK);

    Scope scope = scopeCreator.createScope(parentNode, parentScope);
    scopeCreator.createScope(variableDeclarationNode1, scope);
    scopeCreator.createScope(variableDeclarationNode2, scope);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCreateScopeForNodeWithoutName() {
    Node variableDeclarationNode = Node.newNode(Token.VAR);
    Node variableNameNode = Node.newNode(Token.NUMBER, 1);

    variableDeclarationNode.setProp(Node.CHILDREN_PROP,
        Node.newList(variableNameNode));

    Node parentNode = Node.newNode(Token.BLOCK);

    Scope scope = scopeCreator.createScope(parentNode, parentScope);
    scopeCreator.createScope(variableDeclarationNode, scope);
  }

  private void assertScopeContainsVariable(Scope scope, String variableName) {
    boolean containsVariable = scope.containsVariable(variableName);
    Preconditions.checkState(containsVariable);
  }
}