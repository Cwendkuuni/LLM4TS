package com.google.javascript.jscomp.test;

import com.google.common.collect.Sets;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.NodeFactory;

public class ProcessCommonJSModulesTest {

  private Compiler compiler;
  private CompilerOptions options;
  private NodeFactory nodeFactory;

  @Before
  public void setUp() throws Exception {
    options = new CompilerOptions();
    options.setOutputFile("out.js");
    compiler = new Compiler(options);
    nodeFactory = options.getFactory();
  }

  @Test
  public void testProcessCommonJSModules() throws Exception {
    String sourceCode = "var a = require('b');";
    Node rootNode = nodeFactory.parse(sourceCode);

    CompilerPass processor = new ProcessCommonJSModules(
        compiler, ".test/", false);
    processor.process(null, rootNode);

    NodeTraversal traversal = new NodeTraversal(compiler, rootNode, null);
    Node moduleRef = null;
    for (Node child = rootNode.getFirstChild(); child != null;
        child = child.getNextSibling()) {
      if (child.isCall() && child.getChildCount() == 2 &&
          "require".equals(child.getFirstChild().getQualifiedName())) {
        moduleRef = child;
        break;
      }
    }
    Preconditions.checkNotNull(moduleRef);

    Node expectedModuleRef = nodeFactory.parse("goog.require('b')");
    Node actualModuleRef = traversal.getParent(moduleRef);
    assertEquals("Expected 'goog.require' call in place of 'require'",
        expectedModuleRef, actualModuleRef);
  }

  @Test
  public void testProcessCommonJSModulesWithMultipleRequireCalls()
      throws Exception {
    String sourceCode = "var a = require('b'), c = require('d');";
    Node rootNode = nodeFactory.parse(sourceCode);

    CompilerPass processor = new ProcessCommonJSModules(
        compiler, ".test/", false);
    processor.process(null, rootNode);

    NodeTraversal traversal = new NodeTraversal(compiler, rootNode, null);
    Node moduleRefA = null;
    Node moduleRefC = null;
    for (Node child = rootNode.getFirstChild(); child != null;
        child = child.getNextSibling()) {
      if (child.isCall() && child.getChildCount() == 2 &&
          "require".equals(child.getFirstChild().getQualifiedName())) {
        if (child.getFirstChild(1).getString().equals("b")) {
          moduleRefA = child;
        } else if (child.getFirstChild(1).getString().equals("d")) {
          moduleRefC = child;
        }
      }
    }
    Preconditions.checkNotNull(moduleRefA);
    Preconditions.checkNotNull(moduleRefC);

    Node expectedModuleRefA = nodeFactory.parse("goog.require('b')");
    Node actualModuleRefA = traversal.getParent(moduleRefA);
    assertEquals("Expected 'goog.require' call for module 'b'",
        expectedModuleRefA, actualModuleRefA);

    Node expectedModuleRefC = nodeFactory.parse("goog.require('d')");
    Node actualModuleRefC = traversal.getParent(moduleRefC);
    assertEquals("Expected 'goog.require' call for module 'd'",
        expectedModuleRefC, actualModuleRefC);
  }

  @Test
  public void testProcessCommonJSModulesWithScriptNode() throws Exception {
    String sourceCode = "var a = require('b');\n" +
        "function foo() {}\n";
    Node rootNode = nodeFactory.parse(sourceCode);

    CompilerPass processor = new ProcessCommonJSModules(
        compiler, ".test/", false);
    processor.process(null, rootNode);

    NodeTraversal traversal = new NodeTraversal(compiler, rootNode, null);
    Node moduleRef = null;
    for (Node child = rootNode.getFirstChild(); child != null;
        child = child.getNextSibling()) {
      if (child.isCall() && child.getChildCount() == 2 &&
          "require".equals(child.getFirstChild().getQualifiedName())) {
        moduleRef = child;
        break;
      }
    }
    Preconditions.checkNotNull(moduleRef);

    Node expectedModuleRef = nodeFactory.parse("goog.require('b')");
    Node actualModuleRef = traversal.getParent(moduleRef);
    assertEquals("Expected 'goog.require' call in place of 'require'",
        expectedModuleRef, actualModuleRef);

    Node scriptNode = null;
    for (Node sibling = rootNode.getFirstChild(); sibling != null;
        sibling = sibling.getNextSibling()) {
      if (sibling.isScript()) {
        scriptNode = sibling;
        break;
      }
    }
    Preconditions.checkNotNull(scriptNode);

    Node expectedScriptNode = scriptNode.cloneTree();
    Node actualScriptNode = traversal.getParent(scriptNode);
    assertEquals("Expected script node to be unchanged",
        expectedScriptNode, actualScriptNode);
  }

  @Test
  public void testProcessCommonJSModulesWithModuleExports()
      throws Exception {
    String sourceCode = "module.exports = function foo() {};";
    Node rootNode = nodeFactory.parse(sourceCode);

    CompilerPass processor = new ProcessCommonJSModules(
        compiler, ".test/", false);
    processor.process(null, rootNode);

    Node expectedModuleExports = nodeFactory.parse(
        "goog.exports = function foo() {");
    Node actualModuleExports = rootNode;
    Node module = null;
    for (Node child = rootNode.getFirstChild(); child != null;
        child = child.getNextSibling()) {
      if (child.isCall() && child.getChildCount() == 1 &&
          "module.exports".equals(child.getQualifiedName())) {
        module = child;
        break;
      }
    }
    Preconditions.checkNotNull(module);

    assertEquals("Expected 'goog.exports' assignment",
        expectedModuleExports, module.getChildAtIndex(0));

    NodeTraversal traversal = new NodeTraversal(compiler, rootNode, null);
    Node actualModuleRef = traversal.getParent(module);
    String moduleName = actualModuleRef.getString();
    String expectedModuleName = toModuleName("test/" + moduleName.substring(1));
    assertEquals("Expected 'goog.provide' call for module",
        expectedModuleName, moduleName);
  }

