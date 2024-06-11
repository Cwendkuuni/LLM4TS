package com.google.javascript.jscomp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Callback;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Token;
import org.junit.Before;
import org.junit.Test;

public class CheckGlobalThisTest {

  private AbstractCompiler compiler;
  private NodeTraversal traversal;
  private CheckGlobalThis checker;
  private Node rootNode;
  private Node functionNode;
  private Node assignNode;
  private Node getNode;
  private Node varNode;
  private Node objectLiteralNode;
  private Node prototypeNode;

  @Before
  public void setUp() {
    compiler = mock(AbstractCompiler.class);
    traversal = mock(NodeTraversal.class);
    checker = new CheckGlobalThis(compiler);

    rootNode = Node.newProgram(-1, null, null);
    functionNode = Node.newFunction(0, null, null, null, null, null);
    assignNode = Node.newAssign(1, null, Node.newThis(), Node.newIdent("x"));
    getNode = Node.newGetPropNode(Token.GETPROP, null, Node.newThis(), Node.newIdent("prototype"));
    varNode = Node.newVar("x", null, Node.newFunction(0, null, null, null, null, null), null, null, null, null);
    objectLiteralNode = Node.newObjectLiteral(-1, null, null, null, null, null);
    prototypeNode = Node.newName("prototype", null);

    when(compiler.getTraverser()).thenReturn(traversal);
  }

  @Test
  public void testShouldTraverseFunction() {
    Node parent = Node.newBlock(-1, null, null);
    Node functionParent = Node.newName("myFunction", null);

    Node functionWithThisAnnot =
        Node.newFunction(
            0,
            null,
            null,
            null,
            null,
            new JSDocInfo(
                null,
                null,
                null,
                null,
                null,
                "@constructor",
                "@this",
                null,
                null,
                null,
                null));

    Node functionWithoutAnnot = Node.newFunction(0, null, null, null, null, null);

    Node functionNoPrototype = Node.newBlock(0, null, null);
    Node functionWithPrototype = Node.newBlock(0, null, Node.newList(prototypeNode));

    Node functionWithLends = Node.newBlock(0, null, null);
    Node lendsNode = Node.newName("MyClass", null);
    Node lendsValue = Node.newList(prototypeNode);

    when(traversal.getCurrentNode()).thenReturn(rootNode);
    when(traversal.getParent()).thenReturn(parent);

    // Don't traverse functions that are constructors or have the @constructor or @this annotation.
    checker.shouldTraverse(traversal, functionWithThisAnnot, parent);
    assertFalse(checker.shouldTraverse(traversal, functionWithThisAnnot, parent));

    // Don't traverse functions that are not constructors but have the @constructor or @this annotation.
    checker.shouldTraverse(traversal, functionWithoutAnnot, parent);
    assertFalse(checker.shouldTraverse(traversal, functionWithoutAnnot, parent));

    // Don't traverse functions that are not constructors and do not have the @constructor or @this annotation.
    checker.shouldTraverse(traversal, functionNoPrototype, parent);
    assertTrue(checker.shouldTraverse(traversal, functionNoPrototype, parent));

    // Don't traverse functions that are not constructors and do not have the @constructor or @this annotation.
    checker.shouldTraverse(traversal, functionWithPrototype, parent);
    assertTrue(checker.shouldTraverse(traversal, functionWithPrototype, parent));

    // Don't traverse functions that are getting lent to a prototype.
    when(traversal.getCurrentNode()).thenReturn(functionWithLends);
    when(functionWithLends.getParent()).thenReturn(objectLiteralNode);
    when(objectLiteralNode.getFirstChild()).thenReturn(lendsNode);
    when(lendsNode.getLastChild()).thenReturn(lendsValue);
    when(objectLiteralNode.getLastChild()).thenReturn(functionNode);

    checker.shouldTraverse(traversal, functionNode, objectLiteralNode);
    assertFalse(checker.shouldTraverse(traversal, functionNode, objectLiteralNode));
  }

  @Test
  public void testShouldTraverseFunctionWithAssignLhsChild() {
    Node assignParent = Node.newBlock(0, null, null);

    Node assignFunction = Node.newFunction(0, null, null, null, null, null);
    Node assignLhs = Node.newThis();

    when(traversal.getCurrentNode()).thenReturn(assignNode);
    when(traversal.getParent()).thenReturn(assignParent);

    checker.shouldTraverse(traversal, assignLhs, assignNode);
    assertFalse(checker.shouldTraverse(traversal, assignFunction, assignParent));

    checker.assignLhsChild = assignFunction;
    checker.shouldTraverse(traversal, assignLhs, assignNode);
    assertTrue(checker.shouldTraverse(traversal, assignFunction, assignParent));
  }

