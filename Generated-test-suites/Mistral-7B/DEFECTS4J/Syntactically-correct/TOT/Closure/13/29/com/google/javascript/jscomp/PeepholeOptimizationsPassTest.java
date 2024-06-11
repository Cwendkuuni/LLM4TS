import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.rhino.NodeOperations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class PeepholeOptimizationsPassTest {

  private AbstractCompiler compiler;
  private Node rootNode;
  private CompilerPass pass;

  @Before
  public void setup() {
    compiler = mock(AbstractCompiler.class);
    rootNode = NodeOperations.createRoot();
    pass = new PeepholeOptimizationsPass(compiler, new ConstantFolding(), new UselessCodeRemoval());
  }

  @Test
  public void testProcess() {
    pass.process(null, rootNode);
    assertNotNull(pass.getCompiler());
    assertTrue(pass.getCompiler() instanceof AbstractCompiler);
  }

  @Test
  public void testVisitSimpleStatement() {
    Node simpleStatement = NodeOperations.createBlock(Arrays.asList(
        NodeOperations.createName("x"),
        NodeOperations.createName("="),
        NodeOperations.createNumber(42)
    ));

    pass.traverse(simpleStatement);
    assertEquals(simpleStatement, pass.getCurrentVersionOfNode());
  }

  @Test
  public void testVisitAssignmentExpression() {
    Node assignmentExpression = NodeOperations.createBlock(Arrays.asList(
        NodeOperations.createName("x"),
        NodeOperations.createName("="),
        NodeOperations.createName("y"),
        NodeOperations.createName("+"),
        NodeOperations.createNumber(2)
    ));

    pass.traverse(assignmentExpression);
    assertNull(pass.getCurrentVersionOfNode());
    assertEquals(NodeOperations.createBlock(Arrays.asList(
        NodeOperations.createName("x"),
        NodeOperations.createName("="),
        NodeOperations.createName("y"),
        NodeOperations.createName("+"),
        NodeOperations.createNumber(43)
    )), rootNode.getFirstChild());
  }

  @Test
  public void testVisitFunctionDeclaration() {
    Node functionDeclaration = NodeOperations.createFunction("myFunction",
        Arrays.asList(NodeOperations.createName("x")), Arrays.asList(
            NodeOperations.createName("return"),
            NodeOperations.createNumber(42)
        ));

    pass.traverse(functionDeclaration);
    assertEquals(functionDeclaration, pass.getCurrentVersionOfNode());
  }

  @Test
  public void testVisitFunctionCallExpression() {
    Node functionCallExpression = NodeOperations.createBlock(Arrays.asList(
        NodeOperations.createName("myFunction"),
        NodeOperations.createNumber(2)
    ));

    pass.traverse(functionCallExpression);
    assertNull(pass.getCurrentVersionOfNode());
    assertEquals(NodeOperations.createBlock(Arrays.asList(
        NodeOperations.createName("myFunction"),
        NodeOperations.createNumber(2),
        NodeOperations.createName("return")
    )), rootNode.getFirstChild());
  }

  @Test
  public void testVisitIfStatement() {
    Node ifStatement = NodeOperations.createBlock(Arrays.asList(
        NodeOperations.createIfStatement(NodeOperations.createComparision(
            NodeOperations.createName("x"), NodeOperations.createNumber(10)),
            NodeOperations.createBlock(Arrays.asList(
                NodeOperations.createName("y"),
                NodeOperations.createName("="),
                NodeOperations.createNumber(42)
            )),
            NodeOperations.createElseBlock(NodeOperations.createBlock(Arrays.asList(
                NodeOperations.createName("z"),
                NodeOperations.createName("="),
                NodeOperations.createNumber(43)
            )))
        )
    ));

    pass.traverse(ifStatement);
    assertEquals(ifStatement, pass.getCurrentVersionOfNode());
  }

  @Test
  public void testVisitWhileStatement() {
    Node whileStatement = NodeOperations.createBlock(Arrays.asList(
        NodeOperations.createWhileStatement(NodeOperations.createComparision(
            NodeOperations.createName("x"), NodeOperations.createNumber(10)
        ), NodeOperations.createBlock(Arrays.asList(
            NodeOperations.createName("x"),
            NodeOperations.createName("--")
        ))),
        NodeOperations.createName("x"),
        NodeOperations.createNumber(0)
    ));

    pass.traverse(whileStatement);
    assertEquals(NodeOperations.createBlock(Arrays.asList(
        NodeOperations.createWhileStatement(NodeOperations.createComparision(
            NodeOperations.createName("x"), NodeOperations.createNumber(0)
        ), NodeOperations.createBlock(Arrays.asList(
            NodeOperations.createName("x"),
            NodeOperations.createName("--")
        ))),
        NodeOperations.createName("x")
    )), pass.getCurrentVersionOfNode());
  }

  @Test
  public void testVisitThrowStatement() {
    Node throwStatement = NodeOperations.createBlock(Arrays.asList(
        NodeOperations.createThrowStatement(NodeOperations.createString("Error"))
    ));

    pass.traverse(throwStatement);
    assertNull(pass.getCurrentVersionOfNode());
    assertFalse(rootNode.getFirstChild().isBlock());
  }

  @Test
  public void testVisitTryStatement() {
    Node tryStatement = NodeOperations.createBlock(Arrays.asList(
        NodeOperations.createTryStatement(NodeOperations.createBlock(Arrays.asList(
            NodeOperations.createThrowStatement(NodeOperations.createString("Error"))
        )), NodeOperations.createBlock(Arrays.asList(
            NodeOperations.createName("x")
        )))
    ));

    pass.traverse(tryStatement);
    assertEquals(NodeOperations.createBlock(Arrays.asList(
        NodeOperations.createTryStatement(NodeOperations.createBlock(Arrays.asList()), NodeOperations.createBlock(Arrays.asList(
            NodeOperations.createName("x")
        )))
    )), pass.getCurrentVersionOfNode());
  }

  @Test
  public void testVisitEmptyStatement() {
    Node emptyStatement = NodeOperations.createEmptyStatement();

    pass.traverse(emptyStatement);
    assertEquals(emptyStatement, pass.getCurrentVersionOfNode());
  }

  @Test
  public void testShouldVisitFunction() {
    Node function = NodeOperations.createFunction("myFunction",
        Arrays.asList(), Arrays.asList(NodeOperations.createName("return"), NodeOperations.createNumber(42)));

    assertTrue(pass.shouldVisit(function));
  }

  @Test
  public void testShouldNotVisitFunctionWithChildren() {
    Node functionWithChildren = NodeOperations.createFunction("myFunction",
        Arrays.asList(NodeOperations.createName("x")), Arrays.asList(
            NodeOperations.createName("return"),
            NodeOperations.createName("x"),
            NodeOperations.createNumber(2)
        ));

    assertFalse(pass.shouldVisit(functionWithChildren));
  }

  @Test
  public void testShouldVisitScript() {
    Node script = NodeOperations.createScript("myScript", Arrays.asList(
        NodeOperations.createName("x"),
        NodeOperations.createName("y"),
        NodeOperations.createName("z")
    ));

    assertTrue(pass.shouldVisit(script));
  }

  @Test
  public void testShouldNotVisitScriptWithChildren() {
    Node scriptWithChildren = NodeOperations.createScript("myScript", Arrays.asList(
        NodeOperations.createName("x"),
        NodeOperations.createBlock(Arrays.asList(
            NodeOperations.createName("y"),
            NodeOperations.createName("z")
        ))
    ));

    assertFalse(pass.shouldVisit(scriptWithChildren));
  }

  @Test
  public void testShouldRetraverseFunction() {
    Node function = NodeOperations.createFunction("myFunction",
        Arrays.asList(NodeOperations.createName("x")), Arrays.asList(
            NodeOperations.createName("return"),
            NodeOperations.createName("x"),
            NodeOperations.createNumber(2)
        ));

    pass.traverse(function);
    assertTrue(pass.shouldRetraverse(function));
  }

  @Test
  public void testShouldNotRetraverseFunctionWithTraversalFlagOff() {
    Node function = NodeOperations.createFunction("myFunction",
        Arrays.asList(NodeOperations.createName("x")), Arrays.asList(
            NodeOperations.createName("return"),
            NodeOperations.createName("x"),
            NodeOperations.createNumber(2)
        ));

    pass.traverse(function);
    traversalState.peek().traverseChildScopes = false;
    assertFalse(pass.shouldRetraverse(function));
  }
}