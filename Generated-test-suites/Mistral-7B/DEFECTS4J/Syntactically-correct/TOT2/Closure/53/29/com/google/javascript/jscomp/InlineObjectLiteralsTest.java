import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.InlineObjectLiterals;
import com.google.javascript.rhino.Node;

import java.util.List;

public class InlineObjectLiteralsTest {

  @SuppressWarnings("unchecked")
  private static <T> void assertEqualsObjects(T expected, T actual) {
    assertEquals("Unexpected object structure", expected, actual);
  }

  @SuppressWarnings("unchecked")
  private static <T> void assertEqualsLists(List<T> expected, List<T> actual) {
    assertEquals("Unexpected list structure", expected.size(), actual.size());
    for (int i = 0; i < expected.size(); i++) {
      assertEquals("Unexpected element at index " + i, expected.get(i), actual.get(i));
    }
  }

  @SuppressWarnings("unchecked")
  private static <K, V> void assertEqualsMaps(Map<K, V> expected, Map<K, V> actual) {
    assertEquals("Unexpected map size", expected.size(), actual.size());
    for (Map.Entry<K, V> e : expected.entrySet()) {
      assertEquals("Unexpected key-value pair", e.getKey(), actual.get(e.getKey()));
      assertEquals("Unexpected value", e.getValue(), actual.get(e.getKey()));
    }
  }

  private Node createTestNode(String code) {
    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler(options);
    Node rootNode = new Node(Token.PROGRAM, null);
    Node externsNode = new Node(Token.PROGRAM, null);

    compiler.compile(externsNode, code, null, null);
    Node result = compiler.getJsResult();
    Preconditions.checkState(result != null);
    result.getFirstChild().addChildToFront(externsNode);
    rootNode.addChildToFront(result);

    return rootNode;
  }

  @SuppressWarnings("ResultOfMethodCallIgnored")
  private void runPass(CompilerPass pass, String inputCode, String expectedOutputCode) {
    Node input = createTestNode(inputCode);
    Compiler compiler = new Compiler(new CompilerOptions());
    compiler.addPass(pass);
    compiler.compile(input, null, null, null);
    Node output = compiler.getJsResult();
    String outputCode = NodePrinter.printToString(output);
    assertEquals("Unexpected output", expectedOutputCode, outputCode);
  }

  @Test
  public void testInlineSimpleObject() {
    String inputCode = "var obj = {a: 1, b: 2};";
    String expectedOutputCode = "var a = JSCompiler_object_inline_a_0;
                                var b = JSCompiler_object_inline_b_0;
                                obj = {a: a, b: b};";

    runPass(new InlineObjectLiterals(new StubCompiler(), () -> "JSCompiler_object_inline_0_"), inputCode, expectedOutputCode);
  }

  @Test
  public void testInlineObjectWithNestedObjects() {
    String inputCode = "var obj = {a: {b: 1, c: 2}, d: 3};";
    String expectedOutputCode = "var b = JSCompiler_object_inline_b_0;
                                var c = JSCompiler_object_inline_c_0;
                                var a = {b: b, c: c};
                                var obj = {a: a, d: 3};";

    runPass(new InlineObjectLiterals(new StubCompiler(), () -> "JSCompiler_object_inline_0_"), inputCode, expectedOutputCode);
  }

  @Test
  public void testInlineObjectWithFunctions() {
    String inputCode = "var obj = {foo: function() { return 1; }, bar: function() { return 2; }};";
    String expectedOutputCode = "var foo = JSCompiler_object_inline_foo_0;
                                var bar = JSCompiler_object_inline_bar_0;
                                var obj = {foo: foo, bar: bar};";

    runPass(new InlineObjectLiterals(new StubCompiler(), () -> "JSCompiler_object_inline_0_"), inputCode, expectedOutputCode);
  }

  @Test
  public void testInlineObjectWithFunctionsAndNestedObjects() {
    String inputCode = "var obj = {foo: function() { return 1; }, bar: {b: 2, c: function() { return 3; }}};";
    String expectedOutputCode = "var foo = JSCompiler_object_inline_foo_0;
                                var b = JSCompiler_object_inline_b_0;
                                var c = JSCompiler_object_inline_c_0;
                                var obj = {foo: foo, bar: {b: b, c: c}};";

    runPass(new InlineObjectLiterals(new StubCompiler(), () -> "JSCompiler_object_inline_0_"), inputCode, expectedOutputCode);
  }

  @Test
  public void testDontInlineSelfReferentialObject() {
    String inputCode = "var obj = {a: {b: obj.a}};";

    // The pass should not modify the code since obj.a refers to obj itself.
    Node input = createTestNode(inputCode);
    Compiler compiler = new Compiler(new CompilerOptions());
    compiler.addPass(new InlineObjectLiterals(new StubCompiler(), () -> "JSCompiler_object_inline_0_"));
    compiler.compile(input, null, null, null);
    Node output = compiler.getJsResult();
    String outputCode = NodePrinter.printToString(output);
    assertEquals("Unexpected output", inputCode, outputCode);
  }

