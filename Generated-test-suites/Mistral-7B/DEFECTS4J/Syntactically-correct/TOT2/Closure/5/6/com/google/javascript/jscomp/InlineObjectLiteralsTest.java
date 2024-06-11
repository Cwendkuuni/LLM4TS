import static org.junit.Assert.assertThat;
import static com.google.common.truth.Truth.*;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.InlineObjectLiterals;
import com.google.javascript.jscomp.ReferenceCollectingCallback;
import com.google.javascript.rhino.Node;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class InlineObjectLiteralsTest {

  @Test
  public void testInlineObjectLiterals() {
    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler(options);

    String input = "function test() {" +
        "  var obj = {" +
        "    a: 1," +
        "    b: {'foo': 2}," +
        "    c: function() {}.bind(null)," +
        "    d: [1, 2]," +
        "    e: function() {}, e2: function() {}, e3: function() {}" +
        "  };" +
        "  return obj.a + obj.b.foo + obj.c() + obj.d[0] + obj.e() + obj.e2() + obj.e3();" +
        "}";

    Node root = NodePrinter.printToAst(input).getRootNode();

    CompilerPass inlineObjectLiteralsPass = new InlineObjectLiterals(compiler, new IdentityNameIdSupplier());
    inlineObjectLiteralsPass.process(null, root);

    Node expectedOutput = NodePrinter.printToAst("function test() {" +
        "  var a = 1;" +
        "  var b_foo = 2;" +
        "  var c_fn = function() {}.bind(null);" +
        "  var d_0 = 1;" +
        "  var e_fn = function() {}" +
        "  var e2_fn = function() {}" +
        "  var e3_fn = function() {}" +
        "  return a + b_foo + c_fn() + d_0 + e_fn() + e2_fn() + e3_fn();" +
        "}").getRootNode();

    Node actualOutput = NodeUtil.getChildByDeepName(root, "test");

    NodeUtil.assertNodeTreeEquals(expectedOutput, actualOutput);
  }

  @Test
  public void testInlineObjectLiteralsWithSelfReferentialAssignment() {
    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler(options);

    String input = "function test() {" +
        "  var obj = {" +
        "    a: {b: obj}" +
        "  };" +
        "  return obj.a.b;" +
        "}";

    Node root = NodePrinter.printToAst(input).getRootNode();

    CompilerPass inlineObjectLiteralsPass = new InlineObjectLiterals(compiler, new IdentityNameIdSupplier());
    inlineObjectLiteralsPass.process(null, root);

    // The self-referential assignment should result in an error and not be inlined.
    Node actualOutput = NodeUtil.getChildByDeepName(root, "test");
    Node expectedOutput = actualOutput;

    compiler.reportCodeChange();

    List<String> errors = compiler.getErrors();
    assertEquals(1, errors.size());
    String errorMessage = errors.get(0);
    assertTrue(errorMessage.contains("Self-referential assignment"));

    NodeUtil.assertNodeTreeEquals(expectedOutput, actualOutput);
  }

  @Test
  public void testInlineObjectLiteralsWithDeletedProperty() {
    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler(options);

    String input = "function test() {" +
        "  delete this.obj.a;" +
        "  return this.obj.b;" +
        "}";

    Node root = NodePrinter.printToAst(input).getRootNode();

    CompilerPass inlineObjectLiteralsPass = new InlineObjectLiterals(compiler, new IdentityNameIdSupplier());
    inlineObjectLiteralsPass.process(null, root);

    // The deleted property should not be inlined.
    Node actualOutput = NodeUtil.getChildByDeepName(root, "test");
    Node expectedOutput = actualOutput;

    compiler.reportCodeChange();

    List<String> errors = compiler.getErrors();
    assertEquals(0, errors.size());

    NodeUtil.assertNodeTreeEquals(expectedOutput, actualOutput);
  }

  @Test
  public void testInlineObjectLiteralsWithNestedObjectLiterals() {
    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler(options);

    String input = "function test() {" +
        "  var obj = {" +
        "    a: 1," +
        "    b: {" +
        "      c: 2," +
        "      d: 3" +
        "    }" +
        "  };" +
        "  return obj.a + obj.b.c + obj.b.d;" +
        "}";

    Node root = NodePrinter.printToAst(input).getRootNode();

    CompilerPass inlineObjectLiteralsPass = new InlineObjectLiterals(compiler, new IdentityNameIdSupplier());
    inlineObjectLiteralsPass.process(null, root);

    Node expectedOutput = NodePrinter.printToAst("function test() {" +
        "  var a = 1;" +
        "  var b_c = 2;" +
        "  var b_d = 3;" +
        "  return a + b_c + b_d;" +
        "}").getRootNode();

    Node actualOutput = NodeUtil.getChildByDeepName(root, "test");

    NodeUtil.assertNodeTreeEquals(expectedOutput, actualOutput);
  }

  @Test
  public void testInlineObjectLiteralsWithFunctionProperties() {
    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler(options);

    String input = "function test() {" +
        "  var obj = {" +
        "    getA: function() { return 1; }, " +
        "    getB: function() { return 2; }" +
        "  };" +
        "  return obj.getA() + obj.getB();" +
        "}";

    Node root = NodePrinter.printToAst(input).getRootNode();

    CompilerPass inlineObjectLiteralsPass = new InlineObjectLiterals(compiler, new IdentityNameIdSupplier());
    inlineObjectLiteralsPass.process(null, root);

    Node expectedOutput = NodePrinter.printToAst("function test() {" +
        "  function getA() { return 1; }" +
        "  function getB() { return 2; }" +
        "  return getA() + getB();" +
        "}").getRootNode();

    Node actualOutput = NodeUtil.getChildByDeepName(root, "test");

    NodeUtil.assertNodeTreeEquals(expectedOutput, actualOutput);
  }

  @Test
  public void testInlineObjectLiteralsWithArrayProperty() {
    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler(options);

    String input = "function test() {" +
        "  var obj = {" +
        "    a: [1, 2]" +
        "  };" +
        "  return obj.a[0] + obj.a[1];" +
        "}";

    Node root = NodePrinter.printToAst(input).getRootNode();

    CompilerPass inlineObjectLiteralsPass = new InlineObjectLiterals(compiler, new IdentityNameIdSupplier());
    inlineObjectLiteralsPass.process(null, root);

    Node expectedOutput = NodePrinter.printToAst("function test() {" +
        "  var a_0 = 1;" +
        "  var a_1 = 2;" +
        "  return a_0 + a_1;" +
        "}").getRootNode();

    Node actualOutput = NodeUtil.getChildByDeepName(root, "test");

    NodeUtil.assertNodeTreeEquals(expectedOutput, actualOutput);
  }

  @Test
  public void testInlineObjectLiteralsWithNestedArrayProperty() {
    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler(options);

    String input = "function test() {" +
        "  var obj = {" +
        "    a: [1, {" +
        "      b: 2" +
        "    }]" +
        "  };" +
        "  return obj.a[0] + obj.a[1].b;" +
        "}";

    Node root = NodePrinter.printToAst(input).getRootNode();

    CompilerPass inlineObjectLiteralsPass = new InlineObjectLiterals(compiler, new IdentityNameIdSupplier());
    inlineObjectLiteralsPass.process(null, root);

    Node expectedOutput = NodePrinter.printToAst("function test() {" +
        "  var a_0 = 1;" +
        "  var a_1_b = 2;" +
        "  return a_0 + a_1_b;" +
        "}").getRootNode();

    Node actualOutput = NodeUtil.getChildByDeepName(root, "test");

    NodeUtil.assertNodeTreeEquals(expectedOutput, actualOutput);
  }

  @Test
  public void testInlineObjectLiteralsWithSimpleAssignment() {
    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler(options);

    String input = "function test() {" +
        "  var obj = {" +
        "    a: 1" +
        "  };" +
        "  var a = obj.a;" +
        "  return a;" +
        "}";

    Node root = NodePrinter.printToAst(input).getRootNode();

    CompilerPass inlineObjectLiteralsPass = new InlineObjectLiterals(compiler, new IdentityNameIdSupplier());
    inlineObjectLiteralsPass.process(null, root);

    Node expectedOutput = NodePrinter.printToAst("function test() {" +
        "  var a = 1;" +
        "  return a;" +
        "}").getRootNode();

    Node actualOutput = NodeUtil.getChildByDeepName(root, "test");

    NodeUtil.assertNodeTreeEquals(expectedOutput, actualOutput);
  }

  @Test
  public void testInlineObjectLiteralsWithNestedAssignments() {
    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler(options);

    String input = "function test() {" +
        "  var obj = {" +
        "    a: 1," +
        "    b: {" +
        "      c: 2," +
        "      d: 3" +
        "    }" +
        "  };" +
        "  var a = obj.a;" +
        "  var c = obj.b.c;" +
        "  obj.b.d = 4;" +
        "  return a + c + obj.b.d;" +
        "}";

    Node root = NodePrinter.printToAst(input).getRootNode();

    CompilerPass inlineObjectLiteralsPass = new InlineObjectLiterals(compiler, new IdentityNameIdSupplier());
    inlineObjectLiteralsPass.process(null, root);

    Node expectedOutput = NodePrinter.printToAst("function test() {" +
        "  var a = 1;" +
        "  var c = 2;" +
        "  var d = 4;" +
        "  return a + c + d;" +
        "}").getRootNode();

    Node actualOutput = NodeUtil.getChildByDeepName(root, "test");

    NodeUtil.assertNodeTreeEquals(expectedOutput, actualOutput);
  }

  @Test
  public void testInlineObjectLiteralsWithRecursiveObjectLiterals() {
    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler(options);

    String input = "function test() {" +
        "  var obj = {" +
        "    a: 1," +
        "    b: {" +
        "      c: obj" +
        "    }" +
        "  };" +
        "  return obj.b.c.a;" +
        "}";

    Node root = NodePrinter.printToAst(input).getRootNode();

    CompilerPass inlineObjectLiteralsPass = new InlineObjectLiterals(compiler, new IdentityNameIdSupplier());
    inlineObjectLiteralsPass.process(null, root);

    // The recursive object literal should not be inlined due to potential infinite recursion.
    Node actualOutput = NodeUtil.getChildByDeepName(root, "test");
    Node expectedOutput = actualOutput;

    compiler.reportCodeChange();

    List<String> errors = compiler.getErrors();
    assertEquals(1, errors.size());
    String errorMessage = errors.get(0);
    assertTrue(errorMessage.contains("Recursive object literal"));

    NodeUtil.assertNodeTreeEquals(expectedOutput, actualOutput);
  }
}

class IdentityNameIdSupplier implements Supplier<String> {
  private int counter = 0;

  @Override
  public String get() {
    return "temp_" + counter++;
  }
}