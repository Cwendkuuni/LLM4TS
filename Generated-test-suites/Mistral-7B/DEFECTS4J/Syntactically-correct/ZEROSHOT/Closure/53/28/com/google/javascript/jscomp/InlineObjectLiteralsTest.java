import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.ReferenceCollectingCallback;
import com.google.javascript.jscomp.Scope;

public class InlineObjectLiteralsTest {

  private Compiler compiler;
  private CompilerPass inlineObjectLiterals;

  @Before
  public void setup() {
    compiler = new Compiler();
    inlineObjectLiterals = new InlineObjectLiterals(compiler, () -> "test_variable");
  }

  @Test
  public void testInlineObjectLiterals() {
    String input = "function foo() {\n" +
        "  var obj = {\n" +
        "    a: 1,\n" +
        "    b: function() {}\n" +
        "  };\n" +
        "  var c = obj;\n" +
        "  obj.a = 2;\n" +
        "  obj.b();\n" +
        "}\n";

    Node root = NodePrinter.parseJs(input);
    inlineObjectLiterals.process(NodeUtil.EMPTY_NODE_MAP, root);

    Node expected = NodePrinter.parseJs("function foo() {\n" +
        "  var a = 1;\n" +
        "  var b = function() {}\n" +
        "  var c = a;\n" +
        "  a = 2;\n" +
        "  b();\n" +
        "}\n");

    assertEquals("Unexpected output", expected, root);
  }

  @Test
  public void testInlineObjectLiteralsWithInitialValue() {
    String input = "function foo() {\n" +
        "  var obj = {\n" +
        "    a: 1\n" +
        "  };\n" +
        "}\n";

    Node root = NodePrinter.parseJs(input);
    inlineObjectLiterals.process(NodeUtil.EMPTY_NODE_MAP, root);

    Node expected = NodePrinter.parseJs("function foo() {\n" +
        "  var a = 1;\n" +
        "}\n");

    assertEquals("Unexpected output", expected, root);
  }

  @Test
  public void testInlineObjectLiteralsWithCycle() {
    String input = "function foo() {\n" +
        "  var obj = {\n" +
        "    a: {\n" +
        "      b: obj\n" +
        "    }\n" +
        "  };\n" +
        "}\n";

    Node root = NodePrinter.parseJs(input);
    inlineObjectLiterals.process(NodeUtil.EMPTY_NODE_MAP, root);

    String expected = "function foo() {\n" +
        "  var a = {\n" +
        "    b: null\n" +
        "  };\n" +
        "}\n";

    // Since the input is invalid, the output should also be invalid.
    Node output = root;
    while (output.getFirstChild() != null) {
      output = output.getFirstChild();
    }
    assertEquals("Unexpected output", expected, output.toString());
  }

  @Test
  public void testInlineObjectLiteralsWithNestedObjects() {
    String input = "function foo() {\n" +
        "  var obj = {\n" +
        "    a: 1,\n" +
        "    b: {\n" +
        "      c: 2,\n" +
        "      d: function() {}\n" +
        "    }\n" +
        "  };\n" +
        "  var e = obj.b.c;\n" +
        "  obj.b.d();\n" +
        "}\n";

    Node root = NodePrinter.parseJs(input);
    inlineObjectLiterals.process(NodeUtil.EMPTY_NODE_MAP, root);

    String expected = "function foo() {\n" +
        "  var a = 1;\n" +
        "  var b = {\n" +
        "    c: 2,\n" +
        "    d: function() {}\n" +
        "  };\n" +
        "  var e = b.c;\n" +
        "}\n";

    Node output = root;
    while (output.getFirstChild() != null) {
      output = output.getFirstChild();
    }

    assertEquals("Unexpected output", expected, output.toString());
  }

  @Test
  public void testInlineObjectLiteralsWithFunctionProperties() {
    String input = "function foo() {\n" +
        "  var obj = {\n" +
        "    a: function() {}\n" +
        "  };\n" +
        "  obj.a();\n" +
        "}\n";

    Node root = NodePrinter.parseJs(input);
    inlineObjectLiterals.process(NodeUtil.EMPTY_NODE_MAP, root);

    String expected = "function foo() {\n" +
        "  var a = function() {}\n" +
        "  a();\n" +
        "}\n";

    Node output = root;
    while (output.getFirstChild() != null) {
      output = output.getFirstChild();
    }

    assertEquals("Unexpected output", expected, output.toString());
  }

  @Test
  public void testInlineObjectLiteralsWithGetters() {
    String input = "function foo() {\n" +
        "  var obj = {\n" +
        "    get a() {\n" +
        "      return 1;\n" +
        "    }\n" +
        "  };\n" +
        "  console.log(obj.a);\n" +
        "}\n";

    Node root = NodePrinter.parseJs(input);
    inlineObjectLiterals.process(NodeUtil.EMPTY_NODE_MAP, root);

    String expected = "function foo() {\n" +
        "  var a = 1;\n" +
        "  console.log(a);\n" +
        "}\n";

    Node output = root;
    while (output.getFirstChild() != null) {
      output = output.getFirstChild();
    }

    assertEquals("Unexpected output", expected, output.toString());
  }

  @Test
  public void testInlineObjectLiteralsWithSetters() {
    String input = "function foo() {\n" +
        "  var obj = {\n" +
        "    set a(val) {\n" +
        "      this.b = val;\n" +
        "    }\n" +
        "  };\n" +
        "  obj.a = 1;\n" +
        "}\n";

    Node root = NodePrinter.parseJs(input);
    inlineObjectLiterals.process(NodeUtil.EMPTY_NODE_MAP, root);

    String expected = "function foo() {\n" +
        "  var b = 1;\n" +
        "}\n";

    Node output = root;
    while (output.getFirstChild() != null) {
      output = output.getFirstChild();
    }

    assertEquals("Unexpected output", expected, output.toString());
  }

  @Test
  public void testInlineObjectLiteralsWithFunctionPropertiesAndSetters() {
    String input = "function foo() {\n" +
        "  var obj = {\n" +
        "    get a() {\n" +
        "      return function() {}\n" +
        "    },\n" +
        "    set a(val) {\n" +
        "      this.b = val;\n" +
        "    }\n" +
        "  };\n" +
        "  obj.a();\n" +
        "  obj.a = 1;\n" +
        "}\n";

    Node root = NodePrinter.parseJs(input);
    inlineObjectLiterals.process(NodeUtil.EMPTY_NODE_MAP, root);

    String expected = "function foo() {\n" +
        "  var a = function() {}\n" +
        "  a();\n" +
        "  var b = 1;\n" +
        "}\n";

    Node output = root;
    while (output.getFirstChild() != null) {
      output = output.getFirstChild();
    }

    assertEquals("Unexpected output", expected, output.toString());
  }
}