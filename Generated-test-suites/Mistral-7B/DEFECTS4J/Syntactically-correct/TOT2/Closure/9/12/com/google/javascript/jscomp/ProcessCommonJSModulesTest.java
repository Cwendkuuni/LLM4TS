import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.ProcessCommonJSModules;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.SourceName;

public class ProcessCommonJSModulesTest {

 private Compiler compiler;
 private CompilerOptions options;
 private NodeFactory nodeFactory;

 @Before
 public void setUp() {
 compiler = new Compiler();
 options = new CompilerOptions();
 nodeFactory = compiler.getOptions().nodeFactory;
 }

 @Test
 public void testProcessCommonJSModules() {
 String input = "// A simple CommonJS module\n" +
 "var exports = module.exports = {\n" +
 "  add: function(a, b) {\n" +
 "    return a + b;\n" +
 "  }\n" +
 "};\n" +
 "\n" +
 "exports.add(1, 2); // should be replaced with 1 + 2\n";

 Node root = nodeFactory.parse(input, null, new SourceName("test.js"));
 ProcessCommonJSModules p = new ProcessCommonJSModules(compiler, ".", true);
 p.process(null, root);
 String output = compiler.getSource();
 assertEquals("1 + 2", output);
 }

 @Test
 public void testProcessCommonJSModulesWithRequire() {
 String input = "// Module A\n" +
 "var exports = module.exports = {\n" +
 "  add: function(a, b) {\n" +
 "    return a + b;\n" +
 "  }\n" +
 "};\n" +
 "\n" +
 "// Module B depends on Module A\n" +
 "var a = require('./A');\n" +
 "exports.multiply = function(a, b) {\n" +
 "  return a * b;\n" +
 "};\n" +
 "exports.multiply(a.add(1, 2), 3); // should be replaced with 1 + 2 * 3\n";

 Node root = nodeFactory.parse(input, null, new SourceName("B.js"));
 ProcessCommonJSModules p = new ProcessCommonJSModules(compiler, ".", true);
 p.process(null, root);
 String output = compiler.getSource();
 assertEquals("3 + 4", output);
 }

 @Test(expected = RuntimeException.class)
 public void testProcessCommonJSModulesWithInvalidURI() {
 String input = "// Module A\n" +
 "var exports = module.exports = {\n" +
 "  add: function(a, b) {\n" +
 "    return a + b;\n" +
 "  }\n" +
 "};\n" +
 "\n" +
 "// Module B depends on Module A with invalid URI\n" +
 "var a = require('//B');\n" +
 "exports.multiply = function(a, b) {\n" +
 "  return a * b;\n" +
 "};\n" +
 "exports.multiply(a.add(1, 2), 3); // should be replaced with 1 + 2 * 3\n";

 Node root = nodeFactory.parse(input, null, new SourceName("B.js"));
 ProcessCommonJSModules p = new ProcessCommonJSModules(compiler, ".", true);
 p.process(null, root);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testProcessCommonJSModulesWithInvalidRequiresNoModuleName() {
 String input = "// Module A\n" +
 "var exports = module.exports = {\n" +
 "  add: function(a, b) {\n" +
 "    return a + b;\n" +
 "  }\n" +
 "};\n" +
 "\n" +
 "// Module B depends on Module A with no module name\n" +
 "var a = require('./');\n" +
 "exports.multiply = function(a, b) {\n" +
 "  return a * b;\n" +
 "};\n" +
 "exports.multiply(a.add(1, 2), 3); // should be replaced with 1 + 2 * 3\n";

 Node root = nodeFactory.parse(input, null, new SourceName("B.js"));
 ProcessCommonJSModules p = new ProcessCommonJSModules(compiler, ".", true);
 p.process(null, root);
 }

 @Test(expected = RuntimeException.class)
 public void testProcessCommonJSModulesWithMissingModule() {
 String input = "// Module A\n" +
 "var exports = module.exports = {\n" +
 "  add: function(a, b) {\n" +
 "    return a + b;\n" +
 "  }\n" +
 "};\n" +
 "\n" +
 "// Module B depends on missing module C\n" +
 "var c = require('./C');\n" +
 "exports.multiply = function(a, b) {\n" +
 "  return a * b;\n" +
 "};\n" +
 "exports.multiply(a.add(1, 2), 3); // should be replaced with 1 + 2 * 3\n";

 Node root = nodeFactory.parse(input, null, new SourceName("B.js"));
 ProcessCommonJSModules p = new ProcessCommonJSModules(compiler, ".", true);
 p.process(null, root);
 }

 @Test
 public void testProcessCommonJSModulesWithModuleExports() {
 String input = "// Module A\n" +
 "exports.add = function(a, b) {\n" +
 "  return a + b;\n" +
 "};\n" +
 "\n" +
 "// Module B depends on Module A\n" +
 "exports.multiply = function(a, b) {\n" +
 "  return a * b;\n" +
 "};\n" +
 "exports.multiply(require('./A').add(1, 2), 3); // should be replaced with 1 + 2 * 3\n";

 Node root = nodeFactory.parse(input, null, new SourceName("B.js"));
 ProcessCommonJSModules p = new ProcessCommonJSModules(compiler, ".", true);
 p.process(null, root);
 String output = compiler.getSource();
 assertEquals("3 + 4", output);
 }

 @Test
 public void testProcessCommonJSModulesWithProvide() {
 String input = "// Module A\n" +
 "exports.add = function(a, b) {\n" +
 "  return a + b;\n" +
 "};\n" +
 "goog.provide('A');\n";

 Node root = nodeFactory.parse(input, null, new SourceName("A.js"));
 ProcessCommonJSModules p = new ProcessCommonJSModules(compiler, ".", false);
 p.process(null, root);
 String output = compiler.getSource();
 assertEquals("(function() {\n" +
 "  'use strict';\n" +
 "  goog.provide('A');\n" +
 "  A.add = function(a, b) {\n" +
 "    return a + b;\n" +
 "  };\n" +
 "})();\n", output);
 }

 @Test
 public void testProcessCommonJSModulesWithRequireAndProvide() {
 String input = "// Module A\n" +
 "exports.add = function(a, b) {\n" +
 "  return a + b;\n" +
 "};\n" +
 "goog.provide('A');\n" +
 "\n" +
 "// Module B depends on Module A\n" +
 "exports.require = require;\n" +
 "exports.multiply = function(a, b) {\n" +
 "  return a * b;\n" +
 "};\n" +
 "exports.multiply(require('A').add(1, 2), 3); // should be replaced with 1 + 2 * 3\n" +
 "goog.require('A');\n";

 Node root = nodeFactory.parse(input, null, new SourceName("B.js"));
 ProcessCommonJSModules p = new ProcessCommonJSModules(compiler, ".", true);
 p.process(null, root);
 String output = compiler.getSource();
 assertEquals("(function() {\n" +
 "  'use strict';\n" +
 "  goog.provide('A');\n" +
 "  A.add = function(a, b) {\n" +
 "    return a + b;\n" +
 "  };\n" +
 "\n" +
 "  goog.require('A');\n" +
 "\n" +
 "  exports.multiply = function(a, b) {\n" +
 "    return a * b;\n" +
 "  };\n" +
 "})();\n", output);
 }
}