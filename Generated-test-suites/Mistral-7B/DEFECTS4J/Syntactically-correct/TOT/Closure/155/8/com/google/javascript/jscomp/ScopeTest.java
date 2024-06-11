import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;
import junit.framework.TestCase;

public class ScopeTest extends TestCase {

  private ErrorReporter errorReporter;
  private CompilerInput input;
  private Node rootNode;

  @Override
  protected void setUp() throws Exception {
    super.setUp();
    errorReporter = new ErrorReporter() {};
    input = new CompilerInput("test.js", "test");
    rootNode = NodeUtil.parseMinifiedJs("function foo() { var a = 1; }");
  }

  public void testCreateScope() {
    Scope scope = new Scope(null, rootNode, errorReporter);
    assertEquals(0, scope.getDepth());
    assertFalse(scope.isBottom());
    assertNull(scope.getTypeOfThis());

    Scope parentScope = new Scope(scope, rootNode);
    assertEquals(1, scope.getDepth());
    assertTrue(scope.isBottom());
    assertEquals(new ObjectType(com.google.javascript.rhino.JSType.OBJECT), scope.getTypeOfThis());
  }

  public void testDeclareVariable() {
    Scope scope = new Scope(null, rootNode, errorReporter);
    Scope parentScope = new Scope(scope, NodeUtil.parseMinifiedJs("function foo() { var a; }"));
    Var a = parentScope.declare("a", null, NodeUtil.INT_TYPE, input);

    assertEquals(2, parentScope.getDepth());
    assertFalse(parentScope.isBottom());
    assertEquals(new ObjectType(com.google.javascript.rhino.JSType.OBJECT), parentScope.getTypeOfThis());

    assertEquals("a", a.getName());
    assertNotNull(a.getNameNode());
    assertEquals(NodeUtil.INT_TYPE, a.getType());
    assertFalse(a.isTypeInferred());
    assertNull(a.getParentNode());
    assertFalse(a.isBleedingFunction());
    assertFalse(a.isDefine());
    assertFalse(a.isConst());
    assertEquals(0, a.getIndex());
    assertNull(a.getInputName());
    assertFalse(a.isNoShadow());
  }

  public void testUndelecareVariable() {
    Scope scope = new Scope(null, rootNode, errorReporter);
    Scope parentScope = new Scope(scope, NodeUtil.parseMinifiedJs("function foo() { var a; }"));
    Var a = parentScope.declare("a", null, NodeUtil.INT_TYPE, input);
    parentScope.undeclare(a);
    assertNull(parentScope.getSlot("a"));
  }

  public void testGetVar() {
    Scope scope = new Scope(null, rootNode, errorReporter);
    Scope parentScope = new Scope(scope, NodeUtil.parseMinifiedJs("function foo() { var a = 1; }"));
    Var a = parentScope.declare("a", null, NodeUtil.INT_TYPE, input);

    Var result = scope.getVar("a");
    assertEquals(a, result);

    rootNode = NodeUtil.parseMinifiedJs("function foo() { var a; var b = a; }");
    parentScope = new Scope(scope, rootNode);
    a = parentScope.declare("a", null, NodeUtil.INT_TYPE, input);
    Var b = parentScope.declare("b", null, NodeUtil.INT_TYPE, input);

    result = scope.getVar("a");
    assertNull(result);

    result = scope.getVar("b");
    assertEquals(b, result);
  }

  public void testIsDeclared() {
    Scope scope = new Scope(null, rootNode, errorReporter);
    Scope parentScope = new Scope(scope, NodeUtil.parseMinifiedJs("function foo() { var a; }"));
    Var a = parentScope.declare("a", null, NodeUtil.INT_TYPE, input);

    assertTrue(parentScope.isDeclared("a", false));
    assertTrue(parentScope.isDeclared("a", true));
    assertFalse(scope.isDeclared("a", false));
    assertFalse(scope.isDeclared("a", true));
  }

  public void testGetVars() {
    Scope scope = new Scope(null, rootNode, errorReporter);
    Scope parentScope = new Scope(scope, NodeUtil.parseMinifiedJs("function foo() { var a; var b; }"));
    Var a = parentScope.declare("a", null, NodeUtil.INT_TYPE, input);
    Var b = parentScope.declare("b", null, NodeUtil.INT_TYPE, input);

    Iterator<Var> vars = parentScope.getVars();
    assertEquals(a, vars.next());
    assertEquals(b, vars.next());
    assertFalse(vars.hasNext());
  }

  public void testGetVarCount() {
    Scope scope = new Scope(null, rootNode, errorReporter);
    Scope parentScope = new Scope(scope, NodeUtil.parseMinifiedJs("function foo() { var a; var b; }"));
    Var a = parentScope.declare("a", null, NodeUtil.INT_TYPE, input);
    Var b = parentScope.declare("b", null, NodeUtil.INT_TYPE, input);

    assertEquals(2, parentScope.getVarCount());
  }

  public void testIsLocal() {
    Scope scope = new Scope(null, rootNode, errorReporter);
    Scope parentScope = new Scope(scope, NodeUtil.parseMinifiedJs("function foo() { var a; }"));
    Var a = parentScope.declare("a", null, NodeUtil.INT_TYPE, input);

    assertTrue(parentScope.isLocal());
    assertFalse(scope.isLocal());
  }

  public void testIsGlobal() {
    Scope scope = new Scope(null, NodeUtil.parseMinifiedJs("var a = 1;"), errorReporter);
    assertTrue(scope.isGlobal());

    Scope parentScope = new Scope(scope, NodeUtil.parseMinifiedJs("function foo() { }"));
    assertFalse(parentScope.isGlobal());
  }
}