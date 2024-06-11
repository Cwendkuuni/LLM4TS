import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerPassManager;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.rhino.NodeOperations;
import com.google.javascript.rhino.Token;

import java.util.ArrayList;
import java.util.List;

public class CheckSideEffectsTest {

  private AbstractCompiler compiler;
  private CheckSideEffects checkSideEffects;
  private CompilerPassManager passManager;
  private Node rootNode;

  @org.junit.Before
  public void setUp() {
    compiler = mock(AbstractCompiler.class);
    checkSideEffects = new CheckSideEffects(compiler, CheckLevel.WARNING, true);
    passManager = new CompilerPassManager(compiler);
    passManager.addPasses(checkSideEffects);
    rootNode = NodeOperations.parseJsCode("function foo() { return 42; } var s = 'this string is ' " +
        "continued on the next line but you forgot the +'; x = foo(); ;");
  }

  @org.junit.Test
  public void testCheckSideEffects() {
    CompilerInput input = new CompilerInput("test.js", rootNode);
    passManager.process(input, null);
    List<Node> problemNodes = checkSideEffects.problemNodes;
    assertEquals(2, problemNodes.size());
    Node firstProblemNode = problemNodes.get(0);
    assertEquals("s = 'this string is " +
        "continued on the next line but you forgot the +';",
        firstProblemNode.toSource());
    assertEquals(1, firstProblemNode.getChildCount());
    Node firstProblemNodeChild = firstProblemNode.getChild(0);
    assertEquals("this string is " +
        "continued on the next line but you forgot the +",
        firstProblemNodeChild.toSource());

    Node secondProblemNode = problemNodes.get(1);
    assertEquals("x = foo(); ;", secondProblemNode.toSource());
    assertEquals(3, secondProblemNode.getChildCount());
    assertEquals("foo()", secondProblemNode.getChild(0).toSource());
    assertEquals(";", secondProblemNode.getChild(2).toSource());
  }

  @org.junit.Test
  public void testSideEffectsProtection() {
    checkSideEffects.protectSideEffects();
    int protectionFunctionCount = 0;
    for (Node node : rootNode.getDescendantsAndSelf()) {
      if (node.isCall() && node.getFirstChild().isName() &&
          node.getFirstChild().getString().equals(CheckSideEffects.PROTECTOR_FN)) {
        protectionFunctionCount++;
      }
    }
    assertEquals(2, protectionFunctionCount);
  }

  @org.junit.Test
  public void testStripProtection() {
    checkSideEffects = new CheckSideEffects(compiler, CheckLevel.WARNING, false);
    passManager = new CompilerPassManager(compiler);
    passManager.addPasses(checkSideEffects);
    passManager.addPasses(new CheckSideEffects.StripProtection(compiler));
    CompilerInput input = new CompilerInput("test.js", rootNode);
    passManager.process(input, null);
    Node rootNodeAfterProtectionRemoval = input.getAstRoot(compiler);
    int protectionFunctionCount = 0;
    int checkSideEffectsCallCount = 0;
    for (Node node = rootNodeAfterProtectionRemoval; node != null; node = node.getParent()) {
      if (node.isCall() && node.getFirstChild().isName() &&
          node.getFirstChild().getString().equals(CheckSideEffects.PROTECTOR_FN)) {
        protectionFunctionCount++;
      }
      if (node.isCall() && node.getFirstChild().isName() &&
          node.getFirstChild().getString().equals(checkSideEffects.getClass().getName())) {
        checkSideEffectsCallCount++;
      }
      if (protectionFunctionCount > 0 && checkSideEffectsCallCount > 0) {
        break;
      }
    }
    assertEquals(0, protectionFunctionCount);
    assertEquals(1, checkSideEffectsCallCount);
  }
}