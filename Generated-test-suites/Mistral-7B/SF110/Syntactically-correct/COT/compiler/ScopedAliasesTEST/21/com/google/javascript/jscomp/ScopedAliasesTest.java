import static org.junit.Assert.*;
import static com.google.javascript.jscomp.Diagnostics.*;
import static com.google.javascript.rhino.NodeType.*;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.HotSwapCompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.PreprocessorSymbolTable;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.jscomp.Traversal;
import com.google.javascript.jscomp.AliasTransformationHandler;
import com.google.javascript.rhino.SourcePosition;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class ScopedAliasesTest {
  private AbstractCompiler compiler;
  private PreprocessorSymbolTable preprocessorSymbolTable;
  private AliasTransformationHandler transformationHandler;
  private Traversal traversal;

  @Before
  public void setUp() {
    compiler = new AbstractCompiler() {};
    preprocessorSymbolTable = new PreprocessorSymbolTable();
    transformationHandler = new MockAliasTransformationHandler();
  }

  @Test
  public void testProcessNoAliases() {
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
    Node root = Node.objectLiteral();
    scopedAliases.process(null, root);
    assertTrue(traversal.hasErrors());
  }

  @Test
  public void testProcessWithAliases() {
    String source = "goog.scope(function() {" +
        "  var x = goog.x;" +
        "  goog.scope('x', function() {" +
        "    var x = 10;" +
        "  });" +
        "});";

    Node externs = Node.parse(source, null, null);
    Node root = Node.parse(source, null, null);
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
    scopedAliases.process(externs, root);

    assertEquals(1, traversal.getAliasDefinitionsInOrder().size());
    assertEquals("x", traversal.getAliasDefinitionsInOrder().get(0).getString());
    assertEquals("goog.x", traversal.getAliasDefinitionsInOrder().get(0).getQualifiedName());
  }

  @Test
  public void testProcessInvalidGoogScopeCall() {
    String source = "goog.scope(function() {" +
        "  goog.scope('x', function() {" +
        "    goog.scope('x')"; // extra 'x'
        "  });" +
        "});";

    Node externs = Node.parse(source, null, null);
    Node root = Node.parse(source, null, null);
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
    scopedAliases.process(externs, root);

    assertEquals(1, traversal.getAliasDefinitionsInOrder().size());
    assertEquals("x", traversal.getAliasDefinitionsInOrder().get(0).getString());
    assertEquals("goog.x", traversal.getAliasDefinitionsInOrder().get(0).getQualifiedName());
    assertTrue(traversal.hasErrors());
    assertEquals(GOOG_SCOPE_USED_IMPROPERLY, traversal.getErrorMessage(0));
  }

  @Test
  public void testProcessImproperlyFormattedGoogScopeCall() {
    String source = "goog.scope(" +
        "function() {" +
        "  goog.scope('x', function() {" +
        "  });" +
        "}"; // missing ')'

    Node externs = Node.parse(source, null, null);
    Node root = Node.parse(source, null, null);
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
    scopedAliases.process(externs, root);

    assertTrue(traversal.hasErrors());
    assertEquals(GOOG_SCOPE_USED_IMPROPERLY, traversal.getErrorMessage(0));
  }

  @Test
  public void testProcessImproperlyFormattedGoogScopeFunction() {
    String source = "goog.scope(function() {" +
        "  goog.scope('x', " + // missing 'function'
        "    function() {" +
        "    });" +
        "});";

    Node externs = Node.parse(source, null, null);
    Node root = Node.parse(source, null, null);
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
    scopedAliases.process(externs, root);

    assertTrue(traversal.hasErrors());
    assertEquals(GOOG_SCOPE_HAS_BAD_PARAMETERS, traversal.getErrorMessage(0));
  }

  @Test
  public void testProcessGoogScopeFunctionWithParameters() {
    String source = "goog.scope('foo', function() {" +
        "  goog.scope('bar', function() {" +
        "  });" +
        "});";

    Node externs = Node.parse(source, null, null);
    Node root = Node.parse(source, null, null);
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
    scopedAliases.process(externs, root);

    assertEquals(1, traversal.getScopeCalls().size());
    assertEquals("foo", traversal.getScopeCalls().get(0).getChildAtIndex(1).getString());
    assertEquals(1, traversal.getAliasDefinitionsInOrder().size());
    assertEquals("bar", traversal.getAliasDefinitionsInOrder().get(0).getString());
  }

  @Test
  public void testProcessGoogScopeFunctionWithImproperlyFormattedParameters() {
    String source = "goog.scope('foo', function() {" +
        "  goog.scope('bar', " + // missing 'function'
        "    function() {" +
        "    });" +
        "});";

    Node externs = Node.parse(source, null, null);
    Node root = Node.parse(source, null, null);
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
    scopedAliases.process(externs, root);

    assertEquals(1, traversal.getScopeCalls().size());
    assertEquals("foo", traversal.getScopeCalls().get(0).getChildAtIndex(1).getString());
    assertEquals(1, traversal.getAliasDefinitionsInOrder().size());
    assertEquals("bar", traversal.getAliasDefinitionsInOrder().get(0).getString());
    assertTrue(traversal.hasErrors());
    assertEquals(GOOG_SCOPE_HAS_BAD_PARAMETERS, traversal.getErrorMessage(0));
  }

  @Test
  public void testProcessGoogScopeFunctionWithThisReference() {
    String source = "goog.scope('foo', function() {" +
        "  this.bar = 1;" +
        "  goog.scope('bar', function() {" +
        "  });" +
        "});";

    Node externs = Node.parse(source, null, null);
    Node root = Node.parse(source, null, null);
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
    scopedAliases.process(externs, root);

    assertEquals(1, traversal.getScopeCalls().size());
    assertEquals("foo", traversal.getScopeCalls().get(0).getChildAtIndex(1).getString());
    assertEquals(0, traversal.getAliasDefinitionsInOrder().size());
    assertTrue(traversal.hasErrors());
    assertEquals(GOOG_SCOPE_REFERENCES_THIS, traversal.getErrorMessage(0));
  }

  @Test
  public void testProcessGoogScopeFunctionWithReturnStatement() {
    String source = "goog.scope('foo', function() {" +
        "  return 1;" +
        "  goog.scope('bar', function() {" +
        "  });" +
        "});";

    Node externs = Node.parse(source, null, null);
    Node root = Node.parse(source, null, null);
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
    scopedAliases.process(externs, root);

    assertEquals(1, traversal.getScopeCalls().size());
    assertEquals("foo", traversal.getScopeCalls().get(0).getChildAtIndex(1).getString());
    assertEquals(0, traversal.getAliasDefinitionsInOrder().size());
    assertTrue(traversal.hasErrors());
    assertEquals(GOOG_SCOPE_USES_RETURN, traversal.getErrorMessage(0));
  }

  @Test
  public void testProcessGoogScopeFunctionWithThrowStatement() {
    String source = "goog.scope('foo', function() {" +
        "  throw new Error('foo');" +
        "  goog.scope('bar', function() {" +
        "  });" +
        "});";

    Node externs = Node.parse(source, null, null);
    Node root = Node.parse(source, null, null);
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
    scopedAliases.process(externs, root);

    assertEquals(1, traversal.getScopeCalls().size());
    assertEquals("foo", traversal.getScopeCalls().get(0).getChildAtIndex(1).getString());
    assertEquals(0, traversal.getAliasDefinitionsInOrder().size());
    assertTrue(traversal.hasErrors());
    assertEquals(GOOG_SCOPE_USES_THROW, traversal.getErrorMessage(0));
  }

  @Test
  public void testProcessDuplicateAliasDefinition() {
    String source = "goog.scope('foo', function() {" +
        "  goog.scope('foo', function() {" +
        "  });" +
        "});";

    Node externs = Node.parse(source, null, null);
    Node root = Node.parse(source, null, null);
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
    scopedAliases.process(externs, root);

    assertEquals(1, traversal.getScopeCalls().size());
    assertEquals("foo", traversal.getScopeCalls().get(0).getChildAtIndex(1).getString());
    assertEquals(1, traversal.getAliasDefinitionsInOrder().size());
    assertEquals("foo", traversal.getAliasDefinitionsInOrder().get(0).getString());
    assertTrue(traversal.hasErrors());
    assertEquals(GOOG_SCOPE_ALIAS_REDEFINED, traversal.getErrorMessage(0));
  }

  @Test
  public void testProcessAliasNotInGoogScope() {
    String source = "var x = 1;" +
        "goog.scope('foo', function() {" +
        "  var x = 2;" +
        "});";

    Node externs = Node.parse(source, null, null);
    Node root = Node.parse(source, null, null);
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
    scopedAliases.process(externs, root);

    assertEquals(1, traversal.getScopeCalls().size());
    assertEquals("foo", traversal.getScopeCalls().get(0).getChildAtIndex(1).getString());
    assertEquals(1, traversal.getAliasDefinitionsInOrder().size());
    assertEquals("x", traversal.getAliasDefinitionsInOrder().get(0).getString());
    assertTrue(traversal.hasErrors());
    assertEquals(GOOG_SCOPE_NON_ALIAS_LOCAL, traversal.getErrorMessage(0));
  }

  @Test
  public void testApplyAlias() {
    String source = "goog.scope('foo', function() {" +
        "  var x = goog.x;" +
        "});";

    Node externs = Node.parse(source, null, null);
    Node root = Node.parse(source, null, null);
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
    scopedAliases.process(externs, root);

    assertEquals(1, traversal.getScopeCalls().size());
    assertEquals("foo", traversal.getScopeCalls().get(0).getChildAtIndex(1).getString());
    assertEquals(1, traversal.getAliasDefinitionsInOrder().size());
    assertEquals("x", traversal.getAliasDefinitionsInOrder().get(0).getString());

    // Apply alias
    Node aliasDefinition = traversal.getAliasDefinitionsInOrder().get(0);
    ((AliasedNode) traversal.getAliasUsages().get(0)).applyAlias();

    assertEquals("goog.x", aliasDefinition.getString());
  }

  @Test
  public void testApplyTypeAlias() {
    String source = "goog.scope('foo', function() {" +
        "  var x = goog.X;" +
        "});";

    Node externs = Node.parse(source, null, null);
    Node root = Node.parse(source, null, null);
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
    scopedAliases.process(externs, root);

    assertEquals(1, traversal.getScopeCalls().size());
    assertEquals("foo", traversal.getScopeCalls().get(0).getChildAtIndex(1).getString());
    assertEquals(1, traversal.getAliasDefinitionsInOrder().size());
    assertEquals("X", traversal.getAliasDefinitionsInOrder().get(0).getString());

    // Apply alias
    Node aliasDefinition = traversal.getAliasDefinitionsInOrder().get(0);
    ((AliasedTypeNode) traversal.getAliasUsages().get(0)).applyAlias();

    assertEquals("goog.X", aliasDefinition.getQualifiedName());
  }

  private static class MockAliasTransformationHandler implements AliasTransformationHandler {
    private final List<String> aliases = Lists.newArrayList();

    public AliasTransformation logAliasTransformation(
        String filePath, SourcePosition position) {
      DiagnosticType type = DiagnosticType.warning(
          "JSC_MOCK_ALIAS_TRANSFORMATION",
          "Mock alias transformation for {0}", filePath);
      return new AliasTransformation(type, new String[0]);
    }

    public void logAlias(String alias) {
      aliases.add(alias);
    }

    public List<String> getAliases() {
      return aliases;
    }
  }
}