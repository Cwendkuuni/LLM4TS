package com.google.javascript.jscomp;

import com.google.common.collect.ImmutableSet;
import com.google.javascript.jscomp.NodeTraversal.AbstractPostOrderCallback;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.rhino.Node;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static com.google.javascript.jscomp.MakeDeclaredNamesUnique.ContextualRenamer.UNIQUE_ID_SEPARATOR;

import static org.junit.Assert.*;

public class MakeDeclaredNamesUniqueTest {

  private MakeDeclaredNamesUnique makeDeclaredNamesUnique;
  private Scope scope;
  private Node rootNode;

  @Before
  public void setup() {
    scope = mock(Scope.class);
    makeDeclaredNamesUnique = new MakeDeclaredNamesUnique(new ContextualRenamer());
    rootNode = NodeOperations.createRootNode("root");
  }

  @Test
  public void testRenameVarsWithinFunction() {
    String functionName = "function";
    String var1 = "var1";
    String var2 = "var2";

    Var var1Declaration = createVariableDeclaration(var1);
    Var var2Declaration = createVariableDeclaration(var2);

    Node functionNode = NodeOperations.createFunctionDeclaration(functionName,
        NodeOperations.createList(NodeOperations.createParameterDeclaration(NodeOperations.createNameNode("param")), var1Declaration, var2Declaration),
        NodeOperations.createBlockNode(
            NodeOperations.createVarDeclarationList(var1Declaration, var2Declaration),
            NodeOperations.createStatementList()));

    Node functionCallNode = NodeOperations.createCallExpression(
        NodeOperations.createNameNode(functionName),
        NodeOperations.createList(NodeOperations.createLiteralNode(1), NodeOperations.createLiteralNode(2)));

    rootNode.setFirstChild(functionNode);
    rootNode.setLastChild(functionCallNode);

    makeDeclaredNamesUnique.visit(null, rootNode, null);

    // Check that variables have been renamed
    Var functionVar1 = scope.getVar(var1);
    Var functionVar2 = scope.getVar(var2);
    Var functionVar1Renamed = scope.getVar(makeDeclaredNamesUnique.getReplacementName(var1));
    Var functionVar2Renamed = scope.getVar(makeDeclaredNamesUnique.getReplacementName(var2));

    assertEquals("Function level variables should have been renamed",
        functionVar1Renamed, functionVar1);
    assertEquals("Function level variables should have been renamed",
        functionVar2Renamed, functionVar2);

    // Check that the function call uses the renamed variables
    Node functionCallNodeRenamed = rootNode.getLastChild();
    Node functionCallParam1 = functionCallNodeRenamed.getFirstChild();
    Node functionCallParam2 = functionCallNodeRenamed.getLastChild();

    assertEquals("Function call parameters should use renamed variables",
        makeDeclaredNamesUnique.getReplacementName(var1),
        functionCallParam1.getString());
    assertEquals("Function call parameters should use renamed variables",
        makeDeclaredNamesUnique.getReplacementName(var2),
        functionCallParam2.getString());
  }

  @Test
  public void testRenameVarsWithinFunctionWithDuplicatedName() {
    String functionName = "function";
    String var1 = "var1";
    String var2 = "var1";

    Var var1Declaration = createVariableDeclaration(var1);
    Var var2Declaration = createVariableDeclaration(var2);

    Node functionNode = NodeOperations.createFunctionDeclaration(functionName,
        NodeOperations.createList(NodeOperations.createParameterDeclaration(NodeOperations.createNameNode("param")), var1Declaration, var2Declaration),
        NodeOperations.createBlockNode(
            NodeOperations.createVarDeclarationList(var1Declaration, var2Declaration),
            NodeOperations.createStatementList()));

    rootNode.setFirstChild(functionNode);

    makeDeclaredNamesUnique.visit(null, rootNode, null);

    // Check that variables have been renamed
    Var functionVar1 = scope.getVar(var1);
    Var functionVar2 = scope.getVar(var2);

    Var functionVar1Renamed = scope.getVar(makeDeclaredNamesUnique.getReplacementName(var1));
    Var functionVar2Renamed = scope.getVar(makeDeclaredNamesUnique.getReplacementName(var2));

    assertEquals("Function level variables should have been renamed",
        functionVar1Renamed, functionVar1);
    assertEquals("Function level variables should have been renamed",
        functionVar2Renamed, functionVar2);

    // Check that the function call uses the renamed variables
    Node functionCallNode = rootNode.getLastChild();
    Node functionCallParam1 = functionCallNode.getFirstChild();
    Node functionCallParam2 = functionCallNode.getLastChild();

    assertEquals("Function call parameters should use renamed variables",
        makeDeclaredNamesUnique.getReplacementName(var1),
        functionCallParam1.getString());
    assertEquals("Function call parameters should use renamed variables",
        makeDeclaredNamesUnique.getReplacementName(var2),
        functionCallParam2.getString());
  }

