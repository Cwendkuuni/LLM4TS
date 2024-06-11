import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.SimpleDefinitionFinder;
import org.junit.Before;
import org.junit.Test;

import static com.google.common.truth.Truth.assertThat;

public class DevirtualizePrototypeMethodsTest {

  private Compiler compiler;
  private CompilerOptions options;

  @Before
  public void setUp() {
    options = new CompilerOptions();
    compiler = new Compiler(options);
  }

  @Test
  public void testDevirtualizePrototypeMethods() {

    String input = "function A() {}\n" +
        "A.prototype.accumulate = function(value) { this.total += value; return this.total }\n" +
        "var a = new A();\n" +
        "var total = a.accumulate(2);\n";

    String expectedOutput = "var accumulate = function(self, value) { self.total += value; return self.total }\n" +
        "var a = new A();\n" +
        "var total = accumulate(a, 2);\n";

    Node ast = Parser.parseJavaScript(input);
    SimpleDefinitionFinder defFinder = new SimpleDefinitionFinder(compiler);
    defFinder.process(null, ast);

    DevirtualizePrototypeMethods pass = new DevirtualizePrototypeMethods(compiler);
    pass.process(null, ast);

    String output = Printer.printToJavaScriptString(ast, options);
    assertThat(output).isEqualTo(expectedOutput);
  }

  @Test
  public void testDevirtualizePrototypeMethodsWithMultipleDefinitions() {

    String input = "function A() {}\n" +
        "function B() {}\n" +
        "A.prototype.accumulate = function(value) { this.total += value; return this.total }\n" +
        "B.prototype.accumulate = function(value) { this.total += value; return this.total }\n" +
        "var a = new A();\n" +
        "var b = new B();\n" +
        "var totalA = a.accumulate(2);\n" +
        "var totalB = b.accumulate(3);\n";

    String expectedOutput = "var accumulateA = function(self, value) { self.total += value; return self.total }\n" +
        "var accumulateB = function(self, value) { self.total += value; return self.total }\n" +
        "var A = function() {}\n" +
        "var B = function() {}\n" +
        "var a = new A();\n" +
        "var b = new B();\n" +
        "var totalA = accumulateA(a, 2);\n" +
        "var totalB = accumulateB(b, 3);\n";

    Node ast = Parser.parseJavaScript(input);
    SimpleDefinitionFinder defFinder = new SimpleDefinitionFinder(compiler);
    defFinder.process(null, ast);

    DevirtualizePrototypeMethods pass = new DevirtualizePrototypeMethods(compiler);
    pass.process(null, ast);

    String output = Printer.printToJavaScriptString(ast, options);
    assertThat(output).isEqualTo(expectedOutput);
  }

  @Test
  public void testDevirtualizePrototypeMethodsWithExportedProperty() {

    String input = "function A() {}\n" +
        "A.prototype.accumulate = function(value) { this.total += value; return this.total }\n" +
        "module.exports = A.prototype.accumulate;\n";

    String expectedOutput = "function accumulate(value) { this.total += value; return this.total }\n" +
        "module.exports = accumulate;\n";

    Node ast = Parser.parseJavaScript(input);
    SimpleDefinitionFinder defFinder = new SimpleDefinitionFinder(compiler);
    defFinder.process(null, ast);

    DevirtualizePrototypeMethods pass = new DevirtualizePrototypeMethods(compiler);
    pass.process(null, ast);

    String output = Printer.printToJavaScriptString(ast, options);
    assertThat(output).isEqualTo(expectedOutput);
  }

  @Test
  public void testDevirtualizePrototypeMethodsWithControlStructures() {

    String input = "function A() {}\n" +
        "A.prototype.accumulate = function(value) { this.total += value; return this.total }\n" +
        "if (true) {\n" +
        "  A.prototype.accumulate = function(value) { return value }\n" +
        "}" +
        "var a = new A();\n" +
        "var total = a.accumulate(2);\n";

    String expectedOutput = "if (true) {\n" +
        "  var accumulate = function(value) { return value }\n" +
        "}" +
        "var A = function() {}\n" +
        "var a = new A();\n" +
        "var total = accumulate(a, 2);\n";

    Node ast = Parser.parseJavaScript(input);
    SimpleDefinitionFinder defFinder = new SimpleDefinitionFinder(compiler);
    defFinder.process(null, ast);

    DevirtualizePrototypeMethods pass = new DevirtualizePrototypeMethods(compiler);
    pass.process(null, ast);

    String output = Printer.printToJavaScriptString(ast, options);
    assertThat(output).isEqualTo(expectedOutput);
  }

