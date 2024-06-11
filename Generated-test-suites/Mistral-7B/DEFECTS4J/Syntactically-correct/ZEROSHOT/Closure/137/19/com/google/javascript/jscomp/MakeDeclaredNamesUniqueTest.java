package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.testing.TestCompiler;
import com.google.javascript.rhino.Token;
import com.google.javascript.rhino.TokenStream;

import java.util.HashMap;
import java.util.Map;

public class MakeDeclaredNamesUniqueTest {

  private static Node createNode(int type, String value) {
    Node node = new Node();
    node.setType(type);
    node.setString(value);
    return node;
  }

  private static Node createFunctionNode(String name, Node body) {
    Node functionNode = new Node();
    functionNode.setType(Token.FUNCTION);
    Node functionNameNode = new Node();
    functionNameNode.setType(Token.NAME);
    functionNameNode.setString(name);
    functionNode.addChild(functionNameNode);
    functionNode.addChild(body);
    return functionNode;
  }

  private static Node createVarNode(String name) {
    Node varNode = new Node();
    varNode.setType(Token.VAR);
    Node varNameNode = new Node();
    varNameNode.setType(Token.NAME);
    varNameNode.setString(name);
    varNode.addChild(varNameNode);
    return varNode;
  }

  private static Node createBlockNode(Node body) {
    Node blockNode = new Node();
    blockNode.setType(Token.BLOCK);
    blockNode.addChild(body);
    return blockNode;
  }

  @org.junit.Test
  public void testMakeDeclaredNamesUnique() {
    TestCompiler compiler = new TestCompiler();
    Compiler testCompiler = compiler.toSource();

    Node rootNode = createNode(Token.BLOCK, "");

    // Create a function with a var and a function declaration.
    String functionName = "foo";
    Node functionBody = createNode(Token.IF, "");
    Node varNode = createVarNode("x");
    Node functionDeclaration = createNode(Token.FUNCTION, functionName);
    Node functionDeclarationNameNode = functionDeclaration.getFirstChild();
    functionDeclarationNameNode.setString("bar");
    Node functionParameterNode = functionDeclaration.getFirstChild().getNext();
    functionParameterNode.setString("y");
    Node functionBodyVariableNode = createVarNode("x");
    Node functionBodyDeclarationNode = createNode(Token.VAR, "");
    functionBodyDeclarationNode.addChild(functionBodyVariableNode);
    functionBodyDeclarationNode.setString("z");
    functionDeclaration.addChild(functionDeclarationNameNode);
    functionDeclaration.addChild(functionParameterNode);
    functionDeclaration.addChild(functionBodyDeclarationNode);
    functionBody.addChild(functionBodyDeclarationNode);
    Node functionCallNode = createNode(Token.CALL, "");
    functionCallNode.addChild(functionDeclaration);
    Node functionDeclarationParentNode = createNode(Token.IF, "");
    functionDeclarationParentNode.addChild(functionDeclarationNameNode);
    functionDeclarationParentNode.addChild(functionCallNode);
    rootNode.addChild(functionDeclarationParentNode);

    // Create a var with the same name as the function parameter.
    Node varDeclarationNode = createVarNode("y");
    rootNode.addChild(varDeclarationNode);

    MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();
    NodeTraversal nodeTraversal = new NodeTraversal(rootNode, testCompiler);
    nodeTraversal.setScopedCallback(makeDeclaredNamesUnique);
    nodeTraversal.traverse();

    // Check the results.
    Iterator<Map.Entry<Scope.Var, String>> varIterator = makeDeclaredNamesUnique.rootRenamer.declarations.entrySet().iterator();
    Map.Entry<Scope.Var, String> functionVarEntry = null;
    Map.Entry<Scope.Var, String> varEntry = null;
    while (varIterator.hasNext()) {
      Map.Entry<Scope.Var, String> entry = varIterator.next();
      Scope.Var var = entry.getKey();
      String newName = entry.getValue();
      if (var.isFunction()) {
        functionVarEntry = entry;
      } else if (var.isVar()) {
        varEntry = entry;
      }
    }

    assertEquals("x$0", varEntry.getValue());
    assertEquals("y$1", functionVarEntry.getValue());

    // Check the node values.
    assertEquals("x$0", varDeclarationNode.getString());
    assertEquals("y$1", functionDeclarationNameNode.getString());
    assertEquals("z", functionBodyVariableNode.getString());
    assertNull(functionDeclarationNameNode.getFirstChild().getString());
    assertNull(functionParameterNode.getString());
    assertEquals("y$1", functionCallNode.getFirstChild().getString());
  }
}

class TestCompiler extends Compiler {

  public TestCompiler() {
    super(new CompilerOptions());
  }

  public Compiler toSource() {
    Compiler compiler = new Compiler();
    compiler.setOptions(getOptions());
    return compiler;
  }

  @Override
  public void reportCodeChange() {
    // Do nothing.
  }
}