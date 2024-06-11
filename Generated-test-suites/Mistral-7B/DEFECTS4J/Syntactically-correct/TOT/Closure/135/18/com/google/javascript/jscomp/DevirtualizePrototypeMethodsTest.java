import static com.google.javascript.jscomp.DevirtualizePrototypeMethods.*;
import static org.junit.Assert.*;

import com.google.common.collect.ImmutableList;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.SimpleDefinitionFinder;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.Token;

import java.util.List;

public class DevirtualizePrototypeMethodsTest {

  private Compiler compiler;
  private NodeFactory nodeFactory;

  @Before
  public void setUp() {
    compiler = new Compiler();
    nodeFactory = compiler.getFactory();
    compiler.setOptions(new CompilerOptions());
  }

  @Test
  public void testDevirtualizePrototypeMethods() {
    String sourceCode =
        "function A() {}\n" +
        "A.prototype.accumulate = function(value) { return this.total += value; }\n" +
        "var a = new A();\n" +
        "var total = a.accumulate(2);\n" +
        "assertEquals(4, total);\n";

    Node externs = nodeFactory.compile(sourceCode);
    Node root = nodeFactory.parseTemplate("", externs);

    SimpleDefinitionFinder defFinder = new SimpleDefinitionFinder(compiler);
    defFinder.process(externs, root);

    DevirtualizePrototypeMethods pass = new DevirtualizePrototypeMethods(compiler);
    pass.process(externs, root);

    Node rewrittenCode = compiler.getOutput();

    String expectedCode =
        "var accumulate = function(self, value) { return self.total += value; }\n" +
        "var a = new A();\n" +
        "var total = accumulate(a, 2);\n" +
        "assertEquals(4, total);\n";

    assertEquals(expectedCode, nodeFactory.printToString(rewrittenCode));
  }

  @Test
  public void testDevirtualizePrototypeMethodsWithMultipleCallsites() {
    String sourceCode =
        "function A() {}\n" +
        "A.prototype.accumulate = function(value) { return this.total += value; }\n" +
        "function B() {}\n" +
        "B.prototype.accumulateMore = function() { this.total = this.total + 1; }\n" +
        "var a = new A();\n" +
        "var b = new B();\n" +
        "a.accumulate(2);\n" +
        "b.accumulateMore();\n" +
        "var total = a.accumulate(3);\n" +
        "assertEquals(7, total);\n";

    Node externs = nodeFactory.compile(sourceCode);
    Node root = nodeFactory.parseTemplate("", externs);

    SimpleDefinitionFinder defFinder = new SimpleDefinitionFinder(compiler);
    defFinder.process(externs, root);

    DevirtualizePrototypeMethods pass = new DevirtualizePrototypeMethods(compiler);
    pass.process(externs, root);

    Node rewrittenCode = compiler.getOutput();

    String expectedCode =
        "var accumulate = function(self, value) { return self.total += value; }\n" +
        "var accumulateMore = function(self) { return self.total += 1; }\n" +
        "var a = new A();\n" +
        "var b = new B();\n" +
        "accumulate(a, 2);\n" +
        "accumulateMore(b);\n" +
        "var total = accumulate(a, 3);\n" +
        "assertEquals(7, total);\n";

    assertEquals(expectedCode, nodeFactory.printToString(rewrittenCode));
  }

  @Test
  public void testDevirtualizePrototypeMethodsWithStaticMethods() {
    String sourceCode =
        "function A() {}\n" +
        "A.prototype.accumulate = function(value) { return this.total += value; }\n" +
        "A.staticMethod = function() { return 'static'; }\n" +
        "var a = new A();\n" +
        "assertEquals('static', A.staticMethod());\n";

    Node externs = nodeFactory.compile(sourceCode);
    Node root = nodeFactory.parseTemplate("", externs);

    SimpleDefinitionFinder defFinder = new SimpleDefinitionFinder(compiler);
    defFinder.process(externs, root);

    DevirtualizePrototypeMethods pass = new DevirtualizePrototypeMethods(compiler);
    pass.process(externs, root);

    Node rewrittenCode = compiler.getOutput();

    String expectedCode =
        "var accumulate = function(self, value) { return self.total += value; }\n" +
        "function staticMethod() { return 'static'; }\n" +
        "var a = new A();\n" +
        "assertEquals('static', staticMethod());\n";

    assertEquals(expectedCode, nodeFactory.printToString(rewrittenCode));
  }

