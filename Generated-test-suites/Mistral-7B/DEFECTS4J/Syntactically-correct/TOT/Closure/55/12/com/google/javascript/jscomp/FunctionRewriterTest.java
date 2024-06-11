import com.google.javascript.jscomp.FunctionRewriter;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import org.junit.Before;
import org.junit.Test;

import static com.google.common.truth.Truth.assertThat;

public class FunctionRewriterTest {

  private FunctionRewriter functionRewriter;
  private Node rootNode;

  @Before
  public void setup() {
    functionRewriter = new FunctionRewriter(new AbstractCompiler() {});
    rootNode = createTestNode();
  }

  @Test
  public void testEmptyFunctionReduction() {
    functionRewriter.process(null, rootNode);

    Node expectedEmptyFunction = Node.newFunction(
        Node.EMPTY, Node.newName("emptyFn"), null, null);
    Node actualEmptyFunction = findNodeByName("emptyFn", rootNode);

    assertThat(actualEmptyFunction).isEqualTo(expectedEmptyFunction);
  }

  @Test
  public void testIdentityFunctionReduction() {
    Node identityFunctionNode = Node.newFunction(
        Node.BLOCK, Node.newName("identity"), Node.list(Node.newName("value")),
        Node.newReturn(Node.newName("value")));
    rootNode.replaceChildren(identityFunctionNode);

    functionRewriter.process(null, rootNode);

    Node expectedIdentityFunction = Node.newFunction(
        Node.BLOCK, Node.newName("identity"), Node.list(Node.newName("value")),
        Node.newCall(
            Node.newName("getter"), Node.newName("value")));
    Node actualIdentityFunction = findNodeByName("identity", rootNode);

    assertThat(actualIdentityFunction).isEqualTo(expectedIdentityFunction);
  }

  @Test
  public void testReturnConstantReduction() {
    Node returnConstantFunctionNode = Node.newFunction(
        Node.BLOCK, Node.newName("returnConstant"), null, null,
        Node.newReturn(Node.newNumber(42)));
    rootNode.replaceChildren(returnConstantFunctionNode);

    functionRewriter.process(null, rootNode);

    Node expectedReturnConstantFunction = Node.newFunction(
        Node.BLOCK, Node.newName("returnConstant"), null, null,
        Node.newCall(
            Node.newName("returnconst"), Node.newNumber(42)));
    Node actualReturnConstantFunction = findNodeByName("returnConstant", rootNode);

    assertThat(actualReturnConstantFunction).isEqualTo(expectedReturnConstantFunction);
  }

  @Test
  public void testGetterReduction() {
    Node getterFunctionNode = Node.newFunction(
        Node.BLOCK, Node.newName("getter"), null, null,
        Node.newReturn(Node.newGetProp(Node.newName("this"), Node.newName("property"))));
    rootNode.replaceChildren(getterFunctionNode);

    functionRewriter.process(null, rootNode);

    Node expectedGetterFunction = Node.newFunction(
        Node.BLOCK, Node.newName("getter"), null, null,
        Node.newCall(
            Node.newName("getter"), Node.newName("property")));
    Node actualGetterFunction = findNodeByName("getter", rootNode);

    assertThat(actualGetterFunction).isEqualTo(expectedGetterFunction);
  }

  @Test
  public void testSetterReduction() {
    Node setterFunctionNode = Node.newFunction(
        Node.BLOCK, Node.newName("setter"), Node.list(Node.newName("value")), null,
        Node.newStatementList(
            Node.newExpressionStatement(
                Node.newAssign(Node.newGetProp(Node.newName("this"), Node.newName("property")),
                    Node.newName("value")))));
    rootNode.replaceChildren(setterFunctionNode);

    functionRewriter.process(null, rootNode);

    Node expectedSetterFunction = Node.newFunction(
        Node.BLOCK, Node.newName("setter"), Node.list(Node.newName("value")), null,
        Node.newStatementList(
            Node.newExpressionStatement(
                Node.newCall(
                    Node.newName("setter"), Node.newName("property"),
                    Node.newName("value")))));
    Node actualSetterFunction = findNodeByName("setter", rootNode);

    assertThat(actualSetterFunction).isEqualTo(expectedSetterFunction);
  }

  private Node createTestNode() {
    Node root = new Node(Token.PROGRAM);
    root.addChild(new Node(Token.BLOCK, null, null));
    return root;
  }

  private Node findNodeByName(String name, Node root) {
    NodeTraversal traversal = new NodeTraversal(root, null);
    traversal.traverse(new NameFinder(name));
    return traversal.getCurrentNode();
  }

  private static class NameFinder implements Callback {
    private final String name;

    public NameFinder(String name) {
      this.name = name;
    }

    @Override
    public boolean shouldTraverse(NodeTraversal traversal, Node node, Node parent) {
      if (NodeUtil.isName(node) && node.getString().equals(name)) {
        traversal.setCurrentNode(node);
        return false;
      }
      return true;
    }

    @Override
    public void visit(NodeTraversal traversal, Node node, Node parent) {}
  }
}