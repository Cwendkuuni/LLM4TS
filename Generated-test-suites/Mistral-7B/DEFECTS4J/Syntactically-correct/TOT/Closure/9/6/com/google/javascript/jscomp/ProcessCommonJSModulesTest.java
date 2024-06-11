import static org.junit.Assert.*;
import static com.google.javascript.jscomp.ProcessCommonJSModules.*;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.test.TestCompiler;
import com.google.javascript.rhino.NodeFactory;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Set;

public class ProcessCommonJSModulesTest {

  @org.junit.Test
  public void testProcessCommonJSModules() throws IOException, URISyntaxException {
    String input = "// Input file\n" +
        "var module1 = {\n" +
        "  exports: {\n" +
        "    foo: function() {\n" +
        "      return 'module1';\n" +
        "    }\n" +
        "  }\n" +
        "};\n" +
        "require('module2');\n" +
        "function module2() {\n" +
        "  return 'module2';\n" +
        "}\n" +
        "exports.bar = function() {\n" +
        "  return 'module2 bar';\n" +
        "};\n" +
        "goog.require('module1');\n" +
        "goog.provide('test');\n" +
        "test.baz = function() {\n" +
        "  return test.foo = module1.foo;\n" +
        "};\n";

    CompilerOptions options = CompilerOptions.defaultOptions();
    options.setOutputFile("output.js");
    options.setWarningLevel(Compiler.WarningLevel.ERROR);

    Compiler compiler = new TestCompiler();
    Node root = NodeFactory.createScript(input);

    ProcessCommonJSModules pass = new ProcessCommonJSModules(compiler, "");
    pass.process(null, root);

    NodeTraversal nt = NodeTraversal.forTree(root);
    Node output = nt.findFirstDescendantOfType(Node.SCRIPT);

    String expectedOutput = "// Output file\n" +
        "test = function() {\n" +
        "  this.foo = module1.exports.foo;\n" +
        "  this.baz = function() {\n" +
        "    return this.foo;\n" +
        "  };\n" +
        "};\n" +
        "goog.provide('test');\n" +
        "goog.require('module1');\n";

    String actualOutput = NodePrinter.printToNodeList(output)[0].getString();

    assertEquals(expectedOutput, actualOutput);

    Set<String> requires = compiler.getInput().getRequires();
    assertEquals(1, requires.size());
    assertEquals("module1", requires.iterator().next());

    Node requireNode = nt.findFirstDescendantOfType(Node.CALL)
        .findFirstDescendantOfType(Node.GETPROP)
        .findFirstDescendantOfType(Node.NAME)
        .getFirstChild();
    assertEquals("require", requireNode.getString());

    Node moduleNameNode = nt.findFirstDescendantOfType(Node.STRING)
        .findFirstDescendantOfType(Node.GETPROP)
        .findFirstDescendantOfType(Node.GETPROP)
        .findFirstDescendantOfType(Node.NAME);
    assertEquals("module2", moduleNameNode.getString());

    Node moduleRefNode = nt.findFirstDescendantOfType(Node.CALL)
        .findFirstDescendantOfType(Node.GETPROP)
        .findFirstDescendantOfType(Node.NAME);
    assertEquals("module2", moduleRefNode.getString());

    Node moduleExportsNode = nt.findFirstDescendantOfType(Node.GETPROP)
        .findFirstDescendantOfType(Node.GETPROP)
        .findFirstDescendantOfType(Node.NAME);
    assertEquals("exports", moduleExportsNode.getString());

    Node exportsNode = nt.findFirstDescendantOfType(Node.NAME)
        .findFirstDescendantOfType(Node.SETPROP)
        .getLastChild();
    assertEquals("module$exports", exportsNode.getString());

    Node fooCallNode = nt.findFirstDescendantOfType(Node.CALL)
        .findFirstDescendantOfType(Node.NAME)
        .findFirstDescendantOfType(Node.GETPROP)
        .findFirstDescendantOfType(Node.GETPROP)
        .findFirstDescendantOfType(Node.CALL);
    assertEquals("foo", fooCallNode.getFirstChild().getString());

    Node barCallNode = nt.findFirstDescendantOfType(Node.CALL)
        .findFirstDescendantOfType(Node.GETPROP)
        .findFirstDescendantOfType(Node.GETPROP)
        .findFirstDescendantOfType(Node.NAME);
    assertEquals("bar", barCallNode.getString());

    Node bazCallNode = nt.findFirstDescendantOfType(Node.CALL)
      .findFirstDescendantOfType(Node.GETPROP)
      .findFirstDescendantOfType(Node.GETPROP)
      .findFirstDescendantOfType(Node.NAME)
      .findFirstDescendantOfType(Node.CALL);
    assertEquals("baz", bazCallNode.getFirstChild().getString());
  }

