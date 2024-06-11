import static org.junit.Assert.*;
import static com.google.javascript.rhino.Token.*;

import com.google.common.collect.ImmutableMap;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.rhino.NodeTraversal;
import com.google.javascript.rhino.SourceFile;
import com.google.javascript.rhino.TokenFactory;

import org.junit.Before;
import org.junit.Test;

public class ScopeTest {
  private static final String TEST_CODE =
      "function foo() {" +
      "  var a = 1;" +
      "  var b = 2;" +
      "  function bar() {" +
      "    var c = 3;" +
      "  }" +
      "  var d = 4;" +
      "}";

  private SourceFile testSrc;
  private Node rootNode;
  private Scope globalScope;
  private Scope localScope;
  private Var a;
  private Var b;
  private Var c;
  private Var d;

  @Before
  public void setUp() {
    TokenFactory tf = new TokenFactory();
    testSrc = new SourceFile("TestScope.js", TEST_CODE, tf);
    rootNode = testSrc.getAST();

    NodeTraversal nt = new NodeTraversal(rootNode) {
      @Override
      public void visit(Scope node) {
        if (node.getDepth() == 0) {
          globalScope = node;
        } else if (node.getDepth() == 1) {
          localScope = node;
        }
      }
    };
    nt.visit(rootNode);
  }

  @Test
  public void testDeclareVariables() {
    // Check that variables are correctly declared
    assertEquals(4, localScope.getVarCount());
    Var expectedVars = new Var[]{a, b, c, d};
    Var[] actualVars = localScope.getVars().toArray(new Var[0]);
    assertArrayEquals(expectedVars, actualVars);

    // Check that variables have correct names and types
    assertEquals("a", a.name);
    assertEquals(INT_TYPE, a.type);
    assertEquals("b", b.name);
    assertEquals(INT_TYPE, b.type);
    assertEquals("c", c.name);
    assertEquals(FUNCTION_TYPE, c.type);
    assertEquals("d", d.name);
    assertEquals(INT_TYPE, d.type);
  }

  @Test
  public void testGetVar() {
    // Check that variables can be correctly retrieved by name
    Var aVar = localScope.getVar("a");
    Var bVar = localScope.getVar("b");
    Var cVar = localScope.getVar("c");
    Var dVar = localScope.getVar("d");
    Var nullVar = localScope.getVar("x");

    assertEquals(a, aVar);
    assertEquals(b, bVar);
    assertNull(cVar); // c is not declared in global scope
    assertNull(dVar); // d is not declared in global scope
    assertNull(nullVar);
  }

  @Test
  public void testIsDeclared() {
    // Check that variables are correctly identified as declared
    assertTrue(localScope.isDeclared("a"));
    assertTrue(localScope.isDeclared("b"));
    assertFalse(localScope.isDeclared("x"));

    // Check that variables in parent scope are not considered as declared
    assertFalse(globalScope.isDeclared("a"));
    assertFalse(globalScope.isDeclared("b"));
  }

  @Test
  public void testUndeclareVariable() {
    // Check that variable can be correctly undeclared
    localScope.undeclare(a);
    assertEquals(3, localScope.getVarCount());
    assertFalse(localScope.getVars().contains(a));
  }

  @Test(expected = NullPointerException.class)
  public void testGetVarWhenNull() {
    // Check that null pointer exception is thrown when trying to get variable
    // from null scope
    Scope nullScope = null;
    nullScope.getVar("x");
  }

  @Test
  public void testGetTypeOfThis() {
    // Check that global scope has the correct this type
    assertEquals(GLOBAL_OBJECT_TYPE, globalScope.getTypeOfThis());

    // Check that local scope has the correct this type
    assertEquals(FUNCTION_TYPE, localScope.getTypeOfThis());
  }

  @Test
  public void testGetParent() {
    // Check that parent scope is correctly retrieved
    assertEquals(globalScope, localScope.getParent());
    assertNull(globalScope.getParent());
  }

  @Test
  public void testIsBottom() {
    // Check that global scope is not bottom
    assertFalse(globalScope.isBottom());

    // Check that empty scope is bottom
    Scope emptyScope = new Scope(new Node(TOKEN_FUNCTION, ImmutableMap.of()), null);
    assertTrue(emptyScope.isBottom());
  }

  @Test
  public void testJoin() {
    // Create two scopes with overlapping variables
    Scope scope1 = new Scope(new Node(TOKEN_FUNCTION, ImmutableMap.of()), null);
    scope1.declare("a", new Node(TOKEN_VAR, null, new Node(Token.IDENTIFIER, "a")), INT_TYPE, null);
    Scope scope2 = new Scope(new Node(TOKEN_FUNCTION, ImmutableMap.of()), null);
    scope2.declare("a", new Node(TOKEN_VAR, null, new Node(Token.IDENTIFIER, "a")), STRING_TYPE, null);

    // Join the scopes and check that resulting scope has the correct type for variable "a"
    Scope joinedScope = scope1.join(scope2);
    assertEquals(STRING_TYPE, joinedScope.getVar("a").getType());
  }
}