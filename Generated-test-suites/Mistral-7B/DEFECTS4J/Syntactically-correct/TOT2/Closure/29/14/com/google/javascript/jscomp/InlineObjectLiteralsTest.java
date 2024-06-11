import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.ReferenceCollectingCallback;
import com.google.javascript.rhino.NodeFactory;

@RunWith(JUnit4.class)
public class InlineObjectLiteralsTest {

  @Test
  public void testInlineObjectLiterals() {
    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler(options);

    String input = "function foo() {" +
        "  var obj = {a: 1, b: 2, c: {d: 3, e: 4}};" +
        "  return obj.c.e;" +
        "}";

    Node root = NodeFactory.compile(input);

    CompilerPass inlineObjectLiterals = new InlineObjectLiterals(compiler, NodeFactory::newSupplier);
    inlineObjectLiterals.process(null, root);

    Node expectedOutput = NodeFactory.compile("function foo() {" +
        "  var a = JSCompiler_object_inline_d;" +
        "  var e = JSCompiler_object_inline_e;" +
        "  return e;" +
        "}");

    Node output = compiler.getCompiledSource(root);
    assertEquals(expectedOutput.toSource(), output.toSource());

    // Test for edge case: empty object literal
    input = "function foo() {" +
        "  var obj = {};" +
        "  return obj.a;" +
        "}";

    root = NodeFactory.compile(input);
    inlineObjectLiterals.process(null, root);

    expectedOutput = NodeFactory.compile("function foo() {" +
        "  var obj_inline_ = {};" +
        "  return undefined;" +
        "}");

    output = compiler.getCompiledSource(root);
    assertEquals(expectedOutput.toSource(), output.toSource());

    // Test for error scenario: circular reference
    input = "function foo() {" +
        "  var obj = {a: {b: obj}};" +
        "  return obj.b.a;" +
        "}";

    root = NodeFactory.compile(input);
    inlineObjectLiterals.process(null, root);

    // The compiler should not be able to inline the object in this case,
    // and should therefore throw a compilation error.
    assertNotNull(compiler.getErrors().getErrorList().get(0).getMessage());
  }

  @Test
  public void testInlineObjectLiteralsWithFunctionProperties() {
    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler(options);

    String input = "function foo() {" +
        "  var obj = {" +
        "    a: function() {" +
        "      return 1;" +
        "    }," +
        "    b: 2" +
        "  }" +
        "  return obj.a();" +
        "}";

    Node root = NodeFactory.compile(input);

    CompilerPass inlineObjectLiterals = new InlineObjectLiterals(compiler, NodeFactory::newSupplier);
    inlineObjectLiterals.process(null, root);

    Node expectedOutput = NodeFactory.compile("function foo() {" +
        "  var a = JSCompiler_object_inline_a;" +
        "  return a();" +
        "}");

    Node output = compiler.getCompiledSource(root);
    assertEquals(expectedOutput.toSource(), output.toSource());
  }

  @Test
  public void testInlineObjectLiteralsWithNestedObjects() {
    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler(options);

    String input = "function foo() {" +
        "  var obj = {" +
        "    a: {b: 1, c: 2}," +
        "    d: 3" +
        "  }" +
        "  return obj.a.b;" +
        "}";

    Node root = NodeFactory.compile(input);

    CompilerPass inlineObjectLiterals = new InlineObjectLiterals(compiler, NodeFactory::newSupplier);
    inlineObjectLiterals.process(null, root);

    Node expectedOutput = NodeFactory.compile("function foo() {" +
        "  var b = JSCompiler_object_inline_b;" +
        "  var c = JSCompiler_object_inline_c;" +
        "  return b;" +
        "}");

    Node output = compiler.getCompiledSource(root);
    assertEquals(expectedOutput.toSource(), output.toSource());
  }

  @Test
  public void testInlineObjectLiteralsWithComputedProperties() {
    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler(options);

    String input = "function foo() {" +
        "  var obj = {" +
        "    ['a' + 'b']: 1" +
        "  }" +
        "  return obj['a' + 'b'];" +
        "}";

    Node root = NodeFactory.compile(input);

    CompilerPass inlineObjectLiterals = new InlineObjectLiterals(compiler, NodeFactory::newSupplier);
    inlineObjectLiterals.process(null, root);

    Node expectedOutput = NodeFactory.compile("function foo() {" +
        "  var ab = JSCompiler_object_inline_ab;" +
        "  return ab;" +
        "}");

    Node output = compiler.getCompiledSource(root);
    assertEquals(expectedOutput.toSource(), output.toSource());
  }

  @Test
  public void testInlineObjectLiteralsWithSpreadOperator() {
    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler(options);

    String input = "function foo() {" +
        "  var obj1 = {a: 1};" +
        "  var obj2 = {b: 2};" +
        "  var obj3 = {...obj1, ...obj2};" +
        "  return obj3.a;" +
        "}";

    Node root = NodeFactory.compile(input);

    CompilerPass inlineObjectLiterals = new InlineObjectLiterals(compiler, NodeFactory::newSupplier);
    inlineObjectLiterals.process(null, root);

    Node expectedOutput = NodeFactory.compile("function foo() {" +
        "  var a = JSCompiler_object_inline_a;" +
        "  return a;" +
        "}");

    Node output = compiler.getCompiledSource(root);
    assertEquals(expectedOutput.toSource(), output.toSource());
  }

  @Test
  public void testInlineObjectLiteralsWithPrototypeProperties() {
    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler(options);

    String input = "function Foo() {}" +
        "Foo.prototype.a = {b: 1};" +
        "function foo() {" +
        "  var obj = new Foo();" +
        "  return obj.a.b;" +
        "}";

    Node root = NodeFactory.compile(input);

    CompilerPass inlineObjectLiterals = new InlineObjectLiterals(compiler, NodeFactory::newSupplier);
    inlineObjectLiterals.process(null, root);

    Node expectedOutput = NodeFactory.compile("function Foo() {}" +
        "Foo.prototype.a = JSCompiler_object_inline_b;" +
        "function foo() {" +
        "  return Foo.prototype.a.b;" +
        "}");

    Node output = compiler.getCompiledSource(root);
    assertEquals(expectedOutput.toSource(), output.toSource());
  }

  @Test(expected = RuntimeException.class)
  public void testInlineObjectLiteralsWithInvalidObjectLiteral() {
    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler(options);

    String input = "function foo() {" +
        "  var obj = {a: 1, b: 2, c: {d: [3]}};" +
        "  return obj.c[0];" +
        "}";

    Node root = NodeFactory.compile(input);

    CompilerPass inlineObjectLiterals = new InlineObjectLiterals(compiler, NodeFactory::newSupplier);
    inlineObjectLiterals.process(null, root);

    // This should throw a compilation error since the object literal
    // contains an array, which cannot be inlined as a whole.
  }

  @Test
  public void testInlineObjectLiteralsWithGlobalVariables() {
    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler(options);

    String input = "var obj = {a: 1};" +
        "function foo() {" +
        "  return obj.a;" +
        "}";

    Node root = NodeFactory.compile(input);

    CompilerPass inlineObjectLiterals = new InlineObjectLiterals(compiler, NodeFactory::newSupplier);
    inlineObjectLiterals.process(null, root);

    Node expectedOutput = NodeFactory.compile("var obj_inline_ = {" +
        "  a: 1" +
        "};" +
        "function foo() {" +
        "  return obj_inline_.a;" +
        "}");

    Node output = compiler.getCompiledSource(root);
    assertEquals(expectedOutput.toSource(), output.toSource());
  }
}