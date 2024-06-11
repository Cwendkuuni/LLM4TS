import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.AbstractPeepholeOptimization;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.PeepholeChangeHandler;
import com.google.javascript.jscomp.PeepholeOptimizationsPass;
import com.google.javascript.rhino.NodeFactory;

import org.junit.Before;
import org.junit.Test;

public class PeepholeOptimizationsPassTest {

  private AbstractCompiler compiler;
  private AbstractPeepholeOptimization optimization1;
  private AbstractPeepholeOptimization optimization2;
  private Node rootNode;

  @Before
  public void setUp() {
    compiler = new CompilerOptions().newCompiler(null);
    optimization1 = new DummyPeepholeOptimization();
    optimization2 = new DummyPeepholeOptimization();
    PeepholeOptimizationsPass pass = new PeepholeOptimizationsPass(compiler, optimization1, optimization2);
    rootNode = NodeFactory.createScript("/** @const */ const a = 1;");
  }

  @Test
  public void testPeepholeOptimizationsPass_EmptyAST() {
    pass.process(null, null);
    assertFalse("process should not throw any exception.", pass.getCompiler().getErrorMessages().isEmpty());
  }

  @Test
  public void testPeepholeOptimizationsPass_SimpleNode() {
    pass.process(null, rootNode);
    assertFalse("process should not throw any exception.", pass.getCompiler().getErrorMessages().isEmpty());
    assertNull("rootNode should not be changed.", pass.getCompiler().getASTForSourceUnit(rootNode.getSourceName()));
  }

  @Test
  public void testPeepholeOptimizationsPass_SimpleNodeWithChange() {
    rootNode.setFirstChild(NodeFactory.createNumberLiteral(1));
    pass.process(null, rootNode);
    assertFalse("process should not throw any exception.", pass.getCompiler().getErrorMessages().isEmpty());
    assertNull("rootNode should not be changed.", pass.getCompiler().getASTForSourceUnit(rootNode.getSourceName()));

    Node optimizedNode = pass.getCompiler().getASTForSourceUnit(rootNode.getSourceName());
    assertNotNull("optimizedNode should not be null.", optimizedNode);
    assertNull("optimizedNode should not contain the original Node.", optimizedNode.getFirstChild());
  }

  @Test
  public void testPeepholeOptimizationsPass_ComplexNode() {
    rootNode.setFirstChild(NodeFactory.createCallExpression(
        NodeFactory.createIdentifier("Math"), NodeFactory.createIdentifier("pow"),
        NodeFactory.createNumberLiteral(2), NodeFactory.createNumberLiteral(3)));
    pass.process(null, rootNode);
    assertFalse("process should not throw any exception.", pass.getCompiler().getErrorMessages().isEmpty());
    assertNull("rootNode should not be changed.", pass.getCompiler().getASTForSourceUnit(rootNode.getSourceName()));

    Node optimizedNode = pass.getCompiler().getASTForSourceUnit(rootNode.getSourceName());
    assertNotNull("optimizedNode should not be null.", optimizedNode);
    assertNull("optimizedNode should not contain the original Node.", optimizedNode.getFirstChild());
  }

  @Test
  public void testPeepholeOptimizationsPass_ComplexNodeWithChange() {
    rootNode.setFirstChild(NodeFactory.createCallExpression(
        NodeFactory.createIdentifier("Math"), NodeFactory.createIdentifier("pow"),
        NodeFactory.createNumberLiteral(2), NodeFactory.createNumberLiteral(3)));

    rootNode.getLastChild().setDoubleValue(5.0);
    pass.process(null, rootNode);
    assertFalse("process should not throw any exception.", pass.getCompiler().getErrorMessages().isEmpty());
    assertNull("rootNode should not be changed.", pass.getCompiler().getASTForSourceUnit(rootNode.getSourceName()));

    Node optimizedNode = pass.getCompiler().getASTForSourceUnit(rootNode.getSourceName());
    assertNotNull("optimizedNode should not be null.", optimizedNode);
    assertNull("optimizedNode should not contain the original Node.", optimizedNode.getFirstChild());
    assertNotNull("optimizedNode should contain a new Node.", optimizedNode.getFirstChild());
  }

  @Test
  public void testPeepholeOptimizationsPass_ComplexNodeWithChange_Error() {
    rootNode.setFirstChild(NodeFactory.createCallExpression(
        NodeFactory.createIdentifier("Math"), NodeFactory.createIdentifier("pow"),
        NodeFactory.createNumberLiteral(2), NodeFactory.createNumberLiteral(3)));

    rootNode.getLastChild().setDoubleValue(null);
    try {
      pass.process(null, rootNode);
    } catch (NullPointerException e) {
      // expected
      return;
    }
    assertTrue("process should have thrown a NullPointerException.", true);
  }

  @Test
  public void testPeepholeOptimizationsPass_ComplexNodeWithChange_Error_2() {
    rootNode.setFirstChild(NodeFactory.createCallExpression(
        NodeFactory.createIdentifier("Math"), NodeFactory.createIdentifier("pow"),
        NodeFactory.createNumberLiteral(2), NodeFactory.createNumberLiteral(3)));

    Node errorNode = NodeFactory.createIdentifier("error");
    rootNode.addChild(errorNode);

    try {
      pass.process(null, rootNode);
    } catch (IllegalArgumentException e) {
      // expected
      return;
    }
    assertTrue("process should have thrown an IllegalArgumentException.", true);
  }
}

class DummyPeepholeOptimization implements AbstractPeepholeOptimization {
  @Override
  public Node optimizeSubtree(Node node) {
    return node;
  }

  @Override
  public void beginTraversal(AbstractCompiler compiler) {
  }

  @Override
  public void endTraversal(AbstractCompiler compiler) {
  }
}