import static com.google.javascript.jscomp.RenameLabels.DefaultNameSupplier.NameGenerator.NameGeneratorTest.assertNamesDiffer;
import static com.google.javascript.rhino.NodeTreeMatcher.isNode;
import static org.junit.Assert.assertEquals;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.RenameLabels;
import com.google.javascript.rhino.Node;
import org.junit.Test;

public class RenameLabelsTest {

  @Test
  public void testRenameLabels() throws Exception {
    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions(null);
    RenameLabels renameLabels = new RenameLabels(compiler);

    String jsSource =
        "function bar() {"
            + "  a: {"
            + "    b: {"
            + "      foo();"
            + "    }"
            + "  }"
            + "  a: {"
            + "    b: break a;"
            + "  }"
            + "}";

    Node externs = NodeUtil.compile(jsSource);
    Node root = NodeUtil.getRootNode(externs);

    renameLabels.process(externs, root);
    NodeTraversal traversal = NodeTraversal.create(compiler, root);

    // Check that labels have been renamed.
    assertEquals("a", traversal.findFirstDescendant(isNode(Token.LABEL), "a").getFirstChild().getString());
    assertEquals("b", traversal.findFirstDescendant(isNode(Token.LABEL), "b").getFirstChild().getString());

    // Check that labels references have been updated.
    Node breakNode = traversal.findFirstDescendant(isNode(Token.BREAK), null);
    Node ref = breakNode.getFirstChild();
    assertEquals("b", ref.getString());

    // Check that unused labels have been removed.
    Node unusedLabel = traversal.findFirstDescendant(isNode(Token.LABEL), "c");
    assertEquals(null, unusedLabel);

    // Check that names are different for each depth.
    NodeTraversal.traverse(compiler, root, new DefaultNameSupplier.NameGenerator.NameGeneratorTest.NameGeneratorTest() {
      @Override
      public void testNameGenerator_namesDiffer() {
        assertNamesDiffer(traversal, "a", "b");
        assertNamesDiffer(traversal, "b", "c");
      }
    });
  }
}