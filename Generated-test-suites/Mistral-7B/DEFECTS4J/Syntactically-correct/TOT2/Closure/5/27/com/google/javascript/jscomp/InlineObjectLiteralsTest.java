import org.junit.Test;
import static org.junit.Assert.*;

import com.google.javascript.jscomp.InlineObjectLiterals;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.NodeFactory;

public class InlineObjectLiteralsTest {

  @Test
  public void testInlineSimpleObject() {
    AbstractCompiler compiler = new AbstractCompiler();
    Node root = NodeFactory.createText("function f() { var x = {a: 1}; x.a = 2; }");
    InlineObjectLiterals pass = new InlineObjectLiterals(compiler, () -> "");
    pass.process(null, root);
    compiler.runPasses(root);

    Node f = root.getFirstChild();
    assertEquals(
        "function f() {" +
            "var a = JSCompiler_object_inline_a_0;" +
            "x.a = 2;" +
            "}",
        f.toString());
  }

  @Test
  public void testInlineNestedObject() {
    AbstractCompiler compiler = new AbstractCompiler();
    Node root = NodeFactory.createText("function f() { var x = {a: {b: 1}}; x.a.b = 2; }");
    InlineObjectLiterals pass = new InlineObjectLiterals(compiler, () -> "");
    pass.process(null, root);
    compiler.runPasses(root);

    Node f = root.getFirstChild();
    assertEquals(
        "function f() {" +
            "var a = JSCompiler_object_inline_a_0_b_0;" +
            "x.a.b = 2;" +
            "}",
        f.toString());
  }

  @Test
  public void testInlineObjectWithFunctionProperty() {
    AbstractCompiler compiler = new AbstractCompiler();
    Node root = NodeFactory.createText("function f() { var x = {get a() { return 1; }}; }");
    InlineObjectLiterals pass = new InlineObjectLiterals(compiler, () -> "");
    pass.process(null, root);
    compiler.runPasses(root);

    Node f = root.getFirstChild();
    assertEquals(
        "function f() {" +
            "var get_a = JSCompiler_object_inline_a_get_0;" +
            "}",
        f.toString());
  }

  @Test
  public void testInlineObjectWithSelfAssignment() {
    AbstractCompiler compiler = new AbstractCompiler();
    Node root = NodeFactory.createText("function f() { var x = {x: x}; }");
    InlineObjectLiterals pass = new InlineObjectLiterals(compiler, () -> "");
    pass.process(null, root);
    compiler.runPasses(root);

    Node f = root.getFirstChild();
    assertEquals(
        "function f() {" +
            "var x = {" +
            "  x: null," +
            "};" +
            "}",
        f.toString());
  }

  @Test
  public void testInlineObjectWithMultipleAssignments() {
    AbstractCompiler compiler = new AbstractCompiler();
    Node root = NodeFactory.createText("function f() { var x = {a: 1, b: 1}; x.a = 2; x.b = 3; }");
    InlineObjectLiterals pass = new InlineObjectLiterals(compiler, () -> "");
    pass.process(null, root);
    compiler.runPasses(root);

    Node f = root.getFirstChild();
    assertEquals(
        "function f() {" +
            "var a = JSCompiler_object_inline_a_0;" +
            "var b = JSCompiler_object_inline_b_0;" +
            "x.a = 2;" +
            "x.b = 3;" +
            "}",
        f.toString());
  }

  @Test
  public void testInlineObjectWithEmptyProperties() {
    AbstractCompiler compiler = new AbstractCompiler();
    Node root = NodeFactory.createText("function f() { var x = {}; x.prop = 1; }");
    InlineObjectLiterals pass = new InlineObjectLiterals(compiler, () -> "");
    pass.process(null, root);
    compiler.runPasses(root);

    Node f = root.getFirstChild();
    assertEquals(
        "function f() {" +
            "var prop = JSCompiler_object_inline_prop_0;" +
            "x.prop = 1;" +
            "}",
        f.toString());
  }

