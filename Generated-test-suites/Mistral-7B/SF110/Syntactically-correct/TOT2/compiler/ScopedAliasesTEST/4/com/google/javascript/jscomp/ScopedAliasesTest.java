import static org.junit.Assert.*;
import static com.google.javascript.jscomp.ScopedAliases.GOOG_SCOPE_USED_IMPROPERLY;
import static com.google.javascript.jscomp.ScopedAliases.GOOG_SCOPE_HAS_BAD_PARAMETERS;
import static com.google.javascript.jscomp.ScopedAliases.GOOG_SCOPE_REFERENCES_THIS;
import static com.google.javascript.jscomp.ScopedAliases.GOOG_SCOPE_USES_RETURN;
import static com.google.javascript.jscomp.ScopedAliases.GOOG_SCOPE_USES_THROW;
import static com.google.javascript.jscomp.ScopedAliases.GOOG_SCOPE_ALIAS_REDEFINED;
import static com.google.javascript.jscomp.ScopedAliases.GOOG_SCOPE_NON_ALIAS_LOCAL;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilerOptions.AliasTransformation;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Traversal;
import com.google.javascript.rhino.SourceFile;
import com.google.javascript.rhino.TEST_FOLDER;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.TestRule;
import org.junit.rules.TestRuleArray;

import javax.annotation.Nullable;

public class ScopedAliasesTest {

  private AbstractCompiler compiler;
  private CompilerOptions options;
  private SourceFile sourceFile;

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  @Rule
  public TestRuleArray rules = new TestRuleArray();

  @Rule
  public TestRule errorRule = new TestRule() {
    @Override
    public void apply(@Nullable Statement statement, @Nullable Throwable throwable) {
      if (throwable instanceof AssertionError) {
        throw new RuntimeException("Unexpected assertion error", throwable);
      }
      if (throwable != null) {
        thrown.expect(throwable.getClass());
        thrown.expectMessage(throwable.getMessage());
      }
    }
  };

  @Before
  public void setup() {
    compiler = new AbstractCompiler();
    options = new CompilerOptions();
    options.setAliasTransformation(AliasTransformation.GOOG_ALIASES);
    sourceFile = new SourceFile("Test.js", TEST_FOLDER + "/Test.js");
  }

  @Test
  public void testProperUsage() {
    String code = "goog.scope(function() {" +
        "  var x = goog.dom; " +
        "  var div = x.createElement(x.TagName.DIV); " +
        "});";

    Node root = NodeUtil.parse(compiler, new StringReader(code));

    Traversal traversal = new Traversal();
    NodeTraversal.traverse(compiler, root, traversal);

    // No errors should be reported
    assertEquals(0, traversal.getErrorCount());

    // No aliases should be found
    assertTrue(traversal.getAliasUsages().isEmpty());

    // No scope calls should be found
    assertTrue(traversal.getScopeCalls().isEmpty());

    // Check the result
    Node result = NodeUtil.getTopLevelNode(compiler.getJsDocInfo(root));
    assertNotNull(result);
    assertEquals(Node.BLOCK, result.getType());
  }

  @Test
  public void testImproperUsage() {
    String code = "goog.scope(function() {" +
        "  var x = goog.dom;" +
        "  var y = goog.dom;" +
        "});";

    Node root = NodeUtil.parse(compiler, new StringReader(code));

    Traversal traversal = new Traversal();
    NodeTraversal.traverse(compiler, root, traversal);

    // One error should be reported
    assertEquals(1, traversal.getErrorCount());

    // No aliases should be found
    assertTrue(traversal.getAliasUsages().isEmpty());

    // No scope calls should be found
    assertTrue(traversal.getScopeCalls().isEmpty());

    // Check the error
    DiagnosticInfo error = traversal.getErrors().get(0);
    assertEquals(GOOG_SCOPE_USED_IMPROPERLY, error.getType());
    assertEquals("The call to goog.scope must be alone in a single statement.",
        error.getMessage());
    assertEquals(error.getSourceFile().getName(), error.getSourceName());
  }

  @Test
  public void testBadParameters() {
    String code = "goog.scope(function goo() {" +
        "  var x = goog.dom;" +
        "});";

    Node root = NodeUtil.parse(compiler, new StringReader(code));

    Traversal traversal = new Traversal();
    NodeTraversal.traverse(compiler, root, traversal);

    // One error should be reported
    assertEquals(1, traversal.getErrorCount());

    // No aliases should be found
    assertTrue(traversal.getAliasUsages().isEmpty());

    // No scope calls should be found
    assertTrue(traversal.getScopeCalls().isEmpty());

    // Check the error
    DiagnosticInfo error = traversal.getErrors().get(0);
    assertEquals(GOOG_SCOPE_HAS_BAD_PARAMETERS, error.getType());
    assertEquals("The call to goog.scope must take only a single parameter.  It must" +
                 " be an anonymous function that itself takes no parameters.",
        error.getMessage());
    assertEquals("Test.js", error.getSourceName());
  }

  @Test
  public void testThisReference() {
    String code = "goog.scope(function() {" +
        "  var x = goog.dom;" +
        "  this.x = x.createElement(x.TagName.DIV); " +
        "});";

    Node root = NodeUtil.parse(compiler, new StringReader(code));

    Traversal traversal = new Traversal();
    NodeTraversal.traverse(compiler, root, traversal);

    // One error should be reported
    assertEquals(1, traversal.getErrorCount());

    // No aliases should be found
    assertTrue(traversal.getAliasUsages().isEmpty());

    // No scope calls should be found
    assertTrue(traversal.getScopeCalls().isEmpty());

    // Check the error
    DiagnosticInfo error = traversal.getErrors().get(0);
    assertEquals(GOOG_SCOPE_REFERENCES_THIS, error.getType());
    assertEquals("The body of a goog.scope function cannot reference 'this'.",
        error.getMessage());
    assertEquals("Test.js", error.getSourceName());
  }

