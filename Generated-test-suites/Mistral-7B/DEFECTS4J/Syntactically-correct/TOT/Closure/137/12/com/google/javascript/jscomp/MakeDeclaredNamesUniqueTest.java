package com.google.javascript.jscomp.test;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.test.testTypes.DeclareAndCall;
import com.google.javascript.rhino.TokenStream;

import org.junit.Before;
import org.junit.Test;

public class MakeDeclaredNamesUniqueTest {

  private Compiler compiler;
  private NodeTraversal nodeTraversal;
  private Scope scope;

  @Before
  public void setUp() {
    this.compiler = mock(Compiler.class);
    this.nodeTraversal = mock(NodeTraversal.class);
    this.scope = mock(Scope.class);

    when(compiler.getTraverser()).thenReturn(nodeTraversal);
    when(nodeTraversal.getScope()).thenReturn(scope);
  }

  @Test
  public void testMakeDeclaredNamesUnique_GlobalScope() {
    String code = "function foo() {} var x = 1;";
    Node js = parseCodeToNode(code);

    MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();
    makeDeclaredNamesUnique.traverse(js, null, null);

    Node nameNode = js.getFirstChild();
    assertThat(nameNode.getString()).isEqualTo("foo");

    Node varNode = js.getChildAt(1);
    assertThat(varNode.getType()).isEqualTo(Token.VAR);
    assertThat(varNode.getString()).isEqualTo("x");

    Node functionNode = varNode.getNext();
    assertThat(functionNode.getType()).isEqualTo(Token.FUNCTION);

    nodeTraversal.reset();
    nodeTraversal.traverse(js, null, null);

    List<Node> functionBody = Lists.newArrayList(functionNode.getNext());

    // Check that the variable name is unique
    assertThat(makeDeclaredNamesUnique.getReplacementName("x")).isNotNull();

    // Check that the function name is not changed
    assertThat(functionNode.getString()).isEqualTo("foo");

    // Check that the variable name is used correctly in the function body
    Node varUseNode = findVarUseNode(functionBody, "x");
    assertThat(varUseNode.getString()).isEqualTo(makeDeclaredNamesUnique.getReplacementName("x"));
  }

  @Test
  public void testMakeDeclaredNamesUnique_FunctionScope() {
    String code = "function foo() { var x = 1; }";
    Node js = parseCodeToNode(code);

    MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();
    makeDeclaredNamesUnique.traverse(js, null, null);

    Node functionNode = js.getFirstChild();
    Node functionBody = functionNode.getLastChild();

    // Check that the variable name is unique
    assertThat(makeDeclaredNamesUnique.getReplacementName("x")).isNotNull();

    // Check that the variable name is used correctly in the function body
    Node varUseNode = findVarUseNode(Lists.newArrayList(functionBody), "x");
    assertThat(varUseNode.getString()).isEqualTo(makeDeclaredNamesUnique.getReplacementName("x"));
  }

  @Test
  public void testMakeDeclaredNamesUnique_VarAndFunctionDeclaration() {
    String code = "function foo() { var x = 1; var y = 2; }";
    Node js = parseCodeToNode(code);

    MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();
    makeDeclaredNamesUnique.traverse(js, null, null);

    Node functionNode = js.getFirstChild();
    Node functionBody = functionNode.getLastChild();

    // Check that both variables have unique names
    assertThat(makeDeclaredNamesUnique.getReplacementName("x")).isNotNull();
    assertThat(makeDeclaredNamesUnique.getReplacementName("y")).isNotNull();

    // Check that the variables are used correctly in the function body
    Node varXUseNode = findVarUseNode(Lists.newArrayList(functionBody), "x");
    assertThat(varXUseNode.getString()).isEqualTo(makeDeclaredNamesUnique.getReplacementName("x"));

    Node varYUseNode = findVarUseNode(Lists.newArrayList(functionBody), "y");
    assertThat(varYUseNode.getString()).isEqualTo(makeDeclaredNamesUnique.getReplacementName("y"));
  }

