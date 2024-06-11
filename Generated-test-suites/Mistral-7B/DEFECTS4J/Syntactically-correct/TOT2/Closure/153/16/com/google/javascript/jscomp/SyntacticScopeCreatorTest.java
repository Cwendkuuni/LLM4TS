import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.ScopeCreator;
import com.google.javascript.jscomp.TestCompiler;
import com.google.javascript.jscomp.test.BaseCompilerTest;
import com.google.javascript.rhino.*;
import org.junit.Before;
import org.junit.Test;

import static com.google.common.truth.Truth.assertThat;

public class SyntacticScopeCreatorTest extends BaseCompilerTest {

 private ScopeCreator scopeCreator;
 private AbstractCompiler compiler;

 @Before
 public void setUp() {
 compiler = new TestCompiler();
 scopeCreator = new SyntacticScopeCreator(compiler);
 }

 @Test
 public void testCreateScopeEmptyFunction() {
 Node rootNode = context.mkBlock("function foo() {}");
 Scope scope = scopeCreator.createScope(rootNode, null);
 assertNotNull(scope);
 assertEquals(0, scope.size());
 }

 @Test
 public void testCreateScopeFunctionWithArgsAndBody() {
 Node rootNode = context.mkBlock("function foo(a, b) {\n" +
         "  var c = 1;\n" +
         "  var d = 2;\n" +
         "}");
 Scope scope = scopeCreator.createScope(rootNode, null);
 assertEquals(4, scope.size());
 assertEquals("c", scope.getName(0));
 assertEquals("d", scope.getName(1));
 assertEquals("a", scope.getName(2));
 assertEquals("b", scope.getName(3));
 }

 @Test
 public void testCreateGlobalScope() {
 Node rootNode = context.mkBlock("var a = 1;");
 Scope scope = scopeCreator.createScope(rootNode, null);
 assertEquals(1, scope.size());
 assertEquals("a", scope.getName(0));
 }

 @Test
 public void testCreateScopeNestedFunctions() {
 Node rootNode = context.mkBlock("function foo() {\n" +
         "  function bar() {\n" +
         "    var c = 1;\n" +
         "  }\n" +
         "}");
 Scope parentScope = scopeCreator.createScope(rootNode, null);
 Scope childScope = scopeCreator.createScope(context.mkBlock("bar()"), parentScope);
 assertEquals(2, parentScope.size());
 assertEquals(1, childScope.size());
 }

 @Test
 public void testCreateScopeWithDuplicateVariable() {
 Node rootNode = context.mkBlock("function foo() {\n" +
         "  var x = 1;\n" +
         "  var x = 2;\n" +
         "}");
 try {
 Scope scope = scopeCreator.createScope(rootNode, null);
 fail();
 } catch (AssertionError e) {
 // expected
 }
 compiler.printDiagnosticDescriptionsTo(System.out);
 String expectedError = "Variable x first declared in ";
 assertThat(compiler.getDiagnostics().get(0).getMessage()).isEqualTo(
     String.format(expectedError, "foo.js"));
 }

 @Test
 public void testCreateScopeWithDuplicateArgument() {
 Node rootNode = context.mkBlock("function foo(x, x) {}");
 try {
 Scope scope = scopeCreator.createScope(rootNode, null);
 fail();
 } catch (AssertionError e) {
 // expected
 }
 compiler.printDiagnosticDescriptionsTo(System.out);
 String expectedError = "Shadowing \"arguments\" is not allowed";
 assertThat(compiler.getDiagnostics().get(0).getMessage()).isEqualTo(
     expectedError);
 }

 @Test
 public void testCreateScopeWithDuplicateGlobalVariable() {
 Node rootNode = context.mkBlock("var x = 1;\n" +
         "var x = 2;");
 try {
 Scope scope = scopeCreator.createScope(rootNode, null);
 fail();
 } catch (AssertionError e) {
 // expected
 }
 compiler.printDiagnosticDescriptionsTo(System.out);
 String expectedError = "Variable x first declared in ";
 assertThat(compiler.getDiagnostics().get(0).getMessage()).isEqualTo(
     String.format(expectedError, "foo.js"));
 }
}