  @org.junit.Test
  public void testProcessCommonJSModulesRelativePath() throws IOException, URISyntaxException {
    String input = "// Input file\n" +
        "require('./module2');\n" +
        "exports.foo = function() {\n" +
        "  return 'module1';\n" +
        "};\n";

    CompilerOptions options = CompilerOptions.defaultOptions();
    options.setOutputFile("output.js");
    options.setWarningLevel(Compiler.WarningLevel.ERROR);

    Compiler compiler = new TestCompiler();
    Node root = NodeFactory.createScript(input);

    ProcessCommonJSModules pass = new ProcessCommonJSModules(compiler, "");
    pass.process(null, root);

    NodeTraversal nt = NodeTraversal.forTree(root);
    Node output = nt.findFirstDescendantOfType(Node.SCRIPT);

    String expectedOutput = "// Output file\n" +
        "exports.foo = function() {\n" +
        "  return 'module1';\n" +
        "};\n" +
        "goog.provide('test');\n" +
        "goog.require('./module2');\n" +
        "test = function() {\n" +
        "  this.foo = module2.exports.foo;\n" +
        "};\n";

    String actualOutput = NodePrinter.printToNodeList(output)[0].getString();

    assertEquals(expectedOutput, actualOutput);

    Set<String> requires = compiler.getInput().getRequires();
    assertEquals(1, requires.size());
    assertEquals("./module2", requires.iterator().next());
  }

  @org.junit.Test
  public void testProcessCommonJSModulesInvalidPath() throws IOException, URISyntaxException {
    String input = "// Input file\n" +
        "require('module2.jsx');\n";

    CompilerOptions options = CompilerOptions.defaultOptions();
    options.setOutputFile("output.js");
    options.setWarningLevel(Compiler.WarningLevel.ERROR);

    Compiler compiler = new TestCompiler();
    Node root = NodeFactory.createScript(input);

    ProcessCommonJSModules pass = new ProcessCommonJSModules(compiler, "");

    try {
      pass.process(null, root);
      fail();
    } catch (Exception e) {
      String errorMessage = e.getMessage();
      assertTrue(errorMessage.contains("URI is not hierarchical"));
      assertTrue(errorMessage.contains("module2.jsx"));
    }
  }

  @org.junit.Test
  public void testProcessCommonJSModulesCircularDependency() throws IOException, URISyntaxException {
    String input = "// Input file\n" +
        "var module1 = {\n" +
        "  exports: {\n" +
        "    foo: function() {\n" +
        "      require('./module2');\n" +
        "      return 'module1';\n" +
        "    }\n" +
        "  }\n" +
        "};\n" +
        "var module2 = {\n" +
        "  exports: {\n" +
        "    bar: function() {\n" +
        "      require('./module1');\n" +
        "      return 'module2';\n" +
        "    }\n" +
        "  }\n" +
        "};\n" +
        "goog.provide('test');\n" +
        "test.baz = function() {\n" +
        "  return test.foo = module1.exports.foo;\n" +
        "};\n";

    CompilerOptions options = CompilerOptions.defaultOptions();
    options.setOutputFile("output.js");
    options.setWarningLevel(Compiler.WarningLevel.ERROR);

    Compiler compiler = new TestCompiler();
    Node root = NodeFactory.createScript(input);

    ProcessCommonJSModules pass = new ProcessCommonJSModules(compiler, "");

    try {
      pass.process(null, root);
      fail();
    } catch (Exception e) {
      String errorMessage = e.getMessage();
      assertTrue(errorMessage.contains("Circular dependency detected"));
    }
  }