  @Test
  public void testMakeDeclaredNamesUnique_NestedFunction() {
    String code = "function foo() { function bar() {} }";
    Node js = parseCodeToNode(code);

    MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();
    makeDeclaredNamesUnique.traverse(js, null, null);

    Node functionNode = js.getFirstChild();
    Node functionBody = functionNode.getLastChild();

    Node nestedFunctionNode = findFunctionDeclarationNode(functionBody, "bar");
    Node nestedFunctionBody = nestedFunctionNode.getLastChild();

    // Check that the nested function has a unique name
    assertThat(makeDeclaredNamesUnique.getReplacementName("bar")).isNotNull();

    // Check that the nested function name is used correctly in the nested function body
    Node nestedVarUseNode = findVarUseNode(Lists.newArrayList(nestedFunctionBody), "bar");
    assertThat(nestedVarUseNode.getString()).isEqualTo(makeDeclaredNamesUnique.getReplacementName("bar"));
  }

  @Test
  public void testMakeDeclaredNamesUnique_FunctionWithArguments() {
    String code = "function foo(x, y) {}";
    Node js = parseCodeToNode(code);

    MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();
    makeDeclaredNamesUnique.traverse(js, null, null);

    Node functionNode = js.getFirstChild();
    Node functionBody = functionNode.getLastChild();

    // Check that both arguments have unique names
    assertThat(makeDeclaredNamesUnique.getReplacementName("x")).isNotNull();
    assertThat(makeDeclaredNamesUnique.getReplacementName("y")).isNotNull();

    // Check that the arguments are used correctly in the function body
    Node argXUseNode = findVarUseNode(Lists.newArrayList(functionBody), "x");
    assertThat(argXUseNode.getString()).isEqualTo(makeDeclaredNamesUnique.getReplacementName("x"));

    Node argYUseNode = findVarUseNode(Lists.newArrayList(functionBody), "y");
    assertThat(argYUseNode.getString()).isEqualTo(makeDeclaredNamesUnique.getReplacementName("y"));
  }

  @Test
  public void testMakeDeclaredNamesUnique_ScopeWithMultipleDeclarations() {
    String code = "function foo() { var x = 1; var x = 2; }";
    Node js = parseCodeToNode(code);

    MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();
    makeDeclaredNamesUnique.traverse(js, null, null);

    Node functionNode = js.getFirstChild();
    Node functionBody = functionNode.getLastChild();

    // Check that the second declaration of x has a unique name
    assertThat(makeDeclaredNamesUnique.getReplacementName("x")).isNotNull();

    // Check that the first declaration of x is not changed
    Node firstVarNode = findVarDeclarationNode(functionBody, "x");
    assertThat(firstVarNode.getString()).isEqualTo("x");

    // Check that the second declaration of x is renamed
    Node secondVarNode = findVarDeclarationNode(functionBody, makeDeclaredNamesUnique.getReplacementName("x"));
    assertThat(secondVarNode.getString()).isNotNull();
  }

  @Test
  public void testMakeDeclaredNamesUnique_ScopeWithConflictingNames() {
    String code = "function foo() { var x = 1; var y = 1; }";
    Node js = parseCodeToNode(code);

    MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();
    makeDeclaredNamesUnique.traverse(js, null, null);

    Node functionNode = js.getFirstChild();
    Node functionBody = functionNode.getLastChild();

    // Check that both declarations have unique names
    assertThat(makeDeclaredNamesUnique.getReplacementName("x")).isNotNull();
    assertThat(makeDeclaredNamesUnique.getReplacementName("y")).isNotNull();

    // Check that both variables are used correctly in the function body
    Node varXUseNode = findVarUseNode(Lists.newArrayList(functionBody), makeDeclaredNamesUnique.getReplacementName("x"));
    assertThat(varXUseNode.getString()).isEqualTo(makeDeclaredNamesUnique.getReplacementName("x"));

    Node varYUseNode = findVarUseNode(Lists.newArrayList(functionBody), makeDeclaredNamesUnique.getReplacementName("y"));
    assertThat(varYUseNode.getString()).isEqualTo(makeDeclaredNamesUnique.getReplacementName("y"));
  }

  @Test
  public void testMakeDeclaredNamesUnique_ScopeWithConstDeclarations() {
    String code = "function foo() { const x = 1; const x = 2; }";
    Node js = parseCodeToNode(code);

    MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();
    makeDeclaredNamesUnique.traverse(js, null, null);

    Node functionNode = js.getFirstChild();
    Node functionBody = functionNode.getLastChild();

    // Check that both declarations have unique names
    assertThat(makeDeclaredNamesUnique.getReplacementName("x")).isNotNull();

    // Check that the first declaration of x is not changed
    Node firstVarNode = findVarDeclarationNode(functionBody, "x");
    assertThat(firstVarNode.getNodeType()).isEqualTo(Token.CONST);
    assertThat(firstVarNode.getString()).isEqualTo("x");

    // Check that the second declaration of x is renamed
    Node secondVarNode = findVarDeclarationNode(functionBody, makeDeclaredNamesUnique.getReplacementName("x"));
    assertThat(secondVarNode.getNodeType()).isEqualTo(Token.VAR);
    assertThat(secondVarNode.getString()).isEqualTo(makeDeclaredNamesUnique.getReplacementName("x"));
  }

