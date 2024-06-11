import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerPassManager;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.InlineObjectLiterals;
import org.junit.Before;
import org.junit.Test;

import static com.google.common.truth.Truth.assertThat;

public class InlineObjectLiteralsTest {

  private Compiler compiler;
  private CompilerOptions options;
  private CompilerPassManager passManager;

  @Before
  public void setup() {
    options = new CompilerOptions();
    compiler = new Compiler(options);
    passManager = new CompilerPassManager(options, compiler);
  }

  @Test
  public void testInlineObjectLiterals() {
    String input = "function foo() {" +
        "  var obj = {a: 1, b: 2, c: {d: 3, e: 4}};" +
        "  return obj.c.e; // returns 4";

    Node externs = NodeUtil.parseTree(input, null);
    Node root = NodeUtil.parseTree("function foo() { return arguments[0].c.e; }", null);

    passManager.addPass(new InlineObjectLiterals(compiler, () -> "inline_var_"));
    passManager.runPasses(externs, root);

    // Basic functionality test
    String expectedOutput = "function foo() {" +
        "  var a = inline_var_a; // 1" +
        "  var b = inline_var_b; // 2" +
        "  var d = inline_var_d; // 3" +
        "  var e = inline_var_e; // 4" +
        "  return e; // returns 4";

    String output = NodePrinter.printTree(root);
    assertThat(output).isEqualTo(expectedOutput);

    // Edge cases and exception handling tests
    // 1. Object literals with empty braces {}
    input = "function foo() {" +
        "  var obj = {}; // empty object literal" +
        "  return obj; // should be inlined into an empty object" +
        "}";

    externs = NodeUtil.parseTree(input, null);
    root = NodeUtil.parseTree("function foo() { return {}; }", null);

    passManager.runPasses(externs, root);
    expectedOutput = "function foo() {" +
        "  var obj = {}; // empty object" +
        "  return obj; // empty object is inlined into {}" +
        "}";

    output = NodePrinter.printTree(root);
    assertThat(output).isEqualTo(expectedOutput);

    // 2. Object literals with only one property
    input = "function foo() {" +
        "  var obj = {a: 1}; // object literal with one property" +
        "  return obj.a; // returns 1" +
        "}";

    externs = NodeUtil.parseTree(input, null);
    root = NodeUtil.parseTree("function foo() { return arguments[0].a; }", null);

    passManager.runPasses(externs, root);
    expectedOutput = "function foo() {" +
        "  var a = inline_var_a; // 1" +
        "  return a; // returns 1" +
        "}";

    output = NodePrinter.printTree(root);
    assertThat(output).isEqualTo(expectedOutput);

    // 3. Object literals with cyclic references
    input = "function foo() {" +
        "  var obj1 = {a: 1, b: obj2};" +
        "  var obj2 = {a: obj1, b: 2};" +
        "  return obj1.b; // should return 2" +
        "}";

    externs = NodeUtil.parseTree(input, null);
    root = NodeUtil.parseTree("function foo() { return arguments[0].b; }", null);

    passManager.runPasses(externs, root);
    expectedOutput = "function foo() {" +
        "  var a = inline_var_a; // 1" +
        "  var b = inline_var_b; // 2" +
        "  return b; // returns 2" +
        "}";

    output = NodePrinter.printTree(root);
    assertThat(output).isEqualTo(expectedOutput);

    // 4. Object literals with duplicate keys
    input = "function foo() {" +
        "  var obj = {a: 1, a: 2}; // object literal with duplicate keys" +
        "  return obj.a; // should return 2" +
        "}";

    externs = NodeUtil.parseTree(input, null);
    root = NodeUtil.parseTree("function foo() { return arguments[0].a; }", null);

    passManager.runPasses(externs, root);
    expectedOutput = "function foo() {" +
        "  var a = inline_var_a_2; // 2" +
        "  return a; // returns 2" +
        "}";

    output = NodePrinter.printTree(root);
    assertThat(output).isEqualTo(expectedOutput);

    // 5. Object literals with computed property names
    input = "function foo() {" +
        "  var obj = {" +
        "    'a' + 'b': 1, // computed property name" +
        "    ['a' + 'b']: 2" +
        "  };" +
        "  return obj['a' + 'b']; // should return 2" +
        "}";

    externs = NodeUtil.parseTree(input, null);
    root = NodeUtil.parseTree("function foo() { return arguments[0]['a' + 'b']; }", null);

    passManager.runPasses(externs, root);
    expectedOutput = "function foo() {" +
        "  var ab = inline_var_ab; // 'ab' computed property name" +
        "  return inline_var_ab_1; // 2" +
        "}";

    output = NodePrinter.printTree(root);
    assertThat(output).isEqualTo(expectedOutput);

    // Error scenarios
    // 1. Object literals with circular references in keys
    input = "function foo() {" +
        "  var obj = {" +
        "    a: {b: obj} // circular reference in key" +
        "  };" +
        "  return obj.a.b; // should throw an error" +
        "}";

    externs = NodeUtil.parseTree(input, null);
    root = NodeUtil.parseTree("function foo() { return arguments[0].a.b; }", null);

    try {
      passManager.runPasses(externs, root);
      fail("Should have thrown an error.");
    } catch (RuntimeException e) {
      // Pass
    }

    // 2. Object literals with object properties that reference the object literal itself
    input = "function foo() {" +
        "  var obj = {" +
        "    a: {b: obj} // object property references object literal" +
        "  };" +
        "  return obj.a.b; // should throw an error" +
        "}";

    externs = NodeUtil.parseTree(input, null);
    root = NodeUtil.parseTree("function foo() { return arguments[0].a.b; }", null);

    try {
      passManager.runPasses(externs, root);
      fail("Should have thrown an error.");
    } catch (RuntimeException e) {
      // Pass
    }
  }
}