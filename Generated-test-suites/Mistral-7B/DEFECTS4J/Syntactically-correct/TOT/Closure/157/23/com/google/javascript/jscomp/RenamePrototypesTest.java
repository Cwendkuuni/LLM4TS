import static org.junit.Assert.*;
import static com.google.javascript.jscomp.RenamePrototypes.*;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerResult;
import com.google.javascript.jscomp.Error;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.jscomp.VariableMap;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class RenamePrototypesTest {

  @SuppressWarnings("deprecation")
  @Test
  public void testSimpleRename() throws IOException {
    String input = "function Foo() {} Foo.prototype.bar = function() {};";
    CompilerInput compInput = new CompilerInput(SourceFile.newBuilder("test.js"))
        .setSourceCode(input)
        .build();

    CompilerOptions options = new CompilerOptions(new CompilerOptions.Defaults());
    options.setOutputFile("output.js");
    options.setError(new Error());

    AbstractCompiler compiler = new AbstractCompiler();
    compiler.setOptions(options);
    compiler.registerPass(new CompilerPass() {
      @Override
      public void process(Node externs, Node root) {
        new RenamePrototypes(compiler, false, null, null).process(externs, root);
      }
    });

    CompilerResult result = compiler.compile(compInput);

    List<Error> errors = result.getErrors();
    assertEquals(0, errors.size());

    String expectedOutput = "function Foo() {} Foo.prototype._c = function() {};";
    String actualOutput = result.getOutput();
    assertEquals(expectedOutput, actualOutput);
  }

  @SuppressWarnings("deprecation")
  @Test
  public void testAggressiveRename() throws IOException {
    String input = "function Foo() { Foo.prototype.bar = function() {}; }";
    CompilerInput compInput = new CompilerInput(SourceFile.newBuilder("test.js"))
        .setSourceCode(input)
        .build();

    CompilerOptions options = new CompilerOptions(new CompilerOptions.Defaults());
    options.setOutputFile("output.js");
    options.setError(new Error());
    options.setCompilerFlag("--aggressive_renaming", "");

    AbstractCompiler compiler = new AbstractCompiler();
    compiler.setOptions(options);
    compiler.registerPass(new CompilerPass() {
      @Override
      public void process(Node externs, Node root) {
        new RenamePrototypes(compiler, true, null, null).process(externs, root);
      }
    });

    CompilerResult result = compiler.compile(compInput);

    List<Error> errors = result.getErrors();
    assertEquals(0, errors.size());

    String expectedOutput = "function Foo() { Foo.prototype._b = function() {}; }";
    String actualOutput = result.getOutput();
    assertEquals(expectedOutput, actualOutput);
  }

  @SuppressWarnings("deprecation")
  @Test
  public void testExcludedNames() throws IOException {
    String input = "function Foo() { Foo.prototype.indexOf = function() {}; }";
    CompilerInput compInput = new CompilerInput(SourceFile.newBuilder("test.js"))
        .setSourceCode(input)
        .build();

    CompilerOptions options = new CompilerOptions(new CompilerOptions.Defaults());
    options.setOutputFile("output.js");
    options.setError(new Error());

    AbstractCompiler compiler = new AbstractCompiler();
    compiler.setOptions(options);
    compiler.registerPass(new CompilerPass() {
      @Override
      public void process(Node externs, Node root) {
        new RenamePrototypes(compiler, false, null, null).process(externs, root);
      }
    });

    CompilerResult result = compiler.compile(compInput);

    List<Error> errors = result.getErrors();
    assertEquals(0, errors.size());

    String expectedOutput = "function Foo() { Foo.prototype.indexOf = function() {}; }";
    String actualOutput = result.getOutput();
    assertEquals(expectedOutput, actualOutput);
  }

  @SuppressWarnings("deprecation")
  @Test
  public void testConflictingNames() throws IOException {
    String input = "function Foo() { Foo.prototype.bar = function() {}; }"
        + "var obj = { bar: 42 };";
    CompilerInput compInput = new CompilerInput(SourceFile.newBuilder("test.js"))
        .setSourceCode(input)
        .build();

    CompilerOptions options = new CompilerOptions(new CompilerOptions.Defaults());
    options.setOutputFile("output.js");
    options.setError(new Error());

    AbstractCompiler compiler = new AbstractCompiler();
    compiler.setOptions(options);
    compiler.registerPass(new CompilerPass() {
      @Override
      public void process(Node externs, Node root) {
        new RenamePrototypes(compiler, false, null, null).process(externs, root);
      }
    });

    CompilerResult result = compiler.compile(compInput);

    List<Error> errors = result.getErrors();
    assertEquals(1, errors.size());
    Error error = errors.get(0);
    assertEquals("RenamePrototypes: conflict: 'bar'", error.getMessage());
  }
}