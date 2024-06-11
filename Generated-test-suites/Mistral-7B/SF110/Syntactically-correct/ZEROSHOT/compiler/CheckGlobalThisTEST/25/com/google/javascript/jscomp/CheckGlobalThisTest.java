import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckGlobalThis;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.Token;

public class CheckGlobalThisTest {

  private AbstractCompiler compiler;
  private CheckGlobalThis checker;
  private NodeFactory nodeFactory;

  @org.junit.Before
  public void setUp() {
    compiler = mock(AbstractCompiler.class);
    checker = new CheckGlobalThis(compiler);
    nodeFactory = NodeFactory.getInitial();
  }

  @org.junit.Test
  public void testShouldTraverseFunctionAnnotatedWithThis() {
    Node functionNode = nodeFactory.createFunction(null, null, null, null);
    JSDocInfo jsDoc = new JSDocInfo();
    jsDoc.setThisType("MyType");
    functionNode.setJSDocInfo(jsDoc);
    Node functionParent = nodeFactory.createBlock(nodeFactory.createToken(Token.LEFTPAREN));
    functionParent.addChild(functionNode);

    assertTrue(checker.shouldTraverse(new NodeTraversal(nodeFactory, null, null), functionNode, functionParent));
  }

  @org.junit.Test
  public void testShouldNotTraverseFunctionAnnotatedWithConstructor() {
    Node functionNode = nodeFactory.createFunction(null, null, null, null);
    JSDocInfo jsDoc = new JSDocInfo();
    jsDoc.setConstructor(true);
    functionNode.setJSDocInfo(jsDoc);
    Node functionParent = nodeFactory.createBlock(nodeFactory.createToken(Token.LEFTPAREN));
    functionParent.addChild(functionNode);

    assertFalse(checker.shouldTraverse(new NodeTraversal(nodeFactory, null, null), functionNode, functionParent));
  }

  @org.junit.Test
  public void testShouldNotTraversePrototypeMethod() {
    Node prototypeNode = nodeFactory.createBlock(nodeFactory.createToken(Token.LEFTPAREN));
    Node prototypeFunctionNode = nodeFactory.createFunction(null, null, null, null);
    prototypeFunctionNode.setJSDocInfo(new JSDocInfo().setPrototypeMethod(true));
    prototypeNode.addChild(prototypeFunctionNode);
    Node objectNode = nodeFactory.createObjectLiteral();
    objectNode.addChild(prototypeNode);

    Node functionParent = nodeFactory.createBlock(nodeFactory.createToken(Token.LEFTPAREN));
    functionParent.addChild(objectNode);

    assertFalse(checker.shouldTraverse(new NodeTraversal(nodeFactory, null, null), prototypeFunctionNode, functionParent));
  }

  @org.junit.Test
  public void testShouldNotTraverseFunctionWithThisAnnotationButNoLogicalPlace() {
    Node functionNode = nodeFactory.createFunction(null, null, null, null);
    Node functionParent = nodeFactory.createBlock(nodeFactory.createToken(Token.LEFTPAREN));
    functionParent.addChild(functionNode);

    assertFalse(checker.shouldTraverse(new NodeTraversal(nodeFactory, null, null), functionNode, functionParent));
  }

  @org.junit.Test
  public void testShouldNotTraverseFunctionInNonGlobalContext() {
    Node functionNode = nodeFactory.createFunction(null, null, null, null);
    Node localFunctionParent = nodeFactory.createBlock(nodeFactory.createToken(Token.LEFTPAREN));
    localFunctionParent.addChild(functionNode);

    assertFalse(checker.shouldTraverse(new NodeTraversal(nodeFactory, null, null), functionNode, localFunctionParent));
  }

  @org.junit.Test
  public void testShouldNotTraverseFunctionCalledFromPrototypeProperty() {
    Node objectNode = nodeFactory.createObjectLiteral();
    Node prototypeNode = nodeFactory.createBlock(nodeFactory.createToken(Token.LEFTPAREN));
    Node prototypeFunctionNode = nodeFactory.createFunction(null, null, null, null);
    prototypeFunctionNode.setJSDocInfo(new JSDocInfo().setPrototypeMethod(true));
    prototypeNode.addChild(prototypeFunctionNode);
    objectNode.addChild(prototypeNode);
    Node functionParent = nodeFactory.createBlock(nodeFactory.createToken(Token.LEFTPAREN));
    functionParent.addChild(objectNode);
    Node functionCallNode = nodeFactory.createCall(prototypeFunctionNode, new Node[0]);

    assertFalse(checker.shouldTraverse(new NodeTraversal(nodeFactory, null, null), functionCallNode, functionParent));
  }

