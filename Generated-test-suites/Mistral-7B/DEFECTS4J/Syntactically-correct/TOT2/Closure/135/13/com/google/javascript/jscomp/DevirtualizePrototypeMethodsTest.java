import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.SimpleDefinitionFinder;
import com.google.javascript.rhino.NodeFactory;

public class DevirtualizePrototypeMethodsTest {

 private Compiler compiler;
 private CompilerOptions options;
 private NodeFactory nodeFactory;

 @Before
 public void setup() {
 compiler = new Compiler();
 options = new CompilerOptions();
 nodeFactory = new NodeFactory();
 }

 @After
 public void teardown() {
 compiler.dispose();
 }

 @Test
 public void testDevirtualizePrototypeMethods() {
 // Set up test case
 String input = "function A() {}\n" +
 "A.prototype.accumulate = function(value) {\n" +
 " return this.total += value;\n" +
 "}\n" +
 "var a = new A();\n" +
 "var total = a.accumulate(2);\n";

 // Process input with DevirtualizePrototypeMethods pass
 CompilerPass devirtualizePrototypeMethods = new DevirtualizePrototypeMethods(compiler);
 devitationalizePrototypeMethods.process(null, nodeFactory.parseTree(input, options));

 // Verify output
 Node root = nodeFactory.parseTree(input, options).getFirstChild();
 Node totalNode = assertFindSingleNode(root, "total");
 Node totalValueNode = totalNode.getFirstChild();
 Node accumulateCallNode = assertFindSingleNode(root, "accumulate");

 // Check that accumulate call was transformed to static method call
 assertEquals("JSCompiler_StaticMethods_accumulate",
 accumulateCallNode.getLastChild().getString());
 assertEquals("a", accumulateCallNode.getFirstChild().getString());
 assertEquals(Node.OP_ADD, accumulateCallNode.getLastChild().getFirstChild().getOp().getType());
 assertEquals(totalValueNode, accumulateCallNode.getLastChild().getLastChild());
 }

 @Test
 public void testDevirtualizePrototypeMethodsWithMultipleMethods() {
 // Set up test case
 String input = "function A() {}\n" +
 "A.prototype.accumulate = function(value) {\n" +
 " return this.total += value;\n" +
 "}\n" +
 "A.prototype.subtract = function(value) {\n" +
 " return this.total -= value;\n" +
 "}\n" +
 "var a = new A();\n" +
 "var total = a.accumulate(2);\n" +
 "total = a.subtract(3);\n";

 // Process input with DevirtualizePrototypeMethods pass
 CompilerPass devirtualizePrototypeMethods = new DevirtualizePrototypeMethods(compiler);
 devitationalizePrototypeMethods.process(null, nodeFactory.parseTree(input, options));

 // Verify output
 Node root = nodeFactory.parseTree(input, options).getFirstChild();
 Node totalNode = assertFindSingleNode(root, "total");
 Node totalValueNode = totalNode.getFirstChild();
 Node accumulateCallNode = assertFindSingleNode(root, "accumulate");
 Node subtractCallNode = assertFindSingleNode(root, "subtract");

 // Check that accumulate and subtract calls were transformed to static method calls
 assertEquals("JSCompiler_StaticMethods_accumulate",
 accumulateCallNode.getLastChild().getString());
 assertEquals("a", accumulateCallNode.getFirstChild().getString());
 assertEquals("JSCompiler_StaticMethods_subtract",
 subtractCallNode.getLastChild().getString());
 assertEquals("a", subtractCallNode.getFirstChild().getString());
 }

 @Test
 public void testDevirtualizePrototypeMethodsWithMultipleDefinitions() {
 // Set up test case
 String input = "function A() {}\n" +
 "A.prototype.accumulate = function(value) {\n" +
 " return this.total += value;\n" +
 "}\n" +
 "A.prototype.accumulate = function(value) {\n" +
 " return this.total += value * 2;\n" +
 "}\n" +
 "var a = new A();\n" +
 "var total = a.accumulate(2);\n";

 // Process input with DevirtualizePrototypeMethods pass
 CompilerPass devirtualizePrototypeMethods = new DevirtualizePrototypeMethods(compiler);
 devitationalizePrototypeMethods.process(null, nodeFactory.parseTree(input, options));

 // Verify output
 Node root = nodeFactory.parseTree(input, options).getFirstChild();
 Node totalNode = assertFindSingleNode(root, "total");
 Node totalValueNode = totalNode.getFirstChild();
 Node accumulateCallNode = assertFindSingleNode(root, "accumulate");

 // Check that accumulate call was transformed to static method call
 assertEquals("JSCompiler_StaticMethods_accumulate",
 accumulateCallNode.getLastChild().getString());
 assertEquals("a", accumulateCallNode.getFirstChild().getString());
 }

