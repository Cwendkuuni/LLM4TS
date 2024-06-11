import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.JSDocInfo;

import org.junit.Before;
import org.junit.Test;

public class CheckGlobalThisTest {

  private AbstractCompiler compiler;
  private CheckGlobalThis checker;
  private NodeTraversal traversal;

  @Before
  public void setUp() {
    compiler = mock(AbstractCompiler.class);
    checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    traversal = mock(NodeTraversal.class);
  }

  @Test
  public void testShouldTraverseFunction() {
    Node functionNode = Node.newFunctionNode("testFunction");
    Node parentNode = Node.newBlockNode();
    when(compiler.getTraversal()).thenReturn(traversal);

    assertTrue(checker.shouldTraverse(traversal, functionNode, parentNode));
  }

  @Test
  public void testShouldNotTraverseFunctionWithConstructorAnnotations() {
    Node functionNode = Node.newFunctionNode("testConstructor");
    JSDocInfo jsDoc = Node.newJsDocNode("@constructor");
    functionNode.setFirstChild(jsDoc);
    Node parentNode = Node.newBlockNode();

    when(compiler.getTraversal()).thenReturn(traversal);

    assertFalse(checker.shouldTraverse(traversal, functionNode, parentNode));
  }

  @Test
  public void testShouldNotTraverseFunctionWithThisAnnotation() {
    Node functionNode = Node.newFunctionNode("testFunction");
    JSDocInfo jsDoc = Node.newJsDocNode("@this myType");
    functionNode.setFirstChild(jsDoc);
    Node parentNode = Node.newBlockNode();

    when(compiler.getTraversal()).thenReturn(traversal);

    assertFalse(checker.shouldTraverse(traversal, functionNode, parentNode));
  }

  @Test
  public void testShouldNotTraverseFunctionAnnotatedWithOverride() {
    Node functionNode = Node.newFunctionNode("testFunction");
    JSDocInfo jsDoc = Node.newJsDocNode("@override");
    functionNode.setFirstChild(jsDoc);
    Node parentNode = Node.newBlockNode();

    when(compiler.getTraversal()).thenReturn(traversal);

    assertFalse(checker.shouldTraverse(traversal, functionNode, parentNode));
  }

  @Test
  public void testShouldNotTraverseFunctionInPrototypeChain() {
    Node functionNode = Node.newFunctionNode("prototypeFunction");
    Node prototypeNode = Node.newGetPropNode("prototype", Node.newIdentifierNode("Object"));
    prototypeNode.setLastChild(functionNode);
    Node parentNode = Node.newBlockNode();

    when(compiler.getTraversal()).thenReturn(traversal);

    assertFalse(checker.shouldTraverse(traversal, functionNode, parentNode));
  }

  @Test
  public void testShouldNotTraverseFunctionInPrototypeChainWithDeepNestedProperty() {
    Node functionNode = Node.newFunctionNode("prototypeFunction");
    Node prototypeNode = Node.newGetPropNode("prototype", Node.newGetPropNode("prototype", Node.newIdentifierNode("Object")));
    Node parentNode = Node.newBlockNode();

    when(compiler.getTraversal()).thenReturn(traversal);

    assertFalse(checker.shouldTraverse(traversal, functionNode, parentNode));
  }

  @Test
  public void testShouldTraverseAssignmentToThis() {
    Node assignmentNode = Node.newAssignNode(Node.newThisNode(), Node.newIdentifierNode("myVariable"));
    Node parentNode = Node.newBlockNode();

    when(compiler.getTraversal()).thenReturn(traversal);

    assertTrue(checker.shouldTraverse(traversal, assignmentNode, parentNode));
  }

  @Test
  public void testShouldNotTraverseAssignmentToPrototypeProperty() {
    Node assignmentNode = Node.newAssignNode(Node.newIdentifierNode("prototype"), Node.newThisNode());
    Node parentNode = Node.newBlockNode();

    when(compiler.getTraversal()).thenReturn(traversal);

    assertFalse(checker.shouldTraverse(traversal, assignmentNode, parentNode));
  }

  @Test
  public void testShouldNotTraverseAssignmentToPrototypePropertyWithDeepNestedProperty() {
    Node assignmentNode = Node.newAssignNode(Node.newGetPropNode("prototype", Node.newIdentifierNode("prototype")), Node.newThisNode());
    Node parentNode = Node.newBlockNode();

    when(compiler.getTraversal()).thenReturn(traversal);

    assertFalse(checker.shouldTraverse(traversal, assignmentNode, parentNode));
  }
}