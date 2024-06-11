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
import com.google.javascript.rhino.Token;

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
    traversal = NodeTraversal.createTraversal(checker, null);
  }

  @Test
  public void testShouldTraverseFunctionWithNoAnnotationsOrParents() {
    Node functionNode = Node.newFunction("evil");
    Node scriptNode = Node.newText("(function() { evil(); })();");

    assertTrue(checker.shouldTraverse(traversal, functionNode, scriptNode));
  }

  @Test
  public void testShouldNotTraverseFunctionWithConstructorAnnotation() {
    Node functionNode = Node.newFunction("constructorEvil");
    JSDocInfo jsDocInfo = Node.newJSDocInfo("constructor");
    when(functionNode.getJSDocInfo()).thenReturn(jsDocInfo);

    assertFalse(checker.shouldTraverse(traversal, functionNode, Node.SCOPE));
  }

  @Test
  public void testShouldNotTraverseFunctionWithThisAnnotation() {
    Node functionNode = Node.newFunction("thisEvil");
    JSDocInfo jsDocInfo = Node.newJSDocInfo("this");
    when(functionNode.getJSDocInfo()).thenReturn(jsDocInfo);

    assertFalse(checker.shouldTraverse(traversal, functionNode, Node.SCOPE));
  }

  @Test
  public void testShouldNotTraverseFunctionWithOverrideAnnotation() {
    Node functionNode = Node.newFunction("overrideEvil");
    JSDocInfo jsDocInfo = Node.newJSDocInfo("override");
    when(functionNode.getJSDocInfo()).thenReturn(jsDocInfo);

    assertFalse(checker.shouldTraverse(traversal, functionNode, Node.SCOPE));
  }

  @Test
  public void testShouldNotTraverseFunctionWithNoParent() {
    Node functionNode = Node.newFunction("evil");

    assertFalse(checker.shouldTraverse(traversal, functionNode, null));
  }

  @Test
  public void testShouldTraverseFunctionWithParentBlock() {
    Node functionNode = Node.newFunction("evil");
    Node scriptNode = Node.newBlock("{ evil(); }");

    assertTrue(checker.shouldTraverse(traversal, functionNode, scriptNode));
  }

  @Test
  public void testShouldTraverseFunctionWithParentName() {
    Node functionNode = Node.newFunction("evil");
    Node nameNode = Node.newName("a");

    assertTrue(checker.shouldTraverse(traversal, functionNode, nameNode));
  }

  @Test
  public void testShouldTraverseFunctionWithParentAssign() {
    Node functionNode = Node.newFunction("evil");
    Node assignNode = Node.newAssign(Node.newName("a"), Node.newNumber(42));

    assertTrue(checker.shouldTraverse(traversal, functionNode, assignNode));
  }

  @Test
  public void testShouldNotTraverseFunctionWithPrototypePropertyAccess() {
    Node functionNode = Node.newFunction("evil");
    Node prototypeNode = Node.newGetProp(Node.newName("Object"), "prototype");

    assertFalse(checker.shouldTraverse(traversal, functionNode, prototypeNode));
  }

  @Test
  public void testShouldNotTraverseFunctionWithPrototypeSubPropertyAccess() {
    Node functionNode = Node.newFunction("evil");
    Node prototypeNode = Node.newGetProp(Node.newName("Object"), "prototype");
    Node subPropertyNode = Node.newGetProp(prototypeNode, "constructor");

    assertFalse(checker.shouldTraverse(traversal, functionNode, subPropertyNode));
  }

  @Test
  public void testShouldTraverseFunctionWithParentObjectLiteralKey() {
    Node functionNode = Node.newFunction("evil");
    Node objectNode = Node.newObjectLiteral();
    Node keyNode = Node.newString("a");

    assertTrue(checker.shouldTraverse(traversal, functionNode, objectNode));
    assertTrue(checker.shouldTraverse(traversal, functionNode, keyNode));
  }

  @Test
  public void testShouldNotTraverseFunctionWithPrototypeLending() {
    Node functionNode = Node.newFunction("evil");
    Node objectNode = Node.newObjectLiteral();
    Node prototypeNode = Node.newGetProp(objectNode, "prototype");
    Node lendsNode = Node.newGetProp(prototypeNode, "prototype");

    assertFalse(checker.shouldTraverse(traversal, functionNode, lendsNode));
  }

  @Test
  public void testShouldReportThisOnLeftSideOfAssignment() {
    Node functionNode = Node.newFunction("evil");
    Node assignNode = Node.newAssign(Node.newName("a"), Node.newThis());

    Node rootNode = Node.newAssign(assignNode, functionNode);

    checker.visit(traversal, functionNode, rootNode);
    assertTrue(compiler.reportedErrors.size() > 0);
  }

  @Test
  public void testShouldNotReportThisOnRightSideOfAssignment() {
    Node functionNode = Node.newFunction("evil");
    Node assignNode = Node.newAssign(Node.newName("a"), Node.newThis());

    Node rootNode = Node.newAssign(Node.newName("a"), assignNode);

    checker.visit(traversal, functionNode, rootNode);
    assertTrue(compiler.reportedErrors.size() == 0);
  }

  @Test
  public void testShouldReportThisOnPropertyAccess() {
    Node functionNode = Node.newFunction("evil");
    Node getNode = Node.newGetProp(Node.newName("a"), Node.newThis());

    Node rootNode = Node.newAssign(getNode, functionNode);

    checker.visit(traversal, functionNode, rootNode);
    assertTrue(compiler.reportedErrors.size() > 0);
  }

  @Test
  public void testShouldNotReportThisOnLeftSideOfAssignmentToPrototypeProperty() {
    Node functionNode = Node.newFunction("evil");
    Node assignNode = Node.newAssign(Node.newGetProp(Node.newName("Object"), "prototype"), Node.newThis());

    Node rootNode = Node.newAssign(assignNode, functionNode);

    checker.visit(traversal, functionNode, rootNode);
    assertTrue(compiler.reportedErrors.size() == 0);
  }

  @Test
  public void testShouldNotReportThisOnLeftSideOfAssignmentToPrototypeSubProperty() {
    Node functionNode = Node.newFunction("evil");
    Node prototypeNode = Node.newGetProp(Node.newName("Object"), "prototype");
    Node subPropertyNode = Node.newGetProp(prototypeNode, "constructor");
    Node assignNode = Node.newAssign(subPropertyNode, Node.newThis());

    Node rootNode = Node.newAssign(assignNode, functionNode);

    checker.visit(traversal, functionNode, rootNode);
    assertTrue(compiler.reportedErrors.size() == 0);
  }

  @Test
  public void testShouldNotReportThisOnPropertyAccessToPrototype() {
    Node functionNode = Node.newFunction("evil");
    Node prototypeNode = Node.newGetProp(Node.newName("Object"), "prototype");
    Node getNode = Node.newGetProp(getNode, Node.newThis());

    Node rootNode = Node.newAssign(getNode, functionNode);

    checker.visit(traversal, functionNode, rootNode);
    assertTrue(compiler.reportedErrors.size() == 0);
  }

  @Test
  public void testShouldReportThisOnLeftSideOfAssignmentToNonPrototypeProperty() {
    Node functionNode = Node.newFunction("evil");
    Node assignNode = Node.newAssign(Node.newName("a"), Node.newThis());

    Node rootNode = Node.newAssign(assignNode, functionNode);

    when(compiler.report(traversal.makeError(Node.SCOPE, 0, 0, GLOBAL_THIS))).thenReturn(null);

    checker.visit(traversal, functionNode, rootNode);
    assertTrue(compiler.reportedErrors.size() > 0);
  }

  @Test
  public void testShouldNotReportThisOnRightSideOfAssignmentToNonPrototypeProperty() {
    Node functionNode = Node.newFunction("evil");
    Node assignNode = Node.newAssign(Node.newName("a"), Node.newThis());

    Node rootNode = Node.newAssign(Node.newName("a"), assignNode);

    when(compiler.report(traversal.makeError(Node.SCOPE, 0, 0, GLOBAL_THIS))).thenReturn(null);

    checker.visit(traversal, functionNode, rootNode);
    assertTrue(compiler.reportedErrors.size() == 0);
  }

  @Test
  public void testShouldReportThisOnPropertyAccessToNonPrototypeProperty() {
    Node functionNode = Node.newFunction("evil");
    Node getNode = Node.newGetProp(Node.newName("a"), Node.newThis());

    Node rootNode = Node.newAssign(getNode, functionNode);

    when(compiler.report(traversal.makeError(Node.SCOPE, 0, 0, GLOBAL_THIS))).thenReturn(null);

    checker.visit(traversal, functionNode, rootNode);
    assertTrue(compiler.reportedErrors.size() > 0);
  }

  private static final DiagnosticType GLOBAL_THIS = CheckGlobalThis.GLOBAL_THIS;
}