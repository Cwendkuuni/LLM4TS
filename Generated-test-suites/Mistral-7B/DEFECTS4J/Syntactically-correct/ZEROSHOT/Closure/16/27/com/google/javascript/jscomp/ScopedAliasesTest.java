import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CompilerOptions.AliasTransformation.*;

import java.util.List;

import org.junit.Test;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.HotSwapCompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Traversal;
import com.google.javascript.rhino.SourcePosition;

public class ScopedAliasesTest {

  @Test
  public void testScopedAliases() {
    CompilerOptions options = new CompilerOptions();
    options.setAliasTransformation(REPLACE);
    Compiler compiler = new Compiler();
    compiler.setOptions(options);

    String sourceCode = "/** @suppress {checkTypes} */\n" +
        "goog.scope(function() {\n" +
        "  var dom = goog.dom;\n" +
        "  var DIV = dom.TagName.DIV;\n" +
        "  dom.createElement(DIV);\n" +
        "});";

    Node root = parserForTest(sourceCode);

    HotSwapCompilerPass scopedAliases = new ScopedAliases(compiler, null, null);
    scopedAliases.process(null, root);

    Node expectedRoot = parserForTest("" +
        "goog.dom.createElement(goog.dom.TagName.DIV);");

    assertEquals(expectedRoot.toSource(), root.toSource());
  }

  @Test
  public void testScopedAliasesWithAliasDefinition() {
    CompilerOptions options = new CompilerOptions();
    options.setAliasTransformation(REPLACE);
    Compiler compiler = new Compiler();
    compiler.setOptions(options);

    String sourceCode = "/** @suppress {checkTypes} */\n" +
        "goog.scope(function() {\n" +
        "  var dom = goog.dom;\n" +
        "  var DIV = dom.TagName.DIV;\n" +
        "  goog.dom = {\n" +
        "    TagName: {\n" +
        "      DIV: DIV\n" +
        "    }\n" +
        "  };\n" +
        "  dom.createElement(DIV);\n" +
        "});";

    Node root = parserForTest(sourceCode);

    HotSwapCompilerPass scopedAliases = new ScopedAliases(compiler, null, null);
    scopedAliases.process(null, root);

    Node expectedRoot = parserForTest("" +
        "goog.dom.createElement(goog.dom.TagName.DIV);");

    assertEquals(expectedRoot.toSource(), root.toSource());
  }

  @Test
  public void testScopedAliasesWithMultipleAliases() {
    CompilerOptions options = new CompilerOptions();
    options.setAliasTransformation(REPLACE);
    Compiler compiler = new Compiler();
    compiler.setOptions(options);

    String sourceCode = "/** @suppress {checkTypes} */\n" +
        "goog.scope(function() {\n" +
        "  var dom = goog.dom;\n" +
        "  var DIV = dom.TagName.DIV;\n" +
        "  var BAR = goog.BAR;\n" +
        "  goog.dom = {\n" +
        "    TagName: {\n" +
        "      DIV: DIV\n" +
        "    }\n" +
        "  };\n" +
        "  goog.BAR = 42;\n" +
        "  dom.createElement(DIV);\n" +
        "});";

    Node root = parserForTest(sourceCode);

    HotSwapCompilerPass scopedAliases = new ScopedAliases(compiler, null, null);
    scopedAliases.process(null, root);

    Node expectedRoot = parserForTest("" +
        "goog.dom.createElement(goog.dom.TagName.DIV);\n" +
        "goog.BAR = 42;");

    assertEquals(expectedRoot.toSource(), root.toSource());
  }

  @Test
  public void testScopedAliasesWithInvalidFunction() {
    CompilerOptions options = new CompilerOptions();
    options.setAliasTransformation(REPLACE);
    Compiler compiler = new Compiler();
    compiler.setOptions(options);

    String sourceCode = "/** @suppress {checkTypes} */\n" +
        "goog.scope(function googScopeFunction() {\n" +
        "  var dom = goog.dom;\n" +
        "  var DIV = dom.TagName.DIV;\n" +
        "  dom.createElement(DIV);\n" +
        "});";

    Node root = parserForTest(sourceCode);

    HotSwapCompilerPass scopedAliases = new ScopedAliases(compiler, null, null);
    scopedAliases.process(null, root);

    List<Node> errors = compiler.getErrors();
    assertEquals(1, errors.size());

    Node errorNode = errors.get(0);
    assertEquals("googScopeFunction is not defined", errorNode.getString());
  }

