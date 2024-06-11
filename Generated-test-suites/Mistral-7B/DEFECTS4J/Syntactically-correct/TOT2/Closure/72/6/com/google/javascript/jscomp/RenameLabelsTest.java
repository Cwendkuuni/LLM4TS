import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeTraversal.ScopedCallback;
import com.google.javascript.jscomp.ProcessLabels;
import com.google.javascript.jscomp.RenameLabels;
import com.google.javascript.rhino.Token;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RenameLabelsTest {

  private AbstractCompiler compiler;
  private Node root;
  private RenameLabels renameLabels;

  @org.junit.Before
  public void setUp() {
    compiler = mock(AbstractCompiler.class);
    root = createNode("function test() {\n" +
        "  a: {\n" +
        "    b: break a;\n" +
        "  }\n" +
        "  c: {\n" +
        "    b: break c;\n" +
        "  }\n" +
        "  a: {\n" +
        "    b: break a;\n" +
        "  }\n" +
        "  d: {\n" +
        "    a: break d;\n" +
        "  }\n" +
        "}");
    renameLabels = new RenameLabels(compiler);
  }

  @org.junit.Test
  public void testRenameLabelsWithBasicUsage() {
    renameLabels.process(null, root);
    NodeTraversal nodeTraversal = createNodeTraversal(compiler);
    ProcessLabels processLabels = new ProcessLabels();
    NodeTraversal.traverse(compiler, root, processLabels);

    verifyLabelRenamed("test", "test$1", root);
    verifyLabelRenamed("a", "a$1", root.getChild(0));
    verifyLabelRenamed("b", "b$1", root.getChild(0).getChild(0));
    verifyLabelRenamed("c", "c$1", root.getChild(2));
    verifyLabelRenamed("d", "d$1", root.getChild(3));
  }

  @org.junit.Test
  public void testRenameLabelsWithReusedLabelNames() {
    renameLabels.process(null, root);
    NodeTraversal nodeTraversal = createNodeTraversal(compiler);
    ProcessLabels processLabels = new ProcessLabels();
    NodeTraversal.traverse(compiler, root, processLabels);

    verifyLabelRenamed("test", "test$1", root);
    verifyLabelRenamed("a", "a$1", root.getChild(0));
    verifyLabelRenamed("b", "b$2", root.getChild(0).getChild(0));
    verifyLabelRenamed("c", "c$1", root.getChild(2));
    verifyLabelRenamed("a", "a$2", root.getChild(4));
    verifyLabelRenamed("b", "b$3", root.getChild(4).getChild(0));
    verifyLabelRenamed("d", "d$1", root.getChild(3));
  }

  @org.junit.Test
  public void testRenameLabelsWithUnusedLabel() {
    root = createNode("function test() {\n" +
        "  a: {\n" +
        "    b: break a;\n" +
        "  }\n" +
        "  a: {\n" +
        "    // Unused label\n" +
        "  }\n" +
        "}");

    renameLabels.process(null, root);
    NodeTraversal nodeTraversal = createNodeTraversal(compiler);
    ProcessLabels processLabels = new ProcessLabels();
    NodeTraversal.traverse(compiler, root, processLabels);

    verifyLabelRenamed("test", "test$1", root);
    verifyLabelRenamed("a", "a$1", root.getChild(0));
    verifyLabelRemoved("a", root.getChild(1));
  }

  @org.junit.Test
  public void testRenameLabelsWithConflictingLabelName() {
    root = createNode("function test() {\n" +
        "  label: {\n" +
        "    var label = 0;\n" +
        "    a: break label;\n" +
        "  }\n" +
        "  label: {\n" +
        "    var label = 1;\n" +
        "  }\n" +
        "}");

    renameLabels.process(null, root);
    NodeTraversal nodeTraversal = createNodeTraversal(compiler);
    ProcessLabels processLabels = new ProcessLabels();
    NodeTraversal.traverse(compiler, root, processLabels);

    verifyLabelRenamed("test", "test$1", root);
    verifyLabelRenamed("label", "label$1", root.getChild(0));
    verifyLabelRenamed("label", "label$2", root.getChild(2));
  }

  @org.junit.Test
  public void testRenameLabelsWithNestedLabels() {
    root = createNode("function test() {\n" +
        "  a: {\n" +
        "    b: {\n" +
        "      c: {\n" +
        "        d: break c;\n" +
        "      }\n" +
        "    }\n" +
        "  }\n" +
        "}");

    renameLabels.process(null, root);
    NodeTraversal nodeTraversal = createNodeTraversal(compiler);
    ProcessLabels processLabels = new ProcessLabels();
    NodeTraversal.traverse(compiler, root, processLabels);

    verifyLabelRenamed("test", "test$1", root);
    verifyLabelRenamed("a", "a$1", root.getChild(0));
    verifyLabelRenamed("b", "b$1", root.getChild(0).getChild(0));
    verifyLabelRenamed("c", "c$1", root.getChild(0).getChild(0).getChild(0));
    verifyLabelRenamed("d", "d$1", root.getChild(0).getChild(0).getChild(0).getChild(0));
  }

  @org.junit.Test
  public void testRenameLabelsWithBrokenContinueStatement() {
    root = createNode("function test() {\n" +
        "  a: {\n" +
        "    b: continue a;\n" +
        "  }\n" +
        "}");

    renameLabels.process(null, root);
    NodeTraversal nodeTraversal = createNodeTraversal(compiler);
    ProcessLabels processLabels = new ProcessLabels();
    NodeTraversal.traverse(compiler, root, processLabels);

    verifyLabelRenamed("test", "test$1", root);
    verifyLabelRenamed("a", "a$1", root.getChild(0));
    verifyErrorMessage("Unexpected token 'continue' found at ", root.getChild(0).getChild(0));
  }

  @org.junit.Test
  public void testRenameLabelsWithBrokenBreakStatement() {
    root = createNode("function test() {\n" +
        "  a: {\n" +
        "    break a;\n" +
        "  }\n" +
        "}");

    renameLabels.process(null, root);
    NodeTraversal nodeTraversal = createNodeTraversal(compiler);
    ProcessLabels processLabels = new ProcessLabels();
    NodeTraversal.traverse(compiler, root, processLabels);

    verifyLabelRenamed("test", "test$1", root);
    verifyLabelRenamed("a", "a$1", root.getChild(0));
    verifyErrorMessage("Unexpected token 'break' found at ", root.getChild(0).getChild(0));
  }

  @org.junit.Test
  public void testRenameLabelsWithLabelInCondition() {
    root = createNode("function test() {\n" +
        "  a: {\n" +
        "    if (a) {\n" +
        "      b: break a;\n" +
        "    }\n" +
        "  }\n" +
        "}");

    renameLabels.process(null, root);
    NodeTraversal nodeTraversal = createNodeTraversal(compiler);
    ProcessLabels processLabels = new ProcessLabels();
    NodeTraversal.traverse(compiler, root, processLabels);

    verifyLabelRenamed("test", "test$1", root);
    verifyLabelRenamed("a", "a$1", root.getChild(0));
    verifyLabelRenamed("a", "a$2", root.getChild(1));
    verifyLabelRenamed("b", "b$1", root.getChild(1).getChild(0));
  }

  private void verifyLabelRenamed(String nodeName, String newName, Node node) {
    assertEquals(newName, node.getFirstChild().getString());
  }

  private void verifyLabelRemoved(String labelName, Node node) {
    assertEquals(null, node.getChild(0));
  }

  private void verifyErrorMessage(String message, Node node) {
    when(compiler.reportError).thenReturn(true);
    renameLabels.process(null, root);
    assertEquals(message, compiler.debugLog.getLastLine());
  }

  private Node createNode(String code) {
    return NodeBuilder.createNodeFromString(code, null, null, null);
  }

  private NodeTraversal createNodeTraversal(AbstractCompiler compiler) {
    return NodeTraversal.create(compiler, new ProcessLabels());
  }
}