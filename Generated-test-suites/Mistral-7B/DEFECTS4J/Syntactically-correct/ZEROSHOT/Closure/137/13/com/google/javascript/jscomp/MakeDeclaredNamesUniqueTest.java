package com.google.javascript.jscomp.test;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.jscomp.testutils.TestCompiler;
import com.google.javascript.rhino.NodeTraversal;
import com.google.javascript.rhino.Token;
import com.google.javascript.rhino.TokenStream;

import java.util.List;
import java.util.Map;

public class MakeDeclaredNamesUniqueTest {

  private static class DummyRenamer implements MakeDeclaredNamesUnique.Renamer {
    private List<String> declaredNames = Lists.newArrayList();

    @Override
    public void addDeclaredName(String name) {
      declaredNames.add(name);
    }

    @Override
    public String getReplacementName(String oldName) {
      return oldName;
    }

    @Override
    public boolean stripConstIfReplaced() {
      return false;
    }

    @Override
    public Renamer forChildScope() {
      return this;
    }
  }

  @org.junit.Test
  public void testEnterScope() {
    MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();

    NodeTraversal nodeTraversal = mock(NodeTraversal.class);
    TestCompiler compiler = new TestCompiler();

    Node root = new Node(Token.PROGRAM);
    MakeDeclaredNamesUnique.enterScope(nodeTraversal, root, compiler);

    MakeDeclaredNamesUnique.Renamer renamer = makeDeclaredNamesUnique.nameStack.peek();
    assertThat(renamer).isInstanceOf(MakeDeclaredNamesUnique.ContextualRenamer.class);
  }

  @org.junit.Test
  public void testExitScope() {
    MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();

    NodeTraversal nodeTraversal = mock(NodeTraversal.class);
    TestCompiler compiler = new TestCompiler();

    Node root = new Node(Token.PROGRAM);
    MakeDeclaredNamesUnique.enterScope(nodeTraversal, root, compiler);
    MakeDeclaredNamesUnique.exitScope(nodeTraversal);

    assertThat(makeDeclaredNamesUnique.nameStack.size()).isEqualTo(0);
  }

  @org.junit.Test
  public void testShouldTraverseFunctionCall() {
    MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();

    NodeTraversal nodeTraversal = mock(NodeTraversal.class);
    TestCompiler compiler = new TestCompiler();

    Node functionCall = new Node(Token.CALL, new Node(Token.NAME, "func"));
    Node parent = new Node(Token.NAME, "parent");

    when(nodeTraversal.getScopeRoot()).thenReturn(parent);
    when(nodeTraversal.getScope()).thenReturn(mock(Scope.class));

    MakeDeclaredNamesUnique.shouldTraverse(nodeTraversal, functionCall, parent);

    Renamer renamer = makeDeclaredNamesUnique.nameStack.peek();
    MakeDeclaredNamesUnique.Renamer functionRenamer = renamer.forChildScope();
    functionRenamer.addDeclaredName("func");

    MakeDeclaredNamesUnique.Renamer parentRenamer = renamer.forChildScope();
    parentRenamer.addDeclaredName("parent");
  }

  @org.junit.Test
  public void testVisitName() {
    MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();

    NodeTraversal nodeTraversal = mock(NodeTraversal.class);
    TestCompiler compiler = new TestCompiler();

    Node nameNode = new Node(Token.NAME, "name");
    Node parent = new Node(Token.NAME, "parent");

    when(nodeTraversal.getScopeRoot()).thenReturn(parent);
    when(nodeTraversal.getScope()).thenReturn(mock(Scope.class));

    MakeDeclaredNamesUnique.visit(nodeTraversal, nameNode, parent);

    Renamer renamer = makeDeclaredNamesUnique.nameStack.peek();
    String replacementName = renamer.getReplacementName("name");
    assertThat(replacementName).isNull();
  }

