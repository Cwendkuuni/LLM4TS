import static org.junit.Assert.*;
import static com.google.javascript.jscomp.RenamePrototypes.*;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilerResult;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.ParserStage;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.TokenStream;

import java.io.IOException;
import java.util.Arrays;

/**
 * Tests for the RenamePrototypes compiler pass.
 */
public class RenamePrototypesTest {

  @org.junit.Test
  public void testRenamePrototypeProperties() throws Exception {
    String jsCode = "function Foo() {}\n" +
                    "Foo.prototype.bar = function() {}\n" +
                    "Foo.prototype.baz = function() {}\n" +
                    "Foo.prototype.qux = function() {}\n" +
                    "var x = new Foo();\n" +
                    "x.bar();\n" +
                    "x.baz();\n" +
                    "x.qux();";

    CompilerOptions options = new CompilerOptions();
    options.setAssumeFunctionTypesForStatements(true);
    options.setParseInputAsEsModule(false);
    options.setWarningsAsErrors(true);

    Compiler compiler = new Compiler();
    CompilerPass pass = new RenamePrototypes(compiler, true, null, null);

    TokenStream input = new TokenStream(new NodeFactory(), new ParserStage());
    input.putStream(ParserStage.parse(jsCode));

    compiler.process(input, options, pass, null, null);

    CompilerResult result = compiler.getResult();
    Node root = result.getOutput();

    // Check that the properties have been renamed
    assertEquals("function Foo() {}\n" +
                 "Foo.prototype.foo_bar = function() {}\n" +
                 "Foo.prototype.foo_baz = function() {}\n" +
                 "Foo.prototype.foo_qux = function() {}\n" +
                 "var x = new Foo();\n" +
                 "x.foo_bar();\n" +
                 "x.foo_baz();\n" +
                 "x.foo_qux();",
                 NodePrinter.printTree(root));
  }

  @org.junit.Test
  public void testDontRenamePrototypePropertiesWithLeadingUnderscore()
      throws Exception {
    String jsCode = "function Foo() {}\n" +
                    "Foo.prototype._bar = function() {}\n" +
                    "Foo.prototype._baz = function() {}\n" +
                    "Foo.prototype._qux = function() {}\n" +
                    "var x = new Foo();\n" +
                    "x._bar();\n" +
                    "x._baz();\n" +
                    "x._qux();";

    CompilerOptions options = new CompilerOptions();
    options.setAssumeFunctionTypesForStatements(true);
    options.setParseInputAsEsModule(false);
    options.setWarningsAsErrors(true);

    Compiler compiler = new Compiler();
    CompilerPass pass = new RenamePrototypes(compiler, true, null, null);

    TokenStream input = new TokenStream(new NodeFactory(), new ParserStage());
    input.putStream(ParserStage.parse(jsCode));

    compiler.process(input, options, pass, null, null);

    CompilerResult result = compiler.getResult();
    Node root = result.getOutput();

    // Check that the properties have not been renamed
    assertEquals("function Foo() {}\n" +
                 "Foo.prototype._bar = function() {}\n" +
                 "Foo.prototype._baz = function() {}\n" +
                 "Foo.prototype._qux = function() {}\n" +
                 "var x = new Foo();\n" +
                 "x._bar();\n" +
                 "x._baz();\n" +
                 "x._qux();",
                 NodePrinter.printTree(root));
  }

