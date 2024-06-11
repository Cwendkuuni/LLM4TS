import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.Reference;
import com.google.javascript.jscomp.ReferenceCollection;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Supplier;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class InlineObjectLiteralsTest {

  private static class TestCompiler extends AbstractCompiler {
    private Supplier<String> nameIdSupplier = () -> "test_inline_var";

    @Override
    protected CompilerPass getPass(String passName) {
      if (InlineObjectLiterals.class.getName().equals(passName)) {
        return new InlineObjectLiterals(this, nameIdSupplier);
      }
      return super.getPass(passName);
    }
  }

  @org.junit.Test
  public void testInlineObjectLiterals() {

    TestCompiler compiler = new TestCompiler();

    // Prepare test data
    Node root = Node.newObjectLiteral(
        Node.newProperty("a", Node.newNumberLiteral(1)),
        Node.newProperty("b", Node.newObjectLiteral(
            Node.newProperty("c", Node.newNumberLiteral(2)),
            Node.newProperty("d", Node.newNumberLiteral(3))
        )));

    Scope scope = new Scope();
    scope.setRootNode(root);

    // Run pass
    compiler.process(null, root);

    // Check result
    Node result = scope.getRootNode();
    Iterator<Node> childrenIterator = result.getFirstChild().getChildrenIterator();
    List<Node> nodes = Lists.newArrayList();
    while (childrenIterator.hasNext()) {
      nodes.add(childrenIterator.next());
    }

    // Check for expected number of nodes
    assertEquals(10, nodes.size());

    // Check expected variables are present
    for (Map.Entry<String, String> entry : Maps.newLinkedHashMap()
        .entrySet()) {
      String variableName = entry.getKey();
      String expectedName = "JSCompiler_object_inline_" + variableName;
      Node variableNode = null;
      for (Node node : nodes) {
        if (NodeUtil.isVarNode(node) && variableName.equals(node.getString())) {
          variableNode = node;
          break;
        }
      }
      assertEquals("Variable " + variableName + " not present",
          variableNode, NodeUtil.newVarNode(expectedName, null));
    }

    // Check expected assignments are present
    for (Map.Entry<String, String> entry : Maps.newLinkedHashMap()
        .entrySet()) {
      String variableName = entry.getKey();
      String expectedVariableName = "JSCompiler_object_inline_" + variableName;
      String keyName = null;
      String valueName = null;
      for (Node node : nodes) {
        if (NodeUtil.isAssignNode(node)) {
          Node leftHandSide = node.getFirstChild();
          if (expectedVariableName.equals(leftHandSide.getString())) {
            Node rightHandSide = node.getLastChild();
            if (rightHandSide.getType() == Token.GETPROP) {
              keyName = rightHandSide.getLastChild().getString();
              valueName = rightHandSide.getFirstChild().getString();
            } else if (rightHandSide.getType() == Token.NUMBER
                       || rightHandSide.getType() == Token.STRING) {
              valueName = rightHandSide.getString();
            }
          }
        }
      }
      assertEquals("Key-value pair " + variableName + " not present",
          keyName, variableName + "_c");
      assertEquals("Key-value pair " + variableName + " not present",
          valueName, "2");
    }
  }
}