import static com.google.javascript.jscomp.RenameLabels.DefaultNameSupplier.NameGenerator.NameGeneratorTest.assertLabelRenamed;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerPassManager;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

import org.junit.Before;
import org.junit.Test;

public class RenameLabelsTest {
  private AbstractCompiler compiler;
  private CompilerPassManager passManager;
  private Node rootNode;

  @Before
  public void setUp() {
    compiler = mock(AbstractCompiler.class);
    passManager = new CompilerPassManager(compiler);
    rootNode = Node.objectLiteral();
  }

  @Test
  public void testRenameLabels() {
    String originalCode =
        "function foo() {\n"
            + "  a: {\n"
            + "    b: {\n"
            + "      bar();\n"
            + "    }\n"
            + "  }\n"
            + "  a: {\n"
            + "    b: break a;\n"
            + "  }\n"
            + "}\n"
            + "\n"
            + "function bar() {}";

    // Set up the nodes and options for the testing.
    Node externsNode = Node.objectLiteral();
    CompilerOptions options = new CompilerOptions(null);
    passManager.setOptions(options);

    // Create a new CompilerPass instance.
    RenameLabels renameLabels = new RenameLabels(passManager);

    // Traverse the node tree and perform the renaming.
    NodeTraversal nodeTraversal = new NodeTraversal(rootNode, externsNode, passManager);
    nodeTraversal.traverse(renameLabels);

    // Verify the renaming were done as expected.
    String expectedCode =
        "function foo() {\n"
            + "  " + RenameLabels.DefaultNameSupplier.NameGenerator.NAME_A + ": {\n"
            + "    " + RenameLabels.DefaultNameSupplier.NameGenerator.NAME_B + ": {\n"
            + "      bar();\n"
            + "    }\n"
            + "  }\n"
            + "  " + RenameLabels.DefaultNameSupplier.NameGenerator.NAME_A + ": {\n"
            + "    " + RenameLabels.DefaultNameSupplier.NameGenerator.NAME_B + ": break "
            + RenameLabels.DefaultNameSupplier.NameGenerator.NAME_A + ";\n"
            + "  }\n"
            + "}\n"
            + "\n"
            + "function bar() {}";
    Node expectedRootNode = NodeUtil.parseJSCode(expectedCode);
    assertLabelRenamed(compiler, rootNode, expectedRootNode, RenameLabels.DefaultNameSupplier.NAME_A);
    assertLabelRenamed(compiler, rootNode, expectedRootNode, RenameLabels.DefaultNameSupplier.NAME_B);

    // Verify that unreferenced labels were removed.
    assertNull(findLabelNode(expectedRootNode, RenameLabels.DefaultNameSupplier.NAME_C));
  }

  @Test
  public void testRenameLabelsNestedBreak() {
    String originalCode =
        "function foo() {\n"
            + "  a: {\n"
            + "    b: {\n"
            + "      break a;\n"
            + "    }\n"
            + "  }\n"
            + "}\n"
            + "\n"
            + "function bar() {}";

    // Set up the nodes and options for the testing.
    Node externsNode = Node.objectLiteral();
    CompilerOptions options = new CompilerOptions(null);
    passManager.setOptions(options);

    // Create a new CompilerPass instance.
    RenameLabels renameLabels = new RenameLabels(passManager);

    // Traverse the node tree and perform the renaming.
    NodeTraversal nodeTraversal = new NodeTraversal(rootNode, externsNode, passManager);
    nodeTraversal.traverse(renameLabels);

    // Verify the renaming were done as expected.
    String expectedCode =
        "function foo() {\n"
            + "  " + RenameLabels.DefaultNameSupplier.NameGenerator.NAME_A + ": {\n"
            + "    " + RenameLabels.DefaultNameSupplier.NameGenerator.NAME_A + ": break "
            + RenameLabels.DefaultNameSupplier.NameGenerator.NAME_A + ";\n"
            + "  }\n"
            + "}\n"
            + "\n"
            + "function bar() {}";
    Node expectedRootNode = NodeUtil.parseJSCode(expectedCode);
    assertLabelRenamed(compiler, rootNode, expectedRootNode, RenameLabels.DefaultNameSupplier.NAME_A);

    // Verify that unreferenced labels were removed.
    assertNull(findLabelNode(expectedRootNode, RenameLabels.DefaultNameSupplier.NAME_B));
  }