  @org.junit.Test
  public void testVisitFunction() {
    MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();

    NodeTraversal nodeTraversal = mock(NodeTraversal.class);
    TestCompiler compiler = new TestCompiler();

    Node functionNode = new Node(Token.FUNCTION, null, null, null);
    Node parent = new Node(Token.NAME, "parent");

    when(nodeTraversal.getScopeRoot()).thenReturn(parent);
    when(nodeTraversal.getScope()).thenReturn(mock(Scope.class));

    MakeDeclaredNamesUnique.visit(nodeTraversal, functionNode, parent);

    Renamer renamer = makeDeclaredNamesUnique.nameStack.peek();
    Renamer functionRenamer = renamer.forChildScope();
    functionRenamer.addDeclaredName("func");

    MakeDeclaredNamesUnique.exitScope(nodeTraversal);

    Renamer parentRenamer = renamer.forChildScope();
    parentRenamer.addDeclaredName("parent");
  }

  @org.junit.Test
  public void testVisitCatch() {
    MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();

    NodeTraversal nodeTraversal = mock(NodeTraversal.class);
    TestCompiler compiler = new TestCompiler();

    Node catchNode = new Node(Token.CATCH, null, null, null);
    Node parent = new Node(Token.NAME, "parent");

    when(nodeTraversal.getScopeRoot()).thenReturn(parent);
    when(nodeTraversal.getScope()).thenReturn(mock(Scope.class));

    MakeDeclaredNamesUnique.visit(nodeTraversal, catchNode, parent);

    Renamer renamer = makeDeclaredNamesUnique.nameStack.peek();
    String replacementName = renamer.getReplacementName("catch");
    assertThat(replacementName).isNull();

    MakeDeclaredNamesUnique.exitScope(nodeTraversal);

    Renamer parentRenamer = renamer.forChildScope();
    parentRenamer.addDeclaredName("parent");
  }

  @org.junit.Test
  public void testGetReplacementName() {
    MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();

    DummyRenamer renamer = new DummyRenamer();
    makeDeclaredNamesUnique.nameStack.push(renamer);

    String oldName = "oldName";
    String replacementName = makeDeclaredNamesUnique.getReplacementName(oldName);
    assertThat(replacementName).isNull();
  }

  @org.junit.Test
  public void testFindDeclaredNames() {
    MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();

    NodeTraversal nodeTraversal = mock(NodeTraversal.class);
    TestCompiler compiler = new TestCompiler();

    Node node = new Node(Token.BLOCK, null, null, null);
    Node parent = new Node(Token.NAME, "parent");

    when(nodeTraversal.getScopeRoot()).thenReturn(parent);
    when(nodeTraversal.getScope()).thenReturn(mock(Scope.class));

    MakeDeclaredNamesUnique.findDeclaredNames(node, null, renamer(makeDeclaredNamesUnique));

    List<String> declaredNames = renamer().getDeclaredNames();
    assertThat(declaredNames).isEmpty();
  }

  @org.junit.Test
  public void testFindDeclaredNamesWithVar() {
    MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();

    NodeTraversal nodeTraversal = mock(NodeTraversal.class);
    TestCompiler compiler = new TestCompiler();

    Node varNode = createVarNode("varName", null, null, null);
    Node parent = new Node(Token.NAME, "parent");

    when(nodeTraversal.getScopeRoot()).thenReturn(parent);
    when(nodeTraversal.getScope()).thenReturn(mock(Scope.class));

    MakeDeclaredNamesUnique.findDeclaredNames(varNode, null, renamer(makeDeclaredNamesUnique));

    List<String> declaredNames = renamer().getDeclaredNames();
    assertThat(declaredNames).containsExactly("varName");
  }

