import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.Node;

public class CheckGlobalThisTest {

 private static class TestCompiler extends AbstractCompiler {

 @Override
 public DiagnosticType[] check(Node root, CheckLevel level) {
 return new DiagnosticType[0];
 }
 }

 @Test
 public void testSafeUses() {
 CheckLevel level = CheckLevel.WARNING;
 Compiler compiler = new Compiler(level);
 compiler.setOptions(new Compiler.Options());

 // Test a safe prototype method
 String prototypeMethod = "function ProtoType() { this.prototype.x = 1; }";
 Node root = RhinoJsCompiler.parseJs(prototypeMethod);
 NodeTraversal traversal = new NodeTraversal(root, new TestCompiler(compiler));
 traversal.traverse(root);
 assertEquals(0, compiler.getDiagnostics().size());

 // Test a safe constructor
 String constructor = "function SafeConstructor() { this.x = 1; }";
 root = RhinoJsCompiler.parseJs(constructor);
 traversal = new NodeTraversal(root, new TestCompiler(compiler));
 traversal.traverse(root);
 assertTrue(compiler.getDiagnostics().isEmpty());

 // Test a safe function annotated with @constructor
 String constructorAnnotation = "/** @constructor */ function SafeConstructor() { this.x = 1; }";
 root = RhinoJsCompiler.parseJs(constructorAnnotation);
 traversal = new NodeTraversal(root, new TestCompiler(compiler));
 traversal.traverse(root);
 assertTrue(compiler.getDiagnostics().isEmpty());

 // Test a safe function annotated with @this
 String thisAnnotation = "/** @this {String} */ function SafeFunction(str) { this.x = str; }";
 root = RhinoJsCompiler.parseJs(thisAnnotation);
 traversal = new NodeTraversal(root, new TestCompiler(compiler));
 traversal.traverse(root);
 assertTrue(compiler.getDiagnostics().isEmpty());

 // Test a safe function where there's no logical place to put a @this annotation
 String noLogicalPlace = "var x = function(str) { this.result = str; }";
 root = RhinoJsCompiler.parseJs(noLogicalPlace);
 traversal = new NodeTraversal(root, new TestCompiler(compiler));
 traversal.traverse(root);
 assertTrue(compiler.getDiagnostics().isEmpty());
 }

 @Test
 public void testUnsafeUses() {
 CheckLevel level = CheckLevel.WARNING;
 Compiler compiler = new Compiler(level);
 compiler.setOptions(new Compiler.Options());

 // Test an unsafe use of this on the left side of an assignment
 String unsafeAssignment = "var a = function() { this = {}; }";
 Node root = RhinoJsCompiler.parseJs(unsafeAssignment);
 NodeTraversal traversal = new NodeTraversal(root, new TestCompiler(compiler));
 traversal.traverse(root);
 assertFalse(compiler.getDiagnostics().isEmpty());
 assertNotNull(compiler.getDiagnostics().get(0).getMessage());

 // Test an unsafe use of this as a property access
 String unsafePropertyAccess = "var a = function() { return this.x; }";
 root = RhinoJsCompiler.parseJs(unsafePropertyAccess);
 traversal = new NodeTraversal(root, new TestCompiler(compiler));
 traversal.traverse(root);
 assertFalse(compiler.getDiagnostics().isEmpty());
 assertNotNull(compiler.getDiagnostics().get(0).getMessage());
 }
}