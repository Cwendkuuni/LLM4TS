import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerPassTestCase;
import com.google.javascript.rhino.Node;

public class PeepholeOptimizationsPassTest extends CompilerPassTestCase {

 private PeepholeOptimizationsPass pass;

 @Override
 protected CompilerPass getCompilerPass() {
 return new PeepholeOptimizationsPass(compiler, new ConstantFolding(), new DeadCodeElimination());
 }

 @Override
 protected void setUp() throws Exception {
 super.setUp();
 pass = (PeepholeOptimizationsPass) getCompilerPass();
 }

 /**
 * Test that the pass runs without errors.
 */
 public void testCompilesWithPeepholeOptimizations() {
 Node input = toNode("function foo() { return 1 + 2; }");
 Node output = pass.process(externs, input).getRootNode();
 assertEquals("function foo() { return 3; }", output.toSource());
 }

 /**
 * Test that constant folding is applied correctly.
 */
 public void testConstantFolding() {
 Node input = toNode("function foo() { return 1 + 2; }");
 Node output = pass.process(externs, input).getRootNode();
 assertEquals("function foo() { return 3; }", output.toSource());

 input = toNode("function foo() { return 1 + (2 + 2); }");
 output = pass.process(externs, input).getRootNode();
 assertEquals("function foo() { return 5; }", output.toSource());

 input = toNode("function foo() { return 1 + (2 * 2); }");
 output = pass.process(externs, input).getRootNode();
 assertEquals("function foo() { return 5; }", output.toSource());
 }

 /**
 * Test that dead code elimination is applied correctly.
 */
 public void testDeadCodeElimination() {
 Node input = toNode("function foo() { return 1 + 2; }");
 Node output = pass.process(externs, input).getRootNode();
 assertEquals("function foo() { return 3; }", output.toSource());

 input = toNode("function foo() { return 1 + 2; if (false) {} }");
 output = pass.process(externs, input).getRootNode();
 assertEquals("function foo() { return 3; }", output.toSource());

 input = toNode("function foo() { return 1 + 2; if (true) {} }");
 output = pass.process(externs, input).getRootNode();
 assertEquals("function foo() { return 3; }", output.toSource());
 }

 /**
 * Test that the pass skips nodes that shouldn't be optimized.
 */
 public void testSkipsNodesThatShouldntBeOptimized() {
 Node input = toNode("function foo() { return 1 + 2; }");
 Node output = pass.process(externs, input).getRootNode();
 assertEquals("function foo() { return 1 + 2; }", output.toSource());

 input = toNode("function foo() { var x = 1; return x + 2; }");
 output = pass.process(externs, input).getRootNode();
 assertEquals("function foo() { var x = 1; return x + 2; }", output.toSource());
 }

 /**
 * Test that errors are reported when optimizations fail.
 */
 public void testFailsGracefully() {
 Node input = toNode("function foo() { return undefined; }");
 try {
 pass.process(externs, input);
 fail("Expected a compiler error.");
 } catch (RuntimeException e) {
 // Expected
 }
 }
}