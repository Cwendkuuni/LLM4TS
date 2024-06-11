import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.AbstractCompiler.LifeCycleStage;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeTraversal.AbstractPostOrderCallback;
import com.google.javascript.rhino.Token;
import com.google.javascript.rhino.TokenStream;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

public class RenamePrototypesTest {

  private CompilerPass renamePrototypes;
  private NodeTraversal nodeTraversal;
  private AbstractCompiler compiler;
  private TokenStream input;

  @Before
  public void setUp() {
    compiler = mock(AbstractCompiler.class);
    when(compiler.getCodingConvention()).thenReturn(mockCodingConvention());
    when(compiler.getLifeCycleStage()).thenReturn(LifeCycleStage.NORMALIZED);

    renamePrototypes = new RenamePrototypes(compiler, false, null, null);
    nodeTraversal = mock(NodeTraversal.class);

    input = new TokenStream(new byte[0]);
  }

  @Test
  public void testRenaming() {
    // Test simple case of no renaming needed
    when(compiler.getInput()).thenReturn(input);
    when(nodeTraversal.getInput()).thenReturn(input);

    Node root = new Node();
    NodeTraversal.traverse(compiler, root, renamePrototypes);

    assertEquals(0, renamePrototypes.properties.size());

    // Test simple case of renaming needed
    String oldName = "foo_bar";
    String newName = "baz_qux";

    when(compiler.getInput()).thenReturn(createInputWithStringNode(oldName));
    when(nodeTraversal.getInput()).thenReturn(input);

    Node oldNode = createNodeWithStringChild(oldName);
    Node newNode = createNodeWithStringChild(newName);

    when(nodeTraversal.getParent()).thenReturn(createNode("Object"));
    when(nodeTraversal.getPreviousSibling()).thenReturn(createNode("Object"));

    when(nodeTraversal.getFirstChild()).thenReturn(oldNode);
    when(nodeTraversal.getLastChild()).thenReturn(newNode);

    NodeTraversal.traverse(compiler, root, renamePrototypes);

    assertEquals(1, renamePrototypes.properties.size());
    Property property = renamePrototypes.properties.get(oldName);
    assertEquals(newName, property.newName);

    // Test multiple renamings
    when(compiler.getInput()).thenReturn(createInputWithStringNodes(oldName, oldName));
    when(nodeTraversal.getInput()).thenReturn(input);
    when(nodeTraversal.getLastChild()).thenReturn(oldNode);

    NodeTraversal.traverse(compiler, root, renamePrototypes);

    assertEquals(1, renamePrototypes.properties.size());
    Property property2 = renamePrototypes.properties.get(oldName);
    assertEquals(newName, property2.newName);

    // Test multiple renamings with different new names
    when(compiler.getInput()).thenReturn(createInputWithStringNodes(oldName, "newName1", oldName));
    when(nodeTraversal.getInput()).thenReturn(input);
    when(nodeTraversal.getLastChild()).thenReturn(oldNode);

    NodeTraversal traversal = mock(NodeTraversal.class);
    when(traversal.getLastChild()).thenReturn(oldNode);

    ArgumentCaptor<String> stringCaptor = ArgumentCaptor.forClass(String.class);
    when(nodeTraversal.getParent()).thenReturn(createNode("Object"));

    when(nodeTraversal.getFirstChild()).thenReturn(oldNode);
    when(nodeTraversal.getNextSibling()).thenReturn(newNodeWithStringChild("newName1"));

    NodeTraversal.traverse(compiler, root, renamePrototypes);

    NodeTraversal.traverse(compiler, root, renamePrototypes, traversal);

    assertEquals(2, renamePrototypes.properties.size());
    Property property3 = renamePrototypes.properties.get(oldName);
    assertEquals("newName1", property3.newName);

    Property property4 = renamePrototypes.properties.get(oldName);
    assertEquals(property4.count(), 2);

    // Test case where name can't be renamed
    when(compiler.getInput()).thenReturn(createInputWithStringNode(oldName));
    when(nodeTraversal.getInput()).thenReturn(input);
    when(nodeTraversal.getParent()).thenReturn(createNode("Object"));

    NodeTraversal.traverse(compiler, root, renamePrototypes);

    assertEquals(0, renamePrototypes.properties.size());

    // Test case where name is a reserved name
    when(compiler.getInput()).thenReturn(createInputWithStringNode("toString"));
    when(nodeTraversal.getInput()).thenReturn(input);

    NodeTraversal.traverse(compiler, root, renamePrototypes);

    assertEquals(0, renamePrototypes.properties.size());

    // Test case where name is a reserved name but it's already in use
    when(compiler.getInput()).thenReturn(createInputWithStringNodes("toString", "toString"));
    when(nodeTraversal.getInput()).thenReturn(input);
    when(nodeTraversal.getParent()).thenReturn(createNode("Object"));
    when(nodeTraversal.getLastChild()).thenReturn(newNodeWithStringChild("toString"));

    NodeTraversal.traverse(compiler, root, renamePrototypes);

    assertEquals(0, renamePrototypes.properties.size());
  }

  private Node createNode(String name) {
    Node node = new Node();
    node.setString(name);
    node.setType(Token.IDENT);
    return node;
  }

  private Node createNodeWithStringChild(String name) {
    Node node = createNode(name);
    Node child = new Node();
    child.setString(name);
    child.setType(Token.STRING);
    node.addChild(child);
    return node;
  }

  private Node newNodeWithStringChild(String name) {
    Node node = new Node();
    node.setString(name);
    node.setType(Token.STRING);
    return node;
  }

  private CompilerInput createInputWithStringNode(String name) {
    CompilerInput input = new CompilerInput();
    input.setStream(new TokenStream(new byte[0]));
    Node node = createNodeWithStringChild(name);
    input.setRoot(node);
    return input;
  }

  private CompilerInput createInputWithStringNodes(String... names) {
    CompilerInput input = new CompilerInput();
    input.setStream(new TokenStream(new byte[0]));

    Node parent = createNode("Object");
    for (String name : names) {
      Node node = createNodeWithStringChild(name);
      parent.addChild(node);
    }
    input.setRoot(parent);
    return input;
  }

  private CodingConvention mockCodingConvention() {
    return mock(CodingConvention.class);
  }
}

class CodingConvention {}