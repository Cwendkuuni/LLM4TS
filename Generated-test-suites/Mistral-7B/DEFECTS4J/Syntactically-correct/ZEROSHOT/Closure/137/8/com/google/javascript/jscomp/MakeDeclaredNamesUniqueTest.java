import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.AbstractTraversal.AbstractPostOrderCallback;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.compiler.CompilerOptions;

import java.util.LinkedHashMap;
import java.util.Map;

public class MakeDeclaredNamesUniqueTest {

  private AbstractCompiler compiler;
  private NodeTraversal traverser;
  private MakeDeclaredNamesUnique makeDeclaredNamesUnique;
  private ContextualRenamer contextualRenamer;
  private InlineRenamer inlineRenamer;

  @org.junit.Before
  public void setUp() {
    compiler = mock(AbstractCompiler.class);
    contextualRenamer = new ContextualRenamer();
    inlineRenamer = new InlineRenamer(
        () -> "1", "", true); // Supplier for unique id
    makeDeclaredNamesUnique = new MakeDeclaredNamesUnique(contextualRenamer);
    makeDeclaredNamesUnique = new MakeDeclaredNamesUnique(inlineRenamer);
    traverser = new NodeTraversal(compiler, new CompilerOptions());
  }

  @org.junit.Test
  public void testMakeDeclaredNamesUnique_GlobalScope() {
    String originalCode = "var a = 1;\n" +
        "var b = 2;\n" +
        "var c = a;\n" +
        "var a = 3;\n";

    traverser.setSourceRoot(Node.createTree(originalCode));
    traverser.traverse(Node.ROOT_NODE, makeDeclaredNamesUnique);

    Node rootNode = traverser.getSourceRoot();
    Map<String, Node> nodesByName = getNodesByName(rootNode);

    assertEquals("1", nodesByName.get("a").getString());
    assertEquals("3", nodesByName.get("b").getString());
    assertEquals("3", nodesByName.get("c").getString());
  }

  @org.junit.Test
  public void testMakeDeclaredNamesUnique_LocalScope() {
    String originalCode = "function foo() {\n" +
        "  var a = 1;\n" +
        "  var b = a;\n" +
        "  var a = 3;\n" +
        "  return a;\n" +
        "}\n";

    traverser.setSourceRoot(Node.createTree(originalCode));
    traverser.traverse(Node.ROOT_NODE, makeDeclaredNamesUnique);

    Node rootNode = traverser.getSourceRoot();
    Map<String, Node> nodesByName = getNodesByName(rootNode);

    Node functionNode = nodesByName.get("foo");
    Node functionBodyNode = functionNode.getFirstChild();

    assertEquals("1", nodesByName.get("a").getString());
    assertEquals("2", nodesByName.get("b").getString());
    assertEquals("3", nodesByName.get("a").getString());

    assertEquals("1", functionBodyNode.getFirstChild().getString());
    assertEquals("3", functionBodyNode.getLastChild().getString());
  }

  @org.junit.Test
  public void testMakeDeclaredNamesUnique_NestedScope() {
    String originalCode = "function foo() {\n" +
        "  var a = 1;\n" +
        "  function bar() {\n" +
        "    var a = 2;\n" +
        "  }\n" +
        "  bar();\n" +
        "  return a;\n" +
        "}\n";

    traverser.setSourceRoot(Node.createTree(originalCode));
    traverser.traverse(Node.ROOT_NODE, makeDeclaredNamesUnique);

    Node rootNode = traverser.getSourceRoot();
    Map<String, Node> nodesByName = getNodesByName(rootNode);

    Node functionNode = nodesByName.get("foo");
    Node functionBodyNode = functionNode.getFirstChild();

    Node barFunctionNode = findNodeByName(functionBodyNode, "bar");
    Node barFunctionBodyNode = barFunctionNode.getFirstChild();

    assertEquals("1", nodesByName.get("a").getString());
    assertEquals("3", nodesByName.get("a").getString()); // in foo

    assertEquals("1", nodesByName.get("a").getString()); // in bar
    assertEquals("2", nodesByName.get("a").getString()); // in bar
  }

  private Node findNodeByName(Node parentNode, String name) {
    for (Node node = parentNode.getFirstChild(); node != null; node = node.getNext()) {
      if (NodeUtil.isFunctionDeclaration(node) || NodeUtil.isVarDeclaration(node)) {
        if (node.getString().equals(name)) {
          return node;
        }
      } else {
        Node foundNode = findNodeByName(node, name);
        if (foundNode != null) {
          return foundNode;
        }
      }
    }
    return null;
  }

  private Map<String, Node> getNodesByName(Node node) {
    Map<String, Node> nodesByName = new LinkedHashMap<>();
    collectNodesByName(node, nodesByName);
    return nodesByName;
  }

  private void collectNodesByName(Node node, Map<String, Node> nodesByName) {
    if (NodeUtil.isVarDeclaration(node) || NodeUtil.isFunctionDeclaration(node)) {
      nodesByName.put(node.getString(), node);
    }

    for (Node child = node.getFirstChild(); child != null; child = child.getNext()) {
      collectNodesByName(child, nodesByName);
    }
  }
}