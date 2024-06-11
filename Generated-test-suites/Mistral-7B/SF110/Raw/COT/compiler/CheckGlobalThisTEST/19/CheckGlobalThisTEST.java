import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Callback;
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
    checker = new CheckGlobalThis(compiler);
    traversal = mock(NodeTraversal.class);
  }

  @Test
  public void testShouldTraverseFunctionWithNoThisAnnotationsOrParents() {
    Node functionNode = Node.newFunction("testFunction", new Node[0], new Node[0]);
    Node parentNode = Node.newName("testParent", null);

    when(compiler.getTraverser()).thenReturn(traversal);
    when(traversal.getCurrentScope()).thenReturn(null);

    assertFalse("Should not traverse function without any this annotations or parents", checker.shouldTraverse(traversal, functionNode, parentNode));
  }

  @Test
  public void testShouldTraverseFunctionWithConstructorAnnotation() {
    JSDocInfo constructorAnnotation = new JSDocInfo("testFunction", "function", null, true, null, null, null, null, null, null, null);
    Node functionNode = Node.newFunction("testFunction", new Node[0], new Node[0], constructorAnnotation);
    Node parentNode = Node.newName("testParent", null);

    when(compiler.getTraverser()).thenReturn(traversal);

    assertFalse("Should not traverse function annotated as constructor", checker.shouldTraverse(traversal, functionNode, parentNode));
  }

  @Test
  public void testShouldTraverseFunctionWithThisAnnotation() {
    JSDocInfo thisAnnotation = new JSDocInfo("testFunction", "function", null, null, null, null, "@this", null, null, null, null);
    Node functionNode = Node.newFunction("testFunction", new Node[0], new Node[0], null);
    functionNode.setJSDocInfo(thisAnnotation);
    Node parentNode = Node.newName("testParent", null);

    when(compiler.getTraverser()).thenReturn(traversal);

    assertTrue("Should traverse function annotated with @this", checker.shouldTraverse(traversal, functionNode, parentNode));
  }

  @Test
  public void testShouldTraverseFunctionWithNoParent() {
    Node functionNode = Node.newFunction("testFunction", new Node[0], new Node[0]);

    when(compiler.getTraverser()).thenReturn(traversal);
    when(traversal.getCurrentScope()).thenReturn(null);

    assertTrue("Should traverse function with no parent", checker.shouldTraverse(traversal, functionNode, null));
  }

  @Test
  public void testShouldTraverseFunctionWithPrototypeParent() {
    Node functionNode = Node.newFunction("testFunction", new Node[0], new Node[0]);
    Node parentNode = Node.newName("testParent", null);
    Node prototypeNode = Node.newObjectLiteral("testPrototype", new Node[0]);
    prototypeNode.setProperty("prototype", Node.newName("testPrototypePrototype", null));
    parentNode.setProperty("prototype", prototypeNode);

    when(compiler.getTraverser()).thenReturn(traversal);

    assertTrue("Should traverse function with prototype parent", checker.shouldTraverse(traversal, functionNode, parentNode));
  }

  @Test
  public void testShouldTraverseFunctionWithPrototypePropertyParent() {
    Node functionNode = Node.newFunction("testFunction", new Node[0], new Node[0]);
    Node parentNode = Node.newName("testParentPrototype", null);
    Node prototypeNode = Node.newObjectLiteral("testPrototype", new Node[0]);
    prototypeNode.setProperty("prototype", Node.newName("testPrototypePrototype", null));
    parentNode.setProperty("prototype", prototypeNode);
    Node otherParentNode = Node.newName("testOtherParent", null);

    when(compiler.getTraverser()).thenReturn(traversal);

    assertTrue("Should traverse function with prototype property parent", checker.shouldTraverse(traversal, functionNode, parentNode));
    assertFalse("Should not traverse function with non-prototype parent", checker.shouldTraverse(traversal, functionNode, otherParentNode));
  }

  @Test
  public void testShouldTraverseFunctionGettingLentToPrototype() {
    Node functionNode = Node.newFunction("testFunction", new Node[0], new Node[0]);
    Node parentNode = Node.newName("testParentPrototype", null);
    Node prototypeNode = Node.newObjectLiteral("testPrototype", new Node[0]);
    prototypeNode.setProperty("prototype", Node.newName("testPrototypePrototype", null));
    parentNode.setProperty("prototype", prototypeNode);
    Node lendsNode = Node.newName("testLendsName", null);

    when(compiler.getTraverser()).thenReturn(traversal);

    assertTrue("Should traverse function getting lent to prototype", checker.shouldTraverse(traversal, functionNode, parentNode));
    assertTrue("Should traverse function getting lent to prototype with lends name", checker.shouldTraverse(traversal, functionNode, lendsNode));
  }

  @Test
  public void testShouldTraverseFunctionWithAssignLeftHandSideChild() {
    Node assignNode = Node.newAssign(Node.newName("testAssignLeftHandSide", null), Node.newThis());
    Node functionNode = Node.newFunction("testFunction", new Node[0], new Node[0]);

    when(compiler.getTraverser()).thenReturn(traversal);

    when(traversal.getCurrentScope()).thenReturn(null);

    assertTrue("Should traverse function with assign left hand side child", checker.shouldTraverse(traversal, assignNode, functionNode));
  }

  @Test
  public void testShouldReportThisOnLeftSideOfAssignment() {
    Node assignNode = Node.newAssign(Node.newName("testAssignLeftSide", null), Node.newThis());
    Node parentNode = Node.newName("testParent", null);

    when(compiler.getTraverser()).thenReturn(traversal);

    checker.visit(traversal, assignNode, parentNode);

    assertNotNull("Should report error for THIS on left side of assignment", checker.compiler.getErrorList().getError(0).getLocation().getSourceCode());
  }

  @Test
  public void testShouldNotReportThisOnRightSideOfAssignment() {
    Node assignNode = Node.newAssign(Node.newName("testAssignRightSide", null), Node.newThis());
    Node parentNode = Node.newName("testParent", null);

    when(compiler.getTraverser()).thenReturn(traversal);

    checker.visit(traversal, assignNode, parentNode);

    assertNull("Should not report error for THIS on right side of assignment", checker.compiler.getErrorList().getError(0));
  }

  @Test
  public void testShouldNotReportThisOnPropertyAccess() {
    Node getNode = Node.newGetProp(Node.newName("testGetNode", null), "prototype");
    Node parentNode = Node.newName("testParent", null);

    when(compiler.getTraverser()).thenReturn(traversal);

    checker.visit(traversal, getNode, parentNode);

    assertNull("Should not report error for THIS on property access", checker.compiler.getErrorList().getError(0));
  }

  @Test
  public void testShouldNotReportThisOnNestedPropertyAccess() {
    Node getNode = Node.newGetProp(Node.newGetProp(Node.newName("testGetNode", null), "prototype"), "prototype");
    Node parentNode = Node.newName("testParent", null);

    when(compiler.getTraverser()).thenReturn(traversal);

    checker.visit(traversal, getNode, parentNode);

    assertNull("Should not report error for THIS on nested property access", checker.compiler.getErrorList().getError(0));
  }
}