import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeTraversal.*;
import static com.google.javascript.jscomp.NodeUtil.*;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.PrepareAst;

import org.junit.Before;
import org.junit.Test;

public class PrepareAstTest {
  private AbstractCompiler compiler;
  private CompilerOptions options;
  private PrepareAst prepareAst;

  @Before
  public void setUp() {
    compiler = new AbstractCompiler();
    options = new CompilerOptions();
    prepareAst = new PrepareAst(compiler);
  }

  @Test
  public void testBasicPrepareAst() {
    String source = "function foo() {}";
    Node root = parser(source, options).getTree();
    prepareAst.process(null, root);
    NodeTraversal.traverse(compiler, root, new PrepareAnnotations());
    // Assertions go here
    assertTrue(root.isFunction());
    assertEquals(0, root.getNumChildren());
  }

  @Test
  public void testPrepareAstWithExterns() {
    String source = "function foo() { export function bar() {} }";
    Node externs = parser(null, options).parseExterns(source);
    Node root = parser("function qux() {}", options).getTree();
    prepareAst.process(externs, root);
    // Assertions go here
    assertTrue(root.getFirstChild().isExport());
  }

  @Test
  public void testPrepareAstWithCheckOnly() {
    String source = "function foo() {}";
    Node root = parser(source, options).getTree();
    PrepareAst prepareAstCheckOnly = new PrepareAst(compiler, true);
    prepareAstCheckOnly.process(null, root);
    try {
      normalizeNodeTypes(root);
      fail("prepareAstCheckOnly should have thrown an exception.");
    } catch (IllegalStateException e) {
      // Expected
    }
  }

  @Test
  public void testPrepareAstWithNormalizeNodeTypes() {
    String source = "if (true) {\n  // do something\n}";
    Node root = parser(source, options).getTree();
    prepareAst.process(null, root);
    NodeTraversal.traverse(compiler, root, new PrepareAnnotations());
    NodeTraversal.traverse(compiler, root, new NodeOperation<Void, Node>() {
      @Override
      public Void apply(Node input) {
        normalizeNodeTypes(input);
        return null;
      }
    });
    // Assertions go here
    assertTrue(root.getFirstChild().isControlStructure());
    assertTrue(root.getFirstChild().getFirstChild().isBlock());
  }

  private void normalizeNodeTypes(Node node) {
    Preconditions.checkState(node != null);
    if (node.isObjectLit()) {
      for (Node key = node.getFirstChild();
           key != null; key = key.getNext()) {
        Node value = key.getFirstChild();
        normalizeObjectLiteralKeyAnnotations(node, key, value);
      }
    }
    for (Node child = node.getFirstChild();
         child != null; child = child.getNext()) {
      normalizeNodeTypes(child);
    }
  }

  private void normalizeObjectLiteralKeyAnnotations(Node objlit, Node key, Node value) {
    Preconditions.checkState(objlit != null);
    Preconditions.checkState(key != null);
    if (key.getJSDocInfo() != null && value.isFunction()) {
      value.setJSDocInfo(key.getJSDocInfo());
    }
  }
}