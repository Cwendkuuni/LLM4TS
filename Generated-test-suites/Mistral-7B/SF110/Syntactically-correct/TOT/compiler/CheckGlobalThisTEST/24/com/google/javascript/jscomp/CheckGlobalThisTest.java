import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Checker;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.rhino.JSDocInfo;

public class CheckGlobalThisTest {

 private AbstractCompiler compiler;
 private Checker checker;

 @Before
 public void setup() {
 compiler = new AbstractCompiler();
 checker = new Checker(new CompilerOptions());
 }

 @Test
 public void testSafeUsageOfThis() {
 Node node = new Node("function safeThis() { return this; }");
 NodeTraversal traversal = new NodeTraversal(compiler, checker);
 traversal.traverse(node);
 assertFalse(traversal.statistics().numErrors > 0);
 }

 @Test
 public void testUnsafeUsageOfThisAsProperty() {
 Node node = new Node("var a = {}; function unsafeThis() { a.globalThis = this; }");
 NodeTraversal traversal = new NodeTraversal(compiler, checker);
 traversal.traverse(node);
 assertTrue(traversal.statistics().numWarnings > 0);
 }

 @Test
 public void testUnsafeUsageOfThisAsFunctionParameter() {
 Node node = new Node("function unsafeFunction(o) { o.globalThis = this; }");
 NodeTraversal traversal = new NodeTraversal(compiler, checker);
 traversal.traverse(node);
 assertTrue(traversal.statistics().numWarnings > 0);
 }

 @Test
 public void testUnsafeUsageOfThisAsFunctionReturnValue() {
 Node node = new Node("function unsafeFunction() { return this; }");
 NodeTraversal traversal = new NodeTraversal(compiler, checker);
 traversal.traverse(node);
 assertTrue(traversal.statistics().numWarnings > 0);
 }

 @Test
 public void testUnsafeUsageOfThisInsidePrototypeFunction() {
 Node node = new Node(
 "function Parent() { this.child = function() { return this; } }", null, null);
 Node childNode = node.getFirstChild().getFirstChild();
 NodeTraversal traversal = new NodeTraversal(compiler, checker);
 traversal.traverse(node);
 assertFalse(traversal.statistics().numWarnings > 0);
 }

 @Test
 public void testUnsafeUsageOfThisInsideConstructorAnnotatedFunction() {
 Node node = new Node(
 "function Parent() { @constructor this.child = function() { return this; } }", null, null);
 Node childNode = node.getFirstChild().getFirstChild();
 NodeTraversal traversal = new NodeTraversal(compiler, checker);
 traversal.traverse(node);
 assertFalse(traversal.statistics().numWarnings > 0);
 }

 @Test
 public void testUnsafeUsageOfThisInsideThisAnnotatedFunction() {
 Node node = new Node(
 "function Parent() { @this this.child = function() { return this; } }", null, null);
 Node childNode = node.getFirstChild().getFirstChild();
 NodeTraversal traversal = new NodeTraversal(compiler, checker);
 traversal.traverse(node);
 assertFalse(traversal.statistics().numWarnings > 0);
 }

 @Test
 public void testUnsafeUsageOfThisInsideFunctionWithNoAnnotation() {
 Node node = new Node(
 "function Parent() { this.child = function() { return this; } }", null, null);
 Node childNode = node.getFirstChild().getFirstChild();
 NodeTraversal traversal = new NodeTraversal(compiler, checker);
 traversal.traverse(node);
 assertTrue(traversal.statistics().numWarnings > 0);
 }

 @Test
 public void testIgnoreUnsafeUsageOfThisInsidePrototypeChain() {
 Node node = new Node(
 "function Child() { this.grandChild = function() { return this; } }", null, null);
 Node grandChildNode = node.getFirstChild().getFirstChild();
 Node parentNode = new Node("function Parent() { this.child = new Child(); }", null, null);
 Node childNode = parentNode.getFirstChild().getFirstChild();
 NodeTraversal traversal = new NodeTraversal(compiler, checker);
 traversal.traverse(parentNode);
 assertFalse(traversal.statistics().numWarnings > 0);
 }
}