import static org.junit.Assert.assertTrue;
import static com.google.javascript.jscomp.CheckLevel.*;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.compiler.AbstractCompiler.CompilationResult;
import com.google.javascript.jscomp.test_util.JSTestCase;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

public class CheckGlobalThisTest extends JSTestCase {

  public void testCheckGlobalThis_FunctionWithNoAnnotation() {
    CheckGlobalThis checker = new CheckGlobalThis(null, WARNING);

    String source = "function evil() { this.property = 1; }";
    CompilerOptions options = new CompilerOptions();
    AbstractCompiler compiler = new Compiler(options, new NodeTraversal(checker), null);
    CompilationResult result = compiler.compileString(source, null, null);

    assertEquals(1, result.diagnostics.size());
    DiagnosticType diagnostic = result.diagnostics.get(0);
    assertEquals(DiagnosticType.WARNING, diagnostic.getSeverity());
    assertEquals("JSC_USED_GLOBAL_THIS", diagnostic.getTypeName());
  }

  public void testCheckGlobalThis_FunctionWithConstructorAnnotation() {
    CheckGlobalThis checker = new CheckGlobalThis(null, WARNING);

    String source = "function MyConstructor() { this.property = 1; }";
    CompilerOptions options = new CompilerOptions();
    AbstractCompiler compiler = new Compiler(options, new NodeTraversal(checker), null);
    CompilationResult result = compiler.compileString(source, null, null);

    assertTrue(result.diagnostics.isEmpty());
  }

  public void testCheckGlobalThis_FunctionWithThisAnnotation() {
    CheckGlobalThis checker = new CheckGlobalThis(null, WARNING);

    String source = "function evil() { @this this.property = 1; }";
    CompilerOptions options = new CompilerOptions();
    AbstractCompiler compiler = new Compiler(options, new NodeTraversal(checker), null);
    CompilationResult result = compiler.compileString(source, null, null);

    assertTrue(result.diagnostics.isEmpty());
  }

  public void testCheckGlobalThis_FunctionWithNoLogicalPlaceForAnnotation() {
    CheckGlobalThis checker = new CheckGlobalThis(null, WARNING);

    String source = "function evil() { var a = this; }";
    CompilerOptions options = new CompilerOptions();
    AbstractCompiler compiler = new Compiler(options, new NodeTraversal(checker), null);
    CompilationResult result = compiler.compileString(source, null, null);

    assertTrue(result.diagnostics.isEmpty());
  }

  public void testCheckGlobalThis_FunctionWithPrototypePropertyAccess() {
    CheckGlobalThis checker = new CheckGlobalThis(null, WARNING);

    String source = "function evil() { Object.prototype.property = 1; }";
    CompilerOptions options = new CompilerOptions();
    AbstractCompiler compiler = new Compiler(options, new NodeTraversal(checker), null);
    CompilationResult result = compiler.compileString(source, null, null);

    assertTrue(result.diagnostics.isEmpty());
  }

  public void testCheckGlobalThis_FunctionWithPrototypeMethod() {
    CheckGlobalThis checker = new CheckGlobalThis(null, WARNING);

    String source = "function MyObject() {} MyObject.prototype.evil = function() { this.property = 1; }";
    CompilerOptions options = new CompilerOptions();
    AbstractCompiler compiler = new Compiler(options, new NodeTraversal(checker), null);
    CompilationResult result = compiler.compileString(source, null, null);

    assertTrue(result.diagnostics.isEmpty());
  }

  public void testCheckGlobalThis_FunctionWithAssignmentToVariable() {
    CheckGlobalThis checker = new CheckGlobalThis(null, WARNING);

    String source = "function evil() { var a = this; }";
    CompilerOptions options = new CompilerOptions();
    AbstractCompiler compiler = new Compiler(options, new NodeTraversal(checker), null);
    CompilationResult result = compiler.compileString(source, null, null);

    assertTrue(result.diagnostics.isEmpty());
  }

  public void testCheckGlobalThis_FunctionWithAssignmentToObject() {
    CheckGlobalThis checker = new CheckGlobalThis(null, WARNING);

    String source = "function evil() { var obj = { property: 1 }; this = obj; }";
    CompilerOptions options = new CompilerOptions();
    AbstractCompiler compiler = new Compiler(options, new NodeTraversal(checker), null);
    CompilationResult result = compiler.compileString(source, null, null);

    assertTrue(result.diagnostics.isEmpty());
  }
}