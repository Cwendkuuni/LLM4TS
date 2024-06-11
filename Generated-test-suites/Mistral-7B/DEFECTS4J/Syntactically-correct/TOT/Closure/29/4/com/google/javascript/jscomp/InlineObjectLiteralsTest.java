import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.InlineObjectLiterals;
import com.google.javascript.jscomp.ReferenceCollectingCallback;
import com.google.javascript.rhino.Node;

public class InlineObjectLiteralsTest {

  @Test
  public void testInlineObjectLiterals() {
    Compiler compiler = new Compiler();
    CompilerPass pass = new InlineObjectLiterals(compiler, () -> "testVar");

    String source = "function f() {" +
        "  var obj = {" +
        "    a: 1," +
        "    b: 2," +
        "    c: function() { return 3; }" +
        "  };" +
        "  var x = obj.a;" +
        "  var y = obj.b;" +
        "  var z = obj.c();" +
        "  return x + y + z;" +
        "}";

    Node root = compiler.parseTree(source, null, null);
    pass.process(null, root);
    Node result = compiler.getOutput();

    String expected = "function f() {" +
        "  var a = 1;" +
        "  var b = 2;" +
        "  var c = function() { return 3; }" +
        "  var x = a;" +
        "  var y = b;" +
        "  var z = c();" +
        "  return x + y + z;" +
        "}" +
        "JSCompiler_object_inline_a_0 = 1;" +
        "JSCompiler_object_inline_b_0 = 2;" +
        "JSCompiler_object_inline_c_function_0 = function() { return 3; }";

    assertEquals(expected, compiler.getSourceString(result));
  }

  @Test
  public void testInlineObjectLiteralsWithSelfReference() {
    Compiler compiler = new Compiler();
    CompilerPass pass = new InlineObjectLiterals(compiler, () -> "testVar");

    String source = "function f() {" +
        "  var obj = {" +
        "    a: function() { return this; }," +
        "    b: 2" +
        "  };" +
        "  var x = obj.a();" +
        "  return x.b;" +
        "}";

    Node root = compiler.parseTree(source, null, null);
    pass.process(null, root);
    Node result = compiler.getOutput();

    String expected = "function f() {" +
        "  var a = function() { return this; }" +
        "  var b = 2;" +
        "  var a_prototype = { JSCompiler_object_inline_a_function_0: a }" +
        "  a = a_prototype.JSCompiler_object_inline_a_function_0;" +
        "  var x = a();" +
        "  return b;" +
        "}" +
        "JSCompiler_object_inline_a_function_0 = function() { return this; }";

    assertEquals(expected, compiler.getSourceString(result));
  }

  @Test
  public void testInlineObjectLiteralsWithEmptyObject() {
    Compiler compiler = new Compiler();
    CompilerPass pass = new InlineObjectLiterals(compiler, () -> "testVar");

    String source = "function f() {" +
        "  var obj = {}";

    Node root = compiler.parseTree(source, null, null);
    pass.process(null, root);
    Node result = compiler.getOutput();

    String expected = "function f() {" +
        "  var obj = {}" +
        "  JSCompiler_object_inline_obj_0 = {}";

    assertEquals(expected, compiler.getSourceString(result));
  }

  @Test
  public void testInlineObjectLiteralsWithUndefinedValues() {
    Compiler compiler = new Compiler();
    CompilerPass pass = new InlineObjectLiterals(compiler, () -> "testVar");

    String source = "function f() {" +
        "  var obj = {" +
        "    a: undefined," +
        "    b: 2" +
        "  }";

    Node root = compiler.parseTree(source, null, null);
    pass.process(null, root);
    Node result = compiler.getOutput();

    String expected = "function f() {" +
        "  var a = undefined;" +
        "  var b = 2" +
        "  JSCompiler_object_inline_obj_0 = { a: a }";

    assertEquals(expected, compiler.getSourceString(result));
  }

  @Test
  public void testInlineObjectLiteralsWithCyclicReference() {
    Compiler compiler = new Compiler();
    CompilerPass pass = new InlineObjectLiterals(compiler, () -> "testVar");

    String source = "function f() {" +
        "  var obj1 = {" +
        "    a: 1," +
        "    b: obj2" +
        "  };" +
        "  var obj2 = {" +
        "    a: obj1" +
        "  }";

    Node root = compiler.parseTree(source, null, null);
    pass.process(null, root);
    Node result = compiler.getOutput();

    // This test case is not expected to pass, as the InlineObjectLiterals pass
    // does not handle cyclic references.
    String expected = source;

    assertEquals(expected, compiler.getSourceString(result));
  }

  @Test
  public void testInlineObjectLiteralsWithEmptyFunction() {
    Compiler compiler = new Compiler();
    CompilerPass pass = new InlineObjectLiterals(compiler, () -> "testVar");

    String source = "function f() {" +
        "  var obj = {" +
        "    a: function() {}" +
        "  }";

    Node root = compiler.parseTree(source, null, null);
    pass.process(null, root);
    Node result = compiler.getOutput();

    String expected = "function f() {" +
        "  var a = function() {}" +
        "  JSCompiler_object_inline_obj_0 = { a: a }";

    assertEquals(expected, compiler.getSourceString(result));
  }
}