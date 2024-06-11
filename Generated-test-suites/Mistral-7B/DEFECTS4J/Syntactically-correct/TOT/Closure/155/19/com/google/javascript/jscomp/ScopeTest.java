import static org.junit.Assert.*;
import static com.google.javascript.jscomp.Scope.*;
import com.google.common.collect.Maps;

import java.util.Map;

import org.junit.Test;

public class ScopeTest {

  @Test
  public void testScopeCreation() {
    Node rootNode = new Node(Token.BLOCK);
    Scope scope = new Scope(null, rootNode);

    assertEquals(0, scope.getDepth());
    assertFalse(scope.isBottom());
    assertNull(scope.getParent());
    assertNull(scope.getTypeOfThis());

    Scope parentScope = new Scope(scope, rootNode);
    scope = parentScope;

    assertEquals(1, scope.getDepth());
    assertFalse(scope.isBottom());
    assertNotNull(scope.getParent());
    assertNotNull(scope.getTypeOfThis());

    Scope globalScope = new Scope(null, new Node(Token.SCRIPT));
    scope = globalScope;

    assertEquals(0, scope.getDepth());
    assertTrue(scope.isBottom());
    assertNull(scope.getParent());
    assertNotNull(scope.getTypeOfThis());
  }

  @Test
  public void testScopeVariableDeclaration() {
    Node rootNode = new Node(Token.BLOCK);
    Scope scope = new Scope(null, rootNode);
    CompilerInput input = new CompilerInput("test.js");

    String varName = "testVar";
    Node nameNode = new Node(Token.NAME, varName, null);
    JSType type = integerType();

    Var var = scope.declare(varName, nameNode, type, input);

    assertEquals(1, scope.getVarCount());
    assertEquals(var, scope.getVar(varName));
    assertEquals(type, var.getType());
    assertEquals(varName, var.getName());

    scope.undeclare(var);
    assertEquals(0, scope.getVarCount());
  }

  @Test
  public void testScopeVariableDeclarationWithInitialValue() {
    Node rootNode = new Node(Token.BLOCK);
    Scope scope = new Scope(null, rootNode);
    CompilerInput input = new CompilerInput("test.js");

    String varName = "testVar";
    Node nameNode = new Node(Token.NAME, varName, null);
    Node initialValue = new Node(Token.NUMBER, 42);
    JSType type = integerType();

    Var var = scope.declare(varName, nameNode, type, input, initialValue);

    assertEquals(1, scope.getVarCount());
    assertEquals(var, scope.getVar(varName));
    assertEquals(type, var.getType());
    assertEquals(varName, var.getName());
    assertEquals(initialValue, var.getInitialValue());

    scope.undeclare(var);
    assertEquals(0, scope.getVarCount());
  }

  @Test
  public void testScopeVariableDeclarationWithJSDocInfo() {
    Node rootNode = new Node(Token.BLOCK);
    Scope scope = new Scope(null, rootNode);
    CompilerInput input = new CompilerInput("test.js");

    String varName = "testVar";
    Node nameNode = new Node(Token.NAME, varName, null);
    JSType type = integerType();
    JSDocInfo info = new JSDocInfo("@define testVar");

    Var var = scope.declare(varName, nameNode, type, input, info);

    assertEquals(1, scope.getVarCount());
    assertEquals(var, scope.getVar(varName));
    assertEquals(type, var.getType());
    assertEquals(varName, var.getName());
    assertEquals(info, var.getJSDocInfo());

    scope.undeclare(var);
    assertEquals(0, scope.getVarCount());
  }

  @Test
  public void testScopeVariableDeclarationWithBleedingFunction() {
    Node rootNode = new Node(Token.BLOCK);
    Scope scope = new Scope(null, rootNode);

    String varName = "testVar";
    Node nameNode = new Node(Token.NAME, varName, null);
    Node functionNode = new Node(Token.FUNCTION, null, null, null);
    JSType type = integerType();

    scope.declare(varName, nameNode, type, null, null, functionNode);

    assertTrue(functionNode.isFunctionExpression());
    assertEquals(1, scope.getVarCount());
    assertEquals(functionNode.getLastChild(), scope.getVar(varName).getInitialValue());
  }

  @Test(expected = IllegalStateException.class)
  public void testScopeVariableTypeSet() {
    Node rootNode = new Node(Token.BLOCK);
    Scope scope = new Scope(null, rootNode);
    CompilerInput input = new CompilerInput("test.js");

    String varName = "testVar";
    Node nameNode = new Node(Token.NAME, varName, null);
    JSType type = integerType();
    Var var = scope.getVar(varName);

    var.setType(stringType());
  }

