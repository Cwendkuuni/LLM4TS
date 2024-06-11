import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CompilerOptions.AliasTransformation.*;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.HotSwapCompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Traversal;
import com.google.javascript.rhino.SourcePosition;

import org.junit.Test;

import java.util.List;
import java.util.Map;

public class ScopedAliasesTest {
  @Test
  public void testScopedAliases() {
    CompilerOptions options = new CompilerOptions();
    options.setAliasTransformation(ALWAYS);

    Compiler compiler = new Compiler();
    compiler.setOptions(options);

    String jsCode = "goog.scope(function() {" +
        "  var dom = goog.dom;" +
        "  var DIV = dom.TagName.DIV;" +
        "  var elem = dom.createElement(DIV);" +
        "  document.body.appendChild(elem);" +
        "});";

    Node root = ParserUtil.parseJsCode(jsCode);
    HotSwapCompilerPass scopedAliases = new ScopedAliases(compiler, null, null);
    scopedAliases.process(null, root);

    Node expectedJsCode = "goog.dom.createElement(goog.dom.TagName.DIV);";
    Node expectedRoot = ParserUtil.parseJsCode(expectedJsCode);

    assertEquals(expectedRoot.toString(), root.toString());
  }

  @Test(expected = AssertionError.class)
  public void testScopedAliasesWithMultipleStatements() {
    CompilerOptions options = new CompilerOptions();
    options.setAliasTransformation(ALWAYS);

    Compiler compiler = new Compiler();
    compiler.setOptions(options);

    String jsCode = "goog.scope(function() {" +
        "  var dom = goog.dom;" +
        "  var DIV = dom.TagName.DIV;" +
        "  var elem1 = dom.createElement(DIV);" +
        "  var elem2 = dom.createElement(DIV);" +
        "  document.body.appendChild(elem1);" +
        "  document.body.appendChild(elem2);" +
        "});";

    Node root = ParserUtil.parseJsCode(jsCode);
    HotSwapCompilerPass scopedAliases = new ScopedAliases(compiler, null, null);
    scopedAliases.process(null, root);

    throw new AssertionError("Expected error: JSC_GOOG_SCOPE_USED_IMPROPERLY");
  }

  @Test(expected = AssertionError.class)
  public void testScopedAliasesWithImproperFunctionDefinition() {
    CompilerOptions options = new CompilerOptions();
    options.setAliasTransformation(ALWAYS);

    Compiler compiler = new Compiler();
    compiler.setOptions(options);

    String jsCode = "goog.scope(function() {" +
        "  function foo() {}" +
        "  var elem = document.createElement('DIV');" +
        "});";

    Node root = ParserUtil.parseJsCode(jsCode);
    HotSwapCompilerPass scopedAliases = new ScopedAliases(compiler, null, null);
    scopedAliases.process(null, root);

    throw new AssertionError("Expected error: JSC_GOOG_SCOPE_HAS_BAD_PARAMETERS");
  }

  @Test
  public void testScopedAliasesWithParameterlessFunction() {
    CompilerOptions options = new CompilerOptions();
    options.setAliasTransformation(ALWAYS);

    Compiler compiler = new Compiler();
    compiler.setOptions(options);

    String jsCode = "goog.scope(function() {" +
        "  function foo() {}" +
        "  var elem = document.createElement('DIV');" +
        "});";

    Node root = ParserUtil.parseJsCode(jsCode);
    HotSwapCompilerPass scopedAliases = new ScopedAliases(compiler, null, null);
    scopedAliases.process(null, root);

    Node expectedRoot = ParserUtil.parseJsCode(
        "function foo() {}\n" +
        "goog.dom.createElement('DIV');");

    assertEquals(expectedRoot.toString(), root.toString());
  }

  @Test
  public void testScopedAliasesWithThisKeyword() {
    CompilerOptions options = new CompilerOptions();
    options.setAliasTransformation(ALWAYS);

    Compiler compiler = new Compiler();
    compiler.setOptions(options);

    String jsCode = "goog.scope(function() {" +
        "  this.foo = 'bar';" +
        "  var elem = document.createElement('DIV');" +
        "});";

    Node root = ParserUtil.parseJsCode(jsCode);
    HotSwapCompilerPass scopedAliases = new ScopedAliases(compiler, null, null);
    scopedAliases.process(null, root);

    throw new AssertionError("Expected error: JSC_GOOG_SCOPE_REFERENCES_THIS");
  }

  @Test
  public void testScopedAliasesWithReturnStatement() {
    CompilerOptions options = new CompilerOptions();
    options.setAliasTransformation(ALWAYS);

    Compiler compiler = new Compiler();
    compiler.setOptions(options);

    String jsCode = "goog.scope(function() {" +
        "  return 'hello';" +
        "  var elem = document.createElement('DIV');" +
        "});";

    Node root = ParserUtil.parseJsCode(jsCode);
    HotSwapCompilerPass scopedAliases = new ScopedAliases(compiler, null, null);
    scopedAliases.process(null, root);

    throw new AssertionError("Expected error: JSC_GOOG_SCOPE_USES_RETURN");
  }

  @Test
  public void testScopedAliasesWithThrowStatement() {
    CompilerOptions options = new CompilerOptions();
    options.setAliasTransformation(ALWAYS);

    Compiler compiler = new Compiler();
    compiler.setOptions(options);

    String jsCode = "goog.scope(function() {" +
        "  throw new Error('error');" +
        "  var elem = document.createElement('DIV');" +
        "});";

    Node root = ParserUtil.parseJsCode(jsCode);
    HotSwapCompilerPass scopedAliases = new ScopedAliases(compiler, null, null);
    scopedAliases.process(null, root);

    throw new AssertionError("Expected error: JSC_GOOG_SCOPE_USES_THROW");
  }

  @Test
  public void testScopedAliasesWithRedefinedAlias() {
    CompilerOptions options = new CompilerOptions();
    options.setAliasTransformation(ALWAYS);

    Compiler compiler = new Compiler();
    compiler.setOptions(options);

    String jsCode = "goog.scope(function() {" +
        "  var goog = {};" +
        "  goog.dom = {};" +
        "  var DIV = goog.dom.TagName.DIV;" +
        "  var elem = document.createElement(DIV);" +
        "  document.body.appendChild(elem);" +
        "  goog.dom = {};" + // Redefining the alias
        "  var elem2 = document.createElement(DIV);" +
        "  document.body.appendChild(elem2);" +
        "});";

    Node root = ParserUtil.parseJsCode(jsCode);
    HotSwapCompilerPass scopedAliases = new ScopedAliases(compiler, null, null);
    scopedAliases.process(null, root);

    throw new AssertionError("Expected error: JSC_GOOG_SCOPE_ALIAS_REDEFINED");
  }

  @Test
  public void testScopedAliasesWithNonAliasLocalVariable() {
    CompilerOptions options = new CompilerOptions();
    options.setAliasTransformation(ALWAYS);

    Compiler compiler = new Compiler();
    compiler.setOptions(options);

    String jsCode = "goog.scope(function() {" +
        "  var elem = document.createElement('DIV');" +
        "});";

    Node root = ParserUtil.parseJsCode(jsCode);
    HotSwapCompilerPass scopedAliases = new ScopedAliases(compiler, null, null);
    scopedAliases.process(null, root);

    throw new AssertionError("Expected error: JSC_GOOG_SCOPE_NON_ALIAS_LOCAL");
  }
}