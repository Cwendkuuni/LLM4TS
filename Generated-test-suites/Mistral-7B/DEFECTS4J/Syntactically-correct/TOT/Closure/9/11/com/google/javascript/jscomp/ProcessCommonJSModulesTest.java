import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.rhino.IR;
import static org.junit.Assert.*;

@RunWith(JUnit4.class)
public class ProcessCommonJSModulesTest {

 private Compiler compiler;
 private CompilerPass processCommonJSModules;
 private String sourceFileName1 = "module1.js";
 private String sourceFileName2 = "module2.js";
 private String input = "// Test input\n" +
         "function main() {\n" +
         "  require('./" + sourceFileName1 + "');\n" +
         "}\n" +
         "\n" +
         "// module1.js\n" +
         "exports.hello = function() {\n" +
         "  return 'Hello from module1!';\n" +
         "};\n";
 private String expectedOutput1 = "// Test output\n" +
         "goog.provide('module1');\n" +
         "\n" +
         "goog.require('module1');\n" +
         "\n" +
         "module1.hello = module1.module$exports.hello;\n" +
         "\n" +
         "function main() {\n" +
         "  module1.hello();\n" +
         "}\n";
 private String expectedOutput2 = "// Test output\n" +
         "goog.provide('module2');\n" +
         "\n" +
         "goog.require('module2');\n" +
         "\n" +
         "module2.module$exports = {\n" +
         "  hello: function() {\n" +
         "    return 'Hello from module2!';\n" +
         "  }\n" +
         "};\n" +
         "\n" +
         "function main() {\n" +
         "  module2.hello();\n" +
         "}";

 @Before
 public void setUp() {
 compiler = new Compiler();
 processCommonJSModules = new ProcessCommonJSModules(compiler, ".");
 }

 @Test
 public void testProcessCommonJSModules() {
 Node externs = IR.EmptyNode().toNode();
 Node root = IR.ProgramNode.create(IR.EmptyNode().toNode(), IR.EmptyNode().toNode());
 Node inputNode = IR.parse(input, null);
 NodeTraversal traversal = new NodeTraversal(compiler, root, inputNode);
 processCommonJSModules.process(externs, root);
 assertEquals(expectedOutput1, compiler.toSource(root.getChildAtIndex(0)));
 assertEquals(expectedOutput2, compiler.toSource(root.getChildAtIndex(1)));
 }

 @Test
 public void testRelativeRequire() {
 input = "// Test input\n" +
         "function main() {\n" +
         "  require('./module1/submodule');\n" +
         "}\n" +
         "\n" +
         "// module1/submodule.js\n" +
         "exports.hello = function() {\n" +
         "  return 'Hello from submodule!';\n" +
         "};\n";
 expectedOutput1 = "// Test output\n" +
         "goog.provide('module1.submodule');\n" +
         "\n" +
         "goog.require('module1.submodule');\n" +
         "\n" +
         "module1.submodule.module$exports = {\n" +
         "  hello: function() {\n" +
         "    return 'Hello from submodule!';\n" +
         "  }\n" +
         "};\n" +
         "\n" +
         "function main() {\n" +
         "  module1.submodule.hello();\n" +
         "}";
 Node inputNode = IR.parse(input, null);
 NodeTraversal traversal = new NodeTraversal(compiler, root, inputNode);
 processCommonJSModules.process(externs, root);
 assertEquals(expectedOutput1, compiler.toSource(root.getChildAtIndex(0)));
 }

 @Test
 public void testAbsoluteRequire() {
 input = "// Test input\n" +
         "function main() {\n" +
         "  require('module1.js');\n" +
         "}\n" +
         "\n" +
         "// module1.js\n" +
         "exports.hello = function() {\n" +
         "  return 'Hello from module1!';\n" +
         "};\n";
 expectedOutput1 = "// Test output\n" +
         "goog.provide('module1');\n" +
         "\n" +
         "goog.require('module1');\n" +
         "\n" +
         "module1.module$exports = {\n" +
         "  hello: function() {\n" +
         "    return 'Hello from module1!';\n" +
         "  }\n" +
         "};\n" +
         "\n" +
         "function main() {\n" +
         "  module1.hello();\n" +
         "}";
 Node inputNode = IR.parse(input, null);
 NodeTraversal traversal = new NodeTraversal(compiler, root, inputNode);
 processCommonJSModules.process(externs, root);
 assertEquals(expectedOutput1, compiler.toSource(root.getChildAtIndex(0)));
 }

 @Test
 public void testModuleWithExports() {
 input = "// Test input\n" +
         "exports.hello = function() {\n" +
         "  return 'Hello from module!';\n" +
         "};\n";
 expectedOutput1 = "// Test output\n" +
         "goog.provide('module');\n" +
         "\n" +
         "module.module$exports = {\n" +
         "  hello: function() {\n" +
         "    return 'Hello from module!';\n" +
         "  }\n" +
         "};\n";
 Node inputNode = IR.parse(input, null);
 NodeTraversal traversal = new NodeTraversal(compiler, root, inputNode);
 processCommonJSModules.process(externs, root);
 assertEquals(expectedOutput1, compiler.toSource(root.getChildAtIndex(0)));
 }

 @Test
 public void testRequireCircularDependency() {
 input = "// Test input (Circular dependency)\n" +
         "exports.hello = function() {\n" +
         "  require('./module2');\n" +
         "  return 'Hello from module1!';\n" +
         "};\n" +
         "\n" +
         "// module2.js\n" +
         "exports.hello = function() {\n" +
         "  require('./module1');\n" +
         "  return 'Hello from module2!';\n" +
         "};\n";
 assertThrows(RuntimeException.class, () -> {
 Node inputNode = IR.parse(input, null);
 NodeTraversal traversal = new NodeTraversal(compiler, root, inputNode);
 processCommonJSModules.process(externs, root);
 });
 }

 @Test
 public void testRequireMissingModule() {
 input = "// Test input (Missing module)\n" +
         "function main() {\n" +
         "  require('missingModule');\n" +
         "}\n";
 assertThrows(RuntimeException.class, () -> {
 Node inputNode = IR.parse(input, null);
 NodeTraversal traversal = new NodeTraversal(compiler, root, inputNode);
 processCommonJSModules.process(externs, root);
 });
 }
}