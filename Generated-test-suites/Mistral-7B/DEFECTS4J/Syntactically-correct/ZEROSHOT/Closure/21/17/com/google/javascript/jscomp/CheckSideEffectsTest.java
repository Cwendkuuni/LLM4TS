import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerResult;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.Token;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.JSDocInfoBuilder;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.ScriptContext;
import com.google.javascript.rhino.Types;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CheckSideEffectsTest {

  private static Node createTestNode(String nodeType, String value) {
    NodeFactory nodeFactory = new NodeFactory();
    return nodeFactory.createExpression(
        nodeType, null, null, null, null, null, Types.undefinedType(), null,
        nodeFactory.createString(value), null);
  }

  private static Node createCallNode(String methodName, Node... args) {
    NodeFactory nodeFactory = new NodeFactory();
    Node callNode = nodeFactory.createCall(null, null, null, null);
    Node nameNode = nodeFactory.createName(methodName);
    callNode.addChildToBack(nameNode);
    for (Node argNode : args) {
      callNode.addChildToBack(argNode);
    }
    return callNode;
  }

  private AbstractCompiler createCompiler() {
    return mock(AbstractCompiler.class);
  }

  private CheckSideEffects createCheckSideEffects(AbstractCompiler compiler, CheckLevel level, boolean protectSideEffectFreeCode) {
    return new CheckSideEffects(compiler, level, protectSideEffectFreeCode);
  }

  @org.junit.Test
  public void testCheckSideEffects() {
    AbstractCompiler compiler = createCompiler();
    CheckSideEffects checkSideEffects = createCheckSideEffects(compiler, CheckLevel.WARNING, false);

    CompilerInput compilerInput = new CompilerInput();
    Node root = new Node(Token.BLOCK, null, null, null, null);
    root.addChildToBack(createTestNode(Token.NUMBER, "1"));
    root.addChildToBack(createTestNode(Token.VOID, ""));
    root.addChildToBack(createTestNode(Token.NUMBER, "2"));

    NodeTraversal.traverse(compiler, root, checkSideEffects);
    assertEquals(0, checkSideEffects.problemNodes.size());

    root.replaceChild(1, createCallNode("console.log", createTestNode(Token.NUMBER, "1")));
    NodeTraversal.traverse(compiler, root, checkSideEffects);
    assertEquals(1, checkSideEffects.problemNodes.size());
    assertEquals(root.getFirstChild().getFirstChild(), checkSideEffects.problemNodes.get(0));

    // Test for warning message
    checkSideEffects.level = CheckLevel.ERROR;
    NodeTraversal.traverse(compiler, root, checkSideEffects);
    assertEquals(1, checkSideEffects.problemNodes.size());
    assertEquals(root.getFirstChild().getFirstChild(), checkSideEffects.problemNodes.get(0));
    assertEquals("Suspicious code. This code lacks side-effects. Is there a bug?",
        checkSideEffects.problemNodes.get(0).getFirstChild().getJSDocInfo().getSummary());

    // Test for protecting side-effect free codes
    checkSideEffects = createCheckSideEffects(compiler, CheckLevel.WARNING, true);
    NodeTraversal.traverse(compiler, root, checkSideEffects);
    assertEquals(0, checkSideEffects.problemNodes.size());

    // Test for removing side-effect sync functions
    CheckSideEffects.StripProtection stripProtection = new CheckSideEffects.StripProtection(compiler);
    CompilerPass pass = stripProtection;
    CompilerResult compilerResult = new CompilerResult();
    compilerResult = compiler.process(compilerInput, root, pass, null, null);
    Node resultRoot = compilerResult.getCompiledAst();

    Node sideEffectCallNode = checkSideEffects.problemNodes.get(0);
    Node sideEffectCallResultRoot = resultRoot;
    while (sideEffectCallResultRoot != null) {
      if (sideEffectCallResultRoot.getFirstChild() == sideEffectCallNode) {
        break;
      }
      sideEffectCallResultRoot = sideEffectCallResultRoot.getParent();
    }
    assertEquals(null, sideEffectCallResultRoot);
  }
}