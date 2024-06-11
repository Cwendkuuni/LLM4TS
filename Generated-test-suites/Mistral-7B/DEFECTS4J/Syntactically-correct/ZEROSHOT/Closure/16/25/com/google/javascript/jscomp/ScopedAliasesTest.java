import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.AliasTransformationHandler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.HotSwapCompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Traversal;
import com.google.javascript.jscomp.Traversal.AliasUsage;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.SourcePosition;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Map;

public class ScopedAliasesTest {
  private AbstractCompiler compiler;
  private CompilerOptions options;
  private AliasTransformationHandler transformationHandler;

  @Before
  public void setup() {
    options = new CompilerOptions();
    transformationHandler = mock(AliasTransformationHandler.class);
    compiler = new AbstractCompiler(options);
    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void testBasicScopedAliases() {
    String sourceCode = "goog.scope(function() {" +
        "  var dom = goog.dom;" +
        "  var DIV = dom.TagName.DIV;" +
        "  dom.createElement(DIV);" +
        "});";

    ScopedAliases scopedAliases = new ScopedAliases(compiler, null, transformationHandler);

    Node root = new Node(new SourcePosition(0, 0), null, null);
    Node externs = new Node(new SourcePosition(0, 0), null, null);
    scopedAliases.process(externs, root);

    Traversal traversal = scopedAliases.getLastTraversal();

    // Check that the scoped aliases were applied correctly.
    List<Node> aliasDefinitionsInOrder = traversal.getAliasDefinitionsInOrder();
    assertEquals(1, aliasDefinitionsInOrder.size());
    Node domAliasDefinition = aliasDefinitionsInOrder.get(0);
    assertEquals("goog.dom", domAliasDefinition.getQualifiedName());
    assertEquals(Token.ASSIGN, domAliasDefinition.getType());

    Node domAliasUsage = traversal.getAliasUsages().get(0);
    assertEquals(domAliasDefinition, domAliasUsage.applyAlias());
    assertEquals("goog.dom", domAliasUsage.getParent().getString());

    // Check that the goog.scope calls were removed.
    List<Node> scopeCalls = traversal.getScopeCalls();
    assertEquals(1, scopeCalls.size());
    Node googScopeCall = scopeCalls.get(0);
    assertNull(googScopeCall.getParent());

    // Check that the errors were reported correctly.
    List<DiagnosticType> errors = compiler.getErrors();
    assertEquals(0, errors.size());
  }

  @Test
  public void testImproperlyUsedScopedAliases() {
    String sourceCode = "goog.scope(function() {" +
        "  var dom = goog.dom;" +
        "  var DIV = dom.TagName.DIV;" +
        "  dom.createElement(DIV);" +
        "  goog.scope(function() {" +
        "    var x = 1;" +
        "  });" +
        "});";

    ScopedAliases scopedAliases = new ScopedAliases(compiler, null, transformationHandler);

    Node root = new Node(new SourcePosition(0, 0), null, null);
    Node externs = new Node(new SourcePosition(0, 0), null, null);
    scopedAliases.process(externs, root);

    Traversal traversal = scopedAliases.getLastTraversal();

    List<Node> aliasDefinitionsInOrder = traversal.getAliasDefinitionsInOrder();
    assertEquals(1, aliasDefinitionsInOrder.size());

    List<DiagnosticType> errors = compiler.getErrors();
    assertEquals(1, errors.size());
    DiagnosticType error = errors.get(0);
    assertEquals("JSC_GOOG_SCOPE_USED_IMPROPERLY", error.getTypeName());
  }

  @Test
  public void testBadParametersInScopedAliases() {
    String sourceCode = "goog.scope(function() {" +
        "  var dom = goog.dom;" +
        "  var DIV = dom.TagName.DIV;" +
        "  goog.scope(function(x) {" +
        "    dom.createElement(DIV);" +
        "  });" +
        "});";

    ScopedAliases scopedAliases = new ScopedAliases(compiler, null, transformationHandler);

    Node root = new Node(new SourcePosition(0, 0), null, null);
    Node externs = new Node(new SourcePosition(0, 0), null, null);
    scopedAliases.process(externs, root);

    Traversal traversal = scopedAliases.getLastTraversal();

    List<Node> aliasDefinitionsInOrder = traversal.getAliasDefinitionsInOrder();
    assertEquals(1, aliasDefinitionsInOrder.size());

    List<DiagnosticType> errors = compiler.getErrors();
    assertEquals(1, errors.size());
    DiagnosticType error = errors.get(0);
    assertEquals("JSC_GOOG_SCOPE_HAS_BAD_PARAMETERS", error.getTypeName());
  }

  @Test
  public void testAliasRedefined() {
    String sourceCode = "goog.scope(function() {" +
        "  var dom = goog.dom;" +
        "  var DIV = dom.TagName.DIV;" +
        "  var DIV = 'div';" +
        "  dom.createElement(DIV);" +
        "});";

    ScopedAliases scopedAliases = new ScopedAliases(compiler, null, transformationHandler);

    Node root = new Node(new SourcePosition(0, 0), null, null);
    Node externs = new Node(new SourcePosition(0, 0), null, null);
    scopedAliases.process(externs, root);

    Traversal traversal = scopedAliases.getLastTraversal();

    List<DiagnosticType> errors = compiler.getErrors();
    assertEquals(1, errors.size());
    DiagnosticType error = errors.get(0);
    assertEquals("JSC_GOOG_SCOPE_ALIAS_REDEFINED", error.getTypeName());
  }
}