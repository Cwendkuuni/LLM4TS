package com.google.javascript.jscomp.test;

import static org.junit.Assert.*;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.RuntimeTypeCheck;
import com.google.javascript.rhino.NodeUtil;

import java.io.IOException;
import java.util.TreeSet;

import org.junit.Test;

public class RuntimeTypeCheckTest {

  @Test
  public void testRuntimeTypeCheck() throws IOException {
    CompilerInput input = new CompilerInput(
        ClassLoader.getSystemResource("test_data/runtimeTypeCheckTest.js")
            .getFile().getAbsolutePath(), 0, null);

    CompilerOptions options = new CompilerOptions();
    options.optimizationLevel = CompilerOptions.CompilationLevel.SIMPLE;

    AbstractCompiler compiler = new AbstractCompiler(options);
    compiler.addPasses(new CompilerPass[] { new RuntimeTypeCheck(compiler, null) });

    Node root = NodeUtil.parseJs(input.getCode());
    compiler.process(root, null);

    Node checkType = NodeUtil.newQualifiedNameNode("jscomp.typecheck.checkType", -1, -1);
    Node assertInstanceOf = NodeUtil.newQualifiedNameNode("assert", "assertInstanceOf", -1, -1);

    TreeSet<String> expectedInterfaces = new TreeSet<>();
    expectedInterfaces.add("FooInterface");
    expectedInterfaces.add("BarInterface");

    TreeSet<String> actualInterfaces = new TreeSet<>();

    NodeTraversal.traverse(compiler, root, new NodeTraversal.AbstractPostOrderCallback() {
      @Override
      public void visit(NodeTraversal t, Node n, Node parent) {
        if (n.getType() == Token.CALL && n.getFirstChild().getFirstChild()
            .isIdent("checkType")) {
          Node checkerList = n.getLastChild();
          Node interfaceCheckerNode = null;
          for (Node child = checkerList.getFirstChild(); child != null; child = child.getNext()) {
            if (child.isIdent("interfaceChecker")) {
              interfaceCheckerNode = child;
              break;
            }
          }
          if (interfaceCheckerNode != null) {
            actualInterfaces.add(interfaceCheckerNode.getLastChild().getFirstChild().getString());
          }
        }
      }
    });

    assertEquals("Expected interfaces not equal to actual interfaces", expectedInterfaces, actualInterfaces);

    Node functionWithReturnType = NodeUtil.getFunctionByName(root, "testFunctionWithReturnType");
    Node checkNode = NodeUtil.findFirstDescendantByType(functionWithReturnType, Node.TYPE_CALL, "checkType");
    Node checkerNode = checkNode.getLastChild();
    Node expectedCheckerNode = NodeUtil.newQualifiedNameNode("jscomp.typecheck.valueChecker", "String", -1, -1);
    Node actualCheckerNode = checkerNode.getFirstChild();
    assertEquals("Expected checker node to be String checker, but was " + actualCheckerNode.getString(), expectedCheckerNode, actualCheckerNode);
  }
}