  @Test
  public void testInlineObjectWithCircularReference() {
    AbstractCompiler compiler = new AbstractCompiler();
    Node root = NodeFactory.createText("function f() { var x = {a: {b: x}}; }");
    InlineObjectLiterals pass = new InlineObjectLiterals(compiler, () -> "");
    pass.process(null, root);
    compiler.runPasses(root);

    try {
      Node f = root.getFirstChild();
      fail("Expected error but no error was thrown.");
    } catch (Exception e) {
      // Expected
    }
  }

  @Test
  public void testInlineObjectWithCyclicReference() {
    AbstractCompiler compiler = new AbstractCompiler();
    Node root = NodeFactory.createText("function f() { var x = {a: 1, b: x}; }");
    InlineObjectLiterals pass = new InlineObjectLiterals(compiler, () -> "");
    pass.process(null, root);
    compiler.runPasses(root);

    try {
      Node f = root.getFirstChild();
      fail("Expected error but no error was thrown.");
    } catch (Exception e) {
      // Expected
    }
  }

  @Test
  public void testInlineObjectWithArrayProperty() {
    AbstractCompiler compiler = new AbstractCompiler();
    Node root = NodeFactory.createText("function f() { var x = {arr: [1, 2]}; }");
    InlineObjectLiterals pass = new InlineObjectLiterals(compiler, () -> "");
    pass.process(null, root);
    compiler.runPasses(root);

    Node f = root.getFirstChild();
    assertEquals(
        "function f() {" +
            "var arr = JSCompiler_object_inline_arr_0;" +
            "x.arr = [" +
            "  JSCompiler_object_inline_arr_0_0," +
            "  JSCompiler_object_inline_arr_0_1," +
            "];" +
            "}",
        f.toString());
  }

  @Test
  public void testInlineObjectWithNestedArrayProperty() {
    AbstractCompiler compiler = new AbstractCompiler();
    Node root = NodeFactory.createText("function f() { var x = {arr: [{b: 1}]}; }");
    InlineObjectLiterals pass = new InlineObjectLiterals(compiler, () -> "");
    pass.process(null, root);
    compiler.runPasses(root);

    Node f = root.getFirstChild();
    assertEquals(
        "function f() {" +
            "var arr = JSCompiler_object_inline_arr_0;" +
            "var b = JSCompiler_object_inline_arr_0_0_b_0;" +
            "x.arr = [" +
            "  {" +
            "    b: " + VAR_PREFIX + "b_0" +
            "  }" +
            "];" +
            "}",
        f.toString());
  }

  @Test
  public void testInlineObjectWithObjectProperties() {
    AbstractCompiler compiler = new AbstractCompiler();
    Node root = NodeFactory.createText("function f() { var x = {a: 1, b: {c: 2}}; }");
    InlineObjectLiterals pass = new InlineObjectLiterals(compiler, () -> "");
    pass.process(null, root);
    compiler.runPasses(root);

    Node f = root.getFirstChild();
    assertEquals(
        "function f() {" +
            "var a = JSCompiler_object_inline_a_0;" +
            "var c = JSCompiler_object_inline_b_c_0;" +
            "x.a = 1;" +
            "x.b = {" +
            "  c: " + VAR_PREFIX + "c_0" +
            "};" +
            "}",
        f.toString());
  }

  @Test
  public void testInlineObjectWithNestedObjectProperties() {
    AbstractCompiler compiler = new AbstractCompiler();
    Node root = NodeFactory.createText("function f() { var x = {a: {b: 1, c: {d: 2}}}; }");
    InlineObjectLiterals pass = new InlineObjectLiterals(compiler, () -> "");
    pass.process(null, root);
    compiler.runPasses(root);

    Node f = root.getFirstChild();
    assertEquals(
        "function f() {" +
            "var b = JSCompiler_object_inline_a_b_0;"
            + "var d = JSCompiler_object_inline_a_c_d_0;"
            + "x.a = {" +
            "  b: " + VAR_PREFIX + "b_0" +
            "  c: {" +
            "    d: " + VAR_PREFIX + "d_0" +
            "  }" +
            "};" +
            "}",
        f.toString());
  }
}