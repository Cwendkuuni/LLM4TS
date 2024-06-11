import static org.junit.Assert.*;
import static com.google.javascript.jscomp.Diagnostics.*;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.HotSwapCompilerPass;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Traversal;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.SourcePosition;

import java.util.List;
import java.util.Map;

public class ScopedAliasesTest {
  @SuppressWarnings("unchecked")
  @org.junit.Test
  public void testScopedAliases() {
    // Test with no errors
    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    options.aliasTransformations = new AliasTransformation[0];
    HotSwapCompilerPass scopedAliases = new ScopedAliases(compiler, null, null);

    Node root = Node.newText("goog.scope(function() { var dom = goog.dom; dom.createElement(goog.dom.TagName.DIV); });");
    scopedAliases.process(null, root);

    // Test with improper usage of goog.scope
    root = Node.newText("goog.scope(function() { return 'hello'; });");
    scopedAliases.process(null, root);
    List<Compiler.Error> errors = compiler.getErrors();
    assertFalse(errors.isEmpty());
    DiagnosticType diagnostic = errors.get(0).getDiagnosticType();
    assertTrue(diagnostic.equals(GOOG_SCOPE_USES_RETURN));

    // Test with goog.scope having bad parameters
    root = Node.newText("goog.scope(function() {}, 1)");
    scopedAliases.process(null, root);
    errors = compiler.getErrors();
    assertFalse(errors.isEmpty());
    diagnostic = errors.get(0).getDiagnosticType();
    assertTrue(diagnostic.equals(GOOG_SCOPE_HAS_BAD_PARAMETERS));

    // Test with goog.scope referencing 'this'
    root = Node.newText("goog.scope(function() { return this; });");
    scopedAliases.process(null, root);
    errors = compiler.getErrors();
    assertFalse(errors.isEmpty());
    diagnostic = errors.get(0).getDiagnosticType();
    assertTrue(diagnostic.equals(GOOG_SCOPE_REFERENCES_THIS));

    // Test with goog.scope using 'throw'
    root = Node.newText("goog.scope(function() { throw new Error(); });");
    scopedAliases.process(null, root);
    errors = compiler.getErrors();
    assertFalse(errors.isEmpty());
    diagnostic = errors.get(0).getDiagnosticType();
    assertTrue(diagnostic.equals(GOOG_SCOPE_USES_THROW));

    // Test with alias redefinition
    root = Node.newText("goog.scope(function() { var dom = goog.dom; var dom = goog.dom; });");
    scopedAliases.process(null, root);
    errors = compiler.getErrors();
    assertFalse(errors.isEmpty());
    diagnostic = errors.get(0).getDiagnosticType();
    assertTrue(diagnostic.equals(GOOG_SCOPE_ALIAS_REDEFINED));

    // Test with non-alias local variable
    root = Node.newText("goog.scope(function() { var dom = goog.dom; var element = document.createElement('DIV'); });");
    scopedAliases.process(null, root);
    errors = compiler.getErrors();
    assertFalse(errors.isEmpty());
    diagnostic = errors.get(0).getDiagnosticType();
    assertTrue(diagnostic.equals(GOOG_SCOPE_NON_ALIAS_LOCAL));

    // Test with empty goog.scope
    root = Node.newText("goog.scope(function() { });");
    scopedAliases.process(null, root);
    errors = compiler.getErrors();
    assertTrue(errors.isEmpty());

    // Test with multiple aliases
    root = Node.newText("goog.scope(function() { var dom = goog.dom; var DIV = goog.dom.TagName.DIV; var DOMElement = document.createElement; });");
    scopedAliases.process(null, root);
    errors = compiler.getErrors();
    assertTrue(errors.isEmpty());

    // Test with alias usage
    root = Node.newText("goog.scope(function() { var dom = goog.dom; dom.createElement(goog.dom.TagName.DIV); });");
    scopedAliases.process(null, root);
    errors = compiler.getErrors();
    assertTrue(errors.isEmpty());

    // Test with alias definition in order
    List<Node> aliasDefinitions = scopedAliases.getAliasDefinitionsInOrder();
    assertEquals("goog.dom", aliasDefinitions.get(0).getString());
    assertEquals("goog.dom.TagName.DIV", aliasDefinitions.get(1).getString());

    // Test with alias usage
    List<AliasUsage> aliasUsages = scopedAliases.getAliasUsages();
    assertEquals(1, aliasUsages.size());
    AliasUsage aliasUsage = aliasUsages.get(0);
    if (aliasUsage instanceof AliasedNode) {
      Node node = aliasUsage.applyAlias();
      assertEquals("goog.dom", node.getString());
    } else if (aliasUsage instanceof AliasedTypeNode) {
      Node node = aliasUsage.applyAlias();
      assertEquals("goog.dom", node.getString());
    }

    // Test with scope call
    List<Node> scopeCalls = scopedAliases.getScopeCalls();
    assertEquals(1, scopeCalls.size());
    Node scopeCall = scopeCalls.get(0);
    assertEquals("goog.scope", scopeCall.getFirstChild().getQualifiedName());

    // Test with alias transformation
    CompilerOptions optionsWithTransformations = new CompilerOptions();
    optionsWithTransformations.aliasTransformations = new AliasTransformation[1];
    optionsWithTransformations.aliasTransformations[0] = new AliasTransformation();
    optionsWithTransformations.aliasTransformationHandlers =
        new AliasTransformationHandler[1];
    optionsWithTransformations.aliasTransformationHandlers[0] =
        new AbstractAliasTransformationHandler() {};
    HotSwapCompilerPass scopedAliasesWithTransformations =
        new ScopedAliases(compiler, null, optionsWithTransformations);
    root = Node.newText("goog.scope(function() { var dom = goog.dom; dom.createElement(goog.dom.TagName.DIV); });");
    scopedAliasesWithTransformations.process(null, root);
    errors = compiler.getErrors();
    assertTrue(errors.isEmpty());

    // Test with alias transformation usage
    root = Node.newText("goog.dom.createElement(goog.dom.TagName.DIV);");
    scopedAliasesWithTransformations.process(null, root);
    errors = compiler.getErrors();
    assertTrue(errors.isEmpty());

    // Test with alias transformation definition in order
    aliasDefinitions = scopedAliasesWithTransformations.getAliasDefinitionsInOrder();
    assertEquals("goog.dom", aliasDefinitions.get(0).getString());
    assertEquals("goog.dom.TagName.DIV", aliasDefinitions.get(1).getString());

    // Test with alias transformation usage
    aliasUsages = scopedAliasesWithTransformations.getAliasUsages();
    assertEquals(1, aliasUsages.size());
    aliasUsage = aliasUsages.get(0);
    if (aliasUsage instanceof AliasedNode) {
      Node node = aliasUsage.applyAlias();
      assertEquals("goog.dom", node.getString());
    } else if (aliasUsage instanceof AliasedTypeNode) {
      Node node = aliasUsage.applyAlias();
      assertEquals("goog.dom.TagName.DIV", node.getString());
    }

    // Test with alias transformation errors
    root = Node.newText("goog.scope(function() { var dom = goog.dom; var DIV = goog.dom.TagName.DIV; var DOMElement = document.createElement; return 1; });");
    scopedAliasesWithTransformations.process(null, root);
    errors = compiler.getErrors();
    assertFalse(errors.isEmpty());
    diagnostic = errors.get(0).getDiagnosticType();
    assertTrue(diagnostic.equals(GOOG_SCOPE_USES_RETURN));

    // Test with alias transformation errors
    root = Node.newText("goog.scope(function() { var dom = goog.dom; var DIV = goog.dom.TagName.DIV; var DOMElement = document.createElement; this; });");
    scopedAliasesWithTransformations.process(null, root);
    errors = compiler.getErrors();
    assertFalse(errors.isEmpty());
    diagnostic = errors.get(0).getDiagnosticType();
    assertTrue(diagnostic.equals(GOOG_SCOPE_REFERENCES_THIS));

    // Test with alias transformation errors
    root = Node.newText("goog.scope(function() { var dom = goog.dom; var DIV = goog.dom.TagName.DIV; var DOMElement = document.createElement; throw new Error(); });");
    scopedAliasesWithTransformations.process(null, root);
    errors = compiler.getErrors();
    assertFalse(errors.isEmpty());
    diagnostic = errors.get(0).getDiagnosticType();
    assertTrue(diagnostic.equals(GOOG_SCOPE_USES_THROW));
  }
}