  @Test
  public void testDevirtualizePrototypeMethodsWithMulticall() {
    String sourceCode =
        "function A() {}\n" +
        "A.prototype.accumulate = function(value) { this.total += value; return this.total; }\n" +
        "function B() {}\n" +
        "B.prototype.accumulateMore = function() { this.total += 1; }\n" +
        "var a = new A();\n" +
        "var b = new B();\n" +
        "a.total = 2;\n" +
        "b.total = 1;\n" +
        "assertEquals(3, a.accumulate(1).accumulateMore());\n" +
        "assertEquals(2, b.accumulateMore().accumulate(1));\n";

    Node externs = nodeFactory.compile(sourceCode);
    Node root = nodeFactory.parseTemplate("", externs);

    SimpleDefinitionFinder defFinder = new SimpleDefinitionFinder(compiler);
    defFinder.process(externs, root);

    DevirtualizePrototypeMethods pass = new DevirtualizePrototypeMethods(compiler);
    pass.process(externs, root);

    Node rewrittenCode = compiler.getOutput();

    String expectedCode =
        "var accumulate = function(self, value) { self.total += value; return self.total; }\n" +
        "function accumulateMore() { return self.total += 1; }\n" +
        "var a = new A();\n" +
        "var b = new B();\n" +
        "a.total = 2;\n" +
        "b.total = 1;\n" +
        "assertEquals(3, accumulate(a, 1).accumulateMore());\n" +
        "assertEquals(2, accumulateMore().accumulate(b));\n";

    assertEquals(expectedCode, nodeFactory.printToString(rewrittenCode));
  }

  @Test
  public void testDevirtualizePrototypeMethodsWithGetters() {
    String sourceCode =
        "function A() {}\n" +
        "A.prototype.getTotal = function() { return this.total; }\n" +
        "var a = new A();\n" +
        "a.total = 2;\n" +
        "assertEquals(2, a.getTotal());\n";

    Node externs = nodeFactory.compile(sourceCode);
    Node root = nodeFactory.parseTemplate("", externs);

    SimpleDefinitionFinder defFinder = new SimpleDefinitionFinder(compiler);
    defFinder.process(externs, root);

    DevirtualizePrototypeMethods pass = new DevirtualizePrototypeMethods(compiler);
    pass.process(externs, root);

    Node rewrittenCode = compiler.getOutput();

    String expectedCode =
        "function getTotal() { return this.total; }\n" +
        "var a = new A();\n" +
        "a.total = 2;\n" +
        "assertEquals(2, getTotal());\n";

    assertEquals(expectedCode, nodeFactory.printToString(rewrittenCode));
  }

  @Test
  public void testDevirtualizePrototypeMethodsWithSetters() {
    String sourceCode =
        "function A() { this.total = 0; }\n" +
        "A.prototype.setTotal = function(value) { this.total = value; }\n" +
        "var a = new A();\n" +
        "assertEquals(0, a.total);\n" +
        "a.setTotal(2);\n" +
        "assertEquals(2, a.total);\n";

    Node externs = nodeFactory.compile(sourceCode);
    Node root = nodeFactory.parseTemplate("", externs);

    SimpleDefinitionFinder defFinder = new SimpleDefinitionFinder(compiler);
    defFinder.process(externs, root);

    DevirtualizePrototypeMethods pass = new DevirtualizePrototypeMethods(compiler);
    pass.process(externs, root);

    Node rewrittenCode = compiler.getOutput();

    String expectedCode =
        "var setTotal = function(value) { this.total = value; }\n" +
        "var a = new A();\n" +
        "assertEquals(0, a.total);\n" +
        "setTotal.call(a, 2);\n" +
        "assertEquals(2, a.total);\n";

    assertEquals(expectedCode, nodeFactory.printToString(rewrittenCode));
  }

  @Test
  public void testDevirtualizePrototypeMethodsWithExportedMethod() {
    String sourceCode =
        "function A() {}\n" +
        "A.prototype.accumulate = function(value) { return this.total += value; }\n" +
        "export function exportedMethod() { return this.accumulate(1); }\n" +
        "var a = new A();\n" +
        "assertEquals(1, exportedMethod());\n";

    Node externs = nodeFactory.compile(sourceCode);
    Node root = nodeFactory.parseTemplate("", externs);

    SimpleDefinitionFinder defFinder = new SimpleDefinitionFinder(compiler);
    defFinder.process(externs, root);

    DevirtualizePrototypeMethods pass = new DevirtualizePrototypeMethods(compiler);
    pass.process(externs, root);

    Node rewrittenCode = compiler.getOutput();

    String expectedCode =
        "export function exportedMethod() { return accumulate(1); }\n" +
        "var a = new A();\n" +
        "assertEquals(1, exportedMethod());\n";

    assertEquals(expectedCode, nodeFactory.printToString(rewrittenCode));
  }

