import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.jscomp.CompilationTestBase;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.HotSwapCompilerPass;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Traversal;
import com.google.javascript.rhino.Node;

public class ScopedAliasesTest extends CompilationTestBase {

  @Test
  public void testScopedAliases() {
    String jsCode = "goog.scope(function() {\n" +
        "  var dom = goog.dom; \n" +
        "  var DIV = dom.TagName.DIV;\n" +
        "  dom.createElement(DIV);\n" +
        "});";

    Compiler compiler = newCompiler();
    HotSwapCompilerPass scopedAliases = new ScopedAliases(compiler, null, null);
    Node root = parseTree(compiler, jsCode);
    scopedAliases.process(null, root);

    Node normalizedCode = compiler.getNormalizedSource();
    assertEquals("goog.dom.createElement(goog.dom.TagName.DIV);",
        normalizeSource(normalizedCode));
  }

  @Test
  public void testScopedAliasesWithBadParameters() {
    String jsCode = "goog.scope(function(a, b) {});";

    Compiler compiler = newCompiler();
    HotSwapCompilerPass scopedAliases = new ScopedAliases(compiler, null, null);
    Node root = parseTree(compiler, jsCode);
    scopedAliases.process(null, root);

    List<String> errors = compiler.getErrors();
    assertEquals(1, errors.size());
    assertEquals("The call to goog.scope must take only a single anonymous" +
        " function as a parameter.", errors.get(0));
  }

  @Test
  public void testScopedAliasesWithImproperUsage() {
    String jsCode = "goog.scope(function() {\n" +
        "  var dom = goog.dom;\n" +
        "  var DIV = dom.TagName.DIV;\n" +
        "  dom.createElement(DIV);\n" +
        "  // Another statement.\n" +
        "});";

    Compiler compiler = newCompiler();
    HotSwapCompilerPass scopedAliases = new ScopedAliases(compiler, null, null);
    Node root = parseTree(compiler, jsCode);
    scopedAliases.process(null, root);

    List<String> errors = compiler.getErrors();
    assertEquals(1, errors.size());
    assertEquals("The call to goog.scope must be alone in a single statement.",
        errors.get(0));
  }

  @Test
  public void testScopedAliasesWithThisKeyword() {
    String jsCode = "goog.scope(function() {\n" +
        "  this.dom = goog.dom;\n" +
        "});";

    Compiler compiler = newCompiler();
    HotSwapCompilerPass scopedAliases = new ScopedAliases(compiler, null, null);
    Node root = parseTree(compiler, jsCode);
    scopedAliases.process(null, root);

    List<String> errors = compiler.getErrors();
    assertEquals(1, errors.size());
    assertEquals("The body of a goog.scope function cannot reference 'this'.",
        errors.get(0));
  }

  @Test
  public void testScopedAliasesWithReturnStatement() {
    String jsCode = "goog.scope(function() {\n" +
        "  return {};\n" +
        "});";

    Compiler compiler = newCompiler();
    HotSwapCompilerPass scopedAliases = new ScopedAliases(compiler, null, null);
    Node root = parseTree(compiler, jsCode);
    scopedAliases.process(null, root);

    List<String> errors = compiler.getErrors();
    assertEquals(1, errors.size());
    assertEquals("The body of a goog.scope function cannot use 'return'.",
        errors.get(0));
  }

  @Test
  public void testScopedAliasesWithThrowStatement() {
    String jsCode = "goog.scope(function() {\n" +
        "  throw new Error('Test error');\n" +
        "});";

    Compiler compiler = newCompiler();
    HotSwapCompilerPass scopedAliases = new ScopedAliases(compiler, null, null);
    Node root = parseTree(compiler, jsCode);
    scopedAliases.process(null, root);

    List<String> errors = compiler.getErrors();
    assertEquals(1, errors.size());
    assertEquals("The body of a goog.scope function cannot use 'throw'.",
        errors.get(0));
  }

  @Test
  public void testScopedAliasesWithAliasRedefinition() {
    String jsCode = "goog.scope(function() {\n" +
        "  var dom = goog.dom;\n" +
        "  var DIV = dom.TagName.DIV;\n" +
        "  var DIV = 'div';\n" +
        "});";

    Compiler compiler = newCompiler();
    HotSwapCompilerPass scopedAliases = new ScopedAliases(compiler, null, null);
    Node root = parseTree(compiler, jsCode);
    scopedAliases.process(null, root);

    List<String> errors = compiler.getErrors();
    assertEquals(1, errors.size());
    assertEquals("The alias 'DIV' is assigned a value more than once.",
        errors.get(0));
  }
}