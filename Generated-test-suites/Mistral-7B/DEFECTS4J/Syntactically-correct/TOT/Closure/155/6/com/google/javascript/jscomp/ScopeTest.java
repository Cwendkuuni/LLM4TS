import static org.junit.Assert.*;
import static com.google.javascript.jscomp.Scope.Var.*;

import com.google.common.collect.Maps;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.JSTypeNative;
import com.google.javascript.rhino.NodeTraversal;
import com.google.javascript.rhino.Token;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Tests for {@link Scope} class.
 */
public class ScopeTest {
  private static ErrorReporter errorReporter;

  // Helper method to create a nodes for testing
  private static Node createNode(int type, Node parent) {
    Node node = new Node();
    node.setParent(parent);
    node.setType(type);
    return node;
  }

  /**
   * Make test environment.
   */
  @BeforeClass
  public static void setUpClass() {
    errorReporter = new ErrorReporter();
  }

  /**
   * Test creating a local scope.
   */
  @Test
  public void testLocalScope() {
    // Create root node
    Node rootNode = createNode(Token.BLOCK, null);

    // Create parent scope
    Scope parentScope = new Scope(null, rootNode);

    // Create local scope
    Scope localScope = new Scope(parentScope, rootNode);

    // Create variable
    String variableName = "testVariable";
    Node variableNode = createNode(Token.IDENT, rootNode);
    JSType variableType = JSType.getUndefinedType();
    CompilerInput input = new CompilerInput("test.js", 1, 1);
    Var variable = localScope.declare(variableName, variableNode, variableType, input);

    // Check variable exists in local scope
    assertEquals("Variable should be in local scope", localScope.getVar(variableName), variable);

    // Check variable does not exist in parent scope
    assertNull("Variable should not be in parent scope", parentScope.getVar(variableName));
  }

  /**
   * Test creating a global scope.
   */
  @Test
  public void testGlobalScope() {
    // Create root node
    Node rootNode = createNode(Token.BLOCK, null);

    // Create global scope
    Scope globalScope = new Scope(rootNode, null, null);

    // Create variable
    String variableName = "testVariable";
    Node variableNode = createNode(Token.IDENT, rootNode);
    JSType variableType = JSType.getUndefinedType();
    CompilerInput input = new CompilerInput("test.js", 1, 1);
    Var variable = globalScope.declare(variableName, variableNode, variableType, input);

    // Check variable exists in global scope
    assertEquals("Variable should be in global scope", globalScope.getVar(variableName), variable);

    // Check variable exists in local scope (test recursion)
    Scope localScope = new Scope(globalScope, null);
    assertEquals("Variable should be in local scope (recursion)", localScope.getVar(variableName), variable);
  }

  /**
   * Test creating a bottom scope.
   */
  @Test
  public void testBottomScope() {
    // Create root node
    Node rootNode = createNode(Token.BLOCK, null);

    // Create bottom scope
    Scope bottomScope = new Scope(rootNode, null, null);

    // Check variable does not exist in bottom scope
    assertNull("Variable should not be in bottom scope", bottomScope.getVar("testVariable"));
  }

  /**
   * Test getTypeOfThis method.
   */
  @Test
  public void testgetTypeOfThis() {
    // Create root node
    Node rootNode = createNode(Token.FUNCTION, null);

    // Create parent scope
    Scope parentScope = new Scope(null, rootNode);
    ObjectType parentScopeThisType = new ObjectType(new FunctionType(null, null, null));
    parentScope.thisType = parentScopeThisType;

    // Create local scope
    Scope localScope = new Scope(parentScope, rootNode);

    // Check getTypeOfThis returns correct this type for parent scope
    assertSame("Parent scope this type should match", parentScopeThisType, localScope.getTypeOfThis());

    // Create function node with "this" keyword
    Node functionNode = createNode(Token.FUNCTION, rootNode);
    functionNode.setParameterNames(new String[]{"this"});

    // Create child scope
    Scope childScope = new Scope(localScope, functionNode);

    // Check getTypeOfThis returns correct this type for child scope
    ObjectType childScopeThisType = new ObjectType(new FunctionType(null, new String[]{"this"}, null));
    assertSame("Child scope this type should match", childScopeThisType, childScope.getTypeOfThis());
  }

