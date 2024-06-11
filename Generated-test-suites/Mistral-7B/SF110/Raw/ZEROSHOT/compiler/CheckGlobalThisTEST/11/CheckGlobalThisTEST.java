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

import org.junit.Test;

public class CheckGlobalThisTest {

  @Test
  public void testShouldTraverseFunction() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CheckGlobalThis checker = new CheckGlobalThis(compiler);

    Node functionNode = Node.newFunction("testFunction", null, null, null, null);

    Node functionParent = Node.newBlock("script");
    functionParent.addChildToEnd(functionNode);

    NodeTraversal traversal = mock(NodeTraversal.class);
    when(traversal.getCurrentScope()).thenReturn(null);

    assertTrue(checker.shouldTraverse(traversal, functionNode, functionParent));
  }

  @Test
  public void testShouldNotTraverseConstructorFunction() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CheckGlobalThis checker = new CheckGlobalThis(compiler);

    Node constructorNode = Node.newFunction("testFunction", null, null, null, null);
    JSDocInfo constructorJsDoc = Node.newJSDocInfo("constructor");
    constructorNode.setJSDocInfo(constructorJsDoc);

    Node functionParent = Node.newBlock("script");
    functionParent.addChildToEnd(constructorNode);

    NodeTraversal traversal = mock(NodeTraversal.class);
    when(traversal.getCurrentScope()).thenReturn(null);

    assertFalse(checker.shouldTraverse(traversal, constructorNode, functionParent));
  }

  @Test
  public void testShouldNotTraverseFunctionWithThisAnnotation() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CheckGlobalThis checker = new CheckGlobalThis(compiler);

    Node functionNode = Node.newFunction("testFunction", null, null, null, null);
    JSDocInfo functionJsDoc = Node.newJSDocInfo("@this", null);
    functionNode.setJSDocInfo(functionJsDoc);

    Node functionParent = Node.newBlock("script");
    functionParent.addChildToEnd(functionNode);

    NodeTraversal traversal = mock(NodeTraversal.class);
    when(traversal.getCurrentScope()).thenReturn(null);

    assertFalse(checker.shouldTraverse(traversal, functionNode, functionParent));
  }

  @Test
  public void testShouldNotTraverseFunctionWithNoParent() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CheckGlobalThis checker = new CheckGlobalThis(compiler);

    Node functionNode = Node.newFunction("testFunction", null, null, null, null);

    NodeTraversal traversal = mock(NodeTraversal.class);
    when(traversal.getCurrentScope()).thenReturn(null);

    assertFalse(checker.shouldTraverse(traversal, functionNode, null));
  }

  @Test
  public void testShouldNotTraverseFunctionWithPrototypePropertyAccess() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CheckGlobalThis checker = new CheckGlobalThis(compiler);

    Node functionNode = Node.newFunction("testFunction", null, null, null, null);

    Node functionParent = Node.newBlock("script");
    Node prototypeProperty = Node.newName("prototype");
    Node prototypePropertyAccess = Node.newGetProp(functionParent, prototypeProperty);
    prototypePropertyAccess.addChildToEnd(functionNode);

    NodeTraversal traversal = mock(NodeTraversal.class);
    when(traversal.getCurrentScope()).thenReturn(null);

    assertFalse(checker.shouldTraverse(traversal, functionNode, functionParent));
  }

  @Test
  public void testShouldNotTraverseFunctionWithPrototypeSubPropertyAccess() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CheckGlobalThis checker = new CheckGlobalThis(compiler);

    Node functionNode = Node.newFunction("testFunction", null, null, null, null);

    Node functionParent = Node.newBlock("script");
    Node prototypeProperty = Node.newName("prototype");
    Node prototypePropertyAccess = Node.newGetProp(functionParent, prototypeProperty);
    Node subProperty = Node.newString("subProperty");
    Node subPropertyAccess = Node.newGetProp(prototypePropertyAccess, subProperty);
    subPropertyAccess.addChildToEnd(functionNode);

    NodeTraversal traversal = mock(NodeTraversal.class);
    when(traversal.getCurrentScope()).thenReturn(null);

    assertFalse(checker.shouldTraverse(traversal, functionNode, functionParent));
  }

  @Test
  public void testShouldTraverseFunctionWithNoParentOrPrototypePropertyAccess() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CheckGlobalThis checker = new CheckGlobalThis(compiler);

    Node functionNode = Node.newFunction("testFunction", null, null, null, null);

    NodeTraversal traversal = mock(NodeTraversal.class);
    when(traversal.getCurrentScope()).thenReturn(null);

    assertTrue(checker.shouldTraverse(traversal, functionNode, null));
  }

  @Test
  public void testShouldReportThis() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CheckGlobalThis checker = new CheckGlobalThis(compiler);

    Node thisNode = Node.newThis();

    Node functionParent = Node.newBlock("script");

    NodeTraversal traversal = mock(NodeTraversal.class);
    when(traversal.getCurrentScope()).thenReturn(null);

    assertTrue(checker.shouldReportThis(thisNode));
  }

  @Test
  public void testShouldNotReportThisInsideFunction() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CheckGlobalThis checker = new CheckGlobalThis(compiler);

    Node functionNode = Node.newFunction("testFunction", null, null, null, null);

    Node functionParent = Node.newBlock("script");
    functionParent.addChildToEnd(functionNode);

    Node thisNode = Node.newThis();
    Node assignment = Node.newAssign(functionParent, thisNode);
    assignment.addChildToEnd(Node.newName("testVariable"));

    NodeTraversal traversal = mock(NodeTraversal.class);
    when(traversal.getCurrentScope()).thenReturn(null);

    assertFalse(checker.shouldReportThis(thisNode));
  }

  @Test
  public void testShouldNotReportThisInsidePropertyAccess() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CheckGlobalThis checker = new CheckGlobalThis(compiler);

    Node functionNode = Node.newFunction("testFunction", null, null, null, null);

    Node functionParent = Node.newBlock("script");
    functionParent.addChildToEnd(functionNode);

    Node thisNode = Node.newThis();
    Node getProp = Node.newGetProp(functionParent, Node.newName("prototype"));
    getProp.addChildToEnd(thisNode);

    NodeTraversal traversal = mock(NodeTraversal.class);
    when(traversal.getCurrentScope()).thenReturn(null);

    assertFalse(checker.shouldReportThis(thisNode));
  }

  @Test
  public void testGetFunctionJsDocInfo() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CheckGlobalThis checker = new CheckGlobalThis(compiler);

    Node functionNode = Node.newFunction("testFunction", null, null, null, null);

    Node functionParent = Node.newBlock("script");
    functionParent.addChildToEnd(functionNode);

    NodeTraversal traversal = mock(NodeTraversal.class);
    when(traversal.getCurrentScope()).thenReturn(null);

    JSDocInfo jsDocInfo = checker.getFunctionJsDocInfo(functionNode);

    assertNull(jsDocInfo);

    functionNode.setJSDocInfo(Node.newJSDocInfo());

    jsDocInfo = checker.getFunctionJsDocInfo(functionNode);

    assertEquals("testFunction", jsDocInfo.getName());
    assertNull(jsDocInfo.getDescription());
    assertNull(jsDocInfo.getParameters());
    assertNull(jsDocInfo.getReturnType());
    assertNull(jsDocInfo.getTypes());
    assertNull(jsDocInfo.getAnnotations());
  }

  @Test
  public void testGetFunctionJsDocInfoWithConstructorAnnotation() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CheckGlobalThis checker = new CheckGlobalThis(compiler);

    Node functionNode = Node.newFunction("testFunction", null, null, null, null);

    Node functionParent = Node.newBlock("script");
    functionParent.addChildToEnd(functionNode);

    Node constructorAnnotation = Node.newJSDocAnnotation("constructor");
    functionNode.setJSDocInfo(Node.newJSDocInfo(constructorAnnotation));

    NodeTraversal traversal = mock(NodeTraversal.class);
    when(traversal.getCurrentScope()).thenReturn(null);

    JSDocInfo jsDocInfo = checker.getFunctionJsDocInfo(functionNode);

    assertEquals("testFunction", jsDocInfo.getName());
    assertNull(jsDocInfo.getDescription());
    assertNull(jsDocInfo.getParameters());
    assertNull(jsDocInfo.getReturnType());
    assertNull(jsDocInfo.getTypes());
    assertTrue(jsDocInfo.hasConstructor());
  }

  @Test
  public void testGetFunctionJsDocInfoWithThisAnnotation() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CheckGlobalThis checker = new CheckGlobalThis(compiler);

    Node functionNode = Node.newFunction("testFunction", null, null, null, null);

    Node functionParent = Node.newBlock("script");
    functionParent.addChildToEnd(functionNode);

    Node thisAnnotation = Node.newJSDocAnnotation("@this", Node.newName("MyClass"));
    functionNode.setJSDocInfo(Node.newJSDocInfo(thisAnnotation));

    NodeTraversal traversal = mock(NodeTraversal.class);
    when(traversal.getCurrentScope()).thenReturn(null);

    JSDocInfo jsDocInfo = checker.getFunctionJsDocInfo(functionNode);

    assertEquals("testFunction", jsDocInfo.getName());
    assertNull(jsDocInfo.getDescription());
    assertNull(jsDocInfo.getParameters());
    assertNull(jsDocInfo.getReturnType());
    assertNull(jsDocInfo.getTypes());
    assertEquals("MyClass", jsDocInfo.getThisType());
  }

  @Test
  public void testGetFunctionJsDocInfoWithNoAnnotation() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CheckGlobalThis checker = new CheckGlobalThis(compiler);

    Node functionNode = Node.newFunction("testFunction", null, null, null, null);

    Node functionParent = Node.newBlock("script");
    functionParent.addChildToEnd(functionNode);

    NodeTraversal traversal = mock(NodeTraversal.class);
    when(traversal.getCurrentScope()).thenReturn(null);

    JSDocInfo jsDocInfo = checker.getFunctionJsDocInfo(functionNode);

    assertEquals("testFunction", jsDocInfo.getName());
    assertNull(jsDocInfo.getDescription());
    assertNull(jsDocInfo.getParameters());
    assertNull(jsDocInfo.getReturnType());
    assertNull(jsDocInfo.getTypes());
    assertFalse(jsDocInfo.hasConstructor());
    assertFalse(jsDocInfo.hasThisType());
  }
}