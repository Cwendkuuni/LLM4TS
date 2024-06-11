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

public class CheckGlobalThisTest {

  @org.junit.Before
  public void setup() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    NodeTraversal traversal = mock(NodeTraversal.class);

    when(compiler.report(TraversalTestUtils.nodeTraversalError(traversal, (Node) null, (Node) null, DiagnosticType.warning("JSC_USED_GLOBAL_THIS", "")))).thenReturn(true);

    this.check = checkGlobalThis;
    this.traversal = traversal;
  }

  CheckGlobalThis check;
  NodeTraversal traversal;

  @org.junit.Test
  public void shouldTraverseGlobalFunction() {
    Node functionNode = Node.newFunction("functionName");
    Node parent = Node.newBlock();
    Node root = Node.newScript();

    when(traversal.getCurrentNode()).thenReturn(root);
    when(traversal.getParent()).thenReturn(parent);

    assertTrue(check.shouldTraverse(traversal, functionNode, parent));
  }

  @org.junit.Test
  public void shouldNotTraverseNonGlobalFunction() {
    Node functionNode = Node.newFunction("functionName");
    Node parent = Node.newBlock();
    Node grandParent = Node.newBlock();

    when(traversal.getCurrentNode()).thenReturn(grandParent);
    when(traversal.getParent()).thenReturn(parent);

    assertFalse(check.shouldTraverse(traversal, functionNode, parent));
  }

  @org.junit.Test
  public void shouldNotTraverseConstructorFunction() {
    Node functionNode = Node.newFunction("functionName", new String[]{"constructor"});
    Node parent = Node.newBlock();

    when(traversal.getCurrentNode()).thenReturn(parent);
    when(traversal.getParent()).thenReturn(null);

    assertFalse(check.shouldTraverse(traversal, functionNode, parent));
  }

  @org.junit.Test
  public void shouldNotTraverseFunctionWithThisAnnotation() {
    Node functionNode = Node.newFunction("functionName", null, new String[]{"@this"});
    Node parent = Node.newBlock();

    when(traversal.getCurrentNode()).thenReturn(parent);
    when(traversal.getParent()).thenReturn(null);

    assertFalse(check.shouldTraverse(traversal, functionNode, parent));
  }

  @org.junit.Test
  public void shouldNotTraverseFunctionWithNoParent() {
    Node functionNode = Node.newFunction("functionName");

    when(traversal.getCurrentNode()).thenReturn(null);
    when(traversal.getParent()).thenReturn(null);

    assertFalse(check.shouldTraverse(traversal, functionNode, null));
  }

  @org.junit.Test
  public void shouldTraverseAssignmentToThis() {
    Node assignNode = Node.newAssign(Node.newThis(), Node.newNumber(42));
    Node parent = Node.newBlock();

    when(traversal.getCurrentNode()).thenReturn(parent);
    when(traversal.getParent()).thenReturn(null);

    assertTrue(check.shouldTraverse(traversal, assignNode, parent));
  }

  @org.junit.Test
  public void shouldNotTraverseAssignmentToPrototypeProperty() {
    Node assignNode = Node.newAssign(Node.newGet(Node.newThis(), "prototype"), Node.newString("property"));
    Node parent = Node.newBlock();

    when(traversal.getCurrentNode()).thenReturn(parent);
    when(traversal.getParent()).thenReturn(null);

    assertFalse(check.shouldTraverse(traversal, assignNode, parent));
  }

  @org.junit.Test
  public void shouldNotTraverseAssignmentToPrototypeSubProperty() {
    Node assignNode = Node.newAssign(Node.newGet(Node.newGet(Node.newThis(), "prototype"), "subProperty"), Node.newString("value"));
    Node parent = Node.newBlock();

    when(traversal.getCurrentNode()).thenReturn(parent);
    when(traversal.getParent()).thenReturn(null);

    assertFalse(check.shouldTraverse(traversal, assignNode, parent));
  }

  @org.junit.Test
  public void shouldTraverseFunctionWithNoThisAnnotationAndNoParent() {
    Node functionNode = Node.newFunction("functionName");

    when(traversal.getCurrentNode()).thenReturn(null);
    when(traversal.getParent()).thenReturn(null);

    assertTrue(check.shouldTraverse(traversal, functionNode, null));
  }

  @org.junit.Test
  public void shouldReportThisOnLeftSideOfAssignment() {
    Node assignNode = Node.newAssign(Node.newThis(), Node.newString("property"));
    Node parent = Node.newBlock();

    when(traversal.getCurrentNode()).thenReturn(parent);
    when(traversal.getParent()).thenReturn(null);

    assertTrue(check.shouldReportThis(assignNode, parent));
  }

  @org.junit.Test
  public void shouldNotReportThisOnRightSideOfAssignment() {
    Node assignNode = Node.newAssign(Node.newThis(), Node.newNumber(42));
    Node parent = Node.newBlock();

    when(traversal.getCurrentNode()).thenReturn(parent);
    when(traversal.getParent()).thenReturn(null);

    assertFalse(check.shouldReportThis(assignNode, parent));
  }

  @org.junit.Test
  public void shouldReportThisOnLeftSideOfPropertyAccess() {
    Node getNode = Node.newGet(Node.newThis(), "property");
    Node parent = Node.newBlock();

    when(traversal.getCurrentNode()).thenReturn(parent);
    when(traversal.getParent()).thenReturn(null);

    assertTrue(check.shouldReportThis(getNode, parent));
  }

  @org.junit.Test
  public void shouldNotReportThisOnRightSideOfPropertyAccess() {
    Node getNode = Node.newGet(Node.newThis(), "property");
    Node assignNode = Node.newAssign(getNode, Node.newNumber(42));
    Node parent = Node.newBlock();

    when(traversal.getCurrentNode()).thenReturn(assignNode);
    when(traversal.getParent()).thenReturn(parentNode);

    assertFalse(check.shouldReportThis(getNode, parent));
  }
}