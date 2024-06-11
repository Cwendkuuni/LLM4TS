import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CompilerOptions.CompilerOption;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilerOptions.Context;
import com.google.javascript.jscomp.CompilerOptions.SourceMapOption;
import com.google.javascript.jscomp.CompilerOptions.WarningLevel;
import com.google.javascript.jscomp.JSSourceFile;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.RenamePrototypes;
import com.google.javascript.rhino.SourceFile;

import org.junit.Test;

public class RenamePrototypesTest {

  @Test
  public void testSimpleRename() {
    String input = "function Foo() {}\n" +
                   "Foo.prototype.bar = function() { return 1; };\n" +
                   "var foo = new Foo();\n" +
                   "assertEquals(1, foo.bar());\n";

    CompilerOptions options = new CompilerOptions(Context.EMPTY, new CompilerOptions[] {
      CompilerOption.AGGRESSIVE_RENAMING,
      CompilerOption.OUTPUT_FILE("output.js"),
      CompilerOption.WARNING_LEVEL(WarningLevel.QUIET),
      CompilerOption.SOURCE_MAP(SourceMapOption.NONE),
    });

    Compiler compiler = new Compiler(options);
    JSSourceFile source = new JSSourceFile("input.js", input);
    RenamePrototypes pass = new RenamePrototypes(compiler, true, null, null);
    pass.process(source.getInput(), source.getTree());
    compiler.runPasses(pass);
    assertEquals("function Foo() {}\n" +
                 "Foo.prototype._c = function() { return 1; };\n" +
                 "var foo = new Foo();\n" +
                 "assertEquals(1, foo._c());\n", asString(source.getTree()));
  }

  @Test
  public void testNoRename() {
    String input = "function Foo() {}\n" +
                   "Foo.prototype.toString = function() { return this; };\n" +
                   "var foo = new Foo();\n" +
                   "assertEquals(foo.toString(), foo);\n";

    CompilerOptions options = new CompilerOptions(Context.EMPTY, new CompilerOptions[] {
      CompilerOption.AGGRESSIVE_RENAMING,
      CompilerOption.OUTPUT_FILE("output.js"),
      CompilerOption.WARNING_LEVEL(WarningLevel.QUIET),
      CompilerOption.SOURCE_MAP(SourceMapOption.NONE),
    });

    Compiler compiler = new Compiler(options);
    JSSourceFile source = new JSSourceFile("input.js", input);
    RenamePrototypes pass = new RenamePrototypes(compiler, true, null, null);
    pass.process(source.getInput(), source.getTree());
    compiler.runPasses(pass);
    assertEquals("function Foo() {}\n" +
                 "Foo.prototype.toString = function() { return this; };\n" +
                 "var foo = new Foo();\n" +
                 "assertEquals(foo.toString(), foo);\n", asString(source.getTree()));
  }

  @Test
  public void testNoRenamePrivate() {
    String input = "function Foo() {}\n" +
                   "Foo.prototype._bar = function() { return 1; };\n" +
                   "var foo = new Foo();\n" +
                   "assertEquals(1, foo._bar());\n";

    CompilerOptions options = new CompilerOptions(Context.EMPTY, new CompilerOptions[] {
      CompilerOption.AGGRESSIVE_RENAMing, // misspelled
      CompilerOption.OUTPUT_FILE("output.js"),
      CompilerOption.WARNING_LEVEL(WarningLevel.QUIET),
      CompilerOption.SOURCE_MAP(SourceMapOption.NONE),
    });

    Compiler compiler = new Compiler(options);
    JSSourceFile source = new JSSourceFile("input.js", input);
    RenamePrototypes pass = new RenamePrototypes(compiler, true, null, null);
    pass.process(source.getInput(), source.getTree());
    compiler.runPasses(pass);
    assertEquals("function Foo() {}\n" +
                 "Foo.prototype._bar = function() { return 1; };\n" +
                 "var foo = new Foo();\n" +
                 "assertEquals(1, foo._bar());\n", asString(source.getTree()));
  }

  @Test
  public void testRenameMultiple() {
    String input = "function Foo() {}\n" +
                   "Foo.prototype.bar = function() { return 1; };\n" +
                   "Foo.prototype.baz = function() { return 2; };\n" +
                   "var foo = new Foo();\n" +
                   "assertEquals(1, foo.bar());\n" +
                   "assertEquals(2, foo.baz());\n";

    CompilerOptions options = new CompilerOptions(Context.EMPTY, new CompilerOptions[] {
      CompilerOption.AGGRESSIVE_RENAMING,
      CompilerOption.OUTPUT_FILE("output.js"),
      CompilerOption.WARNING_LEVEL(WarningLevel.QUIET),
      CompilerOption.SOURCE_MAP(SourceMapOption.NONE),
    });

    Compiler compiler = new Compiler(options);
    JSSourceFile source = new JSSourceFile("input.js", input);
    RenamePrototypes pass = new RenamePrototypes(compiler, true, null, null);
    pass.process(source.getInput(), source.getTree());
    compiler.runPasses(pass);
    assertEquals("function Foo() {}\n" +
                 "Foo.prototype._a = function() { return 1; };\n" +
                 "Foo.prototype._b = function() { return 2; };\n" +
                 "var foo = new Foo();\n" +
                 "assertEquals(1, foo._a());\n" +
                 "assertEquals(2, foo._b());\n", asString(source.getTree()));
  }

