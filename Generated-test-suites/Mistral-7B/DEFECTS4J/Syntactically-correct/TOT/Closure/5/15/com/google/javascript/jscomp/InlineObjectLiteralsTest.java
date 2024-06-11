import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Supplier;

@RunWith(org.mockito.jUnit.MockitoJUnitRunner.class)
public class InlineObjectLiteralsTest {

  private AbstractCompiler compiler;
  private InlineObjectLiterals inlineObjectLiterals;
  private Scope scope;

  @SuppressWarnings("unchecked")
  @Before
  public void setUp() {
    compiler = Mockito.mock(AbstractCompiler.class);
    scope = Mockito.mock(Scope.class);

    Supplier<String> safeNameIdSupplier = mock(Supplier.class);
    inlineObjectLiterals = new InlineObjectLiterals(compiler, safeNameIdSupplier);
  }

  @Test
  public void testInlineObjectLiteralWithSingleProperty() {

    // Arrange
    List<Node> rootChildren = Lists.newArrayList();
    Node root = new Node(Token.BLOCK);
    root.setChildren(rootChildren);

    Node objNode = new Node(Token.OBJECT_LITERAL);
    rootChildren.add(objNode);
    Node nameNode = NodeUtil.newNameNode("name");
    Node valueNode = NodeUtil.newNumberNode(1);
    Node propNode = new Node(Token.PROP, nameNode, valueNode);
    objNode.setChildren(ImmutableList.of(propNode));

    Node assignNode = NodeUtil.newAssignNode(NodeUtil.newNameNode("x"), objNode);
    rootChildren.add(assignNode);

    // Act
    inlineObjectLiterals.process(null, root);

    // Assert
    Node expectedAssignNode = NodeUtil.newAssignNode(
        NodeUtil.newNameNode("x"),
        NodeUtil.newVarNode("name_0_name", null));
    Node expectedValueNode = NodeUtil.newNumberNode(1);
    Node expectedVarNode = NodeUtil.newVarNode("name_0_name", null);

    Node newAssignNode = assignNode.getParent().getChildAtIndex(1);
    Node newAssignValueNode = newAssignNode.getFirstChild();

    Node newVarNode = newAssignNode.getLastChild();

    NodeUtil.assertNodeEquals(expectedAssignNode, assignNode);
    NodeUtil.assertNodeEquals(expectedValueNode, newAssignValueNode);
    NodeUtil.assertNodeEquals(expectedVarNode, newVarNode);
  }

  @Test
  public void testInlineObjectLiteralWithMultipleProperties() {

    // Arrange
    List<Node> rootChildren = Lists.newArrayList();
    Node root = new Node(Token.BLOCK);
    root.setChildren(rootChildren);

    Node objNode = new Node(Token.OBJECT_LITERAL);
    rootChildren.add(objNode);

    Node propNode1 = new Node(Token.PROP, NodeUtil.newNameNode("name1"));
    Node valueNode1 = NodeUtil.newStringNode("value1");
    Node nameNode1 = NodeUtil.newNameNode("name1");
    propNode1.setChildren(ImmutableList.of(nameNode1, valueNode1));
    objNode.setChildren(ImmutableList.of(propNode1));

    Node propNode2 = new Node(Token.PROP, NodeUtil.newNameNode("name2"));
    Node valueNode2 = NodeUtil.newStringNode("value2");
    Node nameNode2 = NodeUtil.newNameNode("name2");
    propNode2.setChildren(ImmutableList.of(nameNode2, valueNode2));
    objNode.addChild(propNode2);

    Node assignNode = NodeUtil.newAssignNode(NodeUtil.newNameNode("x"), objNode);
    rootChildren.add(assignNode);

    // Act
    inlineObjectLiterals.process(null, root);

    // Assert
    Node expectedAssignNode = NodeUtil.newAssignNode(
        NodeUtil.newNameNode("x"),
        NodeUtil.newVarNode("name1_0_name1", null));
    Node expectedValueNameNode1 = NodeUtil.newNameNode("name1_0_value1");
    Node expectedValueNode1 = NodeUtil.newStringNode("value1");
    Node expectedVarNode1 = NodeUtil.newVarNode("name1_0_name1", null);

    Node expectedAssignNode2 = NodeUtil.newAssignNode(
        NodeUtil.newNameNode("x"),
        NodeUtil.newVarNode("name2_1_name2", null));
    Node expectedValueNameNode2 = NodeUtil.newNameNode("name2_1_value2");
    Node expectedValueNode2 = NodeUtil.newStringNode("value2");
    Node expectedVarNode2 = NodeUtil.newVarNode("name2_1_name2", null);

    Node newAssignNode = assignNode.getParent().getChildAtIndex(1);
    Node newAssignValueNode1 = newAssignNode.getChildAtIndex(0);
    Node newAssignValueNode2 = newAssignNode.getChildAtIndex(1);

    Node newVarNode1 = newAssignNode.getChildAtIndex(2);
    Node newVarNode2 = newAssignNode.getChildAtIndex(3);

    NodeUtil.assertNodeEquals(expectedAssignNode, assignNode);

    NodeUtil.assertNodeEquals(expectedValueNameNode1, newAssignValueNode1);
    NodeUtil.assertNodeEquals(expectedValueNode1, newAssignValueNode1.getFirstChild());
    NodeUtil.assertNodeEquals(expectedVarNode1, newVarNode1);

    NodeUtil.assertNodeEquals(expectedAssignNode2, newAssignNode.getChildAtIndex(4));
    NodeUtil.assertNodeEquals(expectedValueNameNode2, newAssignNode.getChildAtIndex(5));
    NodeUtil.assertNodeEquals(expectedValueNode2, newAssignNode.getChildAtIndex(6));
    NodeUtil.assertNodeEquals(expectedVarNode2, newVarNode2);
  }

