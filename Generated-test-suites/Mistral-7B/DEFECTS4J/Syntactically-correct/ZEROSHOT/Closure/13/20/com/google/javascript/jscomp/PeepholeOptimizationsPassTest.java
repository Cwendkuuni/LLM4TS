import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static com.google.javascript.jscomp.NodeType.BLOCK;
import static com.google.javascript.jscomp.NodeType.FUNCTION;
import static com.google.javascript.jscomp.NodeType.SCRIPT;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.AbstractPeepholeOptimization;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Traversal;

@RunWith(MockitoJUnitRunner.class)
public class PeepholeOptimizationsPassTest {

 private CompilerPass peepholeOptimizationsPass;
 @Mock private AbstractCompiler compiler;
 @Mock private AbstractPeepholeOptimization optimization1;
 @Mock private AbstractPeepholeOptimization optimization2;

 @Before
 public void setup() {
 Preconditions.checkState(compiler != null, "Compiler is null");
 peepholeOptimizationsPass = new PeepholeOptimizationsPass(compiler, optimization1, optimization2);
 }

 @Test
 public void testProcessEmpty() {
 Node root = Node.ROOT();
 peepholeOptimizationsPass.process(Node.TOP_LEVEL_EXPORTS, root);
 }

 @Test
 public void testProcessWithChanges() {
 Node root = Node.objectLiteral();
 Node func = Node.function("func", new ArrayList<>());
 Node statement = Node.assignment("x", Node.numberLiteral(1));
 func.addChildToEnd(statement);
 root.addChildToEnd(func);

 peepholeOptimizationsPass.process(Node.TOP_LEVEL_EXPORTS, root);

 Traversal traversal = compiler.getTraversal();
 Node actual = traversal.getRoot().getChild(0);
 Node expected = Node.stringLiteral("func() { }");
 assertTrue(actual.getClass().equals(Node.class));
 assertTrue(actual.getFirstChild().getClassName().equals(FUNCTION));
 assertTrue(actual.toString().equals(expected.toString()));
 }

 @Test
 public void testProcessWithoutChanges() {
 Node root = Node.objectLiteral();
 Node func = Node.function("func", new ArrayList<>());
 Node statement1 = Node.assignment("x", Node.numberLiteral(1));
 Node statement2 = Node.assignment("x", Node.numberLiteral(2));
 func.addChildToEnd(statement1);
 func.addChildToEnd(statement2);
 root.addChildToEnd(func);

 peepholeOptimizationsPass.process(Node.TOP_LEVEL_EXPORTS, root);

 Traversal traversal = compiler.getTraversal();
 Node actual = traversal.getRoot().getChild(0);
 Node expected = func;
 assertTrue(actual.getClass().equals(Node.class));
 assertTrue(actual.getFirstChild().getClassName().equals(FUNCTION));
 assertTrue(actual.toString().equals(expected.toString()));
 }

 @Test
 public void testShouldVisitFunction() {
 Node root = Node.function("func", new ArrayList<>());
 assertTrue(peepholeOptimizationsPass.shouldVisit(root));
 }

 @Test
 public void testShouldVisitScript() {
 Node root = Node.script("");
 assertTrue(peepholeOptimizationsPass.shouldVisit(root));
 }

 @Test
 public void testShouldNotVisitRoot() {
 Node root = Node.ROOT();
 assertFalse(peepholeOptimizationsPass.shouldVisit(root));
 }

 @Test
 public void testShouldTraverseChildScopesWhenTrue() {
 Node root = Node.script("");
 Node func = Node.function("func", new ArrayList<>());
 Node statement = Node.assignment("x", Node.numberLiteral(1));
 func.addChildToEnd(statement);
 root.addChildToEnd(func);

 peepholeOptimizationsPass.process(Node.TOP_LEVEL_EXPORTS, root);
 Scope traversalScope = compiler.getCurrentScope();
 assertTrue(traversalScope.getParent().isFunction());
 }

 @Test
 public void testShouldNotTraverseChildScopesWhenFalse() {
 CompilerOptions options = new CompilerOptions();
 options.setOptimizationLevel(Traversal.SimpleCompilationUnit, CompilerOptions.WarningLevel.VERBOSE, 1);
 peepholeOptimizationsPass = new PeepholeOptimizationsPass(compiler, optimization1, optimization2);
 Node root = Node.script("");
 Node func = Node.function("func", new ArrayList<>());
 Node statement = Node.assignment("x", Node.numberLiteral(1));
 func.addChildToEnd(statement);
 root.addChildToEnd(func);

 peepholeOptimizationsPass.process(Node.TOP_LEVEL_EXPORTS, root, options);
 Scope traversalScope = compiler.getCurrentScope();
 assertFalse(traversalScope.getParent().isFunction());
 }
}