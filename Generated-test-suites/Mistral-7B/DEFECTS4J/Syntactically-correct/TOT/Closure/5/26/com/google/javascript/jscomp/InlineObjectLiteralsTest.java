import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.InlineObjectLiterals;

public class InlineObjectLiteralsTest {

  @Test
  public void testInlineSimpleObject() {
    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler(options);
    compiler.addPass(new CompilerPass() {
      @Override
      public void process(Node externs, Node root) {
        InlineObjectLiterals inlineObjectLiterals =
            new InlineObjectLiterals(compiler, InlineObjectLiterals::newNameIdSupplier);
        inlineObjectLiterals.process(externs, root);
      }
    });

    String jsCode = "function foo() {\n" +
        "  var obj = {\n" +
        "    a: 1,\n" +
        "    b: 2,\n" +
        "  };\n" +
        "  return obj.a;\n" +
        "}";

    Node root = AbstractCompiler.parse(jsCode, null);
    Node result = compiler.compile(root, null, null);

    String expectedCode = "function foo() {\n" +
        "  var a = JSCompiler_object_inline_a_;\n" +
        "  var b = JSCompiler_object_inline_b_;\n" +
        "  return a;\n" +
        "}";

    assertEquals(expectedCode, result.toString());
  }

  @Test
  public void testInlineComplexObject() {
    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler(options);
    compiler.addPass(new CompilerPass() {
      @Override
      public void process(Node externs, Node root) {
        InlineObjectLiterals inlineObjectLiterals =
            new InlineObjectLiterals(compiler, InlineObjectLiterals::newNameIdSupplier);
        inlineObjectLiterals.process(externs, root);
      }
    });

    String jsCode = "function foo() {\n" +
        "  var obj = {\n" +
        "    a: {\n" +
        "      b: 1,\n" +
        "      c: {\n" +
        "        d: 2,\n" +
        "        e: 3,\n" +
        "      },\n" +
        "      f: function() {}\n" +
        "    },\n" +
        "    g: [1, 2],\n" +
        "    h: null\n" +
        "  };\n" +
        "  return obj.a.c.d;\n" +
        "}";

    Node root = AbstractCompiler.parse(jsCode, null);
    Node result = compiler.compile(root, null, null);

    String expectedCode = "function foo() {\n" +
        "  var a = JSCompiler_object_inline_a_;\n" +
        "  var b = JSCompiler_object_inline_b_;\n" +
        "  var c = JSCompiler_object_inline_c_;\n" +
        "  var d = JSCompiler_object_inline_d_;\n" +
        "  var e = JSCompiler_object_inline_e_;\n" +
        "  return d;\n" +
        "}";

    assertEquals(expectedCode, result.toString());
  }

  @Test
  public void testInlineObjectWithFunctionProperty() {
    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler(options);
    compiler.addPass(new CompilerPass() {
      @Override
      public void process(Node externs, Node root) {
        InlineObjectLiterals inlineObjectLiterals =
            new InlineObjectLiterals(compiler, InlineObjectLiterals::newNameIdSupplier);
        inlineObjectLiterals.process(externs, root);
      }
    });

    String jsCode = "function foo() {\n" +
        "  var obj = {\n" +
        "    a: function() {},\n" +
        "    b: 1\n" +
        "  };\n" +
        "  return obj.a();\n" +
        "}";

    Node root = AbstractCompiler.parse(jsCode, null);
    Node result = compiler.compile(root, null, null);

    String expectedCode = "function foo() {\n" +
        "  var a = JSCompiler_object_inline_a_;\n" +
        "  return a();\n" +
        "}";

    assertEquals(expectedCode, result.toString());
  }

  @Test
  public void testInlineObjectWithGlobalVariable() {
    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler(options);
    compiler.addPass(new CompilerPass() {
      @Override
      public void process(Node externs, Node root) {
        InlineObjectLiterals inlineObjectLiterals =
            new InlineObjectLiterals(compiler, InlineObjectLiterals::newNameIdSupplier);
        inlineObjectLiterals.process(externs, root);
      }
    });

    String jsCode = "var a = 1;\n" +
        "function foo() {\n" +
        "  var obj = {\n" +
        "    a: a,\n" +
        "  };\n" +
        "  return obj.a;\n" +
        "}";

    Node root = AbstractCompiler.parse(jsCode, null);
    Node result = compiler.compile(root, null, null);

    // Expected code will not change since 'a' is a global variable
    String expectedCode = "function foo() {\n" +
        "  return a;\n" +
        "}";

    assertEquals(expectedCode, result.toString());
  }

  @Test
  public void testInlineObjectWithUndefinedProperty() {
    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler(options);
    compiler.addPass(new CompilerPass() {
      @Override
      public void process(Node externs, Node root) {
        InlineObjectLiterals inlineObjectLiterals =
            new InlineObjectLiterals(compiler, InlineObjectLiterals::newNameIdSupplier);
        inlineObjectLiterals.process(externs, root);
      }
    });

    String jsCode = "function foo() {\n" +
        "  var obj = {\n" +
        "    a: {}\n" +
        "  };\n" +
        "  return obj.b;\n" +
        "}";

    Node root = AbstractCompiler.parse(jsCode, null);
    Node result = compiler.compile(root, null, null);

    String expectedCode = "function foo() {\n" +
        "  return undefined;\n" +
        "}";

    assertEquals(expectedCode, result.toString());
  }

  private static String newNameIdSupplier() {
    return "_inlineObjectLiteralsTest_" + System.currentTimeMillis();
  }
}