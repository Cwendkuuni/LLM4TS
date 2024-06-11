import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.ObjectType;
import com.google.javascript.rhino.jstype.StaticScope;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.MockitoAnnotations;

public class ScopeTest {

  private Scope scope;
  private Node rootNode;
  private AbstractCompiler compiler;
  private ErrorReporter errorReporter;

  @Before
  public void setUp() {
    compiler = mock(AbstractCompiler.class);
    errorReporter = mock(ErrorReporter.class);
    rootNode = mock(Node.class);
    MockitoAnnotations.initMocks(this);
    scope = new Scope(null, rootNode, compiler, errorReporter);
  }

  @Test
  public void testCreateScopeWithNullParentAndRootNode() {
    try {
      new Scope(null, null);
      fail("Should throw exception");
    } catch (NullPointerException e) {
      // Expected
    }
  }

  @Test
  public void testCreateScopeWithNullParentButValidRootNode() {
    try {
      new Scope(null, rootNode, compiler, errorReporter);
      fail("Should throw exception");
    } catch (NullPointerException e) {
      // Expected
    }
  }

  @Test
  public void testCreateScopeWithValidParentAndNullRootNode() {
    try {
      new Scope(scope, null);
      fail("Should throw exception");
    } catch (NullPointerException e) {
      // Expected
    }
  }

  @Test
  public void testCreateScopeWithValidParentAndRootNode() {
    ObjectType thisType = mock(ObjectType.class);

    Scope parent = mock(Scope.class);
    when(parent.getDepth()).thenReturn(1);

    when(rootNode.getJSType()).thenReturn(new FunctionType(thisType, null));

    Scope scope = new Scope(parent, rootNode, compiler, errorReporter);

    ArgumentCaptor<ObjectType> thisTypeArgCaptor = ArgumentCaptor.forClass(ObjectType.class);

    verify(compiler).getTypeRegistry().getNativeObjectType(eq(GLOBAL_THIS));

    verify(scope).setTypeOfThis(thisTypeArgCaptor.capture());
    assertEquals(thisType, thisTypeArgCaptor.getValue());

    assertEquals(1, scope.getDepth());
    assertFalse(scope.isBottom());
  }

  @Test
  public void testCreateGlobalScope() {
    Node rootNode = mock(Node.class);
    ObjectType thisType = mock(ObjectType.class);

    Scope scope = new Scope(rootNode, compiler, errorReporter);

    verify(compiler).getTypeRegistry().getNativeObjectType(eq(GLOBAL_THIS));

    verify(scope).setTypeOfThis(eq(thisType));

    assertEquals(0, scope.getDepth());
    assertFalse(scope.isBottom());
  }

  @Test
  public void testCreateEmptyScope() {
    Node rootNode = mock(Node.class);
    ObjectType thisType = mock(ObjectType.class);

    Scope scope = new Scope(rootNode, thisType);

    assertEquals(0, scope.getDepth());
    assertTrue(scope.isBottom());
  }

  @Test
  public void testGetParent() {
    Scope parent = mock(Scope.class);
    when(parent.getDepth()).thenReturn(1);

    Scope scope = new Scope(parent, rootNode, compiler, errorReporter);

    assertEquals(parent, scope.getParent());
    assertEquals(1, scope.getDepth());
  }

  @Test
  public void testIsGlobal() {
    Scope scope = new Scope(null, rootNode, compiler, errorReporter);

    assertTrue(scope.isGlobal());
  }

  @Test
  public void testIsLocal() {
    Scope parent = mock(Scope.class);

    Scope scope = new Scope(parent, rootNode, compiler, errorReporter);

    assertFalse(scope.isLocal());
  }

  @Test
  public void testDeclareVariable() {
    String name = "name";
    JSType type = JSType.STRING;

    CompilerInput input = mock(CompilerInput.class);
    when(input.getName()).thenReturn("test.js");

    Var var = scope.declare(name, null, type, input, true);

    assertEquals(name, var.getName());
    assertEquals(type, var.getType());
    assertEquals(scope, var.getScope());
    assertEquals(0, var.getIndex());
    assertEquals(input, var.getInput());
    assertTrue(var.isDefine());
    assertFalse(var.isNoShadow());
    assertFalse(var.isExtern());
    assertFalse(var.isConst());
    assertNull(var.getNameNode());
  }

  @Test
  public void testDeclareVariableWithNameNode() {
    String name = "name";
    Node nameNode = mock(Node.class);

    JSType type = JSType.STRING;

    CompilerInput input = mock(CompilerInput.class);
    when(input.getName()).thenReturn("test.js");

    Var var = scope.declare(name, nameNode, type, input, true);

    assertEquals(name, var.getName());
    assertEquals(type, var.getType());
    assertEquals(scope, var.getScope());
    assertEquals(0, var.getIndex());
    assertEquals(input, var.getInput());
    assertTrue(var.isDefine());
    assertFalse(var.isNoShadow());
    assertFalse(var.isExtern());
    assertFalse(var.isConst());
    assertEquals(nameNode, var.getNameNode());
  }

