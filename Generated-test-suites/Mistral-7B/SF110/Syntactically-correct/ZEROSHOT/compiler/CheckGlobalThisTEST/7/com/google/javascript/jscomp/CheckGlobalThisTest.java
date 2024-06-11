import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.JSDocInfo;

public class CheckGlobalThisTest {

  private AbstractCompiler compiler;
  private CheckGlobalThis checker;
  private NodeTraversal traversal;

  @Before
  public void setUp() {
    compiler = mock(AbstractCompiler.class);
    checker = new CheckGlobalThis(compiler);
    traversal = new NodeTraversal(null, null, null, checker);
  }

  @Test
  public void testShouldTraverseFunctions() {
    Node functionNode = Node.newFunction("evil", null);
    Node parentNode = Node.newName("window");

    when(functionNode.getParent()).thenReturn(parentNode);
    when(parentNode.getType()).thenReturn(Token.GLOBAL);

    assertTrue(checker.shouldTraverse(traversal, functionNode, parentNode));
  }

  @Test
  public void testShouldNotTraverseFunctionsWithConstructorAnnotations() {
    Node functionNode = Node.newFunction("ConstructorFunction", null);
    Node parentNode = Node.newName("window");
    JSDocInfo jsDoc = Node.newJSDocInfo(Node.newJSDoc("constructor", null));
    when(functionNode.getJSDocInfo()).thenReturn(jsDoc);

    assertFalse(checker.shouldTraverse(traversal, functionNode, parentNode));
  }

  @Test
  public void testShouldNotTraverseFunctionsWithThisAnnotations() {
    Node functionNode = Node.newFunction("ThisFunction", null);
    Node parentNode = Node.newName("window");
    JSDocInfo jsDoc = Node.newJSDocInfo(Node.newJSDoc("this", null));
    when(functionNode.getJSDocInfo()).thenReturn(jsDoc);

    assertFalse(checker.shouldTraverse(traversal, functionNode, parentNode));
  }

  @Test
  public void testShouldNotTraverseFunctionsWithOverridesAnnotations() {
    Node functionNode = Node.newFunction("OverrideFunction", null);
    Node parentNode = Node.newName("window");
    JSDocInfo jsDoc = Node.newJSDocInfo(Node.newJSDoc("override", null));
    when(functionNode.getJSDocInfo()).thenReturn(jsDoc);

    assertFalse(checker.shouldTraverse(traversal, functionNode, parentNode));
  }

  @Test
  public void testShouldTraverseNonFunctionNodes() {
    Node node = Node.newName("window");

    assertTrue(checker.shouldTraverse(traversal, node, null));
  }

  @Test
  public void testShouldTraverseAssignments() {
    Node assignNode = Node.newAssign(Node.newName("a"), Node.newThis());
    Node parentNode = Node.newName("window");

    assertTrue(checker.shouldTraverse(traversal, assignNode, parentNode));
  }

  @Test
  public void testShouldTraverseGetProperties() {
    Node getNode = Node.newGetProp(Node.newName("window"), "prototype");
    Node parentNode = Node.newName("window");

    assertTrue(checker.shouldTraverse(traversal, getNode, parentNode));
  }

  @Test
  public void testShouldNotTraverseGetPropertiesOfPrototype() {
    Node getNode = Node.newGetProp(Node.newName("Object"), "prototype");
    Node parentNode = Node.newName("window");

    assertFalse(checker.shouldTraverse(traversal, getNode, parentNode));
  }

  @Test
  public void testShouldReportGlobalThis() {
    Node thisNode = Node.newThis();
    Node parentNode = Node.newName("window");

    when(compiler.report(traversal.makeError(thisNode, CheckGlobalThis.GLOBAL_THIS))).thenReturn(true);

    checker.visit(traversal, null, parentNode);
    checker.visit(traversal, thisNode, parentNode);

    assertTrue(compiler.reportWasCalled());
  }
}