  @org.junit.Test
  public void testFindDeclaredNamesWithFunction() {
    MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();

    NodeTraversal nodeTraversal = mock(NodeTraversal.class);
    TestCompiler compiler = new TestCompiler();

    Node functionNode = new Node(Token.FUNCTION, null, null, null);
    Node parent = new Node(Token.NAME, "parent");

    when(nodeTraversal.getScopeRoot()).thenReturn(parent);
    when(nodeTraversal.getScope()).thenReturn(mock(Scope.class));

    Node functionNameNode = new Node(Token.NAME, "funcName");
    functionNode.setFirstChild(functionNameNode);

    when(functionNameNode.getValue()).thenReturn("funcName");

    MakeDeclaredNamesUnique.findDeclaredNames(functionNode, null, renamer(makeDeclaredNamesUnique));

    List<String> declaredNames = renamer().getDeclaredNames();
    assertThat(declaredNames).containsExactly("funcName");
  }

  @org.junit.Test
  public void testFindDeclaredNamesWithVarAndFunction() {
    MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();

    NodeTraversal nodeTraversal = mock(NodeTraversal.class);
    TestCompiler compiler = new TestCompiler();

    Node varNode = createVarNode("varName", null, null, null);
    Node functionNode = new Node(Token.FUNCTION, null, null, null);
    Node parent = new Node(Token.NAME, "parent");

    when(nodeTraversal.getScopeRoot()).thenReturn(parent);
    when(nodeTraversal.getScope()).thenReturn(mock(Scope.class));

    Node varNameNode = varNode.getFirstChild();
    Node functionNameNode = functionNode.getFirstChild();

    when(varNameNode.getValue()).thenReturn("varName");
    when(functionNameNode.getValue()).thenReturn("funcName");

    MakeDeclaredNamesUnique.findDeclaredNames(functionNode, varNode, renamer(makeDeclaredNamesUnique));

    List<String> declaredNames = renamer().getDeclaredNames();
    assertThat(declaredNames).containsExactly("funcName");
  }

  @org.junit.Test
  public void testGetUniqueName() {
    MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();

    DummyRenamer renamer = new DummyRenamer();
    makeDeclaredNamesUnique.nameStack.push(renamer);

    String name = "name";
    String uniqueName = makeDeclaredNamesUnique.getUniqueName(name);

    assertThat(uniqueName).isNotNull();
    assertThat(uniqueName).matches("^" + name + "(\\d+)$");
  }

  private static Node createVarNode(String name, Node initializer, Node leftHandSide, Node condition) {
    Node varNode = new Node(Token.VAR, null, null, null);
    varNode.setFirstChild(new Node(Token.NAME, name));
    varNode.setProperty(Node.IS_INITIALIZER, initializer != null);
    varNode.setProperty(Node.IS_VAR_DECL, true);
    varNode.setProperty(Node.IS_CONSTANT_NAME, false);
    varNode.setProperty(Node.IS_DELETED, false);
    varNode.setProperty(Node.IS_ASSIGNMENT, false);
    varNode.setProperty(Node.IS_ASSIGNMENT_RHS, leftHandSide != null);
    varNode.setProperty(Node.IS_FOR_INIT, condition != null);
    varNode.setProperty(Node.IS_FOR_UPDATE, false);
    varNode.setProperty(Node.IS_LET, false);
    varNode.setProperty(Node.IS_TYPEONLY, false);
    varNode.setProperty(Node.IS_ITERATOR, false);
    varNode.setProperty(Node.IS_SWITCH_DEFAULT, false);
    varNode.setProperty(Node.IS_SWITCH_CASE, false);
    varNode.setProperty(Node.IS_TRY, false);
    varNode.setProperty(Node.IS_TRY_FINALLY, false);
    varNode.setProperty(Node.IS_TRY_CATCH, false);
    varNode.setProperty(Node.IS_TRY_CATCH_VAR, false);

    return varNode;
  }

  private static MakeDeclaredNamesUnique.Renamer renamer(
      MakeDeclaredNamesUnique makeDeclaredNamesUnique) {
    return makeDeclaredNamesUnique.nameStack.peek();
  }

  private static MakeDeclaredNamesUnique.Renamer renamer() {
    return new DummyRenamer();
  }
}