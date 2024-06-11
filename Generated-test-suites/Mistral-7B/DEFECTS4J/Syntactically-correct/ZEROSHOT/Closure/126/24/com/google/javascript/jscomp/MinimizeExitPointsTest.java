package com.google.javascript.jscomp.test;

import static com.google.javascript.jscomp.NodeTraversal.AbstractPostOrderCallback.visit;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.Token;
import com.google.javascript.rhino.jstype.TernaryValue;

/**
 * Test for MinimizeExitPoints class.
 */
public class MinimizeExitPointsTest {

  /** Compiler instance for testing. */
  private AbstractCompiler compiler;

  /**
   * Sets up test environment.
   */
  @Before
  public void setUp() {
    compiler = new Compiler();
  }

  /**
   * Test MinimizeExitPoints class.
   */
  @Test
  public void testMinimizeExitPoints() {
    Node root = NodeFactory.createFunction(Token.FUNCTION, "test", null, null);
    MinimizeExitPoints minimizeExitPoints = new MinimizeExitPoints(compiler);

    Node loop = NodeFactory.createFor(Token.FOR, null, null, null, null, null, null, null);
    Node ifStatement1 = NodeFactory.createIf(Token.IF, null, null, null);
    Node ifBlock1 = NodeFactory.createBlock(Token.BLOCK, null);
    Node returnStatement1 = NodeFactory.createReturn(Token.RETURN, null, null);
    Node ifStatement2 = NodeFactory.createIf(Token.IF, null, null, null);
    Node ifBlock2 = NodeFactory.createBlock(Token.BLOCK, null);
    Node continueStatement = NodeFactory.createContinue(Token.CONTINUE, null);

    ifBlock1.addChildToBack(returnStatement1);
    ifStatement1.setFirstChild(ifBlock1);
    ifStatement1.addChildToBack(NodeFactory.createStatement(Token.EMPTY, null));
    loop.setLastChild(ifStatement1);

    ifBlock2.addChildToBack(continueStatement);
    ifStatement2.setFirstChild(ifBlock2);
    ifStatement2.addChildToBack(NodeFactory.createStatement(Token.EMPTY, null));
    loop.addChildToBack(ifStatement2);

    root.setLastChild(loop);

    // Process test code.
    minimizeExitPoints.process(NodeUtil.createExterns(compiler), root);

    // Check if the test code is as expected.
    Node expectedCode = NodeFactory.createFunction(Token.FUNCTION, "test", null, null);
    Node expectedLoop = NodeFactory.createFor(Token.FOR, null, null, null, null, null, null, null);
    Node expectedIfStatement1 = NodeFactory.createIf(Token.IF, null, null, null);
    Node expectedIfBlock1 = NodeFactory.createBlock(Token.BLOCK, null);
    Node expectedReturnStatement1 = NodeFactory.createReturn(Token.RETURN, null, null);
    Node expectedIfStatement2 = NodeFactory.createIf(Token.IF, null, null, null);
    Node expectedIfBlock2 = NodeFactory.createBlock(Token.BLOCK, null);

    expectedIfBlock1.addChildToBack(expectedReturnStatement1);
    expectedIfStatement1.setFirstChild(expectedIfBlock1);
    expectedIfStatement1.addChildToBack(NodeFactory.createStatement(Token.EMPTY, null));
    expectedLoop.setLastChild(expectedIfStatement1);

    Node currentNode = expectedLoop;
    Node actualNode = minimizeExitPoints.compiler.getCurrentNode();

    // Check if the test code structure is the same.
    while (currentNode != null) {
      assertTrue(currentNode.getType() == actualNode.getType());
      if (currentNode.isBlock()) {
        assertEquals("Block children count mismatch.", currentNode.getNumChildren(), actualNode.getNumChildren());
      }
      currentNode = currentNode.getNext();
      actualNode = actualNode.getNext();
    }

    // Check if the number of explicit exits is minimized.
    int actualNumberOfExits = 0;
    currentNode = minimizeExitPoints.compiler.getCurrentNode();
    while (currentNode != null && currentNode.getType() != Token.FUNCTION) {
      if (currentNode.isBlock()) {
        for (Node blockChild = currentNode.getFirstChild();
            blockChild != null; blockChild = blockChild.getNext()) {
          if (blockChild.isReturn()) {
            actualNumberOfExits++;
          }
        }
      }
      currentNode = currentNode.getNext();
    }

    // Check if the number of explicit exits is minimized to 1.
    assertTrue("Expected one explicit exit in test code, but found " + actualNumberOfExits, actualNumberOfExits == 1);
  }

  /**
   * Checks if two nodes are equal.
   *
   * @param expectedNode The expected node.
   * @param actualNode The actual node.
   */
  private void assertEquals(String message, Node expectedNode, Node actualNode) {
    if (!NodeUtil.isFunctionDeclaration(expectedNode) && !NodeUtil.isFunctionDeclaration(actualNode)) {
      assertEquals("Nodes' types are not equal.", expectedNode.getType(), actualNode.getType());
      assertEquals("Nodes' string representations are not equal.", expectedNode.toString(), actualNode.toString());
    }
  }
}