  @Test
  public void testNoRenameAggressive() {
    String input = "function Foo() {}\n" +
                   "Foo.prototype.toString = function() { return this; };\n" +
                   "var foo = new Foo();\n" +
                   "assertEquals(foo.toString(), foo);\n";

    CompilerOptions options = new CompilerOptions(Context.EMPTY, new CompilerOptions[] {
      CompilerOption.AGGRESSIVE_RENAMING, // enabled
      CompilerOption.OUTPUT_FILE("output.js"),
      CompilerOption.WARNING_LEVEL(WarningLevel.QUIET),
      CompilerOption.SOURCE_MAP(SourceMapOption.NONE),
    });

    Compiler compiler = new Compiler(options);
    JSSourceFile source = new JSSourceFile("input.js", input);
    RenamePrototypes pass = new RenamePrototypes(compiler, false, null, null);
    pass.process(source.getInput(), source.getTree());
    compiler.runPasses(pass);
    assertEquals("function Foo() {}\n" +
                 "Foo.prototype.toString = function() { return this; };\n" +
                 "var foo = new Foo();\n" +
                 "assertEquals(foo.toString(), foo);\n", asString(source.getTree()));
  }

  @Test
  public void testRenameBuiltIn() {
    String input = "function Foo() {}\n" +
                   "Foo.prototype.indexOf = function() { return 1; };\n" +
                   "var foo = new Foo();\n" +
                   "assertEquals(1, foo.indexOf(1));\n";

    CompilerOptions options = new CompilerOptions(Context.EMPTY, new CompilerOptions[] {
      CompilerOption.AGGRESSIVE_RENAMING,
      CompilerOption.OUTPUT_FILE("output.js"),
      CompilerOption.WARNING_LEVEL(WarningLevel.QUIET),
      CompilerOption.SOURCE_MAP(SourceMapOption.NONE),
    });

    Compiler compiler = new Compiler(options);
    JSSourceFile source = new JSSourceFile("input.js", input);
    RenamePrototypes pass = new RenamePrototypes(compiler, false, null, null);
    pass.process(source.getInput(), source.getTree());
    try {
      compiler.runPasses(pass);
      fail("Expected a compiler error.");
    } catch (RuntimeException e) {
      // Expected
    }
    assertEquals("function Foo() {}\n" +
                 "Foo.prototype.indexOf = function() { return 1; };\n" +
                 "var foo = new Foo();\n" +
                 "assertEquals(1, foo.indexOf(1));\n", asString(source.getTree()));
  }

  @Test
  public void testRenameObjLit() {
    String input = "function Foo() {}\n" +
                   "var obj = {\n" +
                   "  bar: function() { return 1; },\n" +
                   "  baz: function() { return 2 },\n" +
                   "  prototype: Foo.prototype,\n" +
                   "};\n" +
                   "Foo.prototype.bar = function() { return 3; };\n" +
                   "assertEquals(1, obj.bar());\n" +
                   "assertEquals(3, obj.bar());\n";

    CompilerOptions options = new CompilerOptions(Context.EMPTY, new CompilerOptions[] {
      CompilerOption.AGGRESSIVE_RENAMING,
      CompilerOption.OUTPUT_FILE("output.js"),
      CompilerOption.WARNING_LEVEL(WarningLevel.QUIET),
      CompilerOption.SOURCE_MAP(SourceMapOption.NONE),
    });

    Compiler compiler = new Compiler(options);
    JSSourceFile source = new JSSourceFile("input.js", input);
    RenamePrototypes pass = new RenamePrototypes(compiler, false, null, null);
    pass.process(source.getInput(), source.getTree());
    compiler.runPasses(pass);
    assertEquals("function Foo() {}\n" +
                 "var obj = {\n" +
                 "  _a: function() { return 1; },\n" +
                 "  _b: function() { return 2 },\n" +
                 "  prototype: Foo.prototype,\n" +
                 "};\n" +
                 "Foo.prototype._a = function() { return 3; };\n" +
                 "assertEquals(1, obj._a());\n" +
                 "assertEquals(3, obj._a());\n", asString(source.getTree()));
  }

  private static String asString(Node tree) {
    SourceFile sf = tree.getSourceFile();
    return sf.getText();
  }
}