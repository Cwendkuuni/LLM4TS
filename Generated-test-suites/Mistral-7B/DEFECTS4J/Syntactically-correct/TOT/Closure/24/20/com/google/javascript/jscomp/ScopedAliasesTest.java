import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CompilerOptions.AliasTransformation.*;

import com.google.common.collect.Lists;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.HotSwapCompilerPass;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Traversal;
import com.google.javascript.jscomp.traverse.NodeTraversal;
import com.google.javascript.rhino.Document;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.SourceFile;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class ScopedAliasesTest {

  @org.junit.Test
  public void testScopedAliases() throws Exception {
    String code =
        "goog.scope(function() {" +
        "  var dom = goog.dom; " +
        "  var DIV = dom.TagName.DIV; " +
        "  goog.dom.createElement(DIV); " +
        "});";

    Compiler compiler = new Compiler();
    compiler.setOption(AliasTransformation.ENABLED, true);
    SourceFile sourceFile = new SourceFile("Test.java", code);
    Document document = new Document();
    document.setSourceFile(sourceFile);
    document.setPosition(new SourcePosition(1, 1));

    List<HotSwapCompilerPass> passes = new ArrayList<>();
    passes.add(new ScopedAliases(compiler, null, new AliasTransformationHandler() {}));

    Node root = compiler.parse(Collections.singletonList(sourceFile), null, null)[0];
    Traversal traversal = new Traversal();
    NodeTraversal.traverse(compiler, root, traversal);

    // Check that the aliases were created and applied correctly.
    assertEquals("goog.dom.createElement(goog.dom.TagName.DIV)",
        root.toString());

    // Check that the number of alias definitions, usage, and scope calls are correct.
    assertEquals(1, traversal.getAliasDefinitionsInOrder().size());
    assertEquals(1, traversal.getScopeCalls().size());
    assertEquals(1, traversal.getAliasUsages().size());

    // Check that the alias definition and usage are in the correct order.
    Node aliasDefinition = traversal.getAliasDefinitionsInOrder().get(0);
    Node aliasUsage = traversal.getAliasUsages().get(0);
    assertEquals(aliasDefinition, aliasUsage.aliasDefinition);

    // Check that the alias definition is used correctly.
    Node aliasReference = aliasUsage.aliasReference;
    assertEquals(aliasDefinition, aliasReference.getParent());
    assertEquals(aliasDefinition.getLastChild(), aliasReference);

    // Check that errors were not reported.
    assertTrue(traversal.hasErrors());
    assertEquals(0, traversal.getErrors().size());
  }

  @org.junit.Test
  public void testScopedAliasesImproperUsage() throws Exception {
    String code =
        "goog.scope(function() {" +
        "  var dom = goog.dom; " +
        "  var DIV = dom.TagName.DIV; " +
        "  goog.dom.createElement(DIV); " +
        "}); " +
        "goog.scope(function() {" +
        "  var dom = goog.dom; " +
        "  var DIV = dom.TagName.DIV; " +
        "}); ";

    Compiler compiler = new Compiler();
    compiler.setOption(AliasTransformation.ENABLED, true);
    SourceFile sourceFile = new SourceFile("Test.java", code);
    Document document = new Document();
    document.setSourceFile(sourceFile);
    document.setPosition(new SourcePosition(1, 1));

    List<HotSwapCompilerPass> passes = new ArrayList<>();
    passes.add(new ScopedAliases(compiler, null, new AliasTransformationHandler() {}));

    Node root = compiler.parse(Collections.singletonList(sourceFile), null, null)[0];

    // Check that an error was reported for the improper usage of goog.scope.
    Traversal traversal = new Traversal();
    NodeTraversal.traverse(compiler, root, traversal);
    List<DiagnosticType> errors = traversal.getErrors();
    assertEquals("goog.scope must be alone in a single statement.", errors.get(0).getMessage());

    // Check that the number of alias definitions, usage, and scope calls are correct.
    assertEquals(0, traversal.getAliasDefinitionsInOrder().size());
    assertEquals(0, traversal.getScopeCalls().size());
    assertEquals(0, traversal.getAliasUsages().size());
  }

  @org.junit.Test
  public void testScopedAliasesBadParameters() throws Exception {
    String code =
        "goog.scope(function(a, b) {" +
        "  var dom = goog.dom; " +
        "  var DIV = dom.TagName.DIV; " +
        "  goog.dom.createElement(DIV); " +
        "}); ";

    Compiler compiler = new Compiler();
    compiler.setOption(AliasTransformation.ENABLED, true);
    SourceFile sourceFile = new SourceFile("Test.java", code);
    Document document = new Document();
    document.setSourceFile(sourceFile);
    document.setPosition(new SourcePosition(1, 1));

    List<HotSwapCompilerPass> passes = new ArrayList<>();
    passes.add(new ScopedAliases(compiler, null, new AliasTransformationHandler() {}));

    Node root = compiler.parse(Collections.singletonList(sourceFile), null, null)[0];

    // Check that an error was reported for the bad parameters of goog.scope.
    Traversal traversal = new Traversal();
    NodeTraversal.traverse(compiler, root, traversal);
    List<DiagnosticType> errors = traversal.getErrors();
    assertEquals("goog.scope must take only a single parameter.  It must" +
        " be an anonymous function that itself takes no parameters.",
        errors.get(0).getMessage());

    // Check that the number of alias definitions, usage, and scope calls are correct.
    assertEquals(0, traversal.getAliasDefinitionsInOrder().size());
    assertEquals(0, traversal.getScopeCalls().size());
    assertEquals(0, traversal.getAliasUsages().size());
  }

  @org.junit.Test
  public void testScopedAliasesReferencingThis() throws Exception {
    String code =
        "goog.scope(function() {" +
        "  this.dom = goog.dom; " +
        "  var DIV = dom.TagName.DIV; " +
        "  goog.dom.createElement(DIV); " +
        "}); ";

    Compiler compiler = new Compiler();
    compiler.setOption(AliasTransformation.ENABLED, true);
    SourceFile sourceFile = new SourceFile("Test.java", code);
    Document document = new Document();
    document.setSourceFile(sourceFile);
    document.setPosition(new SourcePosition(1, 1));

    List<HotSwapCompilerPass> passes = new ArrayList<>();
    passes.add(new ScopedAliases(compiler, null, new AliasTransformationHandler() {}));

    Node root = compiler.parse(Collections.singletonList(sourceFile), null, null)[0];

    // Check that an error was reported for referencing 'this' in the body of a goog.scope function.
    Traversal traversal = new Traversal();
    NodeTraversal.traverse(compiler, root, traversal);
    List<DiagnosticType> errors = traversal.getErrors();
    assertEquals("The body of a goog.scope function cannot reference 'this'.", errors.get(0).getMessage());

    // Check that the number of alias definitions, usage, and scope calls are correct.
    assertEquals(0, traversal.getAliasDefinitionsInOrder().size());
    assertEquals(0, traversal.getScopeCalls().size());
    assertEquals(0, traversal.getAliasUsages().size());
  }

  @org.junit.Test
  public void testScopedAliasesUsingReturn() throws Exception {
    String code =
        "goog.scope(function() {" +
        "  return goog.dom.createElement(goog.dom.TagName.DIV); " +
        "}); ";

    Compiler compiler = new Compiler();
    compiler.setOption(AliasTransformation.ENABLED, true);
    SourceFile sourceFile = new SourceFile("Test.java", code);
    Document document = new Document();
    document.setSourceFile(sourceFile);
    document.setPosition(new SourcePosition(1, 1));

    List<HotSwapCompilerPass> passes = new ArrayList<>();
    passes.add(new ScopedAliases(compiler, null, new AliasTransformationHandler() {}));

    Node root = compiler.parse(Collections.singletonList(sourceFile), null, null)[0];

    // Check that an error was reported for using 'return' in the body of a goog.scope function.
    Traversal traversal = new Traversal();
    NodeTraversal.traverse(compiler, root, traversal);
    List<DiagnosticType> errors = traversal.getErrors();
    assertEquals("The body of a goog.scope function cannot use 'return'.", errors.get(0).getMessage());

    // Check that the number of alias definitions, usage, and scope calls are correct.
    assertEquals(0, traversal.getAliasDefinitionsInOrder().size());
    assertEquals(0, traversal.getScopeCalls().size());
    assertEquals(0, traversal.getAliasUsages().size());
  }

  @org.junit.Test
  public void testScopedAliasesUsingThrow() throws Exception {
    String code =
        "goog.scope(function() {" +
        "  throw new Error('Test'); " +
        "}); ";

    Compiler compiler = new Compiler();
    compiler.setOption(AliasTransformation.ENABLED, true);
    SourceFile sourceFile = new SourceFile("Test.java", code);
    Document document = new Document();
    document.setSourceFile(sourceFile);
    document.setPosition(new SourcePosition(1, 1));

    List<HotSwapCompilerPass> passes = new ArrayList<>();
    passes.add(new ScopedAliases(compiler, null, new AliasTransformationHandler() {}));

    Node root = compiler.parse(Collections.singletonList(sourceFile), null, null)[0];

    // Check that an error was reported for using 'throw' in the body of a goog.scope function.
    Traversal traversal = new Traversal();
    NodeTraversal.traverse(compiler, root, traversal);
    List<DiagnosticType> errors = traversal.getErrors();
    assertEquals("The body of a goog.scope function cannot use 'throw'.", errors.get(0).getMessage());

    // Check that the number of alias definitions, usage, and scope calls are correct.
    assertEquals(0, traversal.getAliasDefinitionsInOrder().size());
    assertEquals(0, traversal.getScopeCalls().size());
    assertEquals(0, traversal.getAliasUsages().size());
  }

  @org.junit.Test
  public void testScopedAliasesAliasRedefined() throws Exception {
    String code =
        "goog.scope(function() {" +
        "  var dom = goog.dom; " +
        "  var DIV = dom.TagName.DIV; " +
        "  var DIV = goog.dom.TagName.INPUT; " +
        "  goog.dom.createElement(DIV); " +
        "}); ";

    Compiler compiler = new Compiler();
    compiler.setOption(AliasTransformation.ENABLED, true);
    SourceFile sourceFile = new SourceFile("Test.java", code);
    Document document = new Document();
    document.setSourceFile(sourceFile);
    document.setPosition(new SourcePosition(1, 1));

    List<HotSwapCompilerPass> passes = new ArrayList<>();
    passes.add(new ScopedAliases(compiler, null, new AliasTransformationHandler() {}));

    Node root = compiler.parse(Collections.singletonList(sourceFile), null, null)[0];

    // Check that an error was reported for redefining an alias.
    Traversal traversal = new Traversal();
    NodeTraversal.traverse(compiler, root, traversal);
    List<DiagnosticType> errors = traversal.getErrors();
    assertEquals("The alias DIV is assigned a value more than once.", errors.get(0).getMessage());

    // Check that the number of alias definitions, usage, and scope calls are correct.
    assertEquals(1, traversal.getAliasDefinitionsInOrder().size());
    assertEquals(1, traversal.getScopeCalls().size());
    assertEquals(1, traversal.getAliasUsages().size());
  }
}