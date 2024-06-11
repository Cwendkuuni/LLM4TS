import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.JSDocInfo;

import java.util.List;
import java.util.Set;

public class CollapseVariableDeclarationsTest {

  private Compiler compiler;
  private Node rootNode;
  private Scope scope;

  @org.junit.Before
  public void setUp() {
    compiler = mock(Compiler.class);
    rootNode = new Node(Token.ROOT);
    scope = new Scope(null, null);
  }

  @org.junit.Test
  public void testCollapsingSingleDeclaration() {
    CollapseVariableDeclarations collapseVariableDeclarations =
        new CollapseVariableDeclarations(compiler);
    NodeTraversal nodeTraversal = new NodeTraversal(compiler, rootNode);

    Node varNode = new Node(Token.VAR, "a");
    varNode.addChildToBack(new Node(Token.SEMICOLON));
    rootNode.addChildToBack(varNode);

    collapseVariableDeclarations.process(null, rootNode);

    assertEquals(1, rootNode.getChildCount());
    assertEquals(varNode, rootNode.getFirstChild());
  }

  @org.junit.Test
  public void testCollapsingMultipleDeclarations() {
    CollapseVariableDeclarations collapseVariableDeclarations =
        new CollapseVariableDeclarations(compiler);
    NodeTraversal nodeTraversal = new NodeTraversal(compiler, rootNode);

    Node varNode1 = new Node(Token.VAR, "a");
    varNode1.addChildToBack(new Node(Token.SEMICOLON));
    rootNode.addChildToBack(varNode1);

    Node varNode2 = new Node(Token.VAR, "b");
    varNode2.addChildToBack(new Node(Token.ASSIGN, Token.NUMBER, 1));
    varNode2.addChildToBack(new Node(Token.SEMICOLON));
    rootNode.addChildToBack(varNode2);

    collapseVariableDeclarations.process(null, rootNode);

    assertEquals(1, rootNode.getChildCount());
    assertEquals(varNode1, rootNode.getFirstChild());
    assertEquals("a, b = 1", varNode1.getString());
  }

  @org.junit.Test
  public void testCollapsingMultipleAssignments() {
    CollapseVariableDeclarations collapseVariableDeclarations =
        new CollapseVariableDeclarations(compiler);
    NodeTraversal nodeTraversal = new NodeTraversal(compiler, rootNode);

    Node varNode1 = new Node(Token.VAR, "a");
    varNode1.addChildToBack(new Node(Token.SEMICOLON));
    rootNode.addChildToBack(varNode1);

    Node assignNode1 = new Node(Token.ASSIGN, Token.NAME, "a");
    assignNode1.addChildToBack(new Node(Token.ASSIGN, Token.BOOL, true));
    rootNode.addChildToBack(assignNode1);

    Node varNode2 = new Node(Token.VAR, "b");
    varNode2.addChildToBack(new Node(Token.SEMICOLON));
    rootNode.addChildToBack(varNode2);

    Node assignNode2 = new Node(Token.ASSIGN, Token.NAME, "b");
    assignNode2.addChildToBack(new Node(Token.ASSIGN, Token.BOOL, true));
    rootNode.addChildToBack(assignNode2);

    Node varNode3 = new Node(Token.VAR, "c");
    varNode3.addChildToBack(new Node(Token.SEMICOLON));
    rootNode.addChildToBack(varNode3);

    collapseVariableDeclarations.process(null, rootNode);

    assertEquals(1, rootNode.getChildCount());
    assertEquals(varNode1, rootNode.getFirstChild());
    assertEquals("a = true, b = true, c", varNode1.getString());
  }

  @org.junit.Test
  public void testCollapsingMultiLevelScopes() {
    CollapseVariableDeclarations collapseVariableDeclarations =
        new CollapseVariableDeclarations(compiler);
    NodeTraversal nodeTraversal = new NodeTraversal(compiler, rootNode);

    Node functionNode = new Node(Token.FUNCTION, ImmutableList.of(), ImmutableList.of());
    rootNode.addChildToBack(functionNode);

    Node varNode1 = new Node(Token.VAR, "a");
    varNode1.addChildToBack(new Node(Token.SEMICOLON));
    functionNode.addChildToBack(varNode1);

    Node functionBody = new Node(Token.BLOCK);
    functionNode.addChildToBack(functionBody);

    Node varNode2 = new Node(Token.VAR, "b");
    varNode2.addChildToBack(new Node(Token.ASSIGN, Token.NUMBER, 1));
    varNode2.addChildToBack(new Node(Token.SEMICOLON));
    functionBody.addChildToBack(varNode2);

    Node varNode3 = new Node(Token.VAR, "c");
    varNode3.addChildToBack(new Node(Token.ASSIGN, Token.NUMBER, 2));
    varNode3.addChildToBack(new Node(Token.SEMICOLON));
    functionBody.addChildToBack(varNode3);

    collapseVariableDeclarations.process(null, rootNode);

    assertEquals(1, rootNode.getChildCount());
    assertEquals(functionNode, rootNode.getFirstChild());

    assertEquals(1, functionNode.getChildCount());
    assertEquals(functionBody, functionNode.getFirstChild());

    assertEquals(1, functionBody.getChildCount());
    assertEquals(varNode1, functionBody.getFirstChild());
    assertEquals("a, b = 1, c = 2", varNode1.getString());
  }

