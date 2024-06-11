import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.Token;

import java.util.HashMap;
import java.util.Map;

public class RenameLabelsTest {

  private AbstractCompiler compiler = mock(AbstractCompiler.class);
  private CompilerPass pass = new RenameLabels(compiler);
  private Node root;

  @org.junit.Before
  public void setUp() {
    root = NodeUtil.createTestNode("function foo() { a: { b: break a; } }");
  }

  @org.junit.Test
  public void testRenameLabels() {
    // Test normal case
    NodeTraversal nodeTraversal = mock(NodeTraversal.class);
    when(compiler.getTraverser(root)).thenReturn(nodeTraversal);
    pass.process(null, root);
    assertEquals("function foo() { a: { b: break a; } }", root.toSource());

    // Test label rename
    when(nodeTraversal.getParent(nodeTraversal.getCurrentNode())).thenReturn(null);
    Node label = NodeUtil.createTestNode("a:");
    root.addChild(label);
    pass.process(null, root);
    assertEquals("function foo() { a: { b: break a; } }", root.toSource());
    Node newLabel = root.getChild(0);
    assertEquals("a", newLabel.getString());

    // Test label rename when label is referenced
    Node reference = NodeUtil.createTestNode("break a;");
    root.addChild(reference);
    pass.process(null, root);
    assertEquals("function foo() { a: { b: break a; } }", root.toSource());
    Node newReference = root.getChild(1);
    assertEquals("a", newReference.getString());

    // Test label removal when not referenced
    label.removeChild(label.getLastChild());
    pass.process(null, root);
    assertEquals("function foo() { b: break b; }", root.toSource());

    // Test label renaming for nested labels
    Node nestedLabel = NodeUtil.createTestNode("c: { a: { b: break a; } }");
    root.addChild(nestedLabel);
    pass.process(null, root);
    assertEquals("function foo() { c: { a: { b: break a; } } }", root.toSource());
    Node newNestedLabel = root.getChild(1);
    assertEquals("c", newNestedLabel.getString());
    Node innerLabel = newNestedLabel.getFirstChild();
    assertEquals("a", innerLabel.getString());

    // Test label renaming for labels without a name
    Node unnamedLabel = NodeUtil.createTestNode(": { b: break :; }");
    root.addChild(unnamedLabel);
    pass.process(null, root);
    assertEquals("function foo() { : { b: break :; } }", root.toSource());
    Node newUnnamedLabel = root.getChild(1);
    assertTrue(newUnnamedLabel.isUndefined());

    // Test removing unused labels
    if (pass.removeUnused) {
      label.removeChild(label.getLastChild());
      pass.process(null, root);
      assertEquals("function foo() { b: break b; }", root.toSource());
    }
  }

  @org.junit.Test
  public void testRenameLabelsWithErrors() {
    // Test label rename with keywords
    Node invalidLabel = NodeUtil.createTestNode("if: { b: break if; }");
    root.addChild(invalidLabel);
    try {
      pass.process(null, root);
      fail("Expected CompilerException");
    } catch (CompilerException e) {
      // Expected
    }

    // Test label rename with function name
    Node invalidFunction = NodeUtil.createTestNode("foo: { b: break foo; }");
    root.addChild(invalidFunction);
    try {
      pass.process(null, root);
      fail("Expected CompilerException");
    } catch (CompilerException e) {
      // Expected
    }

    // Test label rename with variable name
    Node invalidVariable = NodeUtil.createTestNode("x: { b: break x; }");
    root.addChild(invalidVariable);
    try {
      pass.process(null, root);
      fail("Expected CompilerException");
    } catch (CompilerException e) {
      // Expected
    }
  }
}