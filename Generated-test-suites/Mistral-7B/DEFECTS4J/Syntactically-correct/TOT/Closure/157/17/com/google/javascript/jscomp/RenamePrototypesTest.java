import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerResults;
import com.google.javascript.jscomp.CompilerUtils;
import com.google.javascript.jscomp.RenamePrototypes;
import com.google.javascript.jscomp.VariableMap;

public class RenamePrototypesTest {

  private CompilerInput input;
  private CompilerResults results;
  private AbstractCompiler compiler;

  @Before
  public void setUp() {
    CompilerOptions options = new CompilerOptions();
    options.setLanguageIn(CompilerOptions.Language.ECMASCRIPT5_STRICT);
    compiler = new AbstractCompiler();
    compiler.setOptions(options);
    compiler.setLifeCycleHandler(new LifeCycleHandler() {
      @Override
      public void onNormalized(AbstractCompiler compiler) {
        compiler.addPass(new RenamePrototypes(compiler, true, null, null));
      }
    });
    input = new CompilerInput("test.js", "test.js", new StringReader("test code"));
  }

  @Test
  public void testRenaming() {
    compiler.compile(input, null);
    results = compiler.getResults();

    String output = CompilerUtils.getSourceCode(results, "test.js");

    // Test that the prototype property "foo" is renamed to "_foo"
    String expectedOutput = "test code\n" +
        "var Foo = function() {}\n" +
        "Foo.prototype._foo = function() {}\n";
    assertEquals(expectedOutput, output);

    // Test that the prototype property "bar" is not renamed
    expectedOutput = "test code\n" +
        "var Foo = function() {}\n" +
        "Foo.prototype.bar = function() {}\n";
    assertNotEquals(expectedOutput, output);

    // Test that the object literal property "baz" is renamed to "_baz"
    expectedOutput = "test code\n" +
        "var obj = {\n" +
        "  _baz: function() {}\n" +
        "}\n";
    assertEquals(expectedOutput, output);

    // Test that the object literal property "_qux" is not renamed
    expectedOutput = "test code\n" +
        "var obj = {\n" +
        "  _qux: function() {}\n" +
        "}\n";
    assertNotEquals(expectedOutput, output);
  }

  @Test
  public void testReservedNames() {
    CompilerOptions options = new CompilerOptions();
    options.setLanguageIn(CompilerOptions.Language.ECMASCRIPT5_STRICT);
    compiler = new AbstractCompiler();
    compiler.setOptions(options);
    compiler.setLifeCycleHandler(new LifeCycleHandler() {
      @Override
      public void onNormalized(AbstractCompiler compiler) {
        compiler.addPass(new RenamePrototypes(compiler, false, null, null));
      }
    });
    input = new CompilerInput("test.js", "test.js", new StringReader("test code"));

    compiler.compile(input, null);
    results = compiler.getResults();

    String output = CompilerUtils.getSourceCode(results, "test.js");

    // Test that the reserved name "indexOf" is not renamed
    String expectedOutput = "test code\n" +
        "var arr = [1, 2, 3];\n" +
        "arr.indexOf = arr.indexOf;\n";
    assertEquals(expectedOutput, output);
  }

  @Test
  public void testAggressiveRenaming() {
    CompilerOptions options = new CompilerOptions();
    options.setLanguageIn(CompilerOptions.Language.ECMASCRIPT5_STRICT);
    compiler = new AbstractCompiler();
    compiler.setOptions(options);
    compiler.setLifeCycleHandler(new LifeCycleHandler() {
      @Override
      public void onNormalized(AbstractCompiler compiler) {
        compiler.addPass(new RenamePrototypes(compiler, true, null, null));
      }
    });
    input = new CompilerInput("test.js", "test.js", new StringReader("test code"));

    compiler.compile(input, null);
    results = compiler.getResults();

    String output = CompilerUtils.getSourceCode(results, "test.js");

    // Test that all prototype properties are renamed aggressively
    String expectedOutput = "test code\n" +
        "var Foo = function() {}\n" +
        "Foo.prototype._foo = function() {}\n" +
        "Foo.prototype._bar = function() {}\n";
    assertEquals(expectedOutput, output);
  }

  @Test(expected = AssertionError.class)
  public void testExternedPropertyRenaming() {
    CompilerOptions options = new CompilerOptions();
    options.setLanguageIn(CompilerOptions.Language.ECMASCRIPT5_STRICT);
    compiler = new AbstractCompiler();
    compiler.setOptions(options);
    compiler.setLifeCycleHandler(new LifeCycleHandler() {
      @Override
      public void onNormalized(AbstractCompiler compiler) {
        compiler.addPass(new RenamePrototypes(compiler, true, null, null));
      }
    });
    input = new CompilerInput("test.js", "test.js", new StringReader("test code\nObject.prototype.foo = function() {}"));

    compiler.compile(input, null);
    results = compiler.getResults();

    String output = CompilerUtils.getSourceCode(results, "test.js");

    // Should throw an AssertionError because Object.prototype.foo is not renamable
  }

  @Test(expected = AssertionError.class)
  public void testNonStringPropertyNameRenaming() {
    CompilerOptions options = new CompilerOptions();
    options.setLanguageIn(CompilerOptions.Language.ECMASCRIPT5_STRICT);
    compiler = new AbstractCompiler();
    compiler.setOptions(options);
    compiler.setLifeCycleHandler(new LifeCycleHandler() {
      @Override
      public void onNormalized(AbstractCompiler compiler) {
        compiler.addPass(new RenamePrototypes(compiler, true, null, null));
      }
    });
    input = new CompilerInput("test.js", "test.js", new StringReader("test code\nvar obj = {42: function() {}}"));

    compiler.compile(input, null);
    results = compiler.getResults();

    String output = CompilerUtils.getSourceCode(results, "test.js");

    // Should throw an AssertionError because property names must be strings
  }
}