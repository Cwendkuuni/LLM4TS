import static org.junit.Assert.*;
import static com.google.javascript.jscomp.RenameLabels.*;

import org.junit.Test;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.rhino.Token;

public class RenameLabelsTest {

  @Test
  public void testRenameLabels() {
    String input = "function foo() {"
        + "  a: { "
        + "    b: { "
        + "      break a;"
        + "    } "
        + "  } "
        + "  a: { "
        + "    c: break a;"
        + "  } "
        + "}";

    CompilerOptions options = new CompilerOptions();
    AbstractCompiler compiler = new AbstractCompiler(options);
    RenameLabels renameLabels = new RenameLabels(compiler);

    Node root = ParserTestUtil.parseAndGetRoot(input);
    renameLabels.process(null, root);

    NodeTraversal traverser = new NodeTraversal(root, compiler);
    ProcessLabels processLabels = new ProcessLabels();
    NodeTraversal.traverse(compiler, root, processLabels);

    // Check that all labels have been renamed.
    assertEquals("a", processLabels.names.get(0));
    assertEquals("b", processLabels.names.get(1));
    assertEquals("c", processLabels.names.get(2));

    // Check that all references to labels have been updated.
    int aLabelId = 0;
    int bLabelId = 1;
    int cLabelId = 2;

    Node aLabel = getNodeWithName(traverser, "a");
    assertEquals(Token.LABEL, aLabel.getType());
    assertEquals(aLabelId++, processLabels.namespaceStack.peek().renameMap.get(aLabel.getString()).id);

    Node bLabel = getNodeWithName(traverser, "b");
    assertEquals(Token.LABEL, bLabel.getType());
    assertEquals(bLabelId++, processLabels.namespaceStack.peek().renameMap.get(bLabel.getString()).id);

    Node cLabel = getNodeWithName(traverser, "c");
    assertEquals(Token.LABEL, cLabel.getType());
    assertEquals(bLabelId++, processLabels.namespaceStack.peek().renameMap.get(cLabel.getString()).id);

    Node breakA = getNodeWithName(traverser, "break a");
    assertEquals(Token.BREAK, breakA.getType());
    assertEquals(aLabelId, processLabels.namespaceStack.peek().renameMap.get(breakA.getString().substring(5)).id);

    Node continueA = getNodeWithName(traverser, "continue a");
    assertEquals(Token.CONTINUE, continueA.getType());
    assertEquals(aLabelId, processLabels.namespaceStack.peek().renameMap.get(continueA.getString().substring(9)).id);
  }

  private Node getNodeWithName(NodeTraversal traverser, String name) {
    Node current = traverser.getCurrentNode();
    while (current != null) {
      if (current.getType() == Token.LABEL && current.getString().equals(name)) {
        return current;
      }
      current = current.getPreviousSibling();
    }
    fail("Could not find node with name: " + name);
    return null;
  }

}