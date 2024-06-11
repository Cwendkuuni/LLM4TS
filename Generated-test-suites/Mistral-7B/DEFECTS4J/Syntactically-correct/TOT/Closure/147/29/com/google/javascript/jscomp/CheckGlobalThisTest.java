import static org.junit.Assert.assertThat;
import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Lists.singletonList;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Checker;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.rhino.JSDocInfo;

@RunWith(JUnit4.class)
public class CheckGlobalThisTest {

 private AbstractCompiler compiler;
 private CompilerOptions options;

 @Before
 public void setUp() {
 compiler = new Compiler();
 options = new CompilerOptions(null);
 }

 @Test
 public void testSafeUsage() {
 SourceFile sourceFile = new SourceFile("Test.js", "function test() { this.x = 1; }");
 CheckGlobalThis checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);
 Node node = checker.process(sourceFile, options).getTree();
 List<DiagnosticType> diagnostics = newArrayList();
 NodeTraversal traversal = new NodeTraversal(compiler, checker, node, diagnostics);
 traversal.traverse();
 assertThat(diagnostics, isEmpty());
 }

 @Test
 public void testUnsafeConstructorUsage() {
 SourceFile sourceFile = new SourceFile("Test.js", "function Test() { this.x = 1; }");
 CheckGlobalThis checker = new CheckGlobalThis(compiler, CheckLevel.ERROR);
 Node node = checker.process(sourceFile, options).getTree();
 List<DiagnosticType> diagnostics = newArrayList();
 NodeTraversal traversal = new NodeTraversal(compiler, checker, node, diagnostics);
 traversal.traverse();
 List<DiagnosticType> expectedDiagnostics = singletonList(
 DiagnosticType.warning(
 DiagnosticType.JSC_USED_GLOBAL_THIS,
 "dangerous use of the global 'this' object: Test",
 1, 1, 5, 1));
 assertThat(diagnostics, is(equalTo(expectedDiagnostics)));
 }

 @Test
 public void testUnsafeMethodUsage() {
 SourceFile sourceFile = new SourceFile("Test.js", "var obj = { method: function() { this.x = 1; } }; obj.method();");
 CheckGlobalThis checker = new CheckGlobalThis(compiler, CheckLevel.ERROR);
 Node node = checker.process(sourceFile, options).getTree();
 List<DiagnosticType> diagnostics = newArrayList();
 NodeTraversal traversal = new NodeTraversal(compiler, checker, node, diagnostics);
 traversal.traverse();
 List<DiagnosticType> expectedDiagnostics = singletonList(
 DiagnosticType.warning(
 DiagnosticType.JSC_USED_GLOBAL_THIS,
 "dangerous use of the global 'this' object: method",
 1, 1, 13, 1));
 assertThat(diagnostics, is(equalTo(expectedDiagnostics)));
 }

 @Test
 public void testUnsafePropertyAccess() {
 SourceFile sourceFile = new SourceFile("Test.js", "var obj = {}; obj.prototype.method = function() { this.x = 1; }; obj.prototype.method();");
 CheckGlobalThis checker = new CheckGlobalThis(compiler, CheckLevel.ERROR);
 Node node = checker.process(sourceFile, options).getTree();
 List<DiagnosticType> diagnostics = newArrayList();
 NodeTraversal traversal = new NodeTraversal(compiler, checker, node, diagnostics);
 traversal.traverse();
 List<DiagnosticType> expectedDiagnostics = singletonList(
 DiagnosticType.warning(
 DiagnosticType.JSC_USED_GLOBAL_THIS,
 "dangerous use of the global 'this' object: method",
 1, 1, 13, 1));
 assertThat(diagnostics, is(equalTo(expectedDiagnostics)));
 }

 @Test
 public void testUnsafeAssignmentToPrototype() {
 SourceFile sourceFile = new SourceFile("Test.js", "var obj = {}; obj.prototype.a = function() { this.x = 1; };");
 CheckGlobalThis checker = new CheckGlobalThis(compiler, CheckLevel.ERROR);
 Node node = checker.process(sourceFile, options).getTree();
 List<DiagnosticType> diagnostics = newArrayList();
 NodeTraversal traversal = new NodeTraversal(compiler, checker, node, diagnostics);
 traversal.traverse();
 List<DiagnosticType> expectedDiagnostics = emptyList();
 assertThat(diagnostics, is(equalTo(expectedDiagnostics)));
 }

 @Test
 public void testSafeAssignmentToPrototype() {
 SourceFile sourceFile = new SourceFile("Test.js", "var obj = {}; obj.prototype.x = 1;");
 CheckGlobalThis checker = new CheckGlobalThis(compiler, CheckLevel.ERROR);
 Node node = checker.process(sourceFile, options).getTree();
 List<DiagnosticType> diagnostics = newArrayList();
 NodeTraversal traversal = new NodeTraversal(compiler, checker, node, diagnostics);
 traversal.traverse();
 List<DiagnosticType> expectedDiagnostics = emptyList();
 assertThat(diagnostics, is(equalTo(expectedDiagnostics)));
 }
}