package com.example.junit;

import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.DefinitionsRemover.Definition;
import com.google.javascript.jscomp.DefinitionsRemover.DefinitionSite;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.SimpleDefinitionFinder;
import com.google.javascript.jscomp.UseSite;
import com.google.javascript.rhino.NodeUtil;
import com.google.javascript.rhino.Token;
import com.google.javascript.rhino.jstype.FunctionType;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.JSTypeNative;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.ObjectType;
import org.junit.Before;
import org.junit.Test;

public class DevirtualizePrototypeMethodsTest {
  private AbstractCompiler compiler;
  private SimpleDefinitionFinder definitionFinder;
  private CompilerPass pass;

  @Before
  public void setup() {
    compiler = mock(AbstractCompiler.class);
    definitionFinder = mock(SimpleDefinitionFinder.class);
    when(compiler.getCodingConvention()).thenReturn(new CodingConvention());
    when(compiler.getModuleGraph()).thenReturn(mock(JSModuleGraph.class));
    pass = new DevirtualizePrototypeMethods(compiler);
  }

  @Test
  public void testRewritesDefinitionAndCallSites() {
    Node root = Node.newBlock(1);

    Node aPrototype = Node.newObject("a", null);
    Node aPrototypeProperty = Node.newGetProp("prototype", aPrototype);

    Node bDefinitionNode = Node.newExprAssign(aPrototypeProperty, Node.newFunction(0));

    Node bCallNode = Node.newGetProp(Node.newGetProp("a", Node.newThis()), "b");

    root.addChild(aPrototype);
    root.addChild(bDefinitionNode);
    root.addChild(bCallNode);

    Definition definition = new Definition("b", Node.newFunction(0), null, null);

    when(definitionFinder.getDefinition(bCallNode)).thenReturn(definition);

    pass.process(null, root);

    Node expectedCallNode = Node.newCall(
        Node.newGetProp(newNode("JSCompiler_StaticMethods_b"), "call"),
        Node.newThis(),
        Node.newObject("a")
    );

    Node bCallSite = bCallNode.getParent();

    assertCallSiteRewritten(bCallSite, expectedCallNode);

    Node expectedDefinitionNode = Node.newVar(
        Node.newString(Token.NAME, "b"),
        Node.newFunction(1, Lists.newArrayList(Node.newString(Token.NAME, "self"))),
        null,
        Node.newBlock(0)
    );

    Node definitionSite = bDefinitionNode.getParent();

    assertDefinitionRewritten(definitionSite, expectedDefinitionNode);
  }

  @Test
  public void testDoesNotRewriteExportedMethods() {
    Node root = Node.newBlock(1);

    Node aPrototype = Node.newObject("a", null);
    Node aPrototypeProperty = Node.newGetProp("prototype", aPrototype);

    Node exportedBDefinitionNode = Node.newExprAssign(aPrototypeProperty, Node.newFunction(0));
    Node exportedBCallNode = Node.newGetProp(Node.newGetProp("a", Node.newThis()), "exportedB");

    root.addChild(aPrototype);
    root.addChild(exportedBDefinitionNode);
    root.addChild(exportedBCallNode);

    JSModuleDefinition exportingModule = new JSModuleDefinition("module.exports");
    when(compiler.getModuleGraph()).thenReturn(new JSModuleGraph(Lists.newArrayList(exportingModule)));
    when(definitionFinder.getModule(exportedBCallNode)).thenReturn(exportingModule);

    pass.process(null, root);

    assertCallSiteUnchanged(exportedBCallNode);
  }

  @Test
  public void testDoesNotRewriteDefinitionsWithMultipleUseSites() {
    Node root = Node.newBlock(1);

    Node aPrototype = Node.newObject("a", null);
    Node aPrototypeProperty = Node.newGetProp("prototype", aPrototype);

    Node bDefinitionNode1 = Node.newExprAssign(aPrototypeProperty, Node.newFunction(0));
    Node bCallNode1 = Node.newGetProp(Node.newGetProp("a", Node.newThis()), "b");

    Node bDefinitionNode2 = Node.newExprAssign(aPrototypeProperty, Node.newFunction(0));
    Node bCallNode2 = Node.newGetProp(Node.newGetProp("a", Node.newThis()), "b");

    root.addChild(aPrototype);
    root.addChild(bDefinitionNode1);
    root.addChild(bCallNode1);
    root.addChild(bDefinitionNode2);
    root.addChild(bCallNode2);

    Definition definition1 = new Definition("b", Node.newFunction(0), null, null);
    Definition definition2 = new Definition("b", Node.newFunction(0), null, null);

    when(definitionFinder.getDefinition(bCallNode1)).thenReturn(definition1);
    when(definitionFinder.getDefinition(bCallNode2)).thenReturn(definition2);

    pass.process(null, root);

    assertCallSiteUnchanged(bCallNode1);
    assertCallSiteUnchanged(bCallNode2);
  }

