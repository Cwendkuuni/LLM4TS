import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static com.google.javascript.jscomp.CheckLevel.WARNING;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.JSDocInfo;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.CheckGlobalThis;

public class CheckGlobalThisTest {

 private AbstractCompiler compiler;
 private CompilerOptions options;

 @BeforeClass
 public void setUp() {
 compiler = new AbstractCompiler();
 options = new CompilerOptions(new DiagnosticGroup(), new DiagnosticReporter());
 }

 @Test
 public void testSafeUsesOfThis() {
 // Test a safe use of 'this' inside a constructor
 String jsCode = "function MyConstructor() { this.myProperty = 'safe'; }";
 checkCompilation(jsCode, true, true);

 // Test a safe use of 'this' inside a prototype method
 jsCode = "function MyObject() {} MyObject.prototype.myMethod = function() { this.myProperty = 'safe'; };";
 checkCompilation(jsCode, true, true);
 }

 @Test
 public void testUnsafeUsesOfThisInFunction() {
 // Test an unsafe use of 'this' in a non-constructor function
 jsCode = "function evil() { this.myProperty = 'unsafe'; }";
 checkCompilation(jsCode, true, true);
 assertFalse(compiler.hasMessages(DiagnosticType.WARNING));

 // Test an unsafe use of 'this' in a constructor with a property assignment
 jsCode = "function MyConstructor() { this.myProperty = 'unsafe'; }";
 checkCompilation(jsCode, true, false);
 assertFalse(compiler.hasMessages(DiagnosticType.WARNING));

 // Test an unsafe use of 'this' in a constructors with a property access
 jsCode = "function MyConstructor() { x = this.myProperty; }"; // Assuming x is not defined before
 checkCompilation(jsCode, true, false);
 assertFalse(compiler.hasMessages(DiagnosticType.WARNING));
 }

 @Test
 public void testUnsafeUsesOfThisInFunctionWithJSDoc() {
 // Test an unsafe use of 'this' in a non-constructor function with JSDoc
 jsCode = "/** @constructor */ function MyConstructor() { this.myProperty = 'unsafe'; }";
 checkCompilation(jsCode, true, true);
 assertFalse(compiler.hasMessages(DiagnosticType.WARNING));

 // Test an unsafe use of 'this' in a non-constructor function with JSDoc and @this annotation
 jsCode = "/** @constructor @this {myProperty: number} */ function MyConstructor() { this.myProperty = 'unsafe'; }";
 checkCompilation(jsCode, true, true);
 assertFalse(compiler.hasMessages(DiagnosticType.WARNING));

 // Test an unsafe use of 'this' in a non-constructor function with JSDoc and @this annotation and no logical place to put it
 jsCode = "/** @this {myProperty: number} */ function MyConstructor() { this.myProperty = 'unsafe'; }"; // Assuming 'MyConstructor' is not a constructor
 checkCompilation(jsCode, true, true);
 assertFalse(compiler.hasMessages(DiagnosticType.WARNING));
 }

 @Test
 public void testUnsafeUsesOfThisInFunctionWithPrototype() {
 // Test an unsafe use of 'this' in a function that is a prototype method
 jsCode = "function MyObject() {} MyObject.prototype.myMethod = function() { this.myProperty = 'unsafe'; };";
 checkCompilation(jsCode, true, true);
 assertFalse(compiler.hasMessages(DiagnosticType.WARNING));
 }

 @Test
 public void testUnsafeUsesOfThisInFunctionWithPrototypeAndJSDoc() {
 // Test an unsafe use of 'this' in a function that is a prototype method with JSDoc
 jsCode = "function MyObject() {} MyObject.prototype.myMethod = function MyPrototypeMethod() { this.myProperty = 'unsafe'; };";
 checkCompilation(jsCode, true, true);
 assertFalse(compiler.hasMessages(DiagnosticType.WARNING));
 }

 private void checkCompilation(String jsCode, boolean shouldCompile, boolean expectWarning) {
 options.setWarningLevel(CheckGlobalThis.GLOBAL_THIS, shouldCompile ? WARNING : 0);
 compiler.compile(jsCode, null, options, null);
 assertTrue(compiler.isSuccessful());
 if (expectWarning) {
 assertFalse(compiler.hasMessages(DiagnosticType.ERROR));
 assertTrue(compiler.hasMessages(CheckGlobalThis.GLOBAL_THIS));
 } else {
 assertFalse(compiler.hasMessages(CheckGlobalThis.GLOBAL_THIS));
 }
 }
}