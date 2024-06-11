import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.AliasTransformation;
import com.google.javascript.jscomp.AliasTransformationHandler;
import com.google.javascript.jscomp.CompilationResult;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Flags;
import com.google.javascript.jscomp.HotSwapCompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.SourcePosition;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.NodeTraversal.ScopedCallback;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Map;

public class ScopedAliasesTest {

  private static AliasTransformationHandler mockHandler;
  private AbstractCompiler compiler;
  private ScopedAliases scopedAliases;
  private Node rootNode;

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    mockHandler = mock(AliasTransformationHandler.class);

    compiler = new AbstractCompiler();
    compiler.setCompilerOptions(
        new CompilerOptions() {{
          add(Flags.ALLOW_UNTAGGED_TEMPLATE_LITERALS);
          add(Flags.STRICT_MODE_CHECKS);
          add(Flags.PARANOID);
          add(Flags.VERBOSE_AST);
          add(Flags.WARNINGS_AS_ERRORS);
          add(AliasTransformation.class);
        }}
    );
    compiler.addPass(new HotSwapCompilerPass(compiler, null, mockHandler));

    scopedAliases = new ScopedAliases(compiler, null, mockHandler);

    rootNode = NodeUtil.parseJsTimeString(
        "function test() {" +
            "goog.scope(function() {" +
            "  var a = goog.x;" +
            "  goog.scope(" +
            "    function() {" +
            "      var b = goog.y;" +
            "    }" +
            "  );" +
            "  var c = goog.z;" +
            "  goog.scope(function() {" +
            "    var d = goog.x;" +
            "  });" +
            "  var e = goog.foo;" +
            "});" +
            "console.log(a, b, c, d, e);" +
            "}"
    );
  }

  @Test
  public void testProcess() {
    scopedAliases.process(null, rootNode);
    assertTrue(compilationSucceeded());
    assertSame("goog.x", rootNode.getChildAtIndex(2).getString());
    assertSame("goog.y", rootNode.getChildAtIndex(5).getString());
    assertSame("goog.z", rootNode.getChildAtIndex(8).getString());
    assertNull(rootNode.getChildAtIndex(10));
  }

  @Test(expected = AssertionError.class)
  public void testProcessWithBadScopeCall() {
    rootNode = NodeUtil.parseJsTimeString(
        "function test() {" +
            "goog.scope(function() {" +
            "  var a = goog.x;" +
            "});" +
            "console.log(a);" +
            "}"
    );
    scopedAliases.process(null, rootNode);
  }

  @Test(expected = AssertionError.class)
  public void testProcessWithImproperUsage() {
    rootNode = NodeUtil.parseJsTimeString(
        "function test() {" +
            "goog.scope(function() {" +
            "  var a = goog.x;" +
            "  console.log(a);" +
            "});" +
            "goog.scope(function() {" +
            "  var b = goog.x;" +
            "});" +
            "console.log(b);" +
            "}"
    );
    scopedAliases.process(null, rootNode);
  }

  @Test(expected = AssertionError.class)
  public void testProcessWithThisUsage() {
    rootNode = NodeUtil.parseJsTimeString(
        "function test() {" +
            "goog.scope(function() {" +
            "  this.a = goog.x;" +
            "});" +
            "console.log(this.a);" +
            "}"
    );
    scopedAliases.process(null, rootNode);
  }

  @Test(expected = AssertionError.class)
  public void testProcessWithReturnUsage() {
    rootNode = NodeUtil.parseJsTimeString(
        "function test() {" +
            "goog.scope(function() {" +
            "  return goog.x;" +
            "});" +
            "console.log(goog.x);" +
            "}"
    );
    scopedAliases.process(null, rootNode);
  }

  @Test(expected = AssertionError.class)
  public void testProcessWithThrowUsage() {
    rootNode = NodeUtil.parseJsTimeString(
        "function test() {" +
            "goog.scope(function() {" +
            "  throw new Error('Error in goog.scope');" +
            "});" +
            "}"
    );
    scopedAliases.process(null, rootNode);
  }

  @Test
  public void testProcessWithAliasRedefinition() {
    rootNode = NodeUtil.parseJsTimeString(
        "function test() {" +
            "goog.scope(function() {" +
            "  var a = goog.x;" +
            "  goog.scope(function() {" +
            "    var a = goog.y;" +
            "  });" +
            "  console.log(a);" +
            "});" +
            "}"
    );
    scopedAliases.process(null, rootNode);
    String errorMessage = getLastErrorMessage();
    assertTrue(errorMessage.contains("JSC_GOOG_SCOPE_ALIAS_REDEFINED"));
  }

  @Test
  public void testProcessWithNonAliasLocal() {
    rootNode = NodeUtil.parseJsTimeString(
        "function test() {" +
            "goog.scope(function() {" +
            "  var a = 1;" +
            "});" +
            "console.log(a);" +
            "}"
    );
    scopedAliases.process(null, rootNode);
    String errorMessage = getLastErrorMessage();
    assertTrue(errorMessage.contains("JSC_GOOG_SCOPE_NON_ALIAS_LOCAL"));
  }

  @Test
  public void testProcessWithNamespaceShadow() {
    rootNode = NodeUtil.parseJsTimeString(
        "function test() {" +
            "goog.scope(function() {" +
            "  var goog = {};" +
            "  goog.x = 1;" +
            "  goog.scope(function() {" +
            "    var x = 2;" +
            "  });" +
            "  console.log(x);" +
            "});" +
            "}"
    );
    scopedAliases.process(null, rootNode);
    String errorMessage = getLastErrorMessage();
    assertTrue(errorMessage.contains("JSC_GOOG_SCOPE_NON_ALIAS_LOCAL"));
  }

  private boolean compilationSucceeded() {
    CompilationResult result = compiler.compile(rootNode, null, null);
    return result.getErrors().isEmpty();
  }

  private String getLastErrorMessage() {
    return compiler.getLatestError().getMessage();
  }
}