  /**
   * Test declare method with inferred type.
   */
  @Test
  public void testDeclareWithInferredType() {
    // Create root node
    Node rootNode = createNode(Token.BLOCK, null);

    // Create parent scope
    Scope parentScope = new Scope(null, rootNode);

    // Create local scope
    Scope localScope = new Scope(parentScope, rootNode);

    // Create variable with inferred type
    String variableName = "testVariable";
    Node variableNode = createNode(Token.IDENT, rootNode);
    JSType variableType = JSType.getUndefinedType();
    CompilerInput input = new CompilerInput("test.js", 1, 1);
    Var variable = localScope.declare(variableName, variableNode, variableType, input, true);

    // Check variable exists in local scope
    assertEquals("Variable should be in local scope", localScope.getVar(variableName), variable);

    // Check variable type is inferred
    assertFalse("Variable type should be inferred", variable.isTypeInferred());

    // Assign value to variable
    JSType assignedValueType = JSType.getBooleanType();
    Node assignedValueNode = createNode(Token.BOOL, rootNode);
    localScope.setType(variable.getName(), assignedValueType);

    // Check variable type is resolved
    assertTrue("Variable type should be resolved", variable.isTypeInferred());
    assertEquals("Variable type should match", assignedValueType, variable.getType());
  }

  /**
   * Test declare method with declared type.
   */
  @Test
  public void testDeclareWithDeclaredType() {
    // Create root node
    Node rootNode = createNode(Token.BLOCK, null);

    // Create parent scope
    Scope parentScope = new Scope(null, rootNode);

    // Create local scope
    Scope localScope = new Scope(parentScope, rootNode);

    // Create variable with declared type
    String variableName = "testVariable";
    Node variableNode = createNode(Token.IDENT, rootNode);
    JSType variableType = JSType.getBooleanType();
    CompilerInput input = new CompilerInput("test.js", 1, 1);
    Var variable = localScope.declare(variableName, variableNode, variableType, input, false);

    // Check variable exists in local scope
    assertEquals("Variable should be in local scope", localScope.getVar(variableName), variable);

    // Check variable type is declared
    assertFalse("Variable type should not be inferred", variable.isTypeInferred());

    // Check variable type matches declared type
    assertEquals("Variable type should match", variableType, variable.getType());
  }

  /**
   * Test getSlot method.
   */
  @Test
  public void testGetSlot() {
    // Create root node
    Node rootNode = createNode(Token.BLOCK, null);

    // Create parent scope
    Scope parentScope = new Scope(null, rootNode);

    // Create local scope
    Scope localScope = new Scope(parentScope, rootNode);

    // Create variable
    String variableName = "testVariable";
    Node variableNode = createNode(Token.IDENT, rootNode);
    JSType variableType = JSType.getBooleanType();
    CompilerInput input = new CompilerInput("test.js", 1, 1);
    Var variable = localScope.declare(variableName, variableNode, variableType, input, false);

    // Check getting slot by name works
    StaticSlot<JSType> slot = localScope.getSlot(variableName);
    assertEquals("Slot should be the same as variable", variable, slot.getSlotValue());

    // Check getting slot by name of non-existent variable throws an exception
    try {
      localScope.getSlot("nonExistentVariable");
      fail("Should have thrown an exception");
    } catch (NullPointerException e) {
      // Expected exception
    }

    // Check getting slot by name of variable in parent scope works
    Variable parentVariable = parentScope.declare("parentVariable", createNode(Token.IDENT, rootNode), JSType.getNumberType(), null, false);
    StaticSlot<JSType> parentSlot = parentScope.getSlot("parentVariable");
    assertEquals("Slot should be the same as variable", parentVariable, parentSlot.getSlotValue());
  }

  /**
   * Test getOwnSlot method.
   */
  @Test
  public void testGetOwnSlot() {
    // Create root node
    Node rootNode = createNode(Token.BLOCK, null);

    // Create parent scope
    Scope parentScope = new Scope(null, rootNode);

    // Create local scope
    Scope localScope = new Scope(parentScope, rootNode);

    // Create variable
    String variableName = "testVariable";
    Node variableNode = createNode(Token.IDENT, rootNode);
    JSType variableType = JSType.getBooleanType();
    CompilerInput input = new CompilerInput("test.js", 1, 1);
    Var variable = localScope.declare(variableName, variableNode, variableType, input, false);

    // Check getting own slot by name works
    StaticSlot<JSType> slot = localScope.getOwnSlot(variableName);
    assertEquals("Slot should be the same as variable", variable, slot.getSlotValue());

    // Check getting own slot by name of non-existent variable returns null
    StaticSlot<JSType> nullSlot = localScope.getOwnSlot("nonExistentVariable");
    assertNull("Should be null", nullSlot);

    // Check getting own slot by name of variable in parent scope returns null
    Variable parentVariable = parentScope.declare("parentVariable", createNode(Token.IDENT, rootNode), JSType.getNumberType(), null, false);
    StaticSlot<JSType> parentNullSlot = localScope.getOwnSlot("parentVariable");
    assertNull("Should be null", parentNullSlot);
  }

