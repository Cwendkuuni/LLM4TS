import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Checker;
import com.google.javascript.jscomp.CheckerFactory;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CheckGlobalThis;
import com.google.javascript.jscomp.NodeTraversal;

public class CheckGlobalThisTest {

 private AbstractCompiler compiler;

 @Before
 public void setUp() {
 compiler = new AbstractCompiler() {
 @Override
 public NodeTraversal getSourceTraversal(Node root) {
 return new NodeTraversal(this, root);
 }
 };
 CompilerOptions options = new CompilerOptions();
 Checker checker = CheckerFactory.getChecker(compiler, options);
 checker.addPass(new CheckGlobalThis(compiler));
 }

 @Test
 public void testSafeUsage() {
 String jsCode = "function Foo() { this.x = 1; }";
 Node node = compiler.parseTree(jsCode);
 checker.check(node, null);
 assertEquals(0, checker.getDiagnostics().size());
 }

 @Test
 public void testUnsafeUsageFunctionCall() {
 String jsCode = "function evil() { this.useful = 1; }";
 Node node = compiler.parseTree(jsCode);
 checker.check(node, null);
 assertEquals(1, checker.getDiagnostics().size());
 }

 @Test
 public void testUnsafeUsagePropertyAccess() {
 String jsCode = "var a = {}; function evil() { a.x = 1; }";
 Node node = compiler.parseTree(jsCode);
 checker.check(node, null);
 assertEquals(1, checker.getDiagnostics().size());
 }

 @Test
 public void testUnsafeUsagePrototypeProperty() {
 String jsCode = "function Foo() { Object.prototype.x = 1; }";
 Node node = compiler.parseTree(jsCode);
 checker.check(node, null);
 assertEquals(1, checker.getDiagnostics().size());
 }

 @Test
 public void testUnsafeUsagePrototypeMethod() {
 String jsCode = "function Foo() { this.m = 1; } Foo.prototype.p = function() { this.x = 1; }";
 Node node = compiler.parseTree(jsCode);
 checker.check(node, null);
 assertEquals(0, checker.getDiagnostics().size());
 }

 @Test
 public void testUnsafeUsagePrototypeSubproperty() {
 String jsCode = "function Foo() { this.x = { y: 1 }; }";
 Node node = compiler.parseTree(jsCode);
 checker.check(node, null);
 assertEquals(1, checker.getDiagnostics().size());
 }

 @Test
 public void testUnsafeUsageFunctionWithThisAnnotation() {
 String jsCode = "@this\nfunction Foo() { this.x = 1; }";
 Node node = compiler.parseTree(jsCode);
 checker.check(node, null);
 assertEquals(0, checker.getDiagnostics().size());
 }

 @Test
 public void testUnsafeUsageConstructor() {
 String jsCode = "function Foo() { this.x = 1; }";
 Node node = compiler.parseTree("function Foo() { this.x = 1; }");
 checker.check(node, null);
 assertTrue(node.isConstructor());
 assertEquals(0, checker.getDiagnostics().size());
 }

 @Test
 public void testUnsafeUsageObjectLiteral() {
 String jsCode = "{x: function() { this.inner = 1; } }";
 Node node = compiler.parseTree(jsCode);
 checker.check(node, null);
 assertEquals(1, checker.getDiagnostics().size());
 }

 @Test
 public void testUnsafeUsageVariableDeclaration() {
 String jsCode = "var a = function() { this.x = 1; }";
 Node node = compiler.parseTree(jsCode);
 checker.check(node, null);
 assertEquals(1, checker.getDiagnostics().size());
 }

 @Test
 public void testUnsafeUsageLendsToPrototype() {
 String jsCode = "function Foo() {}; Foo.prototype.lendsTo = function() { this.x = 1; }";
 Node node = compiler.parseTree(jsCode);
 checker.check(node, null);
 assertEquals(1, checker.getDiagnostics().size());
 }

 @Test
 public void testUnsafeUsageLendsToPrototypeWithPropertyAccess() {
 String jsCode = "function Foo() {}; Foo.prototype.lendsTo = function() { this.x.y = 1; }";
 Node node = compiler.parseTree(jsCode);
 checker.check(node, null);
 assertEquals(1, checker.getDiagnostics().size());
 }

 @Test
 public void testUnsafeUsageLendsToPrototypeWithPrototypeProperty() {
 String jsCode = "function Foo() {}; Foo.prototype.lendsTo = function() { this.prototype.x = 1; }";
 Node node = compiler.parseTree(jsCode);
 checker.check(node, null);
 assertEquals(1, checker.getDiagnostics().size());
 }
}