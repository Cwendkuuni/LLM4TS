import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.JSError;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.ScopeCreator;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.jscomp.Token;
import com.google.javascript.jscomp.diagnostics.DiagnosticType;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.NodeUtil;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatcher;

public class SyntacticScopeCreatorTest {

  private AbstractCompiler compiler;
  private ScopeCreator syntacticScopeCreator;
  private Scope parentScope;
  private Node rootNode;

  @Before
  public void setUp() {
    compiler = mock(AbstractCompiler.class);
    syntacticScopeCreator = new SyntacticScopeCreator(compiler);
    parentScope = mock(Scope.class);

    rootNode = createFunctionDeclarationNode("testFunction");
  }

  @Test
  public void testCreateScope_RootNodeIsFunctionDeclaration() {
    Scope scope = syntacticScopeCreator.createScope(rootNode, null);
    assertNotNull(scope);
  }

  @Test(expected = NullPointerException.class)
  public void testCreateScope_ParentScopeIsNull() {
    syntacticScopeCreator.createScope(rootNode, null);
  }

  @Test
  public void testScanRoot() {
    final String functionName = "testFunction";
    final String variableName1 = "variable1";
    final String variableName2 = "variable2";

    Node functionDeclaration = rootNode;
    Node argumentsNode = functionDeclaration.getFirstChild();
    Node variable1Node = createVariableDeclarationNode(variableName1);
    Node variable2Node = createVariableDeclarationNode(variableName2);
    Node bodyNode = createStatementBlockNode();

    setFunctionName(functionDeclaration, functionName);
    setChildren(functionDeclaration, argumentsNode, bodyNode);
    setChildren(argumentsNode, variable1Node, variable2Node);

    syntacticScopeCreator.scanRoot(functionDeclaration, parentScope);

    assertVariableDeclared(functionName, functionDeclaration);
    assertVariableDeclared(variableName1, variable1Node);
    assertVariableDeclared(variableName2, variable2Node);
  }

  @Test
  public void testScanVars() {
    final String variableName1 = "variable1";
    final String variableName2 = "variable2";

    Node variable1Node = createVariableDeclarationNode(variableName1);
    Node variable2Node = createVariableDeclarationNode(variableName2);
    Node statementBlockNode = createStatementBlockNode();

    setChildren(variable1Node, createNullNode());
    setChildren(variable2Node, createNullNode());
    setChildren(statementBlockNode, variable1Node, variable2Node);

    syntacticScopeCreator.scanVars(statementBlockNode, null);

    assertVariableDeclared(variableName1, variable1Node);
    assertVariableDeclared(variableName2, variable2Node);
  }

  @Test
  public void testCreateScope_ParentScopeIsFunctionDeclaration() {
    final String parentFunctionName = "parentFunction";
    final String childFunctionName = "childFunction";

    Node parentFunctionDeclaration = createFunctionDeclarationNode(parentFunctionName);
    Node childFunctionDeclaration = createFunctionDeclarationNode(childFunctionName);
    Node childFunctionArgumentsNode = childFunctionDeclaration.getFirstChild();
    Node childFunctionBodyNode = childFunctionArgumentsNode.getNext();

    setFunctionName(parentFunctionDeclaration, parentFunctionName);
    setFunctionName(childFunctionDeclaration, childFunctionName);
    setChildren(parentFunctionDeclaration, createNullNode(), createStatementBlockNode());
    setChildren(childFunctionDeclaration, childFunctionArgumentsNode, childFunctionBodyNode);

    Scope parentScope = mock(Scope.class);
    Scope childScope = syntacticScopeCreator.createScope(childFunctionDeclaration, parentScope);

    assertNotNull(childScope);
    assertParentAndChildScopes(parentScope, childScope);
  }

