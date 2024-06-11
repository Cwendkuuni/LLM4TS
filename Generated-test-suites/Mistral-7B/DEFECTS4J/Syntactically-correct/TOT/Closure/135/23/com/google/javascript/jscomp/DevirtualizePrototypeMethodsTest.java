import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.DevirtualizePrototypeMethods;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.SimpleDefinitionFinder;
import junit.framework.TestCase;

public class DevirtualizePrototypeMethodsTest extends TestCase {

 private Compiler compiler;
 private CompilerOptions options;

 @Override
 protected void setUp() throws Exception {
 super.setUp();

 options = new CompilerOptions();
 compiler = new Compiler(options);
 }

 public void testDevirtualizePrototypeMethods() throws Exception {
 String jsCode = "function A() {}\n" +
 "A.prototype.accumulate = function(value) { this.total += value; return this.total};\n" +
 "var a = new A();\n" +
 "var total = a.accumulate(2);\n";

 Node root = Parser.parseJs(jsCode, "Test", null);
 SimpleDefinitionFinder defFinder = new SimpleDefinitionFinder(compiler);
 defFinder.process(null, root);

 DevirtualizePrototypeMethods devirtualizer = new DevirtualizePrototypeMethods(compiler);
 devirtualizer.process(null, root);

 String expectedJsCode = "var accumulate = function(self, value) { self.total += value; return self.total };\n" +
 "var total = accumulate(a, 2);\n";

 String actualJsCode = Printer.printJs(root, Compiler.JS_PRETTY_PRINT);
 assertEquals(expectedJsCode, actualJsCode);
 }

 public void testDevirtualizePrototypeMethodsWithVarArgs() throws Exception {
 String jsCode = "function A() {}\n" +
 "A.prototype.printArgs = function() { console.log.apply(console, arguments) };\n" +
 "var a = new A();\n" +
 "a.printArgs(1, 2, 3);\n";

 Node root = Parser.parseJs(jsCode, "Test", null);
 SimpleDefinitionFinder defFinder = new SimpleDefinitionFinder(compiler);
 defFinder.process(null, root);

 DevirtualizePrototypeMethods devirtualizer = new DevirtualizePrototypeMethods(compiler);
 devirtualizer.process(null, root);

 String expectedJsCode = "var printArgs = function(self) { console.log.apply(console, arguments) };\n" +
 "printArgs(a);\n";

 String actualJsCode = Printer.printJs(root, Compiler.JS_PRETTY_PRINT);
 assertEquals(expectedJsCode, actualJsCode);
 }

 public void testDevirtualizePrototypeMethodsWithObjectLiterals() throws Exception {
 String jsCode = "var myObj = { foo: function() { this.total += 1 }};\n" +
 "myObj.foo();\n";

 Node root = Parser.parseJs(jsCode, "Test", null);
 SimpleDefinitionFinder defFinder = new SimpleDefinitionFinder(compiler);
 defFinder.process(null, root);

 DevirtualizePrototypeMethods devirtualizer = new DevirtualizePrototypeMethods(compiler);
 devirtualizer.process(null, root);

 String expectedJsCode = "var foo = function(self) { self.total += 1 };\n" +
 "foo(myObj);\n";

 String actualJsCode = Printer.printJs(root, Compiler.JS_PRETTY_PRINT);
 assertEquals(expectedJsCode, actualJsCode);
 }

 public void testDevirtualizePrototypeMethodsWithStaticMethods() throws Exception {
 String jsCode = "function A() {}\n" +
 "A.prototype.accumulate = function() { this.total += 1 };\n" +
 "A.myFunction = function() { console.log(\"Hello, World!\") };\n" +
 "var a = new A();\n" +
 "a.accumulate();\n" +
 "A.myFunction();\n";

 Node root = Parser.parseJs(jsCode, "Test", null);
 SimpleDefinitionFinder defFinder = new SimpleDefinitionFinder(compiler);
 defFinder.process(null, root);

 DevirtualizePrototypeMethods devirtualizer = new DevirtualizePrototypeMethods(compiler);
 devirtualizer.process(null, root);

 String expectedJsCode = "var accumulate = function(self) { self.total += 1 };\n" +
 "accumulate(a);\n" +
 "A.myFunction = function() { console.log(\"Hello, World!\") };\n" +
 "A.myFunction();\n";

 String actualJsCode = Printer.printJs(root, Compiler.JS_PRETTY_PRINT);
 assertEquals(expectedJsCode, actualJsCode);
 }

 public void testDevirtualizePrototypeMethodsWithCyclicDependencies() throws Exception {
 String jsCode = "function A() {}\n" +
 "A.prototype.funcA = function() { this.funcB(); };\n" +
 "A.prototype.funcB = function() { this.funcA(); };\n" +
 "var a = new A();\n";

 Node root = Parser.parseJs(jsCode, "Test", null);
 SimpleDefinitionFinder defFinder = new SimpleDefinitionFinder(compiler);
 defFinder.process(null, root);

 DevirtualizePrototypeMethods devirtualizer = new DevirtualizePrototypeMethods(compiler);
 devirtualizer.process(null, root);

 try {
 devirtualizer.process(null, root);
 fail("Exception expected");
 } catch (RuntimeException e) {
 // expected
 }

 String expectedJsCode = "Error: Cyclic dependencies detected";
 // This test is expected to fail due to cyclic dependencies
 String actualJsCode = Printer.printJs(root, Compiler.JS_PRETTY_PRINT);
 }

 public void testDevirtualizePrototypeMethodsWithExportedProperties() throws Exception {
 String jsCode = "function A() {}\n" +
 "A.prototype.func = function() { this.total += 1 };\n" +
 "exports.func = A.prototype.func;\n";

 Node root = Parser.parseJs(jsCode, "Test", null);
 SimpleDefinitionFinder defFinder = new SimpleDefinitionFinder(compiler);
 defFinder.process(null, root);

 DevirtualizePrototypeMethods devirtualizer = new DevirtualizePrototypeMethods(compiler);
 devirtualizer.process(null, root);

 String expectedJsCode = "exports.func = function(self) { self.total += 1 };\n";

 String actualJsCode = Printer.printJs(root, Compiler.JS_PRETTY_PRINT);
 assertEquals(expectedJsCode, actualJsCode);
 }
}