 @Test
 public void testDevirtualizePrototypeMethodsWithMissingDefinition() {
 // Set up test case
 String input = "function A() {}\n" +
 "A.prototype.accumulate = function(value) {\n" +
 " return this.total += value;\n" +
 "}\n" +
 "var a = new A();\n" +
 "var total = a.accumulate(2);\n" +
 "A.prototype.subtract = function(value) {\n" +
 " return this.total -= value;\n" +
 "}";

 // Process input with DevirtualizePrototypeMethods pass
 CompilerPass devitationalizePrototypeMethods = new DevirtualizePrototypeMethods(compiler);
 devitationalizePrototypeMethods.process(null, nodeFactory.parseTree(input, options));

 // Verify output
 Node root = nodeFactory.parseTree(input, options).getFirstChild();
 Node totalNode = assertFindSingleNode(root, "total");
 Node totalValueNode = totalNode.getFirstChild();
 Node accumulateCallNode = assertFindSingleNode(root, "accumulate");

 // Check that accumulate call was transformed to static method call
 assertEquals("JSCompiler_StaticMethods_accumulate",
 accumulateCallNode.getLastChild().getString());
 assertEquals("a", accumulateCallNode.getFirstChild().getString());

 // Check that subtract call was not transformed since definition was missing
 Node subtractCallNode = assertFindSingleNode(root, "subtract");
 assertNotNull(subtractCallNode);
 assertEquals("A.prototype.subtract", subtractCallNode.getLastChild().getString());
 }

 @Test
 public void testDevirtualizePrototypeMethodsWithExportedMethod() {
 // Set up test case
 String input = "var EXPORTED_ACCUMULATE = function(value) {\n" +
 " return this.total += value;\n" +
 "}\n" +
 "function A() {}\n" +
 "A.prototype.accumulate = EXPORTED_ACCUMULATE;\n" +
 "var a = new A();\n" +
 "var total = a.accumulate(2);\n";

 // Process input with DevirtualizePrototypeMethods pass
 CompilerPass devitutionalizePrototypeMethods = new DevirtualizePrototypeMethods(compiler);
 devitutionalizePrototypeMethods.process(null, nodeFactory.parseTree(input, options));

 // Verify output
 Node root = nodeFactory.parseTree(input, options).getFirstChild();
 Node totalNode = assertFindSingleNode(root, "total");
 Node totalValueNode = totalNode.getFirstChild();
 Node accumulateCallNode = assertFindSingleNode(root, "accumulate");

 // Check that accumulate call was not transformed since method was exported
 assertEquals("EXPORTED_ACCUMULATE", accumulateCallNode.getLastChild().getString());
 assertEquals("a", accumulateCallNode.getFirstChild().getString());
 }

 @Test(expected = AssertionError.class)
 public void testDevirtualizePrototypeMethodsWithCallSiteBeforeDefinition() {
 // Set up test case
 String input = "A.prototype.accumulate = function(value) {\n" +
 " return this.total += value;\n" +
 "}\n" +
 "var total = A.prototype.accumulate(2);\n";

 // Process input with DevirtualizePrototypeMethods pass
 CompilerPass devitationalizePrototypeMethods = new DevirtualizePrototypeMethods(compiler);
 devitationalizePrototypeMethods.process(null, nodeFactory.parseTree(input, options));

 // Verify error
 fail("Expected an AssertionError but nothing was thrown.");
 }

 @Test(expected = AssertionError.class)
 public void testDevirtualizePrototypeMethodsWithCallSiteInDifferentModule() {
 // Set up test case
 String inputA = "function A() {}\n" +
 "A.prototype.accumulate = function(value) {\n" +
 " return this.total += value;\n" +
 "}\n" +
 "var a = new A();\n";
 String inputB = "function B() {}\n" +
 "var total = A.prototype.accumulate(2);\n";

 Node rootA = nodeFactory.parseTree(inputA, options).getFirstChild();
 Node rootB = nodeFactory.parseTree(inputB, options).getFirstChild();
 compiler.addTopLevelRoots(rootA, rootB);
 CompilerPass devitutionalizePrototypeMethods = new DevirtualizePrototypeMethods(compiler);
 devitationalizePrototypeMethods.process(null, null);

 // Verify error
 fail("Expected an AssertionError but nothing was thrown.");
 }

 private Node assertFindSingleNode(Node root, String name) {
 Node node = root;
 while (node != null) {
 if (node.getFirstChild() != null &&
 node.getFirstChild().getLastChild() != null &&
 node.getFirstChild().getLastChild().isIdentifier() &&
 node.getFirstChild().getLastChild().getString().equals(name)) {
 return node;
 }
 node = node.getNext();
 }
 fail("Could not find node with name: " + name);
 }
}