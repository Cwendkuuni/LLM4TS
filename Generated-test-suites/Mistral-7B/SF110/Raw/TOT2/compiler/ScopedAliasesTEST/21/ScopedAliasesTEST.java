import com.google.javascript.jscomp.ScopedAliases;
import com.google.javascript.jscomp.CompilerOptions.AliasTransformation;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ScopedAliasesTest {

  private AbstractCompiler compiler;
  private PreprocessorSymbolTable preprocessorSymbolTable;
  private AliasTransformationHandler transformationHandler;
  private ScopedAliases scopedAliases;

  @Before
  public void setUp() {
    compiler = new Compiler();
    preprocessorSymbolTable = new PreprocessorSymbolTable();
    transformationHandler = new AliasTransformationHandler();
    scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
  }

  @Test
  public void testBasicAliasUsage() {
    String sourceCode = "goog.scope(function() {\n" +
        "  var x = goog.x; // should be renamed to goog.dom.x\n" +
        "  assertEquals('div', x.createElement(goog.dom.TagName.DIV));\n" +
        "});";

    Node root = ParserUtil.parse(sourceCode);
    scopedAliases.process(null, root);
    assertEquals(0, compiler.getDiagnostics().size());
  }

  @Test
  public void testImproperAliasUsage() {
    String sourceCode = "goog.scope(function() {\n" +
        "  goog.scope(function() {\n" +
        "    // should result in GOOG_SCOPE_USED_IMPROPERLY error\n" +
        "  });\n" +
        "});";

    Node root = ParserUtil.parse(sourceCode);
    scopedAliases.process(null, root);
    assertEquals(1, compiler.getDiagnostics().size());
    assertEquals("JSC_GOOG_SCOPE_USED_IMPROPERLY",
        compiler.getDiagnostics().get(0).getCode().name());
  }

  @Test
  public void testBadAliasParameters() {
    String sourceCode = "goog.scope(function() {\n" +
        "  goog.scope(function() {\n" +
        "    this.foo = 'bar'; // should result in GOOG_SCOPE_REFERENCES_THIS error\n" +
        "  });\n" +
        "  function foo() {} // should result in GOOG_SCOPE_HAS_BAD_PARAMETERS error\n" +
        "});";

    Node root = ParserUtil.parse(sourceCode);
    scopedAliases.process(null, root);
    assertEquals(2, compiler.getDiagnostics().size());
    assertEquals("JSC_GOOG_SCOPE_REFERENCES_THIS",
        compiler.getDiagnostics().get(0).getCode().name());
    assertEquals("JSC_GOOG_SCOPE_HAS_BAD_PARAMETERS",
        compiler.getDiagnostics().get(1).getCode().name());
  }

  @Test
  public void testAliasRedefinition() {
    String sourceCode = "goog.scope(function() {\n" +
        "  var x = goog.x; // should be renamed to goog.dom.x\n" +
        "  goog.scope(function() {\n" +
        "    var x = 'foo'; // should result in GOOG_SCOPE_ALIAS_REDEFINED error\n" +
        "  });\n" +
        "  assertEquals('div', x.createElement(goog.dom.TagName.DIV));\n" +
        "});";

    Node root = ParserUtil.parse(sourceCode);
    scopedAliases.process(null, root);
    assertEquals(1, compiler.getDiagnostics().size());
    assertEquals("JSC_GOOG_SCOPE_ALIAS_REDEFINED",
        compiler.getDiagnostics().get(0).getCode().name());
  }
}