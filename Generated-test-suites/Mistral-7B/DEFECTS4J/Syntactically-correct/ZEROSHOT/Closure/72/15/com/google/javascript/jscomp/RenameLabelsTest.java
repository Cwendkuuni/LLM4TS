import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.ScopedCallback;

import java.util.HashMap;
import java.util.Map;

public class RenameLabelsTest {

  private AbstractCompiler compiler;
  private CompilerPass renameLabels;

  @org.junit.Before
  public void setUp() {
    CompilerOptions options = new CompilerOptions();
    compiler = mock(AbstractCompiler.class);
    renameLabels = new RenameLabels(compiler);
  }

  @org.junit.Test
  public void testRenameLabels() {
    String sourceCode =
        "function bar() {" +
        "  a: {" +
        "    b: {" +
        "      foo();" +
        "    }" +
        "  }" +
        "  a: {" +
        "    b: break a;" +
        "  }" +
        "}";

    Node root = NodeUtil.parseJs(sourceCode);
    renameLabels.process(null, root);

    NodeTraversal nodeTraversal = NodeTraversal.create(compiler, root, null);
    ScopedCallback callback = new RenameLabels.ProcessLabels();
    NodeTraversal.traverse(compiler, root, callback);

    Node labelA = root.getFirstChild().getFirstChild();
    Node labelB = labelA.getFirstChild();
    Node breakStatement = root.getLastChild().getLastChild();

    assertEquals("a", labelA.getFirstChild().getString());
    assertEquals("b", labelB.getString());
    assertEquals("b", breakStatement.getFirstChild().getString());
  }

  @org.junit.Test
  public void testRemoveUnusedLabels() {
    String sourceCode = "function bar() {" +
        "  a: {" +
        "    b: {" +
        "      foo();" +
        "    }" +
        "  }" +
        "  a: {" +
        "    b: break a;" +
        "    c: break a;" +
        "  }" +
        "}";

    CompilerOptions options = new CompilerOptions();
    options.setRemoveUnused(true);
    compiler = mock(AbstractCompiler.class);
    renameLabels = new RenameLabels(compiler, new RenameLabels.DefaultNameSupplier(), true);

    Node root = NodeUtil.parseJs(sourceCode);
    renameLabels.process(null, root);

    NodeTraversal nodeTraversal = NodeTraversal.create(compiler, root, null);
    ScopedCallback callback = new RenameLabels.ProcessLabels();
    NodeTraversal.traverse(compiler, root, callback);

    Node labelC = null;
    for (Node child : root.getFirstChild().getChildren()) {
      if (child.getType() == Token.BLOCK && child.getLastChild().getType() == Token.LABEL) {
        Node label = child.getLastChild();
        if ("c".equals(label.getString())) {
          labelC = label;
          break;
        }
      }
    }

    assertEquals(null, labelC);
  }

  @org.junit.Test
  public void testRenameLabelsAcrossFunctionBoundaries() {
    String sourceCode = "function foo() {" +
        "  a: {" +
        "    function bar() {" +
        "      b: break a;" +
        "    }" +
        "  }" +
        "}";

    Node root = NodeUtil.parseJs(sourceCode);
    renameLabels.process(null, root);

    NodeTraversal nodeTraversal = NodeTraversal.create(compiler, root, null);
    ScopedCallback callback = new RenameLabels.ProcessLabels();
    NodeTraversal.traverse(compiler, root, callback);

    Node fooFunction = root.getFirstChild();
    Node barFunction = fooFunction.getLastChild();
    Node labelB = barFunction.getFirstChild().getLastChild();

    assertEquals("a", fooFunction.getName());
    assertEquals("b", labelB.getString());
  }
}