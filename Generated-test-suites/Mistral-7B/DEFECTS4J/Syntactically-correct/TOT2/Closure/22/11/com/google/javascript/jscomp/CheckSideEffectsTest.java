import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.CheckSideEffects;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.SourceFile;

import org.junit.Before;
import org.junit.Test;

public class CheckSideEffectsTest {

  private AbstractCompiler compiler;
  private CheckSideEffects checkSideEffects;
  private NodeFactory nodeFactory;

  @Before
  public void setUp() {
    compiler = new Compiler();
    checkSideEffects = new CheckSideEffects(compiler, CheckLevel.WARNING, false);
    nodeFactory = compiler.getFactory();
  }

  @Test
  public void testCheckSideEffects() {
    SourceFile sourceFile = new SourceFile("CheckSideEffectsTest.js", "source code goes here");
    Node root = nodeFactory.parse(sourceFile);

    checkSideEffects.process(nodeFactory.getExterns(), root);

    NodeTraversal traversal = new NodeTraversal(compiler, root);
    traversal.addCallback(checkSideEffects);
    traversal.traverse();

    Node warning = checkSideEffects.getLastWarning();
    assertFalse(warning == null);
    assertTrue(warning.getJSDocInfo().getDescription().contains("Suspicious code."));
  }

  @Test
  public void testCheckSideEffectsNoWarning() {
    SourceFile sourceFile = new SourceFile("CheckSideEffectsTestNoWarning.js", "source code goes here");
    Node root = nodeFactory.parse(sourceFile);

    checkSideEffects.process(nodeFactory.getExterns(), root);

    NodeTraversal traversal = new NodeTraversal(compiler, root);
    traversal.addCallback(checkSideEffects);
    traversal.traverse();

    Node warning = checkSideEffects.getLastWarning();
    assertNull(warning);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCheckSideEffectsNull() {
    checkSideEffects.process(null, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCheckSideEffectsNullRoot() {
    checkSideEffects.process(nodeFactory.getExterns(), null);
  }

  @Test
  public void testCheckSideEffectsProtectedCode() {
    SourceFile sourceFile = new SourceFile("CheckSideEffectsProtectedCode.js", "source code goes here");
    Node root = nodeFactory.parse(sourceFile);

    checkSideEffects = new CheckSideEffects(compiler, CheckLevel.WARNING, true);
    checkSideEffects.process(nodeFactory.getExterns(), root);

    NodeTraversal traversal = new NodeTraversal(compiler, root);
    traversal.addCallback(checkSideEffects);
    traversal.traverse();

    Node warning = checkSideEffects.getLastWarning();
    assertNull(warning);
  }
}