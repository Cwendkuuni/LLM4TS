import static org.junit.Assert.*;
import static com.google.javascript.rhino.NodeFactory.makeFunction;
import static com.google.javascript.rhino.NodeFactory.makeIdentifier;
import static com.google.javascript.rhino.NodeFactory.makeNumberLiteral;
import static com.google.javascript.rhino.NodeFactory.makeProperty;
import static com.google.javascript.rhino.NodeFactory.makeStringLiteral;
import static com.google.javascript.rhino.NodeFactory.makeThis;
import static com.google.javascript.rhino.NodeFactory.makeVar;
import static com.google.javascript.rhino.token.TokenType.*;

import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Scope.Var;

import org.junit.Before;
import org.junit.Test;

public class ScopeTest {
  private Scope scope;
  private CompilerInput input;
  private ErrorReporter errorReporter;

  @Before
  public void setUp() {
    errorReporter = new ErrorReporter();
    input = new CompilerInput("test.js", null, null);
    scope = new Scope(null, makeFunction(makeIdentifier("test"), new ObjectType(makeThis()), null));
  }

  /**
   * Test constructor with no parameters
   */
  @Test
  public void testScopeConstructorNoParams() {
    Scope globalScope = new Scope(null, null);
    assertNotNull(globalScope);
    assertEquals(0, globalScope.getDepth());
    assertFalse(globalScope.isBottom());
  }

  /**
   * Test constructor with root node
   */
  @Test
  public void testScopeConstructorWithRootNode() {
    Scope scope = new Scope(makeFunction(makeIdentifier("test"), new ObjectType(makeThis()), null), errorReporter);
    assertNotNull(scope);
    assertEquals(1, scope.getDepth());
    assertFalse(scope.isBottom());
  }

  /**
   * Test constructor with bottom
   */
  @Test
  public void testScopeConstructorWithBottom() {
    Scope scope = new Scope(makeFunction(MAKE_FUNCTION, null, null), new ObjectType(makeThis()));
    assertNotNull(scope);
    assertEquals(0, scope.getDepth());
    assertTrue(scope.isBottom());
  }

  /**
   * Test declare method
   */
  @Test
  public void testDeclareMethod() {
    String variableName = "testVariable";
    Node variableNameNode = makeIdentifier(variableName);
    JSType variableType = new FunctionType(makeThis(), new ObjectType(makeThis()), new ObjectType(makeThis()));

    Var testVariable = scope.declare(variableName, variableNameNode, variableType, input);
    assertNotNull(testVariable);
    assertEquals(variableName, testVariable.getName());
    assertEquals(variableType, testVariable.getType());
  }

  /**
   * Test getVar method
   */
  @Test
  public void testGetVarMethod() {
    String variableName = "testVariable";
    Node variableNameNode = makeIdentifier(variableName);
    JSType variableType = new FunctionType(makeThis(), new ObjectType(makeThis()), new ObjectType(makeThis()));

    Var testVariable = scope.declare(variableName, variableNameNode, variableType, input);

    Var testVariableFromGetVar = scope.getVar(variableName);
    assertEquals(testVariable, testVariableFromGetVar);
  }

  /**
   * Test getVar method with undefined variable
   */
  @Test
  public void testGetVarMethodUndefinedVariable() {
    String variableName = "testVariable";

    Var testVariableFromGetVar = scope.getVar(variableName);
    assertNull(testVariableFromGetVar);
  }

  /**
   * Test parent scope
   */
  @Test
  public void testParentScope() {
    String parentVariableName = "parentVariable";
    Node parentVariableNameNode = makeIdentifier(parentVariableName);
    JSType parentVariableType = new FunctionType(makeThis(), new ObjectType(makeThis()), new ObjectType(makeThis()));

    scope.declare(parentVariableName, parentVariableNameNode, parentVariableType, input);

    String variableName = "testVariable";
    Node variableNameNode = makeIdentifier(variableName);
    JSType variableType = new FunctionType(makeThis(), new ObjectType(makeThis()), new ObjectType(makeThis()));

    Var testVariable = scope.declare(variableName, variableNameNode, variableType, input);

    assertEquals(scope.getParent(), testVariable.getScope());
  }