  @Test
  public void testCreateScope_VariableNameIsArguments() {
    final String variableName = "arguments";
    Node argumentsNode = createArgumentsNode();

    Scope scope = syntacticScopeCreator.createScope(argumentsNode, null);

    Var variable = scope.getVar(variableName);
    assertNotNull(variable);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCreateScope_ParentScopeIsNotFunctionDeclaration() {
    Node rootNode = createVariableDeclarationNode("rootVariable");
    Scope scope = syntacticScopeCreator.createScope(rootNode, null);
  }

  @Test
  public void testRedeclarationHandler() {
    final String variableName1 = "variable1";
    final String variableName2 = "variable1";

    Node variable1Node = createVariableDeclarationNode(variableName1);
    Node variable2Node = createVariableDeclarationNode(variableName2);
    Node statementBlockNode = createStatementBlockNode();

    setChildren(variable1Node, createNullNode());
    setChildren(variable2Node, createNullNode());
    setChildren(statementBlockNode, variable1Node, variable2Node);

    ScopeCreator syntacticScopeCreator = new SyntacticScopeCreator(compiler, new RedeclarationHandler() {
      @Override
      public void onRedeclaration(Scope s, String name, Node n, Node parent, Node gramps, Node nodeWithLineNumber) {
        throw new RuntimeException("Should not be called");
      }
    });

    syntacticScopeCreator.scanVars(statementBlockNode, null);

    verify(compiler, new ArgumentMatcher<JSError>() {
      @Override
      public boolean matches(JSError error) {
        return error.getMessage().contains("Variable variable1 first declared in testSyntacticScopeCreatorTest");
      }
    }).hasBeenCalled();
  }

  private Node createFunctionDeclarationNode(String name) {
    Node functionDeclarationNode = new Node();
    functionDeclarationNode.setType(Token.FUNCTION);
    Node functionNameNode = new Node();
    functionNameNode.setType(Token.NAME);
    functionNameNode.setString(name);
    functionDeclarationNode.appendChild(functionNameNode);
    functionDeclarationNode.setProp(Node.SOURCENAME_PROP, name);
    return functionDeclarationNode;
  }

  private Node createArgumentsNode() {
    Node argumentsNode = new Node();
    argumentsNode.setType(Token.LP);
    return argumentsNode;
  }

  private Node createVariableDeclarationNode(String name) {
    Node variableDeclarationNode = new Node();
    variableDeclarationNode.setType(Token.VAR);
    Node variableNameNode = new Node();
    variableNameNode.setType(Token.NAME);
    variableNameNode.setString(name);
    variableDeclarationNode.appendChild(variableNameNode);
    return variableDeclarationNode;
  }

  private Node createStatementBlockNode() {
    Node statementBlockNode = new Node();
    statementBlockNode.setType(Token.SCRIPT);
    return statementBlockNode;
  }

  private Node createNullNode() {
    Node nullNode = new Node();
    nullNode.setType(Token.NULL);
    return nullNode;
  }

  private void setFunctionName(Node functionDeclarationNode, String name) {
    functionDeclarationNode.setProp(Node.NAME_PROP, name);
  }

  private void setChildren(Node node, Node... children) {
    for (Node child : children) {
      node.appendChild(child);
    }
  }

  private void assertParentAndChildScopes(Scope parentScope, Scope childScope) {
    assertNotNull(parentScope.getParent());
    assertNull(childScope.getParent());
    assertSame(parentScope, childScope.getParent());
  }

  private void assertVariableDeclared(String variableName, Node variableNode) {
    Var variable = scope.getVar(variableName);
    assertNotNull(variable);
    assertSame(variableNode, variable.getNode());
  }

  private Node rootNode;

  private Node createFunctionDeclarationNode(String name) {
    // Same as in the setUp method, but for test isolation.
    Node functionDeclarationNode = new Node();
    functionDeclarationNode.setType(Token.FUNCTION);
    Node functionNameNode = new Node();
    functionNameNode.setType(Token.NAME);
    functionNameNode.setString(name);
    functionDeclarationNode.appendChild(functionNameNode);
    functionDeclarationNode.setProp(Node.SOURCENAME_PROP, name);
    return functionDeclarationNode;
  }
}