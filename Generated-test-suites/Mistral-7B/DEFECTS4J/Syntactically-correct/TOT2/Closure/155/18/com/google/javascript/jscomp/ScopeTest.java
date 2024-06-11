import static org.junit.Assert.*;
import static com.google.javascript.jscomp.Scope.*;

import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.Test;

public class ScopeTest {

 @Test
 public void testCreateScope() {
 Scope scope = new Scope(null, new Node(Token.BLOCK));
 assertEquals(new ObjectType(JSType.getJavaObjectType()), scope.getTypeOfThis());
 assertTrue(scope.isBottom());
 assertFalse(scope.isLocal());
 assertFalse(scope.isGlobal());
 }

 @Test
 public void testCreateFunctionScope() {
 Node rootNode = new Node(Token.FUNCTION, new Node[0], new Node[0]);
 Scope scope = new Scope(null, rootNode);
 assertEquals(new ObjectType(JSType.getJavaObjectType()), scope.getTypeOfThis());
 assertFalse(scope.isBottom());
 assertFalse(scope.isLocal());
 assertFalse(scope.isGlobal());
 }

 @Test
 public void testCreateLocalScope() {
 Node rootNode = new Node(Token.BLOCK);
 Scope parentScope = new Scope(null, rootNode);
 Scope scope = new Scope(parentScope, rootNode);
 assertEquals(parentScope.getTypeOfThis(), scope.getTypeOfThis());
 assertFalse(scope.isBottom());
 assertTrue(scope.isLocal());
 assertFalse(scope.isGlobal());
 }

 @Test
 public void testCreateGlobalScope() {
 Node rootNode = new Node(Token.SCRIPT);
 Scope scope = new Scope(rootNode, null);
 assertEquals(createGlobalObjectType(), scope.getTypeOfThis());
 assertTrue(scope.isBottom());
 assertFalse(scope.isLocal());
 assertTrue(scope.isGlobal());
 }

 @Test
 public void testDeclare() {
 Scope scope = new Scope(null, new Node(Token.BLOCK));
 Var var = scope.declare("x", new Node(Token.VAR, new Node("x"), null), null, null);
 assertEquals("x", var.getName());
 assertNull(var.getType());
 assertNull(var.getJSDocInfo());
 assertFalse(var.isDefine());
 assertFalse(var.isTypeInferred());
 assertFalse(var.isNoShadow());
 assertNull(var.getInputName());
 assertNull(var.getInitialValue());
 }

 @Test
 public void testDeclareWithType() {
 Scope scope = new Scope(null, new Node(Token.BLOCK));
 Var var = scope.declare("x", new Node(Token.VAR, new Node("x"), new Node(Token.INT)), null, null);
 assertEquals("x", var.getName());
 assertEquals(JSType.INT, var.getType());
 assertNull(var.getJSDocInfo());
 assertFalse(var.isDefine());
 assertFalse(var.isTypeInferred());
 assertFalse(var.isNoShadow());
 assertNull(var.getInputName());
 assertNull(var.getInitialValue());
 }

 @Test
 public void testDeclareWithDefine() {
 Scope scope = new Scope(null, new Node(Token.BLOCK));
 Var var = scope.declare("x", new Node(Token.VAR, new Node("x"), null), null, null, true);
 assertEquals("x", var.getName());
 assertNull(var.getType());
 assertTrue(var.isDefine());
 assertFalse(var.isTypeInferred());
 assertFalse(var.isNoShadow());
 assertNull(var.getInputName());
 assertNull(var.getInitialValue());
 }

 @Test
 public void testDeclareWithTypeAndDefine() {
 Scope scope = new Scope(null, new Node(Token.BLOCK));
 Var var = scope.declare("x", new Node(Token.VAR, new Node("x"), new Node(Token.INT)), null, null, true);
 assertEquals("x", var.getName());
 assertEquals(JSType.INT, var.getType());
 assertTrue(var.isDefine());
 assertFalse(var.isTypeInferred());
 assertFalse(var.isNoShadow());
 assertNull(var.getInputName());
 assertNull(var.getInitialValue());
 }