  @org.junit.Test
  public void testCollapsingNestedVariables() {
    CollapseVariableDeclarations collapseVariableDeclarations =
        new CollapseVariableDeclarations(compiler);
    NodeTraversal nodeTraversal = new NodeTraversal(compiler, rootNode);

    Node functionNode = new Node(Token.FUNCTION, ImmutableList.of(), ImmutableList.of());
    rootNode.addChildToBack(functionNode);

    Node varNode1 = new Node(Token.VAR, "a");
    varNode1.addChildToBack(new Node(Token.SEMICOLON));
    functionNode.addChildToBack(varNode1);

    Node functionBody = new Node(Token.BLOCK);
    functionNode.addChildToBack(functionBody);

    Node varNode2 = new Node(Token.VAR, "b");
    varNode2.addChildToBack(new Node(Token.ASSIGN, Token.NUMBER, 1));
    varNode2.addChildToBack(new Node(Token.SEMICOLON));
    functionBody.addChildToBack(varNode2);

    Node ifNode = new Node(Token.IF, ImmutableList.of(), ImmutableList.of());
    functionBody.addChildToBack(ifNode);

    Node varNode3 = new Node(Token.VAR, "c");
    varNode3.addChildToBack(new Node(Token.ASSIGN, Token.NUMBER, 2));
    varNode3.addChildToBack(new Node(Token.SEMICOLON));
    ifNode.addChildToBack(varNode3);

    Node elseNode = new Node(Token.ELSE);
    ifNode.addChildToBack(elseNode);

    Node varNode4 = new Node(Token.VAR, "d");
    varNode4.addChildToBack(new Node(Token.ASSIGN, Token.NUMBER, 3));
    varNode4.addChildToBack(new Node(Token.SEMICOLON));
    elseNode.addChildToBack(varNode4);

    collapseVariableDeclarations.process(null, rootNode);

    assertEquals(1, rootNode.getChildCount());
    assertEquals(functionNode, rootNode.getFirstChild());

    assertEquals(1, functionNode.getChildCount());
    assertEquals(functionBody, functionNode.getFirstChild());

    assertEquals(1, functionBody.getChildCount());
    assertEquals(varNode1, functionBody.getFirstChild());
    assertEquals("a, b = 1, c = 2, d = 3", varNode1.getString());
  }

  @org.junit.Test
  public void testCollapsingVariablesWithJSDocs() {
    CollapseVariableDeclarations collapseVariableDeclarations =
        new CollapseVariableDeclarations(compiler);
    NodeTraversal nodeTraversal = new NodeTraversal(compiler, rootNode);

    Node functionNode = new Node(Token.FUNCTION, ImmutableList.of(), ImmutableList.of());
    rootNode.addChildToBack(functionNode);

    Node varNode1 = new Node(Token.VAR, "a");
    varNode1.addChildToBack(new Node(Token.SEMICOLON));
    varNode1.setJSDocInfo(mockJSDocInfo("a", "Description of a"));
    functionNode.addChildToBack(varNode1);

    Node varNode2 = new Node(Token.VAR, "b");
    varNode2.addChildToBack(new Node(Token.ASSIGN, Token.NUMBER, 1));
    varNode2.addChildToBack(new Node(Token.SEMICOLON));
    varNode2.setJSDocInfo(mockJSDocInfo("b", "Description of b"));
    functionNode.addChildToBack(varNode2);

    collapseVariableDeclarations.process(null, rootNode);

    assertEquals(1, rootNode.getChildCount());
    assertEquals(functionNode, rootNode.getFirstChild());

    assertEquals(1, functionNode.getChildCount());
    assertEquals(functionBody, functionNode.getFirstChild());

    assertEquals(1, functionBody.getChildCount());
    assertEquals(varNode1, functionBody.getFirstChild());
    assertEquals("a = 1 // Description of a\n// Description of b", varNode1.getString());
  }

  private JSDocInfo mockJSDocInfo(String name, String description) {
    JSDocInfo info = mock(JSDocInfo.class);
    when(info.getName()).thenReturn(name);
    when(info.getDescription()).thenReturn(description);
    return info;
  }
}