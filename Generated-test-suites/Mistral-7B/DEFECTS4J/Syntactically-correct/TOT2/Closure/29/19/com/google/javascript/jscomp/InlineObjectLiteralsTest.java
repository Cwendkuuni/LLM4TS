import static org.junit.Assert.*;
import static com.google.javascript.jscomp.ReferenceCollectingCallback.*;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerPassManager;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Context;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.rhino.Node;

import org.junit.Before;
import org.junit.Test;

public class InlineObjectLiteralsTest {

  private CompilerOptions options;
  private SourceFile sourceFile;
  private AbstractCompiler compiler;
  private CompilerPassManager passManager;

  @Before
  public void setup() {
    options = new CompilerOptions(null);
    sourceFile = new SourceFile("test.js", "test code");
    compiler = new Compiler(options, null, null, null);
    passManager = new CompilerPassManager(compiler, null, null, null, null);
  }

  @Test
  public void testInlineObjectLiteral() {
    // Basic test case for simple object literal inline
    String input = "var obj = {a: 1, b: 2};";

    Node root = NodePrinter.parseJavaScript(input, null);
    passManager.addPass(new InlineObjectLiterals(compiler, new SimpleNameIdSupplier()));
    passManager.runPasses(root);

    Node expectedOutput = NodePrinter.parseJavaScript(
        "var a, b, JSCompiler_object_inline_a_0, JSCompiler_object_inline_a_1, JSCompiler_object_inline_b_0, JSCompiler_object_inline_b_1;\n" +
        "a = 1;\n" +
        "b = 2;\n" +
        "JSCompiler_object_inline_a_0 = a;\n" +
        "JSCompiler_object_inline_a_1 = {a: JSCompiler_object_inline_a_0};\n" +
        "JSCompiler_object_inline_b_0 = b;\n" +
        "JSCompiler_object_inline_b_1 = {b: JSCompiler_object_inline_b_0};\n" +
        "obj = JSCompiler_object_inline_a_1, JSCompiler_object_inline_b_1;", null);

    checkEqualTrees(expectedOutput, root);
  }

  @Test
  public void testInlineObjectLiteralWithFunction() {
    // Test case for object literal inline with a function
    String input = "var obj = {a: 1, b: {fn: function() {}}};";

    Node root = NodePrinter.parseJavaScript(input, null);
    passManager.addPass(new InlineObjectLiterals(compiler, new SimpleNameIdSupplier()));
    passManager.runPasses(root);

    Node expectedOutput = NodePrinter.parseJavaScript(
        "var a, b, fn, JSCompiler_object_inline_a_0, JSCompiler_object_inline_a_1, JSCompiler_object_inline_b_0, JSCompiler_object_inline_b_1;\n" +
        "a = 1;\n" +
        "fn = function() {};\n" +
        "b = {fn: fn};\n" +
        "JSCompiler_object_inline_a_0 = a;\n" +
        "JSCompiler_object_inline_a_1 = {a: JSCompiler_object_inline_a_0};\n" +
        "JSCompiler_object_inline_b_0 = b;\n" +
        "JSCompiler_object_inline_b_1 = {fn: JSCompiler_object_inline_b_0.fn};\n" +
        "obj = JSCompiler_object_inline_a_1, JSCompiler_object_inline_b_1;", null);

    checkEqualTrees(expectedOutput, root);
  }

  @Test
  public void testInlineObjectLiteralWithNestedObject() {
    // Test case for object literal inline with nested objects
    String input = "var obj = {a: {b: 1, c: 2}};";

    Node root = NodePrinter.parseJavaScript(input, null);
    passManager.addPass(new InlineObjectLiterals(compiler, new SimpleNameIdSupplier()));
    passManager.runPasses(root);

    Node expectedOutput = NodePrinter.parseJavaScript(
        "var a, b, c, JSCompiler_object_inline_a_0, JSCompiler_object_inline_a_1, JSCompiler_object_inline_b_0, JSCompiler_object_inline_b_1, JSCompiler_object_inline_c_0, JSCompiler_object_inline_c_1;\n" +
        "a = {b: 1, c: 2};\n" +
        "JSCompiler_object_inline_a_0 = a;\n" +
        "JSCompiler_object_inline_a_1 = {b: JSCompiler_object_inline_b_0, c: JSCompiler_object_inline_c_0};\n" +
        "b = 1;\n" +
        "c = 2;\n" +
        "obj = JSCompiler_object_inline_a_1;", null);

    checkEqualTrees(expectedOutput, root);
  }

  @Test
  public void testObjectLiteralWithCyclicReference() {
    // Test case for object literal inline with cyclic reference
    String input = "var obj = {a: {b: obj}};";

    Node root = NodePrinter.parseJavaScript(input, null);
    passManager.addPass(new InlineObjectLiterals(compiler, new SimpleNameIdSupplier()));
    try {
      passManager.runPasses(root);
      fail("Expected exception");
    } catch (IllegalStateException e) {
      // Expected exception
    }
  }

  @Test
  public void testObjectLiteralWithPropertyReference() {
    // Test case for object literal inline with property reference
    String input = "var obj = {a: 1, b: obj.a};";

    Node root = NodePrinter.parseJavaScript(input, null);
    passManager.addPass(new InlineObjectLiterals(compiler, new SimpleNameIdSupplier()));
    try {
      passManager.runPasses(root);
      fail("Expected exception");
    } catch (IllegalStateException e) {
      // Expected exception
    }
  }

  @Test
  public void testObjectLiteralWithGetterSetter() {
    // Test case for object literal inline with getter and setter
    String input = "var obj = {get a() {return 1}, set a(x) {}};";

    Node root = NodePrinter.parseJavaScript(input, null);
    passManager.addPass(new InlineObjectLiterals(compiler, new SimpleNameIdSupplier()));
    try {
      passManager.runPasses(root);
      fail("Expected exception");
    } catch (IllegalStateException e) {
      // Expected exception
    }
  }

  @Test
  public void testObjectLiteralWithSelfReference() {
    // Test case for object literal inline with self-reference
    String input = "var obj = {a: function() {return this.a}};";

    Node root = NodePrinter.parseJavaScript(input, null);
    passManager.addPass(new InlineObjectLiterals(compiler, new SimpleNameIdSupplier()));
    try {
      passManager.runPasses(root);
      fail("Expected exception");
    } catch (IllegalStateException e) {
      // Expected exception
    }
  }

  private void checkEqualTrees(Node expected, Node actual) {
    Context context = Context.createEmpty();
    actual.compile(context);
    assertEquals(expected.toString(), actual.toString());
  }
}

class SimpleNameIdSupplier implements Supplier<String> {
  private int counter = 0;

  @Override
  public String get() {
    return "SimpleNameIdSupplier_" + (++counter);
  }
}