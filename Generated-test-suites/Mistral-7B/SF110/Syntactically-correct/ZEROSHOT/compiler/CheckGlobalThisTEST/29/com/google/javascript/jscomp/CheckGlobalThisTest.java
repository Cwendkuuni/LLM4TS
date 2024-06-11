import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.modules.junit4.PowerMockRunner;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckerFactory;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.DiagnosticType;

@RunWith(PowerMockRunner.class)
public class CheckGlobalThisTest {

  @Test
  public void testCheckGlobalThis() {
    CheckerFactory checkerFactory = new CheckerFactory();
    Compiler compiler = checkerFactory.newCompiler();
    AbstractCompiler abstractCompiler = compiler.getAbstractCompiler();
    CheckGlobalThis checker = new CheckGlobalThis(abstractCompiler);
    NodeTraversal nodeTraversal = new NodeTraversal(compiler, checker, null);

    String jsCode = "function evil() { this.useful = undefined; }";

    Compiler.SourceFile sourceFile = new Compiler.SourceFile("test", jsCode, null, null, null);

    compiler.process(sourceFile, null);
    abstractCompiler.process(new Node(Token.SCRIPT, null, sourceFile.getTree()), null);

    nodeTraversal.traverse(sourceFile.getTree());

    assertTrue(compiler.getDiagnostics().hasError(DiagnosticType.WARNING, "JSC_USED_GLOBAL_THIS"));

    // Test safe cases

    jsCode = "function SafeFunction() {}";
    sourceFile = new Compiler.SourceFile("test", jsCode, null, null, null);

    compiler.process(sourceFile, null);
    abstractCompiler.process(new Node(Token.SCRIPT, null, sourceFile.getTree()), null);

    nodeTraversal.traverse(sourceFile.getTree());

    assertFalse(compiler.getDiagnostics().hasError(DiagnosticType.WARNING, "JSC_USED_GLOBAL_THIS"));

    jsCode = "function SafeFunction() { this.prototype.useful = undefined; }";
    sourceFile = new Compiler.SourceFile("test", jsCode, null, null, null);

    compiler.process(sourceFile, null);
    abstractCompiler.process(new Node(Token.SCRIPT, null, sourceFile.getTree()), null);

    nodeTraversal.traverse(sourceFile.getTree());

    assertFalse(compiler.getDiagnostics().hasError(DiagnosticType.WARNING, "JSC_USED_GLOBAL_THIS"));

    jsCode = "function SafeFunction() { return this; }";
    sourceFile = new Compiler.SourceFile("test", jsCode, null, null, null);

    compiler.process(sourceFile, null);
    abstractCompiler.process(new Node(Token.SCRIPT, null, sourceFile.getTree()), null);

    nodeTraversal.traverse(sourceFile.getTree());

    assertFalse(compiler.getDiagnostics().hasError(DiagnosticType.WARNING, "JSC_USED_GLOBAL_THIS"));

    jsCode = "var obj = function() {};";
    sourceFile = new Compiler.SourceFile("test", jsCode, null, null, null);

    compiler.process(sourceFile, null);
    abstractCompiler.process(new Node(Token.SCRIPT, null, sourceFile.getTree()), null);

    nodeTraversal.traverse(sourceFile.getTree());

    assertFalse(compiler.getDiagnostics().hasError(DiagnosticType.WARNING, "JSC_USED_GLOBAL_THIS"));

    jsCode = "var obj = {method: function() {}};";
    sourceFile = new Compiler.SourceFile("test", jsCode, null, null, null);

    compiler.process(sourceFile, null);
    abstractCompiler.process(new Node(Token.SCRIPT, null, sourceFile.getTree()), null);

    nodeTraversal.traverse(sourceFile.getTree());

    assertFalse(compiler.getDiagnostics().hasError(DiagnosticType.WARNING, "JSC_USED_GLOBAL_THIS"));

    jsCode = "var obj = {method: function() { this.useful = undefined; }};";
    sourceFile = new Compiler.SourceFile("test", jsCode, null, null, null);

    compiler.process(sourceFile, null);
    abstractCompiler.process(new Node(Token.SCRIPT, null, sourceFile.getTree()), null);

    nodeTraversal.traverse(sourceFile.getTree());

    assertFalse(compiler.getDiagnostics().hasError(DiagnosticType.WARNING, "JSC_USED_GLOBAL_THIS"));

    jsCode = "function SafeFunction() { this.useful = undefined; } function SafeFunction2() {}";
    sourceFile = new Compiler.SourceFile("test", jsCode, null, null, null);

    compiler.process(sourceFile, null);
    abstractCompiler.process(new Node(Token.SCRIPT, null, sourceFile.getTree()), null);

    nodeTraversal.traverse(sourceFile.getTree());

    assertFalse(compiler.getDiagnostics().hasError(DiagnosticType.WARNING, "JSC_USED_GLOBAL_THIS"));

    jsCode = "function SafeFunction() { this.prototype.useful = undefined; } function SafeFunction2() {}";
    sourceFile = new Compiler.SourceFile("test", jsCode, null, null, null);

    compiler.process(sourceFile, null);
    abstractCompiler.process(new Node(Token.SCRIPT, null, sourceFile.getTree()), null);

    nodeTraversal.traverse(sourceFile.getTree());

    assertFalse(compiler.getDiagnostics().hasError(DiagnosticType.WARNING, "JSC_USED_GLOBAL_THIS"));

    // Test unsafe cases

    jsCode = "this.useful = undefined;";
    sourceFile = new Compiler.SourceFile("test", jsCode, null, null, null);

    compiler.process(sourceFile, null);
    abstractCompiler.process(new Node(Token.SCRIPT, null, sourceFile.getTree()), null);

    nodeTraversal.traverse(sourceFile.getTree());

    assertTrue(compiler.getDiagnostics().hasError(DiagnosticType.WARNING, "JSC_USED_GLOBAL_THIS"));

    jsCode = "function unsafeFunction() { this.useful = undefined; }";
    sourceFile = new Compiler.SourceFile("test", jsCode, null, null, null);

    compiler.process(sourceFile, null);
    abstractCompiler.process(new Node(Token.SCRIPT, null, sourceFile.getTree()), null);

    nodeTraversal.traverse(sourceFile.getTree());

    assertTrue(compiler.getDiagnostics().hasError(DiagnosticType.WARNING, "JSC_USED_GLOBAL_THIS"));

    jsCode = "function unsafeFunction() { this.prototype.useful = undefined; }";
    sourceFile = new Compiler.SourceFile("test", jsCode, null, null, null);

    compiler.process(sourceFile, null);
    abstractCompiler.process(new Node(Token.SCRIPT, null, sourceFile.getTree()), null);

    nodeTraversal.traverse(sourceFile.getTree());

    assertTrue(compiler.getDiagnostics().hasError(DiagnosticType.WARNING, "JSC_USED_GLOBAL_THIS"));

    jsCode = "function unsafeFunction() {} function unsafeFunction2() {}";
    sourceFile = new Compiler.SourceFile("test", jsCode, null, null, null);

    compiler.process(sourceFile, null);
    abstractCompiler.process(new Node(Token.SCRIPT, null, sourceFile.getTree()), null);

    nodeTraversal.traverse(sourceFile.getTree());

    assertTrue(compiler.getDiagnostics().hasError(DiagnosticType.WARNING, "JSC_USED_GLOBAL_THIS"));

    jsCode = "function unsafeFunction() {} function unsafeFunction2() { this.useful = undefined; }";
    sourceFile = new Compiler.SourceFile("test", jsCode, null, null, null);

    compiler.process(sourceFile, null);
    abstractCompiler.process(new Node(Token.SCRIPT, null, sourceFile.getTree()), null);

    nodeTraversal.traverse(sourceFile.getTree());

    assertTrue(compiler.getDiagnostics().hasError(DiagnosticType.WARNING, "JSC_USED_GLOBAL_THIS"));

    jsCode = "function unsafeFunction() {} function unsafeFunction2() { this.prototype.useful = undefined; }";
    sourceFile = new Compiler.SourceFile("test", jsCode, null, null, null);

    compiler.process(sourceFile, null);
    abstractCompiler.process(new Node(Token.SCRIPT, null, sourceFile.getTree()), null);

    nodeTraversal.traverse(sourceFile.getTree());

    assertTrue(compiler.getDiagnostics().hasError(DiagnosticType.WARNING, "JSC_USED_GLOBAL_THIS"));

    jsCode = "function unsafeFunction() { this.useful = undefined; } function unsafeFunction2() { this.prototype.useful = undefined; }";
    sourceFile = new Compiler.SourceFile("test", jsCode, null, null, null);

    compiler.process(sourceFile, null);
    abstractCompiler.process(new Node(Token.SCRIPT, null, sourceFile.getTree()), null);

    nodeTraversal.traverse(sourceFile.getTree());

    assertTrue(compiler.getDiagnostics().hasError(DiagnosticType.WARNING, "JSC_USED_GLOBAL_THIS"));

    // Test edge cases

    jsCode = "function unsafeFunction() { this.useful = undefined; } function unsafeFunction2() {};";
    sourceFile = new Compiler.SourceFile("test", jsCode, null, null, null);

    compiler.process(sourceFile, null);
    abstractCompiler.process(new Node(Token.SCRIPT, null, sourceFile.getTree()), null);

    nodeTraversal.traverse(sourceFile.getTree());

    assertTrue(compiler.getDiagnostics().hasError(DiagnosticType.WARNING, "JSC_USED_GLOBAL_THIS"));

    jsCode = "function unsafeFunction() { this.prototype.useful = undefined; } function unsafeFunction2() {};";
    sourceFile = new Compiler.SourceFile("test", jsCode, null, null, null);

    compiler.process(sourceFile, null);
    abstractCompiler.process(new Node(Token.SCRIPT, null, sourceFile.getTree()), null);

    nodeTraversal.traverse(sourceFile.getTree());

    assertTrue(compiler.getDiagnostics().hasError(DiagnosticType.WARNING, "JSC_USED_GLOBAL_THIS"));

    jsCode = "function unsafeFunction() { this.useful = undefined; } function unsafeFunction2() { function innerFunction() {} }";
    sourceFile = new Compiler.SourceFile("test", jsCode, null, null, null);

    compiler.process(sourceFile, null);
    abstractCompiler.process(new Node(Token.SCRIPT, null, sourceFile.getTree()), null);

    nodeTraversal.traverse(sourceFile.getTree());

    assertTrue(compiler.getDiagnostics().hasError(DiagnosticType.WARNING, "JSC_USED_GLOBAL_THIS"));

    jsCode = "function unsafeFunction() { this.useful = undefined; } function unsafeFunction2() { var innerFunction = function() {}; }";
    sourceFile = new Compiler.SourceFile("test", jsCode, null, null, null);

    compiler.process(sourceFile, null);
    abstractCompiler.process(new Node(Token.SCRIPT, null, sourceFile.getTree()), null);

    nodeTraversal.traverse(sourceFile.getTree());

    assertTrue(compiler.getDiagnostics().hasError(DiagnosticType.WARNING, "JSC_USED_GLOBAL_THIS"));

    jsCode = "function unsafeFunction() { this.useful = undefined; } function unsafeFunction2() { function innerFunction() {}; }";
    sourceFile = new Compiler.SourceFile("test", jsCode, null, null, null);

    compiler.process(sourceFile, null);
    abstractCompiler.process(new Node(Token.SCRIPT, null, sourceFile.getTree()), null);

    nodeTraversal.traverse(sourceFile.getTree());

    assertTrue(compiler.getDiagnostics().hasError(DiagnosticType.WARNING, "JSC_USED_GLOBAL_THIS"));

    jsCode = "function unsafeFunction() { this.useful = undefined; } function unsafeFunction2() { var innerFunction = function() {}; }";
    sourceFile = new Compiler.SourceFile("test", jsCode, null, null, null);

    compiler.process(sourceFile, null);
    abstractCompiler.process(new Node(Token.SCRIPT, null, sourceFile.getTree()), null);

    nodeTraversal.traverse(sourceFile.getTree());

    assertTrue(compiler.getDiagnostics().hasError(DiagnosticType.WARNING, "JSC_USED_GLOBAL_THIS"));

    jsCode = "function unsafeFunction() { function innerFunction() {} this.useful = undefined; }";
    sourceFile = new Compiler.SourceFile("test", jsCode, null, null, null);

    compiler.process(sourceFile, null);
    abstractCompiler.process(new Node(Token.SCRIPT, null, sourceFile.getTree()), null);

    nodeTraversal.traverse(sourceFile.getTree());

    assertTrue(compiler.getDiagnostics().hasError(DiagnosticType.WARNING, "JSC_USED_GLOBAL_THIS"));

    jsCode = "function unsafeFunction() { var innerFunction = function() {}; this.useful = undefined; }";
    sourceFile = new Compiler.SourceFile("test", jsCode, null, null, null);

    compiler.process(sourceFile, null);
    abstractCompiler.process(new Node(Token.SCRIPT, null, sourceFile.getTree()), null);

    nodeTraversal.traverse(sourceFile.getTree());

    assertTrue(compiler.getDiagnostics().hasError(DiagnosticType.WARNING, "JSC_USED_GLOBAL_THIS"));

    jsCode = "function unsafeFunction() { function innerFunction() {} this.prototype.useful = undefined; }";
    sourceFile = new Compiler.SourceFile("test", jsCode, null, null, null);

    compiler.process(sourceFile, null);
    abstractCompiler.process(new Node(Token.SCRIPT, null, sourceFile.getTree()), null);

    nodeTraversal.traverse(sourceFile.getTree());

    assertTrue(compiler.getDiagnostics().hasError(DiagnosticType.WARNING, "JSC_USED_GLOBAL_THIS"));

    jsCode = "function unsafeFunction() { var innerFunction = function() {}; this.prototype.useful = undefined; }";
    sourceFile = new Compiler.SourceFile("test", jsCode, null, null, null);

    compiler.process(sourceFile, null);
    abstractCompiler.process(new Node(Token.SCRIPT, null, sourceFile.getTree()), null);

    nodeTraversal.traverse(sourceFile.getTree());

    assertTrue(compiler.getDiagnostics().hasError(DiagnosticType.WARNING, "JSC_USED_GLOBAL_THIS"));

    jsCode = "function unsafeFunction() { function innerFunction() {} this.prototype.useful = undefined; }";
    sourceFile = new Compiler.SourceFile("test", jsCode, null, null, null);

    compiler.process(sourceFile, null);
    abstractCompiler.process(new Node(Token.SCRIPT, null, sourceFile.getTree()), null);

    nodeTraversal.traverse(sourceFile.getTree());

    assertTrue(compiler.getDiagnostics().hasError(DiagnosticType.WARNING, "JSC_USED_GLOBAL_THIS"));

    PowerMockito.resetStatic(NodeUtil.class);
  }
}