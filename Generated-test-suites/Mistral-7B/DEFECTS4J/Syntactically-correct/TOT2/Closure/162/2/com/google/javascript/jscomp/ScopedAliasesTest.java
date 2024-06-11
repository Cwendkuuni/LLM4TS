import static org.junit.Assert.*;
import static com.google.javascript.jscomp.Diagnostics.createHelper;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilerOptions.AliasTransformation;
import com.google.javascript.jscomp.Diagnostics;
import com.google.javascript.jscomp.HotSwapCompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Traversal;
import com.google.javascript.jscomp.Traversal.AliasUsage;
import com.google.javascript.jscomp.Traversal.ScopedCallback;
import com.google.javascript.jscomp.Traversal.Traversal;
import com.google.javascript.rhino.SourcePosition;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class ScopedAliasesTest {
  @Test
  public void testScopedAliases() {
    AbstractCompiler compiler = new AbstractCompiler();
    CompilerOptions options = new CompilerOptions();
    options.setAliasTransformation(AliasTransformation.GOOG_SCOPE_ALIASES);
    compiler.setOptions(options);

    ScopedAliases scopedAliases = new ScopedAliases(compiler, null, null);
    Node root = Node.parse("goog.scope(function() {" +
        "  var dom = goog.dom;" +
        "  var DIV = dom.TagName.DIV;" +
        "  goog.dom.createElement(DIV);" +
        "});");

    // Test happy path
    scopedAliases.process(null, root);
    Diagnostics diagnostics = compiler.getDiagnostics();
    assertEquals(0, diagnostics.getNumberOfErrors());

    // Test goog.scope used improperly
    root = Node.parse("goog.scope(function() {" +
        "  var dom = goog.dom;" +
        "  var DIV = dom.TagName.DIV;" +
        "  goog.dom.createElement(DIV);" +
        "  goog.scope(function() {" +
        "    goog.dom.createElement(DIV);" +
        "  });" +
        "});");

    scopedAliases.process(null, root);
    diagnostics = compiler.getDiagnostics();
    // TODO(robbyw): Verify that the correct error is reported.
    assertTrue(diagnostics.hasError(ScopedAliases.GOOG_SCOPE_USED_IMPROPERLY));

    // Test goog.scope has bad parameters
    root = Node.parse("goog.scope(function() {" +
        "  var dom = goog.dom;" +
        "  var DIV = dom.TagName.DIV;" +
        "  goog.scope(function() {});" +
        "  goog.scope(function(x) {" +
        "    goog.dom.createElement(DIV);" +
        "  });" +
        "});");

    scopedAliases.process(null, root);
    diagnostics = compiler.getDiagnostics();
    // TODO(robbyw): Verify that the correct error is reported.
    assertTrue(diagnostics.hasError(ScopedAliases.GOOG_SCOPE_HAS_BAD_PARAMETERS));

    // Test goog.scope references this
    root = Node.parse("goog.scope(function() {" +
        "  var dom = goog.dom;" +
        "  var DIV = dom.TagName.DIV;" +
        "  goog.scope(function() {" +
        "    this.dom = dom;" +
        "    goog.dom.createElement(DIV);" +
        "  });" +
        "});");

    scopedAliases.process(null, root);
    diagnostics = compiler.getDiagnostics();
    // TODO(robbyw): Verify that the correct error is reported.
    assertTrue(diagnostics.hasError(ScopedAliases.GOOG_SCOPE_REFERENCES_THIS));

    // Test goog.scope uses return
    root = Node.parse("goog.scope(function() {" +
        "  var dom = goog.dom;" +
        "  var DIV = dom.TagName.DIV;" +
        "  goog.scope(function() {" +
        "    return goog.dom.createElement(DIV);" +
        "  });" +
        "});");

    scopedAliases.process(null, root);
    diagnostics = compiler.getDiagnostics();
    // TODO(robbyw): Verify that the correct error is reported.
    assertTrue(diagnostics.hasError(ScopedAliases.GOOG_SCOPE_USES_RETURN));

    // Test goog.scope uses throw
    root = Node.parse("goog.scope(function() {" +
        "  var dom = goog.dom;" +
        "  var DIV = dom.TagName.DIV;" +
        "  goog.scope(function() {" +
        "    throw new Error('Test error');" +
        "  });" +
        "});");

    scopedAliases.process(null, root);    diagnostics = compiler.getDiagnostics();
    // TODO(robbyw): Verify that the correct error is reported.
    assertTrue(diagnostics.hasError(ScopedAliases.GOOG_SCOPE_USES_THROW));

    // Test alias reassignment
    root = Node.parse("goog.scope(function() {" +
        "  var dom = goog.dom;" +
        "  var DIV = dom.TagName.DIV;" +
        "  var x = 'test';" +
        "  goog.scope(function() {" +
        "    x = 'new test';" +
        "  });" +
        "  goog.scope(function() {" +
        "    x = 'new test2';" +
        "  });" +
        "  goog.dom.createElement(DIV);" +
        "});");

    scopedAliases.process(null, root);
    diagnostics = compiler.getDiagnostics();
    // TODO(robbyw): Verify that the correct error is reported.
    assertTrue(diagnostics.hasError(ScopedAliases.GOOG_SCOPE_ALIAS_REDEFINED));

    // Test non-alias local variable
    root = Node.parse("goog.scope(function() {" +
        "  var dom = goog.dom;" +
        "  var x = 5;" +
        "  var DIV = dom.TagName.DIV;" +
        "  goog.scope(function() {" +
        "    goog.dom.createElement(DIV);" +
        "  });" +
        "  goog.dom.createElement(DIV);" +
        "});");

    scopedAliases.process(null, root);
    diagnostics = compiler.getDiagnostics();
    // TODO(robbyw): Verify that the correct error is reported.
    assertTrue(diagnostics.hasError(ScopedAliases.GOOG_SCOPE_NON_ALIAS_LOCAL));
  }
}