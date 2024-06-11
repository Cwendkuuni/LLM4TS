package com.google.javascript.jscomp.test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.google.common.collect.ImmutableList;
import com.google.javascript.jscomp.*;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerPasses;
import com.google.javascript.jscomp.Node;
import com.google.javascript.rhino.NodeOperations;
import org.junit.*;
import org.mockito.*;

public class PeepholeOptimizationsPassTest {
 private AbstractCompiler compiler;
 private AbstractPeepholeOptimization optimization;
 private CompilerPass pass;

 @BeforeClass
 public static void setupClass() {
 CompilerPasses.registerPasses();
 }

 @Before
 public void setUp() {
 compiler = mock(AbstractCompiler.class);
 optimization = mock(AbstractPeepholeOptimization.class);
 when(compiler.addChangeHandler(any(CodeChangeHandler.class))).thenReturn(null);
 when(compiler.removeChangeHandler(any(CodeChangeHandler.class))).thenReturn(null);
 when(compiler.getSourceFile(any(Node.class))).thenReturn(mock(SourceFile.class));
 when(optimization.optimizeSubtree(any(Node.class))).thenReturn(null);
 when(optimization.beginTraversal(any(AbstractCompiler.class))).thenReturn(null);
 when(optimization.endTraversal(any(AbstractCompiler.class))).thenReturn(null);

 pass = new PeepholeOptimizationsPass(compiler, optimization);
 }

 @Test
 public void testProcess_noChangeHandler() {
 Node root = NodeOperations.createRoot();
 pass.process(null, root);
 }

 @Test
 public void testProcess_optimization() {
 Node root = NodeOperations.createRoot();
 when(optimization.optimizeSubtree(any(Node.class))).thenReturn(new Node("result"));
 pass.process(null, root);
 verify(compiler).addChangeHandler(any(CodeChangeHandler.class));
 verify(compiler).removeChangeHandler(any(CodeChangeHandler.class));
 verifyZeroInteractions(compiler);
 }

 @Test
 public void testProcess_optimization_changeHandler() {
 Node root = NodeOperations.createRoot();
 CodeChangeHandler handler = mock(CodeChangeHandler.class);
 when(optimization.optimizeSubtree(any(Node.class))).thenReturn(new Node("result"));
 when(handler.reportChange()).thenReturn(null);

 pass.process(null, root);
 verify(compiler).addChangeHandler(handler);
 verify(handler).reportChange();
 verifyZeroInteractions(compiler);
 }

 @Test
 public void testProcess_multipleOptimizations() {
 Node root = NodeOperations.createRoot();
 when(optimization.optimizeSubtree(any(Node.class))).thenReturn(new Node("result1"));

 AbstractPeepholeOptimization optimization2 = mock(AbstractPeepholeOptimization.class);
 when(optimization2.optimizeSubtree(any(Node.class))).thenReturn(new Node("result2"));

 pass = new PeepholeOptimizationsPass(compiler, optimization, optimization2);

 pass.process(null, root);
 verify(compiler).addChangeHandler(any(CodeChangeHandler.class));
 verify(compiler).removeChangeHandler(any(CodeChangeHandler.class));
 verifyZeroInteractions(compiler);
 }

 @Test
 public void testProcess_multipleOptimizations_changeHandler() {
 Node root = NodeOperations.createRoot();
 CodeChangeHandler handler1 = mock(CodeChangeHandler.class);
 CodeChangeHandler handler2 = mock(CodeChangeHandler.class);

 when(optimization.optimizeSubtree(any(Node.class))).thenReturn(new Node("result1"));
 when(optimization2.optimizeSubtree(any(Node.class))).thenReturn(new Node("result2"));

 when(handler1.reportChange()).thenReturn(null);
 when(handler2.reportChange()).thenReturn(null);

 pass = new PeepholeOptimizationsPass(compiler, optimization, optimization2);

 pass.process(null, root);
 verify(compiler).addChangeHandler(handler1);
 verify(handler1).reportChange();
 verify(compiler).addChangeHandler(handler2);
 verify(handler2).reportChange();
 verifyZeroInteractions(compiler);
 }

 @After
 public void tearDown() {
 reset(compiler, optimization);
 }

 private void verifyZeroInteractions(AbstractCompiler compiler) {
 verifyNoMoreInteractions(compiler);
 }
}