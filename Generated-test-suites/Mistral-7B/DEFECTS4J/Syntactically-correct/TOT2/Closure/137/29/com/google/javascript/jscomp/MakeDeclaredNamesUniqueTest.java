package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.google.common.collect.ImmutableSet;
import com.google.javascript.jscomp.NodeTraversal.AbstractPostOrderCallback;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;
import com.google.javascript.rhino.TokenStream;

public class MakeDeclaredNamesUniqueTest {

  @Test
  public void testMakeDeclaredNamesUnique() {
    String jsCode = "function f() { var x = 1; function g() { var x = 2; } }";
    Node node = parseJsCodeToAbstractSyntaxTree(jsCode);

    MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();
    makeDeclaredNamesUnique.traverse(NodeTraversal.traverse(null, node, null, null));

    Var xInFunctionF = node.getFirstChild().getFirstChild().getNext();
    assertNotNull(xInFunctionF);
    assertEquals("x_1", xInFunctionF.getName());

    Var xInFunctionG = null;
    for (Node child = node.getLastChild(); child != null; child = child.getNext()) {
      if (child.getType() == Token.FUNCTION) {
        for (Node grandChild = child.getFirstChild(); grandChild != null;
            grandChild = grandChild.getNext()) {
          if (grandChild.getType() == Token.VAR) {
            xInFunctionG = grandChild;
            break;
          }
        }
      }
      if (xInFunctionG != null) {
        break;
      }
    }
    assertNotNull(xInFunctionG);
    assertEquals("x_2", xInFunctionG.getName());

    Var xInGlobalScope = null;
    for (Node child = node.getFirstChild(); child != null; child = child.getNext()) {
      if (child.getType() == Token.VAR) {
        xInGlobalScope = child;
        break;
      }
    }
    assertNotNull(xInGlobalScope);
    assertEquals("x", xInGlobalScope.getName());
  }

  @Test
  public void testMakeDeclaredNamesUniqueWithExceptionHandler() {
    String jsCode = "try { function f() { } } catch (e) { function g() { } }";
    Node node = parseJsCodeToAbstractSyntaxTree(jsCode);

    MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();
    makeDeclaredNamesUnique.traverse(NodeTraversal.traverse(null, node, null, null));

    Var fFunction = null;
    for (Node child = node.getFirstChild(); child != null; child = child.getNext()) {
      if (child.getType() == Token.FUNCTION) {
        fFunction = child;
        break;
      }
    }
    assertNotNull(fFunction);

    Var gFunction = null;
    for (Node child = node.getLastChild(); child != null; child = child.getNext()) {
      if (child.getType() == Token.FUNCTION) {
        gFunction = child;
        break;
      }
    }
    assertNotNull(gFunction);

    assertEquals("f", fFunction.getName());
    assertEquals("g", gFunction.getName());
  }

  @Test
  public void testMakeDeclaredNamesUniqueWithRecursiveFunction() {
    String jsCode = "function f(x) { if (x > 0) { return x - 1; } function f(x) { return x - 1; } }";
    Node node = parseJsCodeToAbstractSyntaxTree(jsCode);

    MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();
    makeDeclaredNamesUnique.traverse(NodeTraversal.traverse(null, node, null, null));

    Var recursiveFunctionF = null;
    for (Node child = node.getFirstChild(); child != null; child = child.getNext()) {
      if (child.getType() == Token.FUNCTION) {
        for (Node grandChild = child.getFirstChild(); grandChild != null;
            grandChild = grandChild.getNext()) {
          if (grandChild.getType() == Token.FUNCTION) {
            recursiveFunctionF = grandChild;
            break;
          }
        }
      }
    }
    assertNotNull(recursiveFunctionF);

    assertEquals("f_1", recursiveFunctionF.getName());
  }

  @Test(expected = AssertionError.class)
  public void testMakeDeclaredNamesUniqueWithInvalidName() {
    String jsCode = "function f() { var 1x = 1; }";
    Node node = parseJsCodeToAbstractSyntaxTree(jsCode);

    MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();
    makeDeclaredNamesUnique.traverse(NodeTraversal.traverse(null, node, null, null));
  }

  private Node parseJsCodeToAbstractSyntaxTree(String jsCode) {
    TokenStream tokenStream = new TokenStream(jsCode, null, null);
    Node root = tokenStream.parse();
    return root;
  }
}