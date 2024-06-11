import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerPassCallback;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.InliningBehavior;
import com.google.javascript.jscomp.InlineObjectLiterals;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class InlineObjectLiteralsTest {

  private Compiler compiler;
  private CompilerOptions options;

  @Before
  public void setup() {
    options = new CompilerOptions();
    compiler = new Compiler(options);
  }

  @Test
  public void testInlineObjectLiterals() {
    String jsCode = "function foo() {\n" +
        "  var obj = {\n" +
        "    prop1: 1,\n" +
        "    prop2: 2,\n" +
        "    prop3: function() {}\n" +
        "  };\n" +
        "  return obj.prop1 + obj.prop2;\n" +
        "}";

    Node externalSourcesNode = Node.objectLiteral();
    externalSourcesNode.setProperty("foo", Node.name("foo"));
    Node rootNode = Node.script(jsCode);

    InlineObjectLiterals pass = new InlineObjectLiterals(compiler, Node::newStringSupplier);
    CompilerPassCallback callback = new CompilerPassCallback();
    pass.process(externalSourcesNode, rootNode);
    callback.checkError();

    String expectedResult = "function foo() {\n" +
        "  var prop1 = JSCompiler_object_inline_prop1_0,\n" +
        "      prop2 = JSCompiler_object_inline_prop2_1;\n" +
        "  return prop1 + prop2;\n" +
        "}";

    assertEquals(expectedResult, compiler.toSource(rootNode));
  }

  @Test(expected = AssertionError.class)
  public void testInlineObjectLiteralsWithCircularReference() {
    String jsCode = "function foo() {\n" +
        "  var obj1 = {\n" +
        "    prop1: obj2,\n" +
        "  };\n" +
        "  var obj2 = {\n" +
        "    prop1: obj1,\n" +
        "  };\n" +
        "  return obj1.prop1;\n" +
        "}";

    Node externalSourcesNode = Node.objectLiteral();
    externalSourcesNode.setProperty("foo", Node.name("foo"));
    Node rootNode = Node.script(jsCode);

    InlineObjectLiterals pass = new InlineObjectLiterals(compiler, Node::newStringSupplier);
    CompilerPassCallback callback = new CompilerPassCallback();
    pass.process(externalSourcesNode, rootNode);
    callback.checkError();
  }

  @Test
  public void testInlineObjectLiteralsWithSelfReference() {
    String jsCode = "function foo() {\n" +
        "  var obj = {\n" +
        "    prop: function() { return obj; },\n" +
        "  };\n" +
        "  return obj.prop();\n" +
        "}";

    Node externalSourcesNode = Node.objectLiteral();
    externalSourcesNode.setProperty("foo", Node.name("foo"));
    Node rootNode = Node.script(jsCode);

    InlineObjectLiterals pass = new InlineObjectLiterals(compiler, Node::newStringSupplier);
    CompilerPassCallback callback = new CompilerPassCallback();
    pass.process(externalSourcesNode, rootNode);
    callback.checkError();

    String expectedResult = "function foo() {\n" +
        "  var prop = JSCompiler_object_inline_prop_0;\n" +
        "  return prop.call(prop);\n" +
        "}";

    assertEquals(expectedResult, compiler.toSource(rootNode));
  }

  @Test
  public void testInlineObjectLiteralsWithExportedVariable() {
    String jsCode = "var exported = {\n" +
        "  prop: 1,\n" +
        "};\n" +
        "function foo() {\n" +
        "  return exported.prop;\n" +
        "}";

    Node externalSourcesNode = Node.objectLiteral();
    externalSourcesNode.setProperty("exported", Node.objectLiteral().setProperty("prop", Node.number(1)));
    Node rootNode = Node.script(jsCode);

    InlineObjectLiterals pass = new InlineObjectLiterals(compiler, Node::newStringSupplier);
    CompilerPassCallback callback = new CompilerPassCallback();
    pass.process(externalSourcesNode, rootNode);
    callback.checkError();

    String expectedResult = "var exported = JSCompiler_object_inline_exported_0;\n" +
        "function foo() {\n" +
        "  return exported.prop;\n" +
        "}";

    assertEquals(expectedResult, compiler.toSource(rootNode));
  }

  @Test
  public void testInlineObjectLiteralsWithUndefinedProperty() {
    String jsCode = "function foo() {\n" +
        "  return undefined.prop;\n" + // this should be an error
        "}";

    Node externalSourcesNode = Node.script(jsCode);

    InlineObjectLiterals pass = new InlineObjectLiterals(compiler, Node::newStringSupplier);
    CompilerPassCallback callback = new CompilerPassCallback();
    pass.process(externalSourcesNode, Node.nullNode());
    callback.checkError("ReferenceError: undefined is not an object");
  }

  private static Supplier<String> newStringSupplier() {
    return Node::newStringSupplier;
  }
}