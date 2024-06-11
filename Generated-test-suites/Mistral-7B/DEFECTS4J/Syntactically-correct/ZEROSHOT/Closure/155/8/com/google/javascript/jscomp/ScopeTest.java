import static org.junit.Assert.*;
import static com.google.javascript.jscomp.Scope.Var.*;

import org.junit.Test;

public class ScopeTest {

  @Test
  public void testScopeCreation() {
    Scope globalScope = new Scope(null, null);
    assertNull(globalScope.getParent());
    assertNotNull(globalScope.getTypeOfThis());
    assertTrue(globalScope.isGlobal());

    Scope localScope = new Scope(globalScope, new Node(Token.FUNCTION));
    assertNotNull(localScope.getParent());
    assertSame(globalScope, localScope.getParent());
    assertNull(localScope.getTypeOfThis());
    assertFalse(localScope.isGlobal());
  }

  @Test
  public void testVariableDeclarations() {
    Scope globalScope = new Scope(null, null);
    Scope localScope = new Scope(globalScope, new Node(Token.FUNCTION));

    Var var1 = localScope.declare("x", new Node(Token.VAR), null, null);
    Var var2 = localScope.declare("y", new Node(Token.VAR), null, null);

    assertEquals(2, localScope.getVarCount());
    assertEquals("y", var1.getName());
    assertEquals("x", var2.getName());
    assertFalse(var1.isDefine());
    assertFalse(var2.isDefine());
    assertNull(var1.getType());
    assertNull(var2.getType());
    assertSame(localScope, var1.getScope());
    assertSame(localScope, var2.getScope());
  }

  @Test
  public void testVariableDeclarationWithType() {
    Scope globalScope = new Scope(null, null);
    Scope localScope = new Scope(globalScope, new Node(Token.FUNCTION));

    Var var1 = localScope.declare("x", new Node(Token.VAR), IntType.INSTANCE, null);
    Var var2 = localScope.declare("y", new Node(Token.VAR), StringType.INSTANCE, null);

    assertEquals(2, localScope.getVarCount());
    assertEquals("y", var1.getName());
    assertEquals("x", var2.getName());
    assertTrue(var1.isTypeInferred());
    assertTrue(var2.isTypeInferred());
    assertEquals(IntType.INSTANCE, var1.getType());
    assertEquals(StringType.INSTANCE, var2.getType());
    assertSame(localScope, var1.getScope());
    assertSame(localScope, var2.getScope());
  }

  @Test
  public void testVariableDeclarationWithJSDocInfo() {
    Scope globalScope = new Scope(null, null);
    Scope localScope = new Scope(globalScope, new Node(Token.FUNCTION));

    JSDocInfo info = new JSDocInfo("MyFunction", null, null);

    Var var1 = localScope.declare("x", new Node(Token.VAR), null, null, info);
    Var var2 = localScope.declare("y", new Node(Token.VAR), null, null, info);

    assertEquals(2, localScope.getVarCount());
    assertEquals("y", var1.getName());
    assertEquals("x", var2.getName());
    assertTrue(var1.isNoShadow());
    assertTrue(var2.isNoShadow());
    assertSame(info, var1.getJSDocInfo());
    assertSame(info, var2.getJSDocInfo());
  }

  @Test
  public void testVariableDeclarationWithJSDocInfoAndType() {
    Scope globalScope = new Scope(null, null);
    Scope localScope = new Scope(globalScope, new Node(Token.FUNCTION));

    JSDocInfo info = new JSDocInfo("MyFunction", null, new TypeHint("MyType"));

    Var var1 = localScope.declare("x", new Node(Token.VAR), null, null, info);
    Var var2 = localScope.declare("y", new Node(Token.VAR), null, null, info);

    assertEquals(2, localScope.getVarCount());
    assertEquals("y", var1.getName());
    assertEquals("x", var2.getName());
    assertTrue(var1.isNoShadow());
    assertTrue(var2.isNoShadow());
    assertSame(info, var1.getJSDocInfo());
    assertSame(info, var2.getJSDocInfo());
    assertEquals("MyType", var1.getJSDocInfo().getType().getName());
    assertEquals("MyType", var2.getJSDocInfo().getType().getName());
  }

