import static org.junit.Assert.*;
import static com.google.javascript.jscomp.ProcessCommonJSModules.*;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.SourceName;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Set;

public class ProcessCommonJSModulesTest {

 private static final String TEST_FILE = "TestCommonJSModules.js";
 private static final String REQUIRED_MODULE_FILE = "RequiredModule.js";

 @BeforeClass
 public static void setup() {
 CompilerOptions options = new CompilerOptions(null);
 Compiler compiler = new Compiler(options);
 }

 @Test
 public void testProcessCommonJSModules() throws IOException, URISyntaxException {
 SourceName sourceName = new SourceName(new URI("file:" + TEST_FILE));
 Node root = NodeFactory.createRoot();
 ProcessCommonJSModules processor = new ProcessCommonJSModules(compiler, ".", false);
 NodeTraversal traversal = new NodeTraversal(compiler, root, processor);

 // Test processing a file with no require statements
 Node scriptNode1 = NodeFactory.createScript(sourceName, "var x = 1;");
 root.addChild(scriptNode1);
 processor.process(null, root);

 // Test processing a file with require statement
 Node scriptNode2 = NodeFactory.createScript(sourceName,
 "require('./RequiredModule');");
 root.addChild(scriptNode2);
 processor.process(null, root);

 // Test processing a file with multiple require statements
 Node scriptNode3 = NodeFactory.createScript(sourceName,
 "require('./RequiredModule1');\n" +
 "require('./RequiredModule2');");
 root.addChild(scriptNode3);
 processor.process(null, root);

 // Test processing a file with multiple require statements and module exports
 Node scriptNode4 = NodeFactory.createScript(sourceName,
 "module.exports = function() {\n" +
 " var x = require('./RequiredModule1');\n" +
 " return x + require('./RequiredModule2').default;\n" +
 "};");
 root.addChild(scriptNode4);
 processor.process(null, root);

 // Test processing a file with a relative require statement
 Node scriptNode5 = NodeFactory.createScript(new SourceName(
 new URI("file:" + TEST_FILE + "?r=" + REQUIRED_MODULE_FILE)),
 "require('./RequiredModule');");
 root.addChild(scriptNode5);
 processor.process(null, root);

 // Test processing a file with a require statement for a non-existent module
 Node scriptNode6 = NodeFactory.createScript(sourceName,
 "require('NonExistentModule');");
 root.addChild(scriptNode6);
 processor.process(null, root);

 // Test processing a file with a require statement for a module with exports
 Node scriptNode7 = NodeFactory.createScript(sourceName,
 "var x = require('./RequiredModuleWithExports').default;");
 root.addChild(scriptNode7);
 processor.process(null, root);

 // Test processing a file with a require statement for a module with no exports
 Node scriptNode8 = NodeFactory.createScript(sourceName,
 "var x = require('./RequiredModuleNoExports');");
 root.addChild(scriptNode8);
 processor.process(null, root);

 // Test processing a file with a require statement for a module with circular dependency
 Node scriptNode9 = NodeFactory.createScript(sourceName,
 "require('./CircularDependency');");
 root.addChild(scriptNode9);
 Node circularDependencyScript = NodeFactory.createScript(
 new SourceName(new URI("file:" + TEST_FILE + "?r=" + TEST_FILE)),
 "require('./" + TEST_FILE + "');");
 root.addChild(circularDependencyScript);
 processor.process(null, root);

 // Test processing a file with a require statement for a module with exports and circular dependency
 Node scriptNode10 = NodeFactory.createScript(sourceName,
 "var x = require('./RequiredModuleWithExports').default;");
 root.addChild(scriptNode10);
 Node requiredModuleWithExportsScript = NodeFactory.createScript(
 new SourceName(new URI("file:" + REQUIRED_MODULE_FILE)),
 "require('./" + TEST_FILE + "');");
 root.addChild(requiredModuleWithExportsScript);
 processor.process(null, root);

 // Test processing a file with a module that exports an array
 Node scriptNode11 = NodeFactory.createScript(sourceName,
 "var x = require('./ArrayModule').default;");
 root.addChild(scriptNode11);
 Node arrayModuleScript = NodeFactory.createScript(
 new SourceName(new URI("file:" + "ArrayModule.js")),
 "[1, 2, 3]");
 root.addChild(arrayModuleScript);
 processor.process(null, root);
 assertArrayEquals(new int[]{1, 2, 3}, (int[])x);

 // Test processing a file with a module that exports a string
 Node scriptNode12 = NodeFactory.createScript(sourceName,
 "var x = require('./StringModule').default;");
 root.addChild(scriptNode12);
 Node stringModuleScript = NodeFactory.createScript(
 new SourceName(new URI("file:" + "StringModule.js")),
 "\"Hello World\";");
 root.addChild(stringModuleScript);
 processor.process(null, root);
 assertEquals("Hello World", x.toString());

 // Test processing a file with a module that exports a function
 Node scriptNode13 = NodeFactory.createScript(sourceName,
 "var x = require('./FunctionModule').default;");
 root.addChild(scriptNode13);
 Node functionModuleScript = NodeFactory.createScript(
 new SourceName(new URI("file:" + "FunctionModule.js")),
 "function() {\n" +
 " return 42;\n" +
 "};");
 root.addChild(functionModuleScript);
 processor.process(null, root);
 assertEquals(42, x());
 }

 private void assertArrayEquals(int[] expected, int[] actual) {
 Arrays.sort(expected);
 Arrays.sort(actual);
 assertEquals(expected.length, actual.length);
 for (int i = 0; i < expected.length; i++) {
 assertEquals(expected[i], actual[i]);
 }
 }
}