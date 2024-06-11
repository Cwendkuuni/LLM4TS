import org.junit.Test;
import static org.junit.Assert.*;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.ErrorReport;
import com.google.javascript.jscomp.HotSwapCompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Traversal;
import com.google.javascript.rhino.NodeTraversal;
import com.google.javascript.rhino.SourcePosition;

/**
 * Test class for {@code ScopedAliases} class.
 */
public class ScopedAliasesTest {

  @Test
  public void testScopedAlias() {
    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    options.setAliasTransformation(AliasTransformation.STRICT);
    options.setPrintStackFrames(false);
    options.setLanguageIn(JavaScriptLanguage.ECMASCRIPT5_STRICT);

    String sourceCode = "goog.scope(function() {" +
        "  var x = goog.x; " +
        "  goog.x = 42;" +
        "  assertTrue(x === 42);" +
        "});";

    ErrorReport errorReport = new ErrorReport();
    HotSwapCompilerPass scopedAliases = new ScopedAliases(
        compiler, null, new CompilerOptions.AliasTransformationHandler() {
          @Override
          public AliasTransformation logAliasTransformation(
              String fileName, SourcePosition position) {
            System.out.println("Alias transformation for " + fileName +
                " at line " + position.getLineNumber() +
                ", column " + position.getColumnNumber() +
                ": " + position.getPositionInformation().getName() + " -> " +
                "goog[" + position.getPositionInformation().getName() + "]");
            return new AliasTransformation(position.getPositionInformation().getName(),
                "goog." + position.getPositionInformation().getName());
          }
        });

    Node root = NodeUtil.parseTree(compiler, sourceCode, errorReport);
    scopedAliases.process(null, root);

    Traversal traversal = new Traversal();
    NodeTraversal.traverse(compiler, root, traversal);

    // Verify that x is an alias for goog.x.
    Node x = traversal.getScopeCalls().get(0).getLastChild().getLastChild()
        .getChildAtIndex(1).getLastChild();
    assertEquals("goog.x", x.getQualifiedName());

    // Verify that goog.x is assigned the value 42.
    Node xAssignment = traversal.getAliasDefinitionsInOrder().get(0);
    Node xValue = xAssignment.getLastChild();
    assertEquals(Node.NUMBER_LITERAL, xValue.getType());
    assertEquals(42, xValue.getNumber().intValueExact());

    // Verify that the alias for goog.x is applied correctly.
    Node xUsage = traversal.getAliasUsages().get(0);
    assertEquals(xAssignment, xUsage.applyAlias());

    // Verify that there are no errors.
    assertTrue(errorReport.getDiagnostics().isEmpty());
  }

  @Test
  public void testInvalidScopedAliasCall() {
    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    options.setAliasTransformation(AliasTransformation.STRICT);
    options.setPrintStackFrames(false);
    options.setLanguageIn(JavaScriptLanguage.ECMASCRIPT5_STRICT);

    String sourceCode = "goog.scope(function() {" +
        "  goog.scope(function() {});" +
        "});";

    ErrorReport errorReport = new ErrorReport();

    HotSwapCompilerPass scopedAliases = new ScopedAliases(
        compiler, null, new CompilerOptions.AliasTransformationHandler() {});

    Node root = NodeUtil.parseTree(compiler, sourceCode, errorReport);
    scopedAliases.process(null, root);

    Traversal traversal = new Traversal();
    NodeTraversal.traverse(compiler, root, traversal);

    // Verify that there is an error for an invalid scoped alias call.
    Node googScopeCall1 = traversal.getScopeCalls().get(0);
    assertEquals(GOOG_SCOPE_USED_IMPROPERLY, traversal.getErrors().get(0).getType());

    // Verify that there is an error for another invalid scoped alias call.
    Node googScopeCall2 = traversal.getScopeCalls().get(1);
    assertEquals(GOOG_SCOPE_USED_IMPROPERLY, traversal.getErrors().get(1).getType());

    // Verify that there are 4 errors in total.
    assertEquals(4, traversal.getErrors().size());
  }

