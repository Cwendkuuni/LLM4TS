import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CompilerOptions.AliasTransformation.*;
import static com.google.javascript.jscomp.Scope.Var.*;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.HotSwapCompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.PreprocessorSymbolTable;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Traversal;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.NodeTraversal;
import com.google.javascript.rhino.SourcePosition;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import org.junit.Before;
import org.junit.Test;

/**
 * Unit tests for {@link ScopedAliases}.
 */
public class ScopedAliasesTest {
  private Compiler compiler;
  private PreprocessorSymbolTable preprocessorSymbolTable;

  @Before
  public void setup() {
    compiler = new Compiler();
    compiler.setOption(CompilerOptions.WARNINGS, false);
    preprocessorSymbolTable = new PreprocessorSymbolTable();
  }

  @Test
  public void testSimpleAlias() {
    String input = "goog.scope(function() {\n" +
        "  var dom = goog.dom; // alias for goog.dom\n" +
        "  var DIV = dom.TagName.DIV;\n" +
        "  dom.createElement(DIV);\n" +
        "});";

    Node root = compiler.parseTree(input, "test.js", null);

    HotSwapCompilerPass pass = new ScopedAliases(
        compiler, preprocessorSymbolTable, new NoOpTransformHandler());
    pass.process(null, root);

    List<Node> aliasDefinitions = pass.getAliasDefinitionsInOrder();
    assertEquals(1, aliasDefinitions.size());
    Node aliasDefinition = aliasDefinitions.get(0);
    assertEquals("goog.dom", aliasDefinition.getFirstChild().getString());

    // Validate the output tree.
    Node expectedOutput = new Node(Token.SCRIPT, null);
    expectedOutput.setSourcePosition(
        new SourcePosition<NoOpTransform>() {
          @Override
          public void setPositionInformation(int line, int charPosition,
              int renderedLine, int renderedCharPosition) {
            // NOP.
          }
        });
    expectedOutput.addChild(
        new Node(Token.CALL, null, null, "goog.dom.createElement", null));

    Node actualOutput = pass.getProcessedNode(root);
    assertEquals(expectedOutput, actualOutput);
  }

  @Test
  public void testNestedAliases() {
    String input = "goog.scope(function() {\n" +
        "  var dom = goog.dom; // alias for goog.dom\n" +
        "  var googDom = dom; // alias for goog.dom\n" +
        "  var DIV = googDom.TagName.DIV;\n" +
        "  dom.createElement(DIV);\n" +
        "});";

    Node root = compiler.parseTree(input, "test.js", null);

    HotSwapCompilerPass pass = new ScopedAliases(
        compiler, preprocessorSymbolTable, new NoOpTransformHandler());
    pass.process(null, root);

    List<Node> aliasDefinitions = pass.getAliasDefinitionsInOrder();
    assertEquals(2, aliasDefinitions.size());
    Node firstAliasDefinition = aliasDefinitions.get(0);
    assertEquals("goog.dom", firstAliasDefinition.getFirstChild().getString());
    Node secondAliasDefinition = aliasDefinitions.get(1);
    assertEquals("googDom", secondAliasDefinition.getFirstChild().getString());

    // Validate the output tree.
    Node expectedOutput = new Node(Token.SCRIPT, null);
    expectedOutput.setSourcePosition(
        new SourcePosition<NoOpTransform>() {
          @Override
          public void setPositionInformation(int line, int charPosition,
              int renderedLine, int renderedCharPosition) {
            // NOP.
          }
        });
    expectedOutput.addChild(
        new Node(Token.CALL, null, null, "goog.dom.createElement", null));

    Node actualOutput = pass.getProcessedNode(root);
    assertEquals(expectedOutput, actualOutput);
  }

