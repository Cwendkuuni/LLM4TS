import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerPassManager;
import com.google.javascript.jscomp.CompilerPassManager.PassManagerFactory;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Traversal;
import com.google.javascript.rhino.NodeTraversal;

import java.util.Arrays;
import java.util.List;

public class CollapseVariableDeclarationsTest {

  @org.junit.Before
  public void setUp() {
    CompilerOptions options = new CompilerOptions(false, null);
    PassManagerFactory factory = new PassManagerFactory();
    CompilerPassManager passManager = factory.createPassManager(options, null);
    CollapseVariableDeclarations collapseVariableDeclarations = new CollapseVariableDeclarations(passManager);
    passManager.addPass(collapseVariableDeclarations);
  }

  @org.junit.Test
  public void testCollapsingSingleDeclaration() {
    Node root = new Node(Token.PROGRAM);
    NodeTraversal traversal = new NodeTraversal(root, null);
    Traversal.traverse(traversal);

    Node varNode = new Node(Token.VAR);
    varNode.setString("a");
    varNode.copyInformationFrom(root);
    root.addChildToBack(varNode);

    CompilerPassManager passManager = new CompilerPassManager.Builder()
        .addPass(collapseVariableDeclarations)
        .build();

    passManager.runPasses(root, null);

    Node collapsedVarNode = root.getFirstChild();
    assertEquals("a", collapsedVarNode.getString());
  }

  @org.junit.Test
  public void testCollapsingMultipleDeclarations() {
    Node root = new Node(Token.PROGRAM);
    NodeTraversal traversal = new NodeTraversal(root, null);
    Traversal.traverse(traversal);

    Node varNodeA = new Node(Token.VAR);
    varNodeA.setString("a");
    varNodeA.copyInformationFrom(root);
    root.addChildToBack(varNodeA);

    Node varNodeB = new Node(Token.VAR);
    varNodeB.setString("b");
    varNodeB.copyInformationFrom(root);
    root.addChildToBack(varNodeB);

    Node varNodeC = new Node(Token.VAR);
    varNodeC.setString("c");
    varNodeC.copyInformationFrom(root);
    root.addChildToBack(varNodeC);

    CompilerPassManager passManager = new CompilerPassManager.Builder()
        .addPass(collapseVariableDeclarations)
        .build();

    passManager.runPasses(root, null);

    Node collapsedVarNodes = root.getFirstChild();
    assertEquals(3, collapsedVarNodes.getNumChildren());

    Node collapsedVarNodeA = collapsedVarNodes.getFirstChild();
    assertEquals("a", collapsedVarNodeA.getString());

    Node collapsedVarNodeB = collapsedVarNodes.getChildAtIndex(1);
    assertEquals("b", collapsedVarNodeB.getString());

    Node collapsedVarNodeC = collapsedVarNodes.getLastChild();
    assertEquals("c", collapsedVarNodeC.getString());

    assertEquals("var a, b, c", collapsedVarNodes.toString());
  }

  @org.junit.Test
  public void testCollapsingMultipleDeclarationsWithAssignments() {
    Node root = new Node(Token.PROGRAM);
    NodeTraversal traversal = new NodeTraversal(root, null);
    Traversal.traverse(traversal);

    Node varNodeA = new Node(Token.VAR);
    varNodeA.setString("a");
    varNodeA.copyInformationFrom(root);
    root.addChildToBack(varNodeA);

    Node assignNodeA = new Node(Token.ASSIGN);
    assignNodeA.copyInformationFrom(root);
    varNodeA.addChildToBack(assignNodeA);

    Node assignNodeAValue = new Node(Token.NUMBER, 1);
    assignNodeA.addChildToBack(assignNodeAValue);

    Node varNodeB = new Node(Token.VAR);
    varNodeB.setString("b");
    varNodeB.copyInformationFrom(root);
    root.addChildToBack(varNodeB);

    Node assignNodeB = new Node(Token.ASSIGN);
    assignNodeB.copyInformationFrom(root);
    varNodeB.addChildToBack(assignNodeB);

    Node assignNodeBValue = new Node(Token.NUMBER, 2);
    assignNodeB.addChildToBack(assignNodeBValue);

    CompilerPassManager passManager = new CompilerPassManager.Builder()
        .addPass(collapseVariableDeclarations)
        .build();

    passManager.runPasses(root, null);

    Node collapsedVarNodes = root.getFirstChild();
    assertEquals(1, collapsedVarNodes.getNumChildren());

    Node collapsedVarNodeAB = collapsedVarNodes.getFirstChild();
    assertEquals("a = 1, b = 2", collapsedVarNodeAB.toString());

    assertEquals("var a, b", collapsedVarNodeAB.toString());
  }