  @Test
  public void testScopedAliasWithThis() {
    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    options.setAliasTransformation(AliasTransformation.STRICT);
    options.setPrintStackFrames(false);
    options.setLanguageIn(JavaScriptLanguage.ECMASCRIPT5_STRICT);

    String sourceCode = "goog.scope(function() {" +
        "  this.x = goog.x;" +
        "  goog.x = 42;" +
        "  assertTrue(this.x === 42);" +
        "});";

    ErrorReport errorReport = new ErrorReport();
    HotSwapCompilerPass scopedAliases = new ScopedAliases(
        compiler, null, new CompilerOptions.AliasTransformationHandler() {});

    Node root = NodeUtil.parseTree(compiler, sourceCode, errorReport);
    scopedAliases.process(null, root);

    Traversal traversal = new Traversal();
    NodeTraversal.traverse(compiler, root, traversal);

    // Verify that there is an error for using 'this' inside a scoped alias.
    Node thisAssignment = traversal.getScopeCalls().get(0).getLastChild()
        .getLastChild().getChildAtIndex(0);
    assertEquals(GOOG_SCOPE_REFERENCES_THIS, traversal.getErrors().get(0).getType());

    // Verify that there are 1 error in total.
    assertEquals(1, traversal.getErrors().size());
  }

  @Test
  public void testScopedAliasWithReturn() {
    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    options.setAliasTransformation(AliasTransformation.STRICT);
    options.setPrintStackFrames(false);
    options.setLanguageIn(JavaScriptLanguage.ECMASCRIPT5_STRICT);

    String sourceCode = "goog.scope(function() {" +
        "  return goog.x;" +
        "});";

    ErrorReport errorReport = new ErrorReport();
    HotSwapCompilerPass scopedAliases = new ScopedAliases(
        compiler, null, new CompilerOptions.AliasTransformationHandler() {});

    Node root = NodeUtil.parseTree(compiler, sourceCode, errorReport);
    scopedAliases.process(null, root);

    Traversal traversal = new Traversal();
    NodeTraversal.traverse(compiler, root, traversal);

    // Verify that there is an error for using 'return' inside a scoped alias.
    Node returnStatement = traversal.getScopeCalls().get(0);
    assertEquals(GOOG_SCOPE_USES_RETURN, traversal.getErrors().get(0).getType());

    // Verify that there are 1 error in total.
    assertEquals(1, traversal.getErrors().size());
  }

  @Test
  public void testScopedAliasWithThrow() {
    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    options.setAliasTransformation(AliasTransformation.STRICT);
    options.setPrintStackFrames(false);
    options.setLanguageIn(JavaScriptLanguage.ECMASCRIPT5_STRICT);

    String sourceCode = "goog.scope(function() {" +
        "  throw new Error('test');" +
        "});";

    ErrorReport errorReport = new ErrorReport();
    HotSwapCompilerPass scopedAliases = new ScopedAliases(
        compiler, null, new CompilerOptions.AliasTransformationHandler() {});

    Node root = NodeUtil.parseTree(compiler, sourceCode, errorReport);
    scopedAliases.process(null, root);

    Traversal traversal = new Traversal();
    NodeTraversal.traverse(compiler, root, traversal);

    // Verify that there is an error for using 'throw' inside a scoped alias.
    Node throwStatement = traversal.getScopeCalls().get(0);
    assertEquals(GOOG_SCOPE_USES_THROW, traversal.getErrors().get(0).getType());

    // Verify that there are 1 error in total.
    assertEquals(1, traversal.getErrors().size());
  }

  @Test
  public void testScopedAliasWithRedefinedAlias() {
    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    options.setAliasTransformation(AliasTransformation.STRICT);
    options.setPrintStackFrames(false);
    options.setLanguageIn(JavaScriptLanguage.ECMASCRIPT5_STRICT);

    String sourceCode = "goog.scope(function() {" +
        "  var x = goog.x;" +
        "  goog.x = 42;" +
        "  var x = 1;" +
        "  assertTrue(x === 1);" +
        "});";

    ErrorReport errorReport = new ErrorReport();
    HotSwapCompilerPass scopedAliases = new ScopedAliases(
        compiler, null, new CompilerOptions.AliasTransformationHandler() {});

    Node root = NodeUtil.parseTree(compiler, sourceCode, errorReport);
    scopedAliases.process(null, root);

    Traversal traversal = new Traversal();
    NodeTraversal.traverse(compiler, root, traversal);

    // Verify that there is an error for redefining an alias.
    Node xAssignment1 = traversal.getScopeCalls().get(0).getLastChild()
        .getLastChild().getChildAtIndex(0);
    assertEquals(GOOG_SCOPE_ALIAS_REDEFINED, traversal.getErrors().get(0).getType());
    assertEquals("x", traversal.getErrors().get(0).getMessage().getArgs()[0]);

    // Verify that there is an error for redefining an alias again.
    Node xAssignment2 = traversal.getScopeCalls().get(0).getLastChild()
        .getLastChild().getChildAtIndex(1);
    assertEquals(GOOG_SCOPE_ALIAS_REDEFINED, traversal.getErrors().get(1).getType());
    assertEquals("x", traversal.getErrors().get(1).getMessage().getArgs()[0]);

    // Verify that there are 2 errors in total.
    assertEquals(2, traversal.getErrors().size());
  }

