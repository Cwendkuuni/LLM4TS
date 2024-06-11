import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.NodeUtils;
import com.google.javascript.rhino.Node;

public class InlineObjectLiteralsTest {

  @Test
  public void testInlineSimpleObjectLiterals() {
    String jsCode = "function foo() {" +
        "  var obj = {" +
        "    name: 'John Doe'," +
        "    age: 30," +
        "    hobbies: ['reading', 'writing']" +
        "  };" +
        "  return obj.name + ' is ' + obj.age + ' years old.'";

    CompilerOptions options = CompilerOptions.defaultOptions();
    Compiler compiler = new Compiler();
    CompilerPass pass = new InlineObjectLiterals(compiler, () -> "inlineVarId");

    Node rootNode = NodeUtils.parseJsCode(jsCode, options, null);
    pass.process(null, rootNode);

    Node expected = NodeUtils.parseJsCode("" +
        "function foo() {" +
        "  var name = 'JSCompiler_object_inline_name_inlineVarId_';" +
        "  var age = 'JSCompiler_object_inline_age_inlineVarId_';" +
        "  var hobbiesLength = 'JSCompiler_object_inline_hobbies_inlineVarId_';" +
        "  var hobbies = [" +
        "    'JSCompiler_object_inline_hobbies_reading_inlineVarId_'," +
        "    'JSCompiler_object_inline_hobbies_writing_inlineVarId_'" +
        "  ];" +
        "  return name + ' is ' + age + ' years old.'",
        options, null);

    Node result = compiler.compileDeep(rootNode, options, null);
    assertEquals(expected.getText(), result.getText());
  }

  @Test
  public void testInlineNestedObjectLiterals() {
    String jsCode = "function foo() {" +
        "  var obj1 = {" +
        "    prop1: 1," +
        "    prop2: {" +
        "      subProp1: 'FOO'," +
        "      subProp2: [1, 2, 3]" +
        "    }" +
        "  };" +
        "  return obj1.prop2.subProp1 + ' ' + obj1.prop2.subProp2[0]";

    CompilerOptions options = CompilerOptions.defaultOptions();
    Compiler compiler = new Compiler();
    CompilerPass pass = new InlineObjectLiterals(compiler, () -> "inlineVarId");

    Node rootNode = NodeUtils.parseJsCode(jsCode, options, null);
    pass.process(null, rootNode);

    Node expected = NodeUtils.parseJsCode("" +
        "function foo() {" +
        "  var prop1 = 'JSCompiler_object_inline_prop1_inlineVarId_';" +
        "  var subProp1 = 'JSCompiler_object_inline_subProp1_inlineVarId_';" +
        "  var subProp2 = [" +
        "    'JSCompiler_object_inline_subProp2_0_inlineVarId_'," +
        "    'JSCompiler_object_inline_subProp2_1_inlineVarId_'," +
        "    'JSCompiler_object_inline_subProp2_2_inlineVarId_'" +
        "  ];" +
        "  return subProp1 + ' ' + subProp2[0]" +
        "}" +
        "",
        options, null);

    Node result = compiler.compileDeep(rootNode, options, null);
    assertEquals(expected.getText(), result.getText());
  }

  @Test
  public void testInlineEmptyObjectLiterals() {
    String jsCode = "function foo() {" +
        "  var obj = {};" +
        "  return 'Empty object literal inlined.'";

    CompilerOptions options = CompilerOptions.defaultOptions();
    Compiler compiler = new Compiler();
    CompilerPass pass = new InlineObjectLiterals(compiler, () -> "inlineVarId");

    Node rootNode = NodeUtils.parseJsCode(jsCode, options, null);
    pass.process(null, rootNode);

    Node expected = NodeUtils.parseJsCode("" +
        "function foo() {" +
        "  var inlineVarId = 'JSCompiler_object_inline_inlineVarId_';" +
        "  return 'Empty object literal inlined.'",
        options, null);

    Node result = compiler.compileDeep(rootNode, options, null);
    assertEquals(expected.getText(), result.getText());
  }

  @Test
  public void testInlineSelfReferentialObjectLiterals() {
    String jsCode = "function foo() {" +
        "  var obj = {" +
        "    a: {'b': obj.a}" +
        "  };" +
        "  return obj.a.b;";

    CompilerOptions options = CompilerOptions.defaultOptions();
    Compiler compiler = new Compiler();
    CompilerPass pass = new InlineObjectLiterals(compiler, () -> "inlineVarId");

    Node rootNode = NodeUtils.parseJsCode(jsCode, options, null);
    pass.process(null, rootNode);

    Node expected = NodeUtils.parseJsCode("" +
        "function foo() {" +
        "  var a = 'JSCompiler_object_inline_a_inlineVarId_';" +
        "  var b = 'JSCompiler_object_inline_b_inlineVarId_';" +
        "  return b;" +
        "}" +
        "",
        options, null);

    Node result = compiler.compileDeep(rootNode, options, null);
    assertEquals(expected.getText(), result.getText());
  }

