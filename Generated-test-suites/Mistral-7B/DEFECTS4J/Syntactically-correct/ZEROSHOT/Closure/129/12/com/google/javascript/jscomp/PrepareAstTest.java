import org.junit.Test;
import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeUtil.*;
import static com.google.common.base.Preconditions.*;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.IR;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeTraversal.Callback;
import com.google.javascript.jscomp.PrepareAnnotations;
import com.google.javascript.jscomp.PrepareAst;

public class PrepareAstTest {

  @Test
  public void testPrepareAst() {
    AbstractCompiler compiler = new AbstractCompiler();
    PrepareAst prepareAst = new PrepareAst(compiler);

    // Test with null externs and root
    prepareAst.process(null, null);

    // Test with null externs and non-null root
    Node root = IR.objectLit().srcref(1);
    prepareAst.process(null, root);

    // Test with non-null externs and null root
    Node externs = IR.objectLit().srcref(1);
    prepareAst.process(externs, null);

    // Test with non-null externs and non-null root
    prepareAst = new PrepareAst(compiler, true);
    root = IR.objectLit().srcref(1);
    prepareAst.process(externs, root);
    checkNodeTypes(root);

    prepareAst = new PrepareAst(compiler, false);
    root = IR.objectLit().srcref(1);
    prepareAst.process(externs, root);
    checkAnnotationsPresent(root);
  }

  // Helper methods

  private void checkNodeTypes(Node node) {
    checkNodeType(node, Node.OBJECT_LITERAL);
    for (Node child = node.getFirstChild(); child != null; child = child.getNext()) {
      checkNodeType(child, Node.KEY_NAME);
      if (child.isObjectLit()) {
        checkNodeTypes(child);
      }
    }
  }

  private void checkNodeType(Node node, int expectedType) {
    int actualType = node.getType();
    assertEquals("Unexpected node type: " + node, expectedType, actualType);
  }

  private void checkAnnotationsPresent(Node node) {
    if (node.isObjectLit()) {
      for (Node key = node.getFirstChild(); key != null; key = key.getNext()) {
        Node value = key.getFirstChild();
        if (value.isFunction()) {
          checkAnnotationPresent(value, "function");
        }
        if (key.getJSDocInfo() != null) {
          checkAnnotationPresent(key, key.getJSDocInfo().getTagName());
        }
        if (value.isObjectLit()) {
          checkAnnotationsPresent(value);
        }
      }
    }
  }

  private void checkAnnotationPresent(Node node, String annotation) {
    String actualAnnotation = node.getJSDocInfo() != null ? node.getJSDocInfo().getTagName() : "";
    assertEquals("Unexpected annotation: " + node, annotation, actualAnnotation);
  }
}

class PrepareNodes implements NodeTraversal.Callback {

  @Override
  public boolean shouldTraverse(NodeTraversal t, Node n, Node parent) {
    if (n.isObjectLit()) {
      markNode(n, "Object Literal");
    } else if (n.isKeyName()) {
      markNode(n, "Key Name");
    } else if (n.isFunction()) {
      markNode(n, "Function");
    }
    return true;
  }

  private void markNode(Node node, String label) {
    System.out.printf("%s: %s%n", label, node);
  }
}