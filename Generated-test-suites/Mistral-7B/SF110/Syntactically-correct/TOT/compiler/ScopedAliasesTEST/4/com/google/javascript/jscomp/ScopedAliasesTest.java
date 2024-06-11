import org.junit.Test;
import static org.junit.Assert.*;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.HotSwapCompilerPass;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.Node;

public class ScopedAliasesTest {

  @Test
  public void testScopedAliases() {
    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    options.setAliasTransformation(AliasTransformation.GOOG_SCOPE);
    options.setPrintStackTraces(true);

    HotSwapCompilerPass scopedAliases = new ScopedAliases(compiler, null,
        new AliasTransformationHandler() {
          @Override
          public void logAliasTransformation(String sourceFileName,
              SourcePosition<AliasTransformation> position) {
            System.out.println("Alias transformation: " + position.getCode()
                + " -> " + position.getAlias());
          }
        });

    String sourceCode =
        "goog.scope(function() {" +
        "  var x = goog.foo;" +
        "  goog.scope(function() {" +
        "    var y = goog.x;" +
        "    var z = goog.foo.bar;" +
        "  });" +
        "  goog.scope(function() {" +
        "    var w = goog.x;" +
        "  });" +
        "});";

    Node root = Node.parse(sourceCode);

    scopedAliases.process(null, root);

    Node normalizedRoot = NodeUtil.normalize(root);
    assertEquals("goog.foo", normalizedRoot.getChildAtIndex(0).getString());
    assertEquals("goog.foo.bar", normalizedRoot.getChildAtIndex(1).getString());
    assertEquals("goog.foo", normalizedRoot.getChildAtIndex(3).getString());
    assertEquals("goog.foo", normalizedRoot.getChildAtIndex(5).getString());
  }

  @Test
  public void testBadScopeCall() {
    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    options.setAliasTransformation(AliasTransformation.GOOG_SCOPE);
    options.setPrintStackTraces(true);

    HotSwapCompilerPass scopedAliases = new ScopedAliases(compiler, null,
        new AliasTransformationHandler() {
        });

    String sourceCode = "goog.scope('function() {}');";

    Node root = Node.parse(sourceCode);

    scopedAliases.process(null, root);

    assertEquals(1, scopedAliases.getAliasUsages().size());
    assertEquals(1, scopedAliases.getAliasDefinitionsInOrder().size());
    assertEquals(1, scopedAliases.getScopeCalls().size());

    Node errorNode = scopedAliases.getAliasUsages().get(0).getAliasDefinition();
    assertNotNull(errorNode);
    assertEquals(1, errorNode.getChildCount());
    assertEquals("function", errorNode.getChildAtIndex(0).getString());

    String errorMessage = scopedAliases.getAliasDefinitionsInOrder().get(0)
        .getParent().getToken().getImage();
    assertEquals("JSC_GOOG_SCOPE_USED_IMPROPERLY", errorMessage);
  }

  @Test
  public void testScopeWithReturn() {
    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    options.setAliasTransformation(AliasTransformation.GOOG_SCOPE);
    options.setPrintStackTraces(true);

    HotSwapCompilerPass scopedAliases = new ScopedAliases(compiler, null,
        new AliasTransformationHandler() {
        });

    String sourceCode = "goog.scope(function() {" +
        "  return goog.foo;" +
        "});";

    Node root = Node.parse(sourceCode);

    scopedAliases.process(null, root);

    assertEquals(1, scopedAliases.getAliasUsages().size());
    assertEquals(1, scopedAliases.getAliasDefinitionsInOrder().size());
    assertEquals(1, scopedAliases.getScopeCalls().size());

    Node errorNode = scopedAliases.getAliasUsages().get(0).getAliasDefinition();
    assertNotNull(errorNode);
    assertEquals(1, errorNode.getChildCount());
    assertEquals("return", errorNode.getChildAtIndex(0).getString());

    String errorMessage = scopedAliases.getAliasDefinitionsInOrder().get(0)
        .getParent().getToken().getImage();
    assertEquals("JSC_GOOG_SCOPE_USES_RETURN", errorMessage);
  }