  @Test(expected = AssertionError.class)
  public void testInlineCyclicObjectLiterals() {
    String jsCode = "function foo() {" +
        "  var obj1 = {" +
        "    a: {'b': obj2}" +
        "  };" +
        "  var obj2 = {" +
        "    a: obj1" +
        "  };" +
        "  return obj1.a.b;";

    CompilerOptions options = CompilerOptions.defaultOptions();
    Compiler compiler = new Compiler();
    CompilerPass pass = new InlineObjectLiterals(compiler, () -> "inlineVarId");

    Node rootNode = NodeUtils.parseJsCode(jsCode, options, null);
    pass.process(null, rootNode);

    Node expected = NodeUtils.parseJsCode("" +
        "function foo() {" +
        "  var a1 = 'JSCompiler_object_inline_a1_inlineVarId_';" +
        "  var b1 = 'JSCompiler_object_inline_b1_inlineVarId_';" +
        "  var a2 = 'JSCompiler_object_inline_a2_inlineVarId_';" +
        "  var b2 = 'JSCompiler_object_inline_b2_inlineVarId_';" +
        "  return b1;" +
        "}" +
        "",
        options, null);

    Node result = compiler.compileDeep(rootNode, options, null);
    assertEquals(expected.getText(), result.getText());
  }

  @Test
  public void testInlineObjectLiteralsWithExportedVariables() {
    String jsCode = "var myObject = {" +
        "  exportedProperty: 42" +
        "};" +
        "function foo() {" +
        "  var obj = {" +
        "    a: obj.exportedProperty" +
        "  };" +
        "  return obj.a;";

    CompilerOptions options = CompilerOptions.defaultOptions();
    Compiler compiler = new Compiler();
    CompilerPass pass = new InlineObjectLiterals(compiler, () -> "inlineVarId");

    Node rootNode = NodeUtils.parseJsCode(jsCode, options, null);
    pass.process(null, rootNode);

    Node expected = NodeUtils.parseJsCode("" +
        "var myObject = {" +
        "  exportedProperty: 42" +
        "};" +
        "function foo() {" +
        "  var exportedProperty = 'JSCompiler_object_inline_exportedProperty_inlineVarId_';" +
        "  var a = exportedProperty;" +
        "  return a;" +
        "}",
        options, null);

    Node result = compiler.compileDeep(rootNode, options, null);
    assertEquals(expected.getText(), result.getText());
  }

  @Test
  public void testInlineObjectLiteralsWithGlobalVariables() {
    String jsCode = "var globalVar = 42;" +
        "function foo() {" +
        "  var obj = {" +
        "    a: obj.globalVar" +
        "  };" +
        "  return obj.a;";

    CompilerOptions options = CompilerOptions.defaultOptions();
    Compiler compiler = new Compiler();
    CompilerPass pass = new InlineObjectLiterals(compiler, () -> "inlineVarId");

    Node rootNode = NodeUtils.parseJsCode(jsCode, options, null);
    pass.process(null, rootNode);

    Node expected = NodeUtils.parseJsCode("" +
        "var globalVar = 42;" +
        "function foo() {" +
        "  var a = globalVar;" +
        "  return a;" +
        "}",
        options, null);

    Node result = compiler.compileDeep(rootNode, options, null);
    assertEquals(expected.getText(), result.getText());
  }

  @Test
  public void testInlineObjectLiteralsWithStaleVariables() {
    String jsCode = "function foo() {" +
        "  var obj1 = {a: 1};" +
        "  var obj2 = {b: obj1};" +
        "  obj1.a = 2;" +
        "  return obj2.b;";

    CompilerOptions options = CompilerOptions.defaultOptions();
    Compiler compiler = new Compiler();
    CompilerPass pass = new InlineObjectLiterals(compiler, () -> "inlineVarId");

    Node rootNode = NodeUtils.parseJsCode(jsCode, options, null);
    pass.process(null, rootNode);

    Node expected = NodeUtils.parseJsCode("" +
        "function foo() {" +
        "  var a1 = 1;" +
        "  var b1 = a1;" +
        "  a1 = 2;" +
        "  return b1;" +
        "}",
        options, null);

    Node result = compiler.compileDeep(rootNode, options, null);
    assertEquals(expected.getText(), result.getText());
  }
}