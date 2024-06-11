import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Checker;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerPassManager;
import com.google.javascript.jscomp.CheckerFactory;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.rhino.NodeOperations;

import java.util.List;

public class CheckSideEffectsTest {

  private AbstractCompiler compiler;
  private Checker checker;
  private CheckSideEffects checkSideEffects;
  private CompilerPassManager passManager;

  @org.junit.Before
  public void setUp() {
    compiler = mock(Compiler.class);
    checker = mock(Checker.class);
    when(compiler.getChecker()).thenReturn(checker);
    checkSideEffects = new CheckSideEffects(compiler, CheckLevel.WARNING, false);
    passManager = new CompilerPassManager(compiler);
  }

  @org.junit.Test
  public void testProcessNoSideEffect() {
    Node root = NodeOperations.createNodeFromString("var x = 5;");
    passManager.addPass(CheckSideEffects.class);
    NodeTraversal traversal = new NodeTraversal(checkSideEffects, root);
    traversal.traverse();
    assertEquals(0, checkSideEffects.problemNodes.size());
  }

  @org.junit.Test
  public void testProcessSideEffect() {
    Node root = NodeOperations.createNodeFromString("var x = foo();");
    passManager.addPass(CheckSideEffects.class);
    NodeTraversal traversal = new NodeTraversal(checkSideEffects, root);
    traversal.traverse();
    assertEquals(1, checkSideEffects.problemNodes.size());
    assertEquals(root.getFirstChild(), checkSideEffects.problemNodes.get(0));
  }

  @org.junit.Test
  public void testProtectSideEffects() {
    Node root = NodeOperations.createNodeFromString("var x = foo();");
    checkSideEffects.protectSideEffects();
    Node expected = NodeOperations.createNodeFromString("JSCOMPILER_PRESERVE(foo());");
    assertEquals(expected.toString(), root.toString());
  }

  @org.junit.Test
  public void testStripProtection() {
    Node root = NodeOperations.createNodeFromString("JSCOMPILER_PRESERVE(foo());");
    CheckSideEffects.StripProtection stripProtection = new CheckSideEffects.StripProtection(compiler);
    NodeTraversal traversal = new NodeTraversal(stripProtection, root);
    traversal.traverse();
    Node expected = NodeOperations.createNodeFromString("foo();");
    assertEquals(expected.toString(), root.toString());
  }
}