  @Test
  public void testInlineObjectLiteralWithCycle() {

    // Arrange
    List<Node> rootChildren = Lists.newArrayList();
    Node root = new Node(Token.BLOCK);
    root.setChildren(rootChildren);

    Node objNode1 = new Node(Token.OBJECT_LITERAL);
    rootChildren.add(objNode1);

    Node propNode11 = new Node(Token.PROP, NodeUtil.newNameNode("a"));
    Node valueNode11 = NodeUtil.newNameNode("obj2");
    Node nameNode11 = NodeUtil.newNameNode("a");
    propNode11.setChildren(ImmutableList.of(nameNode11, valueNode11));
    objNode1.setChildren(ImmutableList.of(propNode11));

    Node objNode2 = new Node(Token.OBJECT_LITERAL);
    rootChildren.add(objNode2);

    Node propNode21 = new Node(Token.PROP, NodeUtil.newNameNode("a"));
    Node valueNode21 = NodeUtil.newNameNode("obj1");
    Node nameNode21 = NodeUtil.newNameNode("a");
    propNode21.setChildren(ImmutableList.of(nameNode21, valueNode21));
    objNode2.setChildren(ImmutableList.of(propNode21));

    Node assignNode = NodeUtil.newAssignNode(NodeUtil.newNameNode("x"), objNode1);
    rootChildren.add(assignNode);

    // Act
    inlineObjectLiterals.process(null, root);

    // Assert
    // This test case should fail as there is a cycle in the object literals.
    // The InlineObjectLiteralsPass should not inline object literals with cycles.
  }

  @Test
  public void testInlineObjectLiteralWithSelfReference() {

    // Arrange
    List<Node> rootChildren = Lists.newArrayList();
    Node root = new Node(Token.BLOCK);
    root.setChildren(rootChildren);

    Node objNode = new Node(Token.OBJECT_LITERAL);
    rootChildren.add(objNode);

    Node propNode = new Node(Token.PROP, NodeUtil.newNameNode("x"));
    Node valueNode = new Node(Token.OBJECT_LITERAL);
    Node nameNode = NodeUtil.newNameNode("x");
    Node assignNode = NodeUtil.newAssignNode(nameNode, valueNode);
    valueNode.setChildren(ImmutableList.of(assignNode));
    propNode.setChildren(ImmutableList.of(nameNode, valueNode));
    objNode.setChildren(ImmutableList.of(propNode));

    Node assignNode2 = NodeUtil.newAssignNode(NodeUtil.newNameNode("x"), objNode);
    rootChildren.add(assignNode2);

    // Act
    inlineObjectLiterals.process(null, root);

    // Assert
    // This test case should fail as the object literal contains a self-reference.
    // The InlineObjectLiteralsPass should not inline object literals with self-references.
  }

  @Test
  public void testInlineObjectLiteralWithFunction() {

    // Arrange
    List<Node> rootChildren = Lists.newArrayList();
    Node root = new Node(Token.BLOCK);
    root.setChildren(rootChildren);

    Node objNode = new Node(Token.OBJECT_LITERAL);
    rootChildren.add(objNode);

    Node propNode = new Node(Token.PROP, NodeUtil.newNameNode("x"));
    Node valueNode = NodeUtil.newFunctionNode(
        NodeUtil.newNameNode("x"), ImmutableList.of(), ImmutableList.of());
    valueNode.setIsStrict();
    Node nameNode = NodeUtil.newNameNode("x");
    propNode.setChildren(ImmutableList.of(nameNode, valueNode));
    objNode.setChildren(ImmutableList.of(propNode));

    Node assignNode = NodeUtil.newAssignNode(NodeUtil.newNameNode("x"), objNode);
    rootChildren.add(assignNode);

    // Act
    inlineObjectLiterals.process(null, root);

    // Assert
    // This test case should fail as the object literal contains a function.
    // The InlineObjectLiteralsPass should not inline object literals with functions.
  }