  @org.junit.Test
  public void testCollapsingMultipleAssignments() {
    Node root = new Node(Token.PROGRAM);
    NodeTraversal traversal = new NodeTraversal(root, null);
    Traversal.traverse(traversal);

    Node assignNodeA = new Node(Token.ASSIGN);
    assignNodeA.copyInformationFrom(root);

    Node lhsNodeA = new Node(Token.NAME, "a");
    assignNodeA.addChildToBack(lhsNodeA);

    Node rhsNodeA = new Node(Token.NUMBER, 1);
    assignNodeA.addChildToBack(rhsNodeA);

    root.addChildToBack(assignNodeA);

    Node assignNodeB = new Node(Token.ASSIGN);
    assignNodeB.copyInformationFrom(root);

    Node lhsNodeB = new Node(Token.NAME, "b");
    assignNodeB.addChildToBack(lhsNodeB);

    Node rhsNodeB = new Node(Token.NUMBER, 2);
    assignNodeB.addChildToBack(rhsNodeB);

    root.addChildToBack(assignNodeB);

    CompilerPassManager passManager = new CompilerPassManager.Builder()
        .addPass(collapseVariableDeclarations)
        .build();

    passManager.runPasses(root, null);

    Node collapsedAssignNodes = root.getFirstChild();
    assertEquals(1, collapsedAssignNodes.getNumChildren());

    Node collapsedAssignNode = collapsedAssignNodes.getFirstChild();
    assertEquals("var a = 1, b = 2", collapsedAssignNode.toString());
  }

  @org.junit.Test
  public void testCollapsingMultipleDeclarationsWithSameVariableName() {
    Node root = new Node(Token.PROGRAM);
    NodeTraversal traversal = new NodeTraversal(root, null);
    Traversal.traverse(traversal);

    Node varNodeA = new Node(Token.VAR);
    varNodeA.setString("a");
    varNodeA.copyInformationFrom(root);
    root.addChildToBack(varNodeA);

    Node varNodeB = new Node(Token.VAR);
    varNodeB.setString("a");
    varNodeB.copyInformationFrom(root);
    root.addChildToBack(varNodeB);

    CompilerPassManager passManager = new CompilerPassManager.Builder()
        .addPass(collapseVariableDeclarations)
        .build();

    passManager.runPasses(root, null);

    Node collapsedVarNodes = root.getFirstChild();
    assertEquals(1, collapsedVarNodes.getNumChildren());

    Node collapsedVarNode = collapsedVarNodes.getFirstChild();
    assertEquals("var a", collapsedVarNode.toString());
  }

