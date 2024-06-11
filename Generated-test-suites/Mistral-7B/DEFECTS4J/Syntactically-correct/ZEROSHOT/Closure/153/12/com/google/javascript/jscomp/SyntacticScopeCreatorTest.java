import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.ScopeCreator;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.jscomp.Token;
import com.google.javascript.jscomp.JSType;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.NodeUtil;

import org.junit.Before;
import org.junit.Test;

public class SyntacticScopeCreatorTest {

  private AbstractCompiler compiler;
  private SyntacticScopeCreator creator;
  private Scope rootScope;
  private Node rootNode;

  @Before
  public void setUp() {
    compiler = mock(AbstractCompiler.class);
    creator = new SyntacticScopeCreator(compiler);
    rootNode = Node.newLiteralNode(Token.SCRIPT, "test");
  }

  @Test
  public void testCreateGlobalScope() {
    rootScope = creator.createScope(rootNode, null);
    assertNotNull(rootScope);
  }

  @Test
  public void testCreateFunctionScope() {
    String functionName = "testFunction";
    Node functionNode = Node.newFunctionNode(Token.FUNCTION, null, null);
    functionNode.setProp(Node.SOURCENAME_PROP, functionName);
    Node bodyNode = Node.newBlockNode(Token.BLOCK);

    Scope functionScope = creator.createScope(functionNode, rootScope);
    assertNotNull(functionScope);
    assertNotNull(functionScope.getParent());
    assertNotNull(functionScope.getParent().getVar(functionName));
  }

  @Test
  public void testCreateFunctionWithArguments() {
    String functionName = "testFunction";
    Node functionNode = Node.newFunctionNode(Token.FUNCTION, null, null);
    Node argumentsNode = Node.newListNode(Token.LP, Node.newListNode(Token.NAME, "arg1"),
        Node.newListNode(Token.COMMA, Node.newListNode(Token.NAME, "arg2")),
        Node.newToken(Token.RP, null));
    functionNode.setProp(Node.SOURCENAME_PROP, functionName);
    Node bodyNode = Node.newBlockNode(Token.BLOCK);

    Scope functionScope = creator.createScope(functionNode, rootScope);
    assertNotNull(functionScope);
    assertNotNull(functionScope.getParent());

    Var arg1 = functionScope.getVar("arg1");
    Var arg2 = functionScope.getVar("arg2");
    assertNotNull(arg1);
    assertNotNull(arg2);
  }

  @Test
  public void testCreateFunctionWithSameNameInParentScope() {
    String functionName = "testFunction";
    Node functionNode = Node.newFunctionNode(Token.FUNCTION, null, null);
    functionNode.setProp(Node.SOURCENAME_PROP, functionName);

    String parentFunctionName = "testParentFunction";
    Node parentFunctionNode = Node.newFunctionNode(Token.FUNCTION, null, null);
    parentFunctionNode.setProp(Node.SOURCENAME_PROP, parentFunctionName);
    parentFunctionNode.setFirstChild(functionNode);

    Node bodyNode = Node.newBlockNode(Token.BLOCK);

    Scope functionScope = creator.createScope(functionNode, rootScope);
    assertNotNull(functionScope);
    assertNotNull(functionScope.getParent());
    assertNotNull(functionScope.getParent().getVar(functionName));

    Scope parentFunctionScope = creator.createScope(parentFunctionNode, rootScope);
    assertNotNull(parentFunctionScope);
    assertNotNull(parentFunctionScope.getVar(functionName));
  }

