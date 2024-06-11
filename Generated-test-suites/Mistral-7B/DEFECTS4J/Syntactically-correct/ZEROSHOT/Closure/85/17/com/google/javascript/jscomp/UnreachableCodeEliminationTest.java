import static org.junit.Assert.*;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.AbstractNode;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.rhino.Node;

public class UnreachableCodeEliminationTest {

 private AbstractCompiler compiler;
 private UnreachableCodeElimination elimination;

 @Before
 public void setUp() {
 compiler = new Compiler();
 elimination = new UnreachableCodeElimination(compiler, false);
 }

 @Test
 public void testEmptyStatement() {
 Node root = AbstractNode.createRoot();
 elimination.process(root, root);
 assertTrue(Nodes.isEmpty(root.getFirstChild()));
 }

 @Test
 public void testRemoveDeadBlock() {
 Node root = AbstractNode.createRoot();
 Node block = AbstractNode.createBlock();
 elimination.process(root, root);
 NodeTraversal traversal = compiler.getTraversal();
 traversal.traverse(elimination, root, null);
 Node blockToRemove = block.getFirstChild();
 assertEquals(0, block.getNumberOfChildren());
 assertTrue(blockToRemove.isDead());
 }

 @Test
 public void testRemoveDeadBlockWithChildren() {
 Node root = AbstractNode.createRoot();
 Node block = AbstractNode.createBlock();
 Node statement = AbstractNode.createExpressionStatement(AbstractNode.createIdentifier("x"));
 block.appendChild(statement);
 elimination.process(root, root);
 NodeTraversal traversal = compiler.getTraversal();
 traversal.traverse(elimination, root, null);
 Node blockToRemove = block.getFirstChild();
 assertEquals(0, block.getNumberOfChildren());
 assertTrue(statement.isDead());
 }

 @Test
 public void testRemoveDeadConstantExpression() {
 Node root = AbstractNode.createRoot();
 Node expression = AbstractNode.createConstant(1);
 elimination.process(root, root);
 NodeTraversal traversal = compiler.getTraversal();
 traversal.traverse(elimination, root, null);
 assertTrue(expression.isDead());
 }

 @Test
 public void testRemoveDeadAssignmentExpression() {
 Node root = AbstractNode.createRoot();
 Node assign = AbstractNode.createAssignment(AbstractNode.createIdentifier("x"),
 AbstractNode.createConstant(1));
 elimination.process(root, root);
 NodeTraversal traversal = compiler.getTraversal();
 traversal.traverse(elimination, root, null);
 assertTrue(assign.isDead());
 }

 @Test
 public void testRemoveDeadExpressionStatement() {
 Node root = AbstractNode.createRoot();
 Node expression = AbstractNode.createExpressionStatement(AbstractNode.createIdentifier("x"));
 elimination.process(root, root);
 NodeTraversal traversal = compiler.getTraversal();
 traversal.traverse(elimination, root, null);
 assertTrue(expression.isDead());
 }

 @Test
 public void testRemoveDeadReturnStatement() {
 Node root = AbstractNode.createRoot();
 Node returnStatement = AbstractNode.createReturnStatement(AbstractNode.createIdentifier("x"));
 elimination.process(root, root);
 NodeTraversal traversal = compiler.getTraversal();
 traversal.traverse(elimination, root, null);
 assertTrue(returnStatement.isDead());
 }

 @Test
 public void testRemoveDeadBreakStatement() {
 Node root = AbstractNode.createRoot();
 Node loop = AbstractNode.createForStatement(AbstractNode.createIdentifier("i"),
 AbstractNode.createNumberLiteral(1),
 AbstractNode.createNumberLiteral(10),
 AbstractNode.createExpressionStatement(AbstractNode.createIdentifier("i").set(AbstractNode.createNumberLiteral(2))),
 AbstractNode.createBlock());
 Node breakStatement = AbstractNode.createBreakStatement();
 loop.getLastChild().appendChild(breakStatement);
 elimination.process(root, root);
 NodeTraversal traversal = compiler.getTraversal();
 traversal.traverse(elimination, root, null);
 assertTrue(breakStatement.isDead());
 }

 @Test
 public void testRemoveDeadContinueStatement() {
 Node root = AbstractNode.createRoot();
 Node loop = AbstractNode.createForStatement(AbstractNode.createIdentifier("i"),
 AbstractNode.createNumberLiteral(1),
 AbstractNode.createNumberLiteral(10),
 AbstractNode.createExpressionStatement(AbstractNode.createIdentifier("i").set(AbstractNode.createNumberLiteral(2))),
 AbstractNode.createBlock());
 Node continueStatement = AbstractNode.createContinueStatement();
 loop.getLastChild().appendChild(continueStatement);
 elimination.process(root, root);
 NodeTraversal traversal = compiler.getTraversal();
 traversal.traverse(elimination, root, null);
 assertTrue(continueStatement.isDead());
 }
}