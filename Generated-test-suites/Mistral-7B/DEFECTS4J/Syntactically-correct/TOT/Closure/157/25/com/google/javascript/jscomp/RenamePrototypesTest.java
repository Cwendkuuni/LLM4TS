import static org.junit.Assert.*;
import static com.google.javascript.jscomp.RenamePrototypes.*;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.ForwardReferenceResolver;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.Parser;
import com.google.javascript.rhino.Token;
import com.google.javascript.rhino.TokenStream;

import java.io.IOException;

public class RenamePrototypesTest {

  private static String input = "function Foo() {}\n" +
      "Foo.prototype.bar = function() { return 1; };\n" +
      "Foo.prototype.baz = function() { return 2; };\n" +
      "Foo.prototype.qux = function() { return 3; };\n" +
      "\n" +
      "var foos = [new Foo(), new Foo()];\n" +
      "function fooFunc() { return new Foo(); }\n" +
      "\n" +
      "Foo.prototype.x = 10;\n" +
      "Foo.prototype.y = 20;\n" +
      "\n" +
      "function test() {\n" +
      "  var f = new Foo();\n" +
      "  assertEquals(1, f.bar());\n" +
      "  assertEquals(2, f.baz());\n" +
      "  assertEquals(3, f.qux());\n" +
      "  assertEquals(10, f.x);\n" +
      "  assertEquals(20, f.y);\n" +
      "  assertEquals(1, foos[0].bar());\n" +
      "  assertEquals(1, foos[1].bar());\n" +
      "  assertEquals(1, foos[0].baz());\n" +
      "  assertEquals(1, foos[1].baz());\n" +
      "  assertEquals(1, foos[0].qux());\n" +
      "  assertEquals(1, foos[1].qux());\n" +
      "  assertEquals(10, foos[0].x);\n" +
      "  assertEquals(10, foos[1].x);\n" +
      "  assertEquals(20, foos[0].y);\n" +
      "  assertEquals(20, foos[1].y);\n" +
      "  assertEquals(1, fooFunc().bar());\n" +
      "}";

  @Test
  public void testRenamePrototypeProperties() throws IOException {
    CompilerOptions options = new CompilerOptions();
    options.setMaxInstances(1);
    options.setOutputFile("test.js");

    Parser parser = new Parser(new TokenStream(new ByteArrayInputStream(input.getBytes())));
    Compiler compiler = new Compiler(options);
    CompilerInput input = new CompilerInput("test.js", input.getBytes(), parser.parseProgram(input, null));
    ForwardReferenceResolver resolver = new ForwardReferenceResolver();

    compiler.addPass(new RenamePrototypes(compiler, false, null, null));
    compiler.process(input, resolver);

    Node rootNode = resolver.getRootNode();
    String expectedOutput = "function Foo() {}\n" +
        "Foo.prototype.a_bar = function() { return 1; };\n" +
        "Foo.prototype.a_baz = function() { return 2; };\n" +
        "Foo.prototype.a_qux = function() { return 3; };\n" +
        "\n" +
        "var foos = [new Foo(), new Foo()];\n" +
        "function fooFunc() { return new Foo(); }\n" +
        "\n" +
        "Foo.prototype.a_x = 10;\n" +
        "Foo.prototype.a_y = 20;\n" +
        "\n" +
        "function test() {\n" +
        "  var f = new Foo();\n" +
        "  assertEquals(1, f.a_bar());\n" +
        "  assertEquals(2, f.a_baz());\n" +
        "  assertEquals(3, f.a_qux());\n" +
        "  assertEquals(10, f.a_x);\n" +
        "  assertEquals(20, f.a_y);\n" +
        "  assertEquals(1, foos[0].a_bar());\n" +
        "  assertEquals(1, foos[1].a_bar());\n" +
        "  assertEquals(1, foos[0].a_baz());\n" +
        "  assertEquals(1, foos[1].a_baz());\n" +
        "  assertEquals(1, foos[0].a_qux());\n" +
        "  assertEquals(1, foos[1].a_qux());\n" +
        "  assertEquals(10, foos[0].a_x);\n" +
        "  assertEquals(10, foos[1].a_x);\n" +
        "  assertEquals(20, foos[0].a_y);\n" +
        "  assertEquals(20, foos[1].a_y);\n" +
        "  assertEquals(1, fooFunc().a_bar());\n" +
        "}";

    // Verify that the output was correctly generated
    Node outputNode = compiler.getDiagnostics().getOutputRoot();
    assertEquals(expectedOutput, outputNode.getPrintable(Printer.js));
  }