  @Test
  public void testRenameVarsInBlock() {
    String var1 = "var1";
    String var2 = "var2";

    Var var1Declaration = createVariableDeclaration(var1);
    Var var2Declaration = createVariableDeclaration(var2);

    Node blockNode = NodeOperations.createBlockNode(
        NodeOperations.createVarDeclarationList(var1Declaration, var2Declaration),
        NodeOperations.createStatementList());

    rootNode.setFirstChild(blockNode);

    makeDeclaredNamesUnique.visit(null, rootNode, null);

    // Check that variables have been renamed
    Var blockVar1 = scope.getVar(var1);
    Var blockVar2 = scope.getVar(var2);

    Var blockVar1Renamed = scope.getVar(makeDeclaredNamesUnique.getReplacementName(var1));
    Var blockVar2Renamed = scope.getVar(makeDeclaredNamesUnique.getReplacementName(var2));

    assertEquals("Block level variables should have been renamed",
        blockVar1Renamed, blockVar1);
    assertEquals("Block level variables should have been renamed",
        blockVar2Renamed, blockVar2);
  }

  @Test
  public void testRenameVarsInBlockWithDuplicatedName() {
    String var1 = "var1";
    String var2 = "var1";

    Var var1Declaration = createVariableDeclaration(var1);
    Var var2Declaration = createVariableDeclaration(var2);

    Node blockNode = NodeOperations.createBlockNode(
        NodeOperations.createVarDeclarationList(var1Declaration, var2Declaration),
        NodeOperations.createStatementList());

    rootNode.setFirstChild(blockNode);

    makeDeclaredNamesUnique.visit(null, rootNode, null);

    // Check that variables have been renamed
    Var blockVar1 = scope.getVar(var1);
    Var blockVar2 = scope.getVar(var2);

    Var blockVar1Renamed = scope.getVar(makeDeclaredNamesUnique.getReplacementName(var1));
    Var blockVar2Renamed = scope.getVar(makeDeclaredNamesUnique.getReplacementName(var2));

    assertEquals("Block level variables should have been renamed",
        blockVar1Renamed, blockVar1);
    assertEquals("Block level variables should have been renamed",
        blockVar2Renamed, blockVar2);
  }

  @Test
  public void testRenameVarsWithinFunctionAndBlock() {
    String functionName = "function";
    String var1 = "var1";
    String var2 = "var1";

    Var functionVar1Declaration = createVariableDeclaration(var1);
    Var functionVar2Declaration = createVariableDeclaration(var2);

    Node functionNode = NodeOperations.createFunctionDeclaration(functionName,
        NodeOperations.createList(NodeOperations.createParameterDeclaration(NodeOperations.createNameNode("param")),
            functionVar1Declaration, functionVar2Declaration),
        NodeOperations.createBlockNode(
            NodeOperations.createVarDeclarationList(functionVar1Declaration, functionVar2Declaration),
            NodeOperations.createStatementList()));

    Var blockVar1Declaration = createVariableDeclaration(var1);
    Var blockVar2Declaration = createVariableDeclaration(var2);

    Node blockNode = NodeOperations.createBlockNode(
        NodeOperations.createVarDeclarationList(blockVar1Declaration, blockVar2Declaration),
        NodeOperations.createStatementList());

    rootNode.setFirstChild(functionNode);
    rootNode.getLastChild().setNext(blockNode);

    makeDeclaredNamesUnique.visit(null, rootNode, null);

    // Check that variables have been renamed
    Var functionVar1 = scope.getVar(var1);
    Var functionVar2 = scope.getVar(var2);
    Var blockVar1 = scope.getVar(var1);
    Var blockVar2 = scope.getVar(var2);

    Var functionVar1Renamed = scope.getVar(makeDeclaredNamesUnique.getReplacementName(var1));
    Var functionVar2Renamed = scope.getVar(makeDeclaredNamesUnique.getReplacementName(var2));
    Var blockVar1Renamed = scope.getVar(makeDeclaredNamesUnique.getReplacementName(var1));
    Var blockVar2Renamed = scope.getVar(makeDeclaredNamesUnique.getReplacementName(var2));

    assertEquals("Function level variables should have been renamed",
        functionVar1Renamed, functionVar1);
    assertEquals("Function level variables should have been renamed",
        functionVar2Renamed, functionVar2);
    assertEquals("Block level variables should have been renamed",
        blockVar1Renamed, blockVar1);
    assertEquals("Block level variables should have been renamed",
        blockVar2Renamed, blockVar2);

    // Check that the function call uses the renamed variables
    Node functionCallNode = rootNode.getLastChild();
    Node functionCallParam1 = functionCallNode.getFirstChild();
    Node functionCallParam2 = functionCallNode.getLastChild();

    assertEquals("Function call parameters should use renamed variables",
        makeDeclaredNamesUnique.getReplacementName(var1),
        functionCallParam1.getString());
    assertEquals("Function call parameters should use renamed variables",
        makeDeclaredNamesUnique.getReplacementName(var2),
        functionCallParam2.getString());

    // Check that the block variables are not affected by the function variables
    assertEquals("Block level variables should not be renamed by the function",
        var1, blockVar1Renamed.getName());
    assertEquals("Block level variables should not be renamed by the function",
        var2, blockVar2Renamed.getName());
  }

