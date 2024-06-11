import static org.junit.Assert.*;
import static com.google.javascript.rhino.NodeTraversal.scope;
import static com.google.javascript.rhino.NodeTraversal.findFunctionNode;
import static com.google.javascript.rhino.jstype.JSTypeNative.GLOBAL_THIS;

import com.google.common.collect.LinkedHashMap;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.rhino.CompilerInput;
import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;
import com.google.javascript.rhino.jstype.FunctionType;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.ObjectType;
import com.google.javascript.rhino.jstype.StaticScope;
import com.google.javascript.rhino.jstype.StaticSlot;

import java.util.Iterator;
import java.util.Map;

/**
 * ScopeTest class for testing the Scope class.
 */
public class ScopeTest {

  @org.junit.BeforeClass
  public static void setUp() {
    // Initialize error reporter for testing
    ErrorReporter errorReporter = new ErrorReporter();
  }

  /**
   * Test constructor with valid root node and parent Scope.
   */
  @org.junit.Test
  public void testScopeConstructor() {
    Node rootNode = Node.newNode(Token.FUNCTION, "testFunction", null, new Node[0], null);
    Scope scope = new Scope(null, rootNode);
    assertNull(scope.getParent());
    assertEquals(0, scope.getDepth());
    assertEquals(GLOBAL_THIS, scope.getTypeOfThis());
    assertTrue(scope.isBottom());
  }

  /**
   * Test constructor with valid root node, parent Scope, and thisType.
   */
  @org.junit.Test
  public void testScopeConstructorWithThisType() {
    Node rootNode = Node.newNode(Token.FUNCTION, "testFunction", null, new Node[0], null);
    ObjectType thisType = new ObjectType("ObjectType");
    Scope scope = new Scope(null, rootNode, thisType);
    assertNull(scope.getParent());
    assertEquals(0, scope.getDepth());
    assertEquals(thisType, scope.getTypeOfThis());
    assertTrue(scope.isBottom());
  }

  /**
   * Test constructor with valid parent Scope and rootNode.
   */
  @org.junit.Test
  public void testScopeConstructorWithParent() {
    Node parentRootNode = Node.newNode(Token.FUNCTION, "parentFunction", null, new Node[0], null);
    Scope parentScope = new Scope(null, parentRootNode);
    Node childRootNode = Node.newNode(Token.FUNCTION, "childFunction", null, new Node[0], null);
    Scope childScope = new Scope(parentScope, childRootNode);

    assertEquals(parentScope, childScope.getParent());
    assertEquals(parentScope.getDepth() + 1, childScope.getDepth());
    assertNotNull(childScope.getTypeOfThis());
    assertFalse(childScope.isBottom());
  }

  /**
   * Test constructor with valid global root node.
   */
  @org.junit.Test
  public void testScopeConstructorWithGlobalRootNode() {
    Node rootNode = Node.newNode(Token.BLOCK, null, null, new Node[0], null);
    Scope scope = new Scope(rootNode, null);
    assertNull(scope.getParent());
    assertEquals(0, scope.getDepth());
    assertEquals(GLOBAL_THIS, scope.getTypeOfThis());
    assertFalse(scope.isBottom());
  }

  /**
   * Test declare method with inferred type and name.
   */
  @org.junit.Test
  public void testDeclareWithInferredType() {
    Node rootNode = Node.newNode(Token.FUNCTION, "testFunction", null, new Node[0], null);
    Scope scope = new Scope(null, rootNode);

    String variableName = "variable";
    JSType variableType = new FunctionType(new ObjectType("ObjectType"), new ObjectType[0]);
    Var variable = scope.declare(variableName, Node.newNode(Token.IDENT, variableName, null), variableType, new CompilerInput("test.js"));

    assertEquals(variableName, variable.getName());
    assertEquals(variableType, variable.getType());
    assertTrue(variable.isTypeInferred());
  }

  /**
   * Test declare method with declared type and name.
   */
  @org.junit.Test
  public void testDeclareWithDeclaredType() {
    Node rootNode = Node.newNode(Token.FUNCTION, "testFunction", null, new Node[0], null);
    Scope scope = new Scope(null, rootNode);

    String variableName = "variable";
    JSType variableType = new ObjectType("ObjectType");
    Var variable = scope.declare(variableName, Node.newNode(Token.IDENT, variableName, null), variableType, new CompilerInput("test.js"), false);

    assertEquals(variableName, variable.getName());
    assertEquals(variableType, variable.getType());
    assertFalse(variable.isTypeInferred());
  }

  /**
   * Test getVar method with declared variable.
   */
  @org.junit.Test
  public void testGetVarWithDeclaredVariable() {
    Node rootNode = Node.newNode(Token.FUNCTION, "testFunction", null, new Node[0], null);
    Scope scope = new Scope(null, rootNode);

    String variableName = "variable";
    JSType variableType = new ObjectType("ObjectType");
    Var variable = scope.declare(variableName, Node.newNode(Token.IDENT, variableName, null), variableType, new CompilerInput("test.js"), false);

    Var getVariable = scope.getVar(variableName);

    assertEquals(variable, getVariable);
  }

  /**
   * Test getVar method with undefined variable.
   */
  @org.junit.Test
  public void testGetVarWithUndefinedVariable() {
    Node rootNode = Node.newNode(Token.FUNCTION, "testFunction", null, new Node[0], null);
    Scope scope = new Scope(null, rootNode);

    Var undefinedVariable = scope.getVar("undefinedVariable");
    assertNull(undefinedVariable);
  }

