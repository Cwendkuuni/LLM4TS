import static org.junit.Assert.*;
import static com.google.javascript.jscomp.Scope.Var.*;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.Test;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Scope.FunctionType;
import com.google.javascript.jscomp.Scope.ObjectType;
import com.google.javascript.jscomp.Scope.Var;

public class ScopeTest {

  @Test
  public void testScopeCreation() {
    Scope parentScope = new Scope(null, new Node(Token.BLOCK, 1, null));
    Scope childScope = new Scope(parentScope, new Node(Token.FUNCTION, 1, null));

    assertEquals(1, childScope.getDepth());
    assertEquals(parentScope, childScope.getParent());
    assertEquals(null, childScope.thisType);

    Scope globalScope = new Scope(new Node(Token.BLOCK, 1, null), null);
    assertEquals(0, globalScope.getDepth());
    assertEquals(null, globalScope.getParent());
    assertEquals(new ObjectType(GLOBAL_THIS), globalScope.thisType);
  }

  @Test
  public void testDeclareVariable() {
    Scope parentScope = new Scope(null, new Node(Token.BLOCK, 1, null));
    Scope childScope = new Scope(parentScope, new Node(Token.FUNCTION, 1, null));

    String variableName = "testVariable";
    Node variableNameNode = new Node(Token.IDENT, 1, null);
    JSType variableType = new FunctionType(new ObjectType(JavaType.STRING), null);

    Var variable = childScope.declare(variableName, variableNameNode, variableType, null);

    assertEquals(variableName, variable.getName());
    assertEquals(variableNameNode, variable.getNameNode());
    assertEquals(variableType, variable.getType());
    assertEquals(childScope, variable.getScope());
    assertEquals(false, variable.isTypeInferred());
    assertEquals(null, variable.getInputName());
  }

  @Test
  public void testDeclareVariableWithInput() {
    Scope parentScope = new Scope(null, new Node(Token.BLOCK, 1, null));
    Scope childScope = new Scope(parentScope, new Node(Token.FUNCTION, 1, null));

    String variableName = "testVariable";
    Node variableNameNode = new Node(Token.IDENT, 1, null);
    JSType variableType = new FunctionType(new ObjectType(JavaType.STRING), null);
    CompilerInput input = new CompilerInput("test.js", "test content");

    Var variable = childScope.declare(variableName, variableNameNode, variableType, input);

    assertEquals(variableName, variable.getName());
    assertEquals(variableNameNode, variable.getNameNode());
    assertEquals(variableType, variable.getType());
    assertEquals(childScope, variable.getScope());
    assertEquals(false, variable.isTypeInferred());
    assertEquals("test.js", variable.getInputName());
  }

  @Test
  public void testDeclareVariableWithJSDocInfo() {
    Scope parentScope = new Scope(null, new Node(Token.BLOCK, 1, null));
    Scope childScope = new Scope(parentScope, new Node(Token.FUNCTION, 1, null));

    String variableName = "testVariable";
    Node variableNameNode = new Node(Token.IDENT, 1, null);
    JSType variableType = new FunctionType(new ObjectType(JavaType.STRING), null);
    JSDocInfo docInfo = new JSDocInfo("test", new Node(Token.STRING_LITERAL, 1, null), null);

    Var variable = childScope.declare(variableName, variableNameNode, variableType, null, docInfo);

    assertEquals(variableName, variable.getName());
    assertEquals(variableNameNode, variable.getNameNode());
    assertEquals(variableType, variable.getType());
    assertEquals(childScope, variable.getScope());
    assertEquals(false, variable.isTypeInferred());
    assertEquals(null, variable.getInputName());
    assertEquals(docInfo, variable.getJSDocInfo());
  }

  @Test
  public void testGetVar() {
    Scope parentScope = new Scope(null, new Node(Token.BLOCK, 1, null));
    Scope childScope = new Scope(parentScope, new Node(Token.FUNCTION, 1, null));

    String variableName1 = "testVariable1";
    Node variableNameNode1 = new Node(Token.IDENT, 1, null);
    Var variable1 = childScope.declare(variableName1, variableNameNode1, new ObjectType(JavaType.NUMBER), null);

    String variableName2 = "testVariable2";
    Node variableNameNode2 = new Node(Token.IDENT, 1, null);
    Var variable2 = childScope.declare(variableName2, variableNameNode2, new ObjectType(JavaType.STRING), null);

    assertEquals(variable1, childScope.getVar(variableName1));
    assertEquals(variable2, childScope.getVar(variableName2));
    assertEquals(null, childScope.getVar("unknownVariable"));
  }

