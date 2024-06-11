package com.example.junit.tests;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.DefinitionsRemover.Definition;
import com.google.javascript.jscomp.DefinitionsRemover.DefinitionSite;
import com.google.javascript.jscomp.SimpleDefinitionFinder;
import com.google.javascript.jscomp.SimpleDefinitionFinder.UseSite;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.JSTypeRegistry;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.Token;

import java.util.Collection;
import java.util.List;

public class DevirtualizePrototypeMethodsTest {

  private AbstractCompiler compiler;
  private JSTypeRegistry typeRegistry;
  private NodeFactory nodeFactory;

  @ org.junit.Before
  public void setup() {
    this.compiler = mock(AbstractCompiler.class);
    this.typeRegistry = mock(JSTypeRegistry.class);
    this.nodeFactory = mock(NodeFactory.class);
  }

  @ org.junit.Test
  public void testDevirtualizePrototypeMethods() {
    // Given
    CompilerPass pass = new DevirtualizePrototypeMethods(compiler);
    Node root = Node.newBlock("test");
    SimpleDefinitionFinder defFinder = new SimpleDefinitionFinder(compiler);
    defFinder.process(null, root);

    Definition definition = new Definition("Object.prototype.accumulate", null, null);
    DefinitionSite definitionSite = new DefinitionSite(definition, null);
    Preconditions.checkState(isPrototypeMethodDefinition(definitionSite.node));

    when(compiler.getTypeRegistry()).thenReturn(typeRegistry);
    when(compiler.getNodeFactory()).thenReturn(nodeFactory);

    // When
    pass.process(null, root);

    // Then
    List<Node> expectedCallSites = Lists.newArrayList();
    List<Node> actualCallSites = Lists.newArrayList();

    for (Node child = root.getFirstChild(); child != null; child = child.getNext()) {
      if (NodeUtil.isCall(child)) {
        actualCallSites.add(child);
      }
    }

    for (UseSite useSite : defFinder.getUseSites(definition)) {
      Node callSite = useSite.node;
      expectedCallSites.add(callSite);
    }

    for (int i = 0; i < expectedCallSites.size(); i++) {
      Node expectedCallSite = expectedCallSites.get(i);
      Node actualCallSite = actualCallSites.get(i);

      // This check assumes that the order of nodes in the AST is preserved during the test.
      assertEquals(expectedCallSite.getChild(0).getString(), actualCallSite.getChild(0).getString());
      assertEquals(expectedCallSite.getChild(1).getString(), actualCallSite.getChild(1).getString());
      assertEquals(expectedCallSite.getChild(2).getString(), actualCallSite.getChild(2).getString());
    }

    String newMethodName = pass.getRewrittenMethodName("accumulate");
    String self = newMethodName + "$self";

    Node expectedFunctionDefinition = nodeFactory.createFunction(null, null, null, null, null, null);
    Node expectedStaticMethodDefinition = nodeFactory.createVar(
        null,
        Node.newString(Token.NAME, newMethodName),
        Node.newFunction(
            null,
            Node.newList(Node.newString(Token.NAME, self), Node.newList(Node.newString(Token.IDENT, "a"), Node.newString(Token.IDENT, "b"), Node.newString(Token.IDENT, "c"))),
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            Node.newList(Node.newStatement(Node.newExpressionStatement(
                Node.newAssignment(
                    Node.newVar(
                        Node.newList(Node.newIdentifier(self), Node.newIdentifier("total")),
                        Node.newNumber(0)
                    )
                )
            ))),
            Node.newStatement(Node.newExpressionStatement(
                Node.newAssignment(
                    Node.newVar(
                        Node.newList(Node.newIdentifier(self), Node.newIdentifier("total")),
                        Node.newBinop(
                            Node.ADD,
                            Node.newVar(Node.newList(Node.newIdentifier(self), Node.newIdentifier("total"))),
                            Node.newNumber(1)
                        )
                    )
                )
            )),
            null
        )
    );

    Node actualFunctionDefinition = definitionSite.node.getLastChild();

    assertEquals(expectedFunctionDefinition.getChild(0).getChild(0).getString(), actualFunctionDefinition.getChild(0).getChild(0).getString());
    assertEquals(expectedFunctionDefinition.getChild(0).getChild(1).getString(), actualFunctionDefinition.getChild(0).getChild(1).getString());
    assertEquals(expectedFunctionDefinition.getChild(1).getString(), actualFunctionDefinition.getChild(1).getString());
    assertEquals(expectedStaticMethodDefinition.getChild(0).getString(), actualFunctionDefinition.getChild(0).getString());
    assertEquals(expectedStaticMethodDefinition.getChild(1).getChildCount(), actualFunctionDefinition.getChild(0).getChildCount());

    String expectedNewMethodName = "JSCompiler_StaticMethods_accumulate";
    String actualNewMethodName = ((Node) actualFunctionDefinition.getChild(0).getChild(0)).getString();
    assertEquals(expectedNewMethodName, actualNewMethodName);

    ObjectType thisType = typeRegistry.getNativeObjectType(JSTypeNative.UNKNOWN_TYPE);
    FunctionType type = (FunctionType) actualFunctionDefinition.getJSType();

    List<JSType> expectedParameterTypes = Lists.newArrayList(thisType, NodeUtil.integerType, NodeUtil.integerType, NodeUtil.integerType);
    List<JSType> actualParameterTypes = Lists.newArrayList(type.getParameters());

    for (int i = 0; i < expectedParameterTypes.size(); i++) {
      assertEquals(expectedParameterTypes.get(i), actualParameterTypes.get(i));
    }

    JSType expectedReturnType = NodeUtil.integerType;
    JSType actualReturnType = type.getReturnType();

    assertEquals(expectedReturnType, actualReturnType);

    assertEquals(1, actualCallSites.size());
    assertEquals(1, expectedCallSites.size());
  }

  private boolean isPrototypeMethodDefinition(Node node) {
    // TODO(user) Implement this method in the test class
    throw new UnsupportedOperationException();
  }
}