import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static com.google.javascript.jscomp.CheckLevel.WARNING;

import com.google.common.collect.ImmutableList;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckGlobalThis;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Parser;
import com.google.javascript.jscomp.TestingCompiler;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.SourceFile;

import org.junit.Test;

import java.io.IOException;
import java.util.List;

public class CheckGlobalThisTest {

  @Test
  public void testCheckGlobalThis() throws IOException {
    String source = "function evil() { this.useful = undefined; }";

    AbstractCompiler compiler = new TestingCompiler();
    CompilerOptions options = new CompilerOptions(true, null);
    List<DiagnosticType> errors = ImmutableList.of(CheckGlobalThis.GLOBAL_THIS);
    options.setErrorReporter(new CustomErrorReporter(errors));
    Node ast = Parser.parse(new SourceFile("Test.js", source), null, options);

    CheckGlobalThis checker = new CheckGlobalThis(compiler, WARNING);
    new NodeTraversal(ast, checker).traverse();

    assertTrue(errors.size() == 1);
  }

  @Test
  public void testCheckGlobalThisNoError() throws IOException {
    String source = "function safe() { return this; }";

    AbstractCompiler compiler = new TestingCompiler();
    CompilerOptions options = new CompilerOptions(true, null);
    List<DiagnosticType> errors = ImmutableList.of();
    options.setErrorReporter(new CustomErrorReporter(errors));
    Node ast = Parser.parse(new SourceFile("Test.js", source), null, options);

    CheckGlobalThis checker = new CheckGlobalThis(compiler, WARNING);
    new NodeTraversal(ast, checker).traverse();

    assertFalse(errors.isEmpty());
  }

  @Test
  public void testCheckGlobalThisPrototype() throws IOException {
    String source = "function Safe() {} Safe.prototype.useful = function() { return this; }";

    AbstractCompiler compiler = new TestingCompiler();
    CompilerOptions options = new CompilerOptions(true, null);
    List<DiagnosticType> errors = ImmutableList.of();
    options.setErrorReporter(new CustomErrorReporter(errors));
    Node ast = Parser.parse(new SourceFile("Test.js", source), null, options);

    CheckGlobalThis checker = new CheckGlobalThis(compiler, WARNING);
    new NodeTraversal(ast, checker).traverse();

    assertFalse(errors.isEmpty());
  }

  @Test
  public void testCheckGlobalThisConstructor() throws IOException {
    String source = "function Safe() { this.useful = undefined; }";

    AbstractCompiler compiler = new TestingCompiler();
    CompilerOptions options = new CompilerOptions(true, null);
    List<DiagnosticType> errors = ImmutableList.of();
    options.setErrorReporter(new CustomErrorReporter(errors));
    Node ast = Parser.parse(new SourceFile("Test.js", source), null, options);

    JSDocInfo jsDoc = ast.getFirstChild().getJSDocInfo();
    assertTrue(jsDoc.isConstructor());

    CheckGlobalThis checker = new CheckGlobalThis(compiler, WARNING);
    new NodeTraversal(ast, checker).traverse();

    assertFalse(errors.isEmpty());
  }

  @Test
  public void testCheckGlobalThisThisAnnotation() throws IOException {
    String source = "/** @this {Type} */ function Safe() { this.useful = undefined; }";

    AbstractCompiler compiler = new TestingCompiler();
    CompilerOptions options = new CompilerOptions(true, null);
    List<DiagnosticType> errors = ImmutableList.of();
    options.setErrorReporter(new CustomErrorReporter(errors));
    Node ast = Parser.parse(new SourceFile("Test.js", source), null, options);

    CheckGlobalThis checker = new CheckGlobalThis(compiler, WARNING);
    new NodeTraversal(ast, checker).traverse();

    assertFalse(errors.isEmpty());
  }

  @Test
  public void testCheckGlobalThisLhsAssignment() throws IOException {
    String source = "var a = function() { this.useful = undefined; };";

    AbstractCompiler compiler = new TestingCompiler();
    CompilerOptions options = new CompilerOptions(true, null);
    List<DiagnosticType> errors = ImmutableList.of();
    options.setErrorReporter(new CustomErrorReporter(errors));
    Node ast = Parser.parse(new SourceFile("Test.js", source), null, options);

    CheckGlobalThis checker = new CheckGlobalThis(compiler, WARNING);
    new NodeTraversal(ast, checker).traverse();

    assertFalse(errors.isEmpty());
  }

  @Test
  public void testCheckGlobalThisRhsAssignment() throws IOException {
    String source = "function evil() { this.useful = a; } var a = 1;";

    AbstractCompiler compiler = new TestingCompiler();
    CompilerOptions options = new CompilerOptions(true, null);
    List<DiagnosticType> errors = ImmutableList.of();
    options.setErrorReporter(new CustomErrorReporter(errors));
    Node ast = Parser.parse(new SourceFile("Test.js", source), null, options);

    CheckGlobalThis checker = new CheckGlobalThis(compiler, WARNING);
    new NodeTraversal(ast, checker).traverse();

    assertTrue(errors.isEmpty());
  }

  @Test
  public void testCheckGlobalThisLhsAssignmentObjectLiteral() throws IOException {
    String source = "var a = { x: function() { this.useful = undefined; } };";

    AbstractCompiler compiler = new TestingCompiler();
    CompilerOptions options = new CompilerOptions(true, null);
    List<DiagnosticType> errors = ImmutableList.of();
    options.setErrorReporter(new CustomErrorReporter(errors));
    Node ast = Parser.parse(new SourceFile("Test.js", source), null, options);

    CheckGlobalThis checker = new CheckGlobalThis(compiler, WARNING);
    new NodeTraversal(ast, checker).traverse();

    assertFalse(errors.isEmpty());
  }

  @Test
  public void testCheckGlobalThisRhsAssignmentObjectLiteral() throws IOException {
    String source = "function evil() { this.useful = a; } var a = { x: 1 };";

    AbstractCompiler compiler = new TestingCompiler();
    CompilerOptions options = new CompilerOptions(true, null);
    List<DiagnosticType> errors = ImmutableList.of();
    options.setErrorReporter(new CustomErrorReporter(errors));
    Node ast = Parser.parse(new SourceFile("Test.js", source), null, options);

    CheckGlobalThis checker = new CheckGlobalThis(compiler, WARNING);
    new NodeTraversal(ast, checker).traverse();

    assertTrue(errors.isEmpty());
  }
}

class CustomErrorReporter implements Compiler.ErrorReporter {
  private final List<DiagnosticType> errors;

  CustomErrorReporter(List<DiagnosticType> errors) {
    this.errors = errors;
  }

  @Override
  public void report(DiagnosticPosition position, DiagnosticType diagnosticType, String message, Object... args) {
    this.errors.add(diagnosticType);
  }

  @Override
  public void report(DiagnosticPosition position, DiagnosticType diagnosticType, String message) {
    this.errors.add(diagnosticType);
  }
}

class TestingCompiler extends AbstractCompiler {
  @Override
  public boolean shouldCompileModule() {
    return false;
  }
}