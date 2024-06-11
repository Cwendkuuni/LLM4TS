package com.google.javascript.jscomp.test;

import static org.junit.Assert.*;

import org.junit.Test;

import com.google.common.collect.ImmutableList;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.TestCompiler;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Token;

/**
 * Test class for {@link CheckGlobalThis}.
 */
public class CheckGlobalThisTest {

  @Test
  public void testCheckGlobalThis() {
    CompilerOptions options = new CompilerOptions(new CompilerOptions.Defaults());
    options.setWarningLevel(CheckLevel.WARNING, "JSC_USED_GLOBAL_THIS");

    TestCompiler compiler = new TestCompiler(options);

    String code = "function evil() { this.x = 1; }";
    Node node = Node.createTree(code);

    NodeTraversal traversal = new NodeTraversal(compiler, new CheckGlobalThis(compiler, CheckLevel.WARNING));
    traversal.traverse(node);

    ImmutableList<DiagnosticType> diagnostics = compiler.getDiagnostics();
    assertEquals(1, diagnostics.size());
    DiagnosticType diagnostic = diagnostics.get(0);
    assertEquals(DiagnosticType.WARNING, diagnostic.getSeverity());
    assertEquals("JSC_USED_GLOBAL_THIS", diagnostic.getType());
    assertEquals("dangerous use of the global 'this' object", diagnostic.getMessage());
    assertEquals(code.indexOf(Token.THIS), diagnostic.getSourceFile().getTree().getLineNumber(diagnostic.getSourcePosition()));
  }

  @Test
  public void testCheckGlobalThisNoWarning() {
    CompilerOptions options = new CompilerOptions(new CompilerOptions.Defaults());
    options.setWarningLevel(CheckLevel.ERROR, "JSC_USED_GLOBAL_THIS");

    TestCompiler compiler = new TestCompiler(options);

    String code = "function evil() { this.x = 1; }";
    Node node = Node.createTree(code);

    NodeTraversal traversal = new NodeTraversal(compiler, new CheckGlobalThis(compiler, CheckLevel.ERROR));
    traversal.traverse(node);

    ImmutableList<DiagnosticType> diagnostics = compiler.getDiagnostics();
    assertTrue(diagnostics.isEmpty());
  }

  @Test
  public void testCheckGlobalThisWithJsDocConstructor() {
    CompilerOptions options = new CompilerOptions(new CompilerOptions.Defaults());
    options.setWarningLevel(CheckLevel.WARNING, "JSC_USED_GLOBAL_THIS");

    TestCompiler compiler = new TestCompiler(options);

    String code = "/** @constructor */ function Evil() { this.x = 1; }";
    Node node = Node.createTree(code);

    NodeTraversal traversal = new NodeTraversal(compiler, new CheckGlobalThis(compiler, CheckLevel.WARNING));
    traversal.traverse(node);

    ImmutableList<DiagnosticType> diagnostics = compiler.getDiagnostics();
    assertTrue(diagnostics.isEmpty());
  }

  @Test
  public void testCheckGlobalThisWithJsDocThis() {
    CompilerOptions options = new CompilerOptions(new CompilerOptions.Defaults());
    options.setWarningLevel(CheckLevel.WARNING, "JSC_USED_GLOBAL_THIS");

    TestCompiler compiler = new TestCompiler(options);

    String code = "/** @this {Object} */ function Evil() { this.x = 1; }";
    Node node = Node.createTree(code);

    NodeTraversal traversal = new NodeTraversal(compiler, new CheckGlobalThis(compiler, CheckLevel.WARNING));
    traversal.traverse(node);

    ImmutableList<DiagnosticType> diagnostics = compiler.getDiagnostics();
    assertTrue(diagnostics.isEmpty());
  }

  @Test
  public void testCheckGlobalThisWithJsDocFunction() {
    CompilerOptions options = new CompilerOptions(new CompilerOptions.Defaults());
    options.setWarningLevel(CheckLevel.WARNING, "JSC_USED_GLOBAL_THIS");

    TestCompiler compiler = new TestCompiler(options);

    String code = "/** @function */ function evil() { this.x = 1; }";
    Node node = Node.createTree(code);

    NodeTraversal traversal = new NodeTraversal(compiler, new CheckGlobalThis(compiler, CheckLevel.WARNING));
    traversal.traverse(node);

    ImmutableList<DiagnosticType> diagnostics = compiler.getDiagnostics();
    assertTrue(diagnostics.isEmpty());
  }

  @Test
  public void testCheckGlobalThisWithPrototypeAccess() {
    CompilerOptions options = new CompilerOptions(new CompilerOptions.Defaults());
    options.setWarningLevel(CheckLevel.WARNING, "JSC_USED_GLOBAL_THIS");

    TestCompiler compiler = new TestCompiler(options);

    String code = "var x = { x: 1 }; function evil() { x.prototype.x = 1; }";
    Node node = Node.createTree(code);

    NodeTraversal traversal = new NodeTraversal(compiler, new CheckGlobalThis(compiler, CheckLevel.WARNING));
    traversal.traverse(node);

    ImmutableList<DiagnosticType> diagnostics = compiler.getDiagnostics();
    assertTrue(diagnostics.isEmpty());
  }

  @Test
  public void testCheckGlobalThisWithAssignment() {
    CompilerOptions options = new CompilerOptions(new CompilerOptions.Defaults());
    options.setWarningLevel(CheckLevel.WARNING, "JSC_USED_GLOBAL_THIS");

    TestCompiler compiler = new TestCompiler(options);

    String code = "function evil() { var a = this; }";
    Node node = Node.createTree(code);

    NodeTraversal traversal = new NodeTraversal(compiler, new CheckGlobalThis(compiler, CheckLevel.WARNING));
    traversal.traverse(node);

    ImmutableList<DiagnosticType> diagnostics = compiler.getDiagnostics();
    assertTrue(diagnostics.isEmpty());
  }
}