  @Test
  public void testDoesNotRewriteDefinitionsInControlStructures() {
    Node root = Node.newBlock(1);

    Node aPrototype = Node.newObject("a", null);
    Node aPrototypeProperty = Node.newGetProp("prototype", aPrototype);

    Node ifStatement = Node.newIf(Node.newLiteral(true), Node.newBlock(0), Node.newBlock(0));

    Node bDefinitionNode = Node.newExprAssign(aPrototypeProperty, Node.newFunction(0));
    Node bCallNode = Node.newGetProp(Node.newGetProp("a", Node.newThis()), "b");

    ifStatement.addChild(bDefinitionNode);
    ifStatement.addChild(bCallNode);

    root.addChild(ifStatement);

    pass.process(null, root);

    assertCallSiteUnchanged(bCallNode);
  }

  @Test
  public void testDoesNotRewriteDefinitionsInModulesLoadedAfterFirstUse() {
    Node root = Node.newBlock(1);

    Node aPrototype = Node.newObject("a", null);
    Node aPrototypeProperty = Node.newGetProp("prototype", aPrototype);

    Node bDefinitionNode1 = Node.newExprAssign(aPrototypeProperty, Node.newFunction(0));
    Node bCallNode1 = Node.newGetProp(Node.newGetProp("a", Node.newThis()), "b");

    root.addChild(aPrototype);
    root.addChild(bDefinitionNode1);
    root.addChild(bCallNode1);

    JSModuleDefinition callingModule = new JSModuleDefinition("callingModule");
    when(compiler.getModuleGraph()).thenReturn(new JSModuleGraph(Lists.newArrayList(callingModule)));

    pass.process(null, root);

    JSModuleDefinition definedModule = new JSModuleDefinition("definedModule");
    when(definitionFinder.getModule(bCallNode1)).thenReturn(definedModule);
    when(compiler.getModuleGraph()).thenReturn(new JSModuleGraph(Lists.newArrayList(callingModule, definedModule)));

    pass.process(null, root);

    assertCallSiteUnchanged(bCallNode1);
  }

  @Test
  public void testDoesNotRewriteFunctionsWithVarArgs() {
    Node root = Node.newBlock(1);

    Node aPrototype = Node.newObject("a", null);
    Node aPrototypeProperty = Node.newGetProp("prototype", aPrototype);

    Node bDefinitionNode = Node.newExprAssign(aPrototypeProperty, Node.newFunction(1, Node.newArray("args")));

    Node bCallNode = Node.newGetProp(Node.newGetProp("a", Node.newThis()), "b");

    Node bCallNodeArgs = Node.newArray(Node.newNumber(1), Node.newNumber(2), Node.newNumber(3));

    bCallNode.addChild(bCallNodeArgs);

    root.addChild(aPrototype);
    root.addChild(bDefinitionNode);
    root.addChild(bCallNode);

    pass.process(null, root);

    assertCallSiteUnchanged(bCallNode);
  }

  @Test
  public void testRewritesFunctionsWithFixedNumberOfArguments() {
    Node root = Node.newBlock(1);

    Node aPrototype = Node.newObject("a", null);
    Node aPrototypeProperty = Node.newGetProp("prototype", aPrototype);

    Node bDefinitionNode = Node.newExprAssign(aPrototypeProperty, Node.newFunction(2, Node.newArray("a", "b")));

    Node bCallNode = Node.newGetProp(Node.newGetProp("a", Node.newThis()), "b");

    Node bCallNodeArgs = Node.newArray(Node.newObject("a", Node.newNumber(1)), Node.newNumber(2));

    bCallNode.addChild(bCallNodeArgs);

    root.addChild(aPrototype);
    root.addChild(bDefinitionNode);
    root.addChild(bCallNode);

    Definition definition = new Definition("b", Node.newFunction(2, Lists.newArrayList(Node.newString("a"), Node.newString("b"))), null, null);

    when(definitionFinder.getDefinition(bCallNode)).thenReturn(definition);

    pass.process(null, root);

    Node expectedCallNode = Node.newCall(
        Node.newGetProp(newNode("JSCompiler_StaticMethods_b"), "call"),
        Node.newThis(),
        Node.newObject("a"),
        Node.newNumber(1)
    );

    Node bCallSite = bCallNode.getParent();

    assertCallSiteRewritten(bCallSite, expectedCallNode);
  }

