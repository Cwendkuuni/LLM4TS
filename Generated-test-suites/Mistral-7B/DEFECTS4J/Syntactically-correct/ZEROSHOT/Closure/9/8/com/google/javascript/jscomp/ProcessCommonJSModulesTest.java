package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.collect.Sets;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerPassManager;
import com.google.javascript.jscomp.CompilerPassTestHelper;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.ProcessCommonJSModules;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.NodeFactory;
import java.util.Set;

public class ProcessCommonJSModulesTest {

  @org.junit.Test
  public void testProcessCommonJSModules() throws Exception {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    ProcessCommonJSModules processor = new ProcessCommonJSModules(compiler, "");

    CompilerPassManager passManager = new CompilerPassManager();
    passManager.addPass(ProcessorName.COMMON_JS_MODULES, processor);

    CompilerPassTestHelper helper = new CompilerPassTestHelper();
    Node node = helper.createTestSource("test.js",
        "var a = require('./a.js');\n" +
        "var b = require('./b.js');\n" +
        "exports.c = function() {}\n" +
        "exports.d = a.e;\n");

    Set<String> requires = Sets.newHashSet();
    when(compiler.getInput()).thenReturn(new CompilerInput() {
      @Override
      public String getSourceName() {
        return "test.js";
      }

      @Override
      public Set<String> getRequireNames() {
        return requires;
      }
    });

    NodeTraversal traversal = NodeTraversal.createTraverser(compiler, node, null);
    passManager.process(traversal);

    Node processedNode = traversal.getOutput();
    Node expectedNode = NodeFactory.createScript(
        "var a_module$exports = require('./a.js');\n" +
        "var b_module$exports = require('./b.js');\n" +
        "goog.provide('test');\n" +
        "goog.require('a');\n" +
        "goog.require('b');\n" +
        "var test = function() {\n" +
        "  this.c = function() {}\n" +
        "  this.d = a_module$exports.e;\n" +
        "};\n" +
        "test.prototype.c = (function() {\n" +
        "  var exports__$exports = {};\n" +
        "  return function() {\n" +
        "  };\n" +
        "})();\n" +
        "goog.export(test, 'test');\n");

    assertEquals(expectedNode.toSource(), processedNode.toSource());
    assertEquals(Sets.newHashSet("a.js", "b.js"), requires);
  }
}