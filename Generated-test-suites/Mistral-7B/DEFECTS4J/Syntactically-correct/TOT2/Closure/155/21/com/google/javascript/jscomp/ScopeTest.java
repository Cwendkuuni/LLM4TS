import static org.junit.Assert.*;
import static com.google.javascript.jscomp.Scope.Var.*;

import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Token;

import java.util.LinkedHashMap;
import java.util.Map;

public class ScopeTest {

  @org.junit.Test
  public void testScopeCreation() {
    // Test creating a new scope with a root node and a parent scope
    Node rootNode = Node.newFunctionNode(Token.FUNCTION, new int[0], null);
    Scope parent = new Scope(null, Node.newBlockNode(Token.BLOCK, null));
    Scope scope = new Scope(parent, rootNode);

    assertEquals(1, scope.getDepth());
    assertEquals(parent, scope.getParent());
    assertNull(scope.getTypeOfThis());

    // Test creating a global scope
    Scope globalScope = new Scope(Node.newBlockNode(Token.BLOCK, null), null);
    assertEquals(0, globalScope.getDepth());
    assertNull(globalScope.getParent());

    // Test creating an empty scope
    Scope emptyScope = new Scope(Node.newFunctionNode(Token.FUNCTION, new int[0], null), null);
    assertEquals(0, emptyScope.getDepth());
    assertNull(emptyScope.getParent());
    assertTrue(emptyScope.isBottom());
  }

  @org.junit.Test
  public void testVariableDeclaration() {
    Node rootNode = Node.newFunctionNode(Token.FUNCTION, new int[0], null);
    Scope parent = new Scope(null, Node.newBlockNode(Token.BLOCK, null));
    Scope scope = new Scope(parent, rootNode);

    String variableName = "testVariable";
    Node variableNode = Node.newNameNode(variableName);
    JSType variableType = JSType.INT;
    CompilerInput input = new CompilerInput("test.js", "test file");

    Var declaredVariable = scope.declare(variableName, variableNode, variableType, input);

    // Test getting variable name
    assertEquals(variableName, declaredVariable.getName());

    // Test getting variable node
    Node expectedVariableNode = variableNode;
    Node actualVariableNode = declaredVariable.getNameNode();
    assertEquals(expectedVariableNode, actualVariableNode);

    // Test getting variable type
    JSType expectedVariableType = variableType;
    JSType actualVariableType = declaredVariable.getType();
    assertEquals(expectedVariableType, actualVariableType);

    // Test resolving variable type
    ErrorReporter errorReporter = new ErrorReporter();
    declaredVariable.resolveType(errorReporter);
    JSType resolvedVariableType = declaredVariable.getType();
    assertEquals(expectedVariableType, resolvedVariableType);

    // Test getting JSDocInfo
    JSDocInfo expectedJSDocInfo = new JSDocInfo("test doc info");
    JSDocInfo actualJSDocInfo = declaredVariable.getJSDocInfo();
    assertEquals(expectedJSDocInfo, actualJSDocInfo);

    // Test isTypeInferred
    assertTrue(declaredVariable.isTypeInferred());

    // Test getInputName
    assertEquals("test.js", declaredVariable.getInputName());
  }

  @org.junit.Test
  public void testVariableUndDeclaration() {
    Node rootNode = Node.newFunctionNode(Token.FUNCTION, new int[0], null);
    Scope parent = new Scope(null, Node.newBlockNode(Token.BLOCK, null));
    Scope scope = new Scope(parent, rootNode);

    String variableName = "testVariable";
    Node variableNode = Node.newNameNode(variableName);
    JSType variableType = JSType.INT;
    CompilerInput input = new CompilerInput("test.js", "test file");

    Var declaredVariable = scope.declare(variableName, variableNode, variableType, input);

    scope.undeclare(declaredVariable);

    // Test getting variable
    assertNull(scope.getVar(variableName));
  }

