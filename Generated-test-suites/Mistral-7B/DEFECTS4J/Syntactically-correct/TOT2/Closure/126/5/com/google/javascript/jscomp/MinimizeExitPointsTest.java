import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.MinimizeExitPoints;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.TernaryValue;
import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MinimizeExitPointsTest {

 private AbstractCompiler compiler = mock(AbstractCompiler.class);
 private CompilerPass miner = new MinimizeExitPoints(compiler);

 @Test
 public void testMinimizeExitPointsWithIfStatement() {
 Node root = createTestNode();
 miner.process(null, root);
 verifyResult("if (true) { return; } else { return; }",
     "if (true) { } else { }");
 }

 @Test
 public void testMinimizeExitPointsWithLabelStatement() {
 Node root = createTestNodeWithLabel();
 miner.process(null, root);
 verifyResult("Label1: if (true) break Label1; if (false) break Label1;",
     "Label1: if (true) { } if (false) { }");
 }

 @Test
 public void testMinimizeExitPointsWithForLoop() {
 Node root = createForLoopNode();
 miner.process(null, root);
 verifyResult("for (int i = 0; i < 10; i++) { if (true) return; }",
     "for (int i = 0; i < 10; i++) { }");
 }

 @Test
 public void testMinimizeExitPointsWithWhileLoop() {
 Node root = createWhileLoopNode();
 miner.process(null, root);
 verifyResult("while (true) { if (true) return; }",
     "while (true) { }");
 }

 @Test
 public void testMinimizeExitPointsWithDoWhileLoop() {
 Node root = createDoWhileLoopNode();
 miner.process(null, root);
 verifyResult("do { if (false) break; if (true) return; } while (true);",
     "do { if (false) { } if (true) { } } while (true);");
 }

 @Test
 public void testMinimizeExitPointsWithFunctionReturn() {
 Node root = createFunctionReturnNode();
 miner.process(null, root);
 verifyResult("function foo() { return; }",
     "function foo() { }");
 }

 @Test
 public void testMinimizeExitPointsWithThrowException() {
 Node root = createThrowExceptionNode();
 miner.process(null, root);
 verifyResult("function foo() { throw new Exception(); }",
     "function foo() { }");
 }

 @Test
 public void testMinimizeExitPointsWithFinallyBlock() {
 Node root = createFinallyBlockNode();
 miner.process(null, root);
 verifyResult("function foo() { try { return; } finally { return; } }",
     "function foo() { try { } finally { } }");
 }

 @Test
 public void testMinimizeExitPointsWithTryCatchBlock() {
 Node root = createTryCatchBlockNode();
 miner.process(null, root);
 verifyResult("function foo() { try { return; } catch (Exception e) { } }",
     "function foo() { try { } catch (Exception e) { } }");
 }

 @Test
 public void testMinimizeExitPointsWithNestedIfStatement() {
 Node root = createNestedIfStatementNode();
 miner.process(null, root);
 verifyResult("if (true) { if (true) return; } else { if (false) return; }",
     "if (true) { } else { if (false) { } }");
 }

 @Test
 public void testMinimizeExitPointsWithNestedLabelStatement() {
 Node root = createNestedLabelStatementNode();
 miner.process(null, root);
 verifyResult("Label1: if (true) break Label1; if (false) break Label2;",
     "Label1: if (true) { } if (false) { } Label2: {}");
 }

 @Test
 public void testMinimizeExitPointsWithNestedForLoop() {
 Node root = createNestedForLoopNode();
 miner.process(null, root);
 verifyResult("for (int i = 0; i < 10; i++) { for (int j = 0; j < 10; j++) { if (true) return; } }",
     "for (int i = 0; i < 10; i++) { for (int j = 0; j < 10; j++) { } }");
 }

 @Test
 public void testMinimizeExitPointsWithNestedWhileLoop() {
 Node root = createNestedWhileLoopNode();
 miner.process(null, root);
 verifyResult("while (true) { while (true) { if (true) return; } }",
     "while (true) { while (true) { } }");
 }

 @Test
 public void testMinimizeExitPointsWithNestedDoWhileLoop() {
 Node root = createNestedDoWhileLoopNode();
 miner.process(null, root);
 verifyResult("do { while (true) { if (false) break; if (true) return; } } while (true);",
     "do { while (true) { if (false) { } if (true) { } } } while (true);");
 }

 private Node createTestNode() {
 Node root = Node.newBlock("return;");
 return root;
 }

 private Node createTestNodeWithLabel() {
 Node root = Node.newBlock("Label1: break Label1; if (false) break Label1;");
 return root;
 }

 private Node createForLoopNode() {
 Node root = Node.newBlock("for (int i = 0; i < 10; i++) { return; }");
 return root;
 }

 private Node createWhileLoopNode() {
 Node root = Node.newBlock("while (true) { return; }");
 return root;
 }

 private Node createDoWhileLoopNode() {
 Node root = Node.newBlock("do { return; } while (true);");
 return root;
 }

 private Node createFunctionReturnNode() {
 Node root = Node.newFunction("foo", null, Node.NEW_EXPRESSION, NodeUtil.emptyList());
 root.setBody(Node.newBlock("return;"));
 return root;
 }

 private Node createThrowExceptionNode() {
 Node root = Node.newFunction("foo", null, Node.NEW_EXPRESSION, NodeUtil.emptyList());
 root.setBody(Node.newBlock("throw new Exception();"));
 return root;
 }

 private Node createFinallyBlockNode() {
 Node tryBlock = Node.newBlock("try { return; }");
 Node finallyBlock = Node.newBlock("return;");
 Node root = Node.newBlock("try { } finally { " + finallyBlock.getString() + " }");
 tryBlock.addChildToBack(root);
 root.setParent(tryBlock);
 return root;
 }

 private Node createTryCatchBlockNode() {
 Node tryBlock = Node.newBlock("try { return; }");
 Node catchBlock = Node.newBlock("{}");
 Node root = Node.newBlock("try { } catch (Exception e) { " + catchBlock.getString() + " }");
 tryBlock.addChildToBack(root);
 root.setParent(tryBlock);
 return root;
 }

 private Node createNestedIfStatementNode() {
 Node root = Node.newBlock("if (true) { if (true) return; } else { if (false) return; }");
 return root;
 }

 private Node createNestedLabelStatementNode() {
 Node root = Node.newBlock("Label1: if (true) break Label1; if (false) break Label2;");
 return root;
 }

 private Node createNestedForLoopNode() {
 Node root = Node.newBlock("for (int i = 0; i < 10; i++) { for (int j = 0; j < 10; j++) { if (true) return; } }");
 return root;
 }

 private Node createNestedWhileLoopNode() {
 Node root = Node.newBlock("while (true) { while (true) { if (true) return; } }");
 return root;
 }

 private Node createNestedDoWhileLoopNode() {
 Node root = Node.newBlock("do { while (true) { if (false) break; if (true) return; } } while (true);");
 return root;
 }

 private void verifyResult(String expected, String actual) {
 Node root = Node.parse(expected);
 miner.process(null, root);
 Node result = NodeUtil.getBody(miner.compiler.getRootNode());
 assertEquals(expected, result.toString());
 }
}