  @Test
  public void testInlineObjectLiteralWithRecursion() {

    // Arrange
    List<Node> rootChildren = Lists.newArrayList();
    Node root = new Node(Token.BLOCK);
    root.setChildren(rootChildren);

    Node objNode1 = new Node(Token.OBJECT_LITERAL);
    rootChildren.add(objNode1);

    Node propNode11 = new Node(Token.PROP, NodeUtil.newNameNode("a"));
    Node valueNode11 = NodeUtil.newNameNode("obj2");
    Node nameNode11 = NodeUtil.newNameNode("a");
    propNode11.setChildren(ImmutableList.of(nameNode11, valueNode11));
    objNode1.setChildren(ImmutableList.of(propNode11));

    Node objNode2 = new Node(Token.OBJECT_LITERAL);
    rootChildren.add(objNode2);

    Node propNode21 = new Node(Token.PROP, NodeUtil.newNameNode("a"));
    Node valueNode21 = NodeUtil.newNameNode("obj1");
    Node nameNode21 = NodeUtil.newNameNode("a");
    propNode21.setChildren(ImmutableList.of(nameNode21, valueNode21));
    objNode2.setChildren(ImmutableList.of(propNode21));

    Node assignNode = NodeUtil.newAssignNode(NodeUtil.newNameNode("x"), objNode1);
    rootChildren.add(assignNode);

    // Act
    inlineObjectLiterals.process(null, root);

    // Assert
    // This test case should fail as there is a recursion in the object literals.
    // The InlineObjectLiteralsPass should not inline object literals with recursion.
  }

  @Test
  public void testInlineObjectLiteralWithExportedVariable() {

    // Arrange
    List<Node> rootChildren = Lists.newArrayList();
    Node root = new Node(Token.BLOCK);
    root.setChildren(rootChildren);

    Node objNode = new Node(Token.OBJECT_LITERAL);
    rootChildren.add(objNode);

    Node propNode = new Node(Token.PROP, NodeUtil.newNameNode("x"));
    Node valueNode = NodeUtil.newNameNode("ExportedVariable");
    Node nameNode = NodeUtil.newNameNode("x");
    propNode.setChildren(ImmutableList.of(nameNode, valueNode));
    objNode.setChildren(ImmutableList.of(propNode));

    Node assignNode = NodeUtil.newAssignNode(NodeUtil.newNameNode("x"), objNode);
    rootChildren.add(assignNode);

    Node exportNode = NodeUtil.newExportNode(NodeUtil.newNameNode("x"));
    rootChildren.add(exportNode);

    // Act
    inlineObjectLiterals.process(null, root);

    // Assert
    // This test case should pass as the exported variable should not prevent inlining.
  }

  @Test
  public void testInlineObjectLiteralWithExternVariable() {

    // Arrange
    List<Node> rootChildren = Lists.newArrayList();
    Node root = new Node(Token.BLOCK);
    root.setChildren(rootChildren);

    Node objNode = new Node(Token.OBJECT_LITERAL);
    rootChildren.add(objNode);

    Node propNode = new Node(Token.PROP, NodeUtil.newNameNode("x"));
    Node valueNode = NodeUtil.newNameNode("ExternVariable");
    Node nameNode = NodeUtil.newNameNode("x");
    propNode.setChildren(ImmutableList.of(nameNode, valueNode));
    objNode.setChildren(ImmutableList.of(propNode));

    Node assignNode = NodeUtil.newAssignNode(NodeUtil.newNameNode("x"), objNode);
    rootChildren.add(assignNode);

    // Act
    inlineObjectLiterals.process(null, root);

    // Assert
    // This test case should pass as the extern variable should not prevent inlining.
  }

  @Test
  public void testInlineObjectLiteralWithGlobalVariable() {

    // Arrange
    List<Node> rootChildren = Lists.newArrayList();
    Node root = new Node(Token.BLOCK);
    root.setChildren(rootChildren);

    Node objNode = new Node(Token.OBJECT_LITERAL);
    rootChildren.add(objNode);

    Node propNode = new Node(Token.PROP, NodeUtil.newNameNode("x"));
    Node valueNode = NodeUtil.newNameNode("window.GlobalVariable");
    Node nameNode = NodeUtil.newNameNode("x");
    propNode.setChildren(ImmutableList.of(nameNode, valueNode));
    objNode.setChildren(ImmutableList.of(propNode));

    Node assignNode = NodeUtil.newAssignNode(NodeUtil.newNameNode("x"), objNode);
    rootChildren.add(assignNode);

    // Act
    inlineObjectLiterals.process(null, root);

    // Assert
    // This test case should fail as the global variable should not be inlined.
  }
}