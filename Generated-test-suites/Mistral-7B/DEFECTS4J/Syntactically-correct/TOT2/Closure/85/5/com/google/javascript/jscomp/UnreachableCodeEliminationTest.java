import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeTraversal.AbstractPostOrderCallback.Scope.ROOT;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerPassManager;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.UnreachableCodeElimination;

import org.junit.Before;
import org.junit.Test;

public class UnreachableCodeEliminationTest {

 private AbstractCompiler compiler;
 private Node rootNode;

 @Before
 public void setup() {
 compiler = new AbstractCompiler();
 rootNode = new Node("function foo() {}");
 }

 @Test
 public void testEmptyFunction() {
 UnreachableCodeElimination elimination = new UnreachableCodeElimination(compiler, false);
 CompilerPassManager passManager = new CompilerPassManager(compiler);
 passManager.addPass(elimination);
 NodeTraversal traversal = new NodeTraversal(compiler, rootNode, passManager);
 NodeTraversal.traverse(compiler, rootNode, traversal);
 assertEquals("", traversal.getRootNode().toString());
 }

 @Test
 public void testBasicControlFlow() {
 Node rootNode = new Node("function foo() {" +
 "  if (true) {" +
 "    return; // Unreachable statement" +
 "  }" +
 "  alert('Hello');" +
 "}");
 UnreachableCodeElimination elimination = new UnreachableCodeElimination(compiler, false);
 CompilerPassManager passManager = new CompilerPassManager(compiler);
 passManager.addPass(elimination);
 NodeTraversal traversal = new NodeTraversal(compiler, rootNode, passManager);
 NodeTraversal.traverse(compiler, rootNode, traversal);
 Node expected = new Node("function foo() { alert('Hello'); }");
 assertEquals(expected.toString(), traversal.getRootNode().toString());
 }

 @Test
 public void testUnreachableStatementsWithSideEffects() {
 Node rootNode = new Node("function foo() {" +
 "  if (true) {" +
 "    return; // Unreachable statement" +
 "  }" +
 "  a.b.MyClass.prototype.propertyName = 42;" + // Side effect
 "}");
 UnreachableCodeElimination elimination = new UnreachableCodeElimination(compiler, false);
 CompilerPassManager passManager = new CompilerPassManager(compiler);
 passManager.addPass(elimination);
 NodeTraversal traversal = new NodeTraversal(compiler, rootNode, passManager);
 NodeTraversal.traverse(compiler, rootNode, traversal);
 assertNotSame(rootNode.toString(), traversal.getRootNode().toString());
 }

 @Test
 public void testRemoveNoOpStatements() {
 Node rootNode = new Node("function foo() {" +
 "  a.b.MyClass.prototype.propertyName;" + // NoOp statement
 "  alert('Hello');" +
 "}");
 UnreachableCodeElimination elimination = new UnreachableCodeElimination(compiler, true);
 CompilerPassManager passManager = new CompilerPassManager(compiler);
 passManager.addPass(elimination);
 NodeTraversal traversal = new NodeTraversal(compiler, rootNode, passManager);
 NodeTraversal.traverse(compiler, rootNode, traversal);
 Node expected = new Node("function foo() { alert('Hello'); }");
 assertEquals(expected.toString(), traversal.getRootNode().toString());
 }

 @Test
 public void testRemoveDeadCodeWithSideEffects() {
 Node rootNode = new Node("function foo() {" +
 "  if (true) {" +
 "    return; // Unreachable statement" +
 "  }" +
 "  a.b.MyClass.prototype.propertyName = 42;" + // Side effect
 "  alert('Hello');" +
 "}");
 UnreachableCodeElimination elimination = new UnreachableCodeElimination(compiler, false);
 elimination.removeNoOpStatements = true;
 CompilerPassManager passManager = new CompilerPassManager(compiler);
 passManager.addPass(elimination);
 NodeTraversal traversal = new NodeTraversal(compiler, rootNode, passManager);
 NodeTraversal.traverse(compiler, rootNode, traversal);
 Node expected = new Node("function foo() { a.b.MyClass.prototype.propertyName = 42; }");
 assertEquals(expected.toString(), traversal.getRootNode().toString());
 }

