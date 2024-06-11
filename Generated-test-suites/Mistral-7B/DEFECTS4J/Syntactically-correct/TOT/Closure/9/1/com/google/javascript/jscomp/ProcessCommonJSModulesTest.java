import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.ProcessCommonJSModules;
import com.google.javascript.rhino.NodeFactory;

public class ProcessCommonJSModulesTest {

 @Test
 public void testProcessCommonJSModules() {
 Compiler compiler = new Compiler();
 CompilerOptions options = new CompilerOptions();

 // Given a valid CommonJS module file
 Node sourceCode = NodeFactory.createTextNode(
 "var module = module || { exports: {} };\n" +
 "exports.hello = function() {\n" +
 " return 'Hello World';\n" +
 "};");

 ProcessCommonJSModules processor = new ProcessCommonJSModules(compiler, "", false);
 processor.process(NodeFactory.createMap(), sourceCode);

 // When processing the code
 Node processedCode = processor.getModule().getSourceNode();

 // Then the output should not contain require statement
 assertFalse(processedCode.getChildAtIndex(0).isCall());

 // And the hello function should be in the global scope
 assertEquals("hello", processedCode.getChildAtIndex(1).getName().getString());

 // Given a CommonJS module with dependencies
 sourceCode = NodeFactory.createTextNode(
 "var module = module || { exports: {} };\n" +
 "require('dep1');\n" +
 "exports.hello = function() {\n" +
 " return 'Hello World';\n" +
 "};");

 processor = new ProcessCommonJSModules(compiler, "", true);
 processor.process(NodeFactory.createMap(), sourceCode);

 // When processing the code
 processedCode = processor.getModule().getSourceNode();

 // Then the dependencies should be reported
 assertEquals(1, processor.getInput().getRequireCount());
 assertEquals("dep1", processor.getInput().getRequire(0));

 // And the hello function should be in the global scope
 assertEquals("hello", processedCode.getChildAtIndex(1).getName().getString());

 // Given a CommonJS module with multiple exports
 sourceCode = NodeFactory.createTextNode(
 "var module = module || { exports: {} };\n" +
 "exports.hello = function() {\n" +
 " return 'Hello World';\n" +
 "};\n" +
 "exports.greet = function(name) {\n" +
 " return 'Hello ' + name;\n" +
 "};");

 processor = new ProcessCommonJSModules(compiler, "", false);
 processor.process(NodeFactory.createMap(), sourceCode);

 // When processing the code
 processedCode = processor.getModule().getSourceNode();

 // Then the hello and greet functions should be in the global scope
 assertEquals("hello", processedCode.getChildAtIndex(1).getName().getString());
 assertEquals("greet", processedCode.getChildAtIndex(2).getName().getString());
 }
}