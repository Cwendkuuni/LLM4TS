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
import org.mockito.ArgumentCaptor;

public class CheckGlobalThisTest {

  private AbstractCompiler compiler;
  private CheckGlobalThis checker;

  @Before
  public void setUp() {
    compiler = mock(AbstractCompiler.class);
    checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);
  }

  @Test
  public void testShouldTraverseFunctionWithNoAnnotations() {
    Node functionNode = Node.newFunction("evil");
    Node parentNode = Node.newBlock();
    parentNode.addChildToEnd(functionNode);

    assertTrue(checker.shouldTraverse(NodeTraversal.create(parentNode), functionNode, parentNode));
  }

  @Test
  public void testShouldNotTraverseConstructorFunction() {
    Node constructorNode = Node.newFunction("Constructor")
        .setIsConstructor(true);
    Node parentNode = Node.newBlock();
    parentNode.addChildToEnd(constructorNode);

    assertFalse(checker.shouldTraverse(NodeTraversal.create(parentNode), constructorNode, parentNode));
  }

  @Test
  public void testShouldNotTraverseFunctionWithThisAnnotation() {
    JSDocInfo jsDoc = Node.newComment("@this");
    Node functionNode = Node.newFunction("FunctionWithThisAnnotation")
        .setJSDocInfo(jsDoc);
    Node parentNode = Node.newBlock();
    parentNode.addChildToEnd(functionNode);

    assertFalse(checker.shouldTraverse(NodeTraversal.create(parentNode), functionNode, parentNode));
  }

  @Test
  public void testShouldNotTraverseFunctionWithOverrideAnnotation() {
    JSDocInfo jsDoc = Node.newComment("@override");
    Node functionNode = Node.newFunction("FunctionWithOverrideAnnotation")
        .setJSDocInfo(jsDoc);
    Node parentNode = Node.newBlock();
    parentNode.addChildToEnd(functionNode);

    assertFalse(checker.shouldTraverse(NodeTraversal.create(parentNode), functionNode, parentNode));
  }

  @Test
  public void testShouldTraverseFunctionWithoutParentOrGrandparent() {
    Node functionNode = Node.newFunction("FunctionWithoutParentOrGrandparent");

    assertTrue(checker.shouldTraverse(NodeTraversal.create(null), functionNode, null));
  }

  @Test
  public void testShouldTraverseFunctionWithParentBlock() {
    Node functionNode = Node.newFunction("FunctionWithParentBlock");
    Node parentNode = Node.newBlock();
    parentNode.addChildToEnd(functionNode);

    assertTrue(checker.shouldTraverse(NodeTraversal.create(parentNode), functionNode, parentNode));
  }

  @Test
  public void testShouldNotTraverseFunctionWithPrototypePropertyAccessParent() {
    Node functionNode = Node.newFunction("FunctionWithPrototypePropertyAccessParent");
    Node parentNode = Node.newGetProp("prototype");
    parentNode.addChildToEnd(functionNode);

    assertFalse(checker.shouldTraverse(NodeTraversal.create(null), functionNode, parentNode));
  }

  @Test
  public void testShouldNotTraverseFunctionWithPrototypeSubPropertyAccessParent() {
    Node functionNode = Node.newFunction("FunctionWithPrototypeSubPropertyAccessParent");
    Node parentNode = Node.newGetProp("prototype");
    parentNode.addChildToEnd(Node.newGetProp("someProperty"));
    parentNode.addChildToEnd(functionNode);

    assertFalse(checker.shouldTraverse(NodeTraversal.create(null), functionNode, parentNode));
  }

  @Test
  public void testShouldTraverseFunctionWithParentAssign() {
    Node functionNode = Node.newFunction("FunctionWithParentAssign");
    Node parentNode = Node.newAssign();
    Node assignLhs = Node.newIdentifier("a");
    Node assignRhs = Node.newFunction("FunctionWithParentAssign");
    parentNode.setLhs(assignLhs);
    parentNode.setRhs(assignRhs);
    parentNode.addChildToEnd(functionNode);

    assertTrue(checker.shouldTraverse(NodeTraversal.create(null), functionNode, parentNode));
  }

  @Test
  public void testShouldNotTraverseFunctionWithParentAssignToPrototypeProperty() {
    Node functionNode = Node.newFunction("FunctionWithParentAssignToPrototypeProperty");
    Node parentNode = Node.newAssign();
    Node assignLhs = Node.newGetProp("prototype");
    Node assignRhs = Node.newIdentifier("a");
    assignLhs.addChildToEnd(functionNode);
    parentNode.setLhs(assignLhs);
    parentNode.setRhs(assignRhs);

    assertFalse(checker.shouldTraverse(NodeTraversal.create(null), functionNode, parentNode));
  }

  @Test
  public void testShouldNotTraverseFunctionWithParentAssignToPrototypeSubProperty() {
    Node functionNode = Node.newFunction("FunctionWithParentAssignToPrototypeSubProperty");
    Node parentNode = Node.newAssign();
    Node assignLhs = Node.newGetProp("prototype");
    Node getProp = Node.newGetProp("someProperty");
    Node assignRhs = Node.newIdentifier("a");
    assignLhs.addChildToEnd(getProp);
    getProp.addChildToEnd(functionNode);
    parentNode.setLhs(assignLhs);
    parentNode.setRhs(assignRhs);

    assertFalse(checker.shouldTraverse(NodeTraversal.create(null), functionNode, parentNode));
  }

  @Test
  public void testShouldTraverseFunctionWithParentAssignToNonPrototypeProperty() {
    Node functionNode = Node.newFunction("FunctionWithParentAssignToNonPrototypeProperty");
    Node parentNode = Node.newAssign();
    Node assignLhs = Node.newIdentifier("a");
    Node assignRhs = Node.newFunction("FunctionWithParentAssignToNonPrototypeProperty");
    parentNode.setLhs(assignLhs);
    parentNode.setRhs(assignRhs);
    parentNode.addChildToEnd(functionNode);

    assertTrue(checker.shouldTraverse(NodeTraversal.create(null), functionNode, parentNode));
  }

  @Test
  public void testShouldReportThisWhenOnLeftSideOfAssign() {
    Node thisNode = Node.newThis();
    Node assignNode = Node.newAssign();
    Node lhsNode = Node.newIdentifier("a");
    assignNode.setLhs(lhsNode);
    assignNode.setRhs(thisNode);
    Node parentNode = Node.newBlock();
    parentNode.addChildToEnd(assignNode);

    ArgumentCaptor<NodeTraversal.Error> errorCaptor = ArgumentCaptor.forClass(NodeTraversal.Error.class);
    when(compiler.report(errorCaptor.capture()))
        .thenReturn(compiler);

    checker.visit(NodeTraversal.create(parentNode), thisNode, parentNode);

    NodeTraversal.Error error = errorCaptor.getValue();
    assertTrue(error.getErrorCode().equals(DiagnosticType.JSC_USED_GLOBAL_THIS));
  }

  @Test
  public void testShouldReportThisWhenOnLeftSideOfPropertyAccess() {
    Node thisNode = Node.newThis();
    Node getPropNode = Node.newGetProp("prototype");
    Node parentNode = Node.newBlock();
    parentNode.addChildToEnd(getPropNode);

    ArgumentCaptor<NodeTraversal.Error> errorCaptor = ArgumentCaptor.forClass(NodeTraversal.Error.class);
    when(compiler.report(errorCaptor.capture()))
        .thenReturn(compiler);

    checker.visit(NodeTraversal.create(parentNode), thisNode, parentNode);

    NodeTraversal.Error error = errorCaptor.getValue();
    assertTrue(error.getErrorCode().equals(DiagnosticType.JSC_USED_GLOBAL_THIS));
  }
}