  /**
   * Test getVar method with parent scope variable.
   */
  @org.junit.Test
  public void testGetVarWithParentScopeVariable() {
    Node parentRootNode = Node.newNode(Token.FUNCTION, "parentFunction", null, new Node[0], null);
    Scope parentScope = new Scope(null, parentRootNode);
    String parentVariableName = "parentVariable";
    JSType parentVariableType = new ObjectType("ObjectType");
    Var parentVariable = parentScope.declare(parentVariableName, Node.newNode(Token.IDENT, parentVariableName, null), parentVariableType, new CompilerInput("parent.js"), false);

    Node childRootNode = Node.newNode(Token.FUNCTION, "childFunction", null, new Node[0], null);
    Scope childScope = new Scope(parentScope, childRootNode);

    String childVariableName = "childVariable";
    JSType childVariableType = new ObjectType("ObjectType");
    Var childVariable = childScope.declare(childVariableName, Node.newNode(Token.IDENT, childVariableName, null), childVariableType, new CompilerInput("child.js"), false);

    Var getChildVariable = childScope.getVar(childVariableName);
    assertEquals(childVariable, getChildVariable);

    Var getParentVariable = childScope.getVar(parentVariableName);
    assertEquals(parentVariable, getParentVariable);
  }

  /**
   * Test isDeclared method with declared variable.
   */
  @org.junit.Test
  public void testIsDeclaredWithDeclaredVariable() {
    Node rootNode = Node.newNode(Token.FUNCTION, "testFunction", null, new Node[0], null);
    Scope scope = new Scope(null, rootNode);

    String variableName = "variable";
    JSType variableType = new ObjectType("ObjectType");
    Var variable = scope.declare(variableName, Node.newNode(Token.IDENT, variableName, null), variableType, new CompilerInput("test.js"), false);

    assertTrue(scope.isDeclared(variableName, true));
  }

  /**
   * Test isDeclared method with undefined variable.
   */
  @org.junit.Test
  public void testIsDeclaredWithUndefinedVariable() {
    Node rootNode = Node.newNode(Token.FUNCTION, "testFunction", null, new Node[0], null);
    Scope scope = new Scope(null, rootNode);

    assertFalse(scope.isDeclared("undefinedVariable", true));
  }

  /**
   * Test getVars method.
   */
  @org.junit.Test
  public void testGetVars() {
    Node rootNode = Node.newNode(Token.FUNCTION, "testFunction", null, new Node[0], null);
    Scope scope = new Scope(null, rootNode);

    String variable1Name = "variable1";
    JSType variable1Type = new ObjectType("ObjectType");
    Var variable1 = scope.declare(variable1Name, Node.newNode(Token.IDENT, variable1Name, null), variable1Type, new CompilerInput("test1.js"), false);

    String variable2Name = "variable2";
    JSType variable2Type = new ObjectType("ObjectType");
    Var variable2 = scope.declare(variable2Name, Node.newNode(Token.IDENT, variable2Name, null), variable2Type, new CompilerInput("test2.js"), false);

    Iterator<Var> varsIterator = scope.getVars().iterator();
    Var variable1FromIterator = varsIterator.next();
    Var variable2FromIterator = varsIterator.next();

    assertEquals(variable1, variable1FromIterator);
    assertEquals(variable2, variable2FromIterator);
  }

  /**
   * Test getVarCount method.
   */
  @org.junit.Test
  public void testGetVarCount() {
    Node rootNode = Node.newNode(Token.FUNCTION, "testFunction", null, new Node[0], null);
    Scope scope = new Scope(null, rootNode);

    String variable1Name = "variable1";
    JSType variable1Type = new ObjectType("ObjectType");
    Var variable1 = scope.declare(variable1Name, Node.newNode(Token.IDENT, variable1Name, null), variable1Type, new CompilerInput("test1.js"), false);

    String variable2Name = "variable2";
    JSType variable2Type = new ObjectType("ObjectType");
    Var variable2 = scope.declare(variable2Name, Node.newNode(Token.IDENT, variable2Name, null), variable2Type, new CompilerInput("test2.js"), false);

    assertEquals(2, scope.getVarCount());
  }

  /**
   * Test isLocal method.
   */
  @org.junit.Test
  public void testIsLocal() {
    Node rootNode = Node.newNode(Token.FUNCTION, "testFunction", null, new Node[0], null);
    Scope scope = new Scope(null, rootNode);

    String variable1Name = "variable1";
    JSType variable1Type = new ObjectType("ObjectType");
    Var variable1 = scope.declare(variable1Name, Node.newNode(Token.IDENT, variable1Name, null), variable1Type, new CompilerInput("test1.js"), false);

    assertTrue(scope.isLocal());

    String globalVariableName = "globalVariable";
    Var globalVariable = scope.getParent().declare(globalVariableName, Node.newNode(Token.IDENT, globalVariableName, null), null, null, false);

    assertFalse(scope.isLocal());
  }

  /**
   * Test isGlobal method.
   */
  @org.junit.Test
  public void testIsGlobal() {
    Node rootNode = Node.newNode(Token.FUNCTION, "testFunction", null, new Node[0], null);
    Scope scope = new Scope(null, rootNode);

    assertFalse(scope.isGlobal());

    Scope globalScope = scope.getParent();
    String globalVariableName = "globalVariable";
    Var globalVariable = globalScope.declare(globalVariableName, Node.newNode(Token.IDENT, globalVariableName, null), null, null, false);

    assertTrue(globalScope.isGlobal());
  }
}