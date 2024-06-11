import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Scope.Var;
import org.junit.Test;
import static org.junit.Assert.*;

public class ScopeTest {

 @Test
 public void testScopeCreation() {
 Scope scope = new Scope(null, null);
 assertNull(scope.getParent());
 assertNotNull(scope.getTypeOfThis());
 }

 @Test
 public void testScopeWithParent() {
 Node rootNode = new Node(Token.FUNCTION, new int[]{}, new int[]{}, null);
 Scope parentScope = new Scope(null, rootNode);
 Scope scope = new Scope(parentScope, rootNode);
 assertEquals(parentScope, scope.getParent());
 assertSame(parentScope.getTypeOfThis(), scope.getTypeOfThis());
 }

 @Test
 public void testDeclareVariable() {
 String name = "testVariable";
 Node nameNode = new Node(Token.NAME, new int[]{}, new int[]{}, null);
 nameNode.setName(name);
 CompilerInput input = new CompilerInput("test.js", 1, 1, null);
 Scope scope = new Scope(null, null);
 Var variable = scope.declare(name, nameNode, JSType.BOOLEAN, input, true);
 assertEquals(name, variable.getName());
 assertEquals(JSType.BOOLEAN, variable.getType());
 }

 @Test
 public void testDeclareVariableWithJSDocInfo() {
 String name = "testVariable";
 Node nameNode = new Node(Token.NAME, new int[]{}, new int[]{}, null);
 nameNode.setName(name);
 JSDocInfo info = new JSDocInfo(null, null, null, null, null, null, null);
 CompilerInput input = new CompilerInput("test.js", 1, 1, null);
 Scope scope = new Scope(null, null);
 Var variable = scope.declare(name, nameNode, JSType.BOOLEAN, input, true, info);
 assertEquals(name, variable.getName());
 assertEquals(JSType.BOOLEAN, variable.getType());
 assertEquals(info, variable.getJSDocInfo());
 }

 @Test
 public void testDeclareVariableTwice() {
 String name = "testVariable";
 Node nameNode = new Node(Token.NAME, new int[]{}, new int[]{}, null);
 nameNode.setName(name);
 CompilerInput input = new CompilerInput("test.js", 1, 1, null);
 Scope scope = new Scope(null, null);
 Var variable1 = scope.declare(name, nameNode, JSType.BOOLEAN, input, true);
 Var variable2 = scope.declare(name, nameNode, JSType.NUMBER, input, true);
 assertSame(variable1, variable2);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDeclareVariableWithNullNameNode() {
 String name = "testVariable";
 CompilerInput input = new CompilerInput("test.js", 1, 1, null);
 Scope scope = new Scope(null, null);
 scope.declare(name, null, JSType.BOOLEAN, input, true);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDeclareVariableWithNegativeNameLength() {
 String name = "test";
 Node nameNode = new Node(Token.NAME, new int[]{}, new int[]{}, null);
 nameNode.setName(name);
 CompilerInput input = new CompilerInput("test.js", 1, 1, null);
 Scope scope = new Scope(null, null);
 scope.declare(name, nameNode, JSType.BOOLEAN, input, true);
 }

 @Test
 public void testDeclareVariableWithSameNameButDifferentNodes() {
 String name = "testVariable";
 Node nameNode1 = new Node(Token.NAME, new int[]{}, new int[]{}, null);
 nameNode1.setName(name);
 Node nameNode2 = new Node(Token.NAME, new int[]{}, new int[]{}, null);
 nameNode2.setName("testVariable2");
 CompilerInput input = new CompilerInput("test.js", 1, 1, null);
 Scope scope = new Scope(null, null);
 Var variable1 = scope.declare(name, nameNode1, JSType.BOOLEAN, input, true);
 Var variable2 = scope.declare(name, nameNode2, JSType.NUMBER, input, true);
 assertNotSame(variable1, variable2);
 }

 @Test
 public void testGetVar() {
 String name = "testVariable";
 Node nameNode = new Node(Token.NAME, new int[]{}, new int[]{}, null);
 nameNode.setName(name);
 CompilerInput input = new CompilerInput("test.js", 1, 1, null);
 Scope scope = new Scope(null, null);
 Var variable = scope.declare(name, nameNode, JSType.BOOLEAN, input, true);
 Var retrievedVariable = scope.getVar(name);
 assertSame(variable, retrievedVariable);
 }

 @Test
 public void testGetVarNull() {
 Scope scope = new Scope(null, null);
 Var retrievedVariable = scope.getVar("testVariable");
 assertNull(retrievedVariable);
 }

 @Test
 public void testGetVarFromParent() {
 String name = "testVariable";
 Node nameNode = new Node(Token.NAME, new int[]{}, new int[]{}, null);
 nameNode.setName(name);
 CompilerInput input = new CompilerInput("test.js", 1, 1, null);
 Scope parentScope = new Scope(null, null);
 Var parentVariable = parentScope.declare(name, nameNode, JSType.BOOLEAN, input, true);
 Scope scope = new Scope(parentScope, null);
 Var retrievedVariable = scope.getVar(name);
 assertSame(parentVariable, retrievedVariable);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetVarWithNullName() {
 Scope scope = new Scope(null, null);
 Var retrievedVariable = scope.getVar(null);
 }
}