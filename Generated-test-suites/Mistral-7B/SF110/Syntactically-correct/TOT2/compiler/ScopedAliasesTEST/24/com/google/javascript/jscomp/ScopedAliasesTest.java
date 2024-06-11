import org.junit.Test;
import static org.junit.Assert.*;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.HotSwapCompilerPass;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Traversal;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.SourcePosition;

public class ScopedAliasesTest {

  @Test
  public void testScopedAliases() {
    Compiler compiler = new Compiler();
    HotSwapCompilerPass scopedAliases = new ScopedAliases(compiler, null, null);

    // Test valid scoped aliases
    String sourceCode = "goog.scope(function() {" +
                         "  var dom = goog.dom; " +
                         "  var DIV = dom.TagName.DIV; " +
                         "  dom.createElement(DIV); " +
                         "});";
    Node root = new ParserUtil().parseCompilationUnit(sourceCode);
    scopedAliases.process(null, root);
    Traversal traversal = scopedAliases.getTraversal();
    assertTrue(traversal.getAliasDefinitionsInOrder().size() == 1);
    assertTrue(traversal.getAliasUsages().size() == 1);

    // Test invalid scoped aliases - too many parameters
    sourceCode = "goog.scope(function(arg1, arg2) {" +
                 "  var dom = goog.dom; " +
                 "  var DIV = dom.TagName.DIV; " +
                 "  dom.createElement(DIV); " +
                 "});";
    root = new ParserUtil().parseCompilationUnit(sourceCode);
    scopedAliases.process(null, root);
    traversal = scopedAliases.getTraversal();
    assertEquals(1, traversal.getDiagnostics().size());
    assertEquals(GOOG_SCOPE_HAS_BAD_PARAMETERS.getCode(),
                traversal.getDiagnostics().get(0).getCode());

    // Test invalid scoped aliases - uses 'this' keyword
    sourceCode = "goog.scope(function() {" +
                 "  this.var = 1; " +
                 "});";
    root = new ParserUtil().parseCompilationUnit(sourceCode);
    scopedAliases.process(null, root);
    traversal = scopedAliases.getTraversal();
    assertEquals(1, traversal.getDiagnostics().size());
    assertEquals(GOOG_SCOPE_REFERENCES_THIS.getCode(),
                traversal.getDiagnostics().get(0).getCode());

    // Test invalid scoped aliases - uses 'return' keyword
    sourceCode = "goog.scope(function() {" +
                 "  return 1; " +
                 "});";
    root = new ParserUtil().parseCompilationUnit(sourceCode);
    scopedAliases.process(null, root);
    traversal = scopedAliases.getTraversal();
    assertEquals(1, traversal.getDiagnostics().size());
    assertEquals(GOOG_SCOPE_USES_RETURN.getCode(),
                traversal.getDiagnostics().get(0).getCode());

    // Test invalid scoped aliases - uses 'throw' keyword
    sourceCode = "goog.scope(function() {" +
                 "  throw new Error('Test Error'); " +
                 "});";
    root = new ParserUtil().parseCompilationUnit(sourceCode);
    scopedAliases.process(null, root);
    traversal = scopedAliases.getTraversal();
    assertEquals(1, traversal.getDiagnostics().size());
    assertEquals(GOOG_SCOPE_USES_THROW.getCode(),
                traversal.getDiagnostics().get(0).getCode());
  }

  @Test
  public void testAliasRedefinition() {
    Compiler compiler = new Compiler();
    HotSwapCompilerPass scopedAliases = new ScopedAliases(compiler, null, null);

    // Test alias redefinition
    String sourceCode = "goog.scope(function() {" +
                         "  var x = goog.x; " +
                         "  var x = 1; " +
                         "});";
    Node root = new ParserUtil().parseCompilationUnit(sourceCode);
    scopedAliases.process(null, root);
    Traversal traversal = scopedAliases.getTraversal();
    assertEquals(1, traversal.getDiagnostics().size());
    assertEquals(GOOG_SCOPE_ALIAS_REDEFINED.getCode(),
                traversal.getDiagnostics().get(0).getCode());
  }

  @Test
  public void testNonAliasLocal() {
    Compiler compiler = new Compiler();
    HotSwapCompilerPass scopedAliases = new ScopedAliases(compiler, null, null);

    // Test non-alias local variable in scoped block
    String sourceCode = "goog.scope(function() {" +
                         "  var x = 1; " +
                         "});";
    Node root = new ParserUtil().parseCompilationUnit(sourceCode);
    scopedAliases.process(null, root);
    Traversal traversal = scopedAliases.getTraversal();
    assertEquals(1, traversal.getDiagnostics().size());
    assertEquals(GOOG_SCOPE_NON_ALIAS_LOCAL.getCode(),
                traversal.getDiagnostics().get(0).getCode());
  }
}