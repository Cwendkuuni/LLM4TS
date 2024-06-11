package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.collect.Sets;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Token;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.NodeOperations;

import org.junit.Test;

import java.util.Set;

public class VarCheckTest {

  private static final String TEST_CODE = "var foo = 1;\n" +
      "var bar = 1;\n" +
      "function qux(x) {\n" +
      "  var foo = x;\n" +
      "}";

  @Test
  public void testVarCheck() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    NodeFactory nodeFactory = mock(NodeFactory.class);
    NodeOperations nodeOperations = mock(NodeOperations.class);
    CompilerInput input = mock(CompilerInput.class);
    CompilerPass varCheck = new VarCheck(compiler);

    when(compiler.newExternInput("{SyntheticVarsDeclar}")).thenReturn(input);
    when(compiler.newCompilerInputFromCode(TEST_CODE)).thenReturn(input);
    when(compiler.newCompilerPass()).thenReturn(varCheck);
    when(compiler.getModuleGraph()).thenReturn(new JSModuleGraph() {
      @Override
      public boolean dependsOn(JSModule module1, JSModule module2) {
        return false;
      }
    });

    Node root = Node.parse(TEST_CODE);
    NodeTraversal nodeTraversal = new NodeTraversal(compiler, root, nodeFactory, nodeOperations);
    nodeTraversal.traverse(null, root, null);

    Set<String> expectedVars = Sets.newHashSet("foo", "bar");
    Set<String> actualVars = Sets.newHashSet();
    Scope scope = nodeTraversal.getScope();
    for (Scope.Var var : scope.getAllVars()) {
      actualVars.add(var.name);
    }
    assertEquals(expectedVars, actualVars);
  }
}

class JSModuleGraph {
  // Empty implementation of JSModuleGraph for testing purpose.
}