  @Test
  public void testNoRenamePrototypePropertiesWithAggressiveOptions() throws IOException {
    CompilerOptions options = new CompilerOptions();
    options.setMaxInstances(1);
    options.setOutputFile("test.js");
    options.setAggressiveRenaming(true);

    Parser parser = new Parser(new TokenStream(new ByteArrayInputStream(input.getBytes())));
    Compiler compiler = new Compiler(options);
    CompilerInput input = new CompilerInput("test.js", input.getBytes(), parser.parseProgram(input, null));
    ForwardReferenceResolver resolver = new ForwardReferenceResolver();

    compiler.addPass(new RenamePrototypes(compiler, true, null, null));
    compiler.process(input, resolver);

    Node rootNode = resolver.getRootNode();
    String expectedOutput = "function Foo() {}\n" +
        "Foo.prototype.bar = function() { return 1; };\n" +
        "Foo.prototype.baz = function() { return 2; };\n" +
        "Foo.prototype.qux = function() { return 3; };\n" +
        "\n" +
        "var foos = [new Foo(), new Foo()];\n" +
        "function fooFunc() { return new Foo(); }\n" +
        "\n" +
        "Foo.prototype.x = 10;\n" +
        "Foo.prototype.y = 20;\n" +
        "\n" +
        "function test() {\n" +
        "  var f = new Foo();\n" +
        "  assertEquals(1, f.bar());\n" +
        "  assertEquals(2, f.baz());\n" +
        "  assertEquals(3, f.qux());\n" +
        "  assertEquals(10, f.x);\n" +
        "  assertEquals(20, f.y);\n" +
        "  assertEquals(1, foos[0].bar());\n" +
        "  assertEquals(1, foos[1].bar());\n" +
        "  assertEquals(1, foos[0].baz());\n" +
        "  assertEquals(1, foos[1].baz());\n" +
        "  assertEquals(1, foos[0].qux());\n" +
        "  assertEquals(1, foos[1].qux());\n" +
        "  assertEquals(10, foos[0].x);\n" +
        "  assertEquals(10, foos[1].x);\n" +
        "  assertEquals(20, foos[0].y);\n" +
        "  assertEquals(20, foos[1].y);\n" +
        "  assertEquals(1, fooFunc().bar());\n" +
        "}";

    // Verify that the output was not changed
    Node outputNode = compiler.getDiagnostics().getOutputRoot();
    assertEquals(expectedOutput, outputNode.getPrintable(Printer.js));
  }

  @Test
  public void testNoRenameObjLitProperties() throws IOException {
    CompilerOptions options = new CompilerOptions();
    options.setMaxInstances(1);
    options.setOutputFile("test.js");

    Parser parser = new Parser(new TokenStream(new ByteArrayInputStream(input.getBytes())));
    Compiler compiler = new Compiler(options);
    CompilerInput input = new CompilerInput("test.js", input.getBytes(), parser.parseProgram(input, null));
    ForwardReferenceResolver resolver = new ForwardReferenceResolver();

    compiler.addPass(new RenamePrototypes(compiler, false, null, null));
    compiler.process(input, resolver);

    Node rootNode = resolver.getRootNode();
    String expectedOutput = "function Foo() {}\n" +
        "Foo.prototype.bar = function() { return 1; };\n" +
        "Foo.prototype.baz = function() { return 2; };\n" +
        "Foo.prototype.qux = function() { return 3; };\n" +
        "\n" +
        "var foos = [new Foo(), new Foo()];\n" +
        "function fooFunc() { return new Foo(); }\n" +
        "\n" +
        "Foo.prototype.x = 10;\n" +
        "Foo.prototype.y = 20;\n" +
        "\n" +
        "function test() {\n" +
        "  var f = new Foo();\n" +
        "  assertEquals(1, f.bar());\n" +
        "  assertEquals(2, f.baz());\n" +
        "  assertEquals(3, f.qux());\n" +
        "  assertEquals(10, f.x);\n" +
        "  assertEquals(20, f.y);\n" +
        "  assertEquals(1, foos[0].bar());\n" +
        "  assertEquals(1, foos[1].bar());\n" +
        "  assertEquals(1, foos[0].baz());\n" +
        "  assertEquals(1, foos[1].baz());\n" +
        "  assertEquals(1, foos[0].qux());\n" +
        "  assertEquals(1, foos[1].qux());\n" +
        "  assertEquals(10, foos[0].x);\n" +
        "  assertEquals(10, foos[1].x);\n" +
        "  assertEquals(20, foos[0].y);\n" +
        "  assertEquals(20, foos[1].y);\n" +
        "  assertEquals(1, fooFunc().bar());\n" +
        "}";

    // Verify that obj lit properties were not renamed
    Node fooObj = rootNode.getFirstChild().getNext();
    Node foosObj = fooObj.getLastChild();
    Node testFunc = foosObj.getLastChild();

    Node f = foosObj.getFirstChild().getNext();
    Node barCall = f.getFirstChild();
    assertEquals("1", barCall.getLastChild().getString());

    Node x = f.getLastChild();
    assertEquals("10", x.getString());

    Node quxCall = foosObj.getLastChild().getLastChild();
    assertEquals("1", quxCall.getLastChild().getString());
  }
}