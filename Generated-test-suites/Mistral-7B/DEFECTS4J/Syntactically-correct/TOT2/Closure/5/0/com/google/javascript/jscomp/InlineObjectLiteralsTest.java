import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.InlineObjectLiterals;
import org.junit.Before;
import org.junit.Test;

import static com.google.common.truth.Truth.assertThat;

public class InlineObjectLiteralsTest {

  private Compiler compiler;
  private CompilerPass inlineObjectLiteralsPass;

  @Before
  public void setUp() {
    compiler = new Compiler();
    inlineObjectLiteralsPass = new InlineObjectLiterals(compiler, () -> "");
  }

  @Test
  public void testInlineSimpleObjectLiterals() {
    String sourceCode = "function foo() {" +
        "  var obj = {" +
        "    a: 1," +
        "    b: 2," +
        "    c: 3" +
        "  };" +
        "  // Some code using obj" +
        "}";

    Node root = NodePrinter.parseJs(sourceCode);
    inlineObjectLiteralsPass.process(NodePrinter.getExternsFromSource(sourceCode), root);

    String resultCode = NodePrinter.printToSource(root);
    assertThat(resultCode)
        .contains("var a = JSCompiler_object_inline_a_")
        .contains("var b = JSCompiler_object_inline_b_")
        .contains("var c = JSCompiler_object_inline_c_")
        .contains("// Some code using a, b, and c")
        .doesNotContain("obj");
  }

  @Test
  public void testInlineNestedObjectLiterals() {
    String sourceCode = "function foo() {" +
        "  var obj1 = {" +
        "    a: {" +
        "      b: 1," +
        "      c: 2" +
        "    }," +
        "    d: 3" +
        "  };" +
        "  // Some code using obj1" +
        "}";

    Node root = NodePrinter.parseJs(sourceCode);
    inlineObjectLiteralsPass.process(NodePrinter.getExternsFromSource(sourceCode), root);

    String resultCode = NodePrinter.printToSource(root);
    assertThat(resultCode)
        .contains("var a = JSCompiler_object_inline_a_")
        .contains("var b = JSCompiler_object_inline_b__")
        .contains("var c = JSCompiler_object_inline_c__")
        .contains("// Some code using a, b, and c, as well as d")
        .doesNotContain("obj1");
  }

  @Test
  public void testInlineObjectWithFunctions() {
    String sourceCode = "function foo() {" +
        "  var obj = {" +
        "    func: function() {" +
        "      console.log('Hello World');" +
        "    }," +
        "    a: 1" +
        "  };" +
        "  obj.func();" +
        "}";

    Node root = NodePrinter.parseJs(sourceCode);
    inlineObjectLiteralsPass.process(NodePrinter.getExternsFromSource(sourceCode), root);

    String resultCode = NodePrinter.printToSource(root);
    assertThat(resultCode)
        .contains("function JSCompiler_object_inline_func_() {")
        .contains("console.log('Hello World');")
        .contains("var a = JSCompiler_object_inline_a_")
        .contains("// Some code using a and the inlined function")
        .doesNotContain("obj");
  }

  @Test
  public void testInlineObjectWithGetters() {
    String sourceCode = "function foo() {" +
        "  var obj = {" +
        "    get prop() {" +
        "      return 42;" +
        "    }," +
        "    a: 1" +
        "  };" +
        "  console.log(obj.prop); // -> 42" +
        "}";

    Node root = NodePrinter.parseJs(sourceCode);
    inlineObjectLiteralsPass.process(NodePrinter.getExternsFromSource(sourceCode), root);

    String resultCode = NodePrinter.printToSource(root);
    assertThat(resultCode)
        .contains("get JSCompiler_object_inline_prop_() {")
        .contains("return 42;")
        .contains("var a = JSCompiler_object_inline_a_")
        .contains("// Some code using a and the inlined getter")
        .doesNotContain("obj");
  }

  @Test
  public void testInlineObjectWithSetters() {
    String sourceCode = "function foo() {" +
        "  var obj = {" +
        "    set prop(value) {" +
        "      this.value = value;" +
        "    }," +
        "    a: 1" +
        "  };" +
        "  obj.prop = 'Hello World';" +
        "}";

    Node root = NodePrinter.parseJs(sourceCode);
    inlineObjectLiteralsPass.process(NodePrinter.getExternsFromSource(sourceCode), root);

    String resultCode = NodePrinter.printToSource(root);
    assertThat(resultCode)
        .contains("set JSCompiler_object_inline_prop_(value) {")
        .contains("this.value = value;")
        .contains("// Some code using the inlined setter")
        .doesNotContain("obj");
  }

  @Test
  public void testInlineObjectWithMixedProperties() {
    String sourceCode = "function foo() {" +
        "  var obj = {" +
        "    a: 1," +
        "    get prop() {" +
        "      return 42;" +
        "    }," +
        "    func: function() {" +
        "      console.log('Hello World');" +
        "    }," +
        "    b: 2" +
        "  };" +
        "  console.log(obj.prop); // -> 42" +
        "  obj.func();" +
        "}";

    Node root = NodePrinter.parseJs(sourceCode);
    inlineObjectLiteralsPass.process(NodePrinter.getExternsFromSource(sourceCode), root);

    String resultCode = NodePrinter.printToSource(root);
    assertThat(resultCode)
        .contains("function JSCompiler_object_inline_func_() {")
        .contains("console.log('Hello World');")
        .contains("get JSCompiler_object_inline_prop_() {")
        .contains("return 42;")
        .contains("var a = JSCompiler_object_inline_a_")
        .contains("var b = JSCompiler_object_inline_b_")
        .doesNotContain("obj");
  }

