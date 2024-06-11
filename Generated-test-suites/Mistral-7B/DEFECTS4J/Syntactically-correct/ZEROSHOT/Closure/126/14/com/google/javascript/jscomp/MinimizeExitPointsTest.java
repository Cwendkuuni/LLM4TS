package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.Token;

public class MinimizeExitPointsTest {

  @Test
  public void testMinimizeExitPoints() {
    CompilerOptions options = new CompilerOptions();
    options.setWarningLevel(CompilerOptions.WarningLevel.WARNINGS, "none");
    AbstractCompiler compiler = new Compiler(options);

    Node sourceCode = NodeFactory.createProgram();
    NodeTraversal traversal = new NodeTraversal(compiler, sourceCode, null);
    MinimizeExitPoints minimizer = new MinimizeExitPoints(compiler);
    traversal.traverse(minimizer);

    Node expectedResult = NodeFactory.createProgram();
    Node expectedLabel = NodeFactory.createLabel("outerLabel");
    Node expectedIf1 = NodeFactory.createIf(Token.AND,
        NodeFactory.createIdentifier("i"),
        NodeFactory.createLiteral(5),
        NodeFactory.createBlock(
            NodeFactory.createLabel("innerLabel1"),
            NodeFactory.createReturn(Token.VOID)
        ));
    Node expectedIf2 = NodeFactory.createIf(Token.AND,
        NodeFactory.createIdentifier("i"),
        NodeFactory.createLiteral(6),
        NodeFactory.createBlock(
            NodeFactory.createLabel("innerLabel2"),
            NodeFactory.createReturn(Token.VOID)
        ));
    Node expectedBlock = NodeFactory.createBlock(
        NodeFactory.createLabel("outerLabel"),
        NodeFactory.createVariable("i", NodeFactory.createLiteral(3)),
        NodeFactory.createIf(Token.AND, NodeFactory.createIdentifier("i"),
            NodeFactory.createLiteral(4),
            NodeFactory.createBlock(
                NodeFactory.createIdentifier("i"),
                NodeFactory.createIncrement(NodeFactory.createIdentifier("i"))
            )
        ),
        NodeFactory.createBlock(
            expectedIf1,
            NodeFactory.createIdentifier("i"),
            NodeFactory.createIncrement(NodeFactory.createIdentifier("i")),
            expectedLabel,
            NodeFactory.createBreak(Token.BREAK, expectedLabel)
        ),
        NodeFactory.createBlock(
            expectedIf2,
            NodeFactory.createIdentifier("i"),
            NodeFactory.createIncrement(NodeFactory.createIdentifier("i")),
            NodeFactory.createLabel("outerLabel")
        ),
        NodeFactory.createLabel("outerLabel").getFirstChild()
    );

    Node actualResult = sourceCode.getFirstChild();
    assertTrue(NodeUtil.isIdentical(expectedResult, actualResult));
  }
}