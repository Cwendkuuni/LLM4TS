import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CompilerOptions.AliasTransformation.*;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.AliasTransformationHandler;
import com.google.javascript.jscomp.CompilationTestBase;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.HotSwapCompilerPass;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Traversal;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.SourcePosition;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class ScopedAliasesTest extends CompilationTestBase {

  private AbstractCompiler compiler;
  private AliasTransformationHandler transformationHandler;

  @Before
  public void setup() {
    compiler = newCompiler();
    transformationHandler = mock(AliasTransformationHandler.class);
  }

  @Test
  public void testValidScopedAlias() {
    String input = "goog.scope(function() {" +
        "  var x = goog.x;" +
        "  goog.scope(function() {" +
        "    assertEquals('google', x);" +
        "  });" +
        "});";

    Node root = parse(compiler, input);
    ScopedAliases scopedAliases = new ScopedAliases(compiler, null, transformationHandler);
    scopedAliases.process(null, root);

    assertEquals(0, compiler.getDiagnostics().size());
  }

  @Test
  public void testInvalidScopedAliasUsingThis() {
    String input = "goog.scope(function() {" +
        "  this.x = 1;" +
        "});";

    Node root = parse(compiler, input);
    ScopedAliases scopedAliases = new ScopedAliases(compiler, null, transformationHandler);
    scopedAliases.process(null, root);

    DiagnosticType diagnosticType = getDiagnosticType("JSC_GOOG_SCOPE_REFERENCES_THIS");
    assertEquals(1, compiler.getDiagnostics().size());
    assertEquals(diagnosticType, compiler.getDiagnostics().get(0).getType());
  }

  @Test
  public void testInvalidScopedAliasUsingReturn() {
    String input = "goog.scope(function() {" +
        "  return 1;" +
        "});";

    Node root = parse(compiler, input);
    ScopedAliases scopedAliases = new ScopedAliases(compiler, null, transformationHandler);
    scopedAliases.process(null, root);

    DiagnosticType diagnosticType = getDiagnosticType("JSC_GOOG_SCOPE_USES_RETURN");
    assertEquals(1, compiler.getDiagnostics().size());
    assertEquals(diagnosticType, compiler.getDiagnostics().get(0).getType());
  }

  @Test
  public void testInvalidScopedAliasUsingThrow() {
    String input = "goog.scope(function() {" +
        "  throw new Error('test');" +
        "});";

    Node root = parse(compiler, input);
    ScopedAliases scopedAliases = new ScopedAliases(compiler, null, transformationHandler);
    scopedAliases.process(null, root);

    DiagnosticType diagnosticType = getDiagnosticType("JSC_GOOG_SCOPE_USES_THROW");
    assertEquals(1, compiler.getDiagnostics().size());
    assertEquals(diagnosticType, compiler.getDiagnostics().get(0).getType());
  }

  @Test
  public void testMultipleScopedAliases() {
    String input = "goog.scope(function() {" +
        "  var x = goog.x;" +
        "  var y = goog.y;" +
        "  goog.scope(function() {" +
        "    assertEquals('google', x);" +
        "    assertEquals('google', y);" +
        "  });" +
        "});";

    Node root = parse(compiler, input);
    ScopedAliases scopedAliases = new ScopedAliases(compiler, null, transformationHandler);
    scopedAliases.process(null, root);

    assertEquals(0, compiler.getDiagnostics().size());
  }

  @Test
  public void testInvalidNumberOfParametersInScopeCall() {
    String input = "goog.scope(function() {" +
        "  goog.scope(function(){}, 1);" +
        "});";

    Node root = parse(compiler, input);
    ScopedAliases scopedAliases = new ScopedAliases(compiler, null, transformationHandler);
    scopedAliases.process(null, root);

    DiagnosticType diagnosticType = getDiagnosticType("JSC_GOOG_SCOPE_HAS_BAD_PARAMETERS");
    assertEquals(1, compiler.getDiagnostics().size());
    assertEquals(diagnosticType, compiler.getDiagnostics().get(0).getType());
  }

  @Test
  public void testInvalidCallToScopeInWrongContext() {
    String input = "function foo() {" +
        "  goog.scope(function() {});" +
        "}";

    Node root = parse(compiler, input);
    ScopedAliases scopedAliases = new ScopedAliases(compiler, null, transformationHandler);
    scopedAliases.process(null, root);

    DiagnosticType diagnosticType = getDiagnosticType("JSC_GOOG_SCOPE_USED_IMPROPERLY");
    assertEquals(1, compiler.getDiagnostics().size());
    assertEquals(diagnosticType, compiler.getDiagnostics().get(0).getType());
  }

  @Test
  public void testScopeAliasRedefinition() {
    String input = "goog.scope(function() {" +
        "  var x = goog.x;" +
        "  goog.scope(function() {" +
        "    var x = 'google';" +
        "  });" +
        "});";

    Node root = parse(compiler, input);
    ScopedAliases scopedAliases = new ScopedAliases(compiler, null, transformationHandler);
    scopedAliases.process(null, root);

    DiagnosticType diagnosticType = getDiagnosticType("JSC_GOOG_SCOPE_ALIAS_REDEFINED");
    assertEquals(1, compiler.getDiagnostics().size());
    assertEquals(diagnosticType, compiler.getDiagnostics().get(0).getType());
  }

  @Test
  public void testScopeAliasUsingNonAliasLocalVariable() {
    String input = "goog.scope(function() {" +
        "  var x = 1;" +
        "  goog.scope(function() {});" +
        "});";

    Node root = parse(compiler, input);
    ScopedAliases scopedAliases = new ScopedAliases(compiler, null, transformationHandler);
    scopedAliases.process(null, root);

    DiagnosticType diagnosticType = getDiagnosticType("JSC_GOOG_SCOPE_NON_ALIAS_LOCAL");
    assertEquals(1, compiler.getDiagnostics().size());
    assertEquals(diagnosticType, compiler.getDiagnostics().get(0).getType());
  }

  @Test
  public void testAliasTransformation() {
    String input = "goog.scope(function() {" +
        "  var x = goog.x;" +
        "  goog.scope(function() {" +
        "    goog.dom.createElement(goog.dom.TagName.DIV);" +
        "  });" +
        "});";

    Node root = parse(compiler, input);
    ScopedAliases scopedAliases = new ScopedAliases(compiler, null, transformationHandler);
    scopedAliases.process(null, root);

    assertEquals(0, compiler.getDiagnostics().size());

    Traversal traversal = scopedAliases.getTraversal();
    assertEquals(1, traversal.getScopeCalls().size());
    assertEquals(1, traversal.getAliasDefinitionsInOrder().size());
    assertEquals(1, traversal.getAliasUsages().size());

    Node aliasDefinition = traversal.getAliasDefinitionsInOrder().get(0);
    assertEquals("goog.dom.TagName", aliasDefinition.getQualifiedName());

    Node aliasUsage = traversal.getAliasUsages().get(0);
    assertEquals(aliasDefinition, aliasUsage.getAliasDefinition());

    String expectedOutput = "goog.dom.createElement(goog.dom.TagName.DIV);";
    assertEquals(expectedOutput, compiler.getCompiledCode());
  }

  @Test
  public void testAliasTransformationWithTypeNodes() {
    String input = "goog.scope(function() {" +
        "  var x = goog.x;" +
        "  goog.scope(function() {" +
        "    /** @type {Number} */" +
        "    var y = goog.y;" +
        "  });" +
        "});";

    Node root = parse(compiler, input);
    ScopedAliases scopedAliases = new ScopedAliases(compiler, null, transformationHandler);
    scopedAliases.process(null, root);

    assertEquals(0, compiler.getDiagnostics().size());

    Traversal traversal = scopedAliases.getTraversal();
    assertEquals(1, traversal.getScopeCalls().size());
    assertEquals(1, traversal.getAliasDefinitionsInOrder().size());
    assertEquals(1, traversal.getAliasUsages().size());

    Node aliasDefinition = traversal.getAliasDefinitionsInOrder().get(0);
    assertEquals("goog.y", aliasDefinition.getQualifiedName());

    Node aliasUsage = traversal.getAliasUsages().get(0);
    assertEquals(aliasDefinition, aliasUsage.getAliasDefinition());

    String expectedOutput = "/** @type {Number} */ var y = goog.y;";
    String compiledCode = compiler.getCompiledCode();
    assertTrue(compiledCode.contains(expectedOutput));
  }

  @Test
  public void testAliasTransformationWithDuplicateTypeNode() {
    String input = "goog.scope(function() {" +
        "  var x = goog.x;" +
        "  goog.scope(function() {" +
        "    /** @type {Number} */" +
        "    var y = goog.y;" +
        "    /** @type {Number} */" +
        "    var y = 0;" +
        "  });" +
        "});";

    Node root = parse(compiler, input);
    ScopedAliases scopedAliases = new ScopedAliases(compiler, null, transformationHandler);
    scopedAliases.process(null, root);

    assertEquals(1, compiler.getDiagnostics().size());
    DiagnosticType diagnosticType = getDiagnosticType("JSC_TYPES_COULD_NOT_BE_RESIOLVED");
    assertEquals(diagnosticType, compiler.getDiagnostics().get(0).getType());
  }

  @Test
  public void testPreprocessorSymbolTable() {
    String input = "goog.scope(function() {" +
        "  goog.scope(function() {" +
        "    goog.scope.setParentScope(goog.scope.getGlobalScope());" +
        "  });" +
        "});";

    Node root = parse(compiler, input);
    PreprocessorSymbolTable preprocessorSymbolTable = mock(PreprocessorSymbolTable.class);
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
    scopedAliases.process(null, root);

    verify(preprocessorSymbolTable).addReference(root.getFirstChild().getFirstChild());
  }
}