  @Test
  public void testInlineObjectWithSpread() {
    String sourceCode = "function foo() {" +
        "  var obj1 = {" +
        "    a: 1," +
        "    b: 2" +
        "  };" +
        "  var obj2 = {" +
        "    c: 3," +
        "    d: 4," +
        "    __spread__: function(target) {" +
        "      Object.assign(target, this);" +
        "    }" +
        "  };" +
        "  var obj3 = {" +
        "    __spread__: obj2.__spread__," +
        "    e: 5" +
        "  };" +
        "  Object.assign({}, obj3, obj1);" +
        "}";

    Node root = NodePrinter.parseJs(sourceCode);
    inlineObjectLiteralsPass.process(NodePrinter.getExternsFromSource(sourceCode), root);

    String resultCode = NodePrinter.printToSource(root);
    assertThat(resultCode)
        .contains("var a = JSCompiler_object_inline_a_")
        .contains("var b = JSCompiler_object_inline_b_")
        .contains("function JSCompiler_object_inline__spread__(target) {")
        .contains("Object.assign(target, this);")
        .contains("var c = JSCompiler_object_inline_c_")
        .contains("var d = JSCompiler_object_inline_d_")
        .contains("var e = JSCompiler_object_inline_e_")
        .doesNotContain("obj1")
        .doesNotContain("obj2")
        .doesNotContain("obj3");
  }

  @Test
  public void testInlineObjectLiteralsInForLoop() {
    String sourceCode = "function foo() {" +
        "  var objLiterals = [];" +
        "  for (var i = 0; i < 10; i++) {" +
        "    objLiterals.push({i: i});" +
        "  }" +
        "  var obj = {" +
        "    a: 1," +
        "    b: 2" +
        "  };" +
        "  for (var i = 0; i < objLiterals.length; i++) {" +
        "    obj[objLiterals[i].i] = objLiterals[i].i * 2;" +
        "  }" +
        "  console.log(JSON.stringify(obj)); // -> \"{a: 1, 0: 0, 1: 4, 2: 8, 3: 12, 4: 16, 5: 20, 6: 24, 7: 28, 8: 32, 9: 36}\"" +
        "}";

    Node root = NodePrinter.parseJs(sourceCode);
    inlineObjectLiteralsPass.process(NodePrinter.getExternsFromSource(sourceCode), root);

    String resultCode = NodePrinter.printToSource(root);
    assertThat(resultCode)
        .contains("for (var i = 0; i < 10; i++) {" +
            "  objLiterals.push(JSCompiler_object_inline_i__");
    for (int i = 0; i < 10; i++) {
      assertThat(resultCode)
          .contains("JSCompiler_object_inline_i__[i] = i * 2;");
    }
    assertThat(resultCode)
        .contains("console.log(JSON.stringify(obj)); // -> \"{a: 1, 0: 0, 1: 4, 1__: 2, 2: 8, 2__: 4, 3: 12, 3__: 6, 4: 16, 4__: 8, 5: 20, 5__: 10, 6: 24, 6__: 12, 7: 28, 7__: 14, 8: 32, 8__: 16, 9: 36, 9__: 18}\"")
        .doesNotContain("objLiterals")
        .doesNotContain("obj");
  }

  @Test
  public void testInlineObjectLiteralsInFunctionCall() {
    String sourceCode = "function foo() {" +
        "  var a = createObjectLiteral({a: 1});" +
        "  var b = createObjectLiteral({b: 2});" +
        "  bar(a, b);" +
        "}";

    Node root = NodePrinter.parseJs(sourceCode);
    inlineObjectLiteralsPass.process(NodePrinter.getExternsFromSource(sourceCode), root);

    String resultCode = NodePrinter.printToSource(root);
    assertThat(resultCode)
        .contains("function createObjectLiteral(obj) {")
        .contains("return obj;")
        .doesNotContain("a = createObjectLiteral({a: 1});")
        .doesNotContain("b = createObjectLiteral({b: 2});")
        .doesNotContain("bar(a, b);");
  }

  @Test
  public void testInlineObjectLiteralsInConditionalExpression() {
    String sourceCode = "function foo() {" +
        "  var a = 1;" +
        "  var conditional = (a > 0) ? {a: true} : {a: false};" +
        "  console.log(conditional.a); // -> true or false" +
        "}";

    Node root = NodePrinter.parseJs(sourceCode);
    inlineObjectLiteralsPass.process(NodePrinter.getExternsFromSource(sourceCode), root);

    String resultCode = NodePrinter.printToSource(root);
    assertThat(resultCode)
        .contains("var conditional = (a > 0) ? JSCompiler_object_inline_true__ : JSCompiler_object_inline_false__;")
        .doesNotContain("{a: true}")
        .doesNotContain("{a: false}");
  }
}