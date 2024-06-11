import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.InlineObjectLiterals;
import org.junit.Assert;
import org.junit.Test;

public class InlineObjectLiteralsTest {

  @Test
  public void testInlineObjectLiterals() {
    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler(options);
    CompilerPass pass = new InlineObjectLiterals(compiler, () -> "testVar");

    String sourceCode = "function foo() {" +
        "var obj1 = {" +
        "  a: 1," +
        "  b: function() { return 2; }," +
        "  c: 3," +
        "};" +
        "var obj2 = {" +
        "  a: 1," +
        "  b: 2," +
        "  c: function() { return 3; }," +
        "};" +
        "var obj3 = {" +
        "  a: 1," +
        "  b: 1," +
        "  c: 1," +
        "};" +
        "}" +
        "foo();";

    Node externs = null;
    Node root = NodePrinter.print(JavaScriptCoreFactory.getJsEngine().compile(sourceCode));

    pass.process(externs, root);

    String expectedOutput = "function foo() {" +
        "var a1, a2, a3, b1, b2, c1, c2;" +
        "a1 = 1;" +
        "b1 = function() { return 2; }" +
        "a2 = 1;" +
        "a3 = 1;" +
        "b2 = 2;" +
        "c1 = 3;" +
        "c2 = function() { return 3; }" +
        "}" +
        "foo();";

    String output = NodePrinter.print(root);

    Assert.assertEquals(expectedOutput, output);
  }

  @Test
  public void testInlineEmptyObject() {
    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler(options);
    CompilerPass pass = new InlineObjectLiterals(compiler, () -> "testVar");

    String sourceCode = "function foo() {" +
        "var obj = {};" +
        "}" +
        "foo();";

    Node externs = null;
    Node root = NodePrinter.print(JavaScriptCoreFactory.getJsEngine().compile(sourceCode));

    pass.process(externs, root);

    String expectedOutput = "function foo() {" +
        "var testVar;" +
        "}" +
        "foo();";

    String output = NodePrinter.print(root);

    Assert.assertEquals(expectedOutput, output);
  }

  @Test
  public void testInlineObjectWithSelfReference() {
    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler(options);
    CompilerPass pass = new InlineObjectLiterals(compiler, () -> "testVar");

    String sourceCode = "function foo() {" +
        "var obj = {" +
        "  a: function() { return this.a; }" +
        "};" +
        "}" +
        "foo();";

    Node externs = null;
    Node root = NodePrinter.print(JavaScriptCoreFactory.getJsEngine().compile(sourceCode));

    pass.process(externs, root);

    String expectedOutput = "function foo() {" +
        "var a, testVar;" +
        "a = function() { return a; }" +
        "}" +
        "foo();";

    String output = NodePrinter.print(root);

    Assert.assertEquals(expectedOutput, output);
  }

  @Test
  public void testInlineObjectWithUndefinedKey() {
    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler(options);
    CompilerPass pass = new InlineObjectLiterals(compiler, () -> "testVar");

    String sourceCode = "function foo() {" +
        "var obj = {" +
        "  a: 1," +
        "  b: undefined," +
        "  c: 3," +
        "};" +
        "}" +
        "foo();";

    Node externs = null;
    Node root = NodePrinter.print(JavaScriptCoreFactory.getJsEngine().compile(sourceCode));

    pass.process(externs, root);

    String expectedOutput = "function foo() {" +
        "var a, b, c, testVar;" +
        "}" +
        "foo();";

    String output = NodePrinter.print(root);

    Assert.assertEquals(expectedOutput, output);
  }

  @Test
  public void testInlineObjectWithCyclicReference() {
    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler(options);
    CompilerPass pass = new InlineObjectLiterals(compiler, () -> "testVar");

    String sourceCode = "function foo() {" +
        "var obj1 = {" +
        "  a: function() { return obj2; }," +
        "  b: 1," +
        "};" +
        "var obj2 = {" +
        "  a: function() { return obj1; }," +
        "  b: 1," +
        "};" +
        "}" +
        "foo();";

    Node externs = null;
    Node root = NodePrinter.print(JavaScriptCoreFactory.getJsEngine().compile(sourceCode));

    try {
      pass.process(externs, root);
      Assert.fail("Expected an error due to cyclic reference.");
    } catch (RuntimeException e) {
      // Expected
    }
  }

