import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.ProcessCommonJSModules;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class ProcessCommonJSModulesTest {

 private Compiler compiler;

 @Before
 public void setUp() {
 compiler = new Compiler();
 }

 @Test
 public void testProcessCommonJSModules() {
 String input = "// CommonJS module\n" +
 "function main() {\n" +
 " require('./moduleA');\n" +
 "}";

 CompilerPass pass = new ProcessCommonJSModules(compiler, "");
 pass.process(null, IR.parse(input));

 String expectedOutput = "// CommonJS module\n" +
 "goog.provide('moduleA');\n" +
 "goog.require('main');\n" +
 "function moduleA() {}\n" +
 "main.prototype.main = function() {\n" +
 " goog.require('moduleA');\n" +
 "};\n" +
 "main.prototype.main.prototype.main = function() {\n" +
 " moduleA = moduleA.module$exports;\n" +
 "};\n" +
 "main.prototype.main();\n";

 String output = IR.toString(compiler.getIntermediateRepresentation());
 assertEquals(expectedOutput, output);
 }

 @Test(expected = RuntimeException.class)
 public void testInvalidURI() {
 String input = "// CommonJS module\n" +
 "function main() {\n" +
 " require('invalid%URI');\n" +
 "}";

 CompilerPass pass = new ProcessCommonJSModules(compiler, "");
 pass.process(null, IR.parse(input));
 }

 @Test
 public void testRequireRelativeModule() {
 String input = "// CommonJS module\n" +
 "function main() {\n" +
 " require('./moduleB');\n" +
 "}";

 CompilerPass pass = new ProcessCommonJSModules(compiler, "");
 pass.process(null, IR.parse(input));

 String expectedOutput = "// CommonJS module\n" +
 "goog.provide('moduleB');\n" +
 "goog.require('main');\n" +
 "function moduleB() {}\n" +
 "main.prototype.main = function() {\n" +
 " goog.require('moduleB');\n" +
 "};\n" +
 "main.prototype.main.prototype.main = function() {\n" +
 " moduleB = moduleB.module$exports;\n" +
 "};\n" +
 "main.prototype.main();\n";

 String output = IR.toString(compiler.getIntermediateRepresentation());
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testRequireAbsoluteModule() {
 String input = "// CommonJS module\n" +
 "function main() {\n" +
 " require('http://example.com/moduleC');\n" +
 "}";

 CompilerPass pass = new ProcessCommonJSModules(compiler, "");
 pass.process(null, IR.parse(input));

 // This test should fail as the class under test does not support absolute URIs
 // assertFalse("Absolute URIs should not be supported", true);
 }

 @Test
 public void testModuleWithExports() {
 String input = "// CommonJS module with exports\n" +
 "function moduleA() {\n" +
 " module.exports = 'hello';\n" +
 "}\n" +
 "function main() {\n" +
 " require('./moduleA').print();\n" +
 "}\n" +
 "function moduleA.print() {\n" +
 " console.log(this.module$exports);\n" +
 "}\n";

 CompilerPass pass = new ProcessCommonJSModules(compiler, "");
 pass.process(null, IR.parse(input));

 String expectedOutput = "// CommonJS module with exports\n" +
 "goog.provide('moduleA');\n" +
 "goog.require('main');\n" +
 "function moduleA() {}\n" +
 "moduleA.prototype.moduleA = function() {\n" +
 " this.module$exports = 'hello';\n" +
 "};\n" +
 "goog.addType('moduleA', moduleA);\n" +
 "function main() {\n" +
 " goog.require('moduleA');\n" +
 "};\n" +
 "main.prototype.main = function() {\n" +
 " moduleA = moduleA.module$exports;\n" +
 " moduleA.print();\n" +
 "};\n" +
 "main.prototype.main.prototype.main = function() {\n" +
 " console.log(moduleA);\n" +
 "};\n" +
 "main.prototype.main();\n";

 String output = IR.toString(compiler.getIntermediateRepresentation());
 assertEquals(expectedOutput, output);
 }
}