package com.google.javascript.jscomp;

import com.google.common.collect.Lists;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.List;

public class ScopedAliasesTest {

  @Test
  public void testScopedAliases() {
    String source = "function foo() {\n" +
        "  goog.scope(function() {\n" +
        "    var dom = goog.dom; \n" +
        "    var DIV = dom.TagName.DIV;\n" +
        "    goog.dom.createElement(DIV);\n" +
        "  });\n" +
        "  goog.dom.createElement(goog.dom.TagName.DIV);\n" +
        "}";

    CompilerOptions options = new CompilerOptions();
    AbstractCompiler compiler = new AbstractCompiler();

    ScopedAliases scopedAliases = new ScopedAliases(compiler, null, null);
    Node root = Parser.parse(source, null, null, options);
    scopedAliases.process(options.getExterns(), root);

    List<String> expectedOutput = Lists.newArrayList("goog.dom.createElement(goog.dom.TagName.DIV)"); ;
    List<String> actualOutput = Lists.newArrayList();
    Traversal traversal = new Traversal();
    NodeTraversal.traverse(compiler, root, traversal);
    for (Node node : traversal.getNodesInOrder()) {
      if (node.isCall() && node.getFirstChild().getQualifiedName().equals("goog.dom.createElement")) {
        actualOutput.add(node.getFirstChild().getLastChild().getString());
      }
    }
    assertEquals(expectedOutput, actualOutput);
  }

  @Test
  public void testScopedAliasesWithInvalidCall() {
    String source = "function foo() {\n" +
        "  goog.scope(function() {\n" +
        "  });\n" +
        "  goog.scope(function() {\n" +
        "  });\n" +
        "}";

    CompilerOptions options = new CompilerOptions();
    AbstractCompiler compiler = new AbstractCompiler();

    ScopedAliases scopedAliases = new ScopedAliases(compiler, null, null);
    Node root = Parser.parse(source, null, null, options);
    scopedAliases.process(options.getExterns(), root);

    List<String> expectedErrors = Lists.newArrayList(
        "JSC_GOOG_SCOPE_USED_IMPROPERLY",
        "JSC_GOOG_SCOPE_USED_IMPROPERLY"
    );
    List<String> actualErrors = Lists.newArrayList();

    for (DiagnosticInfo diagnosticInfo : compiler.getDiagnostics()) {
      actualErrors.add(diagnosticInfo.getMessage());
    }

    assertEquals(expectedErrors, actualErrors);
  }

  @Test
  public void testScopedAliasesWithInvalidParameter() {
    String source = "function foo() {\n" +
        "  goog.scope(function(x) {\n" +
        "  });\n" +
        "}";

    CompilerOptions options = new CompilerOptions();
    AbstractCompiler compiler = new AbstractCompiler();

    ScopedAliases scopedAliases = new ScopedAliases(compiler, null, null);
    Node root = Parser.parse(source, null, null, options);
    scopedAliases.process(options.getExterns(), root);

    List<String> expectedErrors = Lists.newArrayList(
        "JSC_GOOG_SCOPE_HAS_BAD_PARAMETERS"
    );
    List<String> actualErrors = Lists.newArrayList();

    for (DiagnosticInfo diagnosticInfo : compiler.getDiagnostics()) {
      actualErrors.add(diagnosticInfo.getMessage());
    }

    assertEquals(expectedErrors, actualErrors);
  }

  @Test
  public void testScopedAliasesWithThisKeyword() {
    String source = "function foo() {\n" +
        "  goog.scope(function() {\n" +
        "  this.bar = 42;\n" +
        "  });\n" +
        "  console.log(bar);\n" +
        "}";

    CompilerOptions options = new CompilerOptions();
    AbstractCompiler compiler = new AbstractCompiler();

    ScopedAliases scopedAliases = new ScopedAliases(compiler, null, null);
    Node root = Parser.parse(source, null, null, options);
    scopedAliases.process(options.getExterns(), root);

    List<String> expectedErrors = Lists.newArrayList("JSC_GOOG_SCOPE_REFERENCES_THIS");
    List<String> actualErrors = Lists.newArrayList();

    for (DiagnosticInfo diagnosticInfo : compiler.getDiagnostics()) {
      actualErrors.add(diagnosticInfo.getMessage());
    }

    assertEquals(expectedErrors, actualErrors);
  }

