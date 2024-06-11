import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CheckSideEffects.*;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.rhino.Node;

import org.junit.Test;

public class CheckSideEffectsTest {

  @Test
  public void testCheckSideEffects() {
    String sourceCode = "function main() {"
        + "  // This is a comment, no side effects."
        + "  // Side effect example 1:"
        + "  var s = \"this string is \";"
        + "  var s2 = \"continued on the next line but you forgot the +\";"
        + "  var x = 5;"
        + "  x == foo();" // Should that be '='?
        + "  foo();;" // Probably just a stray-semicolon. Doesn't hurt to check though
        + "  // Side effect example 2:"
        + "  var el = document.getElementById(\"id\");"
        + "  el.offsetWidth;"
        + "  // Side effect example 3:"
        + "  for (var i = 0; i < 10; i++) {"
        + "    if (i % 2 === 0) {"
        + "      el.style.backgroundColor = \"red\";"
        + "    }"
        + "  }"
        + "}";

    Compiler compiler = new Compiler();
    CompilerPass checkSideEffectsPass = new CheckSideEffects(compiler, CheckLevel.WARNING, true);

    Node root = compiler.parseTree(sourceCode);
    checkSideEffectsPass.process(null, root);

    Node errorNode = root.getFirstChild();
    assertEquals(EXPECTED_NUM_WARNINGS, errorNode.getChildCount());

    for (int i = 0; i < EXPECTED_NUM_WARNINGS; i++) {
      Node warning = errorNode.getFirstChild();
      assertEquals("Suspicious code.", warning.getString());
      assertEquals("JSC_USELESS_CODE", warning.getType().getName());
    }
  }

  @Test
  public void testNoWarningForSideEffectFreeCode() {
    String sourceCode = "function main() {"
        + "  // This is a comment, no side effects."
        + "  // Side effect free example 1:"
        + "  var x = 5;"
        + "  var y = 10;"
        + "  x = y;"
        + "  // Side effect free example 2:"
        + "  var x = 5;"
        + "  var y = \"hello\";"
        + "  x = y.length;"
        + "  // Side effect free example 3:"
        + "  var x = 5;"
        + "  var y = 10;"
        + "  x = y + 5;"
        + "}";

    Compiler compiler = new Compiler();
    CompilerPass checkSideEffectsPass = new CheckSideEffects(compiler, CheckLevel.WARNING, true);

    Node root = compiler.parseTree(sourceCode);
    checkSideEffectsPass.process(null, root);

    Node errorNode = root.getFirstChild();
    assertNull(errorNode);
  }

  @Test
  public void testNoWarningForDeclarationWithoutAssignment() {
    String sourceCode = "function main() {"
        + "  var x;"
        + "}";

    Compiler compiler = new Compiler();
    CompilerPass checkSideEffectsPass = new CheckSideEffects(compiler, CheckLevel.WARNING, true);

    Node root = compiler.parseTree(sourceCode);
    checkSideEffectsPass.process(null, root);

    Node errorNode = root.getFirstChild();
    assertNull(errorNode);
  }

  @Test(expected = AssertionError.class)
  public void testCheckSideEffectsWithErrors() {
    String sourceCode = "function main() {"
        + "  var s = \"this string is \";"
        + "  var s2 = \"continued on the next line but you forgot the +\";"
        + "  x == foo();" // Should that be '='?
        + "  foo();;" // Probably just a stray-semicolon. Doesn't hurt to check though
        + "  for (var i = 0; i < 10; i++) {"
        + "    if (i % 2 === 0) {"
        + "      el.style.backgroundColor = \"red\";" // el is not defined
        + "    }"
        + "  }"
        + "}";

    Compiler compiler = new Compiler();
    CompilerPass checkSideEffectsPass = new CheckSideEffects(compiler, CheckLevel.WARNING, true);

    Node root = compiler.parseTree(sourceCode);
    checkSideEffectsPass.process(null, root);
  }

  private static final int EXPECTED_NUM_WARNINGS = 3;
}