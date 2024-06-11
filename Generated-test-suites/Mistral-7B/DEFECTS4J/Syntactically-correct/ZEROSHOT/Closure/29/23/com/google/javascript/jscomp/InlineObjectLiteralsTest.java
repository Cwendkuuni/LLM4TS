import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeUtil.*;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.ReferenceCollectingCallback;
import com.google.javascript.jscomp.ReferenceCollectingCallback.Behavior;
import com.google.javascript.jscomp.ReferenceCollectingCallback.Reference;
import com.google.javascript.jscomp.ReferenceCollectingCallback.ReferenceCollection;
import com.google.javascript.jscomp.ReferenceCollectingCallback.ReferenceMap;
import com.google.javascript.rhino.Node;
import org.junit.Before;
import org.junit.Test;

public class InlineObjectLiteralsTest {

  private Compiler compiler;
  private AbstractCompiler abstractCompiler;
  private Supplier<String> safeNameIdSupplier;

  @Before
  public void setUp() {
    CompilerOptions options = new CompilerOptions();
    compiler = new Compiler(options);
    abstractCompiler = new AbstractCompiler(compiler, options);
    safeNameIdSupplier = () -> "test_id";
  }

  private void assertInline(String input, String expectedOutput) {
    Node externs = createNodeFromString(input);
    Node root = createNodeFromString("// empty root node");

    InlineObjectLiterals pass =
        new InlineObjectLiterals(abstractCompiler, safeNameIdSupplier);
    pass.process(externs, root);

    String output = compiler.toSource(root);
    assertEquals(expectedOutput, output);
  }

  @Test
  public void testInlineObjectLiteral() {
    String input =
        "function foo() {" +
        "  var obj = {" +
        "    a: 1," +
        "    b: 2," +
        "    c: 3" +
        "  };" +
        "  return obj.a + obj.b;" +
        "}";

    String expectedOutput =
        "function foo() {" +
        "  var a = JSCompiler_object_inline_a_test_id;" +
        "  var b = JSCompiler_object_inline_b_test_id;" +
        "  var c = JSCompiler_object_inline_c_test_id;" +
        "  return a + b;" +
        "}";

    assertInline(input, expectedOutput);
  }

  @Test
  public void testInlineObjectLiteralWithFunctionProperty() {
    String input =
        "function foo() {" +
        "  var obj = {" +
        "    a: 1," +
        "    func: function() { return 5; }" +
        "  }";

    String expectedOutput =
        "function foo() {" +
        "  var a = JSCompiler_object_inline_a_test_id;" +
        "  var func = function() { return 5; };" +
        "  return a;" +
        "}";

    assertInline(input, expectedOutput);
  }

  @Test
  public void testInlineObjectLiteralWithGetterSetterProperty() {
    String input =
        "function foo() {" +
        "  var obj = {" +
        "    get a() {" +
        "      return 1;" +
        "    }," +
        "    set a(v) {" +
        "      this.x = v;" +
        "    }," +
        "    b: 2" +
        "  }";

    String expectedOutput =
        "function foo() {" +
        "  var a = JSCompiler_object_inline_a_test_id;" +
        "  var b = JSCompiler_object_inline_b_test_id;" +
        "  return a;" +
        "}" +
        "Object.defineProperty(function foo() {" +
        "  // implementation of getter here" +
        "}, \"a\", {" +
        "  get: function() {" +
        "    return this.x;" +
        "  }," +
        "  set: function(v) {" +
        "    this.x = v;" +
        "  }" +
        "})";

    assertInline(input, expectedOutput);
  }

  @Test
  public void testInlineObjectLiteralWithNestedProperties() {
    String input =
        "function foo() {" +
        "  var obj = {" +
        "    a: {" +
        "      b: 1," +
        "      c: {" +
        "        d: 2" +
        "      }" +
        "    }" +
        "  }";

    String expectedOutput =
        "function foo() {" +
        "  var a = JSCompiler_object_inline_a_test_id;" +
        "  var b = JSCompiler_object_inline_b_test_id;" +
        "  var c = JSCompiler_object_inline_c_test_id;" +
        "  var d = JSCompiler_object_inline_d_test_id;" +
        "  return a.b + c.d;" +
        "}";

    assertInline(input, expectedOutput);
  }

  @Test
  public void testInlineObjectLiteralWithCycle() {
    String input =
        "function foo() {" +
        "  var obj1 = {" +
        "    a: 1," +
        "    obj2: obj2" +
        "  }" +
        "  var obj2 = {" +
        "    b: 2," +
        "    obj1: obj1" +
        "  }";

    String expectedOutput =
        "function foo() {" +
        "  var a = JSCompiler_object_inline_a_test_id;" +
        "  var b = JSCompiler_object_inline_b_test_id;" +
        "  var obj1 = {" +
        "    a: a" +
        "  }" +
        "  var obj2 = {" +
        "    b: b" +
        "  }" +
        "  return obj1.a + obj2.b;" +
        "}";

    assertInline(input, expectedOutput);
  }

  @Test
  public void testInlineObjectLiteralWithInitializer() {
    String input =
        "function foo() {" +
        "  var obj = {" +
        "    a: 1," +
        "    b: 2," +
        "    c: 3" +
        "  }";

    String expectedOutput =
        "function foo() {" +
        "  var a = JSCompiler_object_inline_a_test_id;" +
        "  var b = JSCompiler_object_inline_b_test_id;" +
        "  var c = JSCompiler_object_inline_c_test_id;" +
        "  return {" +
        "    a: a," +
        "    b: b," +
        "    c: c" +
        "  }" +
        "}";

    assertInline(input, expectedOutput);
  }

  @Test
  public void testInlineObjectLiteralWithUndefinedProperty() {
    String input =
        "function foo() {" +
        "  var obj = {" +
        "    a: 1," +
        "    b: undefined" +
        "  }";

    String expectedOutput =
        "function foo() {" +
        "  var a = JSCompiler_object_inline_a_test_id;" +
        "  var b = JSCompiler_object_inline_b_test_id;" +
        "  return {" +
        "    a: a," +
        "    b: b" +
        "  }" +
        "}";

    assertInline(input, expectedOutput);
  }

  @Test
  public void testInlineObjectLiteralWithEmptyObject() {
    String input =
        "function foo() {" +
        "  var obj = {}";

    String expectedOutput =
        "function foo() {" +
        "  var obj = {}" +
        "  return obj" +
        "}";

    assertInline(input, expectedOutput);
  }

  @Test
  public void testInlineObjectLiteralWithNoObjectLiteral() {
    String input =
        "function foo() {" +
        "  var a = 1;" +
        "  var b = 2;" +
        "  return a + b" +
        "}";

    String expectedOutput =
        "function foo() {" +
        "  var a = 1;" +
        "  var b = 2;" +
        "  return a + b" +
        "}";

    assertInline(input, expectedOutput);
  }
}