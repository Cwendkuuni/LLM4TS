import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckGlobalThis;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Parser;
import com.google.javascript.rhino.Node;

public class CheckGlobalThisTest {

 private AbstractCompiler compiler;

 @Before
 public void setUp() {
 compiler = new AbstractCompiler() {};
 compiler.setOptions(new CompilerOptions());
 }

 @Test
 public void testNormalFunction() {
 Node node = new Parser().parse("function f() { return this.x; }");
 NodeTraversal traversal = new NodeTraversal(compiler, node, null);
 new CheckGlobalThis(compiler).traverse(traversal, node, null);
 assertEquals(0, compiler.getDiagnostics().size());
 }

 @Test
 public void testPrototypeMethod() {
 Node node = new Parser().parse("function F() { this.x = 1; } F.prototype.m = function() { return this.x; }");
 NodeTraversal traversal = new NodeTraversal(compiler, node, null);
 new CheckGlobalThis(compiler).traverse(traversal, node, null);
 assertEquals(0, compiler.getDiagnostics().size());
 }

 @Test
 public void testConstructor() {
 Node node = new Parser().parse("function F() { this.x = 1; }");
 NodeTraversal traversal = new NodeTraversal(compiler, node, null);
 new CheckGlobalThis(compiler).traverse(traversal, node, null);
 assertTrue(compiler.getDiagnostics().get(0).getMessage().contains("constructor"));
 }

 @Test
 public void testNotInGlobalContext() {
 Node node = new Parser().parse("function f() { return this.x; } function g() { f(); }");
 NodeTraversal traversal = new NodeTraversal(compiler, node, null);
 new CheckGlobalThis(compiler).traverse(traversal, node, null);
 assertEquals(0, compiler.getDiagnostics().size());
 }

 @Test
 public void testLHSAssignment() {
 Node node = new Parser().parse("var x = this; x = 1;");
 NodeTraversal traversal = new NodeTraversal(compiler, node, null);
 new CheckGlobalThis(compiler).traversal(traversal, node, null);
 assertTrue(compiler.getDiagnostics().get(0).getMessage().contains("left side of an assignment"));
 }

 @Test
 public void testPropertyAccess() {
 Node node = new Parser().parse("var x = this; return x.y;");
 NodeTraversal traversal = new NodeTraversal(compiler, node, null);
 new CheckGlobalThis(compiler).traversal(traversal, node, null);
 assertTrue(compiler.getDiagnostics().get(0).getMessage().contains("property access"));
 }

 @Test
 public void testGetPrototypeProperty() {
 Node node = new Parser().parse("var f = function() {}; f.prototype.x = 1; var x = f.prototype.x;");
 NodeTraversal traversal = new NodeTraversal(compiler, node, null);
 new CheckGlobalThis(compiler).traversal(traversal, node, null);
 assertEquals(1, compiler.getDiagnostics().size());
 assertEquals("dangerous use of the global 'this' object", compiler.getDiagnostics().get(0).getMessage());
 }

 @Test
 public void testGetPrototypeSubproperty() {
 Node node = new Parser().parse("var f = function() {}; f.prototype.x = { y: 1 }; var x = f.prototype.x.y;");
 NodeTraversal traversal = new NodeTraversal(compiler, node, null);
 new CheckGlobalThis(compiler).traversal(traversal, node, null);
 assertEquals(1, compiler.getDiagnostics().size());
 assertEquals("dangerous use of the global 'this' object", compiler.getDiagnostics().get(0).getMessage());
 }

 @Test
 public void testLendedProperty() {
 Node node = new Parser().parse("function F() { this.x = 1; } var o = new F(); Object.defineProperty(o, 'p', { get: function() { return this.x; } });");
 NodeTraversal traversal = new NodeTraversal(compiler, node, null);
 new CheckGlobalThis(compiler).traversal(traversal, node, null);
 assertEquals(0, compiler.getDiagnostics().size());
 }

 @Test
 public void testLendedSubproperty() {
 Node node = new Parser().parse("function F() { this.x = 1; } var o = new F(); Object.defineProperty(o, 'p', { get: function() { return this.y; } }); var y = o.p;");
 NodeTraversal traversal = new NodeTraversal(compiler, node, null);
 new CheckGlobalThis(compiler).traversal(traversal, node, null);
 assertEquals(1, compiler.getDiagnostics().size());
 assertEquals("dangerous use of the global 'this' object", compiler.getDiagnostics().get(0).getMessage());
 }
}