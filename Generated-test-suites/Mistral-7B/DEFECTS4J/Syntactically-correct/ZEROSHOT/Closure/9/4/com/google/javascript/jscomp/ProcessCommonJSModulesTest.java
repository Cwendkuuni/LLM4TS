package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.is;

import org.junit.Test;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.JSError;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.ProcessCommonJSModules;
import com.google.javascript.jscomp.TestCompiler;
import com.google.javascript.rhino.NodeOperations;

public class ProcessCommonJSModulesTest {

  @Test
  public void testProcessCommonJSModules() throws Exception {
    CompilerOptions options = new CompilerOptions(null);
    options.setOption("warn_unused_require", false);
    options.setOption("checkTypes", false);

    Compiler compiler = TestCompiler.newProcessor(options, new String[] {
        "test.js"
    });

    ProcessCommonJSModules processor = new ProcessCommonJSModules(compiler, "");

    Node root = compiler.getSource().getProgram();
    NodeTraversal traversal = new NodeTraversal(compiler, root, processor);
    traversal.traverse();

    Node module = root.getFirstChild().getLastChild();
    Node exports = module.getLastChild();

    // Check that require statements are replaced with direct references.
    Node requireCall = module.getChildAtIndex(0);
    Node requireRef = requireCall.getChildAtIndex(1);
    assertThat(requireRef.getQualifiedName(), is("test"));

    // Check that module.exports is renamed to module.$exports.
    Node moduleExports = exports.getChildAtIndex(0);
    Node oldModuleExportsName = moduleExports.getFirstChild();
    assertThat(oldModuleExportsName.getString(), is("exports"));
    Node newModuleExportsName = moduleExports.getChildAtIndex(1);
    assertThat(newModuleExportsName.getString(), is("module$exports"));

    // Check that global variables are renamed to avoid conflicts.
    Node globalVariable = root.getFirstChild().getLastChild().getFirstChild();
    assertThat(globalVariable.getString(), is("test$$global"));

    // Check that no errors were thrown.
    assertThat(compiler.getDiagnostics(), is(empty()));
  }

  @Test(expected = JSError.class)
  public void testProcessCommonJSModulesWithError() throws Exception {
    CompilerOptions options = new CompilerOptions(null);
    options.setOption("warn_unused_require", false);
    options.setOption("checkTypes", false);

    Compiler compiler = TestCompiler.newProcessor(options, new String[] {
        "testWithError.js"
    });

    ProcessCommonJSModules processor = new ProcessCommonJSModules(compiler, "");

    Node root = compiler.getSource().getProgram();
    NodeTraversal traversal = new NodeTraversal(compiler, root, processor);
    traversal.traverse();

    // The testWithError.js file contains an error that should not be
    // ignored.
  }
}