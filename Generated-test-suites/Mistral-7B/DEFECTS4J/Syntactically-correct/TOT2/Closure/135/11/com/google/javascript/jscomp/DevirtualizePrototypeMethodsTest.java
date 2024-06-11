import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.DevirtualizePrototypeMethods;
import com.google.javascript.jscomp.Node;
import com.google.javascript.rhino.ParseException;

import org.junit.Before;
import org.junit.Test;

public class DevirtualizePrototypeMethodsTest {

 private Compiler compiler;
 private CompilerPass devirtualizer;

 @Before
 public void setup() {
 compiler = new Compiler();
 compiler.setOptions(new CompilerOptions());
 devirtualizer = new DevirtualizePrototypeMethods(compiler);
 }

 @Test
 public void testSimpleDevirtualization() {
 String input = "function A() {}\n" +
     "A.prototype.accumulate = function(value) { this.total += value; return this.total };\n" +
     "var a = new A();\n" +
     "var total = a.accumulate(2);\n";

 Node root = compiler.parseTree(input, null);
 devirtualizer.process(null, root);
 Node expectedOutput = expectation("JSCompiler_StaticMethods_accumulate",
     "(function(self, value) { self.total += value; return self.total })",
     "(function(a, value) { var total = JSCompiler_StaticMethods_accumulate(a, value); return total })",
     input);
 assertEquals(expectedOutput.toPrintableTree(), root.toPrintableTree());
 }

 @Test
 public void testDevirtualizationWithArguments() {
 String input = "function A() {}\n" +
     "A.prototype.accumulate = function(a, b, c) { this.total += a + b + c; return this.total };\n" +
     "var a = new A();\n" +
     "var total = a.accumulate(1, 2, 3);\n";

 Node root = compiler.parseTree(input, null);
 devirtualizer.process(null, root);
 Node expectedOutput = expectation("JSCompiler_StaticMethods_accumulate",
     "(function(self, a, b, c) { self.total += a + b + c; return self.total })",
     "(function(a, b, c) { var total = JSCompiler_StaticMethods_accumulate(a, a, b, c); return total })",
     input);
 assertEquals(expectedOutput.toPrintableTree(), root.toPrintableTree());
 }

 @Test
 public void testDevirtualizationWithReturnValue() {
 String input = "function A() {}\n" +
     "A.prototype.accumulate = function(value) { return this.total += value; };\n" +
     "var a = new A();\n" +
     "var total = a.accumulate(2);\n" +
     "assertEquals(4, total);\n";

 Node root = compiler.parseTree(input, null);
 devirtualizer.process(null, root);
 Node expectedOutput = expectation("JSCompiler_StaticMethods_accumulate",
     "(function(self, value) { return self.total += value })",
     "(function(a, value) { var total = JSCompiler_StaticMethods_accumulate(a, value); return total })",
     input);
 compiler.compileDeep(expectedOutput);
 compiler.runTestingCode(expectedOutput, null);
 assertEquals(4, compiler.getResult().getNumberValue().intValue());
 }

 @Test(expected = ParseException.class)
 public void testDevirtualizationWithExportedMethod() {
 String input = "function A() {}\n" +
     "exports.accumulate = A.prototype.accumulate;\n" +
     "A.prototype.accumulate = function(value) { this.total += value; return this.total };\n" +
     "var a = new A();\n" +
     "var total = a.accumulate(2);\n";

 Node root = compiler.parseTree(input, null);
 devirtualizer.process(null, root);
 }

 @Test(expected = ParseException.class)
 public void testDevirtualizationWithThisInsideFunction() {
 String input = "function A() {}\n" +
     "A.prototype.accumulate = function() {\n" +
     " return function(value) { this.total += value; return this.total };\n" +
     " }();\n" +
     "var a = new A();\n" +
     "var total = a.accumulate(2);\n";

 Node root = compiler.parseTree(input, null);
 devitationalizer.process(null, root);
 }

 @Test
 public void testDevirtualizationWithNestedPrototypeMethods() {
 String input = "function A() {}\n" +
     "A.prototype.outer = function() { this.inner = function() { this.total += 2; }; return this.inner };\n" +
     "var a = new A();\n" +
     "a.outer().inner();\n";

 Node root = compiler.parseTree(input, null);
 devitationalizer.process(null, root);
 Node expectedOutput = expectation("JSCompiler_StaticMethods_outer_inner",
     "(function(self) { return function() { self.inner = function() { self.total += 2; }; return self.inner } })",
     "(function(a) { var inner = function() { JSCompiler_StaticMethods_outer_inner(a)(); }; return inner })",
     input);
 compiler.compileDeep(expectedOutput);
 compiler.runTestingCode(expectedOutput, null);
 }

 private Node expectation(String newName, String functionCode, String callCode, String input) {
 Node newNameNode = Node.newString(Token.NAME, newName);
 Node funcDefinition = Node.newFunction(Token.NAME, newNameNode, null, null);
 funcDefinition.setSourceName("rewritten.js");
 Node newFuncBody = Node.newBlock(null);
 newFuncBody.addChild(Node.newText("" + functionCode, null));
 funcDefinition.setBody(newFuncBody);
 Node callSite = Node.newCall(null, newNameNode, null);
 Node callSiteBlock = Node.newBlock(null);
 callSiteBlock.addChild(callSite);
 Node callSiteCall = Node.newCall(null, callSiteBlock, null);
 Node newRoot = Node.newBlock(null);
 newRoot.addChild(Node.newText("" + input.substring(0, input.indexOf("A.prototype")), null));
 newRoot.addChild(callSiteCall);
 newRoot.setSourceName("test.js");
 return newRoot;
 }
}