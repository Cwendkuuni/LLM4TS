import static org.junit.Assert.*;
import static com.google.javascript.jscomp.ProcessCommonJSModules.*;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.Parser;
import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.rhino.NodeFactory;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class ProcessCommonJSModulesTest {

  @org.junit.Test
  public void testProcessCommonJSModules() throws IOException {
    CompilerOptions options = new CompilerOptions(Arrays.asList("-js", "output=compiled.js", "-d", "."));
    AbstractCompiler compiler = new Compiler(options);
    Parser parser = new Parser(new File("input.js").getAbsolutePath());
    CompilerInput input = new CompilerInput("input.js", parser.parseTree(parser), null, null);
    ProcessCommonJSModules pass = new ProcessCommonJSModules(compiler, "");
    pass.process(input, null);
    SourceFile output = compiler.getOutputAsSourceFile("compiled.js");
    String expectedOutput = new File("expectedOutput.js").getCanonicalPath();
    assertEquals(expectedOutput, output.getSourceCode());
  }

  @org.junit.Test
  public void testProcessRequireCall() {
    NodeTraversal traversal = new NodeTraversal(new AbstractCompiler(new CompilerOptions(List.of("-js", "output=compiled.js", "-d", "."))));
    Node root = new Parser(new File("inputWithRequireCall.js").getAbsolutePath()).parseTree(parser);
    ProcessCommonJSModules pass = new ProcessCommonJSModules(traversal.getCompiler(), "");
    pass.process(traversal.getInput(), root);
    Node output = traversal.getOutput();
    Node requireCall = output.getFirstChild().getFirstChild();
    Node expectedOutput = IR.name("goog.require")
        .call(IR.string("moduleA"));
    assertEquals(expectedOutput.toString(), requireCall.toString());
  }

  @org.junit.Test
  public void testProcessModuleExports() {
    NodeTraversal traversal = new NodeTraversal(new AbstractCompiler(new CompilerOptions(List.of("-js", "output=compiled.js", "-d", "."))));
    Node root = new Parser(new File("inputWithModuleExports.js").getAbsolutePath()).parseTree(parser);
    ProcessCommonJSModules pass = new ProcessCommonJSModules(traversal.getCompiler(), "");
    pass.process(traversal.getInput(), root);
    Node output = traversal.getOutput();
    Node moduleExports = output.getLastChild().getLastChild();
    Node expectedOutput = IR.assign(IR.name("moduleB"), IR.name("moduleB$exports"));
    assertEquals(expectedOutput.toString(), moduleExports.toString());
  }

  @org.junit.Test
  public void testProcessScriptWithRelativePathRequireCall() {
    NodeTraversal traversal = new NodeTraversal(new AbstractCompiler(new CompilerOptions(List.of("-js", "output=compiled.js", "-d", "."))));
    Node root = new Parser(new File("inputWithRelativePathRequireCall.js").getAbsolutePath()).parseTree(parser);
    ProcessCommonJSModules pass = new ProcessCommonJSModules(traversal.getCompiler(), "");
    pass.process(traversal.getInput(), root);
    Node output = traversal.getOutput();
    Node requireCall = output.getFirstChild().getFirstChild();
    String expectedOutput = toModuleName("./moduleB.js", "moduleA.js");
    assertEquals(expectedOutput, requireCall.getChildAtIndex(1).getString());
  }

  @org.junit.Test
  public void testProcessScriptWithAbsolutePathRequireCall() {
    NodeTraversal traversal = new NodeTraversal(new AbstractCompiler(new CompilerOptions(List.of("-js", "output=compiled.js", "-d", "."))));
    Node root = new Parser(new File("inputWithAbsolutePathRequireCall.js").getAbsolutePath()).parseTree(parser);
    ProcessCommonJSModules pass = new ProcessCommonJSModules(traversal.getCompiler(), "");
    pass.process(traversal.getInput(), root);
    Node output = traversal.getOutput();
    Node requireCall = output.getFirstChild().getFirstChild();
    String expectedOutput = "moduleC";
    assertEquals(expectedOutput, toModuleName(requireCall.getChildAtIndex(1).getString(), "moduleA.js").getQualifiedName());
  }

  @org.junit.Test
  public void testProcessScriptWithModuleExports() {
    NodeTraversal traversal = new NodeTraversal(new AbstractCompiler(new CompilerOptions(List.of("-js", "output=compiled.js", "-d", "."))));
    Node root = new Parser(new File("inputWithModuleExports.js").getAbsolutePath()).parseTree(parser);
    ProcessCommonJSModules pass = new ProcessCommonJSModules(traversal.getCompiler(), "");
    pass.process(traversal.getInput(), root);
    Node output = traversal.getOutput();
    Node script = output.getLastChild();
    Node expectedOutput = IR.call(IR.getprop(IR.name("goog"), IR.string("require")), IR.string("moduleB"));
    List<Node> requireCalls = Arrays.asList(script.getFirstChild().getFirstChild(), script.getLastChild().getLastChild());
    for (Node requireCall : requireCalls) {
      assertEquals(expectedOutput.toString(), requireCall.toString());
    }
  }

  @org.junit.Test
  public void testProcessScriptWithModuleExportsAndRequireCall() {
    NodeTraversal traversal = new NodeTraversal(new AbstractCompiler(new CompilerOptions(List.of("-js", "output=compiled.js", "-d", "."))));
    Node root = new Parser(new File("inputWithModuleExportsAndRequireCall.js").getAbsolutePath()).parseTree(parser);
    ProcessCommonJSModules pass = new ProcessCommonJSModules(traversal.getCompiler(), "");
    pass.process(traversal.getInput(), root);
    Node output = traversal.getOutput();
    Node script = output.getLastChild();
    Node expectedOutput = IR.call(IR.getprop(IR.name("goog"), IR.string("require")), IR.string("moduleB"));
    Node requireCall = script.getLastChild().getLastChild();
    assertEquals(expectedOutput.toString(), requireCall.toString());
  }

  @org.junit.Test
  public void testProcessScriptWithModuleExportsAndRequireCallWithRenamedVariables() {
    NodeTraversal traversal = new NodeTraversal(new AbstractCompiler(new CompilerOptions(List.of("-js", "output=compiled.js", "-d", "."))));
    Node root = new Parser(new File("inputWithModuleExportsAndRequireCallWithRenamedVariables.js").getAbsolutePath()).parseTree(parser);
    ProcessCommonJSModules pass = new ProcessCommonJSModules(traversal.getCompiler(), "");
    pass.process(traversal.getInput(), root);
    Node output = traversal.getOutput();
    Node script = output.getLastChild();
    Node expectedOutput = IR.call(IR.getprop(IR.name("goog"), IR.string("require")), IR.string("moduleB"));
    Node requireCall = script.getLastChild().getLastChild();
    assertEquals(expectedOutput.toString(), requireCall.toString());
    assertEquals("renamedModuleC", script.getFirstChild().getLastChild().getString());
  }

  @org.junit.Test
  public void testProcessScriptWithModuleExportsAndModuleImport() {
    NodeTraversal traversal = new NodeTraversal(new AbstractCompiler(new CompilerOptions(List.of("-js", "output=compiled.js", "-d", "."))));
    Node root = new Parser(new File("inputWithModuleExportsAndModuleImport.js").getAbsolutePath()).parseTree(parser);
    ProcessCommonJSModules pass = new ProcessCommonJSModules(traversal.getCompiler(), "");
    pass.process(traversal.getInput(), root);
    Node output = traversal.getOutput();
    Node script = output.getLastChild();
    Node expectedOutput = IR.call(IR.getprop(IR.name("goog"), IR.string("require")), IR.string("moduleB"));
    Node importStatement = script.getFirstChild();
    assertEquals(expectedOutput.toString(), importStatement.getLastChild().toString());
  }

  @org.junit.Test
  public void testProcessScriptWithModuleExportsAndModuleImportWithRenamedVariables() {
    NodeTraversal traversal = new NodeTraversal(new AbstractCompiler(new CompilerOptions(List.of("-js", "output=compiled.js", "-d", "."))));
    Node root = new Parser(new File("inputWithModuleExportsAndModuleImportWithRenamedVariables.js").getAbsolutePath()).parseTree(parser);
    ProcessCommonJSModules pass = new ProcessCommonJSModules(traversal.getCompiler(), "");
    pass.process(traversal.getInput(), root);
    Node output = traversal.getOutput();
    Node script = output.getLastChild();
    Node expectedOutput = IR.call(IR.getprop(IR.name("goog"), IR.string("require")), IR.string("moduleB"));
    Node importStatement = script.getFirstChild();
    assertEquals(expectedOutput.toString(), importStatement.getLastChild().toString());
    assertEquals("renamedModuleC", script.getLastChild().getLastChild().getString());
  }

  @org.junit.Test
  public void testProcessScriptWithModuleExportsAndRequireCallWithCircularDependency() {
    NodeTraversal traversal = new NodeTraversal(new AbstractCompiler(new CompilerOptions(List.of("-js", "output=compiled.js", "-d", "."))));
    Node root = new Parser(new File("inputWithModuleExportsAndRequireCallWithCircularDependency.js").getAbsolutePath()).parseTree(parser);
    ProcessCommonJSModules pass = new ProcessCommonJSModules(traversal.getCompiler(), "");
    pass.process(traversal.getInput(), root);
    Node output = traversal.getOutput();
    try {
      Node script = output.getLastChild();
      script.getFirstChild().getLastChild().toString();
      fail("Expected exception");
    } catch (RuntimeException e) {
      // Expected
    }
  }

  @org.junit.Test
  public void testProcessScriptWithModuleExportsAndModuleImportWithCircularDependency() {
    NodeTraversal traversal = new NodeTraversal(new AbstractCompiler(new CompilerOptions(List.of("-js", "output=compiled.js", "-d", "."))));
    Node root = new Parser(new File("inputWithModuleExportsAndModuleImportWithCircularDependency.js").getAbsolutePath()).parseTree(parser);
    ProcessCommonJSModules pass = new ProcessCommonJSModules(traversal.getCompiler(), "");
    pass.process(traversal.getInput(), root);
    Node output = traversal.getOutput();
    try {
      Node script = output.getLastChild();
      script.getFirstChild().getLastChild().toString();
      fail("Expected exception");
    } catch (RuntimeException e) {
      // Expected
    }
  }
}