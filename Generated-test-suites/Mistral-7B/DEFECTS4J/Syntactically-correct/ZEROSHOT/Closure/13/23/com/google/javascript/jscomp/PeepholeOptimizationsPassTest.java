import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeUtil.createTree;
import static com.google.javascript.jscomp.NodeUtil.isNode;
import static com.google.javascript.jscomp.NodeUtil.toSource;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Lists;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.AbstractPeepholeOptimization;
import com.google.javascript.jscomp.AbstractCompiler;

public class PeepholeOptimizationsPassTest {

 private Compiler compiler;
 private PeepholeOptimizationsPass optimizationsPass;

 @Before
 public void setUp() {
 CompilerOptions options = new CompilerOptions();
 compiler = new Compiler(options);

 AbstractPeepholeOptimization constantFolding = new ConstantFolding();
 AbstractPeepholeOptimization deadCodeElimination = new DeadCodeElimination();

 optimizationsPass = new PeepholeOptimizationsPass(compiler, constantFolding, deadCodeElimination);
 }

 @Test
 public void testPeepholeOptimizationsPass() {
 Node root = createTree("function foo(x) { return x + 42; }");
 optimizationsPass.process(null, root);

 Node optimizedRoot = optimizationsPass.getCompiler().getOutput();
 assertEquals("function foo(x) { return 42; }", toSource(optimizedRoot));
 }

 @Test(expected = AssertionError.class)
 public void testOptimizationError() {
 Node root = createTree("function foo(x) { return x ^ 3; }");
 optimizationsPass.process(null, root);
 }

 @Test
 public void testOptimizationVisit() {
 Node root = createTree("function foo(x) { return x + 42; x = 1; }");
 optimizationsPass.process(null, root);

 Node optimizedRoot = optimizationsPass.getCompiler().getOutput();
 assertEquals("function foo(x) { return 42; }", toSource(optimizedRoot));
 }

 @Test
 public void testOptimizationRetraverse() {
 Node root = createTree("function foo(x) { return x + 42; x = 1; }");
 Node firstVisit = root;

 optimizationsPass.process(null, root);
 optimizationsPass.traverse(firstVisit);
 optimizationsPass.traverse(firstVisit);

 Node optimizedRoot = optimizationsPass.getCompiler().getOutput();
 assertEquals("function foo(x) { return 42; }", toSource(optimizedRoot));
 }

 @Test
 public void testOptimizationShouldVisit() {
 Node root = createTree("function foo() { return 1; } function bar() { return foo(); }");
 optimizationsPass.process(null, root);

 Node optimizedRoot = optimizationsPass.getCompiler().getOutput();
 assertEquals("function foo() { return 1; } function bar() { return 1; }", toSource(optimizedRoot));
 }

 @Test
 public void testOptimizationShouldNotVisit() {
 Node root = createTree("function foo() { return 1; } function bar() { return 2; }");
 optimizationsPass.process(null, root);

 Node optimizedRoot = optimizationsPass.getCompiler().getOutput();
 assertEquals("function foo() { return 1; } function bar() { return 2; }", toSource(optimizedRoot));
 }
}