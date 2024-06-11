package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.google.common.collect.Sets;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.JSErrorReporter;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.ProcessCommonJSModules;
import com.google.javascript.rhino.NodeOperations;
import com.google.javascript.rhino.SourceFile;

import java.io.IOException;
import java.util.Set;

public class ProcessCommonJSModulesTest {

  @org.junit.Test
  public void testProcessCommonJSModules() throws IOException {
    CompilerOptions options = new CompilerOptions();
    options.setEnableExperimentalOptions(true);
    Compiler compiler = new Compiler(new JSErrorReporter(), options);

    SourceFile sourceFile = new SourceFile("test.js", "test");
    Node rootNode = sourceFile.getAST();

    ProcessCommonJSModules processor = new ProcessCommonJSModules(compiler, "");
    processor.process(null, rootNode);

    Node output = NodeOperations.getFirstChild(rootNode);
    assertNotNull(output);
    JSModule module = processor.getModule();
    assertNotNull(module);
    assertEquals("test", module.getName());

    Set<String> expectedModules = Sets.newHashSet("moduleA", "moduleB");
    assertEquals(expectedModules, module.getDependedUponModules());
  }

  @org.junit.Test
  public void testProcessCommonJSModulesMultipleFiles() throws IOException {
    CompilerOptions options = new CompilerOptions();
    options.setEnableExperimentalOptions(true);
    Compiler compiler = new Compiler(new JSErrorReporter(), options);

    SourceFile sourceFileA = new SourceFile("moduleA.js", "moduleA");
    Node rootNodeA = sourceFileA.getAST();
    Node moduleARequire = NodeOperations.getFirstChild(rootNodeA);
    Node moduleARequiredModuleName = moduleARequire.getChildAtIndex(1);

    SourceFile sourceFileB = new SourceFile("moduleB.js", "moduleB");
    Node rootNodeB = sourceFileB.getAST();
    Node moduleBProvide = NodeOperations.getLastChild(rootNodeB);
    Node moduleBProvidedModuleName = moduleBProvide.getChildAtIndex(1);

    ProcessCommonJSModules processor = new ProcessCommonJSModules(compiler, "");

    // Process moduleA
    processor.process(null, rootNodeA);
    JSModule moduleA = processor.getModule();
    assertNotNull(moduleA);
    assertEquals("moduleA", moduleA.getName());
    assertEquals(Sets.newHashSet("moduleB"), moduleA.getDependedUponModules());

    // Process moduleB
    rootNodeB = NodeOperations.replaceChild(rootNodeB, moduleBProvide,
        IR.exprResult(IR.call(IR.getprop(IR.name("goog"), IR.string("require")),
            IR.string(moduleARequiredModuleName.getString()))));
    processor.process(null, rootNodeB);
    JSModule moduleB = processor.getModule();
    assertNotNull(moduleB);
    assertEquals("moduleB", moduleB.getName());
    assertEquals(Sets.newHashSet("moduleA"), moduleB.getDependedUponModules());
  }
}