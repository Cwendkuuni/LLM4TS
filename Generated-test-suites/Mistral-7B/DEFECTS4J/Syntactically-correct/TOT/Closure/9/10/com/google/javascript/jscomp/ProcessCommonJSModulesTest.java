import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;

import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.ProcessCommonJSModules;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.NodeFactory;

public class ProcessCommonJSModulesTest {

 private Compiler compiler;
 private CompilerOptions options;
 private NodeFactory factory;

 @Before
 public void setUp() {
 compiler = new Compiler();
 options = new CompilerOptions();
 factory = compiler.getFactory();
 }

 @Test
 public void testProcessCommonJSModules() {
 String input = "// File: main.js\n" +
 "var m = require('./test');\n" +
 "goog.provide('test');\n" +
 "goog.require('main');\n" +
 "test.foo = function() {\n" +
 " console.log('Hello from test!');\n" +
 "};\n";

 String output = "// File: main.js\n" +
 "goog.provide('test');\n" +
 "goog.require('main');\n" +
 "test.foo = function() {\n" +
 " console.log('Hello from test!');\n" +
 "};\n" +
 "// File: test.js\n" +
 "goog.provide('test');\n" +
 "goog.require('main');\n" +
 "exports.foo = function() {\n" +
 " console.log('Hello from test-renamed!');\n" +
 "};\n";

 Node root = factory.parseTree(input);
 ProcessCommonJSModules pass = new ProcessCommonJSModules(compiler, "", false);
 pass.process(factory.getEmptyJSObject(), root);
 String result = compiler.getSource();
 assertEquals(output, result);
 }

 @Test
 public void testProcessCommonJSModulesWithError() {
 String input = "// File: main.js\n" +
 "var m = require('./test');\n" +
 "goog.provide('test');\n" +
 "goog.require('main');\n" +
 "test.foo = function() {\n" +
 " throw new Error('Oops');\n" +
 "};\n";

 Node root = factory.parseTree(input);
 ProcessCommonJSModules pass = new ProcessCommonJSModules(compiler, "", false);
 try {
 pass.process(factory.getEmptyJSObject(), root);
 fail("Expected CompilerError");
 } catch (Compiler.CompilerError e) {
 // Expected
 }

 String result = compiler.getSource();
 assertNull(result);
 }

 @Test
 public void testProcessCommonJSModulesWithCycle() {
 String input = "// File: test1.js\n" +
 "goog.provide('test1');\n" +
 "goog.require('test2');\n" +
 "test1.foo = function() {\n" +
 " console.log('Hello from test1!');\n" +
 "};\n" +
 "\n" +
 "// File: test2.js\n" +
 "goog.provide('test2');\n" +
 "goog.require('test1');\n" +
 "test2.foo = function() {\n" +
 " console.log('Hello from test2!');\n" +
 "};\n";

 Node root = factory.parseTree(input);
 ProcessCommonJSModules pass = new ProcessCommonJSModules(compiler, "", false);
 try {
 pass.process(factory.getEmptyJSObject(), root);
 fail("Expected CompilerError with message:\n" +
 "  Circular dependency between test1 and test2");
 } catch (Compiler.CompilerError e) {
 String expectedErrorMessage = "Circular dependency between test1 and test2";
 assertEquals(expectedErrorMessage, e.getMessage());
 }

 String result = compiler.getSource();
 assertNull(result);
 }
}