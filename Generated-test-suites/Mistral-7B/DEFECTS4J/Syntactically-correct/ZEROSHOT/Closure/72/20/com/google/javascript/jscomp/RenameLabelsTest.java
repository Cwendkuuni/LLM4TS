import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal.ScopedCallback;
import com.google.javascript.jscomp.NodeTraversal.TraversalType;
import com.google.javascript.jscomp.test.AbstractCompilerTestCase;

public class RenameLabelsTest extends AbstractCompilerTestCase {

  public void testRenameLabels() throws Exception {
    Compiler compiler = mock(Compiler.class);
    CompilerPass renameLabels = new RenameLabels(compiler);

    String input =
        "function bar() {"
            + "  a: {"
            + "    b: {"
            + "      break a;"
            + "    }"
            + "  }"
            + "  a: {"
            + "    b: break a;"
            + "  }"
            + "}";

    Node root = parse(input);

    renameLabels.process(null, root);

    NodeTraversal traversal = new NodeTraversal(root, TraversalType.BREADTH_FIRST);
    traversal.traverse(renameLabels);

    ScopedCallback callback = new ScopedCallback() {
      Map<String, LabelInfo> labelMap = new HashMap<>();

      @Override
      public void enterScope(NodeTraversal nodeTraversal) {
        labelMap.put("outer", new LabelInfo(1));
      }

      @Override
      public boolean shouldTraverse(NodeTraversal nodeTraversal, Node node,
          Node parent) {
        if (node.getType() == Token.LABEL) {
          String name = node.getFirstChild().getString();
          LabelInfo labelInfo = labelMap.get(name);
          if (labelInfo != null) {
            assertEquals(name, getNameForId(labelInfo.id));
          }
          return true;
        }
        return false;
      }

      private String getNameForId(int id) {
        return((RenameLabels) renameLabels).getProcessLabels().names.get(id - 1);
      }
    };

    traversal.traverse(callback);
  }

  private static class LabelInfo {
    int id;
    String name;

    LabelInfo(int id, String name) {
      this.id = id;
      this.name = name;
    }

    @Override
    public String toString() {
      return "LabelInfo{" +
          "id=" + id +
          ", name='" + name + '\'' +
          '}';
    }
  }

  private void assertLabelMappings(LabelInfo... expected) {
    String output = getCompilerLog();
    String[] lines = output.split("\n");
    assertEquals(expected.length, lines.length);
    for (int i = 0; i < lines.length; i++) {
      String line = lines[i];
      String[] parts = line.split(": ");
      assertEquals(expected[i].name, parts[1]);
    }
  }

  public void testRenameLabelsMultipleLabelsPerScope() throws Exception {
    Compiler compiler = mock(Compiler.class);
    CompilerPass renameLabels = new RenameLabels(compiler);

    String input =
        "function foo() {"
            + "  a: {"
            + "    b: {"
            + "      break a;"
            + "      break b;"
            + "    }"
            + "    c: {"
            + "      break c;"
            + "    }"
            + "  }"
            + "}";

    Node root = parse(input);

    renameLabels.process(null, root);

    NodeTraversal traversal = new NodeTraversal(root, TraversalType.BREADTH_FIRST);
    traversal.traverse(renameLabels);

    ScopedCallback callback = new ScopedCallback() {
      Map<String, LabelInfo> labelMap = new HashMap<>();

      @Override
      public void enterScope(NodeTraversal nodeTraversal) {
        labelMap.put("outer", new LabelInfo(1, "a"));
      }

      @Override
      public boolean shouldTraverse(NodeTraversal nodeTraversal, Node node,
          Node parent) {
        if (node.getType() == Token.LABEL) {
          String name = node.getFirstChild().getString();
          LabelInfo labelInfo = labelMap.get(name);
          if (labelInfo != null) {
            assertEquals(name, getNameForId(labelInfo.id));
          }
          return true;
        }
        return false;
      }

      @Override
      public void visit(NodeTraversal nodeTraversal, Node node, Node parent) {
        if (node.getType() == Token.LABEL) {
          String name = node.getFirstChild().getString();
          LabelInfo labelInfo = labelMap.get(name);
          if (labelInfo != null) {
            assertEquals(getNameForId(labelInfo.id), node.getFirstChild().getString());
          }
        }
      }

      private String getNameForId(int id) {
        return((RenameLabels) renameLabels).getProcessLabels().names.get(id - 1);
      }
    };

    traversal.traverse(callback);
  }

