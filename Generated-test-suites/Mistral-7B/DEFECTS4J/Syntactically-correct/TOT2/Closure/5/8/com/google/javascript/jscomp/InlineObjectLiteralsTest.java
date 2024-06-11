import org.junit.Test;
import static org.junit.Assert.*;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.InlineObjectLiterals;
import com.google.javascript.jscomp.ReferenceCollectingCallback.Behavior;
import com.google.javascript.rhino.Node;

public class InlineObjectLiteralsTest {

  @Test
  public void testInlineSimpleObjectLiteral() {
    String code = "function f() {" +
                  "  var obj = {a: 1, b: 2};" +
                  "  return obj.a + obj.b;" +
                  "}";

    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler(options);
    CompilerPass pass = new InlineObjectLiterals(compiler, () -> "test_");

    Node root = NodePrinter.parseJs(code);
    pass.process(null, root);
    String result = NodePrinter.printNode(compiler.getProgram());

    assertEquals("function f() {" +
                 "  var a = JSCompiler_object_inline_a_test_;\n" +
                 "  var b = JSCompiler_object_inline_b_test_;\n" +
                 "  return a + b;" +
                 "}", result);
  }

  @Test
  public void testInlineComplexObjectLiteral() {
    String code = "function f() {" +
                  "  var obj = {" +
                  "    a: {x: 1, y: 2}," +
                  "    b: function() {return 3;}" +
                  "  };" +
                  "  return obj.a.x + obj.a.y + obj.b();" +
                  "}";

    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler(options);
    CompilerPass pass = new InlineObjectLiterals(compiler, () -> "test_");

    Node root = NodePrinter.parseJs(code);
    pass.process(null, root);
    String result = NodePrinter.printNode(compiler.getProgram());

    assertEquals("function f() {" +
                 "  var x = JSCompiler_object_inline_a_x_test_;\n" +
                 "  var y = JSCompiler_object_inline_a_y_test_;\n" +
                 "  var b = function() {return 3;};\n" +
                 "  return x + y + b.call(this);" +
                 "}", result);
  }

  @Test
  public void testInlineEmptyObjectLiteral() {
    String code = "function f() {" +
                  "  var obj = {};" +
                  "  return obj;" +
                  "}";

    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler(options);
    CompilerPass pass = new InlineObjectLiterals(compiler, () -> "test_");

    Node root = NodePrinter.parseJs(code);
    pass.process(null, root);
    String result = NodePrinter.printNode(compiler.getProgram());

    assertEquals("function f() {" +
                 "  return {};" +
                 "}", result);
  }

  @Test
  public void testInlineObjectWithSelfReference() {
    String code = "function f() {" +
                  "  var obj = {" +
                  "    a: function() {return this.a;}," +
                  "    b: 1" +
                  "  };" +
                  "  return obj.a + obj.b;" +
                  "}";

    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler(options);
    CompilerPass pass = new InlineObjectLiterals(compiler, () -> "test_");

    Node root = NodePrinter.parseJs(code);
    pass.process(null, root);
    String result = NodePrinter.printNode(compiler.getProgram());

    // This test case fails as the InlineObjectLiterals pass doesn't support self-referential object literals
    // In order to handle this case, you should modify the InlineObjectLiterals class to support it
    fail("Test failed, self-referential object literals are not supported");
  }

  @Test
  public void testInlineObjectWithRecursiveProperty() {
    String code = "function f() {" +
                  "  var obj = {" +
                  "    a: {b: function() {return this.a.a.a;}}," +
                  "  };" +
                  "  return obj.a.b();" +
                  "}";

    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler(options);
    CompilerPass pass = new InlineObjectLiterals(compiler, () -> "test_");

    Node root = NodePrinter.parseJs(code);
    pass.process(null, root);
    String result = NodePrinter.printNode(compiler.getProgram());

    // This test case fails as the InlineObjectLiterals pass doesn't support recursive properties
    // In order to handle this case, you should modify the InlineObjectLiterals class to support it
    fail("Test failed, recursive properties are not supported");
  }

  @Test
  public void testInlineObjectWithNestedObject() {
    String code = "function f() {" +
                  "  var obj = {" +
                  "    a: {" +
                  "      x: 1," +
                  "      y: 2" +
                  "    }," +
                  "    b: 3" +
                  "  };" +
                  "  return obj.a.x + obj.b;" +
                  "}";

    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler(options);
    CompilerPass pass = new InlineObjectLiterals(compiler, () -> "test_");

    Node root = NodePrinter.parseJs(code);
    pass.process(null, root);
    String result = NodePrinter.printNode(compiler.getProgram());

    assertEquals("function f() {" +
                 "  var x = JSCompiler_object_inline_a_x_test_;\n" +
                 "  var b = 3;\n" +
                 "  return x + b;" +
                 "}", result);
  }

  @Test
  public void testInlineObjectWithNestedObjectAndFunction() {
    String code = "function f() {" +
                  "  var obj = {" +
                  "    a: {" +
                  "      x: 1," +
                  "      y: {" +
                  "        fn: function() {return 4;}" +
                  "      }" +
                  "    }," +
                  "    b: 3" +
                  "  };" +
                  "  return obj.a.x + obj.a.y.fn();" +
                  "}";

    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler(options);
    CompilerPass pass = new InlineObjectLiterals(compiler, () -> "test_");

    Node root = NodePrinter.parseJs(code);
    pass.process(null, root);
    String result = NodePrinter.printNode(compiler.getProgram());

    assertEquals("function f() {" +
                 "  var x = JSCompiler_object_inline_a_x_test_;\n" +
                 "  var fn = JSCompiler_object_inline_a_y_fn_test_;\n" +
                 "  return x + fn.call(this);" +
                 "}", result);
  }
}