import org.junit.Test;
import static org.junit.Assert.*;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.InlineObjectLiterals;
import com.google.javascript.rhino.Node;

public class InlineObjectLiteralsTest {

  @Test
  public void testInlineSimpleObject() {
    String jsCode = "var obj = {a: 1, b: 2};";

    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();

    CompilerPass pass = new InlineObjectLiterals(compiler, () -> null);
    Node root = Node.parse(jsCode);
    pass.process(null, root);

    Node expected = Node.objectLiteral();
    expected.addProperty("a", Node.numberLiteral(1));
    expected.addProperty("b", Node.numberLiteral(2));

    Node actual = root;
    while (actual.isObjectLit()) {
      expected.copyInformationFromForTree(actual);
      actual = actual.getFirstChild();
    }

    assertEquals("Expected inline object literal to be expanded correctly",
        expected, root);
  }

  @Test
  public void testInlineEmptyObject() {
    String jsCode = "var obj = {};";

    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();

    CompilerPass pass = new InlineObjectLiterals(compiler, () -> null);
    Node root = Node.parse(jsCode);
    pass.process(null, root);

    Node expected = Node.objectLiteral();

    Node actual = root;
    while (actual.isObjectLit()) {
      expected.copyInformationFromForTree(actual);
      actual = actual.getFirstChild();
    }

    Node expectedVar = Node.newVar("obj");
    expectedVar.copyInformationFromForTree(expected);

    assertEquals("Expected empty object literal to be expanded correctly",
        expectedVar, root);
  }

  @Test
  public void testInlineComplexObject() {
    String jsCode = "var obj = {\n" +
        "  a: 1,\n" +
        "  b: {\n" +
        "    c: 3,\n" +
        "    d: {\n" +
        "      e: 5,\n" +
        "      f: '\u0061'\n" +
        "    }\n" +
        "  }\n" +
        "};";

    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();

    CompilerPass pass = new InlineObjectLiterals(compiler, () -> null);
    Node root = Node.parse(jsCode);
    pass.process(null, root);

    Node expected = Node.objectLiteral();
    expected.addProperty("a", Node.numberLiteral(1));

    Node subObj = Node.objectLiteral();
    expected.addProperty("b", subObj);

    subObj.addProperty("c", Node.numberLiteral(3));

    Node subSubObj = Node.objectLiteral();
    subObj.addProperty("d", subSubObj);

    subSubObj.addProperty("e", Node.numberLiteral(5));
    subSubObj.addProperty("f", Node.stringLiteral("\u0061"));

    assertEquals("Expected complex object literal to be expanded correctly",
        expected, root);
  }

  @Test(expected = AssertionError.class)
  public void testInlineObjectWithSideEffect() {
    String jsCode = "var obj = {\n" +
        "  x: function() { return 42; },\n" +
        "  y: function() { this.x(); }\n" +
        "};\n" +
        "var result = obj.y();";

    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();

    CompilerPass pass = new InlineObjectLiterals(compiler, () -> null);
    Node root = Node.parse(jsCode);
    pass.process(null, root);

    Node expected = Node.call(Node.name("InlineObjectLiterals"), "inlineObject", root);
    Node expectedResult = Node.numberLiteral(42);

    Node actual = Node.parse(compiler.compile(root).getSource());
    Node actualResult = Node.getFirstChild(Node.getLastChild(actual));

    assertEquals("Expected the result of the inlined object literal to be correct",
        expectedResult, actualResult);
  }

  @Test
  public void testInlineObjectWithCyclicReference() {
    String jsCode = "var obj = {\n" +
        "  a: 1,\n" +
        "  b: obj\n" +
        "};";

    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();

    CompilerPass pass = new InlineObjectLiterals(compiler, () -> null);
    Node root = Node.parse(jsCode);
    try {
      pass.process(null, root);
      fail("Expected an error when processing a cyclic object reference.");
    } catch (RuntimeException e) {
      // Expected
    }
  }

  @Test
  public void testInlineObjectWithNonObjectValue() {
    String jsCode = "var obj = {\n" +
        "  a: 1,\n" +
        "  b: 2\n" +
        "};\n" +
        "obj.c = 3;";

    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();

    CompilerPass pass = new InlineObjectLiterals(compiler, () -> null);
    Node root = Node.parse(jsCode);
    pass.process(null, root);

    Node expected = Node.objectLiteral();
    expected.addProperty("a", Node.numberLiteral(1));
    expected.addProperty("b", Node.numberLiteral(2));

    Node actual = root;
    while (actual.isObjectLit()) {
      expected.copyInformationFromForTree(actual);
      actual = actual.getFirstChild();
    }

    Node actualVar = Node.newVar("obj");
    expected.copyInformationFromForTree(actualVar);
    actualVar.addChild(Node.numberLiteral(3));

    assertEquals("Expected object literal with non-object value to be expanded correctly",
        expected, actualVar);
  }

  @Test
  public void testInlineObjectWithFunctionValue() {
    String jsCode = "var obj = {\n" +
        "  a: function() {},\n" +
        "  b: function() {}\n" +
        "};";

    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();

    CompilerPass pass = new InlineObjectLiterals(compiler, () -> null);
    Node root = Node.parse(jsCode);
    pass.process(null, root);

    Node expected = Node.objectLiteral();
    expected.addProperty("a", Node.newFunction(null, null, null));
    expected.addProperty("b", Node.newFunction(null, null, null));

    Node actual = root;
    while (actual.isObjectLit()) {
      expected.copyInformationFromForTree(actual);
      actual = actual.getFirstChild();
    }

    Node actualVar = Node.newVar("obj");
    expected.copyInformationFromForTree(actualVar);

    assertEquals("Expected object literal with function value to be expanded correctly",
        expected, actualVar);
  }

  @Test
  public void testInlineNestedObjectWithFunctionValue() {
    String jsCode = "var obj = {\n" +
        "  a: function() {}\n" +
        ",\n" +
        "  b: {\n" +
        "    c: function() {}\n" +
        "  }\n" +
        "};";

    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();

    CompilerPass pass = new InlineObjectLiterals(compiler, () -> null);
    Node root = Node.parse(jsCode);
    pass.process(null, root);

    Node expected = Node.objectLiteral();
    expected.addProperty("a", Node.newFunction(null, null, null));

    Node subObj = Node.objectLiteral();
    expected.addProperty("b", subObj);

    Node subSubObj = Node.objectLiteral();
    subObj.addProperty("c", subSubObj);
    subSubObj.addProperty("value", Node.newFunction(null, null, null));

    assertEquals("Expected nested object literal with function value to be expanded correctly",
        expected, root);
  }
}