  @Test
  public void testShouldTraverseFunctionWithAssignRhs() {
    Node assignParent = Node.newBlock(0, null, null);

    Node assignFunction = Node.newFunction(0, null, null, null, null, null);
    Node assignRhs = Node.newIdent("x");

    when(traversal.getCurrentNode()).thenReturn(assignNode);
    when(traversal.getParent()).thenReturn(assignParent);

    checker.shouldTraverse(traversal, assignRhs, assignNode);
    assertTrue(checker.shouldTraverse(traversal, assignFunction, assignParent));
  }

  @Test
  public void testShouldTraverseFunctionWithGetProperty() {
    Node getParent = Node.newBlock(0, null, null);
    Node getFunction = Node.newFunction(0, null, null, null, null, null);
    Node getLhs = Node.newThis();
    Node getProp = Node.newName("prototype");

    when(traversal.getCurrentNode()).thenReturn(getNode);
    when(traversal.getParent()).thenReturn(getParent);

    checker.shouldTraverse(traversal, getProp, getNode);
    assertFalse(checker.shouldTraverse(traversal, getFunction, getParent));
  }

  @Test
  public void testShouldTraverseFunctionWithGetPropertyDeep() {
    Node getParent = Node.newBlock(0, null, null);
    Node getFunction = Node.newFunction(0, null, null, null, null, null);
    Node getLhs = Node.newThis();
    Node getProp1 = Node.newName("prototype");
    Node getProp2 = Node.newGetPropNode(Token.GETPROP, null, Node.newIdent("prototype"), Node.newIdent("prototype"));

    when(traversal.getCurrentNode()).thenReturn(getNode);
    when(traversal.getParent()).thenReturn(getParent);

    checker.shouldTraverse(traversal, getProp2, getNode);
    assertFalse(checker.shouldTraverse(traversal, getFunction, getParent));
  }

  @Test
  public void testShouldTraverseVar() {
    Node varParent = Node.newBlock(-1, null, null);
    Node varFunction = Node.newFunction(0, null, null, null, null, null);
    Node varNode = Node.newVar("x", null, varFunction, null, null, null, null, null, null, null, null);

    when(traversal.getCurrentNode()).thenReturn(varNode);
    when(traversal.getParent()).thenReturn(varParent);

    checker.shouldTraverse(traversal, varNode, varParent);
    assertTrue(checker.shouldTraverse(traversal, varFunction, varParent));
  }

  @Test
  public void testShouldTraverseObjectLiteral() {
    Node objectParent = Node.newBlock(-1, null, null);
    Node objectNode = Node.newObjectLiteral(-1, null, null, null, null, null);
    Node propertyKey = Node.newString("prototype");
    Node propertyValue = Node.newFunction(0, null, null, null, null, null);

    when(traversal.getCurrentNode()).thenReturn(propertyValue);
    when(traversal.getParent()).thenReturn(objectNode);

    checker.shouldTraverse(traversal, propertyKey, objectNode);
    assertTrue(checker.shouldTraverse(traversal, propertyValue, objectParent));
  }

  @Test
  public void testShouldTraverseObjectLiteralKey() {
    Node objectParent = Node.newBlock(-1, null, null);
    Node objectNode = Node.newObjectLiteral(-1, null, null, null, null, null);
    Node prototypeKey = Node.newString("prototype");

    when(traversal.getCurrentNode()).thenReturn(prototypeKey);
    when(traversal.getParent()).thenReturn(objectNode);
    when(objectNode.getFirstChild()).thenReturn(prototypeKey);

    checker.shouldTraverse(traversal, prototypeKey, objectNode);
    assertTrue(checker.shouldTraverse(traversal, objectNode, objectParent));
  }

  @Test
  public void testShouldTraverseNull() {
    Node parent = Node.newBlock(-1, null, null);
    Node node = null;

    when(traversal.getCurrentNode()).thenReturn(node);
    when(traversal.getParent()).thenReturn(parent);

    checker.shouldTraverse(traversal, node, parent);
    assertFalse(checker.shouldTraverse(traversal, functionNode, parent));
  }
}