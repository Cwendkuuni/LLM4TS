import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.JSCompiler;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.ProcessCommonJSModules;
import com.google.javascript.jscomp. compiler.CompilerInput;
import com.google.javascript.jscomp.compiler.CompilerOptions;
import com.google.javascript.rhino.NodeFactory;

public class ProcessCommonJSModulesTest {

 @Test
 public void testProcessCommonJSModules() {
 CompilerOptions options = new CompilerOptions(null);
 AbstractCompiler compiler = new JSCompiler(options, null, null);
 CompilerInput input = new CompilerInput(new NodeFactory(), null);

 ProcessCommonJSModules pass = new ProcessCommonJSModules(compiler, "");
 input.setFile("test.js", "test module");
 input.setSourceFileName("test.js");
 input.setInput("test code", "test.js");

 Node rootNode = Node.root();
 Node moduleRequire = Node.newCall(Node.newName("require"),
 Node.newString("commonModule"));
 Node moduleModuleExports = Node.newObjectLiteral();
 moduleModuleExports.setProperty("module$exports", Node.newObjectLiteral());
 Node moduleVariable = Node.newName("myModule");
 moduleVariable.setString("myModule");
 moduleVariable.setProperty(Node.ORIGINALNAME_PROP, "myModule");
 moduleVariable.setProperty("module$", moduleModuleExports);
 moduleVariable.setProperty(Node.ORIGINALNAME_PROP, "myModule");

 Node moduleScript = Node.newScript(
 "var myModule = " + moduleVariable.toString() + ";");

 rootNode.setScript(0, moduleScript);
 rootNode.setChild(1, moduleRequire);
 Node module = Node.newObjectLiteral();
 module.setProperty("id", Node.newString("commonModule"));
 module.setProperty("filename", Node.newString("commonModule.js"));
 module.setProperty("exports", Node.newObjectLiteral());
 module.setProperty("exports.foo", Node.newString("bar"));
 rootNode.setChild(2, module);

 pass.process(input, rootNode);
 Node actualOutput = input.getOutput();

 assertEquals("(function (){\n" +
 "  goog.require('commonModule');\n" +
 "  var myModule = commonModule;\n" +
 "  myModule = myModule.module$exports;\n" +
 "  goog.provide('myModule');\n" +
 "})();\n", actualOutput.toString());
 }

 @Test
 public void testProcessCommonJSModulesWhenNoModuleExports() {
 CompilerOptions options = new CompilerOptions(null);
 AbstractCompiler compiler = new JSCompiler(options, null, null);
 CompilerInput input = new CompilerInput(new NodeFactory(), null);

 ProcessCommonJSModules pass = new ProcessCommonJSModules(compiler, "");
 input.setFile("test.js", "test module");
 input.setSourceFileName("test.js");
 input.setInput("test code", "test.js");

 Node rootNode = Node.root();
 Node moduleRequire = Node.newCall(Node.newName("require"),
 Node.newString("commonModule"));
 Node moduleVariable = Node.newName("myModule");
 moduleVariable.setString("myModule");
 moduleVariable.setProperty(Node.ORIGINALNAME_PROP, "myModule");
 Node moduleScript = Node.newScript(
 "var myModule = " + moduleVariable.toString() + ";");

 rootNode.setScript(0, moduleScript);
 rootNode.setChild(1, moduleRequire);
 Node module = Node.newObjectLiteral();
 rootNode.setChild(2, module);

 pass.process(input, rootNode);
 Node actualOutput = input.getOutput();

 assertNull(actualOutput);
 }

