import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.InlineObjectLiterals;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.assertEquals;

@RunWith(JUnit4.class)
public class InlineObjectLiteralsTest {

  @Test
  public void testInlineObjectLiterals() {
    String input = "function foo() {\n" +
        "  var obj = {\n" +
        "    a: 1,\n" +
        "    b: {\n" +
        "      c: 2,\n" +
        "      d: 3\n" +
        "    },\n" +
        "    e: function() {}\n" +
        "  };\n" +
        "  var x = obj.b.c;\n" +
        "  var y = obj.e;\n" +
        "}\n";

    Compiler compiler = new Compiler();
    CompilerPass pass = new InlineObjectLiterals(compiler, () -> "temp");
    compiler.addPass(pass);
    Node root = new Node(Token.SCRIPT, null);
    Node externs = new Node(Token.SCRIPT, null);
    pass.process(externs, root);
    compiler.compile(input, root, externs);

    String expectedOutput = "function foo() {\n" +
        "  var a = 1;\n" +
        "  var c = 2;\n" +
        "  var d = 3;\n" +
        "  var e = function() {}\n" +
        "  var x = c;\n" +
        "  var y = e;\n" +
        "}\n";

    assertEquals(expectedOutput, compiler.getSource());
  }

  @Test
  public void testInlineObjectLiteralsWithSelfReference() {
    String input = "function foo() {\n" +
        "  var obj = {\n" +
        "    a: 1,\n" +
        "    b: {\n" +
        "      c: function() { return obj.b; },\n" +
        "      d: 2\n" +
        "    },\n" +
        "    e: 3\n" +
        "  };\n" +
        "}\n";

    Compiler compiler = new Compiler();
    CompilerPass pass = new InlineObjectLiterals(compiler, () -> "temp");
    compiler.addPass(pass);
    Node root = new Node(Token.SCRIPT, null);
    Node externs = new Node(Token.SCRIPT, null);
    pass.process(externs, root);
    compiler.compile(input, root, externs);

    String expectedOutput = "function foo() {\n" +
        "  var a = 1;\n" +
        "  var c = function() { return c; };\n" +
        "  var d = 2;\n" +
        "  var e = 3;\n" +
        "}\n";

    assertEquals(expectedOutput, compiler.getSource());
  }

  @Test
  public void testInlineObjectLiteralsWithRecursiveObject() {
    String input = "function foo() {\n" +
        "  var obj = {\n" +
        "    a: 1,\n" +
        "    b: {\n" +
        "      c: obj\n" +
        "    },\n" +
        "    d: 2\n" +
        "  };\n" +
        "}\n";

    Compiler compiler = new Compiler();
    CompilerPass pass = new InlineObjectLiterals(compiler, () -> "temp");
    compiler.addPass(pass);
    Node root = new Node(Token.SCRIPT, null);
    Node externs = new Node(Token.SCRIPT, null);
    pass.process(externs, root);
    compiler.compile(input, root, externs);

    String expectedOutput = "function foo() {\n" +
        "  var a = 1;\n" +
        "  var obj = {\n" +
        "    c: obj,\n" +
        "    d: 2\n" +
        "  };\n" +
        "  var c = obj.c;\n" +
        "}\n";

    assertEquals(expectedOutput, compiler.getSource());
  }

  @Test
  public void testInlineObjectLiteralsWithNestedObject() {
    String input = "function foo() {\n" +
        "  var obj = {\n" +
        "    a: 1,\n" +
        "    b: {\n" +
        "      c: 2,\n" +
        "      d: {\n" +
        "        e: 3,\n" +
        "        f: 4\n" +
        "      }\n" +
        "    },\n" +
        "    e: 5\n" +
        "  };\n" +
        "  var x = obj.b.d.e;\n" +
        "  var y = obj.e;\n" +
        "}\n";

    Compiler compiler = new Compiler();
    CompilerPass pass = new InlineObjectLiterals(compiler, () -> "temp");
    compiler.addPass(pass);
    Node root = new Node(Token.SCRIPT, null);
    Node externs = new Node(Token.SCRIPT, null);
    pass.process(externs, root);
    compiler.compile(input, root, externs);

    String expectedOutput = "function foo() {\n" +
        "  var a = 1;\n" +
        "  var c = 2;\n" +
        "  var e = 3;\n" +
        "  var f = 4;\n" +
        "  var x = c;\n" +
        "  var y = e;\n" +
        "}\n";

    assertEquals(expectedOutput, compiler.getSource());
  }

