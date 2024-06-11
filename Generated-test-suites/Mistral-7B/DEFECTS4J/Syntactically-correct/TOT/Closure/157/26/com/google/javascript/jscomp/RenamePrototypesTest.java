import static org.junit.Assert.*;
import static com.google.javascript.jscomp.RenamePrototypes.*;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerResult;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.AbstractCompiler.LifeCycleStage;
import com.google.javascript.rhino.TokenStream;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

/**
 * RenamePrototypesTest tests the expected behavior of the {@code RenamePrototypes}
 * compiler pass.
 */
public class RenamePrototypesTest {

  /**
   * Test data for RenamePrototypes.
   */
  private static final String[] TEST_DATA = {
    // Test case 1: Simple prototype renaming
    "function Foo() {}\n" +
    "Foo.prototype.bar = function() {};\n" +
    "function Foo2() {}\n" +
    "Foo2.prototype.bar = function() {};\n" +
    "function Foo3() {}\n" +
    "Foo3.prototype.bar = Foo.prototype.bar;\n" +
    "assertRenamed('Foo.prototype.bar', 'Foo2.prototype.bar', 'baz');\n" +
    "assertRenamed('Foo3.prototype.bar', 'Foo2.prototype.bar', 'baz');\n" +
    // Test case 2: Simple object literal renaming
    "function Foo() {}\n" +
    "Foo.prototype.qux = { quux: 42 };\n" +
    "assertRenamed('Foo.prototype.qux.quux', 'Foo.prototype.qux.quux', 'quuxNew');\n" +
    // Test case 3: Aggressive prototype renaming
    "function Foo() {}\n" +
    "Foo.prototype.qux = function() {};\n" +
    "assertRenamed('Foo.prototype.qux', 'Foo.prototype.qux', 'newQux');\n" +
    // Test case 4: Multiple prototype and object literal properties sharing the same name
    "function Foo() {}\n" +
    "Foo.prototype.qux = function() {};\n" +
    "Foo.prototype.qux = { quux: 42 };\n" +
    "assertRenamed('Foo.prototype.qux', 'Foo.prototype.qux.quux', 'quuxNew');\n" +
    "assertRenamed('Foo.prototype.qux', 'Foo.prototype.qux', 'quxNew');\n" +
    // Test case 5: Renaming a property after it has already been defined as a method
    "function Foo() {}\n" +
    "Foo.prototype.qux = function() {};\n" +
    "Foo.prototype.qux = { quux: 42 };\n" +
    "assertRenamed('Foo.prototype.qux', 'Foo.prototype.qux.quux', 'quuxNew');\n" +
    "assertRenamed('Foo.prototype.qux', 'Foo.prototype.qux', 'quxNew');\n" +
    "assertRenamed('Foo.prototype.qux', 'Foo.prototype.qux', 'qux');\n" +
    // Test case 6: Renaming a property of a parent prototype
    "function Foo() {}\n" +
    "function Bar() {\n" +
    "  Foo.prototype.qux = function() {};\n" +
    "}\n" +
    "Bar.prototype.qux = function() {};\n" +
    "assertRenamed('Foo.prototype.qux', 'Bar.prototype.qux', 'quxNew');\n" +
    "assertRenamed('Bar.prototype.qux', 'Bar.prototype.qux', 'qux');\n" +
    // Test case 7: Renaming a property of a parent prototype with a leading underscore
    "function Foo() {}\n" +
    "function Bar() {\n" +
    "  Foo.prototype._qux = function() {};\n" +
    "}\n" +
    "Bar.prototype._qux = function() {};\n" +
    "assertRenamed('Foo.prototype._qux', 'Bar.prototype._qux', '_quxNew');\n" +
    "assertRenamed('Bar.prototype._qux', 'Bar.prototype._qux', '_qux');\n" +
    // Test case 8: Renaming a property of a parent prototype with a trailing underscore
    "function Foo() {}\n" +
    "function Bar() {\n" +
    "  Foo.prototype.qux_ = function() {};\n" +
    "}\n" +
    "Bar.prototype.qux_ = function() {};\n" +
    "assertRenamed('Foo.prototype.qux_', 'Bar.prototype.qux_', 'qux_New');\n" +
    "assertRenamed('Bar.prototype.qux_', 'Bar.prototype.qux_', 'qux_');\n" +
    // Test case 9: Renaming a property of a parent prototype with a reserved name
    "function Foo() {}\n" +
    "function Bar() {\n" +
    "  Foo.prototype.toString = function() {};\n" +
    "}\n" +
    "Bar.prototype.toString = function() {};\n" +
    "assertNotRenamed('Foo.prototype.toString', 'Bar.prototype.toString');\n" +
    "assertNotRenamed('Bar.prototype.toString', 'Bar.prototype.toString');\n"
  };

