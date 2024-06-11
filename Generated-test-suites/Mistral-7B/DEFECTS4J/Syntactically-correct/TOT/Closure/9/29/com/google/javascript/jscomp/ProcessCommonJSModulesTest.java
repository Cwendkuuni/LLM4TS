import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.JSModule;
import com.google.javascript.rhino.Node;

public class ProcessCommonJSModulesTest {

 private CompilerInput createTestInput(String fileName, String content) {
 return new CompilerInput(null, new Node(fileName), null, null, null, null, null);
 }

 @Test
 public void testProcessCommonJSModules() {
 // Given a simple CommonJS module
 String inputFileName = "input.js";
 String inputContent = "var module = {\n" +
 "exports: {\n" +
 "hello: function() {\n" +
 "console.log('Hello world!');\n" +
 "}\n" +
 "}";
 CompilerInput input = createTestInput(inputFileName, inputContent);

 AbstractCompiler compiler = new AbstractCompiler() {};
 ProcessCommonJSModules p = new ProcessCommonJSModules(compiler, ".");
 p.process(input, null);

 // When processing the CommonJS module
 Node output = input.getRootNode();

 // Then the output should have goog.provide and goog.require statements
 assertTrue(output.isCall() && "goog.provide".equals(output.getFirstChild().getString()));
 assertTrue(output.isCall() && "goog.require".equals(output.getLastChild().getString()));

 // And the exports should be accessible as module.exports
 Node exports = output.findChildByType("GetProp", "module.exports");
 assertEquals(1, exports.getChildCount());
 Node helloFunction = exports.getChildAtIndex(0);
 assertTrue(helloFunction.isFunction());

 // And the "hello" function should be callable
 assertTrue(helloFunction.call(null));

 // Given a CommonJS module with a dependency
 String dependencyFileName = "dependency.js";
 String dependencyContent = "var message = 'Hello dependency!';";
 CompilerInput dependencyInput = createTestInput(dependencyFileName, dependencyContent);
 input = new CompilerInput(input, null, null, null, dependencyInput, null, null);

 p.process(input, null);

 // And the dependency should be imported correctly
 Node dependencyNode = input.getRootNode().findChildByType("RequireCall");
 assertEquals(1, dependencyNode.getChildCount());
 Node dependencyModuleNameNode = dependencyNode.getChildAtIndex(0);
 assertEquals("dependency", dependencyModuleNameNode.getString());

 // And the dependency's exports should be accessible
 exports = output.findChildByType("GetProp", "module.exports");
 assertEquals(1, exports.getChildCount());
 Node messageNode = exports.getChildAtIndex(0);
 assertEquals(messageContent, messageNode.getString());
 }

 @Test(expected = RuntimeException.class)
 public void testProcessingInvalidInput() {
 String inputFileName = "input.js";
 String inputContent = "var module = {\n" +
 "exports: {\n" +
 "hello: function() {\n" +
 "console.log('Hello world!');\n" +
 "}\n" +
 "}\n" +
 "console.log('This should fail');";
 CompilerInput input = createTestInput(inputFileName, inputContent);
 AbstractCompiler compiler = new AbstractCompiler() {};
 ProcessCommonJSModules p = new ProcessCommonJSModules(compiler, ".");
 p.process(input, null);
 fail("Expected a RuntimeException but no exception was thrown.");
 }
}