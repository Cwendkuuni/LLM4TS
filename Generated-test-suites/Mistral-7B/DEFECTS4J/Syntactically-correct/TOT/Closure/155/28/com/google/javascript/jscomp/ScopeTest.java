import static org.junit.Assert.*;
import static com.google.javascript.jscomp.Scope.Var.*;

import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Scope.CompilerInput;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * ScopeTest class to test the functionality of Scope class.
 */
public class ScopeTest {

  // Test data
  private Node rootNode;
  private Scope scope;
  private CompilerInput input;

  @org.junit.Before
  public void setUp() {
    // Initialize test data
    rootNode = new Node("function foo() { var x = 1; var y = 2; }");
    input = new CompilerInput("TestFile.js", "test");
    scope = new Scope(null, rootNode);
  }

  /**
   * Test declaring a local variable.
   */
  @org.junit.Test
  public void testDeclareLocalVariable() {
    // Declare a local variable "x"
    Var x = scope.declare("x", rootNode.getFirstChild().getNextSibling(),
        JSType.getIntType(), input);

    // Check the variable has been declared successfully
    assertEquals("x", x.getName());
    assertEquals(0, x.getIndex());
    assertEquals(JSType.getIntType(), x.getType());
    assertFalse(x.isDefine());
    assertFalse(x.isExtern());
    assertFalse(x.isConst());
    assertFalse(x.isBleedingFunction());
  }

  /**
   * Test declaring a constant variable.
   */
  @org.junit.Test
  public void testDeclareConstantVariable() {
    // Declare a constant variable "PI"
    Node constantNode = new Node("const PI = 3.14159265358979323846;");
    Var pi = scope.declare("PI", constantNode, JSType.getNumberType(), input);

    // Check the variable has been declared as a constant
    assertEquals("PI", pi.getName());
    assertEquals(0, pi.getIndex());
    assertEquals(JSType.getNumberType(), pi.getType());
    assertTrue(pi.isConst());
    assertFalse(pi.isDefine());
    assertFalse(pi.isExtern());
    assertFalse(pi.isBleedingFunction());
  }

  /**
   * Test declaring a define variable.
   */
  @org.junit.Test
  public void testDeclareDefineVariable() {
    // Declare a define variable "PI"
    Node defineNode = new Node("/** @define PI */");
    Var pi = scope.declare("PI", defineNode, null, input, false);

    // Check the variable has been declared as a define
    assertEquals("PI", pi.getName());
    assertEquals(0, pi.getIndex());
    assertNull(pi.getType());
    assertTrue(pi.isDefine());
    assertFalse(pi.isExtern());
    assertFalse(pi.isConst());
    assertFalse(pi.isBleedingFunction());
  }

  /**
   * Test declaring an external variable.
   */
  @org.junit.Test
  public void testDeclareExternalVariable() {
    // Initialize a new CompilerInput for an external file
    CompilerInput externalInput = new CompilerInput("ExternalFile.js", "test");

    // Declare an external variable "PI"
    Var pi = scope.declare("PI", null, null, externalInput, false);

    // Check the variable has been declared as external
    assertEquals("PI", pi.getName());
    assertEquals(-1, pi.getIndex());
    assertNull(pi.getType());
    assertFalse(pi.isDefine());
    assertTrue(pi.isExtern());
    assertFalse(pi.isConst());
    assertFalse(pi.isBleedingFunction());
  }

  /**
   * Test getting the variable by name.
   */
  @org.junit.Test
  public void testGetVariableByName() {
    // Declare variables "x" and "y"
    Var x = scope.declare("x", rootNode.getFirstChild().getNextSibling(),
        JSType.getIntType(), input);
    Var y = scope.declare("y", rootNode.getLastChild().getPreviousSibling(),
        JSType.getIntType(), input);

    // Get variables by name
    Var xVar = scope.getVar("x");
    Var yVar = scope.getVar("y");

    // Check the variables are the same as declared
    assertEquals(x, xVar);
    assertEquals(y, yVar);
  }

  /**
   * Test getting the non-existent variable.
   */
  @org.junit.Test(expected = NullPointerException.class)
  public void testGetNonExistentVariable() {
    // Get a non-existent variable
    scope.getVar("nonExistentVariable");
  }

