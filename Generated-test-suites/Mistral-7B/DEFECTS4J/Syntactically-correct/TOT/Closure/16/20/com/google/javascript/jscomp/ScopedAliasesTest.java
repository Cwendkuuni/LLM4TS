import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CompilerOptions.AliasTransformation.*;
import static com.google.javascript.rhino.NodeType.*;
import static com.google.javascript.jscomp.Scope.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.HotSwapCompilerPass;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Traversal;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.SourcePosition;

/**
 * Tests for the ScopedAliases class.
 */
@RunWith(JUnit4.class)
public class ScopedAliasesTest {
  @Test
  public void testSimpleAlias() {
    AbstractCompiler compiler = new Compiler();
    PreprocessorSymbolTable preprocessorSymbolTable = null;
    AliasTransformationHandler transformationHandler = new AliasTransformationHandler();

    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    String sourceCode = "goog.scope(function() {" +
            "  var dom = goog.dom;" +
            "  var DIV = dom.TagName.DIV;" +
            "  goog.dom.createElement(DIV);" +
            "});";

    Node root = NodeUtil.parse(sourceCode, null, null);
    scopedAliases.process(null, root);

    Node result = root.getFirstChild();
    assertEquals(
        "goog.dom.createElement(goog.dom.TagName.DIV)",
        result.getFirstChild().getFirstChild().toString());
  }

  @Test
  public void testMultipleAliases() {
    AbstractCompiler compiler = new Compiler();
    PreprocessorSymbolTable preprocessorSymbolTable = null;
    AliasTransformationHandler transformationHandler = new AliasTransformationHandler();

    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    String sourceCode = "goog.scope(function() {" +
            "  var dom = goog.dom;" +
            "  var DIV = dom.TagName.DIV;" +
            "  var doc = document;" +
            "  goog.dom.createElement(DIV);" +
            "  goog.dom.createElement(doc.DOCTYPE);" +
            "});";

    Node root = NodeUtil.parse(sourceCode, null, null);
    scopedAliases.process(null, root);

    Node result = root.getFirstChild();
    assertEquals(
        "goog.dom.createElement(goog.dom.TagName.DIV)",
        result.getFirstChild().getFirstChild().toString());
    assertEquals(
        "goog.dom.createElement(document.DOCTYPE)",
        result.getFirstChild().getLastChild().getFirstChild().toString());
  }

  @Test(expected = AssertionError.class)
  public void testMoreThanOneParameter() {
    AbstractCompiler compiler = new Compiler();
    PreprocessorSymbolTable preprocessorSymbolTable = null;
    AliasTransformationHandler transformationHandler = new AliasTransformationHandler();

    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    String sourceCode = "goog.scope(function(x, y) {" +
            "  x + y;" +
            "});";

    Node root = NodeUtil.parse(sourceCode, null, null);
    scopedAliases.process(null, root);

    fail("Expected error: goog.scope must take only a single parameter.");
  }

  @Test(expected = AssertionError.class)
  public void testThisInsideScopeBlock() {
    AbstractCompiler compiler = new Compiler();
    PreprocessorSymbolTable preprocessorSymbolTable = null;
    AliasTransformationHandler transformationHandler = new AliasTransformationHandler();

    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    String sourceCode = "goog.scope(function() {" +
            "  this;" +
            "});";

    Node root = NodeUtil.parse(sourceCode, null, null);
    scopedAliases.process(null, root);

    fail("Expected error: The body of a goog.scope function cannot reference 'this'.");
  }

  @Test(expected = AssertionError.class)
  public void testReturnInsideScopeBlock() {
    AbstractCompiler compiler = new Compiler();
    PreprocessorSymbolTable preprocessorSymbolTable = null;
    AliasTransformationHandler transformationHandler = new AliasTransformationHandler();

    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    String sourceCode = "goog.scope(function() {" +
            "  return 1;" +
            "});";

    Node root = NodeUtil.parse(sourceCode, null, null);
    scopedAliases.process(null, root);

    fail("Expected error: The body of a goog.scope function cannot use 'return'.");
  }

  @Test(expected = AssertionError.class)
  public void testThrowInsideScopeBlock() {
    AbstractCompiler compiler = new Compiler();
    PreprocessorSymbolTable preprocessorSymbolTable = null;
    AliasTransformationHandler transformationHandler = new AliasTransformationHandler();

    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    String sourceCode = "goog.scope(function() {" +
            "  throw new Error();" +
            "});";

    Node root = NodeUtil.parse(sourceCode, null, null);
    scopedAliases.process(null, root);

    fail("Expected error: The body of a goog.scope function cannot use 'throw'.");
  }

