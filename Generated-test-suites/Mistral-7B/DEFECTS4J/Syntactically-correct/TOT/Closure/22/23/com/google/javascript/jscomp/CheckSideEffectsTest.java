import static com.google.javascript.jscomp.CheckLevel.WARNING;
import static com.google.javascript.jscomp.CheckSideEffects.*;
import static org.junit.Assert.*;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.ParserOptions;
import com.google.javascript.rhino.JSDocInfo;

import org.junit.Test;

public class CheckSideEffectsTest {

  @Test
  public void testCheckSideEffects() {
    String input = "function foo() { var x = 1; }";
    CompilerOptions options = CompilerOptions.defaultOptions();
    Compiler compiler = new Compiler();
    AbstractCompiler abstractCompiler = new Compiler() {
      @Override
      public Node parseTree(Node externs, Node root) {
        return NodeTraversal.traverse(this, root, new StripProtection(this));
      }
    };
    compiler.setOption(CompilerOptions.LanguageMode, ParserOptions.Mode.ECMASCRIPT5);
    compiler.setOption(CompilerOptions.PrintStackFrames, false);
    compiler.setOption(CompilerOptions.WarningLevel, WARNING);
    Node root = compiler.parseTree(externs, input);
    NodeTraversal.traverse(abstractCompiler, root, new CheckSideEffects(abstractCompiler, WARNING, false));
    assertEquals(0, problemNodes.size());

    input = "function foo() { var x = 1; x = 2; }";
    root = compiler.parseTree(externs, input);
    NodeTraversal.traverse(abstractCompiler, root, new CheckSideEffects(abstractCompiler, WARNING, false));
    assertEquals(1, problemNodes.size());
    Node problemNode = problemNodes.get(0);
    JSDocInfo jsdocInfo = problemNode.getJSDocInfo();
    assertNotNull(jsdocInfo);
    String expectedMessage = "Suspicious code. This code lacks side-effects. Is there a bug?";
    String actualMessage = jsdocInfo.getMessage(USELESS_CODE_ERROR);
    assertEquals(expectedMessage, actualMessage);

    input = "function foo() { var x = 1 + '1'; }";
    root = compiler.parseTree(externs, input);
    NodeTraversal.traverse(abstractCompiler, root, new CheckSideEffects(abstractCompiler, WARNING, false));
    assertEquals(0, problemNodes.size());

    input = "function foo() { var x = 1, y = 2; }";
    root = compiler.parseTree(externs, input);
    NodeTraversal.traverse(abstractCompiler, root, new CheckSideEffects(abstractCompiler, WARNING, false));
    assertEquals(0, problemNodes.size());

    input = "function foo() { var x = 1; x + 1; }";
    root = compiler.parseTree(externs, input);
    NodeTraversal.traverse(abstractCompiler, root, new CheckSideEffects(abstractCompiler, WARNING, false));
    assertEquals(0, problemNodes.size());

    input = "function foo() { var x = 1; return x + 1; }";
    root = compiler.parseTree(externs, input);
    NodeTraversal.traverse(abstractCompiler, root, new CheckSideEffects(abstractCompiler, WARNING, false));
    assertEquals(0, problemNodes.size());

    input = "function foo() { var x = 1; x++ }";
    root = compiler.parseTree(externs, input);
    NodeTraversal.traverse(abstractCompiler, root, new CheckSideEffects(abstractCompiler, WARNING, false));
    assertEquals(1, problemNodes.size());
    problemNode = problemNodes.get(0);
    expectedMessage = "Suspicious code. The ++ operator has a side-effect.";
    actualMessage = jsdocInfo.getMessage(USELESS_CODE_ERROR);
    assert FalseEquality(expectedMessage, actualMessage);
  }

  private static boolean FalseEquality(String expected, String actual) {
    return !expected.equals(actual);
  }
}