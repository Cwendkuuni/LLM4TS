import static org.junit.Assert.*;
import static com.google.javascript.jscomp.RenameVars.*;

import com.google.common.collect.ImmutableMap;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.rhino.NodeFactory;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class RenameVarsTest {
  private Compiler compiler;
  private SourceFile sourceFile;
  private NodeFactory nodeFactory;

  @Before
  public void setUp() throws IOException {
    CompilerOptions options = new CompilerOptions();
    options.setWarningLevel("types", "quiet");
    compiler = new Compiler();
    compiler.init(options);
    sourceFile = new SourceFile("Test.java", "package test;\n" +
        "class Test {\n" +
        "  var a = 1;\n" +
        "  var b = 2;\n" +
        "  function foo(x, y) {\n" +
        "    return x + y;\n" +
        "  }\n" +
        "}", null);
    nodeFactory = compiler.getFactory();
  }

  @Test
  public void testRenameVars() {
    CompilerPass renameVars = new RenameVars(compiler, "prefix_", false, false, false, null, null, null);
    renameVars.process(null, sourceFile.getTree());
    Node root = sourceFile.getTree();

    NodeTraversal traversal = new NodeTraversal(root, new AbstractPostOrderVisitor() {
      @Override
      public void visit(Node n) {
        if (n.getType() != Token.VAR && n.getType() != Token.FUNCTION) {
          return;
        }
        String name = n.getFirstChild().getString();
        String newName = renameVars.renameMap.get(name);
        assertEquals(newName, n.getString());
      }
    });
    NodeTraversal.traverse(compiler, root, traversal);

    Node functionNode = findNode("foo", root);
    assertTrue(functionNode.getFirstChild().getString().startsWith("prefix_"));

    List<Node> varNodes = Arrays.asList(
        findNode("a", root),
        findNode("b", root)
    );
    for (Node varNode : varNodes) {
      assertTrue(varNode.getFirstChild().getString().startsWith("prefix_"));
    }
  }

  @Test
  public void testExterns() {
    CompilerOptions options = new CompilerOptions();
    options.setWarningLevel("types", "quiet");
    compiler = new Compiler();
    compiler.init(options);

    CompilerPass renameVars = new RenameVars(compiler, null, false, false, false, null, null, Sets.newHashSet("EXISTING_GLOBAL"));
    renameVars.process(null, sourceFile.getTree());

    Node root = sourceFile.getTree();
    assertFalse(renameVars.renameMap.containsKey("EXISTING_GLOBAL"));
    Node globalsNode = findNode("EXISTING_GLOBAL", root);
    assertEquals("EXISTING_GLOBAL", globalsNode.getString());
  }

  @Test
  public void testLocalRenamingOnly() {
    CompilerOptions options = new CompilerOptions();
    options.setWarningLevel("types", "quiet");
    compiler = new Compiler();
    compiler.init(options);

    CompilerPass renameVars = new RenameVars(compiler, null, true, false, false, null, null, null);
    renameVars.process(null, sourceFile.getTree());

    Node root = sourceFile.getTree();
    NodeTraversal traversal = new NodeTraversal(root, new AbstractPostOrderVisitor() {
      @Override
      public void visit(Node n) {
        if (n.getType() != Token.VAR) {
          return;
        }
        String name = n.getFirstChild().getString();
        if (!name.startsWith("L")) {
          return;
        }
        int index = Integer.parseInt(name.substring(1));
        int expectedIndex = index;
        if (index > 1) {
          expectedIndex--;
        }
        Node varNode = findNode("a", root);
        Node expectedVarNode = findNode("L" + expectedIndex, root);
        assertEquals(expectedVarNode.getFirstChild().getString(), n.getString());
      }
    });
    NodeTraversal.traverse(compiler, root, traversal);
  }

  private Node findNode(String name, Node root) {
    for (Node child = root.getFirstChild(); child != null; child = child.getNext()) {
      if (child.getType() != Token.NAME || !child.getString().equals(name)) {
        continue;
      }
      return child;
    }
    throw new RuntimeException("Node not found: " + name);
  }
}