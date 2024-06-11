import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeTraversal.*;
import static com.google.javascript.rhino.NodeType.*;
import static com.google.javascript.rhino.JSType.*;
import static com.google.common.collect.Iterators.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.rhino.CompilerContext;
import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.SourceName;
import com.google.javascript.rhino.Token;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.ObjectType;
import com.google.javascript.rhino.jstype.StaticScope;

@RunWith(JUnit4.class)
public class ScopeTest {

  @Test
  public void testScopeCreation() {
    // Test creating a function scope
    CompilerContext ctx = new CompilerContext(new ErrorReporter());
    Node rootNode = createFunctionNode("function foo() {}", ctx);
    Scope functionScope = new Scope(null, rootNode, ctx.getTypeRegistry());

    // Test creating a global scope
    Scope globalScope = new Scope(createBlockNode("", ctx), ctx.getTypeRegistry());

    // Test creating a bottom scope
    Scope bottomScope = new Scope(rootNode, ctx.getTypeRegistry());

    // Test getters
    assertEquals(1, functionScope.getDepth());
    assertFalse(functionScope.isBottom());
    assertTrue(functionScope.isLocal());
    assertNull(functionScope.getParent());

    assertEquals(0, globalScope.getDepth());
    assertFalse(globalScope.isBottom());
    assertTrue(globalScope.isGlobal());
    assertNull(globalScope.getParent());

    assertTrue(bottomScope.isBottom());

    // Test getParent and getParentScope
    assertEquals(globalScope, functionScope.getParentScope());
    assertEquals(bottomScope, globalScope.getParentScope());
    assertEquals(null, bottomScope.getParentScope());
  }

  @Test
  public void testScopeDeclaration() {
    CompilerContext ctx = new CompilerContext(new ErrorReporter());
    Node rootNode = createFunctionNode("function foo() {var x; var y = 5;}", ctx);
    Scope functionScope = new Scope(null, rootNode, ctx.getTypeRegistry());

    Var xVar = functionScope.declare("x", null, UNDEFINED, null);
    Var yVar = functionScope.declare("y", null, INT, null);

    assertEquals("x", xVar.getName());
    assertEquals("y", yVar.getName());

    // Test getters
    assertEquals(1, functionScope.getDepth());
    assertFalse(functionScope.isBottom());
    assertTrue(functionScope.isLocal());

    // Test getVar
    Var x = functionScope.getVar("x");
    Var y = functionScope.getVar("y");

    assertNotNull(x);
    assertNotNull(y);
    assertEquals("x", x.getName());
    assertEquals("y", y.getName());

    // Test getOwnSlot and getSlot
    assertEquals(x, functionScope.getOwnSlot("x"));
    assertEquals(y, functionScope.getSlot("y"));

    // Test isDeclared
    assertTrue(functionScope.isDeclared("x", true));
    assertTrue(functionScope.isDeclared("y", true));
    assertFalse(functionScope.isDeclared("z", true));
    assertFalse(functionScope.isDeclared("x", false));

    // Test getVars
    Iterator<Var> vars = functionScope.getVars();
    assertEquals(2, vars.size());
    assertTrue(contains(xVar, vars));
    assertTrue(contains(yVar, vars));

    // Test getVarCount
    assertEquals(2, functionScope.getVarCount());
  }

  @Test
  public void testScopeUndelearation() {
    CompilerContext ctx = new CompilerContext(new ErrorReporter());
    Node rootNode = createFunctionNode("function foo() {var x;}", ctx);
    Scope functionScope = new Scope(null, rootNode, ctx.getTypeRegistry());
    Var xVar = functionScope.declare("x", null, UNDEFINED, null);

    functionScope.undeclare(xVar);

    // Test getVar
    assertNull(functionScope.getVar("x"));

    // Test getVars
    Iterator<Var> vars = functionScope.getVars();
    assertEquals(0, vars.size());

    // Test getVarCount
    assertEquals(0, functionScope.getVarCount());
  }

