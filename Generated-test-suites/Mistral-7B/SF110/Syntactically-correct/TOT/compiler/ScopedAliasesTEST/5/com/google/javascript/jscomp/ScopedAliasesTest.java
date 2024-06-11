import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertThrows;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.AliasTransformation;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.HotSwapCompilerPass;
import com.google.javascript.jscomp.PreprocessorSymbolTable;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Traversal;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.SourcePosition;

import org.junit.Before;
import org.junit.Test;

public class ScopedAliasesTest {
  private AbstractCompiler compiler;
  private PreprocessorSymbolTable preprocessorSymbolTable;
  private AliasTransformationHandler transformationHandler;

  @Before
  public void setup() {
    CompilerOptions options = new CompilerOptions();
    options.setAliasTransformation(AliasTransformation.STRICT);
    compiler = new Compiler();
    compiler.setOptions(options);
    preprocessorSymbolTable = new PreprocessorSymbolTable();
    transformationHandler = new MockAliasTransformationHandler();

    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
  }

  @Test
  public void testNoScopedAliases() {
    String input = "function foo() {}";
    Node rootNode = NodePrinter.parse(input);
    scopedAliases.process(null, rootNode);

    // No changes should be made to the input.
    String output = NodePrinter.print(rootNode);
    assertThat(output).isEqualTo(input);
  }

  @Test
  public void testSimpleScopedAlias() {
    String input = "goog.scope(function() {\n" +
        "  var x = goog.foo;\n" +
        "});";
    Node rootNode = NodePrinter.parse(input);
    scopedAliases.process(null, rootNode);

    String output = NodePrinter.print(rootNode);
    assertThat(output).isEqualTo(
        "goog.foo;\n");
  }

  @Test
  public void testScopedAliasWithQualifiedName() {
    String input = "goog.scope(function() {\n" +
        "  var goog = {\n" +
        "    dom: {\n" +
        "      createElement: function() {}\n" +
        "    }\n" +
        "  };\n" +
        "  var x = goog.dom.createElement;\n" +
        "});";
    Node rootNode = NodePrinter.parse(input);
    scopedAliases.process(null, rootNode);

    String output = NodePrinter.print(rootNode);
    assertThat(output).isEqualTo(
        "goog.dom.createElement;\n");
  }

  @Test
  public void testScopedAliasWithFunctionParameter() {
    String input = "goog.scope(function foo(bar) {\n" +
        "  var x = goog.bar;\n" +
        "});\n" +
        "function foo(bar) {\n" +
        "  goog.scope(function() {\n" +
        "    var x = this.bar;\n" +
        "  });\n" +
        "  return x;\n" +
        "}";
    Node rootNode = NodePrinter.parse(input);
    scopedAliases.process(null, rootNode);

    String output = NodePrinter.print(rootNode);
    assertThat(output).isEqualTo(
        "function foo(bar) {\n" +
        "  goog.bar;\n" +
        "  return this.bar;\n" +
        "}");
  }

  @Test
  public void testScopedAliasWithReturnStatement() {
    String input = "goog.scope(function() {\n" +
        "  return goog.foo;\n" +
        "});";
    Node rootNode = NodePrinter.parse(input);
    scopedAliases.process(null, rootNode);

    assertThrows(RuntimeException.class, () -> {
      String output = NodePrinter.print(rootNode);
      assertThat(output).isEqualTo("goog.foo");
    });
  }

  @Test
  public void testScopedAliasWithThrowStatement() {
    String input = "goog.scope(function() {\n" +
        "  throw new Error('foo');\n" +
        "});";
    Node rootNode = NodePrinter.parse(input);
    scopedAliases.process(null, rootNode);

    assertThrows(RuntimeException.class, () -> {
      String output = NodePrinter.print(rootNode);
      assertThat(output).isEqualTo("throw new Error('foo')");
    });
  }

