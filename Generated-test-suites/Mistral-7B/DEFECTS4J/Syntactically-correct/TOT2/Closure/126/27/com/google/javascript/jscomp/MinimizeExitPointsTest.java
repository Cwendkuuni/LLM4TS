import com.google.javascript.jscomp.MinimizeExitPoints;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.rhino.Node;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class MinimizeExitPointsTest {

 private Node rootNode;
 private MinimizeExitPoints minimizer;

 @Before
 public void setup() {
 rootNode = new Node("function() {"
 + "var x = 1;"
 + "if (x) return;"
 + "else if (x > 2) return;"
 + "else {"
 + " var y = 2;"
 + "}"
 + "}");
 minimizer = new MinimizeExitPoints(new NoOpCompilerPass());
 }

 @Test
 public void testMinimizeExitPoints() {
 NodeTraversal.traverse(minimizer, rootNode, null);
 assertEquals("function() {"
 + "var x = 1;"
 + "if (x) {};"
 + "if (x > 2) return;", rootNode.toString());
 }

 @Test
 public void testMinimizeExitPointsWithLabel() {
 rootNode = new Node("function foo() {"
 + "label1: for (var i = 0; i < 3; i++) {"
 + " if (i === 1) break label1;"
 + "}"
 + "}"
 + "function() {"
 + "foo();"
 + "}"
 + "");
 minimizer = new MinimizeExitPoints(new NoOpCompilerPass());
 NodeTraversal.traverse(minimizer, rootNode, null);
 assertEquals("function foo() {"
 + "label1: for (var i = 0; i < 3; i++) {"
 + " if (i === 1) {}"
 + "}"
 + "}"
 + "function() {"
 + "foo();", rootNode.toString());
 }

 @Test(expected = RuntimeException.class)
 public void testMinimizeExitPointsWithException() {
 rootNode = new Node("function() {"
 + "try {"
 + " throw new Error('boom');"
 + " } catch (e) {"
 + " if (e.message === 'boom') {"
 + " return;"
 + " }"
 + "}"
 + "}"
 + "");
 minimizer = new MinimizeExitPoints(new NoOpCompilerPass());
 try {
 NodeTraversal.traverse(minimizer, rootNode, null);
 fail("Expected exception but none was thrown.");
 } catch (RuntimeException e) {
 // Expected
 }
 }

 @Test
 public void testMinimizeExitPointsWithNestedIf() {
 rootNode = new Node("function() {"
 + "if (true) {"
 + " if (false) return;"
 + " } else {"
 + " if (false) return;"
 + " }"
 + "}"
 + "");
 minimizer = new MinimizeExitPoints(new NoOpCompilerPass());
 NodeTraversal.traverse(minimizer, rootNode, null);
 assertEquals("function() {"
 + "if (true) {"
 + " } else { }"
 + "}", rootNode.toString());
 }

 @Test
 public void testMinimizeExitPointsWithNestedIfElse() {
 rootNode = new Node("function() {"
 + "if (true) {"
 + " if (false) return;"
 + " } else {"
 + " if (false) return;"
 + " }"
 + "}"
 + "");
 minimizer = new MinimizeExitPoints(new NoOpCompilerPass());
 NodeTraversal.traverse(minimizer, rootNode, null);
 assertEquals("function() {"
 + "if (true) {"
 + " } else { }"
 + "}", rootNode.toString());
 }

 @Test
 public void testMinimizeExitPointsWithNestedIfElseIf() {
 rootNode = new Node("function() {"
 + "if (true) {"
 + " if (false) return;"
 + " } else if (false) {"
 + " return;"
 + " }"
 + "}"
 + "");
 minimizer = new MinimizeExitPoints(new NoOpCompilerPass());
 NodeTraversal.traverse(minimizer, rootNode, null);
 assertEquals("function() {"
 + "if (true) {"
 + " } else if (false) { return; }"
 + "}", rootNode.toString());
 }

 @Test
 public void testMinimizeExitPointsWithNestedIfElseIfElse() {
 rootNode = new Node("function() {"
 + "if (true) {"
 + " if (false) return;"
 + " } else if (false) {"
 + " if (false) return;"
 + " } else {"
 + " return;"
 + " }"
 + "}"
 + "");
 minimizer = new MinimizeExitPoints(new NoOpCompilerPass());
 NodeTraversal.traverse(minimizer, rootNode, null);
 assertEquals("function() {"
 + "if (true) {"
 + " if (false) { return; } }"
 + "else {"
 + " return; }"
 + "}", rootNode.toString());
 }

 @Test
 public void testMinimizeExitPointsWithComplexControlFlow() {
 rootNode = new Node("function() {"
 + "var x = 1;"
 + "if (true) {"
 + " if (x > 1) {"
 + " if (x < 3) {"
 + " return;"
 + " } else {"
 + " if (x > 5) {"
 + " return;"
 + " } } else {"
 + " if (x < 0) {"
 + " return;"
 + " } }"
 + "}"
 + "}"
 + "");
 minimizer = new MinimizeExitPoints(new NoOpCompilerPass());
 NodeTraversal.traverse(minimizer, rootNode, null);
 assertEquals("function() {"
 + "var x = 1;"
 + "if (true) {"
 + " if (x > 1) {"
 + " if (x < 3) { return; } }"
 + " else {"
 + " if (x < 0) { return; } }"
 + " }"
 + "}", rootNode.toString());
 }
}