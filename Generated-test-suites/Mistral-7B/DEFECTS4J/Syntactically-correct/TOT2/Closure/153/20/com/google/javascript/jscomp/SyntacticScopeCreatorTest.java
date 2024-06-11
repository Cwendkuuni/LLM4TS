import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.ScopeCreator;
import com.google.javascript.jscomp.TestCompiler;
import org.junit.Before;
import org.junit.Test;

import static com.google.javascript.jscomp.NodeUtil.isFunctionExpression;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class SyntacticScopeCreatorTest {

 private AbstractCompiler compiler;
 private ScopeCreator syntacticScopeCreator;

 @Before
 public void setUp() {
 compiler = new TestCompiler();
 syntacticScopeCreator = new SyntacticScopeCreator(compiler);
 }

 @Test
 public void testCreateScopeForFunction() {
 Node functionNode = Node.createString("function foo() {}");
 Scope scope = syntacticScopeCreator.createScope(functionNode, null);
 assertNotNull(scope);
 }

 @Test
 public void testCreateScopeForGlobalBlock() {
 Node globalBlockNode = Node.createString("var x = 1;");
 Scope scope = syntacticScopeCreator.createScope(globalBlockNode, null);
 assertNotNull(scope);
 }

 @Test
 public void testCreateScopeForFunctionWithArguments() {
 Node functionNode = Node.createString("function foo(x, y) {}");
 Scope scope = syntacticScopeCreator.createScope(functionNode, null);
 assertNotNull(scope);

 // Check that 'arguments' has been declared in the scope
 assertEquals(2, scope.getVarCount());
 Node argumentsVar = scope.getVar("arguments");
 assertNotNull(argumentsVar);
 assertEquals(JSType.ARRAY, argumentsVar.getType());
 }

 @Test
 public void testCreateScopeForFunctionWithName() {
 Node functionNode = Node.createString("function foo() {}");
 Scope scope = syntacticScopeCreator.createScope(functionNode, null);
 assertNotNull(scope);

 // Check that 'foo' has been declared in the scope
 Node fooVar = scope.getVar("foo");
 assertNotNull(fooVar);
 assertEquals(JSType.FUNCTION, fooVar.getType());
 }

 @Test
 public void testCreateScopeForFunctionWithNameAndArguments() {
 Node functionNode = Node.createString("function foo(x, y) {}");
 Scope scope = syntacticScopeCreator.createScope(functionNode, null);
 assertNotNull(scope);

 // Check that 'foo' and 'arguments' have been declared in the scope
 assertEquals(3, scope.getVarCount());
 Node fooVar = scope.getVar("foo");
 assertNotNull(fooVar);
 assertEquals(JSType.FUNCTION, fooVar.getType());
 Node argumentsVar = scope.getVar("arguments");
 assertNotNull(argumentsVar);
 assertEquals(JSType.ARRAY, argumentsVar.getType());
 }

 @Test(expected = RuntimeException.class)
 public void testCreateScopeWithDuplicateVariableDeclaration() {
 Node globalBlockNode = Node.createString("var x = 1;\nvar x = 2;");
 Scope scope = syntacticScopeCreator.createScope(globalBlockNode, null);
 }

 @Test
 public void testCreateScopeWithDuplicateVariableDeclarationAndSuppressDuplicateWarning() {
 Node globalBlockNode = Node.createString("/**@suppress {duplicate} */\nvar x = 1;\nvar x = 2;");
 Scope scope = syntacticScopeCreator.createScope(globalBlockNode, null);
 assertEquals(1, scope.getVarCount());
 }
}