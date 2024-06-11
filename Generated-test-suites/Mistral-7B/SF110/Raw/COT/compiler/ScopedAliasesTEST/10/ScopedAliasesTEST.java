import static org.junit.Assert.*;
import static com.google.javascript.jscomp.DiagnosticType.*;

import com.google.common.collect.ImmutableList;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.HotSwapCompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Traversal;
import com.google.javascript.rhino.SourcePosition;

import org.junit.Before;
import org.junit.Test;

public class ScopedAliasesTest {
  private Compiler compiler;
  private CompilerOptions options;
  private PreprocessorSymbolTable preprocessorSymbolTable;
  private AliasTransformationHandler transformationHandler;

  @Before
  public void setUp() {
    compiler = new Compiler();
    options = new CompilerOptions();
    preprocessorSymbolTable = new PreprocessorSymbolTable();
    transformationHandler = new AliasTransformationHandler();
  }

  @Test
  public void testScopedAliases() {
    options.setAliasTransformation(AliasTransformation.GOOG_SCOPE);
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    String sourceCode =
        "goog.scope(function() {" +
            "var dom = goog.dom;" +
            "var DIV = dom.TagName.DIV;" +
            "goog.scope(function() {" +
            "dom.createElement(DIV);" +
            "});" +
            "});";

    Node node = compiler.parseTree(new SourcePosition(0, 0), sourceCode, null);
    scopedAliases.process(null, node);

    Traversal traversal = new Traversal();
    NodeTraversal.traverse(compiler, node, traversal);

    // Check for errors
    assertTrue(traversal.hasErrors());
    assertEquals(4, traversal.getNumDiagnostics());

    List<DiagnosticType> expectedDiagnostics =
        ImmutableList.of(
            GOOG_SCOPE_USED_IMPROPERLY, // The first goog.scope call
            GOOG_SCOPE_HAS_BAD_PARAMETERS, // The second goog.scope call
            GOOG_SCOPE_REFERENCES_THIS, // The inner scope refers to 'this'
            GOOG_SCOPE_ALIAS_REDEFINED // 'dom' is redefined
        );

    for (int i = 0; i < traversal.getNumDiagnostics(); i++) {
      DiagnosticType diagnostic = traversal.getDiagnosticAtIndex(i);
      assertEquals(expectedDiagnostics.get(i), diagnostic);
    }
  }

  @Test
  public void testScopedAliasesNoErrors() {
    options.setAliasTransformation(AliasTransformation.GOOG_SCOPE);
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    String sourceCode =
        "goog.scope(function() {" +
            "var dom = goog.dom;" +
            "goog.scope(function() {" +
            "dom.createElement(goog.dom.TagName.DIV);" +
            "});" +
            "});";

    Node node = compiler.parseTree(new SourcePosition(0, 0), sourceCode, null);
    scopedAliases.process(null, node);

    Traversal traversal = new Traversal();
    NodeTraversal.traverse(compiler, node, traversal);

    assertFalse(traversal.hasErrors());
  }
}