  @Test
  public void testInlineObjectWithFunctionAsValue() {
    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler(options);
    CompilerPass pass = new InlineObjectLiterals(compiler, () -> "testVar");

    String sourceCode = "function foo() {" +
        "var obj = {" +
        "  a: function() { return 1; }," +
        "  b: 1," +
        "};" +
        "}" +
        "foo();";

    Node externs = null;
    Node root = NodePrinter.print(JavaScriptCoreFactory.getJsEngine().compile(sourceCode));

    try {
      pass.process(externs, root);
      Assert.fail("Expected an error due to function as value.");
    } catch (RuntimeException e) {
      // Expected
    }
  }

  @Test
  public void testInlineObjectWithExportedVariable() {
    CompilerOptions options = new CompilerOptions();
    options.setExposeGlobals(true);
    Compiler compiler = new Compiler(options);
    CompilerPass pass = new InlineObjectLiterals(compiler, () -> "testVar");

    String sourceCode = "function foo() {" +
        "var obj = {" +
        "  a: 1," +
        "  b: 2," +
        "};" +
        "exports.obj = obj;" +
        "}" +
        "foo();";

    Node externs = NodePrinter.print(JavaScriptCoreFactory.getJsEngine().compile("exports = {}"));
    Node root = NodePrinter.print(JavaScriptCoreFactory.getJsEngine().compile(sourceCode));

    pass.process(externs, root);

    String expectedOutput = "function foo() {" +
        "var a, b, testVar;" +
        "}" +
        "foo();" +
        "exports.a = a;" +
        "exports.b = b;";

    String output = NodePrinter.print(root);

    Assert.assertEquals(expectedOutput, output);
  }

  @Test
  public void testInlineObjectWithExportedVariableAndFunction() {
    CompilerOptions options = new CompilerOptions();
    options.setExposeGlobals(true);
    Compiler compiler = new Compiler(options);
    CompilerPass pass = new InlineObjectLiterals(compiler, () -> "testVar");

    String sourceCode = "function foo() {" +
        "var obj = {" +
        "  a: 1," +
        "  b: function() { return 2; }," +
        "};" +
        "exports.obj = obj;" +
        "}" +
        "foo();";

    Node externs = NodePrinter.print(JavaScriptCoreFactory.getJsEngine().compile("exports = {}"));
    Node root = NodePrinter.print(JavaScriptCoreFactory.getJsEngine().compile(sourceCode));

    try {
      pass.process(externs, root);
      Assert.fail("Expected an error due to function as value.");
    } catch (RuntimeException e) {
      // Expected
    }
  }

  @Test
  public void testInlineObjectWithInlinedVariableAsValue() {
    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler(options);
    CompilerPass pass = new InlineObjectLiterals(compiler, () -> "testVar");

    String sourceCode = "function foo() {" +
        "var obj = {" +
        "  a: a," +
        "  b: 1," +
        "};" +
        "}" +
        "foo();";

    Node externs = null;
    Node root = NodePrinter.print(JavaScriptCoreFactory.getJsEngine().compile(sourceCode));

    pass.process(externs, root);

    String expectedOutput = "function foo() {" +
        "var a, testVar;" +
        "}" +
        "foo();" +
        "var obj = {" +
        "  a: a," +
        "  b: 1" +
        "};";

    String output = NodePrinter.print(root);

    Assert.assertEquals(expectedOutput, output);
  }

  @Test
  public void testInlineObjectWithSelfReferencingVariable() {
    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler(options);
    CompilerPass pass = new InlineObjectLiterals(compiler, () -> "testVar");

    String sourceCode = "function foo() {" +
        "var obj = {" +
        "  a: function() { return this.a; }," +
        "};" +
        "}" +
        "foo();";

    Node externs = null;
    Node root = NodePrinter.print(JavaScriptCoreFactory.getJsEngine().compile(sourceCode));

    try {
      pass.process(externs, root);
      Assert.fail("Expected an error due to self-referencing variable.");
    } catch (RuntimeException e) {
      // Expected
    }
  }
}