  /**
   * Test isDeclared method.
   */
  @Test
  public void testIsDeclared() {
    // Create root node
    Node rootNode = createNode(Token.BLOCK, null);

    // Create parent scope
    Scope parentScope = new Scope(null, rootNode);

    // Create local scope
    Scope localScope = new Scope(parentScope, rootNode);

    // Create variable
    String variableName = "testVariable";
    Node variableNode = createNode(Token.IDENT, rootNode);
    JSType variableType = JSType.getBooleanType();
    CompilerInput input = new CompilerInput("test.js", 1, 1);
    Var variable = localScope.declare(variableName, variableNode, variableType, input, false);

    // Check variable is declared in local scope
    assertTrue("Variable should be declared in local scope", localScope.isDeclared(variableName, true));

    // Check variable is not declared in parent scope
    assertFalse("Variable should not be declared in parent scope", parentScope.isDeclared(variableName, true));

    // Check variable is declared in local scope even if parent scope has variable with the same name
    Variable parentVariable = parentScope.declare("testVariable", createNode(Token.IDENT, rootNode), JSType.getNumberType(), null, false);
    assertTrue("Variable should be declared in local scope even if parent scope has the same variable", localScope.isDeclared(variableName, true));

    // Check variable is not declared in local scope if it's not present in the scope hierarchy
    Scope grandParentScope = new Scope(parentScope, rootNode);
    assertFalse("Variable should not be declared in local scope if it's not present in the scope hierarchy", localScope.isDeclared(variableName, true));
  }

  /**
   * Test getVars method.
   */
  @Test
  public void testGetVars() {
    // Create root node
    Node rootNode = createNode(Token.BLOCK, null);

    // Create parent scope
    Scope parentScope = new Scope(null, rootNode);

    // Create local scope
    Scope localScope = new Scope(parentScope, rootNode);

    // Create variables
    String variable1Name = "testVariable1";
    Node variable1Node = createNode(Token.IDENT, rootNode);
    JSType variable1Type = JSType.getBooleanType();
    CompilerInput input1 = new CompilerInput("test.js", 1, 1);
    Var variable1 = localScope.declare(variable1Name, variable1Node, variable1Type, input1, false);

    String variable2Name = "testVariable2";
    Node variable2Node = createNode(Token.IDENT, rootNode);
    JSType variable2Type = JSType.getNumberType();
    CompilerInput input2 = new CompilerInput("test.js", 1, 1);
    Var variable2 = localScope.declare(variable2Name, variable2Node, variable2Type, input2, false);

    // Check getting vars works
    Iterator<Var> vars = localScope.getVars().iterator();
    assertEquals("Number of variables should match", 2, vars.size());

    // Check getting first variable
    Var firstVariable = vars.next();
    assertEquals("First variable should be the same as declared", variable1, firstVariable);

    // Check getting second variable
    Var secondVariable = vars.next();
    assertEquals("Second variable should be the same as declared", variable2, secondVariable);

    // Check getting next variable after last one returns null
    assertNull("Should be null", vars.hasNext());
  }

  /**
   * Test getVarCount method.
   */
  @Test
  public void testGetVarCount() {
    // Create root node
    Node rootNode = createNode(Token.BLOCK, null);

    // Create parent scope
    Scope parentScope = new Scope(null, rootNode);

    // Create local scope
    Scope localScope = new Scope(parentScope, rootNode);

    // Create variables
    String variable1Name = "testVariable1";
    Node variable1Node = createNode(Token.IDENT, rootNode);
    JSType variable1Type = JSType.getBooleanType();
    CompilerInput input1 = new CompilerInput("test.js", 1, 1);
    Var variable1 = localScope.declare(variable1Name, variable1Node, variable1Type, input1, false);

    String variable2Name = "testVariable2";
    Node variable2Node = createNode(Token.IDENT, rootNode);
    JSType variable2Type = JSType.getNumberType();
    CompilerInput input2 = new CompilerInput("test.js", 1, 1);
    Var variable2 = localScope.declare(variable2Name, variable2Node, variable2Type, input2, false);

    // Check getting var count works
    int varCount = localScope.getVarCount();
    assertEquals("Variable count should match", 2, varCount);
  }
}