import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import static org.junit.Assert.*;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.CheckerFactory;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.JSError;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Parser;
import com.google.javascript.rhino.NodeFactory;

@RunWith(JUnit4.class)
public class CheckGlobalThisTest {

 private AbstractCompiler compiler;

 @Before
 public void setUp() {
 compiler = new AbstractCompiler();
 CompilerOptions options = new CompilerOptions(null);
 compiler.setOptions(options);
 }

 @Test
 public void testSafeUsage() {
 String source = "function Foo() { this.x = 5; }";
 Node ast = Parser.parse(Parser.javaScriptSource, source, null, null, null);
 CheckGlobalThis checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);
 NodeTraversal traversal = new NodeTraversal(ast, checker);
 traversal.traverse();
 assertEquals(0, compiler.getNumDiagnostics());
 }

 @Test
 public void testUnsafeAssignment() {
 String source = "var x = this; x.y = 5;";
 Node ast = Parser.parse(Parser.javaScriptSource, source, null, null, null);
 CheckGlobalThis checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);
 NodeTraversal traversal = new NodeTraversal(ast, checker);
 traversal.traverse();
 assertEquals(1, compiler.getNumDiagnostics());
 JSError error = compiler.getDiagnostics().get(0);
 assertEquals(DiagnosticType.WARNING, error.getType());
 }

 @Test
 public void testUnsafePrototypePropertyAccess() {
 String source = "function Foo() { Object.prototype.x = 'global'; }";
 Node ast = Parser.parse(Parser.javaScriptSource, source, null, null, null);
 CheckGlobalThis checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);
 NodeTraversal traversal = new NodeTraversal(ast, checker);
 traversal.traverse();
 assertEquals(1, compiler.getNumDiagnostics());
 JSError error = compiler.getDiagnostics().get(0);
 assertEquals(DiagnosticType.WARNING, error.getType());
 }

 @Test
 public void testConstructorFunction() {
 String source = "function Foo() { this.x = 5; }";
 Node ast = Parser.parse(Parser.javaScriptSource, source, null, null, null);
 CheckGlobalThis checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);
 NodeTraversal traversal = new NodeTraversal(ast, checker);
 traversal.traverse();
 assertEquals(0, compiler.getNumDiagnostics());
 }

 @Test
 public void testConstructorFunctionWithJSDoc() {
 String source = "/** @constructor */ function Foo() { this.x = 5; }";
 Node ast = Parser.parse(Parser.javaScriptSource, source, null, null, null);
 CheckGlobalThis checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);
 NodeTraversal traversal = new NodeTraversal(ast, checker);
 traversal.traverse();
 assertEquals(0, compiler.getNumDiagnostics());
 }

 @Test
 public void testMethodFunction() {
 String source = "function Foo() {} Foo.prototype.x = function() { this.y = 5; }";
 Node ast = Parser.parse(Parser.javaScriptSource, source, null, null, null);
 CheckGlobalThis checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);
 NodeTraversal traversal = new NodeTraversal(ast, checker);
 traversal.traverse();
 assertEquals(0, compiler.getNumDiagnostics());
 }

 @Test
 public void testMethodFunctionWithJSDoc() {
 String source = "/** @this {Foo} */ function Foo() {} Foo.prototype.x = function() { this.y = 5; }";
 Node ast = Parser.parse(Parser.javaScriptSource, source, null, null, null);
 CheckGlobalThis checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);
 NodeTraversal traversal = new NodeTraversal(ast, checker);
 traversal.traverse();
 assertEquals(0, compiler.getNumDiagnostics());
 }

 @Test
 public void testFunctionAnnotatedWithThis() {
 String source = "function Foo() { /** @this {Foo} */ this.x = 5; }";
 Node ast = Parser.parse(Parser.javaScriptSource, source, null, null, null);
 CheckGlobalThis checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);
 NodeTraversal traversal = new NodeTraversal(ast, checker);
 traversal.traverse();
 assertEquals(0, compiler.getNumDiagnostics());
 }

 @Test
 public void testFunctionWhereNoThisAnnotationExists() {
 String source = "function Foo() { this.x = 5; }";
 CheckerFactory checkerFactory = new CheckerFactory();
 CheckerFactory.Checker checker = checkerFactory.getCheckers().get(CheckGlobalThis.class);
 CompilerOptions options = new CompilerOptions(null);
 options.warnOnly = true;
 AbstractCompiler compiler = new AbstractCompiler();
 compiler.setOptions(options);
 compiler.addChecker(checker);
 NodeTraversal traversal = new NodeTraversal(Parser.parse(Parser.javaScriptSource, source, null, null, null), null);
 compiler.traverse(traversal);
 assertEquals(1, compiler.getNumDiagnostics());
 }
}