  @Test
  public void testAliasRedefinition() {
    AbstractCompiler compiler = new Compiler();
    PreprocessorSymbolTable preprocessorSymbolTable = null;
    AliasTransformationHandler transformationHandler = new AliasTransformationHandler();

    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    String sourceCode = "goog.scope(function() {" +
            "  var x = goog.x;" +
            "  var x = 1;" +
            "});";

    Node root = NodeUtil.parse(sourceCode, null, null);
    scopedAliases.process(null, root);

    fail("Expected error: The alias 'x' is assigned a value more than once.");
  }

  @Test
  public void testAliasReassignment() {
    AbstractCompiler compiler = new Compiler();
    PreprocessorSymbolTable preprocessorSymbolTable = null;
    AliasTransformationHandler transformationHandler = new AliasTransformationHandler();

    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    String sourceCode = "goog.scope(function() {" +
            "  var x = goog.x;" +
            "  x = 1;" +
            "});";

    Node root = NodeUtil.parse(sourceCode, null, null);
    scopedAliases.process(null, root);

    // This should not fail, as we allow reassignment of alias variables.
  }

  @Test
  public void testNoAlias() {
    AbstractCompiler compiler = new Compiler();
    PreprocessorSymbolTable preprocessorSymbolTable = null;
    AliasTransformationHandler transformationHandler = new AliasTransformationHandler();

    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    String sourceCode = "goog.scope(function() {" +
            "  var x = 1;" +
            "});";

    Node root = NodeUtil.parse(sourceCode, null, null);
    scopedAliases.process(null, root);

    // This should not fail, as there are no aliases to process.
  }

  @Test
  public void testAliasDeclaredInsideScope() {
    AbstractCompiler compiler = new Compiler();
    PreprocessorSymbolTable preprocessorSymbolTable = null;
    AliasTransformationHandler transformationHandler = new AliasTransformationHandler();

    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    String sourceCode = "goog.scope(function() {" +
            "  var x = goog.x;" +
            "  var goog = {x: 1};" +
            "});";

    Node root = NodeUtil.parse(sourceCode, null, null);
    scopedAliases.process(null, root);

    fail("Expected error: The local variable x is in a goog.scope and is not an alias.");
  }

  @Test
  public void testAliasInMemberExpression() {
    AbstractCompiler compiler = new Compiler();
    PreprocessorSymbolTable preprocessorSymbolTable = null;
    AliasTransformationHandler transformationHandler = new AliasTransformationHandler();

    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    String sourceCode = "goog.scope(function() {" +
            "  var x = goog.x;" +
            "  goog.dom.createElement(goog.dom.TagName.DIV);" +
            "});";

    Node root = NodeUtil.parse(sourceCode, null, null);
    scopedAliases.process(null, root);

    Node result = root.getFirstChild();
    assertEquals(
        "goog.dom.createElement(goog.dom.TagName.DIV)",
        result.getFirstChild().getFirstChild().toString());
  }

  @Test
  public void testAliasInTypeExpression() {
    AbstractCompiler compiler = new Compiler();
    PreprocessorSymbolTable preprocessorSymbolTable = null;
    AliasTransformationHandler transformationHandler = new AliasTransformationHandler();

    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    String sourceCode = "goog.scope(function() {" +
            "  var x = goog.x;" +
            "  function foo(x) {" +
            "    return {x: x};" +
            "  }" +
            "});";

    Node root = NodeUtil.parse(sourceCode, null, null);
    scopedAliases.process(null, root);

    Node typeNode = NodeUtil.createTypeNode(root, "Object", null, null);
    Node typeNodeWithAlias = NodeUtil.createTypeNode(
        root, "Object", null, Lists.newArrayList(new NodeName("x")), null);
    scopedAliases.process(null, root);

    assertEquals("{x: goog.x}", typeNodeWithAlias.toString());
  }

  @Test
  public void testAliasInTryCatchStatement() {
    AbstractCompiler compiler = new Compiler();
    PreprocessorSymbolTable preprocessorSymbolTable = null;
    AliasTransformationHandler transformationHandler = new AliasTransformationHandler();

    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    String sourceCode = "goog.scope(function() {" +
            "  try {" +
            "    var x = goog.x;" +
            "  } catch (e) {" +
            "    e.message = \"Error: \" + x.toString() + \";\"" +
            "  }" +
            "});";

    Node root = NodeUtil.parse(sourceCode, null, null);
    scopedAliases.process(null, root);

    Node tryStatement = root.getFirstChild().getLastChild();
    Node catchClause = tryStatement.getLastChild();

    Node errorObject = catchClause.getLastChild().getLastChild();
    Node messageProperty = errorObject.getFirstChild();
    Node aliasNode = messageProperty.getLastChild().getLastChild();

    assertEquals("goog.x", aliasNode.getQualifiedName());
  }
}