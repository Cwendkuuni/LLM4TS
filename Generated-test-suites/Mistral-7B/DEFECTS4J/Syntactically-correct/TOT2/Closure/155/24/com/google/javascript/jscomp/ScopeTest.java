import static org.junit.Assert.*;
import static com.google.javascript.jscomp.Scope.Var.*;

import com.google.common.collect.Maps;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.ObjectType;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public class ScopeTest {

  private static Scope createScope(Node rootNode) {
    Scope parent = null;
    Scope scope = new Scope(parent, rootNode);
    return scope;
  }

  @org.junit.Test
  public void testEmptyScope() {
    Node rootNode = Node.newFunction(null, null, null, null, null, null);
    Scope scope = createScope(rootNode);

    assertEquals(0, scope.getVarCount());
    assertNull(scope.getVar("foo"));
  }

  @org.junit.Test
  public void testDeclaredVariable() {
    Node rootNode = Node.newVar("x", null, null, Token.ASSIGN, null, null);
    Scope scope = createScope(rootNode);
    Var variable = scope.declare("x", rootNode.getFirstChild(), JSType.NULL, null);
    assertEquals("x", variable.getName());
    assertEquals(1, scope.getVarCount());
    assertEquals(JSType.NULL, variable.getType());
  }

  @org.junit.Test
  public void testDeclaredVariableWithDocInfo() {
    Node rootNode = Node.newVar("x", null, null, Token.ASSIGN, null, null);
    Node jsdocNode = Node.newText("@{@define x}", null, null);
    jsdocNode.setParent(rootNode);
    Scope scope = createScope(rootNode);
    Var variable = scope.declare("x", rootNode.getFirstChild(), JSType.NULL, null, true, jsdocNode);
    assertEquals("x", variable.getName());
    assertEquals(1, scope.getVarCount());
    assertEquals(JSType.NULL, variable.getType());
    assertTrue(variable.isDefine());
  }

  @org.junit.Test
  public void testDeclaredVariableWithType() {
    Node rootNode = Node.newVar("x", null, null, Token.ASSIGN, null, null);
    Node numberNode = Node.newInt(1);
    Node typeNode = Node.newIdentifier("Number");
    Node typeCallNode = Node.newMember(typeNode, "TYPE");
    Node assignNode = Node.newAssign(rootNode.getFirstChild(), typeCallNode, null);
    Scope scope = createScope(rootNode);
    Var variable = scope.declare("x", rootNode.getFirstChild(), new ObjectType(new FunctionType(null, new JSType[]{}, new JSType[]{}, null, null, null).getTypeOfThis()), null, true);
    assertEquals("x", variable.getName());
    assertEquals(1, scope.getVarCount());
    assertNotNull(variable.getType());
    assertEquals(new ObjectType(new FunctionType(null, new JSType[]{}, new JSType[]{}, null, null, null).getTypeOfThis()), variable.getType());
  }

  @org.junit.Test
  public void testDeclaredVariableWithInitialValue() {
    Node rootNode = Node.newVar("x", null, null, Token.ASSIGN, null, null);
    Node valueNode = Node.newInt(1);
    Node assignNode = Node.newAssign(rootNode.getFirstChild(), valueNode, null);
    Scope scope = createScope(rootNode);
    Var variable = scope.declare("x", rootNode.getFirstChild(), JSType.INT, null);
    assertEquals("x", variable.getName());
    assertEquals(1, scope.getVarCount());
    assertEquals(JSType.INT, variable.getType());
    assertEquals(1, variable.getIndex());
    assertEquals(valueNode, variable.getInitialValue());
  }

  @org.junit.Test
  public void testDeclaredVariableWithInitialValueAndDocInfo() {
    Node rootNode = Node.newVar("x", null, null, Token.ASSIGN, null, null);
    Node valueNode = Node.newInt(1);
    Node jsdocNode = Node.newText("@{@define x}", null, null);
    jsdocNode.setParent(rootNode);
    Scope scope = createScope(rootNode);
    Var variable = scope.declare("x", rootNode.getFirstChild(), JSType.INT, null, true, jsdocNode);
    assertEquals("x", variable.getName());
    assertEquals(1, scope.getVarCount());
    assertEquals(JSType.INT, variable.getType());
    assertEquals(1, variable.getIndex());
    assertEquals(valueNode, variable.getInitialValue());
    assertTrue(variable.isDefine());
  }

  @org.junit.Test
  public void testDeclaredBleedingFunctionVariable() {
    Node rootNode = Node.newFunctionExpression(null, null, null, null, null, null);
    Node xNode = Node.newVar("x", null, null, Token.VAR, null, null);
    Node assignNode = Node.newAssign(xNode, Node.newInt(1), null);
    Node functionBody = Node.newBlock(null, null, assignNode, null);
    rootNode.setBody(functionBody);
    Scope scope = createScope(rootNode);
    Var variable = scope.declare("x", xNode, JSType.INT, null);
    assertEquals("x", variable.getName());
    assertEquals(1, scope.getVarCount());
    assertEquals(JSType.INT, variable.getType());
    assertTrue(variable.isBleedingFunction());
  }

  @org.junit.Test
  public void testDeclaredConstantVariable() {
    Node rootNode = Node.newVar("x", null, null, Token.ASSIGN, null, null);
    Node nameNode = Node.newIdentifier("CONSTANT_X");
    Node assignNode = Node.newAssign(rootNode.getFirstChild(), nameNode, null);
    Scope scope = createScope(rootNode);
    Var variable = scope.declare("x", rootNode.getFirstChild(), JSType.NULL, null, true, null, null, null, NodeUtil.isConstantName(nameNode));
    assertEquals("x", variable.getName());
    assertEquals(1, scope.getVarCount());
    assertEquals(JSType.NULL, variable.getType());
    assertTrue(variable.isConst());
  }

  @org.junit.Test
  public void testDeclaredExternVariable() {
    Node rootNode = Node.newVar("x", null, null, Token.ASSIGN, null, null);
    Node inputNode = Node.newNull();
    Scope scope = createScope(rootNode);
    Var variable = scope.declare("x", rootNode.getFirstChild(), JSType.NULL, null, false, null, null, null, null, new CompilerInput(Maps.newHashMap(), null));
    assertEquals("x", variable.getName());
    assertEquals(1, scope.getVarCount());
    assertEquals(JSType.NULL, variable.getType());
    assertTrue(variable.isExtern());
  }

  @org.junit.Test
  public void testUndeclareVariable() {
    Node rootNode = Node.newVar("x", null, null, Token.ASSIGN, null, null);
    Scope scope = createScope(rootNode);
    Var variable = scope.declare("x", rootNode.getFirstChild(), JSType.NULL, null);
    scope.undeclare(variable);
    assertNull(scope.getVar("x"));
  }

  @org.junit.Test
  public void testGetSlot() {
    Node rootNode = Node.newFunctionExpression(null, null, null, null, null, null);
    Node xNode = Node.newVar("x", null, null, Token.ASSIGN, null, null);
    Node assignNode = Node.newAssign(xNode, Node.newInt(1), null);
    Node functionBody = Node.newBlock(null, null, assignNode, null);
    rootNode.setBody(functionBody);
    Scope scope = createScope(rootNode);
    Var variable = scope.declare("x", xNode, JSType.INT, null);
    assertEquals(variable, scope.getSlot("x"));
  }

  @org.junit.Test
  public void testGetSlotWithUndeclaredVariable() {
    Node rootNode = Node.newFunctionExpression(null, null, null, null, null, null);
    Node xNode = Node.newVar("x", null, null, Token.ASSIGN, null, null);
    Node functionBody = Node.newBlock(null, null, null, null);
    rootNode.setBody(functionBody);
    Scope scope = createScope(rootNode);
    assertNull(scope.getSlot("x"));
  }

  @org.junit.Test
  public void testGetSlotWithParentVariable() {
    Node parentRootNode = Node.newFunctionExpression(null, null, null, null, null, null);
    Node parentXNode = Node.newVar("x", null, null, Token.ASSIGN, null, null);
    Node parentAssignNode = Node.newAssign(parentXNode, Node.newInt(1), null);
    Node parentFunctionBody = Node.newBlock(null, null, parentAssignNode, null);
    parentRootNode.setBody(parentFunctionBody);
    Node childRootNode = Node.newFunctionExpression(null, null, null, null, null, parentRootNode);
    Node childXNode = Node.newVar("x", null, null, Token.ASSIGN, null, null);
    Node childAssignNode = Node.newAssign(childXNode, Node.newInt(2), null);
    Node childFunctionBody = Node.newBlock(null, null, childAssignNode, null);
    childRootNode.setBody(childFunctionBody);
    Scope childScope = createScope(childRootNode);
    Var parentVariable = ((Scope) parentRootNode.getFirstChild()).declare("x", parentXNode, JSType.INT, null);
    Var childVariable = childScope.declare("x", childXNode, JSType.INT, null);
    assertEquals(childVariable, childScope.getSlot("x"));
    assertEquals(parentVariable, childScope.getParent().getSlot("x"));
  }

  @org.junit.Test
  public void testJoinScopes() {
    Node parentRootNode = Node.newFunctionExpression(null, null, null, null, null, null);
    Node parentXNode = Node.newVar("x", null, null, Token.ASSIGN, null, null);
    Node parentAssignNode = Node.newAssign(parentXNode, Node.newInt(1), null);
    Node parentFunctionBody = Node.newBlock(null, null, parentAssignNode, null);
    parentRootNode.setBody(parentFunctionBody);
    Node childRootNode = Node.newFunctionExpression(null, null, null, null, null, parentRootNode);
    Node childXNode = Node.newVar("x", null, null, Token.ASSIGN, null, null);
    Node childAssignNode = Node.newAssign(childXNode, Node.newInt(2), null);
    Node childFunctionBody = Node.newBlock(null, null, childAssignNode, null);
    childRootNode.setBody(childFunctionBody);
    Scope parentScope = createScope(parentRootNode);
    Scope childScope = createScope(childRootNode);
    Map<String, Var> parentVars = parentScope.vars;
    Map<String, Var> childVars = childScope.vars;
    Var parentVariable = parentScope.declare("x", parentXNode, JSType.INT, null);
    Var childVariable = childScope.declare("x", childXNode, JSType.INT, null);
    parentVars.put("x", parentVariable);
    childVars.put("x", childVariable);
    Var joinedVariable = childScope.getParent().getSlot("x");
    assertEquals(parentVariable, joinedVariable);
  }
}