 @Test
 public void testDeclareWithInput() {
 Scope scope = new Scope(null, new Node(Token.BLOCK));
 CompilerInput input = new CompilerInput("test.js", null, null, null, null, null, null, null, null, null, null, null, null, null);
 Var var = scope.declare("x", new Node(Token.VAR, new Node("x"), null), null, input);
 assertEquals("x", var.getName());
 assertNull(var.getType());
 assertNull(var.getJSDocInfo());
 assertFalse(var.isDefine());
 assertFalse(var.isTypeInferred());
 assertFalse(var.isNoShadow());
 assertNull(var.getInputName());
 assertNull(var.getInitialValue());
 assertEquals("test.js", var.getInputName());
 }

 @Test
 public void testDeclareWithTypeAndInput() {
 Scope scope = new Scope(null, new Node(Token.BLOCK));
 CompilerInput input = new CompilerInput("test.js", null, null, null, null, null, null, null, null, null, null, null, null, null);
 Var var = scope.declare("x", new Node(Token.VAR, new Node("x"), new Node(Token.INT)), null, input);
 assertEquals("x", var.getName());
 assertEquals(JSType.INT, var.getType());
 assertNull(var.getJSDocInfo());
 assertFalse(var.isDefine());
 assertFalse(var.isTypeInferred());
 assertFalse(var.isNoShadow());
 assertNull(var.getInputName());
 assertNull(var.getInitialValue());
 assertEquals("test.js", var.getInputName());
 }

 @Test
 public void testDeclareWithTypeDefineAndInput() {
 Scope scope = new Scope(null, new Node(Token.BLOCK));
 CompilerInput input = new CompilerInput("test.js", null, null, null, null, null, null, null, null, null, null, null, null, null);
 Var var = scope.declare("x", new Node(Token.VAR, new Node("x"), new Node(Token.INT)), null, input, true);
 assertEquals("x", var.getName());
 assertEquals(JSType.INT, var.getType());
 assertTrue(var.isDefine());
 assertFalse(var.isTypeInferred());
 assertFalse(var.isNoShadow());
 assertNull(var.getInputName());
 assertNull(var.getInitialValue());
 assertEquals("test.js", var.getInputName());
 }

 @Test
 public void testGetVar() {
 Scope scope = new Scope(null, new Node(Token.BLOCK));
 Var var1 = scope.declare("x", new Node(Token.VAR, new Node("x"), null), null, null);
 Var var2 = scope.declare("y", new Node(Token.VAR, new Node("y"), null), null, null);
 Var var3 = scope.getVar("x");
 assertEquals(var1, var3);
 Var var4 = scope.getVar("y");
 assertEquals(var2, var4);
 Var var5 = scope.getVar("z");
 assertNull(var5);
 }

 @Test
 public void testGetVarInParentScope() {
 Scope parentScope = new Scope(null, new Node(Token.BLOCK));
 Var var1 = parentScope.declare("x", new Node(Token.VAR, new Node("x"), null), null, null);
 Scope scope = new Scope(parentScope, new Node(Token.BLOCK));
 Var var2 = scope.getVar("x");
 assertEquals(var1, var2);
 }

 @Test
 public void testUndelecare() {
 Scope scope = new Scope(null, new Node(Token.BLOCK));
 Var var = scope.declare("x", new Node(Token.VAR, new Node("x"), null), null, null);
 scope.undeclare(var);
 assertNull(scope.getVar("x"));
 }

 @Test
 public void testGetVars() {
 Scope scope = new Scope(null, new Node(Token.BLOCK));
 Var var1 = scope.declare("x", new Node(Token.VAR, new Node("x"), null), null, null);
 Var var2 = scope.declare("y", new Node(Token.VAR, new Node("y"), null), null, null);
 Iterator<Var> iterator = scope.getVars();
 List<Var> list = new ArrayList<Var>();
 while (iterator.hasNext()) {
 list.add(iterator.next());
 }
 assertEquals(2, list.size());
 assertEquals(var1, list.get(0));
 assertEquals(var2, list.get(1));
 }

