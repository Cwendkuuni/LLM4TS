package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.Test;
import org.mockito.ArgumentCaptor;

import com.google.common.collect.Lists;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.AbstractPostOrderCallback;
import com.google.javascript.jscomp.AbstractTraversalCallback;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Diagnostics;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.ParseTree;
import com.google.javascript.rhino.NodeOperations;
import com.google.javascript.rhino.Token;

public class CheckSideEffectsTest {

  @Test
  public void testCheckSideEffects() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CompilerOptions options = new CompilerOptions(null);
    CheckSideEffects checker = new CheckSideEffects(compiler, CheckLevel.WARNING, false);

    ParseTree tree = NodeOperations.parseString("function foo() {;\n" +
        "var s = \"this string is \";\n" +
        "var s2 = s + 'continued on the next line but you forgot the +';\n" +
        "x == foo();\n" +
        "foo();;\n" +
        "var a = [1, 2];\n" +
        "var b = a.map(function(x) { return x * 2; });\n" +
        "return b; }\n");

    CompilerInput input = new CompilerInput("test.js", tree, null);

    checker.process(input, tree.getRootNode());

    AbstractTraversalCallback callback = mock(AbstractTraversalCallback.class);
    ArgumentCaptor<Diagnostics> diagnosticsCaptor = ArgumentCaptor.forClass(Diagnostics.class);
    when(callback.getDiagnostics()).thenReturn(diagnosticsCaptor.capture());

    checker.stripProtection().process(input, tree.getRootNode());
    NodeTraversal.traverse(compiler, tree.getRootNode(), callback);

    List<String> messages = Lists.newArrayList();
    for (DiagnosticType type : Diagnostics.getWarnings(diagnosticsCaptor.getValue())) {
      messages.add(type.getDisplayName() + ": " + type.getMessage(null));
    }

    List<String> expectedMessages = Lists.newArrayList(
        "Suspicious code. x == foo();",
        "Suspicious code. foo();;"
    );

    assertEquals(expectedMessages, messages);

    List<Node> problemNodes = checker.problemNodes;
    assertEquals(2, problemNodes.size());
    assertEquals(tree.getRootNode().getChild(1), problemNodes.get(0));
    assertEquals(tree.getRootNode().getChild(3), problemNodes.get(1));

    // Check that the warning messages are suppressed for the map function.
    List<Node> mapFunction = Lists.newArrayList(
        tree.getRootNode().getChild(6),
        tree.getRootNode().getChild(7),
        tree.getRootNode().getChild(8),
        tree.getRootNode().getChild(9),
        tree.getRootNode().getChild(10)
    );
    CheckSideEffects mapChecker = new CheckSideEffects(compiler, CheckLevel.WARNING, false);
    mapChecker.process(input, mapFunction.get(0));
    assertEquals(0, mapChecker.problemNodes.size());
  }
}

class NodeUtilTest extends AbstractPostOrderCallback {
  // Add any helper methods you need here to test the methods in NodeUtil.
}

class CheckSideEffectsTest extends CheckSideEffects {
  // Override any methods you need to test in CheckSideEffects here.
}