  @Test
  public void testRewritesFunctionWithThisParameterRenamed() {
    Node root = Node.newBlock(1);

    Node aPrototype = Node.newObject("a", null);
    Node aPrototypeProperty = Node.newGetProp("prototype", aPrototype);

    Node bDefinitionNode = Node.newExprAssign(aPrototypeProperty, Node.newFunction(2, Node.newArray("self", "arg1", "arg2")));

    Node bCallNode = Node.newGetProp(Node.newGetProp("a", Node.newThis()), "b");

    Node bCallNodeArgs = Node.newArray(Node.newObject("self", Node.newObject("a", Node.newNumber(1))), Node.newNumber(2), Node.newNumber(3));

    bCallNode.addChild(bCallNodeArgs);

    root.addChild(aPrototype);
    root.addChild(bDefinitionNode);
    root.addChild(bCallNode);

    Definition definition = new Definition("b", Node.newFunction(2, Lists.newArrayList(Node.newString("self"), Node.newString("arg1"), Node.newString("arg2"))), null, null);

    when(definitionFinder.getDefinition(bCallNode)).thenReturn(definition);

    pass.process(null, root);

    Node expectedCallNode = Node.newCall(
        Node.newGetProp(newNode("JSCompiler_StaticMethods_b"), "call"),
        Node.newThis(),
        Node.newObject("self"),
        Node.newObject("a", Node.newNumber(1)),
        Node.newNumber(2)
    );

    Node bCallSite = bCallNode.getParent();

    assertCallSiteRewritten(bCallSite, expectedCallNode);
  }

  private void assertCallSiteRewritten(Node expectedCallSite, Node actualCallSite) {
    Node expectedCallNode = expectedCallSite.getFirstChild();
    Node actualCallNode = actualCallSite.getFirstChild();

    Node expectedNameNode = expectedCallNode.getLastChild();
    Node actualNameNode = actualCallNode.getLastChild();

    assert(expectedNameNode.getString().equals(actualNameNode.getString()));
    assert(expectedCallNode.getChildCount() == actualCallNode.getChildCount());

    for (int i = 0; i < expectedCallNode.getChildCount(); i++) {
      Node expectedArgNode = expectedCallNode.getChild(i);
      Node actualArgNode = actualCallNode.getChild(i);

      assert(expectedArgNode.getClass().equals(actualArgNode.getClass()));
      assert(expectedArgNode.getString().equals(actualArgNode.getString()));
    }
  }

  private void assertCallSiteUnchanged(Node callSite) {
    assertNull(NodeUtil.getFirstChildOfType(callSite, Node.class, Node.NAME));
  }

  private void assertDefinitionRewritten(Node expectedDefinitionSite, Node actualDefinitionSite) {
    Node expectedNameNode = expectedDefinitionSite.getFirstChild();
    Node actualNameNode = actualDefinitionSite.getFirstChild();

    assert(expectedNameNode.getString().equals(actualNameNode.getString()));

    Node expectedFunctionNode = expectedDefinitionSite.getLastChild();
    Node actualFunctionNode = actualDefinitionSite.getLastChild();

    Node expectedRValueNode = expectedFunctionNode.getFirstChild();
    Node actualRValueNode = actualFunctionNode.getFirstChild();

    assert(expectedRValueNode.getClass().equals(actualRValueNode.getClass()));
    assert(expectedRValueNode.getString().equals(actualRValueNode.getString()));

    Node expectedLValueNode = expectedFunctionNode.getLastChild();
    Node actualLValueNode = actualFunctionNode.getLastChild();

    assert(expectedLValueNode.getClass().equals(actualLValueNode.getClass()));
    assert(expectedLValueNode.getString().equals(actualLValueNode.getString()));

    assert(expectedFunctionNode.getJSType() == actualFunctionNode.getJSType());
  }

  private Node newNode(int type, Object... children) {
    return Node.newNode(type, children);
  }
}