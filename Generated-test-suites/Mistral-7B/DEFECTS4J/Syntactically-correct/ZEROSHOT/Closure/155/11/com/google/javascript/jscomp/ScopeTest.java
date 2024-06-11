import static com.google.javascript.jscomp.Scope.*;
import static org.junit.Assert.*;

import java.util.Map;

import org.junit.Test;

public class ScopeTest {

  @Test
  public void testScopeCreation() {
    Scope parentScope = new Scope(null, new Node(Token.BLOCK));
    Scope childScope = new Scope(parentScope, new Node(Token.FUNCTION));

    assertEquals(0, parentScope.getDepth());
    assertEquals(1, childScope.getDepth());
    assertEquals(null, parentScope.getParent());
    assertNotNull(childScope.getParent());
    assertEquals(parentScope, childScope.getParent());

    childScope.undeclare(childScope.getVar("x"));
    assertNull(childScope.getVar("x"));
    assertNull(parentScope.getVar("x"));

    childScope.declare("x", new Node(Token.VAR), new JSTypeInt(), null);
    Var x = childScope.getVar("x");
    assertNotNull(x);
    assertEquals("x", x.getName());
    assertEquals(childScope, x.getScope());
    assertEquals(1, childScope.getVarCount());

    assertEquals(x, parentScope.getVar("x"));
    assertEquals(x, childScope.getOwnSlot("x"));
    assertFalse(parentScope.isDeclared("x", false));
    assertTrue(childScope.isDeclared("x", false));
    assertTrue(childScope.isDeclared("x", true));
    assertFalse(parentScope.isDeclared("x", true));

    assertEquals(x, childScope.getSlot("x"));
  }

  @Test
  public void testScopeCreationGlobal() {
    Scope globalScope = new Scope(null, new Node(Token.BLOCK), null);

    assertEquals(0, globalScope.getDepth());
    assertEquals(null, globalScope.getParent());

    globalScope.declare("x", new Node(Token.VAR), new JSTypeInt(), null);
    Var x = globalScope.getVar("x");
    assertNotNull(x);
    assertEquals("x", x.getName());
    assertEquals(globalScope, x.getScope());
    assertEquals(1, globalScope.getVarCount());

    assertEquals(x, globalScope.getSlot("x"));
    assertEquals(x, globalScope.getOwnSlot("x"));
    assertTrue(globalScope.isDeclared("x", false));
    assertTrue(globalScope.isDeclared("x", true));

    Scope childScope = new Scope(globalScope, new Node(Token.FUNCTION));
    assertFalse(childScope.isDeclared("x", false));
    assertFalse(childScope.isDeclared("x", true));
    assertNull(childScope.getVar("x"));
  }

  @Test
  public void testScopeCreationFunction() {
    Scope parentScope = new Scope(null, new Node(Token.BLOCK));
    Scope childScope = new Scope(parentScope, new Node(Token.FUNCTION));

    parentScope.declare("x", new Node(Token.VAR), new JSTypeInt(), null);

    childScope.declare("x", new Node(Token.VAR), new JSTypeInt(), null);
    Var x = childScope.getVar("x");
    assertNotNull(x);
    assertEquals("x", x.getName());
    assertEquals(childScope, x.getScope());
    assertEquals(1, childScope.getVarCount());

    assertEquals(x, parentScope.getVar("x"));
    assertEquals(x, childScope.getOwnSlot("x"));
    assertTrue(childScope.isDeclared("x", false));
    assertTrue(childScope.isDeclared("x", true));
    assertFalse(parentScope.isDeclared("x", false));
    assertTrue(parentScope.isDeclared("x", true));

    assertEquals(x, parentScope.getSlot("x"));
  }

  @Test
  public void testScopeCreationFunctionWithArguments() {
    Scope parentScope = new Scope(null, new Node(Token.BLOCK));
    Scope childScope = new Scope(parentScope, new Node(Token.FUNCTION), null);

    Var arguments = childScope.getVar("arguments");
    assertNotNull(arguments);
    assertEquals("arguments", arguments.getName());
    assertEquals(childScope, arguments.getScope());
    assertFalse(arguments.isDefine());
    assertFalse(arguments.isExtern());
    assertNull(arguments.getType());
    assertNull(arguments.getNameNode());
  }

  @Test
  public void testScopeCreationGlobalWithArguments() {
    Scope globalScope = new Scope(null, new Node(Token.BLOCK), null);

    Var arguments = globalScope.getVar("arguments");
    assertNull(arguments);
  }

  @Test
  public void testScopeCreationEmpty() {
    Scope scope = new Scope(null, new Node(Token.BLOCK));

    assertEquals(0, scope.getDepth());
    assertEquals(null, scope.getParent());
    assertEquals(0, scope.getVarCount());
  }

  @Test
  public void testScopeJoin() {
    Scope parentScope = new Scope(null, new Node(Token.BLOCK));
    Scope childScope1 = new Scope(parentScope, new Node(Token.FUNCTION));
    childScope1.declare("x", new Node(Token.VAR), new JSTypeInt(), null);

    Scope childScope2 = new Scope(parentScope, new Node(Token.FUNCTION));
    childScope2.declare("x", new Node(Token.VAR), new JSTypeString(), null);

    Map<String, Var> parentMap = parentScope.vars;
    Map<String, Var> child1Map = childScope1.vars;
    Map<String, Var> child2Map = childScope2.vars;

    Var parentX = parentMap.get("x");
    Var child1X = child1Map.get("x");
    Var child2X = child2Map.get("x");

    Var joinedX = child1X.join(child2X);
    assertEquals(JSType.getLeastSupertype(child1X.getType(), child2X.getType()),
        joinedX.getType());
  }

  @Test
  public void testScopeJoinGlobal() {
    Scope globalScope = new Scope(null, new Node(Token.BLOCK), null);
    globalScope.declare("x", new Node(Token.VAR), new JSTypeInt(), null);

    Scope childScope = new Scope(globalScope, new Node(Token.FUNCTION));
    childScope.declare("x", new Node(Token.VAR), new JSTypeString(), null);

    Var globalX = globalScope.getVar("x");
    Var childX = childScope.getVar("x");

    Var joinedX = globalX.join(childX);
    assertEquals(JSType.getLeastSupertype(globalX.getType(), childX.getType()),
        joinedX.getType());
  }
}