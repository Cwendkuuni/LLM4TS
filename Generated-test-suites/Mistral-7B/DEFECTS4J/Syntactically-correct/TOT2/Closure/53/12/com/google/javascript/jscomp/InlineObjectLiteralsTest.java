import org.junit.Test;
import static org.junit.Assert.*;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.ReferenceCollectingCallback.Behavior;
import com.google.javascript.jscomp.ReferenceCollectingCallback.Reference;
import com.google.javascript.jscomp.ReferenceCollectingCallback.ReferenceCollection;
import com.google.javascript.jscomp.ReferenceCollectingCallback.ReferenceMap;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.Node;

public class InlineObjectLiteralsTest {

  @Test
  public void testInlineObjectLiterals() {
    // Given a simple Java text with object literals
    String input = "function main() {" +
        "  var obj1 = {foo: 1, bar: 2};" +
        "  var obj2 = {qux: obj1};" +
        "  var obj3 = {quux: obj2};" +
        "  main();" +
        "}";

    // When running the InlineObjectLiterals pass
    Compiler compiler = new Compiler();
    CompilerPass inlineObjectLiterals = new InlineObjectLiterals(compiler, () -> "");
    Node root = NodePrinter.print(NodePrinter.printToNode(input));
    inlineObjectLiterals.process(null, root);

    // Then the generated code should have the objects inlined into variables
    String expectedOutput = "function main() {" +
        "  var foo1 = 1;" +
        "  var bar1 = 2;" +
        "  var qux1_foo = 1;" +
        "  var qux1_bar = 2;" +
        "  var quux1_qux = {foo: 1, bar: 2};" +
        "  main();" +
        "}" +
        "function main() {}";

    // Verify the code change
    assertEquals(expectedOutput, NodePrinter.print(compiler.getIntermediateRepresentation()));
  }

  @Test
  public void testInlineObjectLiteralsWithDeepNesting() {
    // Given a Java text with deeply nested object literals
    String input = "function main() {" +
        "  var obj1 = {foo: 1, bar: {" +
        "        qux: 2," +
        "        quux: {" +
        "          quuz: 3" +
        "        }}" +
        "  };" +
        "  main();" +
        "}";

    // When running the InlineObjectLiterals pass
    Compiler compiler = new Compiler();
    CompilerPass inlineObjectLiterals = new InlineObjectLiterals(compiler, () -> "");
    Node root = NodePrinter.print(NodePrinter.printToNode(input));
    inlineObjectLiterals.process(null, root);

    // Then the generated code should have the objects inlined into variables
    String expectedOutput = "function main() {" +
        "  var foo1 = 1;" +
        "  var bar1_qux = 2;" +
        "  var bar1_quuz = 3;" +
        "  main();" +
        "}" +
        "function main() {}";

    // Verify the code change
    assertEquals(expectedOutput, NodePrinter.print(compiler.getIntermediateRepresentation()));
  }

  @Test
  public void testInlineObjectLiteralsWithSelfReferentialAssignment() {
    // Given a Java text with a self-referential object literal assignment
    String input = "function main() {" +
        "  var obj = {" +
        "    a: 1," +
        "    b: obj" +
        "  };" +
        "  main();" +
        "}";

    // When running the InlineObjectLiterals pass
    Compiler compiler = new Compiler();
    CompilerPass inlineObjectLiterals = new InlineObjectLiterals(compiler, () -> "");
    Node root = NodePrinter.print(NodePrinter.printToNode(input));
    inlineObjectLiterals.process(null, root);

    // Then the generated code should fail due to self-referential object literals not being supported
    String expectedErrorMessage = "Error: Self-referential object literal assignments are not supported.";

    // Verify the error message
    try {
      assertEquals(expectedOutput, NodePrinter.print(compiler.getIntermediateRepresentation()));
      fail("Expected error message but did not receive one.");
    } catch (AssertionError e) {
      String actualErrorMessage = e.getMessage();
      assertTrue(actualErrorMessage.contains(expectedErrorMessage));
    }
  }

  @Test
  public void testInlineObjectLiteralsWithNonObjectLiterals() {
    // Given a Java text with non-object literal assignment
    String input = "function main() {" +
        "  var obj = 1;" +
        "  main();" +
        "}";

    // When running the InlineObjectLiterals pass
    Compiler compiler = new Compiler();
    CompilerPass inlineObjectLiterals = new InlineObjectLiterals(compiler, () -> "");
    Node root = NodePrinter.print(NodePrinter.printToNode(input));
    inlineObjectLiterals.process(null, root);

    // Then the generated code should be identical
    String expectedOutput = "function main() {" +
        "  var obj = 1;" +
        "  main();" +
        "}";

    // Verify the code change
    assertEquals(expectedOutput, NodePrinter.print(compiler.getIntermediateRepresentation()));
  }
}