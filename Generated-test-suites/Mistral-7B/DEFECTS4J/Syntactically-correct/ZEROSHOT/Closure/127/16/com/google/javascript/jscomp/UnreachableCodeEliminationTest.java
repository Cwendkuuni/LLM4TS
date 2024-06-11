import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.ControlFlowAnalysis;
import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.GraphReachability;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.CompilerOptions;

public class UnreachableCodeEliminationTest {

 private AbstractCompiler compiler;
 private CompilerOptions options;
 private ControlFlowAnalysis cfa;
 private ControlFlowGraph cfg;

 @Before
 public void setup() {
 compiler = mock(AbstractCompiler.class);
 options = new CompilerOptions();
 cfa = new ControlFlowAnalysis(compiler, false, false);
 }

 @Test
 public void testUnreachableCodeElimination() {
 // Setup UnreachableCodeElimination instance
 UnreachableCodeElimination elimination = new UnreachableCodeElimination(compiler, false);
 options.setRemoveUnreachableCode(true);
 // Setup ControlFlowAnalysis and ControlFlowGraph
 Node root = Node.newBlock("function() { return; a = 1; b = 2; }");
 cfa.process(null, root);
 cfg = cfa.getCfg();
 // Test process method
 elimination.process(Node.ROOT_NODE, root);
 // Assert root node is empty after elimination
 assertEquals(Node.ROOT_NODE, Node.newBlock(""));
 // Test eliminateDeadCode method
 NodeTraversal.traverse(compiler, root, new NodeTraversal.AbstractCallback() {
 @Override
 public void visit(NodeTraversal t, Node n) {
 if (n.isBlock()) {
 assertEquals(2, n.getChildCount());
 return;
 }
 if (n.isNumber()) {
 assertEquals("1", n.toString());
 return;
 }
 if (n.isName()) {
 assertEquals("a", n.toString());
 return;
 }
 if (n.isName("b")) {
 assertEquals("2", n.getFirstChild().toString());
 return;
 }
 fail("Unexpected node encountered: " + n.toString());
 }
 });
 }

 @Test
 public void testRemoveUnconditionalBranching() {
 // Setup UnreachableCodeElimination instance
 UnreachableCodeElimination elimination = new UnreachableCodeElimination(compiler, false);
 options.setRemoveUnreachableCode(true);
 // Setup ControlFlowAnalysis and ControlFlowGraph
 Node root = Node.newBlock("function f() { if (true) { break label1; } label1: continue; }");
 cfa.process(null, root);
 cfg = cfa.getCfg();
 // Test eliminatePass method
 NodeTraversal.traverse(compiler, root, new NodeTraversal.AbstractCallback() {
 @Override
 public void visit(NodeTraversal t, Node n) {
 if (n.isFunction()) {
 assertEquals(0, n.getChildCount());
 return;
 }
 fail("Unexpected node encountered: " + n.toString());
 }
 });
 }

 @Test
 public void testRemoveNoOpStatements() {
 // Setup UnreachableCodeElimination instance
 UnreachableCodeElimination elimination = new UnreachableCodeElimination(compiler, true);
 options.setRemoveUnreachableCode(true);
 // Setup ControlFlowAnalysis and ControlFlowGraph
 Node root = Node.newBlock("function f() { return; a = 1; b = 2; }");
 cfa.process(null, root);
 cfg = cfa.getCfg();
 // Test eliminateDeadCode method
 NodeTraversal.traverse(compiler, root, new NodeTraversal.AbstractCallback() {
 @Override
 public void visit(NodeTraversal t, Node n) {
 if (n.isBlock()) {
 assertEquals(1, n.getChildCount());
 Node firstChild = n.getFirstChild();
 assertEquals("return;", firstChild.toString());
 return;
 }
 fail("Unexpected node encountered: " + n.toString());
 }
 });
 }

 @Test
 public void testRemoveDeadVariableDeclarations() {
 // Setup UnreachableCodeElimination instance
 UnreachableCodeElimination elimination = new UnreachableCodeElimination(compiler, false);
 options.setRemoveUnreachableCode(true);
 // Setup ControlFlowAnalysis and ControlFlowGraph
 Node root = Node.newBlock("var x; function f() { return; }");
 cfa.process(null, root);
 cfg = cfa.getCfg();
 // Test eliminateDeadCode method
 NodeTraversal.traverse(compiler, root, new NodeTraversal.AbstractCallback() {
 @Override
 public void visit(NodeTraversal t, Node n) {
 if (n.isFunction()) {
 assertEquals(1, n.getChildCount());
 Node functionNode = n.getFirstChild();
 assertEquals("function f() { return; }", functionNode.toString());
 return;
 }
 if (n.isVar()) {
 assertEquals("x", n.toString());
 return;
 }
 fail("Unexpected node encountered: " + n.toString());
 }
 });
 }
}