  /**
   * Test isDeclared method
   */
  @Test
  public void testIsDeclaredMethod() {
    String variableName = "testVariable";
    Node variableNameNode = makeIdentifier(variableName);
    JSType variableType = new FunctionType(makeThis(), new ObjectType(makeThis()), new ObjectType(makeThis()));

    scope.declare(variableName, variableNameNode, variableType, input);

    assertTrue(scope.isDeclared(variableName, false));
  }

  /**
   * Test isDeclared method with recursive parent scope
   */
  @Test
  public void testIsDeclaredMethodWithRecursiveParentScope() {
    String parentVariableName = "parentVariable";
    Node parentVariableNameNode = makeIdentifier(parentVariableName);
    JSType parentVariableType = new FunctionType(makeThis(), new ObjectType(makeThis()), new ObjectType(makeThis()));

    Scope parentScope = new Scope(null, null);
    parentScope.declare(parentVariableName, parentVariableNameNode, parentVariableType, input);

    String variableName = "testVariable";
    Node variableNameNode = makeIdentifier(variableName);
    JSType variableType = new FunctionType(makeThis(), new ObjectType(makeThis()), new ObjectType(makeThis()));

    scope.declare(variableName, variableNameNode, variableType, input, parentScope);

    assertTrue(scope.isDeclared(variableName, true));
  }

  /**
   * Test isDeclared method with undefined variable in parent scope
   */
  @Test
  public void testIsDeclaredMethodWithUndefinedVariableInParentScope() {
    String parentVariableName = "parentVariable";

    Scope parentScope = new Scope(null, null);

    String variableName = "testVariable";
    Node variableNameNode = makeIdentifier(variableName);
    JSType variableType = new FunctionType(makeThis(), new ObjectType(makeThis()), new ObjectType(makeThis()));

    scope.declare(variableName, variableNameNode, variableType, input, parentScope);

    assertFalse(scope.isDeclared(parentVariableName, false));
  }

  /**
   * Test getVars method
   */
  @Test
  public void testGetVarsMethod() {
    String variableName1 = "testVariable1";
    Node variableNameNode1 = makeIdentifier(variableName1);
    JSType variableType1 = new FunctionType(makeThis(), new ObjectType(makeThis()), new ObjectType(makeThis()));

    String variableName2 = "testVariable2";
    Node variableNameNode2 = makeIdentifier(variableName2);
    JSType variableType2 = new FunctionType(makeThis(), new ObjectType(makeThis()), new ObjectType(makeThis()));

    scope.declare(variableName1, variableNameNode1, variableType1, input);
    scope.declare(variableName2, variableNameNode2, variableType2, input);

    Iterator<Var> variables = scope.getVars();

    Var firstVariable = variables.next();
    Var secondVariable = variables.next();

    assertEquals(variableName1, firstVariable.getName());
    assertEquals(variableName2, secondVariable.getName());
  }

  /**
   * Test getVarCount method
   */
  @Test
  public void testGetVarCountMethod() {
    String variableName1 = "testVariable1";
    Node variableNameNode1 = makeIdentifier(variableName1);
    JSType variableType1 = new FunctionType(makeThis(), new ObjectType(makeThis()), new ObjectType(makeThis()));

    String variableName2 = "testVariable2";
    Node variableNameNode2 = makeIdentifier(variableName2);
    JSType variableType2 = new FunctionType(makeThis(), new ObjectType(makeThis()), new ObjectType(makeThis()));

    scope.declare(variableName1, variableNameNode1, variableType1, input);
    scope.declare(variableName2, variableNameNode2, variableType2, input);

    assertEquals(2, scope.getVarCount());
  }

  /**
   * Test getSlot method
   */
  @Test
  public void testGetSlotMethod() {
    String variableName = "testVariable";
    Node variableNameNode = makeIdentifier(variableName);
    JSType variableType = new FunctionType(makeThis(), new ObjectType(makeThis()), new ObjectType(makeThis()));

    Var testVariable = scope.declare(variableName, variableNameNode, variableType, input);

    assertEquals(testVariable, scope.getSlot(variableName));
  }

  /**
   * Test getSlot method with undefined variable
   */
  @Test
  public void testGetSlotMethodUndefinedVariable() {
    String variableName = "testVariable";

    assertEquals(null, scope.getSlot(variableName));
  }
}