  @Test
  public void testScopedAliasWithNonAliasLocalVariable() {
    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    options.setAliasTransformation(AliasTransformation.STRICT);
    options.setPrintStackFrames(false);
    options.setLanguageIn(JavaScriptLanguage.ECMASCRIPT5_STRICT);

    String sourceCode = "goog.scope(function() {" +
        "  var x = 1;" +
        "});";

    ErrorReport errorReport = new ErrorReport();
    HotSwapCompilerPass scopedAliases = new ScopedAliases(
        compiler, null, new CompilerOptions.AliasTransformationHandler() {});

    Node root = NodeUtil.parseTree(compiler, sourceCode, errorReport);
    scopedAliases.process(null, root);

    Traversal traversal = new Traversal();
    NodeTraversal.traverse(compiler, root, traversal);

    // Verify that there is an error for using a non-alias local variable inside a scoped alias.
    Node xDeclaration = traversal.getScopeCalls().get(0).getLastChild()
        .getLastChild().getChildAtIndex(0);
    assertEquals(GOOG_SCOPE_NON_ALIAS_LOCAL, traversal.getErrors().get(0).getType());
    assertEquals("x", traversal.getErrors().get(0).getMessage().getArgs()[0]);

    // Verify that there are 1 error in total.
    assertEquals(1, traversal.getErrors().size());
  }

  @Test
  public void testScopedAliasWithMultipleAliases() {
    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    options.setAliasTransformation(AliasTransformation.STRICT);
    options.setPrintStackFrames(false);
    options.setLanguageIn(JavaScriptLanguage.ECMASCRIPT5_STRICT);

    String sourceCode = "goog.scope(function() {" +
        "  var x = goog.x;" +
        "  goog.x = 42;" +
        "  var y = goog.y;" +
        "  goog.y = 43;" +
        "  assertTrue(x === 42 && y === 43);" +
        "});";

    ErrorReport errorReport = new ErrorReport();
    HotSwapCompilerPass scopedAliases = new ScopedAliases(
        compiler, null, new CompilerOptions.AliasTransformationHandler() {});

    Node root = NodeUtil.parseTree(compiler, sourceCode, errorReport);
    scopedAliases.process(null, root);

    Traversal traversal = new Traversal();
    NodeTraversal.traverse(compiler, root, traversal);

    // Verify that x is an alias for goog.x.
    Node xAssignment1 = traversal.getScopeCalls().get(0).getLastChild()
        .getLastChild().getChildAtIndex(0);
    assertEquals("goog.x", xAssignment1.getQualifiedName());

    // Verify that goog.x is assigned the value 42.
    Node xAssignment2 = traversal.getAliasDefinitionsInOrder().get(0);
    Node xValue = xAssignment2.getLastChild();
    assertEquals(Node.NUMBER_LITERAL, xValue.getType());
    assertEquals(42, xValue.getNumber().intValueExact());

    // Verify that y is an alias for goog.y.
    Node yAssignment1 = traversal.getScopeCalls().get(0).getLastChild()
        .getLastChild().getChildAtIndex(2);
    assertEquals("goog.y", yAssignment1.getQualifiedName());

    // Verify that goog.y is assigned the value 43.
    Node yAssignment2 = traversal.getAliasDefinitionsInOrder().get(1);
    Node yValue = yAssignment2.getLastChild();
    assertEquals(Node.NUMBER_LITERAL, yValue.getType());
    assertEquals(43, yValue.getNumber().intValueExact());

    // Verify that the aliases for goog.x and goog.y are applied correctly.
    Node xUsage = traversal.getAliasUsages().get(0);
    assertEquals(xAssignment1, xUsage.applyAlias());
    Node yUsage = traversal.getAliasUsages().get(1);
    assertEquals(yAssignment1, yUsage.applyAlias());

    // Verify that there are no errors.
    assertTrue(errorReport.getDiagnostics().isEmpty());
  }
}