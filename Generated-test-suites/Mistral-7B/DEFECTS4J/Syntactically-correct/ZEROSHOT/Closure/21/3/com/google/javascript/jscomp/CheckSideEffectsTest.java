import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CheckLevel.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.AbstractPostOrderCallback;
import com.google.javascript.rhino.Token;
import com.google.common.collect.Lists;

@RunWith(JUnit4.class)
public class CheckSideEffectsTest {

  @Test
  public void testCheckSideEffects() {
    String sourceCode = "function foo() {\n" +
        "  var s = 'this string is \ncontinued on the next line but you forgot the +\n';\n" +
        "  x == foo();\n" +
        "  foo();;\n" +
        "}\n" +
        "function bar() {\n" +
        "  var x;\n" +
        "  foo();\n" +
        "}\n";

    CompilerOptions options = new CompilerOptions(null);
    options.setWarningLevel(WARNING, CheckLevel.YOUR_CODE);
    Compiler compiler = new Compiler();
    Node root = compiler.parseTree(sourceCode, null, null, options);

    AbstractPostOrderCallback checkSideEffects = new CheckSideEffects(
        compiler, WARNING, false);
    NodeTraversal.traverse(compiler, root, checkSideEffects);

    int warningCount = checkSideEffects.problemNodes.size();
    assertEquals("One warning expected.", 1, warningCount);

    Node warningNode = checkSideEffects.problemNodes.get(0);
    assertNotNull("Warning node should not be null.", warningNode);
    assertEquals("Expected warning node to be an expression.",
        Node.EXPRESSION, warningNode.getType());
  }

  @Test
  public void testProtectSideEffects() {
    String sourceCode = "function foo() {\n" +
        "  var s = 'this string is \ncontinued on the next line but you forgot the +\n';\n" +
        "  x == foo();\n" +
        "  foo();;\n" +
        "}\n";

    CompilerOptions options = new CompilerOptions(null);
    options.setWarningLevel(WARNING, CHECK_SIDE_EFFECTS);
    options.setWarningLevel(ERROR, CHECK_SIDE_EFFECTS);
    Compiler compiler = new Compiler();
    Node root = compiler.parseTree(sourceCode, null, null, options);

    CheckSideEffects checkSideEffects = new CheckSideEffects(
        compiler, CHECK_SIDE_EFFECTS, true);
    NodeTraversal.traverse(compiler, root, checkSideEffects);

    int warningCount = checkSideEffects.problemNodes.size();
    assertEquals("No warnings expected.", 0, warningCount);

    checkSideEffects.protectSideEffects();

    // Verify that the protected nodes were not removed.
    int protectedNodeCount = 0;
    Node currentNode = root;
    while (currentNode != null) {
      if (currentNode.isCall() && currentNode.getFirstChild().isName() &&
          currentNode.getFirstChild().getString().equals(
              CheckSideEffects.PROTECTOR_FN)) {
        protectedNodeCount++;
      }
      currentNode = currentNode.getNextSibling();
    }
    assertEquals("One protected node expected.", 1, protectedNodeCount);
  }

  @Test
  public void testStripProtection() {
    String sourceCode = "function foo() {\n" +
        "  var s = 'this string is \ncontinued on the next line but you forgot the +\n';\n" +
        "  x == foo();\n" +
        "  foo();;\n" +
        "  JSCOMPILER_PRESERVE(s);\n" +
        "}\n";

    CompilerOptions options = new CompilerOptions(null);
    options.setWarningLevel(ERROR, CHECK_SIDE_EFFECTS);
    Compiler compiler = new Compiler();
    Node root = compiler.parseTree(sourceCode, null, null, options);

    CheckSideEffects checkSideEffects = new CheckSideEffects(
        compiler, CHECK_SIDE_EFFECTS, false);
    NodeTraversal.traverse(compiler, root, checkSideEffects);

    int warningCount = checkSideEffects.problemNodes.size();
    assertEquals("One warning expected.", 1, warningCount);

    StripProtection stripProtection = new StripProtection(compiler);
    NodeTraversal.traverse(compiler, root, stripProtection);

    warningCount = checkSideEffects.problemNodes.size();
    assertEquals("No warnings expected.", 0, warningCount);

    // Verify that the protected node was removed.
    int protectedNodeCount = 0;
    Node currentNode = root;
    while (currentNode != null) {
      if (currentNode.isCall() && currentNode.getFirstChild().isName() &&
          currentNode.getFirstChild().getString().equals(
              CheckSideEffects.PROTECTOR_FN)) {
        protectedNodeCount++;
      }
      currentNode = currentNode.getNextSibling();
    }
    assertEquals("No protected nodes expected.", 0, protectedNodeCount);
  }
}