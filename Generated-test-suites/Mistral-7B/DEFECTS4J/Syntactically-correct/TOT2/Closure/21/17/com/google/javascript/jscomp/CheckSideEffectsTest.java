import static org.junit.Assert.assertTrue;
import static com.google.javascript.rhino.NodeFactory.createNode;
import static com.google.javascript.rhino.NodeFactory.createToken;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.CheckSideEffects;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import org.junit.Test;

public class CheckSideEffectsTest {

  @Test
  public void testCheckSideEffects() {
    // Testing the functionality of the class
    CompilerInput input = new CompilerInput(
        "test",
        "function main() {\n" +
        "  var a = 5;\n" +
        "  var b = 10;\n" +
        "  var c = a + b;\n" +
        "  var d = a + b;\n" +
        "  return c;\n" +
        "}"
    );
    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler(options);
    CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, CheckLevel.WARNING, false);
    checkSideEffects.process(input, null);
    Node root = input.getAstRoot(compiler);
    NodeTraversal.traverse(checkSideEffects, root, null);
    assertTrue(checkSideEffects.problemNodes.isEmpty());

    // Testing for edge cases
    input = new CompilerInput(
        "test",
        "function main() {\n" +
        "  var a = 5;\n" +
        "  var b = 10;\n" +
        "  var c = a + b;\n" +
        "  return c;\n" +
        "  var x = 1;\n" + // An extra statement
        "}"
    );
    checkSideEffects = new CheckSideEffects(compiler, CheckLevel.WARNING, false);
    checkSideEffects.process(input, null);
    root = input.getAstRoot(compiler);
    NodeTraversal.traverse(checkSideEffects, root, null);
    assertTrue(checkSideEffects.problemNodes.isEmpty());

    input = new CompilerInput(
        "test",
        "function main() {\n" +
        "  return null;\n" +
        "}"
    );
    checkSideEffects = new CheckSideEffects(compiler, CheckLevel.WARNING, false);
    checkSideEffects.process(input, null);
    root = input.getAstRoot(compiler);
    NodeTraversal.traverse(checkSideEffects, root, null);
    assertTrue(checkSideEffects.problemNodes.isEmpty());

    // Testing for exception handling scenarios
    try {
      input = new CompilerInput(
          "test",
          "function main() {\n" +
          "  throw new Error(\"Test Error\");\n" +
          "}"
      );
      checkSideEffects = new CheckSideEffects(compiler, CheckLevel.WARNING, false);
      checkSideEffects.process(input, null);
      root = input.getAstRoot(compiler);
      NodeTraversal.traverse(checkSideEffects, root, null);
      assertTrue(checkSideEffects.problemNodes.isEmpty());
    } catch (Exception e) {
      // Expected exception
    }
  }
}