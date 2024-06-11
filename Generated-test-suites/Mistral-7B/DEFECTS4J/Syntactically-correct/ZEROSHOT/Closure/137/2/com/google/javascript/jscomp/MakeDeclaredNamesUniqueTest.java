import static org.junit.Assert.*;
import static com.google.common.truth.Truth.assertThat;

import com.google.common.collect.ImmutableSet;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.TokenStream;

import java.util.List;
import java.util.Map;

public class MakeDeclaredNamesUniqueTest {

  private static Node createNodeWithName(String name) {
    TokenStream ts = new TokenStream(new char[0]);
    Node root = ts.parseIdentifier(name);
    return root;
  }

  @SuppressWarnings("unchecked")
  private static <T> ImmutableSet<T> asImmutableSet(Object obj) {
    return (ImmutableSet<T>) obj;
  }

  private static void assertUniqueNames(Node node, Renamer renamer) {
    Map<String, Node> nameToNode = Maps.newHashMap();
    traverseAndCollectNames(node, nameToNode, renamer);
    ImmutableSet<String> uniqueNames = ImmutableSet.copyOf(nameToNode.keySet());
    assertEquals(uniqueNames.size(), renamer.getDeclaredNames().size());
  }

  private static void traverseAndCollectNames(Node node, Map<String, Node> nameToNode, Renamer renamer) {
    if (node == null) {
      return;
    }

    switch (node.getType()) {
      case Token.NAME:
        String name = node.getString();
        if (renamer != null) {
          String newName = renamer.getReplacementName(name);
          assertEquals(newName, node.getString());
          nameToNode.put(newName, node);
          renamer.addDeclaredName(newName);
        }
        break;

      case Token.FUNCTION:
      case Token.CATCH:
        Deque<Renamer> renamerStack = renamer.getRenamerStack().copyOf();
        Renamer currentRenamer = renamerStack.peek();
        if (currentRenamer != null) {
          traverseAndCollectNames(node, nameToNode, currentRenamer);
        }
        break;

      default:
        if (node.getFirstChild() != null) {
          traverseAndCollectNames(node.getFirstChild(), nameToNode, renamer);
        }

        if (node.getLastChild() != null) {
          traverseAndCollectNames(node.getLastChild(), nameToNode, renamer);
        }
    }
  }

  @Test
  public void testSimple() {
    Renamer renamer = new MakeDeclaredNamesUnique().new ContextualRenamer();
    Node node = createNodeWithName("a");
    node = createNodeWithName("b");
    node = createNodeWithName("a");
    NodeTraversal.traverse(new MakeDeclaredNamesUnique(), node, renamer);
    assertUniqueNames(node, renamer);
    Iterator<String> iter = renamer.getDeclaredNames().iterator();
    assertEquals("a$0", iter.next());
    assertEquals("b", iter.next());
  }

  @Test
  public void testDuplicateNamesInFunction() {
    Renamer renamer = new MakeDeclaredNamesUnique().new ContextualRenamer();
    Node node = createNodeWithName("a");
    Node function = createNode(Token.FUNCTION);
    Node fnParams = createNodeWithChildren("params", ImmutableList.of(Node.newIdentifier("a")));
    Node functionBody = createNodeWithChildren("{a}", ImmutableList.of(createNodeWithName("a")));
    function.setFirstChild(fnParams);
    function.setLastChild(functionBody);
    node.setLastChild(function);

    node = createNodeWithName("a");
    NodeTraversal.traverse(new MakeDeclaredNamesUnique(), node, renamer);
    assertUniqueNames(node, renamer);
    Iterator<String> iter = renamer.getDeclaredNames().iterator();
    assertEquals("a$0", iter.next());
    assertEquals("a$1", iter.next());
  }

  @Test
  public void testDuplicateNamesInBlock() {
    Renamer renamer = new MakeDeclaredNamesUnique().new ContextualRenamer();
    Node node = createNodeWithName("a");
    Node block = createNodeWithChildren("{a}", ImmutableList.of(createNodeWithName("a")));
    node.setLastChild(block);

    node = createNodeWithName("a");
    NodeTraversal.traverse(new MakeDeclaredNamesUnique(), node, renamer);
    assertUniqueNames(node, renamer);
    Iterator<String> iter = renamer.getDeclaredNames().iterator();
    assertEquals("a$0", iter.next());
    assertEquals("a$1", iter.next());
  }

