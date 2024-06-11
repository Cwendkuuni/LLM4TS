import static org.junit.Assert.*;
import static com.google.javascript.rhino.Token.*;
import static com.google.javascript.rhino.node.Property.PropertyType.*;

import com.google.common.collect.Maps;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.NodeTraversal;
import com.google.javascript.rhino.SourceName;

import java.util.HashMap;
import java.util.Map;

public class ScopeTest {

  private static Node createFunctionNode(String code) {
    return NodeTraversal.parseFunction(code, new SourceName("test.js"), null);
  }

  private static Scope createScope(Node rootNode) {
    return new Scope(null, rootNode);
  }

  private static Var declareVariable(Scope scope, String name, JSType type) {
    Node nameNode = Node.newIdentifier(name);
    CompilerInput input = new CompilerInput("test.js", null, null, null, null);
    return scope.declare(name, nameNode, type, input);
  }

  @org.junit.Test
  public void testGlobalScope() {
    Scope globalScope = createScope(Node.newBlock(new SourceName("test.js")));
    assertTrue(globalScope.isGlobal());

    Var globalVar = globalScope.declare("globalVar", null, JSType.STRING, null);
    assertEquals("globalVar", globalVar.getName());
    assertEquals(JSType.STRING, globalVar.getType());

    Var globalVar2 = globalScope.declare("globalVar2", null, JSType.NUMBER, null);
    assertEquals("globalVar2", globalVar2.getName());
    assertEquals(JSType.NUMBER, globalVar2.getType());

    Var globalVar3 = globalScope.declare("globalVar3", null, JSType.BOOLEAN, null);
    assertEquals("globalVar3", globalVar3.getName());
    assertEquals(JSType.BOOLEAN, globalVar3.getType());

    Var globalVar4 = globalScope.declare("globalVar4", null, null, null);
    assertNull(globalVar4.getType());

    Var globalVar5 = globalScope.getVar("globalVar5");
    assertNull(globalVar5);

    Var globalVar6 = globalScope.getVar("globalVar6");
    assertNull(globalVar6);
  }

  @org.junit.Test
  public void testFunctionScope() {
    String code = "function foo() { var x = 5; }";
    Node rootNode = createFunctionNode(code);

    Scope functionScope = new Scope(null, rootNode);
    assertTrue(functionScope.isLocal());

    Var x = functionScope.declare("x", null, JSType.NUMBER, null);
    assertEquals("x", x.getName());
    assertEquals(JSType.NUMBER, x.getType());

    Var y = functionScope.getVar("y");
    assertNull(y);

    Var y2 = functionScope.getParent().getVar("y");
    assertEquals(JSType.UNDEFINED, y2.getType());
  }

  @org.junit.Test
  public void testNestedFunctionScope() {
    String code = "function foo() { function bar() {} }";
    Node rootNode = createFunctionNode(code);

    Scope functionScope = new Scope(null, rootNode);
    assertTrue(functionScope.isLocal());

    Scope nestedFunctionScope = functionScope.getChild();
    assertTrue(nestedFunctionScope.isLocal());

    Var bar = nestedFunctionScope.getVar("bar");
    assertNotNull(bar);
    assertEquals(JSType.FUNCTION, bar.getType());

    Var x = functionScope.getVar("x");
    assertNull(x);
  }

  @org.junit.Test
  public void testFunctionWithArguments() {
    String code = "function foo(x, y) {}";
    Node rootNode = createFunctionNode(code);

    Scope functionScope = new Scope(null, rootNode);
    assertTrue(functionScope.isLocal());

    Var x = functionScope.getSlot("x");
    Var y = functionScope.getSlot("y");

    assertEquals(VAR, x.getType());
    assertEquals(VAR, y.getType());
  }

  @org.junit.Test
  public void testFunctionWithArgumentsAndLocalVariables() {
    String code = "function foo(x, y) { var z = 5; }";
    Node rootNode = createFunctionNode(code);

    Scope functionScope = new Scope(null, rootNode);
    assertTrue(functionScope.isLocal());

    Var x = functionScope.getSlot("x");
    Var y = functionScope.getSlot("y");
    Var z = functionScope.getVar("z");

    assertEquals(VAR, x.getType());
    assertEquals(VAR, y.getType());
    assertEquals(JSType.NUMBER, z.getType());

    Var z2 = functionScope.getParent().getVar("z");
    assertNull(z2);
  }

  @org.junit.Test
  public void testVariableShadowing() {
    String code = "function foo(x) { var x = 5; }";
    Node rootNode = createFunctionNode(code);

    Scope functionScope = new Scope(null, rootNode);
    assertTrue(functionScope.isLocal());

    Var x = functionScope.getVar("x");
    assertEquals(JSType.NUMBER, x.getType());

    Var x2 = functionScope.getParent().getVar("x");
    assertEquals(VAR, x2.getType());
  }

  @org.junit.Test
  public void testVariableReuse() {
    String code = "function foo(x, y) { var x = y; }";
    Node rootNode = createFunctionNode(code);

    Scope functionScope = new Scope(null, rootNode);
    assertTrue(functionScope.isLocal());

    Var y = functionScope.getSlot("y");
    Var x = functionScope.getVar("x");

    assertEquals(VAR, y.getType());
    assertEquals(JSType.VAR, x.getType());
  }
}