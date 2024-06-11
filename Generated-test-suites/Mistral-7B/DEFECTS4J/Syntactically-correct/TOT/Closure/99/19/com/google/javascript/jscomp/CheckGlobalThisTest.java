import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Checker;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.DiagnosticGroup;
import com.google.javascript.jscomp.Diagnostics;
import com.google.javascript.jscomp.Parser;
import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.jscomp.CheckGlobalThis;

public class CheckGlobalThisTest {

 private AbstractCompiler compiler;

 @BeforeClass
 public static void setup() {
 CompilerOptions options = new CompilerOptions();
 options.setWarningLevel(CompilerOptions.WarningLevel.WARNING, "JSC_USED_GLOBAL_THIS");
 Parser parser = new Parser();
 compiler = new AbstractCompiler(parser, new Checker(), options);
 }

 @Test
 public void testSafeUsage() {
 SourceFile source = new SourceFile("Test.js", "function Test() { this.x = 1; }", null);
 Diagnostics diagnostics = compiler.check(source);
 assertTrue(diagnostics.isEmpty());
 }

 @Test
 public void testConstructor() {
 SourceFile source = new SourceFile("Test.js", "function Test() { this.constructor = Test; }", null);
 Diagnostics diagnostics = compiler.check(source);
 assertTrue(diagnostics.isEmpty());
 }

 @Test
 public void testPrototypeMethod() {
 SourceFile source = new SourceFile("Test.js", "function Test() { } Test.prototype.method = function() { this.x = 1; }", null);
 Diagnostics diagnostics = compiler.check(source);
 assertTrue(diagnostics.isEmpty());
 }

 @Test
 public void testThisAnnotation() {
 SourceFile source = new SourceFile("Test.js", "/** @this {number} */ function Test() { this.x = 1; }", null);
 Diagnostics diagnostics = compiler.check(source);
 assertTrue(diagnostics.isEmpty());
 }

 @Test
 public void testUnsafeUsageInFunctionBody() {
 SourceFile source = new SourceFile("Test.js", "function Test() { function inner() { this.x = 1; } }", null);
 Diagnostics diagnostics = compiler.check(source);
 assertEquals(1, diagnostics.getNumberOfErrors());
 DiagnosticGroup group = diagnostics.getGroup(Diagnostics.Type.WARNING);
 assertEquals("dangerous use of the global 'this' object", group.getDiagnostic(0).getMessage());
 }

 @Test
 public void testUnsafeAssignment() {
 SourceFile source = new SourceFile("Test.js", "var a = function() { this.x = 1; }", null);
 Diagnostics diagnostics = compiler.check(source);
 assertTrue(diagnostics.isEmpty());
 }

 @Test
 public void testUnsafeAssignmentWithProperty() {
 SourceFile source = new SourceFile("Test.js", "var a = { x: function() { this.y = 1; } }", null);
 Diagnostics diagnostics = compiler.check(source);
 assertTrue(diagnostics.isEmpty());
 }

 @Test
 public void testUnsafeAssignmentWithPrototypeProperty() {
 SourceFile source = new SourceFile("Test.js", "var a = { x: { y: function() { this.z = 1; } } }", null);
 Diagnostics diagnostics = compiler.check(source);
 assertTrue(diagnostics.isEmpty());
 }

 @Test
 public void testUnsafeAssignmentWithPrototypeSubProperty() {
 SourceFile source = new SourceFile("Test.js", "var a = { x: { y: { z: function() { this.w = 1; } } } }", null);
 Diagnostics diagnostics = compiler.check(source);
 assertTrue(diagnostics.isEmpty());
 }
}