 @Test
 public void testProcessCommonJSModulesWithRelativeRequire() {
 CompilerOptions options = new CompilerOptions(null);
 AbstractCompiler compiler = new JSCompiler(options, null, null);
 CompilerInput input = new CompilerInput(new NodeFactory(), null);

 ProcessCommonJSModules pass = new ProcessCommonJSModules(compiler, "");
 input.setFile("test.js", "test module");
 input.setSourceFileName("test.js");
 input.setInput("test code", "test.js");

 Node rootNode = Node.root();
 Node moduleRequire = Node.newCall(Node.newName("require"),
 Node.newString("./commonModule"));
 Node moduleModuleExports = Node.newObjectLiteral();
 moduleModuleExports.setProperty("module$exports", Node.newObjectLiteral());
 Node moduleVariable = Node.newName("myModule");
 moduleVariable.setString("myModule");
 moduleVariable.setProperty(Node.ORIGINALNAME_PROP, "myModule");
 moduleVariable.setProperty("module$", moduleModuleExports);
 moduleVariable.setProperty(Node.ORIGINALNAME_PROP, "myModule");
 Node moduleScript = Node.newScript(
 "var myModule = " + moduleVariable.toString() + ";");

 rootNode.setScript(0, moduleScript);
 Node commonModule = Node.newObjectLiteral();
 commonModule.setProperty("id", Node.newString("commonModule"));
 commonModule.setProperty("filename", Node.newString("commonModule.js"));
 commonModule.setProperty("exports", Node.newObjectLiteral());
 commonModule.setProperty("exports.foo", Node.newString("bar"));
 rootNode.setChild(1, moduleRequire);
 rootNode.setChild(2, commonModule);

 pass.process(input, rootNode);
 Node actualOutput = input.getOutput();

 assertEquals("(function (){\n" +
 "  require('./commonModule');\n" +
 "  var myModule = commonModule;\n" +
 "  myModule = myModule.module$exports;\n" +
 "  goog.provide('myModule');\n" +
 "})();\n", actualOutput.toString());
 }

 @Test
 public void testProcessCommonJSModulesWithAbsoluteRequire() {
 CompilerOptions options = new CompilerOptions(null);
 AbstractCompiler compiler = new JSCompiler(options, null, null);
 CompilerInput input = new CompilerInput(new NodeFactory(), null);

 ProcessCommonJSModules pass = new ProcessCommonJSModules(compiler, "");
 input.setFile("test.js", "test module");
 input.setSourceFileName("test.js");
 input.setInput("test code", "test.js");

 Node rootNode = Node.root();
 Node moduleRequire = Node.newCall(Node.newName("require"),
 Node.newString("commonModule.js"));
 Node moduleModuleExports = Node.newObjectLiteral();
 moduleModuleExports.setProperty("module$exports", Node.newObjectLiteral());
 Node moduleVariable = Node.newName("myModule");
 moduleVariable.setString("myModule");
 moduleVariable.setProperty(Node.ORIGINALNAME_PROP, "myModule");
 moduleVariable.setProperty("module$", moduleModuleExports);
 moduleVariable.setProperty(Node.ORIGINALNAME_PROP, "myModule");
 Node moduleScript = Node.newScript(
 "var myModule = " + moduleVariable.toString() + ";");

 rootNode.setScript(0, moduleScript);
 Node commonModule = Node.newObjectLiteral();
 commonModule.setProperty("id", Node.newString("commonModule"));
 commonModule.setProperty("filename", Node.newString("commonModule.js"));
 commonModule.setProperty("exports", Node.newObjectLiteral());
 commonModule.setProperty("exports.foo", Node.newString("bar"));
 rootNode.setChild(1, moduleRequire);
 rootNode.setChild(2, commonModule);

 pass.process(input, rootNode);
 Node actualOutput = input.getOutput();

 assertEquals("(function (){\n" +
 "  require('commonModule.js');\n" +
 "  var myModule = commonModule;\n" +
 "  myModule = myModule.module$exports;\n" +
 "  goog.provide('myModule');\n" +
 "})();\n", actualOutput.toString());
 }

