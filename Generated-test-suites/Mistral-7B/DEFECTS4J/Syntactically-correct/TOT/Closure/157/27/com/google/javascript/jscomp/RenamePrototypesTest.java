import static org.junit.Assert.*;
import static com.google.javascript.jscomp.RenamePrototypes.*;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.Parser;
import com.google.javascript.rhino.NodeFactory;

import java.io.IOException;
import java.io.StringReader;
import java.util.Map;

/**
 * Tests for the RenamePrototypes compiler pass.
 */
public class RenamePrototypesTest {

  @org.junit.Before
  public void setUp() {
    CompilerOptions options = new CompilerOptions(new CompilerOptions.Defaults());
    compiler = new Compiler(options);
  }

  /** The compiler instance for testing. */
  private Compiler compiler;

  /** The test input. */
  private String input =
      "function Foo() {}\n" +
      "Foo.prototype.bar = function() { return 1; };\n" +
      "Foo.prototype.qux = function() { return 2; };\n" +
      "var foo = new Foo();\n" +
      "var x = foo.bar;\n" +
      "var y = foo.qux;\n";

  /** The expected output. */
  private String expectedOutput =
      "function Foo() {}\n" +
      "Foo.prototype._c_ = function() { return 1; };\n" +
      "Foo.prototype._d_ = function() { return 2; };\n" +
      "var foo = new Foo();\n" +
      "var x = foo._c_();\n" +
      "var y = foo._d_();\n";

  /**
   * Test that the compiler correctly renames prototype properties.
   * @throws IOException
   */
  @org.junit.Test
  public void testRenamePrototypeProperties() throws IOException {
    CompilerInput compInput = new CompilerInput(new StringReader(input));
    Node ast = Parser.parse(compInput, null, null, new NodeFactory());
    RenamePrototypes pass = new RenamePrototypes(compiler, false, null, null);
    pass.process(compInput.getProgram(), ast);

    String actualOutput = compiler.getProgramText();
    assertEquals(expectedOutput, actualOutput);

    // Verify that the properties were actually renamed.
    Map<String, String> renamings = pass.getPropertyMap();
    assertEquals("expected 2 renamings, but got " + renamings.size(), 2, renamings.size());
    assertTrue("expected 'bar' to be renamed to '_c_', but got: " + renamings.get("bar"), renamings.get("bar").equals("_c_"));
    assertTrue("expected 'qux' to be renamed to '_d_', but got: " + renamings.get("qux"), renamings.get("qux").equals("_d_"));
  }

  /**
   * Test that the compiler does not rename exported properties.
   * @throws IOException
   */
  @org.junit.Test
  public void testDoNotRenameExportedProperties() throws IOException {
    input = "function Foo() {}\n" +
           "//# export Foo.prototype.bar = function() { return 1; };\n" +
           "Foo.prototype.qux = function() { return 2; };\n" +
           "var foo = new Foo();\n" +
           "var x = foo.bar;\n" +
           "var y = foo.qux;\n";

    expectedOutput = input;

    pass = new RenamePrototypes(compiler, false, null, null);
    pass.process(new CompilerInput(new StringReader(input)), null);

    assertEquals(expectedOutput, compiler.getProgramText());

    // Verify that 'bar' was not renamed.
    assertFalse("expected 'bar' to not be renamed", pass.properties.containsKey("bar"));
  }

  /**
   * Test that the compiler does not rename private properties.
   * @throws IOException
   */
  @org.junit.Test
  public void testDoNotRenamePrivateProperties() throws IOException {
    input = "function Foo() {}\n" +
           "Foo.prototype._bar = function() { return 1; };\n" +
           "Foo.prototype.qux = function() { return 2; };\n" +
           "var foo = new Foo();\n" +
           "var x = foo._bar;\n" +
           "var y = foo.qux;\n";

    expectedOutput = input;

    pass = new RenamePrototypes(compiler, false, null, null);
    pass.process(new CompilerInput(new StringReader(input)), null);

    assertEquals(expectedOutput, compiler.getProgramText());

    // Verify that '_bar' was not renamed.
    assertTrue("expected '_bar' to not be renamed", pass.properties.get("_bar") != null);
  }

  /**
   * Test that the compiler does not rename properties with only lowercase
   * letters.
   * @throws IOException
   */
  @org.junit.Test
  public void testDoNotRenameLowercaseProperties() throws IOException {
    input = "function Foo() {}\n" +
           "Foo.prototype.foo = function() { return 1; };\n" +
           "Foo.prototype.qux = function() { return 2; };\n" +
           "var foo = new Foo();\n" +
           "var x = foo.foo;\n" +
           "var y = foo.qux;\n";

    expectedOutput = input;

    pass = new RenamePrototypes(compiler, false, null, null);
    pass.process(new CompilerInput(new StringReader(input)), null);

    assertEquals(expectedOutput, compiler.getProgramText());

    // Verify that 'foo' was not renamed.
    assertTrue("expected 'foo' to not be renamed", pass.properties.get("foo") != null);
  }