  @Test
  public void testDontInlineObjectWithNotLvalueReference() {
    String inputCode = "var obj = {a: 1}; function f() { return obj.a; }";

    // The pass should not modify the code since obj.a is not an lvalue.
    Node input = createTestNode(inputCode);
    Compiler compiler = new Compiler(new CompilerOptions());
    compiler.addPass(new InlineObjectLiterals(new StubCompiler(), () -> "JSCompiler_object_inline_0_"));
    compiler.compile(input, null, null, null);
    Node output = compiler.getJsResult();
    String outputCode = NodePrinter.printToString(output);
    assertEquals("Unexpected output", inputCode, outputCode);
  }

  @Test
  public void testDontInlineObjectWithInitializerReference() {
    String inputCode = "var obj = {a: 1}; function f() { var x = obj; }";

    // The pass should not modify the code since obj is an initializer.
    Node input = createTestNode(inputCode);
    Compiler compiler = new Compiler(new CompilerOptions());
    compiler.addPass(new InlineObjectLiterals(new StubCompiler(), () -> "JSCompiler_object_inline_0_"));
    compiler.compile(input, null, null, null);
    Node output = compiler.getJsResult();
    String outputCode = NodePrinter.printToString(output);
    assertEquals("Unexpected output", inputCode, outputCode);
  }

  @Test(expected = IllegalStateException.class)
  public void testDontInlineVarWithSameNameAsObject() {
    String inputCode = "var obj = {a: 1}; var a = 2;";

    // The pass should throw an exception since 'a' already exists as a variable.
    Compiler compiler = new Compiler(new CompilerOptions());
    compiler.addPass(new InlineObjectLiterals(new StubCompiler(), () -> "JSCompiler_object_inline_0_"));
    Node input = createTestNode(inputCode);
    compiler.compile(input, null, null, null);
  }

  @Test
  public void testDontInlineObjectWithCircularReferences() {
    String inputCode = "var obj1 = {a: obj2}; var obj2 = {a: obj1};";

    // The pass should not modify the code since it would create a circular reference.
    Node input = createTestNode(inputCode);
    Compiler compiler = new Compiler(new CompilerOptions());
    compiler.addPass(new InlineObjectLiterals(new StubCompiler(), () -> "JSCompiler_object_inline_0_"));
    assertThrows(IllegalArgumentException.class, () -> compiler.compile(input, null, null, null));
  }

  @Test
  public void testInlineObjectWithGlobalVariables() {
    String inputCode = "var globalVar = 1; var obj = {a: globalVar};";
    String expectedOutputCode = "var globalVar = 1;
                                var a = JSCompiler_object_inline_a_0;
                                obj = {a: a};";

    runPass(new InlineObjectLiterals(new StubCompiler(), () -> "JSCompiler_object_inline_0_"), inputCode, expectedOutputCode);
  }

  @Test
  public void testInlineObjectWithGlobalFunctions() {
    String inputCode = "function globalFunction() { return 1; }
                       var obj = {a: globalFunction};";
    String expectedOutputCode = "function globalFunction() { return 1; }
                                var a = JSCompiler_object_inline_a_0;
                                obj = {a: a};";

    runPass(new InlineObjectLiterals(new StubCompiler(), () -> "JSCompiler_object_inline_0_"), inputCode, expectedOutputCode);
  }

  @Test
  public void testInlineObjectWithGlobalVariablesAndFunctions() {
    String inputCode = "var globalVar = 1;
                       function globalFunction() { return 1; }
                       var obj = {a: globalVar, b: globalFunction};";
    String expectedOutputCode = "var globalVar = 1;
                                function globalFunction() { return 1; }
                                var a = JSCompiler_object_inline_a_0;
                                var b = JSCompiler_object_inline_b_0;
                                obj = {a: a, b: b};";

    runPass(new InlineObjectLiterals(new StubCompiler(), () -> "JSCompiler_object_inline_0_"), inputCode, expectedOutputCode);
  }

  @Test
  public void testInlineObjectWithExportedVariable() {
    String inputCode = "export let globalVar = 1;
                       var obj = {a: globalVar};";

    // The pass should not inline the exported variable since it's marked as exported.
    Node input = createTestNode(inputCode);
    Compiler compiler = new Compiler(new CompilerOptions());
    compiler.addPass(new InlineObjectLiterals(new StubCompiler(), () -> "JSCompiler_object_inline_0_"));
    assertThrows(IllegalArgumentException.class, () -> compiler.compile(input, null, null, null));
  }

  @Test
  public void testInlineObjectWithExportedFunction() {
    String inputCode = "export function globalFunction() { return 1; }
                       var obj = {a: globalFunction};";

    // The pass should not inline the exported function since it's marked as exported.
    Node input = createTestNode(inputCode);
    Compiler compiler = new Compiler(new CompilerOptions());
    compiler.addPass(new InlineObjectLiterals(new StubCompiler(), () -> "JSCompiler_object_inline_0_"));
    assertThrows(IllegalArgumentException.class, () -> compiler.compile(input, null, null, null));
  }

  @Test
  public void testInlineObjectWithExportedVariableAndFunction() {
    String inputCode = "export let globalVar = 1;
                       export function globalFunction() { return 1; }
                       var obj = {a: globalVar, b: globalFunction};";

    // The pass should not inline the exported variable and function since they're marked as exported.
    Node input = createTestNode(inputCode);
    Compiler compiler = new Compiler(new CompilerOptions());
    compiler.addPass(new InlineObjectLiterals(new StubCompiler(), () -> "JSCompiler_object_inline_0_"));
    assertThrows(IllegalArgumentException.class, () -> compiler.compile(input, null, null, null));
  }
}