import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.RenameLabels;
import com.google.javascript.rhino.Node;

public class RenameLabelsTest {

  @Test
  public void testRenameLabels() {
    String sourceCode = "function foo() {" +
        "  a: { " +
        "    b: { " +
        "      x = 1; " +
        "    }" +
        "  }" +
        "  a: { " +
        "    break a; " +
        "  }" +
        "}";

    Compiler compiler = new Compiler();
    RenameLabels renameLabels = new RenameLabels(compiler);
    Node root = compiler.parse(sourceCode, null, null);
    renameLabels.process(null, root);
    NodeTraversal nodeTraversal = new NodeTraversal(root);
    NodeTraversal.traverse(compiler, root, new ProcessLabels() {
      @Override
      public void visitLabel(Node node, Node parent) {
        System.out.println("Label renamed: " + node.getString() + " => " + getNameForId(node.getFirstChild().getInt32()));
      }
    });

    String output = compiler.getSource(root);
    String expectedOutput = "function foo() {" +
        "  a: { " +
        "    b: { " +
        "      x = 1; " +
        "    }" +
        "  }" +
        "  c: { " +
        "    break c; " +
        "  }" +
        "}";
    assertEquals(expectedOutput, output);
  }

  private int getNameForId(Node node) {
    Preconditions.checkState(node.getType() == Token.LABEL);
    int labelId = Integer.parseInt(node.getFirstChild().getString());
    return labelId;
  }
}