  @Test
  public void testScopeVariableTypeInference() {
    Node rootNode = new Node(Token.BLOCK);
    Scope scope = new Scope(null, rootNode);
    CompilerInput input = new CompilerInput("test.js");

    String varName = "testVar";
    Node nameNode = new Node(Token.NAME, varName, null);
    JSType type = integerType();

    Var var = scope.declare(varName, nameNode, null, input);

    assertEquals(1, scope.getVarCount());
    assertEquals(var, scope.getVar(varName));
    assertEquals(type, var.getType());

    type = stringType();
    var.setType(type);

    assertEquals(type, var.getType());
  }

  @Test
  public void testScopeVariableResolution() {
    Node rootNode = new Node(Token.BLOCK);
    Scope scope = new Scope(null, rootNode);
    CompilerInput input1 = new CompilerInput("test1.js");
    CompilerInput input2 = new CompilerInput("test2.js");

    String varName = "testVar";
    Node nameNode = new Node(Token.NAME, varName, null);
    JSType type = integerType();

    Var var1 = scope.declare(varName, nameNode, type, input1);
    Var var2 = scope.declare(varName, nameNode, type, input2);

    assertEquals(2, scope.getVarCount());
    assertEquals(var2, scope.getVar(varName));

    assertEquals(type, var1.getType());
    assertEquals(type, var2.getType());
    assertEquals(input1, var1.getInput());
    assertEquals(input2, var2.getInput());
  }

  @Test
  public void testScopeVariableResolutionWithOverriddenType() {
    Node rootNode = new Node(Token.BLOCK);
    Scope scope = new Scope(null, rootNode);
    CompilerInput input1 = new CompilerInput("test1.js");
    CompilerInput input2 = new CompilerInput("test2.js");

    String varName = "testVar";
    Node nameNode = new Node(Token.NAME, varName, null);
    JSType type1 = integerType();
    JSType type2 = stringType();

    Var var1 = scope.declare(varName, nameNode, type1, input1);
    Var var2 = scope.declare(varName, nameNode, type2, input2);

    assertEquals(2, scope.getVarCount());
    assertEquals(var2, scope.getVar(varName));

    assertEquals(type2, var1.getType());
    assertEquals(type2, var2.getType());
    assertEquals(input2, var1.getInput());
    assertEquals(input2, var2.getInput());
  }

  @Test
  public void testScopeVariableResolutionWithInferredType() {
    Node rootNode = new Node(Token.BLOCK);
    Scope scope = new Scope(null, rootNode);
    CompilerInput input1 = new CompilerInput("test1.js");
    CompilerInput input2 = new CompilerInput("test2.js");

    String varName = "testVar";
    Node nameNode = new Node(Token.NAME, varName, null);
    JSType type1 = integerType();
    JSType type2 = stringType();

    Var var1 = scope.declare(varName, nameNode, null, input1);
    Var var2 = scope.declare(varName, nameNode, null, input2);

    assertEquals(2, scope.getVarCount());
    assertEquals(var2, scope.getVar(varName));

    // Resolve type for var1
    var1.resolveType(new ErrorReporter() {
      @Override
      public void error(String message, int line, int lineNumber) {
        fail("Error while resolving type for var1: " + message);
      }
    });

    assertEquals(type2, var1.getType());
    assertEquals(type2, var2.getType());
    assertEquals(input2, var1.getInput());
    assertEquals(input2, var2.getInput());
  }

  @Test
  public void testScopeVariableIsDeclared() {
    Node rootNode = new Node(Token.BLOCK);
    Scope scope = new Scope(null, rootNode);

    String varName = "testVar";
    Node nameNode = new Node(Token.NAME, varName, null);

    assertTrue(scope.isDeclared(varName, true));

    Scope parentScope = new Scope(scope, rootNode);
    scope = parentScope;

    assertFalse(scope.isDeclared(varName, true));

    assertFalse(scope.isDeclared(varName, false));
  }

  @Test
  public void testScopeVariableIsNotDeclared() {
    Node rootNode = new Node(Token.BLOCK);
    Scope scope = new Scope(null, rootNode);

    String varName = "testVar";
    Node nameNode = new Node(Token.NAME, varName, null);

    assertFalse(scope.isDeclared(varName, true));

    Scope parentScope = new Scope(scope, rootNode);
    scope = parentScope;

    assertFalse(scope.isDeclared(varName, true));

    assertFalse(scope.isDeclared(varName, false));
  }

  @Test
  public void testScopeVariableIsDeclaredInParentScope() {
    Node rootNode = new Node(Token.BLOCK);
    Scope scope = new Scope(null, rootNode);

    String varName = "testVar";
    Node nameNode = new Node(Token.NAME, varName, null);

    Scope parentScope = new Scope(scope, rootNode);
    scope = parentScope;

    Var var = scope.declare(varName, nameNode, null, null);

    assertTrue(scope.isDeclared(varName, true));

    assertFalse(scope.isDeclared(varName, false));
  }