  @Test
  public void testInlineObjectLiteralsWithExportedVariable() {
    String input = "function foo() {\n" +
        "  export let obj = {\n" +
        "    a: 1,\n" +
        "    b: 2,\n" +
        "    c: function() {}\n" +
        "  };\n" +
        "  var x = obj.b;\n" +
        "}\n";

    Compiler compiler = new Compiler();
    CompilerPass pass = new InlineObjectLiterals(compiler, () -> "temp");
    compiler.addPass(pass);
    Node root = new Node(Token.SCRIPT, null);
    Node externs = new Node(Token.SCRIPT, null);
    pass.process(externs, root);
    compiler.compile(input, root, externs);

    String expectedOutput = "function foo() {\n" +
        "  export let a = 1;\n" +
        "  export let b = 2;\n" +
        "  export let c = function() {}\n" +
        "  var x = b;\n" +
        "}\n";

    assertEquals(expectedOutput, compiler.getSource());
  }

  @Test
  public void testInlineObjectLiteralsWithExternVariable() {
    String input = "function foo() {\n" +
        "  export let obj = {\n" +
        "    a: 1,\n" +
        "    b: 2,\n" +
        "    c: function() {}\n" +
        "  };\n" +
        "  var x = obj.b;\n" +
        "  export let myObj = obj;\n" +
        "}\n";

    Compiler compiler = new Compiler();
    CompilerPass pass = new InlineObjectLiterals(compiler, () -> "temp");
    compiler.addPass(pass);
    Node root = new Node(Token.SCRIPT, null);
    Node externs = new Node(Token.SCRIPT, null);
    pass.process(externs, root);
    compiler.compile(input, root, externs);

    String expectedOutput = "function foo() {\n" +
        "  export let a = 1;\n" +
        "  export let b = 2;\n" +
        "  export let c = function() {}\n" +
        "  export let myObj = null;\n" +
        "  var x = b;\n" +
        "}\n";

    assertEquals(expectedOutput, compiler.getSource());
  }

  @Test
  public void testInlineObjectLiteralsWithVarInForLoop() {
    String input = "function foo() {\n" +
        "  for (var i = 0; i < 10; i++) {\n" +
        "    var obj = {\n" +
        "      a: i,\n" +
        "      b: i + 1\n" +
        "    };\n" +
        "    console.log(obj.a + obj.b);\n" +
        "  }\n" +
        "}\n";

    Compiler compiler = new Compiler();
    CompilerPass pass = new InlineObjectLiterals(compiler, () -> "temp");
    compiler.addPass(pass);
    Node root = new Node(Token.SCRIPT, null);
    Node externs = new Node(Token.SCRIPT, null);
    pass.process(externs, root);
    compiler.compile(input, root, externs);

    String expectedOutput = "function foo() {\n" +
        "  for (var i = 0; i < 10; i++) {\n" +
        "    var a = i;\n" +
        "    var b = i + 1;\n" +
        "    console.log(a + b);\n" +
        "  }\n" +
        "}\n";

    assertEquals(expectedOutput, compiler.getSource());
  }

  @Test
  public void testInlineObjectLiteralsWithSelfInObjectLiteral() {
    String input = "function foo() {\n" +
        "  var obj = {\n" +
        "    a: function() { return this; },\n" +
        "    b: 2\n" +
        "  };\n" +
        "  var x = obj.a();\n" +
        "  console.log(x === obj); // Should print true\n" +
        "}\n";

    Compiler compiler = new Compiler();
    CompilerPass pass = new InlineObjectLiterals(compiler, () -> "temp");
    compiler.addPass(pass);
    Node root = new Node(Token.SCRIPT, null);
    Node externs = new Node(Token.SCRIPT, null);
    pass.process(externs, root);
    compiler.compile(input, root, externs);

    String expectedOutput = "function foo() {\n" +
        "  var a = function() { return this; };\n" +
        "  var b = 2;\n" +
        "  var obj = {\n" +
        "    a: a,\n" +
        "    b: b\n" +
        "  };\n" +
        "  var x = a();\n" +
        "  console.log(x === obj); // Should print true\n" +
        "}\n";

    assertEquals(expectedOutput, compiler.getSource());
  }

  @Test
  public void testInlineObjectLiteralsWithGlobalVariable() {
    String input = "function foo() {\n" +
        "  var obj = {\n" +
        "    a: 1,\n" +
        "    b: {\n" +
        "      c: 2\n" +
        "    }\n" +
        "  };\n" +
        "  globalVar = obj.b.c;\n" +
        "}\n";

    Compiler compiler = new Compiler();
    CompilerPass pass = new InlineObjectLiterals(compiler, () -> "temp");
    compiler.addPass(pass);
    Node root = new Node(Token.SCRIPT, null);
    Node externs = new Node(Token.SCRIPT, null);
    pass.process(externs, root);
    compiler.compile(input, root, externs);

    String expectedOutput = "function foo() {\n" +
        "  var a = 1;\n" +
        "  var c = 2;\n" +
        "  globalVar = c;\n" +
        "}\n";

    assertEquals(expectedOutput, compiler.getSource());
  }
}