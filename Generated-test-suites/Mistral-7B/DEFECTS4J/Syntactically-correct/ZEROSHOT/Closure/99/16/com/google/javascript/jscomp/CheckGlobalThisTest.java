import static org.junit.Assert.*;
import static com.google.javascript.rhino.NodeType.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.Diagnostics;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.DiagnosticMessageCategory;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

@RunWith(JUnit4.class)
public class CheckGlobalThisTest {

  @Test
  public void testCheckGlobalThis() {
    String jsSource = "function evil() { this.useful = undefined; }";

    CompilerOptions options = new CompilerOptions(null);
    options.setWarningLevel(DiagnosticMessageCategory.CODEGEN, CheckLevel.ERROR);

    AbstractCompiler compiler = new AbstractCompiler(options);

    Diagnostics diagnostics = new Diagnostics();
    Node node = AbstractCompiler.parseCompilationUnit(jsSource, null, compiler);

    NodeTraversal traversal = new NodeTraversal(compiler, null) {
      @Override
      public boolean shouldTraverse(NodeTraversal t, Node n, Node parent) {
        return true;
      }

      @Override
      public void visit(NodeTraversal t, Node n, Node parent) {
        if (n.getType() == THIS && shouldReportThis(n, parent)) {
          fail("Should not reach here.");
        }
      }
    };

    CheckGlobalThis checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    checker.traverse(node, traversal);

    assertTrue(diagnostics.getNumErrors() > 0);
    assertEquals(1, diagnostics.getNumWarnings());
  }

  private boolean shouldReportThis(Node n, Node parent) {
    if (parent != null && NodeUtil.isGet(parent)) {
      return true;
    }

    return false;
  }
}