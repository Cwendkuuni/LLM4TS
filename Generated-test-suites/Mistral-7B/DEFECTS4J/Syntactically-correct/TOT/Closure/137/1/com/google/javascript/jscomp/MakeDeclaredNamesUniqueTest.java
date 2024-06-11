package com.google.javascript.jscomp;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static com.google.javascript.rhino.Token.FUNCTION;
import static com.google.javascript.rhino.Token.NAME;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class MakeDeclaredNamesUniqueTest {

  @Mock
  NodeTraversal traversal;

  @Mock
  Compiler compiler;

  @Mock
  Scope scope;

  @InjectMocks
  MakeDeclaredNamesUnique makeDeclaredNamesUnique;

  @Before
  public void setUp() {
    when(traversal.getScope()).thenReturn(scope);
    when(compiler.getOptions()).thenReturn(mock(CompilerOptions.class));
  }

  @Test
  public void testGlobalScope() {
    List<String> initialNames = Arrays.asList("a", "b", "c");
    when(scope.getVars()).thenReturn(ImmutableSet.copyOf(initialNames.toArray(new String[0])));

    makeDeclaredNamesUnique.enterScope(traversal);

    for (Node nameNode : initialNames.stream().map(String::new).map(this::getNode).toList()) {
      makeDeclaredNamesUnique.visit(traversal, null, null);
    }

    makeDeclaredNamesUnique.exitScope(traversal);

    Node rootNode = mock(Node.class);
    when(scope.getRootNode()).thenReturn(rootNode);

    for (Node nameNode : initialNames.stream().map(this::getNode).toList()) {
      makeDeclaredNamesUnique.visit(traversal, nameNode, rootNode);
      String expectedName = getUniqueName(nameNode.getString(), 0);
      verify(nameNode).setString(expectedName);
    }
  }

  @Test
  public void testFunctionScope() {
    List<String> initialNames = Arrays.asList("a", "b", "c");
    when(scope.getVars()).thenReturn(ImmutableSet.copyOf(initialNames.toArray(new String[0])));
    Node functionNode = mock(Node.class);
    when(functionNode.getType()).thenReturn(FUNCTION);
    when(scope.getParentScope()).thenReturn(scope);
    when(scope.getRootNode()).thenReturn(functionNode);

    makeDeclaredNamesUnique.enterScope(traversal);

    for (Node nameNode : initialNames.stream().map(this::getNode).toList()) {
      makeDeclaredNamesUnique.visit(traversal, nameNode, null);
    }

    Node functionParamNode = mock(Node.class);
    Node functionBodyNode = mock(Node.class);
    when(functionNode.getFirstChild()).thenReturn(functionParamNode);
    when(functionNode.getLastChild()).thenReturn(functionBodyNode);

    makeDeclaredNamesUnique.visit(traversal, functionNode, null);

    makeDeclaredNamesUnique.findDeclaredNames(functionBodyNode, null, makeDeclaredNamesUnique.nameStack.peek());

    for (Node nameNode : initialNames.stream().map(this::getNode).toList()) {
      String expectedName = getUniqueName(nameNode.getString(), 0);
      verify(functionParamNode, times(1)).addChild(eq(nodeWithName(expectedName)));
      Node resultNode = makeDeclaredNamesUnique.visit(traversal, nameNode, functionNode);
      verify(nameNode).setString(expectedName);
      verify(functionBodyNode).addChild(eq(resultNode));
    }

    makeDeclaredNamesUnique.exitScope(traversal);
  }

  @Test
  public void testRecursiveFunctionScope() {
    List<String> initialNames = Arrays.asList("a", "b", "c");
    when(scope.getVars()).thenReturn(ImmutableSet.copyOf(initialNames.toArray(new String[0])));
    Node functionNode = mock(Node.class);
    Node recursiveFunctionNode = mock(Node.class);
    when(functionNode.getType()).thenReturn(FUNCTION);
    when(functionNode.getFirstChild()).thenReturn(nodeWithName("params"));
    when(functionNode.getLastChild()).thenReturn(functionBodyNode);
    when(functionBodyNode.getFirstChild()).thenReturn(recursiveFunctionNode);
    when(functionNode.getParentScope()).thenReturn(scope);
    when(scope.getParentScope()).thenReturn(scope);
    when(scope.getRootNode()).thenReturn(functionNode);

    makeDeclaredNamesUnique.enterScope(traversal);

    for (Node nameNode : initialNames.stream().map(this::getNode).toList()) {
      makeDeclaredNamesUnique.visit(traversal, nameNode, null);
    }

    makeDeclaredNamesUnique.visit(traversal, recursiveFunctionNode, functionNode);

    for (Node nameNode : initialNames.stream().map(this::getNode).toList()) {
      String expectedName = getUniqueName(nameNode.getString(), 0);
      verify(functionNode, times(1)).addChild(eq(nodeWithName(expectedName)));
      Node resultNode = makeDeclaredNamesUnique.visit(traversal, nameNode, functionNode);
      verify(nameNode).setString(expectedName);
      verify(functionBodyNode).addChild(eq(resultNode));
    }

    makeDeclaredNamesUnique.exitScope(traversal);
  }

  @Test
  public void testGlobalScopeWithDuplicateNames() {
    List<String> initialNames = Arrays.asList("a", "a", "c");
    when(scope.getVars()).thenReturn(ImmutableSet.copyOf(initialNames.toArray(new String[0])));

    makeDeclaredNamesUnique.enterScope(traversal);

    for (Node nameNode : initialNames.stream().map(this::getNode).toList()) {
      makeDeclaredNamesUnique.visit(traversal, nameNode, null);
    }

    makeDeclaredNamesUnique.exitScope(traversal);

    Node rootNode = mock(Node.class);
    when(scope.getRootNode()).thenReturn(rootNode);

    for (Node nameNode : initialNames.stream().map(this::getNode).toList()) {
      String expectedName = getUniqueName(nameNode.getString(), 1);
      verify(nameNode).setString(expectedName);
    }
  }

  @Test
  public void testFunctionScopeWithDuplicateNames() {
    List<String> initialNames = Arrays.asList("a", "a", "c");
    when(scope.getVars()).thenReturn(ImmutableSet.copyOf(initialNames.toArray(new String[0])));
    Node functionNode = mock(Node.class);
    when(functionNode.getType()).thenReturn(FUNCTION);
    when(scope.getParentScope()).thenReturn(scope);
    when(scope.getRootNode()).thenReturn(functionNode);

    makeDeclaredNamesUnique.enterScope(traversal);

    for (Node nameNode : initialNames.stream().map(this::getNode).toList()) {
      makeDeclaredNamesUnique.visit(traversal, nameNode, null);
    }

    Node functionParamNode = mock(Node.class);
    Node functionBodyNode = mock(Node.class);
    when(functionNode.getFirstChild()).thenReturn(functionParamNode);
    when(functionNode.getLastChild()).thenReturn(functionBodyNode);

    makeDeclaredNamesUnique.visit(traversal, functionNode, null);

    makeDeclaredNamesUnique.findDeclaredNames(functionBodyNode, null, makeDeclaredNamesUnique.nameStack.peek());

    for (Node nameNode : initialNames.stream().map(this::getNode).toList()) {
      String expectedName = getUniqueName(nameNode.getString(), 1);
      verify(functionParamNode, times(1)).addChild(eq(nodeWithName(expectedName)));
      Node resultNode = makeDeclaredNamesUnique.visit(traversal, nameNode, functionNode);
      verify(nameNode).setString(expectedName);
      verify(functionBodyNode).addChild(eq(resultNode));
    }

    makeDeclaredNamesUnique.exitScope(traversal);
  }

  @Test
  public void testFunctionScopeWithRecursiveDuplicateName() {
    List<String> initialNames = Arrays.asList("a", "a");
    when(scope.getVars()).thenReturn(ImmutableSet.copyOf(initialNames.toArray(new String[0])));
    Node functionNode = mock(Node.class);
    Node recursiveFunctionNode = mock(Node.class);
    when(functionNode.getType()).thenReturn(FUNCTION);
    when(functionNode.getFirstChild()).thenReturn(nodeWithName("params"));
    when(functionNode.getLastChild()).thenReturn(functionBodyNode);
    when(functionBodyNode.getFirstChild()).thenReturn(recursiveFunctionNode);
    when(functionNode.getParentScope()).thenReturn(scope);
    when(scope.getParentScope()).thenReturn(scope);
    when(scope.getRootNode()).thenReturn(functionNode);

    makeDeclaredNamesUnique.enterScope(traversal);

    for (Node nameNode : initialNames.stream().map(this::getNode).toList()) {
      makeDeclaredNamesUnique.visit(traversal, nameNode, null);
    }

    makeDeclaredNamesUnique.visit(traversal, recursiveFunctionNode, functionNode);

    String expectedName = getUniqueName("a", 2);
    verify(functionNode, times(1)).addChild(eq(nodeWithName(expectedName)));
    Node resultNode = makeDeclaredNamesUnique.visit(traversal, nodeWithName("a"), functionNode);
    verify(nodeWithName("a")).setString(expectedName);
    verify(functionBodyNode).addChild(eq(resultNode));

    makeDeclaredNamesUnique.exitScope(traversal);
  }

  @Test(expected = AssertionError.class)
  public void testFunctionScopeWithConflictingGlobalName() {
    List<String> initialNames = Arrays.asList("a");
    when(scope.getVars()).thenReturn(ImmutableSet.copyOf(initialNames.toArray(new String[0])));
    Node functionNode = mock(Node.class);
    when(functionNode.getType()).thenReturn(FUNCTION);
    when(scope.getParentScope()).thenReturn(scope);
    when(scope.getRootNode()).thenReturn(functionNode);

    makeDeclaredNamesUnique.enterScope(traversal);

    for (Node nameNode : initialNames.stream().map(this::getNode).toList()) {
      makeDeclaredNamesUnique.visit(traversal, nameNode, null);
    }

    Node globalNameNode = nodeWithName("a");
    makeDeclaredNamesUnique.visit(traversal, globalNameNode, null);

    makeDeclaredNamesUnique.exitScope(traversal);
  }

  @Test(expected = AssertionError.class)
  public void testFunctionScopeWithConflictingLocalName() {
    List<String> initialNames = Arrays.asList("a");
    when(scope.getVars()).thenReturn(ImmutableSet.copyOf(initialNames.toArray(new String[0])));
    Node functionNode = mock(Node.class);
    when(functionNode.getType()).thenReturn(FUNCTION);
    Node localNameNode = nodeWithName("a");
    when(scope.getParentScope()).thenReturn(scope);
    when(scope.getRootNode()).thenReturn(functionNode);

    makeDeclaredNamesUnique.enterScope(traversal);

    for (Node nameNode : initialNames.stream().map(this::getNode).toList()) {
      makeDeclaredNamesUnique.visit(traversal, nameNode, null);
    }

    makeDeclaredNamesUnique.visit(traversal, localNameNode, functionNode);

    makeDeclaredNamesUnique.exitScope(traversal);
  }

  private Node nodeWithName(String name) {
    Node node = mock(Node.class);
    when(node.getType()).thenReturn(NAME);
    when(node.getString()).thenReturn(name);
    return node;
  }

  private Node getNode(String name) {
    Node node = newNode(NAME, name);
    return node;
  }

  private Node newNode(int type, String value) {
    return NodeFactory.createTokenNode(new Token(type, value, 0, 0));
  }

  private String getUniqueName(String name, int id) {
    return name + ContextualRenamer.UNIQUE_ID_SEPARATOR + id;
  }
}