  /**
   * Test the join method of two scopes.
   */
  @org.junit.Test
  public void testJoinScopes() {
    // Initialize child and parent scopes
    Scope childScope = new Scope(scope, rootNode.getFirstChild());
    Scope parentScope = new Scope(null, rootNode);

    // Declare variables in child and parent scopes
    Var xChild = childScope.declare("x", rootNode.getFirstChild().getNextSibling(),
        JSType.getIntType(), input);
    Var xParent = parentScope.declare("x", rootNode.getFirstChild().getNextSibling(),
        JSType.getIntType(), input);
    Var yChild = childScope.declare("y", rootNode.getLastChild().getPreviousSibling(),
        JSType.getIntType(), input);
    Var yParent = parentScope.declare("y", rootNode.getLastChild().getPreviousSibling(),
        JSType.getIntType(), input);

    // Join the scopes
    Map<String, Var> childVars = childScope.vars;
    Map<String, Var> parentVars = parentScope.vars;
    Map<String, Var> combinedVars = new LinkedHashMap<>();

    combinedVars.putAll(childVars);
    combinedVars.putAll(parentVars);

    // Create a new Scope instance with the combined variables
    Scope combinedScope = new Scope(null, rootNode);
    combinedScope.vars = combinedVars;

    // Check if the combined scope contains all variables from both child and parent scopes
    Iterator<Var> combinedVarsIterator = combinedScope.getVars();
    Var currentVar;

    while (combinedVarsIterator.hasNext()) {
      currentVar = combinedVarsIterator.next();

      if (currentVar.getName().equals("x")) {
        assertEquals(xChild, currentVar);
      } else if (currentVar.getName().equals("y")) {
        assertEquals(yChild, currentVar);
      }
    }
  }

  /**
   * Test the type inference for a local variable.
   */
  @org.junit.Test
  public void testTypeInferenceForLocalVariable() {
    // Initialize test data
    Node assignmentNode = rootNode.getFirstChild().getNextSibling();
    Node rightHandSide = assignmentNode.getLastChild();

    // Declare variables "x" and "y"
    Var x = scope.declare("x", rootNode.getFirstChild().getNextSibling(), null, input, true);
    Var y = scope.declare("y", rightHandSide, JSType.getNumberType(), input, false);

    // Assign a number value to "x"
    assignmentNode.setType(Token.ASSIGN);
    Node assignValueNode = assignmentNode.getFirstChild();
    assignValueNode.setType(Token.NUMBER);
    assignValueNode.setValue("3.14159265358979323846");

    // Infer the type of "x"
    x.resolveType(null);

    // Check the type of "x"
    assertEquals(JSType.getNumberType(), x.getType());
  }

  /**
   * Test the type inference for a global variable.
   */
  @org.junit.Test
  public void testTypeInferenceForGlobalVariable() {
    // Declare a global variable "PI"
    Var pi = scope.declare("PI", null, null, null, false);

    // Infer the type of "PI"
    pi.resolveType(null);

    // Check the type of "PI"
    assertEquals(null, pi.getType());
  }

  /**
   * Test the type inference for a bleeding function.
   */
  @org.junit.Test
  public void testTypeInferenceForBleedingFunction() {
    // Initialize test data
    Node functionNode = rootNode.getLastChild();

    // Declare a local variable "x"
    Var x = scope.declare("x", rootNode.getFirstChild().getNextSibling(), null, input, true);

    // Declare a bleeding function "foo"
    Node bleedingFunctionNode = new Node("function foo() { var x = 5; }");
    Node bleedingFunctionExpressionNode = bleedingFunctionNode.getLastChild();
    bleedingFunctionExpressionNode.setType(Token.FUNCTION_EXPRESSION);

    // Assign the bleeding function to a local variable "y"
    Var y = scope.declare("y", new Node("var y = foo;"), null, input, true);

    // Infer the type of "y"
    y.resolveType(null);

    // Check the type of "y"
    FunctionType functionType = (FunctionType) y.getType();
    ObjectType functionThisType = functionType.getTypeOfThis();
    assertEquals(scope.thisType, functionThisType);
  }
}