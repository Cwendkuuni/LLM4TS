import static org.junit.Assert.*;
import static com.google.javascript.jscomp.Scope.*;

import com.google.common.collect.ImmutableMap;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Type;
import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.NodeUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ScopeTest {

  private static ErrorReporter errorReporter;
  private static Node rootNode;
  private static Scope scope;

  @org.junit.BeforeClass
  public static void setUp() {
    // Setup JSDocInfo and ErrorReporter for tests
    errorReporter = new ErrorReporter();
    rootNode = new Node("function foo() { var x = 1; var y = 2; }");
  }

  @org.junit.After
  public void tearDown() {
    if (scope != null) {
      scope.undeclareAll();
    }
  }

  /**
   * Test constructor when rootNode is a function node
   */
  @org.junit.Test
  public void testConstructFunction() {
    scope = new Scope(rootNode, new AbstractCompiler() {});
    assertEquals(1, scope.getDepth());
    assertFalse(scope.isBottom());
  }

  /**
   * Test constructor when rootNode is a block node
   */
  @org.junit.Test
  public void testConstructBlock() {
    rootNode = new Node("{}");
    scope = new Scope(rootNode, new AbstractCompiler() {});
    assertEquals(0, scope.getDepth());
    assertFalse(scope.isBottom());
  }

  /**
   * Test constructor when rootNode is bottom of the lattice
   */
  @org.junit.Test
  public void testConstructBottom() {
    rootNode = new Node("{}");
    scope = new Scope(rootNode, new ObjectType(JSType.NULL));
    assertEquals(0, scope.getDepth());
    assertTrue(scope.isBottom());
  }

  /**
   * Test getParentScope
   */
  @org.junit.Test
  public void testGetParentScope() {
    scope = new Scope(rootNode, new AbstractCompiler() {});
    assertNull(scope.getParent());

    Node blockNode = new Node("{}");
    Scope blockScope = new Scope(blockNode, scope);
    assertEquals(scope, blockScope.getParent());
  }

  /**
   * Test getTypeOfThis
   */
  @org.junit.Test
  public void testGetTypeOfThis() {
    scope = new Scope(rootNode, new AbstractCompiler() {});
    Node functionNode = new Node("function foo() { var x = 1; }");
    Scope functionScope = new Scope(functionNode, scope);
    assertEquals(functionScope.thisType, functionNode.getJSType());
  }

  /**
   * Test isBottom
   */
  @org.junit.Test
  public void testIsBottom() {
    scope = new Scope(rootNode, new AbstractCompiler() {});
    assertFalse(scope.isBottom());

    Scope bottomScope = new Scope(rootNode, new ObjectType(JSType.NULL));
    assertTrue(bottomScope.isBottom());
  }

  /**
   * Test getDepth
   */
  @org.junit.Test
  public void testGetDepth() {
    scope = new Scope(rootNode, new AbstractCompiler() {});
    assertEquals(1, scope.getDepth());

    Node blockNode = new Node("{}");
    Scope blockScope = new Scope(blockNode, scope);
    assertEquals(2, blockScope.getDepth());
  }

  /**
   * Test declare
   */
  @org.junit.Test
  public void testDeclare() {
    scope = new Scope(rootNode, new AbstractCompiler() {});
    Var x = scope.declare("x", rootNode.getFirstChild(), Type.INT, null);
    assertEquals("x", x.getName());
    assertEquals(Type.INT, x.getType());
    assertFalse(x.isDefine());

    Var y = scope.declare("y", rootNode.getFirstChild().getNextSibling(), Type.STRING, null, false);
    assertEquals("y", y.getName());
    assertEquals(Type.STRING, y.getType());
    assertTrue(y.isDefine());

    Var z = scope.declare("z", rootNode.getFirstChild().getNextSibling().getNextSibling(), Type.BOOLEAN, null, false);
    assertEquals("z", z.getName());
    assertEquals(Type.BOOLEAN, z.getType());
    assertFalse(z.isDefine());
  }

  /**
   * Test undeclareAll
   */
  @org.junit.Test
  public void testUndeclareAll() {
    scope = new Scope(rootNode, new AbstractCompiler() {});
    Var x = scope.declare("x", rootNode.getFirstChild(), Type.INT, null);
    Var y = scope.declare("y", rootNode.getFirstChild().getNextSibling(), Type.STRING, null, false);
    Var z = scope.declare("z", rootNode.getFirstChild().getNextSibling().getNextSibling(), Type.BOOLEAN, null, false);

    scope.undeclareAll();
    assertNull(scope.getVar("x"));
    assertNull(scope.getVar("y"));
    assertNull(scope.getVar("z"));
  }

  /**
   * Test getVar
   */
  @org.junit.Test
  public void testGetVar() {
    scope = new Scope(rootNode, new AbstractCompiler() {});
    Var x = scope.declare("x", rootNode.getFirstChild(), Type.INT, null);
    Var y = scope.declare("y", rootNode.getFirstChild().getNextSibling(), Type.STRING, null, false);
    Var z = scope.declare("z", rootNode.getFirstChild().getNextSibling().getNextSibling(), Type.BOOLEAN, null, false);

    assertEquals(x, scope.getVar("x"));
    assertEquals(y, scope.getVar("y"));
    assertEquals(z, scope.getVar("z"));

    // Test when variable is not declared
    assertNull(scope.getVar("w"));

    // Test when variable is in parent scope
    Node functionNode = new Node("function foo() { var x = 1; }");
    Scope functionScope = new Scope(functionNode, scope);
    assertEquals(scope.getVar("x"), functionScope.getVar("x"));
  }

  /**
   * Test isDeclared
   */
  @org.junit.Test
  public void testIsDeclared() {
    scope = new Scope(rootNode, new AbstractCompiler() {});
    Var x = scope.declare("x", rootNode.getFirstChild(), Type.INT, null);
    Var y = scope.declare("y", rootNode.getFirstChild().getNextSibling(), Type.STRING, null, false);
    Var z = scope.declare("z", rootNode.getFirstChild().getNextSibling().getNextSibling(), Type.BOOLEAN, null, false);

    assertTrue(scope.isDeclared("x"));
    assertTrue(scope.isDeclared("y"));
    assertTrue(scope.isDeclared("z"));

    // Test when variable is not declared
    assertFalse(scope.isDeclared("w"));

    // Test when variable is in parent scope
    Node functionNode = new Node("function foo() { var x = 1; }");
    Scope functionScope = new Scope(functionNode, scope);
    assertTrue(functionScope.isDeclared("x"));
  }

  /**
   * Test getVars
   */
  @org.junit.Test
  public void testGetVars() {
    scope = new Scope(rootNode, new AbstractCompiler() {});
    Var x = scope.declare("x", rootNode.getFirstChild(), Type.INT, null);
    Var y = scope.declare("y", rootNode.getFirstChild().getNextSibling(), Type.STRING, null, false);
    Var z = scope.declare("z", rootNode.getFirstChild().getNextSibling().getNextSibling(), Type.BOOLEAN, null, false);

    List<Var> vars = new ArrayList<>();
    for (Var var : scope.getVars()) {
      vars.add(var);
    }

    assertEquals(3, vars.size());
    assertTrue(vars.contains(x));
    assertTrue(vars.contains(y));
    assertTrue(vars.contains(z));
  }

  /**
   * Test getVarCount
   */
  @org.junit.Test
  public void testGetVarCount() {
    scope = new Scope(rootNode, new AbstractCompiler() {});
    Var x = scope.declare("x", rootNode.getFirstChild(), Type.INT, null);
    Var y = scope.declare("y", rootNode.getFirstChild().getNextSibling(), Type.STRING, null, false);
    Var z = scope.declare("z", rootNode.getFirstChild().getNextSibling().getNextSibling(), Type.BOOLEAN, null, false);

    assertEquals(3, scope.getVarCount());
  }

  /**
   * Test isGlobal
   */
  @org.junit.Test
  public void testIsGlobal() {
    Scope globalScope = new Scope(new Node("{}"), new AbstractCompiler() {});
    assertTrue(globalScope.isGlobal());

    Scope functionScope = new Scope(new Node("function foo() {}"), globalScope);
    assertFalse(functionScope.isGlobal());
  }

  /**
   * Test isLocal
   */
  @org.junit.Test
  public void testIsLocal() {
    Scope globalScope = new Scope(new Node("{}"), new AbstractCompiler() {});
    assertFalse(globalScope.isLocal());

    Scope functionScope = new Scope(new Node("function foo() {}"), globalScope);
    assertTrue(functionScope.isLocal());
  }

  /**
   * Test getSlot
   */
  @org.junit.Test
  public void testGetSlot() {
    scope = new Scope(rootNode, new AbstractCompiler() {});
    Var x = scope.declare("x", rootNode.getFirstChild(), Type.INT, null);
    Var y = scope.declare("y", rootNode.getFirstChild().getNextSibling(), Type.STRING, null, false);
    Var z = scope.declare("z", rootNode.getFirstChild().getNextSibling().getNextSibling(), Type.BOOLEAN, null, false);

    assertEquals(x, scope.getSlot("x"));
    assertEquals(y, scope.getSlot("y"));
    assertEquals(z, scope.getSlot("z"));

    // Test when variable is not declared
    assertNull(scope.getSlot("w"));

    // Test when variable is in parent scope
    Node functionNode = new Node("function foo() { var x = 1; }");
    Scope functionScope = new Scope(functionNode, scope);
    assertEquals(scope.getSlot("x"), functionScope.getSlot("x"));
  }

  /**
   * Test getOwnSlot
   */
  @org.junit.Test
  public void testGetOwnSlot() {
    scope = new Scope(rootNode, new AbstractCompiler() {});
    Var x = scope.declare("x", rootNode.getFirstChild(), Type.INT, null);
    Var y = scope.declare("y", rootNode.getFirstChild().getNextSibling(), Type.STRING, null, false);
    Var z = scope.declare("z", rootNode.getFirstChild().getNextSibling().getNextSibling(), Type.BOOLEAN, null, false);

    assertEquals(x, scope.getOwnSlot("x"));
    assertEquals(y, scope.getOwnSlot("y"));
    assertEquals(z, scope.getOwnSlot("z"));

    // Test when variable is not declared
    assertNull(scope.getOwnSlot("w"));
  }

  /**
   * Test join
   */
  @org.junit.Test
  public void testJoin() {
    Scope parentScope = new Scope(rootNode, new AbstractCompiler() {});
    Scope childScope = new Scope(new Node("function foo() { var x = 1; }"), parentScope);

    Map<String, JSType> parentMap = new ImmutableMap.Builder<String, JSType>()
        .put("x", Type.INT)
        .build();

    Map<String, JSType> childMap = new ImmutableMap.Builder<String, JSType>()
        .put("x", Type.NUMBER)
        .build();

    Map<String, JSType> resultMap = childScope.join(parentMap);
    assertEquals(Type.NUMBER, resultMap.get("x"));

    // Test with bottom scope
    Map<String, JSType> bottomMap = new ImmutableMap.Builder<String, JSType>()
        .build();

    resultMap = childScope.join(bottomMap);
    assertEquals(Type.UNKNOWN, resultMap.get("x"));
  }
}