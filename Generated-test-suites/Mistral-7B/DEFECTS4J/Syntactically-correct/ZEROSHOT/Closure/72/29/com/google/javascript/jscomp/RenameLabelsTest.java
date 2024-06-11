import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.base.Supplier;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerPassManager;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.RenameLabels;
import com.google.javascript.jscomp.ScopedCallback;
import com.google.javascript.jscomp.SupplierUtil;
import com.google.javascript.rhino.Token;

import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class RenameLabelsTest {

  private static final String TEST_CODE = "function foo() {" +
      "  a: { b: break a; } " +
      "  a: { c: continue a; } " +
      "  a: {d: break d; } " +
      "  a: { e: continue e; } " +
      "}";

  @SuppressWarnings("unchecked")
  private AbstractCompiler createCompiler() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    when(compiler.addToDebugLog(anyString())).thenAnswer(invocation -> {});
    return compiler;
  }

  @SuppressWarnings("unchecked")
  private Node createNodeFromString(String code) {
    return Node.fromJsString(code);
  }

  @SuppressWarnings("unchecked")
  private NodeTraversal createNodeTraversal(AbstractCompiler compiler) {
    return new NodeTraversal(compiler, new ScopedCallback() {}, null);
  }

  @SuppressWarnings("unchecked")
  private CompilerPass createPass(AbstractCompiler compiler) {
    return new RenameLabels(compiler);
  }

  @SuppressWarnings("unchecked")
  private Supplier<String> createNameSupplier() {
    return (Supplier<String>) SupplierUtil.createStringSupplier();
  }

  @org.junit.Test
  public void testRenameLabels() {
    AbstractCompiler compiler = createCompiler();
    CompilerPass pass = createPass(compiler);

    Node root = createNodeFromString(TEST_CODE);
    CompilerPassManager passManager = new CompilerPassManager();
    passManager.addPass(pass);
    passManager.process(root, null);

    NodeTraversal nodeTraversal = createNodeTraversal(compiler);
    nodeTraversal.traverse(root, pass);

    Node a = findLabelByName("a", root);
    assertSame("Expected label 'a' to exist", root, a.getParent());

    Node b = findLabelByName("b", root);
    Node c = findLabelByName("c", root);
    Node d = findLabelByName("d", root);
    Node e = findLabelByName("e", root);

    assertSame("Expected label 'b' to be renamed to 'a1'", a, b);
    assertSame("Expected label 'c' to be renamed to 'a2'", a, c);
    assertSame("Expected label 'd' to be removed", null, d);
    assertSame("Expected label 'e' to be removed", null, e);

    assertFalse("Expected no label references after renaming",
        hasLabelReference(root, "a1"));
    assertFalse("Expected no label references after renaming",
        hasLabelReference(root, "a2"));
    assertFalse("Expected no label references after renaming",
        hasLabelReference(root, "a3"));
  }

  private Node findLabelByName(String name, Node root) {
    for (Node child = root.getFirstChild(); child != null;
        child = child.getNextSibling()) {
      if (child.getType() == Token.LABEL && child.getString().equals(name)) {
        return child;
      }
    }
    return null;
  }

  private boolean hasLabelReference(Node root, String name) {
    for (Node child = root.getFirstChild(); child != null;
        child = child.getNextSibling()) {
      if (child.getType() == Token.BREAK || child.getType() == Token.CONTINUE) {
        Node labelNode = child.getFirstChild();
        if (labelNode != null && labelNode.getString().equals(name)) {
          return true;
        }
      }
    }
    return false;
  }
}