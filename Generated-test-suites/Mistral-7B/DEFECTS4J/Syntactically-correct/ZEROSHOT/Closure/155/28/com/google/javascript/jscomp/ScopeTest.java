import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.google.common.collect.Maps;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.NodeUtil;
import com.google.javascript.rhino.StaticScope;
import com.google.javascript.rhino.jstype.FunctionType;
import com.google.javascript.rhino.jstype.ObjectType;
import com.google.javascript.rhino.jstype.StaticScope;
import com.google.javascript.rhino.jstype.StaticSlot;
import com.google.javascript.rhino.jstype.JSType;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public class ScopeTest {

  private ErrorReporter errorReporter;
  private CompilerInput input;
  private Scope scope;
  private Node rootNode;

  @Before
  public void setUp() throws Exception {
    errorReporter = mock(ErrorReporter.class);
    input = new CompilerInput("test.js", "test", null, false, null);
    rootNode = mock(Node.class);
    MockitoAnnotations.initMocks(this);
    scope = new Scope(null, rootNode);
  }

  @Test
  public void testCreateGlobalScope() {
    Scope globalScope = new Scope(rootNode, null);
    assertEquals(0, globalScope.getDepth());
    assertFalse(globalScope.isBottom());
    assertNull(globalScope.getParent());
    assertNotNull(globalScope.getTypeOfThis());
  }

  @Test
  public void testCreateLocalScope() {
    Scope parentScope = mock(Scope.class);
    when(parentScope.getDepth()).thenReturn(1);
    when(parentScope.getTypeOfThis()).thenReturn(new ObjectType(JSType.OBJECT));

    scope = new Scope(parentScope, rootNode);
    assertEquals(2, scope.getDepth());
    assertFalse(scope.isBottom());
    assertEquals(parentScope, scope.getParent());
    assertNotNull(scope.getTypeOfThis());
  }

  @Test
  public void testDeclareVariable() {
    String name = "variableName";
    Node nameNode = mock(Node.class);
    JSType type = new FunctionType(JSType.VOID, new ObjectType(JSType.NUMBER));
    Var variable = scope.declare(name, nameNode, type, input);
    assertNotNull(variable);
    assertEquals(name, variable.getName());
    assertEquals(nameNode, variable.getNameNode());
    assertEquals(type, variable.getType());
    assertEquals(scope, variable.getScope());
    assertEquals(0, variable.getIndex());
    assertEquals(input, variable.getInput());
    assertTrue(variable.isDefine());
  }

  @Test
  public void testDeclareVariableWithJSDocInfo() {
    String name = "variableName";
    Node nameNode = mock(Node.class);
    JSType type = new FunctionType(JSType.VOID, new ObjectType(JSType.NUMBER));
    JSDocInfo jsdocInfo = mock(JSDocInfo.class);
    Var variable = scope.declare(name, nameNode, type, input, jsdocInfo);
    assertNotNull(variable);
    assertEquals(name, variable.getName());
    assertEquals(nameNode, variable.getNameNode());
    assertEquals(type, variable.getType());
    assertEquals(scope, variable.getScope());
    assertEquals(0, variable.getIndex());
    assertEquals(input, variable.getInput());
    assertTrue(variable.isDefine());
    assertEquals(jsdocInfo, variable.getJSDocInfo());
  }

  @Test
  public void testGetVar() {
    String name = "variableName";
    Node nameNode = mock(Node.class);
    Var variable = new Var(true, name, nameNode, null, scope, 0, input, false, null);
    scope.vars = new LinkedHashMap<String, Var>() {{
      put(name, variable);
    }};

    Var result = scope.getVar(name);
    assertEquals(variable, result);
  }

  @Test
  public void testGetVarNull() {
    String name = "variableName";
    Var result = scope.getVar(name);
    assertNull(result);
  }

  @Test
  public void testIsDeclared() {
    String name = "variableName";
    Var variable = new Var(true, name, null, null, scope, 0, input, false, null);
    scope.vars = new LinkedHashMap<String, Var>() {{
      put(name, variable);
    }};

    assertTrue(scope.isDeclared(name, true));
  }

  @Test
  public void testIsDeclaredFalse() {
    assertFalse(scope.isDeclared("variableName", true));
  }

  @Test
  public void testGetSlot() {
    String name = "variableName";
    Var variable = new Var(true, name, null, null, scope, 0, input, false, null);
    scope.vars = new LinkedHashMap<String, Var>() {{
      put(name, variable);
    }};

    StaticSlot<JSType> slot = scope.getSlot(name);
    assertEquals(variable, slot);
  }

  @Test
  public void testGetOwnSlot() {
    String name = "variableName";
    Var variable = new Var(true, name, null, null, scope, 0, input, false, null);
    scope.vars = new LinkedHashMap<String, Var>() {{
      put(name, variable);
    }};

    StaticSlot<JSType> slot = scope.getOwnSlot(name);
    assertEquals(variable, slot);
  }

  @Test
  public void testGetVars() {
    Map<String, Var> vars = Maps.newLinkedHashMap();
    vars.put("variable1", new Var(true, "variable1", null, null, scope, 0, input, false, null));
    vars.put("variable2", new Var(true, "variable2", null, null, scope, 1, input, false, null));

    Iterator<Var> iterator = scope.getVars().iterator();
    Var variable1 = iterator.next();
    Var variable2 = iterator.next();

    assertEquals("variable1", variable1.getName());
    assertEquals("variable2", variable2.getName());
  }

  @Test
  public void testGetVarCount() {
    scope.vars = new LinkedHashMap<String, Var>() {{
      put("variable1", new Var(true, "variable1", null, null, scope, 0, input, false, null));
      put("variable2", new Var(true, "variable2", null, null, scope, 1, input, false, null));
    }};

    assertEquals(2, scope.getVarCount());
  }

  @Test
  public void testIsGlobal() {
    assertTrue(scope.isGlobal());
  }

  @Test
  public void testIsLocal() {
    Scope parentScope = mock(Scope.class);
    when(parentScope.isGlobal()).thenReturn(false);
    when(parentScope.getDepth()).thenReturn(1);

    scope = new Scope(parentScope, rootNode);

    assertFalse(scope.isGlobal());
  }

  @Test
  public void testUndelecareVariable() {
    String name = "variableName";
    Var variable = new Var(true, name, null, null, scope, 0, input, false, null);
    scope.vars = new LinkedHashMap<String, Var>() {{
      put(name, variable);
    }};

    scope.undeclare(variable);
    assertNull(scope.getVar(name));
  }

  @Test
  public void testIsDeclaredWithParent() {
    String name = "variableName";
    Var variable = new Var(true, name, null, null, scope, 0, input, false, null);
    Scope parentScope = mock(Scope.class);
    when(parentScope.isDeclared(name, true)).thenReturn(true);

    scope = new Scope(parentScope, rootNode);

    assertTrue(scope.isDeclared(name, true));
  }

  @Test
  public void testJoin() {
    Scope childScope = new Scope(scope, rootNode);
    Var childVar = mock(Var.class);
    when(childVar.getName()).thenReturn("childVar");
    when(childVar.getType()).thenReturn(new ObjectType(JSType.NUMBER));
    childScope.vars = new LinkedHashMap<String, Var>() {{
      put("childVar", childVar);
    }};

    Var parentVar = mock(Var.class);
    when(parentVar.getName()).thenReturn("parentVar");
    when(parentVar.getType()).thenReturn(new ObjectType(JSType.STRING));
    scope.vars = new LinkedHashMap<String, Var>() {{
      put("parentVar", parentVar);
    }};

    StaticScope<JSType> joinedScope = childScope.join(scope);

    assertEquals("parentVar: JSType.STRING, childVar: JSType.NUMBER", joinedScope.toString());
  }
}