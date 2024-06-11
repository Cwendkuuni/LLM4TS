import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.ScopeTest;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.compiler.AbstractCompiler;
import com.google.javascript.rhino.compiler.CompilerInput;
import com.google.javascript.rhino.jstype.JSType;
import org.junit.Assert;
import org.junit.Test;

public class ScopeTest {

 @Test
 public void testCreateScopeWithParentAndRootNode() {
 Scope scope = new Scope(new Scope(null, null), new Node(Token.FUNCTION, null, null));
 Assert.assertEquals(0, scope.getDepth());
 Assert.assertEquals(false, scope.isBottom());
 }

 @Test
 public void testCreateGlobalScope() {
 Scope scope = new Scope(new Node(Token.BLOCK, null, null), new AbstractCompiler() {});
 Assert.assertEquals(0, scope.getDepth());
 Assert.assertEquals(false, scope.isBottom());
 }

 @Test
 public void testCreateEmptyScope() {
 Scope scope = new Scope(new Node(Token.FUNCTION, null, null), new ObjectType(JSType.NULL));
 Assert.assertEquals(0, scope.getDepth());
 Assert.assertEquals(true, scope.isBottom());
 }

 @Test
 public void testDeclareVariable() {
 Scope scope = new Scope(null, new Node(Token.FUNCTION, null, null));
 CompilerInput input = new CompilerInput("test.js", "test");
 String variableName = "testVariable";
 JSType variableType = JSType.STRING;
 Scope.Var declaredVariable = scope.declare(variableName, new Node(Token.NAME, variableName), variableType, input);

 Assert.assertEquals(variableName, declaredVariable.getName());
 Assert.assertEquals(variableType, declaredVariable.getType());
 Assert.assertEquals(input, declaredVariable.getInput());
 Assert.assertEquals(false, declaredVariable.isTypeInferred());
 }