  @Test
  public void testScopedAliasesWithThisKeyword() {
    CompilerOptions options = new CompilerOptions();
    options.setAliasTransformation(REPLACE);
    Compiler compiler = new Compiler();
    compiler.setOptions(options);

    String sourceCode = "/** @suppress {checkTypes} */\n" +
        "goog.scope(function() {\n" +
        "  var self = this;\n" +
        "  this.foo = function() {\n" +
        "    self.bar = 'baz';\n" +
        "  };\n" +
        "  this.foo();\n" +
        "});";

    Node root = parserForTest(sourceCode);

    HotSwapCompilerPass scopedAliases = new ScopedAliases(compiler, null, null);
    scopedAliases.process(null, root);

    List<Node> errors = compiler.getErrors();
    assertEquals(1, errors.size());

    Node errorNode = errors.get(0);
    assertEquals("goog.scope functions cannot reference this", errorNode.getString());
  }

  @Test
  public void testScopedAliasesWithReturnStatement() {
    CompilerOptions options = new CompilerOptions();
    options.setAliasTransformation(REPLACE);
    Compiler compiler = new Compiler();
    compiler.setOptions(options);

    String sourceCode = "/** @suppress {checkTypes} */\n" +
        "goog.scope(function() {\n" +
        "  return {};\n" +
        "});";

    Node root = parserForTest(sourceCode);

    HotSwapCompilerPass scopedAliases = new ScopedAliases(compiler, null, null);
    scopedAliases.process(null, root);

    List<Node> errors = compiler.getErrors();
    assertEquals(1, errors.size());

    Node errorNode = errors.get(0);
    assertEquals("goog.scope functions cannot use return", errorNode.getString());
  }

  @Test
  public void testScopedAliasesWithThrowStatement() {
    CompilerOptions options = new CompilerOptions();
    options.setAliasTransformation(REPLACE);
    Compiler compiler = new Compiler();
    compiler.setOptions(options);

    String sourceCode = "/** @suppress {checkTypes} */\n" +
        "goog.scope(function() {\n" +
        "  throw new Error('Error occurred');\n" +
        "});";

    Node root = parserForTest(sourceCode);

    HotSwapCompilerPass scopedAliases = new ScopedAliases(compiler, null, null);
    scopedAliases.process(null, root);

    List<Node> errors = compiler.getErrors();
    assertEquals(1, errors.size());

    Node errorNode = errors.get(0);
    assertEquals("goog.scope functions cannot use throw", errorNode.getString());
  }

  @Test
  public void testScopedAliasesWithInvalidAliasName() {
    CompilerOptions options = new CompilerOptions();
    options.setAliasTransformation(REPLACE);
    Compiler compiler = new Compiler();
    compiler.setOptions(options);

    String sourceCode = "/** @suppress {checkTypes} */\n" +
        "goog.scope(function() {\n" +
        "  var goog = {\n" +
        "    SCOPING_METHOD_NAME: 'goog.scope'\n" +
        "  };\n" +
        "  goog.SCOPING_METHOD_NAME = function() {\n" +
        "  };\n" +
        "});";

    Node root = parserForTest(sourceCode);

    HotSwapCompilerPass scopedAliases = new ScopedAliases(compiler, null, null);
    scopedAliases.process(null, root);

    List<Node> errors = compiler.getErrors();
    assertEquals(1, errors.size());

    Node errorNode = errors.get(0);
    assertEquals("goog.SCOPING_METHOD_NAME is already defined", errorNode.getString());
  }

  @Test
  public void testScopedAliasesWithInvalidAliasUsage() {
    CompilerOptions options = new CompilerOptions();
    options.setAliasTransformation(REPLACE);
    Compiler compiler = new Compiler();
    compiler.setOptions(options);

    String sourceCode = "/** @suppress {checkTypes} */\n" +
        "goog.scope(function() {\n" +
        "  var aliased = goog.aliased;\n" +
        "});";

    Node root = parserForTest(sourceCode);

    HotSwapCompilerPass scopedAliases = new ScopedAliases(compiler, null, null);
    scopedAliases.process(null, root);

    List<Node> errors = compiler.getErrors();
    assertEquals(1, errors.size());

    Node errorNode = errors.get(0);
    assertEquals("goog.aliased is not a valid alias name", errorNode.getString());
  }

  @Test
  public void testScopedAliasesWithAliasRedefined() {
    CompilerOptions options = new CompilerOptions();
    options.setAliasTransformation(REPLACE);
    Compiler compiler = new Compiler();
    compiler.setOptions(options);

    String sourceCode = "/** @suppress {checkTypes} */\n" +
        "goog.scope(function() {\n" +
        "  var aliased = goog.aliased = 1;\n" +
        "  aliased = 2;\n" +
        "});";

    Node root = parserForTest(sourceCode);

    HotSwapCompilerPass scopedAliases = new ScopedAliases(compiler, null, null);
    scopedAliases.process(null, root);

    List<Node> errors = compiler.getErrors();
    assertEquals(1, errors.size());

    Node errorNode = errors.get(0);
    assertEquals("The alias aliased is assigned a value more than once", errorNode.getString());
  }

  private Node parserForTest(String sourceCode) {
    SourcePosition pos = new SourcePosition();
    Node root = Parser.parseCompilationUnit(sourceCode, pos, null);
    return root;
  }
}