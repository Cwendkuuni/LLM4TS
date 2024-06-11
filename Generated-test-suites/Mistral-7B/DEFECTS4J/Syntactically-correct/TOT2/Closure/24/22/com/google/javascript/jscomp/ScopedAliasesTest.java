import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CompilerOptions.AliasTransformation.*;

import com.google.common.collect.Lists;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.HotSwapCompilerPass;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.SourceFile;

import java.util.List;
import java.util.Map;

/**
 * Tests for the ScopedAliases class.
 */
public class ScopedAliasesTest {

  private static Compiler compiler;
  private static SourceFile sourceFile;

  @BeforeClass
  public static void setUp() {
    compiler = new Compiler();
    compiler.setOptions(new CompilerOptions());
    compiler.addPass(new HotSwapCompilerPass());
    compiler.addPass(new ScopedAliases(compiler, null, null));
    sourceFile = new SourceFile("test.js");
  }

  @AfterClass
  public static void tearDown() {
    compiler = null;
    sourceFile = null;
  }

  /**
   * Tests that goog.scope is processed correctly.
   */
  @Test
  public void testGoogScope() {
    String code = "goog.scope(function() {" +
        "var dom = goog.dom;" +
        "var DIV = dom.TagName.DIV;" +
        "goog.dom.createElement(DIV);" +
        "});";

    List<DiagnosticType> errors = compile(code);
    assertTrue(errors.isEmpty());

    List<Node> ast = compiler.getAst();
    Node root = ast.get(0);
    assertEquals("goog.dom.createElement(goog.dom.TagName.DIV)",
        root.toString());
  }

  /**
   * Tests that goog.scope with improper usage is detected and reported.
   */
  @Test
  public void testGoogScopeImproperUsage() {
    String code = "goog.scope(function() {" +
        "var dom = goog.dom;" +
        "goog.scope(function() {" +
        "dom.createElement(goog.dom.TagName.DIV);" +
        "});" +
        "});";

    List<DiagnosticType> errors = compile(code);
    assertEquals(1, errors.size());
    assertEquals(GOOG_SCOPE_USED_IMPROPERLY, errors.get(0));
  }

  /**
   * Tests that goog.scope with bad parameters is detected and reported.
   */
  @Test
  public void testGoogScopeBadParameters() {
    String code = "goog.scope(function(a, b) {" +
        "var dom = goog.dom;" +
        "goog.dom.createElement(goog.dom.TagName.DIV);" +
        "});";

    List<DiagnosticType> errors = compile(code);
    assertEquals(1, errors.size());
    assertEquals(GOOG_SCOPE_HAS_BAD_PARAMETERS, errors.get(0));
  }

  /**
   * Tests that goog.scope with reference to 'this' is detected and reported.
   */
  @Test
  public void testGoogScopeThis() {
    String code = "goog.scope(function() {" +
        "this.dom = goog.dom;" +
        "goog.dom.createElement(goog.dom.TagName.DIV);" +
        "});";

    List<DiagnosticType> errors = compile(code);
    assertEquals(1, errors.size());
    assertEquals(GOOG_SCOPE_REFERENCES_THIS, errors.get(0));
  }

  /**
   * Tests that goog.scope with return statement is detected and reported.
   */
  @Test
  public void testGoogScopeUseReturn() {
    String code = "goog.scope(function() {" +
        "return goog.dom.createElement(goog.dom.TagName.DIV);" +
        "});";

    List<DiagnosticType> errors = compile(code);
    assertEquals(1, errors.size());
    assertEquals(GOOG_SCOPE_USES_RETURN, errors.get(0));
  }

  /**
   * Tests that goog.scope with throw statement is detected and reported.
   */
  @Test
  public void testGoogScopeUseThrow() {
    String code = "goog.scope(function() {" +
        "throw new Error('Test');" +
        "});";

    List<DiagnosticType> errors = compile(code);
    assertEquals(1, errors.size());
    assertEquals(GOOG_SCOPE_USES_THROW, errors.get(0));
  }

  /**
   * Tests that alias redefinition is detected and reported.
   */
  @Test
  public void testAliasRedefinition() {
    String code = "goog.scope(function() {" +
        "var dom = goog.dom;" +
        "var DIV = goog.dom.TagName.DIV;" +
        "goog.scope(function() {" +
        "var DIV = 'newType';" +
        "});" +
        "goog.dom.createElement(DIV);" +
        "});";

    List<DiagnosticType> errors = compile(code);
    assertEquals(1, errors.size());
    assertEquals(GOOG_SCOPE_ALIAS_REDEFINED, errors.get(0));
  }

  /**
   * Tests that non-alias local variable is not allowed inside goog.scope.
   */
  @Test
  public void testNonAliasLocalVariable() {
    String code = "goog.scope(function() {" +
        "var dom = goog.dom;" +
        "var local = 'local';" +
        "goog.dom.createElement(goog.dom.TagName.DIV);" +
        "});";

    List<DiagnosticType> errors = compile(code);
    assertEquals(1, errors.size());
    assertEquals(GOOG_SCOPE_NON_ALIAS_LOCAL, errors.get(0));
  }

  /**
   * Helper method to compile the given code and return the errors.
   */
  private List<DiagnosticType> compile(String code) {
    sourceFile.setCode(code);
    List<DiagnosticType> errors = Lists.newArrayList();
    try {
      compiler.compile(sourceFile);
    } catch (Compiler.CompilationFailedException e) {
      errors.addAll(e.getDiagnostics());
    }
    return errors;
  }
}