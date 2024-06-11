import static org.junit.Assert.*;
import static com.google.javascript.jscomp.ScopedAliases.*;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.OptLogger;
import com.google.javascript.jscomp.PreprocessorSymbolTable;
import com.google.javascript.jscomp.TestCompiler;
import com.google.javascript.rhino.Node;

import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class ScopedAliasesTest {
  private Compiler compiler;
  private PreprocessorSymbolTable preprocessorSymbolTable;
  private OptLogger logger;

  @Before
  public void setUp() {
    compiler = new TestCompiler();
    preprocessorSymbolTable = null;
    logger = new OptLogger.TestLogger();
  }

  @Test
  public void testSimpleAlias() throws IOException {
    String input = "goog.scope(function() {" +
        "  var dom = goog.dom;" +
        "  var div = dom.createElement(dom.TagName.DIV);" +
        "});";

    Node root = NodePrinter.parseJs(input);
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, null);
    scopedAliases.process(null, root);

    Node expectedOutput = NodePrinter.printJs(compiler.getRoot());
    assertEquals(expectedOutput, NodePrinter.printJs(root));
  }

  @Test
  public void testAliasRedefinition() throws IOException {
    String input = "goog.scope(function() {" +
        "  var x = goog.x;" +
        "  var x = 1;" +
        "});";

    Node root = NodePrinter.parseJs(input);
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, null);
    try {
      scopedAliases.process(null, root);
      fail("Expected GOOG_SCOPE_ALIAS_REDEFINED error");
    } catch (AssertionError e) {
      // Expected
    }
  }

  @Test
  public void testInvalidScopeCall() throws IOException {
    String input = "goog.scope(function() {" +
        "  function goog() {}" +
        "});";

    Node root = NodePrinter.parseJs(input);
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, null);
    try {
      scopedAliases.process(null, root);
      fail("Expected GOOG_SCOPE_USED_IMPROPERLY error");
    } catch (AssertionError e) {
      // Expected
    }
  }

  @Test
  public void testScopeCallWithArguments() throws IOException {
    String input = "goog.scope(function(a, b) {" +
        "  var c = goog.dom.createElement(goog.dom.TagName.DIV);" +
        "});";

    Node root = NodePrinter.parseJs(input);
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, null);
    scopedAliases.process(null, root);

    Node expectedOutput = NodePrinter.printJs(compiler.getRoot());
    assertEquals(expectedOutput, NodePrinter.printJs(root));
  }

  @Test
  public void testScopeCallWithReturnStatement() throws IOException {
    String input = "goog.scope(function() {" +
        "  return 1;" +
        "});";

    Node root = NodePrinter.parseJs(input);
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, null);
    try {
      scopedAliases.process(null, root);
      fail("Expected GOOG_SCOPE_USES_RETURN error");
    } catch (AssertionError e) {
      // Expected
    }
  }

  @Test
  public void testScopeCallWithThrowStatement() throws IOException {
    String input = "goog.scope(function() {" +
        "  throw new Error('test');" +
        "});";

    Node root = NodePrinter.parseJs(input);
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, null);
    try {
      scopedAliases.process(null, root);
      fail("Expected GOOG_SCOPE_USES_THROW error");
    } catch (AssertionError e) {
      // Expected
    }
  }

  @Test
  public void testAliasDefinitionWithThisKeyword() throws IOException {
    String input = "goog.scope(function() {" +
        "  this.x = 1;" +
        "});";

    Node root = NodePrinter.parseJs(input);
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, null);
    try {
      scopedAliases.process(null, root);
      fail("Expected GOOG_SCOPE_REFERENCES_THIS error");
    } catch (AssertionError e) {
      // Expected
    }
  }
}