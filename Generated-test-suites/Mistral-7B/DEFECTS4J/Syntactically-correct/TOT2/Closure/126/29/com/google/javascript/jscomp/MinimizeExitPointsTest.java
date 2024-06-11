import com.google.javascript.jscomp.MinimizeExitPoints;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.rhino.Node;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class MinimizeExitPointsTest {

 private Compiler compiler;
 private MinimizeExitPoints minimizer;

 @Before
 public void setUp() {
 compiler = mock(Compiler.class);
 minimizer = new MinimizeExitPoints(compiler);
 }

 @Test
 public void testMinimizeExitPointsInSimpleFunction() {
 Node node = Node.newBlock();
 Node returnStatement = Node.newReturn();
 node.addChildToBack(returnStatement);

 when(compiler.reportCodeChange()).thenReturn(node);

 MinimizeExitPoints minimizer = new MinimizeExitPoints(compiler);
 minimizer.process(null, null);
 Node result = node;

 // The return statement should be removed
 assertNull(result.getLastChild());
 }

 @Test
 public void testMinimizeExitPointsInFunctionWithEmptyBlock() {
 Node node = Node.newFunction("test", null, null, Node.newBlock(), null);

 when(compiler.reportCodeChange()).thenReturn(node);

 MinimizeExitPoints minimizer = new MinimizeExitPoints(compiler);
 minimizer.process(null, null);
 Node result = node;

 // The block should be empty
 assertFalse(result.isBlock());
 }

 @Test
 public void testMinimizeExitPointsInFunctionWithSingleStatement() {
 Node node = Node.newFunction("test", null, null, Node.newBlock(), Node.newPrint("hello"));

 when(compiler.reportCodeChange()).thenReturn(node);

 MinimizeExitPoints minimizer = new MinimizeExitPoints(compiler);
 minimizer.process(null, null);
 Node result = node;

 // The print statement should be moved to the implicit return
 assertEquals("hello", result.getLastChild().toString());
 }

 @Test
 public void testMinimizeExitPointsInFunctionWithMultipleStatements() {
 Node node = Node.newFunction("test", null, null, Node.newBlock(),
 Node.newPrint("hello"), Node.newPrint("world"));

 when(compiler.reportCodeChange()).thenReturn(node);

 MinimizeExitPoints minimizer = new MinimizeExitPoints(compiler);
 minimizer.process(null, null);
 Node result = node;

 // The print statements should be merged into a single implicit return
 assertEquals("world", result.getLastChild().toString());
 }

 @Test
 public void testMinimizeExitPointsInControlFlowWithContinue() {
 Node labelNode = Node.newLabel("loopLabel");
 Node loopNode = Node.newFor("i", null, Node.newInt(0), Node.newInt(1), null);
 Node continueNode = Node.newContinue();
 labelNode.addChildToBack(loopNode);
 labelNode.addChildToBack(continueNode);

 when(compiler.reportCodeChange()).thenReturn(labelNode);

 MinimizeExitPoints minimizer = new MinimizeExitPoints(compiler);
 minimizer.process(null, null);
 Node result = labelNode;

 // The continue statement should be removed
 assertNull(result.getLastChild());
 }

 @Test
 public void testMinimizeExitPointsInControlFlowWithBreak() {
 Node labelNode = Node.newLabel("loopLabel");
 Node loopNode = Node.newFor("i", null, Node.newInt(0), Node.newInt(1), null);
 Node breakNode = Node.newBreak("loopLabel");
 labelNode.addChildToBack(loopNode);
 labelNode.addChildToBack(breakNode);

 when(compiler.reportCodeChange()).thenReturn(labelNode);

 MinimizeExitPoints minimizer = new MinimizeExitPoints(compiler);
 minimizer.process(null, null);
 Node result = labelNode;

 // The break statement should be removed
 assertNull(result.getLastChild());
 }

 @Test
 public void testMinimizeExitPointsInControlFlowWithLabeledBreak() {
 Node labelNode1 = Node.newLabel("outsideLabel");
 Node labelNode2 = Node.newLabel("loopLabel");
 Node loopNode = Node.newFor("i", null, Node.newInt(0), Node.newInt(1), null);
 Node breakNode = Node.newBreak("loopLabel");
 labelNode1.addChildToBack(labelNode2);
 labelNode2.addChildToBack(loopNode);
 labelNode2.addChildToBack(breakNode);

 when(compiler.reportCodeChange()).thenReturn(labelNode1);

 MinimizeExitPoints minimizer = new MinimizeExitPoints(compiler);
 minimizer.process(null, null);
 Node result = labelNode1;

 // The break statement should be removed and replaced with an empty block
 assertFalse(result.isBlock());
 }

 @Test(expected = RuntimeException.class)
 public void testMinimizeExitPointsInControlFlowWithMismatchedLabel() {
 Node labelNode1 = Node.newLabel("outsideLabel");
 Node labelNode2 = Node.newLabel("loopLabel");
 Node loopNode = Node.newFor("i", null, Node.newInt(0), Node.newInt(1), null);
 Node breakNode = Node.newBreak("mismatchedLabel");
 labelNode1.addChildToBack(labelNode2);
 labelNode2.addChildToBack(loopNode);
 labelNode2.addChildToBack(breakNode);

 when(compiler.reportCodeChange()).thenReturn(labelNode1);

 MinimizeExitPoints minimizer = new MinimizeExitPoints(compiler);
 minimizer.process(null, null);
 }

 @Test
 public void testMinimizeExitPointsInControlFlowWithNestedLoops() {
 Node labelNode1 = Node.newLabel("outerLoopLabel");
 Node labelNode2 = Node.newLabel("innerLoopLabel");
 Node outerLoop = Node.newFor("i", null, Node.newInt(0), Node.newInt(1), null);
 Node innerLoop = Node.newFor("j", null, Node.newInt(0), Node.newInt(1), null);
 Node continueNode = Node.newContinue();
 Node breakNode1 = Node.newBreak("outerLoopLabel");
 Node breakNode2 = Node.newBreak("innerLoopLabel");
 labelNode1.addChildToBack(labelNode2);
 labelNode2.addChildToBack(outerLoop);
 labelNode2.addChildToBack(innerLoop);
 labelNode2.addChildToBack(continueNode);
 labelNode2.addChildToBack(breakNode1);
 labelNode2.addChildToBack(breakNode2);

 when(compiler.reportCodeChange()).thenReturn(labelNode1);

 MinimizeExitPoints minimizer = new MinimizeExitPoints(compiler);
 minimizer.process(null, null);
 Node result = labelNode1;

 // The continue statement should be removed
 assertNull(result.getLastChild());
 }

 @Test(expected = RuntimeException.class)
 public void testMinimizeExitPointsInControlFlowWithUnmatchedContinue() {
 Node labelNode1 = Node.newLabel("outerLoopLabel");
 Node labelNode2 = Node.newLabel("innerLoopLabel");
 Node outerLoop = Node.newFor("i", null, Node.newInt(0), Node.newInt(1), null);
 Node innerLoop = Node.newFor("j", null, Node.newInt(0), Node.newInt(1), null);
 Node continueNode = Node.newContinue();
 Node breakNode1 = Node.newBreak("outerLoopLabel");
 Node breakNode2 = Node.newBreak("innerLoopLabel");
 labelNode1.addChildToBack(labelNode2);
 labelNode2.addChildToBack(outerLoop);
 labelNode2.addChildToBack(innerLoop);
 labelNode2.addChildToBack(continueNode);
 labelNode2.addChildToBack(breakNode1);
 labelNode2.addChildToBack(breakNode2);

 when(compiler.reportCodeChange()).thenReturn(labelNode1);

 MinimizeExitPoints minimizer = new MinimizeExitPoints(compiler);
 minimizer.process(null, null);
 }

 @Test
 public void testMinimizeExitPointsInTryCatchFinallyBlock() {
 Node tryNode = Node.newTry();
 Node catchNode = Node.newCatch(Node.newIdentifier("e"), Node.newFunction(
 Node.newIdentifier("handleException"), null, null, null, null));
 Node finallyNode = Node.newFinally(Node.newFunction(
 Node.newIdentifier("handleFinally"), null, null, null, null));
 Node returnStatement = Node.newReturn();
 tryNode.addChildToBack(returnStatement);
 tryNode.addChildToBack(catchNode);
 tryNode.addChildToBack(finallyNode);

 when(compiler.reportCodeChange()).thenReturn(tryNode);

 MinimizeExitPoints minimizer = new MinimizeExitPoints(compiler);
 minimizer.process(null, null);
 Node result = tryNode;

 // The return statement should be moved to the end of the finally block
 assertEquals(1, result.getChildCount());
 assertTrue(result.getFirstChild() instanceof Node.Block);
 Node finalBlock = (Node.Block) result.getFirstChild();
 assertEquals(1, finalBlock.getChildCount());
 assertEquals(returnStatement, finalBlock.getLastChild());
 }
}