 @Test(expected = AssertionError.class)
 public void testExceptionThrown() {
 Node rootNode = new Node("function foo() {" +
 "  throw new Error('Error');" + // Exception thrown
 "}");
 UnreachableCodeElimination elimination = new UnreachableCodeElimination(compiler, false);
 CompilerPassManager passManager = new CompilerPassManager(compiler);
 passManager.addPass(elimination);
 NodeTraversal traversal = new NodeTraversal(compiler, rootNode, passManager);
 try {
 NodeTraversal.traverse(compiler, rootNode, traversal);
 } catch (CompilerException e) {
 // Expected
 }
 fail("CompilerException should have been thrown.");
 }

 @Test
 public void testUnconditionalBranching() {
 Node rootNode = new Node("function foo() {" +
 "  if (true) {" +
 "    return; // Unreachable statement" +
 "  }" +
 "  if (false) {" +
 "    break; // Fallthrough, but it's safe to remove" +
 "  }" +
 "  alert('Hello');" +
 "}");
 UnreachableCodeElimination elimination = new UnreachableCodeElimination(compiler, false);
 CompilerPassManager passManager = new CompilerPassManager(compiler);
 passManager.addPass(elimination);
 NodeTraversal traversal = new NodeTraversal(compiler, rootNode, passManager);
 NodeTraversal.traverse(compiler, rootNode, traversal);
 Node expected = new Node("function foo() { alert('Hello'); }");
 assertEquals(expected.toString(), traversal.getRootNode().toString());
 }

 @Test
 public void testUnconditionalBranchingWithUnreachableStatement() {
 Node rootNode = new Node("function foo() {" +
 "  if (true) {" +
 "    return; // Unreachable statement" +
 "  }" +
 "  if (false) {" +
 "    break; // Fallthrough" +
 "  }" +
 "  alert('Hello');" +
 "}");
 UnreachableCodeElimination elimination = new UnreachableCodeElimination(compiler, false);
 elimination.removeNoOpStatements = false; // Fallthrough is not a no-op statement
 CompilerPassManager passManager = new CompilerPassManager(compiler);
 passManager.addPass(elimination);
 NodeTraversal traversal = new NodeTraversal(compiler, rootNode, passManager);
 NodeTraversal.traverse(compiler, rootNode, traversal);
 Node expected = new Node("function foo() { return; }"); // Return statement is replaced by the fallthrough break
 assertEquals(expected.toString(), traversal.getRootNode().toString());
 }

 @Test
 public void testRemoveDeadCodeWithSideEffectsInsideLoop() {
 Node rootNode = new Node("function foo() {" +
 "  var i = 0;" +
 "  if (true) {" +
 "    return; // Unreachable statement" +
 "  }" +
 "  for (; i < 10; i++) {" +
 "    a.b.MyClass.prototype.propertyName = i;" + // Side effect
 "  }" +
 "}");
 UnreachableCodeElimination elimination = new UnreachableCodeElimination(compiler, false);
 elimination.removeNoOpStatements = false; // Side effect is not a no-op statement
 CompilerPassManager passManager = new CompilerPassManager(compiler);
 passManager.addPass(elimination);
 NodeTraversal traversal = new NodeTraversal(compiler, rootNode, passManager);
 NodeTraversal.traverse(compiler, rootNode, traversal);
 Node expected = new Node("function foo() {" +
 "  var i = 0;" +
 "  for (; i < 10; i++) {" +
 "    a.b.MyClass.prototype.propertyName = i;" +
 "  }" +
 "}");
 assertEquals(expected.toString(), traversal.getRootNode().toString());
 }
}