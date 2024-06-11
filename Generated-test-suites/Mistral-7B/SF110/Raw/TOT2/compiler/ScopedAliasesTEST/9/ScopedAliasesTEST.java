import static org.junit.Assert.*;
import static com.google.javascript.jscomp.ScopedAliases.*;

import com.google.javascript.jscomp.CompilerOptions.AliasTransformation;
import com.google.javascript.rhino.Node;

import org.junit.Before;
import org.junit.Test;

public class ScopedAliasesTest {

  private AbstractCompiler compiler;
  private PreprocessorSymbolTable preprocessorSymbolTable;
  private AliasTransformationHandler transformationHandler;
  private ScopedAliases scopedAliases;

  @Before
  public void setup() {
    compiler = new CompilerOptions(new CompilerOptions.Defaults());
    preprocessorSymbolTable = new PreprocessorSymbolTable();
    transformationHandler = new AliasTransformationHandler() {};
    scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
  }

  @Test
  public void testSimpleAlias() {
    String sourceCode = "goog.scope(function() {" +
        "var dom = goog.dom;" +
        "var DIV = dom.TagName.DIV;" +
        "dom.createElement(DIV);" +
        "});";

    Node root = ParserUtil.parse(sourceCode);
    scopedAliases.process(null, root);

    Node expected = ParserUtil.parse("goog.dom.createElement(goog.dom.TagName.DIV);");
    Node actual = compiler.getOutput(root);
    assertEquals(expected.toSource(), actual.toSource());
  }

  @Test
  public void testMultipleAliases() {
    String sourceCode = "goog.scope(function() {" +
        "var dom = goog.dom;" +
        "var DIV = dom.TagName.DIV;" +
        "var foo = goog.foo;" +
        "var BAR = goog.BAR;" +
        "dom.createElement(DIV);" +
        "foo();" +
        "BAR();" +
        "});";

    Node root = ParserUtil.parse(sourceCode);
    scopedAliases.process(null, root);

    Node expected = ParserUtil.parse("goog.dom.createElement(goog.dom.TagName.DIV);" +
        "goog.foo();" +
        "goog.BAR();");
    Node actual = compiler.getOutput(root);
    assertEquals(expected.toSource(), actual.toSource());
  }

  @Test(expected = AssertionError.class)
  public void testImproperUsage() {
    String sourceCode = "goog.scope(function() {" +
        "var dom = goog.dom;" +
        "goog.scope(function() {" +
        "  var DIV = dom.TagName.DIV;" +
        "  dom.createElement(DIV);" +
        "});" +
        "});";

    Node root = ParserUtil.parse(sourceCode);
    scopedAliases.process(null, root);
  }

  @Test(expected = AssertionError.class)
  public void testMultipleParameters() {
    String sourceCode = "goog.scope(function(x) {" +
        "var dom = goog.dom;" +
        "var DIV = dom.TagName.DIV;" +
        "dom.createElement(DIV);" +
        "x();" +
        "});";

    Node root = ParserUtil.parse(sourceCode);
    scopedAliases.process(null, root);
  }

  @Test(expected = AssertionError.class)
  public void testThisReference() {
    String sourceCode = "goog.scope(function() {" +
        "this.x = 0;" +
        "goog.scope(function() {" +
        "  this.x = 1;" +
        "});" +
        "});";

    Node root = ParserUtil.parse(sourceCode);
    scopedAliases.process(null, root);
  }

  @Test(expected = AssertionError.class)
  public void testReturnStatement() {
    String sourceCode = "goog.scope(function() {" +
        "return; " +
        "});";

    Node root = ParserUtil.parse(sourceCode);
    scopedAliases.process(null, root);
  }

  @Test(expected = AssertionError.class)
  public void testThrowStatement() {
    String sourceCode = "goog.scope(function() {" +
        "throw new Error(\"Test\");" +
        "});";

    Node root = ParserUtil.parse(sourceCode);
    scopedAliases.process(null, root);
  }

  @Test
  public void testAliasRedefined() {
    String sourceCode = "goog.scope(function() {" +
        "var DIV = goog.dom.TagName.DIV;" +
        "goog.scope(function() {" +
        "  var DIV = 123;" +
        "});" +
        "dom.createElement(DIV);" +
        "});";

    Node root = ParserUtil.parse(sourceCode);
    try {
      scopedAliases.process(null, root);
      fail("Expected GOOG_SCOPE_ALIAS_REDEFINED error.");
    } catch (AssertionError e) {
      String expectedMessage = "Expected only one alias for name: DIV";
      String actualMessage = e.getMessage();
      assertTrue(expectedMessage.contains(actualMessage));
    }
  }
}