  @Test
  public void testCreateFunctionWithSameNameInGlobalScope() {
    String functionName = "testFunction";
    Node functionNode = Node.newFunctionNode(Token.FUNCTION, null, null);
    functionNode.setProp(Node.SOURCENAME_PROP, functionName);

    Node bodyNode = Node.newBlockNode(Token.BLOCK);

    Scope functionScope = creator.createScope(functionNode, rootScope);
    assertNotNull(functionScope);
    assertNotNull(functionScope.getParent());
    assertNotNull(functionScope.getParent().getVar(functionName));

    assertNotNull(rootScope.getVar(functionName));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCreateFunctionWithInvalidName() {
    String functionName = "testFunction";
    Node functionNode = Node.newFunctionNode(Token.FUNCTION, null, null);
    functionNode.setProp(Node.SOURCENAME_PROP, functionName);

    // The first child of FunctionNode should be a NameNode representing function name
    Node invalidNameNode = Node.newToken(Token.NAME, "invalidName");
    functionNode.setFirstChild(invalidNameNode);

    creator.createScope(functionNode, rootScope);
  }

  @Test
  public void testCreateFunctionWithDuplicateLocalVariable() {
    String functionName = "testFunction";
    Node functionNode = Node.newFunctionNode(Token.FUNCTION, null, null);
    functionNode.setProp(Node.SOURCENAME_PROP, functionName);

    String localVariableName = "testVariable";
    Node variableNode = Node.newVarNode(Token.VAR, null, localVariableName, null);

    Node bodyNode = Node.newBlockNode(Token.BLOCK);
    bodyNode.setFirstChild(variableNode);

    Scope functionScope = creator.createScope(functionNode, rootScope);
    assertNotNull(functionScope);
    assertNotNull(functionScope.getParent());

    Node duplicateVariableNode = Node.newVarNode(Token.VAR, null, localVariableName, null);

    try {
      creator.createScope(bodyNode, functionScope);
    } catch (IllegalArgumentException e) {
      String expectedMessage =
          "Variable " + localVariableName + " first declared in " + functionName;
      DiagnosticType diagnosticType = DiagnosticType.error(
          "JSC_VAR_MULTIPLY_DECLARED_ERROR", expectedMessage);
      assert(e.getMessage().contains(diagnosticType.getMessage()));
      return;
    }

    fail();
  }

  @Test
  public void testCreateFunctionWithDuplicateGlobalVariable() {
    String functionName = "testFunction";
    Node functionNode = Node.newFunctionNode(Token.FUNCTION, null, null);
    functionNode.setProp(Node.SOURCENAME_PROP, functionName);

    String globalVariableName = "testVariable";
    Var globalVariable = rootScope.getVar(globalVariableName);

    Node bodyNode = Node.newBlockNode(Token.BLOCK);
    Var functionVariable = NodeUtil.createVarNode(Token.VAR, null, globalVariableName, null);
    bodyNode.setFirstChild(functionVariable);

    try {
      creator.createScope(bodyNode, functionScope);
    } catch (IllegalArgumentException e) {
      String expectedMessage = "Variable " + globalVariableName + " first declared in " + rootScope.getName();
      DiagnosticType diagnosticType = DiagnosticType.error(
          "JSC_VAR_MULTIPLY_DECLARED_ERROR", expectedMessage);
      assert(e.getMessage().contains(diagnosticType.getMessage()));
      return;
    }

    fail();
  }

  @Test
  public void testCreateFunctionWithDuplicateArgumentVariable() {
    String functionName = "testFunction";
    Node functionNode = Node.newFunctionNode(Token.FUNCTION, null, null);
    functionNode.setProp(Node.SOURCENAME_PROP, functionName);

    String argumentVariableName = "testArgument";
    Node argumentNode = Node.newListItemNode(Token.NAME, argumentVariableName);
    Node argumentsNode = Node.newListNode(Token.LP, argumentNode, Node.newToken(Token.RP, null));
    functionNode.setFirstChild(argumentsNode);

    Node bodyNode = Node.newBlockNode(Token.BLOCK);

    Scope functionScope = creator.createScope(functionNode, rootScope);
    assertNotNull(functionScope);
    assertNotNull(functionScope.getParent());

    Node duplicateArgumentVariableNode = Node.newListItemNode(Token.NAME, argumentVariableName);

    try {
      creator.createScope(bodyNode, functionScope);
    } catch (IllegalArgumentException e) {
      String expectedMessage = "Shadowing \"arguments\" is not allowed";
      DiagnosticType diagnosticType = DiagnosticType.error(
          "JSC_VAR_ARGUMENTS_SHADOWED_ERROR", expectedMessage);
      assert(e.getMessage().contains(diagnosticType.getMessage()));
      return;
    }

    fail();
  }

  @Test
  public void testCreateFunctionWithDuplicateVariableInParentWithSameType() {
    String functionName = "testFunction";
    Node functionNode = Node.newFunctionNode(Token.FUNCTION, null, null);
    functionNode.setProp(Node.SOURCENAME_PROP, functionName);

    String parentVariableName = "testVariable";
    String parentFunctionName = "testParentFunction";
    Node parentFunctionNode = Node.newFunctionNode(Token.FUNCTION, null, null);
    parentFunctionNode.setProp(Node.SOURCENAME_PROP, parentFunctionName);
    parentFunctionNode.setFirstChild(functionNode);

    Node parentVariableNode = Node.newVarNode(Token.VAR, null, parentVariableName, JSType.undefinedType());
    Node parentBodyNode = Node.newBlockNode(Token.BLOCK);
    parentBodyNode.setFirstChild(parentVariableNode);

    Scope functionScope = creator.createScope(functionNode, rootScope);
    assertNotNull(functionScope);
    assertNotNull(functionScope.getParent());

    Var parentVariable = creator.createScope(parentBodyNode, null).getVar(parentVariableName);
    JSType parentVariableType = parentVariable.getType();

    Var functionVariable = NodeUtil.createVarNode(Token.VAR, null, parentVariableName, parentVariableType);

    try {
      creator.createScope(bodyNode, functionScope);
    } catch (IllegalArgumentException e) {
      fail();
    }

    Var functionVariable2 = functionScope.getVar(parentVariableName);
    assertEquals(parentVariableType, functionVariable2.getType());
  }

  @Test
  public void testCreateFunctionWithDuplicateVariableInParentWithDifferentType() {
    String functionName = "testFunction";
    Node functionNode = Node.newFunctionNode(Token.FUNCTION, null, null);
    functionNode.setProp(Node.SOURCENAME_PROP, functionName);

    String parentVariableName = "testVariable";
    String parentFunctionName = "testParentFunction";
    Node parentFunctionNode = Node.newFunctionNode(Token.FUNCTION, null, null);
    parentFunctionNode.setProp(Node.SOURCENAME_PROP, parentFunctionName);
    parentFunctionNode.setFirstChild(functionNode);

    Node parentVariableNode = Node.newVarNode(Token.VAR, null, parentVariableName, JSType.booleanType());
    Node parentBodyNode = Node.newBlockNode(Token.BLOCK);
    parentBodyNode.setFirstChild(parentVariableNode);

    Scope functionScope = creator.createScope(functionNode, rootScope);
    assertNotNull(functionScope);
    assertNotNull(functionScope.getParent());

    Var parentVariable = creator.createScope(parentBodyNode, null).getVar(parentVariableName);

    Var functionVariable = NodeUtil.createVarNode(Token.VAR, null, parentVariableName, JSType.numberType());

    try {
      creator.createScope(bodyNode, functionScope);
    } catch (IllegalArgumentException e) {
      fail();
    }

    Var functionVariable2 = functionScope.getVar(parentVariableName);
    assertNotEquals(parentVariable.getType(), functionVariable2.getType());
  }

  @Test
  public void testCreateFunctionWithDuplicateVariableInParentWithJSDocType() {
    String functionName = "testFunction";
    Node functionNode = Node.newFunctionNode(Token.FUNCTION, null, null);
    functionNode.setProp(Node.SOURCENAME_PROP, functionName);

    String parentVariableName = "testVariable";
    String parentFunctionName = "testParentFunction";
    Node parentFunctionNode = Node.newFunctionNode(Token.FUNCTION, null, null);
    parentFunctionNode.setProp(Node.SOURCENAME_PROP, parentFunctionName);
    parentFunctionNode.setFirstChild(functionNode);

    Node parentVariableNode = Node.newVarNode(Token.VAR, null, parentVariableName, null);
    Node parentJSDocInfoNode = Node.newObjectLiteralNode(Token.OBJECT, null);
    Node parentJSDocTypeNode = Node.newNameNode(Token.NAME, "MyType");
    Node parentJSDocInfo = Node.newObjectLiteralNode(Token.NAME, "JSDocInfo", null);
    Node parentJSDocInfoPropertyNode = Node.newPropertyNode(Token.PROPS, null, parentJSDocInfoNode, null);
    Node parentJSDocType = Node.newPropertyNode(Token.NAME, "type", null, parentJSDocTypeNode);
    Node parentJSDocInfoTypeNode = Node.newListItemNode(Token.PARENTHESIS_OPEN, parentJSDocInfoPropertyNode, parentJSDocType, Node.newToken(Token.PARENTHSIS_CLOSE, null));
    Node parentBodyNode = Node.newBlockNode(Token.BLOCK);
    parentBodyNode.setFirstChild(parentVariableNode);
    parentBodyNode.setLastChild(parentJSDocInfoTypeNode);

    Scope functionScope = creator.createScope(functionNode, rootScope);
    assertNotNull(functionScope);
    assertNotNull(functionScope.getParent());

    Var parentVariable = creator.createScope(parentBodyNode, null).getVar(parentVariableName);

    JSType parentVariableType = parentVariable.getType();

    Var functionVariable = NodeUtil.createVarNode(Token.VAR, null, parentVariableName, null);

    try {
      creator.createScope(bodyNode, functionScope);
    } catch (IllegalArgumentException e) {
      fail();
    }

    Var functionVariable2 = functionScope.getVar(parentVariableName);
    assertEquals(parentVariableType, functionVariable2.getType());
  }
}