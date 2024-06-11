import static org.junit.Assert.*;
import static com.google.javascript.jscomp.Scope.*;
import com.google.common.collect.Maps;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

import java.util.HashMap;
import java.util.Map;

public class ScopeTest {

  private static Node createFunctionNode(String name) {
    Node functionNode = new Node(Token.FUNCTION);
    Node body = new Node(Token.BLOCK);
    Node nameNode = new Node(Token.IDENT, name);
    Node argsNode = new Node(Token.VAR, nameNode);
    Node paramsNode = new Node(Token.VARS, argsNode);
    functionNode.setFirstChild(paramsNode);
    functionNode.setLastChild(body);
    return functionNode;
  }

  private static Scope createScope(Node rootNode) {
    return new Scope(null, rootNode) {
      @Override
      public void declare(String name, Node nameNode, JSType type, CompilerInput input) {
        // Do nothing
      }
    };
  }

  @org.junit.Test
  public void testEmptyScope() {
    Scope scope = createScope(new Node(Token.BLOCK));
    assertEquals(0, scope.getVarCount());
  }

  @org.junit.Test
  public void testDeclareVariable() {
    Scope scope = createScope(createFunctionNode("test"));
    assertEquals(0, scope.getVarCount());

    String variableName = "a";
    Node variableNode = new Node(Token.VAR, new Node(Token.IDENT, variableName));
    JSType variableType = JSType.getNullType();
    CompilerInput input = new CompilerInput("", 0);

    Var variable = scope.declare(variableName, variableNode, variableType, input);
    assertEquals(1, scope.getVarCount());
    assertEquals(variable, scope.getVar(variableName));
  }

  @org.junit.Test
  public void testDeclareVariableWithInitialValue() {
    Scope scope = createScope(createFunctionNode("test"));
    assertEquals(0, scope.getVarCount());

    String variableName = "a";
    Node variableNode = new Node(Token.VAR, new Node(Token.IDENT, variableName));
    Node initialValueNode = new Node(Token.NUMBER, 42);

    JSType variableType = JSType.getNumberType();
    CompilerInput input = new CompilerInput("", 0);

    Var variable = scope.declare(variableName, variableNode, variableType, input);
    assertEquals(1, scope.getVarCount());
    assertEquals(variable, scope.getVar(variableName));

    Node initialValue = variable.getInitialValue();
    assertEquals(initialValueNode, initialValue);
  }

  @org.junit.Test
  public void testDeclareVariableWithSameNameInParentScope() {
    Scope parentScope = createScope(new Node(Token.BLOCK));
    String variableName = "a";
    Node variableNode = new Node(Token.VAR, new Node(Token.IDENT, variableName));
    JSType variableType = JSType.getNumberType();
    CompilerInput input = new CompilerInput("", 0);

    parentScope.declare(variableName, variableNode, variableType, input);

    Scope childScope = createScope(new Node(Token.FUNCTION, null, null, null));

    try {
      childScope.declare(variableName, variableNode, variableType, input);
      fail("Expected IllegalStateException");
    } catch (IllegalStateException e) {
      // Expected
    }
  }

  @org.junit.Test
  public void testUndelecareVariable() {
    Scope scope = createScope(new Node(Token.BLOCK));
    Var variable = scope.declare("a", null, null, null);
    assertEquals(1, scope.getVarCount());

    scope.undeclare(variable);
    assertEquals(0, scope.getVarCount());
  }

  @org.junit.Test
  public void testGetVariable() {
    Scope scope = createScope(createFunctionNode("test"));

    String variableName = "a";
    Node variableNode = new Node(Token.VAR, new Node(Token.IDENT, variableName));
    JSType variableType = JSType.getNullType();
    CompilerInput input = new CompilerInput("", 0);

    Var variable = scope.declare(variableName, variableNode, variableType, input);

    Var result = scope.getVar(variableName);
    assertEquals(variable, result);
  }

  @org.junit.Test
  public void testGetNonExistentVariable() {
    Scope scope = createScope(new Node(Token.BLOCK));
    assertNull(scope.getVar("a"));
  }

  @org.junit.Test
  public void testGetVariableFromParentScope() {
    Scope parentScope = createScope(new Node(Token.BLOCK));
    String variableName = "a";
    Node variableNode = new Node(Token.VAR, new Node(Token.IDENT, variableName));
    JSType variableType = JSType.getNumberType();
    CompilerInput input = new CompilerInput("", 0);

    parentScope.declare(variableName, variableNode, variableType, input);

    Scope childScope = createScope(new Node(Token.FUNCTION, null, null, null));
    Var result = childScope.getVar(variableName);
    assertNotNull(result);
  }

  @org.junit.Test
  public void testIsDeclared() {
    Scope scope = createScope(new Node(Token.BLOCK));
    assertFalse(scope.isDeclared("a", false));

    scope.declare("a", null, null, null);
    assertTrue(scope.isDeclared("a", false));
  }

  @org.junit.Test
  public void testIsDeclaredRecursively() {
    Scope scope = createScope(new Node(Token.BLOCK));
    assertFalse(scope.isDeclared("a", true));

    Node functionNode = createFunctionNode("test");
    Scope childScope = new Scope(scope, functionNode);
    childScope.declare("a", null, null, null);

    assertTrue(scope.isDeclared("a", true));
  }

  @org.junit.Test
  public void testGetVars() {
    Scope scope = createScope(createFunctionNode("test"));

    String variableName1 = "a";
    Node variableNode1 = new Node(Token.VAR, new Node(Token.IDENT, variableName1));
    JSType variableType1 = JSType.getNumberType();
    CompilerInput input1 = new CompilerInput("", 0);
    Var variable1 = scope.declare(variableName1, variableNode1, variableType1, input1);

    String variableName2 = "b";
    Node variableNode2 = new Node(Token.VAR, new Node(Token.IDENT, variableName2));
    JSType variableType2 = JSType.getBooleanType();
    CompilerInput input2 = new CompilerInput("", 0);
    Var variable2 = scope.declare(variableName2, variableNode2, variableType2, input2);

    Map<String, Var> expectedVars = Maps.newHashMap();
    expectedVars.put(variableName1, variable1);
    expectedVars.put(variableName2, variable2);

    Iterator<Var> varsIterator = scope.getVars();
    Map<String, Var> vars = Maps.newHashMap();
    while (varsIterator.hasNext()) {
      vars.put(varsIterator.next().getName(), varsIterator.next());
    }

    assertEquals(expectedVars, vars);
  }

  @org.junit.Test
  public void testGetTypeOfThis() {
    Scope globalScope = createScope(new Node(Token.SCRIPT));
    ObjectType thisType = JSType.getObjectType();
    globalScope.thisType = thisType;

    Scope functionScope = new Scope(globalScope, new Node(Token.FUNCTION, null, null, null));

    ObjectType result = functionScope.getTypeOfThis();
    assertEquals(thisType, result);
  }
}