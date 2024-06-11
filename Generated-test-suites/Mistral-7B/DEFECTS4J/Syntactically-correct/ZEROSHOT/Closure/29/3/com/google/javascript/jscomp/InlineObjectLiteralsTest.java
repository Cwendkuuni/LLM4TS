import org.junit.Test;
import static org.junit.Assert.*;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.rhino.Node;

public class InlineObjectLiteralsTest {

  @Test
  public void testInlineObjectLiterals() {
    String code = "function f() {" +
        "  var obj = {" +
        "    a: 1," +
        "    b: 2," +
        "    c: 3" +
        "  };" +
        "  var x = obj.a;" +
        "  var y = obj.b;" +
        "  var z = obj.c;" +
        "  obj = null;" +
        "  return x + y + z;" +
        "}";

    Compiler compiler = new Compiler();
    CompilerPass pass = new InlineObjectLiterals(compiler, () -> null);
    Node root = parser(code);
    pass.process(null, root);
    Node result = compiler.getOutput();

    String expectedCode = "function f() {" +
        "  var a, b, c, x, y, z;" +
        "  a = 1;" +
        "  b = 2;" +
        "  c = 3;" +
        "  x = a;" +
        "  y = b;" +
        "  z = c;" +
        "  obj = null;" +
        "  return x + y + z;" +
        "}";

    assertEquals(expectedCode, printer(result));
  }

  @Test
  public void testInlineNestedObjectLiterals() {
    String code = "function f() {" +
        "  var obj = {" +
        "    a: {" +
        "      b: 1," +
        "      c: 2" +
        "    }," +
        "    d: 3" +
        "  };" +
        "  var x = obj.a.b;" +
        "  var y = obj.a.c;" +
        "  obj = null;" +
        "  return x + y + obj.d;" +
        "}";

    Compiler compiler = new Compiler();
    CompilerPass pass = new InlineObjectLiterals(compiler, () -> null);
    Node root = parser(code);
    pass.process(null, root);
    Node result = compiler.getOutput();

    String expectedCode = "function f() {" +
        "  var a_b, a_c, d, x, y, obj;" +
        "  a_b = 1;" +
        "  a_c = 2;" +
        "  d = 3;" +
        "  x = a_b;" +
        "  y = a_c;" +
        "  obj = null;" +
        "  return x + y + obj.d;" +
        "}";

    assertEquals(expectedCode, printer(result));
  }

  @Test
  public void testInlineObjectLiteralsWithProperties() {
    String code = "function f() {" +
        "  var obj = {" +
        "    a: 1," +
        "    get b() { return 2; }," +
        "    set b(x) { }" +
        "  };" +
        "  obj.b = 3;" +
        "  return obj.a + obj.b;" +
        "}";

    Compiler compiler = new Compiler();
    CompilerPass pass = new InlineObjectLiterals(compiler, () -> null);
    Node root = parser(code);
    pass.process(null, root);
    Node result = compiler.getOutput();

    String expectedCode = "function f() {" +
        "  var a, b, obj;" +
        "  a = 1;" +
        "  b = 3;" +
        "  obj = null;" +
        "  return a + b;" +
        "}";

    assertEquals(expectedCode, printer(result));
  }

  @Test
  public void testInlineObjectLitteralsWithCyclicReference() {
    String code = "function f() {" +
        "  var obj1 = { a: { b: obj1 }};" +
        "  return obj1.a.b;" +
        "}";

    Compiler compiler = new Compiler();
    CompilerPass pass = new InlineObjectLiterals(compiler, () -> null);
    Node root = parser(code);
    try {
      pass.process(null, root);
      fail("Expected an exception.");
    } catch (IllegalStateException e) {
      // Expected
    }
  }

  private static Node parser(String code) {
    Parser parser = new Parser();
    return parser.parse(new CharSequenceInput(code), null);
  }

  private static String printer(Node node) {
    PrettyPrinter prettyPrinter = new PrettyPrinter();
    return prettyPrinter.printNode(node);
  }
}