  @org.junit.Test
  public void testDontRenamePrototypePropertiesWithTrailingUnderscore()
      throws Exception {
    String jsCode = "function Foo() {}\n" +
                    "Foo.prototype.bar_ = function() {}\n" +
                    "Foo.prototype.baz_ = function() {}\n" +
                    "Foo.prototype.qux_ = function() {}\n" +
                    "var x = new Foo();\n" +
                    "x.bar();\n" +
                    "x.baz();\n" +
                    "x.qux();";

    CompilerOptions options = new CompilerOptions();
    options.setAssumeFunctionTypesForStatements(true);
    options.setParseInputAsEsModule(false);
    options.setWarningsAsErrors(true);

    Compiler compiler = new Compiler();
    CompilerPass pass = new RenamePrototypes(compiler, true, null, null);

    TokenStream input = new TokenStream(new NodeFactory(), new ParserStage());
    input.putStream(ParserStage.parse(jsCode));

    compiler.process(input, options, pass, null, null);

    CompilerResult result = compiler.getResult();
    Node root = result.getOutput();

    // Check that the properties have not been renamed
    assertEquals("function Foo() {}\n" +
                 "Foo.prototype.bar_ = function() {}\n" +
                 "Foo.prototype.baz_ = function() {}\n" +
                 "Foo.prototype.qux_ = function() {}\n" +
                 "var x = new Foo();\n" +
                 "x.bar();\n" +
                 "x.baz();\n" +
                 "x.qux();",
                 NodePrinter.printTree(root));
  }

  @org.junit.Test
  public void testRenameObjLitProperties() throws Exception {
    String jsCode = "var myObj = {\n" +
                    "  bar: function() {},\n" +
                    "  baz: function() {},\n" +
                    "  qux: function() {}\n" +
                    "};\n" +
                    "myObj.bar();\n" +
                    "myObj.baz();\n" +
                    "myObj.qux();";

    CompilerOptions options = new CompilerOptions();
    options.setAssumeFunctionTypesForStatements(true);
    options.setParseInputAsEsModule(false);
    options.setWarningsAsErrors(true);

    Compiler compiler = new Compiler();
    CompilerPass pass = new RenamePrototypes(compiler, true, null, null);

    TokenStream input = new TokenStream(new NodeFactory(), new ParserStage());
    input.putStream(ParserStage.parse(jsCode));

    compiler.process(input, options, pass, null, null);

    CompilerResult result = compiler.getResult();
    Node root = result.getOutput();

    // Check that the properties have been renamed
    assertEquals("var myObj = {\n" +
                 "  foo_bar: function() {},\n" +
                 "  foo_baz: function() {},\n" +
                 "  foo_qux: function() {}\n" +
                 "};\n" +
                 "myObj.foo_bar();\n" +
                 "myObj.foo_baz();\n" +
                 "myObj.foo_qux();",
                 NodePrinter.printTree(root));
  }

  @org.junit.Test
  public void testDontRenameObjLitPropertiesWithLeadingUnderscore()
      throws Exception {
    String jsCode = "var myObj = {\n" +
                    "  _bar: function() {},\n" +
                    "  _baz: function() {},\n" +
                    "  _qux: function() {}\n" +
                    "};\n" +
                    "myObj._bar();\n" +
                    "myObj._baz();\n" +
                    "myObj._qux();";

    CompilerOptions options = new CompilerOptions();
    options.setAssumeFunctionTypesForStatements(true);
    options.setParseInputAsEsModule(false);
    options.setWarningsAsErrors(true);

    Compiler compiler = new Compiler();
    CompilerPass pass = new RenamePrototypes(compiler, true, null, null);

    TokenStream input = new TokenStream(new NodeFactory(), new ParserStage());
    input.putStream(ParserStage.parse(jsCode));

    compiler.process(input, options, pass, null, null);

    CompilerResult result = compiler.getResult();
    Node root = result.getOutput();

    // Check that the properties have not been renamed
    assertEquals("var myObj = {\n" +
                 "  _bar: function() {},\n" +
                 "  _baz: function() {},\n" +
                 "  _qux: function() {}\n" +
                 "};\n" +
                 "myObj._bar();\n" +
                 "myObj._baz();\n" +
                 "myObj._qux();",
                 NodePrinter.printTree(root));
  }

