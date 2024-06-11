import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CheckLevel.*;
import com.google.javascript.jscomp.*;
import com.google.javascript.rhino.*;

public class CheckGlobalThisTest {

  @org.junit.Test
  public void testCheckGlobalThis() {
    Node node = AstUtil.mkFuncDecl("evil", new Node[0], new Node[0]);
    Node lhs = AstUtil.mkAssign(node, "a", node);
    Node rhs = AstUtil.mkPropertyAccess(AstUtil.mkIdentifier("this"), "useful");
    Node assignment = AstUtil.mkAssign(lhs, rhs);

    Compiler compiler = new Compiler();
    AbstractCompiler abstractCompiler = new AbstractCompiler(compiler, new CheckLevel(WARNING));
    CheckGlobalThis checker = new CheckGlobalThis(abstractCompiler, new CheckLevel(WARNING));

    NodeTraversal traversal = new NodeTraversal(abstractCompiler) {
      @Override
      public boolean shouldTraverse(NodeTraversal t, Node n, Node parent) {
        return checker.shouldTraverse(this, n, parent);
      }

      @Override
      public void visit(NodeTraversal t, Node n, Node parent) {
        checker.visit(this, n, parent);
      }
    };

    traversal.traverse(node);

    assertEquals(0, compiler.getDiagnostics().size());

    // Test for warning
    checker.level = ERROR;
    traversal = new NodeTraversal(abstractCompiler) {
      @Override
      public boolean shouldTraverse(NodeTraversal t, Node n, Node parent) {
        return checker.shouldTraverse(this, n, parent);
      }

      @Override
      public void visit(NodeTraversal t, Node n, Node parent) {
        checker.visit(this, n, parent);
      }
    };

    traversal.traverse(node);

    List<Diagnostic> diagnostics = compiler.getDiagnostics();
    assertEquals(1, diagnostics.size());
    Diagnostic diagnostic = diagnostics.get(0);
    assertEquals("dangerous use of the global 'this' object", diagnostic.getMessage());
  }

  // Add edge cases and error scenarios here
}