  @org.junit.Test
  public void testShouldNotTraverseFunctionCalledFromPrototypeSubProperty() {
    Node objectNode = nodeFactory.createObjectLiteral();
    Node prototypeNode = nodeFactory.createBlock(nodeFactory.createToken(Token.LEFTPAREN));
    Node prototypeFunctionNode = nodeFactory.createFunction(null, null, null, null);
    prototypeFunctionNode.setJSDocInfo(new JSDocInfo().setPrototypeMethod(true));
    prototypeNode.addChild(prototypeFunctionNode);
    Node propertyNode = nodeFactory.createStringLiteral("property");
    Node propertyAccessNode = nodeFactory.createGetProp(prototypeNode, propertyNode);
    objectNode.addChild(propertyAccessNode);
    Node functionParent = nodeFactory.createBlock(nodeFactory.createToken(Token.LEFTPAREN));
    functionParent.addChild(objectNode);
    Node functionCallNode = nodeFactory.createCall(prototypeFunctionNode, new Node[0]);

    assertFalse(checker.shouldTraverse(new NodeTraversal(nodeFactory, null, null), functionCallNode, functionParent));
  }

  @org.junit.Test
  public void testShouldTraverseAssignmentToLeftSide() {
    Node assignNode = nodeFactory.createAssign();
    Node leftSide = nodeFactory.createThis();
    assignNode.addChild(leftSide);
    Node rightSide = nodeFactory.createStringLiteral("globalThis");
    assignNode.addChild(rightSide);
    Node parent = nodeFactory.createBlock(nodeFactory.createToken(Token.LEFTPAREN));
    parent.addChild(assignNode);

    assertTrue(checker.shouldTraverse(new NodeTraversal(nodeFactory, null, null), leftSide, parent));
  }

  @org.junit.Test
  public void testShouldNotTraverseAssignmentToRightSide() {
    Node assignNode = nodeFactory.createAssign();
    Node leftSide = nodeFactory.createBlock(nodeFactory.createToken(Token.LEFTPAREN));
    Node rightSide = nodeFactory.createThis();
    assignNode.addChild(leftSide);
    assignNode.addChild(rightSide);
    Node parent = nodeFactory.createBlock(nodeFactory.createToken(Token.LEFTPAREN));
    parent.addChild(assignNode);

    assertFalse(checker.shouldTraverse(new NodeTraversal(nodeFactory, null, null), rightSide, parent));
  }

  @org.junit.Test
  public void testShouldNotTraverseAssignmentToPrototypeProperty() {
    Node assignNode = nodeFactory.createAssign();
    Node objectNode = nodeFactory.createObjectLiteral();
    Node prototypeNode = nodeFactory.createBlock(nodeFactory.createToken(Token.LEFTPAREN));
    Node prototypeFunctionNode = nodeFactory.createFunction(null, null, null, null);
    prototypeFunctionNode.setJSDocInfo(new JSDocInfo().setPrototypeMethod(true));
    prototypeNode.addChild(prototypeFunctionNode);
    objectNode.addChild(prototypeNode);
    Node propertyNode = nodeFactory.createStringLiteral("property");
    Node propertyAccessNode = nodeFactory.createGetProp(prototypeNode, propertyNode);
    assignNode.addChild(propertyAccessNode);
    Node leftSideThis = nodeFactory.createThis();
    assignNode.addChild(leftSideThis);
    Node parent = nodeFactory.createBlock(nodeFactory.createToken(Token.LEFTPAREN));
    parent.addChild(objectNode);
    parent.addChild(assignNode);

    assertFalse(checker.shouldTraverse(new NodeTraversal(nodeFactory, null, null), leftSideThis, parent));
  }

  @org.junit.Test
  public void testShouldNotTraverseAssignmentToPrototypeSubProperty() {
    Node assignNode = nodeFactory.createAssign();
    Node objectNode = nodeFactory.createObjectLiteral();
    Node prototypeNode = nodeFactory.createBlock(nodeFactory.createToken(Token.LEFTPAREN));
    Node prototypeFunctionNode = nodeFactory.createFunction(null, null, null, null);
    prototypeFunctionNode.setJSDocInfo(new JSDocInfo().setPrototypeMethod(true));
    prototypeNode.addChild(prototypeFunctionNode);
    objectNode.addChild(prototypeNode);
    Node propertyNode = nodeFactory.createStringLiteral("property");
    Node propertyAccessNode = nodeFactory.createGetProp(prototypeNode, propertyNode);
    Node propertySubPropertyNode = nodeFactory.createGetProp(propertyAccessNode, new NodeFactory().createStringLiteral("subProperty"));
    assignNode.addChild(propertySubPropertyNode);
    Node leftSideThis = nodeFactory.createThis();
    assignNode.addChild(leftSideThis);
    Node parent = nodeFactory.createBlock(nodeFactory.createToken(Token.LEFTPAREN));
    parent.addChild(objectNode);
    parent.addChild(assignNode);

    assertFalse(checker.shouldTraverse(new NodeTraversal(nodeFactory, null, null), leftSideThis, parent));
  }
}