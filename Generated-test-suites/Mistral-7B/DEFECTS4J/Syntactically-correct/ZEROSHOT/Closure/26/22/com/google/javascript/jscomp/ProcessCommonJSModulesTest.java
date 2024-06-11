package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerPassManager;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.RewriterFactory;
import com.google.javascript.jscomp.test.TestCompiler;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.NodeFactory;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Set;
import java.util.regex.Pattern;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class ProcessCommonJSModulesTest {

  private CompilerInput input;
  private CompilerPassManager passManager;
  private NodeFactory nodeFactory;
  private TestCompiler compiler;
  private ProcessCommonJSModules processor;

  @Before
  public void setUp() throws Exception {
    input = new CompilerInput(new File("testInput.js"), new URI("test://testInput.js"), null);
    nodeFactory = new NodeFactory();
    compiler = new TestCompiler(null, null, null, input, null);
    compiler.initPassManager();
    passManager = compiler.getPassManager();
    ProcessCommonJSModules processor = new ProcessCommonJSModules(compiler, "");
  }

  @Test
  public void testProcessCommonJSModulesSimple() throws Exception {
    Node root = createTestNode();
    processor.process(input.getExterns(), root);
    checkOutput("commonjs1.js");
  }

  @Test
  public void testProcessCommonJSModulesWithDependencies() throws Exception {
    Node root = createTestNodeWithDependencies();
    processor.process(input.getExterns(), root);
    checkOutput("commonjs2.js");
  }

  @Test
  public void testProcessCommonJSModulesWithModuleExports() throws Exception {
    Node root = createTestNodeWithModuleExports();
    processor.process(input.getExterns(), root);
    checkOutput("commonjs3.js");
  }

  @Test
  public void testProcessCommonJSModulesWithRequireCall() throws Exception {
    Node root = createTestNodeWithRequireCall();
    processor.process(input.getExterns(), root);
    checkOutput("commonjs4.js");
  }

  @Test
  public void testProcessCommonJSModulesWithModuleExportsAndRequireCall()
      throws Exception {
    Node root = createTestNodeWithModuleExportsAndRequireCall();
    processor.process(input.getExterns(), root);
    checkOutput("commonjs5.js");
  }

  private Node createTestNode() {
    Node script = nodeFactory.createScriptNode("(function() {" +
        "goog.require('module1');" +
        "goog.provide('moduleA');" +
        "})();");
    return script;
  }

  private Node createTestNodeWithDependencies() {
    Node script = nodeFactory.createScriptNode("(function() {" +
        "goog.require('module1');" +
        "goog.require('module2');" +
        "goog.provide('moduleA');" +
        "})();");
    return script;
  }

  private Node createTestNodeWithModuleExports() {
    Node moduleExports = nodeFactory.createExpressionStatementNode(
        IR.exprResult(IR.assign(IR.name("module.exports"), IR.name("foo"))));
    Node script = nodeFactory.createScriptNode("(function() {" +
        "goog.require('module1');" +
        "goog.provide('moduleA');" +
        "moduleA.foo = 42;" +
        "moduleA.exports = moduleA;" +
        "})();" +
        moduleExports.toString());
    return script;
  }

  private Node createTestNodeWithRequireCall() {
    Node requireCall = nodeFactory.createCallNode("require",
        nodeFactory.createStringNode("module1"));
    Node script = nodeFactory.createScriptNode("(function() {" +
        "goog.provide('moduleA');" +
        "moduleA.x = 42;" +
        "goog.require('module1');" +
        "moduleA.y = require('module1').x;" +
        "})();");
    script.setChild(0, nodeFactory.createBlockNode(
        nodeFactory.createStatementsNode(script.getChild(0), requireCall)));
    return script;
  }

  private Node createTestNodeWithModuleExportsAndRequireCall() {
    Node moduleExports = nodeFactory.createExpressionStatementNode(
        IR.exprResult(IR.assign(IR.name("module.exports"), IR.name("foo"))));
    Node requireCall = nodeFactory.createCallNode("require",
        nodeFactory.createStringNode("module1"));
    Node script = nodeFactory.createScriptNode("(function() {" +
        "goog.require('module1');" +
        "goog.provide('moduleA');" +
        "moduleA.foo = 42;" +
        "moduleA.exports = moduleA;" +
        "moduleA.y = require('module1').x;" +
        "})();" +
        moduleExports.toString() +
        requireCall.toString());
    script.setChild(0, nodeFactory.createBlockNode(
        nodeFactory.createStatementsNode(script.getChild(0), requireCall)));
    return script;
  }

  private void checkOutput(String outputFileName) throws Exception {
    Node expected = readExpectedOutput(outputFileName);
    assertCodeEqual(expected, processor.getModule().getJs());
  }

  private Node readExpectedOutput(String outputFileName) throws URISyntaxException {
    Class<?> clazz = this.getClass();
    URI uri = new URI(clazz.getResource("/" + outputFileName).toString());
    NodeFactory nodeFactory = new NodeFactory();
    return nodeFactory.parse(uri.getFile());
  }

  private void assertCodeEqual(Node expected, Node actual) {
    assertEquals("Unexpected output from ProcessCommonJSModules:\n" +
        "Expected:\n" + printNode(expected) + "\n" +
        "  Actual:\n" + printNode(actual), expected, actual);
  }

  private String printNode(Node node) {
    return node.toString();
  }

  private static class TestCompiler extends Compiler {

    public TestCompiler(RewriterFactory rewriterFactory,
        CompilerOptions options,
        CompilerPassManager passManager,
        CompilerInput input,
        File tempFile) {
      super(rewriterFactory, options, passManager, input, tempFile);
    }

    @Override
    protected void initPassManager() {
      super.initPassManager();
      passManager.addPass(processor);
    }
  }
}