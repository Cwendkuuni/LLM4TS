import org.junit.Test;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeTraversal.ScopedCallback;
import com.google.javascript.jscomp.Supplier;
import com.google.javascript.jscomp.TestCompiler;

import java.util.HashMap;
import java.util.Map;

public class RenameLabelsTest {

  @Test
  public void testRenameLabels() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    Supplier<String> nameSupplier = mock(Supplier.class);
    CompilerPass compilerPass = new RenameLabels(compiler, nameSupplier, true);

    Node node = Node.newBlock("function foo() { a: { b: break a; } }");

    NodeTraversal traversal = new NodeTraversal(node, new EmptyVisitor());
    NodeTraversal.traverse(compiler, node, compilerPass, traversal);

    assertNotNull(compilerPass.process);

    // Check that the label has been renamed
    Node label = node.getChild(0).getFirstChild();
    assertNotNull(label);
    assert label.getType() == Token.LABEL;
    assert label.getString().equals("a");

    Node breakStatement = label.getNextSibling();
    assert breakStatement.getType() == Token.BREAK;
    Node breakName = breakStatement.getFirstChild();
    assert breakName.getType() == Token.IDENT;
    assert breakName.getString().equals("a");
  }

  @Test
  public void testRemoveUnusedLabel() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    Supplier<String> nameSupplier = mock(Supplier.class);
    CompilerPass compilerPass = new RenameLabels(compiler, nameSupplier, true);

    Node node = Node.newBlock("function foo() { a: {} b: {} }");

    NodeTraversal traversal = new NodeTraversal(node, new EmptyVisitor());
    NodeTraversal.traverse(compiler, node, compilerPass, traversal);

    assertNotNull(compilerPass.process);

    // Check that the unused label has been removed
    Node label = node.getChild(0).getFirstChild();
    assertNotNull(label);
    assert label.getType() == Token.LABEL;
    assert label.getParentNode().getChildCount() == 1;
  }

  static class EmptyVisitor implements NodeVisitor {
    @Override
    public void visit(NodeTraversal nodeTraversal, Node node, Node parent) {
    }
  }
}