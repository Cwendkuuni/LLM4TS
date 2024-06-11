import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeTraversal.*;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.ProcessCommonJSModules;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.Parser;
import com.google.javascript.rhino.IR;
import java.io.File;
import java.io.IOException;
import java.net.URL;

public class ProcessCommonJSModulesTest {

 private Compiler compiler;
 private CompilerOptions options;

 @Before
 public void setUp() throws Exception {
 compiler = new Compiler();
 options = new CompilerOptions();
 }

 @Test
 public void testProcessCommonJSModules() throws IOException {
 String inputFileName = "input.js";
 String expectedOutputFileName = "output.js";

 File inputFile = new File(inputFileName);
 File expectedOutputFile = new File(expectedOutputFileName);

 // Test with default options
 processAndTest(inputFile, expectedOutputFile, options);

 // Test with reportDependencies flag enabled
 options.setOption(CompilerOptions.Option.OUTPUT_FILE, "output_reportDependencies.js");
 processAndTest(inputFile, expectedOutputFile, options);
 }

 private void processAndTest(File inputFile, File expectedOutputFile, CompilerOptions options)
 throws IOException {
 // Compile the input file
 Node root = Parser.parse(new File(inputFile.getAbsolutePath()), null, options);
 CompilerPass pass = new ProcessCommonJSModules(compiler, options.getOutputDirectory(), options.getOption(CompilerOptions.Option.REPORT_DEPENDENCIES));
 pass.process(null, root);

 // Check the output
 String actualOutput = compiler.getDiagnostics().getDiagnostics().get(0).getCode().getOutputText();
 String expectedOutput = new String(expectedOutputFile.length() > 0 ? new File(expectedOutputFile.getAbsolutePath()).getBytes() : new byte[0]);
 assertEquals(new String(expectedOutput), actualOutput);
 }

 @Test
 public void testProcessRequireWithAbsolutePath() throws IOException {
 String inputFileName = "input_require_absolute.js";
 String expectedOutputFileName = "output_require_absolute.js";

 File inputFile = new File(inputFileName);
 File expectedOutputFile = new File(expectedOutputFileName);

 // Test with default options
 processAndTest(inputFile, expectedOutputFile, options);
 }

 @Test
 public void testProcessRequireWithRelativePath() throws IOException {
 String inputFileName = "input_require_relative.js";
 String expectedOutputFileName = "output_require_relative.js";

 File inputFile = new File(inputFileName);
 File expectedOutputFile = new File(expectedOutputFileName);

 // Test with default options
 processAndTest(inputFile, expectedOutputFile, options);
 }

 @Test(expected = RuntimeException.class)
 public void testProcessRequireWithNonExistentModule() throws IOException {
 String inputFileName = "input_require_non_existent.js";
 String expectedOutputFileName = "output_require_non_existent.js";

 File inputFile = new File(inputFileName);
 File expectedOutputFile = new File(expectedOutputFileName);

 // Test with default options
 processAndTest(inputFile, expectedOutputFile, options);
 }

 @Test
 public void testProcessModuleExports() throws IOException {
 String inputFileName = "input_module_exports.js";
 String expectedOutputFileName = "output_module_exports.js";

 File inputFile = new File(inputFileName);
 File expectedOutputFile = new File(expectedOutputFileName);

 // Test with default options
 processAndTest(inputFile, expectedOutputFile, options);
 }

 @Test
 public void testProcessModuleExportsWithRenamedVariable() throws IOException {
 String inputFileName = "input_module_exports_renamed.js";
 String expectedOutputFileName = "output_module_exports_renamed.js";

 File inputFile = new File(inputFileName);
 File expectedOutputFile = new File(expectedOutputFileName);

 // Test with default options
 processAndTest(inputFile, expectedOutputFile, options);
 }

 @After
 public void tearDown() throws Exception {
 compiler = null;
 options = null;
 }
}