  @Test
  public void testScopeWithThrow() {
    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    options.setAliasTransformation(AliasTransformation.GOOG_SCOPE);
    options.setPrintStackTraces(true);

    HotSwapCompilerPass scopedAliases = new ScopedAliases(compiler, null,
        new AliasTransformationHandler() {
        });

    String sourceCode = "goog.scope(function() {" +
        "  throw new Error('Test error');" +
        "});";

    Node root = Node.parse(sourceCode);

    scopedAliases.process(null, root);

    assertEquals(1, scopedAliases.getAliasUsages().size());
    assertEquals(1, scopedAliases.getAliasDefinitionsInOrder().size());
    assertEquals(1, scopedAliases.getScopeCalls().size());

    Node errorNode = scopedAliases.getAliasUsages().get(0).getAliasDefinition();
    assertNotNull(errorNode);
    assertEquals(1, errorNode.getChildCount());
    assertEquals("throw", errorNode.getChildAtIndex(0).getString());

    String errorMessage = scopedAliases.getAliasDefinitionsInOrder().get(0)
        .getParent().getToken().getImage();
    assertEquals("JSC_GOOG_SCOPE_USES_THROW", errorMessage);
  }

  @Test
  public void testScopeWithThis() {
    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    options.setAliasTransformation(AliasTransformation.GOOG_SCOPE);
    options.setPrintStackTraces(true);

    HotSwapCompilerPass scopedAliases = new ScopedAliases(compiler, null,
        new AliasTransformationHandler() {
        });

    String sourceCode = "goog.scope(function() {" +
        "  this.foo = 42;" +
        "});";

    Node root = Node.parse(sourceCode);

    scopedAliases.process(null, root);

    assertEquals(1, scopedAliases.getAliasUsages().size());
    assertEquals(1, scopedAliases.getAliasDefinitionsInOrder().size());
    assertEquals(1, scopedAliases.getScopeCalls().size());

    Node errorNode = scopedAliases.getAliasUsages().get(0).getAliasDefinition();
    assertNotNull(errorNode);
    assertEquals(1, errorNode.getChildCount());
    assertEquals("this", errorNode.getChildAtIndex(0).getString());

    String errorMessage = scopedAliases.getAliasDefinitionsInOrder().get(0)
        .getParent().getToken().getImage();
    assertEquals("JSC_GOOG_SCOPE_REFERENCES_THIS", errorMessage);
  }

  @Test
  public void testAliasRedefinition() {
    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    options.setAliasTransformation(AliasTransformation.GOOG_SCOPE);
    options.setPrintStackTraces(true);

    HotSwapCompilerPass scopedAliases = new ScopedAliases(compiler, null,
        new AliasTransformationHandler() {
        });

    String sourceCode =
        "goog.scope(function() {" +
        "  var x = goog.foo;" +
        "  goog.scope(function() {" +
        "    var x = goog.bar;" +
        "  });" +
        "});";

    Node root = Node.parse(sourceCode);

    scopedAliases.process(null, root);

    assertEquals(2, scopedAliases.getAliasUsages().size());
    assertEquals(1, scopedAliases.getAliasDefinitionsInOrder().size());
    assertEquals(1, scopedAliases.getScopeCalls().size());

    Node errorNode = scopedAliases.getAliasUsages().get(1).getAliasDefinition();
    assertNotNull(errorNode);
    assertEquals(1, errorNode.getChildCount());
    assertEquals("x", errorNode.getChildAtIndex(0).getString());

    String errorMessage = scopedAliases.getAliasDefinitionsInOrder().get(0)
        .getParent().getToken().getImage();
    assertEquals("JSC_GOOG_SCOPE_ALIAS_REDEFINED", errorMessage);
  }
}