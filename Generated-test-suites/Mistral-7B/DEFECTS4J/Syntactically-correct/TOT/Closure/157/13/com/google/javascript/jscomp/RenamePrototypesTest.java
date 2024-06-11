import org.junit.Assert;
import org.junit.Test;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.RenamePrototypes;

/**
 * Tests for the RenamePrototypes CompilerPass.
 */
public class RenamePrototypesTest {

  @Test
  public void testRenamePrototypes() {
    String jsCode = "function Foo() {}\n" +
        "Foo.prototype.bar = function() { return 42; };\n" +
        "Foo.prototype.baz = 'hello';\n" +
        "Foo.prototype.qux = function() { return this.bar(); };\n" +
        "var foo = new Foo();\n" +
        "Assert.assertEquals(42, foo.bar());\n" +
        "Assert.assertEquals('hello', foo.baz);\n" +
        "Assert.assertEquals(42, foo.qux());";

    CompilerOptions options = CompilerOptions.defaultOptions();
    Compiler compiler = new Compiler();
    compiler.addPass(RenamePrototypes.class);

    RenamePrototypes renamer = new RenamePrototypes(compiler, true, null, null);
    renamer.process(null, parser.parse(jsCode, options.getSourceRoot(), null));

    String obfuscatedJsCode = compiler.getJavascriptText();

    // Check that the property names were renamed
    String expectedObfuscatedJsCode = "function Foo() {}\n" +
        "Foo.prototype._bar = function() { return 42; };\n" +
        "Foo.prototype._baz = 'hello';\n" +
        "Foo.prototype._qux = function() { return this._bar(); };\n" +
        "var foo = new Foo();\n" +
        "Assert.assertEquals(42, foo._bar());\n" +
        "Assert.assertEquals('hello', foo._baz);\n" +
        "Assert.assertEquals(42, foo._qux());";

    Assert.assertEquals(expectedObfuscatedJsCode, obfuscatedJsCode);
  }

  @Test
  public void testNoRenameForPrivateProperties() {
    String jsCode = "function Foo() {}\n" +
        "Foo.prototype._bar = function() { return 42; };\n" +
        "Foo.prototype.baz = 'hello';\n" +
        "var foo = new Foo();\n" +
        "Assert.assertEquals(42, foo._bar());\n" +
        "Assert.assertEquals('hello', foo.baz);";

    CompilerOptions options = CompilerOptions.defaultOptions();
    Compiler compiler = new Compiler();
    compiler.addPass(RenamePrototypes.class);

    RenamePrototypes renamer = new RenamePrototypes(compiler, false, null, null);
    renamer.process(null, parser.parse(jsCode, options.getSourceRoot(), null));

    String obfuscatedJsCode = compiler.getJavascriptText();

    // Check that the private property was not renamed
    String expectedObfuscatedJsCode = jsCode;

    Assert.assertEquals(expectedObfuscatedJsCode, obfuscatedJsCode);
  }

  @Test
  public void testNoRenameForReservedNames() {
    String jsCode = "function Foo() {}\n" +
        "Foo.prototype.indexOf = function() { return 42; };\n" +
        "Foo.prototype.toString = function() { return 'hello'; };\n" +
        "var foo = new Foo();\n" +
        "Assert.assertEquals(42, Foo.prototype.indexOf.call(foo));\n" +
        "Assert.assertEquals('hello', Foo.prototype.toString.call(foo));";

    CompilerOptions options = CompilerOptions.defaultOptions();
    Compiler compiler = new Compiler();
    compiler.addPass(RenamePrototypes.class);

    RenamePrototypes renamer = new RenamePrototypes(compiler, true, null, null);
    renamer.process(null, parser.parse(jsCode, options.getSourceRoot(), null));

    String obfuscatedJsCode = compiler.getJavascriptText();

    // Check that the reserved names were not renamed
    String expectedObfuscatedJsCode = jsCode;

    Assert.assertEquals(expectedObfuscatedJsCode, obfuscatedJsCode);
  }

  @Test
  public void testNoRenameForAggressiveRenamingOff() {
    String jsCode = "function Foo() {}\n" +
        "Foo.prototype.fooBar = function() { return 42; };\n" +
        "Foo.prototype.baz = 'hello';\n" +
        "var foo = new Foo();\n" +
        "Assert.assertEquals(42, foo.fooBar());\n" +
        "Assert.assertEquals('hello', foo.baz);";

    CompilerOptions options = CompilerOptions.defaultOptions();
    Compiler compiler = new Compiler();
    compiler.addPass(RenamePrototypes.class);

    RenamePrototypes renamer = new RenamePrototypes(compiler, false, null, null);
    renamer.process(null, parser.parse(jsCode, options.getSourceRoot(), null));

    String obfuscatedJsCode = compiler.getJavascriptText();

    // Check that the property names were not renamed because they only contain
    // lowercase letters
    String expectedObfuscatedJsCode = jsCode;

    Assert.assertEquals(expectedObfuscatedJsCode, obfuscatedJsCode);
  }
}