  @Test
  public void testDuplicateNamesInBlockAndFunction() {
    Renamer renamer = new MakeDeclaredNamesUnique().new ContextualRenamer();
    Node node = createNodeWithName("a");
    Node block = createNodeWithChildren("{a}", ImmutableList.of(createNodeWithName("a")));
    Node function = createNode(Token.FUNCTION);
    Node fnParams = createNodeWithChildren("params", ImmutableList.of(Node.newIdentifier("a")));
    Node functionBody = createNodeWithChildren("{a}", ImmutableList.of(createNodeWithName("a")));
    function.setFirstChild(fnParams);
    function.setLastChild(functionBody);
    node.setLastChild(function);
    node = createNodeWithName("a");

    node = createNodeWithName("a");
    NodeTraversal.traverse(new MakeDeclaredNamesUnique(), node, renamer);
    assertUniqueNames(node, renamer);
    Iterator<String> iter = renamer.getDeclaredNames().iterator();
    assertEquals("a$0", iter.next());
    assertEquals("a$2", iter.next());
    assertEquals("a$3", iter.next());
  }

  @Test
  public void testDuplicateNamesInFunctionAndBlock() {
    Renamer renamer = new MakeDeclaredNamesUnique().new ContextualRenamer();
    Node node = createNodeWithName("a");
    Node block = createNodeWithChildren("{a}", ImmutableList.of(createNodeWithName("a")));
    Node function = createNode(Token.FUNCTION);
    Node fnParams = createNodeWithChildren("params", ImmutableList.of(Node.newIdentifier("a")));
    Node functionBody = createNodeWithChildren("{a}", ImmutableList.of(createNodeWithName("a")));
    function.setFirstChild(fnParams);
    function.setLastChild(functionBody);
    node.setLastChild(function);
    node = createNodeWithName("a");

    NodeTraversal.traverse(new MakeDeclaredNamesUnique(), node, renamer);
    assertUniqueNames(node, renamer);
    Iterator<String> iter = renamer.getDeclaredNames().iterator();
    assertEquals("a$0", iter.next());
    assertEquals("a$1", iter.next());
    assertEquals("a$3", iter.next());
  }

  @Test
  public void testDuplicateNamesInFunctionWithParentFunction() {
    Renamer renamer = new MakeDeclaredNamesUnique().new ContextualRenamer();
    Node node = createNodeWithName("a");
    Node parentFunction = createNode(Token.FUNCTION);
    Node parentFnParams = createNodeWithChildren("params", ImmutableList.of());
    Node parentFunctionBody = createNodeWithChildren("{a}", ImmutableList.of(createNodeWithName("a")));
    parentFunction.setFirstChild(parentFnParams);
    parentFunction.setLastChild(parentFunctionBody);
    Node function = createNode(Token.FUNCTION);
    Node fnParams = createNodeWithChildren("params", ImmutableList.of(Node.newIdentifier("a")));
    Node functionBody = createNodeWithChildren("{a}", ImmutableList.of(createNodeWithName("a")));
    function.setFirstChild(fnParams);
    function.setLastChild(functionBody);
    parentFunction.setLastChild(function);
    node = createNodeWithName("a");

    NodeTraversal.traverse(new MakeDeclaredNamesUnique(), node, renamer);
    assertUniqueNames(node, renamer);
    Iterator<String> iter = renamer.getDeclaredNames().iterator();
    assertEquals("a$0", iter.next());
    assertEquals("a$4", iter.next());
    assertEquals("a$5", iter.next());
  }