  @Test
  public void testAliasWithTypeError() {
    String input = "goog.scope(function() {\n" +
        "  var dom = goog.dom;\n" +
        "  var DIV = dom.TagName.DIV;\n" +
        "  var DIV_TYPE = goog.dom.TagName.DIV;\n" +
        "});";

    Node root = compiler.parseTree(input, "test.js", null);

    HotSwapCompilerPass pass = new ScopedAliases(
        compiler, preprocessorSymbolTable, new NoOpTransformHandler());
    DiagnosticType error = null;

    try {
      pass.process(null, root);
    } catch (Compiler.CompilationError e) {
      error = e.getError();
    }

    assertNotNull(error);
    assertEquals("JSC_GOOG_SCOPE_NON_ALIAS_LOCAL", error.getType());
    assertEquals("DIV_TYPE", error.getMessageArguments()[0]);
  }

  @Test
  public void testAliasWithFunctionError() {
    String input = "goog.scope(function() {\n" +
        "  function foo() {}\n" +
        "  var DIV = goog.dom.TagName.DIV;\n" +
        "});";

    Node root = compiler.parseTree(input, "test.js", null);

    HotSwapCompilerPass pass = new ScopedAliases(
        compiler, preprocessorSymbolTable, new NoOpTransformHandler());
    DiagnosticType error = null;

    try {
      pass.process(null, root);
    } catch (Compiler.CompilationError e) {
      error = e.getError();
    }

    assertNotNull(error);
    assertEquals("JSC_GOOG_SCOPE_REFERENCES_THIS", error.getType());
    assertEquals("foo", error.getMessageArguments()[0]);
  }

  @Test
  public void testAliasWithReturnError() {
    String input = "goog.scope(function() {\n" +
        "  return 42;\n" +
        "  var DIV = goog.dom.TagName.DIV;\n" +
        "});";

    Node root = compiler.parseTree(input, "test.js", null);

    HotSwapCompilerPass pass = new ScopedAliases(
        compiler, preprocessorSymbolTable, new NoOpTransformHandler());
    DiagnosticType error = null;

    try {
      pass.process(null, root);
    } catch (Compiler.CompilationError e) {
      error = e.getError();
    }

    assertNotNull(error);
    assertEquals("JSC_GOOG_SCOPE_USES_RETURN", error.getType());
  }

  @Test
  public void testAliasWithThrowError() {
    String input = "goog.scope(function() {\n" +
        "  throw new Error(\"Test error\");\n" +
        "  var DIV = goog.dom.TagName.DIV;\n" +
        "});";

    Node root = compiler.parseTree(input, "test.js", null);

    HotSwapCompilerPass pass = new ScopedAliases(
        compiler, preprocessorSymbolTable, new NoOpTransformHandler());
    DiagnosticType error = null;

    try {
      pass.process(null, root);
    } catch (Compiler.CompilationError e) {
      error = e.getError();
    }

    assertNotNull(error);
    assertEquals("JSC_GOOG_SCOPE_USES_THROW", error.getType());
  }

  @Test
  public void testAliasWithRedefinitionError() {
    String input = "goog.scope(function() {\n" +
        "  var DIV = goog.dom.TagName.DIV;\n" +
        "  var DIV = 42;\n" +
        "});";

    Node root = compiler.parseTree(input, "test.js", null);

    HotSwapCompilerPass pass = new ScopedAliases(
        compiler, preprocessorSymbolTable, new NoOpTransformHandler());
    DiagnosticType error = null;

    try {
      pass.process(null, root);
    } catch (Compiler.CompilationError e) {
      error = e.getError();
    }

    assertNotNull(error);
    assertEquals("JSC_GOOG_SCOPE_ALIAS_REDEFINED", error.getType());
    assertEquals("DIV", error.getMessageArguments()[0]);
  }
}

class NoOpTransformHandler implements AliasTransformationHandler {
  @Override
  public AliasTransformation logAliasTransformation(String fileName,
      SourcePosition position) {
    return null;
  }

  @Override
  public void logAliasTransformation(String message, Object... args) {
    // NOP.
  }
}