import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.InlineObjectLiterals;
import com.google.javascript.jscomp.ReferenceCollectingCallback;
import com.google.javascript.jscomp.ReferenceCollectingCallback.Behavior;
import com.google.javascript.jscomp.ReferenceCollectingCallback.Reference;
import com.google.javascript.jscomp.ReferenceCollectingCallback.ReferenceCollection;
import com.google.javascript.jscomp.ReferenceCollectingCallback.ReferenceMap;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.Node;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static com.google.common.collect.Maps.newHashMap;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class InlineObjectLiteralsTest {

  private Compiler compiler;
  private AbstractCompiler abstractCompiler;
  private Supplier<String> safeNameIdSupplier;

  @Before
  public void setUp() {
    compiler = new Compiler();
    abstractCompiler = new AbstractCompiler(compiler);
    safeNameIdSupplier = () -> "test_id";

    InlineObjectLiterals pass = new InlineObjectLiterals(abstractCompiler, safeNameIdSupplier);
    compiler.addPass(pass);
  }

  @Test
  public void testInlineObjectLiteral() {
    String sourceCode = "function foo() {" +
        "  var obj = {" +
        "    a: 1," +
        "    b: 2," +
        "    c: function() { return 3; }" +
        "  };" +
        "  var x = obj.a;" +
        "  var y = obj.b;" +
        "  x + y; // should be 3" +
        "}";

    Node ast = compiler.parseTree(sourceCode, null, null);
    Node root = ast.getFirstChild();

    String expectedCode = "function foo() {" +
        "  var a = 1;" +
        "  var b = 2;" +
        "  var c = function() { return 3; }" +
        "  var x = a;" +
        "  var y = b;" +
        "  x + y; // should be 3" +
        "}" +
        "JSCompiler_object_inline_a = 1;" +
        "JSCompiler_object_inline_b = 2;" +
        "JSCompiler_object_inline_c = function() { return 3; }";

    pass.process(null, root);
    String resultCode = compiler.printCompiledJs(ast, Compiler.CompilationLevel.SIMPLE);

    assertEquals(expectedCode, resultCode);
  }

  @Test
  public void testDontInlineObjectLiteralWithSideEffects() {
    String sourceCode = "function foo() {" +
        "  var obj = {" +
        "    a: function() { return ++i; }" +
        "  };" +
        "  var x = obj.a();" +
        "  return x; // should return 1, then 2" +
        "}" +
        "var i = 0;";

    Node ast = compiler.parseTree(sourceCode, null, null);
    Node root = ast.getFirstChild();

    String expectedCode = "function foo() {" +
        "  var a = function() { return ++i; }" +
        "  var x = a();" +
        "  return x; // should return 1, then 2" +
        "  JSCompiler_object_inline_a = function() { return ++i; }";

    pass.process(null, root);
    String resultCode = compiler.printCompiledJs(ast, Compiler.CompilationLevel.SIMPLE);

    assertEquals(expectedCode, resultCode);
  }

  @Test
  public void testDontInlineObjectLiteralWithGetterSetter() {
    String sourceCode = "function foo() {" +
        "  var obj = {" +
        "    get a() { return 1; }," +
        "    set a(val) { this.val = val; }," +
        "    b: 2" +
        "  };" +
        "  obj.a = 3;" +
        "  var x = obj.b;" +
        "  x; // should be 2" +
        "}" +
        "var obj = {};";

    Node ast = compiler.parseTree(sourceCode, null, null);
    Node root = ast.getFirstChild();

    String expectedCode = "function foo() {" +
        "  var getA = function() { return 1; };" +
        "  var setA = function(val) { this.val = val; };" +
        "  var b = 2;" +
        "  obj.a = getA;" +
        "  obj.a = 3;" +
        "  var x = b;" +
        "  x; // should be 2" +
        "  JSCompiler_object_inline_getA = function() { return 1; }" +
        "  JSCompiler_object_inline_setA = function(val) { this.val = val; }";

    pass.process(null, root);
    String resultCode = compiler.printCompiledJs(ast, Compiler.CompilationLevel.SIMPLE);

    assertEquals(expectedCode, resultCode);
  }

  @Test
  public void testInlineObjectLiteralWithReference() {
    String sourceCode = "function foo() {" +
        "  var obj = {" +
        "    a: 1," +
        "    b: 2" +
        "  };" +
        "  var x = obj;" +
        "  function bar(o) {" +
        "    o.a++;" +
        "  }" +
        "  bar(obj);" +
        "  var y = obj.a;" +
        "  x.a; // should be 2" +
        "}" +
        "var obj = {};";

    Node ast = compiler.parseTree(sourceCode, null, null);
    Node root = ast.getFirstChild();

    String expectedCode = "function foo() {" +
        "  var a = 1;" +
        "  var b = 2;" +
        "  var x = a;" +
        "  function bar(o) {" +
        "    o++;" +
        "  }" +
        "  bar(a);" +
        "  var y = a;" +
        "  x; // should be 2" +
        "  JSCompiler_object_inline_a = 1" +
        "  JSCompiler_object_inline_b = 2";

    pass.process(null, root);
    String resultCode = compiler.printCompiledJs(ast, Compiler.CompilationLevel.SIMPLE);

    assertEquals(expectedCode, resultCode);
  }

  @Test
  public void testInlineObjectLiteralWithCycle() {
    String sourceCode = "function foo() {" +
        "  var obj1 = {" +
        "    a: 1," +
        "    b: { b: obj1 }" +
        "  };" +
        "  var x = obj1.b.b; // should be obj1" +
        "}" +
        "var obj1 = {}";

    Node ast = compiler.parseTree(sourceCode, null, null);
    Node root = ast.getFirstChild();

    String expectedError = "Error: Object cycle detected.";

    try {
      pass.process(null, root);
      fail("Expected an error.");
    } catch (RuntimeException e) {
      assertEquals(expectedError, e.getMessage());
    }

    String resultCode = compiler.printCompiledJs(ast, Compiler.CompilationLevel.SIMPLE);
    assertTrue(resultCode.contains(expectedError));
  }

  @Test
  public void testInlineObjectLiteralWithNestedObjectLiteral() {
    String sourceCode = "function foo() {" +
        "  var obj1 = {" +
        "    a: 1," +
        "    b: {" +
        "      c: 2," +
        "      d: 3" +
        "    }" +
        "  };" +
        "  var x = obj1.b.c;" +
        "  var y = obj1.b.d;" +
        "}" +
        "var obj1 = {};";

    Node ast = compiler.parseTree(sourceCode, null, null);
    Node root = ast.getFirstChild();

    String expectedCode = "function foo() {" +
        "  var a = 1;" +
        "  var c = 2;" +
        "  var d = 3;" +
        "  var b = {" +
        "    c: c," +
        "    d: d" +
        "  }" +
        "  var x = c;" +
        "  var y = d" +
        "  JSCompiler_object_inline_a = 1" +
        "  JSCompiler_object_inline_c = 2" +
        "  JSCompiler_object_inline_d = 3";

    pass.process(null, root);
    String resultCode = compiler.printCompiledJs(ast, Compiler.CompilationLevel.SIMPLE);

    assertEquals(expectedCode, resultCode);
  }

  @Test
  public void testInlineObjectLiteralWithEmptyObject() {
    String sourceCode = "function foo() {" +
        "  var obj = {};" +
        "  var x = obj;" +
        "}" +
        "var obj = {}";

    Node ast = compiler.parseTree(sourceCode, null, null);
    Node root = ast.getFirstChild();

    String expectedCode = "function foo() {" +
        "  var x = undefined;" +
        "  JSCompiler_object_inline_empty = {}";

    pass.process(null, root);
    String resultCode = compiler.printCompiledJs(ast, Compiler.CompilationLevel.SIMPLE);

    assertEquals(expectedCode, resultCode);
  }

  @Test
  public void testInlineObjectLiteralWithSelfReferencingAssignment() {
    String sourceCode =
        "function foo() {" +
        "  var x = {" +
        "    a: 1," +
        "    b: function() { return x; }" +
        "  };" +
        "  x.b = x;" +
        "}" +
        "var x = {}";

    Node ast = compiler.parseTree(sourceCode, null, null);
    Node root = ast.getFirstChild();

    String expectedError = "Error: Object cycle detected.";

    try {
      pass.process(null, root);
      fail("Expected an error.");
    } catch (RuntimeException e) {
      assertEquals(expectedError, e.getMessage());
    }

    String resultCode = compiler.printCompiledJs(ast, Compiler.CompilationLevel.SIMPLE);
    assertTrue(resultCode.contains(expectedError));
  }

  @Test
  public void testInlineObjectLiteralWithFunctionDeclaration() {
    String sourceCode = "function foo() {" +
        "  var obj = {" +
        "    a: 1," +
        "    b: 2," +
        "    func: function myFunc() { return 3; }" +
        "  };" +
        "  var x = obj.func();" +
        "}" +
        "var obj = {}";

    Node ast = compiler.parseTree(sourceCode, null, null);
    Node root = ast.getFirstChild();

    String expectedCode = "function foo() {" +
        "  var a = 1;" +
        "  var b = 2;" +
        "  var func = function myFunc() { return 3; }" +
        "  var x = func();" +
        "  JSCompiler_object_inline_a = 1" +
        "  JSCompiler_object_inline_b = 2";

    pass.process(null, root);
    String resultCode = compiler.printCompiledJs(ast, Compiler.CompilationLevel.SIMPLE);

    assertEquals(expectedCode, resultCode);
  }
}