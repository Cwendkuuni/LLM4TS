import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Callback;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Token;

import org.junit.Test;

public class CheckGlobalThisTest {

  @Test
  public void testCheckGlobalThis() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CheckLevel level = CheckLevel.WARNING;
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);

    NodeTraversal traversal = mock(NodeTraversal.class);
    Node node = createNode("function evil() { this.useful = undefined; }");

    when(traversal.makeError(node, level, DiagnosticType.warning(null, null))).thenReturn(node);

    checkGlobalThis.visit(traversal, node, null);

    assertTrue(node.getDiagnosticMessages().size() > 0);

    node = createNode("function evil() { this.useful = undefined; }.prototype.method = function() {};");
    checkGlobalThis.visit(traversal, node, null);

    assertFalse(node.getDiagnosticMessages().size() > 0);

    node = createNode("function evil() { }.prototype.method = function() { this.useful = undefined; };");
    checkGlobalThis.visit(traversal, node, null);

    assertFalse(node.getDiagnosticMessages().size() > 0);

    node = createNode("function evil() { this.useful = undefined; function useful() {} }");
    checkGlobalThis.visit(traversal, node, null);

    assertFalse(node.getDiagnosticMessages().size() > 0);

    node = createNode("function evil() { this.useful = undefined; this.useful2 = undefined; }");
    checkGlobalThis.visit(traversal, node, null);

    assertTrue(node.getDiagnosticMessages().size() > 0);

    node = createNode("function evil() { this.useful = undefined; var this = {}; this.useful2 = undefined; }");
    checkGlobalThis.visit(traversal, node, null);

    assertFalse(node.getDiagnosticMessages().size() > 0);
  }

  private Node createNode(String nodeCode) {
    NodeCompilerOptions options = NodeCompilerOptions.getDefault();
    Parser parser = new Parser(options);
    return parser.parse(nodeCode, null, null)[0];
  }
}