  @Test
  public void testScopeGetVars() {
    Node rootNode = new Node(Token.BLOCK);
    Scope scope = new Scope(null, rootNode);

    String varName1 = "testVar1";
    Node nameNode1 = new Node(Token.NAME, varName1, null);
    String varName2 = "testVar2";
    Node nameNode2 = new Node(Token.NAME, varName2, null);

    Var var1 = scope.declare(varName1, nameNode1, null, null);
    Var var2 = scope.declare(varName2, nameNode2, null, null);

    Iterator<Var> varIterator = scope.getVars();

    assertTrue(varIterator.hasNext());
    Var nextVar = varIterator.next();
    assertEquals(var1, nextVar);

    assertTrue(varIterator.hasNext());
    nextVar = varIterator.next();
    assertEquals(var2, nextVar);

    assertFalse(varIterator.hasNext());
  }

  @Test
  public void testScopeGetOwnSlot() {
    Node rootNode = new Node(Token.BLOCK);
    Scope scope = new Scope(null, rootNode);

    String varName = "testVar";
    Node nameNode = new Node(Token.NAME, varName, null);

    Var var = scope.declare(varName, nameNode, null, null);

    assertEquals(var, scope.getOwnSlot(varName));
  }

  @Test
  public void testScopeGetSlot() {
    Node rootNode = new Node(Token.BLOCK);
    Scope scope = new Scope(null, rootNode);

    String varName = "testVar";
    Node nameNode = new Node(Token.NAME, varName, null);

    Var var = scope.declare(varName, nameNode, null, null);

    assertEquals(var, scope.getSlot(varName));
  }

  @Test
  public void testScopeGetSlotWithUndefinedVariable() {
    Node rootNode = new Node(Token.BLOCK);
    Scope scope = new Scope(null, rootNode);

    String varName = "testVar";

    assertEquals(null, scope.getSlot(varName));
  }

  @Test
  public void testScopeGetSlotWithVariableInParentScope() {
    Node rootNode = new Node(Token.BLOCK);
    Scope scope = new Scope(null, rootNode);

    String varName = "testVar";
    Node nameNode = new Node(Token.NAME, varName, null);

    Scope parentScope = new Scope(scope, rootNode);
    scope = parentScope;

    Var var = scope.declare(varName, nameNode, null, null);

    assertEquals(var, scope.getParent().getSlot(varName));
  }

  @Test
  public void testScopeGetVarCount() {
    Node rootNode = new Node(Token.BLOCK);
    Scope scope = new Scope(null, rootNode);

    String varName1 = "testVar1";
    Node nameNode1 = new Node(Token.NAME, varName1, null);
    String varName2 = "testVar2";
    Node nameNode2 = new Node(Token.NAME, varName2, null);

    Var var1 = scope.declare(varName1, nameNode1, null, null);
    Var var2 = scope.declare(varName2, nameNode2, null, null);

    assertEquals(2, scope.getVarCount());
  }

  @Test
  public void testScopeIsLocal() {
    Node rootNode = new Node(Token.BLOCK);
    Scope scope = new Scope(null, rootNode);

    assertTrue(scope.isLocal());

    Scope parentScope = new Scope(scope, rootNode);
    scope = parentScope;

    assertTrue(scope.isLocal());

    Scope globalScope = new Scope(null, new Node(Token.SCRIPT));
    scope = globalScope;

    assertFalse(scope.isLocal());
  }

  @Test
  public void testScopeIsGlobal() {
    Node rootNode = new Node(Token.BLOCK);
    Scope scope = new Scope(null, rootNode);

    assertFalse(scope.isGlobal());

    Scope parentScope = new Scope(scope, rootNode);
    scope = parentScope;

    assertFalse(scope.isGlobal());

    Scope globalScope = new Scope(null, new Node(Token.SCRIPT));
    scope = globalScope;

    assertTrue(scope.isGlobal());
  }

  @Test
  public void testScopeWithNoShadow() {
    Node rootNode = new Node(Token.BLOCK);
    Scope scope = new Scope(null, rootNode);

    String varName = "testVar";
    Node nameNode = new Node(Token.NAME, varName, null);
    JSType type = integerType();
    JSDocInfo info = new JSDocInfo("@noShadow testVar");

    Var var = scope.declare(varName, nameNode, type, null, null, null, info);

    assertTrue(var.isNoShadow());
  }

  private JSType integerType() {
    return new PrimitiveType(JSType.INT);
  }

  private JSType stringType() {
    return new PrimitiveType(JSType.STRING);
  }
}