  @Test
  public void testReturnStatement() {
    String code = "goog.scope(function() {" +
        "  var x = goog.dom;" +
        "  return x.createElement(x.TagName.DIV); " +
        "});";

    Node root = NodeUtil.parse(compiler, new StringReader(code));

    Traversal traversal = new Traversal();
    NodeTraversal.traverse(compiler, root, traversal);

    // One error should be reported
    assertEquals(1, traversal.getErrorCount());

    // No aliases should be found
    assertTrue(traversal.getAliasUsages().isEmpty());

    // No scope calls should be found
    assertTrue(traversal.getScopeCalls().isEmpty());

    // Check the error
    DiagnosticInfo error = traversal.getErrors().get(0);
    assertEquals(GOOG_SCOPE_USES_RETURN, error.getType());
    assertEquals("The body of a goog.scope function cannot use 'return'.",
        error.getMessage());
    assertEquals("Test.js", error.getSourceName());
  }

  @Test
  public void testThrowStatement() {
    String code = "goog.scope(function() {" +
        "  try {" +
        "    var x = goog.dom;" +
        "  } catch (e) {" +
        "    throw new Error('test'); " +
        "  } " +
        "});";

    Node root = NodeUtil.parse(compiler, new StringReader(code));

    Traversal traversal = new Traversal();
    NodeTraversal.traverse(compiler, root, traversal);

    // One error should be reported
    assertEquals(1, traversal.getErrorCount());

    // No aliases should be found
    assertTrue(traversal.getAliasUsages().isEmpty());

    // No scope calls should be found
    assertTrue(traversal.getScopeCalls().isEmpty());

    // Check the error
    DiagnosticInfo error = traversal.getErrors().get(0);
    assertEquals(GOOG_SCOPE_USES_THROW, error.getType());
    assertEquals("The body of a goog.scope function cannot use 'throw'.",
        error.getMessage());
    assertEquals("Test.js", error.getSourceName());
  }

  @Test
  public void testAliasRedefinition() {
    String code = "goog.scope(function() {" +
        "  var x = goog.dom;" +
        "  var x = goog.foo; " +
        "});";

    Node root = NodeUtil.parse(compiler, new StringReader(code));

    Traversal traversal = new Traversal();
    NodeTraversal.traverse(compiler, root, traversal);

    // One error should be reported
    assertEquals(1, traversal.getErrorCount());

    // No aliases should be found
    assertTrue(traversal.getAliasUsages().isEmpty());

    // No scope calls should be found
    assertTrue(traversal.getScopeCalls().isEmpty());

    // Check the error
    DiagnosticInfo error = traversal.getErrors().get(0);
    assertEquals(GOOG_SCOPE_ALIAS_REDEFINED, error.getType());
    assertEquals("The alias 'x' is assigned a value more than once.",
        error.getMessage());
  }

  @Test
  public void testNonAliasLocal() {
    String code = "goog.scope(function() {" +
        "  var x = 1;" +
        "});";

    Node root = NodeUtil.parse(compiler, new StringReader(code));

    Traversal traversal = new Traversal();
    NodeTraversal.traverse(compiler, root, traversal);

    // One error should be reported
    assertEquals(1, traversal.getErrorCount());

    // No aliases should be found
    assertTrue(traversal.getAliasUsages().isEmpty());

    // No scope calls should be found
    assertTrue(traversal.getScopeCalls().isEmpty());

    // Check the error
    DiagnosticInfo error = traversal.getErrors().get(0);
    assertEquals(GOOG_SCOPE_NON_ALIAS_LOCAL, error.getType());
    assertEquals("The local variable 'x' is in a goog.scope and is not an alias.",
        error.getMessage());
  }

  @Test
  public void testMultipleAliases() {
    String code = "goog.scope(function() {" +
        "  var x = goog.dom;" +
        "  var y = goog.foo;" +
        "});";

    Node root = NodeUtil.parse(compiler, new StringReader(code));

    Traversal traversal = new Traversal();
    NodeTraversal.traverse(compiler, root, traversal);

    // No errors should be reported
    assertEquals(0, traversal.getErrorCount());

    // Two aliases should be found
    List<Node> aliasDefinitions = traversal.getAliasDefinitionsInOrder();
    assertEquals(2, aliasDefinitions.size());
    assertEquals("dom", aliasDefinitions.get(0).getString());
    assertEquals("foo", aliasDefinitions.get(1).getString());

    // No scope calls should be found
    assertTrue(traversal.getScopeCalls().isEmpty());

    // Check the aliases
    List<AliasUsage> aliasUsages = traversal.getAliasUsages();
    assertEquals(2, aliasUsages.size());

    AliasUsage aliasUsage1 = aliasUsages.get(0);
    assertEquals("dom", aliasUsage1.getClass().getSimpleName());
    Node aliasDefinition1 = ((AliasedNode) aliasUsage1).aliasDefinition;
    assertEquals("dom", aliasDefinition1.getString());

    AliasUsage aliasUsage2 = aliasUsages.get(1);
    assertEquals("foo", aliasUsage2.getClass().getSimpleName());
    Node aliasDefinition2 = ((AliasedNode) aliasUsage2).aliasDefinition;
    assertEquals("foo", aliasDefinition2.getString());
  }
}