import static org.junit.Assert.*;
import static com.google.javascript.jscomp.Diagnostics.*;
import com.google.javascript.jscomp.*;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.PreprocessorSymbolTable;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Traversal;
import com.google.javascript.jscomp.Traversal.AliasUsage;
import com.google.javascript.rhino.SourcePosition;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class ScopedAliasesTest {
  @Test
  public void testScopedAliases() throws Exception {
    // Given
    String sourceCode = "goog.scope(function() {\n" +
            "  var dom = goog.dom;\n" +
            "  var DIV = dom.TagName.DIV;\n" +
            "  dom.createElement(DIV);\n" +
            "});";

    CompilerOptions compilerOptions = CompilerOptions.defaultInstance();
    AbstractCompiler compiler = new Compiler(compilerOptions);
    PreprocessorSymbolTable preprocessorSymbolTable = null;
    AliasTransformationHandler transformationHandler = context -> {};
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    // When
    Node ast = compiler.parseTree(sourceCode);
    scopedAliases.process(null, ast);

    // Then
    List<Node> expectedAliasDefinitionsInOrder = Arrays.asList(
        Node.newVar(Token.NAME, "dom", Node.NODE, null, null),
        Node.newVar(Token.NAME, "DIV", Node.QUALIFIED, Node.newQualifiedName(
            Node.newName(Token.NAME, "goog", null),
            Node.newName(Token.NAME, "dom", null)
        ), null, null),
        Node.newCall(Token.CALL, Node.newName(Token.NAME, "createElement", null),
            Node.newThis(), Node.newQualifiedName(
                Node.newName(Token.NAME, "goog", null),
                Node.newName(Token.NAME, "dom", null)
            ), Node.newName(Token.NAME, "DIV", null), null, null)
    );
    List<Node> actualAliasDefinitionsInOrder = scopedAliases.getAliasDefinitionsInOrder();
    assertEquals(expectedAliasDefinitionsInOrder, actualAliasDefinitionsInOrder);

    List<AliasUsage> expectedAliasUsages = Arrays.asList(
        new AliasedNode(
            Node.newCall(Token.CALL, Node.newName(Token.NAME, "createElement", null),
                Node.newThis(), Node.newQualifiedName(
                    Node.newName(Token.NAME, "goog", null),
                    Node.newName(Token.NAME, "dom", null)
                ), Node.newName(Token.NAME, "DIV", null), null, null),
            Node.newVar(Token.NAME, "dom", Node.NODE, null, null)
        ),
        new AliasedTypeNode(
            Node.newQualifiedName(
                Node.newName(Token.NAME, "goog", null),
                Node.newName(Token.NAME, "dom", null)
            ),
            Node.newVar(Token.NAME, "dom", Node.NODE, null, null),
            "dom"
        )
    );
    List<AliasUsage> actualAliasUsages = scopedAliases.getAliasUsages();
    assertEquals(expectedAliasUsages, actualAliasUsages);

    List<Node> expectedScopeCalls = Arrays.asList(
        Node.newCall(Token.CALL, Node.newName(Token.NAME, "goog.scope", null),
            Node.newFunction(
                Node.newLParen(), null,
                Node.newRParen(), null,
                Node.newBlock(null)
            ), null, null)
    );
    List<Node> actualScopeCalls = scopedAliases.getScopeCalls();
    assertEquals(expectedScopeCalls, actualScopeCalls);

    // Edge cases and exception handling

    // Test improper usage of goog.scope
    String sourceCodeWithImproperUsage = "goog.scope(function() {\n" +
            "  var dom = goog.dom;\n" +
            "  var DIV = dom.TagName.DIV;\n" +
            "  dom.createElement(DIV);\n" +
            "  return 42; // goog.scope should not return a value\n" +
            "});";
    scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
    ast = compiler.parseTree(sourceCodeWithImproperUsage);
    scopedAliases.process(null, ast);
    List<Diagnostic> diagnostics = compiler.getDiagnostics();
    assertEquals(1, diagnostics.size());
    Diagnostic expectedDiagnostic = getDiagnostic(GOOG_SCOPE_USED_IMPROPERLY);
    Diagnostic actualDiagnostic = diagnostics.get(0);
    assertEquals(expectedDiagnostic, actualDiagnostic);

    // Test goog.scope with no parameter
    sourceCodeWithImproperUsage = "goog.scope();";
    scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
    ast = compiler.parseTree(sourceCodeWithImproperUsage);
    try {
      scopedAliases.process(null, ast);
      fail("Expected an error for 'goog.scope' with no parameter.");
    } catch (RuntimeException e) {
      List<Diagnostic> actualDiagnostics = e.getCauses();
      assertEquals(1, actualDiagnostics.size());
      Diagnostic actualDiagnostic = actualDiagnostics.get(0);
      assertEquals(expectedDiagnostic, actualDiagnostic);
    }

    // Test goog.scope with more than one parameter
    sourceCodeWithImproperUsage = "goog.scope(function(arg1, arg2) {\n" +
            "  var dom = goog.dom;\n" +
            "  var DIV = dom.TagName.DIV;\n" +
            "  dom.createElement(DIV);\n" +
            "});";
    scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
    ast = compiler.parseTree(sourceCodeWithImproperUsage);
    try {
      scopedAliases.process(null, ast);
      fail("Expected an error for 'goog.scope' with more than one parameter.");
    } catch (RuntimeException e) {
      List<Diagnostic> actualDiagnostics = e.getCauses();
      assertEquals(1, actualDiagnostics.size());
      Diagnostic actualDiagnostic = actualDiagnostics.get(0);
      assertEquals(expectedDiagnostic, actualDiagnostic);
    }

    // Test goog.scope with a function body that references 'this'
    sourceCodeWithImproperUsage = "goog.scope(function() {\n" +
            "  this.counter = 0;\n" +
            "  var dom = goog.dom;\n" +
            "  var DIV = dom.TagName.DIV;\n" +
            "  dom.createElement(DIV);\n" +
            "});";
    scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
    ast = compiler.parseTree(sourceCodeWithImproperUsage);
    try {
      scopedAliases.process(null, ast);
      fail("Expected an error for 'this' inside the goog.scope function body.");
    } catch (RuntimeException e) {
      List<Diagnostic> actualDiagnostics = e.getCauses();
      assertEquals(1, actualDiagnostics.size());
      Diagnostic actualDiagnostic = actualDiagnostics.get(0);
      assertEquals(expectedDiagnostic, actualDiagnostic);
    }

    // Test goog.scope with a function body that uses 'return'
    sourceCodeWithImproperUsage = "goog.scope(function() {\n" +
            "  return dom.createElement(DIV);\n" +
            "});";
    scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
    ast = compiler.parseTree(sourceCodeWithImproperUsage);
    try {
      scopedAliases.process(null, ast);
      fail("Expected an error for 'return' inside the goog.scope function body.");
    } catch (RuntimeException e) {
      List<Diagnostic> actualDiagnostics = e.getCauses();
      assertEquals(1, actualDiagnostics.size());
      Diagnostic actualDiagnostic = actualDiagnostics.get(0);
      assertEquals(expectedDiagnostic, actualDiagnostic);
    }

    // Test goog.scope with a function body that uses 'throw'
    sourceCodeWithImproperUsage = "goog.scope(function() {\n" +
            "  throw new Error('Something went wrong');\n" +
            "});";
    scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
    ast = compiler.parseTree(sourceCodeWithImproperUsage);
    try {
      scopedAliases.process(null, ast);
      fail("Expected an error for 'throw' inside the goog.scope function body.");
    } catch (RuntimeException e) {
      List<Diagnostic> actualDiagnostics = e.getCauses();
      assertEquals(1, actualDiagnostics.size());
      Diagnostic actualDiagnostic = actualDiagnostics.get(0);
      assertEquals(expectedDiagnostic, actualDiagnostic);
    }

    // Test goog.scope with an alias redefined
    sourceCodeWithImproperUsage = "goog.scope(function() {\n" +
            "  var dom = goog.dom;\n" +
            "  var DIV = dom.TagName.DIV;\n" +
            "  var DIV = 'another name';\n" +
            "  dom.createElement(DIV);\n" +
            "});";
    scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
    ast = compiler.parseTree(sourceCodeWithImproperUsage);
    try {
      scopedAliases.process(null, ast);
      fail("Expected an error for a redefined alias.");
    } catch (RuntimeException e) {
      List<Diagnostic> actualDiagnostics = e.getCauses();
      assertEquals(1, actualDiagnostics.size());
      Diagnostic actualDiagnostic = actualDiagnostics.get(0);
      assertEquals(expectedDiagnostic, actualDiagnostic);
    }
  }
}