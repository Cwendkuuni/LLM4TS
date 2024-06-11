import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.DataFlowAnalysis;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.StaticScope;
import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.JSTypeNative;
import com.google.javascript.rhino.LinkedHashMap;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.StaticSlot;

import java.util.Iterator;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

public class ScopeTest {

  private ErrorReporter errorReporter;
  private Node rootNode;
  private AbstractCompiler compiler;
  private Scope scope;

  @Before
  public void setup() {
    errorReporter = mock(ErrorReporter.class);
    rootNode = mock(Node.class);
    compiler = mock(AbstractCompiler.class);
    when(compiler.getTypeRegistry()).thenReturn(mock(TypeRegistry.class));
    scope = new Scope(rootNode, compiler);
  }

  @Test
  public void testCreateGlobalScope() {
    Scope globalScope = new Scope(null, rootNode, compiler);
    assertTrue(globalScope.isGlobal());
    assertEquals(0, globalScope.getDepth());
  }

  @Test
  public void testCreateFunctionScope() {
    Scope parentScope = new Scope(null, rootNode, compiler);
    Node functionNode = mock(Node.class);
    Scope functionScope = new Scope(parentScope, functionNode);
    assertTrue(functionScope.isLocal());
    assertEquals(parentScope.getDepth() + 1, functionScope.getDepth());
  }

  @Test
  public void testGetTypeOfThis() {
    when(compiler.getTypeRegistry()).thenReturn(mock(TypeRegistry.class));
    when(compiler.getTypeRegistry().getNativeObjectType(JSTypeNative.GLOBAL_THIS)).thenReturn(mock(ObjectType.class));

    Node rootNode = mock(Node.class);
    Scope globalScope = new Scope(rootNode, compiler);
    ObjectType thisType = mock(ObjectType.class);
    when(globalScope.getTypeOfThis()).thenReturn(thisType);

    assertTrue(globalScope.isGlobal());
    assertEquals(thisType, globalScope.getTypeOfThis());
  }

  @Test
  public void testDeclareVariable() {
    String name = "foo";
    Node nameNode = mock(Node.class);
    JSType type = mock(JSType.class);
    CompilerInput input = mock(CompilerInput.class);
    boolean inferred = true;

    Var variable = scope.declare(name, nameNode, type, input, inferred);

    ArgumentCaptor<Var> varArgumentCaptor = ArgumentCaptor.forClass(Var.class);
    Mockito.verify(vars).put(name, varArgumentCaptor.capture());
    Var capturedVariable = varArgumentCaptor.getValue();
    assertEquals(name, capturedVariable.getName());
    assertEquals(nameNode, capturedVariable.getNameNode());
    assertEquals(type, capturedVariable.getType());
    assertEquals(input, capturedVariable.getInput());
    assertEquals(inferred, capturedVariable.isTypeInferred());
  }

  @Test
  public void testGetVar() {
    String name = "foo";
    Node nameNode = mock(Node.class);
    Var variable = mock(Var.class);

    when(vars.get(name)).thenReturn(variable);

    assertEquals(variable, scope.getVar(name));
  }

  @Test
  public void testUndelecareVariable() {
    String name = "foo";
    Var variable = mock(Var.class);

    scope.undeclare(variable);

    Mockito.verify(vars).remove(name);
  }

  @Test
  public void testGetParentScope() {
    Scope parentScope = mock(Scope.class);
    when(parentScope.getParentScope()).thenReturn(null);
    when(scope.getParent()).thenReturn(parentScope);

    assertNull(scope.getParentScope());
  }

  @Test
  public void testGetSlot() {
    String name = "foo";
    Var variable = mock(Var.class);

    when(vars.get(name)).thenReturn(variable);

    StaticSlot<JSType> slot = scope.getSlot(name);
    assertEquals(variable, slot);
  }

  @Test
  public void testGetOwnSlot() {
    String name = "foo";
    Var variable = mock(Var.class);

    when(vars.get(name)).thenReturn(variable);

    StaticSlot<JSType> slot = scope.getOwnSlot(name);
    assertEquals(variable, slot);
  }

  @Test
  public void testIsDeclared() {
    String name1 = "foo1";
    String name2 = "foo2";

    scope.declare(name1, null, null, null, true);
    scope.declare(name2, null, null, null, true);

    assertTrue(scope.isDeclared(name1, true));
    assertTrue(scope.isDeclared(name2, true));
    assertFalse(scope.isDeclared(name1, false));
    assertFalse(scope.isDeclared(name2, false));
  }

  @Test
  public void testGetVars() {
    String name1 = "foo1";
    String name2 = "foo2";

    scope.declare(name1, null, null, null, true);
    scope.declare(name2, null, null, null, true);

    Iterator<Var> vars = scope.getVars();
    assertTrue(vars.hasNext());
    Var variable1 = vars.next();
    assertTrue(variable1.getName().equals(name1));
    assertTrue(vars.hasNext());
    Var variable2 = vars.next();
    assertTrue(variable2.getName().equals(name2));
    assertFalse(vars.hasNext());
  }

  @Test
  public void testGetVarCount() {
    String name1 = "foo1";
    String name2 = "foo2";

    scope.declare(name1, null, null, null, true);
    scope.declare(name2, null, null, null, true);

    assertEquals(2, scope.getVarCount());
  }

  @Test
  public void testIsLocal() {
    assertTrue(scope.isLocal());
  }

  @Test
  public void testIsGlobal() {
    assertFalse(scope.isGlobal());
  }

  @Test
  public void testJoin() {
    ObjectType objectType = mock(ObjectType.class);
    StaticScope<JSType> parentScope = mock(Scope.class);
    StaticScope<JSType> childScope = new Scope(parentScope, rootNode);
    childScope.vars.put("foo", new Var(true, "foo", null, objectType, childScope, 0, null, false, null));

    StaticScope<JSType> joinedScope = DataFlowAnalysis.join(childScope, parentScope);

    ArgumentCaptor<String> nameArgumentCaptor = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<Var> varArgumentCaptor = ArgumentCaptor.forClass(Var.class);

    Mockito.verify(parentScope.vars).put(nameArgumentCaptor.capture(), varArgumentCaptor.capture());
    Var joinedVar = varArgumentCaptor.getValue();
    assertEquals(objectType, joinedVar.getType());
  }

  @Test
  public void testIsBottom() {
    assertTrue(scope.isBottom());
  }

  @Test
  public void testCreateEmptyScope() {
    Scope emptyScope = new Scope(rootNode, null);
    assertTrue(emptyScope.isBottom());
  }
}