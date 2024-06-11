import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CompilerOptions.*;
import static com.google.javascript.rhino.NodeType.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import com.google.common.collect.ImmutableList;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.AbstractPeepholeOptimization;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.JSErrorReporter;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.rhino.Node;

@RunWith(MockitoJUnitRunner.class)
public class PeepholeOptimizationsPassTest {

 @Mock
 private AbstractCompiler compiler;
 @Mock
 private JSErrorReporter errorReporter;
 @Mock
 private AbstractPeepholeOptimization optimization1;
 @Mock
 private AbstractPeepholeOptimization optimization2;

 @InjectMocks
 private PeepholeOptimizationsPass testee;

 @Before
 public void setUp() {
 CompilerOptions options = CompilerOptions.defaultOptions();
 testee = new PeepholeOptimizationsPass(compiler, optimization1, optimization2);
 }

 @Test
 public void testProcess() {
 Node node = Node.newTextNode("test");
 testee.process(NodeOperations.emptyNode(), node);
 // Do nothing here, as testee should not throw any exception
 }

 @Test(expected = RuntimeException.class)
 public void testProcessWithErrorReporter() {
 testee = new PeepholeOptimizationsPass(compiler, optimization1, optimization2) {
 @Override
 public void endTraversal(AbstractCompiler compiler) {
 throw new RuntimeException("Expected error");
 }
 };
 Node node = Node.newTextNode("test");
 testee.process(NodeOperations.emptyNode(), node);
 }

 @Test
 public void testBeginTraversal() {
 testee.beginTraversal();
 for (AbstractPeepholeOptimization optimization : testee.peepholeOptimizations) {
 Mockito.verify(optimization).beginTraversal(compiler);
 }
 }

 @Test
 public void testEndTraversal() {
 testee.endTraversal();
 for (AbstractPeepholeOptimization optimization : testee.peepholeOptimizations) {
 Mockito.verify(optimization).endTraversal(compiler);
 }
 }

 @Test
 public void testShouldVisitFunctionNode() {
 Node functionNode = NodeOperations.createFunction(null, "test", null, ImmutableList.of(), NodeOperations.createBlock(null, null, null, ImmutableList.of()));
 assertTrue(testee.shouldVisit(functionNode));
 }

 @Test
 public void testShouldVisitScriptNode() {
 Node scriptNode = NodeOperations.createScript(NodeOperations.createText("function test() {}"));
 assertTrue(testee.shouldVisit(scriptNode));
 }

 @Test
 public void testShouldNotVisitNonFunctionNonScriptNode() {
 Node node = NodeOperations.newIntNode(42);
 assertFalse(testee.shouldVisit(node));
 }

 @Test
 public void testShouldNotVisitFunctionNodeWithDisabledTraversalOfChildScopes() {
 testee = new PeepholeOptimizationsPass(compiler, optimization1) {
 @Override
 public void beginTraversal(AbstractCompiler compiler) {
 super.beginTraversal(compiler);
 compiler.setOption(Option.TRAVERSE_CHILD_SCOPES, false);
 }
 };
 Node functionNode = NodeOperations.createFunction(null, "test", null, ImmutableList.of(), NodeOperations.createBlock(null, null, null, ImmutableList.of()));
 assertFalse(testee.shouldVisit(functionNode));
 }

 @Test
 public void testVisitNode() {
 Node originalNode = NodeOperations.createTextNode("originalText");
 Node optimizedNode = NodeOperations.createTextNode("optimizedText");
 when(optimization1.optimizeSubtree(any(Node.class)))
 .thenReturn(optimizedNode);

 testee.visit(originalNode);
 assertEquals(optimizedNode, testee.compiler.getCurrentNode());
 }

 @Test
 public void testShouldRetraverseNode() {
 Node functionNode = NodeOperations.createFunction(null, "test", null, ImmutableList.of(), NodeOperations.createBlock(null, null, null, ImmutableList.of()));
 ScopeState previousState = NodeTraversal.extractScopeState(functionNode);
 testee.traversalState.push();
 assertFalse(testee.shouldRetraverse(functionNode));
 testee.traversalState.pop();

 testee.traversalState.push();
 testee.traversalState.peek().traverseChildScopes = false;
 assertTrue(testee.shouldRetraverse(functionNode));
 }

 @Test
 public void testShouldNotRetraverseNonFunctionOrScriptNode() {
 Node node = NodeOperations.newIntNode(42);
 assertFalse(testee.shouldRetraverse(node));
 }
}