  @Test
  public void testDuplicateNamesInBlockWithParentFunction() {
    Renamer renamer = new MakeDeclaredNamesUnique().new ContextualRenamer();
    Node node = createNodeWithName("a");
    Node parentFunction = createNode(Token.FUNCTION);
    Node parentFnParams = createNodeWithChildren("params", ImmutableList.of());
    Node parentFunctionBody = createNodeWithChildren("{a}", ImmutableList.of(createNodeWithName("a")));
    parentFunction.setFirstChild(parentFnParams);
    parentFunction.setLastChild(parentFunctionBody);
    Node block = createNodeWithChildren("{a}", ImmutableList.of(createNodeWithName("a")));
    node.setLastChild(block);

    NodeTraversal.traverse(new MakeDeclaredNamesUnique(), node, renamer);
    assertUniqueNames(node, renamer);
    Iterator<String> iter = renamer.getDeclaredNames().iterator();
    assertEquals("a$0", iter.next());
    assertEquals("a$1", iter.next());
    assertEquals("a$2", iter.next());
  }

  @Test
  public void testDuplicateNamesInFunctionAndBlockWithParentFunction() {
    Renamer renamer = new MakeDeclaredNamesUnique().new ContextualRenamer();
    Node node = createNodeWithName("a");
    Node parentFunction = createNode(Token.FUNCTION);
    Node parentFnParams = createNodeWithChildren("params", ImmutableList.of());
    Node parentFunctionBody = createNodeWithChildren("{a}", ImmutableList.of(createNodeWithName("a")));
    parentFunction.setFirstChild(parentFnParams);
    parentFunction.setLastChild(parentFunctionBody);
    Node function = createNode(Token.FUNCTION);
    Node fnParams = createNodeWithChildren("params", ImmutableList.of(Node.newIdentifier("a")));
    Node functionBody = createNodeWithChildren("{a}", ImmutableList.of(createNodeWithName("a")));
    function.setFirstChild(fnParams);
    function.setLastChild(functionBody);
    parentFunction.setLastChild(function);
    Node block = createNodeWithChildren("{a}", ImmutableList.of(createNodeWithName("a")));
    node.setLastChild(block);

    NodeTraversal.traverse(new MakeDeclaredNamesUnique(), node, renamer);
    assertUniqueNames(node, renamer);
    Iterator<String> iter = renamer.getDeclaredNames().iterator();
    assertEquals("a$0", iter.next());
    assertEquals("a$1", iter.next());
    assertEquals("a$3", iter.next());
    assertEquals("a$4", iter.next());
  }

  @Test
  public void testInlineRenamer() {
    Supplier<String> uniqueIdSupplier = () -> "_";
    String idPrefix = "";
    Renamer renamer = new MakeDeclaredNamesUnique().new InlineRenamer(uniqueIdSupplier, idPrefix, false);
    Node node = createNodeWithName("a");
    node = createNodeWithName("b");
    node = createNodeWithName("a");
    NodeTraversal.traverse(new MakeDeclaredNamesUnique(), node, renamer);
    assertUniqueNames(node, renamer);
    Iterator<String> iter = renamer.getDeclaredNames().iterator();
    assertEquals("a_", iter.next());
    assertEquals("b_", iter.next());
  }

  @Test
  public void testInlineRenamerPreserveConst() {
    Supplier<String> uniqueIdSupplier = () -> "_";
    String idPrefix = "const_";
    Renamer renamer = new MakeDeclaredNamesUnique().new InlineRenamer(uniqueIdSupplier, idPrefix, true);
    Node node = createNodeWithName("const a");
    node = createNodeWithName("b");
    node = createNodeWithName("const a");
    NodeTraversal.traverse(new MakeDeclaredNamesUnique(), node, renamer);
    assertUniqueNames(node, renamer);
    Iterator<String> iter = renamer.getDeclaredNames().iterator();
    assertEquals("const a", iter.next());
    assertEquals("b", iter.next());
  }

  private static Node createNode(int type) {
    return new Node(type);
  }

  private static Node createNodeWithChildren(String name, List<Node> children) {
    Node node = new Node(Token.NAME, name);
    for (Node child : children) {
      node.addChild(child);
    }
    return node;
  }

  private static Node createNodeWithName(String name) {
    Node node = new Node(Token.NAME, name);
    return node;
  }
}