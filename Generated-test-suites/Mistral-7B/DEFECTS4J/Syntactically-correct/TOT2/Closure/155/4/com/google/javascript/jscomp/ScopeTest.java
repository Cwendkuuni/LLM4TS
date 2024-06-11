import static org.junit.Assert.*;
import static com.google.javascript.rhino.NodeFactory.*;
import static com.google.javascript.rhino.JSDocInfo.*;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.rhino.CompilerInput;
import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.JSDocInfo;

import java.util.LinkedHashMap;
import java.util.Map;

public class ScopeTest {

  private static ErrorReporter errorReporter = new ErrorReporter() {};

  @org.junit.Test
  public void testScopeCreation() {
    // Test creating a new scope with a parent and root node
    Node rootNode = newFunction(IDENTIFIER, "func", newArray(newName("arg")), newBlock());
    Scope scope = new Scope(new Scope(null, rootNode), rootNode);

    // Test creating a new global scope
    Node globalRootNode = newBlock();
    Scope globalScope = new Scope(globalRootNode, null);

    // Test creating an empty scope
    Node emptyRootNode = newFunction(IDENTIFIER, "func", newArray(), newBlock());
    Scope emptyScope = new Scope(emptyRootNode, null);

    // Test scope depth
    assertEquals(1, scope.getDepth());
    assertEquals(0, globalScope.getDepth());
    assertEquals(0, emptyScope.getDepth());
  }

  @org.junit.Test
  public void testDeclareVariable() {
    // Test declaring a variable with a name node and JSDocInfo
    Node rootNode = newFunction(IDENTIFIER, "func", newArray(newName("arg")), newBlock());
    Scope scope = new Scope(new Scope(null, rootNode), rootNode);

    String variableName = "name";
    Node variableNameNode = newName(variableName);
    JSType variableType = INT_TYPE;
    CompilerInput input = new CompilerInput("input.js", null);
    Var variable = scope.declare(variableName, variableNameNode, variableType, input);

    // Test getting the variable
    Var declaredVariable = scope.getVar(variableName);
    assertEquals(variable, declaredVariable);

    // Test getting the variable type
    assertEquals(variableType, declaredVariable.getType());
  }

  @org.junit.Test
  public void testDeclareVariableWithJSDocInfo() {
    // Test declaring a variable with a name node, JSDocInfo, and inferred type
    Node rootNode = newFunction(IDENTIFIER, "func", newArray(newName("arg")), newBlock());
    Scope scope = new Scope(new Scope(null, rootNode), rootNode);

    String variableName = "name";
    Node variableNameNode = newName(variableName);
    JSType variableType = INT_TYPE;
    JSDocInfo variableDocInfo = new JSDocInfo(new JSDocAnnotation("@param", variableName, " Description."));
    Var variable = scope.declare(variableName, variableNameNode, variableType, null, false, variableDocInfo);

    // Test getting the variable JSDocInfo
    assertEquals(variableDocInfo, variable.getJSDocInfo());
  }

  @org.junit.Test
  public void testDeclareVariableWithExternType() {
    // Test declaring a variable with a name node and input
    Node rootNode = newFunction(IDENTIFIER, "func", newArray(newName("arg")), newBlock());
    Scope scope = new Scope(new Scope(null, rootNode), rootNode);

    String variableName = "name";
    Node variableNameNode = newName(variableName);
    CompilerInput input = new CompilerInput("input.js", null);
    input.setExtern(true);
    Var variable = scope.declare(variableName, variableNameNode, null, input, false, null);

    // Test getting the variable type
    assertTrue(variable.isExtern());
  }

  @org.junit.Test
  public void testDeclareVariableWithInputAndJSDocInfo() {
    // Test declaring a variable with a name node, input, and JSDocInfo
    Node rootNode = newFunction(IDENTIFIER, "func", newArray(newName("arg")), newBlock());
    Scope scope = new Scope(new Scope(null, rootNode), rootNode);

    String variableName = "name";
    Node variableNameNode = newName(variableName);
    CompilerInput input = new CompilerInput("input.js", "description");
    Var variable = scope.declare(variableName, variableNameNode, INT_TYPE, input, false, null);

    // Test getting the variable JSDocInfo
    assertEquals("description", variable.getJSDocInfo().getDescription());
  }

