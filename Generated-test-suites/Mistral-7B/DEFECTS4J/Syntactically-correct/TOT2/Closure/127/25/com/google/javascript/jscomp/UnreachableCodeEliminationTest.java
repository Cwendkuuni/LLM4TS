import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerResults;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.UnreachableCodeElimination;

public class UnreachableCodeEliminationTest {

 @Test
 public void testUnreachableCodeElimination() {
 Compiler compiler = new Compiler();
 CompilerOptions options = new CompilerOptions();
 CompilerPass pass = new UnreachableCodeElimination(compiler, true);
 options.setCompilerPass(pass.getClass().getSimpleName(), pass);
 CompilerResults result = compiler.compile("test.js", options);

 // Test basic functionality
 Node root = result.getOutput().getBody();
 assertFalse("Unreachable code should not be present.",
 root.hasDescendantsSatisfying(n -> n.isExpressionStatement() &&
 NodeUtil.mayHaveSideEffects(n, compiler) &&
 !GraphReachability.isReachableFromEntry(n, pass.getCfg())));

 // Test edge cases and exception handling
 root.traverse(new NodeTraversal.AbstractVisitor() {
 @Override
 public void visit(NodeTraversal t, Node n, Node parent) {
 if (parent == null || n.isFunction() || n.isScript()) {
 return;
 }
 if (n.isExpressionStatement() &&
 NodeUtil.mayHaveSideEffects(n, compiler) &&
 !GraphReachability.isReachableFromEntry(n, pass.getCfg())) {
 throw new AssertionError("Unreachable code found: " + n);
 }
 }
 });
 }
}