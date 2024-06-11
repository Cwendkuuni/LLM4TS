import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.CheckSideEffects;
import com.google.javascript.rhino.Node;

public class CheckSideEffectsTest {

  @Test
  public void testCheckSideEffects() {
    CheckSideEffects checkSideEffects = new CheckSideEffects(null, CheckLevel.WARNING, false);
    Node root = createTestNode("x = 1; y = 2;");
    checkSideEffects.process(null, root);
    assertTrue(checkSideEffects.problemNodes.isEmpty());

    root = createTestNode("x = 1;\n y = 2;");
    checkSideEffects.process(null, root);
    assertTrue(checkSideEffects.problemNodes.isEmpty());

    root = createTestNode("x = 1;\n y = 2;\n"); // trailing semicolon
    checkSideEffects.process(null, root);
    assertTrue(checkSideEffects.problemNodes.isEmpty());

    root = createTestNode("x = foo();");
    checkSideEffects.process(null, root);
    assertFalse(checkSideEffects.problemNodes.isEmpty());

    root = createTestNode("x = 1;\n if (true) {}\n x = 2;");
    checkSideEffects.process(null, root);
    assertTrue(checkSideEffects.problemNodes.isEmpty());

    root = createTestNode("for (int i = 0; i < 10; i++) { x = i; }");
    checkSideEffects.process(null, root);
    assertTrue(checkSideEffects.problemNodes.isEmpty());

    root = createTestNode("eval(\"x = 1\")"); // protected by the CheckSideEffects.visit method
    checkSideEffects.process(null, root);
    assertTrue(checkSideEffects.problemNodes.isEmpty());
  }

  private Node createTestNode(String javaCode) {
    // Create a Node from the given Java code using a mock compiler.
    // This is a simplified implementation and may not cover all edge cases.
    ComplierMock compiler = new ComplierMock();
    Node node = compiler.parse(javaCode);
    return node;
  }
}