  @Test
  public void testScopedAliasWithInvalidParameters() {
    String input = "goog.scope(1);";
    Node rootNode = NodePrinter.parse(input);
    scopedAliases.process(null, rootNode);

    assertThrows(RuntimeException.class, () -> {
      String output = NodePrinter.print(rootNode);
      assertThat(output).isEqualTo(input);
    });
  }

  @Test
  public void testScopedAliasWithMultipleDefinitions() {
    String input = "goog.scope(function() {\n" +
        "  var x = goog.foo;\n" +
        "  var x = goog.bar;\n" +
        "});";
    Node rootNode = NodePrinter.parse(input);
    scopedAliases.process(null, rootNode);

    assertThrows(RuntimeException.class, () -> {
      String output = NodePrinter.print(rootNode);
      assertThat(output).isEqualTo(input);
    });
  }

  @Test
  public void testScopedAliasWithInvalidCall() {
    String input = "goog.scope(1);";
    Node rootNode = NodePrinter.parse(input);
    scopedAliases.process(null, rootNode);

    assertThrows(RuntimeException.class, () -> {
      String output = NodePrinter.print(rootNode);
      assertThat(output).isEqualTo("1");
    });
  }

  @Test
  public void testScopedAliasWithInvalidCallAndDefinition() {
    String input = "goog.scope(1);\n" +
        "var x = goog.foo;\n";
    Node rootNode = NodePrinter.parse(input);
    scopedAliases.process(null, rootNode);

    assertThrows(RuntimeException.class, () -> {
      String output = NodePrinter.print(rootNode);
      assertThat(output).isEqualTo("1");
    });
  }

  @Test
  public void testScopedAliasWithInvalidCallInFunction() {
    String input = "function foo() {\n" +
        "  goog.scope(1);\n" +
        "  return x;\n" +
        "}";
    Node rootNode = NodePrinter.parse(input);
    scopedAliases.process(null, rootNode);

    assertThrows(RuntimeException.class, () -> {
      String output = NodePrinter.print(rootNode);
      assertThat(output).isEqualTo("1");
    });
  }

  @Test
  public void testScopedAliasWithInvalidCallInFunctionAndDefinition() {
    String input = "function foo() {\n" +
        "  goog.scope(1);\n" +
        "  var x = goog.foo;\n" +
        "  return x;\n" +
        "}";
    Node rootNode = NodePrinter.parse(input);
    scopedAliases.process(null, rootNode);

    assertThrows(RuntimeException.class, () -> {
      String output = NodePrinter.print(rootNode);
      assertThat(output).isEqualTo("1");
    });
  }

  @Test
  public void testScopedAliasWithInvalidCallAndDefinitionInFunction() {
    String input = "function foo() {\n" +
        "  goog.scope(1);\n" +
        "  var x = goog.foo;\n" +
        "  goog.scope(function() {\n" +
        "    var x = 1;\n" +
        "  });\n" +
        "  return x;\n" +
        "}";
    Node rootNode = NodePrinter.parse(input);
    scopedAliases.process(null, rootNode);

    assertThrows(RuntimeException.class, () -> {
      String output = NodePrinter.print(rootNode);
      assertThat(output).isEqualTo("1");
    });
  }

  @Test
  public void testScopedAliasWithMultipleDefinitionsInFunction() {
    String input = "function foo() {\n" +
        "  var x = goog.foo;\n" +
        "  var x = goog.bar;\n" +
        "  return x;\n" +
        "}";
    Node rootNode = NodePrinter.parse(input);
    scopedAliases.process(null, rootNode);

    assertThrows(RuntimeException.class, () -> {
      String output = NodePrinter.print(rootNode);
      assertThat(output).isEqualTo(input);
    });
  }
}

class MockAliasTransformationHandler implements AliasTransformationHandler {
  @Override
  public void logAliasTransformation(String file, SourcePosition position) {
    // Do nothing.
  }
}