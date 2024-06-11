import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.InlineObjectLiterals;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class InlineObjectLiteralsTest {

  @Test
  public void testInlineObjectLiterals() throws Exception {
    String jsSource = "function foo() {" +
        "  var obj = {" +
        "    a: 1," +
        "    b: {'c': 3}," +
        "    d: function() { return 4; }" +
        "  };" +
        "}" +
        "foo();";

    Compiler compiler = new Compiler();
    CompilerPass pass = new InlineObjectLiterals(compiler, () -> "tmp");
    compiler.addPass(pass);
    compiler.compile(jsSource, null, null);

    // Basic functionality test
    String expectedOutput = "function foo() {" +
        "  var a = 1;" +
        "  var b_c = 3;" +
        "  var d = function () { return 4; };" +
        "  var tmp_a = undefined;" +
        "  var tmp_b_c = undefined;" +
        "  var tmp_d = undefined;" +
        "  tmp_a = a;\n" +
        "  tmp_b_c = b_c;" +
        "  tmp_d = d;" +
        "}" +
        "foo();";
    String actualOutput = compiler.getCompiledCode();
    assertCompiledCodeEquals(expectedOutput, actualOutput);

    // Edge cases

    // Test object with no properties
    jsSource = "function foo() {" +
        "  var obj = {};" +
        "}" +
        "foo();";
    compiler.reset();
    compiler.addPass(pass);
    compiler.compile(jsSource, null, null);
    String emptyObjectExpectedOutput = "function foo() {}";
    assertCompiledCodeEquals(emptyObjectExpectedOutput, actualOutput);

    // Test object with only one property
    jsSource = "function foo() {" +
        "  var obj = { a: 1 };" +
        "}" +
        "foo();";
    compiler.reset();
    compiler.addPass(pass);
    compiler.compile(jsSource, null, null);
    String onePropertyObjectExpectedOutput = "function foo() {" +
        "  var a = 1;" +
        "}" +
        "foo();";
    assertCompiledCodeEquals(onePropertyObjectExpectedOutput, actualOutput);

    // Test object with multiple properties and nested objects
    jsSource = "function foo() {" +
        "  var obj = {" +
        "    a: 1," +
        "    b: {" +
        "      c: 2," +
        "      d: 3" +
        "    }," +
        "    e: 4" +
        "  };" +
        "}" +
        "foo();";
    compiler.reset();
    compiler.addPass(pass);
    compiler.compile(jsSource, null, null);
    String multipleObjectPropertiesExpectedOutput = "function foo() {" +
        "  var a = 1;" +
        "  var b_c = 2;" +
        "  var b_d = 3;" +
        "  var e = 4;" +
        "}" +
        "foo();";
    assertCompiledCodeEquals(multipleObjectPropertiesExpectedOutput, actualOutput);

    // Test object with only getters and setters
    jsSource = "function foo() {" +
        "  var obj = {" +
        "    get a() { return 1; }, " +
        "    set a(val) { this._a = val; }," +
        "    get b() { return 2; }" +
        "  };" +
        "}" +
        "foo();";
    compiler.reset();
    compiler.addPass(pass);
    compiler.compile(jsSource, null, null);
    String getterAndSetterObjectExpectedOutput = "function foo() {" +
        "  var _a = undefined;" +
        "  var a = {" +
        "    get: function() { return 1; }," +
        "    set: function(val) { _a = val; }" +
        "  }" +
        "  var b = 2;" +
        "}" +
        "foo();";
    assertCompiledCodeEquals(getterAndSetterObjectExpectedOutput, actualOutput);

    // Test object with only functions
    jsSource = "function foo() {" +
        "  var obj = {" +
        "    f1: function() { return 1; }, " +
        "    f2: function() { return 2; }" +
        "  };" +
        "}" +
        "foo();";
    compiler.reset();
    compiler.addPass(pass);
    compiler.compile(jsSource, null, null);
    String onlyFunctionObjectExpectedOutput = "function foo() {" +
        "  var f1 = function() { return 1; }" +
        "  var f2 = function() { return 2; }" +
        "}" +
        "foo();";
    assertCompiledCodeEquals(onlyFunctionObjectExpectedOutput, actualOutput);

    // Test object with only undefined properties
    jsSource = "function foo() {" +
        "  var obj = {" +
        "    a: undefined," +
        "    b: undefined," +
        "    c: undefined" +
        "  };" +
        "}" +
        "foo();";
    compiler.reset();
    compiler.addPass(pass);
    compiler.compile(jsSource, null, null);
    String onlyUndefinedObjectExpectedOutput = "function foo() {" +
        "}" +
        "foo();";
    assertCompiledCodeEquals(onlyUndefinedObjectExpectedOutput, actualOutput);

    // Test object with only null properties
    jsSource = "function foo() {" +
        "  var obj = {" +
        "    a: null," +
        "    b: null," +
        "    c: null" +
        "  };" +
        "}" +
        "foo();";
    compiler.reset();
    compiler.addPass(pass);
    compiler.compile(jsSource, null, null);
    String onlyNullObjectExpectedOutput = "function foo() {" +
        "}" +
        "foo();";
    assertCompiledCodeEquals(onlyNullObjectExpectedOutput, actualOutput);

    // Test object with only boolean properties
    jsSource = "function foo() {" +
        "  var obj = {" +
        "    a: true," +
        "    b: false" +
        "  };" +
        "}" +
        "foo();";
    compiler.reset();
    compiler.addPass(pass);
    compiler.compile(jsSource, null, null);
    String onlyBooleanObjectExpectedOutput = "function foo() {" +
        "}" +
        "foo();";
    assertCompiledCodeEquals(onlyBooleanObjectExpectedOutput, actualOutput);

    // Test object with only number properties
    jsSource = "function foo() {" +
        "  var obj = {" +
        "    a: 1," +
        "    b: 2," +
        "    c: 3" +
        "  };" +
        "}" +
        "foo();";
    compiler.reset();
    compiler.addPass(pass);
    compiler.compile(jsSource, null, null);
    String onlyNumberObjectExpectedOutput = "function foo() {" +
        "}" +
        "foo();";
    assertCompiledCodeEquals(onlyNumberObjectExpectedOutput, actualOutput);

    // Test object with only string properties
    jsSource = "function foo() {" +
        "  var obj = {" +
        "    a: '1'," +
        "    b: '2'," +
        "    c: '3'" +
        "  };" +
        "}" +
        "foo();";
    compiler.reset();
    compiler.addPass(pass);
    compiler.compile(jsSource, null, null);
    String onlyStringObjectExpectedOutput = "function foo() {" +
        "}" +
        "foo();";
    assertCompiledCodeEquals(onlyStringObjectExpectedOutput, actualOutput);

    // Test object with only symbols
    jsSource = "function foo() {" +
        "  var obj = {" +
        "    a: Symbol('1')," +
        "    b: Symbol('2')," +
        "    c: Symbol('3')" +
        "  };" +
        "}" +
        "foo();";
    compiler.reset();
    compiler.addPass(pass);
    compiler.compile(jsSource, null, null);
    String onlySymbolObjectExpectedOutput = "function foo() {" +
        "}" +
        "foo();";
    assertCompiledCodeEquals(onlySymbolObjectExpectedOutput, actualOutput);

    // Test object with only primitives
    jsSource = "function foo() {" +
        "  var obj = {" +
        "    a: 1," +
        "    b: '2'," +
        "    c: true" +
        "  };" +
        "}" +
        "foo();";
    compiler.reset();
    compiler.addPass(pass);
    compiler.compile(jsSource, null, null);
    String onlyPrimitiveObjectExpectedOutput = "function foo() {" +
        "}" +
        "foo();";
    assertCompiledCodeEquals(onlyPrimitiveObjectExpectedOutput, actualOutput);

    // Test object with mixed types
    jsSource = "function foo() {" +
        "  var obj = {" +
        "    a: 1," +
        "    b: '2'," +
        "    c: true," +
        "    d: { e: 2, f: '3' }," +
        "    g: function() { return 4; }" +
        "  };" +
        "}" +
        "foo();";
    compiler.reset();
    compiler.addPass(pass);
    compiler.compile(jsSource, null, null);
    String mixedTypeObjectExpectedOutput = "function foo() {" +
        "  var a = 1;" +
        "  var b = '2'" +
        "  var c = true;" +
        "  var d_e = 2;" +
        "  var d_f = '3'" +
        "  var g = function () { return 4; };" +
        "}" +
        "foo();";
    assertCompiledCodeEquals(mixedTypeObjectExpectedOutput, actualOutput);
  }

  private void assertCompiledCodeEquals(String expected, String actual) {
    assert (expected.equals(actual));
  }
}