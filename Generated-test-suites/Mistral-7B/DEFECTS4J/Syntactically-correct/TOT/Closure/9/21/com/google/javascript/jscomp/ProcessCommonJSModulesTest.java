import static org.junit.Assert.*;
import static com.google.javascript.jscomp.ProcessCommonJSModules.*;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.rhino.IR;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Set;

public class ProcessCommonJSModulesTest {

  @org.junit.Test
  public void testProcessCommonJSModules() throws IOException, URISyntaxException {
    String inputFile = "input.js";
    String expectedOutputFile = "expectedOutput.js";

    // Set up input file
    File input = new File(getClass().getResource(inputFile).getPath());
    SourceFile inputSource = new SourceFile(input.getAbsolutePath());

    // Set up options
    CompilerOptions options = new CompilerOptions(null);
    options.setOutputFile(new File(expectedOutputFile));
    options.addPass(new ProcessCommonJSModules(
        new TestCompiler(options), DEFAULT_FILENAME_PREFIX));

    // Compile
    Compiler compiler = new Compiler(options);
    compiler.compile(Arrays.asList(inputSource), null);

    // Check output
    File output = new File(expectedOutputFile);
    String outputCode = FileUtils.readFileToString(output, "UTF-8");
    String expectedOutputCode = FileUtils.readFileToString(
        new File(getClass().getResource(expectedOutputFile + ".expected").getPath()),
        "UTF-8");
    assertEquals(expectedOutputCode, outputCode);
  }

  private static class TestCompiler implements CompilerPass {

    private final Compiler compiler;

    TestCompiler(CompilerOptions options) {
      this.compiler = new Compiler(options);
    }

    @Override
    public void process(Node externs, Node root) {
      compiler.process(externs, root);
    }
  }

  @org.junit.Test
  public void testGuessCJSModuleName() {
    String filename = "test/path/to/module.js";
    String expectedModuleName = "test$path$to$module";
    assertEquals(expectedModuleName, guessCJSModuleName(filename));
  }

  @org.junit.Test
  public void testToModuleName() {
    String filename = "test/path/to/module.js";
    String requiredFilename = "other/path/to/require";
    String currentFilename = "test/path/to/script.js";
    String expectedModuleName = "test$path$to$module";

    assertEquals(expectedModuleName, toModuleName(filename));
    assertEquals(expectedModuleName, toModuleName(requiredFilename, currentFilename));
  }

  @org.junit.Test
  public void testNormalizeSourceName() {
    String filename = "test/path/to/module.js";
    String expectedNormalizedFilename = "test/path/to/module";
    assertEquals(expectedNormalizedFilename, normalizeSourceName(filename));
  }

  @org.junit.Test
  public void testProcessCommonJsModulesCallback_requireCall() {
    NodeTraversal nodeTraversal = new NodeTraversal(null, new TestCompiler(null));
    Node root = IR.program();
    Node requireCall = IR.call(IR.name("require"), IR.string("module"));
    root.addChildToBack(requireCall);

    nodeTraversal.traverse(root);

    Node requireCallTransformed = requireCall.getLastChild();
    Node moduleRef = requireCallTransformed.getFirstChild();
    assertEquals("module", moduleRef.getString());
  }

  @org.junit.Test
  public void testProcessCommonJsModulesCallback_scriptNode() {
    NodeTraversal nodeTraversal = new NodeTraversal(null, new TestCompiler(null));
    Node root = IR.program();
    Node script = IR.script();
    root.addChildToBack(script);

    nodeTraversal.traverse(root);

    Node module = script.getFirstChild();
    String expectedModuleName = "test$path$to$script";
    assertEquals(expectedModuleName, module.getString());
  }

  @org.junit.Test
  public void testProcessCommonJsModulesCallback_moduleExports() {
    NodeTraversal nodeTraversal = new NodeTraversal(null, new TestCompiler(null));
    Node root = IR.program();
    Node module = IR.exprResult(IR.assign(IR.name("module"), IR.objectLiteral()));
    root.addChildToBack(module);

    nodeTraversal.traverse(root);

    String expectedModuleName = "test$path$to$module";
    Node moduleName = module.getFirstChild();
    assertEquals(expectedModuleName, moduleName.getString());

    Node exports = module.getLastChild();
    String expectedExportsName = "module$exports";
    assertEquals(expectedExportsName, exports.getString());
  }

  @org.junit.Test
  public void testProcessCommonJsModulesCallback_moduleWithExports() {
    NodeTraversal nodeTraversal = new NodeTraversal(null, new TestCompiler(null));
    Node root = IR.program();

    Node module = IR.exprResult(IR.assign(IR.name("module"), IR.objectLiteral()));
    module.addChildToBack(IR.prop(IR.string("exports"), IR.objectLiteral()));
    root.addChildToBack(module);

    nodeTraversal.traverse(root);

    String expectedModuleName = "test$path$to$module";
    Node moduleName = module.getFirstChild();
    assertEquals(expectedModuleName, moduleName.getString());

    Node exports = module.getLastChild();
    String expectedExportsName = "module$exports";
    assertEquals(expectedExportsName, exports.getString());
  }

  @org.junit.Test
  public void testProcessCommonJsModulesCallback_renameVars() {
    NodeTraversal nodeTraversal = new NodeTraversal(null, new TestCompiler(null));
    Node root = IR.program();
    root.addChildToBack(IR.var(IR.name("globalVar")));

    nodeTraversal.traverse(root);

    Node globalVar = root.getLastChild();
    String expectedGlobalVarName = "globalVar$$test$path$to$module";
    assertEquals(expectedGlobalVarName, globalVar.getString());
  }

  @org.junit.Test
  public void testProcessCommonJsModulesCallback_renameModuleExports() {
    NodeTraversal nodeTraversal = new NodeTraversal(null, new TestCompiler(null));
    Node root = IR.program();
    Node module = IR.exprResult(IR.assign(IR.name("module"), IR.objectLiteral()));
    module.addChildToBack(IR.prop(IR.string("exports"), IR.objectLiteral()));
    root.addChildToBack(module);

    nodeTraversal.traverse(root);

    String expectedModuleName = "test$path$to$module";
    Node moduleName = module.getFirstChild();
    assertEquals(expectedModuleName, moduleName.getString());

    Node exports = module.getLastChild();
    String expectedExportsName = "module$exports$$test$path$to$module";
    assertEquals(expectedExportsName, exports.getString());
  }
}