  @Test
  public void testDevirtualizePrototypeMethodsWithNonFunctionDefinition() {

    String input = "function A() {}\n" +
        "A.prototype.accumulate = 123;\n";

    String expectedOutput = "function A() {}\n" +
        "A.prototype.accumulate = 123;\n";

    Node ast = Parser.parseJavaScript(input);
    SimpleDefinitionFinder defFinder = new SimpleDefinitionFinder(compiler);
    defFinder.process(null, ast);

    DevirtualizePrototypeMethods pass = new DevirtualizePrototypeMethods(compiler);
    pass.process(null, ast);

    String output = Printer.printToJavaScriptString(ast, options);
    assertThat(output).isEqualTo(expectedOutput);
  }

  @Test
  public void testDevirtualizePrototypeMethodsWithVariableAccess() {

    String input = "function A() {}\n" +
        "A.prototype.accumulate = function(value) { this.total += value; return this.total }\n" +
        "var a = new A();\n" +
        "console.log(this.total);\n";

    String expectedOutput = "function accumulate(self, value) { self.total += value; return self.total }\n" +
        "var a = new A();\n" +
        "console.log(a.total);\n";

    Node ast = Parser.parseJavaScript(input);
    SimpleDefinitionFinder defFinder = new SimpleDefinitionFinder(compiler);
    defFinder.process(null, ast);

    DevirtualizePrototypeMethods pass = new DevirtualizePrototypeMethods(compiler);
    pass.process(null, ast);

    String output = Printer.printToJavaScriptString(ast, options);
    assertThat(output).isEqualTo(expectedOutput);
  }

  @Test
  public void testDevirtualizePrototypeMethodsWithFunctionAccess() {

    String input = "function A() {}\n" +
        "A.prototype.accumulate = function(value) { return this.total + value }\n" +
        "function B() {}\n" +
        "B.prototype = new A();\n" +
        "var b = new B();\n" +
        "console.log(b.accumulate(2));\n";

    String expectedOutput = "function accumulate(self, value) { return self.total + value }\n" +
        "function B() {}\n" +
        "B.prototype = new Object(\n" +
        "  'accumulate': function(value) { return accumulate(this, value) }\n" +
        ")\n" +
        "var b = new B();\n" +
        "console.log(b.accumulate(2));\n";

    Node ast = Parser.parseJavaScript(input);
    SimpleDefinitionFinder defFinder = new SimpleDefinitionFinder(compiler);
    defFinder.process(null, ast);

    DevirtualizePrototypeMethods pass = new DevirtualizePrototypeMethods(compiler);
    pass.process(null, ast);

    String output = Printer.printToJavaScriptString(ast, options);
    assertThat(output).isEqualTo(expectedOutput);
  }

  @Test
  public void testDevirtualizePrototypeMethodsWithMultipleInheritance() {

    String input = "function A() {}\n" +
        "A.prototype.accumulate = function(value) { this.total += value; return this.total }\n" +
        "function B() {}\n" +
        "B.prototype = new A();\n" +
        "function C() {}\n" +
        "C.prototype = new B();\n" +
        "var c = new C();\n" +
        "console.log(c.accumulate(2));\n";

    String expectedOutput = "function accumulate(self, value) { self.total += value; return self.total }\n" +
        "function C() {}\n" +
        "C.prototype = new Object(\n" +
        "  'accumulate': function(value) { return accumulate(this, value) }\n" +
        ")\n" +
        "var c = new C();\n" +
        "console.log(c.accumulate(2));\n";

    Node ast = Parser.parseJavaScript(input);
    SimpleDefinitionFinder defFinder = new SimpleDefinitionFinder(compiler);
    defFinder.process(null, ast);

    DevirtualizePrototypeMethods pass = new DevirtualizePrototypeMethods(compiler);
    pass.process(null, ast);

    String output = Printer.printToJavaScriptString(ast, options);
    assertThat(output).isEqualTo(expectedOutput);
  }
}