  /**
   * Tests that a property is renamed.
   *
   * @param oldName the old name of the property
   * @param newName the expected new name of the property
   * @param name the name to test
   */
  private static void assertRenamed(String oldName, String newName, String name) {
    CompilerOptions options = new CompilerOptions();
    options.setOutputName("output.js");
    Compiler compiler = new Compiler(options);
    CompilerPass renamePrototypes = new RenamePrototypes(compiler, true, null, null);

    List<String> inputList = Lists.newArrayList(TEST_DATA);
    List<Node> astList = compileAndGetASTList(compiler, inputList);

    RenamePrototypes pass = new RenamePrototypes(compiler, true, null, null);
    pass.process(astList.get(0), astList.get(1));

    CompilerResult result = compiler.getResult();
    String output = result.toSource();

    // Check that the property has been renamed in the output
    Function<String, String> findProperty = new Function<String, String>() {
      @Override
      public String apply(String input) {
        int startIndex = input.indexOf(name);
        if (startIndex == -1) {
          return null;
        }
        int propertyStartIndex = startIndex - 1;
        int propertyEndIndex = name.length();
        if (propertyStartIndex >= 0 && Character.isWhitespace(input.charAt(propertyStartIndex))) {
          propertyStartIndex--;
        }
        int declarationEndIndex = input.indexOf(';', propertyStartIndex);
        if (declarationEndIndex == -1) {
          declarationEndIndex = input.length();
        }
        return input.substring(propertyStartIndex, declarationEndIndex);
      }
    };

    String property = findProperty.apply(output);
    assertTrue(property != null);
    assertEquals(newName, property);
  }

  /**
   * Tests that a property is not renamed.
   *
   * @param oldName the old name of the property
   * @param newName the expected old name of the property
   */
  private static void assertNotRenamed(String oldName, String newName) {
    CompilerOptions options = new CompilerOptions();
    options.setOutputName("output.js");
    Compiler compiler = new Compiler(options);
    CompilerPass renamePrototypes = new RenamePrototypes(compiler, true, null, null);

    List<String> inputList = Lists.newArrayList(TEST_DATA);
    List<Node> astList = compileAndGetASTList(compiler, inputList);

    RenamePrototypes pass = new RenamePrototypes(compiler, true, null, null);
    pass.process(astList.get(0), astList.get(1));

    CompilerResult result = compiler.getResult();
    String output = result.toSource();

    // Check that the property has not been renamed in the output
    Function<String, String> findProperty = new Function<String, String>() {
      @Override
      public String apply(String input) {
        int startIndex = input.indexOf(oldName);
        if (startIndex == -1) {
          return null;
        }
        int propertyStartIndex = startIndex - 1;
        int propertyEndIndex = oldName.length();
        if (propertyStartIndex >= 0 && Character.isWhitespace(input.charAt(propertyStartIndex))) {
          propertyStartIndex--;
        }
        int declarationEndIndex = input.indexOf(';', propertyStartIndex);
        if (declarationEndIndex == -1) {
          declarationEndIndex = input.length();
        }
        return input.substring(propertyStartIndex, declarationEndIndex);
      }
    };

    String property = findProperty.apply(output);
    assertNull(property);
  }

  /**
   * Compiles the given sources and returns the corresponding ASTs.
   *
   * @param compiler the compiler to use
   * @param sources the sources to compile
   * @return the corresponding ASTs
   */
  private static List<Node> compileAndGetASTList(Compiler compiler, List<String> sources) {
    Writer errorWriter = new OutputStreamWriter(System.out);
    CompilerInput input = new CompilerInput(sources.toArray(new String[0]));
    CompilerPass[] passes = new CompilerPass[1];
    passes[0] = new BasicTreeRewriterPass();
    compiler.compile(input, null, passes);
    List<Node> astList = Lists.newArrayList();
    if (passes[0] instanceof AbstractCompiler.TreePrinter) {
      AbstractCompiler.TreePrinter printer = (AbstractCompiler.TreePrinter) passes[0];
      Node root = printer.getRootNode();
      for (int i = 0; i < root.getChildCount(); i++) {
        astList.add(root.getChild(i));
      }
    }
    return astList;
  }

  /**
   * Main method.
   *
   * @param args the arguments
   */
  public static void main(String[] args) throws IOException {
    List<String> inputList = Lists.newArrayList(TEST_DATA);
    List<Node> astList = compileAndGetASTList(new Compiler(), inputList);

    RenamePrototypes pass = new RenamePrototypes(new Compiler(), true, null, null);
    pass.process(astList.get(0), astList.get(1));

    CompilerResult result = new Compiler(new CompilerOptions()).compile(
        new CompilerInput(new String[]{"output.js"}), null, new CompilerPass[0]);

    Joiner joiner = Joiner.on("\n").skipNulls();
    assertEquals(joiner.join(inputList), joiner.join(result.getDiagnostics()));
  }
}