import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.base.Supplier;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.ProcessStats;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.Token;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RenameLabelsTest {

  private AbstractCompiler compiler;
  private Node rootNode;
  private RenameLabels renameLabels;

  @SuppressWarnings("unchecked")
  @Before
  public void setUp() {
    compiler = mock(AbstractCompiler.class);
    when(compiler.getOptions()).thenReturn(new CompilerOptions());
    when(compiler.getGlobalScope()).thenReturn(NodeFactory.createScope());
    when(compiler.getSourceUnit()).thenReturn(mock(SourceUnit.class));
    when(compiler.getProcessStats()).thenReturn(mock(ProcessStats.class));

    CompilerPass pass = new RenameLabels(compiler);
    NodeFactory nodeFactory = new NodeFactory();
    NodeTraversal nodeTraversal = new NodeTraversal(compiler, nodeFactory, pass);

    rootNode = NodeFactory.createBlock(new ArrayList<>());
    ProcessStats processStats = mock(ProcessStats.class);
    when(compiler.getProcessStats()).thenReturn(processStats);

    pass.process(NodeFactory.createScope(), rootNode);
    NodeUtil.traverse(nodeTraversal, rootNode);
  }

  @Test
  public void testSimpleRename() {
    Node labelNode = rootNode.getFirstChild();
    Node labelNameNode = labelNode.getFirstChild();
    assertEquals("a", labelNameNode.getString());

    Node breakNode = rootNode.getLastChild();
    Node breakLabelNameNode = breakNode.getFirstChild();
    assertEquals("a", breakLabelNameNode.getString());
  }

  @Test
  public void testDeepRename() {
    rootNode = NodeFactory.createBlock(createBlock("a:", createBlock("b:", NodeFactory.createBlock(new ArrayList<>()))));

    Node labelNode = rootNode.getFirstChild();
    Node labelNameNode = labelNode.getFirstChild();
    assertEquals("a", labelNameNode.getString());

    Node nestedLabelNode = labelNode.getLastChild();
    Node nestedLabelNameNode = nestedLabelNode.getFirstChild();
    assertEquals("b", nestedLabelNameNode.getString());

    Node breakNode = rootNode.getLastChild();
    Node breakLabelNameNode = breakNode.getFirstChild();
    assertEquals("b", breakLabelNameNode.getString());
  }

  @Test
  public void testRemoveUnusedLabel() {
    rootNode = NodeFactory.createBlock(NodeFactory.createLabel("a:"), NodeFactory.createBlock(new ArrayList<>()));

    rootNode = NodeFactory.createBlock(
        NodeFactory.createLabel("a:"),
        NodeFactory.createBlock(new ArrayList<>()),
        NodeFactory.createLabel("a:") // unused label
    );

    Node labelNode = rootNode.getFirstChild();
    assertEquals("a", labelNode.getFirstChild().getString());

    Node unusedLabelNode = rootNode.getLastChild();
    assertEquals("a", unusedLabelNode.getFirstChild().getString());

    // Check that unused label was removed
    assertEquals(2, rootNode.getNumChildren());
  }

  private Node createBlock(String... nodes) {
    ArrayList<Node> children = new ArrayList<>();
    for (String node : nodes) {
      children.add(createLabel(node));
    }
    return NodeFactory.createBlock(children);
  }

  private Node createLabel(String labelName) {
    Node labelNode = NodeFactory.createLabel(labelName + ":");
    Node labelNameNode = NodeFactory.createString(labelName);
    labelNode.setFirstChild(labelNameNode);
    return labelNode;
  }
}