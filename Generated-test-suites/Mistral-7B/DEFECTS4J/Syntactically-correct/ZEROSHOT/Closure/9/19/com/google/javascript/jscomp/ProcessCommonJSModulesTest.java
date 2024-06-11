package com.google.javascript.jscomp.test;

import static org.junit.Assert.*;

import com.google.common.base.Joiner;
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Processor;
import com.google.javascript.jscomp.test.TestSourceFile;
import com.google.javascript.rhino.Node;

import java.io.File;
import java.io.IOException;
import java.util.Set;

/**
 * Test file for ProcessCommonJSModules class.
 */
public class ProcessCommonJSModulesTest {

  @org.junit.Test
  public void testProcessCommonJSModules() throws IOException {
    // Test input source files.
    String inputPath = "test_data/ProcessCommonJSModulesTest/";

    // Test ProcessCommonJSModules with default options.
    CompilerOptions options = new CompilerOptions();
    ProcessCommonJSModules processor = new ProcessCommonJSModules(
        new Compiler(options), "");

    Set<String> expectedModules = Sets.newHashSet(
        "a", "b", "c", "d");

    Processor processor1 = new Processor(options);
    processor1.addPass(processor);
    TestSourceFile sourceFile1 = new TestSourceFile(
        new File(inputPath + "input1.js"));
    Node ast1 = processor1.process(sourceFile1.getSourceCode(),
        sourceFile1.getSourceFileName());

    assertEquals(expectedModules, getModules(ast1));

    // Test ProcessCommonJSModules with reportDependencies=false.
    ProcessCommonJSModules processor2 = new ProcessCommonJSModules(
        new Compiler(options), "", false);
    Processor processor3 = new Processor(options);
    processor3.addPass(processor2);
    TestSourceFile sourceFile2 = new TestSourceFile(
        new File(inputPath + "input2.js"));
    Node ast2 = processor3.process(sourceFile2.getSourceCode(),
        sourceFile2.getSourceFileName());

    assertNull(getModule(ast2));

  }

  private JSModule getModule(Node ast) {
    for (Node n = ast; n != null; n = n.getParent()) {
      if (n.isBlock() && n.getChildCount() > 0 &&
          n.getChildAtIndex(0) instanceof JSModule) {
        return (JSModule) n.getChildAtIndex(0);
      }
    }
    return null;
  }

  private Set<String> getModules(Node ast) {
    Set<String> modules = Sets.newHashSet();
    for (Node n = ast; n != null; n = n.getParent()) {
      if (n.isBlock() && n.getChildCount() > 0 &&
          n.getChildAtIndex(0) instanceof JSModule) {
        JSModule module = (JSModule) n.getChildAtIndex(0);
        modules.add(module.getName());
      }
    }
    return modules;
  }
}