  @Test
  public void testRenameCatchBlockWithFunctionDeclaration() {
    String exceptionName = "Exception";
    String var1 = "var1";

    Var var1Declaration = createVariableDeclaration(var1);

    Node catchBlockNode = NodeOperations.createCatchBlockNode(
        NodeOperations.createNameNode(exceptionName),
        NodeOperations.createBlockNode(
            NodeOperations.createVarDeclarationList(var1Declaration),
            NodeOperations.createStatementList()));

    Node functionNode = NodeOperations.createFunctionDeclaration(
        "function",
        NodeOperations.createList(),
        NodeOperations.createBlockNode(NodeOperations.createList(catchBlockNode), NodeOperations.createStatementList()));

    rootNode.setFirstChild(functionNode);

    makeDeclaredNamesUnique.visit(null, rootNode, null);

    // Check that variables have been renamed
    Var catchBlockVar1 = scope.getVar(var1);

    Var catchBlockVar1Renamed = scope.getVar(makeDeclaredNamesUnique.getReplacementName(var1));

    assertEquals("Catch block variable should have been renamed",
        catchBlockVar1Renamed, catchBlockVar1);

    // Check that the catch block is not affected by the function variables
    assertEquals("Catch block variable should not be renamed by the function",
        var1, catchBlockVar1Renamed.getName());
  }

  @Test
  public void testRenameCatchBlockWithVariableDeclaration() {
    String exceptionName = "Exception";
    String var1 = "var1";

    Var var1Declaration = createVariableDeclaration(var1);

    Node catchBlockNode = NodeOperations.createCatchBlockNode(
        NodeOperations.createNameNode(exceptionName),
        NodeOperations.createBlockNode(
            NodeOperations.createVarDeclarationList(var1Declaration),
            NodeOperations.createStatementList()));

    Node blockNode = NodeOperations.createBlockNode(
        NodeOperations.createVarDeclarationList(var1Declaration),
        NodeOperations.createStatementList());

    rootNode.setFirstChild(NodeOperations.createCallExpression(
        NodeOperations.createNameNode("function"),
        NodeOperations.createList(catchBlockNode, blockNode)));

    makeDeclaredNamesUnique.visit(null, rootNode, null);

    // Check that variables have been renamed
    Var catchBlockVar1 = scope.getVar(var1);

    Var catchBlockVar1Renamed = scope.getVar(makeDeclaredNamesUnique.getReplacementName(var1));

    assertEquals("Catch block variable should have been renamed",
        catchBlockVar1Renamed, catchBlockVar1);

    // Check that the catch block is not affected by the function variables
    assertEquals("Catch block variable should not be renamed by the function",
        var1, catchBlockVar1Renamed.getName());
  }

  @Test
  public void testRenameFunctionWithinFunction() {
    String outerFunctionName = "outerFunction";
    String innerFunctionName = "innerFunction";

    Var outerFunctionVarDeclaration = createVariableDeclaration(outerFunctionName);

    Node outerFunctionNode = NodeOperations.createFunctionDeclaration(
        outerFunctionName,
        NodeOperations.createList(),
        NodeOperations.createBlockNode(NodeOperations.createVarDeclarationList(outerFunctionVarDeclaration),
            NodeOperations.createStatementList()));

    Var innerFunctionVarDeclaration = createVariableDeclaration(innerFunctionName);

    Node innerFunctionNode = NodeOperations.createFunctionDeclaration(
        innerFunctionName,
        NodeOperations.createList(),
        NodeOperations.createBlockNode(NodeOperations.createVarDeclarationList(innerFunctionVarDeclaration),
            NodeOperations.createStatementList()));

    outerFunctionNode.getLastChild().setNext(innerFunctionNode);

    rootNode.setFirstChild(outerFunctionNode);

    makeDeclaredNamesUnique.visit(null, rootNode, null);

    // Check that functions have been renamed
    Var outerFunctionRenamed = scope.getVar(outerFunctionName);
    Var innerFunctionRenamed = scope.getVar(innerFunctionName);

    Var outerFunctionRenamedByName = scope.getVar(makeDeclaredNamesUnique.getReplacementName(outerFunctionName));
    Var innerFunctionRenamedByName = scope.getVar(makeDeclaredNamesUnique.getReplacementName(innerFunctionName));

    assertEquals("Outer function should have been renamed",
        outerFunctionRenamedByName, outerFunctionRenamed);
    assertEquals("Inner function should have been renamed",
        innerFunctionRenamedByName, innerFunctionRenamed);

    // Check that the functions are not affected by each other
    assertEquals("Outer function should not be renamed by inner function",
        outerFunctionName, outerFunctionRenamedByName.getName());
    assertEquals("Inner function should not be renamed by outer function",
        innerFunctionName, innerFunctionRenamedByName.getName());
  }

  private Var createVariableDeclaration(String name) {
    return new Var(Name.create(name), null, null, null, null, null, null);
  }
}