  @Test
  public void testScopeWithExterns() {
    CompilerContext ctx = new CompilerContext(new ErrorReporter());
    Node rootNode = createFunctionNode("function foo() {var x;}", ctx);
    Scope functionScope = new Scope(null, rootNode, ctx.getTypeRegistry());

    // Externs are not part of the scope, they are treated as globals
    Var x = functionScope.getVar("Math");
    assertNotNull(x);
    assertTrue(x.isGlobal());
    assertTrue(x.isExtern());
  }

  @Test
  public void testScopeWithFunctions() {
    CompilerContext ctx = new CompilerContext(new ErrorReporter());
    Node rootNode = createFunctionNode("function foo() {function bar() {};}", ctx);
    Scope functionScope = new Scope(null, rootNode, ctx.getTypeRegistry());

    // Test getVar
    Var bar = functionScope.getVar("bar");
    assertNotNull(bar);
    assertFalse(bar.isGlobal());
    assertFalse(bar.isExtern());
    assertFalse(bar.isDefine());
    assertFalse(bar.isConst());

    // Test getVar for a variable that is not declared
    Var undeclared = functionScope.getVar("undeclared");
    assertNull(undeclared);

    // Test getVars
    Iterator<Var> vars = functionScope.getVars();

    // There should be only one variable declared in this function
    assertEquals(1, vars.size());
    assertTrue(vars.next() instanceof Var);

    // Test getVarCount
    assertEquals(1, functionScope.getVarCount());
  }

  @Test
  public void testScopeWithBleedingFunctions() {
    CompilerContext ctx = new CompilerContext(new ErrorReporter());
    Node rootNode = createFunctionNode("function foo() {function bar() {}}", ctx);
    Scope functionScope = new Scope(null, rootNode, ctx.getTypeRegistry());

    // Test getVar for a bleeding function
    Var bar = functionScope.getVar("bar");
    assertNotNull(bar);
    assertTrue(bar.isBleedingFunction());
  }

  @Test(expected = NullPointerException.class)
  public void testScopeNullNameNode() {
    CompilerContext ctx = new CompilerContext(new ErrorReporter());
    Node rootNode = createFunctionNode("function foo() {}", ctx);
    Scope functionScope = new Scope(null, rootNode, ctx.getTypeRegistry());
    functionScope.declare(null, null, UNDEFINED, null);
  }

  @Test(expected = NullPointerException.class)
  public void testScopeNullParent() {
    Node rootNode = createFunctionNode("function foo() {}", SourceName.guessSourceName("foo.js"));
    Scope functionScope = new Scope(null, rootNode, getTypeRegistry());
    functionScope.declare("x", null, UNDEFINED, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testScopeParentIsSelf() {
    CompilerContext ctx = new CompilerContext(new ErrorReporter());
    Node rootNode = createFunctionNode("function foo() {}", ctx);
    Scope functionScope = new Scope(functionScope, rootNode, ctx.getTypeRegistry());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testScopeInvalidTypeForThis() {
    Node rootNode = createFunctionNode("function foo() {}", SourceName.guessSourceName("foo.js"));
    Scope functionScope = new Scope(null, rootNode, getTypeRegistry());
    functionScope = new Scope(functionScope, rootNode, new ObjectType(INT));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testScopeNegativeDepth() {
    Node rootNode = createFunctionNode("function foo() {}", SourceName.guessSourceName("foo.js"));
    new Scope(null, rootNode, getTypeRegistry()).new Scope(null, rootNode, getTypeRegistry(), -1);
  }

  private static Node createFunctionNode(String code, CompilerContext ctx) {
    Node rootNode = ctx.makeFunctionDeclaration(
        "foo",
        null,
        null,
        0,
        new FunctionType(null, null, null),
        null,
        null,
        null,
        code);
    return rootNode;
  }

  private static Node createBlockNode(String code, CompilerContext ctx) {
    return ctx.makeBlock(
        null,
        null,
        null,
        null,
        code,
        0);
  }

  private static CompilerTypeRegistry getTypeRegistry() {
    CompilerContext ctx = new CompilerContext(new ErrorReporter());
    return ctx.getTypeRegistry();
  }
}