import com.google.javascript.jscomp.FunctionRewriter;
import com.google.javascript.jscomp.Node;
import com.google.javascript.rhino.NodeTraversal;
import org.junit.Test;
import static org.junit.Assert.*;

public class FunctionRewriterTest {

 private Node createTestNode(String code) {
 return new FunctionRewriter(null).compiler.parse(code, null, null);
 }

 @Test
 public void testEmptyFunctionReducer() {
 Node root = createTestNode("function foo() {}");
 FunctionRewriter functionRewriter = new FunctionRewriter(null);
 functionRewriter.process(null, root);
 Node expected = createTestNode("function foo() {return emptyFn();}");
 assertEquals(expected, root);
 }

 @Test
 public void testIdentityFunctionReducer() {
 Node root = createTestNode("function foo(x) {return x}");
 FunctionRewriter functionRewriter = new FunctionRewriter(null);
 functionRewriter.process(null, root);
 Node expected = createTestNode("function foo(x) {return identityFn(x);}");
 assertEquals(expected, root);
 }

 @Test
 public void testReturnConstantReducer() {
 Node root = createTestNode("function foo() {return 10}");
 FunctionRewriter functionRewriter = new FunctionRewriter(null);
 functionRewriter.process(null, root);
 Node expected = createTestNode("function foo() {return returnconst(10);}");
 assertEquals(expected, root);
 }

 @Test
 public void testGetterReducer() {
 Node root = createTestNode("function foo() {return this.x}");
 FunctionRewriter functionRewriter = new FunctionRewriter(null);
 functionRewriter.process(null, root);
 Node expected = createTestNode("function foo() {return getter(\"x\");}");
 assertEquals(expected, root);
 }

 @Test
 public void testSetterReducer() {
 Node root = createTestNode("function foo(x) {this.x = x}");
 FunctionRewriter functionRewriter = new FunctionRewriter(null);
 functionRewriter.process(null, root);
 Node expected = createTestNode("function foo(x) {setter(\"x\", x);}");
 assertEquals(expected, root);
 }

 @Test
 public void testComplexFunctionReducer() {
 Node root = createTestNode("function foo(x, y) {return x + y * 2}");
 FunctionRewriter functionRewriter = new FunctionRewriter(null);
 functionRewriter.process(null, root);
 Node expected = createTestNode("function foo(x, y) {return returnconst(x + y * 2);}");
 assertEquals(expected, root);
 }

 @Test(expected = AssertionError.class)
 public void testInapplicableFunctionReducer() {
 Node root = createTestNode("function foo() {return this.x + 1}");
 FunctionRewriter functionRewriter = new FunctionRewriter(null);
 functionRewriter.process(null, root);
 fail("Expected an AssertionError, but no error was thrown.");
 }

 @Test
 public void testErrorHandling() {
 Node root = createTestNode("function foo() {throw new Error(\"Expected error\");}");
 FunctionRewriter functionRewriter = new FunctionRewriter(null);
 try {
 functionRewriter.process(null, root);
 fail("Expected an AssertionError, but no error was thrown.");
 } catch (AssertionError e) {
 // Expected error
 }
 }

 @Test
 public void testEdgeCases() {
 Node root = createTestNode("function foo(x, y) {return (x + y) * 2}");
 FunctionRewriter functionRewriter = new FunctionRewriter(null);
 functionRewriter.process(null, root);
 Node expected = createTestNode("function foo(x, y) {return returnconst((x + y) * 2);}");
 assertEquals(expected, root);

 root = createTestNode("function foo(x, y) {return (x + y) * 2; x++;}");
 functionRewriter = new FunctionRewriter(null);
 functionRewriter.process(null, root);
 expected = createTestNode("function foo(x, y) {return returnconst((x + y) * 2); x++;}");
 assertEquals(expected, root);

 root = createTestNode("function foo(x, y) {return (x + y) * (x + y);}");
 functionRewriter = new FunctionRewriter(null);
 functionRewriter.process(null, root);
 expected = createTestNode("function foo(x, y) {return returnconst((x + y) * (x + y));}");
 assertEquals(expected, root);

 root = createTestNode("function foo() {return this.x = 10;}");
 functionRewriter = new FunctionRewriter(null);
 try {
 functionRewriter.process(null, root);
 fail("Expected an AssertionError, but no error was thrown.");
 } catch (AssertionError e) {
 // Expected error
 }
 }
}