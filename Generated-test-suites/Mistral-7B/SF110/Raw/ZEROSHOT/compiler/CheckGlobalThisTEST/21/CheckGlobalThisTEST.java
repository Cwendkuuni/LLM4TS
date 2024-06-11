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
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.Token;
import org.junit.Test;

public class CheckGlobalThisTest {

  private AbstractCompiler compiler = mock(AbstractCompiler.class);
  private NodeFactory nodeFactory = mock(NodeFactory.class);
  private NodeTraversal traversal = new NodeTraversal(nodeFactory, compiler, null);

  @Test
  public void testShouldTraverseFunctionAnnotatedWithConstructor() {
    Node functionNode = createNode("function x() {}");
    when(compiler.getFactory()).thenReturn(nodeFactory);
    when(functionNode.getJSDocInfo()).thenReturn(createFunctionJsDocInfo("@constructor"));

    CheckGlobalThis checker = new CheckGlobalThis(compiler);
    assertTrue(checker.shouldTraverse(traversal, functionNode, null));
  }

  @Test
  public void testShouldTraverseFunctionAnnotatedWithThis() {
    Node functionNode = createNode("function x() { @this }");
    when(compiler.getFactory()).thenReturn(nodeFactory);
    when(functionNode.getJSDocInfo()).thenReturn(createFunctionJsDocInfo("@this"));

    CheckGlobalThis checker = new CheckGlobalThis(compiler);
    assertTrue(checker.shouldTraverse(traversal, functionNode, null));
  }

  @Test
  public void testShouldTraverseFunctionWithoutJSDoc() {
    Node functionNode = createNode("function x() {}");
    when(compiler.getFactory()).thenReturn(nodeFactory);

    CheckGlobalThis checker = new CheckGlobalThis(compiler);
    assertTrue(checker.shouldTraverse(traversal, functionNode, null));
  }

  @Test
  public void testShouldTraverseFunctionWithPrototype() {
    Node functionNode = createNode("function x() {}");
    Node prototypeNode = createNode("x.prototype");
    Node objectLiteralNode = createNode("{x: function x() {}}");
    when(compiler.getFactory()).thenReturn(nodeFactory);

    when(functionNode.getParent()).thenReturn(objectLiteralNode);
    when(objectLiteralNode.getFirstChild()).thenReturn(prototypeNode);

    CheckGlobalThis checker = new CheckGlobalThis(compiler);
    assertTrue(checker.shouldTraverse(traversal, functionNode, null));
  }

  @Test
  public void testShouldTraverseLeftSideOfAssignment() {
    Node assignNode = createNode("a = this");
    Node leftSide = assignNode.getFirstChild();

    CheckGlobalThis checker = new CheckGlobalThis(compiler);
    assertTrue(checker.shouldReportThis(leftSide));
  }

  @Test
  public void testShouldTraverseRightSideOfAssignment() {
    Node assignNode = createNode("a = this.x");
    Node rightSide = assignNode.getLastChild();

    CheckGlobalThis checker = new CheckGlobalThis(compiler);
    assertFalse(checker.shouldReportThis(rightSide));
  }

  @Test
  public void testShouldTraverseGetPropertyWithPrototype() {
    Node getNode = createNode("a.x.prototype");

    CheckGlobalThis checker = new CheckGlobalThis(compiler);
    assertTrue(checker.shouldReportThis(getNode.getLastChild()));
  }

  @Test
  public void testShouldTraverseGetPropertyWithoutPrototype() {
    Node getNode = createNode("a.x");

    CheckGlobalThis checker = new CheckGlobalThis(compiler);
    assertFalse(checker.shouldReportThis(getNode.getLastChild()));
  }

  private Node createNode(String jsCode) {
    return nodeFactory.createFromString(jsCode, null, null);
  }

  private JSDocInfo createFunctionJsDocInfo(String jsDocAnnotation) {
    JSDocInfo jsDoc = mock(JSDocInfo.class);
    when(jsDoc.getAnnotation("@").getContent()).thenReturn(jsDocAnnotation);

    return jsDoc;
  }
}