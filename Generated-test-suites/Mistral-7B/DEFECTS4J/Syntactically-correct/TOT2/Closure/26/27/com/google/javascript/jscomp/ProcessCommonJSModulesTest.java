import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.jscomp.ProcessCommonJSModules;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.SourceName;

public class ProcessCommonJSModulesTest {

 private Compiler compiler;
 private CompilerOptions options;

 @Before
 public void setup() {
 compiler = new Compiler();
 options = new CompilerOptions();
 }

 @Test
 public void testProcessCommonJSModules() {
 String inputFileName = "testInput.js";
 String expectedOutput = "testOutput.js";

 // Given a valid input file
 String inputCode = "require('./module1.js');";
 SourceName inputSourceName = new SourceName(inputFileName);
 Node inputNode = IR.parseStatement(inputCode).getFirstChild();

 // When processing the input file with ProcessCommonJSModules
 ProcessCommonJSModules pass = new ProcessCommonJSModules(compiler,
 inputFileName.substring(0, inputFileName.lastIndexOf(File.separator)), true);
 pass.process(null, inputNode);

 // Then the output should be as expected
 String actualOutput = compiler.getEmittedCode();
 assertEquals(expectedOutput, actualOutput);
 }

 @Test(expected = RuntimeException.class)
 public void testInvalidURI() {
 String inputFileName = "testInput.js";
 String requiredFilename = "invalidURI:./.";
 String currentFilename = "testInput.js";

 // Given an input file and a require call with an invalid URI
 String inputCode = "require(" + requiredFilename + ");";
 SourceName inputSourceName = new SourceName(inputFileName);
 Node inputNode = IR.parseStatement(inputCode).getFirstChild();

 // When processing the input file with ProcessCommonJSModules
 ProcessCommonJSModules pass = new ProcessCommonJSModules(compiler,
 inputFileName.substring(0, inputFileName.lastIndexOf(File.separator)), true);
 pass.process(null, inputNode);

 // Then an exception should be thrown
 }

 @Test
 public void testModuleRenaming() {
 String inputFileName = "testInput.js";
 String moduleName = "module1";
 String expectedOutput = "testOutput.js";

 // Given a valid input file with a module named 'module1'
 String inputCode = "var module1 = {exports: {foo: 'bar'}};";
 SourceName inputSourceName = new SourceName(inputFileName);
 Node inputNode = IR.parseStatement(inputCode).getFirstChild();

 // When processing the input file with ProcessCommonJSModules
 ProcessCommonJSModules pass = new ProcessCommonJSModules(compiler,
 inputFileName.substring(0, inputFileName.lastIndexOf(File.separator)), true);
 pass.process(null, inputNode);

 // Then the output should be as expected, with the module renamed to 'module1$$module1'
 String actualOutput = compiler.getEmittedCode();
 String expectedOutputWithSuffix = expectedOutput.replaceAll(moduleName,
 moduleName + "$$" + moduleName);
 assertEquals(expectedOutputWithSuffix, actualOutput);
 }

 @Test
 public void testModuleExports() {
 String inputFileName = "testInput.js";
 String moduleName = "module1";
 String expectedOutput = "testOutput.js";

 // Given a valid input file with a module named 'module1' and exports
 String inputCode = "var module1 = {exports: {foo: 'bar'}};";
 SourceName inputSourceName = new SourceName(inputFileName);
 Node inputNode = IR.parseStatement(inputCode).getFirstChild();

 // When processing the input file with ProcessCommonJSModules
 ProcessCommonJSModules pass = new ProcessCommonJSModules(compiler,
 inputFileName.substring(0, inputFileName.lastIndexOf(File.separator)), true);
 pass.process(null, inputNode);

 // Then the output should contain a module exports assignment
 Node actualOutput = compiler.getRootNode();
 Node moduleExports = actualOutput.mayContainMember("module$exports");
 assertTrue(moduleExports != null);
 }

 @Test
 public void testRequiringModule() {
 String inputFileName = "testInput.js";
 String requiredModuleName = "module1";
 String expectedOutput = "testOutput.js";

 // Given a valid input file with a require call for a module named 'module1'
 String inputCode = "require('./" + requiredModuleName + "');";
 SourceName inputSourceName = new SourceName(inputFileName);
 Node inputNode = IR.parseStatement(inputCode).getFirstChild();

 // When processing the input file with ProcessCommonJSModules
 ProcessCommonJSModules pass = new ProcessCommonJSModules(compiler,
 inputFileName.substring(0, inputFileName.lastIndexOf(File.separator)), true);
 pass.process(null, inputNode);

 // Then the output should contain the required module code directly, without a require call
 String actualOutput = compiler.getEmittedCode();
 String expectedOutputWithoutRequire = expectedOutput.replaceAll("require\\(.*\\)", "");
 assertEquals(expectedOutputWithoutRequire, actualOutput);
 }
}