  @Test
  public void testRenameLabelsNestedContinue() {
    String originalCode =
        "function foo() {\n"
            + "  a: {\n"
            + "    b: {\n"
            + "      continue a;\n"
            + "    }\n"
            + "  }\n"
            + "}\n"
            + "\n"
            + "function bar() {}";

    // Set up the nodes and options for the testing.
    Node externsNode = Node.objectLiteral();
    CompilerOptions options = new CompilerOptions(null);
    passManager.setOptions(options);

    // Create a new CompilerPass instance.
    RenameLabels renameLabels = new RenameLabels(passManager);

    // Traverse the node tree and perform the renaming.
    NodeTraversal nodeTraversal = new NodeTraversal(rootNode, externsNode, passManager);
    nodeTraversal.traverse(renameLabels);

    // Verify the renaming were done as expected.
    String expectedCode =
        "function foo() {\n"
            + "  " + RenameLabels.DefaultNameSupplier.NameGenerator.NAME_A + ": {\n"
            + "    " + RenameLabels.DefaultNameSupplier.NameGenerator.NAME_A + ": continue "
            + RenameLabels.DefaultNameSupplier.NameGenerator.NAME_A + ";\n"
            + "  }\n"
            + "}\n"
            + "\n"
            + "function bar() {}";
    Node expectedRootNode = NodeUtil.parseJSCode(expectedCode);
    assertLabelRenamed(compiler, rootNode, expectedRootNode, RenameLabels.DefaultNameSupplier.NAME_A);

    // Verify that unreferenced labels were removed.
    assertNull(findLabelNode(expectedRootNode, RenameLabels.DefaultNameSupplier.NAME_B));
  }

  @Test
  public void testRenameLabelsNestedLabel() {
    String originalCode =
        "function foo() {\n"
            + "  a: {\n"
            + "    b: {\n"
            + "      c: {\n"
            + "        break c;\n"
            + "      }\n"
            + "    }\n"
            + "  }\n"
            + "}\n"
            + "\n"
            + "function bar() {}";

    // Set up the nodes and options for the testing.
    Node externsNode = Node.objectLiteral();
    CompilerOptions options = new CompilerOptions(null);
    passManager.setOptions(options);

    // Create a new CompilerPass instance.
    RenameLabels renameLabels = new RenameLabels(passManager);

    // Traverse the node tree and perform the renaming.
    NodeTraversal nodeTraversal = new NodeTraversal(rootNode, externsNode, passManager);
    nodeTraversal.traverse(renameLabels);

    // Verify the renaming were done as expected.
    String expectedCode =
        "function foo() {\n"
            + "  " + RenameLabels.DefaultNameSupplier.NameGenerator.NAME_A + ": {\n"
            + "    " + RenameLabels.DefaultNameSupplier.NameGenerator.NAME_A + ": {\n"
            + "      " + RenameLabels.DefaultNameSupplier.NameGenerator.NAME_C + ": break "
            + RenameLabels.DefaultNameSupplier.NameGenerator.NAME_C + ";\n"
            + "    }\n"
            + "  }\n"
            + "}\n"
            + "\n"
            + "function bar() {}";
    Node expectedRootNode = NodeUtil.parseJSCode(expectedCode);
    assertLabelRenamed(compiler, rootNode, expectedRootNode, RenameLabels.DefaultNameSupplier.NAME_A);
    assertLabelRenamed(compiler, rootNode, expectedRootNode, RenameLabels.DefaultNameSupplier.NAME_C);

    // Verify that unreferenced labels were removed.
    assertNull(findLabelNode(expectedRootNode, RenameLabels.DefaultNameSupplier.NAME_B));
  }

  @Test
  public void testRenameLabelsEmptyBlock() {
    String originalCode = "function foo() {}\n";

    // Set up the nodes and options for the testing.
    Node externsNode = Node.objectLiteral();
    CompilerOptions options = new CompilerOptions(null);
    passManager.setOptions(options);

    // Create a new CompilerPass instance.
    RenameLabels renameLabels = new RenameLabels(passManager);

    // Traverse the node tree and perform the renaming.
    NodeTraversal nodeTraversal = new NodeTraversal(rootNode, externsNode, passManager);
    nodeTraversal.traverse(renameLabels);

    // Verify that nothing was changed.
    String expectedCode = originalCode;
    Node expectedRootNode = NodeUtil.parseJSCode(expectedCode);
    assertLabelRenamed(compiler, rootNode, expectedRootNode, null);
  }

  private Node findLabelNode(Node node, String name) {
    if (name == null) {
      return null;
    }
    if (node.getType() == Token.LABEL && node.getFirstChild().getString().equals(name)) {
      return node;
    }
    for (int i = 0; i < node.getNumChildren(); i++) {
      Node child = node.getChild(i);
      Node labelNode = findLabelNode(child, name);
      if (labelNode != null) {
        return labelNode;
      }
    }
    return null;
  }
}