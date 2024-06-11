import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Reference;
import com.google.javascript.jscomp.ReferenceCollectingCallback;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.NodeUtil;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class InlineObjectLiteralsTest {

  private AbstractCompiler compiler;
  private Supplier<String> safeNameIdSupplier;

  @org.junit.Before
  public void setUp() {
    this.compiler = mock(AbstractCompiler.class);
    this.safeNameIdSupplier = () -> "test_id";
  }

  @org.junit.Test
  public void testInlineObjectLiterals() {
    Scope scope = Scope.createRoot();
    Node root = Node.newProgram(-1, ImmutableList.of());

    ReferenceCollectingCallback callback = new ReferenceCollectingCallback(
        compiler, new InliningBehavior());
    callback.process(scope.getExternsNode(), root);

    List<Reference> references = callback.getReferences();

    for (Reference ref : references) {
      if (ref.getNode().getType() != Token.ASSIGN) continue;
      Node assign = ref.getNode();
      Node left = assign.getFirstChild();
      Node right = assign.getLastChild();
      if (right.getType() != Token.OBJECTLIT) continue;

      InlineObjectLiterals pass = new InlineObjectLiterals(compiler, safeNameIdSupplier);
      pass.process(scope.getExternsNode(), root);

      Node newAssign = NodeUtil.findStatementContainingNode(assign.getFirstChild(), root);
      Node newLeft = newAssign.getFirstChild();
      Node newRight = newAssign.getLastChild();

      // Check that the object literal has been replaced with separate variable assignments.
      assertEquals(newLeft.getChildCount(), right.getChildCount() * 2 + 1);

      int i = 0;
      for (Node property = right.getFirstChild(); property != null;
           property = property.getNext()) {
        String propertyName = property.getString();
        Node propertyValue = property.removeFirstChild();

        Node newPropertyLeft = newLeft.getChildAtIndex(i);
        Node newPropertyRight = newLeft.getChildAtIndex(i + 1);

        assertEquals(newPropertyLeft.getFirstChild().getString(), propertyName);
        assertEquals(newPropertyRight.getType(), Token.ASSIGN);
        assertEquals(newPropertyRight.getLastChild().getType(), Token.NAME);
        assertEquals(newPropertyRight.getLastChild().getString(), propertyValue.getString());

        i++;
      }

      // Check that the initial values have been set correctly.
      Map<String, Node> expectedInitValues = ImmutableMap.of(
          "prop1", propertyValue,
          "prop2", propertyValue
      );
      Map<String, Node> actualInitValues = ImmutableMap.of();
      NodeUtil.visitPreOrder(newRight, new NodeUtil.Visitor<Node, Void>() {
        @Override
        public Void visit(Node node) {
          if (node.getType() != Token.OBJECTLIT) return null;
          for (Node child = node.getFirstChild(); child != null;
               child = child.getNext()) {
            String propertyName = child.getString();
            Node propertyValue = child.removeFirstChild();
            actualInitValues.put(propertyName, propertyValue);
          }
          return null;
        }
      });
      assertEquals(expectedInitValues, actualInitValues);
    }
  }

  private static class InliningBehavior extends ReferenceCollectingCallback.Behavior {

    // Empty for test purposes.

  }
}