  @Test
  public void testDevirtualizePrototypeMethodsWithExportedMethodWithStaticMethod() {
    String sourceCode =
        "function A() {}\n" +
        "A.prototype.accumulate = function(value) { return this.total += value; }\n" +
        "export function exportedMethod() { return this.accumulate(1); }\n" +
        "A.staticMethod = function() { return 'static'; }\n" +
        "var a = new A();\n" +
        "assertEquals('static', A.staticMethod());\n" +
        "assertEquals(1, exportedMethod());\n";

    Node externs = nodeFactory.compile(sourceCode);
    Node root = nodeFactory.parseTemplate("", externs);

    SimpleDefinitionFinder defFinder = new SimpleDefinitionFinder(compiler);
    defFinder.process(externs, root);

    DevirtualizePrototypeMethods pass = new DevirtualizePrototypeMethods(compiler);
    pass.process(externs, root);

    Node rewrittenCode = compiler.getOutput();

    String expectedCode =
        "export function exportedMethod() { return accumulate(1); }\n" +
        "function staticMethod() { return 'static'; }\n" +
        "var a = new A();\n" +
        "assertEquals('static', staticMethod());\n" +
        "assertEquals(1, exportedMethod());\n";

    assertEquals(expectedCode, nodeFactory.printToString(rewrittenCode));
  }

  @Test
  public void testDevirtualizePrototypeMethodsWithFunctionDeclaration() {
    String sourceCode =
        "function A() {}\n" +
        "A.prototype.accumulate = function(value) { return this.total += value; }\n" +
        "function B() {}\n" +
        "B.prototype.accumulateMore = function() { this.total += 1; }\n" +
        "var a = new A();\n" +
        "var b = new B();\n" +
        "a.total = 2;\n" +
        "b.total = 1;\n" +
        "assertEquals(3, a.accumulate(1).accumulateMore());\n" +
        "assertEquals(2, b.accumulateMore().accumulate(1));\n";

    Node externs = nodeFactory.compile(sourceCode);
    Node root = nodeFactory.parseTemplate("", externs);

    SimpleDefinitionFinder defFinder = new SimpleDefinitionFinder(compiler);
    defFinder.process(externs, root);

    DevirtualizePrototypeMethods pass = new DevirtualizePrototypeMethods(compiler);
    pass.process(externs, root);

    Node rewrittenCode = compiler.getOutput();

    String expectedCode =
        "function accumulate(self, value) { return self.total += value; }\n" +
        "function accumulateMore() { return self.total += 1; }\n" +
        "var a = new A();\n" +
        "var b = new B();\n" +
        "a.total = 2;\n" +
        "b.total = 1;\n" +
        "assertEquals(3, accumulate(a, 1).accumulateMore());\n" +
        "assertEquals(2, accumulateMore().accumulate(b));\n";

    assertEquals(expectedCode, nodeFactory.printToString(rewrittenCode));
  }

  @Test
  public void testDevirtualizePrototypeMethodsWithMulticallWithFunctionDeclaration() {
    String sourceCode =
        "function A() { this.total = 0; }\n" +
        "A.prototype.accumulate = function(value) { this.total += value; return this.total; }\n" +
        "function B() { this.total = 0; }\n" +
        "B.prototype.accumulateMore = function() { this.total += 1; return this.total; }\n" +
        "var a = new A();\n" +
        "var b = new B();\n" +
        "assertEquals(0, a.accumulate(1).accumulateMore());\n" +
        "assertEquals(0, b.accumulateMore().accumulate(1));\n";

    Node externs = nodeFactory.compile(sourceCode);
    Node root = nodeFactory.parseTemplate("", externs);

    SimpleDefinitionFinder defFinder = new SimpleDefinitionFinder(compiler);
    defFinder.process(externs, root);

    DevirtualizePrototypeMethods pass = new DevirtualizePrototypeMethods(compiler);
    pass.process(externs, root);

    Node rewrittenCode = compiler.getOutput();

    String expectedCode =
        "function accumulate(self, value) { self.total += value; return self.total; }\n" +
        "function accumulateMore() { return self.total += 1; }\n" +
        "var a = new A();\n" +
        "var b = new B();\n" +
        "assertEquals(0, accumulate(a, 1).accumulateMore());\n" +
        "assertEquals(0, accumulateMore().accumulate(b));\n";

    assertEquals(expectedCode, nodeFactory.printToString(rewrittenCode));
  }
}