  @Test
  public void testIsDeclared() {
    Scope parentScope = new Scope(null, new Node(Token.BLOCK, 1, null));
    Scope childScope = new Scope(parentScope, new Node(Token.FUNCTION, 1, null));

    String variableName1 = "testVariable1";
    Node variableNameNode1 = new Node(Token.IDENT, 1, null);
    Var variable1 = childScope.declare(variableName1, variableNameNode1, new ObjectType(JavaType.NUMBER), null);

    String variableName2 = "testVariable2";
    Node variableNameNode2 = new Node(Token.IDENT, 1, null);
    Var variable2 = childScope.declare(variableName2, variableNameNode2, new ObjectType(JavaType.STRING), null);

    assertTrue(childScope.isDeclared(variableName1, true));
    assertTrue(childScope.isDeclared(variableName2, true));
    assertFalse(childScope.isDeclared("unknownVariable", true));

    assertFalse(childScope.isDeclared(variableName1, false));
    assertFalse(childScope.isDeclared(variableName2, false));
    assertFalse(childScope.isDeclared("unknownVariable", false));
  }

  @Test
  public void testGetParentScope() {
    Scope grandparentScope = new Scope(null, new Node(Token.BLOCK, 1, null));
    Scope parentScope = new Scope(grandparentScope, new Node(Token.FUNCTION, 1, null));
    Scope childScope = new Scope(parentScope, new Node(Token.FUNCTION, 1, null));

    assertEquals(parentScope, childScope.getParentScope());
    assertEquals(grandparentScope, parentScope.getParentScope());
    assertEquals(null, grandparentScope.getParentScope());
  }

  @Test
  public void testGetSlot() {
    Scope parentScope = new Scope(null, new Node(Token.BLOCK, 1, null));
    Scope childScope = new Scope(parentScope, new Node(Token.FUNCTION, 1, null));

    String variableName1 = "testVariable1";
    Node variableNameNode1 = new Node(Token.IDENT, 1, null);
    Var variable1 = childScope.declare(variableName1, variableNameNode1, new ObjectType(JavaType.NUMBER), null);

    assertEquals(variable1, childScope.getSlot(variableName1));

    assertNull(childScope.getSlot("unknownVariable"));
  }

  @Test
  public void testGetOwnSlot() {
    Scope parentScope = new Scope(null, new Node(Token.BLOCK, 1, null));
    Scope childScope = new Scope(parentScope, new Node(Token.FUNCTION, 1, null));

    String variableName1 = "testVariable1";
    Node variableNameNode1 = new Node(Token.IDENT, 1, null);
    Var variable1 = childScope.declare(variableName1, variableNameNode1, new ObjectType(JavaType.NUMBER), null);

    assertEquals(variable1, childScope.getOwnSlot(variableName1));

    assertNull(childScope.getOwnSlot("unknownVariable"));
  }

  @Test
  public void testGetVars() {
    Scope parentScope = new Scope(null, new Node(Token.BLOCK, 1, null));
    Scope childScope = new Scope(parentScope, new Node(Token.FUNCTION, 1, null));

    String variableName1 = "testVariable1";
    Node variableNameNode1 = new Node(Token.IDENT, 1, null);
    Var variable1 = childScope.declare(variableName1, variableNameNode1, new ObjectType(JavaType.NUMBER), null);

    String variableName2 = "testVariable2";
    Node variableNameNode2 = new Node(Token.IDENT, 1, null);
    Var variable2 = childScope.declare(variableName2, variableNameNode2, new ObjectType(JavaType.STRING), null);

    Iterator<Var> vars = childScope.getVars();
    assertEquals(Arrays.asList(variable1, variable2), Iterators.toList(vars));
  }

  @Test
  public void testGetVarCount() {
    Scope parentScope = new Scope(null, new Node(Token.BLOCK, 1, null));
    Scope childScope = new Scope(parentScope, new Node(Token.FUNCTION, 1, null));

    String variableName1 = "testVariable1";
    Node variableNameNode1 = new Node(Token.IDENT, 1, null);
    Var variable1 = childScope.declare(variableName1, variableNameNode1, new ObjectType(JavaType.NUMBER), null);

    String variableName2 = "testVariable2";
    Node variableNameNode2 = new Node(Token.IDENT, 1, null);
    Var variable2 = childScope.declare(variableName2, variableNameNode2, new ObjectType(JavaType.STRING), null);

    assertEquals(2, childScope.getVarCount());
  }

  @Test
  public void testIsGlobal() {
    Scope parentScope = new Scope(null, new Node(Token.BLOCK, 1, null));
    Scope childScope = new Scope(parentScope, new Node(Token.FUNCTION, 1, null));

    assertTrue(parentScope.isGlobal());
    assertFalse(childScope.isGlobal());
  }

  @Test
  public void testIsLocal() {
    Scope parentScope = new Scope(null, new Node(Token.BLOCK, 1, null));
    Scope childScope = new Scope(parentScope, new Node(Token.FUNCTION, 1, null));

    assertTrue(childScope.isLocal());
    assertFalse(parentScope.isLocal());
  }
}