  @Test
  public void testDeclareVariableWithInferredType() {
    String name = "name";
    Node nameNode = mock(Node.class);

    JSType type = JSType.NUMBER;
    Var var =
        scope.declare(name, nameNode, null, null, true); // null input to make it inferred

    assertEquals(name, var.getName());
    assertNull(var.getType());
    assertEquals(scope, var.getScope());
    assertEquals(0, var.getIndex());
    assertNull(var.getInput());
    assertTrue(var.isDefine());
    assertFalse(var.isNoShadow());
    assertFalse(var.isExtern());
    assertFalse(var.isConst());
    assertEquals(nameNode, var.getNameNode());
  }

  @Test
  public void testDeclareVariableWithExternType() {
    String name = "name";
    Node nameNode = mock(Node.class);

    CompilerInput input = mock(CompilerInput.class);
    when(input.isExtern()).thenReturn(true);

    Var var = scope.declare(name, nameNode, null, input, true);

    assertEquals(name, var.getName());
    assertNull(var.getType());
    assertEquals(scope, var.getScope());
    assertEquals(0, var.getIndex());
    assertEquals(input, var.getInput());
    assertTrue(var.isDefine());
    assertFalse(var.isNoShadow());
    assertTrue(var.isExtern());
    assertFalse(var.isConst());
    assertEquals(nameNode, var.getNameNode());
  }

  @Test
  public void testDeclareVariableWithConstType() {
    String name = "name";
    Node nameNode = mock(Node.class);

    Node constantNode = mock(Node.class);
    when(nameNode.getType()).thenReturn(Token.CONST);

    Var var = scope.declare(name, nameNode, null, null, true);

    assertEquals(name, var.getName());
    assertNull(var.getType());
    assertEquals(scope, var.getScope());
    assertEquals(0, var.getIndex());
    assertNull(var.getInput());
    assertTrue(var.isDefine());
    assertTrue(var.isNoShadow());
    assertFalse(var.isExtern());
    assertTrue(var.isConst());
    assertNull(var.getNameNode());
  }

  @Test
  public void testGetVar() {
    String name = "name";
    Var var = new Var("inferred", name, null, JSType.STRING, scope, 0, null, false, null);
    vars.put(name, var);

    assertEquals(var, scope.getVar(name));

    String otherName = "otherName";
    assertNull(scope.getVar(otherName));
  }

  @Test
  public void testGetVarFromParent() {
    String name = "name";
    Scope parent = mock(Scope.class);
    Var var = new Var("inferred", name, null, JSType.STRING, scope, 0, null, false, null);
    vars.put(name, var);

    when(parent.getVar(name)).thenReturn(new Var("declared", name, null, JSType.NUMBER, parent, 0, null, false, null));

    assertEquals(var, scope.getVar(name));
    assertEquals(new Var("declared", name, null, JSType.NUMBER, parent, 0, null, false, null), parent.getVar(name));
  }

  @Test
  public void testGetSlot() {
    String name = "name";
    Var var = new Var("inferred", name, null, JSType.STRING, scope, 0, null, false, null);
    vars.put(name, var);

    assertEquals(var, scope.getSlot(name));
  }

  @Test
  public void testGetOwnSlot() {
    String name = "name";
    Var var = new Var("inferred", name, null, JSType.STRING, scope, 0, null, false, null);
    vars.put(name, var);

    assertEquals(var, scope.getOwnSlot(name));
  }

  @Test
  public void testIsDeclared() {
    String name = "name";

    when(scope.getVar(name)).thenReturn(new Var("declared", name, null, JSType.NUMBER, scope, 0, null, false, null));

    assertTrue(scope.isDeclared(name, true));
  }

  @Test
  public void testIsDeclaredWithSameNameButDifferentScope() {
    String name = "name";

    Scope parent = mock(Scope.class);
    when(parent.isDeclared(name, true)).thenReturn(true);

    assertFalse(scope.isDeclared(name, true));
  }

  @Test
  public void testGetVars() {
    String name1 = "name1";
    String name2 = "name2";

    Var var1 = new Var("inferred", name1, null, JSType.STRING, scope, 0, null, false, null);
    Var var2 = new Var("inferred", name2, null, JSType.NUMBER, scope, 1, null, false, null);

    vars.put(name1, var1);
    vars.put(name2, var2);

    Map<String, Var> expectedVars = new LinkedHashMap<>();
    expectedVars.put(name1, var1);
    expectedVars.put(name2, var2);

    assertEquals(expectedVars, ((LinkedHashMap<String, Var>) scope.getVars()));
  }

  @Test
  public void testGetVarCount() {
    String name1 = "name1";
    String name2 = "name2";

    Var var1 = new Var("inferred", name1, null, JSType.STRING, scope, 0, null, false, null);
    Var var2 = new Var("inferred", name2, null, JSType.NUMBER, scope, 1, null, false, null);

    vars.put(name1, var1);
    vars.put(name2, var2);

    assertEquals(2, scope.getVarCount());
  }

  @Test
  public void testUndeclareVariable() {
    String name = "name";
    Var var = new Var("inferred", name, null, JSType.STRING, scope, 0, null, false, null);
    vars.put(name, var);

    scope.undeclare(var);

    assertNull(scope.getVar(name));
  }
}