  /**
   * Test that the compiler aggressively renames all properties, even those
   * with only lowercase letters.
   * @throws IOException
   */
  @org.junit.Test
  public void testAggressivelyRenameProperties() throws IOException {
    input = "function Foo() {}\n" +
           "Foo.prototype.foo = function() { return 1; };\n" +
           "Foo.prototype.qux = function() { return 2; };\n" +
           "var foo = new Foo();\n" +
           "var x = foo.foo;\n" +
           "var y = foo.qux;\n";

    expectedOutput = "function Foo() {}\n" +
                    "Foo.prototype._a_ = function() { return 1; };\n" +
                    "Foo.prototype._b_ = function() { return 2; };\n" +
                    "var foo = new Foo();\n" +
                    "var x = foo._a_;\n" +
                    "var y = foo._b_;\n";

    pass = new RenamePrototypes(compiler, true, null, null);
    pass.process(new CompilerInput(new StringReader(input)), null);

    assertEquals(expectedOutput, compiler.getProgramText());

    // Verify that 'foo' was renamed to '_a_'.
    assertTrue("expected 'foo' to be renamed to '_a_'", pass.properties.get("foo").newName.equals("_a_"));
  }

  /**
   * Test that the compiler does not rename properties assigned to PROTOTYPE
   * objects.
   * @throws IOException
   */
  @org.junit.Test
  public void testDoNotRenamePrototypeProperties() throws IOException {
    input = "function Foo() {}\n" +
           "Foo.prototype.foo = function() { return 1; };\n" +
           "Foo.prototype = {\n" +
           "  bar: function() { return 2; },\n" +
           "  qux: function() { return 3; }\n" +
           "};\n" +
           "var foo = new Foo();\n" +
           "var x = foo.foo;\n" +
           "var y = foo.bar;\n" +
           "var z = foo.qux;\n";

    expectedOutput = input;

    pass = new RenamePrototypes(compiler, false, null, null);
    pass.process(new CompilerInput(new StringReader(input)), null);

    assertEquals(expectedOutput, compiler.getProgramText());

    // Verify that 'bar' and 'qux' were not renamed.
    assertFalse("expected 'bar' to not be renamed", pass.properties.containsKey("bar"));
    assertFalse("expected 'qux' to not be renamed", pass.properties.containsKey("qux"));
  }

  /**
   * Test that the compiler renames both prototype properties and object
   * literal properties that satisfy the renaming policies.
   * @throws IOException
   */
  @org.junit.Test
  public void testRenameBothPrototypeAndObjectLiteralProperties() throws IOException {
    input = "function Foo() {}\n" +
           "Foo.prototype.foo = function() { return 1; };\n" +
           "Foo.prototype.qux = function() { return 2; };\n" +
           "Foo.prototype = {\n" +
           "  bar: function() { return 3; },\n" +
           "  baz: 'hello'\n" +
           "};\n" +
           "var foo = new Foo();\n" +
           "var x = foo.foo;\n" +
           "var y = foo.qux;\n" +
           "var z = foo.bar;\n" +
           "var w = foo.baz;\n";

    expectedOutput = "function Foo() {}\n" +
                    "Foo.prototype._a_ = function() { return 1; };\n" +
                    "Foo.prototype._b_ = function() { return 2; };\n" +
                    "Foo.prototype = {\n" +
                    "  _c_: function() { return 3; },\n" +
                    "  _d_: 'hello'\n" +
                    "};\n" +
                    "var foo = new Foo();\n" +
                    "var x = foo._a_;\n" +
                    "var y = foo._b_;\n" +
                    "var z = foo._c_;\n" +
                    "var w = foo._d_;\n";

    pass = new RenamePrototypes(compiler, false, null, null);
    pass.process(new CompilerInput(new StringReader(input)), null);

    assertEquals(expectedOutput, compiler.getProgramText());

    // Verify that 'foo', 'qux', 'bar', and 'baz' were all renamed.
    assertTrue("expected 'foo' to be renamed", pass.properties.get("foo").newName != null);
    assertTrue("expected 'qux' to be renamed", pass.properties.get("qux").newName != null);
    assertTrue("expected 'bar' to be renamed", pass.properties.get("bar").newName != null);
    assertTrue("expected 'baz' to be renamed", pass.properties.get("baz").newName != null);
  }

  /**
   * Test that the compiler properly handles duplicate property names.
   * @throws IOException
   */
  @org.junit.Test
  public void testHandleDuplicatePropertyNames() throws IOException {
    input = "function Foo() {}\n" +
           "Foo.prototype.foo = function() { return 1; };\n" +
           "Foo.prototype.foo = function() { return 2; };\n" +
           "Foo.prototype = {\n" +
           "  foo: function() { return 3; },\n" +
           "  qux: function() { return 4 }\n" +
           "};\n" +
           "var foo = new Foo();\n" +
           "var x = foo.foo;\n" +
           "var y = foo.qux;\n";

    expectedOutput = "function Foo() {}\n" +
                    "Foo.prototype._a_ = function() { return 1; };\n" +
                    "Foo.prototype._b_ = function() { return 2; };\n" +
                    "Foo.prototype = {\n" +
                    "  _c_: function() { return 3; },\n" +
                    "  qux: function() { return 4 }\n" +
                    "};\n" +
                    "var foo = new Foo();\n" +
                    "var x = foo._b_;\n" +
                    "var y = foo.qux;\n";

    pass = new RenamePrototypes(compiler, false, null, null);
    pass.process(new CompilerInput(new StringReader(input)), null);

    assertEquals(expectedOutput, compiler.getProgramText());

    // Verify that the first 'foo' was renamed to '_b_'.
    assertTrue("expected the first 'foo' to be renamed to '_b_'", pass.properties.get("foo").newName.equals("_b_"));
    // Verify that the second 'foo' was not renamed.
    assertTrue("expected the second 'foo' to not be renamed", pass.properties.get("foo").oldName.equals("foo"));
  }
}