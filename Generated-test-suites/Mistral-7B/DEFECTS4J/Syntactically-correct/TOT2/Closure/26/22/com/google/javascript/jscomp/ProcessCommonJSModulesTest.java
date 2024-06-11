import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.JSModule;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.ProcessCommonJSModules;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ProcessCommonJSModulesTest {

 private AbstractCompiler compiler;
 private ProcessCommonJSModules processor;
 private CompilerInput input;

 @Before
 public void setup() throws IOException, URISyntaxException {
 compiler = new AbstractCompiler();
 input = new CompilerInput(new File("test.js"));
 processor = new ProcessCommonJSModules(compiler, input.getSourceFileName());
 }

 @Test
 public void testProcessCommonJSModules() {
 Node root = IR.literalNull();
 processor.process(input.getExterns(), root);
 assertEquals(0, root.getChildCount());
 }

 @Test
 public void testProcessRequireCall() throws IOException, URISyntaxException {
 // Given: A require call in a script file
 Node requireCall = IR.call(IR.getprop(IR.name("require"), IR.string("module")),
 IR.string("test"));
 Node script = IR.script();
 script.addChild(requireCall);

 // When: Processing the script file
 processor.process(input.getExterns(), script);

 // Then: The require call is replaced by a direct reference to the required module
 Node expectedModuleRef = IR.name("test");
 assertEquals(expectedModuleRef, script.getChildAtIndex(0));
 }

 @Test
 public void testProcessRequireRelativeCall() throws IOException, URISyntaxException {
 // Given: A require call with a relative path in a script file
 Node requireCall = IR.call(IR.getprop(IR.name("require"), IR.string("module")),
 IR.string("./test"));
 Node script = IR.script();
 script.addChild(requireCall);

 // When: Processing the script file
 processor.process(input.getExterns(), script);

 // Then: The require call is replaced by a direct reference to the required module
 String testFileName = "test.js";
 Node expectedModuleRef = IR.name(toModuleName(testFileName, input.getSourceFileName()));
 assertEquals(expectedModuleRef, script.getChildAtIndex(0));
 }

 @Test
 public void testProcessRequireCallWithModuleNameCollision()
 throws IOException, URISyntaxException {
 // Given: A require call with a name that collides with a global variable
 Node requireCall = IR.call(IR.getprop(IR.name("require"), IR.string("module")),
 IR.string("module"));
 Node script = IR.script();
 script.addChild(requireCall);

 // When: Processing the script file
 processor.process(input.getExterns(), script);

 // Then: The require call is renamed to avoid a collision with the global variable
 Node expectedModuleRef = IR.name("module$$module");
 assertEquals(expectedModuleRef, script.getChildAtIndex(0));
 }

 @Test
 public void testProcessRequireCallWithMissingModule()
 throws IOException, URISyntaxException {
 // Given: A require call for a missing module
 Node requireCall = IR.call(IR.getprop(IR.name("require"), IR.string("missing")),
 IR.undefined());
 Node script = IR.script();
 script.addChild(requireCall);

 // When: Processing the script file
 processor.process(input.getExterns(), script);

 // Then: The require call emits a runtime error
 Node expectedError = IR.call(IR.getprop(IR.name("goog"), IR.string("error")),
 IR.string("Module 'missing' not found"));
 assertEquals(expectedError, script.getChildAtIndex(0));
 }

 @Test
 public void testProcessModuleExports() throws IOException, URISyntaxException {
 // Given: A module exports assignment in a script file
 Node exportsAssignment = IR.assign(IR.getprop(IR.name("module$exports"), IR.nullLiteral()),
 IR.string("Hello World"));
 Node script = IR.script();
 script.addChild(exportsAssignment);
 // When: Processing the script file
 processor.process(input.getExterns(), script);
 // Then: The exports assignment is renamed to avoid a collision with other exports
 String moduleName = toModuleName(input.getSourceFileName());
 Node expectedModuleExports = IR.getprop(IR.name(moduleName), IR.string("module$exports"));
 assertEquals(expectedModuleExports, script.getChildAtIndex(0));
 assertEquals(IR.string("Hello World"),
 script.getChildAtIndex(script.getChildCount() - 1));
 }

 @Test
 public void testProcessModuleExportsWithMissingModule()
 throws IOException, URISyntaxException {
 // Given: A module exports assignment for a missing module
 Node exportsAssignment = IR.assign(IR.getprop(IR.name("module$exports"), IR.nullLiteral()),
 IR.getprop(IR.name("require"), IR.string("missing")));
 Node script = IR.script();
 script.addChild(exportsAssignment);
 // When: Processing the script file
 processor.process(input.getExterns(), script);
 // Then: The exports assignment emits a runtime error
 Node expectedError = IR.call(IR.getprop(IR.name("goog"), IR.string("error")),
 IR.string("Module 'missing' not found"));
 assertEquals(expectedError, script.getChildAtIndex(0));
 }

 @Test
 public void testProcessModuleExportsWithCyclicDependency()
 throws IOException, URISyntaxException {
 // Given: Two modules with a cyclic dependency
 String moduleA = "moduleA";
 String moduleB = "moduleB";
 Node moduleARequireCall = IR.call(IR.getprop(IR.name("require"), IR.string(moduleB)),
 IR.undefined());
 Node moduleBRequireCall = IR.call(IR.getprop(IR.name("require"), IR.string(moduleA)),
 IR.undefined());
 Node moduleAScript = IR.script();
 Node moduleBScript = IR.script();
 moduleAScript.addChild(moduleARequireCall);
 moduleBScript.addChild(moduleBRequireCall);
 Node root = IR.script();
 root.addChild(moduleAScript);
 root.addChild(moduleBScript);
 // When: Processing the script file
 processor.process(input.getExterns(), root);
 // Then: The cyclic dependency emits a runtime error
 Node expectedError = IR.call(IR.getprop(IR.name("goog"), IR.string("error")),
 IR.string("Circular dependency: " + moduleA + " requires " + moduleB +
 " which requires " + moduleA));
 assertEquals(expectedError, root.getChildAtIndex(0));
 }

 @Test
 public void testProcessModuleExportsWithModuleNameCollision()
 throws IOException, URISyntaxException {
 // Given: Two modules with the same name
 String moduleA = "module";
 Node moduleARequireCall = IR.call(IR.getprop(IR.name("require"), IR.string(moduleA)),
 IR.undefined());
 Node moduleAScript = IR.script();
 moduleAScript.addChild(moduleARequireCall);
 Node moduleBRequireCall = IR.call(IR.getprop(IR.name("require"), IR.string(moduleA)),
 IR.undefined());
 Node moduleBScript = IR.script();
 moduleBScript.addChild(moduleBRequireCall);
 Node root = IR.script();
 root.addChild(moduleAScript);
 root.addChild(moduleBScript);
 // When: Processing the script file
 processor.process(input.getExterns(), root);
 // Then: The module names are renamed to avoid a collision
 String moduleAModuleName = toModuleName(input.getSourceFileName() + "_A");
 String moduleBModuleName = toModuleName(input.getSourceFileName() + "_B");
 Node expectedModuleAReference = IR.name(moduleAModuleName);
 Node expectedModuleBReference = IR.name(moduleBModuleName);
 assertEquals(expectedModuleAReference, root.getChildAtIndex(0).getChildAtIndex(0));
 assertEquals(expectedModuleBReference, root.getChildAtIndex(1).getChildAtIndex(0));
 }

 @Test
 public void testProcessModuleWithEmptyContent() throws IOException, URISyntaxException {
 // Given: An empty module file
 Node emptyModuleScript = IR.script();
 Node root = IR.script();
 root.addChild(emptyModuleScript);
 // When: Processing the script file
 processor.process(input.getExterns(), root);
 // Then: The empty module file is processed correctly
 assertEquals(0, root.getChildCount());
 }

 @Test
 public void testProcessModuleWithSingleStatement() throws IOException, URISyntaxException {
 // Given: A module file with a single statement
 Node singleStatementModuleScript = IR.script();
 singleStatementModuleScript.addChild(IR.exprStatement(IR.assign(
 IR.getprop(IR.name("module$exports"), IR.nullLiteral()), IR.number(42))));
 Node root = IR.script();
 root.addChild(singleStatementModuleScript);
 // When: Processing the script file
 processor.process(input.getExterns(), root);
 // Then: The single statement is executed and the exports object is updated
 assertEquals(IR.number(42), root.getChildAtIndex(0).getChildAtIndex(1));
 }

 @Test
 public void testProcessModuleWithMultipleStatements() throws IOException, URISyntaxException {
 // Given: A module file with multiple statements
 Node multipleStatementsModuleScript = IR.script();
 multipleStatementsModuleScript.addChild(IR.assign(IR.getprop(IR.name("module$exports"), IR.nullLiteral()),
 IR.number(1)));
 multipleStatementsModuleScript.addChild(IR.assign(IR.getprop(IR.name("module$exports"), IR.name("foo")),
 IR.string("bar")));
 Node root = IR.script();
 root.addChild(multipleStatementsModuleScript);
 // When: Processing the script file
 processor.process(input.getExterns(), root);
 // Then: The multiple statements are executed and the exports object is updated
 assertEquals(IR.number(1), root.getChildAtIndex(0).getChildAtIndex(1));
 assertEquals(IR.string("bar"), root.getChildAtIndex(0).getChildAtIndex(2));
 }

 private String toModuleName(String filename) {
 return ProcessCommonJSModules.toModuleName(filename);
 }
}