  @org.junit.Test
  public void testCollapsingDeclarationsInsideFunction() {
    Node root = new Node(Token.PROGRAM);
    Node functionNode = new Node(Token.FUNCTION, 1);
    functionNode.setString("myFunction");
    root.addChildToBack(functionNode);

    Node functionBodyNode = new Node(Token.BLOCK);
    functionNode.addChildToBack(functionBodyNode);

    Node varNodeA = new Node(Token.VAR);
    varNodeA.setString("a");
    varNodeA.copyInformationFrom(root);
    functionBodyNode.addChildToBack(varNodeA);

    Node varNodeB = new Node(Token.VAR);
    varNodeB.setString("b");
    varNodeB.copyInformationFrom(root);
    functionBodyNode.addChildToBack(varNodeB);

    CompilerPassManager passManager = new CompilerPassManager.Builder()
        .addPass(collapseVariableDeclarations)
        .build();

    passManager.runPasses(root, null);

    Node collapsedVarNodes = functionBodyNode.getFirstChild();
    assertEquals(1, collapsedVarNodes.getNumChildren());

    Node collapsedVarNode = collapsedVarNodes.getFirstChild();
    assertEquals("var a, b", collapsedVarNode.toString());
  }

  @org.junit.Test
  public void testCollapsingDeclarationsWithinForStatement() {
    Node root = new Node(Token.PROGRAM);
    Node forStatementNode = new Node(Token.FOR);
    root.addChildToBack(forStatementNode);

    Node initNode = new Node(Token.BLOCK);
    forStatementNode.addChildToBack(initNode);

    Node varNodeA = new Node(Token.VAR);
    varNodeA.setString("a");
    varNodeA.copyInformationFrom(root);
    initNode.addChildToBack(varNodeA);

    Node varNodeB = new Node(Token.VAR);
    varNodeB.setString("b");
    varNodeB.copyInformationFrom(root);
    initNode.addChildToBack(varNodeB);

    Node conditionNode = new Node(Token.LITERAL, Token.TRUE);
    forStatementNode.addChildToBack(conditionNode);

    Node statementNode = new Node(Token.BLOCK);
    forStatementNode.addChildToBack(statementNode);

    CompilerPassManager passManager = new CompilerPassManager.Builder()
        .addPass(collapseVariableDeclarations)
        .build();

    passManager.runPasses(root, null);

    Node collapsedVarNodes = initNode.getFirstChild();
    assertEquals(1, collapsedVarNodes.getNumChildren());

    Node collapsedVarNode = collapsedVarNodes.getFirstChild();
    assertEquals("var a, b", collapsedVarNode.toString());
  }

  @org.junit.Test
  public void testCollapsingDeclarationsWithinIfStatement() {
    Node root = new Node(Token.PROGRAM);
    Node ifStatementNode = new Node(Token.IF);
    root.addChildToBack(ifStatementNode);

    Node conditionNode = new Node(Token.LITERAL, Token.TRUE);
    ifStatementNode.addChildToBack(conditionNode);

    Node thenStatementNode = new Node(Token.BLOCK);
    ifStatementNode.addChildToBack(thenStatementNode);

    Node varNodeA = new Node(Token.VAR);
    varNodeA.setString("a");
    varNodeA.copyInformationFrom(root);
    thenStatementNode.addChildToBack(varNodeA);

    Node varNodeB = new Node(Token.VAR);
    varNodeB.setString("b");
    varNodeB.copyInformationFrom(root);
    thenStatementNode.addChildToBack(varNodeB);

    Node elseStatementNode = new Node(Token.ELSE);
    root.addChildToBack(elseStatementNode);

    Node elseIfStatementNode = new Node(Token.IF);
    elseStatementNode.addChildToBack(elseIfStatementNode);

    Node elseConditionNode = new Node(Token.LITERAL, Token.TRUE);
    elseIfStatementNode.addChildToBack(elseConditionNode);

    Node elseStatementBodyNode = new Node(Token.BLOCK);
    elseStatementNode.addChildToBack(elseStatementBodyNode);

    CompilerPassManager passManager = new CompilerPassManager.Builder()
        .addPass(collapseVariableDeclarations)
        .build();

    passManager.runPasses(root, null);

    Node collapsedVarNodes = ifStatementNode.getFirstChild();
    assertEquals(1, collapsedVarNodes.getNumChildren());

    Node collapsedVarNode = collapsedVarNodes.getFirstChild();
    assertEquals("var a, b", collapsedVarNode.toString());
  }
}