import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.InlineObjectLiterals;
import org.junit.Before;
import org.junit.Test;

import static com.google.common.truth.Truth.assertThat;

public class InlineObjectLiteralsTest {

  Compiler compiler;
  CompilerPass inlineObjectLiterals;

  @Before
  public void setUp() {
    compiler = new Compiler();
    inlineObjectLiterals = new InlineObjectLiterals(compiler, () -> "tempVar");
    compiler.addPass(inlineObjectLiterals);
  }

  @Test
  public void testInlineObjectLiterals_SimpleObject() {
    String sourceCode = "function test() {" +
        "  var obj = {" +
        "    name: 'John Doe'," +
        "    age: 30," +
        "    hobbies: ['reading', 'swimming', 'coding']" +
        "  };" +
        "  return obj.name + ' is ' + obj.age + ' years old.';" +
        "}";

    Node root = compiler.parseAndCompile(sourceCode);

    String expectedResult = "function test() {" +
        "  var name = JSCompiler_object_inline_name_tempVar;" +
        "  var age = JSCompiler_object_inline_age_tempVar;" +
        "  var hobbies = JSCompiler_object_inline_hobbies_tempVar;" +
        "  var hobbies_0 = JSCompiler_object_inline_hobbies_tempVar_0_tempVar;" +
        "  var hobbies_1 = JSCompiler_object_inline_hobbies_tempVar_1_tempVar;" +
        "  var hobbies_2 = JSCompiler_object_inline_hobbies_tempVar_2_tempVar;" +
        "  return name + ' is ' + age + ' years old.';" +
        "}" +
        "JSCompiler_object_inline_name_tempVar = 'John Doe';" +
        "JSCompiler_object_inline_age_tempVar = 30;" +
        "JSCompiler_object_inline_hobbies_tempVar = [JSCompiler_object_inline_hobbies_tempVar_0_tempVar, JSCompiler_object_inline_hobbies_tempVar_1_tempVar, JSCompiler_object_inline_hobbies_tempVar_2_tempVar]";

    String result = compiler.getCompiledSourceForTest();
    assertThat(result).isEqualTo(expectedResult);
  }

  @Test
  public void testInlineObjectLiterals_NestedObjects() {
    String sourceCode = "function test() {" +
        "  var obj1 = {" +
        "    name: 'John Doe'," +
        "    age: 30," +
        "    obj2: {" +
        "      hobby: 'reading'" +
        "    }" +
        "  };" +
        "  return obj1.name + ' is ' + obj1.age + ' years old. ' + obj1.obj2.hobby;" +
        "}";

    Node root = compiler.parseAndCompile(sourceCode);

    String expectedResult = "function test() {" +
        "  var name = JSCompiler_object_inline_name_tempVar;" +
        "  var age = JSCompiler_object_inline_age_tempVar;" +
        "  var hobby = JSCompiler_object_inline_hobby_tempVar;" +
        "  var obj1 = {" +
        "    name: name," +
        "    age: age," +
        "    obj2: {" +
        "      hobby: hobby" +
        "    }" +
        "  };" +
        "  return name + ' is ' + age + ' years old. ' + hobby;" +
        "}" +
        "JSCompiler_object_inline_name_tempVar = 'John Doe';" +
        "JSCompiler_object_inline_age_tempVar = 30;" +
        "JSCompiler_object_inline_hobby_tempVar = 'reading'";

    String result = compiler.getCompiledSourceForTest();
    assertThat(result).isEqualTo(expectedResult);
  }

