import com.google.javascript.jscomp.CompilationTestBase;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.JSError;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Traversal;
import com.google.javascript.rhino.SourcePosition;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class ScopedAliasesTest extends CompilationTestBase {

  public void testBasicScopedAlias() {
    CompilerOptions options = new CompilerOptions();
    options.aliasTransformation = CompilerOptions.AliasTransformation.GOOG;
    testCompile("testcases/ScopedAliasesTest/testBasicScopedAlias.js", options);

    Node root = getCompiledCode();
    Traversal traversal = new Traversal();
    NodeTraversal.traverse(compiler, root, traversal);

    assertEquals(1, traversal.getAliasDefinitionsInOrder().size());
    Node aliasDefinition = traversal.getAliasDefinitionsInOrder().get(0);
    assertEquals("goog.dom", aliasDefinition.getString());

    assertEquals(1, traversal.getScopeCalls().size());
    Node scopeCall = traversal.getScopeCalls().get(0);
    assertEquals("goog.scope", scopeCall.getFirstChild().getString());

    Node aliasUsage = traversal.getAliasUsages().get(0);
    assertEquals("goog.dom.createElement", aliasUsage.getAliasDefinition().getString());

    assertFalse(hasErrors());
  }

  public void testImproperlyUsedScopedAlias() {
    CompilerOptions options = new CompilerOptions();
    options.aliasTransformation = CompilerOptions.AliasTransformation.GOOG;

    String sourceCode = "goog.scope(function() {" +
        "  var a = goog.dom; " +
        "  goog.scope(function() {" +
        "    goog.scope(function() {" +
        "      a.createElement('DIV');" +
        "    });" +
        "  });" +
        "});";

    testCompileWithErrors(sourceCode, options);

    Node root = getCompiledCode();
    Traversal traversal = new Traversal();
    NodeTraversal.traverse(compiler, root, traversal);

    List<JSError> errors = getErrors();
    assertEquals(1, errors.size());
    assertEquals(GOOG_SCOPE_USED_IMPROPERLY, errors.get(0).getType());

    assertFalse(hasErrors());
  }

  public void testBadParametersScopedAlias() {
    CompilerOptions options = new CompilerOptions();
    options.aliasTransformation = CompilerOptions.AliasTransformation.GOOG;

    String sourceCode = "goog.scope(function() {" +
        "  goog.scope(function(a) {" +
        "    a = 'invalid';" +
        "  });" +
        "});";

    testCompileWithErrors(sourceCode, options);

    Node root = getCompiledCode();
    Traversal traversal = new Traversal();
    NodeTraversal.traverse(compiler, root, traversal);

    List<JSError> errors = getErrors();
    assertEquals(1, errors.size());
    assertEquals(GOOG_SCOPE_HAS_BAD_PARAMETERS, errors.get(0).getType());

    assertFalse(hasErrors());
  }

  public void testRefersThisScopedAlias() {
    CompilerOptions options = new CompilerOptions();
    options.aliasTransformation = CompilerOptions.AliasTransformation.GOOG;

    String sourceCode = "goog.scope(function() {" +
        "  this.a = 1;" +
        "  goog.scope(function() {" +
        "    this.a = 2;" +
        "  });" +
        "});";

    testCompileWithErrors(sourceCode, options);

    Node root = getCompiledCode();
    Traversal traversal = new Traversal();
    NodeTraversal.traverse(compiler, root, traversal);

    List<JSError> errors = getErrors();
    assertEquals(1, errors.size());
    assertEquals(GOOG_SCOPE_REFERENCES_THIS, errors.get(0).getType());

    assertFalse(hasErrors());
  }

  public void testUsesReturnScopedAlias() {
    CompilerOptions options = new CompilerOptions();
    options.aliasTransformation = CompilerOptions.AliasTransformation.GOOG;

    String sourceCode = "goog.scope(function() {" +
        "  return 1;" +
        "});";

    testCompileWithErrors(sourceCode, options);

    Node root = getCompiledCode();
    Traversal traversal = new Traversal();
    NodeTraversal.traverse(compiler, root, traversal);

    List<JSError> errors = getErrors();
    assertEquals(1, errors.size());
    assertEquals(GOOG_SCOPE_USES_RETURN, errors.get(0).getType());

    assertFalse(hasErrors());
  }

  public void testUsesThrowScopedAlias() {
    CompilerOptions options = new CompilerOptions();
    options.aliasTransformation = CompilerOptions.AliasTransformation.GOOG;

    String sourceCode = "goog.scope(function() {" +
        "  throw new Error('test');" +
        "});";

    testCompileWithErrors(sourceCode, options);

    Node root = getCompiledCode();
    Traversal traversal = new Traversal();
    NodeTraversal.traverse(compiler, root, traversal);

    List<JSError> errors = getErrors();
    assertEquals(1, errors.size());
    assertEquals(GOOG_SCOPE_USES_THROW, errors.get(0).getType());

    assertFalse(hasErrors());
  }

  public void testAliasRedefinedScopedAlias() {
    CompilerOptions options = new CompilerOptions();
    options.aliasTransformation = CompilerOptions.AliasTransformation.GOOG;

    String sourceCode = "goog.scope(function() {" +
        "  var a = 1;" +
        "  goog.scope(function() {" +
        "    var a = 'test';" +
        "  });" +
        "});";

    testCompileWithErrors(sourceCode, options);

    Node root = getCompiledCode();
    Traversal traversal = new Traversal();
    NodeTraversal.traverse(compiler, root, traversal);

    List<JSError> errors = getErrors();
    assertEquals(1, errors.size());
    assertEquals(GOOG_SCOPE_ALIAS_REDEFINED, errors.get(0).getType());
    assertEquals("a", errors.get(0).getMessageArguments()[0]);

    assertFalse(hasErrors());
  }

  public void testNonAliasLocalScopedAlias() {
    CompilerOptions options = new CompilerOptions();
    options.aliasTransformation = CompilerOptions.AliasTransformation.GOOG;

    String sourceCode = "goog.scope(function() {" +
        "  var a = 1;" +
        "});";

    testCompileWithErrors(sourceCode, options);

    Node root = getCompiledCode();
    Traversal traversal = new Traversal();
    NodeTraversal.traverse(compiler, root, traversal);

    List<JSError> errors = getErrors();
    assertEquals(1, errors.size());
    assertEquals(GOOG_SCOPE_NON_ALIAS_LOCAL, errors.get(0).getType());
    assertEquals("a", errors.get(0).getMessageArguments()[0]);

    assertFalse(hasErrors());
  }

  public void testAliasTransformationHandling() {
    CompilerOptions options = new CompilerOptions();
    options.aliasTransformation = CompilerOptions.AliasTransformation.GOOG;

    String sourceCode = "goog.scope(function() {" +
        "  var a = goog.dom;" +
        "  var b = goog.dom.createElement('DIV');" +
        "});";

    testCompile(sourceCode, options);

    Node root = getCompiledCode();
    Traversal traversal = new Traversal();
    NodeTraversal.traverse(compiler, root, traversal);

    assertEquals("goog.dom", traversal.getAliasDefinitionsInOrder().get(0).getString());
    assertEquals("goog.dom.createElement", traversal.getAliasUsages().get(0).getAliasDefinition().getString());

    assertFalse(hasErrors());
  }

  public void testAliasTransformationHandlingWithPrivateVariables() {
    CompilerOptions options = new CompilerOptions();
    options.aliasTransformation = CompilerOptions.AliasTransformation.GOOG;

    String sourceCode = "goog.scope(function(private _a) {" +
        "  var a = _a;" +
        "});";

    testCompile(sourceCode, options);

    Node root = getCompiledCode();
    Traversal traversal = new Traversal();
    NodeTraversal.traverse(compiler, root, traversal);

    assertNull(traversal.getAliasDefinitionsInOrder().get(0));
    assertNull(traversal.getAliasUsages().get(0));

    assertFalse(hasErrors());
  }

  public void testAliasTransformationHandlingWithFunctionReturn() {
    CompilerOptions options = new CompilerOptions();
    options.aliasTransformation = CompilerOptions.AliasTransformation.GOOG;

    String sourceCode = "goog.scope(function() {" +
        "  return function() {" +
        "    return 1;" +
        "  };" +
        "});";

    testCompileWithErrors(sourceCode, options);

    Node root = getCompiledCode();
    Traversal traversal = new Traversal();
    NodeTraversal.traverse(compiler, root, traversal);

    List<JSError> errors = getErrors();
    assertEquals(1, errors.size());
    assertEquals(GOOG_SCOPE_USES_RETURN, errors.get(0).getType());

    assertFalse(hasErrors());
  }

  public void testAliasTransformationHandlingWithFunctionCall() {
    CompilerOptions options = new CompilerOptions();
    options.aliasTransformation = CompilerOptions.AliasTransformation.GOOG;

    String sourceCode = "goog.scope(function() {" +
        "  goog.dom.createElement('DIV');" +
        "});";

    testCompileWithErrors(sourceCode, options);

    Node root = getCompiledCode();
    Traversal traversal = new Traversal();
    NodeTraversal.traverse(compiler, root, traversal);

    List<JSError> errors = getErrors();
    assertEquals(1, errors.size());
    assertEquals(GOOG_SCOPE_USED_IMPROPERLY, errors.get(0).getType());

    assertFalse(hasErrors());
  }
}