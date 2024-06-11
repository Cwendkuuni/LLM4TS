import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.rhino.NodeTraversal;
import com.google.javascript.rhino.ErrorReporter;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class ScopeTest {

 private static final ErrorReporter REPORTER = new ErrorReporter();
 private static final CompilerInput INPUT = new CompilerInput("test.js", "test", true);

 @Test
 public void testDeclareVariable() {
 Scope scope = new Scope(null, new Node(Token.BLOCK, 0, null));
 Var var1 = scope.declare("var1", new Node(Token.VAR, 0, null), null, INPUT);
 Var var2 = scope.declare("var1", new Node(Token.VAR, 0, null), new FunctionType(null, null), INPUT);
 assertEquals(var1, var2);
 assertEquals("var1", var1.getName());
 }

 @Test
 public void testDeclareConstant() {
 Scope scope = new Scope(null, new Node(Token.BLOCK, 0, null));
 Var var1 = scope.declare("const", new Node(Token.VAR, 0, null), new IntType(), INPUT);
 assertEquals("const", var1.getName());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDeclareSameNameTwice() {
 Scope scope = new Scope(null, new Node(Token.BLOCK, 0, null));
 scope.declare("var1", new Node(Token.VAR, 0, null), null, INPUT);
 scope.declare("var1", new Node(Token.VAR, 0, null), null, INPUT);
 }

 @Test
 public void testSetType() {
 Scope scope = new Scope(null, new Node(Token.BLOCK, 0, null));
 Var var = scope.declare("var1", new Node(Token.VAR, 0, null), null, INPUT);
 var.setType(new IntType());
 assertEquals(new IntType(), var.getType());
 }

 @Test
 public void testResolveType() {
 Scope scope = new Scope(null, new Node(Token.BLOCK, 0, null));
 Var var = scope.declare("var1", new Node(Token.VAR, 0, null), null, INPUT);
 var.resolveType(REPORTER);
 assertEquals(BottomType.INSTANCE, var.getType());
 }

 @Test
 public void testGetVar() {
 Scope parentScope = new Scope(null, new Node(Token.BLOCK, 0, null));
 Var parentVar = parentScope.declare("parentVar", new Node(Token.VAR, 0, null), null, INPUT);
 Scope childScope = new Scope(parentScope, new Node(Token.FUNCTION, 0, null));
 Var childVar = childScope.declare("childVar", new Node(Token.VAR, 0, null), null, INPUT);
 assertEquals(parentScope, childVar.getScope());
 assertEquals(parentVar, childScope.getVar("parentVar"));
 }

 @Test
 public void testGetVarNotDeclared() {
 Scope scope = new Scope(null, new Node(Token.BLOCK, 0, null));
 assertNull(scope.getVar("nonExistentVar"));
 }

 @Test
 public void testGetVarRecursively() {
 Scope parentScope = new Scope(null, new Node(Token.BLOCK, 0, null));
 Var parentVar = parentScope.declare("parentVar", new Node(Token.VAR, 0, null), null, INPUT);
 Scope childScope = new Scope(parentScope, new Node(Token.FUNCTION, 0, null));
 Var childVar = childScope.declare("childVar", new Node(Token.VAR, 0, null), null, INPUT);
 assertEquals(parentScope, childVar.getScope());
 assertEquals(parentVar, childScope.getVar("parentVar"));
 }

 @Test
 public void testIsDeclared() {
 Scope scope = new Scope(null, new Node(Token.BLOCK, 0, null));
 scope.declare("var1", new Node(Token.VAR, 0, null), null, INPUT);
 assertTrue(scope.isDeclared("var1", true));
 assertFalse(scope.isDeclared("nonExistentVar", true));
 assertFalse(scope.isDeclared("var1", false));
 }

 @Test
 public void testIsGlobal() {
 Scope globalScope = new Scope(null, new Node(Token.SCRIPT, 0, null));
 assertTrue(globalScope.isGlobal());
 Scope localScope = new Scope(globalScope, new Node(Token.FUNCTION, 0, null));
 assertFalse(localScope.isGlobal());
 }

 @Test
 public void testGetSlot() {
 Scope scope = new Scope(null, new Node(Token.BLOCK, 0, null));
 Var var = scope.declare("var1", new Node(Token.VAR, 0, null), null, INPUT);
 assertEquals(var, scope.getSlot("var1"));
 }

 @Test
 public void testGetOwnSlot() {
 Scope scope = new Scope(null, new Node(Token.BLOCK, 0, null));
 Var var = scope.declare("var1", new Node(Token.VAR, 0, null), null, INPUT);
 assertEquals(var, scope.getOwnSlot("var1"));
 }

 @Test
 public void testGetVars() {
 Scope scope = new Scope(null, new Node(Token.BLOCK, 0, null));
 scope.declare("var1", new Node(Token.VAR, 0, null), null, INPUT);
 scope.declare("var2", new Node(Token.VAR, 0, null), null, INPUT);
 Iterator<Var> vars = scope.getVars();
 assertEquals(2, vars.size());
 }

 @Test
 public void testGetVarCount() {
 Scope scope = new Scope(null, new Node(Token.BLOCK, 0, null));
 scope.declare("var1", new Node(Token.VAR, 0, null), null, INPUT);
 scope.declare("var2", new Node(Token.VAR, 0, null), null, INPUT);
 assertEquals(2, scope.getVarCount());
 }
}