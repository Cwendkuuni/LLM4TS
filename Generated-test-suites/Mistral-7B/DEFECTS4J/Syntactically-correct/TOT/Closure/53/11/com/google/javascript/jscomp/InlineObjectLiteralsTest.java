import org.junit.Test;
import static org.junit.Assert.*;
import static com.google.javascript.jscomp.InlineObjectLiterals.*;

public class InlineObjectLiteralsTest {

  @Test
  public void testInlineSimpleObjectLiteral() {
    AbstractCompiler compiler = new Compiler();
    AbstractCompiler.Options options = new AbstractCompiler.Options();
    InlineObjectLiterals pass = new InlineObjectLiterals(compiler, () -> "t");
    Node root = NodePrinter.printToNode("var obj = { a: 1, b: 2 };");
    pass.process(NodeOps.EMPTY_NODE, root);
    String result = NodePrinter.printToString(compiler.getCompiledCode());
    assertEquals("var a = t, b = t;var obj = { a: a, b: b };", result);
  }

  @Test
  public void testInlineObjectLiteralWithFunctionExpression() {
    AbstractCompiler compiler = new Compiler();
    AbstractCompiler.Options options = new AbstractCompiler.Options();
    InlineObjectLiterals pass = new InlineObjectLiterals(compiler, () -> "t");
    Node root = NodePrinter.printToNode("var obj = { a: function() { return 1; } };");
    pass.process(NodeOps.EMPTY_NODE, root);
    String result = NodePrinter.printToString(compiler.getCompiledCode());
    assertEquals("var a = t;var obj = { a: a };function() { return 1; }", result);
  }

  @Test
  public void testInlineObjectLiteralWithCircularReference() {
    AbstractCompiler compiler = new Compiler();
    AbstractCompiler.Options options = new AbstractCompiler.Options();
    InlineObjectLiterals pass = new InlineObjectLiterals(compiler, () -> "t");

    Node root = NodePrinter.printToNode("var obj1 = { a: obj2 };var obj2 = { a: obj1 };");
    pass.process(NodeOps.EMPTY_NODE, root);

    try {
      String result = NodePrinter.printToString(compiler.getCompiledCode());
      fail("Should not be able to inline object literals with circular references.");
    } catch (RuntimeException e) {
      // Expected
    }
  }

  @Test
  public void testInlineObjectLiteralWithSelfReference() {
    AbstractCompiler compiler = new Compiler();
    AbstractCompiler.Options options = new AbstractCompiler.Options();
    InlineObjectLiterals pass = new InlineObjectLiterals(compiler, () -> "t");

    Node root = NodePrinter.printToNode("var obj = { a: obj };");
    pass.process(NodeOps.EMPTY_NODE, root);

    try {
      String result = NodePrinter.printToString(compiler.getCompiledCode());
      fail("Should not be able to inline object literals with self-references.");
    } catch (RuntimeException e) {
      // Expected
    }
  }

  @Test
  public void testInlineObjectLiteralWithGetter() {
    AbstractCompiler compiler = new Compiler();
    AbstractCompiler.Options options = new AbstractCompiler.Options();
    InlineObjectLiterals pass = new InlineObjectLiterals(compiler, () -> "t");

    Node root = NodePrinter.printToNode("var obj = { get a() { return 1; } };");
    pass.process(NodeOps.EMPTY_NODE, root);

    try {
      String result = NodePrinter.printToString(compiler.getCompiledCode());
      fail("Should not be able to inline object literals with getters.");
    } catch (RuntimeException e) {
      // Expected
    }
  }

  @Test
  public void testInlineObjectLiteralWithSetter() {
    AbstractCompiler compiler = new Compiler();
    AbstractCompiler.Options options = new AbstractCompiler.Options();
    InlineObjectLiterals pass = new InlineObjectLiterals(compiler, () -> "t");

    Node root = NodePrinter.printToNode("var obj = { set a(val) { } };");
    pass.process(NodeOps.EMPTY_NODE, root);

    try {
      String result = NodePrinter.printToString(compiler.getCompiledCode());
      fail("Should not be able to inline object literals with setters.");
    } catch (RuntimeException e) {
      // Expected
    }
  }

  @Test
  public void testInlineObjectLiteralWithFalsePositive() {
    AbstractCompiler compiler = new Compiler();
    AbstractCompiler.Options options = new AbstractCompiler.Options();
    InlineObjectLiterals pass = new InlineObjectLiterals(compiler, () -> "t");

    Node root = NodePrinter.printToNode("var obj = { a: 1 };var x = obj.a;");
    pass.process(NodeOps.EMPTY_NODE, root);

    String result = NodePrinter.printToString(compiler.getCompiledCode());
    assertEquals("var a = t;var obj = { a: a };var x = a;", result);
  }

  @Test
  public void testInlineObjectLiteralWithTruePositive() {
    AbstractCompiler compiler = new Compiler();
    AbstractCompiler.Options options = new AbstractCompiler.Options();
    InlineObjectLiterals pass = new InlineObjectLiterals(compiler, () -> "t");

    Node root = NodePrinter.printToNode("var obj = { a: 1 }.");
    pass.process(NodeOps.EMPTY_NODE, root);

    String result = NodePrinter.printToString(compiler.getCompiledCode());
    assertEquals("var a = t;var obj = { a: a };", result);
  }

  @Test
  public void testInlineObjectLiteralWithComplexObject() {
    AbstractCompiler compiler = new Compiler();
    AbstractCompiler.Options options = new AbstractCompiler.Options();
    InlineObjectLiterals pass = new InlineObjectLiterals(compiler, () -> "t");

    Node root = NodePrinter.printToNode("var obj = { a: { b: 1 }, c: 2 };");
    pass.process(NodeOps.EMPTY_NODE, root);

    String result = NodePrinter.printToString(compiler.getCompiledCode());
    assertEquals("var a = t, b = t;var obj = { a: a, c: c };", result);
  }
}