  @Test
  public void testProcessCommonJSModulesWithMultipleModuleExports()
      throws Exception {
    String sourceCode = "module1.exports = function foo1() {};\n" +
        "module2.exports = function foo2() {};";
    Node rootNode = nodeFactory.parse(sourceCode);

    CompilerPass processor = new ProcessCommonJSModules(
        compiler, ".test/", false);
    processor.process(null, rootNode);

    Node expectedModuleExports1 = nodeFactory.parse(
        "goog.exports['module1'] = function foo1() {");
    Node actualModuleExports1 = rootNode;
    Node module1 = null;
    for (Node child = rootNode.getFirstChild(); child != null;
        child = child.getNextSibling()) {
      if (child.isCall() && child.getChildCount() == 1 &&
          "module1.exports".equals(child.getQualifiedName())) {
        module1 = child;
        break;
      }
    }
    Preconditions.checkNotNull(module1);

    assertEquals("Expected 'goog.exports' assignment for module1",
        expectedModuleExports1, module1.getChildAtIndex(0));

    Node expectedModuleExports2 = nodeFactory.parse(
        "goog.exports['module2'] = function foo2() {");
    Node actualModuleExports2 = rootNode;
    Node module2 = null;
    for (Node sibling = rootNode.getFirstChild(); sibling != null;
        sibling = sibling.getNextSibling()) {
      if (sibling.isCall() && sibling.getChildCount() == 1 &&
          "module2.exports".equals(sibling.getQualifiedName())) {
        module2 = sibling;
        break;
      }
    }
    Preconditions.checkNotNull(module2);

    assertEquals("Expected 'goog.exports' assignment for module2",
        expectedModuleExports2, module2.getChildAtIndex(0));

    NodeTraversal traversal = new NodeTraversal(compiler, rootNode, null);
    String moduleName1 = traversal.getParent(module1).getString();
    String expectedModuleName1 = toModuleName("test/" + moduleName1.substring(1));
    assertEquals("Expected 'goog.provide' call for module1",
        expectedModuleName1, moduleName1);

    String moduleName2 = traversal.getParent(module2).getString();
    String expectedModuleName2 = toModuleName("test/" + moduleName2.substring(1));
    assertEquals("Expected 'goog.provide' call for module2",
        expectedModuleName2, moduleName2);
  }

  @Test
  public void testProcessCommonJSModulesWithRelativeRequireCalls()
      throws Exception {
    String sourceCode = "var a = require('./b').foo();";
    Node rootNode = nodeFactory.parse(sourceCode);

    CompilerPass processor = new ProcessCommonJSModules(
        compiler, ".test/", false);
    processor.process(null, rootNode);

    Node expectedModuleRef = nodeFactory.parse("goog.require('./b')");
    Node actualModuleRef = rootNode;
    Node requireCall = null;
    for (Node child = rootNode.getFirstChild(); child != null;
        child = child.getNextSibling()) {
      if (child.isCall() && child.getChildCount() == 2 &&
          "require".equals(child.getFirstChild().getQualifiedName())) {
        requireCall = child;
        break;
      }
    }
    Preconditions.checkNotNull(requireCall);

    Node expectedModuleRequiresB = nodeFactory.parse("require('./b')");
    Node actualModuleRequiresB = requireCall.getChildAtIndex(1);
    assertEquals("Expected 'require' argument to be 'b'",
        expectedModuleRequiresB, actualModuleRequiresB);

    Node expectedModuleRefB = traversal.getParent(actualModuleRequiresB);
    String moduleNameB = expectedModuleRefB.getString();
    String expectedModuleNameB = toModuleName("test/" + moduleNameB.substring(1));
    assertEquals("Expected 'goog.provide' call for module 'b'",
        expectedModuleNameB, moduleNameB);
  }

  @Test
  public void testProcessCommonJSModulesWithRequiresFromDifferentFiles()
      throws Exception {
    String sourceCode1 = "var a = require('b').foo();";
    Node rootNode1 = nodeFactory.parse(sourceCode1);

    CompilerPass processor = new ProcessCommonJSModules(
        compiler, ".test/", false);
    processor.process(null, rootNode1);

    String sourceCode2 = "var c = require('a').bar();";
    Node rootNode2 = nodeFactory.parse(sourceCode2);

    processor.process(null, rootNode2);

    Node expectedModuleRefA = traversal.getParent(traversal.getParent(
        traversal.getParent(rootNode1)))
        .getChildAtIndex(0);
    Node actualModuleRefA = traversal.getParent(
        traversal.getParent(rootNode2));

    assertEquals("Expected 'goog.require' call for 'a'",
        expectedModuleRefA, actualModuleRefA);
  }

  private String toModuleName(String filename) {
    return ProcessCommonJSModules.toModuleName(filename,
        normalizeSourceName(rootNode.getSourceName()));
  }

  private String normalizeSourceName(String filename) {
    return ProcessCommonJSModules.normalizeSourceName(filename);
  }
}