  @Test
  public void testInlineObjectLiterals_SelfReferentialObject() {
    String sourceCode = "function test() {" +
        "  var obj = {" +
        "    name: 'John Doe'," +
        "    getName: function() { return this.name; }" +
        "  };" +
        "  return obj.getName() + ' is ' + obj.age + ' years old.';" +
        "}" +
        "test.prototype.age = 30";

    Node root = compiler.parseAndCompile(sourceCode);

    String expectedResult = "function test() {" +
        "  var name = JSCompiler_object_inline_name_tempVar;" +
        "  var getName = JSCompiler_object_inline_getName_tempVar;" +
        "  var age = JSCompiler_object_inline_age_tempVar;" +
        "  var obj = {" +
        "    name: name," +
        "    getName: getName" +
        "  };" +
        "  return getName() + ' is ' + age + ' years old.'"; +
        "}" +
        "JSCompiler_object_inline_name_tempVar = 'John Doe';" +
        "JSCompiler_object_inline_age_tempVar = 30;" +
        "Test.prototype.age = 30";

    String result = compiler.getCompiledSourceForTest();
    assertThat(result).isEqualTo(expectedResult);
  }

  @Test(expected = IllegalStateException.class)
  public void testInlineObjectLiterals_CircularObjectReference() {
    String sourceCode = "function test() {" +
        "  var obj1 = {" +
        "    name: 'John Doe'," +
        "    obj2: obj2" +
        "  };" +
        "  var obj2 = obj1;" +
        "  return obj1.name; // never reached due to circular reference" +
        "}" +
        "test();";

    Node root = compiler.parseAndCompile(sourceCode);

    // The test should fail due to a circular reference that cannot be
    // inlined.
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInlineObjectLiterals_InvalidObjectLiteral() {
    String sourceCode = "function test() {" +
        "  var obj = {" +
        "    name: 'John Doe'," +
        "    getName: function() { return this.name; }" +
        "  };" +
        "  var age = 30;" +
        "  return obj.getName() + ' is ' + age + ' years old.';" +
        "}" +
        "test.prototype.age = function() { return 40; }"; // Introducing a function instead of a number

    Node root = compiler.parseAndCompile(sourceCode);

    // The test should fail due to an invalid object literal.
  }

  @Test
  public void testInlineObjectLiterals_UndefinedObjectProperty() {
    String sourceCode = "function test() {" +
        "  var obj = {" +
        "    name: 'John Doe'," +
        "    getName: function() { return this.nonExistentProperty; }" +
        "  };" +
        "  return obj.getName();" +
        "}" +
        "test();";

    Node root = compiler.parseAndCompile(sourceCode);

    String expectedResult = "function test() {" +
        "  var name = JSCompiler_object_inline_name_tempVar;" +
        "  var getName = JSCompiler_object_inline_getName_tempVar;" +
        "  return getName();" +
        "}" +
        "JSCompiler_object_inline_name_tempVar = 'John Doe';" +
        "JSCompiler_object_inline_getName_tempVar = function() { return undefined; }";

    String result = compiler.getCompiledSourceForTest();
    assertThat(result).isEqualTo(expectedResult);
  }

  @Test
  public void testInlineObjectLiterals_GlobalObject() {
    String sourceCode = "window.test = function test() {" +
        "  var obj = {" +
        "    name: 'John Doe'," +
        "    getName: function() { return this.name; }" +
        "  };" +
        "  return obj.getName();" +
        "}" +
        "window.test();";

    Node root = compiler.parseAndCompile(sourceCode);

    String expectedResult = "window.test = function test() {" +
        "  var name = JSCompiler_object_inline_name_tempVar;" +
        "  return name;" +
        "}" +
        "JSCompiler_object_inline_name_tempVar = 'John Doe'";

    String result = compiler.getCompiledSourceForTest();
    assertThat(result).isEqualTo(expectedResult);
  }

  @Test
  public void testInlineObjectLiterals_CallableObject() {
    String sourceCode = "function test() {" +
        "  var obj = {" +
        "    name: 'John Doe'," +
        "    doSomething: function() { return 'Hello World'; }" +
        "  };" +
        "  return obj.doSomething();" +
        "}" +
        "test();";

    Node root = compiler.parseAndCompile(sourceCode);

    String expectedResult = "function test() {" +
        "  var doSomething = JSCompiler_object_inline_doSomething_tempVar;" +
        "  return doSomething();" +
        "}" +
        "JSCompiler_object_inline_doSomething_tempVar = function() { return 'Hello World'; }";

    String result = compiler.getCompiledSourceForTest();
    assertThat(result).isEqualTo(expectedResult);
  }
}