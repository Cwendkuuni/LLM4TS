import static org.junit.Assert.*;
import static com.google.common.truth.Truth.*;

import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Scope.Var;

import java.util.Map;

public class ScopeTest {

  @Test
  public void testScopeCreation() {
    Scope parentScope = new Scope(null, new Node(Token.BLOCK, null));
    Scope childScope = new Scope(parentScope, new Node(Token.FUNCTION, null));

    assertEquals(0, parentScope.getDepth());
    assertEquals(1, childScope.getDepth());
    assertEquals(parentScope, childScope.getParent());
  }

  @Test
  public void testGlobalScopeCreation() {
    Scope globalScope = new Scope(new Node(Token.SCRIPT, null), null);

    assertEquals(0, globalScope.getDepth());
    assertNull(globalScope.getParent());
  }

  @Test
  public void testBottomScopeCreation() {
    Scope bottomScope = new Scope(new Node(Token.FUNCTION, null), null);

    assertTrue(bottomScope.isBottom());
  }

  @Test
  public void testDeclareVariable() {
    Scope parentScope = new Scope(null, new Node(Token.BLOCK, null));
    Scope childScope = new Scope(parentScope, new Node(Token.FUNCTION, null));

    String variableName = "testVariable";
    Var variable = childScope.declare(variableName, null, JSType.getUnknownType(), null);

    assertEquals(variableName, variable.getName());
    assertEquals(childScope, variable.getScope());
  }

  @Test
  public void testGetVariableByScopeAndName() {
    Scope parentScope = new Scope(null, new Node(Token.BLOCK, null));
    Scope childScope = new Scope(parentScope, new Node(Token.FUNCTION, null));

    String variableName = "testVariable";
    Var variable = childScope.declare(variableName, null, JSType.getUnknownType(), null);

    Var resultVariable = childScope.getVar(variableName);

    assertEquals(variable, resultVariable);
  }

  @Test
  public void testGetVariableByScopeAndName_NullResult() {
    Scope parentScope = new Scope(null, new Node(Token.BLOCK, null));
    Scope childScope = new Scope(parentScope, new Node(Token.FUNCTION, null));

    Var variable = childScope.declare("nonExistentVariable", null, JSType.getUnknownType(), null);

    assertNull(childScope.getVar("nonExistentVariable"));
  }

  @Test
  public void testGetVariableByScopeAndName_ParentScope() {
    Scope parentScope = new Scope(null, new Node(Token.BLOCK, null));
    Scope childScope = new Scope(parentScope, new Node(Token.FUNCTION, null));

    String variableName = "testVariable";
    Var variable = childScope.declare(variableName, null, JSType.getUnknownType(), null);

    assertNull(parentScope.getVar(variableName));
  }

  @Test
  public void testIsDeclared() {
    Scope parentScope = new Scope(null, new Node(Token.BLOCK, null));
    Scope childScope = new Scope(parentScope, new Node(Token.FUNCTION, null));

    String variableName = "testVariable";
    Var variable = childScope.declare(variableName, null, JSType.getUnknownType(), null);

    assertTrue(childScope.isDeclared(variableName, true));
    assertFalse(parentScope.isDeclared(variableName, true));
    assertFalse(parentScope.isDeclared(variableName, false));
  }

  @Test
  public void testGetVarCount() {
    Scope parentScope = new Scope(null, new Node(Token.BLOCK, null));
    Scope childScope = new Scope(parentScope, new Node(Token.FUNCTION, null));

    String variable1Name = "testVariable1";
    String variable2Name = "testVariable2";

    childScope.declare(variable1Name, null, JSType.getUnknownType(), null);
    childScope.declare(variable2Name, null, JSType.getUnknownType(), null);

    assertEquals(2, childScope.getVarCount());
    assertEquals(0, parentScope.getVarCount());
  }

  @Test
  public void testGetIteratorOverVariables() {
    Scope parentScope = new Scope(null, new Node(Token.BLOCK, null));
    Scope childScope = new Scope(parentScope, new Node(Token.FUNCTION, null));

    String variable1Name = "testVariable1";
    String variable2Name = "testVariable2";

    childScope.declare(variable1Name, null, JSType.getUnknownType(), null);
    childScope.declare(variable2Name, null, JSType.getUnknownType(), null);

    Iterator<Var> variablesIterator = childScope.getVars();

    Var firstVariable = variablesIterator.next();
    Var secondVariable = variablesIterator.next();

    assertEquals(variable1Name, firstVariable.getName());
    assertEquals(variable2Name, secondVariable.getName());
  }

  @Test
  public void testIsLocalScope() {
    Scope parentScope = new Scope(null, new Node(Token.BLOCK, null));
    Scope childScope = new Scope(parentScope, new Node(Token.FUNCTION, null));

    assertTrue(childScope.isLocal());
    assertFalse(parentScope.isLocal());
  }

  @Test
  public void testIsGlobalScope() {
    Scope globalScope = new Scope(new Node(Token.SCRIPT, null), null);

    assertTrue(globalScope.isGlobal());
  }
}