  @Test
  public void testGetSlot() {
    Scope globalScope = new Scope(null, null);
    Scope localScope = new Scope(globalScope, new Node(Token.FUNCTION));

    Var var1 = localScope.declare("x", new Node(Token.VAR), null, null);
    Var var2 = localScope.declare("y", new Node(Token.VAR), null, null);

    assertEquals(var1, localScope.getSlot("x"));
    assertEquals(var2, localScope.getSlot("y"));

    // Test getting slot for non-existent variable
    assertNull(localScope.getSlot("z"));

    // Test getting slot for global variable
    Var globalVar = globalScope.declare("z", new Node(Token.VAR), null, null);
    assertEquals(globalVar, globalScope.getSlot("z"));
  }

  @Test
  public void testGetOwnSlot() {
    Scope globalScope = new Scope(null, null);
    Scope localScope = new Scope(globalScope, new Node(Token.FUNCTION));

    Var var1 = localScope.declare("x", new Node(Token.VAR), null, null);
    Var var2 = localScope.declare("y", new Node(Token.VAR), null, null);

    assertEquals(var1, localScope.getOwnSlot("x"));
    assertEquals(var2, localScope.getOwnSlot("y"));

    // Test getting slot for non-existent variable
    assertNull(localScope.getOwnSlot("z"));

    // Test getting slot for global variable
    Var globalVar = globalScope.declare("z", new Node(Token.VAR), null, null);
    assertNull(localScope.getOwnSlot("z"));
  }

  @Test
  public void testIsDeclared() {
    Scope globalScope = new Scope(null, null);
    Scope localScope = new Scope(globalScope, new Node(Token.FUNCTION));

    Var var1 = localScope.declare("x", new Node(Token.VAR), null, null);
    Var var2 = localScope.declare("y", new Node(Token.VAR), null, null);

    assertTrue(localScope.isDeclared("x"));
    assertTrue(localScope.isDeclared("y"));

    assertTrue(globalScope.isDeclared("x", false));
    assertFalse(globalScope.isDeclared("x", true));

    assertTrue(globalScope.isDeclared("y", false));
    assertFalse(globalScope.isDeclared("y", true));
  }

  @Test
  public void testGetVars() {
    Scope globalScope = new Scope(null, null);
    Scope localScope = new Scope(globalScope, new Node(Token.FUNCTION));

    Var var1 = localScope.declare("x", new Node(Token.VAR), null, null);
    Var var2 = localScope.declare("y", new Node(Token.VAR), null, null);

    Iterator<Var> vars = localScope.getVars();

    assertEquals(var1, vars.next());
    assertEquals(var2, vars.next());
    assertFalse(vars.hasNext());
  }

  @Test
  public void testGetVarCount() {
    Scope globalScope = new Scope(null, null);
    Scope localScope = new Scope(globalScope, new Node(Token.FUNCTION));

    Var var1 = localScope.declare("x", new Node(Token.VAR), null, null);
    Var var2 = localScope.declare("y", new Node(Token.VAR), null, null);

    assertEquals(2, localScope.getVarCount());
  }

  @Test
  public void testIsLocal() {
    Scope globalScope = new Scope(null, null);
    Scope localScope = new Scope(globalScope, new Node(Token.FUNCTION));

    assertTrue(localScope.isLocal());
    assertFalse(globalScope.isLocal());
  }

  @Test
  public void testIsGlobal() {
    Scope globalScope = new Scope(null, null);
    Scope localScope = new Scope(globalScope, new Node(Token.FUNCTION));

    assertFalse(localScope.isGlobal());
    assertTrue(globalScope.isGlobal());
  }
}