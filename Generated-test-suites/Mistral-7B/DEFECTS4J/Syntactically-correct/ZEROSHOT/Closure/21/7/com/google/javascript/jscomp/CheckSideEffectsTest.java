import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Checker;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.SourceFile;
 import org.junit.Before;
 import org.junit.Test;

/**
 * Test class for CheckSideEffects class
 */
public class CheckSideEffectsTest {

 private AbstractCompiler compiler;
 private CheckSideEffects checkSideEffects;
 private CompilerPass stripProtection;

 @Before
 public void setUp() {
 compiler = mock(AbstractCompiler.class);
 checkSideEffects = new CheckSideEffects(compiler, CheckLevel.WARNING, false);
 stripProtection = new CheckSideEffects.StripProtection(compiler);
 }

 @Test
 public void testProcess() {
 // Prepare test input
 String testCode = "function test() {"
 + "var s = \"this string is \";"
 + " var x = foo();"
 + " foo();;"
 + "}";

 SourceFile sourceFile = new SourceFile("test.js", testCode, null);
 Node root = sourceFile.getAst();

 // Run CheckSideEffects
 NodeTraversal.traverse(compiler, root, checkSideEffects);

 // Check for warnings
 List<Checker.Diagnostic> diagnostics = compiler.getDiagnostics();
 for (Checker.Diagnostic diagnostic : diagnostics) {
 System.out.println(diagnostic.getMessage(null));
 assertTrue(diagnostic.getCategory().equals(Checker.Diagnostic.Category.WARNING));
 }
 }

 @Test
 public void testProcessNoWarnings() {
 // Prepare test input
 String testCode = "function test() {"
 + "var x = 5 + 3;"
 + "}";

 SourceFile sourceFile = new SourceFile("test.js", testCode, null);
 Node root = sourceFile.getAst();

 // Run CheckSideEffects
 NodeTraversal.traverse(compiler, root, checkSideEffects);

 // Check for no warnings
 List<Checker.Diagnostic> diagnostics = compiler.getDiagnostics();
 assertTrue(diagnostics.isEmpty());
 }

 @Test
 public void testProtectSideEffects() {
 // Prepare test input
 String testCode = "function test() {"
 + "var s = \"this string is \";"
 + " var x = foo();"
 + "}"
 + " function foo() {}";

 SourceFile sourceFile = new SourceFile("test.js", testCode, null);
 Node root = sourceFile.getAst();

 // Run CheckSideEffects
 checkSideEffects.process(sourceFile, root);

 // Check if side-effect free nodes are protected
 Node sideEffectFreeNode = root.getChild(0);
 Node protectionCall = sideEffectFreeNode.getLastChild();
 assertTrue(protectionCall.isCall());
 assertTrue(protectionCall.getFirstChild().isName());
 assertTrue(protectionCall.getFirstChild().getString().equals("JSCOMPILER_PRESERVE"));
 }

 @Test
 public void testStripProtection() {
 // Prepare test input
 String testCode = "function test() {"
 + "var s = \"this string is \";"
 + " var x = JSCOMPILER_PRESERVE(foo());"
 + "}"
 + " function foo() {}";

 SourceFile sourceFile = new SourceFile("test.js", testCode, null);
 Node root = sourceFile.getAst();

 // Run StripProtection
 NodeTraversal.traverse(compiler, root, stripProtection);

 // Check if side-effect free nodes are stripped
 Node sideEffectFreeNode = root.getChild(0);
 assertTrue(sideEffectFreeNode.isCall());
 assertFalse(sideEffectFreeNode.getLastChild().isCall());
 }
}