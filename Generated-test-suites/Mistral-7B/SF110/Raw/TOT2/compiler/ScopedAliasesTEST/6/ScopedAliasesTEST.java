import static org.junit.Assert.*;
import static com.google.javascript.jscomp.ScopedAliases.*;

import com.google.common.collect.Lists;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilerOptions.AliasTransformation;
import com.google.javascript.jscomp.CompilerOptions.AliasTransformationHandler;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Traversal;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.SourcePosition;
import com.google.javascript.rhino.Token;

import java.util.List;
import java.util.Map;

/**
 * Tests for the ScopedAliases class.
 */
public class ScopedAliasesTest {

  @SuppressWarnings("unused")
  private static final DiagnosticType[] DIAGNOSTICS = new DiagnosticType[] {
      GOOG_SCOPE_USED_IMPROPERLY,
      GOOG_SCOPE_HAS_BAD_PARAMETERS,
      GOOG_SCOPE_REFERENCES_THIS,
      GOOG_SCOPE_USES_RETURN,
      GOOG_SCOPE_USES_THROW,
      GOOG_SCOPE_ALIAS_REDEFINED,
      GOOG_SCOPE_NON_ALIAS_LOCAL
  };

  /** Test for a valid ScopedAliases usage. */
  @Test
  public void testValidScopedAliases() {
    String source = "goog.scope(function() {" +
        "var dom = goog.dom; var DIV = dom.TagName.DIV;" +
        "dom.createElement(DIV);" +
        "})();";

    CompilerOptions options = new CompilerOptions();
    AliasTransformationHandler handler = new AliasTransformationHandler();
    options.setAliasTransformationHandler(handler);

    Compiler compiler = new Compiler();
    compiler.setCompilerOptions(options);
    List<String> errors = Lists.newArrayList();

    Node root = compiler.parse(source, null, errors);
    Traversal traversal = new Traversal();
    NodeTraversal.traverse(compiler, root, traversal);

    // Check for no errors.
    assertEquals(0, traversal.getAliasDefinitionsInOrder().size());
    assertEquals(0, traversal.getAliasUsages().size());
    assertEquals(0, traversal.getScopeCalls().size());
    assertEquals(0, errors.size());

    // Check for valid aliases.
    assertEquals("goog.dom", traversal.getAliasDefinitionsInOrder().get(0)
        .getString());
    assertEquals("DIV", traversal.getAliasDefinitionsInOrder().get(1)
        .getString());

    // Check for valid scope calls.
    assertEquals(1, traversal.getScopeCalls().size());
    Node scopeCallNode = traversal.getScopeCalls().get(0);
    assertEquals("goog.scope", scopeCallNode.getFirstChild().getString());
    assertEquals("function() {...}", scopeCallNode.getLastChild().toString());
  }

  /** Test for a ScopedAliases usage with improper usage. */
  @Test
  public void testImproperScopedAliasesUsage() {
    String source = "goog.scope(function() {" +
        "var dom = goog.dom; var DIV = dom.TagName.DIV;" +
        "dom.createElement(DIV);" +
        "goog.scope(function() {});" +
        "})();";

    CompilerOptions options = new CompilerOptions();
    AliasTransformationHandler handler = new AliasTransformationHandler();
    options.setAliasTransformationHandler(handler);

    Compiler compiler = new Compiler();
    compiler.setCompilerOptions(options);
    List<String> errors = Lists.newArrayList();

    Node root = compiler.parse(source, null, errors);
    Traversal traversal = new Traversal();
    NodeTraversal.traverse(compiler, root, traversal);

    // Check for an error.
    assertEquals(1, traversal.getAliasDefinitionsInOrder().size());
    assertEquals(1, traversal.getAliasUsages().size());
    assertEquals(1, traversal.getScopeCalls().size());
    assertEquals(1, errors.size());

    // Check for the error message.
    assertEquals(GOOG_SCOPE_USED_IMPROPERLY, errors.get(0).getType());
  }

  /** Test for a ScopedAliases usage with bad parameters. */
  @Test
  public void testBadScopedAliasesUsage() {
    String source = "goog.scope(function() {" +
        "var dom = goog.dom; var DIV = dom.TagName.DIV;" +
        "goog.scope(function() {}, function() {});" +
        "dom.createElement(DIV);" +
        "})();";

    CompilerOptions options = new CompilerOptions();
    AliasTransformationHandler handler = new AliasTransformationHandler();
    options.setAliasTransformationHandler(handler);

    Compiler compiler = new Compiler();
    compiler.setCompilerOptions(options);
    List<String> errors = Lists.newArrayList();

    Node root = compiler.parse(source, null, errors);
    Traversal traversal = new Traversal();
    NodeTraversal.traverse(compiler, root, traversal);

    // Check for an error.
    assertEquals(1, traversal.getAliasDefinitionsInOrder().size());
    assertEquals(1, traversal.getAliasUsages().size());
    assertEquals(1, traversal.getScopeCalls().size());
    assertEquals(1, errors.size());

    // Check for the error message.
    assertEquals(GOOG_SCOPE_HAS_BAD_PARAMETERS, errors.get(0).getType());
  }