  @Test
  public void testMakeDeclaredNamesUnique_ScopeWithConflictingConstDeclarations() {
    String code = "function foo() { const x = 1; const x = 2; }";
    Node js = parseCodeToNode(code);

    MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();
    makeDeclaredNamesUnique.traverse(js, null, null);

    Node functionNode = js.getFirstChild();
    Node functionBody = functionNode.getLastChild();

    // Check that both declarations have unique names
    assertThat(makeDeclaredNamesUnique.getReplacementName("x")).isNotNull();

    // Check that the first declaration of x is not changed
    Node firstVarNode = findVarDeclarationNode(functionBody, "x");
    assertThat(firstVarNode.getNodeType()).isEqualTo(Token.CONST);
    assertThat(firstVarNode.getString()).isEqualTo("x");

    // Check that the second declaration of x is renamed
    Node secondVarNode = findVarDeclarationNode(functionBody, makeDeclaredNamesUnique.getReplacementName("x"));
    assertThat(secondVarNode.getNodeType()).isEqualTo(Token.VAR);
    assertThat(secondVarNode.getString()).isEqualTo(makeDeclaredNamesUnique.getReplacementName("x"));
  }

  private Node parseCodeToNode(String code) {
    TokenStream ts = new TokenStream();
    ts.putStream(code);
    Node js = new Node(Token.PROGRAM, null, null);
    parser(ts, null, js);
    return js;
  }

  private void parser(TokenStream ts, Node parent, Node node) {
    int type = ts.la();
    if (type == -1) {
      return;
    }

    Node newNode = null;
    switch (type) {
      case Token.PROGRAM:
        newNode = new Node(Token.PROGRAM, parent, null);
        break;
      case Token.BLOCK:
        newNode = new Node(Token.BLOCK, parent, null);
        break;
      case Token.FUNCTION:
        newNode = new Node(Token.FUNCTION, parent, null);
        newNode.setProperty(Node.IS_FUNCTION_DECLARATION, true);
        ts.match(Token.FUNCTION);
        newNode.setProperty(Node.NAME, ts.consumeIdent());
        newNode.setProperty(Node.IS_CONSTANT_NAME, NodeUtil.isFunctionDeclaration(newNode));
        break;
      case Token.VAR:
        newNode = new Node(Token.VAR, null, null);
        newNode.setProperty(Node.IS_CONSTANT_NAME, NodeUtil.isVarDeclaration(newNode));
        newNode.setProperty(Node.NAME, ts.consumeIdent());
        break;
      case Token.NAME:
        newNode = new Node(type, parent, null);
        newNode.setString(ts.consumeIdent());
        break;
      case Token.PUNCTUATION:
        newNode = new Node(type, parent, null);
        ts.consume();
        break;
      default:
        newNode = new Node(type, parent, null);
    }

    if (newNode != null) {
      if (parent != null) {
        parent.addChild(newNode);
      }
      parser(ts, newNode, null);
    }
  }

  private Node findVarDeclarationNode(Node node, String variableName) {
    for (Node child = node.getFirstChild(); child != null; child = child.getNext()) {
      if (child.getNodeType() == Token.VAR && child.getString().equals(variableName)) {
        return child;
      }
      Node result = findVarDeclarationNode(child, variableName);
      if (result != null) {
        return result;
      }
    }
    return null;
  }

  private Node findFunctionDeclarationNode(Node node, String functionName) {
    for (Node child = node.getFirstChild(); child != null; child = child.getNext()) {
      if (child.getNodeType() == Token.FUNCTION && child.getString().equals(functionName)) {
        return child;
      }
      Node result = findFunctionDeclarationNode(child, functionName);
      if (result != null) {
        return result;
      }
    }
    return null;
  }

  private Node findVarUseNode(List<Node> nodes, String variableName) {
    for (Node node : nodes) {
      if (node.getNodeType() == Token.NAME && node.getString().equals(variableName)) {
        return node;
      }
      Node result = findVarUseNode(node.getChildren(), variableName);
      if (result != null) {
        return result;
      }
    }
    return null;
  }
}