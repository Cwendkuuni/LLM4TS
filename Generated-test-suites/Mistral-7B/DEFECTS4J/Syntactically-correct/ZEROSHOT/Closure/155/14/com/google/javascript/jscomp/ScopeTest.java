import static org.junit.Assert.*;
import static com.google.javascript.jscomp.Scope.*;

import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.Test;

public class ScopeTest {

  @Test
  public void testScopeCreation() {
    // Test creation of a local scope
    Node rootNode = new Node(Token.FUNCTION, null, null, null);
    Scope localScope = new Scope(null, rootNode);
    assertEquals(1, localScope.getDepth());
    assertFalse(localScope.isBottom());

    // Test creation of a global scope
    Node globalRootNode = new Node(Token.BLOCK, null, null, null);
    Scope globalScope = new Scope(globalRootNode, null);
    assertEquals(0, globalScope.getDepth());
    assertFalse(globalScope.isBottom());

    // Test creation of a bottom scope
    Node bottomRootNode = new Node(Token.FUNCTION, null, null, null);
    Scope bottomScope = new Scope(bottomRootNode, null);
    assertEquals(0, bottomScope.getDepth());
    assertTrue(bottomScope.isBottom());
  }

  @Test
  public void testScopeDeclaration() {
    Node rootNode = new Node(Token.FUNCTION, null, null, null);
    Scope parentScope = new Scope(null, rootNode);
    Scope localScope = new Scope(parentScope, rootNode);

    String variableName = "testVariable";
    Var declaredVariable = localScope.declare(variableName, null, JSType.INT, null);
    assertNotNull(declaredVariable);
    assertEquals(variableName, declaredVariable.getName());
    assertEquals(localScope, declaredVariable.getScope());
    assertEquals(0, declaredVariable.getIndex());
  }

  @Test
  public void testScopeUndelearation() {
    Node rootNode = new Node(Token.FUNCTION, null, null, null);
    Scope parentScope = new Scope(null, rootNode);
    Scope localScope = new Scope(parentScope, rootNode);

    String variableName = "testVariable";
    Var declaredVariable = localScope.declare(variableName, null, JSType.INT, null);
    localScope.undeclare(declaredVariable);
    assertNull(localScope.getVar(variableName));
  }

  @Test
  public void testScopeGetVar() {
    Node rootNode = new Node(Token.FUNCTION, null, null, null);
    Scope parentScope = new Scope(null, rootNode);
    Scope localScope = new Scope(parentScope, rootNode);

    String variableName1 = "testVariable1";
    Var variable1 = localScope.declare(variableName1, null, JSType.INT, null);

    String variableName2 = "testVariable2";
    Var variable2 = localScope.declare(variableName2, null, JSType.STRING, null);

    assertEquals(variable1, localScope.getVar(variableName1));
    assertEquals(variable2, localScope.getVar(variableName2));
    assertNull(localScope.getVar("nonexistentVariable"));
  }

  @Test
  public void testScopeIsDeclared() {
    Node rootNode = new Node(Token.FUNCTION, null, null, null);
    Scope parentScope = new Scope(null, rootNode);
    Scope localScope = new Scope(parentScope, rootNode);

    String variableName1 = "testVariable1";
    Var variable1 = localScope.declare(variableName1, null, JSType.INT, null);

    String variableName2 = "testVariable2";
    Var variable2 = localScope.declare(variableName2, null, JSType.STRING, null);

    assertTrue(localScope.isDeclared(variableName1, false));
    assertTrue(localScope.isDeclared(variableName2, false));
    assertFalse(localScope.isDeclared("nonexistentVariable", false));

    assertTrue(localScope.isDeclared(variableName1, true));
    assertTrue(localScope.isDeclared(variableName2, true));
    assertFalse(localScope.isDeclared("nonexistentVariable", true));

    assertTrue(parentScope.isDeclared(variableName1, false));
    assertFalse(parentScope.isDeclared(variableName2, false));
    assertFalse(parentScope.isDeclared("nonexistentVariable", false));
  }

  @Test
  public void testScopeGetVars() {
    Node rootNode = new Node(Token.FUNCTION, null, null, null);
    Scope parentScope = new Scope(null, rootNode);
    Scope localScope = new Scope(parentScope, rootNode);

    String variableName1 = "testVariable1";
    Var variable1 = localScope.declare(variableName1, null, JSType.INT, null);

    String variableName2 = "testVariable2";
    Var variable2 = localScope.declare(variableName2, null, JSType.STRING, null);

    LinkedHashMap<String, Var> expectedVars = new LinkedHashMap<>();
    expectedVars.put(variableName1, variable1);
    expectedVars.put(variableName2, variable2);

    assertEquals(expectedVars, localScope.getVars());
  }

  @Test
  public void testScopeGetVarCount() {
    Node rootNode = new Node(Token.FUNCTION, null, null, null);
    Scope parentScope = new Scope(null, rootNode);
    Scope localScope = new Scope(parentScope, rootNode);

    String variableName1 = "testVariable1";
    Var variable1 = localScope.declare(variableName1, null, JSType.INT, null);

    String variableName2 = "testVariable2";
    Var variable2 = localScope.declare(variableName2, null, JSType.STRING, null);

    assertEquals(2, localScope.getVarCount());
  }

  @Test
  public void testScopeIsGlobal() {
    Node rootNode = new Node(Token.FUNCTION, null, null, null);
    Scope localScope = new Scope(null, rootNode);
    assertFalse(localScope.isGlobal());

    Node globalRootNode = new Node(Token.BLOCK, null, null, null);
    Scope globalScope = new Scope(globalRootNode, null);
    assertTrue(globalScope.isGlobal());
  }

  @Test
  public void testScopeIsLocal() {
    Node rootNode = new Node(Token.FUNCTION, null, null, null);
    Scope localScope = new Scope(null, rootNode);
    assertFalse(localScope.isLocal());

    Node globalRootNode = new Node(Token.BLOCK, null, null, null);
    Scope globalScope = new Scope(globalRootNode, null);
    assertTrue(globalScope.isLocal());
  }
}