  @org.junit.Test
  public void testDontRenameObjLitPropertiesWithTrailingUnderscore()
      throws Exception {
    String jsCode = "var myObj = {\n" +
                    "  bar_: function() {},\n" +
                    "  baz_: function() {},\n" +
                    "  qux_: function() {}\n" +
                    "};\n" +
                    "myObj.bar();\n" +
                    "myObj.baz();\n" +
                    "myObj.qux();";

    CompilerOptions options = new CompilerOptions();
    options.setAssumeFunctionTypesForStatements(true);
    options.setParseInputAsEsModule(false);
    options.setWarningsAsErrors(true);

    Compiler compiler = new Compiler();
    CompilerPass pass = new RenamePrototypes(compiler, true, null, null);

    TokenStream input = new TokenStream(new NodeFactory(), new ParserStage());
    input.putStream(ParserStage.parse(jsCode));

    compiler.process(input, options, pass, null, null);

    CompilerResult result = compiler.getResult();
    Node root = result.getOutput();

    // Check that the properties have not been renamed
    assertEquals("var myObj = {\n" +
                 "  bar_: function() {},\n" +
                 "  baz_: function() {},\n" +
                 "  qux_: function() {}\n" +
                 "};\n" +
                 "myObj.bar();\n" +
                 "myObj.baz();\n" +
                 "myObj.qux();",
                 NodePrinter.printTree(root));
  }

  @org.junit.Test
  public void testRenamePrototypePropertiesWithAggressiveRenaming()
      throws Exception {
    String jsCode = "function Foo() {}\n" +
                    "Foo.prototype.foo = function() {}\n" +
                    "Foo.prototype.bar = function() {}\n" +
                    "Foo.prototype.baz = function() {}\n" +
                    "var x = new Foo();\n" +
                    "x.foo();\n" +
                    "x.bar();\n" +
                    "x.baz();";

    CompilerOptions options = new CompilerOptions();
    options.setAssumeFunctionTypesForStatements(true);
    options.setParseInputAsEsModule(false);
    options.setWarningsAsErrors(true);

    Compiler compiler = new Compiler();
    CompilerPass pass = new RenamePrototypes(compiler, true, null, null);

    TokenStream input = new TokenStream(new NodeFactory(), new ParserStage());
    input.putStream(ParserStage.parse(jsCode));

    compiler.process(input, options, pass, null, null);

    CompilerResult result = compiler.getResult();
    Node root = result.getOutput();

    // Check that the properties have been aggressively renamed
    assertEquals("function Foo() {}\n" +
                 "Foo.prototype.foo_foo = function() {}\n" +
                 "Foo.prototype.foo_bar = function() {}\n" +
                 "Foo.prototype.foo_baz = function() {}\n" +
                 "var x = new Foo();\n" +
                 "x.foo_foo();\n" +
                 "x.foo_bar();\n" +
                 "x.foo_baz();",
                 NodePrinter.printTree(root));
  }

  @org.junit.Test
  public void testDontRenamePrototypePropertiesWithReservedNames()
      throws Exception {
    String jsCode = "function Foo() {}\n" +
                    "Foo.prototype.indexOf = function() {}\n" +
                    "Foo.prototype.lastIndexOf = function() {}\n" +
                    "Foo.prototype.toString = function() {}\n" +
                    "Foo.prototype.valueOf = function() {}\n" +
                    "var x = new Foo();\n" +
                    "x.indexOf();\n" +
                    "x.lastIndexOf();\n" +
                    "x.toString();\n" +
                    "x.valueOf();";

    CompilerOptions options = new CompilerOptions();
    options.setAssumeFunctionTypesForStatements(true);
    options.setParseInputAsEsModule(false);
    options.setWarningsAsErrors(true);

    Compiler compiler = new Compiler();
    CompilerPass pass = new RenamePrototypes(compiler, true, null, null);

    TokenStream input = new TokenStream(new NodeFactory(), new ParserStage());
    input.putStream(ParserStage.parse(jsCode));

    compiler.process(input, options, pass, null, null);

    CompilerResult result = compiler.getResult();
    Node root = result.getOutput();

    // Check that the reserved property names have not been renamed
    assertEquals("function Foo() {}\n" +
                 "Foo.prototype.indexOf = function() {}\n" +
                 "Foo.prototype.lastIndexOf = function() {}\n" +
                 "Foo.prototype.toString = function() {}\n" +
                 "Foo.prototype.valueOf = function() {}\n" +
                 "var x = new Foo();\n" +
                 "x.indexOf();\n" +
                 "x.lastIndexOf();\n" +
                 "x.toString();\n" +
                 "x.valueOf();",
                 NodePrinter.printTree(root));
  }
}