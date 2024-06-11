import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerContext;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.Token;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class RenameLabelsTest {

  @SuppressWarnings("unchecked")
  private AbstractCompiler createCompiler() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    when(compiler.reportCodeChange()).thenReturn(true);
    CompilerContext context = mock(CompilerContext.class);
    when(compiler.getCompilerContext()).thenReturn(context);
    when(context.getOptions()).thenReturn(Collections.emptyMap());
    Node root = mock(Node.class);
    NodeTraversal nodeTraversal = new NodeTraversal(compiler, root);
    return compiler;
  }

  @SuppressWarnings("ResultOfMethodCallIgnored")
  private void testProcessLabels(AbstractCompiler compiler) {
    RenameLabels renameLabels = new RenameLabels(compiler);
    NodeTraversal nodeTraversal = new NodeTraversal(compiler, null);
    Node root = NodeUtil.createRootNode("function foo() { a: break a; }");
    nodeTraversal.traverse(renameLabels, root, null);

    Map<String, LabelInfo> renameMap = renameLabels.new ProcessLabels().namespaceStack.peek().renameMap;
    LabelInfo a = renameMap.get("a");
    assertNotNull(a);
    assertEquals(1, a.id);
    assertFalse(a.referenced);
  }

  @SuppressWarnings("ResultOfMethodCallIgnored")
  private void testProcessLabelsWithReferencedLabel(AbstractCompiler compiler) {
    RenameLabels renameLabels = new RenameLabels(compiler);
    NodeTraversal nodeTraversal = new NodeTraversal(compiler, null);
    Node root = NodeUtil.createRootNode("function foo() { a: break a; a: return; }");
    nodeTraversal.traverse(renameLabels, root, null);

    Map<String, LabelInfo> renameMap = renameLabels.new ProcessLabels().namespaceStack.peek().renameMap;
    LabelInfo a = renameMap.get("a");
    assertNotNull(a);
    assertEquals(1, a.id);
    assertTrue(a.referenced);
  }

  @SuppressWarnings("ResultOfMethodCallIgnored")
  private void testProcessLabelsUnusedLabel(AbstractCompiler compiler) {
    RenameLabels renameLabels = new RenameLabels(compiler);
    NodeTraversal nodeTraversal = new NodeTraversal(compiler, null);
    Node root = NodeUtil.createRootNode("function foo() { a: {}; }");
    nodeTraversal.traverse(renameLabels, root, null);

    Map<String, LabelInfo> renameMap = renameLabels.new ProcessLabels().namespaceStack.peek().renameMap;
    LabelInfo a = renameMap.get("a");
    assertNotNull(a);
    assertEquals(1, a.id);
    assertFalse(a.referenced);

    NodeUtils.assertNodeEquals(
        "function foo() { }",
        compiler.getProcessor().getProcessorResult(
            compiler, root, null).getSourceCode());
  }

  @SuppressWarnings("ResultOfMethodCallIgnored")
  private void testProcessLabelsWithNestedLabels(AbstractCompiler compiler) {
    RenameLabels renameLabels = new RenameLabels(compiler);
    NodeTraversal nodeTraversal = new NodeTraversal(compiler, null);
    Node root = NodeUtil.createRootNode("function foo() {" +
                                        "  a: { b: break a; }" +
                                        "  a: { b: return; }" +
                                        "}");
    nodeTraversal.traverse(renameLabels, root, null);

    Map<String, LabelInfo> renameMap = renameLabels.new ProcessLabels().namespaceStack.peek().renameMap;
    LabelInfo a = renameMap.get("a");
    LabelInfo b = renameMap.get("b");
    assertNotNull(a);
    assertEquals(1, a.id);
    assertEquals(1, b.id);
    assertEquals(0, a.id - b.id);
    assertFalse(a.referenced);
    assertFalse(b.referenced);

    NodeUtils.assertNodeEquals(
        "function foo() {" +
            "  c: { }" +
            "  c: { break c; }" +
            "  c: { return; }" +
        "}",
        compiler.getProcessor().getProcessorResult(
            compiler, root, null).getSourceCode());
  }

  @SuppressWarnings("ResultOfMethodCallIgnored")
  private void testProcessLabelsWithNestedReferencedLabels(AbstractCompiler compiler) {
    RenameLabels renameLabels = new RenameLabels(compiler);
    NodeTraversal nodeTraversal = new NodeTraversal(compiler, null);
    Node root = NodeUtil.createRootNode("function foo() {" +
                                        "  a: { b: break a; }" +
                                        "  a: { b: return; }" +
                                        "  a: { b: break a; }" +
                                        "}");
    nodeTraversal.traverse(renameLabels, root, null);

    Map<String, LabelInfo> renameMap = renameLabels.new ProcessLabels().namespaceStack.peek().renameMap;
    LabelInfo a = renameMap.get("a");
    LabelInfo b = renameMap.get("b");
    assertNotNull(a);
    assertEquals(1, a.id);
    assertEquals(1, b.id);
    assertEquals(0, a.id - b.id);
    assertTrue(a.referenced);
    assertTrue(b.referenced);

    NodeUtils.assertNodeEquals(
        "function foo() {" +
            "  c: { }" +
            "  c: { break c; }" +
            "  c: { break c; }" +
            "}",
        compiler.getProcessor().getProcessorResult(
            compiler, root, null).getSourceCode());
  }

  @SuppressWarnings("ResultOfMethodCallIgnored")
  private void testProcessLabelsWithUnreferencedLabelAndBreak(AbstractCompiler compiler) {
    RenameLabels renameLabels = new RenameLabels(compiler);
    NodeTraversal nodeTraversal = new NodeTraversal(compiler, null);
    Node root = NodeUtil.createRootNode("function foo() {" +
                                        "  a: {}" +
                                        "  b: break a;" +
                                        "}");
    nodeTraversal.traverse(renameLabels, root, null);

    Map<String, LabelInfo> renameMap = renameLabels.new ProcessLabels().namespaceStack.peek().renameMap;
    LabelInfo a = renameMap.get("a");
    LabelInfo b = renameMap.get("b");
    assertNotNull(a);
    assertEquals(1, a.id);
    assertEquals(0, a.id - b.id);
    assertFalse(a.referenced);
    assertNull(b);

    NodeUtils.assertNodeEquals(
        "function foo() { }",
        compiler.getProcessor().getProcessorResult(
            compiler, root, null).getSourceCode());
  }

  @SuppressWarnings("ResultOfMethodCallIgnored")
  private void testProcessLabelsWithUnreferencedLabelAndContinue(AbstractCompiler compiler) {
    RenameLabels renameLabels = new RenameLabels(compiler);
    NodeTraversal nodeTraversal = new NodeTraversal(compiler, null);
    Node root = NodeUtil.createRootNode("function foo() {" +
                                        "  a: {}" +
                                        "  b: continue a;" +
                                        "}");
    nodeTraversal.traverse(renameLabels, root, null);

    Map<String, LabelInfo> renameMap = renameLabels.new ProcessLabels().namespaceStack.peek().renameMap;
    LabelInfo a = renameMap.get("a");
    LabelInfo b = renameMap.get("b");
    assertNotNull(a);
    assertEquals(1, a.id);
    assertEquals(0, a.id - b.id);
    assertFalse(a.referenced);
    assertNull(b);

    NodeUtils.assertNodeEquals(
        "function foo() { }",
        compiler.getProcessor().getProcessorResult(
            compiler, root, null).getSourceCode());
  }

  @SuppressWarnings("ResultOfMethodCallIgnored")
  private void testProcessLabelsWithUnreferencedLabelAndReturn(AbstractCompiler compiler) {
    RenameLabels renameLabels = new RenameLabels(compiler);
    NodeTraversal nodeTraversal = new NodeTraversal(compiler, null);
    Node root = NodeUtil.createRootNode("function foo() {" +
                                        "  a: {}" +
                                        "  return a;" +
                                        "}");
    nodeTraversal.traverse(renameLabels, root, null);

    Map<String, LabelInfo> renameMap = renameLabels.new ProcessLabels().namespaceStack.peek().renameMap;
    LabelInfo a = renameMap.get("a");
    assertNotNull(a);
    assertEquals(1, a.id);
    assertEquals(0, a.id - a.id);
    assertFalse(a.referenced);

    NodeUtils.assertNodeEquals(
        "function foo() { return {}; }",
        compiler.getProcessor().getProcessorResult(
            compiler, root, null).getSourceCode());
  }

  @Test
  public void testProcessLabels() {
    AbstractCompiler compiler = createCompiler();
    testProcessLabels(compiler);
  }

  @Test
  public void testProcessLabelsWithReferencedLabel() {
    AbstractCompiler compiler = createCompiler();
    testProcessLabelsWithReferencedLabel(compiler);
  }

  @Test
  public void testProcessLabelsUnusedLabel() {
    AbstractCompiler compiler = createCompiler();
    testProcessLabelsUnusedLabel(compiler);
  }

  @Test
  public void testProcessLabelsWithNestedLabels() {
    AbstractCompiler compiler = createCompiler();
    testProcessLabelsWithNestedLabels(compiler);
  }

  @Test
  public void testProcessLabelsWithNestedReferencedLabels() {
    AbstractCompiler compiler = createCompiler();
    testProcessLabelsWithNestedReferencedLabels(compiler);
  }

  @Test
  public void testProcessLabelsWithUnreferencedLabelAndBreak() {
    AbstractCompiler compiler = createCompiler();
    testProcessLabelsWithUnreferencedLabelAndBreak(compiler);
  }

  @Test
  public void testProcessLabelsWithUnreferencedLabelAndContinue() {
    AbstractCompiler compiler = createCompiler();
    testProcessLabelsWithUnreferencedLabelAndContinue(compiler);
  }

  @Test
  public void testProcessLabelsWithUnreferencedLabelAndReturn() {
    AbstractCompiler compiler = createCompiler();
    testProcessLabelsWithUnreferencedLabelAndReturn(compiler);
  }

}