  @Test
  public void testScopedAliasesWithReturnStatement() {
    String source = "function foo() {\n" +
        "  goog.scope(function() {\n" +
        "    return 42;\n" +
        "  });\n" +
        "}";

    CompilerOptions options = new CompilerOptions();
    AbstractCompiler compiler = new AbstractCompiler();

    ScopedAliases scopedAliases = new ScopedAliases(compiler, null, null);
    Node root = Parser.parse(source, null, null, options);
    scopedAliases.process(options.getExterns(), root);

    List<String> expectedErrors = Lists.newArrayList("JSC_GOOG_SCOPE_USES_RETURN");
    List<String> actualErrors = Lists.newArrayList();

    for (DiagnosticInfo diagnosticInfo : compiler.getDiagnostics()) {
      actualErrors.add(diagnosticInfo.getMessage());
    }

    assertEquals(expectedErrors, actualErrors);
  }

  @Test
  public void testScopedAliasesWithThrowStatement() {
    String source = "function foo() {\n" +
        "  goog.scope(function() {\n" +
        "    throw new Error('Error!');\n" +
        "  });\n" +
        "}";

    CompilerOptions options = new CompilerOptions();
    AbstractCompiler compiler = new AbstractCompiler();

    ScopedAliases scopedAliases = new ScopedAliases(compiler, null, null);
    Node root = Parser.parse(source, null, null, options);
    scopedAliases.process(options.getExterns(), root);

    List<String> expectedErrors = Lists.newArrayList("JSC_GOOG_SCOPE_USES_THROW");
    List<String> actualErrors = Lists.newArrayList();

    for (DiagnosticInfo diagnosticInfo : compiler.getDiagnostics()) {
      actualErrors.add(diagnosticInfo.getMessage());
    }

    assertEquals(expectedErrors, actualErrors);
  }

  @Test
  public void testScopedAliasesWithAliasRedefined() {
    String source = "function foo() {\n" +
        "  goog.scope(function() {\n" +
        "    var a = 42;\n" +
        "    var a = 'string';\n" +
        "  });\n" +
        "}";

    CompilerOptions options = new CompilerOptions();
    AbstractCompiler compiler = new AbstractCompiler();

    ScopedAliases scopedAliases = new ScopedAliases(compiler, null, null);
    Node root = Parser.parse(source, null, null, options);
    scopedAliases.process(options.getExterns(), root);

    List<String> expectedErrors = Lists.newArrayList("JSC_GOOG_SCOPE_ALIAS_REDEFINED");
    List<String> actualErrors = Lists.newArrayList();

    for (DiagnosticInfo diagnosticInfo : compiler.getDiagnostics()) {
      actualErrors.add(diagnosticInfo.getMessage());
    }

    assertEquals(expectedErrors, actualErrors);
  }

  @Test
  public void testScopedAliasesWithNonAliasLocal() {
    String source = "function foo() {\n" +
        "  goog.scope(function() {\n" +
        "    var a = 42;\n" +
        "    var b = a;\n" +
        "  });\n" +
        "}";

    CompilerOptions options = new CompilerOptions();
    AbstractCompiler compiler = new AbstractCompiler();

    ScopedAliases scopedAliases = new ScopedAliases(compiler, null, null);
    Node root = Parser.parse(source, null, null, options);
    scopedAliases.process(options.getExterns(), root);

    List<String> expectedErrors = Lists.newArrayList("JSC_GOOG_SCOPE_NON_ALIAS_LOCAL");
    List<String> actualErrors = Lists.newArrayList();

    for (DiagnosticInfo diagnosticInfo : compiler.getDiagnostics()) {
      actualErrors.add(diagnosticInfo.getMessage());
    }

    assertEquals(expectedErrors, actualErrors);
  }

  @Test
  public void testScopedAliasesWithNestedScope() {
    String source = "function foo() {\n" +
        "  goog.scope(function() {\n" +
        "    goog.scope(function() {\n" +
        "      var a = 42;\n" +
        "    });\n" +
        "    goog.dom.createElement(goog.dom.TagName.DIV);\n" +
        "  });\n" +
        "}";

    CompilerOptions options = new CompilerOptions();
    AbstractCompiler compiler = new AbstractCompiler();

    ScopedAliases scopedAliases = new ScopedAliases(compiler, null, null);
    Node root = Parser.parse(source, null, null, options);
    scopedAliases.process(options.getExterns(), root);

    List<String> expectedOutput = Lists.newArrayList("goog.dom.createElement(goog.dom.TagName.DIV)"); ;
    List<String> actualOutput = Lists.newArrayList();
    Traversal traversal = new Traversal();
    NodeTraversal.traverse(compiler, root, traversal);
    for (Node node : traversal.getNodesInOrder()) {
      if (node.isCall() && node.getFirstChild().getQualifiedName().equals("goog.dom.createElement")) {
        actualOutput.add(node.getFirstChild().getLastChild().getString());
      }
    }
    assertEquals(expectedOutput, actualOutput);
  }
}