  /** Test for a ScopedAliases usage with a reference to 'this'. */
  @Test
  public void testScopedAliasesUsageWithThisReference() {
    String source = "goog.scope(function() {" +
        "var goo = this;" +
        "goog.scope(function() {" +
        "  var dom = goog.dom;" +
        "  var DIV = dom.TagName.DIV;" +
        "  dom.createElement(DIV);" +
        "});" +
        "})();";

    CompilerOptions options = new CompilerOptions();
    AliasTransformationHandler handler = new AliasTransformationHandler();
    options.setAliasTransformationHandler(handler);

    Compiler compiler = new Compiler();
    compiler.setCompilerOptions(options);
    List<String> errors = Lists.newArrayList();

    Node root = compiler.parse(source, null, errors);
    Traversal traversal = new Traversal();
    NodeTraversal.traverse(compiler, root, traversal);

    // Check for an error.
    assertEquals(1, traversal.getAliasDefinitionsInOrder().size());
    assertEquals(1, traversal.getAliasUsages().size());
    assertEquals(1, traversal.getScopeCalls().size());
    assertEquals(1, errors.size());

    // Check for the error message.
    assertEquals(GOOG_SCOPE_REFERENCES_THIS, errors.get(0).getType());
  }

  /** Test for a ScopedAliases usage with a return statement. */
  @Test
  public void testScopedAliasesUsageWithReturnStatement() {
    String source = "goog.scope(function() {" +
        "return goog.dom.createElement(goog.dom.TagName.DIV);" +
        "})();";

    CompilerOptions options = new CompilerOptions();
    AliasTransformationHandler handler = new AliasTransformationHandler();
    options.setAliasTransformationHandler(handler);

    Compiler compiler = new Compiler();
    compiler.setCompilerOptions(options);
    List<String> errors = Lists.newArrayList();

    Node root = compiler.parse(source, null, errors);
    Traversal traversal = new Traversal();
    NodeTraversal.traverse(compiler, root, traversal);

    // Check for an error.
    assertEquals(1, traversal.getAliasDefinitionsInOrder().size());
    assertEquals(1, traversal.getAliasUsages().size());
    assertEquals(1, traversal.getScopeCalls().size());
    assertEquals(1, errors.size());

    // Check for the error message.
    assertEquals(GOOG_SCOPE_USES_RETURN, errors.get(0).getType());
  }

  /** Test for a ScopedAliases usage with a throw statement. */
  @Test
  public void testScopedAliasesUsageWithThrowStatement() {
    String source = "goog.scope(function() {" +
        "try {" +
        "  goog.scope(function() {" +
        "    throw new Error('test');" +
        "  });" +
        "} catch (e) {" +
        "  // Do nothing." +
        "}" +
        "})();";

    CompilerOptions options = new CompilerOptions();
    AliasTransformationHandler handler = new AliasTransformationHandler();
    options.setAliasTransformationHandler(handler);

    Compiler compiler = new Compiler();
    compiler.setCompilerOptions(options);
    List<String> errors = Lists.newArrayList();

    Node root = compiler.parse(source, null, errors);
    Traversal traversal = new Traversal();
    NodeTraversal.traverse(compiler, root, traversal);

    // Check for an error.
    assertEquals(1, traversal.getAliasDefinitionsInOrder().size());
    assertEquals(1, traversal.getAliasUsages().size());
    assertEquals(1, traversal.getScopeCalls().size());
    assertEquals(1, errors.size());

    // Check for the error message.
    assertEquals(GOOG_SCOPE_USES_THROW, errors.get(0).getType());
  }

  /** Test for a ScopedAliases usage with an alias redefinition. */
  @Test
  public void testScopedAliasesUsageWithAliasRedefinition() {
    String source = "goog.scope(function() {" +
        "var dom = goog.dom;" +
        "var domAlias = goog.dom;" +
        "domAlias = goog.dom;" +
        "})();";

    CompilerOptions options = new CompilerOptions();
    AliasTransformationHandler handler = new AliasTransformationHandler();
    options.setAliasTransformationHandler(handler);

    Compiler compiler = new Compiler();
    compiler.setCompilerOptions(options);
    List<String> errors = Lists.newArrayList();

    Node root = compiler.parse(source, null, errors);
    Traversal traversal = new Traversal();
    NodeTraversal.traverse(compiler, root, traversal);

    // Check for an error.
    assertEquals(1, traversal.getAliasDefinitionsInOrder().size());
    assertEquals(1, traversal.getAliasUsages().size());
    assertEquals(1, traversal.getScopeCalls().size());
    assertEquals(1, errors.size());

    // Check for the error message.
    assertEquals(GOOG_SCOPE_ALIAS_REDEFINED, errors.get(0).getType());
  }

  /** Test for a ScopedAliases usage with a non-alias local variable. */
  @Test
  public void testScopedAliasesUsageWithNonAliasLocalVariable() {
    String source = "goog.scope(function() {" +
        "var x = 1;" +
        "})();";

    CompilerOptions options = new CompilerOptions();
    AliasTransformationHandler handler = new AliasTransformationHandler();
    options.setAliasTransformationHandler(handler);

    Compiler compiler = new Compiler();
    compiler.setCompilerOptions(options);
    List<String> errors = Lists.newArrayList();

    Node root = compiler.parse(source, null, errors);
    Traversal traversal = new Traversal();
    NodeTraversal.traverse(compiler, root, traversal);

    // Check for an error.
    assertEquals(1, traversal.getAliasDefinitionsInOrder().size());
    assertEquals(1, traversal.getAliasUsages().size());
    assertEquals(1, traversal.getScopeCalls().size());
    assertEquals(1, errors.size());

    // Check for the error message.
    assertEquals(GOOG_SCOPE_NON_ALIAS_LOCAL, errors.get(0).getType());
  }
}