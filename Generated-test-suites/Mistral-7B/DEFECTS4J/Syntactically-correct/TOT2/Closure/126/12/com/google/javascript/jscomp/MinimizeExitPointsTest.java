import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.powermock.api.junit5.effects.AnnotationEffect.runBefores;
import static com.google.javascript.rhino.Token.*;

import com.google.common.collect.Lists;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.Token;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.junit5.JUnit5Configuration;
import org.powermock.api.junit5.JUnit5Runner;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit5.PowerMockStandalone;

import java.util.Arrays;
import java.util.List;

@RunWith(JUnit5Runner.class)
@PrepareForTest({MinimizeExitPoints.class})
@JUnit5Configuration(JUnit5Runner.class)
@SuppressWarnings("unchecked")
public class MinimizeExitPointsTest {

 private static AbstractCompiler compiler;
 private static NodeFactory nodeFactory;

 @BeforeClass
 public static void setup() {
 compiler = new AbstractCompiler();
 nodeFactory = new NodeFactory();
 PowerMockStandalone.start();
 }

 @Test
 public void testMinimizeExitPoints() {
 MinimizeExitPoints minimizeExitPoints = new MinimizeExitPoints(compiler);

 // Test empty class
 Node root = nodeFactory.createBlock("{}");
 NodeTraversal.traverse(compiler, root, minimizeExitPoints);
 assertFalse(root.hasChildren());

 // Test label
 Node labelNode = nodeFactory.createLabel("label1", null);
 Node exitNode = nodeFactory.createBreakStatement();
 Node blockNode = nodeFactory.createBlock("{label1: {}; label1: {}}");
 blockNode.addChildToBack(labelNode);
 blockNode.addChildToBack(exitNode);
 Node entryNode = nodeFactory.createExpressionStatement(
     nodeFactory.createAssign(nodeFactory.createPropertyAccess(labelNode, "label1"),
         nodeFactory.createNumberLiteral(1)));
 blockNode.setFirstChild(entryNode);
 NodeTraversal.traverse(compiler, blockNode, minimizeExitPoints);
 assertFalse(blockNode.hasChildren());

 // Test continue
 Node continueNode = nodeFactory.createContinueStatement();
 Node labelContinueNode = nodeFactory.createLabel("label1");
 Node forNode = nodeFactory.createForStatement(
     null, null, null, null, null, null, null, null, null, null);
 Node conditionNode = nodeFactory.createConditionalExpression(
     nodeFactory.createBooleanLiteral(true), new IR(1), nodeFactory.createNull());
 Node loopBlockNode = nodeFactory.createBlock("{}, {}");
 Node loopCodeBlockNode = loopBlockNode.getFirstChild();
 forNode.setFirstChild(new IR(2));
 forNode.setLastChild(loopBlockNode);
 loopCodeBlockNode.addChildToBack(continueNode);
 loopCodeBlockNode.addChildToBack(nodeFactory.createExpressionStatement(
     nodeFactory.createAssign(nodeFactory.createPropertyAccess(labelContinueNode, "label1"),
         nodeFactory.createNumberLiteral(1))));
 Node blockBeforeForNode = nodeFactory.createBlock("{}");
 Node blockBeforeForNodeParent = nodeFactory.createBlock("{}", null);
 blockBeforeForNodeParent.setFirstChild(blockBeforeForNode);
 forNode.setFirstChild(blockBeforeForNodeParent);
 Node rootWithForNode = nodeFactory.createBlock("{}", null);
 rootWithForNode.setFirstChild(forNode);
 NodeTraversal.traverse(compiler, rootWithForNode, minimizeExitPoints);
 assertFalse(rootWithForNode.hasChildren());

 // Test return
 Node returnNode = nodeFactory.createReturnStatement(
     nodeFactory.createObjectLiteral(Arrays.asList(new IR(1))));
 Node functionNode = nodeFactory.createFunctionDeclaration("test", null, null, Arrays.asList(
     new IR(1)), null, null, null, null, null, nodeFactory.createBlock("{return;}", null));
 Node functionNodeParent = nodeFactory.createBlock("{}", null);
 functionNodeParent.setFirstChild(functionNode);
 Node rootWithFunctionNode = nodeFactory.createBlock("{}", null);
 rootWithFunctionNode.setFirstChild(functionNodeParent);
 NodeTraversal.traverse(compiler, rootWithFunctionNode, minimizeExitPoints);
 assertFalse(rootWithFunctionNode.hasChildren());

 // Test break without label
 Node breakWithoutLabelNode = nodeFactory.createBreakStatement();
 Node forBreakWithoutLabelNode = nodeFactory.createForStatement(
     null, null, null, null, null, null, null, null, null, null);
 Node loopBlockWithoutLabelNode = forBreakWithoutLabelNode.getLastChild();
 loopBlockWithoutLabelNode.addChildToBack(breakWithoutLabelNode);
 Node blockWithForBreakWithoutLabelNode = nodeFactory.createBlock("{}", null);
 blockWithForBreakWithoutLabelNode.setFirstChild(forBreakWithoutLabelNode);
 Node rootWithBreakWithoutLabelNode = nodeFactory.createBlock("{}", null);
 rootWithBreakWithoutLabelNode.setFirstChild(blockWithForBreakWithoutLabelNode);
 NodeTraversal.traverse(compiler, rootWithBreakWithoutLabelNode, minimizeExitPoints);
 assertFalse(rootWithBreakWithoutLabelNode.hasChildren());

 // Test break with label
 Node labelBreakNode = nodeFactory.createLabel("label1");
 Node breakWithLabelNode = nodeFactory.createBreakStatement(labelBreakNode);
 Node forBreakWithLabelNode = nodeFactory.createForStatement(
     null, null, null, null, null, null, null, null, null, null);
 Node loopBlockWithLabelNode = forBreakWithLabelNode.getLastChild();
 loopBlockWithLabelNode.addChildToBack(labelBreakNode);
 loopBlockWithLabelNode.addChildToBack(breakWithLabelNode);
 Node blockWithForBreakWithLabelNode = nodeFactory.createBlock("{}", null);
 blockWithForBreakWithLabelNode.setFirstChild(forBreakWithLabelNode);
 Node rootWithBreakWithLabelNode = nodeFactory.createBlock("{}", null);
 rootWithBreakWithLabelNode.setFirstChild(blockWithForBreakWithLabelNode);
 NodeTraversal.traverse(compiler, rootWithBreakWithLabelNode, minimizeExitPoints);
 assertFalse(rootWithBreakWithLabelNode.hasChildren());

 // Test try-catch
 Node tryNode = nodeFactory.createTryStatement(null, null, null);
 Node catchNode = nodeFactory.createCatchClause(null, null, null);
 Node finallyNode = nodeFactory.createFinallyClause(null);
 Node tryCatchFinallyNode = nodeFactory.createTryCatchFinallyStatement(
     null, null, tryNode, catchNode, finallyNode);
 Node blockWithTryCatchFinallyNode = nodeFactory.createBlock("{}", null);
 blockWithTryCatchFinallyNode.setFirstChild(tryCatchFinallyNode);
 Node rootWithTryCatchFinallyNode = nodeFactory.createBlock("{}", null);
 rootWithTryCatchFinallyNode.setFirstChild(blockWithTryCatchFinallyNode);
 NodeTraversal.traverse(compiler, rootWithTryCatchFinallyNode, minimizeExitPoints);
 assertFalse(rootWithTryCatchFinallyNode.hasChildren());

 // Test try-catch with return
 Node returnInTryNode = nodeFactory.createReturnStatement(
     nodeFactory.createObjectLiteral(Arrays.asList(new IR(1))));
 Node catchReturnNode = nodeFactory.createReturnStatement(
     nodeFactory.createObjectLiteral(Arrays.asList(new IR(2))));
 Node tryCatchNode = nodeFactory.createTryCatchStatement(
     null, null, null, null, null, null, null);
 Node tryBlockNodeTryCatch = tryCatchNode.getLastChild();
 tryBlockNodeTryCatch.addChildToBack(returnInTryNode);
 Node catchBlockNodeTryCatch = tryCatchNode.getFirstChild();
 catchBlockNodeTryCatch.addChildToBack(catchReturnNode);
 Node tryCatchFinallyNode = nodeFactory.createTryFinallyStatement(
     null, null, tryCatchNode, null);
 Node blockWithTryCatchFinallyNode = nodeFactory.createBlock("{}", null);
 blockWithTryCatchFinallyNode.setFirstChild(tryCatchFinallyNode);
 Node rootWithTryCatchFinallyNode = nodeFactory.createBlock("{}", null);
 rootWithTryCatchFinallyNode.setFirstChild(blockWithTryCatchFinallyNode);
 NodeTraversal.traverse(compiler, rootWithTryCatchFinallyNode, minimizeExitPoints);
 assertFalse(rootWithTryCatchFinallyNode.hasChildren());

 // Test try-catch with throw
 Node throwInTryNode = nodeFactory.createThrowStatement(
     nodeFactory.createName("Error"));
 Node catchThrowNode = nodeFactory.createCatchClause(
     "Error", null, nodeFactory.createExpressionStatement(
         nodeFactory.createAssign(nodeFactory.createPropertyAccess(nodeFactory.createThis(), "message"),
             nodeFactory.createStringLiteral("test"))));
 Node tryCatchNode = nodeFactory.createTryCatchStatement(
     null, null, null, null, null, null, null);
 Node tryBlockNodeTryCatch = tryCatchNode.getLastChild();
 tryBlockNodeTryCatch.addChildToBack(throwInTryNode);
 Node catchBlockNodeTryCatch = tryCatchNode.getFirstChild();
 catchBlockNodeTryCatch.addChildToBack(catchThrowNode);
 Node tryCatchFinallyNode = nodeFactory.createTryFinallyStatement(
     null, null, tryCatchNode, null);
 Node blockWithTryCatchFinallyNode = nodeFactory.createBlock("{}", null);
 blockWithTryCatchFinallyNode.setFirstChild(tryCatchFinallyNode);
 Node rootWithTryCatchFinallyNode = nodeFactory.createBlock("{}", null);
 rootWithTryCatchFinallyNode.setFirstChild(blockWithTryCatchFinallyNode);
 NodeTraversal.traverse(compiler, rootWithTryCatchFinallyNode, minimizeExitPoints);
 assertFalse(rootWithTryCatchFinallyNode.hasChildren());

 // Test while
 Node whileNode = nodeFactory.createWhileStatement(
     nodeFactory.createConditionalExpression(
         nodeFactory.createBooleanLiteral(true), new IR(1), nodeFactory.createNull()));
 Node whileCodeBlockNode = whileNode.getLastChild();
 whileCodeBlockNode.addChildToBack(nodeFactory.createExpressionStatement(
     nodeFactory.createAssign(nodeFactory.createPropertyAccess(nodeFactory.createThis(), "x"),
         nodeFactory.createNumberLiteral(2))));
 Node blockWithWhileNode = nodeFactory.createBlock("{}", null);
 blockWithWhileNode.setFirstChild(whileNode);
 Node rootWithWhileNode = nodeFactory.createBlock("{}", null);
 rootWithWhileNode.setFirstChild(blockWithWhileNode);
 NodeTraversal.traverse(compiler, rootWithWhileNode, minimizeExitPoints);
 assertFalse(rootWithWhileNode.hasChildren());

 // Test do-while
 Node doWhileNode = nodeFactory.createDoWhileStatement(
     nodeFactory.createConditionalExpression(
         nodeFactory.createBooleanLiteral(true), new IR(1), nodeFactory.createNull()));
 Node doWhileCodeBlockNode = doWhileNode.getLastChild();
 doWhileCodeBlockNode.addChildToBack(nodeFactory.createExpressionStatement(
     nodeFactory.createAssign(nodeFactory.createPropertyAccess(nodeFactory.createThis(), "x"),
         nodeFactory.createNumberLiteral(2))));
 Node blockWithDoWhileNode = nodeFactory.createBlock("{}", null);
 blockWithDoWhileNode.setFirstChild(doWhileNode);
 Node rootWithDoWhileNode = nodeFactory.createBlock("{}", null);
 rootWithDoWhileNode.setFirstChild(blockWithDoWhileNode);
 NodeTraversal.traverse(compiler, rootWithDoWhileNode, minimizeExitPoints);
 assertFalse(rootWithDoWhileNode.hasChildren());

 // Test if
 Node ifNode = nodeFactory.createIfStatement(
     nodeFactory.createConditionalExpression(
         nodeFactory.createBooleanLiteral(true), new IR(1), nodeFactory.createNull()));
 Node ifCodeBlockNode = ifNode.getLastChild();
 ifCodeBlockNode.addChildToBack(nodeFactory.createExpressionStatement(
     nodeFactory.createAssign(nodeFactory.createPropertyAccess(nodeFactory.createThis(), "x"),
         nodeFactory.createNumberLiteral(2))));
 Node elseIfNode = nodeFactory.createElseIfStatement(
     nodeFactory.createConditionalExpression(
         nodeFactory.createBooleanLiteral(false), new IR(3), nodeFactory.createNull()),
         ifNode);
 Node elseNode = nodeFactory.createElseStatement();
 Node ifElseNode = nodeFactory.createIfStatement(ifNode);
 Node ifElseCodeBlockNode = ifElseNode.getLastChild();
 ifElseCodeBlockNode.addChildToBack(nodeFactory.createExpressionStatement(
     nodeFactory.createAssign(nodeFactory.createPropertyAccess(nodeFactory.createThis(), "x"),
         nodeFactory.createNumberLiteral(3))));
 Node blockWithIfElseNode = nodeFactory.createBlock("{}", null);
 blockWithIfElseNode.setFirstChild(ifElseNode);
 Node rootWithIfElseNode = nodeFactory.createBlock("{}", null);
 rootWithIfElseNode.setFirstChild(blockWithIfElseNode);
 NodeTraversal.traverse(compiler, rootWithIfElseNode, minimizeExitPoints);
 assertFalse(rootWithIfElseNode.hasChildren());

 // Test switch
 Node switchNode = nodeFactory.createSwitchStatement(
     nodeFactory.createName("x"), null, null);
 Node switchCodeBlockNode = switchNode.getLastChild();
 switchCodeBlockNode.addChildToBack(nodeFactory.createExpressionStatement(
     nodeFactory.createAssign(nodeFactory.createPropertyAccess(nodeFactory.createThis(), "x"),
         nodeFactory.createNumberLiteral(2))));
 Node caseNode1 = nodeFactory.createCaseLabel(new IR(1));
 Node caseNode1CodeBlockNode = nodeFactory.createBlock("{}", null);
 caseNode1CodeBlockNode.addChildToBack(nodeFactory.createExpressionStatement(
     nodeFactory.createAssign(nodeFactory.createPropertyAccess(nodeFactory.createThis(), "x"),
         nodeFactory.createNumberLiteral(4))));
 switchCodeBlockNode.addChildToBack(caseNode1);
 Node defaultNode = nodeFactory.createDefaultClause();
 Node blockWithSwitchNode = nodeFactory.createBlock("{}", null);
 blockWithSwitchNode.setFirstChild(switchNode);
 Node rootWithSwitchNode = nodeFactory.createBlock("{}", null);
 rootWithSwitchNode.setFirstChild(blockWithSwitchNode);
 NodeTraversal.traverse(compiler, rootWithSwitchNode, minimizeExitPoints);
 assertFalse(rootWithSwitchNode.hasChildren());
 }

 @Test
 @runBefores(JSDocInfo.class)
 public void testMinimizeExitPointsWithJSDocs() {
 MinimizeExitPoints minimizeExitPoints = new MinimizeExitPoints(compiler);

 Node root = nodeFactory.createBlock("{}", new JSDocInfo("@preserve\n" +
     "*\n" +
     "@constructor\n" +
     "*/"));
 Node functionNode = nodeFactory.createFunctionDeclaration("test", null, null, Arrays.asList(
     new IR(1)), null, null, null, null, null, nodeFactory.createBlock("{}", null),
     new JSDocInfo("* @constructor\n" +
         "*\n" +
         "@param {number} x - The x value\n" +
         "@returns {number} The result.\n"
     ));
 Node blockWithFunctionNode = nodeFactory.createBlock("{}", null);
 blockWithFunctionNode.setFirstChild(functionNode);
 Node rootWithFunctionNode = nodeFactory.createBlock("{}", null);
 rootWithFunctionNode.setFirstChild(blockWithFunctionNode);
 NodeTraversal.traverse(compiler, rootWithFunctionNode, minimizeExitPoints);
 assertFalse(rootWithFunctionNode.hasChildren());
 }
}