import static org.junit.Assert.*;
import static com.google.javascript.jscomp.Scope.Var.*;

import com.google.common.collect.LinkedHashMap;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.Scope;

import java.util.Iterator;
import java.util.Map;

public class ScopeTest {

  @SuppressWarnings("unchecked")
  private static <T> Scope<T> createScope(Node rootNode, Scope<T> parent) {
    return new Scope<T>(parent, rootNode) {
      @Override
      public T getTypeOfThis() {
        throw new UnsupportedOperationException();
      }
    };
  }

  @Test
  public void testCreateScope() {
    Scope<Object> globalScope = createScope(Node.rootBlock(), null);
    assertNotNull(globalScope);
    assertEquals(0, globalScope.getDepth());
    assertFalse(globalScope.isBottom());
    assertNull(globalScope.getParent());

    Scope<Object> functionScope = createScope(Node.newFunctionExpression(null, null), globalScope);
    assertNotNull(functionScope);
    assertEquals(1, functionScope.getDepth());
    assertFalse(functionScope.isBottom());
    assertEquals(globalScope, functionScope.getParent());
  }

  @Test
  public void testDeclareVariable() {
    Scope<Object> globalScope = createScope(Node.rootBlock(), null);
    Scope<Object> functionScope = createScope(Node.newFunctionExpression(null, null), globalScope);

    String variableName = "foo";
    Node variableNameNode = Node.newName("foo");

    Var variable = functionScope.declare(variableName, variableNameNode, ObjectType.UNDEFINED, null);
    assertNotNull(variable);
    assertEquals(variableName, variable.getName());
    assertEquals(variableNameNode, variable.getNameNode());
    assertEquals(functionScope, variable.getScope());
    assertFalse(variable.isGlobal());
    assertFalse(variable.isLocal());
    assertFalse(variable.isExtern());
    assertFalse(variable.isConst());
    assertFalse(variable.isDefine());
    assertNull(variable.getType());
    assertNull(variable.getJSDocInfo());
    assertFalse(variable.isTypeInferred());
    assertEquals("<non-file>", variable.getInputName());

    // Check that the variable is in the function scope
    Var functionScopeVariable = functionScope.getVar(variableName);
    assertNotNull(functionScopeVariable);
    assertEquals(variable, functionScopeVariable);

    // Check that the variable is not in the global scope
    Var globalScopeVariable = globalScope.getVar(variableName);
    assertNull(globalScopeVariable);

    // Declare the same variable again
    Var duplicateVariable = functionScope.declare(variableName, variableNameNode, ObjectType.UNDEFINED, null);
    assertEquals(variable, duplicateVariable);
  }

  @Test
  public void testGetSlot() {
    Scope<Object> globalScope = createScope(Node.rootBlock(), null);
    Scope<Object> functionScope = createScope(Node.newFunctionExpression(null, null), globalScope);

    String variableName = "foo";
    Node variableNameNode = Node.newName("foo");

    Var variable = functionScope.declare(variableName, variableNameNode, ObjectType.UNDEFINED, null);
    assertEquals(variable, functionScope.getSlot(variableName));
  }

  @Test
  public void testUndeclareVariable() {
    Scope<Object> globalScope = createScope(Node.rootBlock(), null);
    Scope<Object> functionScope = createScope(Node.newFunctionExpression(null, null), globalScope);

    String variableName = "foo";
    Node variableNameNode = Node.newName("foo");

    Var variable = functionScope.declare(variableName, variableNameNode, ObjectType.UNDEFINED, null);
    functionScope.undeclare(variable);

    assertNull(functionScope.getVar(variableName));
  }

  @Test
  public void testIsDeclared() {
    Scope<Object> globalScope = createScope(Node.rootBlock(), null);
    Scope<Object> functionScope = createScope(Node.newFunctionExpression(null, null), globalScope);

    String variableName1 = "foo";
    Node variableNameNode1 = Node.newName("foo");
    Var variable1 = functionScope.declare(variableName1, variableNameNode1, ObjectType.UNDEFINED, null);

    String variableName2 = "foo";
    Node variableNameNode2 = Node.newName("foo");
    Var variable2 = functionScope.declare(variableName2, variableNameNode2, ObjectType.UNDEFINED, null);

    assertFalse(functionScope.isDeclared(variableName1, false));
    assertFalse(functionScope.isDeclared(variableName2, false));

    assertFalse(functionScope.isDeclared("bar", false));

    assertTrue(functionScope.isDeclared(variableName1, true));
    assertTrue(functionScope.isDeclared(variableName2, true));

    assertFalse(globalScope.isDeclared(variableName1, false));
    assertFalse(globalScope.isDeclared(variableName2, false));

    assertTrue(globalScope.isDeclared(variableName1, true));
    assertTrue(globalScope.isDeclared(variableName2, true));
  }

  @Test
  public void testGetVars() {
    Scope<Object> globalScope = createScope(Node.rootBlock(), null);
    Scope<Object> functionScope = createScope(Node.newFunctionExpression(null, null), globalScope);

    String variableName1 = "foo";
    Node variableNameNode1 = Node.newName("foo");
    Var variable1 = functionScope.declare(variableName1, variableNameNode1, ObjectType.UNDEFINED, null);

    String variableName2 = "bar";
    Node variableNameNode2 = Node.newName("bar");
    Var variable2 = functionScope.declare(variableName2, variableNameNode2, ObjectType.UNDEFINED, null);

    Map<String, Var> expectedVars = new LinkedHashMap<>();
    expectedVars.put(variableName1, variable1);
    expectedVars.put(variableName2, variable2);

    Iterator<Var> varsIterator = functionScope.getVars().iterator();
    while (varsIterator.hasNext()) {
      Var var = varsIterator.next();
      String varName = var.getName();
      assertEquals(expectedVars.get(varName), var);
      expectedVars.remove(varName);
    }

    assertTrue(expectedVars.isEmpty());
  }

  @Test
  public void testGetVarCount() {
    Scope<Object> globalScope = createScope(Node.rootBlock(), null);
    Scope<Object> functionScope = createScope(Node.newFunctionExpression(null, null), globalScope);

    String variableName1 = "foo";
    Node variableNameNode1 = Node.newName("foo");
    Var variable1 = functionScope.declare(variableName1, variableNameNode1, ObjectType.UNDEFINED, null);

    String variableName2 = "bar";
    Node variableNameNode2 = Node.newName("bar");
    Var variable2 = functionScope.declare(variableName2, variableNameNode2, ObjectType.UNDEFINED, null);

    assertEquals(2, functionScope.getVarCount());
  }

  @Test
  public void testIsGlobal() {
    Scope<Object> globalScope = createScope(Node.rootBlock(), null);
    Scope<Object> functionScope = createScope(Node.newFunctionExpression(null, null), globalScope);

    assertFalse(functionScope.isGlobal());
    assertTrue(globalScope.isGlobal());
  }

  @Test
  public void testIsLocal() {
    Scope<Object> globalScope = createScope(Node.rootBlock(), null);
    Scope<Object> functionScope = createScope(Node.newFunctionExpression(null, null), globalScope);

    assertFalse(functionScope.isLocal());
    assertTrue(globalScope.isLocal());
  }

  @Test
  public void testGetParent() {
    Scope<Object> globalScope = createScope(Node.rootBlock(), null);
    Scope<Object> functionScope = createScope(Node.newFunctionExpression(null, null), globalScope);

    assertEquals(globalScope, functionScope.getParent());
  }
}