  public void testRenameLabelsNestedScopes() throws Exception {
    Compiler compiler = mock(Compiler.class);
    CompilerPass renameLabels = new RenameLabels(compiler);

    String input =
        "function foo() {"
            + "  a: {"
            + "    b: {"
            + "      c: {"
            + "        break c;"
            + "        break b;"
            + "      }"
            + "    }"
            + "  }"
            + "}";

    Node root = parse(input);

    renameLabels.process(null, root);

    NodeTraversal traversal = new NodeTraversal(root, TraversalType.BREADTH_FIRST);
    traversal.traverse(renameLabels);

    ScopedCallback callback = new ScopedCallback() {
      Map<String, LabelInfo> labelMap = new HashMap<>();

      @Override
      public void enterScope(NodeTraversal nodeTraversal) {
        labelMap.put("outer", new LabelInfo(1, "a"));
      }

      @Override
      public boolean shouldTraverse(NodeTraversal nodeTraversal, Node node,
          Node parent) {
        if (node.getType() == Token.LABEL) {
          String name = node.getFirstChild().getString();
          LabelInfo labelInfo = labelMap.get(name);
          if (labelInfo != null) {
            assertEquals(name, getNameForId(labelInfo.id));
          }
          return true;
        }
        return false;
      }

      @Override
      public void visit(NodeTraversal nodeTraversal, Node node, Node parent) {
        if (node.getType() == Token.LABEL) {
          String name = node.getFirstChild().getString();
          LabelInfo labelInfo = labelMap.get(name);
          if (labelInfo != null) {
            assertEquals(getNameForId(labelInfo.id), node.getFirstChild().getString());
          }
        }
      }

      private String getNameForId(int id) {
        return((RenameLabels) renameLabels).getProcessLabels().names.get(id - 1);
      }
    };

    traversal.traverse(callback);
  }

  public void testRenameLabelsUnreferencedLabels() throws Exception {
    Compiler compiler = mock(Compiler.class);
    CompilerPass renameLabels = new RenameLabels(compiler);

    String input =
        "function foo() {"
            + "  a: {"
            + "    b: {"
            + "      break b;"
            + "    }"
            + "    dead: {}"
            + "  }"
            + "}";

    Node root = parse(input);

    renameLabels.process(null, root);

    NodeTraversal traversal = new NodeTraversal(root, TraversalType.BREADTH_FIRST);
    traversal.traverse(renameLabels);

    ScopedCallback callback = new ScopedCallback() {
      @Override
      public boolean shouldTraverse(NodeTraversal nodeTraversal, Node node,
          Node parent) {
        if (node.getType() == Token.LABEL) {
          String name = node.getFirstChild().getString();
          if ("dead".equals(name)) {
            Node labelNode = node;
            assertEquals(0, labelNode.getNumChildren());
          }
          return true;
        }
        return false;
      }
    };

    traversal.traverse(callback);
  }

  public void testRemoveUnusedLabels() throws Exception {
    Compiler compiler = mock(Compiler.class);
    CompilerPass renameLabels = new RenameLabels(compiler, new DefaultNameSupplier(), false);

    String input =
        "function foo() {"
            + "  a: {"
            + "    b: {"
            + "      break b;"
            + "    }"
            + "  }"
            + "  a: {"
            + "    c: {}"
            + "  }"
            + "}";

    Node root = parse(input);

    renameLabels.process(null, root);

    NodeTraversal traversal = new NodeTraversal(root, TraversalType.BREADTH_FIRST);
    traversal.traverse(renameLabels);

    ScopedCallback callback = new ScopedCallback() {
      @Override
      public boolean shouldTraverse(NodeTraversal nodeTraversal, Node node,
          Node parent) {
        if (node.getType() == Token.LABEL && node.getLastChild().getType() == Token.BLOCK) {
          Node labelNode = node;
          assertEquals(1, labelNode.getNumChildren());
          Node blockNode = labelNode.getLastChild();
          assertEquals(0, blockNode.getNumChildren());
        }
        return true;
      }
    };

    traversal.traverse(callback);

    assertEquals(2, getNumChildren(root));
    assertEquals(0, getNumChildren(root.getChild(1)));
  }

  private static class DefaultNameSupplier implements RenameLabels.Supplier<String> {
    @Override
    public String get() {
      return "label";
    }
  }

  private static int getNumChildren(Node node) {
    return node.getNumChildren();
  }

  private String getCompilerLog() {
    return((Compiler) compiler).getDiagnostics().getAllErrors().getErrorList()
        .toString();
  }
}