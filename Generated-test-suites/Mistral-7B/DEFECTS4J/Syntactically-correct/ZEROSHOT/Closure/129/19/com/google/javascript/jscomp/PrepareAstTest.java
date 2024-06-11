package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.NodeTraversal.Callback;

/**
 * Test class for PrepareAst class.
 */
public class PrepareAstTest {

  @org.junit.Before
  public void setup() {
  }

  /**
   * Test constructor.
   */
  @org.junit.Test
  public void testConstructor() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    PrepareAst prepareAst = new PrepareAst(compiler);
    assertSame(compiler, prepareAst.compiler);
    assertFalse(prepareAst.checkOnly);

    PrepareAst prepareAstCheckOnly = new PrepareAst(compiler, true);
    assertSame(compiler, prepareAstCheckOnly.compiler);
    assertTrue(prepareAstCheckOnly.checkOnly);
  }

  /**
   * Test process method with no externs and root.
   */
  @org.junit.Test
  public void testProcessNoExternsOrRoot() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    PrepareAst prepareAst = new PrepareAst(compiler);
    Node root = null;
    prepareAst.process(null, root);
  }

  /**
   * Test process method with externs but no root.
   */
  @org.junit.Test
  public void testProcessExternsButNoRoot() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    PrepareAst prepareAst = new PrepareAst(compiler);
    Node externs = createMockNode();
    prepareAst.process(externs, null);
  }

  /**
   * Test process method with both externs and root.
   */
  @org.junit.Test
  public void testProcessExternsAndRoot() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    PrepareAst prepareAst = new PrepareAst(compiler);
    Node externs = createMockNode();
    Node root = createMockNode();
    prepareAst.process(externs, root);
  }

  /**
   * Test process method with checkOnly flag set.
   */
  @org.junit.Test
  public void testProcessCheckOnly() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    PrepareAst prepareAst = new PrepareAst(compiler, true);
    Node externs = createMockNode();
    Node root = createMockNode();
    prepareAst.process(externs, root);
  }

  /**
   * Test normalizeNodeTypes method.
   */
  @org.junit.Test
  public void testNormalizeNodeTypes() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    PrepareAst prepareAst = new PrepareAst(compiler);
    Node root = createMockNode();
    prepareAst.normalizeNodeTypes(root);
  }

  /**
   * Test normalizeBlocks method.
   */
  @org.junit.Test
  public void testNormalizeBlocks() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    PrepareAst prepareAst = new PrepareAst(compiler);
    Node root = createMockNode();
    prepareAst.normalizeBlocks(root);
  }

  /**
   * Test shouldTraverse method.
   */
  @org.junit.Test
  public void testShouldTraverse() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    PrepareAst.PrepareAnnotations prepareAnnotations =
        new PrepareAst.PrepareAnnotations();
    NodeTraversal nodeTraversal = mock(NodeTraversal.class);
    NodeTraversal.Callback callback = prepareAnnotations;
    Node node = createMockNode();
    Node parent = createMockNode();

    when(node.isObjectLit()).thenReturn(true);
    when(node.getFirstChild()).thenReturn(createMockNode());

    when(nodeTraversal.getCurrentNode()).thenReturn(node);
    when(nodeTraversal.getParentNode()).thenReturn(parent);

    assertTrue(prepareAnnotations.shouldTraverse(nodeTraversal, node, parent));
  }

  /**
   * Test visit method for object litteral annotations.
   */
  @org.junit.Test
  public void testVisitObjectLiteralAnnotations() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    PrepareAst.PrepareAnnotations prepareAnnotations =
        new PrepareAst.PrepareAnnotations();
    NodeTraversal nodeTraversal = mock(NodeTraversal.class);
    Node node = createMockNode();
    Node parent = createMockNode();

    when(node.isObjectLit()).thenReturn(true);
    when(node.getFirstChild()).thenReturn(createMockNode());

    when(nodeTraversal.getCurrentNode()).thenReturn(node);
    when(nodeTraversal.getParentNode()).thenReturn(parent);

    prepareAnnotations.visit(nodeTraversal, node, parent);
  }

  /**
   * Test visit method for calls.
   */
  @org.junit.Test
  public void testVisitCalls() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    PrepareAst.PrepareAnnotations prepareAnnotations =
        new PrepareAst.PrepareAnnotations();
    NodeTraversal nodeTraversal = mock(NodeTraversal.class);
    Node node = createMockNode();
    Node parent = createMockNode();

    when(node.isCall()).thenReturn(true);
    when(node.getFirstChild()).thenReturn(createMockNode());

    when(nodeTraversal.getCurrentNode()).thenReturn(node);
    when(nodeTraversal.getParentNode()).thenReturn(parent);

    prepareAnnotations.visit(nodeTraversal, node, parent);
  }

  /**
   * Test visit method for dispatchers.
   */
  @org.junit.Test
  public void testVisitDispatchers() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    PrepareAst.PrepareAnnotations prepareAnnotations =
        new PrepareAst.PrepareAnnotations();
    NodeTraversal nodeTraversal = mock(NodeTraversal.class);
    Node node = createMockFunctionNode();
    Node parent = createMockNode();

    when(node.isFunction()).thenReturn(true);
    when(parent.getJSDocInfo()).thenReturn(new JSDocInfo("JavaDispatch"));

    when(nodeTraversal.getCurrentNode()).thenReturn(node);
    when(nodeTraversal.getParentNode()).thenReturn(parent);

    prepareAnnotations.visit(nodeTraversal, node, parent);
  }

  /**
   * Test visit method for object literal key annotations.
   */
  @org.junit.Test
  public void testVisitObjectLiteralKeyAnnotations() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    PrepareAst.PrepareAnnotations prepareAnnotations =
        new PrepareAst.PrepareAnnotations();
    NodeTraversal nodeTraversal = mock(NodeTraversal.class);
    Node objlit = createMockNode();
    Node key = createMockNode();
    Node value = createMockNode();
    Node parent = createMockNode();

    when(objlit.isObjectLit()).thenReturn(true);
    when(key.getJSDocInfo()).thenReturn(new JSDocInfo("MyAnnotation"));
    when(value.isFunction()).thenReturn(true);

    when(nodeTraversal.getCurrentNode()).thenReturn(objlit);
    when(nodeTraversal.getParentNode()).thenReturn(parent);
    when(nodeTraversal.getCurrentNode()).thenReturn(key);
    when(nodeTraversal.getParentNode()).thenReturn(objlit);
    when(nodeTraversal.getCurrentNode()).thenReturn(value);

    prepareAnnotations.visit(nodeTraversal, objlit, parent);

    assertNull(objlit.getJSDocInfo());
    assertSame(key.getJSDocInfo(), value.getJSDocInfo());
  }

  /**
   * Helper method to create mock Node instance.
   */
  private Node createMockNode() {
    return new MockNode();
  }

  /**
   * Helper method to create mock Function Node instance.
   */
  private Node createMockFunctionNode() {
    return new MockFunctionNode();
  }

  /**
   * Mock Node class.
   */
  private static class MockNode extends Node {

    @Override
    public int getType() {
      return Token.UNDEFINED;
    }

    @Override
    public boolean isObjectLit() {
      return false;
    }

    @Override
    public boolean isFunction() {
      return false;
    }

    @Override
    public boolean isCall() {
      return false;
    }

    @Override
    public JSDocInfo getJSDocInfo() {
      return null;
    }

    @Override
    public void setJSDocInfo(JSDocInfo jsDocInfo) {
      // Do nothing
    }

    @Override
    public void putBooleanProp(String prop, boolean value) {
      // Do nothing
    }
  }

  /**
   * Mock Function Node class.
   */
  private static class MockFunctionNode extends Node {

    @Override
    public int getType() {
      return Token.FUNCTION;
    }

    @Override
    public boolean isObjectLit() {
      return false;
    }

    @Override
    public boolean isFunction() {
      return true;
    }

    @Override
    public boolean isCall() {
      return false;
    }

    @Override
    public JSDocInfo getJSDocInfo() {
      return null;
    }

    @Override
    public void setJSDocInfo(JSDocInfo jsDocInfo) {
      // Do nothing
    }

    @Override
    public void putBooleanProp(String prop, boolean value) {
      // Do nothing
    }
  }

  /**
   * JSDocInfo mock class.
   */
  private static class JSDocInfo {

    private String name;

    public JSDocInfo(String name) {
      this.name = name;
    }

    public String getName() {
      return name;
    }
  }
}