 @Test
 public void testGetVarCount() {
 Scope scope = new Scope(null, new Node(Token.BLOCK));
 Var var1 = scope.declare("x", new Node(Token.VAR, new Node("x"), null), null, null);
 Var var2 = scope.declare("y", new Node(Token.VAR, new Node("y"), null), null, null);
 assertEquals(2, scope.getVarCount());
 }

 @Test
 public void testIsDeclared() {
 Scope scope = new Scope(null, new Node(Token.BLOCK));
 Var var1 = scope.declare("x", new Node(Token.VAR, new Node("x"), null), null, null);
 Var var2 = scope.declare("y", new Node(Token.VAR, new Node("y"), null), null, null);
 assertTrue(scope.isDeclared("x"));
 assertTrue(scope.isDeclared("y"));
 assertFalse(scope.isDeclared("z"));
 }

 @Test
 public void testIsDeclaredRecursive() {
 Scope parentScope = new Scope(null, new Node(Token.BLOCK));
 Var var1 = parentScope.declare("x", new Node(Token.VAR, new Node("x"), null), null, null);
 Scope scope = new Scope(parentScope, new Node(Token.BLOCK));
 assertTrue(scope.isDeclared("x"));
 }

 @Test
 public void testGetSlot() {
 Scope scope = new Scope(null, new Node(Token.BLOCK));
 Var var = scope.declare("x", new Node(Token.VAR, new Node("x"), null), null, null);
 assertEquals(var, scope.getSlot("x"));
 }

 @Test
 public void testGetOwnSlot() {
 Scope scope = new Scope(null, new Node(Token.BLOCK));
 Var var = scope.declare("x", new Node(Token.VAR, new Node("x"), null), null, null);
 assertEquals(var, scope.getOwnSlot("x"));
 }

 @Test(expected = NullPointerException.class)
 public void testGetOwnSlotNull() {
 Scope scope = new Scope(null, new Node(Token.BLOCK));
 assertNull(scope.getOwnSlot(null));
 }

 @Test
 public void testGetSlotNull() {
 Scope scope = new Scope(null, new Node(Token.BLOCK));
 assertNull(scope.getSlot(null));
 }

 @Test
 public void testGetVarAndType() {
 Scope scope = new Scope(null, new Node(Token.BLOCK));
 Var var = scope.declare("x", new Node(Token.VAR, new Node("x"), new Node(Token.INT)), null, null);
 assertEquals(JSType.INT, scope.getVar("x").getType());
 }

 @Test
 public void testGetVarTypeNull() {
 Scope scope = new Scope(null, new Node(Token.BLOCK));
 Var var = scope.declare("x", new Node(Token.VAR, new Node("x"), null), null, null);
 assertNull(scope.getVar(null).getType());
 }

 @Test(expected = NullPointerException.class)
 public void testGetVarTypeNull2() {
 Scope scope = new Scope(null, new Node(Token.BLOCK));
 assertNull(scope.getVar(null).getType());
 }

 @Test
 public void testJoin() {
 Scope scope1 = new Scope(null, new Node(Token.BLOCK));
 Var var1 = scope1.declare("x", new Node(Token.VAR, new Node("x"), new Node(Token.INT)), null, null);
 Scope scope2 = new Scope(null, new Node(Token.BLOCK));
 Var var2 = scope2.declare("x", new Node(Token.VAR, new Node("x"), new Node(Token.NUMBER)), null, null);
 Scope scope3 = new Scope(scope1, new Node(Token.BLOCK));
 Scope scope4 = new Scope(scope2, new Node(Token.BLOCK));
 Scope scope5 = new Scope(scope3, scope4);
 assertEquals(JSType.join(var1.getType(), var2.getType()), scope5.getVar("x").getType());
 }
}