 @Test
 public void testDeclareVariableWithJSDocInfo() {
 Scope scope = new Scope(null, new Node(Token.FUNCTION, null, null));
 CompilerInput input = new CompilerInput("test.js", "test");
 String variableName = "testVariable";
 Node nameNode = new Node(Token.NAME, variableName);
 JSType variableType = JSType.NUMBER;
 JSDocInfo jsdocInfo = new JSDocInfo("@define testVariable");
 Scope.Var declaredVariable = scope.declare(variableName, nameNode, variableType, input, false, jsdocInfo);

 Assert.assertEquals(variableName, declaredVariable.getName());
 Assert.assertEquals(variableType, declaredVariable.getType());
 Assert.assertEquals(input, declaredVariable.getInput());
 Assert.assertEquals(false, declaredVariable.isTypeInferred());
 Assert.assertEquals(jsdocInfo, declaredVariable.getJSDocInfo());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDeclareVariableWithDuplicateName() {
 Scope scope = new Scope(null, new Node(Token.FUNCTION, null, null));
 CompilerInput input = new CompilerInput("test.js", "test");
 String variableName = "testVariable";
 Node nameNode = new Node(Token.NAME, variableName);
 JSType variableType = JSType.NUMBER;
 Scope.Var declaredVariable1 = scope.declare(variableName, nameNode, variableType, input);
 Scope.Var declaredVariable2 = scope.declare(variableName, nameNode, variableType, input);
 }

 @Test
 public void testGetVariable() {
 Scope scope = new Scope(null, new Node(Token.FUNCTION, null, null));
 String variableName = "testVariable";
 CompilerInput input = new CompilerInput("test.js", "test");
 Scope.Var declaredVariable = scope.declare(variableName, new Node(Token.NAME, variableName), JSType.NUMBER, input);
 Scope.Var variable = scope.getVar(variableName);

 Assert.assertEquals(declaredVariable, variable);
 }

 @Test
 public void testUndeleclareVariable() {
 Scope scope = new Scope(null, new Node(Token.FUNCTION, null, null));
 String variableName = "testVariable";
 CompilerInput input = new CompilerInput("test.js", "test");
 Scope.Var declaredVariable = scope.declare(variableName, new Node(Token.NAME, variableName), JSType.NUMBER, input);
 scope.undeclare(declaredVariable);
 Scope.Var variable = scope.getVar(variableName);

 Assert.assertEquals(null, variable);
 }

 @Test
 public void testIsDeclared() {
 Scope scope = new Scope(null, new Node(Token.FUNCTION, null, null));
 String variableName1 = "testVariable1";
 String variableName2 = "testVariable2";
 Scope.Var declaredVariable1 = scope.declare(variableName1, new Node(Token.NAME, variableName1), JSType.NUMBER, null);
 Scope.Var declaredVariable2 = scope.declare(variableName2, new Node(Token.NAME, variableName2), JSType.STRING, null);

 Assert.assertTrue(scope.isDeclared(variableName1, true));
 Assert.assertTrue(scope.isDeclared(variableName2, true));
 Assert.assertFalse(scope.isDeclared(variableName1, false));
 Assert.assertFalse(scope.isDeclared(variableName2, false));
 }

 @Test
 public void testGetVars() {
 Scope scope = new Scope(null, new Node(Token.FUNCTION, null, null));
 String variableName1 = "testVariable1";
 String variableName2 = "testVariable2";
 Scope.Var declaredVariable1 = scope.declare(variableName1, new Node(Token.NAME, variableName1), JSType.NUMBER, null);
 Scope.Var declaredVariable2 = scope.declare(variableName2, new Node(Token.NAME, variableName2), JSType.STRING, null);

 Iterator<Scope.Var> iterator = scope.getVars();
 List<Scope.Var> variables = new ArrayList<>();
 while (iterator.hasNext()) {
 variables.add(iterator.next());
 }

 Assert.assertEquals(2, variables.size());
 Assert.assertEquals(declaredVariable1, variables.get(0));
 Assert.assertEquals(declaredVariable2, variables.get(1));
 }

 @Test
 public void testGetVarCount() {
 Scope scope = new Scope(null, new Node(Token.FUNCTION, null, null));
 String variableName1 = "testVariable1";
 String variableName2 = "testVariable2";
 Scope.Var declaredVariable1 = scope.declare(variableName1, new Node(Token.NAME, variableName1), JSType.NUMBER, null);
 Scope.Var declaredVariable2 = scope.declare(variableName2, new Node(Token.NAME, variableName2), JSType.STRING, null);

 Assert.assertEquals(2, scope.getVarCount());
 }

 @Test
 public void testIsGlobal() {
 Scope scope = new Scope(null, new Node(Token.FUNCTION, null, null));
 Assert.assertFalse(scope.isGlobal());

 Scope globalScope = new Scope(null, new Node(Token.BLOCK, null, null));
 Assert.assertTrue(globalScope.isGlobal());
 }

 @Test
 public void testIsLocal() {
 Scope scope = new Scope(null, new Node(Token.FUNCTION, null, null));
 Assert.assertFalse(scope.isLocal());

 Scope globalScope = new Scope(null, new Node(Token.BLOCK, null, null));
 Assert.assertTrue(globalScope.isLocal());
 }

 @Test
 public void testJoin() {
 Scope scope1 = new Scope(null, new Node(Token.FUNCTION, null, null));
 Scope scope2 = new Scope(null, new Node(Token.FUNCTION, null, null));
 String variableName1 = "testVariable1";
 String variableName2 = "testVariable2";
 Scope.Var declaredVariable1 = scope1.declare(variableName1, new Node(Token.NAME, variableName1), JSType.NUMBER, null);
 Scope.Var declaredVariable2 = scope2.declare(variableName2, new Node(Token.NAME, variableName2), JSType.STRING, null);
 Scope joinedScope = new Scope(scope1, scope2);
 Scope.Var joinedVariable1 = joinedScope.getVar(variableName1);
 Scope.Var joinedVariable2 = joinedScope.getVar(variableName2);

 Assert.assertEquals(declaredVariable1, joinedVariable1);
 Assert.assertEquals(declaredVariable2, joinedVariable2);
 }
}