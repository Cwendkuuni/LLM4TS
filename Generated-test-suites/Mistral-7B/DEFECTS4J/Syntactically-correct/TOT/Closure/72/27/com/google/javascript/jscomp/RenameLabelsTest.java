import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static com.google.javascript.jscomp.NodeTraversal.traverse;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.RenameLabels;
import com.google.javascript.rhino.NodeFactory;

import java.util.HashMap;
import java.util.Map;

public class RenameLabelsTest {

  public static void main(String[] args) throws Exception {
    CompilerOptions options = new CompilerOptions();
    AbstractCompiler compiler = new AbstractCompiler();
    CompilerPass pass = new RenameLabels(compiler);

    String source = "function foo() {" +
        "  a: { break a; }" +
        "  b: {" +
        "    a: { break a; }" +
        "    c: { continue a; }" +
        "    d: { continue b; }" +
        "  }" +
        "  e: { label e; continue e; }" +
        "}";

    Node externs = NodeFactory.compile(source);
    Node root = NodeFactory.createScope(NodeFactory.SCOPE_GLOBAL, null);
    NodeUtil.setSourceUnit(root, "main.js", "test", null);

    Map<String, String> originalNames = new HashMap<>();

    traverse(compiler, root, new NodeTraversal.SimpleCallback() {
      @Override
      public boolean shouldTraverse(NodeTraversal t, Node n, Node parent) {
        if (n.getType() == Token.LABEL) {
          String name = n.getFirstChild().getString();
          originalNames.put(name, nameNodeToString(n.getFirstChild()));
          return false;
        }
        return true;
      }

      @Override
      public void visit(NodeTraversal t, Node n, Node parent) {
        // Do nothing
      }
    });

    pass.process(externs, root);

    traverse(compiler, root, new ProcessLabelsAndCheckRenaming(pass, originalNames));

    printRenamedLabels(pass);
  }

  private static String nameNodeToString(Node node) {
    return node.getFirstChild().getString();
  }

  private static void printRenamedLabels(RenameLabels pass) {
    NodeTraversal.traverse(pass.compiler, pass.compiler.getRootNode(),
        new PrintRenamedLabels(pass));
  }

  private static class PrintRenamedLabels implements NodeTraversal.Callback {

    private final RenameLabels pass;

    PrintRenamedLabels(RenameLabels pass) {
      this.pass = pass;
    }

    @Override
    public boolean shouldTraverse(NodeTraversal nodeTraversal, Node node,
        Node parent) {
      if (node.getType() == Token.LABEL) {
        String oldName = pass.getLabelInfo(node.getFirstChild().getString()).name;
        String newName = node.getFirstChild().getString();
        System.out.println("label renamed: " + oldName + " => " + newName);
      }
      return true;
    }

    @Override
    public void visit(NodeTraversal nodeTraversal, Node node, Node parent) {
      // Do nothing
    }
  }

  private static class ProcessLabelsAndCheckRenaming implements NodeTraversal.SimpleCallback {
    private final RenameLabels pass;
    private final Map<String, String> originalNames;

    ProcessLabelsAndCheckRenaming(RenameLabels pass, Map<String, String> originalNames) {
      this.pass = pass;
      this.originalNames = originalNames;
    }

    @Override
    public boolean shouldTraverse(NodeTraversal t, Node n, Node parent) {
      if (n.getType() == Token.LABEL && originalNames.containsKey(n.getFirstChild().getString())) {
        String oldName = originalNames.get(n.getFirstChild().getString());
        LabelInfo li = pass.getLabelInfo(n.getFirstChild().getString());
        assertNotNull(li);
        String newName = getNameForId(li.id);
        assertNotNull(newName);
        assertNotSame(oldName, newName);
        System.out.println("label renamed: " + oldName + " => " + newName);
      }
      return true;
    }

    @Override
    public void visit(NodeTraversal t, Node n, Node parent) {
      // Do nothing
    }

    private String getNameForId(int id) {
      return pass.processLabels.names.get(id);
    }
  }
}