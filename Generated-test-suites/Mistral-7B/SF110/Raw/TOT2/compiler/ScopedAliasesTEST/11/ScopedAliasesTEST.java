import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.HotSwapCompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Traversal;
import com.google.javascript.rhino.SourcePosition;

import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;

import java.util.List;

public class ScopedAliasesTest {
  private Compiler compiler;
  private CompilerOptions options;
  private ScopedAliases scopedAliases;

  @Before
  public void setup() {
    compiler = new Compiler();
    options = new CompilerOptions();
    options.setAliasTransformation(AliasTransformation.ALWAYS);
    scopedAliases = new ScopedAliases(compiler, null, new NoOpAliasTransformationHandler());
  }

  @Test
  public void testBasicScopedAlias() {
    String sourceCode = "goog.scope(function() {" +
        "  var x = goog.foo;" +
        "});";

    List<DiagnosticType> diagnostics = compiler.compile(sourceCode, options);
    assertEquals(0, diagnostics.size());

    Node root = compiler.getRootNode();
    Traversal traversal = new Traversal();
    NodeTraversal.traverse(scopedAliases, root, traversal);

    assertEquals("goog.foo", traversal.getAliasDefinitionsInOrder().get(0)
        .getString());
  }

  @Test
  public void testError_GoogScopeUsedImproperly() {
    String sourceCode = "goog.scope(function() {" +
        "  goog.scope(function() {});" +
        "});";

    List<DiagnosticType> diagnostics = compiler.compile(sourceCode, options);
    assertEquals(1, diagnostics.size());
    assertEquals(DiagnosticType.error, diagnostics.get(0).getSeverity());
  }

  @Test
  public void testError_GoogScopeHasBadParameters() {
    String sourceCode = "goog.scope(function(x) {});";

    List<DiagnosticType> diagnostics = compiler.compile(sourceCode, options);
    assertEquals(1, diagnostics.size());
    assertEquals(DiagnosticType.error, diagnostics.get(0).getSeverity());
  }

  @Test
  public void testError_GoogScopeRefersThis() {
    String sourceCode = "goog.scope(function() {" +
        "  this.x = 42;" +
        "});";

    List<DiagnosticType> diagnostics = compiler.compile(sourceCode, options);
    assertEquals(1, diagnostics.size());
    assertEquals(DiagnosticType.error, diagnostics.get(0).getSeverity());
  }

  @Test
  public void testError_GoogScopeUsesReturn() {
    String sourceCode = "goog.scope(function() {" +
        "  return 42;" +
        "});";

    List<DiagnosticType> diagnostics = compiler.compile(sourceCode, options);
    assertEquals(1, diagnostics.size());
    assertEquals(DiagnosticType.error, diagnostics.get(0).getSeverity());
  }

  @Test
  public void testError_GoogScopeUsesThrow() {
    String sourceCode = "goog.scope(function() {" +
        "  throw new Error('oops');" +
        "});";

    List<DiagnosticType> diagnostics = compiler.compile(sourceCode, options);
    assertEquals(1, diagnostics.size());
    assertEquals(DiagnosticType.error, diagnostics.get(0).getSeverity());
  }

  @Test
  public void testError_AliasedNameRedefined() {
    String sourceCode = "goog.scope(function() {" +
        "  var foo = 'bar';" +
        "  var foo = 42;" +
        "});";

    List<DiagnosticType> diagnostics = compiler.compile(sourceCode, options);
    assertEquals(1, diagnostics.size());
    assertEquals(DiagnosticType.error, diagnostics.get(0).getSeverity());
  }

  @Test(expected = RuntimeException.class)
  public void testError_NonAliasLocalInScope() {
    String sourceCode = "goog.scope(function() {" +
        "  var x = 42;" +
        "});";

    List<DiagnosticType> diagnostics = compiler.compile(sourceCode, options);
    assertEquals(1, diagnostics.size());
    assertEquals(DiagnosticType.error, diagnostics.get(0).getSeverity());

    Traversal traversal = new Traversal();
    Node root = compiler.getRootNode();
    NodeTraversal.traverse(scopedAliases, root, traversal);
    traversal.getAliasUsages().get(0).applyAlias();

    assertThrows(RuntimeException.class, () -> {
      assertEquals(42, traversal.getScopeCalls().get(0).getChildAtIndex(1)
          .getLastChild()
          .getInt());
    });
  }

  @Test
  public void testSuccess_ScopedAliasWithType() {
    String sourceCode = "goog.scope(function() {" +
        "  /** @type {number} */" +
        "  var x = goog.foo;" +
        "});";

    List<DiagnosticType> diagnostics = compiler.compile(sourceCode, options);
    assertEquals(0, diagnostics.size());

    Node root = compiler.getRootNode();
    Traversal traversal = new Traversal();
    NodeTraversal.traverse(scopedAliases, root, traversal);

    assertEquals("goog.foo", traversal.getAliasDefinitionsInOrder().get(0)
        .getString());
    assertEquals("number", ((Node) traversal.getAliasDefinitionsInOrder().get(0)
        .getFirstChild().getLastChild())
        .getString());
  }

  @Test
  public void testSuccess_ScopedAliasWithComplexType() {
    String sourceCode = "goog.scope(function() {" +
        "  /** @type {Array.<number>} */" +
        "  var x = goog.foo;" +
        "});";

    List<DiagnosticType> diagnostics = compiler.compile(sourceCode, options);
    assertEquals(0, diagnostics.size());

    Node root = compiler.getRootNode();
    Traversal traversal = new Traversal();
    NodeTraversal.traverse(scopedAliases, root, traversal);

    assertEquals("goog.foo", traversal.getAliasDefinitionsInOrder().get(0)
        .getString());
    assertEquals("Array.<number>", ((Node) traversal.getAliasDefinitionsInOrder().get(0)
        .getFirstChild().getLastChild())
        .getString());
  }

  @Test
  public void testSuccess_ScopedAliasWithFunction() {
    String sourceCode = "goog.scope(function() {" +
        "  /** @type {function(number): number} */" +
        "  var x = goog.foo;" +
        "});";

    List<DiagnosticType> diagnostics = compiler.compile(sourceCode, options);
    assertEquals(0, diagnostics.size());

    Node root = compiler.getRootNode();
    Traversal traversal = new Traversal();
    NodeTraversal.traverse(scopedAliases, root, traversal);

    assertEquals("goog.foo", traversal.getAliasDefinitionsInOrder().get(0)
        .getString());
    assertEquals("function(number): number", ((Node) traversal.getAliasDefinitionsInOrder().get(0)
        .getFirstChild().getLastChild())
        .getString());
  }

  @Test
  public void testSuccess_ScopedAliasWithNestedScope() {
    String sourceCode = "goog.scope(function() {" +
        "  goog.scope(function() {" +
        "    var x = goog.foo;" +
        "  });" +
        "});";

    List<DiagnosticType> diagnostics = compiler.compile(sourceCode, options);
    assertEquals(0, diagnostics.size());

    Node root = compiler.getRootNode();
    Traversal traversal = new Traversal();
    NodeTraversal.traverse(scopedAliases, root, traversal);

    assertEquals("goog.foo", traversal.getAliasDefinitionsInOrder().get(0)
        .getString());
    assertEquals("goog.foo", traversal.getAliasDefinitionsInOrder().get(1)
        .getString());
  }

  private static class NoOpAliasTransformationHandler implements AliasTransformationHandler {
    @Override
    public void logAliasTransformation(String sourceFileName, SourcePosition<AliasTransformation> pos) {
      // Do nothing
    }
  }
}