  @org.junit.Test
  public void testVariableIsDefined() {
    Node rootNode = Node.newFunctionNode(Token.FUNCTION, new int[0], null);
    Scope parent = new Scope(null, Node.newBlockNode(Token.BLOCK, null));
    Scope scope = new Scope(parent, rootNode);

    String variableName = "testVariable";
    Node variableNode = Node.newNameNode(variableName);
    JSType variableType = JSType.INT;
    CompilerInput input = new CompilerInput("test.js", "test file");

    Var declaredVariable = scope.declare(variableName, variableNode, variableType, input);

    // Test isDefine
    assertTrue(declaredVariable.isDefine());

    // Test isGlobal
    assertFalse(declaredVariable.isGlobal());

    // Test isLocal
    assertTrue(declaredVariable.isLocal());
  }

  @org.junit.Test
  public void testVariableIsExtern() {
    Node rootNode = Node.newFunctionNode(Token.FUNCTION, new int[0], null);
    Scope scope = new Scope(null, rootNode);

    String variableName = "testVariable";
    CompilerInput input = new CompilerInput("test.js", "test file");

    // Test isExtern when variable has no input
    Var declaredVariable = scope.declare(variableName, null, JSType.INT, input);
    assertTrue(declaredVariable.isExtern());

    // Test isExtern when variable does have an input
    CompilerInput inputWithFile = new CompilerInput("test.js", "test file", "testFile.js");
    Var declaredVariableWithFile = scope.declare(variableName, null, JSType.INT, inputWithFile);
    assertFalse(declaredVariableWithFile.isExtern());
  }

  @org.junit.Test
  public void testVariableIsConst() {
    Node rootNode = Node.newFunctionNode(Token.FUNCTION, new int[0], null);
    Scope parent = new Scope(null, Node.newBlockNode(Token.BLOCK, null));
    Scope scope = new Scope(parent, rootNode);

    String variableName = "testVariable";
    Node variableNode = Node.newNameNode(variableName);

    // Test isConst when variable node is a constant name
    Var declaredVariable = scope.declare(variableName, variableNode, JSType.INT, null);
    assertTrue(declaredVariable.isConst());

    // Test isConst when variable node is not a constant name
    Node nonConstantNode = Node.newNameNode("nonConstantVariable");
    Var nonConstantVariable = scope.declare(variableName, nonConstantNode, JSType.INT, null);
    assertFalse(nonConstantVariable.isConst());
  }

  @org.junit.Test
  public void testVariableIsBleedingFunction() {
    Node rootNode = Node.newFunctionNode(Token.FUNCTION, new int[0], null);
    Scope parent = new Scope(null, Node.newBlockNode(Token.BLOCK, null));
    Scope scope = new Scope(parent, rootNode);

    String variableName = "testVariable";
    Node functionNode = Node.newFunctionExpressionNode(Token.FUNCTION, new int[0], null, null);
    Node variableNode = Node.newNameNode(variableName);

    // Test isBleedingFunction when variable is directly under function
    Var declaredVariable = scope.declare(variableName, variableNode, JSType.INT, null);
    Var functionVariable = scope.getVar(variableName);
    assertTrue(functionVariable.isBleedingFunction());

    // Test isBleedingFunction when variable is not directly under function
    Node parentFunctionNode = Node.newFunctionExpressionNode(Token.FUNCTION, new int[0], null, null);
    Scope parentOfParentScope = new Scope(scope, parentFunctionNode);
    Var notBleedingVariable = parentOfParentScope.declare(variableName, variableNode, JSType.INT, null);
    assertFalse(notBleedingVariable.isBleedingFunction());
  }

  @org.junit.Test
  public void testVariableGetInitialValue() {
    Node rootNode = Node.newFunctionNode(Token.FUNCTION, new int[0], null);
    Scope parent = new Scope(null, Node.newBlockNode(Token.BLOCK, null));
    Scope scope = new Scope(parent, rootNode);

    String variableName = "testVariable";
    Node assignNode = Node.newAssignNode(Token.ASSIGN, Node.newNameNode(variableName), Node.newNumberNode(1));
    Node variableNode = Node.newNameNode(variableName);

    // Test getting initial value when variable is directly assigned
    Var declaredVariable = scope.declare(variableName, null, JSType.INT, null);
    Node initialValueNode = declaredVariable.getInitialValue();
    assertEquals(assignNode, initialValueNode);

    // Test getting initial value when variable is not directly assigned
    Var notInitializedVariable = scope.declare(variableName, variableNode, JSType.INT, null);
    assertNull(notInitializedVariable.getInitialValue());
  }
}