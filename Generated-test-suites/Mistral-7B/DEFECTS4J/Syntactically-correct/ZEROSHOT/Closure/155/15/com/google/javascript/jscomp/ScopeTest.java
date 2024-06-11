import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.Maps;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.NodeTraversal;
import com.google.javascript.rhino.NodeUtil;
import com.google.javascript.rhino.TypeChecker;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.ObjectType;
import com.google.javascript.rhino.jstype.StaticScope;
import com.google.javascript.rhino.jstype.StaticSlot;
import com.google.javascript.rhino.jstype.StaticType;
import com.google.javascript.rhino.jstype.ThisType;
import com.google.javascript.rhino.jstype.UndefinedType;

import java.util.Map;

public class ScopeTest {
  private static final Node ROOT_NODE = NodeUtil.parseJsFunction("function test() {}");

  // Test constructor with parent
  @org.junit.Test
  public void testScopeConstructionWithParent() {
    Node childRootNode = NodeUtil.parseJsFunction("function childTest() {}");
    Scope parentScope = new Scope(null, ROOT_NODE);
    Scope childScope = new Scope(parentScope, childRootNode);

    assertEquals(ROOT_NODE, childScope.getRootNode());
    assertEquals(parentScope, childScope.getParent());

    ObjectType parentThisType = new ThisType(Maps.newHashMap());
    ObjectType childThisType = new ThisType(Maps.newHashMap());
    childThisType.put("childTest", new FunctionType(UndefinedType.instance(), new FunctionType(
        JSType.VOID, new FunctionType(JSType.VOID, new FunctionType(JSType.VOID, JSType.ANY)))));
    parentScope.thisType = parentThisType;
    childScope.thisType = childThisType;

    assertTrue(childScope.isLocal());
    assertFalse(childScope.isGlobal());
  }

  // Test constructor with root node only
  @org.junit.Test
  public void testScopeConstructionWithRootNode() {
    Scope globalScope = new Scope(ROOT_NODE, null);

    assertEquals(ROOT_NODE, globalScope.getRootNode());
    assertNull(globalScope.getParent());

    ObjectType globalThisType = new ThisType(Maps.newHashMap());
    globalScope.thisType = globalThisType;

    assertTrue(globalScope.isGlobal());
    assertFalse(globalScope.isLocal());
  }

  // Test constructor with root node and this type
  @org.junit.Test
  public void testScopeConstructionWithRootNodeAndThisType() {
    Node rootNode = NodeUtil.parseJsFunction("function test() {}");
    ObjectType thisType = new ThisType(Maps.newHashMap());

    Scope globalScope = new Scope(rootNode, thisType);

    assertEquals(rootNode, globalScope.getRootNode());
    assertNull(globalScope.getParent());

    assertTrue(globalScope.isGlobal());
    assertFalse(globalScope.isLocal());
  }

  // Test constructor with bottom
  @org.junit.Test
  public void testScopeConstructionWithBottom() {
    Node rootNode = NodeUtil.parseJsFunction("function test() {}");

    Scope bottomScope = new Scope(rootNode, new ObjectType(Maps.newHashMap()));

    assertEquals(rootNode, bottomScope.getRootNode());
    assertNull(bottomScope.getParent());

    assertTrue(bottomScope.isBottom());
    assertFalse(bottomScope.isLocal());
    assertFalse(bottomScope.isGlobal());
  }

  // Test getDepth
  @org.junit.Test
  public void testGetDepth() {
    Node childRootNode = NodeUtil.parseJsFunction("function childTest() {}");
    Scope parentScope = new Scope(null, ROOT_NODE);
    Scope childScope = new Scope(parentScope, childRootNode);

    assertEquals(1, childScope.getDepth());
    assertEquals(0, parentScope.getDepth());
  }

  // Test isBottom
  @org.junit.Test
  public void testIsBottom() {
    Node rootNode = NodeUtil.parseJsFunction("function test() {}");

    Scope globalScope = new Scope(rootNode, null);
    Scope bottomScope = new Scope(rootNode, new ObjectType(Maps.newHashMap()));

    assertFalse(globalScope.isBottom());
    assertTrue(bottomScope.isBottom());
  }

  // Test getParent
  @org.junit.Test
  public void testGetParent() {
    Node childRootNode = NodeUtil.parseJsFunction("function childTest() {}");
    Scope parentScope = new Scope(null, ROOT_NODE);
    Scope childScope = new Scope(parentScope, childRootNode);

    assertEquals(parentScope, childScope.getParent());
    assertNull(parentScope.getParent());
  }

  // Test getTypeOfThis
  @org.junit.Test
  public void testGetTypeOfThis() {
    Node rootNode = NodeUtil.parseJsFunction("function test() {}");

    ObjectType parentThisType = new ThisType(Maps.newHashMap());
    ObjectType childThisType = new ThisType(Maps.newHashMap());
    childThisType.put("childTest", new FunctionType(UndefinedType.instance(), new FunctionType(
        JSType.VOID, new FunctionType(JSType.VOID, new FunctionType(JSType.VOID, JSType.ANY)))));
    Scope parentScope = new Scope(null, rootNode);
    parentScope.thisType = parentThisType;
    Scope childScope = new Scope(parentScope, NodeUtil.parseJsFunction("function childTest() {}"));
    childScope.thisType = childThisType;

    assertEquals(parentThisType, childScope.getTypeOfThis());
  }