  @org.junit.Test
  public void testProcessCommonJSModulesMissingModule() throws IOException, URISyntaxException {
    String input = "// Input file\n" +
        "require('nonexistentmodule');\n";

    CompilerOptions options = CompilerOptions.defaultOptions();
    options.setOutputFile("output.js");
    options.setWarningLevel(Compiler.WarningLevel.ERROR);

    Compiler compiler = new TestCompiler();
    Node root = NodeFactory.createScript(input);

    ProcessCommonJSModules pass = new ProcessCommonJSModules(compiler, "");

    try {
      pass.process(null, root);
      fail();
    } catch (Exception e) {
      String errorMessage = e.getMessage();
      assertTrue(errorMessage.contains("Module not found: nonexistentmodule"));
    }
  }

  @org.junit.Test
  public void testProcessCommonJSModulesMissingGoogRequire() throws IOException, URISyntaxException {
    String input = "// Input file\n" +
        "goog.provide('test');\n" +
        "test.baz = function() {\n" +
        "  return test.foo = require('module1').exports.foo;\n" +
        "};\n";

    CompilerOptions options = CompilerOptions.defaultOptions();
    options.setOutputFile("output.js");
    options.setWarningLevel(Compiler.WarningLevel.ERROR);

    Compiler compiler = new TestCompiler();
    Node root = NodeFactory.createScript(input);

    ProcessCommonJSModules pass = new ProcessCommonJSModules(compiler, "");

    try {
      pass.process(null, root);
      fail();
    } catch (Exception e) {
      String errorMessage = e.getMessage();
      assertTrue(errorMessage.contains("Could not find goog.require call"));
    }
  }

  @org.junit.Test
  public void testProcessCommonJSModulesMissingGoogProvide() throws IOException, URISyntaxException {
    String input = "// Input file\n" +
        "require('module1');\n" +
        "exports.baz = function() {\n" +
        "  return module1.exports.foo;\n" +
        "};\n";

    CompilerOptions options = CompilerOptions.defaultOptions();
    options.setOutputFile("output.js");
    options.setWarningLevel(Compiler.WarningLevel.ERROR);

    Compiler compiler = new TestCompiler();
    Node root = NodeFactory.createScript(input);

    ProcessCommonJSModules pass = new ProcessCommonJSModules(compiler, "");

    try {
      pass.process(null, root);
      fail();
    } catch (Exception e) {
      String errorMessage = e.getMessage();
      assertTrue(errorMessage.contains("Could not find goog.provide call"));
    }
  }

  @org.junit.Test
  public void testProcessCommonJSModulesMissingModuleExports() throws IOException, URISyntaxException {
    String input = "// Input file\n" +
        "goog.provide('test');\n" +
        "goog.require('module1');\n" +
        "test.baz = function() {\n" +
        "  return module1.exports;\n" +
        "};\n";

    CompilerOptions options = CompilerOptions.defaultOptions();
    options.setOutputFile("output.js");
    options.setWarningLevel(Compiler.WarningLevel.ERROR);

    Compiler compiler = new TestCompiler();
    Node root = NodeFactory.createScript(input);

    ProcessCommonJSModules pass = new ProcessCommonJSModules(compiler, "");

    try {
      pass.process(null, root);
      fail();
    } catch (Exception e) {
      String errorMessage = e.getMessage();
      assertTrue(errorMessage.contains("module1 has no exports object"));
    }
  }

  @org.junit.Test
  public void testProcessCommonJSModulesEmptyModule() throws IOException, URISyntaxException {
    String input = "// Input file\n" +
        "goog.provide('test');\n" +
        "goog.require('module1');\n" +
        "test.baz = function() {\n" +
        "};\n";

    CompilerOptions options = CompilerOptions.defaultOptions();
    options.setOutputFile("output.js");
    options.setWarningLevel(Compiler.WarningLevel.ERROR);

    Compiler compiler = new TestCompiler();
    Node root = NodeFactory.createScript(input);

    ProcessCommonJSModules pass = new ProcessCommonJSModules(compiler, "");

    try {
      pass.process(null, root);
    } catch (Exception e) {
      // Expected pass
    }

    NodeTraversal nt = NodeTraversal.forTree(root);
    Node output = nt.findFirstDescendantOfType(Node.SCRIPT);

    String expectedOutput = "// Output file\n" +
        "test = function() {\n" +
        "};\n" +
        "goog.provide('test');\n" +
        "goog.require('module1');\n";

    String actualOutput = NodePrinter.printToNodeList(output)[0].getString();

    assertEquals(expectedOutput, actualOutput);

    Set<String> requires = compiler.getInput().getRequires();
    assertEquals(1, requires.size());
    assertEquals("module1", requires.iterator().next());
  }
}