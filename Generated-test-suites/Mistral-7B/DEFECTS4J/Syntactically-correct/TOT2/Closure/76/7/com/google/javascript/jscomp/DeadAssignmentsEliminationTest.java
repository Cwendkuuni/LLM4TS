import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.DeadAssignmentsElimination;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import org.junit.Test;
import static org.junit.Assert.*;

public class DeadAssignmentsEliminationTest {

 private AbstractCompiler compiler;

 @Before
 public void setup() {
 compiler = new AbstractCompiler();
 }

 @Test
 public void testDeadAssignmentsElimination() {
 DeadAssignmentsElimination deadAssignmentsElimination =
 new DeadAssignmentsElimination(compiler);

 // Test a simple dead assignment
 Node root = Node.objectLiteral();
 Node a = Node.newIdentifier("a");
 Node assignA = Node.newAssignment(a, Node.newNumber(1));
 Node assignDeadA = Node.newAssignment(a, Node.newNumber(2));
 root.addChild(assignA);
 root.addChild(assignDeadA);

 NodeTraversal traversal = new NodeTraversal(root, compiler);
 deadAssignmentsElimination.process(null, root);

 // Check that dead assignment has been removed
 assertEquals(1, root.getChildCount());
 assertNull(root.getChild(0).getNext());

 // Test dead assignment in a conditional statement
 Node rootWithCond = Node.objectLiteral();
 Node cond = Node.newBinaryOp(Token.AND, Node.newBoolean(true), Node.newBoolean(false));
 Node a1 = Node.newIdentifier("a");
 Node assignA1 = Node.newAssignment(a1, Node.newNumber(1));
 Node assignDeadA1 = Node.newAssignment(a1, Node.newNumber(2));
 Node ifCond = new Node(Token.IF, cond, Node.newBlock(), Node.newBlock());
 rootWithCond.addChild(ifCond);
 ifCond.addChild(assignA1);
 ifCond.addChild(assignDeadA1);

 NodeTraversal traversalWithCond = new NodeTraversal(rootWithCond, compiler);
 deadAssignmentsElimination.process(null, rootWithCond);

 // Check that dead assignment has been removed
 assertEquals(1, ifCond.getChildCount());
 assertNull(ifCond.getChild(0).getNext());

 // Test dead assignment in a loop
 Node rootWithLoop = Node.objectLiteral();
 Node loop = Node.newExpressionStatement(Node.newAssignment(
 Node.newIdentifier("i"), Node.newNumber(0)));
 Node condLoop = Node.newBinaryOp(Token.LE, Node.newIdentifier("i"), Node.newNumber(1));
 Node loopBody = Node.newExpressionStatement(Node.newAssignment(
 Node.newIdentifier("a"), Node.newIdentifier("a")));
 Node whileLoop = new Node(Token.WHILE, condLoop, loopBody, Node.newBlock());
 rootWithLoop.addChild(whileLoop);
 rootWithLoop.addChild(Node.newAssignment(Node.newIdentifier("a"), Node.newNumber(1)));

 NodeTraversal traversalWithLoop = new NodeTraversal(rootWithLoop, compiler);
 deadAssignmentsElimination.process(null, rootWithLoop);

 // Check that dead assignment inside loop has been removed
 assertEquals(1, whileLoop.getChildCount());
 assertFalse(NodeUtil.has(whileLoop, NodeUtil.getIdentifier("a")));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullCompiler() {
 DeadAssignmentsElimination deadAssignmentsElimination =
 new DeadAssignmentsElimination(null);
 }
}