  // Test isLocal
  @org.junit.Test
  public void testIsLocal() {
    Node childRootNode = NodeUtil.parseJsFunction("function childTest() {}");
    Scope parentScope = new Scope(null, ROOT_NODE);
    Scope childScope = new Scope(parentScope, childRootNode);

    assertTrue(childScope.isLocal());
    assertFalse(parentScope.isLocal());
  }

  // Test isGlobal
  @org.junit.Test
  public void testIsGlobal() {
    Node rootNode = NodeUtil.parseJsFunction("function test() {}");
    Scope globalScope = new Scope(rootNode, null);

    assertTrue(globalScope.isGlobal());
    assertFalse(new Scope(null, new Node("function test() {}")).isGlobal());
  }

  // Test getVars
  @org.junit.Test
  public void testGetVars() {
    Node rootNode = NodeUtil.parseJsFunction("function test() {" +
        "var x = 1; " +
        "var y = 2; " +
        "var z = 3; " +
        "}");
    NodeTraversal traversal = new NodeTraversal(rootNode);
    Scope scope = new Scope(null, rootNode);

    assertEquals(3, scope.getVars().size());
  }

  // Test declare
  @org.junit.Test
  public void testDeclare() {
    Node rootNode = NodeUtil.parseJsFunction("function test() {" +
        "var x = 1; " +
        "var y = 2; " +
        "}");
    Scope scope = new Scope(null, rootNode);

    Var xVar = scope.declare("x", NodeUtil.parseNameNode("x"), JSType.getNumberType(), null);
    Var yVar = scope.declare("y", NodeUtil.parseNameNode("y"), JSType.getNumberType(), null);

    assertEquals("x", xVar.getName());
    assertEquals("y", yVar.getName());

    assertEquals(2, scope.getVars().size());

    assertEquals(JSType.getNumberType(), xVar.getType());
    assertEquals(JSType.getNumberType(), yVar.getType());

    assertEquals(1, xVar.getIndex());
    assertEquals(2, yVar.getIndex());

    assertTrue(xVar.isDefine());
    assertTrue(yVar.isDefine());

    assertEquals(NodeUtil.parseNameNode("x"), xVar.getNameNode());
    assertEquals(NodeUtil.parseNameNode("y"), yVar.getNameNode());
  }

  // Test undeclare
  @org.junit.Test
  public void testUndeclare() {
    Node rootNode = NodeUtil.parseJsFunction("function test() {" +
        "var x = 1; " +
        "var y = 2; " +
        "}");
    Scope scope = new Scope(null, rootNode);
    Var xVar = scope.declare("x", NodeUtil.parseNameNode("x"), JSType.getNumberType(), null);
    Var yVar = scope.declare("y", NodeUtil.parseNameNode("y"), JSType.getNumberType(), null);

    scope.undeclare(xVar);

    assertEquals(1, scope.getVars().size());
    assertFalse(xVar.isDeclared(scope, false));
    assertTrue(yVar.isDeclared(scope, false));
  }

  // Test getSlot
  @org.junit.Test
  public void testGetSlot() {
    Node rootNode = NodeUtil.parseJsFunction("function test() {" +
        "var x = 1; " +
        "var y = 2; " +
        "}");
    Scope scope = new Scope(null, rootNode);
    Var xVar = scope.declare("x", NodeUtil.parseNameNode("x"), JSType.getNumberType(), null);
    Var yVar = scope.declare("y", NodeUtil.parseNameNode("y"), JSType.getNumberType(), null);

    StaticSlot<JSType> xSlot = scope.getSlot("x");
    StaticSlot<JSType> ySlot = scope.getSlot("y");

    assertEquals(xVar, xSlot);
    assertEquals(yVar, ySlot);
  }

  // Test getOwnSlot
  @org.junit.Test
  public void testGetOwnSlot() {
    Node rootNode = NodeUtil.parseJsFunction("function test() {" +
        "var x = 1; " +
        "var y = 2; " +
        "}");
    Scope scope = new Scope(null, rootNode);
    Var xVar = scope.declare("x", NodeUtil.parseNameNode("x"), JSType.getNumberType(), null);
    Var yVar = scope.declare("y", NodeUtil.parseNameNode("y"), JSType.getNumberType(), null);

    StaticSlot<JSType> xOwnSlot = scope.getOwnSlot("x");
    StaticSlot<JSType> yOwnSlot = scope.getOwnSlot("y");

    assertEquals(xVar, xOwnSlot);
    assertNull(yOwnSlot);
  }

  // Test isDeclared
  @org.junit.Test
  public void testIsDeclared() {
    Node rootNode = NodeUtil.parseJsFunction("function test() {" +
        "var x = 1; " +
        "var y = 2; " +
        "}");
    Scope scope = new Scope(null, rootNode);
    Var xVar = scope.declare("x", NodeUtil.parseNameNode("x"), JSType.getNumberType(), null);
    Var yVar = scope.declare("y", NodeUtil.parseNameNode("y"), JSType.getNumberType(), null);

    assertTrue(scope.isDeclared("x", false));
    assertTrue(scope.isDeclared("x", true));
    assertTrue(scope.isDeclared("y", false));
    assertTrue(scope.isDeclared("y", true));

    assertFalse(scope.isDeclared("z", false));
    assertFalse(scope.isDeclared("z", true));

    assertFalse(scope.getParent().isDeclared("x", false));
    assertFalse(scope.getParent().isDeclared("x", true));
  }
}