  @org.junit.Test
  public void testUndeclareVariable() {
    // Test undeclaring a variable
    Node rootNode = newFunction(IDENTIFIER, "func", newArray(), newBlock());
    Scope scope = new Scope(new Scope(null, rootNode), rootNode);

    String variableName = "name";
    Node variableNameNode = newName(variableName);
    JSType variableType = INT_TYPE;
    Var variable = scope.declare(variableName, variableNameNode, variableType, null);
    scope.undeclare(variable);

    // Test getting the variable
    assertNull(scope.getVar(variableName));
  }

  @org.junit.Test
  public void testIsDeclared() {
    // Test checking if a variable is declared
    Node rootNode = newFunction(IDENTIFIER, "func", newArray(newName("arg")), newBlock());
    Scope scope = new Scope(new Scope(null, rootNode), rootNode);

    String variableName1 = "name1";
    Node variableNameNode1 = newName(variableName1);
    Var variable1 = scope.declare(variableName1, variableNameNode1, INT_TYPE, null);

    String variableName2 = "name2";
    Node variableNameNode2 = newName(variableName2);

    assertTrue(scope.isDeclared(variableName1, true));
    assertTrue(scope.isDeclared(variableName1, false));
    assertFalse(scope.isDeclared(variableName2, true));
    assertFalse(scope.isDeclared(variableName2, false));
  }

  @org.junit.Test
  public void testGetVars() {
    // Test getting all variables in the scope
    Node rootNode = newFunction(IDENTIFIER, "func", newArray(newName("arg")), newBlock());
    Scope scope = new Scope(new Scope(null, rootNode), rootNode);

    String variableName1 = "name1";
    Node variableNameNode1 = newName(variableName1);
    Var variable1 = scope.declare(variableName1, variableNameNode1, INT_TYPE, null);

    String variableName2 = "name2";
    Node variableNameNode2 = newName(variableName2);
    Var variable2 = scope.declare(variableName2, variableNameNode2, STRING_TYPE, null);

    Iterator<Var> vars = scope.getVars();

    assertEquals(2, vars.size());
    assertTrue(vars.hasNext());
    Var firstVariable = vars.next();
    assertEquals(variable1, firstVariable);
    assertTrue(vars.hasNext());
    Var secondVariable = vars.next();
    assertEquals(variable2, secondVariable);
  }

  @org.junit.Test
  public void testGetVarCount() {
    // Test getting the number of variables in the scope
    Node rootNode = newFunction(IDENTIFIER, "func", newArray(newName("arg")), newBlock());
    Scope scope = new Scope(new Scope(null, rootNode), rootNode);

    String variableName1 = "name1";
    Node variableNameNode1 = newName(variableName1);
    Var variable1 = scope.declare(variableName1, variableNameNode1, INT_TYPE, null);

    String variableName2 = "name2";
    Node variableNameNode2 = newName(variableName2);
    Var variable2 = scope.declare(variableName2, variableNameNode2, STRING_TYPE, null);

    assertEquals(2, scope.getVarCount());
  }

  @org.junit.Test
  public void testGetParentScope() {
    // Test getting the parent scope
    Node rootNode = newFunction(IDENTIFIER, "func", newArray(newName("arg")), newBlock());
    Scope parentScope = new Scope(null, rootNode);
    Scope childScope = new Scope(parentScope, rootNode);

    assertEquals(parentScope, childScope.getParentScope());
    assertNull(parentScope.getParentScope());
  }

  @org.junit.Test
  public void testGetTypeOfThis() {
    // Test getting the type of this in the current scope
    Node rootNode = newFunction(IDENTIFIER, "func", newArray(newName("arg")), newBlock());
    Scope scope = new Scope(new Scope(null, rootNode), rootNode);

    ObjectType thisType = INT_OBJECT_TYPE;
    scope.setTypeOfThis(thisType);

    assertEquals(thisType, scope.getTypeOfThis());
  }
}