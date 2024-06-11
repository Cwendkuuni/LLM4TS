import org.junit.Test;
import static org.junit.Assert.*;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.InlineObjectLiterals;
import com.google.javascript.rhino.Node;

public class InlineObjectLiteralsTest {

  @Test
  public void testInlineObjectLiterals_SimpleObject() {
    String code = "function f() { var x = {a: 1, b: 2}; }";
    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    Node root = null;

    // Apply InlineObjectLiterals pass
    CompilerPass pass = new InlineObjectLiterals(compiler, null);
    pass.process(null, root);

    // Check the generated code
    Node result = compiler.processString(code, options);
    assertEquals(
        "function f() {" +
        "  var a = 1;" +
        "  var b = 2;" +
        "}",
        compiler.getSourceCode(result));
  }

  @Test
  public void testInlineObjectLiterals_NestedObject() {
    String code = "function f() { var x = {a: 1, b: {c: 2, d: 3}}; }";
    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    Node root = null;

    // Apply InlineObjectLiterals pass
    CompilerPass pass = new InlineObjectLiterals(compiler, null);
    pass.process(null, root);

    // Check the generated code
    Node result = compiler.processString(code, options);
    assertEquals(
        "function f() {" +
        "  var a = 1;" +
        "  var c = 2;" +
        "  var d = 3;" +
        "}",
        compiler.getSourceCode(result));
  }

  @Test
  public void testInlineObjectLiterals_EmptyObject() {
    String code = "function f() { var x = {}; }";
    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    Node root = null;

    // Apply InlineObjectLiterals pass
    CompilerPass pass = new InlineObjectLiterals(compiler, null);
    pass.process(null, root);

    // Check the generated code
    Node result = compiler.processString(code, options);
    assertEquals("function f() {}", compiler.getSourceCode(result));
  }

  @Test
  public void testInlineObjectLiterals_ObjectWithFunction() {
    String code = "function f() { var x = {a: 1, func: function() {}}; }";
    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    Node root = null;

    // Apply InlineObjectLiterals pass
    CompilerPass pass = new InlineObjectLiterals(compiler, null);
    pass.process(null, root);

    // Check the generated code
    Node result = compiler.processString(code, options);
    assertEquals(
        "function f() {" +
        "  var a = 1;" +
        "  function func() {}" +
        "}",
        compiler.getSourceCode(result));
  }

  @Test(expected = AssertionError.class)
  public void testInlineObjectLiterals_SelfReference() {
    String code = "function f() { var x = {a: x.b}; }";
    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    Node root = null;

    // Apply InlineObjectLiterals pass
    CompilerPass pass = new InlineObjectLiterals(compiler, null);
    pass.process(null, root);

    // Check the generated code
    Node result = compiler.processString(code, options);
    // This test should fail due to the self-reference in the object literal.
    compiler.getSourceCode(result);
  }

  @Test(expected = AssertionError.class)
  public void testInlineObjectLiterals_CircularReference() {
    String code = "function f() { var x = {a: {b: x}}; }";
    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    Node root = null;

    // Apply InlineObjectLiterals pass
    CompilerPass pass = new InlineObjectLiterals(compiler, null);
    pass.process(null, root);

    // Check the generated code
    Node result = compiler.processString(code, options);
    // This test should fail due to the circular reference in the object literal.
    compiler.getSourceCode(result);
  }

  @Test
  public void testInlineObjectLiterals_ObjectWithPropertiesAndValues() {
    String code = "function f() { var x = {a: 1, b: 2}; }";
    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    Node root = null;

    // Apply InlineObjectLiterals pass
    CompilerPass pass = new InlineObjectLiterals(compiler, null);
    pass.process(null, root);

    // Check the generated code
    Node result = compiler.processString(code, options);
    assertEquals(
        "function f() {" +
        "  var a = 1;" +
        "  var b = 2;" +
        "}",
        compiler.getSourceCode(result));
  }

  @Test
  public void testInlineObjectLiterals_ObjectWithFunctionAndProperties() {
    String code = "function f() { var x = {a: 1, func: function() {}}; }";
    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    Node root = null;

    // Apply InlineObjectLiterals pass
    CompilerPass pass = new InlineObjectLiterals(compiler, null);
    pass.process(null, root);

    // Check the generated code
    Node result = compiler.processString(code, options);
    assertEquals(
        "function f() {" +
        "  var a = 1;" +
        "  function func() {}" +
        "}",
        compiler.getSourceCode(result));
  }

  @Test
  public void testInlineObjectLiterals_ObjectWithPropertiesAndValuesAndFunction() {
    String code = "function f() { var x = {a: 1, b: 2, func: function() {}}; }";
    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    Node root = null;

    // Apply InlineObjectLiterals pass
    CompilerPass pass = new InlineObjectLiterals(compiler, null);
    pass.process(null, root);

    // Check the generated code
    Node result = compiler.processString(code, options);
    assertEquals(
        "function f() {" +
        "  var a = 1;" +
        "  var b = 2;" +
        "  function func() {}" +
        "}",
        compiler.getSourceCode(result));
  }
}