 @Test
 public void testProcessCommonJSModulesWithModulesHavingSameName() {
 CompilerOptions options = new CompilerOptions(null);
 AbstractCompiler compiler = new JSCompiler(options, null, null);
 CompilerInput input = new CompilerInput(new NodeFactory(), null);

 ProcessCommonJSModules pass = new ProcessCommonJSModules(compiler, "");
 input.setFile("test1.js", "test module 1");
 input.setSourceFileName("test1.js");
 input.setInput("test code 1", "test1.js");

 Node test1RootNode = Node.root();
 Node test1ModuleRequire = Node.newCall(Node.newName("require"),
 Node.newString("test2Module"));
 Node test1ModuleModuleExports = Node.newObjectLiteral();
 test1ModuleModuleExports.setProperty("module$exports", Node.newObjectLiteral());
 Node test1ModuleVariable = Node.newName("test1Module");
 test1ModuleVariable.setString("test1Module");
 test1ModuleVariable.setProperty(Node.ORIGINALNAME_PROP, "test1Module");
 test1ModuleVariable.setProperty("module$", test1ModuleModuleExports);
 test1ModuleVariable.setProperty(Node.ORIGINALNAME_PROP, "test1Module");
 Node test1ModuleScript = Node.newScript(
 "var test1Module = " + test1ModuleVariable.toString() + ";");

 test1RootNode.setScript(0, test1ModuleScript);
 Node test1CommonModule = Node.newObjectLiteral();
 test1CommonModule.setProperty("id", Node.newString("test2Module"));
 test1CommonModule.setProperty("filename", Node.newString("test2Module.js"));
 test1CommonModule.setProperty("exports", Node.newObjectLiteral());
 test1CommonModule.setProperty("exports.foo", Node.newString("bar1"));
 test1RootNode.setChild(1, test1ModuleRequire);
 test1RootNode.setChild(2, test1CommonModule);

 input.setFile("test2.js", "test module 2");
 input.setSourceFileName("test2.js");
 input.setInput("test code 2", "test2.js");

 Node test2RootNode = Node.root();
 Node test2ModuleRequire = Node.newCall(Node.newName("require"),
 Node.newString("test1Module"));
 Node test2ModuleModuleExports = Node.newObjectLiteral();
 test2ModuleModuleExports.setProperty("module$exports", Node.newObjectLiteral());
 Node test2ModuleVariable = Node.newName("test2Module");
 test2ModuleVariable.setString("test2Module");
 test2ModuleVariable.setProperty(Node.ORIGINALNAME_PROP, "test2Module");
 test2ModuleVariable.setProperty("module$", test2ModuleModuleExports);
 test2ModuleVariable.setProperty(Node.ORIGINALNAME_PROP, "test2Module");
 Node test2ModuleScript = Node.newScript(
 "var test2Module = " + test2ModuleVariable.toString() + ";");

 test2RootNode.setScript(0, test2ModuleScript);
 Node test2CommonModule = Node.newObjectLiteral();
 test2CommonModule.setProperty("id", Node.newString("test1Module"));
 test2CommonModule.setProperty("filename", Node.newString("test1Module.js"));
 test2CommonModule.setProperty("exports", Node.newObjectLiteral());
 test2CommonModule.setProperty("exports.foo", Node.newString("bar2"));
 test2RootNode.setChild(1, test2ModuleRequire);
 test2RootNode.setChild(2, test2CommonModule);

 pass.process(input, test1RootNode);
 Node test1ActualOutput = input.getOutput();

 input.setFile("test2.js", null);
 input.setFile("test1.js", null);
 input.setInput(null, null);
 input.setSourceFileName(null);
 pass.process(input, test2RootNode);
 Node test2ActualOutput = input.getOutput();

 assertEquals("(function (){\n" +
 "  require('test1Module');\n" +
 "  test1Module = test1Module.module$exports;\n" +
 "  goog.provide('test2Module');\n" +
 "  test2Module.module$exports = test1Module;\n" +
 "})();\n", test2ActualOutput.toString());
 assertEquals("(function (){\n" +
 "  require('test2Module');\n" +
 "  test2Module = test2Module.module$exports;\n" +
 "  test2Module.module$exports = {\n" +
 "    foo: 'bar2'\n" +
 "  };\n" +
 "  goog.provide('test1Module');\n" +
 "  test1Module.module$exports = {\n" +
 "    foo: 'bar1'\n" +
 "  };\n" +
 "})();\n", test1ActualOutput.toString());
 }
}